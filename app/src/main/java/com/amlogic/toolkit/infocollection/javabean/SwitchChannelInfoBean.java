package com.amlogic.toolkit.infocollection.javabean;

/**
 * Created by Wenjie.Chen on 2017/8/2.
 */

public class SwitchChannelInfoBean {
    private String switchChannelTimeName;
    private String switchChannelTimeValue;

    public SwitchChannelInfoBean(String switchChannelTimeName, String switchChannelTimeValue) {
        this.switchChannelTimeName = switchChannelTimeName;
        this.switchChannelTimeValue = switchChannelTimeValue;
    }

    public String getSwitchChannelTimeName() {
        return switchChannelTimeName;
    }

    public void setSwitchChannelTimeName(String switchChannelTimeName) {
        switchChannelTimeName = switchChannelTimeName;
    }

    public String getSwitchChannelTimeValue() {
        return switchChannelTimeValue;
    }

    public void setSwitchChannelTimeValue(String switchChannelTimeValue) {
        switchChannelTimeValue = switchChannelTimeValue;
    }
}
