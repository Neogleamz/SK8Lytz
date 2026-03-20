package com.google.android.gms.signin.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.zav;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zak extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zak> CREATOR = new e();

    /* renamed from: a  reason: collision with root package name */
    final int f17324a;

    /* renamed from: b  reason: collision with root package name */
    private final ConnectionResult f17325b;

    /* renamed from: c  reason: collision with root package name */
    private final zav f17326c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zak(int i8, ConnectionResult connectionResult, zav zavVar) {
        this.f17324a = i8;
        this.f17325b = connectionResult;
        this.f17326c = zavVar;
    }

    public final ConnectionResult t() {
        return this.f17325b;
    }

    public final zav u() {
        return this.f17326c;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 1, this.f17324a);
        o6.a.q(parcel, 2, this.f17325b, i8, false);
        o6.a.q(parcel, 3, this.f17326c, i8, false);
        o6.a.b(parcel, a9);
    }
}
