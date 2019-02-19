/*
 * Copyright 2012-2019 the original author or authors.
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

package de.vorb.npmstat;

import de.vorb.npmstat.services.MonthlyReportGenerator;

import lombok.RequiredArgsConstructor;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.SwingWrapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.YearMonth;

@Component
@RequiredArgsConstructor
public class Report implements CommandLineRunner {

    private final MonthlyReportGenerator generator;

    @Override
    public void run(String... args) throws Exception {
        final CategoryChart chart = generator.generateReportForMonth(YearMonth.of(2019, 2));
        new SwingWrapper<>(chart).displayChart();
    }

}
