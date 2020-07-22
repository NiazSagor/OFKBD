package com.ofk.bd.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ofk.bd.R;

import java.util.List;

public class ProfileListAdapter extends RecyclerView.Adapter<ProfileListAdapter.ProfileListViewHolder> {

    private int[] avatars = {R.drawable.dog, R.drawable.duck, R.drawable.fox, R.drawable.lion, R.drawable.lion, R.drawable.lion1, R.drawable.squirrel, R.drawable.duck};

    private List<String> profileItems;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public ProfileListAdapter(List<String> list) {
        profileItems = list;
    }

    @NonNull
    @Override
    public ProfileListAdapter.ProfileListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.profile_item_layout, parent, false);
        return new ProfileListViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileListAdapter.ProfileListViewHolder holder, int position) {
        holder.profileItemTextView.setText(profileItems.get(position));
        holder.profileItemImage.setImageResource(avatars[position]);
    }

    @Override
    public int getItemCount() {
        return profileItems.size();
    }

    public static class ProfileListViewHolder extends RecyclerView.ViewHolder {

        ImageView profileItemImage;
        TextView profileItemTextView;

        public ProfileListViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            profileItemImage = itemView.findViewById(R.id.profileItemImage);
            profileItemTextView = itemView.findViewById(R.id.profileItemName);

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
