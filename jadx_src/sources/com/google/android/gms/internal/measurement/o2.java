package com.google.android.gms.internal.measurement;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.dynamite.DynamiteModule;
import com.google.android.gms.dynamite.descriptors.com.google.android.gms.measurement.dynamite.ModuleDescriptor;
import com.google.android.gms.internal.measurement.p2;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class o2 extends p2.a {

    /* renamed from: e  reason: collision with root package name */
    private final /* synthetic */ String f12392e;

    /* renamed from: f  reason: collision with root package name */
    private final /* synthetic */ String f12393f;

    /* renamed from: g  reason: collision with root package name */
    private final /* synthetic */ Context f12394g;

    /* renamed from: h  reason: collision with root package name */
    private final /* synthetic */ Bundle f12395h;

    /* renamed from: j  reason: collision with root package name */
    private final /* synthetic */ p2 f12396j;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public o2(p2 p2Var, String str, String str2, Context context, Bundle bundle) {
        super(p2Var);
        this.f12392e = str;
        this.f12393f = str2;
        this.f12394g = context;
        this.f12395h = bundle;
        this.f12396j = p2Var;
    }

    @Override // com.google.android.gms.internal.measurement.p2.a
    public final void a() {
        boolean C;
        String str;
        String str2;
        String str3;
        c2 c2Var;
        int b9;
        c2 c2Var2;
        String str4;
        String str5;
        try {
            C = this.f12396j.C(this.f12392e, this.f12393f);
            if (C) {
                String str6 = this.f12393f;
                String str7 = this.f12392e;
                str5 = this.f12396j.f12421a;
                str2 = str7;
                str3 = str6;
                str = str5;
            } else {
                str = null;
                str2 = null;
                str3 = null;
            }
            n6.j.l(this.f12394g);
            p2 p2Var = this.f12396j;
            p2Var.f12429i = p2Var.c(this.f12394g, true);
            c2Var = this.f12396j.f12429i;
            if (c2Var == null) {
                str4 = this.f12396j.f12421a;
                Log.w(str4, "Failed to connect to measurement client.");
                return;
            }
            int a9 = DynamiteModule.a(this.f12394g, ModuleDescriptor.MODULE_ID);
            zzdq zzdqVar = new zzdq(87000L, Math.max(a9, b9), DynamiteModule.b(this.f12394g, ModuleDescriptor.MODULE_ID) < a9, str, str2, str3, this.f12395h, f7.l.a(this.f12394g));
            c2Var2 = this.f12396j.f12429i;
            ((c2) n6.j.l(c2Var2)).initialize(x6.b.g(this.f12394g), zzdqVar, this.f12430a);
        } catch (Exception e8) {
            this.f12396j.p(e8, true, false);
        }
    }
}
