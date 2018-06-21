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

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;

@FeignClient(name = "downloads", url = "${npm.api.baseUrl}/downloads/")
public interface DownloadsClient {

    LocalDate MINIMAL_DATE = LocalDate.parse("2015-01-10");

    @GetMapping("range/{from}:{until}/{package}")
    DownloadsJson getPackageDownloadsForTimeRange(
            @PathVariable("package") String packageName,
            @PathVariable("from") LocalDate from,
            @PathVariable("until") LocalDate until);

}
