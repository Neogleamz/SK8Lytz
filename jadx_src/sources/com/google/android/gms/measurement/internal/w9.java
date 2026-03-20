package com.google.android.gms.measurement.internal;

import android.os.RemoteException;
import android.text.TextUtils;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class w9 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ AtomicReference f17088a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ String f17089b;

    /* renamed from: c  reason: collision with root package name */
    private final /* synthetic */ String f17090c;

    /* renamed from: d  reason: collision with root package name */
    private final /* synthetic */ String f17091d;

    /* renamed from: e  reason: collision with root package name */
    private final /* synthetic */ zzn f17092e;

    /* renamed from: f  reason: collision with root package name */
    private final /* synthetic */ f9 f17093f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public w9(f9 f9Var, AtomicReference atomicReference, String str, String str2, String str3, zzn zznVar) {
        this.f17088a = atomicReference;
        this.f17089b = str;
        this.f17090c = str2;
        this.f17091d = str3;
        this.f17092e = zznVar;
        this.f17093f = f9Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        AtomicReference atomicReference;
        f7.d dVar;
        AtomicReference atomicReference2;
        List<zzac> s02;
        synchronized (this.f17088a) {
            try {
                dVar = this.f17093f.f16529d;
            } catch (RemoteException e8) {
                this.f17093f.i().E().d("(legacy) Failed to get conditional properties; remote exception", x4.t(this.f17089b), this.f17090c, e8);
                this.f17088a.set(Collections.emptyList());
                atomicReference = this.f17088a;
            }
            if (dVar == null) {
                this.f17093f.i().E().d("(legacy) Failed to get conditional properties; not connected to service", x4.t(this.f17089b), this.f17090c, this.f17091d);
                this.f17088a.set(Collections.emptyList());
                this.f17088a.notify();
                return;
            }
            if (TextUtils.isEmpty(this.f17089b)) {
                n6.j.l(this.f17092e);
                atomicReference2 = this.f17088a;
                s02 = dVar.t0(this.f17090c, this.f17091d, this.f17092e);
            } else {
                atomicReference2 = this.f17088a;
                s02 = dVar.s0(this.f17089b, this.f17090c, this.f17091d);
            }
            atomicReference2.set(s02);
            this.f17093f.f0();
            atomicReference = this.f17088a;
            atomicReference.notify();
        }
    }
}
