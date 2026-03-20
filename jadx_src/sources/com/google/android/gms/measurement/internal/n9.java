package com.google.android.gms.measurement.internal;

import android.os.RemoteException;
import java.util.concurrent.atomic.AtomicReference;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class n9 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ AtomicReference f16836a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ zzn f16837b;

    /* renamed from: c  reason: collision with root package name */
    private final /* synthetic */ f9 f16838c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public n9(f9 f9Var, AtomicReference atomicReference, zzn zznVar) {
        this.f16836a = atomicReference;
        this.f16837b = zznVar;
        this.f16838c = f9Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        AtomicReference atomicReference;
        f7.d dVar;
        synchronized (this.f16836a) {
            try {
            } catch (RemoteException e8) {
                this.f16838c.i().E().b("Failed to get app instance id", e8);
                atomicReference = this.f16836a;
            }
            if (!this.f16838c.f().J().B()) {
                this.f16838c.i().K().a("Analytics storage consent denied; will not get app instance id");
                this.f16838c.p().S(null);
                this.f16838c.f().f16607i.b(null);
                this.f16836a.set(null);
                this.f16836a.notify();
                return;
            }
            dVar = this.f16838c.f16529d;
            if (dVar == null) {
                this.f16838c.i().E().a("Failed to get app instance id");
                this.f16836a.notify();
                return;
            }
            n6.j.l(this.f16837b);
            this.f16836a.set(dVar.n1(this.f16837b));
            String str = (String) this.f16836a.get();
            if (str != null) {
                this.f16838c.p().S(str);
                this.f16838c.f().f16607i.b(str);
            }
            this.f16838c.f0();
            atomicReference = this.f16836a;
            atomicReference.notify();
        }
    }
}
