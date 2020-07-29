package com.ofk.bd.CourseActivityAdapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.ofk.bd.HelperClass.Section;
import com.ofk.bd.HelperClass.Video;
import com.ofk.bd.R;
import com.ofk.bd.ViewModel.VideoFromListViewModel;

import java.util.List;

public class CourseSectionAdapter extends RecyclerView.Adapter<CourseSectionAdapter.CourseSectionListViewHolder> {

    private static final String TAG = "CourseSectionAdapter";

    private List<Section> sectionNameList;

    private List<Video> videoList;

    private Activity courseActivity;
    
    private VideoFromListViewModel videoFromListViewModel;

    public CourseSectionAdapter(Activity activity, List<Section> sectionNameList, List<Video> videoList) {
        this.courseActivity = activity;
        this.sectionNameList = sectionNameList;
        this.videoList = videoList;
        
        videoFromListViewModel = ViewModelProviders.of((FragmentActivity) courseActivity).get(VideoFromListViewModel.class);
    }

    @NonNull
    @Override
    public CourseSectionAdapter.CourseSectionListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.video_section_layout, parent, false);
        return new CourseSectionListViewHolder(view, videoList, sectionNameList);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseSectionAdapter.CourseSectionListViewHolder holder, int position) {
        holder.sectionName.setText(sectionNameList.get(position).getSectionName());
        boolean isExpanded = sectionNameList.get(position).isExpanded();
        holder.layout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return sectionNameList.size();
    }

    public class CourseSectionListViewHolder extends RecyclerView.ViewHolder {

        private SectionVideoAdapter adapter;

        TextView sectionName;
        View dropDownArrowView;
        RecyclerView sectionVideoListRecyclerView;
        ConstraintLayout layout;

        public CourseSectionListViewHolder(@NonNull View itemView, final List<Video> videoList, final List<Section> sections) {
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
            });


            adapter = new SectionVideoAdapter(videoList);

            sectionVideoListRecyclerView.setAdapter(adapter);

            adapter.setOnItemClickListener(new SectionVideoAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, View view) {
                    //TODO somehow send the video id to the activity
                    videoFromListViewModel.getMutableLiveData().setValue(videoList.get(position).getVideoTitle());
                }
            });
        }
    }
}
