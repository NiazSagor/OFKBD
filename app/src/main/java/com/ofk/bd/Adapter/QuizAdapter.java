package com.ofk.bd.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ofk.bd.R;

import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder> {

    private final List<String> quizTitleList;
    private final Context context;
    private OnItemClickListener mListener;


    public QuizAdapter(List<String> quizTitleList, Context context) {
        this.quizTitleList = quizTitleList;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    @NonNull
    @Override
    public QuizAdapter.QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.section_video_item_layout, parent, false);

        return new QuizViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizAdapter.QuizViewHolder holder, int position) {
        holder.quizTitle.setText(quizTitleList.get(position));
        holder.iconImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_quiz));
    }

    @Override
    public int getItemCount() {
        return quizTitleList.size();
    }

    public static class QuizViewHolder extends RecyclerView.ViewHolder {

        private final TextView quizTitle;
        private final ImageView iconImageView;

        public QuizViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            quizTitle = itemView.findViewById(R.id.videoTitle);
            iconImageView = itemView.findViewById(R.id.videoThumbNail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();

                    if (listener != null) {
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position, view);
                        }
                    }
                }
            });
        }
    }
}
