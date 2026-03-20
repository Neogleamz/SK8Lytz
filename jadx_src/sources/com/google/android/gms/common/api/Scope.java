package com.google.android.gms.common.api;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import n6.j;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class Scope extends AbstractSafeParcelable implements ReflectedParcelable {
    public static final Parcelable.Creator<Scope> CREATOR = new d();

    /* renamed from: a  reason: collision with root package name */
    final int f11544a;

    /* renamed from: b  reason: collision with root package name */
    private final String f11545b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Scope(int i8, String str) {
        j.g(str, "scopeUri must not be null or empty");
        this.f11544a = i8;
        this.f11545b = str;
    }

    public Scope(String str) {
        this(1, str);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Scope) {
            return this.f11545b.equals(((Scope) obj).f11545b);
        }
        return false;
    }

    public int hashCode() {
        return this.f11545b.hashCode();
    }

    public String t() {
        return this.f11545b;
    }

    public String toString() {
        return this.f11545b;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        int i9 = this.f11544a;
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 1, i9);
        o6.a.r(parcel, 2, t(), false);
        o6.a.b(parcel, a9);
    }
}
