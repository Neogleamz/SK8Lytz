package com.example.seedpoint.event;

import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class EventData {
    private Map<String, Object> attributes;
    private String event_id;
    private long event_time;
    private String event_type;
    private Location location;
    private NetworkInfo networkInfo;
    private Property properties;
    private String user_id = "Unknown";

    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    public String getEvent_id() {
        return this.event_id;
    }

    public long getEvent_time() {
        return this.event_time;
    }

    public String getEvent_type() {
        return this.event_type;
    }

    public Location getLocation() {
        return this.location;
    }

    public NetworkInfo getNetworkInfo() {
        return this.networkInfo;
    }

    public Property getProperties() {
        return this.properties;
    }

    public String getUser_id() {
        return this.user_id;
    }

    public void setAttributes(Map<String, Object> map) {
        this.attributes = map;
    }

    public void setEvent_id(String str) {
        this.event_id = str;
    }

    public void setEvent_time(long j8) {
        this.event_time = j8;
    }

    public void setEvent_type(String str) {
        this.event_type = str;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setNetworkInfo(NetworkInfo networkInfo) {
        this.networkInfo = networkInfo;
    }

    public void setProperties(Property property) {
        this.properties = property;
    }

    public void setUser_id(String str) {
        this.user_id = str;
    }

    public String toString() {
        return "EventData{event_id='" + this.event_id + "', event_time=" + this.event_time + ", event_type='" + this.event_type + "', user_id='" + this.user_id + "', properties=" + this.properties + ", networkInfo=" + this.networkInfo + ", location=" + this.location + ", attributes=" + this.attributes + '}';
    }
}
