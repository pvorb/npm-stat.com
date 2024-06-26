/*
 * This file is generated by jOOQ.
*/
package de.vorb.npmstat.persistence.jooq.tables;


import de.vorb.npmstat.persistence.jooq.Public;
import de.vorb.npmstat.persistence.jooq.tables.records.HypertableRelationSizeRecord;

import jakarta.annotation.Generated;

import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.7"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class HypertableRelationSize extends TableImpl<HypertableRelationSizeRecord> {

    private static final long serialVersionUID = 914500209;

    /**
     * The reference instance of <code>public.hypertable_relation_size</code>
     */
    public static final HypertableRelationSize HYPERTABLE_RELATION_SIZE = new HypertableRelationSize();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<HypertableRelationSizeRecord> getRecordType() {
        return HypertableRelationSizeRecord.class;
    }

    /**
     * The column <code>public.hypertable_relation_size.table_bytes</code>.
     */
    public final TableField<HypertableRelationSizeRecord, Long> TABLE_BYTES = createField("table_bytes", org.jooq.impl.SQLDataType.BIGINT, this, "");

    /**
     * The column <code>public.hypertable_relation_size.index_bytes</code>.
     */
    public final TableField<HypertableRelationSizeRecord, Long> INDEX_BYTES = createField("index_bytes", org.jooq.impl.SQLDataType.BIGINT, this, "");

    /**
     * The column <code>public.hypertable_relation_size.toast_bytes</code>.
     */
    public final TableField<HypertableRelationSizeRecord, Long> TOAST_BYTES = createField("toast_bytes", org.jooq.impl.SQLDataType.BIGINT, this, "");

    /**
     * The column <code>public.hypertable_relation_size.total_bytes</code>.
     */
    public final TableField<HypertableRelationSizeRecord, Long> TOTAL_BYTES = createField("total_bytes", org.jooq.impl.SQLDataType.BIGINT, this, "");

    /**
     * Create a <code>public.hypertable_relation_size</code> table reference
     */
    public HypertableRelationSize() {
        this(DSL.name("hypertable_relation_size"), null);
    }

    /**
     * Create an aliased <code>public.hypertable_relation_size</code> table reference
     */
    public HypertableRelationSize(String alias) {
        this(DSL.name(alias), HYPERTABLE_RELATION_SIZE);
    }

    /**
     * Create an aliased <code>public.hypertable_relation_size</code> table reference
     */
    public HypertableRelationSize(Name alias) {
        this(alias, HYPERTABLE_RELATION_SIZE);
    }

    private HypertableRelationSize(Name alias, Table<HypertableRelationSizeRecord> aliased) {
        this(alias, aliased, new Field[1]);
    }

    private HypertableRelationSize(Name alias, Table<HypertableRelationSizeRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HypertableRelationSize as(String alias) {
        return new HypertableRelationSize(DSL.name(alias), this, parameters);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HypertableRelationSize as(Name alias) {
        return new HypertableRelationSize(alias, this, parameters);
    }

    /**
     * Rename this table
     */
    @Override
    public HypertableRelationSize rename(String name) {
        return new HypertableRelationSize(DSL.name(name), null, parameters);
    }

    /**
     * Rename this table
     */
    @Override
    public HypertableRelationSize rename(Name name) {
        return new HypertableRelationSize(name, null, parameters);
    }

    /**
     * Call this table-valued function
     */
    public HypertableRelationSize call(Object mainTable) {
        return new HypertableRelationSize(DSL.name(getName()), null, new Field[] { 
              DSL.val(mainTable, org.jooq.impl.DefaultDataType.getDefaultDataType("regclass"))
        });
    }

    /**
     * Call this table-valued function
     */
    public HypertableRelationSize call(Field<Object> mainTable) {
        return new HypertableRelationSize(DSL.name(getName()), null, new Field[] { 
              mainTable
        });
    }
}
