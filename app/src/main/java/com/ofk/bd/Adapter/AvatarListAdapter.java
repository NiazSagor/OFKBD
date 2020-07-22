package com.ofk.bd.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ofk.bd.R;

public class AvatarListAdapter extends RecyclerView.Adapter<AvatarListAdapter.AvatarListViewHolder> {

    public static final int CHOOSE_AVATAR = 1;
    public static final int VIEW_BADGE = 2;

    private String mSender;

    private OnItemClickListener mListener;

    private int[] avatars = {R.drawable.dog, R.drawable.duck, R.drawable.fox, R.drawable.lion, R.drawable.lion, R.drawable.lion1, R.drawable.squirrel, R.drawable.duck};

    public AvatarListAdapter(String mSender) {
        this.mSender = mSender;
    }

    @NonNull
    @Override
    public AvatarListAdapter.AvatarListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case 1:
                View view = inflater.inflate(R.layout.avatar_layout, parent, false);
                return new AvatarListViewHolder(view, mListener);

            case 2:
                View view2 = inflater.inflate(R.layout.mybadge_layout, parent, false);
                return new AvatarListViewHolder(view2, mListener);

            default:
                View view3 = inflater.inflate(R.layout.age_layout, parent, false);
                return new AvatarListViewHolder(view3, mListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull AvatarListAdapter.AvatarListViewHolder holder, int position) {

        if(mSender.equals("view_badge")){
            holder.myBadgeImageView.setImageResource(avatars[position]);
        }else{
            holder.avatarImageView.setImageResource(avatars[position]);
        }
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

        ImageView myBadgeImageView;

        public AvatarListViewHolder(@NonNull final View itemView, final OnItemClickListener listener) {
            super(itemView);

            flipper = itemView.findViewById(R.id.viewFlipper);

            avatarImageView = itemView.findViewById(R.id.avatarImageView);

            myBadgeImageView = itemView.findViewById(R.id.myBadgeImage);

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

    @Override
    public int getItemViewType(int position) {
        if (mSender.equals("choose_avatar")) {
            return CHOOSE_AVATAR;
        } else if (mSender.equals("view_badge")) {
            return VIEW_BADGE;
        }
        return -1;
    }
}
