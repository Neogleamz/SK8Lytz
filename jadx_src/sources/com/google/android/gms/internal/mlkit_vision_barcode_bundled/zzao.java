package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzao extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzao> CREATOR = new o();

    /* renamed from: a  reason: collision with root package name */
    private final int f14896a;

    /* renamed from: b  reason: collision with root package name */
    private final String[] f14897b;

    public zzao(int i8, String[] strArr) {
        this.f14896a = i8;
        this.f14897b = strArr;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 1, this.f14896a);
        o6.a.s(parcel, 2, this.f14897b, false);
        o6.a.b(parcel, a9);
    }
}
