package com.google.android.gms.internal.measurement;

import android.os.Bundle;
import com.google.android.gms.internal.measurement.p2;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class i3 extends p2.a {

    /* renamed from: e  reason: collision with root package name */
    private final /* synthetic */ Long f12239e;

    /* renamed from: f  reason: collision with root package name */
    private final /* synthetic */ String f12240f;

    /* renamed from: g  reason: collision with root package name */
    private final /* synthetic */ String f12241g;

    /* renamed from: h  reason: collision with root package name */
    private final /* synthetic */ Bundle f12242h;

    /* renamed from: j  reason: collision with root package name */
    private final /* synthetic */ boolean f12243j;

    /* renamed from: k  reason: collision with root package name */
    private final /* synthetic */ boolean f12244k;

    /* renamed from: l  reason: collision with root package name */
    private final /* synthetic */ p2 f12245l;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public i3(p2 p2Var, Long l8, String str, String str2, Bundle bundle, boolean z4, boolean z8) {
        super(p2Var);
        this.f12239e = l8;
        this.f12240f = str;
        this.f12241g = str2;
        this.f12242h = bundle;
        this.f12243j = z4;
        this.f12244k = z8;
        this.f12245l = p2Var;
    }

    @Override // com.google.android.gms.internal.measurement.p2.a
    final void a() {
        c2 c2Var;
        Long l8 = this.f12239e;
        long longValue = l8 == null ? this.f12430a : l8.longValue();
        c2Var = this.f12245l.f12429i;
        ((c2) n6.j.l(c2Var)).logEvent(this.f12240f, this.f12241g, this.f12242h, this.f12243j, this.f12244k, longValue);
    }
}
