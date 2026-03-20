package com.google.android.gms.common.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import n6.j0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ConnectionTelemetryConfiguration extends AbstractSafeParcelable {
    public static final Parcelable.Creator<ConnectionTelemetryConfiguration> CREATOR = new j0();

    /* renamed from: a  reason: collision with root package name */
    private final RootTelemetryConfiguration f11774a;

    /* renamed from: b  reason: collision with root package name */
    private final boolean f11775b;

    /* renamed from: c  reason: collision with root package name */
    private final boolean f11776c;

    /* renamed from: d  reason: collision with root package name */
    private final int[] f11777d;

    /* renamed from: e  reason: collision with root package name */
    private final int f11778e;

    /* renamed from: f  reason: collision with root package name */
    private final int[] f11779f;

    public ConnectionTelemetryConfiguration(RootTelemetryConfiguration rootTelemetryConfiguration, boolean z4, boolean z8, int[] iArr, int i8, int[] iArr2) {
        this.f11774a = rootTelemetryConfiguration;
        this.f11775b = z4;
        this.f11776c = z8;
        this.f11777d = iArr;
        this.f11778e = i8;
        this.f11779f = iArr2;
    }

    public boolean D0() {
        return this.f11775b;
    }

    public boolean E0() {
        return this.f11776c;
    }

    public final RootTelemetryConfiguration I0() {
        return this.f11774a;
    }

    public int[] Z() {
        return this.f11779f;
    }

    public int t() {
        return this.f11778e;
    }

    public int[] u() {
        return this.f11777d;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.q(parcel, 1, this.f11774a, i8, false);
        o6.a.c(parcel, 2, D0());
        o6.a.c(parcel, 3, E0());
        o6.a.m(parcel, 4, u(), false);
        o6.a.l(parcel, 5, t());
        o6.a.m(parcel, 6, Z(), false);
        o6.a.b(parcel, a9);
    }
}
