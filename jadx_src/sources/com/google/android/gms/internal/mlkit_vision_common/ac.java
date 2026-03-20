package com.google.android.gms.internal.mlkit_vision_common;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ac {

    /* renamed from: a  reason: collision with root package name */
    private static zb f15325a;

    public static synchronized rb a(mb mbVar) {
        rb rbVar;
        synchronized (ac.class) {
            if (f15325a == null) {
                f15325a = new zb(null);
            }
            rbVar = (rb) f15325a.b(mbVar);
        }
        return rbVar;
    }

    public static synchronized rb b(String str) {
        rb a9;
        synchronized (ac.class) {
            a9 = a(mb.d("vision-common").c());
        }
        return a9;
    }
}
