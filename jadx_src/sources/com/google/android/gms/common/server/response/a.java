package com.google.android.gms.common.server.response;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
import com.google.android.gms.common.server.converter.zaa;
import com.google.android.gms.common.server.response.FastJsonResponse;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a implements Parcelable.Creator {
    @Override // android.os.Parcelable.Creator
    public final /* bridge */ /* synthetic */ Object createFromParcel(Parcel parcel) {
        int I = SafeParcelReader.I(parcel);
        String str = null;
        String str2 = null;
        zaa zaaVar = null;
        int i8 = 0;
        int i9 = 0;
        boolean z4 = false;
        int i10 = 0;
        boolean z8 = false;
        int i11 = 0;
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
                    z4 = SafeParcelReader.v(parcel, B);
                    break;
                case 4:
                    i10 = SafeParcelReader.D(parcel, B);
                    break;
                case 5:
                    z8 = SafeParcelReader.v(parcel, B);
                    break;
                case 6:
                    str = SafeParcelReader.o(parcel, B);
                    break;
                case 7:
                    i11 = SafeParcelReader.D(parcel, B);
                    break;
                case 8:
                    str2 = SafeParcelReader.o(parcel, B);
                    break;
                case 9:
                    zaaVar = (zaa) SafeParcelReader.n(parcel, B, zaa.CREATOR);
                    break;
                default:
                    SafeParcelReader.H(parcel, B);
                    break;
            }
        }
        SafeParcelReader.t(parcel, I);
        return new FastJsonResponse.Field(i8, i9, z4, i10, z8, str, i11, str2, zaaVar);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ Object[] newArray(int i8) {
        return new FastJsonResponse.Field[i8];
    }
}
