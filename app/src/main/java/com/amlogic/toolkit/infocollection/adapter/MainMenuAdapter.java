package com.amlogic.toolkit.infocollection.adapter;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.amlogic.toolkit.infocollection.R;
import com.amlogic.toolkit.infocollection.javabean.MainMenuBean;
import com.amlogic.toolkit.infocollection.utils.OtherUtil;

import java.util.List;

/**
 * Created by Wenjie.Chen on 2017/8/10.
 */

public class MainMenuAdapter extends BaseAdapter {

    private List<MainMenuBean> mainMenuBeanList;
    private Context context;

    public MainMenuAdapter(Context context, List<MainMenuBean> mainMenuBeanList) {
        this.context = context;
        this.mainMenuBeanList = mainMenuBeanList;
    }

    @Override
    public int getCount() {
        return mainMenuBeanList.size();
    }

    @Override
    public Object getItem(int postion) {
        return mainMenuBeanList.get(postion);
    }

    @Override
    public long getItemId(int postion) {
        return postion;
    }

    @Override
    public View getView(final int postion, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        LayoutInflater mInflater = LayoutInflater.from(context);
        if (view == null) {
            view = mInflater.inflate(R.layout.menu_item,null);
            MainMenuBean  mainMenuBean = mainMenuBeanList.get(postion);
            viewHolder = new ViewHolder();
            viewHolder.mainMenuTextView = view.findViewById(R.id.menu_name_item);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (mainMenuBeanList.get(postion).getIsService()) {
            String menuName = null;
            if (OtherUtil.isServiceRunning(context,mainMenuBeanList.get(postion).getClassName())) {
                menuName = "关闭" + mainMenuBeanList.get(postion).getMenuName();
                viewHolder.mainMenuTextView.setText(menuName);
            } else {
                menuName = "开启" + mainMenuBeanList.get(postion).getMenuName();
                viewHolder.mainMenuTextView.setText(menuName);
            }
        } else {
            viewHolder.mainMenuTextView.setText(mainMenuBeanList.get(postion).getMenuName());
        }

        return view;
    }

    private class ViewHolder {
        TextView mainMenuTextView;
    }
}
