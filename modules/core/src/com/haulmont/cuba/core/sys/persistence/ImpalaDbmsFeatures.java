package com.haulmont.cuba.core.sys.persistence;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class ImpalaDbmsFeatures implements DbmsFeatures {
    @Override
    public Map<String, String> getJpaParameters() {
        HashMap<String, String> params = new HashMap<>();
        params.put("eclipselink.target-database", "com.haulmont.addon.impala.core.ImpalaSQLPlatform");
        return params;
    }

    @Override
    public String getIdColumn() {
        return "id";
    }

    @Override
    public String getDeleteTsColumn() {
        return "delete_ts";
    }

    @Override
    public String getTimeStampType() {
        return "timestamp";
    }

    @Nullable
    @Override
    public String getUuidTypeClassName() {
        return null;
    }

    @Nullable
    @Override
    public String getTransactionTimeoutStatement() {
        return null;
    }

    @Override
    public String getUniqueConstraintViolationPattern() {
        return "ERROR: duplicate key value violates unique constraint \"(.+)\"";
    }

    @Override
    public boolean isNullsLastSorting() {
        return true;
    }

    @Override
    public boolean isSchemaByUser() {
        return false;
    }

    @Override
    public boolean supportsLobSortingAndFiltering() {
        return false;
    }

    @Override
    public boolean emulateEqualsByLike() {
        return true;
    }

    @Override
    public boolean useOrderByForPaging() {
        return true;
    }
}
