package com.google.android.gms.common.internal;

import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.internal.b;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class t extends m {

    /* renamed from: g  reason: collision with root package name */
    public final IBinder f11864g;

    /* renamed from: h  reason: collision with root package name */
    final /* synthetic */ b f11865h;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public t(b bVar, int i8, IBinder iBinder, Bundle bundle) {
        super(bVar, i8, bundle);
        this.f11865h = bVar;
        this.f11864g = iBinder;
    }

    @Override // com.google.android.gms.common.internal.m
    protected final void f(ConnectionResult connectionResult) {
        if (this.f11865h.B != null) {
            this.f11865h.B.e(connectionResult);
        }
        this.f11865h.J(connectionResult);
    }

    @Override // com.google.android.gms.common.internal.m
    protected final boolean g() {
        String str;
        String interfaceDescriptor;
        b.a aVar;
        b.a aVar2;
        try {
            IBinder iBinder = this.f11864g;
            n6.j.l(iBinder);
            interfaceDescriptor = iBinder.getInterfaceDescriptor();
        } catch (RemoteException unused) {
            str = "service probably died";
        }
        if (!this.f11865h.C().equals(interfaceDescriptor)) {
            str = "service descriptor mismatch: " + this.f11865h.C() + " vs. " + interfaceDescriptor;
            Log.w("GmsClient", str);
            return false;
        }
        IInterface q = this.f11865h.q(this.f11864g);
        if (q == null || !(b.e0(this.f11865h, 2, 4, q) || b.e0(this.f11865h, 3, 4, q))) {
            return false;
        }
        this.f11865h.G = null;
        b bVar = this.f11865h;
        Bundle v8 = bVar.v();
        aVar = bVar.A;
        if (aVar != null) {
            aVar2 = this.f11865h.A;
            aVar2.f(v8);
            return true;
        }
        return true;
    }
}
