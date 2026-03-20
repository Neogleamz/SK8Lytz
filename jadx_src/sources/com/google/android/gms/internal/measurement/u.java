package com.google.android.gms.internal.measurement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class u implements r {

    /* renamed from: a  reason: collision with root package name */
    private final String f12542a;

    /* renamed from: b  reason: collision with root package name */
    private final ArrayList<r> f12543b;

    public u(String str, List<r> list) {
        this.f12542a = str;
        ArrayList<r> arrayList = new ArrayList<>();
        this.f12543b = arrayList;
        arrayList.addAll(list);
    }

    @Override // com.google.android.gms.internal.measurement.r
    public final r a() {
        return this;
    }

    @Override // com.google.android.gms.internal.measurement.r
    public final Boolean b() {
        throw new IllegalStateException("Statement cannot be cast as Boolean");
    }

    public final String c() {
        return this.f12542a;
    }

    @Override // com.google.android.gms.internal.measurement.r
    public final Double d() {
        throw new IllegalStateException("Statement cannot be cast as Double");
    }

    @Override // com.google.android.gms.internal.measurement.r
    public final String e() {
        throw new IllegalStateException("Statement cannot be cast as String");
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof u) {
            u uVar = (u) obj;
            String str = this.f12542a;
            if (str == null ? uVar.f12542a == null : str.equals(uVar.f12542a)) {
                ArrayList<r> arrayList = this.f12543b;
                ArrayList<r> arrayList2 = uVar.f12543b;
                return arrayList != null ? arrayList.equals(arrayList2) : arrayList2 == null;
            }
            return false;
        }
        return false;
    }

    @Override // com.google.android.gms.internal.measurement.r
    public final Iterator<r> f() {
        return null;
    }

    @Override // com.google.android.gms.internal.measurement.r
    public final r g(String str, g6 g6Var, List<r> list) {
        throw new IllegalStateException("Statement is not an evaluated entity");
    }

    public final ArrayList<r> h() {
        return this.f12543b;
    }

    public final int hashCode() {
        String str = this.f12542a;
        int hashCode = (str != null ? str.hashCode() : 0) * 31;
        ArrayList<r> arrayList = this.f12543b;
        return hashCode + (arrayList != null ? arrayList.hashCode() : 0);
    }
}
