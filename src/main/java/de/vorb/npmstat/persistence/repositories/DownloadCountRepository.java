package de.vorb.npmstat.persistence.repositories;

import de.vorb.npmstat.persistence.jooq.tables.records.DownloadCountRecord;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static de.vorb.npmstat.persistence.jooq.Tables.DOWNLOAD_COUNT;
import static de.vorb.npmstat.persistence.jooq.Tables.PACKAGE;

@Repository
@RequiredArgsConstructor
public class DownloadCountRepository {

    private final DSLContext dslContext;

    public Map<String, Map<LocalDate, Integer>> findDownloadCounts(Set<String> packageNames,
            LocalDate from, LocalDate until) {
        return dslContext.select(PACKAGE.NAME, DOWNLOAD_COUNT.DATE, DOWNLOAD_COUNT.COUNT)
                .from(DOWNLOAD_COUNT
                        .join(PACKAGE).on(DOWNLOAD_COUNT.PACKAGE_ID.eq(PACKAGE.ID)))
                .where(PACKAGE.NAME.in(packageNames))
                .and(DOWNLOAD_COUNT.DATE.between(from, until))
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(
                        record -> record.get(PACKAGE.NAME),
                        Collectors.toMap(
                                record -> record.get(DOWNLOAD_COUNT.DATE),
                                record -> record.get(DOWNLOAD_COUNT.COUNT))));
    }

    public void insert(List<DownloadCountRecord> records) {
        dslContext.batchInsert(records)
                .execute();
    }

}
