package com.google.android.gms.internal.measurement;

import java.util.Iterator;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class h implements r {

    /* renamed from: a  reason: collision with root package name */
    private final boolean f12222a;

    public h(Boolean bool) {
        this.f12222a = bool == null ? false : bool.booleanValue();
    }

    @Override // com.google.android.gms.internal.measurement.r
    public final r a() {
        return new h(Boolean.valueOf(this.f12222a));
    }

    @Override // com.google.android.gms.internal.measurement.r
    public final Boolean b() {
        return Boolean.valueOf(this.f12222a);
    }

    @Override // com.google.android.gms.internal.measurement.r
    public final Double d() {
        return Double.valueOf(this.f12222a ? 1.0d : 0.0d);
    }

    @Override // com.google.android.gms.internal.measurement.r
    public final String e() {
        return Boolean.toString(this.f12222a);
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof h) && this.f12222a == ((h) obj).f12222a;
    }

    @Override // com.google.android.gms.internal.measurement.r
    public final Iterator<r> f() {
        return null;
    }

    @Override // com.google.android.gms.internal.measurement.r
    public final r g(String str, g6 g6Var, List<r> list) {
        if ("toString".equals(str)) {
            return new t(Boolean.toString(this.f12222a));
        }
        throw new IllegalArgumentException(String.format("%s.%s is not a function.", Boolean.toString(this.f12222a), str));
    }

    public final int hashCode() {
        return Boolean.valueOf(this.f12222a).hashCode();
    }

    public final String toString() {
        return String.valueOf(this.f12222a);
    }
}
