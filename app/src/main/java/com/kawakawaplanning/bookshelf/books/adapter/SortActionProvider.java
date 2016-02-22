package com.kawakawaplanning.bookshelf.books.adapter;

import android.content.Context;
import android.support.v4.view.ActionProvider;
import android.view.MenuInflater;
import android.view.SubMenu;
import android.view.View;

import com.kawakawaplanning.bookshelf.R;

/**
 * Created by KP on 16/02/18.
 */
public class SortActionProvider extends ActionProvider{

    private static final String TAG = "SimpleActionProvider";
    private Context mContext;

    public SortActionProvider(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.mContext = context;
    }

    /*
     * MenuItemが作成されるタイミングで呼ばれ、作成されるMenuItemに対して何か変更を加える(例えばある条件の場合だけ背景画像を変えるなど)
     * 場合に戻り値として表示させたいViewクラスのオブジェクトを指定すると、ActionBarに戻り値で返したViewが表示されるようになります
     *
     * @see android.support.v4.view.ActionProvider#onCreateActionView()
     */
    @Override
    public View onCreateActionView() {
        return null;
    }

    /*
     * サブメニューがあることを伝えるためにhasSunMenu()ではtrueを返します
     *
     * @see android.support.v4.view.ActionProvider#hasSubMenu()
     */
    @Override
    public boolean hasSubMenu() {
        return true;
    }

    /*
     * メニュー表示のたびにonPrepareSubMenu()が呼ばれるので、そこでサブメニューを作成
     *
     * @see
     * android.support.v4.view.ActionProvider#onPrepareSubMenu(android.view.
     * SubMenu)
     */
    @Override
    public void onPrepareSubMenu(SubMenu subMenu) {

        // クリアしないとサブメニューがどんどん追加されてしまう。
        subMenu.clear();
        MenuInflater inflator = new MenuInflater(mContext);
        inflator.inflate(R.menu.provider_menu, subMenu);
    }
}