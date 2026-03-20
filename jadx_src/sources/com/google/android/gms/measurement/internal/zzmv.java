package com.google.android.gms.measurement.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzmv extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzmv> CREATOR = new ya();

    /* renamed from: a  reason: collision with root package name */
    public final String f17285a;

    /* renamed from: b  reason: collision with root package name */
    public final long f17286b;

    /* renamed from: c  reason: collision with root package name */
    public final int f17287c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzmv(String str, long j8, int i8) {
        this.f17285a = str;
        this.f17286b = j8;
        this.f17287c = i8;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.r(parcel, 1, this.f17285a, false);
        o6.a.n(parcel, 2, this.f17286b);
        o6.a.l(parcel, 3, this.f17287c);
        o6.a.b(parcel, a9);
    }
}
