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

import com.ofk.bd.Adapter.ActivitySliderAdapter;
import com.ofk.bd.Adapter.CourseSliderListAdapter;
import com.ofk.bd.HelperClass.Common;
import com.ofk.bd.HelperClass.Course;
import com.ofk.bd.R;
import com.ofk.bd.Utility.AlertDialogUtility;
import com.ofk.bd.ViewModel.MainActivityViewModel;
import com.ofk.bd.databinding.FragmentMoreBinding;
import com.thefinestartist.finestwebview.FinestWebView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoreFragment extends Fragment {

    private static final String TAG = "MoreFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MoreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MoreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MoreFragment newInstance(String param1, String param2) {
        MoreFragment fragment = new MoreFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private MainActivityViewModel mainActivityViewModel;

    private final CourseSliderListAdapter.OnItemClickListener onItemClickListener = new CourseSliderListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position, View view) {
            if (!isConnected()) {
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
    private final Observer<List<Course>> blogListObserver = new Observer<List<Course>>() {
        @Override
        public void onChanged(List<Course> courses) {
            CourseSliderListAdapter adapter = new CourseSliderListAdapter(getContext(), courses, "blog");

            binding.blogRecyclerView.setAdapter(adapter);

            adapter.setOnItemClickListener(onItemClickListener);
        }
    };

    private FragmentMoreBinding binding;

    private int customPosition = 0;

    private static final int[] indicator = {R.drawable.dot, R.drawable.inactivedot};

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
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();

        binding.activityViewPager.setAdapter(activityAdapter);
        changeIndicator(0);

        if (!mainActivityViewModel.getBlogListMutableLiveData().hasObservers()) {
            mainActivityViewModel.getBlogListMutableLiveData().observe(this, blogListObserver);
        }
    }

    // activity pics adapter
    private ActivitySliderAdapter activityAdapter;

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mainActivityViewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);

        activityAdapter = new ActivitySliderAdapter(Common.fieldActivityList);
    }

    @Override
    public void onResume() {
        super.onResume();

        binding.activityViewPager.registerOnPageChangeCallback(onPageChangeCallback);
    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}