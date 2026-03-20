package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.Feature;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class x implements Parcelable.Creator {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(GetServiceRequest getServiceRequest, Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 1, getServiceRequest.f11783a);
        o6.a.l(parcel, 2, getServiceRequest.f11784b);
        o6.a.l(parcel, 3, getServiceRequest.f11785c);
        o6.a.r(parcel, 4, getServiceRequest.f11786d, false);
        o6.a.k(parcel, 5, getServiceRequest.f11787e, false);
        o6.a.u(parcel, 6, getServiceRequest.f11788f, i8, false);
        o6.a.e(parcel, 7, getServiceRequest.f11789g, false);
        o6.a.q(parcel, 8, getServiceRequest.f11790h, i8, false);
        o6.a.u(parcel, 10, getServiceRequest.f11791j, i8, false);
        o6.a.u(parcel, 11, getServiceRequest.f11792k, i8, false);
        o6.a.c(parcel, 12, getServiceRequest.f11793l);
        o6.a.l(parcel, 13, getServiceRequest.f11794m);
        o6.a.c(parcel, 14, getServiceRequest.f11795n);
        o6.a.r(parcel, 15, getServiceRequest.t(), false);
        o6.a.b(parcel, a9);
    }

    @Override // android.os.Parcelable.Creator
    public final /* bridge */ /* synthetic */ Object createFromParcel(Parcel parcel) {
        int I = SafeParcelReader.I(parcel);
        Scope[] scopeArr = GetServiceRequest.q;
        Bundle bundle = new Bundle();
        Feature[] featureArr = GetServiceRequest.f11782t;
        Feature[] featureArr2 = featureArr;
        String str = null;
        IBinder iBinder = null;
        Account account = null;
        String str2 = null;
        int i8 = 0;
        int i9 = 0;
        int i10 = 0;
        boolean z4 = false;
        int i11 = 0;
        boolean z8 = false;
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
                    str = SafeParcelReader.o(parcel, B);
                    break;
                case 5:
                    iBinder = SafeParcelReader.C(parcel, B);
                    break;
                case 6:
                    scopeArr = (Scope[]) SafeParcelReader.r(parcel, B, Scope.CREATOR);
                    break;
                case 7:
                    bundle = SafeParcelReader.f(parcel, B);
                    break;
                case 8:
                    account = (Account) SafeParcelReader.n(parcel, B, Account.CREATOR);
                    break;
                case 9:
                default:
                    SafeParcelReader.H(parcel, B);
                    break;
                case 10:
                    featureArr = (Feature[]) SafeParcelReader.r(parcel, B, Feature.CREATOR);
                    break;
                case 11:
                    featureArr2 = (Feature[]) SafeParcelReader.r(parcel, B, Feature.CREATOR);
                    break;
                case 12:
                    z4 = SafeParcelReader.v(parcel, B);
                    break;
                case 13:
                    i11 = SafeParcelReader.D(parcel, B);
                    break;
                case 14:
                    z8 = SafeParcelReader.v(parcel, B);
                    break;
                case 15:
                    str2 = SafeParcelReader.o(parcel, B);
                    break;
            }
        }
        SafeParcelReader.t(parcel, I);
        return new GetServiceRequest(i8, i9, i10, str, iBinder, scopeArr, bundle, account, featureArr, featureArr2, z4, i11, z8, str2);
    }

    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ Object[] newArray(int i8) {
        return new GetServiceRequest[i8];
    }
}
