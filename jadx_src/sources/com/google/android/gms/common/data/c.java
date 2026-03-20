package com.google.android.gms.common.data;

import android.database.CursorWindow;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c implements Parcelable.Creator {
    @Override // android.os.Parcelable.Creator
    public final /* bridge */ /* synthetic */ Object createFromParcel(Parcel parcel) {
        int I = SafeParcelReader.I(parcel);
        int i8 = 0;
        int i9 = 0;
        String[] strArr = null;
        CursorWindow[] cursorWindowArr = null;
        Bundle bundle = null;
        while (parcel.dataPosition() < I) {
            int B = SafeParcelReader.B(parcel);
            int u8 = SafeParcelReader.u(B);
            if (u8 == 1) {
                strArr = SafeParcelReader.p(parcel, B);
            } else if (u8 == 2) {
                cursorWindowArr = (CursorWindow[]) SafeParcelReader.r(parcel, B, CursorWindow.CREATOR);
            } else if (u8 == 3) {
                i9 = SafeParcelReader.D(parcel, B);
            } else if (u8 == 4) {
                bundle = SafeParcelReader.f(parcel, B);
            } else if (u8 != 1000) {
                SafeParcelReader.H(parcel, B);
            } else {
                i8 = SafeParcelReader.D(parcel, B);
            }
        }
        SafeParcelReader.t(parcel, I);
        DataHolder dataHolder = new DataHolder(i8, strArr, cursorWindowArr, i9, bundle);
        dataHolder.Z();
        return dataHolder;
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ Object[] newArray(int i8) {
        return new DataHolder[i8];
    }
}
