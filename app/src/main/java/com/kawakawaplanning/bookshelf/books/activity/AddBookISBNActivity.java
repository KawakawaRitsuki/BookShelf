package com.kawakawaplanning.bookshelf.books.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.kawakawaplanning.bookshelf.R;
import com.kawakawaplanning.bookshelf.books.database.Books;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddBookISBNActivity extends AppCompatActivity {

    @Bind(R.id.searchBtn)
    Button nextBtn;
    @Bind(R.id.editText)
    MaterialEditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book_isbn);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Books book = new Books.Builder().isbnSearch(editText.getText().toString());
                if(book.canSave()){
                    book.save();
                    setResult(RESULT_OK);
                }else{
                    setResult(RESULT_CANCELED);
                }
                finish();
            }
        });
    }
}
