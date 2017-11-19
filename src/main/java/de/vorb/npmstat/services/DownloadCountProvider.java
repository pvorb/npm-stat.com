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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DownloadCountProvider {

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
                .forEach(packageName -> downloadCounts.put(packageName, new HashMap<>()));
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
        final LocalDate to = gaps.get(gaps.size() - 1).getTo();

        final DownloadsJson downloadsFromApi = downloadsClient.getPackageDownloadsForTimeRange(packageName, from, to);

        final Long packageId = packageRepository.getIdForPackageName(packageName);

        final List<DownloadCountRecord> recordsToInsert = downloadsFromApi.getDownloads().stream()
                .filter(elem -> !downloadCounts.containsKey(elem.getDay()))
                .peek(elem -> downloadCounts.put(elem.getDay(), elem.getDownloads()))
                .map(elem -> new DownloadCountRecord(packageId, elem.getDay(), elem.getDownloads()))
                .collect(Collectors.toList());

        downloadCountRepository.insert(recordsToInsert);
    }

}
