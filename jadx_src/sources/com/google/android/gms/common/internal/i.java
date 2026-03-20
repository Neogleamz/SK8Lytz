package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class i implements Parcelable.Creator {
    @Override // android.os.Parcelable.Creator
    public final /* bridge */ /* synthetic */ Object createFromParcel(Parcel parcel) {
        int I = SafeParcelReader.I(parcel);
        Account account = null;
        int i8 = 0;
        int i9 = 0;
        GoogleSignInAccount googleSignInAccount = null;
        while (parcel.dataPosition() < I) {
            int B = SafeParcelReader.B(parcel);
            int u8 = SafeParcelReader.u(B);
            if (u8 == 1) {
                i8 = SafeParcelReader.D(parcel, B);
            } else if (u8 == 2) {
                account = (Account) SafeParcelReader.n(parcel, B, Account.CREATOR);
            } else if (u8 == 3) {
                i9 = SafeParcelReader.D(parcel, B);
            } else if (u8 != 4) {
                SafeParcelReader.H(parcel, B);
            } else {
                googleSignInAccount = (GoogleSignInAccount) SafeParcelReader.n(parcel, B, GoogleSignInAccount.CREATOR);
            }
        }
        SafeParcelReader.t(parcel, I);
        return new zat(i8, account, i9, googleSignInAccount);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ Object[] newArray(int i8) {
        return new zat[i8];
    }
}
