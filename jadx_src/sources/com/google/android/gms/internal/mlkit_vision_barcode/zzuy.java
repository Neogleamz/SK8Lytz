package com.google.android.gms.internal.mlkit_vision_barcode;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzuy extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzuy> CREATOR = new kh();

    /* renamed from: a  reason: collision with root package name */
    private final int f14622a;

    /* renamed from: b  reason: collision with root package name */
    private final int f14623b;

    /* renamed from: c  reason: collision with root package name */
    private final int f14624c;

    /* renamed from: d  reason: collision with root package name */
    private final int f14625d;

    /* renamed from: e  reason: collision with root package name */
    private final int f14626e;

    /* renamed from: f  reason: collision with root package name */
    private final int f14627f;

    /* renamed from: g  reason: collision with root package name */
    private final boolean f14628g;

    /* renamed from: h  reason: collision with root package name */
    private final String f14629h;

    public zzuy(int i8, int i9, int i10, int i11, int i12, int i13, boolean z4, String str) {
        this.f14622a = i8;
        this.f14623b = i9;
        this.f14624c = i10;
        this.f14625d = i11;
        this.f14626e = i12;
        this.f14627f = i13;
        this.f14628g = z4;
        this.f14629h = str;
    }

    public final int D0() {
        return this.f14623b;
    }

    public final int E0() {
        return this.f14627f;
    }

    public final int I0() {
        return this.f14622a;
    }

    public final String T0() {
        return this.f14629h;
    }

    public final boolean W0() {
        return this.f14628g;
    }

    public final int Z() {
        return this.f14626e;
    }

    public final int t() {
        return this.f14624c;
    }

    public final int u() {
        return this.f14625d;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 1, this.f14622a);
        o6.a.l(parcel, 2, this.f14623b);
        o6.a.l(parcel, 3, this.f14624c);
        o6.a.l(parcel, 4, this.f14625d);
        o6.a.l(parcel, 5, this.f14626e);
        o6.a.l(parcel, 6, this.f14627f);
        o6.a.c(parcel, 7, this.f14628g);
        o6.a.r(parcel, 8, this.f14629h, false);
        o6.a.b(parcel, a9);
    }
}
