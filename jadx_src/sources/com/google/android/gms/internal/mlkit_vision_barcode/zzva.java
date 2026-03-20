package com.google.android.gms.internal.mlkit_vision_barcode;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzva extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzva> CREATOR = new mh();

    /* renamed from: a  reason: collision with root package name */
    private final zzve f14637a;

    /* renamed from: b  reason: collision with root package name */
    private final String f14638b;

    /* renamed from: c  reason: collision with root package name */
    private final String f14639c;

    /* renamed from: d  reason: collision with root package name */
    private final zzvf[] f14640d;

    /* renamed from: e  reason: collision with root package name */
    private final zzvc[] f14641e;

    /* renamed from: f  reason: collision with root package name */
    private final String[] f14642f;

    /* renamed from: g  reason: collision with root package name */
    private final zzux[] f14643g;

    public zzva(zzve zzveVar, String str, String str2, zzvf[] zzvfVarArr, zzvc[] zzvcVarArr, String[] strArr, zzux[] zzuxVarArr) {
        this.f14637a = zzveVar;
        this.f14638b = str;
        this.f14639c = str2;
        this.f14640d = zzvfVarArr;
        this.f14641e = zzvcVarArr;
        this.f14642f = strArr;
        this.f14643g = zzuxVarArr;
    }

    public final zzux[] D0() {
        return this.f14643g;
    }

    public final zzvc[] E0() {
        return this.f14641e;
    }

    public final zzvf[] I0() {
        return this.f14640d;
    }

    public final String[] T0() {
        return this.f14642f;
    }

    public final String Z() {
        return this.f14639c;
    }

    public final zzve t() {
        return this.f14637a;
    }

    public final String u() {
        return this.f14638b;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.q(parcel, 1, this.f14637a, i8, false);
        o6.a.r(parcel, 2, this.f14638b, false);
        o6.a.r(parcel, 3, this.f14639c, false);
        o6.a.u(parcel, 4, this.f14640d, i8, false);
        o6.a.u(parcel, 5, this.f14641e, i8, false);
        o6.a.s(parcel, 6, this.f14642f, false);
        o6.a.u(parcel, 7, this.f14643g, i8, false);
        o6.a.b(parcel, a9);
    }
}
