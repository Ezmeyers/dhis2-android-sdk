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

package org.hisp.dhis.android.sdk.ui.rows;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.Editable;
import android.text.InputType;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.hisp.dhis.android.sdk.ui.R;
import org.hisp.dhis.android.sdk.ui.models.FormEntity;
import org.hisp.dhis.android.sdk.ui.models.edittext.FormEntityEditText;
import org.hisp.dhis.android.sdk.ui.views.AbsTextWatcher;

import static android.text.TextUtils.isEmpty;

public final class EditTextRowView implements RowView {

    public EditTextRowView() {
        // explicit empty constructor
    }

    @Override
    public ViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent) {
        return new EditTextRowViewHolder(inflater.inflate(
                R.layout.recyclerview_row_edittext, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, FormEntity formEntity) {
        FormEntityEditText entity = (FormEntityEditText) formEntity;
        ((EditTextRowViewHolder) viewHolder).update(entity);
    }

    private static class EditTextRowViewHolder extends RecyclerView.ViewHolder {

        /* in order to improve performance, we pre-fetch
        all hints from resources */
                /* in order to improve performance, we pre-fetch
        all hints from resources */
        SparseArray<String> hintCache;

        private final String enterText;
        private final String enterLongText;
        private final String enterNumber;
        private final String enterInteger;
        private final String enterPositiveInteger;
        private final String enterPositiveOrZeroInteger;
        private final String enterNegativeInteger;

        public final TextView textViewLabel;
        public final TextInputLayout textInputLayout;
        public final EditText editText;

        /* we use OnFocusChangeListener in order to hide
        hint from user when row is not focused */
        public final OnFocusChangeListener onFocusChangeListener;

        /* callback which is triggered on value changes */
        public final OnValueChangedListener onValueChangedListener;

        public EditTextRowViewHolder(View itemView) {
            super(itemView);

            Context context = itemView.getContext();

            hintCache = new SparseArray<>();
            hintCache.append(R.string.enter_text, context.getString(R.string.enter_text));
            hintCache.append(R.string.enter_long_text, context.getString(R.string.enter_long_text));
            hintCache.append(R.string.enter_number, context.getString(R.string.enter_number));
            // fetching hint strings

            enterNumber = context.getString(R.string.enter_number);
            enterInteger = context.getString(R.string.enter_integer);
            enterPositiveInteger = context.getString(R.string.enter_positive_integer);
            enterPositiveOrZeroInteger = context.getString(R.string.enter_positive_integer_or_zero);
            enterNegativeInteger = context.getString(R.string.enter_negative_integer);

            textViewLabel = (TextView) itemView
                    .findViewById(R.id.textview_row_label);
            textInputLayout = (TextInputLayout) itemView
                    .findViewById(R.id.edittext_row_textinputlayout);
            editText = (EditText) itemView
                    .findViewById(R.id.edittext_row_edittext);

            onFocusChangeListener = new OnFocusChangeListener(textInputLayout, editText);
            onValueChangedListener = new OnValueChangedListener();

            editText.setOnFocusChangeListener(onFocusChangeListener);
            editText.addTextChangedListener(onValueChangedListener);
        }

        public void update(FormEntityEditText entity) {
            // update callbacks with current entities
            onValueChangedListener.setDataEntity(entity);
            textViewLabel.setText(entity.getLabel());
            editText.setText(entity.getValue());
            editText.setEnabled(!entity.isLocked());

            // configure edittext according to entity
            configureView(entity);
        }

        private boolean configureView(FormEntityEditText dataEntityText) {

            String hint = isEmpty(dataEntityText.getHint()) ?
                    enterText : dataEntityText.getHint();

            return configure(hint, dataEntityText.getAndroidInputType(), dataEntityText.getMaxLines());

            switch (dataEntityText.getInputType()) {
                case TEXT: {
                    String hint = isEmpty(dataEntityText.getHint()) ?
                            enterText : dataEntityText.getHint();
                    return configure(hint, InputType.TYPE_CLASS_TEXT, SHORT_TEXT_LINE_COUNT);
                }
                case LONG_TEXT: {
                    String hint = isEmpty(dataEntityText.getHint()) ?
                            enterLongText : dataEntityText.getHint();
                    return configure(hint, InputType.TYPE_CLASS_TEXT, LONG_TEXT_LINE_COUNT);
                }
                case NUMBER: {
                    String hint = isEmpty(dataEntityText.getHint()) ?
                            enterNumber : dataEntityText.getHint();
                    return configure(hint,
                            InputType.TYPE_CLASS_NUMBER |
                                    InputType.TYPE_NUMBER_FLAG_DECIMAL |
                                    InputType.TYPE_NUMBER_FLAG_SIGNED, SHORT_TEXT_LINE_COUNT);
                }
                case INTEGER: {
                    String hint = isEmpty(dataEntityText.getHint()) ?
                            enterInteger : dataEntityText.getHint();
                    return configure(hint,
                            InputType.TYPE_CLASS_NUMBER |
                                    InputType.TYPE_NUMBER_FLAG_SIGNED, SHORT_TEXT_LINE_COUNT);
                }
                case INTEGER_NEGATIVE: {
                    String hint = isEmpty(dataEntityText.getHint()) ?
                            enterNegativeInteger : dataEntityText.getHint();
                    return configure(hint,
                            InputType.TYPE_CLASS_NUMBER |
                                    InputType.TYPE_NUMBER_FLAG_SIGNED, SHORT_TEXT_LINE_COUNT);
                }
                case INTEGER_ZERO_OR_POSITIVE: {
                    String hint = isEmpty(dataEntityText.getHint()) ?
                            enterPositiveOrZeroInteger : dataEntityText.getHint();
                    return configure(hint, InputType.TYPE_CLASS_NUMBER, SHORT_TEXT_LINE_COUNT);
                }
                case INTEGER_POSITIVE: {
                    String hint = isEmpty(dataEntityText.getHint()) ?
                            enterPositiveInteger : dataEntityText.getHint();
                    return configure(hint, InputType.TYPE_CLASS_NUMBER, SHORT_TEXT_LINE_COUNT);
                }
                default:
                    return false;
            }
        }

        private boolean configure(String hint, int inputType, int maxLines) {
            String textInputLayoutHint = isEmpty(editText.getText()) ? hint : null;

            onFocusChangeListener.setHint(hint);
            textInputLayout.setHint(textInputLayoutHint);

            editText.setInputType(inputType);
            editText.setMaxLines(maxLines);

            return true;
        }
    }

    private static class OnValueChangedListener extends AbsTextWatcher {
        private FormEntityEditText dataEntity;

        public void setDataEntity(FormEntityEditText dataEntity) {
            this.dataEntity = dataEntity;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (dataEntity != null) {
                dataEntity.setValue(editable.toString(), true);
            }
        }
    }

    private static class OnFocusChangeListener implements View.OnFocusChangeListener {
        private final TextInputLayout textInputLayout;
        private final EditText editText;
        private CharSequence hint;

        public OnFocusChangeListener(TextInputLayout inputLayout, EditText editText) {
            this.textInputLayout = inputLayout;
            this.editText = editText;
        }

        public void setHint(CharSequence hint) {
            this.hint = hint;
        }

        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if (hasFocus) {
                textInputLayout.setHint(hint);
            } else {
                if (!isEmpty(editText.getText().toString())) {
                    textInputLayout.setHint(null);
                }
            }
        }
    }
}
