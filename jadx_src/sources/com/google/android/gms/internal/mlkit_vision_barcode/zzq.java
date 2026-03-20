package com.google.android.gms.internal.mlkit_vision_barcode;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzq extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzq> CREATOR = new e();

    /* renamed from: a  reason: collision with root package name */
    public int f14578a;

    /* renamed from: b  reason: collision with root package name */
    public String f14579b;

    public zzq() {
    }

    public zzq(int i8, String str) {
        this.f14578a = i8;
        this.f14579b = str;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 2, this.f14578a);
        o6.a.r(parcel, 3, this.f14579b, false);
        o6.a.b(parcel, a9);
    }
}
