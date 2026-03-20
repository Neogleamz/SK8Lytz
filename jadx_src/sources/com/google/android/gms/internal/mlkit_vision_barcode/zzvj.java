package com.google.android.gms.internal.mlkit_vision_barcode;

import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzvj extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzvj> CREATOR = new ih();

    /* renamed from: a  reason: collision with root package name */
    private final int f14680a;

    /* renamed from: b  reason: collision with root package name */
    private final String f14681b;

    /* renamed from: c  reason: collision with root package name */
    private final String f14682c;

    /* renamed from: d  reason: collision with root package name */
    private final byte[] f14683d;

    /* renamed from: e  reason: collision with root package name */
    private final Point[] f14684e;

    /* renamed from: f  reason: collision with root package name */
    private final int f14685f;

    /* renamed from: g  reason: collision with root package name */
    private final zzvc f14686g;

    /* renamed from: h  reason: collision with root package name */
    private final zzvf f14687h;

    /* renamed from: j  reason: collision with root package name */
    private final zzvg f14688j;

    /* renamed from: k  reason: collision with root package name */
    private final zzvi f14689k;

    /* renamed from: l  reason: collision with root package name */
    private final zzvh f14690l;

    /* renamed from: m  reason: collision with root package name */
    private final zzvd f14691m;

    /* renamed from: n  reason: collision with root package name */
    private final zzuz f14692n;

    /* renamed from: p  reason: collision with root package name */
    private final zzva f14693p;
    private final zzvb q;

    public zzvj(int i8, String str, String str2, byte[] bArr, Point[] pointArr, int i9, zzvc zzvcVar, zzvf zzvfVar, zzvg zzvgVar, zzvi zzviVar, zzvh zzvhVar, zzvd zzvdVar, zzuz zzuzVar, zzva zzvaVar, zzvb zzvbVar) {
        this.f14680a = i8;
        this.f14681b = str;
        this.f14682c = str2;
        this.f14683d = bArr;
        this.f14684e = pointArr;
        this.f14685f = i9;
        this.f14686g = zzvcVar;
        this.f14687h = zzvfVar;
        this.f14688j = zzvgVar;
        this.f14689k = zzviVar;
        this.f14690l = zzvhVar;
        this.f14691m = zzvdVar;
        this.f14692n = zzuzVar;
        this.f14693p = zzvaVar;
        this.q = zzvbVar;
    }

    public final zzva D0() {
        return this.f14693p;
    }

    public final zzvb E0() {
        return this.q;
    }

    public final zzvc I0() {
        return this.f14686g;
    }

    public final zzvd T0() {
        return this.f14691m;
    }

    public final zzvf W0() {
        return this.f14687h;
    }

    public final zzvg X0() {
        return this.f14688j;
    }

    public final zzuz Z() {
        return this.f14692n;
    }

    public final zzvh Z0() {
        return this.f14690l;
    }

    public final zzvi a1() {
        return this.f14689k;
    }

    public final String b1() {
        return this.f14681b;
    }

    public final String c1() {
        return this.f14682c;
    }

    public final byte[] f1() {
        return this.f14683d;
    }

    public final Point[] g1() {
        return this.f14684e;
    }

    public final int t() {
        return this.f14680a;
    }

    public final int u() {
        return this.f14685f;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 1, this.f14680a);
        o6.a.r(parcel, 2, this.f14681b, false);
        o6.a.r(parcel, 3, this.f14682c, false);
        o6.a.f(parcel, 4, this.f14683d, false);
        o6.a.u(parcel, 5, this.f14684e, i8, false);
        o6.a.l(parcel, 6, this.f14685f);
        o6.a.q(parcel, 7, this.f14686g, i8, false);
        o6.a.q(parcel, 8, this.f14687h, i8, false);
        o6.a.q(parcel, 9, this.f14688j, i8, false);
        o6.a.q(parcel, 10, this.f14689k, i8, false);
        o6.a.q(parcel, 11, this.f14690l, i8, false);
        o6.a.q(parcel, 12, this.f14691m, i8, false);
        o6.a.q(parcel, 13, this.f14692n, i8, false);
        o6.a.q(parcel, 14, this.f14693p, i8, false);
        o6.a.q(parcel, 15, this.q, i8, false);
        o6.a.b(parcel, a9);
    }
}
