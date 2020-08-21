package com.ofk.bd.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ofk.bd.R;

public class AgeListAdapter extends RecyclerView.Adapter<AgeListAdapter.AgeListViewHolder> {

    private String[] age = {"2", "3", "4", "5", "6", "7+"};

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class AgeListViewHolder extends RecyclerView.ViewHolder{

        TextView ageTextView;

        ViewFlipper viewFlipper;

        public AgeListViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            ageTextView = itemView.findViewById(R.id.ageTextView);

            viewFlipper = itemView.findViewById(R.id.viewFlipper);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    viewFlipper.showNext();

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

    @NonNull
    @Override
    public AgeListAdapter.AgeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.age_layout, parent, false);
        return new AgeListViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AgeListAdapter.AgeListViewHolder holder, int position) {
        holder.ageTextView.setText(age[position]);
    }

    @Override
    public int getItemCount() {
        return age.length;
    }

    public String getSelectedAge(int position){
        return age[position];
    }
}
