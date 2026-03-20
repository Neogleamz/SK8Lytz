package com.google.android.gms.common.internal;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.Feature;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class w implements Parcelable.Creator {
    @Override // android.os.Parcelable.Creator
    public final /* bridge */ /* synthetic */ Object createFromParcel(Parcel parcel) {
        int I = SafeParcelReader.I(parcel);
        Bundle bundle = null;
        ConnectionTelemetryConfiguration connectionTelemetryConfiguration = null;
        int i8 = 0;
        Feature[] featureArr = null;
        while (parcel.dataPosition() < I) {
            int B = SafeParcelReader.B(parcel);
            int u8 = SafeParcelReader.u(B);
            if (u8 == 1) {
                bundle = SafeParcelReader.f(parcel, B);
            } else if (u8 == 2) {
                featureArr = (Feature[]) SafeParcelReader.r(parcel, B, Feature.CREATOR);
            } else if (u8 == 3) {
                i8 = SafeParcelReader.D(parcel, B);
            } else if (u8 != 4) {
                SafeParcelReader.H(parcel, B);
            } else {
                connectionTelemetryConfiguration = (ConnectionTelemetryConfiguration) SafeParcelReader.n(parcel, B, ConnectionTelemetryConfiguration.CREATOR);
            }
        }
        SafeParcelReader.t(parcel, I);
        return new zzk(bundle, featureArr, i8, connectionTelemetryConfiguration);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ Object[] newArray(int i8) {
        return new zzk[i8];
    }
}
