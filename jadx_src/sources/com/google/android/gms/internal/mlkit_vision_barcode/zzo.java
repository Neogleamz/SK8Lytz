package com.google.android.gms.internal.mlkit_vision_barcode;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzo extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzo> CREATOR = new c();

    /* renamed from: a  reason: collision with root package name */
    public double f14362a;

    /* renamed from: b  reason: collision with root package name */
    public double f14363b;

    public zzo() {
    }

    public zzo(double d8, double d9) {
        this.f14362a = d8;
        this.f14363b = d9;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.g(parcel, 2, this.f14362a);
        o6.a.g(parcel, 3, this.f14363b);
        o6.a.b(parcel, a9);
    }
}
