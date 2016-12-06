package org.hisp.dhis.android.core.commons.database;

import android.content.ContentValues;
import android.database.Cursor;

import com.gabrielittner.auto.value.cursor.ColumnTypeAdapter;

import org.hisp.dhis.android.models.common.BaseIdentifiableObject;

import java.text.ParseException;
import java.util.Date;

public class DateColumnAdapter implements ColumnTypeAdapter<Date> {

    @Override
    public Date fromCursor(Cursor cursor, String columnName) {
        // infer index from column name
        int columnIndex = cursor.getColumnIndex(columnName);
        String sourceDate = cursor.getString(columnIndex);

        Date date = null;
        if (sourceDate != null) {
            try {
                date = BaseIdentifiableObject.DATE_FORMAT.parse(sourceDate);
            } catch (ParseException parseException) {
                // wrap checked exception into unchecked
                throw new RuntimeException(parseException);
            }
        }

        return date;
    }

    @Override
    public void toContentValues(ContentValues contentValues, String columnName, Date date) {
        if (date != null) {
            contentValues.put(columnName,
                    BaseIdentifiableObject.DATE_FORMAT.format(date));
        }
    }
}