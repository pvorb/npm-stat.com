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

import de.vorb.npmstat.clients.authors.AuthorJson;
import de.vorb.npmstat.clients.authors.AuthorsClient;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorPackageProvider {

    private final AuthorsClient authorsClient;

    public Set<String> findPackageNamesForAuthor(String authorName) {

        final String startKey = "[\"" + authorName + "\"]";
        final String endKey = "[\"" + authorName + "\",{}]";

        final AuthorJson authorJson = authorsClient.browseAuthors(startKey, endKey);

        return extractPackageNamesFromAuthorJson(authorJson, authorName);
    }

    private Set<String> extractPackageNamesFromAuthorJson(AuthorJson authorJson, String authorName) {
        return authorJson.getRows().stream()
                .filter(row -> authorName.equals(row.getKey().get(0)))
                .map(row -> row.getKey().get(1))
                .collect(Collectors.toSet());
    }

}
