/*
 * This file is generated by jOOQ.
*/
package de.vorb.npmstat.persistence.jooq.tables.pojos;


import java.io.Serializable;
import java.util.Arrays;

import jakarta.annotation.Generated;


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
public class ChunkRelationSize implements Serializable {

    private static final long serialVersionUID = -245030974;

    private Integer  chunkId;
    private String   chunkTable;
    private String[] partitioningColumns;
    private Object[] partitioningColumnTypes;
    private String[] partitioningHashFunctions;
    private Object[] ranges;
    private Long     tableBytes;
    private Long     indexBytes;
    private Long     toastBytes;
    private Long     totalBytes;

    public ChunkRelationSize() {}

    public ChunkRelationSize(ChunkRelationSize value) {
        this.chunkId = value.chunkId;
        this.chunkTable = value.chunkTable;
        this.partitioningColumns = value.partitioningColumns;
        this.partitioningColumnTypes = value.partitioningColumnTypes;
        this.partitioningHashFunctions = value.partitioningHashFunctions;
        this.ranges = value.ranges;
        this.tableBytes = value.tableBytes;
        this.indexBytes = value.indexBytes;
        this.toastBytes = value.toastBytes;
        this.totalBytes = value.totalBytes;
    }

    public ChunkRelationSize(
        Integer  chunkId,
        String   chunkTable,
        String[] partitioningColumns,
        Object[] partitioningColumnTypes,
        String[] partitioningHashFunctions,
        Object[] ranges,
        Long     tableBytes,
        Long     indexBytes,
        Long     toastBytes,
        Long     totalBytes
    ) {
        this.chunkId = chunkId;
        this.chunkTable = chunkTable;
        this.partitioningColumns = partitioningColumns;
        this.partitioningColumnTypes = partitioningColumnTypes;
        this.partitioningHashFunctions = partitioningHashFunctions;
        this.ranges = ranges;
        this.tableBytes = tableBytes;
        this.indexBytes = indexBytes;
        this.toastBytes = toastBytes;
        this.totalBytes = totalBytes;
    }

    public Integer getChunkId() {
        return this.chunkId;
    }

    public ChunkRelationSize setChunkId(Integer chunkId) {
        this.chunkId = chunkId;
        return this;
    }

    public String getChunkTable() {
        return this.chunkTable;
    }

    public ChunkRelationSize setChunkTable(String chunkTable) {
        this.chunkTable = chunkTable;
        return this;
    }

    public String[] getPartitioningColumns() {
        return this.partitioningColumns;
    }

    public ChunkRelationSize setPartitioningColumns(String... partitioningColumns) {
        this.partitioningColumns = partitioningColumns;
        return this;
    }

    public Object[] getPartitioningColumnTypes() {
        return this.partitioningColumnTypes;
    }

    public ChunkRelationSize setPartitioningColumnTypes(Object... partitioningColumnTypes) {
        this.partitioningColumnTypes = partitioningColumnTypes;
        return this;
    }

    public String[] getPartitioningHashFunctions() {
        return this.partitioningHashFunctions;
    }

    public ChunkRelationSize setPartitioningHashFunctions(String... partitioningHashFunctions) {
        this.partitioningHashFunctions = partitioningHashFunctions;
        return this;
    }

    public Object[] getRanges() {
        return this.ranges;
    }

    public ChunkRelationSize setRanges(Object... ranges) {
        this.ranges = ranges;
        return this;
    }

    public Long getTableBytes() {
        return this.tableBytes;
    }

    public ChunkRelationSize setTableBytes(Long tableBytes) {
        this.tableBytes = tableBytes;
        return this;
    }

    public Long getIndexBytes() {
        return this.indexBytes;
    }

    public ChunkRelationSize setIndexBytes(Long indexBytes) {
        this.indexBytes = indexBytes;
        return this;
    }

    public Long getToastBytes() {
        return this.toastBytes;
    }

    public ChunkRelationSize setToastBytes(Long toastBytes) {
        this.toastBytes = toastBytes;
        return this;
    }

    public Long getTotalBytes() {
        return this.totalBytes;
    }

    public ChunkRelationSize setTotalBytes(Long totalBytes) {
        this.totalBytes = totalBytes;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final ChunkRelationSize other = (ChunkRelationSize) obj;
        if (chunkId == null) {
            if (other.chunkId != null)
                return false;
        }
        else if (!chunkId.equals(other.chunkId))
            return false;
        if (chunkTable == null) {
            if (other.chunkTable != null)
                return false;
        }
        else if (!chunkTable.equals(other.chunkTable))
            return false;
        if (partitioningColumns == null) {
            if (other.partitioningColumns != null)
                return false;
        }
        else if (!Arrays.equals(partitioningColumns, other.partitioningColumns))
            return false;
        if (partitioningColumnTypes == null) {
            if (other.partitioningColumnTypes != null)
                return false;
        }
        else if (!Arrays.equals(partitioningColumnTypes, other.partitioningColumnTypes))
            return false;
        if (partitioningHashFunctions == null) {
            if (other.partitioningHashFunctions != null)
                return false;
        }
        else if (!Arrays.equals(partitioningHashFunctions, other.partitioningHashFunctions))
            return false;
        if (ranges == null) {
            if (other.ranges != null)
                return false;
        }
        else if (!Arrays.equals(ranges, other.ranges))
            return false;
        if (tableBytes == null) {
            if (other.tableBytes != null)
                return false;
        }
        else if (!tableBytes.equals(other.tableBytes))
            return false;
        if (indexBytes == null) {
            if (other.indexBytes != null)
                return false;
        }
        else if (!indexBytes.equals(other.indexBytes))
            return false;
        if (toastBytes == null) {
            if (other.toastBytes != null)
                return false;
        }
        else if (!toastBytes.equals(other.toastBytes))
            return false;
        if (totalBytes == null) {
            if (other.totalBytes != null)
                return false;
        }
        else if (!totalBytes.equals(other.totalBytes))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.chunkId == null) ? 0 : this.chunkId.hashCode());
        result = prime * result + ((this.chunkTable == null) ? 0 : this.chunkTable.hashCode());
        result = prime * result + ((this.partitioningColumns == null) ? 0 : Arrays.hashCode(this.partitioningColumns));
        result = prime * result + ((this.partitioningColumnTypes == null) ? 0 : Arrays.hashCode(this.partitioningColumnTypes));
        result = prime * result + ((this.partitioningHashFunctions == null) ? 0 : Arrays.hashCode(this.partitioningHashFunctions));
        result = prime * result + ((this.ranges == null) ? 0 : Arrays.hashCode(this.ranges));
        result = prime * result + ((this.tableBytes == null) ? 0 : this.tableBytes.hashCode());
        result = prime * result + ((this.indexBytes == null) ? 0 : this.indexBytes.hashCode());
        result = prime * result + ((this.toastBytes == null) ? 0 : this.toastBytes.hashCode());
        result = prime * result + ((this.totalBytes == null) ? 0 : this.totalBytes.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ChunkRelationSize (");

        sb.append(chunkId);
        sb.append(", ").append(chunkTable);
        sb.append(", ").append(Arrays.toString(partitioningColumns));
        sb.append(", ").append(Arrays.toString(partitioningColumnTypes));
        sb.append(", ").append(Arrays.toString(partitioningHashFunctions));
        sb.append(", ").append(Arrays.toString(ranges));
        sb.append(", ").append(tableBytes);
        sb.append(", ").append(indexBytes);
        sb.append(", ").append(toastBytes);
        sb.append(", ").append(totalBytes);

        sb.append(")");
        return sb.toString();
    }
}
