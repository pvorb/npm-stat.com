/*
 * Copyright 2012-2019 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package de.vorb.npmstat.services;

import de.vorb.npmstat.clients.downloads.DownloadsClient;
import de.vorb.npmstat.clients.downloads.DownloadsJson;
import de.vorb.npmstat.jobs.PackageNameIterator;
import de.vorb.npmstat.persistence.jooq.tables.pojos.DownloadCount;
import de.vorb.npmstat.persistence.jooq.tables.records.PackageRecord;
import de.vorb.npmstat.persistence.repositories.DownloadCountRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Iterators;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.InsertValuesStep2;
import org.jooq.Record1;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static de.vorb.npmstat.persistence.jooq.Tables.PACKAGE;

@Slf4j
@Service
public class DownloadCountSynchronizer {

    private final Clock clock;
    private final URL allDocsUrl;
    private final ObjectMapper objectMapper;
    private final DSLContext jooq;
    private final DownloadsClient downloadsClient;
    private final DownloadCountRepository downloadCountRepository;

    public DownloadCountSynchronizer(Clock clock,
            @Value("${npm.registry.baseUrl}/_all_docs") URL allDocsUrl,
            ObjectMapper objectMapper,
            DSLContext jooq, DownloadsClient downloadsClient,
            DownloadCountRepository downloadCountRepository) {
        this.clock = clock;
        this.allDocsUrl = allDocsUrl;
        this.objectMapper = objectMapper;
        this.jooq = jooq;
        this.downloadsClient = downloadsClient;
        this.downloadCountRepository = downloadCountRepository;
    }

    public void synchronize(LocalDate start, LocalDate end) throws Exception {
        // writePackageNamesToDatabase();

        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "8");

        try (final Stream<String> relevantPackageNames = selectRelevantPackageNames()) {
            StreamSupport.stream(
                    Spliterators.spliteratorUnknownSize(Iterators.partition(relevantPackageNames.iterator(), 128),
                            Spliterator.ORDERED | Spliterator.NONNULL), true)
                    .forEach((List<String> batch) -> {
                        final String[] packageNames = batch.toArray(new String[0]);
                        final Map<String, DownloadsJson> downloads = downloadsClient.getMultiPackageDownloadsForTimeRange(
                                packageNames, start, end);
                        final List<DownloadCount> downloadCounts = downloads.values().stream()
                                .filter(Objects::nonNull)
                                .filter(json -> json.getDownloads() != null)
                                .flatMap(json -> json.getDownloads().stream()
                                        .map(downloadsPerDay ->
                                                new DownloadCount(json.getPackageName(), downloadsPerDay.getDay(),
                                                        downloadsPerDay.getDownloads())))
                                .collect(Collectors.toList());
                        downloadCountRepository.storeAll(downloadCounts);
                        markImportedPackages(packageNames);
                        log.info("Fetched packages: {}", String.join(", ", packageNames));
                    });
        }
    }

    private void markImportedPackages(String... packageNames) {
        jooq.update(PACKAGE)
                .set(PACKAGE.IMPORTED, true)
                .where(PACKAGE.NAME.in(packageNames))
                .execute();
    }

    private Stream<String> selectRelevantPackageNames() {
        return jooq.select(PACKAGE.NAME)
                .from(PACKAGE)
                .where(PACKAGE.IMPORTED.isFalse())
                .and(PACKAGE.NAME.notLike("@%"))
                .fetchLazy()
                .stream()
                .map(Record1::value1);
    }

    private void writePackageNamesToDatabase() throws Exception {

        log.info("Writing packages to database");

        final AtomicLong packageCount = new AtomicLong(0);

        jooq.transaction(configuration -> {
            final DSLContext jooq = DSL.using(configuration);

            jooq.truncate(PACKAGE).execute();

            try (final PackageNameIterator packageNames = new PackageNameIterator(allDocsUrl, objectMapper)) {
                final Iterator<List<String>> partitionedPackageNames = Iterators.partition(packageNames, 50);
                while (partitionedPackageNames.hasNext()) {

                    InsertValuesStep2<PackageRecord, String, Boolean> insert =
                            jooq.insertInto(PACKAGE)
                                    .columns(PACKAGE.NAME, PACKAGE.IMPORTED);

                    for (final String packageName : partitionedPackageNames.next()) {
                        insert = insert.values(packageName, false);
                        final long count = packageCount.incrementAndGet();
                        if (count % 10000L == 0L) {
                            log.info("... Inserted {} packages ...", count);
                        }
                    }

                    insert.execute();
                }
            }
        });

        log.info("Inserted {} packages", packageCount.get());
    }

}
