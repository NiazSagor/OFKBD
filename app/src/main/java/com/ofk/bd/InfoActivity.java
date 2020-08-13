package com.ofk.bd;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ofk.bd.Fragments.BottomSheet;
import com.ofk.bd.InfoActivityAdapter.InfoActivityViewPagerAdapter;
import com.ofk.bd.databinding.ActivityInfoBinding;

public class InfoActivity extends AppCompatActivity implements BottomSheet.BottomSheetListener {

    ActivityInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.viewpager.setAdapter(new InfoActivityViewPagerAdapter(this));

        /*
        binding.ageRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.ageRecyclerView.setAdapter(new AgeListAdapter());

        binding.avatarRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        binding.avatarRecyclerView.setAdapter(new AvatarListAdapter("choose_avatar"));

        binding.progressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheet bottomSheet = new BottomSheet();
                bottomSheet.show(getSupportFragmentManager(), "bottom");
            }
        });

         */
    }

    @Override
    public void onButtonClick(String userEmail) {
        if (!userEmail.equals("")) {
            // user has input the email address
        }
    }
}