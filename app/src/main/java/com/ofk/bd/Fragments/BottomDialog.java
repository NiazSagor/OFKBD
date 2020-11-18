package com.ofk.bd.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ofk.bd.R;

public class BottomDialog extends BottomSheetDialogFragment {


    private BottomSheetListener mListener;

    Button interestedButton;
    Button notInterestedButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.bottom_sheet_dialog, container, false);


        interestedButton = view.findViewById(R.id.interestedButton);
        notInterestedButton = view.findViewById(R.id.notInterestedButton);
        interestedButton.setOnClickListener(listener);
        notInterestedButton.setOnClickListener(listener);

        TextView textView = view.findViewById(R.id.dialogTextView);
        textView.setOnClickListener(listener);

        ImageView closeSheet = view.findViewById(R.id.closeBottomSheet);
        closeSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }

    public interface BottomSheetListener {
        void onButtonClicked(int isInterested);
    }

    private final View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == notInterestedButton.getId()) {
                mListener.onButtonClicked(0);
            } else if (view.getId() == interestedButton.getId()) {
                mListener.onButtonClicked(1);
            } else {
                mListener.onButtonClicked(3);
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }
    }
}