package com.google.android.gms.cloudmessaging;

import android.os.Looper;
import android.os.Message;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class d extends b7.f {

    /* renamed from: b  reason: collision with root package name */
    final /* synthetic */ b f11487b;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public d(b bVar, Looper looper) {
        super(looper);
        this.f11487b = bVar;
    }

    @Override // android.os.Handler
    public final void handleMessage(Message message) {
        b.e(this.f11487b, message);
    }
}
