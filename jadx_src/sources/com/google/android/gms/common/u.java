package com.google.android.gms.common;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class u implements Parcelable.Creator {
    @Override // android.os.Parcelable.Creator
    public final /* bridge */ /* synthetic */ Object createFromParcel(Parcel parcel) {
        int I = SafeParcelReader.I(parcel);
        String str = null;
        boolean z4 = false;
        boolean z8 = false;
        IBinder iBinder = null;
        while (parcel.dataPosition() < I) {
            int B = SafeParcelReader.B(parcel);
            int u8 = SafeParcelReader.u(B);
            if (u8 == 1) {
                str = SafeParcelReader.o(parcel, B);
            } else if (u8 == 2) {
                iBinder = SafeParcelReader.C(parcel, B);
            } else if (u8 == 3) {
                z4 = SafeParcelReader.v(parcel, B);
            } else if (u8 != 4) {
                SafeParcelReader.H(parcel, B);
            } else {
                z8 = SafeParcelReader.v(parcel, B);
            }
        }
        SafeParcelReader.t(parcel, I);
        return new zzs(str, iBinder, z4, z8);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ Object[] newArray(int i8) {
        return new zzs[i8];
    }
}
