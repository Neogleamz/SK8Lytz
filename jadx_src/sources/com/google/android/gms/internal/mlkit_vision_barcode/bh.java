package com.google.android.gms.internal.mlkit_vision_barcode;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class bh {

    /* renamed from: a  reason: collision with root package name */
    private static ah f13318a;

    public static synchronized qg a(ig igVar) {
        qg qgVar;
        synchronized (bh.class) {
            if (f13318a == null) {
                f13318a = new ah(null);
            }
            qgVar = (qg) f13318a.b(igVar);
        }
        return qgVar;
    }

    public static synchronized qg b(String str) {
        qg a9;
        synchronized (bh.class) {
            a9 = a(ig.d(str).c());
        }
        return a9;
    }
}
