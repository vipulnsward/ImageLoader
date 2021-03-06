package com.novoda.imageloader.demo.activity.base;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;

import com.novoda.imageloader.core.ImageManager;
import com.novoda.imageloader.core.model.ImageTagFactory;
import com.novoda.imageloader.demo.DemoApplication;
import com.novoda.imageloader.demo.R;

public abstract class SingleTableBaseListActivity extends BaseListActivity {

    private static final String[] FROM = new String[] { "url" };
    private static final int[] TO = new int[] { R.id.list_item_image };

    // TODO add this to your class
    protected ImageManager imageManager;
    protected ImageTagFactory imageTagFactory;

    //

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_table_base_list_activity);
        // TODO add this to your class
        prepareLoader();
        //
        setAdapter();
    }

    protected void prepareLoader() {
        imageManager = DemoApplication.getImageLoader();
        imageTagFactory = new ImageTagFactory(this, R.drawable.bg_img_loading);
        imageTagFactory.setErrorImageId(R.drawable.bg_img_notfound);
    }

    protected void load(View view) {
        // TODO add this to your class
        imageManager.getLoader().load((ImageView) view);
        //
    }

    protected int getImageItem() {
        return R.layout.image_item;
    }

    private ViewBinder getViewBinder() {
        return new ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                setTag(view, cursor, columnIndex);
                return true;
            }

        };
    }

    protected void setTag(View view, Cursor cursor, int columnIndex) {
        String url = cursor.getString(columnIndex);
        ((ImageView) view).setTag(imageTagFactory.build(url));
        load(view);
    }

    private SimpleCursorAdapter initAdapter() {
        return new SimpleCursorAdapter(this, getImageItem(), getCursor(), FROM, TO);
    }

    private Cursor getCursor() {
        return managedQuery(Uri.parse("content://com.novoda.imageloader.demo/" + getTableName()), null, null, null,
                null);
    }

    protected abstract String getTableName();

    private void setAdapter() {
        SimpleCursorAdapter adapter = initAdapter();
        ListView lv = getListView();
        ViewBinder binder = getViewBinder();
        if (binder != null) {
            adapter.setViewBinder(binder);
        }
        lv.setAdapter(adapter);
    }

}
