package com.google.android.gms.internal.mlkit_vision_barcode;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzvf extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzvf> CREATOR = new vh();

    /* renamed from: a  reason: collision with root package name */
    private final int f14671a;

    /* renamed from: b  reason: collision with root package name */
    private final String f14672b;

    public zzvf(int i8, String str) {
        this.f14671a = i8;
        this.f14672b = str;
    }

    public final int t() {
        return this.f14671a;
    }

    public final String u() {
        return this.f14672b;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 1, this.f14671a);
        o6.a.r(parcel, 2, this.f14672b, false);
        o6.a.b(parcel, a9);
    }
}
