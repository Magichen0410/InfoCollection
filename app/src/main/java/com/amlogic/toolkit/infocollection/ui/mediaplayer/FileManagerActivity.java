package com.amlogic.toolkit.infocollection.ui.mediaplayer;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.amlogic.toolkit.infocollection.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;

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

        listView = findViewById(android.R.id.list);
        listItem = new ArrayList<HashMap<String, Object>>();
        simpleAdapter = new SimpleAdapter(this, listItem, R.layout.file_list_item ,
                new String[] { "image_view", "text_file_name" },
                new int[] { R.id.image_view, R.id.text_file_name});
        setListAdapter(simpleAdapter);
        updateFileList(currPath);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            File[] fileLists;
            String filePath = null;
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                if ((listItem != null) && (listItem.get(pos) != null)
                        && (((HashMap)listItem.get(pos)).get("text_file_path") != null)) {
                    filePath = ((HashMap)listItem.get(pos)).get("text_file_path").toString();
                    File fileObj = new File(filePath);
                    if (fileObj.isFile()) {
                        Intent intent = new Intent(FileManagerActivity.this,MutliMediaPlayerActivity.class);
                        intent.putExtra("path", filePath);
                        setResult(-1, intent);
                        finish();
                    } else {
                        updateFileList(filePath);
                    }
                }
            }
        });
    }

    private void updateFileList(String path) {
        listItem.clear();
        File[] fileChilds;
        Object FileObject = new File(path);
        HashMap hashMap = new HashMap();
        hashMap.put("image_view", Integer.valueOf(R.drawable.filemanager_floder));
        hashMap.put("text_file_name", "..");
        if (path.equals("/")) {
            hashMap.put("text_file_path", "/");
        }
        listItem.add(hashMap);
        fileChilds = ((File)FileObject).listFiles();
        if (fileChilds != null) {
            hashMap.put("text_file_path", ((File)FileObject).getParentFile());
        }

        int i = 0;
        while (i < fileChilds.length)
        {
            if (fileChilds[i].isFile())
            {
                HashMap hashMapFileObject = new HashMap();
                hashMapFileObject.put("image_view", Integer.valueOf(R.drawable.filemanager_file));
                hashMapFileObject.put("text_file_name", fileChilds[i].getName());
                hashMapFileObject.put("text_file_path", fileChilds[i].getPath());
                if (fileChilds[i].canRead()) {
                    listItem.add(hashMapFileObject);
                }
            }

            if (fileChilds[i].isDirectory())
            {
                HashMap hashMapDirObject = new HashMap();
                hashMapDirObject.put("image_view", Integer.valueOf(R.drawable.filemanager_floder));
                hashMapDirObject.put("text_file_name", fileChilds[i].getName());
                hashMapDirObject.put("text_file_path", fileChilds[i].getPath());
                if (fileChilds[i].canRead()) {
                    listItem.add(hashMapDirObject);
                }
            }
            i += 1;
        }
        Collections.sort(this.listItem, new MyComparator());
        ((SimpleAdapter)getListView().getAdapter()).notifyDataSetChanged();
        listView.setSelection(0);
        listView.requestFocus();
    }

    private class MyComparator implements Comparator<HashMap<String, Object>>
    {
        private MyComparator() {}

        public int compare(HashMap<String, Object> hashMap1, HashMap<String, Object> hashMap2)
        {
            String hashMap1Str = null;
            String hashMap2Str = null;
            int i = ((Integer)hashMap1.get("image_view")).intValue();
            int j = ((Integer)hashMap2.get("image_view")).intValue();
            hashMap1Str = ((String)hashMap1.get("text_file_name")).toUpperCase(Locale.getDefault());
            hashMap2Str = ((String)hashMap2.get("text_file_name")).toUpperCase(Locale.getDefault());
            if (i == j) {
                return hashMap1Str.compareTo(hashMap2Str);
            }
            if (i == R.drawable.filemanager_floder) {
                return -1;
            }
            return 1;
        }
    }
}
