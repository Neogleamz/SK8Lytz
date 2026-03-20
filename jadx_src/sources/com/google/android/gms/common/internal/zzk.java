package com.google.android.gms.common.internal;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.Feature;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzk extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzk> CREATOR = new w();

    /* renamed from: a  reason: collision with root package name */
    Bundle f11889a;

    /* renamed from: b  reason: collision with root package name */
    Feature[] f11890b;

    /* renamed from: c  reason: collision with root package name */
    int f11891c;

    /* renamed from: d  reason: collision with root package name */
    ConnectionTelemetryConfiguration f11892d;

    public zzk() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzk(Bundle bundle, Feature[] featureArr, int i8, ConnectionTelemetryConfiguration connectionTelemetryConfiguration) {
        this.f11889a = bundle;
        this.f11890b = featureArr;
        this.f11891c = i8;
        this.f11892d = connectionTelemetryConfiguration;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.e(parcel, 1, this.f11889a, false);
        o6.a.u(parcel, 2, this.f11890b, i8, false);
        o6.a.l(parcel, 3, this.f11891c);
        o6.a.q(parcel, 4, this.f11892d, i8, false);
        o6.a.b(parcel, a9);
    }
}
