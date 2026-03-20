package com.google.android.gms.internal.measurement;

import android.app.Activity;
import android.os.Bundle;
import com.google.android.gms.internal.measurement.p2;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class l3 extends p2.a {

    /* renamed from: e  reason: collision with root package name */
    private final /* synthetic */ Bundle f12292e;

    /* renamed from: f  reason: collision with root package name */
    private final /* synthetic */ Activity f12293f;

    /* renamed from: g  reason: collision with root package name */
    private final /* synthetic */ p2.b f12294g;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public l3(p2.b bVar, Bundle bundle, Activity activity) {
        super(p2.this);
        this.f12292e = bundle;
        this.f12293f = activity;
        this.f12294g = bVar;
    }

    @Override // com.google.android.gms.internal.measurement.p2.a
    final void a() {
        Bundle bundle;
        c2 c2Var;
        if (this.f12292e != null) {
            bundle = new Bundle();
            if (this.f12292e.containsKey("com.google.app_measurement.screen_service")) {
                Object obj = this.f12292e.get("com.google.app_measurement.screen_service");
                if (obj instanceof Bundle) {
                    bundle.putBundle("com.google.app_measurement.screen_service", (Bundle) obj);
                }
            }
        } else {
            bundle = null;
        }
        c2Var = p2.this.f12429i;
        ((c2) n6.j.l(c2Var)).onActivityCreated(x6.b.g(this.f12293f), bundle, this.f12431b);
    }
}
