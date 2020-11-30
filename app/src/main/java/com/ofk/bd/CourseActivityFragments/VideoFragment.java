package com.ofk.bd.CourseActivityFragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.developer.kalert.KAlertDialog;
import com.ofk.bd.CourseActivityAdapter.CourseSectionAdapter;
import com.ofk.bd.HelperClass.Common;
import com.ofk.bd.ViewModel.VideoFromListViewModel;
import com.ofk.bd.databinding.FragmentVideoBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VideoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoFragment extends Fragment {

    private static final String TAG = "VideoFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VideoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VideoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VideoFragment newInstance(String param1, String param2) {
        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private VideoFromListViewModel videoFromListViewModel;
    private KAlertDialog pDialog;
    private Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        handler = new Handler();
        videoFromListViewModel = ViewModelProviders.of(getActivity()).get(VideoFromListViewModel.class);
    }

    private FragmentVideoBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentVideoBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (Common.sectionVideoList != null && Common.sectionVideoList.size() != 0) {
            Log.d(TAG, "onViewCreated: section");
            videoFromListViewModel.getMutableLiveData().setValue(Common.sectionVideoList.get(0).getVideos().get(0).getVideoURL());
            CourseSectionAdapter adapter = new CourseSectionAdapter(getActivity(), Common.sectionVideoList);
            binding.sectionListRecyclerView.setAdapter(adapter);
        }

        if (Common.videoId != null && !Common.videoId.equals("")) {
            videoFromListViewModel.getMutableLiveData().setValue(Common.videoId);
            Log.d(TAG, "onViewCreated: " + Common.videoId);
            //showAlertDialog("done");
        }
    }

    private void showAlertDialog(String command) {
        switch (command) {
            case "start":
                pDialog = new KAlertDialog(getActivity(), KAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#00c1c3"));
                pDialog.setTitleText("লোড হচ্ছে");
                pDialog.setCancelable(false);
                pDialog.show();
                break;
            case "end":
                pDialog.dismissWithAnimation();
                break;
            case "done":
                pDialog.dismiss();
                pDialog = new KAlertDialog(getActivity(), KAlertDialog.ERROR_TYPE);
                pDialog.setTitleText("ভিডিও পাওয়া যায় নি")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                            @Override
                            public void onClick(KAlertDialog kAlertDialog) {
                                pDialog.dismissWithAnimation();
                            }
                        })
                        .show();
                break;
        }
    }
}