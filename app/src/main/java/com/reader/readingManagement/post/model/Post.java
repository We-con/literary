package com.reader.readingManagement.post.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.reader.readingManagement.model.Book;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by naver on 2017. 2. 12..
 */
public class Post extends RealmObject implements Parcelable{
    //    META 정보
    private long id;
    private String bookId;
    private Date createDate;
    private Date updateDate;
    public static DateFormat df = new SimpleDateFormat("yyyy.MM.dd");


    //  USER Info (res header userId로 자동 처리)
    private String userId;
    private String userNickname;
    private String userProfileUrl;

    //    DATA (req 필수.)
    private int page;
    private String content;
    private String imageUrl;
    private String themeName;
    private String postImageUrl;

    // 실시간 데이터 (api 받을때)
    private int likeCount;
    private int replyCount;

    public Post() {
        super();
    }

    public Post(String id,
                String bookId,
                long create,
                long update,
                String userId,
                String userNickname,
                String userProfileUrl,
                String page,
                String content,
                String imageUrl,
                String themeName,
                String likeCount,
                String replyCount) {

        this.id = parse(id);
        this.bookId = bookId;
        this.userId = userId;
        this.userNickname = userNickname;
        this.userProfileUrl = userProfileUrl;
        this.page = parse(page);
        this.content = content;
        this.imageUrl = imageUrl;
        this.themeName = themeName;
        this.likeCount = parse(likeCount);
        this.replyCount = parse(replyCount);
    }

    int parse(String num){
        try {
            return Integer.parseInt(num);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public Post(String bookId) {
        this.bookId = bookId;
    }

    public Post(Book book) {
        this(book.getBookId());
        this.imageUrl = book.getThumbnailUrl();
        this.content = book.getName();
    }
    protected Post(Parcel in) {
        bookId = in.readString();
        imageUrl= in.readString();
        content = in.readString();
        themeName = in.readString();
        page = Integer.parseInt(in.readString());
        postImageUrl = in.readString();
        id = in.readLong();
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookId() {
        return bookId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public void setTheme(String name) {
        this.themeName = name;
    }

    public String getThemeName() {
        return themeName;
    }

    public Date getCreateDate() {
        return createDate;
    }
    public String getCreateDateString(){
        return df.format(createDate);
    }

    public void setCreateDate(Date createData) {
        this.createDate = createData;
        this.id = createData.getTime();
    }

    public String getUserProfileUrl() {
        return userProfileUrl;
    }

    public void setUserProfileUrl(String userProfileUrl) {
        this.userProfileUrl = userProfileUrl;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public void setPostImageUrl(String postImageUrl) {
        this.postImageUrl = postImageUrl;
    }

    public static final Parcelable.Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bookId);
        dest.writeString(imageUrl);
        dest.writeString(content);
        dest.writeString(themeName);
        dest.writeString(String.valueOf(page));
        dest.writeString(postImageUrl);
        dest.writeLong(id);
    }
}
