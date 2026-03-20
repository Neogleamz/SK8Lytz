package com.google.android.gms.internal.mlkit_vision_barcode;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzvg extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzvg> CREATOR = new wh();

    /* renamed from: a  reason: collision with root package name */
    private final String f14673a;

    /* renamed from: b  reason: collision with root package name */
    private final String f14674b;

    public zzvg(String str, String str2) {
        this.f14673a = str;
        this.f14674b = str2;
    }

    public final String t() {
        return this.f14673a;
    }

    public final String u() {
        return this.f14674b;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.r(parcel, 1, this.f14673a, false);
        o6.a.r(parcel, 2, this.f14674b, false);
        o6.a.b(parcel, a9);
    }
}
