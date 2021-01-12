package com.ofk.bd.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ofk.bd.R;
import com.ofk.bd.Utility.DrawableUtility;
import com.ofk.bd.Utility.StringUtility;

public class AvatarListAdapter extends RecyclerView.Adapter<AvatarListAdapter.AvatarListViewHolder> {

    public static boolean isClickable = true;

    public static final int CHOOSE_AVATAR = 1;
    public static final int VIEW_BADGE = 2;

    private final String mSender;

    private final Context mContext;

    private OnItemClickListener mListener;

    private int currentBadgeIndex;

    public AvatarListAdapter(Context context, String mSender) {
        this.mSender = mSender;
        this.mContext = context;
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
                View view3 = inflater.inflate(R.layout.bottom_sheet_layout, parent, false);
                return new AvatarListViewHolder(view3, mListener, mSender);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull AvatarListAdapter.AvatarListViewHolder holder, int position) {

        if (mSender.equals("view_badge")) {
            holder.myBadgeImageView.setImageDrawable(DrawableUtility.getDrawable(mContext, position + currentBadgeIndex));
            holder.levelName.setText(StringUtility.getCurrentLevelName(position + currentBadgeIndex));
        } else {
            holder.avatarImageView.setImageDrawable(DrawableUtility.getAvatarDrawable(mContext, position));
        }
    }

    @Override
    public int getItemCount() {
        if (mSender.equals("view_badge")) {
            return 15 - currentBadgeIndex;
        }
        return 10;
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
