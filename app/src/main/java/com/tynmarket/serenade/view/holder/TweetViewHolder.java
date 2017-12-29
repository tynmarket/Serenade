package com.tynmarket.serenade.view.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tynmarket.serenade.R;

/**
 * Created by tyn-iMarket on 2017/12/18.
 */

public class TweetViewHolder extends RecyclerView.ViewHolder {
    private View view;
    public TextView text;

    public TweetViewHolder(View itemView) {
        super(itemView);
        this.view = itemView;
        this.text = (TextView) itemView.findViewById(R.id.name);
    }
}
