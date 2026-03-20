package com.google.android.gms.cloudmessaging;

import android.os.Bundle;
import android.util.Log;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class p {

    /* renamed from: a  reason: collision with root package name */
    final int f11509a;

    /* renamed from: b  reason: collision with root package name */
    final j7.k f11510b = new j7.k();

    /* renamed from: c  reason: collision with root package name */
    final int f11511c;

    /* renamed from: d  reason: collision with root package name */
    final Bundle f11512d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public p(int i8, int i9, Bundle bundle) {
        this.f11509a = i8;
        this.f11511c = i9;
        this.f11512d = bundle;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void a(Bundle bundle);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract boolean b();

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void c(zzs zzsVar) {
        if (Log.isLoggable("MessengerIpcClient", 3)) {
            String obj = toString();
            String obj2 = zzsVar.toString();
            Log.d("MessengerIpcClient", "Failing " + obj + " with " + obj2);
        }
        this.f11510b.b(zzsVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void d(Object obj) {
        if (Log.isLoggable("MessengerIpcClient", 3)) {
            String obj2 = toString();
            String valueOf = String.valueOf(obj);
            Log.d("MessengerIpcClient", "Finishing " + obj2 + " with " + valueOf);
        }
        this.f11510b.c(obj);
    }

    public final String toString() {
        return "Request { what=" + this.f11511c + " id=" + this.f11509a + " oneWay=" + b() + "}";
    }
}
