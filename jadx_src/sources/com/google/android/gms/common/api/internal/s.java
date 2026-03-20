package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.Feature;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class s {

    /* renamed from: a  reason: collision with root package name */
    private final l6.b f11693a;

    /* renamed from: b  reason: collision with root package name */
    private final Feature f11694b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ s(l6.b bVar, Feature feature, l6.n nVar) {
        this.f11693a = bVar;
        this.f11694b = feature;
    }

    public final boolean equals(Object obj) {
        if (obj != null && (obj instanceof s)) {
            s sVar = (s) obj;
            if (n6.i.a(this.f11693a, sVar.f11693a) && n6.i.a(this.f11694b, sVar.f11694b)) {
                return true;
            }
        }
        return false;
    }

    public final int hashCode() {
        return n6.i.b(this.f11693a, this.f11694b);
    }

    public final String toString() {
        return n6.i.c(this).a("key", this.f11693a).a("feature", this.f11694b).toString();
    }
}
