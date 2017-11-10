package com.amlogic.toolkit.infocollection.javabean;

import java.io.Serializable;

/**
 * Created by Wenjie.Chen on 2017/8/8.
 */

public class SystemInfoBean implements Serializable{
    private String systemInfoTitle;
    private String systemInfoContent;

    public SystemInfoBean() {
    }

    public SystemInfoBean(String systemInfoTitle, String systemInfoContent) {
        this.systemInfoTitle = systemInfoTitle;
        this.systemInfoContent = systemInfoContent;
    }

    public String getSystemInfoTitle() {
        return systemInfoTitle;
    }

    public void setSystemInfoTitle(String systemInfoTitle) {
        this.systemInfoTitle = systemInfoTitle;
    }

    public String getSystemInfoContent() {
        return systemInfoContent;
    }

    public void setSystemInfoContent(String systemInfoContent) {
        this.systemInfoContent = systemInfoContent;
    }
}
