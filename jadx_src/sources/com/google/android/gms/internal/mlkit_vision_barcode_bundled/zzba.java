package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzba extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzba> CREATOR = new q();

    /* renamed from: a  reason: collision with root package name */
    private final int f14956a;

    /* renamed from: b  reason: collision with root package name */
    private final String f14957b;

    /* renamed from: c  reason: collision with root package name */
    private final String f14958c;

    /* renamed from: d  reason: collision with root package name */
    private final byte[] f14959d;

    /* renamed from: e  reason: collision with root package name */
    private final Point[] f14960e;

    /* renamed from: f  reason: collision with root package name */
    private final int f14961f;

    /* renamed from: g  reason: collision with root package name */
    private final zzat f14962g;

    /* renamed from: h  reason: collision with root package name */
    private final zzaw f14963h;

    /* renamed from: j  reason: collision with root package name */
    private final zzax f14964j;

    /* renamed from: k  reason: collision with root package name */
    private final zzaz f14965k;

    /* renamed from: l  reason: collision with root package name */
    private final zzay f14966l;

    /* renamed from: m  reason: collision with root package name */
    private final zzau f14967m;

    /* renamed from: n  reason: collision with root package name */
    private final zzaq f14968n;

    /* renamed from: p  reason: collision with root package name */
    private final zzar f14969p;
    private final zzas q;

    public zzba(int i8, String str, String str2, byte[] bArr, Point[] pointArr, int i9, zzat zzatVar, zzaw zzawVar, zzax zzaxVar, zzaz zzazVar, zzay zzayVar, zzau zzauVar, zzaq zzaqVar, zzar zzarVar, zzas zzasVar) {
        this.f14956a = i8;
        this.f14957b = str;
        this.f14958c = str2;
        this.f14959d = bArr;
        this.f14960e = pointArr;
        this.f14961f = i9;
        this.f14962g = zzatVar;
        this.f14963h = zzawVar;
        this.f14964j = zzaxVar;
        this.f14965k = zzazVar;
        this.f14966l = zzayVar;
        this.f14967m = zzauVar;
        this.f14968n = zzaqVar;
        this.f14969p = zzarVar;
        this.q = zzasVar;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 1, this.f14956a);
        o6.a.r(parcel, 2, this.f14957b, false);
        o6.a.r(parcel, 3, this.f14958c, false);
        o6.a.f(parcel, 4, this.f14959d, false);
        o6.a.u(parcel, 5, this.f14960e, i8, false);
        o6.a.l(parcel, 6, this.f14961f);
        o6.a.q(parcel, 7, this.f14962g, i8, false);
        o6.a.q(parcel, 8, this.f14963h, i8, false);
        o6.a.q(parcel, 9, this.f14964j, i8, false);
        o6.a.q(parcel, 10, this.f14965k, i8, false);
        o6.a.q(parcel, 11, this.f14966l, i8, false);
        o6.a.q(parcel, 12, this.f14967m, i8, false);
        o6.a.q(parcel, 13, this.f14968n, i8, false);
        o6.a.q(parcel, 14, this.f14969p, i8, false);
        o6.a.q(parcel, 15, this.q, i8, false);
        o6.a.b(parcel, a9);
    }
}
