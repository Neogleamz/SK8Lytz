package com.google.android.gms.internal.mlkit_vision_barcode;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzk extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzk> CREATOR = new bi();

    /* renamed from: a  reason: collision with root package name */
    public String f14330a;

    /* renamed from: b  reason: collision with root package name */
    public String f14331b;

    /* renamed from: c  reason: collision with root package name */
    public String f14332c;

    /* renamed from: d  reason: collision with root package name */
    public String f14333d;

    /* renamed from: e  reason: collision with root package name */
    public String f14334e;

    /* renamed from: f  reason: collision with root package name */
    public zzj f14335f;

    /* renamed from: g  reason: collision with root package name */
    public zzj f14336g;

    public zzk() {
    }

    public zzk(String str, String str2, String str3, String str4, String str5, zzj zzjVar, zzj zzjVar2) {
        this.f14330a = str;
        this.f14331b = str2;
        this.f14332c = str3;
        this.f14333d = str4;
        this.f14334e = str5;
        this.f14335f = zzjVar;
        this.f14336g = zzjVar2;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.r(parcel, 2, this.f14330a, false);
        o6.a.r(parcel, 3, this.f14331b, false);
        o6.a.r(parcel, 4, this.f14332c, false);
        o6.a.r(parcel, 5, this.f14333d, false);
        o6.a.r(parcel, 6, this.f14334e, false);
        o6.a.q(parcel, 7, this.f14335f, i8, false);
        o6.a.q(parcel, 8, this.f14336g, i8, false);
        o6.a.b(parcel, a9);
    }
}
