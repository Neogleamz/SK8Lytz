package com.google.android.gms.internal.measurement;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzdq extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzdq> CREATOR = new n2();

    /* renamed from: a  reason: collision with root package name */
    public final long f12785a;

    /* renamed from: b  reason: collision with root package name */
    public final long f12786b;

    /* renamed from: c  reason: collision with root package name */
    public final boolean f12787c;

    /* renamed from: d  reason: collision with root package name */
    public final String f12788d;

    /* renamed from: e  reason: collision with root package name */
    public final String f12789e;

    /* renamed from: f  reason: collision with root package name */
    public final String f12790f;

    /* renamed from: g  reason: collision with root package name */
    public final Bundle f12791g;

    /* renamed from: h  reason: collision with root package name */
    public final String f12792h;

    public zzdq(long j8, long j9, boolean z4, String str, String str2, String str3, Bundle bundle, String str4) {
        this.f12785a = j8;
        this.f12786b = j9;
        this.f12787c = z4;
        this.f12788d = str;
        this.f12789e = str2;
        this.f12790f = str3;
        this.f12791g = bundle;
        this.f12792h = str4;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.n(parcel, 1, this.f12785a);
        o6.a.n(parcel, 2, this.f12786b);
        o6.a.c(parcel, 3, this.f12787c);
        o6.a.r(parcel, 4, this.f12788d, false);
        o6.a.r(parcel, 5, this.f12789e, false);
        o6.a.r(parcel, 6, this.f12790f, false);
        o6.a.e(parcel, 7, this.f12791g, false);
        o6.a.r(parcel, 8, this.f12792h, false);
        o6.a.b(parcel, a9);
    }
}
