/* Copyright 2016 Patrick Löwenstein
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. */

package com.patloew.navigationviewfragmentadaptersample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SampleFragment extends Fragment {

    private static final String KEY_TEXT = "text";
    private static final String KEY_INPUT = "input";

    public static SampleFragment newInstance(String text) {
        SampleFragment f = new SampleFragment();
        Bundle args = new Bundle();
        args.putString(KEY_TEXT, text);
        f.setArguments(args);
        return f;
    }

    @BindView(R.id.text_view)
    TextView textView;

    @BindView(R.id.edit_text)
    EditText editText;

    private String input = "";

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_INPUT, input);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sample, container, false);
        ButterKnife.bind(this, rootView);
        ((MainActivityView) getActivity()).activateDrawerLayout();

        if(savedInstanceState != null) {
            input = savedInstanceState.getString(KEY_INPUT);
        }

        textView.setText(getArguments().getString(KEY_TEXT));

        editText.setText(input);
        editText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override public void afterTextChanged(Editable editable) { }
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                input = charSequence.toString();
            }
        });

        return rootView;
    }
}
