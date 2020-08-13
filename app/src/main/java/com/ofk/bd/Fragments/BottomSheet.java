package com.ofk.bd.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.ofk.bd.R;

public class BottomSheet extends BottomSheetDialogFragment {

    private static final String TAG = "BottomSheet";

    private BottomSheetListener listener;

    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_sheet_layout, container, false);

        Button addButton = view.findViewById(R.id.addEmailButton);

        TextInputEditText editText = view.findViewById(R.id.emailEditText);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    editText.setError("Please input a valid email address");
                } else {
                    if (!charSequence.toString().matches(emailPattern)) {
                        Log.d(TAG, "onTextChanged: not valid");
                        editText.setError("Please input a valid email address");
                    } else {
                        addButton.setEnabled(true);
                        Log.d(TAG, "onTextChanged: valid email");
                        listener.onButtonClick(charSequence.toString());
                        // dismiss();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onButtonClick(editText.getText().toString());
                dismiss();
            }
        });

        Button cancelButton = view.findViewById(R.id.cancelEmailButton);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onButtonClick("");
                dismiss();
            }
        });

        return view;
    }

    public interface BottomSheetListener {
        void onButtonClick(String userEmail);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }
    }
}
