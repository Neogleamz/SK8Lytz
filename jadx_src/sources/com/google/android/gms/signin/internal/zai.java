package com.google.android.gms.signin.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.zat;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zai extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zai> CREATOR = new d();

    /* renamed from: a  reason: collision with root package name */
    final int f17322a;

    /* renamed from: b  reason: collision with root package name */
    final zat f17323b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zai(int i8, zat zatVar) {
        this.f17322a = i8;
        this.f17323b = zatVar;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 1, this.f17322a);
        o6.a.q(parcel, 2, this.f17323b, i8, false);
        o6.a.b(parcel, a9);
    }
}
