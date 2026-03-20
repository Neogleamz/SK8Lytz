package com.google.android.gms.common.moduleinstall;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import n6.j;
import q6.i;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ModuleInstallStatusUpdate extends AbstractSafeParcelable {
    public static final Parcelable.Creator<ModuleInstallStatusUpdate> CREATOR = new i();

    /* renamed from: a  reason: collision with root package name */
    private final int f11898a;

    /* renamed from: b  reason: collision with root package name */
    private final int f11899b;

    /* renamed from: c  reason: collision with root package name */
    private final Long f11900c;

    /* renamed from: d  reason: collision with root package name */
    private final Long f11901d;

    /* renamed from: e  reason: collision with root package name */
    private final int f11902e;

    /* renamed from: f  reason: collision with root package name */
    private final a f11903f;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {

        /* renamed from: a  reason: collision with root package name */
        private final long f11904a;

        /* renamed from: b  reason: collision with root package name */
        private final long f11905b;

        a(long j8, long j9) {
            j.o(j9);
            this.f11904a = j8;
            this.f11905b = j9;
        }
    }

    public ModuleInstallStatusUpdate(int i8, int i9, Long l8, Long l9, int i10) {
        this.f11898a = i8;
        this.f11899b = i9;
        this.f11900c = l8;
        this.f11901d = l9;
        this.f11902e = i10;
        this.f11903f = (l8 == null || l9 == null || l9.longValue() == 0) ? null : new a(l8.longValue(), l9.longValue());
    }

    public int Z() {
        return this.f11898a;
    }

    public int t() {
        return this.f11902e;
    }

    public int u() {
        return this.f11899b;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 1, Z());
        o6.a.l(parcel, 2, u());
        o6.a.o(parcel, 3, this.f11900c, false);
        o6.a.o(parcel, 4, this.f11901d, false);
        o6.a.l(parcel, 5, t());
        o6.a.b(parcel, a9);
    }
}
