package com.kawakawaplanning.bookshelf.dojin.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import com.kawakawaplanning.bookshelf.dojin.model.DojinBookData;
import com.kawakawaplanning.bookshelf.dojin.adapter.DojinBookShelfAdapter;
import com.kawakawaplanning.bookshelf.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by KP on 16/02/15.
 */
public class DojinMainActivity extends AppCompatActivity {

    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.gridView)
    GridView gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dojin);

        ButterKnife.bind(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DojinBookShelfAdapter adapter = new DojinBookShelfAdapter(getApplicationContext(), R.layout.list_card);

        DojinBookData bd1 = new DojinBookData();
        bd1.setTitle("面舵いっぱいいっぱいの艦これまとめ本2");
        bd1.setCircle("面舵いっぱいいっぱい");
        bd1.setEvent("コミックマーケット 89");
        bd1.setBookId(861033);
        adapter.add(bd1);

        DojinBookData bd2 = new DojinBookData();
        bd2.setTitle("初雪29歳");
        bd2.setCircle("にたか屋");
        bd2.setEvent("コミックマーケット 89");
        bd2.setBookId(865251);
        adapter.add(bd2);

        DojinBookData bd3 = new DojinBookData();
        bd3.setTitle("カンムス・ア・ゴーゴー");
        bd3.setCircle("ポンコツ");
        bd3.setEvent("コミックマーケット 88");
        bd3.setBookId(830210);
        adapter.add(bd3);

        DojinBookData bd4 = new DojinBookData();
        bd4.setTitle("司令官がちいさくなったのです");
        bd4.setCircle("だーくさいどるーむ");
        bd4.setEvent("コミックマーケット 89");
        bd4.setBookId(863345);
        adapter.add(bd4);

        DojinBookData bd5 = new DojinBookData();
        bd5.setTitle("艦娘日誌-吹雪型の一日-");
        bd5.setCircle("こるり屋");
        bd5.setEvent("不詳");
        bd5.setBookId(660549);
        adapter.add(bd5);

        gv.setNumColumns(3);//GridView.AUTO_FIT);
        gv.setHorizontalSpacing(10);
//        gv.setColumnWidth(80);
        gv.setDrawSelectorOnTop(true);
        gv.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

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

        return super.onOptionsItemSelected(item);
    }
}
