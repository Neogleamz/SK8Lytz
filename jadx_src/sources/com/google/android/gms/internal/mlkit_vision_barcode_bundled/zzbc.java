package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzbc extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzbc> CREATOR = new r();

    /* renamed from: a  reason: collision with root package name */
    private final int f14970a;

    /* renamed from: b  reason: collision with root package name */
    private final boolean f14971b;

    public zzbc(int i8, boolean z4) {
        this.f14970a = i8;
        this.f14971b = z4;
    }

    public final int t() {
        return this.f14970a;
    }

    public final boolean u() {
        return this.f14971b;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 1, this.f14970a);
        o6.a.c(parcel, 2, this.f14971b);
        o6.a.b(parcel, a9);
    }
}
