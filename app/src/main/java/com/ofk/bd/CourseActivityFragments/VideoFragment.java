package com.ofk.bd.CourseActivityFragments;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.developer.kalert.KAlertDialog;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ofk.bd.CourseActivityAdapter.CourseSectionAdapter;
import com.ofk.bd.HelperClass.Section;
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
        courseActivityViewModel = ViewModelProviders.of(getActivity()).get(CourseActivityViewModel.class);
    }

    private FragmentVideoBinding binding;

    private List<Section> sectionList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentVideoBinding.inflate(getLayoutInflater());

        courseActivityViewModel.getCombinedList().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                showAlertDialog("start");
                new GetData(strings).execute();
            }
        });
        return binding.getRoot();
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

            db.child(parentNode).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        if (!dataSnapshot.child(childNode).exists()) {
                            showAlertDialog("done");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            db.child(parentNode).child(childNode).child("Section").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    if (!dataSnapshot.exists()) {
                        showAlertDialog("done");
                        return;
                    }

                    Section section = dataSnapshot.getValue(Section.class);
                    sectionList.add(section);
                    CourseSectionAdapter adapter = new CourseSectionAdapter(getActivity(), sectionList);
                    adapter.setNodeList(myList);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showAlertDialog("end");
                            binding.sectionListRecyclerView.setAdapter(adapter);
                        }
                    }, 2300);
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