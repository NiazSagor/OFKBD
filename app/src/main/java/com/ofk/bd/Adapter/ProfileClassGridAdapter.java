package com.ofk.bd.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.ofk.bd.R;

@SuppressLint("ViewHolder")
public class ProfileClassGridAdapter extends BaseAdapter {

    private final Context context;
    private final String[] contents;
    private final String type;
    private OnItemClickListener listener;

    private boolean isClickable = true;

    public ProfileClassGridAdapter(Context context, String[] contents, String type) {
        this.context = context;
        this.contents = contents;
        this.type = type;
    }

    public interface OnItemClickListener {
        void onClickListener(int position);
    }

    public void setOnItemClickListener (OnItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return contents.length;
    }

    @Override
    public Object getItem(int i) {
        return contents[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View customView = LayoutInflater.from(context).inflate(R.layout.profile_edit_item, viewGroup, false);

        TextView content = customView.findViewById(R.id.content);
        TextView headline = customView.findViewById(R.id.headline);

        if (type.equals("gender")) {
            headline.setText(contents[i]);
            content.setVisibility(View.INVISIBLE);
        } else if (type.equals("class")) {
            content.setText(contents[i]);
        }

        ConstraintLayout constraintLayout = customView.findViewById(R.id.parentLayout);

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isClickable) {
                    constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.background1_blue_color));
                    headline.setTextColor(context.getResources().getColor(R.color.darkOrange));
                    content.setTextColor(context.getResources().getColor(R.color.darkOrange));
                    listener.onClickListener(i);
                    isClickable = false;
                } else {
                    Toast.makeText(context, "You've selected already", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return customView;
    }
}
