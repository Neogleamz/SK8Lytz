package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class g4 {

    /* renamed from: c  reason: collision with root package name */
    private static final g4 f14769c = new g4();

    /* renamed from: b  reason: collision with root package name */
    private final ConcurrentMap f14771b = new ConcurrentHashMap();

    /* renamed from: a  reason: collision with root package name */
    private final s4 f14770a = new q3();

    private g4() {
    }

    public static g4 a() {
        return f14769c;
    }

    public final r4 b(Class cls) {
        y2.c(cls, "messageType");
        r4 r4Var = (r4) this.f14771b.get(cls);
        if (r4Var == null) {
            r4Var = this.f14770a.a(cls);
            y2.c(cls, "messageType");
            y2.c(r4Var, "schema");
            r4 r4Var2 = (r4) this.f14771b.putIfAbsent(cls, r4Var);
            if (r4Var2 != null) {
                return r4Var2;
            }
        }
        return r4Var;
    }
}
