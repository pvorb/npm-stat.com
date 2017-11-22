/*
 * Copyright 2012-2017 the original author or authors.
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

package de.vorb.npmstat.services;

import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class GapFinderTest {

    private final GapFinder gapFinder = new GapFinder();

    private final LocalDate from = LocalDate.parse("2016-12-01");
    private final LocalDate until = LocalDate.parse("2017-02-01");

    @Test
    public void returnsEmptyListIfAllDaysInMap() throws Exception {
        final LocalDate onlyDay = LocalDate.parse("2017-01-01");
        final List<Gap> gaps = gapFinder.findGaps(onlyDay, onlyDay, Collections.singletonMap(onlyDay, 0));
        assertThat(gaps).isEmpty();
    }

    @Test
    public void findsGapAtBeginningOfRange() throws Exception {

        final LocalDate dayAfterGap = LocalDate.parse("2017-01-10");

        final Map<LocalDate, Integer> downloadCounts = toDownloadCounts(dateRange(dayAfterGap, until));

        final List<Gap> gaps = gapFinder.findGaps(from, until, downloadCounts);

        assertThat(gaps).hasSize(1);
        assertThat(gaps.get(0)).isEqualTo(new Gap(from, dayAfterGap.minusDays(1)));
    }

    @Test
    public void findsGapAtEndingOfRange() throws Exception {

        final LocalDate dayBeforeGap = LocalDate.parse("2016-12-27");

        final Map<LocalDate, Integer> downloadCounts = toDownloadCounts(dateRange(from, dayBeforeGap));

        final List<Gap> gaps = gapFinder.findGaps(from, until, downloadCounts);

        assertThat(gaps).hasSize(1);
        assertThat(gaps.get(0)).isEqualTo(new Gap(dayBeforeGap.plusDays(1), until));
    }

    @Test
    public void findsMultipleGaps() throws Exception {

        final Stream<LocalDate> days = dateRange(from, until).filter(day -> day.getDayOfWeek() == DayOfWeek.FRIDAY);

        final List<Gap> expectedGaps = Arrays.asList(
                new Gap(LocalDate.parse("2016-12-01"), LocalDate.parse("2016-12-01")),
                new Gap(LocalDate.parse("2016-12-03"), LocalDate.parse("2016-12-08")),
                new Gap(LocalDate.parse("2016-12-10"), LocalDate.parse("2016-12-15")),
                new Gap(LocalDate.parse("2016-12-17"), LocalDate.parse("2016-12-22")),
                new Gap(LocalDate.parse("2016-12-24"), LocalDate.parse("2016-12-29")),
                new Gap(LocalDate.parse("2016-12-31"), LocalDate.parse("2017-01-05")),
                new Gap(LocalDate.parse("2017-01-07"), LocalDate.parse("2017-01-12")),
                new Gap(LocalDate.parse("2017-01-14"), LocalDate.parse("2017-01-19")),
                new Gap(LocalDate.parse("2017-01-21"), LocalDate.parse("2017-01-26")),
                new Gap(LocalDate.parse("2017-01-28"), LocalDate.parse("2017-02-01"))
        );

        final List<Gap> gaps = gapFinder.findGaps(from, until, toDownloadCounts(days));

        assertThat(gaps).isEqualTo(expectedGaps);
    }

    @Test
    public void findsGapForEmptyMap() throws Exception {

        final List<Gap> gaps = gapFinder.findGaps(from, until, Collections.emptyMap());

        assertThat(gaps).hasSize(1);
        assertThat(gaps.get(0)).isEqualTo(new Gap(from, until));
    }

    private Stream<LocalDate> dateRange(LocalDate from, LocalDate until) {
        return LongStream.rangeClosed(0, ChronoUnit.DAYS.between(from, until))
                .mapToObj(from::plusDays);
    }

    private Map<LocalDate, Integer> toDownloadCounts(Stream<LocalDate> days) {
        return days.collect(Collectors.toMap(Function.identity(), any -> 0));
    }
}
