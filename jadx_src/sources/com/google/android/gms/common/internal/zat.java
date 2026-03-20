package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zat extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zat> CREATOR = new i();

    /* renamed from: a  reason: collision with root package name */
    final int f11875a;

    /* renamed from: b  reason: collision with root package name */
    private final Account f11876b;

    /* renamed from: c  reason: collision with root package name */
    private final int f11877c;

    /* renamed from: d  reason: collision with root package name */
    private final GoogleSignInAccount f11878d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zat(int i8, Account account, int i9, GoogleSignInAccount googleSignInAccount) {
        this.f11875a = i8;
        this.f11876b = account;
        this.f11877c = i9;
        this.f11878d = googleSignInAccount;
    }

    public zat(Account account, int i8, GoogleSignInAccount googleSignInAccount) {
        this(2, account, i8, googleSignInAccount);
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 1, this.f11875a);
        o6.a.q(parcel, 2, this.f11876b, i8, false);
        o6.a.l(parcel, 3, this.f11877c);
        o6.a.q(parcel, 4, this.f11878d, i8, false);
        o6.a.b(parcel, a9);
    }
}
