package com.kawakawaplanning.bookshelf;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;

/**
 * http://qiita.com/masahide318/items/e9afbbbf6747bb091f63
 */
public class BitmapResizer {
    private Context context;

    public BitmapResizer(Context context) {
        this.context = context;
    }

    public Bitmap resize(Uri uri,int mMaxWidth,int mMaxHeight) throws IOException {

        Bitmap bitmap = null;
        BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
        decodeOptions.inJustDecodeBounds = true;
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        BitmapFactory.decodeStream(inputStream,null,decodeOptions);
        inputStream.close();
        int actualWidth = decodeOptions.outWidth;
        int actualHeight = decodeOptions.outHeight;

        int desiredWidth = getResizedDimension(mMaxWidth, mMaxHeight,
                actualWidth, actualHeight);
        int desiredHeight = getResizedDimension(mMaxHeight, mMaxWidth,
                actualHeight, actualWidth);

        decodeOptions.inJustDecodeBounds = false;
        decodeOptions.inSampleSize =
                findBestSampleSize(actualWidth, actualHeight, desiredWidth, desiredHeight);

        inputStream = context.getContentResolver().openInputStream(uri);
        Bitmap tempBitmap = BitmapFactory.decodeStream(inputStream,null,decodeOptions);
        inputStream.close();

        if (tempBitmap != null && (tempBitmap.getWidth() > desiredWidth ||
                tempBitmap.getHeight() > desiredHeight)) {
            bitmap = Bitmap.createScaledBitmap(tempBitmap,
                    desiredWidth, desiredHeight, true);
            tempBitmap.recycle();
        } else {
            bitmap = tempBitmap;
        }
        return bitmap;
    }

    private int getResizedDimension(int maxPrimary, int maxSecondary, int actualPrimary,
                                    int actualSecondary) {
        if ((maxPrimary == 0) && (maxSecondary == 0)) {
            return actualPrimary;
        }

        if (maxPrimary == 0) {
            double ratio = (double) maxSecondary / (double) actualSecondary;
            return (int) (actualPrimary * ratio);
        }

        if (maxSecondary == 0) {
            return maxPrimary;
        }

        double ratio = (double) actualSecondary / (double) actualPrimary;
        int resized = maxPrimary;

        if ((resized * ratio) < maxSecondary) {
            resized = (int) (maxSecondary / ratio);
        }
        return resized;
    }

    static int findBestSampleSize(
            int actualWidth, int actualHeight, int desiredWidth, int desiredHeight) {
        double wr = (double) actualWidth / desiredWidth;
        double hr = (double) actualHeight / desiredHeight;
        double ratio = Math.min(wr, hr);
        float n = 1.0f;
        while ((n * 2) <= ratio) {
            n *= 2;
        }
        return (int) n;
    }
}
