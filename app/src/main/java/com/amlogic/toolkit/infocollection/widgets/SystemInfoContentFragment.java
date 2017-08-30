package com.amlogic.toolkit.infocollection.widgets;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amlogic.toolkit.infocollection.R;

/**
 * Created by Wenjie.Chen on 2017/8/8.
 */

public class SystemInfoContentFragment extends Fragment {

    public SystemInfoContentFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_content,container,false);
        TextView infoContent = view.findViewById(R.id.info_content);
        infoContent.setText(getArguments().getString("content"));
        return view;
    }
}
