package com.google.android.gms.measurement.internal;

import android.os.RemoteException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class t9 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ zzbf f17009a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ String f17010b;

    /* renamed from: c  reason: collision with root package name */
    private final /* synthetic */ com.google.android.gms.internal.measurement.h2 f17011c;

    /* renamed from: d  reason: collision with root package name */
    private final /* synthetic */ f9 f17012d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public t9(f9 f9Var, zzbf zzbfVar, String str, com.google.android.gms.internal.measurement.h2 h2Var) {
        this.f17009a = zzbfVar;
        this.f17010b = str;
        this.f17011c = h2Var;
        this.f17012d = f9Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        f7.d dVar;
        byte[] bArr = null;
        try {
            try {
                dVar = this.f17012d.f16529d;
                if (dVar == null) {
                    this.f17012d.i().E().a("Discarding data. Failed to send event to service to bundle");
                } else {
                    bArr = dVar.K1(this.f17009a, this.f17010b);
                    this.f17012d.f0();
                }
            } catch (RemoteException e8) {
                this.f17012d.i().E().b("Failed to send event to the service to bundle", e8);
            }
        } finally {
            this.f17012d.g().T(this.f17011c, bArr);
        }
    }
}
