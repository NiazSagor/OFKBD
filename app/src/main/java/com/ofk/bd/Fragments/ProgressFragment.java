package com.ofk.bd.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ofk.bd.Adapter.ProgressListAdapter;
import com.ofk.bd.HelperClass.Progress;
import com.ofk.bd.R;
import com.ofk.bd.databinding.FragmentProgressBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProgressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProgressFragment extends Fragment {

    private int[] avatars = {R.drawable.dog, R.drawable.duck, R.drawable.fox, R.drawable.lion, R.drawable.lion, R.drawable.lion1, R.drawable.squirrel, R.drawable.duck};
    private String[] names = {"All", "Arts", "Robotics", "English", "Communication"};

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProgressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProgressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProgressFragment newInstance(String param1, String param2) {
        ProgressFragment fragment = new ProgressFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private FragmentProgressBinding binding;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private List<Progress> dummyProgress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProgressBinding.inflate(getLayoutInflater());

        createDummyProgress();

        binding.subjectProgressRecyclerView.setAdapter(new ProgressListAdapter(dummyProgress));

        return binding.getRoot();
    }

    private void createDummyProgress() {

        dummyProgress = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            dummyProgress.add(new Progress("Subject " + i, i * 10));
        }
    }
}