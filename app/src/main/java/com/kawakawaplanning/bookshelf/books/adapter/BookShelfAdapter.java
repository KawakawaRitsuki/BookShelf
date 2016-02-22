package com.kawakawaplanning.bookshelf.books.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kawakawaplanning.bookshelf.R;
import com.kawakawaplanning.bookshelf.books.database.Books;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import butterknife.ButterKnife;
import jp.sharakova.android.urlimageview.UrlImageView;

/**
 * Created by KP on 16/02/15.
 */
public class BookShelfAdapter extends ArrayAdapter<Books> {

    private int resourceId;
    private Resources resources;
    private Context context;
    private int width = 0;

    public BookShelfAdapter(Context context, int resourceId,Resources resources) {
        super(context, resourceId);
        this.resourceId = resourceId;
        this.resources = resources;
        this.context = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resourceId,  null);
        }

        Books book = getItem(position);

        TextView title = ButterKnife.findById(convertView, R.id.title);
        title.setText(book.title);
        TextView circle = ButterKnife.findById(convertView, R.id.circle);
        circle.setText(book.author);
        TextView event = ButterKnife.findById(convertView, R.id.event);
        event.setText(book.publisher);

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

        final UrlImageView image = ButterKnife.findById(convertView, R.id.image);
        image.setImageBitmap(bmp);
        image.setScaleType(ImageView.ScaleType.FIT_END);

        if(width == 0){
            image.post(new Runnable() {//描画後に実行？じゃなくてメインスレッドでだわ
                @Override
                public void run() {
                    if(image.getWidth() != 0){
                        width = image.getWidth();
                    }
                    image.setLayoutParams(new LinearLayout.LayoutParams(image.getWidth(), (int) (image.getWidth() * 1.3636363636363635)));
                }
            });
        }else{
            image.setLayoutParams(new LinearLayout.LayoutParams(width, (int) (width * 1.3636363636363635)));
        }

        return convertView;
    }

}