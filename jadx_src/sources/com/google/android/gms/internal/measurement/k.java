package com.google.android.gms.internal.measurement;

import java.util.Iterator;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class k implements r {

    /* renamed from: a  reason: collision with root package name */
    private final r f12272a;

    /* renamed from: b  reason: collision with root package name */
    private final String f12273b;

    public k() {
        this.f12272a = r.f12463r;
        this.f12273b = "return";
    }

    public k(String str) {
        this.f12272a = r.f12463r;
        this.f12273b = str;
    }

    public k(String str, r rVar) {
        this.f12272a = rVar;
        this.f12273b = str;
    }

    @Override // com.google.android.gms.internal.measurement.r
    public final r a() {
        return new k(this.f12273b, this.f12272a.a());
    }

    @Override // com.google.android.gms.internal.measurement.r
    public final Boolean b() {
        throw new IllegalStateException("Control is not a boolean");
    }

    public final r c() {
        return this.f12272a;
    }

    @Override // com.google.android.gms.internal.measurement.r
    public final Double d() {
        throw new IllegalStateException("Control is not a double");
    }

    @Override // com.google.android.gms.internal.measurement.r
    public final String e() {
        throw new IllegalStateException("Control is not a String");
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof k) {
            k kVar = (k) obj;
            return this.f12273b.equals(kVar.f12273b) && this.f12272a.equals(kVar.f12272a);
        }
        return false;
    }

    @Override // com.google.android.gms.internal.measurement.r
    public final Iterator<r> f() {
        return null;
    }

    @Override // com.google.android.gms.internal.measurement.r
    public final r g(String str, g6 g6Var, List<r> list) {
        throw new IllegalStateException("Control does not have functions");
    }

    public final String h() {
        return this.f12273b;
    }

    public final int hashCode() {
        return (this.f12273b.hashCode() * 31) + this.f12272a.hashCode();
    }
}
