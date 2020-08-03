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

    private List<List<Video>> sectionWiseVideo;

    private Activity courseActivity;

    private SectionVideoAdapter adapter;

    private VideoFromListViewModel videoFromListViewModel;

    public CourseSectionAdapter(Activity activity, List<Section> sectionNameList, List<List<Video>> sectionWiseVideo) {
        this.courseActivity = activity;
        this.sectionNameList = sectionNameList;
        this.sectionWiseVideo = sectionWiseVideo;

        videoFromListViewModel = ViewModelProviders.of((FragmentActivity) courseActivity).get(VideoFromListViewModel.class);
        videoFromListViewModel.getMutableLiveData().setValue(sectionWiseVideo.get(0).get(0).getVideoURL());
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

        List<Video> currentList = sectionWiseVideo.get(position);
        adapter = new SectionVideoAdapter(currentList);
        holder.sectionVideoListRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new SectionVideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                videoFromListViewModel.getMutableLiveData().setValue(currentList.get(position).getVideoURL());
            }
        });

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
            });

/*
            adapter = new SectionVideoAdapter(videoList);

            sectionVideoListRecyclerView.setAdapter(adapter);


 */
        }
    }
}
