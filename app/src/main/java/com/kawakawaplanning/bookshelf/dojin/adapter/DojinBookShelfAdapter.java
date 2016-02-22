package com.kawakawaplanning.bookshelf.dojin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kawakawaplanning.bookshelf.R;
import com.kawakawaplanning.bookshelf.dojin.model.DojinBookData;

import butterknife.ButterKnife;
import jp.sharakova.android.urlimageview.UrlImageView;

/**
 * Created by KP on 16/02/15.
 */
public class DojinBookShelfAdapter extends ArrayAdapter<DojinBookData> {

    private int resourceId;

    public DojinBookShelfAdapter(Context context, int resource) {
        super(context, resource);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resourceId,  null);
        }


//        convertView.setLayoutParams(new AbsListView.LayoutParams(size.x / 3, size.y / 3));


        DojinBookData bd = getItem(position);

        TextView title = ButterKnife.findById(convertView, R.id.title);
        title.setText(bd.getTitle());
        TextView circle = ButterKnife.findById(convertView, R.id.circle);
        circle.setText(bd.getCircle());
        TextView event = ButterKnife.findById(convertView, R.id.event);
        event.setText(bd.getEvent());

        final UrlImageView image = ButterKnife.findById(convertView, R.id.image);
        System.out.println("http://img.doujinshi.org/big/" + (int)Math.floor(bd.getBookId() / 2000.0) + "/" + bd.getBookId() + ".jpg");
        image.setImageUrl("http://img.doujinshi.org/big/" + (int) Math.floor(bd.getBookId() / 2000.0) + "/" + bd.getBookId() + ".jpg");

        image.post(new Runnable() {
            @Override
            public void run() {
                image.setLayoutParams(new LinearLayout.LayoutParams(image.getWidth(), (int) (image.getWidth() * 1.3636363636363635)));


            }

        });

        return convertView;
    }

}