package com.kawakawaplanning.bookshelf.books.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kawakawaplanning.bookshelf.R;
import com.kawakawaplanning.bookshelf.books.database.Books;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailActivity extends AppCompatActivity {

    @Bind(R.id.image)
    ImageView imageView;

    @Bind(R.id.titleTv)
    TextView titleTv;
    @Bind(R.id.authorTv)
    TextView authorTv;
    @Bind(R.id.isbnTv)
    TextView isbnTv;
    @Bind(R.id.kanTv)
    TextView kanTv;
    @Bind(R.id.publisherTv)
    TextView publisherTv;
    @Bind(R.id.labelTv)
    TextView labelTv;

    Books book;
    Long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(DetailActivity.this,AddBookInputActivity.class);
                intent.putExtra("title", book.title);
                intent.putExtra("titleKana", book.titleKana);
                intent.putExtra("author", book.author);
                intent.putExtra("isbn", book.isbn);
                intent.putExtra("kan", book.kan);
                intent.putExtra("publisher", book.publisher);
                intent.putExtra("label", book.label);
                intent.putExtra("image", book.image);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();

        book = new Books();

            book.title = intent.getStringExtra("title");
            book.titleKana = intent.getStringExtra("titleKana");
            book.author = intent.getStringExtra("author");
            book.isbn = intent.getStringExtra("isbn");
            book.kan = intent.getStringExtra("kan");
            book.publisher =intent.getStringExtra("publisher");
            book.label = intent.getStringExtra("label");
            book.image = intent.getStringExtra("image");
        id = intent.getLongExtra("id",0L);

        getSupportActionBar().setTitle(book.title);


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

        load();
    }

    public void load(){
        titleTv.setText(book.title);
        authorTv.setText(book.author);
        isbnTv.setText(book.isbn);
        kanTv.setText(book.kan);
        publisherTv.setText(book.publisher);
        labelTv.setText(book.label);
    }

    @OnClick(R.id.image)
    public void image(View v){
        System.out.println("touched");
    }
}
