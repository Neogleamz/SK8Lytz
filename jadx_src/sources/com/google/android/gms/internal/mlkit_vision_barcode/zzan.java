package com.google.android.gms.internal.mlkit_vision_barcode;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzan extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzan> CREATOR = new n();

    /* renamed from: a  reason: collision with root package name */
    public int f14304a;

    /* renamed from: b  reason: collision with root package name */
    public int f14305b;

    /* renamed from: c  reason: collision with root package name */
    public int f14306c;

    /* renamed from: d  reason: collision with root package name */
    public long f14307d;

    /* renamed from: e  reason: collision with root package name */
    public int f14308e;

    public zzan() {
    }

    public zzan(int i8, int i9, int i10, long j8, int i11) {
        this.f14304a = i8;
        this.f14305b = i9;
        this.f14306c = i10;
        this.f14307d = j8;
        this.f14308e = i11;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 2, this.f14304a);
        o6.a.l(parcel, 3, this.f14305b);
        o6.a.l(parcel, 4, this.f14306c);
        o6.a.n(parcel, 5, this.f14307d);
        o6.a.l(parcel, 6, this.f14308e);
        o6.a.b(parcel, a9);
    }
}
