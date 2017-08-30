package com.amlogic.toolkit.infocollection.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amlogic.toolkit.infocollection.R;
import com.amlogic.toolkit.infocollection.javabean.MutliMediaPlayerBean;

import java.util.List;

/**
 * Created by Wenjie.Chen on 2017/8/28.
 */

public class MutliMediaPlayerAdapter extends BaseAdapter {
    private List<MutliMediaPlayerBean> mutliMediaPlayerBeans;
    private Context context;

    public MutliMediaPlayerAdapter(Context context, List<MutliMediaPlayerBean> mutliMediaPlayerBeans) {
        this.context = context;
        this.mutliMediaPlayerBeans = mutliMediaPlayerBeans;
    }

    @Override
    public int getCount() {
        return mutliMediaPlayerBeans.size();
    }

    @Override
    public Object getItem(int pos) {
        return mutliMediaPlayerBeans.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        int mediaPlayerId = 0;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.mediaplayer_item,null);
            viewHolder = new ViewHolder();
            viewHolder.mediaPlayerText = view.findViewById(R.id.mediaplayer_text_name);
            viewHolder.mediaPlayerEdit = view.findViewById(R.id.mediaplayer_edit_name);
            viewHolder.mediaPlayerButton = view.findViewById(R.id.mediaplayer_button_name);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        mediaPlayerId = i+1;
        viewHolder.mediaPlayerText.setText("第"+ mediaPlayerId +"个播放器：");
        viewHolder.mediaPlayerButton.setText(R.string.mediaplayer_browse);

        return view;
    }


    private class ViewHolder {
        private TextView mediaPlayerText;
        private EditText mediaPlayerEdit;
        private Button mediaPlayerButton;

    }
}
