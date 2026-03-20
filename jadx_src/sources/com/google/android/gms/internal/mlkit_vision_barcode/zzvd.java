package com.google.android.gms.internal.mlkit_vision_barcode;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzvd extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzvd> CREATOR = new ph();

    /* renamed from: a  reason: collision with root package name */
    private final double f14662a;

    /* renamed from: b  reason: collision with root package name */
    private final double f14663b;

    public zzvd(double d8, double d9) {
        this.f14662a = d8;
        this.f14663b = d9;
    }

    public final double t() {
        return this.f14662a;
    }

    public final double u() {
        return this.f14663b;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.g(parcel, 1, this.f14662a);
        o6.a.g(parcel, 2, this.f14663b);
        o6.a.b(parcel, a9);
    }
}
