package com.ofk.bd.CourseActivityFragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ofk.bd.CourseActivityAdapter.CourseSectionAdapter;
import com.ofk.bd.HelperClass.Section;
import com.ofk.bd.HelperClass.Video;
import com.ofk.bd.R;
import com.ofk.bd.ViewModel.CourseActivityViewModel;
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

    private CourseActivityViewModel courseActivityViewModel;


    private String parentNode;
    private String childNode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        courseActivityViewModel = ViewModelProviders.of(getActivity()).get(CourseActivityViewModel.class);
    }

    private FragmentVideoBinding binding;

    private List<Section> sectionList;

    private List<List<Video>> sectionWiseVideoList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentVideoBinding.inflate(getLayoutInflater());

        courseActivityViewModel.getCombinedList().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                Log.d(TAG, "onChanged: " + strings.toString());
                new GetData(strings).execute();
            }
        });
        return binding.getRoot();
    }

    private void setUpList() {

        sectionList = new ArrayList<>();

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

    public class GetData extends AsyncTask<Void, Void, Void> {

        List<String> myList;
        String parentNode, childNode;

        public GetData(List<String> list) {
            this.myList = list;
            parentNode = myList.get(0) + " Section";
            childNode = myList.get(1);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            sectionList = new ArrayList<>();
            DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Sub Section");
            db.child(parentNode).child(childNode).child("Section").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    if (!dataSnapshot.exists()) {
                        Toast.makeText(getContext(), "No video found", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Section section = dataSnapshot.getValue(Section.class);
                    sectionList.add(section);
                    CourseSectionAdapter adapter = new CourseSectionAdapter(getActivity(), sectionList);
                    adapter.setNodeList(myList);
                    binding.sectionListRecyclerView.setAdapter(adapter);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            return null;
        }
    }
}