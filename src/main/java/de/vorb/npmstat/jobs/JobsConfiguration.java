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

package de.vorb.npmstat.jobs;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.separator.SimpleRecordSeparatorPolicy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.nio.file.Paths;
import java.util.List;

@Configuration
public class JobsConfiguration {

    private static final String DOWNLOAD_WEEK_DATA = "downloadWeekData";
    private static final String GENERATE_WEEK_REPORT = "generateWeekReport";

    private final JobBuilderFactory jobs;
    private final StepBuilderFactory steps;

    public JobsConfiguration(JobBuilderFactory jobs, StepBuilderFactory steps) {
        this.jobs = jobs;
        this.steps = steps;
    }
/*
    @Bean
    public Job job(@Qualifier(DOWNLOAD_WEEK_DATA) Step downloadWeekData,
            @Qualifier(GENERATE_WEEK_REPORT) Step generateWeekReport) {
        return jobs.get("weeklyReport")
                .start(downloadWeekData)
                .next(generateWeekReport)
                .build();
    }

    @Bean
    protected Step listPackageNames() {
        steps.get("listPackageNames")
                .
    }

    @Bean
    protected Step downloadWeekData(ItemReader<String> packageNameReader,
            ItemProcessor<String, Person> processor,
            ItemWriter<Person> writer) {
        return steps.get("downloadWeekData")
                .<Person, Person>chunk(128)
                .reader(packageNameReader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    protected Step generateWeekReport(Tasklet tasklet) {
        return steps.get("generateWeekReport")
                .tasklet(tasklet)
                .build();
    }

    @Bean
    protected ItemReader<String> flatFileItemReader() {
        final FlatFileItemReader<String> fileItemReader = new FlatFileItemReader<>();
        fileItemReader.setRecordSeparatorPolicy(new SimpleRecordSeparatorPolicy());
        fileItemReader.setEncoding("UTF-8");
        fileItemReader.setResource(new FileSystemResource(Paths.get("packages.txt")));
        return fileItemReader;
    }*/

}
