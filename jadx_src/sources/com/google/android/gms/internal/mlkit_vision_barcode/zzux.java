package com.google.android.gms.internal.mlkit_vision_barcode;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzux extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzux> CREATOR = new gh();

    /* renamed from: a  reason: collision with root package name */
    private final int f14620a;

    /* renamed from: b  reason: collision with root package name */
    private final String[] f14621b;

    public zzux(int i8, String[] strArr) {
        this.f14620a = i8;
        this.f14621b = strArr;
    }

    public final int t() {
        return this.f14620a;
    }

    public final String[] u() {
        return this.f14621b;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 1, this.f14620a);
        o6.a.s(parcel, 2, this.f14621b, false);
        o6.a.b(parcel, a9);
    }
}
