package com.amlogic.toolkit.infocollection.javabean;

/**
 * Created by Wenjie.Chen on 2017/10/31.
 */

public class MemInfoBean {
    private String memTotal;
    private String memFree;
    private String memAvailable;
    private int availableProgress;

    public MemInfoBean() {
        super();
        this.memTotal = null;
        this.memFree = null;
        this.memAvailable = null;
        this.availableProgress = 0;
    }

    public MemInfoBean(String memTotal, String memFree, String memAvailable, int availableProgress){
        this.memTotal = memTotal;
        this.memFree = memFree;
        this.memAvailable = memAvailable;
        this.availableProgress = availableProgress;
    }

    public String getMemTotal() {
        return memTotal;
    }

    public void setMemTotal(String memTotal) {
        this.memTotal = memTotal;
    }

    public String getMemFree() {
        return memFree;
    }

    public void setMemFree(String memFree) {
        this.memFree = memFree;
    }

    public String getMemAvailable() {
        return memAvailable;
    }

    public void setMemAvailable(String memAvailable) {
        this.memAvailable = memAvailable;
    }

    public int getAvailableProgress() {
        return availableProgress;
    }

    public void setAvailableProgress(int availableProgress) {
        this.availableProgress = availableProgress;
    }

    public void toClean(){
        this.memTotal = null;
        this.memFree = null;
        this.memAvailable = null;
        this.availableProgress = 0;
    }
}
