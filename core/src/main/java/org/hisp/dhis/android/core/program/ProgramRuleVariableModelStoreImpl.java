package org.hisp.dhis.android.core.program;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.hisp.dhis.android.core.common.BaseIdentifiableObject;
import org.hisp.dhis.android.core.data.database.DbOpenHelper.Tables;
import org.hisp.dhis.android.core.program.ProgramRuleVariableContract.Columns;

import java.util.Date;

import static org.hisp.dhis.android.core.common.StoreUtils.sqLiteBind;

public class ProgramRuleVariableModelStoreImpl implements ProgramRuleVariableModelStore {

    private static final String INSERT_STATEMENT = "INSERT INTO " + Tables.PROGRAM_RULE_VARIABLE + " (" +
            Columns.UID + ", " +
            Columns.CODE + ", " +
            Columns.NAME + ", " +
            Columns.DISPLAY_NAME + ", " +
            Columns.CREATED + ", " +
            Columns.LAST_UPDATED + ", " +
            Columns.USE_CODE_FOR_OPTION_SET + ", " +
            Columns.PROGRAM + ", " +
            Columns.PROGRAM_STAGE + ", " +
            Columns.DATA_ELEMENT + ", " +
            Columns.TRACKED_ENTITY_ATTRIBUTE + ", " +
            Columns.PROGRAM_RULE_VARIABLE_SOURCE_TYPE + ") " +
            "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    private final SQLiteStatement sqLiteStatement;

    public ProgramRuleVariableModelStoreImpl(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteStatement = sqLiteDatabase.compileStatement(INSERT_STATEMENT);
    }

    @Override
    public long insert(@NonNull String uid, @Nullable String code, @NonNull String name,
                       @NonNull String displayName, @NonNull Date created,
                       @NonNull Date lastUpdated, @Nullable Boolean useCodeForOptionSet,
                       @NonNull String program, @Nullable String programStage,
                       @Nullable String dataElement, @Nullable String trackedEntityAttribute,
                       @Nullable ProgramRuleVariableSourceType programRuleVariableSourceType) {
        sqLiteStatement.clearBindings();

        sqLiteBind(sqLiteStatement, 1, uid);
        sqLiteBind(sqLiteStatement, 2, code);
        sqLiteBind(sqLiteStatement, 3, name);
        sqLiteBind(sqLiteStatement, 4, displayName);
        sqLiteBind(sqLiteStatement, 5, created);
        sqLiteBind(sqLiteStatement, 6, lastUpdated);
        sqLiteBind(sqLiteStatement, 7, useCodeForOptionSet);
        sqLiteBind(sqLiteStatement, 8, program);
        sqLiteBind(sqLiteStatement, 9, programStage);
        sqLiteBind(sqLiteStatement, 10, dataElement);
        sqLiteBind(sqLiteStatement, 11, trackedEntityAttribute);
        sqLiteBind(sqLiteStatement, 12, programRuleVariableSourceType.name());

        return sqLiteStatement.executeInsert();
    }

    @Override
    public void close() {
        sqLiteStatement.close();
    }
}
