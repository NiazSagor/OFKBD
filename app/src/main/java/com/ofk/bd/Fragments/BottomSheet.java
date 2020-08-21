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

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editText.getText().toString().isEmpty()) {
                    editText.setError("ইমেইল এড্রেস দেয়া হয় নি");
                } else {
                    String email = editText.getText().toString().trim();
                    if (email.matches(emailPattern)) {
                        listener.onButtonClick(email);
                        dismiss();
                    } else {
                        editText.setError("ইমেইল এড্রেসটি সঠিক হয় নি");
                    }
                }
            }
        });

        Button cancelButton = view.findViewById(R.id.cancelEmailButton);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onButtonClick(null);
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
