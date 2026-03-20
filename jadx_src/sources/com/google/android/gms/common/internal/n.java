package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.Parcel;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class n implements n6.g {

    /* renamed from: a  reason: collision with root package name */
    private final IBinder f11855a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public n(IBinder iBinder) {
        this.f11855a = iBinder;
    }

    @Override // android.os.IInterface
    public final IBinder asBinder() {
        return this.f11855a;
    }

    @Override // n6.g
    public final void k1(n6.f fVar, GetServiceRequest getServiceRequest) {
        Parcel obtain = Parcel.obtain();
        Parcel obtain2 = Parcel.obtain();
        try {
            obtain.writeInterfaceToken("com.google.android.gms.common.internal.IGmsServiceBroker");
            obtain.writeStrongBinder(fVar != null ? fVar.asBinder() : null);
            if (getServiceRequest != null) {
                obtain.writeInt(1);
                x.a(getServiceRequest, obtain, 0);
            } else {
                obtain.writeInt(0);
            }
            this.f11855a.transact(46, obtain, obtain2, 0);
            obtain2.readException();
        } finally {
            obtain2.recycle();
            obtain.recycle();
        }
    }
}
