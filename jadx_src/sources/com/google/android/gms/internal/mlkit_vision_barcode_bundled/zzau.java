package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzau extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzau> CREATOR = new x();

    /* renamed from: a  reason: collision with root package name */
    private final double f14938a;

    /* renamed from: b  reason: collision with root package name */
    private final double f14939b;

    public zzau(double d8, double d9) {
        this.f14938a = d8;
        this.f14939b = d9;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.g(parcel, 1, this.f14938a);
        o6.a.g(parcel, 2, this.f14939b);
        o6.a.b(parcel, a9);
    }
}
