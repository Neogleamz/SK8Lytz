package com.google.android.gms.internal.mlkit_vision_barcode;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzvc extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzvc> CREATOR = new oh();

    /* renamed from: a  reason: collision with root package name */
    private final int f14658a;

    /* renamed from: b  reason: collision with root package name */
    private final String f14659b;

    /* renamed from: c  reason: collision with root package name */
    private final String f14660c;

    /* renamed from: d  reason: collision with root package name */
    private final String f14661d;

    public zzvc(int i8, String str, String str2, String str3) {
        this.f14658a = i8;
        this.f14659b = str;
        this.f14660c = str2;
        this.f14661d = str3;
    }

    public final String D0() {
        return this.f14660c;
    }

    public final String Z() {
        return this.f14661d;
    }

    public final int t() {
        return this.f14658a;
    }

    public final String u() {
        return this.f14659b;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 1, this.f14658a);
        o6.a.r(parcel, 2, this.f14659b, false);
        o6.a.r(parcel, 3, this.f14660c, false);
        o6.a.r(parcel, 4, this.f14661d, false);
        o6.a.b(parcel, a9);
    }
}
