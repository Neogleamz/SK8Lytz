package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzaq extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzaq> CREATOR = new t();

    /* renamed from: a  reason: collision with root package name */
    private final String f14906a;

    /* renamed from: b  reason: collision with root package name */
    private final String f14907b;

    /* renamed from: c  reason: collision with root package name */
    private final String f14908c;

    /* renamed from: d  reason: collision with root package name */
    private final String f14909d;

    /* renamed from: e  reason: collision with root package name */
    private final String f14910e;

    /* renamed from: f  reason: collision with root package name */
    private final zzap f14911f;

    /* renamed from: g  reason: collision with root package name */
    private final zzap f14912g;

    public zzaq(String str, String str2, String str3, String str4, String str5, zzap zzapVar, zzap zzapVar2) {
        this.f14906a = str;
        this.f14907b = str2;
        this.f14908c = str3;
        this.f14909d = str4;
        this.f14910e = str5;
        this.f14911f = zzapVar;
        this.f14912g = zzapVar2;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.r(parcel, 1, this.f14906a, false);
        o6.a.r(parcel, 2, this.f14907b, false);
        o6.a.r(parcel, 3, this.f14908c, false);
        o6.a.r(parcel, 4, this.f14909d, false);
        o6.a.r(parcel, 5, this.f14910e, false);
        o6.a.q(parcel, 6, this.f14911f, i8, false);
        o6.a.q(parcel, 7, this.f14912g, i8, false);
        o6.a.b(parcel, a9);
    }
}
