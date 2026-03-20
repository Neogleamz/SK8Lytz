package com.google.android.gms.measurement.internal;

import java.util.Map;
import java.util.Set;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class wb extends bb {

    /* renamed from: d  reason: collision with root package name */
    private String f17096d;

    /* renamed from: e  reason: collision with root package name */
    private Set<Integer> f17097e;

    /* renamed from: f  reason: collision with root package name */
    private Map<Integer, yb> f17098f;

    /* renamed from: g  reason: collision with root package name */
    private Long f17099g;

    /* renamed from: h  reason: collision with root package name */
    private Long f17100h;

    /* JADX INFO: Access modifiers changed from: package-private */
    public wb(gb gbVar) {
        super(gbVar);
    }

    private final yb w(Integer num) {
        if (this.f17098f.containsKey(num)) {
            return this.f17098f.get(num);
        }
        yb ybVar = new yb(this, this.f17096d);
        this.f17098f.put(num, ybVar);
        return ybVar;
    }

    private final boolean y(int i8, int i9) {
        yb ybVar = this.f17098f.get(Integer.valueOf(i8));
        if (ybVar == null) {
            return false;
        }
        return yb.b(ybVar).get(i9);
    }

    @Override // com.google.android.gms.measurement.internal.bb
    protected final boolean v() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:110:0x02f6  */
    /* JADX WARN: Removed duplicated region for block: B:268:0x02fd A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.util.List<com.google.android.gms.internal.measurement.p4> x(java.lang.String r53, java.util.List<com.google.android.gms.internal.measurement.r4> r54, java.util.List<com.google.android.gms.internal.measurement.y4> r55, java.lang.Long r56, java.lang.Long r57) {
        /*
            Method dump skipped, instructions count: 1794
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.wb.x(java.lang.String, java.util.List, java.util.List, java.lang.Long, java.lang.Long):java.util.List");
    }
}
