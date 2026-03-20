package com.google.android.gms.measurement.internal;

import android.os.RemoteException;
import android.text.TextUtils;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class x9 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ boolean f17134a = true;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ zzn f17135b;

    /* renamed from: c  reason: collision with root package name */
    private final /* synthetic */ boolean f17136c;

    /* renamed from: d  reason: collision with root package name */
    private final /* synthetic */ zzac f17137d;

    /* renamed from: e  reason: collision with root package name */
    private final /* synthetic */ zzac f17138e;

    /* renamed from: f  reason: collision with root package name */
    private final /* synthetic */ f9 f17139f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public x9(f9 f9Var, boolean z4, zzn zznVar, boolean z8, zzac zzacVar, zzac zzacVar2) {
        this.f17135b = zznVar;
        this.f17136c = z8;
        this.f17137d = zzacVar;
        this.f17138e = zzacVar2;
        this.f17139f = f9Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        f7.d dVar;
        dVar = this.f17139f.f16529d;
        if (dVar == null) {
            this.f17139f.i().E().a("Discarding data. Failed to send conditional user property to service");
            return;
        }
        if (this.f17134a) {
            n6.j.l(this.f17135b);
            this.f17139f.M(dVar, this.f17136c ? null : this.f17137d, this.f17135b);
        } else {
            try {
                if (TextUtils.isEmpty(this.f17138e.f17250a)) {
                    n6.j.l(this.f17135b);
                    dVar.B(this.f17137d, this.f17135b);
                } else {
                    dVar.u1(this.f17137d);
                }
            } catch (RemoteException e8) {
                this.f17139f.i().E().b("Failed to send conditional user property to the service", e8);
            }
        }
        this.f17139f.f0();
    }
}
