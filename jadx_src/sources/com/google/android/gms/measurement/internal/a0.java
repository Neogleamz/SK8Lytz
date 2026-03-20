package com.google.android.gms.measurement.internal;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a0 implements Parcelable.Creator<zzba> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzba createFromParcel(Parcel parcel) {
        int I = SafeParcelReader.I(parcel);
        Bundle bundle = null;
        while (parcel.dataPosition() < I) {
            int B = SafeParcelReader.B(parcel);
            if (SafeParcelReader.u(B) != 2) {
                SafeParcelReader.H(parcel, B);
            } else {
                bundle = SafeParcelReader.f(parcel, B);
            }
        }
        SafeParcelReader.t(parcel, I);
        return new zzba(bundle);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzba[] newArray(int i8) {
        return new zzba[i8];
    }
}
