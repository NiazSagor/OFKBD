package com.ofk.bd.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ofk.bd.R;

public class AvatarListAdapter extends RecyclerView.Adapter<AvatarListAdapter.AvatarListViewHolder> {

    private OnItemClickListener mListener;

    private int[] avatars = {R.drawable.dog, R.drawable.duck, R.drawable.fox, R.drawable.lion, R.drawable.lion, R.drawable.lion1, R.drawable.squirrel, R.drawable.duck};

    @NonNull
    @Override
    public AvatarListAdapter.AvatarListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.avatar_layout, parent, false);
        return new AvatarListViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AvatarListAdapter.AvatarListViewHolder holder, int position) {
        holder.avatarImageView.setImageResource(avatars[position]);
    }

    @Override
    public int getItemCount() {
        return avatars.length;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class AvatarListViewHolder extends RecyclerView.ViewHolder {

        ImageView avatarImageView;

        ViewFlipper flipper;

        public AvatarListViewHolder(@NonNull final View itemView, final OnItemClickListener listener) {
            super(itemView);

            flipper = itemView.findViewById(R.id.viewFlipper);

            avatarImageView = itemView.findViewById(R.id.avatarImageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    flipper.showNext();

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
