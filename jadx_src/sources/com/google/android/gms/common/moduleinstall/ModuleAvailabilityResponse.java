package com.google.android.gms.common.moduleinstall;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import o6.a;
import q6.e;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ModuleAvailabilityResponse extends AbstractSafeParcelable {
    public static final Parcelable.Creator<ModuleAvailabilityResponse> CREATOR = new e();

    /* renamed from: a  reason: collision with root package name */
    private final boolean f11893a;

    /* renamed from: b  reason: collision with root package name */
    private final int f11894b;

    public ModuleAvailabilityResponse(boolean z4, int i8) {
        this.f11893a = z4;
        this.f11894b = i8;
    }

    public boolean t() {
        return this.f11893a;
    }

    public int u() {
        return this.f11894b;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        int a9 = a.a(parcel);
        a.c(parcel, 1, t());
        a.l(parcel, 2, u());
        a.b(parcel, a9);
    }
}
