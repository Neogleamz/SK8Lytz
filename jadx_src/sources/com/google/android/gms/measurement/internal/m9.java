package com.google.android.gms.measurement.internal;

import android.os.RemoteException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class m9 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ zzn f16796a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ com.google.android.gms.internal.measurement.h2 f16797b;

    /* renamed from: c  reason: collision with root package name */
    private final /* synthetic */ f9 f16798c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public m9(f9 f9Var, zzn zznVar, com.google.android.gms.internal.measurement.h2 h2Var) {
        this.f16796a = zznVar;
        this.f16797b = h2Var;
        this.f16798c = f9Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        f7.d dVar;
        String str = null;
        try {
            try {
                if (this.f16798c.f().J().B()) {
                    dVar = this.f16798c.f16529d;
                    if (dVar == null) {
                        this.f16798c.i().E().a("Failed to get app instance id");
                    } else {
                        n6.j.l(this.f16796a);
                        str = dVar.n1(this.f16796a);
                        if (str != null) {
                            this.f16798c.p().S(str);
                            this.f16798c.f().f16607i.b(str);
                        }
                        this.f16798c.f0();
                    }
                } else {
                    this.f16798c.i().K().a("Analytics storage consent denied; will not get app instance id");
                    this.f16798c.p().S(null);
                    this.f16798c.f().f16607i.b(null);
                }
            } catch (RemoteException e8) {
                this.f16798c.i().E().b("Failed to get app instance id", e8);
            }
        } finally {
            this.f16798c.g().Q(this.f16797b, null);
        }
    }
}
