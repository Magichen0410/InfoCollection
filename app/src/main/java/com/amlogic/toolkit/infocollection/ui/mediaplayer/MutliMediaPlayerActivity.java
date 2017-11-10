package com.amlogic.toolkit.infocollection.ui.mediaplayer;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.amlogic.toolkit.infocollection.R;
import com.amlogic.toolkit.infocollection.adapter.MutliMediaPlayerAdapter;
import com.amlogic.toolkit.infocollection.javabean.MutliMediaPlayerBean;
import com.amlogic.toolkit.infocollection.utils.DensityUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wenjie.Chen on 2017/8/24.
 */

public class MutliMediaPlayerActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,View.OnClickListener{

    private static final int BUTTON_BASE_ID = 1000;
    private static final int EDITTEXT_BASE_ID = 2000;
    private static final String SDCARD_PATH = "/storage/external_storage";
    private static final String URLCONFIGPATH = "/system/package_config/urilist.txt";
    private static final String TAG = "MutliMediaPlayerActivit";
    private String[] mediaplayerNum = new String[]{"1","2","3","4","5","6","7","8","9"};
//    private String[] mediaplayerType = new String[]{"MediaPlayer", "CTCPlayer"};
    private String[] filePaths = new String[9];
    private Spinner mediaplayerSpinner;
//    private Spinner mediaplayerTypeSpinner;
    private TableLayout mediaplayerTable;
    private List<MutliMediaPlayerBean> mutliMediaPlayerBeanList;
    private ArrayList<String> urlList;
    private int playerNum = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_mutli_mediaplayer_layout);

        initData();

        mutliMediaPlayerBeanList = new ArrayList<MutliMediaPlayerBean>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.mediaplayer_toolbar);
        setSupportActionBar(toolbar);

        mediaplayerSpinner = (Spinner) findViewById(R.id.mediaplayer_num);
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),R.layout.spinner_item,mediaplayerNum);
        mediaplayerSpinner.setAdapter(adapter);
        mediaplayerSpinner.setOnItemSelectedListener(this);

        /*mediaplayerTypeSpinner = (Spinner) findViewById(R.id.mediaplayer_type);
        ArrayAdapter mediaPlayerTypeadapter = new ArrayAdapter(getApplicationContext(),R.layout.spinner_item,mediaplayerType);
        mediaplayerTypeSpinner.setAdapter(mediaPlayerTypeadapter);
        mediaplayerTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/

        //mutliMediaPlayerAdapter = new MutliMediaPlayerAdapter(this,mutliMediaPlayerBeanList);
        //mediaplayerListView = (ListView) findViewById(R.id.mediaplayer_info);
        //mediaplayerListView.setAdapter(mutliMediaPlayerAdapter);

        mediaplayerTable = (TableLayout) findViewById(R.id.mediaplayer_info);
        Button button = (Button) findViewById(R.id.mediaplayer_play);
        button.setOnClickListener(this);

    }

    private void addRow(Context context,int playerId)
    {
        TableRow tableRow = new TableRow(context);
        TextView textView = new TextView(context);
        EditText editText = new EditText(context);
        Button button = new Button(context);



        textView.setWidth(DensityUtil.dip2px(this,1));
        textView.setTextSize(20);
        textView.setText("第"+ playerId + "个播放器：");
        textView.setGravity(Gravity.END|Gravity.CENTER);

        editText.setWidth(DensityUtil.dip2px(this,1));
        editText.setId(EDITTEXT_BASE_ID+playerId);
        if (urlList != null) {
            if (playerId <= urlList.size()) {
                editText.setText(urlList.get(playerId - 1));
                filePaths[playerId - 1] = urlList.get(playerId - 1);
            }
        }

        button.setText(R.string.mediaplayer_browse);
        button.setWidth(DensityUtil.dip2px(this,1));
        button.setOnClickListener(this);
        button.setId(BUTTON_BASE_ID+playerId);

        tableRow.addView(textView);
        tableRow.addView(editText);
        tableRow.addView(button);

        mediaplayerTable.addView(tableRow);
    }

        @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        mediaplayerTable.removeAllViews();
        playerNum = Integer.valueOf(mediaplayerNum[pos]);
        for (int i = 0; i < playerNum; i++) {
            addRow(this,i+1);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        Log.e(TAG, "onClick: getId = " + view.getId());
        if (view.getId() == Integer.valueOf(R.id.mediaplayer_play)){
            Intent intent = new Intent(this, VideoPlayerActivity.class);
            intent.putExtra("count", playerNum);
            intent.putExtra("filePaths", filePaths);
            startActivity(intent);
        } else {
            EditText editText = (EditText) mediaplayerTable.findViewById((EDITTEXT_BASE_ID + (view.getId() - BUTTON_BASE_ID)));
            selectFile(view.getId());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String filePath = null;
        int filePathId = 0;
        //EditText editText = (EditText) mediaplayerTable.findViewById((EDITTEXT_BASE_ID + (view.getId() - BUTTON_BASE_ID)));
        if ((-1 == resultCode) && (requestCode >= BUTTON_BASE_ID + 1)
                && (requestCode <= BUTTON_BASE_ID + 9)) {
            filePath = data.getStringExtra("path");
            EditText editText = mediaplayerTable.findViewById((EDITTEXT_BASE_ID
                    + (requestCode - BUTTON_BASE_ID)));
            editText.setText(filePath);
            filePathId = requestCode-BUTTON_BASE_ID -1;
            filePaths[filePathId] = filePath;
        }
    }

    private void selectFile(int playerId)
    {
        Intent intent = new Intent(this, FileManagerActivity.class);
        intent.putExtra("path", SDCARD_PATH);
        intent.putExtra("mediaplayerType", "MediaPlayer");
        startActivityForResult(intent, playerId);
    }

    public  ArrayList<String> getFileSite(InputStream fileInputStream) {
        InputStreamReader inputStreamReader = null;

        if (fileInputStream == null){
            return null;
        }
        inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);

        ArrayList<String> txtContext = new ArrayList<String>();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                txtContext.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return txtContext;
    }

    public ArrayList<String> getFileStream(String filepath) {
        File file = null;
        file = new File(filepath);
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return getFileSite(fileInputStream);
    }

    private void initData(){
        urlList = getFileStream(URLCONFIGPATH);
        Log.e(TAG, "initData: ok");
    }
}
