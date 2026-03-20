package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzat extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzat> CREATOR = new w();

    /* renamed from: a  reason: collision with root package name */
    private final int f14934a;

    /* renamed from: b  reason: collision with root package name */
    private final String f14935b;

    /* renamed from: c  reason: collision with root package name */
    private final String f14936c;

    /* renamed from: d  reason: collision with root package name */
    private final String f14937d;

    public zzat(int i8, String str, String str2, String str3) {
        this.f14934a = i8;
        this.f14935b = str;
        this.f14936c = str2;
        this.f14937d = str3;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 1, this.f14934a);
        o6.a.r(parcel, 2, this.f14935b, false);
        o6.a.r(parcel, 3, this.f14936c, false);
        o6.a.r(parcel, 4, this.f14937d, false);
        o6.a.b(parcel, a9);
    }
}
