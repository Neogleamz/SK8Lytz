package com.google.android.gms.common.stats;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
import java.util.ArrayList;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a implements Parcelable.Creator {
    @Override // android.os.Parcelable.Creator
    public final /* bridge */ /* synthetic */ Object createFromParcel(Parcel parcel) {
        int I = SafeParcelReader.I(parcel);
        long j8 = 0;
        long j9 = 0;
        long j10 = 0;
        int i8 = 0;
        int i9 = 0;
        int i10 = 0;
        int i11 = 0;
        boolean z4 = false;
        String str = null;
        ArrayList<String> arrayList = null;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        float f5 = 0.0f;
        while (parcel.dataPosition() < I) {
            int B = SafeParcelReader.B(parcel);
            switch (SafeParcelReader.u(B)) {
                case 1:
                    i8 = SafeParcelReader.D(parcel, B);
                    break;
                case 2:
                    j8 = SafeParcelReader.E(parcel, B);
                    break;
                case 3:
                case 7:
                case 9:
                default:
                    SafeParcelReader.H(parcel, B);
                    break;
                case 4:
                    str = SafeParcelReader.o(parcel, B);
                    break;
                case 5:
                    i10 = SafeParcelReader.D(parcel, B);
                    break;
                case 6:
                    arrayList = SafeParcelReader.q(parcel, B);
                    break;
                case 8:
                    j9 = SafeParcelReader.E(parcel, B);
                    break;
                case 10:
                    str3 = SafeParcelReader.o(parcel, B);
                    break;
                case 11:
                    i9 = SafeParcelReader.D(parcel, B);
                    break;
                case 12:
                    str2 = SafeParcelReader.o(parcel, B);
                    break;
                case 13:
                    str4 = SafeParcelReader.o(parcel, B);
                    break;
                case 14:
                    i11 = SafeParcelReader.D(parcel, B);
                    break;
                case 15:
                    f5 = SafeParcelReader.z(parcel, B);
                    break;
                case 16:
                    j10 = SafeParcelReader.E(parcel, B);
                    break;
                case 17:
                    str5 = SafeParcelReader.o(parcel, B);
                    break;
                case 18:
                    z4 = SafeParcelReader.v(parcel, B);
                    break;
            }
        }
        SafeParcelReader.t(parcel, I);
        return new WakeLockEvent(i8, j8, i9, str, i10, arrayList, str2, j9, i11, str3, str4, f5, j10, str5, z4);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ Object[] newArray(int i8) {
        return new WakeLockEvent[i8];
    }
}
