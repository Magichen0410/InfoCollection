package com.amlogic.toolkit.infocollection.widgets;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.amlogic.toolkit.infocollection.R;
import com.amlogic.toolkit.infocollection.adapter.SystemInfoAdapter;
import com.amlogic.toolkit.infocollection.javabean.SystemInfoBean;

import java.util.ArrayList;

/**
 * Created by Wenjie.Chen on 2017/8/8.
 */

public class SystemInfoOptionFragment extends Fragment {

    private FragmentManager fragmentManager = null;
    private ArrayList<SystemInfoBean> systemInfoBeanArrayList;
    private ListView optionListView;

    public SystemInfoOptionFragment() {
    }


/*  //当重新创建时，可能会丢失数据 recreate走的是默认构造函数。
    public SystemInfoOptionFragment(FragmentManager fragmentManager, ArrayList<SystemInfoBean> systemInfoBeanArrayList) {
        this.fragmentManager = fragmentManager;
        this.systemInfoBeanArrayList = systemInfoBeanArrayList;
    }*/

    private class  OptionListItemListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            SystemInfoContentFragment systemInfoContentFragment = new SystemInfoContentFragment();
            Bundle bundle = new Bundle();
            bundle.putString("content", systemInfoBeanArrayList.get(position).getSystemInfoContent());
            systemInfoContentFragment.setArguments(bundle);
            //获取Activity的控件
            TextView systeminfoTitle = getActivity().findViewById(R.id.systeminfo_title);
            systeminfoTitle.setText(systemInfoBeanArrayList.get(position).getSystemInfoTitle());
            //加上Fragment替换动画
            fragmentTransaction.setCustomAnimations(R.animator.fragment_slide_left_enter,
                    R.animator.fragment_slide_left_exit);
            fragmentTransaction.replace(R.id.systeminfo_content_layout,systemInfoContentFragment);
            //调用addToBackStack将Fragment添加到栈中
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

            this.systemInfoBeanArrayList = (ArrayList<SystemInfoBean>) this.getArguments().getSerializable("systeminfobeanlist");
            if (fragmentManager == null || systemInfoBeanArrayList == null) {
                View view = inflater.inflate(R.layout.fg_optionlist,container,false);
                optionListView = (ListView) view.findViewById(R.id.option_list);
                SystemInfoAdapter systemInfoAdapter = new SystemInfoAdapter(getActivity(),systemInfoBeanArrayList);
                optionListView.setAdapter(systemInfoAdapter);
                return view;
            } else {
                View view = inflater.inflate(R.layout.fg_optionlist,container,false);
                optionListView = (ListView) view.findViewById(R.id.option_list);
                SystemInfoAdapter systemInfoAdapter = new SystemInfoAdapter(getActivity(),systemInfoBeanArrayList);
                optionListView.setAdapter(systemInfoAdapter);
                optionListView.setOnItemClickListener(new OptionListItemListener());
                return view;
            }
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }
}
