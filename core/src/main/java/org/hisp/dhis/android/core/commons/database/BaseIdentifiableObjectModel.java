/*
 * Copyright (c) 2016, University of Oslo
 *
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.hisp.dhis.android.core.commons.database;

import android.support.annotation.Nullable;

import com.gabrielittner.auto.value.cursor.ColumnAdapter;
import com.gabrielittner.auto.value.cursor.ColumnName;

import org.hisp.dhis.android.models.common.IdentifiableObject;

import java.util.Date;

public abstract class BaseIdentifiableObjectModel extends BaseModel implements IdentifiableObject {
    private static final int UID_LENGTH = 11;

    @Override
    @ColumnName(BaseIdentifiableObjectContract.Columns.UID)
    public abstract String uid();

    @Override
    @Nullable
    @ColumnName(BaseIdentifiableObjectContract.Columns.CODE)
    public abstract String code();

    @Override
    @Nullable
    @ColumnName(BaseIdentifiableObjectContract.Columns.NAME)
    public abstract String name();

    @Override
    @Nullable
    @ColumnName(BaseIdentifiableObjectContract.Columns.DISPLAY_NAME)
    public abstract String displayName();

    @Override
    @Nullable
    @ColumnName(BaseIdentifiableObjectContract.Columns.CREATED)
    @ColumnAdapter(DateColumnAdapter.class)
    public abstract Date created();

    @Override
    @Nullable
    @ColumnName(BaseIdentifiableObjectContract.Columns.LAST_UPDATED)
    @ColumnAdapter(DateColumnAdapter.class)
    public abstract Date lastUpdated();

    @Override
    public boolean isValid() {
        // check if properties are null or not
        if (created() == null || lastUpdated() == null) {
            return false;
        }

        // check uid length which must be 11 characters long
        if (uid().length() != UID_LENGTH) {
            return false;
        }

        return true;
    }

    protected static abstract class Builder<T extends Builder> extends BaseModel.Builder<T> {
        public abstract T uid(String uid);

        public abstract T code(@Nullable String code);

        public abstract T name(@Nullable String name);

        public abstract T displayName(@Nullable String displayName);

        public abstract T created(@Nullable Date created);

        public abstract T lastUpdated(@Nullable Date lastUpdated);
    }
}