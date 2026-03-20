package com.google.android.gms.internal.measurement;

import android.content.Context;
import com.google.common.base.Optional;
import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class y5 extends y6 {

    /* renamed from: a  reason: collision with root package name */
    private final Context f12681a;

    /* renamed from: b  reason: collision with root package name */
    private final com.google.common.base.r<Optional<l6>> f12682b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public y5(Context context, com.google.common.base.r<Optional<l6>> rVar) {
        Objects.requireNonNull(context, "Null context");
        this.f12681a = context;
        this.f12682b = rVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.measurement.y6
    public final Context a() {
        return this.f12681a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.measurement.y6
    public final com.google.common.base.r<Optional<l6>> b() {
        return this.f12682b;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof y6) {
            y6 y6Var = (y6) obj;
            if (this.f12681a.equals(y6Var.a())) {
                com.google.common.base.r<Optional<l6>> rVar = this.f12682b;
                com.google.common.base.r<Optional<l6>> b9 = y6Var.b();
                if (rVar != null ? rVar.equals(b9) : b9 == null) {
                    return true;
                }
            }
        }
        return false;
    }

    public final int hashCode() {
        int hashCode = (this.f12681a.hashCode() ^ 1000003) * 1000003;
        com.google.common.base.r<Optional<l6>> rVar = this.f12682b;
        return hashCode ^ (rVar == null ? 0 : rVar.hashCode());
    }

    public final String toString() {
        String valueOf = String.valueOf(this.f12681a);
        String valueOf2 = String.valueOf(this.f12682b);
        return "FlagsContext{context=" + valueOf + ", hermeticFileOverrides=" + valueOf2 + "}";
    }
}
