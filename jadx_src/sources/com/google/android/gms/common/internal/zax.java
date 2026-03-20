package com.google.android.gms.common.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zax extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zax> CREATOR = new k();

    /* renamed from: a  reason: collision with root package name */
    final int f11884a;

    /* renamed from: b  reason: collision with root package name */
    private final int f11885b;

    /* renamed from: c  reason: collision with root package name */
    private final int f11886c;
    @Deprecated

    /* renamed from: d  reason: collision with root package name */
    private final Scope[] f11887d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zax(int i8, int i9, int i10, Scope[] scopeArr) {
        this.f11884a = i8;
        this.f11885b = i9;
        this.f11886c = i10;
        this.f11887d = scopeArr;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 1, this.f11884a);
        o6.a.l(parcel, 2, this.f11885b);
        o6.a.l(parcel, 3, this.f11886c);
        o6.a.u(parcel, 4, this.f11887d, i8, false);
        o6.a.b(parcel, a9);
    }
}
