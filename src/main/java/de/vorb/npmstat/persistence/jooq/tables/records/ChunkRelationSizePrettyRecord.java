/*
 * This file is generated by jOOQ.
*/
package de.vorb.npmstat.persistence.jooq.tables.records;


import de.vorb.npmstat.persistence.jooq.tables.ChunkRelationSizePretty;

import jakarta.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record10;
import org.jooq.Row10;
import org.jooq.impl.TableRecordImpl;


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
public class ChunkRelationSizePrettyRecord extends TableRecordImpl<ChunkRelationSizePrettyRecord> implements Record10<Integer, String, String[], Object[], String[], String[], String, String, String, String> {

    private static final long serialVersionUID = 705615353;

    /**
     * Setter for <code>public.chunk_relation_size_pretty.chunk_id</code>.
     */
    public ChunkRelationSizePrettyRecord setChunkId(Integer value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.chunk_relation_size_pretty.chunk_id</code>.
     */
    public Integer getChunkId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>public.chunk_relation_size_pretty.chunk_table</code>.
     */
    public ChunkRelationSizePrettyRecord setChunkTable(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.chunk_relation_size_pretty.chunk_table</code>.
     */
    public String getChunkTable() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.chunk_relation_size_pretty.partitioning_columns</code>.
     */
    public ChunkRelationSizePrettyRecord setPartitioningColumns(String... value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.chunk_relation_size_pretty.partitioning_columns</code>.
     */
    public String[] getPartitioningColumns() {
        return (String[]) get(2);
    }

    /**
     * Setter for <code>public.chunk_relation_size_pretty.partitioning_column_types</code>.
     */
    public ChunkRelationSizePrettyRecord setPartitioningColumnTypes(Object... value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>public.chunk_relation_size_pretty.partitioning_column_types</code>.
     */
    public Object[] getPartitioningColumnTypes() {
        return (Object[]) get(3);
    }

    /**
     * Setter for <code>public.chunk_relation_size_pretty.partitioning_hash_functions</code>.
     */
    public ChunkRelationSizePrettyRecord setPartitioningHashFunctions(String... value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>public.chunk_relation_size_pretty.partitioning_hash_functions</code>.
     */
    public String[] getPartitioningHashFunctions() {
        return (String[]) get(4);
    }

    /**
     * Setter for <code>public.chunk_relation_size_pretty.ranges</code>.
     */
    public ChunkRelationSizePrettyRecord setRanges(String... value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>public.chunk_relation_size_pretty.ranges</code>.
     */
    public String[] getRanges() {
        return (String[]) get(5);
    }

    /**
     * Setter for <code>public.chunk_relation_size_pretty.table_size</code>.
     */
    public ChunkRelationSizePrettyRecord setTableSize(String value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>public.chunk_relation_size_pretty.table_size</code>.
     */
    public String getTableSize() {
        return (String) get(6);
    }

    /**
     * Setter for <code>public.chunk_relation_size_pretty.index_size</code>.
     */
    public ChunkRelationSizePrettyRecord setIndexSize(String value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>public.chunk_relation_size_pretty.index_size</code>.
     */
    public String getIndexSize() {
        return (String) get(7);
    }

    /**
     * Setter for <code>public.chunk_relation_size_pretty.toast_size</code>.
     */
    public ChunkRelationSizePrettyRecord setToastSize(String value) {
        set(8, value);
        return this;
    }

    /**
     * Getter for <code>public.chunk_relation_size_pretty.toast_size</code>.
     */
    public String getToastSize() {
        return (String) get(8);
    }

    /**
     * Setter for <code>public.chunk_relation_size_pretty.total_size</code>.
     */
    public ChunkRelationSizePrettyRecord setTotalSize(String value) {
        set(9, value);
        return this;
    }

    /**
     * Getter for <code>public.chunk_relation_size_pretty.total_size</code>.
     */
    public String getTotalSize() {
        return (String) get(9);
    }

    // -------------------------------------------------------------------------
    // Record10 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row10<Integer, String, String[], Object[], String[], String[], String, String, String, String> fieldsRow() {
        return (Row10) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row10<Integer, String, String[], Object[], String[], String[], String, String, String, String> valuesRow() {
        return (Row10) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field1() {
        return ChunkRelationSizePretty.CHUNK_RELATION_SIZE_PRETTY.CHUNK_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return ChunkRelationSizePretty.CHUNK_RELATION_SIZE_PRETTY.CHUNK_TABLE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String[]> field3() {
        return ChunkRelationSizePretty.CHUNK_RELATION_SIZE_PRETTY.PARTITIONING_COLUMNS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Object[]> field4() {
        return ChunkRelationSizePretty.CHUNK_RELATION_SIZE_PRETTY.PARTITIONING_COLUMN_TYPES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String[]> field5() {
        return ChunkRelationSizePretty.CHUNK_RELATION_SIZE_PRETTY.PARTITIONING_HASH_FUNCTIONS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String[]> field6() {
        return ChunkRelationSizePretty.CHUNK_RELATION_SIZE_PRETTY.RANGES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field7() {
        return ChunkRelationSizePretty.CHUNK_RELATION_SIZE_PRETTY.TABLE_SIZE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field8() {
        return ChunkRelationSizePretty.CHUNK_RELATION_SIZE_PRETTY.INDEX_SIZE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field9() {
        return ChunkRelationSizePretty.CHUNK_RELATION_SIZE_PRETTY.TOAST_SIZE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field10() {
        return ChunkRelationSizePretty.CHUNK_RELATION_SIZE_PRETTY.TOTAL_SIZE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component1() {
        return getChunkId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component2() {
        return getChunkTable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] component3() {
        return getPartitioningColumns();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] component4() {
        return getPartitioningColumnTypes();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] component5() {
        return getPartitioningHashFunctions();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] component6() {
        return getRanges();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component7() {
        return getTableSize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component8() {
        return getIndexSize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component9() {
        return getToastSize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component10() {
        return getTotalSize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value1() {
        return getChunkId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value2() {
        return getChunkTable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] value3() {
        return getPartitioningColumns();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] value4() {
        return getPartitioningColumnTypes();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] value5() {
        return getPartitioningHashFunctions();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] value6() {
        return getRanges();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value7() {
        return getTableSize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value8() {
        return getIndexSize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value9() {
        return getToastSize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value10() {
        return getTotalSize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChunkRelationSizePrettyRecord value1(Integer value) {
        setChunkId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChunkRelationSizePrettyRecord value2(String value) {
        setChunkTable(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChunkRelationSizePrettyRecord value3(String... value) {
        setPartitioningColumns(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChunkRelationSizePrettyRecord value4(Object... value) {
        setPartitioningColumnTypes(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChunkRelationSizePrettyRecord value5(String... value) {
        setPartitioningHashFunctions(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChunkRelationSizePrettyRecord value6(String... value) {
        setRanges(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChunkRelationSizePrettyRecord value7(String value) {
        setTableSize(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChunkRelationSizePrettyRecord value8(String value) {
        setIndexSize(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChunkRelationSizePrettyRecord value9(String value) {
        setToastSize(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChunkRelationSizePrettyRecord value10(String value) {
        setTotalSize(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChunkRelationSizePrettyRecord values(Integer value1, String value2, String[] value3, Object[] value4, String[] value5, String[] value6, String value7, String value8, String value9, String value10) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        value10(value10);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ChunkRelationSizePrettyRecord
     */
    public ChunkRelationSizePrettyRecord() {
        super(ChunkRelationSizePretty.CHUNK_RELATION_SIZE_PRETTY);
    }

    /**
     * Create a detached, initialised ChunkRelationSizePrettyRecord
     */
    public ChunkRelationSizePrettyRecord(Integer chunkId, String chunkTable, String[] partitioningColumns, Object[] partitioningColumnTypes, String[] partitioningHashFunctions, String[] ranges, String tableSize, String indexSize, String toastSize, String totalSize) {
        super(ChunkRelationSizePretty.CHUNK_RELATION_SIZE_PRETTY);

        set(0, chunkId);
        set(1, chunkTable);
        set(2, partitioningColumns);
        set(3, partitioningColumnTypes);
        set(4, partitioningHashFunctions);
        set(5, ranges);
        set(6, tableSize);
        set(7, indexSize);
        set(8, toastSize);
        set(9, totalSize);
    }
}
