package com.google.android.gms.common;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzq extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzq> CREATOR = new t();

    /* renamed from: a  reason: collision with root package name */
    private final boolean f12005a;

    /* renamed from: b  reason: collision with root package name */
    private final String f12006b;

    /* renamed from: c  reason: collision with root package name */
    private final int f12007c;

    /* renamed from: d  reason: collision with root package name */
    private final int f12008d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzq(boolean z4, String str, int i8, int i9) {
        this.f12005a = z4;
        this.f12006b = str;
        this.f12007c = x.a(i8) - 1;
        this.f12008d = h.a(i9) - 1;
    }

    public final int D0() {
        return x.a(this.f12007c);
    }

    public final int Z() {
        return h.a(this.f12008d);
    }

    public final String t() {
        return this.f12006b;
    }

    public final boolean u() {
        return this.f12005a;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.c(parcel, 1, this.f12005a);
        o6.a.r(parcel, 2, this.f12006b, false);
        o6.a.l(parcel, 3, this.f12007c);
        o6.a.l(parcel, 4, this.f12008d);
        o6.a.b(parcel, a9);
    }
}
