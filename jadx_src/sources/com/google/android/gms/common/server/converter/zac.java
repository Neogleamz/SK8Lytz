package com.google.android.gms.common.server.converter;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zac extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zac> CREATOR = new c();

    /* renamed from: a  reason: collision with root package name */
    final int f11949a;

    /* renamed from: b  reason: collision with root package name */
    final String f11950b;

    /* renamed from: c  reason: collision with root package name */
    final int f11951c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zac(int i8, String str, int i9) {
        this.f11949a = i8;
        this.f11950b = str;
        this.f11951c = i9;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public zac(String str, int i8) {
        this.f11949a = 1;
        this.f11950b = str;
        this.f11951c = i8;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 1, this.f11949a);
        o6.a.r(parcel, 2, this.f11950b, false);
        o6.a.l(parcel, 3, this.f11951c);
        o6.a.b(parcel, a9);
    }
}
