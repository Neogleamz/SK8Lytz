package com.google.android.gms.internal.mlkit_vision_common;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ec {

    /* renamed from: a  reason: collision with root package name */
    private static ec f15435a;

    private ec() {
    }

    public static synchronized ec a() {
        ec ecVar;
        synchronized (ec.class) {
            if (f15435a == null) {
                f15435a = new ec();
            }
            ecVar = f15435a;
        }
        return ecVar;
    }

    public static final boolean b() {
        return dc.a("mlkit-dev-profiling");
    }
}
