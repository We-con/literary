package com.reader.readingManagement.post;

import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.crash.FirebaseCrash;
import com.reader.readingManagement.DialogActivity;
import com.reader.readingManagement.R;
import com.reader.readingManagement.post.add.PostEditActivity;
import com.reader.readingManagement.utils.CollectionsUtils;
import com.reader.readingManagement.post.model.Post;

import io.realm.RealmResults;

/**
 * Created by naver on 2017. 2. 12..
 */
public abstract class HeaderPostListAdapter extends RecyclerView.Adapter {
    private static final int HEADER_TYPE = 0;
    private static final int TEXT_TYPE = 1;
    private static final int IMAGE_TYPE = 2;
    private final FragmentActivity activity;
    private RealmResults<Post> list;
    protected boolean hasHeader = false;
    protected View.OnClickListener onClickListener;

    public HeaderPostListAdapter(FragmentActivity activity) {
        this.activity = activity;
    }

    public FragmentActivity getActivity() {
        return activity;
    }

    public void setHasHeader(boolean b) {
        hasHeader = b;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case HEADER_TYPE:
                return createHeaderView(activity.getLayoutInflater(), parent);
            case IMAGE_TYPE:
                return new ImagePost(activity.getLayoutInflater().inflate(R.layout.bookinfo_post_item, parent, false));
            case TEXT_TYPE:
            default:
                return new TextPost(activity.getLayoutInflater().inflate(R.layout.bookinfo_post_item, parent, false));
        }
    }

    protected abstract RecyclerView.ViewHolder createHeaderView(LayoutInflater layoutInflater, ViewGroup parent);

    protected abstract void onBindHeaderView(RecyclerView.ViewHolder holder, int position);

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FirebaseCrash.log("position : " + position + holder.getItemViewType());
        switch (getItemViewType(position)) {
            case HEADER_TYPE:
                onBindHeaderView(holder, position);
                break;
            case IMAGE_TYPE:
                ((ImagePost) holder).onBind(getItem(position));
                break;
            case TEXT_TYPE:
                ((TextPost) holder).onBind(getItem(position));
                break;
        }
    }


    private Post getItem(int position) {
        return list.get(position - 1);
    }

    @Override
    public int getItemViewType(int position) {
        if (hasHeader && position == 0) {
            return HEADER_TYPE;
        } else {
            return TextUtils.isEmpty(list.get(position - 1).getPostImageUrl()) ? TEXT_TYPE : IMAGE_TYPE;
        }
    }

    @Override
    public int getItemCount() {
        return (list != null ? list.size() + (hasHeader ? 1 : 0) : (hasHeader ? 1 : 0));
    }

    public void setData(RealmResults<Post> list) {
        if (CollectionsUtils.isEmpty(list)) {
            return;
        }
        this.list = list;
        notifyItemRangeChanged(1, list.size() - 1);
    }

    public void setClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    class TextPost extends PostViewHolder {
        private Post mPost;
        private ImageView postTheme;
        private TextView page, date, content;

        public TextPost(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PostEditActivity.startActivity(activity, mPost);
                }
            });
            itemView.findViewById(R.id.post_include).setVisibility(View.VISIBLE);
            postTheme = ((ImageView) itemView.findViewById(R.id.post_head_theme));
            content = ((TextView) itemView.findViewById(R.id.post_content));
            page = ((TextView) itemView.findViewById(R.id.post_head));
            date = (TextView) itemView.findViewById(R.id.post_date);


        }

        @Override
        public void onBind(Post post) {
            mPost = post;
            page.setText("P." + String.valueOf(post.getPage()));
            postTheme.setImageDrawable( ContextCompat.getDrawable(activity, PostTheme.valueOf(post.getThemeName()).getResId()));
            date.setText(post.getCreateDateString());
            content.setText(post.getContent());
            itemView.findViewById(R.id.post_include).setVisibility(View.VISIBLE);

            itemView.findViewById(R.id.post_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogActivity.startActivity(getActivity(), mPost);
                }
            });
        }

    }

    class ImagePost extends PostViewHolder {
        private Post mPost;
        private ImageView thumbnail, postTheme;
        private TextView page, date, content;
        private ImageView postImageView;


        public ImagePost(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PostEditActivity.startActivity(activity, mPost);
                }
            });
            page = (TextView) itemView.findViewById(R.id.post_head);
            thumbnail = ((ImageView) itemView.findViewById(R.id.thumbnail));
            postTheme = ((ImageView) itemView.findViewById(R.id.post_head_theme));
            date = ((TextView) itemView.findViewById(R.id.post_date));
            postImageView = ((ImageView) itemView.findViewById(R.id.post_image_content));
            content = ((TextView) itemView.findViewById(R.id.post_content));
            출처: http://digapps.tistory.com/entry/안드로이드-TextView-스크롤-넣기 [Dig Apps
            postImageView.setVisibility(View.VISIBLE);

        }

        @Override
        public void onBind(Post post) {
//            String dateString = new SimpleDateFormat("yyyy.MM.dd").format(post.getCreateDate());
//            date.setText(dateString);
            //TODO : 맞는지는 모르겠다.
            mPost = post;
            page.setText("P." + String.valueOf(post.getPage()));
            Log.d("headerpostList", post.getThemeName());
            postTheme.setImageDrawable( ContextCompat.getDrawable(activity, PostTheme.valueOf(post.getThemeName()).getResId()));
            Glide.with(activity).load(post.getImageUrl()).into(thumbnail);
            itemView.findViewById(R.id.post_include).setVisibility(View.VISIBLE);


            content.setText(post.getContent());
            Glide.with(activity).load(post.getPostImageUrl()).into(postImageView);
            date.setText(post.getCreateDateString());

            itemView.findViewById(R.id.post_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogActivity.startActivity(getActivity(), mPost);
                }
            });
        }

    }

}
