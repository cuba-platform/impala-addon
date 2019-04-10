package com.haulmont.addon.impala.core;

import org.eclipse.persistence.exceptions.ValidationException;
import org.eclipse.persistence.internal.databaseaccess.DatabaseCall;
import org.eclipse.persistence.internal.expressions.ExpressionSQLPrinter;
import org.eclipse.persistence.internal.expressions.SQLSelectStatement;
import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.platform.database.DatabasePlatform;
import org.eclipse.persistence.queries.ValueReadQuery;

import java.io.Writer;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ImpalaSQLPlatform extends DatabasePlatform {

    protected static final int KUDU_MAX_STRING_LENGTH = 510;

    @Override
    protected void initializePlatformOperators() {
        super.initializePlatformOperators();
    }

    @Override
    public boolean supportsUniqueColumns() {
        return false;
    }

    @Override
    public boolean supportsIdentity() {
        return false;
    }

    @Override
    public ValueReadQuery buildSelectQueryForIdentity() {
        throw new UnsupportedOperationException("Impala doesn't support identity");
    }

    @Override
    public void printFieldIdentityClause(Writer writer) throws ValidationException {
        throw new UnsupportedOperationException("Impala doesn't support identity");
    }

    /**
     * JDBC escape syntax for outer joins is not supported (not required).
     */
    @Override
    public boolean shouldUseJDBCOuterJoinSyntax() {
        return false;
    }


    @Override
    public boolean supportsSequenceObjects() {
        return false;
    }

    @Override
    public boolean isAlterSequenceObjectSupported() {
        return false;
    }

    @Override
    public ValueReadQuery buildSelectQueryForSequenceObject(String seqName, Integer size) {
        throw new UnsupportedOperationException("Impala doesn't support sequences");
    }


    @Override
    public int computeMaxRowsForSQL(int firstResultIndex, int maxResults) {
        return maxResults - ((firstResultIndex >= 0) ? firstResultIndex : 0);
    }

    /**
     * Print the pagination SQL using Impala syntax "SELECT ... FROM ... LIMIT {@literal <max>} OFFSET {@literal <max>}".
     */
    @Override
    public void printSQLSelectStatement(DatabaseCall call, ExpressionSQLPrinter printer, SQLSelectStatement statement) {
        int max = 0;
        int firstRow = 0;

        if (statement.getQuery() != null) {
            max = statement.getQuery().getMaxRows();
            firstRow = statement.getQuery().getFirstResult();
        }

        statement.setUseUniqueFieldAliases(true);
        call.setFields(statement.printSQL(printer));

        if (max > 0) {
            printer.printString(" LIMIT ");
            printer.printParameter(DatabaseCall.MAXROW_FIELD);
        }

        if (firstRow > 0) {
            printer.printString(" OFFSET ");
            printer.printParameter(DatabaseCall.FIRSTRESULT_FIELD);
        }

        call.setIgnoreFirstRowSetting(true);
        call.setIgnoreMaxResultsSetting(true);
    }

    @Override
    public void setParameterValueInDatabaseCall(Object parameter, PreparedStatement statement, int index, AbstractSession session) throws SQLException {
        if (parameter instanceof String) {
            if (((String) parameter).length() >= KUDU_MAX_STRING_LENGTH) {
                statement.setObject(index, parameter);
            } else {
                super.setParameterValueInDatabaseCall(parameter, statement, index, session);
            }
        } else {
            super.setParameterValueInDatabaseCall(parameter, statement, index, session);
        }
    }

    @Override
    public ValueReadQuery getTimestampQuery() {
        //Impala doesn't support timestamp query
        return null;
    }

    @Override
    public Writer buildSequenceObjectCreationWriter(Writer writer, String fullSeqName, int increment, int start) {
        throw new UnsupportedOperationException("Impala doesn't support sequences");
    }
}
