package com.google.android.gms.internal.mlkit_vision_barcode;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzah extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzah> CREATOR = new i();

    /* renamed from: a  reason: collision with root package name */
    public int f14302a;

    /* renamed from: b  reason: collision with root package name */
    public boolean f14303b;

    public zzah() {
    }

    public zzah(int i8, boolean z4) {
        this.f14302a = i8;
        this.f14303b = z4;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof zzah) {
            zzah zzahVar = (zzah) obj;
            return this.f14302a == zzahVar.f14302a && n6.i.a(Boolean.valueOf(this.f14303b), Boolean.valueOf(zzahVar.f14303b));
        }
        return false;
    }

    public final int hashCode() {
        return n6.i.b(Integer.valueOf(this.f14302a), Boolean.valueOf(this.f14303b));
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 2, this.f14302a);
        o6.a.c(parcel, 3, this.f14303b);
        o6.a.b(parcel, a9);
    }
}
