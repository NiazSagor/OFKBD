package com.ofk.bd.Fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.database.DataSnapshot;
import com.ofk.bd.Adapter.ActivitySliderAdapter;
import com.ofk.bd.Adapter.CourseSliderListAdapter;
import com.ofk.bd.HelperClass.Activity;
import com.ofk.bd.HelperClass.Course;
import com.ofk.bd.HelperClass.MyApp;
import com.ofk.bd.R;
import com.ofk.bd.Utility.AlertDialogUtility;
import com.ofk.bd.ViewModel.MainActivityViewModel;
import com.ofk.bd.databinding.FragmentMoreBinding;
import com.thefinestartist.finestwebview.FinestWebView;

import java.util.ArrayList;
import java.util.List;

public class MoreFragment extends Fragment {

    private static final String TAG = "MoreFragment";

    private MainActivityViewModel mainActivityViewModel;

    private FragmentMoreBinding binding;

    private int customPosition = 0;

    private static final int[] indicator = {R.drawable.dot, R.drawable.inactivedot};

    // activity pics adapter
    private ActivitySliderAdapter activityAdapter;

    public MoreFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivityViewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMoreBinding.inflate(getLayoutInflater());

        binding.activityViewPager.setClipToPadding(false);
        binding.activityViewPager.setPageTransformer(new MarginPageTransformer(40));

        binding.blogRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!mainActivityViewModel.getBlogListMutableLiveData().hasObservers()) {
            mainActivityViewModel.getBlogListMutableLiveData().observe(this, blogListObserver);
        }

        if (!mainActivityViewModel.getFieldWorkLiveData().hasObservers()) {
            mainActivityViewModel.getFieldWorkLiveData().observe(this, activityFieldWorkObserver);
        }
    }

    private final CourseSliderListAdapter.OnItemClickListener onItemClickListener = new CourseSliderListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position, View view) {
            if (!MyApp.IS_CONNECTED) {
                new AlertDialogUtility().showAlertDialog(getContext(), "noConnection");
                return;
            }

            if (position == 0) {
                new FinestWebView.Builder(getActivity()).show("https://ofkbd.com/inspirational/");
            } else if (position == 1) {
                new FinestWebView.Builder(getActivity()).show("https://ofkbd.com/story/");
            } else if (position == 2) {
                new FinestWebView.Builder(getActivity()).show("https://ofkbd.com/tips-and-tricks/");
            } else if (position == 3) {
                new FinestWebView.Builder(getActivity()).show("https://ofkbd.com/skill-development/");
            } else if (position == 4) {
                new FinestWebView.Builder(getActivity()).show("https://ofkbd.com/awarness/");
            } else if (position == 5) {
                new FinestWebView.Builder(getActivity()).show("https://ofkbd.com/english-article/");
            }
        }
    };

    private final androidx.lifecycle.Observer<DataSnapshot> activityFieldWorkObserver = new Observer<DataSnapshot>() {
        @Override
        public void onChanged(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {

                List<Activity> fieldWorkActivityList = new ArrayList<>();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    fieldWorkActivityList.add(ds.getValue(Activity.class));
                }

                activityAdapter = new ActivitySliderAdapter(fieldWorkActivityList);
                binding.activityViewPager.setAdapter(activityAdapter);
                binding.activityViewPager.registerOnPageChangeCallback(onPageChangeCallback);
            }
        }
    };

    private final Observer<List<Course>> blogListObserver = new Observer<List<Course>>() {
        @Override
        public void onChanged(List<Course> courses) {
            CourseSliderListAdapter adapter = new CourseSliderListAdapter(getContext(), courses, "blog");

            binding.blogRecyclerView.setAdapter(adapter);

            adapter.setOnItemClickListener(onItemClickListener);
        }
    };


    private final ViewPager2.OnPageChangeCallback onPageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            if (customPosition > activityAdapter.getItemCount() - 1) {
                customPosition = 0;
            }
            changeIndicator(position++);
        }
    };

    private void changeIndicator(int currentSlidePosition) {
        if (binding.dotLayout.getChildCount() > 0) {
            binding.dotLayout.removeAllViews();
        }

        ImageView[] dots = new ImageView[activityAdapter.getItemCount()];

        for (int i = 0; i < activityAdapter.getItemCount(); i++) {
            dots[i] = new ImageView(getContext());

            if (i == currentSlidePosition) {
                //dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.dot));

                dots[i].setImageResource(indicator[0]);
            } else {
                //dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.inactivedot));
                dots[i].setImageResource(indicator[1]);
            }

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            layoutParams.setMargins(4, 0, 4, 0);
            binding.dotLayout.addView(dots[i], layoutParams);
        }
    }
}