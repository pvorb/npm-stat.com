package de.vorb.npmstat.clients.downloads;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.time.ZoneId;

@FeignClient(name = "downloads", url = "${npm.api.baseUrl}/downloads/")
public interface DownloadsClient {

    ZoneId API_TIME_ZONE =  ZoneId.of("America/Los_Angeles");
    LocalDate MINIMAL_DATE = LocalDate.parse("2015-01-10");

    @GetMapping("range/{from}:{until}/{package}")
    DownloadsJson getPackageDownloadsForTimeRange(
            @PathVariable("package") String packageName,
            @PathVariable("from") LocalDate from,
            @PathVariable("until") LocalDate until);

}
