package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzar extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzar> CREATOR = new u();

    /* renamed from: a  reason: collision with root package name */
    private final zzav f14913a;

    /* renamed from: b  reason: collision with root package name */
    private final String f14914b;

    /* renamed from: c  reason: collision with root package name */
    private final String f14915c;

    /* renamed from: d  reason: collision with root package name */
    private final zzaw[] f14916d;

    /* renamed from: e  reason: collision with root package name */
    private final zzat[] f14917e;

    /* renamed from: f  reason: collision with root package name */
    private final String[] f14918f;

    /* renamed from: g  reason: collision with root package name */
    private final zzao[] f14919g;

    public zzar(zzav zzavVar, String str, String str2, zzaw[] zzawVarArr, zzat[] zzatVarArr, String[] strArr, zzao[] zzaoVarArr) {
        this.f14913a = zzavVar;
        this.f14914b = str;
        this.f14915c = str2;
        this.f14916d = zzawVarArr;
        this.f14917e = zzatVarArr;
        this.f14918f = strArr;
        this.f14919g = zzaoVarArr;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.q(parcel, 1, this.f14913a, i8, false);
        o6.a.r(parcel, 2, this.f14914b, false);
        o6.a.r(parcel, 3, this.f14915c, false);
        o6.a.u(parcel, 4, this.f14916d, i8, false);
        o6.a.u(parcel, 5, this.f14917e, i8, false);
        o6.a.s(parcel, 6, this.f14918f, false);
        o6.a.u(parcel, 7, this.f14919g, i8, false);
        o6.a.b(parcel, a9);
    }
}
