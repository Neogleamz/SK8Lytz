package com.example.seedpoint.utils;

import com.daimajia.numberprogressbar.BuildConfig;
import java.util.UUID;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class UUIDUtil {
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", BuildConfig.FLAVOR);
    }
}
