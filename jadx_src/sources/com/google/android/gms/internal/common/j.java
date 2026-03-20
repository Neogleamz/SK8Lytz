package com.google.android.gms.internal.common;

import android.os.Handler;
import android.os.Looper;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class j extends Handler {

    /* renamed from: a  reason: collision with root package name */
    private final Looper f12048a;

    public j(Looper looper) {
        super(looper);
        this.f12048a = Looper.getMainLooper();
    }

    public j(Looper looper, Handler.Callback callback) {
        super(looper, callback);
        this.f12048a = Looper.getMainLooper();
    }
}
