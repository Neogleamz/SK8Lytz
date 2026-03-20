package com.google.android.gms.internal.mlkit_vision_barcode;

import java.util.Map;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
abstract class k0 implements p1 {

    /* renamed from: a  reason: collision with root package name */
    private transient Set f13615a;

    /* renamed from: b  reason: collision with root package name */
    private transient Map f13616b;

    abstract Map b();

    abstract Set c();

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof p1) {
            return o().equals(((p1) obj).o());
        }
        return false;
    }

    public final int hashCode() {
        return o().hashCode();
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.p1
    public final Set k() {
        Set set = this.f13615a;
        if (set == null) {
            Set c9 = c();
            this.f13615a = c9;
            return c9;
        }
        return set;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.p1
    public final Map o() {
        Map map = this.f13616b;
        if (map == null) {
            Map b9 = b();
            this.f13616b = b9;
            return b9;
        }
        return map;
    }

    public final String toString() {
        return o().toString();
    }
}
