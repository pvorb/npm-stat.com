package de.vorb.npmstat.persistence.repositories;

import lombok.Data;

import java.time.LocalDate;
import java.util.Map;

@Data
public class PackageDownloadCounts {

    private final String packageName;
    private final Map<LocalDate, Integer> countsPerDay;

}
