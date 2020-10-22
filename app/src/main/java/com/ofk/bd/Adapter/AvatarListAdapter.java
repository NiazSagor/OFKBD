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

public class AvatarListAdapter extends RecyclerView.Adapter<AvatarListAdapter.AvatarListViewHolder> {

    public static boolean isClickable = true;

    public static final int CHOOSE_AVATAR = 1;
    public static final int VIEW_BADGE = 2;

    private final String mSender;

    private OnItemClickListener mListener;

    private int currentBadgeIndex;

    private static final int[] avatars = {R.drawable.ic_dog, R.drawable.ic_duck, R.drawable.ic_fox, R.drawable.ic_lion, R.drawable.ic_cat, R.drawable.ic_tiger, R.drawable.ic_squirrel, R.drawable.ic_giraffe, R.drawable.ic_elephant, R.drawable.ic_parrot};

    private static final int[] badge_icons = {R.drawable.apprentice_1, R.drawable.apprentice_2, R.drawable.apprentice_3,
            R.drawable.journeyman_1, R.drawable.journeyman_2, R.drawable.journeyman_3,
            R.drawable.master_1, R.drawable.master_2, R.drawable.master_3,
            R.drawable.grand_master_1, R.drawable.grand_master_2, R.drawable.grand_master_3,
            R.drawable.super_kids_1, R.drawable.super_kids_2, R.drawable.super_kids_3};

    private static final String[] level_names = {"Apprentice 1", "Apprentice 2", "Apprentice 3",
            "Journeyman 1", "Journeyman 2", "Journeyman 3",
            "Master 1", "Master 2", "Master 3",
            "Grand Master 1", "Grand Master 2", "Grand Master 3",
            "Super Kids 1", "Super Kids 2", "Super Kids 3"};

    public AvatarListAdapter(String mSender) {
        this.mSender = mSender;
    }

    public void setCurrentBadgeIndex(int currentBadgeIndex) {
        this.currentBadgeIndex = currentBadgeIndex + 1;
    }

    @NonNull
    @Override
    public AvatarListAdapter.AvatarListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case 1:
                View view = inflater.inflate(R.layout.avatar_layout, parent, false);
                return new AvatarListViewHolder(view, mListener, mSender);

            case 2:
                View view2 = inflater.inflate(R.layout.mybadge_layout, parent, false);
                return new AvatarListViewHolder(view2, mListener, mSender);

            default:
                // not needed
                View view3 = inflater.inflate(R.layout.age_layout, parent, false);
                return new AvatarListViewHolder(view3, mListener, mSender);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull AvatarListAdapter.AvatarListViewHolder holder, int position) {

        if (mSender.equals("view_badge")) {
            holder.myBadgeImageView.setImageResource(badge_icons[position + currentBadgeIndex]);
            holder.levelName.setText(level_names[position + currentBadgeIndex]);
        } else {
            holder.avatarImageView.setImageResource(avatars[position]);
        }
    }

    @Override
    public int getItemCount() {
        if (mSender.equals("view_badge")) {
            return 15 - currentBadgeIndex;
        }
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

        TextView levelName;

        public AvatarListViewHolder(@NonNull final View itemView, final OnItemClickListener listener, String sender) {
            super(itemView);

            flipper = itemView.findViewById(R.id.viewFlipper);

            avatarImageView = itemView.findViewById(R.id.avatarImageView);

            myBadgeImageView = itemView.findViewById(R.id.myBadgeImage);

            levelName = itemView.findViewById(R.id.levelName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();

                    if (!sender.equals("view_badge") && isClickable) {
                        flipper.showNext();
                    }
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
