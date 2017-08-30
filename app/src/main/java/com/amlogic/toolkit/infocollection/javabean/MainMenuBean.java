package com.amlogic.toolkit.infocollection.javabean;

/**
 * Created by Wenjie.Chen on 2017/8/10.
 */

public class MainMenuBean {
    private String menuName;
    private String packageNmae;
    private String className;
    private boolean isService;

    public MainMenuBean(String packageNmae, String className, String menuName, boolean isService) {
        this.packageNmae = packageNmae;
        this.className = className;
        this.menuName = menuName;
        this.isService = isService;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getPackageNmae() {
        return packageNmae;
    }

    public void setPackageNmae(String packageNmae) {
        this.packageNmae = packageNmae;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public boolean getIsService() {
        return isService;
    }

    public void setIsService(boolean service) {
        isService = service;
    }
}
