package com.google.android.gms.internal.mlkit_vision_barcode;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzn extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzn> CREATOR = new b();

    /* renamed from: a  reason: collision with root package name */
    public int f14358a;

    /* renamed from: b  reason: collision with root package name */
    public String f14359b;

    /* renamed from: c  reason: collision with root package name */
    public String f14360c;

    /* renamed from: d  reason: collision with root package name */
    public String f14361d;

    public zzn() {
    }

    public zzn(int i8, String str, String str2, String str3) {
        this.f14358a = i8;
        this.f14359b = str;
        this.f14360c = str2;
        this.f14361d = str3;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 2, this.f14358a);
        o6.a.r(parcel, 3, this.f14359b, false);
        o6.a.r(parcel, 4, this.f14360c, false);
        o6.a.r(parcel, 5, this.f14361d, false);
        o6.a.b(parcel, a9);
    }
}
