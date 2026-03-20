package com.google.android.gms.internal.mlkit_vision_barcode;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzvl extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzvl> CREATOR = new jh();

    /* renamed from: a  reason: collision with root package name */
    private final int f14694a;

    /* renamed from: b  reason: collision with root package name */
    private final boolean f14695b;

    public zzvl(int i8, boolean z4) {
        this.f14694a = i8;
        this.f14695b = z4;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 1, this.f14694a);
        o6.a.c(parcel, 2, this.f14695b);
        o6.a.b(parcel, a9);
    }
}
