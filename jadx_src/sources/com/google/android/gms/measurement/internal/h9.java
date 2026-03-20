package com.google.android.gms.measurement.internal;

import android.os.Bundle;
import android.os.RemoteException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class h9 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ String f16647a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ String f16648b;

    /* renamed from: c  reason: collision with root package name */
    private final /* synthetic */ zzn f16649c;

    /* renamed from: d  reason: collision with root package name */
    private final /* synthetic */ boolean f16650d;

    /* renamed from: e  reason: collision with root package name */
    private final /* synthetic */ com.google.android.gms.internal.measurement.h2 f16651e;

    /* renamed from: f  reason: collision with root package name */
    private final /* synthetic */ f9 f16652f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public h9(f9 f9Var, String str, String str2, zzn zznVar, boolean z4, com.google.android.gms.internal.measurement.h2 h2Var) {
        this.f16647a = str;
        this.f16648b = str2;
        this.f16649c = zznVar;
        this.f16650d = z4;
        this.f16651e = h2Var;
        this.f16652f = f9Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        f7.d dVar;
        Bundle bundle = new Bundle();
        try {
            try {
                dVar = this.f16652f.f16529d;
                if (dVar == null) {
                    this.f16652f.i().E().c("Failed to get user properties; not connected to service", this.f16647a, this.f16648b);
                } else {
                    n6.j.l(this.f16649c);
                    bundle = sb.E(dVar.I0(this.f16647a, this.f16648b, this.f16650d, this.f16649c));
                    this.f16652f.f0();
                }
            } catch (RemoteException e8) {
                this.f16652f.i().E().c("Failed to get user properties; remote exception", this.f16647a, e8);
            }
        } finally {
            this.f16652f.g().P(this.f16651e, bundle);
        }
    }
}
