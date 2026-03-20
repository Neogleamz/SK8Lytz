package com.google.android.gms.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
/* JADX INFO: Access modifiers changed from: package-private */
@SuppressLint({"HandlerLeak"})
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class f extends a7.k {

    /* renamed from: a  reason: collision with root package name */
    private final Context f11749a;

    /* renamed from: b  reason: collision with root package name */
    final /* synthetic */ a f11750b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public f(a aVar, Context context) {
        super(Looper.myLooper() == null ? Looper.getMainLooper() : Looper.myLooper());
        this.f11750b = aVar;
        this.f11749a = context.getApplicationContext();
    }

    @Override // android.os.Handler
    public final void handleMessage(Message message) {
        int i8 = message.what;
        if (i8 != 1) {
            Log.w("GoogleApiAvailability", "Don't know how to handle this message: " + i8);
            return;
        }
        int g8 = this.f11750b.g(this.f11749a);
        if (this.f11750b.j(g8)) {
            this.f11750b.o(this.f11749a, g8);
        }
    }
}
