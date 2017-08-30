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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wenjie.Chen on 2017/8/24.
 */

public class MutliMediaPlayerActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,View.OnClickListener{

    private static final int BUTTON_BASE_ID = 1000;
    private static final int EDITTEXT_BASE_ID = 2000;
    private static final String SDCARD_PATH = "/";
    private static final String TAG = "MutliMediaPlayerActivit";
    private String[] mediaplayerNum = new String[]{"1","2","3","4","5","6","7","8","9"};
    private Spinner mediaplayerSpinner;
    private TableLayout mediaplayerTable;
    private List<MutliMediaPlayerBean> mutliMediaPlayerBeanList;
    private MutliMediaPlayerAdapter mutliMediaPlayerAdapter;
    private int playerNum = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_mutli_mediaplayer_layout);

        mutliMediaPlayerBeanList = new ArrayList<MutliMediaPlayerBean>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.mediaplayer_toolbar);
        setSupportActionBar(toolbar);

        mediaplayerSpinner = (Spinner) findViewById(R.id.mediaplayer_num);
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),R.layout.spinner_item,mediaplayerNum);
        mediaplayerSpinner.setAdapter(adapter);
        mediaplayerSpinner.setOnItemSelectedListener(this);

        //mutliMediaPlayerAdapter = new MutliMediaPlayerAdapter(this,mutliMediaPlayerBeanList);
        //mediaplayerListView = (ListView) findViewById(R.id.mediaplayer_info);
        //mediaplayerListView.setAdapter(mutliMediaPlayerAdapter);

        mediaplayerTable = (TableLayout) findViewById(R.id.mediaplayer_info);

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
        EditText editText = (EditText) mediaplayerTable.findViewById((EDITTEXT_BASE_ID + (view.getId() - BUTTON_BASE_ID)));
        //editText.setText("Hello");
        selectFile(view.getId());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //EditText editText = (EditText) mediaplayerTable.findViewById((EDITTEXT_BASE_ID + (view.getId() - BUTTON_BASE_ID)));
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void selectFile(int playerId)
    {
        Intent intent = new Intent(this, FileManagerActivity.class);
        intent.putExtra("path", SDCARD_PATH);
        startActivityForResult(intent, playerId);
    }
}
