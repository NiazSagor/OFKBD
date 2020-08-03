package com.ofk.bd.CourseActivityFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.ofk.bd.CourseActivityAdapter.CourseSectionAdapter;
import com.ofk.bd.HelperClass.Section;
import com.ofk.bd.HelperClass.Video;
import com.ofk.bd.ViewModel.VideoFromListViewModel;
import com.ofk.bd.databinding.FragmentVideoBinding;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private FragmentVideoBinding binding;
    private List<Section> sectionList;
//    private List<Video> videoList;

    private List<List<Video>> sectionWiseVideoList;

    private VideoFromListViewModel videoFromListViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentVideoBinding.inflate(getLayoutInflater());

        setUpList();

        binding.sectionListRecyclerView.setAdapter(new CourseSectionAdapter(getActivity(), sectionList, sectionWiseVideoList));

        return binding.getRoot();
    }

    private void setUpList() {

        sectionList = new ArrayList<>();

        //videoList = new ArrayList<>();

        sectionWiseVideoList = new ArrayList<>();

        sectionList.add(new Section("প্রথম অধ্যায়ঃ বিভিন্ন ধরণের ফল (পার্ট ১)"));
        sectionList.add(new Section("দ্বিতীয় অধ্যায়ঃ বিভিন্ন ধরণের ফল (পার্ট ২)"));
        sectionList.add(new Section("তৃতীয় অধ্যায়ঃ বিভিন্ন ধরণের ফুল"));


        List<Video> list1 = new ArrayList<>();
        list1.add(new Video("", "How To Draw Apple", "Zf6lflubBNA"));
        list1.add(new Video("", "How to Draw Banana", "Fmcwap9uwLI"));
        list1.add(new Video("", "How to Draw Mango", "P0ysy7Bwjy0"));

        List<Video> list2 = new ArrayList<>();

        list2.add(new Video("", "How to Draw Papaya", "hnVy59D6vLE"));
        list2.add(new Video("", "How to Draw Pineapple", "LryXdfWTKX4"));

        List<Video> list3 = new ArrayList<>();

        list3.add(new Video("", "How To Draw China Rose", "G4CCpvW6wJM"));

        sectionWiseVideoList.add(list1);
        sectionWiseVideoList.add(list2);
        sectionWiseVideoList.add(list3);
    }
}