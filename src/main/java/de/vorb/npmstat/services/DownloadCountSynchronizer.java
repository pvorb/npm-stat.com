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
import de.vorb.npmstat.persistence.repositories.DownloadCountRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Iterators;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DownloadCountSynchronizer {

    private final Clock clock;
    private final URL allDocsUrl;
    private final ObjectMapper objectMapper;
    private final DownloadsClient downloadsClient;
    private final DownloadCountRepository downloadCountRepository;
    private final Path tempFile;

    public DownloadCountSynchronizer(Clock clock,
            @Value("${npm.registry.baseUrl}/_all_docs") URL allDocsUrl,
            ObjectMapper objectMapper,
            DownloadsClient downloadsClient,
            DownloadCountRepository downloadCountRepository) throws IOException {
        this.clock = clock;
        this.allDocsUrl = allDocsUrl;
        this.objectMapper = objectMapper;
        this.downloadsClient = downloadsClient;
        this.downloadCountRepository = downloadCountRepository;

        final Path cwd = Paths.get("");
        tempFile = Paths.get("npm-stat-625291444027377353.dat"); // Files.createTempFile(cwd, "npm-stat-", ".dat");
    }

    //@Scheduled(initialDelay = 100, fixedDelay = 24 * 60 * 60 * 1000)
    public void sync() throws Exception {
        try {
            // writePackageNamesToTemporaryFile();

            final LocalDate lastWeekStart = LocalDate.now(clock).minusDays(7);
            final LocalDate lastWeekEnd = LocalDate.now(clock).minusDays(1);
            Iterators.partition(Files.lines(tempFile, StandardCharsets.UTF_8)
                    .filter(packageName -> !packageName.startsWith("@")).iterator(), 128)
                    .forEachRemaining((List<String> batch) -> {
                        final String[] packageNames = batch.toArray(new String[0]);
                        final Map<String, DownloadsJson> downloads = downloadsClient.getMultiPackageDownloadsForTimeRange(
                                packageNames, lastWeekStart, lastWeekEnd);
                        final List<DownloadCount> downloadCounts = downloads.values().stream()
                                .filter(Objects::nonNull)
                                .filter(json -> json.getDownloads() != null)
                                .flatMap(json -> json.getDownloads().stream()
                                        .map(downloadsPerDay ->
                                                new DownloadCount(json.getPackageName(), downloadsPerDay.getDay(),
                                                        downloadsPerDay.getDownloads())))
                                .collect(Collectors.toList());
                        downloadCountRepository.storeAll(downloadCounts);
                        log.debug("Fetched packages: {}", String.join(", ", packageNames));
                    });
        } finally {
            // Files.deleteIfExists(tempFile);
        }
    }

    private void writePackageNamesToTemporaryFile() throws Exception {

        log.info("Writing packages to {}", tempFile.toAbsolutePath());

        long packageCount = 0;
        try (final PackageNameIterator packageNames = new PackageNameIterator(allDocsUrl, objectMapper);
             final BufferedWriter writer = Files.newBufferedWriter(tempFile, StandardCharsets.UTF_8,
                     StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {

            while (packageNames.hasNext()) {
                writer.write(packageNames.next());
                writer.newLine();
                packageCount++;
            }
        }

        log.info("Wrote {} packages to {}", packageCount, tempFile.toAbsolutePath());
    }

}
