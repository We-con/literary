package com.reader.readingManagement.post.add;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.lantouzi.wheelview.WheelView;
import com.reader.readingManagement.R;
import com.reader.readingManagement.book.BaseActivity;
import com.reader.readingManagement.model.Book;
import com.reader.readingManagement.post.PostTheme;
import com.reader.readingManagement.post.add.imagepicker.PickerBuilder;
import com.reader.readingManagement.post.model.Post;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;

/**
 * Created by naver on 2017. 2. 12..
 */
public class PostAddActivity extends BaseActivity {
    public static final String EXTRA_BOOK_ID = "bookId";
    public static final String EXTRA_BOOK = "book";

    public static final String PARAM_CONTENT = "content";
    public static final String PARAM_THEME = "theme";
    public static final String PARAM_PAGE = "page";
    private static final String PARAM_IMAGE_URL = "imageUrl";

    private static final String PARAM_USER_ID = "userId";
    private static final String PARAM_NICKNAME = "nickname";
    private static final String PARAM_PROFILE_URL = "profileUrl";

    private String folder = null;
    private String imageName = null;
    private boolean withTimeStamp = true;

    private EditText editText;
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            setPostTheme((PostTheme) view.getTag());
        }
    };
    private View.OnClickListener onColorClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            setLineColor((LineColor) view.getTag());
        }
    };
    private PostTheme mPostTheme;
    private LineColor mLineColor;

    private WheelView pageWheelView;
    private View buttons;
    private View pageImageContainter;
    private HighlightPenImageView highlightImageView;

    protected String mBookId;
    protected Book mBook;
    protected File mFile;
    private int mPage;

    private ViewGroup themeSet;
    private ViewGroup colorSet;

    private ImageView lineSample;
    private SeekBar seekbar;

    public static void startActivity(Activity activity, String bookId) {
        Intent intent = new Intent(activity, PostAddActivity.class);
        intent.putExtra(EXTRA_BOOK_ID, bookId);
        activity.startActivity(intent);
    }

    public static void startActivity(Activity activity, Book book) {
        Intent intent = new Intent(activity, PostAddActivity.class);
        intent.putExtra(EXTRA_BOOK, book);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreatedToolbar() {
        super.onCreatedToolbar();
        setTitle("포스트 작성");
        setAction("작성", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO : 서버와 통신해서 return true되면 로컬에 추가하도록 핸들링
                //writePost(getPostBody());
                addPostToDB();
                finish();
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imageName = getString(R.string.app_name);

        if (getIntent() != null) {
            mBookId = getIntent().getStringExtra(EXTRA_BOOK_ID);
            mBook = getIntent().getParcelableExtra(EXTRA_BOOK);
        }
        if (TextUtils.isEmpty(getBookId())) {
            Toast.makeText(this, "책 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
        }

        setContentView(R.layout.activity_add_post);
        initToolbar();

        buttons = findViewById(R.id.image_button_container);
        pageImageContainter = findViewById(R.id.page_image_container);
        highlightImageView = ((HighlightPenImageView) findViewById(R.id.page_image));
        buttons.findViewById(R.id.add_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PickerBuilder(PostAddActivity.this, PickerBuilder.SELECT_FROM_GALLERY)
                        .setOnImageReceivedListener(new PickerBuilder.onImageReceivedListener() {
                            @Override
                            public void onImageReceived(Uri imageUri) {
                                applyImage(imageUri);
                            }
                        })
                        .start();
            }
        });

        //TODO : 카메라로 이미지 추가!!!!!!!
        buttons.findViewById(R.id.take_picture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PickerBuilder(PostAddActivity.this, PickerBuilder.SELECT_FROM_CAMERA)
                        .setOnImageReceivedListener(new PickerBuilder.onImageReceivedListener() {
                            @Override
                            public void onImageReceived(Uri imageUri) {
                                applyImage(imageUri);
                            }
                        })
                        .start();
            }
        });

        editText = ((EditText) findViewById(R.id.edit_content));

        initLineThickSet();
        initMainButtons();
        initThemeSet();
        initColorSet();
        setPostTheme(PostTheme.WHITE);
        setLineColor(LineColor.YELLOW);
    }

    private void applyImage(Uri imageUri) {
        pageImageContainter.setVisibility(View.VISIBLE);
        buttons.setVisibility(View.GONE);
        highlightImageView.setImageURI(imageUri);
    }

    void initMainButtons() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        invalidateOptionsMenu();

        pageWheelView = ((WheelView) findViewById(R.id.page_wheel));
        List<String> pages = new ArrayList<String>();
        int pageSize = Integer.parseInt(mBook.getTotalPage());
        for (int i = 0; i < pageSize; i++) {
            pages.add("" + i);//극혐이다_-;...어쩌지.
        }
        pageWheelView.setItems(pages);
        pageWheelView.setMinSelectableIndex(0);
        pageWheelView.setMaxSelectableIndex(pageSize);
        setPage(mBook.getRecentIndexedPage());
        pageWheelView.selectIndex(mPage);
        pageWheelView.setAdditionCenterMark(".p");
        pageWheelView.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onWheelItemChanged(WheelView wheelView, int position) {
            }

            @Override
            public void onWheelItemSelected(WheelView wheelView, int position) {
                Log.d("currentPosition", "" + position);
                setPage(position);
            }
        });
    }

    void setPage(int page) {
        mPage = page;
    }

    int getPage() {
        return mPage;
    }

    void writePost(Post post) {
        String imageUrl = null;



        //TODO : 이미지를 서버에 추가? ㅠ
        /*if (mFile != null) {
            try {
                String url = UrlHelper.getApiUrl(R.id.api_add_photo);
                imageUrl = Ion.with(this).load("POST", url)
                        .setMultipartFile("file", "image/jpeg", mFile).asString().get(1, TimeUnit.MINUTES);
                Log.d("POST test", imageUrl);
            } catch (Exception e) {
                Log.e("post-add image", "url : " + UrlHelper.getApiUrl(R.id.api_add_photo), e);
            }
        }*/
        //TODO API check 필요.
        //TODO : POST 서버에 업로드 해야겠지
/*        String url = NeoUrlHelper.addPost(getBookId());
        try {
            JsonObject result = AuthIon.withPost(this, url)
                    .setBodyParameter(EXTRA_BOOK_ID, getBookId())
                    .setBodyParameter(PARAM_CONTENT, post.getContent())
                    .setBodyParameter(PARAM_PAGE, String.valueOf(getPage()))
                    .setBodyParameter(PARAM_THEME, post.getThemeName())
                    .setBodyParameter(PARAM_IMAGE_URL, imageUrl)
                    .asJsonObject()
                    .get(30, TimeUnit.SECONDS);

            Log.d("POST(TEXT) TEST ", url + " : " + result.get("message").getAsJsonObject().get("result").getAsJsonObject().toString());
        } catch (Exception e) {
            Log.e("post-add post", "url : " + url, e);
        }*/
    }

    protected String getBookId() {
        return mBook != null ? mBook.getBookId() : mBookId;
    }

    public Post getPostBody() {
        Post newPost = new Post(mBook);
        newPost.setContent(editText.getEditableText().toString());
        newPost.setTheme(mPostTheme.name());
        newPost.setPage(mPage);
        if (pageImageContainter.getVisibility() != View.GONE) {
            highlightImageView.buildDrawingCache();
            mFile = saveBitmapToFileCache(highlightImageView.getDrawingCache());
            newPost.setPostImageUrl(mFile.toURI().toString());
        }

        newPost.setCreateDate(Calendar.getInstance().getTime());
        return newPost;
    }

    private void setPostTheme(PostTheme newTheme) {
        mPostTheme = newTheme;
        radioButtons(newTheme);
    }

    private void radioButtons(PostTheme newTheme) {
        for (int i = 0; i < themeSet.getChildCount(); i++) {
            View theme = themeSet.getChildAt(i);
            if (theme.getId() == newTheme.getId()) {
                theme.setAlpha(.8f);
                theme.setScaleX(1.1f);
                theme.setScaleY(1.1f);
            } else {
                theme.setAlpha(.4f);
                theme.setScaleX(1.f);
                theme.setScaleY(1.f);
            }
        }
    }

    private void setLineColor(LineColor newColor) {
        mLineColor = newColor;
        for (int i = 0; i < colorSet.getChildCount(); i++) {
            View color = colorSet.getChildAt(i);
            if (color.getId() == newColor.getId()) {
                color.setAlpha(.8f);
                color.setScaleX(1.2f);
                color.setScaleY(1.2f);
            } else {
                color.setAlpha(.6f);
                color.setScaleX(1.f);
                color.setScaleY(1.f);
            }
        }
        highlightImageView.setPenColor(newColor.getResId());
    }

    private void initLineThickSet(){
        lineSample = (ImageView) findViewById(R.id.line_sample);
        seekbar = (SeekBar) findViewById(R.id.line_thick);
        seekbar.setMax(100);
        seekbar.setProgress(50);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                highlightImageView.setThickness(adjustRate((float)progress));
                lineSample.setScaleY(adjustRate(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private float adjustRate(float rate){
        Log.d("ADJUSTRATE", Float.toString(rate*2/100));
        return rate*2/100;

    }

    private void initThemeSet() {
        themeSet = ((ViewGroup) findViewById(R.id.theme_set));
        for (int i = 0; i < themeSet.getChildCount(); i++) {
            View theme = themeSet.getChildAt(i);
            theme.setOnClickListener(onClickListener);
            theme.setTag(PostTheme.values()[i]);
        }
    }

    private void initColorSet() {
        colorSet = ((ViewGroup) findViewById(R.id.color_set));
        for (int i = 0; i < colorSet.getChildCount(); i++) {
            View color = colorSet.getChildAt(i);
            color.setOnClickListener(onColorClickListener);
            color.setTag(LineColor.values()[i]);
        }
    }

    private File saveBitmapToFileCache(Bitmap bitmap) {
        String imagePathStr = Environment.getExternalStorageDirectory() + "/" +
                (folder == null ?
                        Environment.DIRECTORY_DCIM + "/" + getString(R.string.app_name) :
                        folder);

        File path = new File(imagePathStr);
        if (!path.exists()) {
            path.mkdir();
        }

        String finalPhotoName = imageName +
                (withTimeStamp ? "_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date(System.currentTimeMillis())) : "")
                + ".jpg";

        File fileCacheItem = new File(path, finalPhotoName);
        FileOutputStream fos = null;
        Log.d("er", "" + fileCacheItem.isFile());
        try {
            fos = new FileOutputStream(fileCacheItem);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fileCacheItem.createNewFile();
            fos.flush();
            fos.close();
            return fileCacheItem;
        } catch (Exception e) {
            Log.e("IMAGE", e.toString(), e);
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                Log.e("IMAGE", e.toString(), e);
            }
        }
        return null;
    }

    public void addPostToDB(){
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Book tmp = realm.where(Book.class).equalTo("bookId", mBook.getBookId()).findFirst();
                tmp.setRecentIndexedPage(mPage);
                tmp.posts.add(getPostBody());
            }
        });

        // IMAGE CACHE 정리
        deleteCache();
    }

    public void deleteCache() {
        String file_dj_path = Environment.getExternalStorageDirectory() + "/" +
                Environment.DIRECTORY_DCIM + "/" + getString(R.string.app_name) +
                "/" + getString(R.string.app_name) + "_tmp.jpg";

        Log.e("IMAGE", "file Deleted :" + file_dj_path);

        File fdelete = new File(file_dj_path);
        if (fdelete.exists()) {
            Toast.makeText(this, file_dj_path, Toast.LENGTH_SHORT).show();

            if (fdelete.delete()) {
                Log.e("IMAGE", "file Deleted :" + file_dj_path);
                callBroadCast();
            } else {
                Log.e("IMAGE", "file not Deleted :" + file_dj_path);
            }
        }
    }

    public void callBroadCast() {
        if (Build.VERSION.SDK_INT >= 14) {
            Log.e("-->", " >= 14");
            MediaScannerConnection.scanFile(this, new String[]{Environment.getExternalStorageDirectory().toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                /*
                 *   (non-Javadoc)
                 * @see android.media.MediaScannerConnection.OnScanCompletedListener#onScanCompleted(java.lang.String, android.net.Uri)
                 */
                public void onScanCompleted(String path, Uri uri) {
                    Log.e("ExternalStorage", "Scanned " + path + ":");
                    Log.e("ExternalStorage", "-> uri=" + uri);
                }
            });
        } else {
            Log.e("-->", " < 14");
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }
    }
}
