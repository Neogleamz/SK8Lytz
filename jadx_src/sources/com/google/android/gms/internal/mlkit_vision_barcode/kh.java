package com.google.android.gms.internal.mlkit_vision_barcode;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class kh implements Parcelable.Creator {
    @Override // android.os.Parcelable.Creator
    public final /* bridge */ /* synthetic */ Object createFromParcel(Parcel parcel) {
        int I = SafeParcelReader.I(parcel);
        int i8 = 0;
        int i9 = 0;
        int i10 = 0;
        int i11 = 0;
        int i12 = 0;
        int i13 = 0;
        boolean z4 = false;
        String str = null;
        while (parcel.dataPosition() < I) {
            int B = SafeParcelReader.B(parcel);
            switch (SafeParcelReader.u(B)) {
                case 1:
                    i8 = SafeParcelReader.D(parcel, B);
                    break;
                case 2:
                    i9 = SafeParcelReader.D(parcel, B);
                    break;
                case 3:
                    i10 = SafeParcelReader.D(parcel, B);
                    break;
                case 4:
                    i11 = SafeParcelReader.D(parcel, B);
                    break;
                case 5:
                    i12 = SafeParcelReader.D(parcel, B);
                    break;
                case 6:
                    i13 = SafeParcelReader.D(parcel, B);
                    break;
                case 7:
                    z4 = SafeParcelReader.v(parcel, B);
                    break;
                case 8:
                    str = SafeParcelReader.o(parcel, B);
                    break;
                default:
                    SafeParcelReader.H(parcel, B);
                    break;
            }
        }
        SafeParcelReader.t(parcel, I);
        return new zzuy(i8, i9, i10, i11, i12, i13, z4, str);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ Object[] newArray(int i8) {
        return new zzuy[i8];
    }
}
