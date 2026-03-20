package com.google.android.gms.internal.mlkit_vision_barcode;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzi extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzi> CREATOR = new d4();

    /* renamed from: a  reason: collision with root package name */
    public int f14320a;

    /* renamed from: b  reason: collision with root package name */
    public String[] f14321b;

    public zzi() {
    }

    public zzi(int i8, String[] strArr) {
        this.f14320a = i8;
        this.f14321b = strArr;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 2, this.f14320a);
        o6.a.s(parcel, 3, this.f14321b, false);
        o6.a.b(parcel, a9);
    }
}
