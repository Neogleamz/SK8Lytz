package com.google.android.gms.internal.mlkit_vision_barcode;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzuz extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzuz> CREATOR = new lh();

    /* renamed from: a  reason: collision with root package name */
    private final String f14630a;

    /* renamed from: b  reason: collision with root package name */
    private final String f14631b;

    /* renamed from: c  reason: collision with root package name */
    private final String f14632c;

    /* renamed from: d  reason: collision with root package name */
    private final String f14633d;

    /* renamed from: e  reason: collision with root package name */
    private final String f14634e;

    /* renamed from: f  reason: collision with root package name */
    private final zzuy f14635f;

    /* renamed from: g  reason: collision with root package name */
    private final zzuy f14636g;

    public zzuz(String str, String str2, String str3, String str4, String str5, zzuy zzuyVar, zzuy zzuyVar2) {
        this.f14630a = str;
        this.f14631b = str2;
        this.f14632c = str3;
        this.f14633d = str4;
        this.f14634e = str5;
        this.f14635f = zzuyVar;
        this.f14636g = zzuyVar2;
    }

    public final String D0() {
        return this.f14632c;
    }

    public final String E0() {
        return this.f14633d;
    }

    public final String I0() {
        return this.f14634e;
    }

    public final String T0() {
        return this.f14630a;
    }

    public final String Z() {
        return this.f14631b;
    }

    public final zzuy t() {
        return this.f14636g;
    }

    public final zzuy u() {
        return this.f14635f;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.r(parcel, 1, this.f14630a, false);
        o6.a.r(parcel, 2, this.f14631b, false);
        o6.a.r(parcel, 3, this.f14632c, false);
        o6.a.r(parcel, 4, this.f14633d, false);
        o6.a.r(parcel, 5, this.f14634e, false);
        o6.a.q(parcel, 6, this.f14635f, i8, false);
        o6.a.q(parcel, 7, this.f14636g, i8, false);
        o6.a.b(parcel, a9);
    }
}
