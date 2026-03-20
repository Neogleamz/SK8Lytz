package com.google.android.gms.common.api.internal;

import android.os.Handler;
import com.google.android.gms.common.api.internal.a;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class m implements a.InterfaceC0124a {

    /* renamed from: a  reason: collision with root package name */
    final /* synthetic */ b f11673a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public m(b bVar) {
        this.f11673a = bVar;
    }

    @Override // com.google.android.gms.common.api.internal.a.InterfaceC0124a
    public final void a(boolean z4) {
        Handler handler;
        Handler handler2;
        b bVar = this.f11673a;
        handler = bVar.f11619t;
        handler2 = bVar.f11619t;
        handler.sendMessage(handler2.obtainMessage(1, Boolean.valueOf(z4)));
    }
}
