package com.amlogic.toolkit.infocollection.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amlogic.toolkit.infocollection.R;
import com.amlogic.toolkit.infocollection.constant.SwitchChannelInfoEnum;
import com.amlogic.toolkit.infocollection.javabean.SwitchChannelInfoBean;

import java.util.List;

/**
 * Created by Wenjie.Chen on 2017/8/2.
 */

public class SwitchChannelAdapter extends BaseAdapter {
    private static final int TOTALTIME = 1;
    private static final int TOTALTIME_NOWAIT_KEYFRAME = 2;
    private static final int TOTALTIME_INIT_FIRSTPIC = 3;
    private static final int TOTALTIME_KEY_FIRSTPIC = 4;
    private static final int TOTALTIME_NO_PRESS_KEY = 5;
    private static final int PRESS_CLOSE_TIME = 6;
    private static final int CODEC_CLOSE_TIME = 7;
    private static final int CLOSE_INIT_TIME = 8;
    private static final int CODEC_INIT_TIME = 9;
    private static final int INIT_FIRSTCHECKIN_TIME = 10;
    private static final int FIRSTCHECKIN_CMD_TIME = 11;
    private static final int FIRSTCHECKOUT_CMD_TIME = 12;
    private static final int FIRSTCHECKOUT_DECODED_TIME = 13;
    private static final int DECODED_FRAME0_TIME = 14;
    private static final int DI0_FIRSTOUT_TIME = 15;
    private static final int FRAME0_FRAME1_TIME = 16;
    private static final int DI1_FIRSTOUT_TIME = 17;
    private static final int DI2_FIRSTOUT_TIME = 18;
    private static final int FRAME1_FRAME2_TIME = 19;
    private static final int DI_FIRSTPIC_TIME = 20;
    private static final String TAG = "SwitchChannelAdapter";

    private List<SwitchChannelInfoBean> switchChannelInfoBeens;
    private Context context;

    public SwitchChannelAdapter(Context context, List<SwitchChannelInfoBean> switchChannelInfoBeens) {
        this.switchChannelInfoBeens = switchChannelInfoBeens;
        this.context = context;
    }

    @Override
    public int getCount() {
        return switchChannelInfoBeens.size();
    }

    @Override
    public Object getItem(int i) {
        return switchChannelInfoBeens.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        final SwitchChannelInfoBean switchChannelInfoBean = switchChannelInfoBeens.get(i);
        LayoutInflater mInflater = LayoutInflater.from(context);
        if (view == null) {
            view = mInflater.inflate(R.layout.switch_channel_time_item,null);
            holder = new ViewHolder();
            holder.switchChannelTimeName = (TextView) view.findViewById(R.id.switch_channel_time_name);
            holder.switchChannelTimeValue = (TextView) view.findViewById(R.id.switch_channel_time_value);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        //Log.i(TAG, "getView: SwitchChannelTimeName : " + switchChannelInfoBean.getSwitchChannelTimeName());

        if (i == 0 || i ==1) {
            //holder.switchChannelTimeName.setText(switchChannelInfoBean.getSwitchChannelTimeName());
            setSwitchChannelTimeName(holder.switchChannelTimeName,switchChannelInfoBean.getSwitchChannelTimeName());
            holder.switchChannelTimeName.setTextColor(context.getResources().getColor(R.color.yellow));
            holder.switchChannelTimeName.setTextSize(17);
            holder.switchChannelTimeValue.setText(switchChannelInfoBean.getSwitchChannelTimeValue());
            holder.switchChannelTimeValue.setTextColor(context.getResources().getColor(R.color.yellow));
            holder.switchChannelTimeValue.setTextSize(17);
        } else {
            //holder.switchChannelTimeName.setText(switchChannelInfoBean.getSwitchChannelTimeName());
            setSwitchChannelTimeName(holder.switchChannelTimeName,switchChannelInfoBean.getSwitchChannelTimeName());
            holder.switchChannelTimeName.setTextColor(context.getResources().getColor(R.color.white));
            holder.switchChannelTimeName.setTextSize(15);
            holder.switchChannelTimeValue.setText(switchChannelInfoBean.getSwitchChannelTimeValue());
            holder.switchChannelTimeValue.setTextColor(context.getResources().getColor(R.color.white));
            holder.switchChannelTimeName.setTextSize(15);
        }

        return view;
    }

    private void setSwitchChannelTimeName(TextView textView, String key) {
        int switchChannelTimeKey = 0;

        switchChannelTimeKey = SwitchChannelInfoEnum.getKeyBySwitchChannelInfoName(key);

        switch (switchChannelTimeKey){
            case TOTALTIME:
                textView.setText(R.string.total_time);
                break;
            case TOTALTIME_NOWAIT_KEYFRAME:
                textView.setText(R.string.total_time_nowait_keyframe);
                break;
            case TOTALTIME_INIT_FIRSTPIC:
                textView.setText(R.string.total_time_init_firstpic);
                break;
            case TOTALTIME_KEY_FIRSTPIC:
                textView.setText(R.string.total_time_key_firstpic);
                break;
            case TOTALTIME_NO_PRESS_KEY:
                textView.setText(R.string.totoal_time_no_press_key);
                break;
            case PRESS_CLOSE_TIME:
                textView.setText(R.string.press_close_time);
                break;
            case CODEC_CLOSE_TIME:
                textView.setText(R.string.codec_close_time);
                break;
            case CLOSE_INIT_TIME:
                textView.setText(R.string.close_init_time);
                break;
            case CODEC_INIT_TIME:
                textView.setText(R.string.codec_init_time);
                break;
            case INIT_FIRSTCHECKIN_TIME:
                textView.setText(R.string.init_firstcheckin_time);
                break;
            case FIRSTCHECKIN_CMD_TIME:
                textView.setText(R.string.firstcheckin_cmd1_time);
                break;
            case FIRSTCHECKOUT_CMD_TIME:
                textView.setText(R.string.cmd1_firstcheckout_time);
                break;
            case FIRSTCHECKOUT_DECODED_TIME:
                textView.setText(R.string.firstcheckout_decoded_time);
                break;
            case DECODED_FRAME0_TIME:
                textView.setText(R.string.decoded_frame0_time);
                break;
            case DI0_FIRSTOUT_TIME:
                textView.setText(R.string.di0_firstout_time);
                break;
            case FRAME0_FRAME1_TIME:
                textView.setText(R.string.frame0_frame1_time);
                break;
            case DI1_FIRSTOUT_TIME:
                textView.setText(R.string.di1_firstout_time);
                break;
            case DI2_FIRSTOUT_TIME:
                textView.setText(R.string.di2_firstout_time);
                break;
            case FRAME1_FRAME2_TIME:
                textView.setText(R.string.frame1_frame2_time);
                break;
            case DI_FIRSTPIC_TIME:
                textView.setText(R.string.di_firstpic_time);
                break;
            default:
                break;
        }
    }

    private class ViewHolder {
        public TextView switchChannelTimeName;
        public TextView switchChannelTimeValue;
    }
}
