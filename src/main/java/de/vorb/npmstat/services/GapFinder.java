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

package de.vorb.npmstat.services;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static com.google.common.base.Preconditions.checkArgument;

@Service
class GapFinder {

    List<Gap> findGaps(LocalDate from, LocalDate until, Map<LocalDate, Integer> downloadCounts) {

        checkArgument(!until.isBefore(from), "until < from");

        final List<LocalDate> expectedDays = determineExpectedDays(from, until);

        final List<Gap> gaps = new ArrayList<>();
        LocalDate firstMissingDayOfGap = null;
        LocalDate previousMissingDay = null;
        for (final LocalDate day : expectedDays) {
            if (!downloadCounts.containsKey(day)) {
                if (firstMissingDayOfGap == null) {
                    firstMissingDayOfGap = day;
                }
                previousMissingDay = day;
            } else if (previousMissingDay != null) {

                gaps.add(new Gap(firstMissingDayOfGap, previousMissingDay));

                previousMissingDay = null;
                firstMissingDayOfGap = null;
            }
        }

        if (firstMissingDayOfGap != null) {
            gaps.add(new Gap(firstMissingDayOfGap, until));
        }

        return gaps;
    }

    private List<LocalDate> determineExpectedDays(LocalDate from, LocalDate until) {
        return LongStream.rangeClosed(0, ChronoUnit.DAYS.between(from, until))
                .mapToObj(from::plusDays)
                .collect(Collectors.toList());
    }

}
