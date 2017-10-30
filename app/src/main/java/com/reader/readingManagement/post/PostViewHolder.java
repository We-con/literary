package com.reader.readingManagement.post;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.reader.readingManagement.post.model.Post;

/**
 * Created by naver on 2017. 2. 12..
 */
public abstract class PostViewHolder extends RecyclerView.ViewHolder{

    public PostViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void onBind(Post post);
}
