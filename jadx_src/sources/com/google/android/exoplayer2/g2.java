package com.google.android.exoplayer2;

import android.os.Bundle;
import com.google.android.exoplayer2.g;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class g2 extends b2 {

    /* renamed from: e  reason: collision with root package name */
    private static final String f9712e = b6.l0.r0(1);

    /* renamed from: f  reason: collision with root package name */
    private static final String f9713f = b6.l0.r0(2);

    /* renamed from: g  reason: collision with root package name */
    public static final g.a<g2> f9714g = i4.k0.a;

    /* renamed from: c  reason: collision with root package name */
    private final boolean f9715c;

    /* renamed from: d  reason: collision with root package name */
    private final boolean f9716d;

    public g2() {
        this.f9715c = false;
        this.f9716d = false;
    }

    public g2(boolean z4) {
        this.f9715c = true;
        this.f9716d = z4;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static g2 d(Bundle bundle) {
        b6.a.a(bundle.getInt(b2.f9474a, -1) == 3);
        return bundle.getBoolean(f9712e, false) ? new g2(bundle.getBoolean(f9713f, false)) : new g2();
    }

    public boolean equals(Object obj) {
        if (obj instanceof g2) {
            g2 g2Var = (g2) obj;
            return this.f9716d == g2Var.f9716d && this.f9715c == g2Var.f9715c;
        }
        return false;
    }

    public int hashCode() {
        return com.google.common.base.k.b(Boolean.valueOf(this.f9715c), Boolean.valueOf(this.f9716d));
    }
}
