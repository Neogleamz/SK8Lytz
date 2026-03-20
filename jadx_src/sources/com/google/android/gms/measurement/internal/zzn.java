package com.google.android.gms.measurement.internal;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzn extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzn> CREATOR = new tb();
    private final String A;
    public final String B;
    public final String C;
    public final String E;
    public final boolean F;
    public final long G;
    public final int H;
    public final String K;
    public final int L;
    public final long O;
    public final String P;
    public final String Q;

    /* renamed from: a  reason: collision with root package name */
    public final String f17288a;

    /* renamed from: b  reason: collision with root package name */
    public final String f17289b;

    /* renamed from: c  reason: collision with root package name */
    public final String f17290c;

    /* renamed from: d  reason: collision with root package name */
    public final String f17291d;

    /* renamed from: e  reason: collision with root package name */
    public final long f17292e;

    /* renamed from: f  reason: collision with root package name */
    public final long f17293f;

    /* renamed from: g  reason: collision with root package name */
    public final String f17294g;

    /* renamed from: h  reason: collision with root package name */
    public final boolean f17295h;

    /* renamed from: j  reason: collision with root package name */
    public final boolean f17296j;

    /* renamed from: k  reason: collision with root package name */
    public final long f17297k;

    /* renamed from: l  reason: collision with root package name */
    public final String f17298l;
    @Deprecated

    /* renamed from: m  reason: collision with root package name */
    private final long f17299m;

    /* renamed from: n  reason: collision with root package name */
    public final long f17300n;

    /* renamed from: p  reason: collision with root package name */
    public final int f17301p;
    public final boolean q;

    /* renamed from: t  reason: collision with root package name */
    public final boolean f17302t;

    /* renamed from: w  reason: collision with root package name */
    public final String f17303w;

    /* renamed from: x  reason: collision with root package name */
    public final Boolean f17304x;

    /* renamed from: y  reason: collision with root package name */
    public final long f17305y;

    /* renamed from: z  reason: collision with root package name */
    public final List<String> f17306z;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzn(String str, String str2, String str3, long j8, String str4, long j9, long j10, String str5, boolean z4, boolean z8, String str6, long j11, long j12, int i8, boolean z9, boolean z10, String str7, Boolean bool, long j13, List<String> list, String str8, String str9, String str10, String str11, boolean z11, long j14, int i9, String str12, int i10, long j15, String str13, String str14) {
        n6.j.f(str);
        this.f17288a = str;
        this.f17289b = TextUtils.isEmpty(str2) ? null : str2;
        this.f17290c = str3;
        this.f17297k = j8;
        this.f17291d = str4;
        this.f17292e = j9;
        this.f17293f = j10;
        this.f17294g = str5;
        this.f17295h = z4;
        this.f17296j = z8;
        this.f17298l = str6;
        this.f17299m = j11;
        this.f17300n = j12;
        this.f17301p = i8;
        this.q = z9;
        this.f17302t = z10;
        this.f17303w = str7;
        this.f17304x = bool;
        this.f17305y = j13;
        this.f17306z = list;
        this.A = null;
        this.B = str9;
        this.C = str10;
        this.E = str11;
        this.F = z11;
        this.G = j14;
        this.H = i9;
        this.K = str12;
        this.L = i10;
        this.O = j15;
        this.P = str13;
        this.Q = str14;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzn(String str, String str2, String str3, String str4, long j8, long j9, String str5, boolean z4, boolean z8, long j10, String str6, long j11, long j12, int i8, boolean z9, boolean z10, String str7, Boolean bool, long j13, List<String> list, String str8, String str9, String str10, String str11, boolean z11, long j14, int i9, String str12, int i10, long j15, String str13, String str14) {
        this.f17288a = str;
        this.f17289b = str2;
        this.f17290c = str3;
        this.f17297k = j10;
        this.f17291d = str4;
        this.f17292e = j8;
        this.f17293f = j9;
        this.f17294g = str5;
        this.f17295h = z4;
        this.f17296j = z8;
        this.f17298l = str6;
        this.f17299m = j11;
        this.f17300n = j12;
        this.f17301p = i8;
        this.q = z9;
        this.f17302t = z10;
        this.f17303w = str7;
        this.f17304x = bool;
        this.f17305y = j13;
        this.f17306z = list;
        this.A = str8;
        this.B = str9;
        this.C = str10;
        this.E = str11;
        this.F = z11;
        this.G = j14;
        this.H = i9;
        this.K = str12;
        this.L = i10;
        this.O = j15;
        this.P = str13;
        this.Q = str14;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.r(parcel, 2, this.f17288a, false);
        o6.a.r(parcel, 3, this.f17289b, false);
        o6.a.r(parcel, 4, this.f17290c, false);
        o6.a.r(parcel, 5, this.f17291d, false);
        o6.a.n(parcel, 6, this.f17292e);
        o6.a.n(parcel, 7, this.f17293f);
        o6.a.r(parcel, 8, this.f17294g, false);
        o6.a.c(parcel, 9, this.f17295h);
        o6.a.c(parcel, 10, this.f17296j);
        o6.a.n(parcel, 11, this.f17297k);
        o6.a.r(parcel, 12, this.f17298l, false);
        o6.a.n(parcel, 13, this.f17299m);
        o6.a.n(parcel, 14, this.f17300n);
        o6.a.l(parcel, 15, this.f17301p);
        o6.a.c(parcel, 16, this.q);
        o6.a.c(parcel, 18, this.f17302t);
        o6.a.r(parcel, 19, this.f17303w, false);
        o6.a.d(parcel, 21, this.f17304x, false);
        o6.a.n(parcel, 22, this.f17305y);
        o6.a.t(parcel, 23, this.f17306z, false);
        o6.a.r(parcel, 24, this.A, false);
        o6.a.r(parcel, 25, this.B, false);
        o6.a.r(parcel, 26, this.C, false);
        o6.a.r(parcel, 27, this.E, false);
        o6.a.c(parcel, 28, this.F);
        o6.a.n(parcel, 29, this.G);
        o6.a.l(parcel, 30, this.H);
        o6.a.r(parcel, 31, this.K, false);
        o6.a.l(parcel, 32, this.L);
        o6.a.n(parcel, 34, this.O);
        o6.a.r(parcel, 35, this.P, false);
        o6.a.r(parcel, 36, this.Q, false);
        o6.a.b(parcel, a9);
    }
}
