package com.kawakawaplanning.bookshelf.books.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.activeandroid.query.Select;
import com.isseiaoki.simplecropview.CropImageView;
import com.kawakawaplanning.bookshelf.R;
import com.kawakawaplanning.bookshelf.books.database.Books;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddBookInputActivity extends AppCompatActivity {

    Books book;

    @Bind(R.id.titleEt)
    EditText titleEt;
    @Bind(R.id.titleKanaEt)
    EditText titleKanaEt;
    @Bind(R.id.authorEt)
    EditText authorEt;
    @Bind(R.id.isbnEt)
    EditText isbnEt;
    @Bind(R.id.kanEt)
    EditText kanEt;
    @Bind(R.id.publisherEt)
    EditText publisherEt;
    @Bind(R.id.labelEt)
    EditText labelEt;
    @Bind(R.id.imageView)
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book_input);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        book = new Books();

        Intent intent = getIntent();


        if(intent.getLongExtra("id",123123123123L) != 123123123123L){
            book = new Select().from(Books.class).where("Id = ?", intent.getLongExtra("id", 0L)).executeSingle();
        }else{
            if(intent != null){
                book.title = intent.getStringExtra("title");
                book.titleKana = intent.getStringExtra("titleKana");
                book.author = intent.getStringExtra("author");
                book.isbn = intent.getStringExtra("isbn");
                book.kan = intent.getStringExtra("kan");
                book.publisher =intent.getStringExtra("publisher");
                book.label = intent.getStringExtra("label");
                book.image = intent.getStringExtra("image");
                if(book.image == null) {
                    book.setNoImage();
                }else if(book.image.equals("")){
                    book.setNoImage();
                }
            }
        }
        reflect();
    }

    private void reflect(){
        titleEt.setText(book.title);
        titleKanaEt.setText(book.titleKana);
        authorEt.setText(book.author);
        isbnEt.setText(book.isbn);
        kanEt.setText(book.kan);
        publisherEt.setText(book.publisher);
        labelEt.setText(book.label);

        if( book.image != null){
            if(!book.image.equals("")){
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
                imageView.setImageBitmap(bmp);
            }
        }

    }

    @OnClick(R.id.submitBtn)
    public void submit(Button btn){
        book.title = titleEt.getText().toString();
        book.titleKana = titleKanaEt.getText().toString();
        book.author = authorEt.getText().toString();
        book.isbn = isbnEt.getText().toString();
        book.kan = kanEt.getText().toString();
        book.publisher = publisherEt.getText().toString();
        book.label = labelEt.getText().toString();

        if(book.title.equals("")){book.title = null;}
        if(book.titleKana.equals("")) {book.titleKana = null;}
        if(book.author.equals("")){book.author = null;}
        if(book.isbn.equals("")){book.isbn = null;}
        if(book.kan.equals("")){book.kan = null;}
        if(book.publisher.equals("")){book.publisher = null;}
        if(book.label.equals("")){book.label = null;}

        if(book.canSave()){
            System.out.println("Can save");
            book.save();
            finish();
        }else{
            System.out.println("Can't save");
        }
    }

    static final int REQUEST_CAPTURE_IMAGE = 100;
    static final int REQUEST_CROP_IMAGE = 101;

    @OnClick(R.id.imageView)
    public void image(View v){
        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
        startActivityForResult(intent, REQUEST_CAPTURE_IMAGE);
//        Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE);
//
//        Date date = new Date(System.currentTimeMillis());
//        SimpleDateFormat dataFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
//        String filename = dataFormat.format(date) + ".jpg";
//        mSaveUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera", filename));
//
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, mSaveUri);
//        startActivityForResult( intent, REQUEST_CAPTURE_IMAGE);
    }

    private String selectedImagePath = "";
    String imgPath;

    public Uri setImageUri() {
        // Store image in dcim
        Uri imgUri;
        do {
            File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "image" + new Date().getTime() + ".png");
            imgUri = Uri.fromFile(file);
            this.imgPath = file.getAbsolutePath();
        }while (imgPath == null);

        return imgUri;
    }

    public Bitmap decodeFile(String path) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, o);
            // The new size we want to scale to
            final int REQUIRED_SIZE = 70;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeFile(path, o2);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data) {
        if(REQUEST_CAPTURE_IMAGE == requestCode && resultCode == Activity.RESULT_OK ){

            if(selectedImagePath != null){
                selectedImagePath = imgPath;

                Bitmap bmp = decodeFile(selectedImagePath);
                System.out.println(selectedImagePath);


                android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view =  inflater.inflate(R.layout.activity_crop,(ViewGroup)findViewById(R.id.root));

                final CropImageView civ = (CropImageView)view.findViewById(R.id.crop);
                final Button rotate = (Button)view.findViewById(R.id.button);
                civ.setImageBitmap(bmp);
                civ.setCropMode(CropImageView.CropMode.RATIO_CUSTOM);
                civ.setCustomRatio(11, 15);
                civ.setHandleShowMode(CropImageView.ShowMode.SHOW_ALWAYS);
                civ.setGuideShowMode(CropImageView.ShowMode.SHOW_ON_TOUCH);

                alertDialogBuilder.setTitle("トリミング");
                alertDialogBuilder.setView(view);
                alertDialogBuilder.setCancelable(true);

                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        civ.getCroppedBitmap().compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] bytes = baos.toByteArray();

                        book.image = new String(Hex.encodeHex(bytes));

                        reflect();
                    }
                });

                rotate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        civ.rotateImage(CropImageView.RotateDegrees.ROTATE_90D);
                    }
                });


                alertDialogBuilder.show();
            }else{
                android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("エラー");
                alertDialogBuilder.setMessage("画像の取得に失敗しました。もう一度お試しください。");
                alertDialogBuilder.setCancelable(true);
                alertDialogBuilder.setPositiveButton("OK", null);
                alertDialogBuilder.show();
            }
        }
        if(REQUEST_CROP_IMAGE == requestCode && resultCode == Activity.RESULT_OK){
            book.image = data.getStringExtra("image");
            if( book.image != null){
                if(!book.image.equals("")){
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
                    imageView.setImageBitmap(bmp);
                }
            }
        }
    }
}
