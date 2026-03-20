package com.google.android.gms.internal.mlkit_vision_barcode;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzvi extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzvi> CREATOR = new zh();

    /* renamed from: a  reason: collision with root package name */
    private final String f14677a;

    /* renamed from: b  reason: collision with root package name */
    private final String f14678b;

    /* renamed from: c  reason: collision with root package name */
    private final int f14679c;

    public zzvi(String str, String str2, int i8) {
        this.f14677a = str;
        this.f14678b = str2;
        this.f14679c = i8;
    }

    public final String Z() {
        return this.f14677a;
    }

    public final int t() {
        return this.f14679c;
    }

    public final String u() {
        return this.f14678b;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.r(parcel, 1, this.f14677a, false);
        o6.a.r(parcel, 2, this.f14678b, false);
        o6.a.l(parcel, 3, this.f14679c);
        o6.a.b(parcel, a9);
    }
}
