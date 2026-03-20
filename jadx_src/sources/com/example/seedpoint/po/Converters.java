package com.example.seedpoint.po;

import java.util.Date;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class Converters {
    public static Long dateToTimestamp(Date date) {
        if (date == null) {
            return null;
        }
        return Long.valueOf(date.getTime());
    }

    public static Date fromTimestamp(Long l8) {
        if (l8 == null) {
            return null;
        }
        return new Date(l8.longValue());
    }
}
