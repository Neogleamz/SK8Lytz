package com.google.android.gms.internal.mlkit_common;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class s0 {

    /* renamed from: a  reason: collision with root package name */
    private static r0 f13012a;

    public static synchronized k0 a(g0 g0Var) {
        k0 k0Var;
        synchronized (s0.class) {
            if (f13012a == null) {
                f13012a = new r0(null);
            }
            k0Var = (k0) f13012a.b(g0Var);
        }
        return k0Var;
    }

    public static synchronized k0 b(String str) {
        k0 a9;
        synchronized (s0.class) {
            a9 = a(g0.d("common").c());
        }
        return a9;
    }
}
