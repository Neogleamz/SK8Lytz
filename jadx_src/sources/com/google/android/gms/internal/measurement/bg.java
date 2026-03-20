package com.google.android.gms.internal.measurement;

import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class bg extends m {

    /* renamed from: c  reason: collision with root package name */
    private final kg f12111c;

    public bg(kg kgVar) {
        super("internal.logger");
        this.f12111c = kgVar;
        this.f12329b.put("log", new jg(this, false, true));
        this.f12329b.put("silent", new af(this, "silent"));
        ((m) this.f12329b.get("silent")).n("log", new jg(this, true, true));
        this.f12329b.put("unmonitored", new ig(this, "unmonitored"));
        ((m) this.f12329b.get("unmonitored")).n("log", new jg(this, false, false));
    }

    @Override // com.google.android.gms.internal.measurement.m
    public final r c(g6 g6Var, List<r> list) {
        return r.f12463r;
    }
}
