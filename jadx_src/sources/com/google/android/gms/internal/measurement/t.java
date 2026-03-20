package com.google.android.gms.internal.measurement;

import java.util.Iterator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class t implements r, Iterable<r> {

    /* renamed from: a  reason: collision with root package name */
    private final String f12514a;

    public t(String str) {
        if (str == null) {
            throw new IllegalArgumentException("StringValue cannot be null.");
        }
        this.f12514a = str;
    }

    @Override // com.google.android.gms.internal.measurement.r
    public final r a() {
        return new t(this.f12514a);
    }

    @Override // com.google.android.gms.internal.measurement.r
    public final Boolean b() {
        return Boolean.valueOf(!this.f12514a.isEmpty());
    }

    @Override // com.google.android.gms.internal.measurement.r
    public final Double d() {
        double d8;
        if (this.f12514a.isEmpty()) {
            d8 = 0.0d;
        } else {
            try {
                return Double.valueOf(this.f12514a);
            } catch (NumberFormatException unused) {
                d8 = Double.NaN;
            }
        }
        return Double.valueOf(d8);
    }

    @Override // com.google.android.gms.internal.measurement.r
    public final String e() {
        return this.f12514a;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof t) {
            return this.f12514a.equals(((t) obj).f12514a);
        }
        return false;
    }

    @Override // com.google.android.gms.internal.measurement.r
    public final Iterator<r> f() {
        return new w(this);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:282:0x0671  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00a8  */
    @Override // com.google.android.gms.internal.measurement.r
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final com.google.android.gms.internal.measurement.r g(java.lang.String r22, com.google.android.gms.internal.measurement.g6 r23, java.util.List<com.google.android.gms.internal.measurement.r> r24) {
        /*
            Method dump skipped, instructions count: 1778
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.t.g(java.lang.String, com.google.android.gms.internal.measurement.g6, java.util.List):com.google.android.gms.internal.measurement.r");
    }

    public final int hashCode() {
        return this.f12514a.hashCode();
    }

    @Override // java.lang.Iterable
    public final Iterator<r> iterator() {
        return new v(this);
    }

    public final String toString() {
        String str = this.f12514a;
        return "\"" + str + "\"";
    }
}
