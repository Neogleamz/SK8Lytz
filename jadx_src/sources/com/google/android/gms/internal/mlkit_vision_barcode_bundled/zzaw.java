package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzaw extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzaw> CREATOR = new e0();

    /* renamed from: a  reason: collision with root package name */
    private final int f14947a;

    /* renamed from: b  reason: collision with root package name */
    private final String f14948b;

    public zzaw(int i8, String str) {
        this.f14947a = i8;
        this.f14948b = str;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 1, this.f14947a);
        o6.a.r(parcel, 2, this.f14948b, false);
        o6.a.b(parcel, a9);
    }
}
