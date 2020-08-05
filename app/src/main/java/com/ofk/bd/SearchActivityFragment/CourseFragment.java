package com.ofk.bd.SearchActivityFragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ofk.bd.DisplayCourseActivityAdapter.CourseListAdapter;
import com.ofk.bd.HelperClass.DisplayCourse;
import com.ofk.bd.ViewModel.FirebaseSearchViewModel;
import com.ofk.bd.databinding.FragmentCourseBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseFragment extends Fragment {

    private static final String TAG = "CourseFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CourseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CourseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CourseFragment newInstance(String param1, String param2) {
        CourseFragment fragment = new CourseFragment();
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

    private FragmentCourseBinding binding;
    private FirebaseSearchViewModel viewModel;
    private RecyclerView courseRecyclerView;
    private CourseListAdapter adapter;
    private DatabaseReference db_ref_all_courses;
    private List<DisplayCourse> courses = new ArrayList<>();
    private String searchQuery;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCourseBinding.inflate(getLayoutInflater());
        viewModel = ViewModelProviders.of(getActivity()).get(FirebaseSearchViewModel.class);

        courseRecyclerView = binding.courseSearchResultRecyclerView;
        courseRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        viewModel.getMutableLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.d(TAG, "onChanged: " + s);
                searchQuery = s;
            }
        });

        db_ref_all_courses = FirebaseDatabase.getInstance().getReference().child("All Course");
        db_ref_all_courses.keepSynced(true);

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        db_ref_all_courses.addChildEventListener(mListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db_ref_all_courses.removeEventListener(mListener);
    }

    private ChildEventListener mListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            DisplayCourse course = dataSnapshot.getValue(DisplayCourse.class);

            if (course.getCourseTitle().toLowerCase().contains(searchQuery.toLowerCase())) {
                Log.d(TAG, "onChildAdded: ");
                courses.add(course);
                adapter = new CourseListAdapter(courses, "displayCourse");
                courseRecyclerView.setAdapter(adapter);
            }
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
    };
}