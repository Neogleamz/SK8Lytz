package com.google.android.gms.internal.mlkit_vision_barcode;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzt extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzt> CREATOR = new h();

    /* renamed from: a  reason: collision with root package name */
    public String f14584a;

    /* renamed from: b  reason: collision with root package name */
    public String f14585b;

    /* renamed from: c  reason: collision with root package name */
    public int f14586c;

    public zzt() {
    }

    public zzt(String str, String str2, int i8) {
        this.f14584a = str;
        this.f14585b = str2;
        this.f14586c = i8;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.r(parcel, 2, this.f14584a, false);
        o6.a.r(parcel, 3, this.f14585b, false);
        o6.a.l(parcel, 4, this.f14586c);
        o6.a.b(parcel, a9);
    }
}
