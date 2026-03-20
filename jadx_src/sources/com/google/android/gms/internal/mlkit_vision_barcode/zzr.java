package com.google.android.gms.internal.mlkit_vision_barcode;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzr extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzr> CREATOR = new f();

    /* renamed from: a  reason: collision with root package name */
    public String f14580a;

    /* renamed from: b  reason: collision with root package name */
    public String f14581b;

    public zzr() {
    }

    public zzr(String str, String str2) {
        this.f14580a = str;
        this.f14581b = str2;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.r(parcel, 2, this.f14580a, false);
        o6.a.r(parcel, 3, this.f14581b, false);
        o6.a.b(parcel, a9);
    }
}
