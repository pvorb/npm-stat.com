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
