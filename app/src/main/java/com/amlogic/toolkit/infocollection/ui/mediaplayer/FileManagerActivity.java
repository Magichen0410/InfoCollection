package com.amlogic.toolkit.infocollection.ui.mediaplayer;

import android.app.ListActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.amlogic.toolkit.infocollection.R;

import java.util.ArrayList;
import java.util.HashMap;

public class FileManagerActivity extends ListActivity {

    private static final String TAG = "FileManagerActivity";
    private SimpleAdapter simpleAdapter;
    private ArrayList<HashMap<String, Object>> listItem;
    private String currPath;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_manager);
        currPath = getIntent().getStringExtra("path");

        if (TextUtils.isEmpty(currPath))
        {
            Log.e(TAG, "onCreate: currPath is Empty");
            return;
        }

        listItem = new ArrayList<HashMap<String, Object>>();
        //simpleAdapter = new SimpleAdapter(this, this.listItem, , new String[] { "image_view", "text_file_name" }, new int[] { ,  });
        setListAdapter(simpleAdapter);
    }
}
