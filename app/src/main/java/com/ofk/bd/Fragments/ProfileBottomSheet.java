package com.ofk.bd.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ofk.bd.Adapter.ProfileClassGridAdapter;
import com.ofk.bd.Interface.DisplayCourseLoadCallback;
import com.ofk.bd.R;
import com.ofk.bd.Utility.StringUtility;

import java.util.Calendar;

public class ProfileBottomSheet extends BottomSheetDialogFragment {

    private static final String TAG = "ProfileBottomSheet";

    private BottomSheetListener mListener;

    public interface BottomSheetListener {
        void onButtonClicked(String text);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_bottom_sheet, container, false);

        ImageView closeSheet = view.findViewById(R.id.closeBottomSheet);
        closeSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        Button setButton = view.findViewById(R.id.setButton);

        switch (getArguments().getString("type")) {
            case "date":

                DatePicker datePicker = view.findViewById(R.id.datePicker);

                datePicker.setVisibility(View.VISIBLE);

                final Calendar c = Calendar.getInstance();

                datePicker.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                        setButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mListener.onButtonClicked(StringUtility.getFormattedDob(i, i1, i2));
                                dismiss();
                            }
                        });
                    }
                });
                break;
            case "gender": {

                GridView genderGridView = view.findViewById(R.id.classGridView);

                genderGridView.setVisibility(View.VISIBLE);

                ProfileClassGridAdapter adapter = new ProfileClassGridAdapter(
                        getContext(), StringUtility.getGender(), "gender"
                );

                genderGridView.setAdapter(adapter);

                adapter.setOnItemClickListener(new ProfileClassGridAdapter.OnItemClickListener() {
                    @Override
                    public void onClickListener(int position) {
                        setButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mListener.onButtonClicked(StringUtility.getGender()[position]);
                                dismiss();
                            }
                        });
                    }
                });

                break;
            }
            case "class": {
                GridView classGridView = view.findViewById(R.id.classGridView);

                classGridView.setVisibility(View.VISIBLE);

                ProfileClassGridAdapter adapter = new ProfileClassGridAdapter(getContext(), StringUtility.getClasses(), "class");

                classGridView.setAdapter(adapter);

                adapter.setOnItemClickListener(new ProfileClassGridAdapter.OnItemClickListener() {
                    @Override
                    public void onClickListener(int position) {
                        setButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mListener.onButtonClicked(StringUtility.getClasses()[position]);
                                dismiss();
                            }
                        });
                    }
                });
                break;
            }
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (ProfileBottomSheet.BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }
    }
}
