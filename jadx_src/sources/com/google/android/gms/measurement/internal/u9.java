package com.google.android.gms.measurement.internal;

import android.os.RemoteException;
import android.text.TextUtils;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class u9 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ boolean f17027a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ zzn f17028b;

    /* renamed from: c  reason: collision with root package name */
    private final /* synthetic */ boolean f17029c;

    /* renamed from: d  reason: collision with root package name */
    private final /* synthetic */ zzbf f17030d;

    /* renamed from: e  reason: collision with root package name */
    private final /* synthetic */ String f17031e;

    /* renamed from: f  reason: collision with root package name */
    private final /* synthetic */ f9 f17032f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public u9(f9 f9Var, boolean z4, zzn zznVar, boolean z8, zzbf zzbfVar, String str) {
        this.f17027a = z4;
        this.f17028b = zznVar;
        this.f17029c = z8;
        this.f17030d = zzbfVar;
        this.f17031e = str;
        this.f17032f = f9Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        f7.d dVar;
        dVar = this.f17032f.f16529d;
        if (dVar == null) {
            this.f17032f.i().E().a("Discarding data. Failed to send event to service");
            return;
        }
        if (this.f17027a) {
            n6.j.l(this.f17028b);
            this.f17032f.M(dVar, this.f17029c ? null : this.f17030d, this.f17028b);
        } else {
            try {
                if (TextUtils.isEmpty(this.f17031e)) {
                    n6.j.l(this.f17028b);
                    dVar.f1(this.f17030d, this.f17028b);
                } else {
                    dVar.a1(this.f17030d, this.f17031e, this.f17032f.i().M());
                }
            } catch (RemoteException e8) {
                this.f17032f.i().E().b("Failed to send event to the service", e8);
            }
        }
        this.f17032f.f0();
    }
}
