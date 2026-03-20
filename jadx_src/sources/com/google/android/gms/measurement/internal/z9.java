package com.google.android.gms.measurement.internal;

import android.os.Bundle;
import android.os.RemoteException;
import java.util.ArrayList;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class z9 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ String f17242a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ String f17243b;

    /* renamed from: c  reason: collision with root package name */
    private final /* synthetic */ zzn f17244c;

    /* renamed from: d  reason: collision with root package name */
    private final /* synthetic */ com.google.android.gms.internal.measurement.h2 f17245d;

    /* renamed from: e  reason: collision with root package name */
    private final /* synthetic */ f9 f17246e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public z9(f9 f9Var, String str, String str2, zzn zznVar, com.google.android.gms.internal.measurement.h2 h2Var) {
        this.f17242a = str;
        this.f17243b = str2;
        this.f17244c = zznVar;
        this.f17245d = h2Var;
        this.f17246e = f9Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        f7.d dVar;
        ArrayList<Bundle> arrayList = new ArrayList<>();
        try {
            try {
                dVar = this.f17246e.f16529d;
                if (dVar == null) {
                    this.f17246e.i().E().c("Failed to get conditional properties; not connected to service", this.f17242a, this.f17243b);
                } else {
                    n6.j.l(this.f17244c);
                    arrayList = sb.r0(dVar.t0(this.f17242a, this.f17243b, this.f17244c));
                    this.f17246e.f0();
                }
            } catch (RemoteException e8) {
                this.f17246e.i().E().d("Failed to get conditional properties; remote exception", this.f17242a, this.f17243b, e8);
            }
        } finally {
            this.f17246e.g().R(this.f17245d, arrayList);
        }
    }
}
