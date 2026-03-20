package com.google.android.gms.internal.mlkit_vision_barcode;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzve extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzve> CREATOR = new uh();

    /* renamed from: a  reason: collision with root package name */
    private final String f14664a;

    /* renamed from: b  reason: collision with root package name */
    private final String f14665b;

    /* renamed from: c  reason: collision with root package name */
    private final String f14666c;

    /* renamed from: d  reason: collision with root package name */
    private final String f14667d;

    /* renamed from: e  reason: collision with root package name */
    private final String f14668e;

    /* renamed from: f  reason: collision with root package name */
    private final String f14669f;

    /* renamed from: g  reason: collision with root package name */
    private final String f14670g;

    public zzve(String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        this.f14664a = str;
        this.f14665b = str2;
        this.f14666c = str3;
        this.f14667d = str4;
        this.f14668e = str5;
        this.f14669f = str6;
        this.f14670g = str7;
    }

    public final String D0() {
        return this.f14668e;
    }

    public final String E0() {
        return this.f14666c;
    }

    public final String I0() {
        return this.f14665b;
    }

    public final String T0() {
        return this.f14670g;
    }

    public final String Z() {
        return this.f14669f;
    }

    public final String t() {
        return this.f14667d;
    }

    public final String u() {
        return this.f14664a;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.r(parcel, 1, this.f14664a, false);
        o6.a.r(parcel, 2, this.f14665b, false);
        o6.a.r(parcel, 3, this.f14666c, false);
        o6.a.r(parcel, 4, this.f14667d, false);
        o6.a.r(parcel, 5, this.f14668e, false);
        o6.a.r(parcel, 6, this.f14669f, false);
        o6.a.r(parcel, 7, this.f14670g, false);
        o6.a.b(parcel, a9);
    }
}
