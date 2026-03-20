package com.google.android.gms.internal.mlkit_vision_barcode;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzm extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzm> CREATOR = new di();

    /* renamed from: a  reason: collision with root package name */
    public String f14344a;

    /* renamed from: b  reason: collision with root package name */
    public String f14345b;

    /* renamed from: c  reason: collision with root package name */
    public String f14346c;

    /* renamed from: d  reason: collision with root package name */
    public String f14347d;

    /* renamed from: e  reason: collision with root package name */
    public String f14348e;

    /* renamed from: f  reason: collision with root package name */
    public String f14349f;

    /* renamed from: g  reason: collision with root package name */
    public String f14350g;

    /* renamed from: h  reason: collision with root package name */
    public String f14351h;

    /* renamed from: j  reason: collision with root package name */
    public String f14352j;

    /* renamed from: k  reason: collision with root package name */
    public String f14353k;

    /* renamed from: l  reason: collision with root package name */
    public String f14354l;

    /* renamed from: m  reason: collision with root package name */
    public String f14355m;

    /* renamed from: n  reason: collision with root package name */
    public String f14356n;

    /* renamed from: p  reason: collision with root package name */
    public String f14357p;

    public zzm() {
    }

    public zzm(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, String str13, String str14) {
        this.f14344a = str;
        this.f14345b = str2;
        this.f14346c = str3;
        this.f14347d = str4;
        this.f14348e = str5;
        this.f14349f = str6;
        this.f14350g = str7;
        this.f14351h = str8;
        this.f14352j = str9;
        this.f14353k = str10;
        this.f14354l = str11;
        this.f14355m = str12;
        this.f14356n = str13;
        this.f14357p = str14;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.r(parcel, 2, this.f14344a, false);
        o6.a.r(parcel, 3, this.f14345b, false);
        o6.a.r(parcel, 4, this.f14346c, false);
        o6.a.r(parcel, 5, this.f14347d, false);
        o6.a.r(parcel, 6, this.f14348e, false);
        o6.a.r(parcel, 7, this.f14349f, false);
        o6.a.r(parcel, 8, this.f14350g, false);
        o6.a.r(parcel, 9, this.f14351h, false);
        o6.a.r(parcel, 10, this.f14352j, false);
        o6.a.r(parcel, 11, this.f14353k, false);
        o6.a.r(parcel, 12, this.f14354l, false);
        o6.a.r(parcel, 13, this.f14355m, false);
        o6.a.r(parcel, 14, this.f14356n, false);
        o6.a.r(parcel, 15, this.f14357p, false);
        o6.a.b(parcel, a9);
    }
}
