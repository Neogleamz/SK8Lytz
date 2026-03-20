package com.google.android.gms.internal.measurement;

import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zd extends m {

    /* renamed from: c  reason: collision with root package name */
    private final /* synthetic */ yc f12732c;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public zd(za zaVar, String str, yc ycVar) {
        super(str);
        this.f12732c = ycVar;
    }

    @Override // com.google.android.gms.internal.measurement.m
    public final r c(g6 g6Var, List<r> list) {
        e5.g("getValue", 2, list);
        r b9 = g6Var.b(list.get(0));
        r b10 = g6Var.b(list.get(1));
        String h8 = this.f12732c.h(b9.e());
        return h8 != null ? new t(h8) : b10;
    }
}
