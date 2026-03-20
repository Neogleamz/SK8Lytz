package com.example.seedpoint.event;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class Property {
    private String app_name;
    private String app_version;
    private String brand;
    private String device_id;
    private String lib_version;
    private String os_version;
    private String package_id;
    private int screen_height;
    private int screen_width;
    private String sys_model;
    private String sys_os;

    public String getApp_name() {
        return this.app_name;
    }

    public String getApp_version() {
        return this.app_version;
    }

    public String getBrand() {
        return this.brand;
    }

    public String getDevice_id() {
        return this.device_id;
    }

    public String getLib_version() {
        return this.lib_version;
    }

    public String getOs_version() {
        return this.os_version;
    }

    public String getPackage_id() {
        return this.package_id;
    }

    public int getScreen_height() {
        return this.screen_height;
    }

    public int getScreen_width() {
        return this.screen_width;
    }

    public String getSys_model() {
        return this.sys_model;
    }

    public String getSys_os() {
        return this.sys_os;
    }

    public void setApp_name(String str) {
        this.app_name = str;
    }

    public void setApp_version(String str) {
        this.app_version = str;
    }

    public void setBrand(String str) {
        this.brand = str;
    }

    public void setDevice_id(String str) {
        this.device_id = str;
    }

    public void setLib_version(String str) {
        this.lib_version = str;
    }

    public void setOs_version(String str) {
        this.os_version = str;
    }

    public void setPackage_id(String str) {
        this.package_id = str;
    }

    public void setScreen_height(int i8) {
        this.screen_height = i8;
    }

    public void setScreen_width(int i8) {
        this.screen_width = i8;
    }

    public void setSys_model(String str) {
        this.sys_model = str;
    }

    public void setSys_os(String str) {
        this.sys_os = str;
    }

    public String toString() {
        return "Property{os_version='" + this.os_version + "', sys_model='" + this.sys_model + "', sys_os='" + this.sys_os + "', screen_width=" + this.screen_width + ", screen_height=" + this.screen_height + ", brand='" + this.brand + "', app_version='" + this.app_version + "', device_id='" + this.device_id + "', app_name='" + this.app_name + "', package_id='" + this.package_id + "', lib_version='" + this.lib_version + "'}";
    }
}
