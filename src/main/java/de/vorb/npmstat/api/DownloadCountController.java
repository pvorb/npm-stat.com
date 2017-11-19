package de.vorb.npmstat.api;

import de.vorb.npmstat.services.DownloadCountProvider;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class DownloadCountController {

    private final DownloadCountProvider downloadCountProvider;

    @GetMapping(value = "/api/download-counts", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map<String, Map<LocalDate, Integer>> getDownloadCounts(
            @RequestParam("package") Set<String> packageNames,
            @RequestParam("from") LocalDate from,
            @RequestParam("until") LocalDate until) {

        return downloadCountProvider.getDownloadCounts(packageNames, from, until);
    }

}
