package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.internal.e;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zav extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zav> CREATOR = new j();

    /* renamed from: a  reason: collision with root package name */
    final int f11879a;

    /* renamed from: b  reason: collision with root package name */
    final IBinder f11880b;

    /* renamed from: c  reason: collision with root package name */
    private final ConnectionResult f11881c;

    /* renamed from: d  reason: collision with root package name */
    private final boolean f11882d;

    /* renamed from: e  reason: collision with root package name */
    private final boolean f11883e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zav(int i8, IBinder iBinder, ConnectionResult connectionResult, boolean z4, boolean z8) {
        this.f11879a = i8;
        this.f11880b = iBinder;
        this.f11881c = connectionResult;
        this.f11882d = z4;
        this.f11883e = z8;
    }

    public final boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof zav) {
            zav zavVar = (zav) obj;
            return this.f11881c.equals(zavVar.f11881c) && n6.i.a(u(), zavVar.u());
        }
        return false;
    }

    public final ConnectionResult t() {
        return this.f11881c;
    }

    public final e u() {
        IBinder iBinder = this.f11880b;
        if (iBinder == null) {
            return null;
        }
        return e.a.e(iBinder);
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 1, this.f11879a);
        o6.a.k(parcel, 2, this.f11880b, false);
        o6.a.q(parcel, 3, this.f11881c, i8, false);
        o6.a.c(parcel, 4, this.f11882d);
        o6.a.c(parcel, 5, this.f11883e);
        o6.a.b(parcel, a9);
    }
}
