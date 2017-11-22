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

package de.vorb.npmstat.persistence.repositories;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static de.vorb.npmstat.persistence.jooq.Tables.PACKAGE;

@Repository
@RequiredArgsConstructor
public class PackageRepository {

    private final DSLContext dslContext;

    @Transactional
    public Long getIdForPackageName(String packageName) {
        final Long packageId = dslContext.select(PACKAGE.ID)
                .from(PACKAGE)
                .where(PACKAGE.NAME.eq(packageName))
                .fetchOne(PACKAGE.ID);

        if (packageId != null) {
            return packageId;
        } else {
            return dslContext.insertInto(PACKAGE)
                    .set(PACKAGE.NAME, packageName)
                    .returning(PACKAGE.ID)
                    .fetchOne()
                    .getId();
        }
    }

}
