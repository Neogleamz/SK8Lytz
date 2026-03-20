package com.google.android.gms.common.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ClientIdentity extends AbstractSafeParcelable {
    public static final Parcelable.Creator<ClientIdentity> CREATOR = new n6.p();

    /* renamed from: a  reason: collision with root package name */
    public final int f11772a;

    /* renamed from: b  reason: collision with root package name */
    public final String f11773b;

    public ClientIdentity(int i8, String str) {
        this.f11772a = i8;
        this.f11773b = str;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof ClientIdentity) {
            ClientIdentity clientIdentity = (ClientIdentity) obj;
            return clientIdentity.f11772a == this.f11772a && n6.i.a(clientIdentity.f11773b, this.f11773b);
        }
        return false;
    }

    public final int hashCode() {
        return this.f11772a;
    }

    public final String toString() {
        int i8 = this.f11772a;
        String str = this.f11773b;
        return i8 + ":" + str;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 1, this.f11772a);
        o6.a.r(parcel, 2, this.f11773b, false);
        o6.a.b(parcel, a9);
    }
}
