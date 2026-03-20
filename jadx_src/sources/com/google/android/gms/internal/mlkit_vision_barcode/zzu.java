package com.google.android.gms.internal.mlkit_vision_barcode;

import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzu extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzu> CREATOR = new hh();

    /* renamed from: a  reason: collision with root package name */
    public int f14604a;

    /* renamed from: b  reason: collision with root package name */
    public String f14605b;

    /* renamed from: c  reason: collision with root package name */
    public String f14606c;

    /* renamed from: d  reason: collision with root package name */
    public int f14607d;

    /* renamed from: e  reason: collision with root package name */
    public Point[] f14608e;

    /* renamed from: f  reason: collision with root package name */
    public zzn f14609f;

    /* renamed from: g  reason: collision with root package name */
    public zzq f14610g;

    /* renamed from: h  reason: collision with root package name */
    public zzr f14611h;

    /* renamed from: j  reason: collision with root package name */
    public zzt f14612j;

    /* renamed from: k  reason: collision with root package name */
    public zzs f14613k;

    /* renamed from: l  reason: collision with root package name */
    public zzo f14614l;

    /* renamed from: m  reason: collision with root package name */
    public zzk f14615m;

    /* renamed from: n  reason: collision with root package name */
    public zzl f14616n;

    /* renamed from: p  reason: collision with root package name */
    public zzm f14617p;
    public byte[] q;

    /* renamed from: t  reason: collision with root package name */
    public boolean f14618t;

    /* renamed from: w  reason: collision with root package name */
    public double f14619w;

    public zzu() {
    }

    public zzu(int i8, String str, String str2, int i9, Point[] pointArr, zzn zznVar, zzq zzqVar, zzr zzrVar, zzt zztVar, zzs zzsVar, zzo zzoVar, zzk zzkVar, zzl zzlVar, zzm zzmVar, byte[] bArr, boolean z4, double d8) {
        this.f14604a = i8;
        this.f14605b = str;
        this.q = bArr;
        this.f14606c = str2;
        this.f14607d = i9;
        this.f14608e = pointArr;
        this.f14618t = z4;
        this.f14619w = d8;
        this.f14609f = zznVar;
        this.f14610g = zzqVar;
        this.f14611h = zzrVar;
        this.f14612j = zztVar;
        this.f14613k = zzsVar;
        this.f14614l = zzoVar;
        this.f14615m = zzkVar;
        this.f14616n = zzlVar;
        this.f14617p = zzmVar;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 2, this.f14604a);
        o6.a.r(parcel, 3, this.f14605b, false);
        o6.a.r(parcel, 4, this.f14606c, false);
        o6.a.l(parcel, 5, this.f14607d);
        o6.a.u(parcel, 6, this.f14608e, i8, false);
        o6.a.q(parcel, 7, this.f14609f, i8, false);
        o6.a.q(parcel, 8, this.f14610g, i8, false);
        o6.a.q(parcel, 9, this.f14611h, i8, false);
        o6.a.q(parcel, 10, this.f14612j, i8, false);
        o6.a.q(parcel, 11, this.f14613k, i8, false);
        o6.a.q(parcel, 12, this.f14614l, i8, false);
        o6.a.q(parcel, 13, this.f14615m, i8, false);
        o6.a.q(parcel, 14, this.f14616n, i8, false);
        o6.a.q(parcel, 15, this.f14617p, i8, false);
        o6.a.f(parcel, 16, this.q, false);
        o6.a.c(parcel, 17, this.f14618t);
        o6.a.g(parcel, 18, this.f14619w);
        o6.a.b(parcel, a9);
    }
}
