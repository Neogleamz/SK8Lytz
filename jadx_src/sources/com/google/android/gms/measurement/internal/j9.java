package com.google.android.gms.measurement.internal;

import android.os.Bundle;
import android.os.RemoteException;
import java.util.concurrent.atomic.AtomicReference;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class j9 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ AtomicReference f16708a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ zzn f16709b;

    /* renamed from: c  reason: collision with root package name */
    private final /* synthetic */ Bundle f16710c;

    /* renamed from: d  reason: collision with root package name */
    private final /* synthetic */ f9 f16711d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public j9(f9 f9Var, AtomicReference atomicReference, zzn zznVar, Bundle bundle) {
        this.f16708a = atomicReference;
        this.f16709b = zznVar;
        this.f16710c = bundle;
        this.f16711d = f9Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        AtomicReference atomicReference;
        f7.d dVar;
        synchronized (this.f16708a) {
            try {
                dVar = this.f16711d.f16529d;
            } catch (RemoteException e8) {
                this.f16711d.i().E().b("Failed to get trigger URIs; remote exception", e8);
                atomicReference = this.f16708a;
            }
            if (dVar == null) {
                this.f16711d.i().E().a("Failed to get trigger URIs; not connected to service");
                this.f16708a.notify();
                return;
            }
            n6.j.l(this.f16709b);
            this.f16708a.set(dVar.K(this.f16709b, this.f16710c));
            this.f16711d.f0();
            atomicReference = this.f16708a;
            atomicReference.notify();
        }
    }
}
