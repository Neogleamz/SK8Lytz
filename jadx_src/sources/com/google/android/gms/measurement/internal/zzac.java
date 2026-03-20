package com.google.android.gms.measurement.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzac extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzac> CREATOR = new f();

    /* renamed from: a  reason: collision with root package name */
    public String f17250a;

    /* renamed from: b  reason: collision with root package name */
    public String f17251b;

    /* renamed from: c  reason: collision with root package name */
    public zzno f17252c;

    /* renamed from: d  reason: collision with root package name */
    public long f17253d;

    /* renamed from: e  reason: collision with root package name */
    public boolean f17254e;

    /* renamed from: f  reason: collision with root package name */
    public String f17255f;

    /* renamed from: g  reason: collision with root package name */
    public zzbf f17256g;

    /* renamed from: h  reason: collision with root package name */
    public long f17257h;

    /* renamed from: j  reason: collision with root package name */
    public zzbf f17258j;

    /* renamed from: k  reason: collision with root package name */
    public long f17259k;

    /* renamed from: l  reason: collision with root package name */
    public zzbf f17260l;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzac(zzac zzacVar) {
        n6.j.l(zzacVar);
        this.f17250a = zzacVar.f17250a;
        this.f17251b = zzacVar.f17251b;
        this.f17252c = zzacVar.f17252c;
        this.f17253d = zzacVar.f17253d;
        this.f17254e = zzacVar.f17254e;
        this.f17255f = zzacVar.f17255f;
        this.f17256g = zzacVar.f17256g;
        this.f17257h = zzacVar.f17257h;
        this.f17258j = zzacVar.f17258j;
        this.f17259k = zzacVar.f17259k;
        this.f17260l = zzacVar.f17260l;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzac(String str, String str2, zzno zznoVar, long j8, boolean z4, String str3, zzbf zzbfVar, long j9, zzbf zzbfVar2, long j10, zzbf zzbfVar3) {
        this.f17250a = str;
        this.f17251b = str2;
        this.f17252c = zznoVar;
        this.f17253d = j8;
        this.f17254e = z4;
        this.f17255f = str3;
        this.f17256g = zzbfVar;
        this.f17257h = j9;
        this.f17258j = zzbfVar2;
        this.f17259k = j10;
        this.f17260l = zzbfVar3;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.r(parcel, 2, this.f17250a, false);
        o6.a.r(parcel, 3, this.f17251b, false);
        o6.a.q(parcel, 4, this.f17252c, i8, false);
        o6.a.n(parcel, 5, this.f17253d);
        o6.a.c(parcel, 6, this.f17254e);
        o6.a.r(parcel, 7, this.f17255f, false);
        o6.a.q(parcel, 8, this.f17256g, i8, false);
        o6.a.n(parcel, 9, this.f17257h);
        o6.a.q(parcel, 10, this.f17258j, i8, false);
        o6.a.n(parcel, 11, this.f17259k);
        o6.a.q(parcel, 12, this.f17260l, i8, false);
        o6.a.b(parcel, a9);
    }
}
