package com.reader.readingManagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.reader.readingManagement.book.add.AddBookActivity;
import com.reader.readingManagement.book.info.BookInfoActivity;
import com.reader.readingManagement.home.HomeActivity;
import com.reader.readingManagement.user.LoginActivity;

import jp.wasabeef.blurry.Blurry;


public class MainActivity extends AppCompatActivity {
    private ImageView blur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, R.string.your_name, Toast.LENGTH_SHORT).show();

        blur = (ImageView) findViewById(R.id.blur);
        blur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //시간차를 두고 써야하는게 좀 이상하군..
                Blurry.with(MainActivity.this)
                        .radius(5)
                        .sampling(5)
                        .async()
                        .animate(1500)
                        .capture(blur)
                        .into(blur);
            }
        });

        findViewById(R.id.test2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BookInfoActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.test3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


        findViewById(R.id.test6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddBookActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.test7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

    }
}
