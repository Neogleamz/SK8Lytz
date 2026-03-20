package com.google.android.gms.internal.measurement;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c7 {

    /* renamed from: a  reason: collision with root package name */
    private final boolean f12114a;

    public c7(b7 b7Var) {
        com.google.common.base.l.o(b7Var, "BuildInfo must be non-null");
        this.f12114a = !b7Var.zza();
    }

    public final boolean a(String str) {
        com.google.common.base.l.o(str, "flagName must not be null");
        if (this.f12114a) {
            return e7.f12156a.get().d(str);
        }
        return true;
    }
}
