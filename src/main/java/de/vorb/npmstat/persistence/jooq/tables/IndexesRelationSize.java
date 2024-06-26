/*
 * This file is generated by jOOQ.
*/
package de.vorb.npmstat.persistence.jooq.tables;


import de.vorb.npmstat.persistence.jooq.Public;
import de.vorb.npmstat.persistence.jooq.tables.records.IndexesRelationSizeRecord;

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
public class IndexesRelationSize extends TableImpl<IndexesRelationSizeRecord> {

    private static final long serialVersionUID = -1459501788;

    /**
     * The reference instance of <code>public.indexes_relation_size</code>
     */
    public static final IndexesRelationSize INDEXES_RELATION_SIZE = new IndexesRelationSize();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<IndexesRelationSizeRecord> getRecordType() {
        return IndexesRelationSizeRecord.class;
    }

    /**
     * The column <code>public.indexes_relation_size.index_name</code>.
     */
    public final TableField<IndexesRelationSizeRecord, String> INDEX_NAME = createField("index_name", org.jooq.impl.SQLDataType.CLOB, this, "");

    /**
     * The column <code>public.indexes_relation_size.total_bytes</code>.
     */
    public final TableField<IndexesRelationSizeRecord, Long> TOTAL_BYTES = createField("total_bytes", org.jooq.impl.SQLDataType.BIGINT, this, "");

    /**
     * Create a <code>public.indexes_relation_size</code> table reference
     */
    public IndexesRelationSize() {
        this(DSL.name("indexes_relation_size"), null);
    }

    /**
     * Create an aliased <code>public.indexes_relation_size</code> table reference
     */
    public IndexesRelationSize(String alias) {
        this(DSL.name(alias), INDEXES_RELATION_SIZE);
    }

    /**
     * Create an aliased <code>public.indexes_relation_size</code> table reference
     */
    public IndexesRelationSize(Name alias) {
        this(alias, INDEXES_RELATION_SIZE);
    }

    private IndexesRelationSize(Name alias, Table<IndexesRelationSizeRecord> aliased) {
        this(alias, aliased, new Field[1]);
    }

    private IndexesRelationSize(Name alias, Table<IndexesRelationSizeRecord> aliased, Field<?>[] parameters) {
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
    public IndexesRelationSize as(String alias) {
        return new IndexesRelationSize(DSL.name(alias), this, parameters);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IndexesRelationSize as(Name alias) {
        return new IndexesRelationSize(alias, this, parameters);
    }

    /**
     * Rename this table
     */
    @Override
    public IndexesRelationSize rename(String name) {
        return new IndexesRelationSize(DSL.name(name), null, parameters);
    }

    /**
     * Rename this table
     */
    @Override
    public IndexesRelationSize rename(Name name) {
        return new IndexesRelationSize(name, null, parameters);
    }

    /**
     * Call this table-valued function
     */
    public IndexesRelationSize call(Object mainTable) {
        return new IndexesRelationSize(DSL.name(getName()), null, new Field[] { 
              DSL.val(mainTable, org.jooq.impl.DefaultDataType.getDefaultDataType("regclass"))
        });
    }

    /**
     * Call this table-valued function
     */
    public IndexesRelationSize call(Field<Object> mainTable) {
        return new IndexesRelationSize(DSL.name(getName()), null, new Field[] { 
              mainTable
        });
    }
}
