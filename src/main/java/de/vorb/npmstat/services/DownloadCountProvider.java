/*
 * Copyright 2012-2018 the original author or authors.
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
import de.vorb.npmstat.persistence.jooq.tables.pojos.DownloadCount;
import de.vorb.npmstat.persistence.repositories.DownloadCountRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.exception.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DownloadCountProvider {

    private static final long APPROX_DAYS_IN_MONTH = 30;
    private static final long MAX_NUM_DAYS = 18 * APPROX_DAYS_IN_MONTH - 1;

    private final DownloadsClient downloadsClient;
    private final DownloadCountRepository downloadCountRepository;
    private final GapFinder gapFinder;
    private final Clock clock;

    public Map<String, Map<LocalDate, Integer>> getDownloadCounts(
            Set<String> packageNames,
            LocalDate from,
            LocalDate until) {

        final Map<String, Map<LocalDate, Integer>> downloadCounts =
                new HashMap<>(downloadCountRepository.findDownloadCounts(packageNames, from, until));

        initDownloadCountsForMissingPackages(packageNames, downloadCounts);

        final Map<String, List<Gap>> gapsPerPackage = findGapsPerPackage(from, until, downloadCounts);

        gapsPerPackage.entrySet().parallelStream().forEach(entry -> {
            final String packageName = entry.getKey();
            final List<Gap> gaps = entry.getValue();
            fillGapsForPackage(packageName, downloadCounts.get(packageName), gaps);
        });

        return downloadCounts;
    }

    private void initDownloadCountsForMissingPackages(Set<String> packageNames,
            Map<String, Map<LocalDate, Integer>> downloadCounts) {
        packageNames.stream()
                .filter(packageName -> !downloadCounts.containsKey(packageName))
                .forEach(packageName -> downloadCounts.put(packageName, new TreeMap<>()));
    }

    private Map<String, List<Gap>> findGapsPerPackage(LocalDate from, LocalDate until,
            Map<String, Map<LocalDate, Integer>> downloadCounts) {

        final Function<String, List<Gap>> findGapsForPackageName =
                packageName -> gapFinder.findGaps(from, until, downloadCounts.get(packageName));

        return downloadCounts.keySet().stream()
                .collect(Collectors.toMap(Function.identity(), findGapsForPackageName));
    }

    private void fillGapsForPackage(String packageName, Map<LocalDate, Integer> downloadCounts, List<Gap> gaps) {
        if (gaps.isEmpty()) {
            return;
        }

        final LocalDate today = LocalDate.now(clock);
        final LocalDate lastDayWhereZeroValuesAreStored = today.minusDays(7);
        final LocalDate lastDayWhereNonZeroValuesAreStored = today.minusDays(1);

        final LocalDate from = gaps.get(0).getFrom();
        final LocalDate until = gaps.get(gaps.size() - 1).getTo();

        LocalDate nextUntil = from;
        do {
            nextUntil = minDate(nextUntil.plusDays(MAX_NUM_DAYS), until);

            final DownloadsJson downloadsFromApi =
                    downloadsClient.getPackageDownloadsForTimeRange(packageName, from, nextUntil);

            final List<DownloadCount> downloadCountsToStore = downloadsFromApi.getDownloads().stream()
                    .filter(elem -> !downloadCounts.containsKey(elem.getDay()))
                    .peek(elem -> downloadCounts.put(elem.getDay(), elem.getDownloads()))
                    .map(elem -> new DownloadCount(packageName, elem.getDay(), elem.getDownloads()))
                    .collect(Collectors.toList());

            for (DownloadCount downloadCount : downloadCountsToStore) {
                try {
                    if (!downloadCount.getDate().isAfter(lastDayWhereZeroValuesAreStored)
                            || (!downloadCount.getDate().isAfter(lastDayWhereNonZeroValuesAreStored)
                            && downloadCount.getCount() != 0)) {
                        downloadCountRepository.store(downloadCount);
                    }
                } catch (DataAccessException e) {
                    log.debug("Could not store {}", downloadCount);
                }
            }

        } while (nextUntil.isBefore(until));

    }

    private LocalDate minDate(LocalDate a, LocalDate b) {
        return a.isBefore(b) ? a : b;
    }

}
