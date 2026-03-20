package com.google.android.gms.measurement.internal;

import android.os.RemoteException;
import android.text.TextUtils;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class y9 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ AtomicReference f17191a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ String f17192b;

    /* renamed from: c  reason: collision with root package name */
    private final /* synthetic */ String f17193c;

    /* renamed from: d  reason: collision with root package name */
    private final /* synthetic */ String f17194d;

    /* renamed from: e  reason: collision with root package name */
    private final /* synthetic */ zzn f17195e;

    /* renamed from: f  reason: collision with root package name */
    private final /* synthetic */ boolean f17196f;

    /* renamed from: g  reason: collision with root package name */
    private final /* synthetic */ f9 f17197g;

    /* JADX INFO: Access modifiers changed from: package-private */
    public y9(f9 f9Var, AtomicReference atomicReference, String str, String str2, String str3, zzn zznVar, boolean z4) {
        this.f17191a = atomicReference;
        this.f17192b = str;
        this.f17193c = str2;
        this.f17194d = str3;
        this.f17195e = zznVar;
        this.f17196f = z4;
        this.f17197g = f9Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        AtomicReference atomicReference;
        f7.d dVar;
        AtomicReference atomicReference2;
        List<zzno> s8;
        synchronized (this.f17191a) {
            try {
                dVar = this.f17197g.f16529d;
            } catch (RemoteException e8) {
                this.f17197g.i().E().d("(legacy) Failed to get user properties; remote exception", x4.t(this.f17192b), this.f17193c, e8);
                this.f17191a.set(Collections.emptyList());
                atomicReference = this.f17191a;
            }
            if (dVar == null) {
                this.f17197g.i().E().d("(legacy) Failed to get user properties; not connected to service", x4.t(this.f17192b), this.f17193c, this.f17194d);
                this.f17191a.set(Collections.emptyList());
                this.f17191a.notify();
                return;
            }
            if (TextUtils.isEmpty(this.f17192b)) {
                n6.j.l(this.f17195e);
                atomicReference2 = this.f17191a;
                s8 = dVar.I0(this.f17193c, this.f17194d, this.f17196f, this.f17195e);
            } else {
                atomicReference2 = this.f17191a;
                s8 = dVar.s(this.f17192b, this.f17193c, this.f17194d, this.f17196f);
            }
            atomicReference2.set(s8);
            this.f17197g.f0();
            atomicReference = this.f17191a;
            atomicReference.notify();
        }
    }
}
