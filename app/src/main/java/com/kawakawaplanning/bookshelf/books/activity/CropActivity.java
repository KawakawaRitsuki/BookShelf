package com.kawakawaplanning.bookshelf.books.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.isseiaoki.simplecropview.CropImageView;
import com.kawakawaplanning.bookshelf.R;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CropActivity extends AppCompatActivity {

    Bitmap bitmap;

    @Bind(R.id.crop)
    CropImageView crop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        ButterKnife.bind(this);
        System.out.println("CropActivity");

        Intent intent = getIntent();
        String data = "";
        if(intent != null){
            data = intent.getStringExtra("image");
        } else finish();

        if(!data.equals("")){
            byte[] b = null;
            try {
                b = Hex.decodeHex(data.toCharArray());
            } catch (DecoderException e) {
                e.printStackTrace();
            }

            if (b != null) {
                bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            }
        } else finish();

        crop.setImageBitmap(bitmap);

        crop.setCropMode(CropImageView.CropMode.RATIO_CUSTOM);
        crop.setCustomRatio(11, 15);
        crop.setHandleShowMode(CropImageView.ShowMode.SHOW_ALWAYS);
        crop.setGuideShowMode(CropImageView.ShowMode.SHOW_ON_TOUCH);
    }

    @OnClick(R.id.button)
    public void rotate(View v){
        crop.rotateImage(CropImageView.RotateDegrees.ROTATE_90D);
    }

//    @OnClick(R.id.button2)
//    public void complate(View v){
//        Bitmap bmp = crop.getCroppedBitmap();
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
//        byte[] bytes = baos.toByteArray();
//
//        String image = new String(Hex.encodeHex(bytes));
//        Intent intent = new Intent();
//        intent.putExtra("image", image);
//        setResult(RESULT_OK,intent);
//        finish();
//    }
}
