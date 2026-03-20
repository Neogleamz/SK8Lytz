package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzbu extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzbu> CREATOR = new i0();

    /* renamed from: a  reason: collision with root package name */
    private final int f14972a;

    /* renamed from: b  reason: collision with root package name */
    private final int f14973b;

    /* renamed from: c  reason: collision with root package name */
    private final int f14974c;

    /* renamed from: d  reason: collision with root package name */
    private final int f14975d;

    /* renamed from: e  reason: collision with root package name */
    private final long f14976e;

    public zzbu(int i8, int i9, int i10, int i11, long j8) {
        this.f14972a = i8;
        this.f14973b = i9;
        this.f14974c = i10;
        this.f14975d = i11;
        this.f14976e = j8;
    }

    public final int D0() {
        return this.f14973b;
    }

    public final int Z() {
        return this.f14975d;
    }

    public final int t() {
        return this.f14974c;
    }

    public final int u() {
        return this.f14972a;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 1, this.f14972a);
        o6.a.l(parcel, 2, this.f14973b);
        o6.a.l(parcel, 3, this.f14974c);
        o6.a.l(parcel, 4, this.f14975d);
        o6.a.n(parcel, 5, this.f14976e);
        o6.a.b(parcel, a9);
    }
}
