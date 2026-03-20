package com.google.android.gms.internal.mlkit_vision_barcode;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzl extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzl> CREATOR = new ci();

    /* renamed from: a  reason: collision with root package name */
    public zzp f14337a;

    /* renamed from: b  reason: collision with root package name */
    public String f14338b;

    /* renamed from: c  reason: collision with root package name */
    public String f14339c;

    /* renamed from: d  reason: collision with root package name */
    public zzq[] f14340d;

    /* renamed from: e  reason: collision with root package name */
    public zzn[] f14341e;

    /* renamed from: f  reason: collision with root package name */
    public String[] f14342f;

    /* renamed from: g  reason: collision with root package name */
    public zzi[] f14343g;

    public zzl() {
    }

    public zzl(zzp zzpVar, String str, String str2, zzq[] zzqVarArr, zzn[] zznVarArr, String[] strArr, zzi[] zziVarArr) {
        this.f14337a = zzpVar;
        this.f14338b = str;
        this.f14339c = str2;
        this.f14340d = zzqVarArr;
        this.f14341e = zznVarArr;
        this.f14342f = strArr;
        this.f14343g = zziVarArr;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.q(parcel, 2, this.f14337a, i8, false);
        o6.a.r(parcel, 3, this.f14338b, false);
        o6.a.r(parcel, 4, this.f14339c, false);
        o6.a.u(parcel, 5, this.f14340d, i8, false);
        o6.a.u(parcel, 6, this.f14341e, i8, false);
        o6.a.s(parcel, 7, this.f14342f, false);
        o6.a.u(parcel, 8, this.f14343g, i8, false);
        o6.a.b(parcel, a9);
    }
}
