package com.ofk.bd.CourseActivityAdapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.ofk.bd.HelperClass.Section;
import com.ofk.bd.HelperClass.Video;
import com.ofk.bd.Interface.VideoLoadCallback;
import com.ofk.bd.R;
import com.ofk.bd.ViewModel.VideoFromListViewModel;

import java.util.ArrayList;
import java.util.List;

public class CourseSectionAdapter extends RecyclerView.Adapter<CourseSectionAdapter.CourseSectionListViewHolder> {

    private static final String TAG = "CourseSectionAdapter";

    private List<Section> sectionNameList;

    private List<List<Video>> sectionWiseVideo;

    private Activity courseActivity;

    private SectionVideoAdapter adapter;

    private VideoFromListViewModel videoFromListViewModel;

    private List<String> nodeList;

    public CourseSectionAdapter(Activity activity, List<Section> sectionNameList) {
        this.courseActivity = activity;
        this.sectionNameList = sectionNameList;

        videoFromListViewModel = ViewModelProviders.of((FragmentActivity) courseActivity).get(VideoFromListViewModel.class);
    }

    public void setNodeList(List<String> nodeList) {
        this.nodeList = nodeList;
/*
        getVideoList(new VideoLoadCallback() {
            @Override
            public void onLoadCallback(List<Video> list) {
                videoFromListViewModel.getMutableLiveData().setValue(list.get(0).getVideoURL());
            }
        });

 */
    }

    @NonNull
    @Override
    public CourseSectionAdapter.CourseSectionListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.video_section_layout, parent, false);
        return new CourseSectionListViewHolder(view, sectionNameList);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseSectionAdapter.CourseSectionListViewHolder holder, int position) {
        holder.sectionName.setText(sectionNameList.get(position).getSectionName());
        boolean isExpanded = sectionNameList.get(position).isExpanded();

        if(isExpanded){

            ArrayList<Video> videoList = new ArrayList<>();

            String currentSectionCode = sectionNameList.get(position).getSectionCode();

            String parentNode = nodeList.get(0) + " Section";
            String childNode = nodeList.get(1);

            Log.d(TAG, "getVideoList: " + currentSectionCode);

            DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Sub Section");
            Query query = db.child(parentNode).child(childNode).child("Video").child(currentSectionCode);
            query.keepSynced(true);

            query.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Video video = dataSnapshot.getValue(Video.class);
                    videoList.add(video);

                    adapter = new SectionVideoAdapter(videoList);

                    holder.sectionVideoListRecyclerView.setAdapter(adapter);
                    adapter.setOnItemClickListener(new SectionVideoAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position, View view) {
                            videoFromListViewModel.getMutableLiveData().setValue(videoList.get(position).getVideoURL());
                        }
                    });
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
        }

        holder.layout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return sectionNameList.size();
    }

    public class CourseSectionListViewHolder extends RecyclerView.ViewHolder {

        TextView sectionName;
        View dropDownArrowView;
        RecyclerView sectionVideoListRecyclerView;
        ConstraintLayout layout;

        public CourseSectionListViewHolder(@NonNull View itemView, final List<Section> sections) {
            super(itemView);

            sectionName = itemView.findViewById(R.id.sectionName);
            dropDownArrowView = itemView.findViewById(R.id.dropDownArrow);
            sectionVideoListRecyclerView = itemView.findViewById(R.id.sectionVideoListRecyclerView);
            layout = itemView.findViewById(R.id.constraintLayout);

            sectionName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Section section = sections.get(getAdapterPosition());
                    section.setExpanded(!section.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });

            dropDownArrowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Section section = sections.get(getAdapterPosition());
                    section.setExpanded(!section.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }

                // TODO bg thread
                public void getVideoList(VideoLoadCallback callback) {
                    List<Video> videoList = new ArrayList<>();
                    String currentSectionCode = sectionNameList.get(getAdapterPosition()).getSectionCode();
                    String parentNode = nodeList.get(0) + " Section";
                    String childNode = nodeList.get(1);
                    Log.d(TAG, "getVideoList: " + currentSectionCode);
                    DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Sub Section");
                    Query query = db.child(parentNode).child(childNode).child("Video").child(currentSectionCode);
                    query.keepSynced(true);
                    query.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            Video video = dataSnapshot.getValue(Video.class);
                            videoList.add(video);
                            callback.onLoadCallback(videoList);
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

                }
            });
        }
    }
/*
    // TODO bg thread
    public void getVideoList(VideoLoadCallback callback) {
        List<Video> videoList = new ArrayList<>();
        String currentSectionCode = sectionNameList.get(0).getSectionCode();
        String parentNode = nodeList.get(0) + " Section";
        String childNode = nodeList.get(1);
        Log.d(TAG, "getVideoList: " + currentSectionCode);
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Sub Section");
        Query query = db.child(parentNode).child(childNode).child("Video").child(currentSectionCode).limitToFirst(1);
        query.keepSynced(true);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Video video = dataSnapshot.getValue(Video.class);
                videoList.add(video);
                callback.onLoadCallback(videoList);
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

    }

 */
}
