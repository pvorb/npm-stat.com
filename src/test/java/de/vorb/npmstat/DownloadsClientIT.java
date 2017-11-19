package de.vorb.npmstat;

import de.vorb.npmstat.clients.downloads.DownloadsClient;
import de.vorb.npmstat.clients.downloads.DownloadsJson;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = NONE)
public class DownloadsClientIT {

    @Autowired
    private DownloadsClient downloadsClient;

    @Test
    public void requestsDownloadsForPackageClone() throws Exception {

        final String packageName = "clone";

        final LocalDate from = LocalDate.parse("2017-02-12");
        final LocalDate until = LocalDate.parse("2017-02-16");

        final int expectedNumberOfDays = (int) DAYS.between(from, until.plusDays(1));

        final DownloadsJson downloadsJson = downloadsClient.getPackageDownloadsForTimeRange(packageName, from, until);

        assertThat(downloadsJson.getPackageName()).isEqualTo(packageName);
        assertThat(downloadsJson.getStart()).isEqualTo(from);
        assertThat(downloadsJson.getEnd()).isEqualTo(until);

        assertThat(downloadsJson.getDownloads()).hasSize(expectedNumberOfDays);
    }

}
