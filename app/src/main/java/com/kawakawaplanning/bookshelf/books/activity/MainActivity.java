package com.kawakawaplanning.bookshelf.books.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SearchView;

import com.activeandroid.query.Select;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.kawakawaplanning.bookshelf.R;
import com.kawakawaplanning.bookshelf.books.adapter.BookShelfAdapter;
import com.kawakawaplanning.bookshelf.books.adapter.SortActionProvider;
import com.kawakawaplanning.bookshelf.books.database.Books;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

//    @Bind(R.id.fab)
//    FloatingActionButton fab;
    @Bind(R.id.float_menu)
    FloatingActionsMenu float_menu;
    @Bind(R.id.isbn_btn)
    FloatingActionButton isbn_btn;
    @Bind(R.id.read_isbn_btn)
    FloatingActionButton read_isbn_btn;
    @Bind(R.id.input_btn)
    FloatingActionButton input_btn;
    @Bind(R.id.gridView)
    GridView gv;


    Toolbar toolbar;

    BookShelfAdapter adapter;
    static final int ADD_ISBN_REQUEST = 0;
    static final int READ_BARCORD_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SearchView searchView = (SearchView)findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.clear();

                List<Books> books = new Select().from(Books.class).where("title Like '%" + newText + "%'").orderBy(order).execute();

                for (Books book : books) {
                    adapter.add(book);
                }

                gv.setAdapter(adapter);
                return false;
            }
        });

        adapter = new BookShelfAdapter(getApplicationContext(), R.layout.list_card,getResources());

        gv.setNumColumns(3);//GridView.AUTO_FIT);
        gv.setHorizontalSpacing(10);
        gv.setDrawSelectorOnTop(true);
//        gv.setColumnWidth(80);


        isbn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AddBookISBNActivity.class);
                startActivityForResult(intent, ADD_ISBN_REQUEST);
                float_menu.toggle();
            }
        });
        read_isbn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ReadBarcodeActivity.class);
                startActivityForResult(intent, READ_BARCORD_REQUEST);
            }
        });
        input_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AddBookInputActivity.class);
                startActivityForResult(intent, READ_BARCORD_REQUEST);
            }
        });

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Books book = (Books) gv.getItemAtPosition(position);

                Intent intent = new Intent();
                intent.setClass(MainActivity.this, DetailActivity.class);
                intent.putExtra("title", book.title);
                intent.putExtra("titleKana", book.titleKana);
                intent.putExtra("author", book.author);
                intent.putExtra("isbn", book.isbn);
                intent.putExtra("kan", book.kan);
                intent.putExtra("publisher", book.publisher);
                intent.putExtra("label", book.label);
                intent.putExtra("image", book.image);
                intent.putExtra("id", book.getId());

                startActivity(intent);
            }
        });
        gv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("確認")
                        .setMessage("この書籍を削除してもいいですか？")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Books book = (Books)gv.getItemAtPosition(position);
                                book.delete();
                                load();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
                return false;
            }
        });

        load();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_ISBN_REQUEST) {
            if(resultCode == RESULT_OK){
                load();
            }else{
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("エラー")
                        .setMessage("書籍の情報が正常に取得できませんでした。")
                        .setPositiveButton("OK", null)
                        .show();
            }

        }
        if (requestCode == READ_BARCORD_REQUEST) {
            if(resultCode == RESULT_OK){
                load();
            }else{
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("エラー")
                        .setMessage("書籍の情報が正常に取得できませんでした。")
                        .setPositiveButton("OK", null)
                        .show();
            }
        }
    }


    public void load() {

        adapter.clear();

        List<Books> books = new Select().from(Books.class).orderBy(order).execute();

        for (Books book : books) {
            adapter.add(book);
        }

        gv.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // メニューインフレーターを取得して、xmlのリソースファイルを使用してメニューにアイテムを追加
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // ItemのProviderを取得
        MenuItem item = menu.findItem(R.id.sort);

        // ActionProviderを継承したクラスをインスタンス化
        SortActionProvider actionProvider = new SortActionProvider(this);

        MenuItemCompat.setActionProvider(item, actionProvider);

        return super.onCreateOptionsMenu(menu);
    }

    private String order = "id ASC";
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        switch (id){
            case R.id.item1:
                order = "id ASC";
                break;
            case R.id.item2:
                order = "id DESC";
                break;
            case R.id.item3:
                order = "titleKana ASC";
                break;
            case R.id.item4:
                order = "titleKana DESC";
                break;
            case R.id.item5:
                order = "author ASC";
                break;
            case R.id.item6:
                order = "author DESC";
                break;
        }

        load();

        return super.onOptionsItemSelected(item);
    }
}
