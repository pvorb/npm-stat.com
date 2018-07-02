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

package de.vorb.npmstat.api;

import de.vorb.npmstat.clients.downloads.DownloadsClient;
import de.vorb.npmstat.services.AuthorPackageProvider;
import de.vorb.npmstat.services.DownloadCountProvider;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Clock;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;

@RestController
@RequiredArgsConstructor
public class DownloadCountController {

    private final DownloadCountProvider downloadCountProvider;
    private final AuthorPackageProvider authorPackageProvider;
    private final ObjectMapper objectMapper;
    private final Clock clock;

    @GetMapping(value = "/api/download-counts", produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            params = {"package", "!author"})
    public Map<String, Map<LocalDate, Integer>> getDownloadCounts(
            @RequestParam("package") Set<String> packageNames,
            @RequestParam("from") LocalDate from,
            @RequestParam("until") LocalDate until) {

        checkArgument(!from.isAfter(until), "from > until");

        return downloadCountProvider.getDownloadCounts(packageNames, sanitizeFrom(from), sanitizeUntil(until));
    }

    @GetMapping(value = "/api/download-counts", produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            params = {"author", "!params"})
    public Map<String, Map<LocalDate, Integer>> getDownloadCounts(
            @RequestParam("author") String author,
            @RequestParam("from") LocalDate from,
            @RequestParam("until") LocalDate until) {

        final Set<String> packageNames = authorPackageProvider.findPackageNamesForAuthor(author);

        return getDownloadCounts(packageNames, from, until);
    }

    private LocalDate sanitizeFrom(LocalDate from) {
        return from.isBefore(DownloadsClient.MINIMAL_DATE) ? DownloadsClient.MINIMAL_DATE : from;
    }

    private LocalDate sanitizeUntil(LocalDate until) {
        final OffsetDateTime currentTimeUtc = OffsetDateTime.now(clock);

        final LocalDate today = currentTimeUtc.toLocalDate();
        final LocalDate lastDayWithData;

        final OffsetDateTime calculationEndTime = today.atTime(6, 0, 0).atOffset(ZoneOffset.UTC);

        if (currentTimeUtc.isBefore(calculationEndTime)) {
            lastDayWithData = today.minusDays(2);
        } else {
            lastDayWithData = today.minusDays(1);
        }

        return until.isAfter(lastDayWithData) ? lastDayWithData : sanitizeFrom(until);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorJson> handleFeignException(FeignException e) {
        final ResponseEntity.BodyBuilder responseEntity = ResponseEntity.status(e.status())
                .contentType(MediaType.APPLICATION_JSON_UTF8);

        final String exceptionString = e.toString();
        try {
            final String errorBody = exceptionString.split("\n")[1];
            final JsonNode jsonNode = objectMapper.readTree(errorBody);
            final String errorMessage = jsonNode.get("error").asText();
            return responseEntity.body(new ErrorJson(errorMessage));
        } catch (Exception ignored) {
            return responseEntity.body(new ErrorJson(exceptionString));
        }
    }

}
