package com.google.android.gms.common.moduleinstall;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import o6.a;
import q6.h;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ModuleInstallResponse extends AbstractSafeParcelable {
    public static final Parcelable.Creator<ModuleInstallResponse> CREATOR = new h();

    /* renamed from: a  reason: collision with root package name */
    private final int f11896a;

    /* renamed from: b  reason: collision with root package name */
    private final boolean f11897b;

    public ModuleInstallResponse(int i8) {
        this(i8, false);
    }

    public ModuleInstallResponse(int i8, boolean z4) {
        this.f11896a = i8;
        this.f11897b = z4;
    }

    public int t() {
        return this.f11896a;
    }

    public final boolean u() {
        return this.f11897b;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        int a9 = a.a(parcel);
        a.l(parcel, 1, t());
        a.c(parcel, 2, this.f11897b);
        a.b(parcel, a9);
    }
}
