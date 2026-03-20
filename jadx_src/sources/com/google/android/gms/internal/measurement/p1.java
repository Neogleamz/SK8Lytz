package com.google.android.gms.internal.measurement;

import java.net.URL;
import java.net.URLConnection;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class p1 {

    /* renamed from: a  reason: collision with root package name */
    private static p1 f12419a = new o1();

    public static synchronized p1 a() {
        p1 p1Var;
        synchronized (p1.class) {
            p1Var = f12419a;
        }
        return p1Var;
    }

    public abstract URLConnection b(URL url, String str);
}
