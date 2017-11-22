package de.vorb.npmstat.services;

import de.vorb.npmstat.clients.downloads.DownloadsClient;
import de.vorb.npmstat.clients.downloads.DownloadsJson;
import de.vorb.npmstat.persistence.jooq.tables.records.DownloadCountRecord;
import de.vorb.npmstat.persistence.repositories.DownloadCountRepository;
import de.vorb.npmstat.persistence.repositories.PackageRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DownloadCountProvider {

    private static final long APPROX_DAYS_IN_MONTH = 30;
    private static final long MAX_NUM_DAYS = 18 * APPROX_DAYS_IN_MONTH - 1;

    private final DownloadsClient downloadsClient;
    private final DownloadCountRepository downloadCountRepository;
    private final PackageRepository packageRepository;
    private final GapFinder gapFinder;

    @Transactional
    public Map<String, Map<LocalDate, Integer>> getDownloadCounts(
            Set<String> packageNames,
            LocalDate from,
            LocalDate until) {

        final Map<String, Map<LocalDate, Integer>> downloadCounts =
                new HashMap<>(downloadCountRepository.findDownloadCounts(packageNames, from, until));

        initDownloadCountsForMissingPackages(packageNames, downloadCounts);

        final Map<String, List<Gap>> gapsPerPackage = findGapsPerPackage(from, until, downloadCounts);

        gapsPerPackage.forEach((packageName, gaps) ->
                fillGapsForPackage(packageName, downloadCounts.get(packageName), gaps));

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

        final LocalDate from = gaps.get(0).getFrom();
        final LocalDate until = gaps.get(gaps.size() - 1).getTo();

        final List<DownloadCountRecord> recordsToInsert = new ArrayList<>();

        LocalDate nextUntil = from;
        do {
            nextUntil = minDate(nextUntil.plusDays(MAX_NUM_DAYS), until);

            final DownloadsJson downloadsFromApi =
                    downloadsClient.getPackageDownloadsForTimeRange(packageName, from, nextUntil);

            final Long packageId = packageRepository.getIdForPackageName(packageName);

            recordsToInsert.addAll(
                    downloadsFromApi.getDownloads().stream()
                            .filter(elem -> !downloadCounts.containsKey(elem.getDay()))
                            .peek(elem -> downloadCounts.put(elem.getDay(), elem.getDownloads()))
                            .map(elem -> new DownloadCountRecord(packageId, elem.getDay(), elem.getDownloads()))
                            .collect(Collectors.toList()));

        } while (nextUntil.isBefore(until));

        downloadCountRepository.insert(recordsToInsert);
    }

    private LocalDate minDate(LocalDate a, LocalDate b) {
        return a.isBefore(b) ? a : b;
    }

}
