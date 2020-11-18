package com.ofk.bd.CourseActivityAdapter;

import android.app.Activity;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.ofk.bd.HelperClass.Section;
import com.ofk.bd.HelperClass.SectionVideo;
import com.ofk.bd.HelperClass.Video;
import com.ofk.bd.R;
import com.ofk.bd.ViewModel.VideoFromListViewModel;

import java.util.List;

public class CourseSectionAdapter extends RecyclerView.Adapter<CourseSectionAdapter.CourseSectionListViewHolder> {

    int switchNumber = 0;

    private AnimatedVectorDrawableCompat drawableCompat;
    private AnimatedVectorDrawable animatedVectorDrawable;

    private static final String TAG = "CourseSectionAdapter";

    private final Activity courseActivity;

    private SectionVideoAdapter adapter;

    private VideoFromListViewModel videoFromListViewModel;

    private final List<SectionVideo> sectionVideoList;

    public CourseSectionAdapter(Activity courseActivity, List<SectionVideo> sectionVideoList) {
        this.courseActivity = courseActivity;
        this.sectionVideoList = sectionVideoList;
        videoFromListViewModel = ViewModelProviders.of((FragmentActivity) courseActivity).get(VideoFromListViewModel.class);
    }

    @NonNull
    @Override
    public CourseSectionAdapter.CourseSectionListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.video_section_layout, parent, false);
        return new CourseSectionListViewHolder(view, sectionVideoList);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseSectionAdapter.CourseSectionListViewHolder holder, int position) {

        holder.sectionName.setText(sectionVideoList.get(position).getSectionName().getSectionName());
        boolean isExpanded = sectionVideoList.get(position).getSectionName().isExpanded();

        if (isExpanded) {
            List<Video> videoList = sectionVideoList.get(position).getVideos();
            adapter = new SectionVideoAdapter(videoList, "sectionVideo");
            holder.sectionVideoListRecyclerView.setAdapter(adapter);

            adapter.setOnItemClickListener(new SectionVideoAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, View view) {
                    videoFromListViewModel.getMutableLiveData().setValue(videoList.get(position).getVideoURL());
                }
            });
        }
        holder.layout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return sectionVideoList.size();
    }


    public class CourseSectionListViewHolder extends RecyclerView.ViewHolder {

        TextView sectionName;
        RecyclerView sectionVideoListRecyclerView;
        ConstraintLayout layout, expandLayout;

        public CourseSectionListViewHolder(@NonNull View itemView, final List<SectionVideo> sections) {
            super(itemView);

            sectionName = itemView.findViewById(R.id.sectionName);
            sectionVideoListRecyclerView = itemView.findViewById(R.id.sectionVideoListRecyclerView);
            layout = itemView.findViewById(R.id.constraintLayout);
            expandLayout = itemView.findViewById(R.id.expandList);

            expandLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Section section = sections.get(getAdapterPosition()).getSectionName();
                    section.setExpanded(!section.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}
