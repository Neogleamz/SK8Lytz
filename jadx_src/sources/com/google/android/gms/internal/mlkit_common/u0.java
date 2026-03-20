package com.google.android.gms.internal.mlkit_common;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class u0 {

    /* renamed from: a  reason: collision with root package name */
    private static u0 f13021a;

    private u0() {
    }

    public static synchronized u0 a() {
        u0 u0Var;
        synchronized (u0.class) {
            if (f13021a == null) {
                f13021a = new u0();
            }
            u0Var = f13021a;
        }
        return u0Var;
    }

    public static void b() {
        t0.a();
    }
}
