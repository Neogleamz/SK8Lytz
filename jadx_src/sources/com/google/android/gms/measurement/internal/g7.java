package com.google.android.gms.measurement.internal;

import android.content.Context;
import android.os.Bundle;
import com.google.android.gms.internal.measurement.zzdq;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class g7 {

    /* renamed from: a  reason: collision with root package name */
    final Context f16549a;

    /* renamed from: b  reason: collision with root package name */
    String f16550b;

    /* renamed from: c  reason: collision with root package name */
    String f16551c;

    /* renamed from: d  reason: collision with root package name */
    String f16552d;

    /* renamed from: e  reason: collision with root package name */
    Boolean f16553e;

    /* renamed from: f  reason: collision with root package name */
    long f16554f;

    /* renamed from: g  reason: collision with root package name */
    zzdq f16555g;

    /* renamed from: h  reason: collision with root package name */
    boolean f16556h;

    /* renamed from: i  reason: collision with root package name */
    Long f16557i;

    /* renamed from: j  reason: collision with root package name */
    String f16558j;

    public g7(Context context, zzdq zzdqVar, Long l8) {
        this.f16556h = true;
        n6.j.l(context);
        Context applicationContext = context.getApplicationContext();
        n6.j.l(applicationContext);
        this.f16549a = applicationContext;
        this.f16557i = l8;
        if (zzdqVar != null) {
            this.f16555g = zzdqVar;
            this.f16550b = zzdqVar.f12790f;
            this.f16551c = zzdqVar.f12789e;
            this.f16552d = zzdqVar.f12788d;
            this.f16556h = zzdqVar.f12787c;
            this.f16554f = zzdqVar.f12786b;
            this.f16558j = zzdqVar.f12792h;
            Bundle bundle = zzdqVar.f12791g;
            if (bundle != null) {
                this.f16553e = Boolean.valueOf(bundle.getBoolean("dataCollectionDefaultEnabled", true));
            }
        }
    }
}
