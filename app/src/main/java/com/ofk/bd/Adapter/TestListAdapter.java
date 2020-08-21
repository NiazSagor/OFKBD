package com.ofk.bd.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ofk.bd.HelperClass.DisplayCourse;
import com.ofk.bd.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TestListAdapter extends RecyclerView.Adapter<TestListAdapter.TestViewHolder> {

    private List<DisplayCourse> courseSet;

    public TestListAdapter(Set<DisplayCourse> courseSet) {
        this.courseSet = new ArrayList<>(courseSet);
    }

    @NonNull
    @Override
    public TestListAdapter.TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view1 = inflater.inflate(R.layout.recom_course_home_layout, parent, false);
        return new TestViewHolder(view1);
    }

    @Override
    public void onBindViewHolder(@NonNull TestListAdapter.TestViewHolder holder, int position) {
        DisplayCourse current = courseSet.get(position);
        holder.title.setText(current.getCourseTitle());
    }

    @Override
    public int getItemCount() {
        return courseSet.size();
    }

    public static class TestViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public TestViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.courseTitle);
        }
    }
}
