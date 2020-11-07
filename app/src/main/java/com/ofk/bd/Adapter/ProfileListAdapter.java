package com.ofk.bd.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ofk.bd.R;
import com.ofk.bd.Utility.DrawableUtility;

import java.util.List;

public class ProfileListAdapter extends RecyclerView.Adapter<ProfileListAdapter.ProfileListViewHolder> {

    private static final String TAG = "ProfileListAdapter";

    private final List<String> profileItems;

    private final Context mContext;

    private OnItemClickListener mListener;
    private OnItemEditListener mEditListener;

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }

    public interface OnItemEditListener {
        void onItemClick(int position, View view, String text);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public void setOnItemEditListener(OnItemEditListener listener) {
        mEditListener = listener;
    }

    public ProfileListAdapter(List<String> list, Context mContext) {
        this.profileItems = list;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ProfileListAdapter.ProfileListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.profile_item_layout, parent, false);
        return new ProfileListViewHolder(view, mListener, mEditListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileListAdapter.ProfileListViewHolder holder, int position) {

        if (position == getItemCount() - 1) {
            holder.profileItemTextView.setTextColor(Color.RED);
            holder.profileItemTextView.setTypeface(null, Typeface.BOLD);
            holder.profileItemTextView.setText(profileItems.get(position));
        } else if (position == 2 && profileItems.get(2).equals("")) {
            holder.profileItemTextView.setText("Add your email");
            holder.editImageView.setVisibility(View.VISIBLE);
        } else if (position == 3 && profileItems.get(3).equals("")) {
            holder.profileItemTextView.setText("Add your class");
            holder.editImageView.setVisibility(View.VISIBLE);
        } else if (position == 4 && profileItems.get(4).equals("")) {
            holder.profileItemTextView.setText("Add your institute");
            holder.editImageView.setVisibility(View.VISIBLE);
        } else if (position == 5 && profileItems.get(5).equals("")) {
            holder.profileItemTextView.setText("Add your date of birth");
            holder.editImageView.setVisibility(View.VISIBLE);
        } else if (position == 6 && profileItems.get(6).equals("")) {
            holder.profileItemTextView.setText("Add your gender");
            holder.editImageView.setVisibility(View.VISIBLE);
        } else {
            holder.profileItemTextView.setText(profileItems.get(position));
        }
        holder.profileItemImage.setImageDrawable(DrawableUtility.getProfileItemDrawable(mContext, position));
    }

    @Override
    public int getItemCount() {
        return profileItems.size();
    }

    public static class ProfileListViewHolder extends RecyclerView.ViewHolder {

        ImageView profileItemImage;
        TextView profileItemTextView;

        EditText editText;
        ImageView editImageView;

        public ProfileListViewHolder(@NonNull View itemView, final OnItemClickListener listener, final OnItemEditListener editListener) {
            super(itemView);

            profileItemImage = itemView.findViewById(R.id.profileItemImage);
            profileItemTextView = itemView.findViewById(R.id.profileItemName);
            editText = itemView.findViewById(R.id.editText);
            editImageView = itemView.findViewById(R.id.imageView);

            editImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();

                    if (listener != null) {
                        if (position != RecyclerView.NO_POSITION) {

                            editText.setVisibility(View.VISIBLE);

                            editText.requestFocus();

                            if (position == 2) {
                                profileItemTextView.setVisibility(View.INVISIBLE);
                                editText.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);
                            } else if (position == 3) {
                                profileItemTextView.setVisibility(View.INVISIBLE);
                                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                            } else if (position == 4) {
                                profileItemTextView.setVisibility(View.INVISIBLE);
                                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                            } else if (position == 5) {
                                profileItemTextView.setVisibility(View.INVISIBLE);
                                editText.setInputType(InputType.TYPE_CLASS_DATETIME);
                            } else if (position == 6) {
                                profileItemTextView.setVisibility(View.INVISIBLE);
                                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                            }

                            editText.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }

                                @Override
                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                    if (charSequence.length() > 0) {

                                        editImageView.setImageResource(R.drawable.ic_baseline_save);

                                        editImageView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Log.d(TAG, "onClick: " + charSequence.toString());
                                                editListener.onItemClick(position, view, charSequence.toString());
                                            }
                                        });
                                    } else {
                                        editImageView.setImageResource(R.drawable.ic_baseline_edit);
                                    }
                                }

                                @Override
                                public void afterTextChanged(Editable editable) {

                                }
                            });
                        }
                    }
                }
            });

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
