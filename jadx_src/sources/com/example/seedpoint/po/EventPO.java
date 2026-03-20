package com.example.seedpoint.po;

import java.util.Date;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class EventPO {
    public Date createDate = new Date();
    public String eventLog;
    private long objectId;

    public EventPO(String str) {
        this.eventLog = str;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public String getEventLog() {
        return this.eventLog;
    }

    public long getObjectId() {
        return this.objectId;
    }

    public void setCreateDate(Date date) {
        this.createDate = date;
    }

    public void setEventLog(String str) {
        this.eventLog = str;
    }

    public void setObjectId(long j8) {
        this.objectId = j8;
    }
}
