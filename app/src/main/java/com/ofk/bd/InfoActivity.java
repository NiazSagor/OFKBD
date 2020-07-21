package com.ofk.bd;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ofk.bd.Adapter.AgeListAdapter;
import com.ofk.bd.Adapter.AvatarListAdapter;
import com.ofk.bd.databinding.ActivityInfoBinding;

public class InfoActivity extends AppCompatActivity {

    ActivityInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.ageRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.ageRecyclerView.setAdapter(new AgeListAdapter());

        binding.avatarRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        binding.avatarRecyclerView.setAdapter(new AvatarListAdapter());
    }

    private void changeFontFace(){
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/kalpurush_ANSI.ttf");
        binding.yourNameEditText.setTypeface(typeface);
        binding.yourAge.setTypeface(typeface);
        binding.yourAvatar.setTypeface(typeface);
    }
}