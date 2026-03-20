package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzax extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzax> CREATOR = new f0();

    /* renamed from: a  reason: collision with root package name */
    private final String f14949a;

    /* renamed from: b  reason: collision with root package name */
    private final String f14950b;

    public zzax(String str, String str2) {
        this.f14949a = str;
        this.f14950b = str2;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.r(parcel, 1, this.f14949a, false);
        o6.a.r(parcel, 2, this.f14950b, false);
        o6.a.b(parcel, a9);
    }
}
