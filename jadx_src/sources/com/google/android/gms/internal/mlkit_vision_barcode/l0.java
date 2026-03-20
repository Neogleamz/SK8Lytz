package com.google.android.gms.internal.mlkit_vision_barcode;

import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class l0 {

    /* renamed from: a  reason: collision with root package name */
    private static final ClassLoader f13650a = l0.class.getClassLoader();

    private l0() {
    }

    public static void a(Parcel parcel, Parcelable parcelable) {
        parcel.writeInt(1);
        parcelable.writeToParcel(parcel, 0);
    }

    public static void b(Parcel parcel, IInterface iInterface) {
        parcel.writeStrongBinder(iInterface == null ? null : iInterface.asBinder());
    }
}
