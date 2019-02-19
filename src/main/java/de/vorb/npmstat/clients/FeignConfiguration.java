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

package de.vorb.npmstat.clients;

import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;

import java.util.Locale;

@Configuration
public class FeignConfiguration {

    @Bean
    public FeignFormatterRegistrar localDateFeignFormatterRegistrar() {
        return registry -> {
            final DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
            registrar.setUseIsoFormat(true);
            registrar.registerFormatters(registry);
        };
    }

    @Bean
    public FeignFormatterRegistrar stringArrayFeignFormatterRegistrar() {
        return registry -> registry.addFormatterForFieldType(String[].class, new Formatter<String[]>() {
            @Override
            public String[] parse(String text, Locale locale) {
                return text.split(",");
            }

            @Override
            public String print(String[] object, Locale locale) {
                return String.join(",", object);
            }
        });
    }

}
