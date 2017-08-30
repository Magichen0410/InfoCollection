package com.amlogic.toolkit.infocollection.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amlogic.toolkit.infocollection.R;
import com.amlogic.toolkit.infocollection.javabean.SystemInfoBean;

import java.util.List;

/**
 * Created by Wenjie.Chen on 2017/8/8.
 */

public class SystemInfoAdapter extends BaseAdapter {
    private List<SystemInfoBean> systemInfoBeens;
    private Context context;

    public SystemInfoAdapter(Context context, List<SystemInfoBean> systemInfoBeens) {
        this.context = context;
        this.systemInfoBeens = systemInfoBeens;
    }

    @Override
    public int getCount() {
        return systemInfoBeens.size();
    }

    @Override
    public Object getItem(int position) {
        return systemInfoBeens.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item,null);
            viewHolder = new ViewHolder();
            viewHolder.systemInfoNameList = view.findViewById(R.id.systeminfo_item_title);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.systemInfoNameList.setText(systemInfoBeens.get(position).getSystemInfoTitle());

        return view;
    }

    private class ViewHolder {
        private TextView systemInfoNameList;
    }
}
