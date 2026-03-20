package com.google.android.gms.common.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import n6.h0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class RootTelemetryConfiguration extends AbstractSafeParcelable {
    public static final Parcelable.Creator<RootTelemetryConfiguration> CREATOR = new h0();

    /* renamed from: a  reason: collision with root package name */
    private final int f11806a;

    /* renamed from: b  reason: collision with root package name */
    private final boolean f11807b;

    /* renamed from: c  reason: collision with root package name */
    private final boolean f11808c;

    /* renamed from: d  reason: collision with root package name */
    private final int f11809d;

    /* renamed from: e  reason: collision with root package name */
    private final int f11810e;

    public RootTelemetryConfiguration(int i8, boolean z4, boolean z8, int i9, int i10) {
        this.f11806a = i8;
        this.f11807b = z4;
        this.f11808c = z8;
        this.f11809d = i9;
        this.f11810e = i10;
    }

    public boolean D0() {
        return this.f11808c;
    }

    public int E0() {
        return this.f11806a;
    }

    public boolean Z() {
        return this.f11807b;
    }

    public int t() {
        return this.f11809d;
    }

    public int u() {
        return this.f11810e;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 1, E0());
        o6.a.c(parcel, 2, Z());
        o6.a.c(parcel, 3, D0());
        o6.a.l(parcel, 4, t());
        o6.a.l(parcel, 5, u());
        o6.a.b(parcel, a9);
    }
}
