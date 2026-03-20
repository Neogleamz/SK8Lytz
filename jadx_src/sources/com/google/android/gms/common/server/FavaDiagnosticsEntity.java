package com.google.android.gms.common.server;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import s6.a;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class FavaDiagnosticsEntity extends AbstractSafeParcelable implements ReflectedParcelable {
    public static final Parcelable.Creator<FavaDiagnosticsEntity> CREATOR = new a();

    /* renamed from: a  reason: collision with root package name */
    final int f11941a;

    /* renamed from: b  reason: collision with root package name */
    public final String f11942b;

    /* renamed from: c  reason: collision with root package name */
    public final int f11943c;

    public FavaDiagnosticsEntity(int i8, String str, int i9) {
        this.f11941a = i8;
        this.f11942b = str;
        this.f11943c = i9;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 1, this.f11941a);
        o6.a.r(parcel, 2, this.f11942b, false);
        o6.a.l(parcel, 3, this.f11943c);
        o6.a.b(parcel, a9);
    }
}
