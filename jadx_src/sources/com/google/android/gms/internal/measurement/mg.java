package com.google.android.gms.internal.measurement;

import java.util.List;
import java.util.concurrent.Callable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class mg extends m {

    /* renamed from: c  reason: collision with root package name */
    private final Callable<Object> f12357c;

    public mg(String str, Callable<Object> callable) {
        super(str);
        this.f12357c = callable;
    }

    @Override // com.google.android.gms.internal.measurement.m
    public final r c(g6 g6Var, List<r> list) {
        try {
            return g8.b(this.f12357c.call());
        } catch (Exception unused) {
            return r.f12463r;
        }
    }
}
