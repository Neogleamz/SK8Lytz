package com.google.android.gms.internal.mlkit_vision_barcode;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a implements IInterface {

    /* renamed from: a  reason: collision with root package name */
    private final IBinder f13238a;

    /* renamed from: b  reason: collision with root package name */
    private final String f13239b;

    /* JADX INFO: Access modifiers changed from: protected */
    public a(IBinder iBinder, String str) {
        this.f13238a = iBinder;
        this.f13239b = str;
    }

    @Override // android.os.IInterface
    public final IBinder asBinder() {
        return this.f13238a;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final Parcel d() {
        Parcel obtain = Parcel.obtain();
        obtain.writeInterfaceToken(this.f13239b);
        return obtain;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final Parcel e(int i8, Parcel parcel) {
        Parcel obtain = Parcel.obtain();
        try {
            try {
                this.f13238a.transact(i8, parcel, obtain, 0);
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
            this.f13238a.transact(i8, parcel, obtain, 0);
            obtain.readException();
        } finally {
            parcel.recycle();
            obtain.recycle();
        }
    }
}
