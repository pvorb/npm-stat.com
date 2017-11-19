package de.vorb.npmstat.clients.downloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class DownloadsJson {

    private LocalDate start;
    private LocalDate end;
    @JsonProperty("package")
    private String packageName;
    private List<DownloadsPerDayJson> downloads;

    @Data
    public static class DownloadsPerDayJson {
        private int downloads;
        private LocalDate day;
    }

}
