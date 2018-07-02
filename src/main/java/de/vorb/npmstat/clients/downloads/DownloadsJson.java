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
