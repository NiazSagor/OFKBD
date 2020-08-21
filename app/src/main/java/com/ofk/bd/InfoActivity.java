package com.ofk.bd;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.ofk.bd.Fragments.BottomSheet;
import com.ofk.bd.InfoActivityAdapter.InfoActivityViewPagerAdapter;
import com.ofk.bd.ViewModel.InfoActivityViewModel;
import com.ofk.bd.databinding.ActivityInfoBinding;

public class InfoActivity extends AppCompatActivity implements BottomSheet.BottomSheetListener {

    private ActivityInfoBinding binding;

    private InfoActivityViewModel activityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        activityViewModel = ViewModelProviders.of(this).get(InfoActivityViewModel.class);

        //binding.viewpager.setUserInputEnabled(false);
        binding.viewpager.setAdapter(new InfoActivityViewPagerAdapter(this));
    }

    @Override
    public void onButtonClick(String userEmail) {

    }
}