package com.kawakawaplanning.bookshelf.books.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.kawakawaplanning.bookshelf.R;
import com.kawakawaplanning.bookshelf.books.database.Books;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Intent intent = getIntent();

        Books book = new Books();

        book.title = intent.getStringExtra("title");
        book.titleKana = intent.getStringExtra("titleKana");
        book.author = intent.getStringExtra("author");
        book.isbn = intent.getStringExtra("isbn");
        book.kan = intent.getStringExtra("kan");
        book.publisher =intent.getStringExtra("publisher");
        book.label = intent.getStringExtra("label");
        book.image = intent.getStringExtra("image");

        getSupportActionBar().setTitle(book.title);

        ImageView imageView = (ImageView)findViewById(R.id.image);
        byte[] b = null;
        try {
            b = Hex.decodeHex(book.image.toCharArray());
        } catch (DecoderException e) {
            e.printStackTrace();
        }

        Bitmap bmp = null;
        if (b != null) {
            bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
        }

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageDrawable(new BitmapDrawable(getResources(), bmp));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("touched");
            }
        });
    }
}
