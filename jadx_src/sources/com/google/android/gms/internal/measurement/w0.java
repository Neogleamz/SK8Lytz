package com.google.android.gms.internal.measurement;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class w0 implements IInterface {

    /* renamed from: a  reason: collision with root package name */
    private final IBinder f12628a;

    /* renamed from: b  reason: collision with root package name */
    private final String f12629b;

    /* JADX INFO: Access modifiers changed from: protected */
    public w0(IBinder iBinder, String str) {
        this.f12628a = iBinder;
        this.f12629b = str;
    }

    @Override // android.os.IInterface
    public IBinder asBinder() {
        return this.f12628a;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final Parcel d() {
        Parcel obtain = Parcel.obtain();
        obtain.writeInterfaceToken(this.f12629b);
        return obtain;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final Parcel e(int i8, Parcel parcel) {
        Parcel obtain = Parcel.obtain();
        try {
            try {
                this.f12628a.transact(i8, parcel, obtain, 0);
                obtain.readException();
                return obtain;
            } catch (RuntimeException e8) {
                obtain.recycle();
                throw e8;
            }
        } finally {
            parcel.recycle();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void f(int i8, Parcel parcel) {
        Parcel obtain = Parcel.obtain();
        try {
            this.f12628a.transact(i8, parcel, obtain, 0);
            obtain.readException();
        } finally {
            parcel.recycle();
            obtain.recycle();
        }
    }
}
