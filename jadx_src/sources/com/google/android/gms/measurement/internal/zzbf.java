package com.google.android.gms.measurement.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzbf extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzbf> CREATOR = new f7.b();

    /* renamed from: a  reason: collision with root package name */
    public final String f17263a;

    /* renamed from: b  reason: collision with root package name */
    public final zzba f17264b;

    /* renamed from: c  reason: collision with root package name */
    public final String f17265c;

    /* renamed from: d  reason: collision with root package name */
    public final long f17266d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzbf(zzbf zzbfVar, long j8) {
        n6.j.l(zzbfVar);
        this.f17263a = zzbfVar.f17263a;
        this.f17264b = zzbfVar.f17264b;
        this.f17265c = zzbfVar.f17265c;
        this.f17266d = j8;
    }

    public zzbf(String str, zzba zzbaVar, String str2, long j8) {
        this.f17263a = str;
        this.f17264b = zzbaVar;
        this.f17265c = str2;
        this.f17266d = j8;
    }

    public final String toString() {
        String str = this.f17265c;
        String str2 = this.f17263a;
        String valueOf = String.valueOf(this.f17264b);
        return "origin=" + str + ",name=" + str2 + ",params=" + valueOf;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.r(parcel, 2, this.f17263a, false);
        o6.a.q(parcel, 3, this.f17264b, i8, false);
        o6.a.r(parcel, 4, this.f17265c, false);
        o6.a.n(parcel, 5, this.f17266d);
        o6.a.b(parcel, a9);
    }
}
