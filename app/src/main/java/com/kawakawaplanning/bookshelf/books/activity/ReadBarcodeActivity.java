package com.kawakawaplanning.bookshelf.books.activity;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.kawakawaplanning.bookshelf.R;
import com.kawakawaplanning.bookshelf.books.CameraPreview;
import com.kawakawaplanning.bookshelf.books.database.Books;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ReadBarcodeActivity extends AppCompatActivity {

    @Bind(R.id.cameraPreview)
    FrameLayout mCameraPreview;

    private Camera mCamera;
    private ImageScanner mScanner;
    private CameraPreview mPreview;

    static {
        System.loadLibrary("iconv");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_barcode);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mScanner           = new ImageScanner();
        mCamera            = Camera.open();
        mPreview           = new CameraPreview(this, mCamera, previewCb, null);

        mCameraPreview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                try {
                    if(!isScaned){
                        mCamera.autoFocus(null);
                    }
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        mScanner.setConfig(0, Config.X_DENSITY, 3);
        mScanner.setConfig(0, Config.Y_DENSITY, 3);
        mCameraPreview.addView(mPreview);
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    public void onPause() {
        super.onPause();
        releaseCamera();
        finish();
    }

    boolean isScaned = false;
    Camera.PreviewCallback previewCb = new Camera.PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size size = parameters.getPreviewSize();

            Image barcode = new Image(size.width, size.height, "Y800");
            barcode.setData(data);

            int result = mScanner.scanImage(barcode);

            if (result != 0) {
                SymbolSet symbolSet = mScanner.getResults();
                for (Symbol symbol : symbolSet)
                    if (Books.isISBN(symbol.getData())) {
                        String isbn = symbol.getData();

                        mCamera.setPreviewCallback(null);
                        mCamera.stopPreview();
                        isScaned = true;
//                        mVibrator.vibrate(100);

                        Books book = new Books.Builder().isbnSearch(isbn);
                        if(book.canSave()){
                            book.save();
                            setResult(RESULT_OK);
                        }else{
                            Intent intent = new Intent();
                            intent.setClass(ReadBarcodeActivity.this, AddBookInputActivity.class);
                            startActivity(intent);


                            setResult(RESULT_OK);
                        }
                        finish();

                    }
            }
        }
    };
}
