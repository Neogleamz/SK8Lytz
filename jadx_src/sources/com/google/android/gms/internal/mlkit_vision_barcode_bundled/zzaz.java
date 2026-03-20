package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzaz extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzaz> CREATOR = new h0();

    /* renamed from: a  reason: collision with root package name */
    private final String f14953a;

    /* renamed from: b  reason: collision with root package name */
    private final String f14954b;

    /* renamed from: c  reason: collision with root package name */
    private final int f14955c;

    public zzaz(String str, String str2, int i8) {
        this.f14953a = str;
        this.f14954b = str2;
        this.f14955c = i8;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.r(parcel, 1, this.f14953a, false);
        o6.a.r(parcel, 2, this.f14954b, false);
        o6.a.l(parcel, 3, this.f14955c);
        o6.a.b(parcel, a9);
    }
}
