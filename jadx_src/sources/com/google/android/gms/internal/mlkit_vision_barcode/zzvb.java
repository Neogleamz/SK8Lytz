package com.google.android.gms.internal.mlkit_vision_barcode;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzvb extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzvb> CREATOR = new nh();

    /* renamed from: a  reason: collision with root package name */
    private final String f14644a;

    /* renamed from: b  reason: collision with root package name */
    private final String f14645b;

    /* renamed from: c  reason: collision with root package name */
    private final String f14646c;

    /* renamed from: d  reason: collision with root package name */
    private final String f14647d;

    /* renamed from: e  reason: collision with root package name */
    private final String f14648e;

    /* renamed from: f  reason: collision with root package name */
    private final String f14649f;

    /* renamed from: g  reason: collision with root package name */
    private final String f14650g;

    /* renamed from: h  reason: collision with root package name */
    private final String f14651h;

    /* renamed from: j  reason: collision with root package name */
    private final String f14652j;

    /* renamed from: k  reason: collision with root package name */
    private final String f14653k;

    /* renamed from: l  reason: collision with root package name */
    private final String f14654l;

    /* renamed from: m  reason: collision with root package name */
    private final String f14655m;

    /* renamed from: n  reason: collision with root package name */
    private final String f14656n;

    /* renamed from: p  reason: collision with root package name */
    private final String f14657p;

    public zzvb(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, String str13, String str14) {
        this.f14644a = str;
        this.f14645b = str2;
        this.f14646c = str3;
        this.f14647d = str4;
        this.f14648e = str5;
        this.f14649f = str6;
        this.f14650g = str7;
        this.f14651h = str8;
        this.f14652j = str9;
        this.f14653k = str10;
        this.f14654l = str11;
        this.f14655m = str12;
        this.f14656n = str13;
        this.f14657p = str14;
    }

    public final String D0() {
        return this.f14652j;
    }

    public final String E0() {
        return this.f14656n;
    }

    public final String I0() {
        return this.f14644a;
    }

    public final String T0() {
        return this.f14655m;
    }

    public final String W0() {
        return this.f14645b;
    }

    public final String X0() {
        return this.f14648e;
    }

    public final String Z() {
        return this.f14649f;
    }

    public final String Z0() {
        return this.f14654l;
    }

    public final String a1() {
        return this.f14657p;
    }

    public final String b1() {
        return this.f14647d;
    }

    public final String c1() {
        return this.f14653k;
    }

    public final String f1() {
        return this.f14646c;
    }

    public final String t() {
        return this.f14650g;
    }

    public final String u() {
        return this.f14651h;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.r(parcel, 1, this.f14644a, false);
        o6.a.r(parcel, 2, this.f14645b, false);
        o6.a.r(parcel, 3, this.f14646c, false);
        o6.a.r(parcel, 4, this.f14647d, false);
        o6.a.r(parcel, 5, this.f14648e, false);
        o6.a.r(parcel, 6, this.f14649f, false);
        o6.a.r(parcel, 7, this.f14650g, false);
        o6.a.r(parcel, 8, this.f14651h, false);
        o6.a.r(parcel, 9, this.f14652j, false);
        o6.a.r(parcel, 10, this.f14653k, false);
        o6.a.r(parcel, 11, this.f14654l, false);
        o6.a.r(parcel, 12, this.f14655m, false);
        o6.a.r(parcel, 13, this.f14656n, false);
        o6.a.r(parcel, 14, this.f14657p, false);
        o6.a.b(parcel, a9);
    }
}
