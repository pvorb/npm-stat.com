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

package de.vorb.npmstat.api;

import de.vorb.npmstat.services.AuthorPackageProvider;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;

@RestController
@RequiredArgsConstructor
public class AuthorPackageController {

    private final AuthorPackageProvider authorPackageProvider;

    @GetMapping(value = "/api/author-packages", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Set<String> getPackageNames(@RequestParam("author") String authorName) {
        checkArgument(!authorName.isEmpty(), "author missing");

        return authorPackageProvider.findPackageNamesForAuthor(authorName);
    }

}
