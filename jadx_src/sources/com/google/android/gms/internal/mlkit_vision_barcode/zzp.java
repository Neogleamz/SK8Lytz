package com.google.android.gms.internal.mlkit_vision_barcode;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzp extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzp> CREATOR = new d();

    /* renamed from: a  reason: collision with root package name */
    public String f14376a;

    /* renamed from: b  reason: collision with root package name */
    public String f14377b;

    /* renamed from: c  reason: collision with root package name */
    public String f14378c;

    /* renamed from: d  reason: collision with root package name */
    public String f14379d;

    /* renamed from: e  reason: collision with root package name */
    public String f14380e;

    /* renamed from: f  reason: collision with root package name */
    public String f14381f;

    /* renamed from: g  reason: collision with root package name */
    public String f14382g;

    public zzp() {
    }

    public zzp(String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        this.f14376a = str;
        this.f14377b = str2;
        this.f14378c = str3;
        this.f14379d = str4;
        this.f14380e = str5;
        this.f14381f = str6;
        this.f14382g = str7;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.r(parcel, 2, this.f14376a, false);
        o6.a.r(parcel, 3, this.f14377b, false);
        o6.a.r(parcel, 4, this.f14378c, false);
        o6.a.r(parcel, 5, this.f14379d, false);
        o6.a.r(parcel, 6, this.f14380e, false);
        o6.a.r(parcel, 7, this.f14381f, false);
        o6.a.r(parcel, 8, this.f14382g, false);
        o6.a.b(parcel, a9);
    }
}
