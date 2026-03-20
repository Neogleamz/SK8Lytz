package com.google.android.gms.measurement.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
import java.util.ArrayList;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class tb implements Parcelable.Creator<zzn> {
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzn createFromParcel(Parcel parcel) {
        int I = SafeParcelReader.I(parcel);
        boolean z4 = true;
        boolean z8 = true;
        String str = BuildConfig.FLAVOR;
        String str2 = str;
        String str3 = str2;
        String str4 = str3;
        boolean z9 = false;
        int i8 = 0;
        boolean z10 = false;
        boolean z11 = false;
        int i9 = 0;
        long j8 = 0;
        long j9 = 0;
        long j10 = 0;
        long j11 = 0;
        long j12 = 0;
        long j13 = 0;
        long j14 = 0;
        String str5 = null;
        String str6 = null;
        String str7 = null;
        String str8 = null;
        String str9 = null;
        String str10 = null;
        String str11 = null;
        Boolean bool = null;
        ArrayList<String> arrayList = null;
        String str12 = null;
        String str13 = null;
        String str14 = null;
        long j15 = -2147483648L;
        int i10 = 100;
        while (parcel.dataPosition() < I) {
            int B = SafeParcelReader.B(parcel);
            switch (SafeParcelReader.u(B)) {
                case 2:
                    str5 = SafeParcelReader.o(parcel, B);
                    break;
                case 3:
                    str6 = SafeParcelReader.o(parcel, B);
                    break;
                case 4:
                    str7 = SafeParcelReader.o(parcel, B);
                    break;
                case 5:
                    str8 = SafeParcelReader.o(parcel, B);
                    break;
                case 6:
                    j8 = SafeParcelReader.E(parcel, B);
                    break;
                case 7:
                    j9 = SafeParcelReader.E(parcel, B);
                    break;
                case 8:
                    str9 = SafeParcelReader.o(parcel, B);
                    break;
                case 9:
                    z4 = SafeParcelReader.v(parcel, B);
                    break;
                case 10:
                    z9 = SafeParcelReader.v(parcel, B);
                    break;
                case 11:
                    j15 = SafeParcelReader.E(parcel, B);
                    break;
                case 12:
                    str10 = SafeParcelReader.o(parcel, B);
                    break;
                case 13:
                    j10 = SafeParcelReader.E(parcel, B);
                    break;
                case 14:
                    j11 = SafeParcelReader.E(parcel, B);
                    break;
                case 15:
                    i8 = SafeParcelReader.D(parcel, B);
                    break;
                case 16:
                    z8 = SafeParcelReader.v(parcel, B);
                    break;
                case 17:
                case 20:
                case 33:
                default:
                    SafeParcelReader.H(parcel, B);
                    break;
                case 18:
                    z10 = SafeParcelReader.v(parcel, B);
                    break;
                case 19:
                    str11 = SafeParcelReader.o(parcel, B);
                    break;
                case 21:
                    bool = SafeParcelReader.w(parcel, B);
                    break;
                case 22:
                    j12 = SafeParcelReader.E(parcel, B);
                    break;
                case 23:
                    arrayList = SafeParcelReader.q(parcel, B);
                    break;
                case 24:
                    str12 = SafeParcelReader.o(parcel, B);
                    break;
                case 25:
                    str = SafeParcelReader.o(parcel, B);
                    break;
                case 26:
                    str2 = SafeParcelReader.o(parcel, B);
                    break;
                case 27:
                    str13 = SafeParcelReader.o(parcel, B);
                    break;
                case 28:
                    z11 = SafeParcelReader.v(parcel, B);
                    break;
                case 29:
                    j13 = SafeParcelReader.E(parcel, B);
                    break;
                case 30:
                    i10 = SafeParcelReader.D(parcel, B);
                    break;
                case 31:
                    str3 = SafeParcelReader.o(parcel, B);
                    break;
                case 32:
                    i9 = SafeParcelReader.D(parcel, B);
                    break;
                case 34:
                    j14 = SafeParcelReader.E(parcel, B);
                    break;
                case 35:
                    str14 = SafeParcelReader.o(parcel, B);
                    break;
                case 36:
                    str4 = SafeParcelReader.o(parcel, B);
                    break;
            }
        }
        SafeParcelReader.t(parcel, I);
        return new zzn(str5, str6, str7, str8, j8, j9, str9, z4, z9, j15, str10, j10, j11, i8, z8, z10, str11, bool, j12, arrayList, str12, str, str2, str13, z11, j13, i10, str3, i9, j14, str14, str4);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ zzn[] newArray(int i8) {
        return new zzn[i8];
    }
}
