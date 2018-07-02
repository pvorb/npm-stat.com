/*
 * This file is generated by jOOQ.
*/
package de.vorb.npmstat.persistence.jooq.tables.pojos;


import java.io.Serializable;

import javax.annotation.Generated;


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
public class HypertableRelationSizePretty implements Serializable {

    private static final long serialVersionUID = -1586799470;

    private String tableSize;
    private String indexSize;
    private String toastSize;
    private String totalSize;

    public HypertableRelationSizePretty() {}

    public HypertableRelationSizePretty(HypertableRelationSizePretty value) {
        this.tableSize = value.tableSize;
        this.indexSize = value.indexSize;
        this.toastSize = value.toastSize;
        this.totalSize = value.totalSize;
    }

    public HypertableRelationSizePretty(
        String tableSize,
        String indexSize,
        String toastSize,
        String totalSize
    ) {
        this.tableSize = tableSize;
        this.indexSize = indexSize;
        this.toastSize = toastSize;
        this.totalSize = totalSize;
    }

    public String getTableSize() {
        return this.tableSize;
    }

    public HypertableRelationSizePretty setTableSize(String tableSize) {
        this.tableSize = tableSize;
        return this;
    }

    public String getIndexSize() {
        return this.indexSize;
    }

    public HypertableRelationSizePretty setIndexSize(String indexSize) {
        this.indexSize = indexSize;
        return this;
    }

    public String getToastSize() {
        return this.toastSize;
    }

    public HypertableRelationSizePretty setToastSize(String toastSize) {
        this.toastSize = toastSize;
        return this;
    }

    public String getTotalSize() {
        return this.totalSize;
    }

    public HypertableRelationSizePretty setTotalSize(String totalSize) {
        this.totalSize = totalSize;
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
        final HypertableRelationSizePretty other = (HypertableRelationSizePretty) obj;
        if (tableSize == null) {
            if (other.tableSize != null)
                return false;
        }
        else if (!tableSize.equals(other.tableSize))
            return false;
        if (indexSize == null) {
            if (other.indexSize != null)
                return false;
        }
        else if (!indexSize.equals(other.indexSize))
            return false;
        if (toastSize == null) {
            if (other.toastSize != null)
                return false;
        }
        else if (!toastSize.equals(other.toastSize))
            return false;
        if (totalSize == null) {
            if (other.totalSize != null)
                return false;
        }
        else if (!totalSize.equals(other.totalSize))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.tableSize == null) ? 0 : this.tableSize.hashCode());
        result = prime * result + ((this.indexSize == null) ? 0 : this.indexSize.hashCode());
        result = prime * result + ((this.toastSize == null) ? 0 : this.toastSize.hashCode());
        result = prime * result + ((this.totalSize == null) ? 0 : this.totalSize.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("HypertableRelationSizePretty (");

        sb.append(tableSize);
        sb.append(", ").append(indexSize);
        sb.append(", ").append(toastSize);
        sb.append(", ").append(totalSize);

        sb.append(")");
        return sb.toString();
    }
}
