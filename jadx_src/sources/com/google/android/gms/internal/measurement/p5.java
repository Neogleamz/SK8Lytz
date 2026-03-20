package com.google.android.gms.internal.measurement;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class p5 {

    /* renamed from: a  reason: collision with root package name */
    private static o5 f12437a;

    public static synchronized o5 a() {
        o5 o5Var;
        synchronized (p5.class) {
            if (f12437a == null) {
                b(new r5());
            }
            o5Var = f12437a;
        }
        return o5Var;
    }

    private static synchronized void b(o5 o5Var) {
        synchronized (p5.class) {
            if (f12437a != null) {
                throw new IllegalStateException("init() already called");
            }
            f12437a = o5Var;
        }
    }
}
