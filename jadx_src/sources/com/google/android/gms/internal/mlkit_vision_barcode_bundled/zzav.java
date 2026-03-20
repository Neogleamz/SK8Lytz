package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzav extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzav> CREATOR = new d0();

    /* renamed from: a  reason: collision with root package name */
    private final String f14940a;

    /* renamed from: b  reason: collision with root package name */
    private final String f14941b;

    /* renamed from: c  reason: collision with root package name */
    private final String f14942c;

    /* renamed from: d  reason: collision with root package name */
    private final String f14943d;

    /* renamed from: e  reason: collision with root package name */
    private final String f14944e;

    /* renamed from: f  reason: collision with root package name */
    private final String f14945f;

    /* renamed from: g  reason: collision with root package name */
    private final String f14946g;

    public zzav(String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        this.f14940a = str;
        this.f14941b = str2;
        this.f14942c = str3;
        this.f14943d = str4;
        this.f14944e = str5;
        this.f14945f = str6;
        this.f14946g = str7;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.r(parcel, 1, this.f14940a, false);
        o6.a.r(parcel, 2, this.f14941b, false);
        o6.a.r(parcel, 3, this.f14942c, false);
        o6.a.r(parcel, 4, this.f14943d, false);
        o6.a.r(parcel, 5, this.f14944e, false);
        o6.a.r(parcel, 6, this.f14945f, false);
        o6.a.r(parcel, 7, this.f14946g, false);
        o6.a.b(parcel, a9);
    }
}
