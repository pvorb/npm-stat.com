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

package de.vorb.npmstat.services;

import de.vorb.npmstat.persistence.jooq.tables.records.DownloadCountRecord;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record2;
import org.jooq.Result;
import org.jooq.TableField;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.style.Styler;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import static de.vorb.npmstat.persistence.jooq.Tables.DOWNLOAD_COUNT;
import static org.jooq.impl.DSL.sum;

@Slf4j
@Service
@RequiredArgsConstructor
public class MonthlyReportGenerator {

    private static final int NUMBER_OF_RESULTS = 5;

    private final DSLContext jooq;

    public CategoryChart generateReportForMonth(YearMonth yearMonth) {
        final TableField<DownloadCountRecord, String> packageName = DOWNLOAD_COUNT.PACKAGE_NAME;
        final Field<BigDecimal> sum = sum(DOWNLOAD_COUNT.COUNT).as("s");
        final LocalDate start = yearMonth.atDay(1);
        final LocalDate end = yearMonth.atEndOfMonth();
        final Result<Record2<String, BigDecimal>> result = jooq.select(packageName, sum)
                .from(DOWNLOAD_COUNT)
                .where(DOWNLOAD_COUNT.DATE.between(start, end))
                .groupBy(packageName)
                .orderBy(sum.desc(), packageName.asc())
                .limit(NUMBER_OF_RESULTS)
                .fetch();

        final List<String> packageNames = result.getValues(packageName);
        final List<BigDecimal> sums = result.getValues(sum);
        log.info("sums = {}", sums);
        return drawChart(packageNames, sums);
    }

    private CategoryChart drawChart(List<String> packageNames, List<BigDecimal> sums) {

        final CategoryChart chart = new CategoryChartBuilder()
                .width(800)
                .height(600)
                .title("Temperature vs. Color")
                .xAxisTitle("Color")
                .yAxisTitle("Temperature")
                .theme(Styler.ChartTheme.Matlab)
                .build();

        chart.addSeries("fish", packageNames, sums);

        return chart;
    }

}
