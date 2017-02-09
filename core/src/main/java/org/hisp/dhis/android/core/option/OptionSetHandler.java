/*
 * Copyright (c) 2017, University of Oslo
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
package org.hisp.dhis.android.core.option;

import static org.hisp.dhis.android.core.utils.Utils.isDeleted;

public class OptionSetHandler {
    private final OptionSetStore optionSetStore;

    public OptionSetHandler(OptionSetStore optionSetStore) {
        this.optionSetStore = optionSetStore;
    }

    public void handleOptionSet(OptionSet optionSet) {
        deleteOrPersistOptionSet(optionSet);
    }


    /**
     * In this call we will only insert or update option sets with uid's.
     * The options will be inserted/updated in the @see option.OptionSetCall#Call
     *
     * @param optionSet
     */
    private void deleteOrPersistOptionSet(OptionSet optionSet) {
        if (optionSet == null) {
            return;
        }

        if (isDeleted(optionSet)) {
            optionSetStore.delete(optionSet.uid());
        } else {
            int updatedRow = optionSetStore.update(optionSet.uid(), optionSet.code(), optionSet.name(),
                    optionSet.displayName(), optionSet.created(), optionSet.lastUpdated(), optionSet.version(),
                    optionSet.valueType(), optionSet.uid());

            if (updatedRow <= 0) {
                optionSetStore.insert(optionSet.uid(), optionSet.code(), optionSet.name(), optionSet.displayName(),
                        optionSet.created(), optionSet.lastUpdated(), optionSet.version(), optionSet.valueType());
            }
        }
    }
}