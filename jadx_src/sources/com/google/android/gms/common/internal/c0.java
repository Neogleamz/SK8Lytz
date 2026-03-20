package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.os.IBinder;
import android.os.Parcel;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c0 extends com.google.android.gms.internal.common.a implements e {
    /* JADX INFO: Access modifiers changed from: package-private */
    public c0(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.common.internal.IAccountAccessor");
    }

    @Override // com.google.android.gms.common.internal.e
    public final Account zzb() {
        Parcel d8 = d(2, e());
        Account account = (Account) com.google.android.gms.internal.common.h.a(d8, Account.CREATOR);
        d8.recycle();
        return account;
    }
}
