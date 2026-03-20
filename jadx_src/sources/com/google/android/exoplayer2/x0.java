package com.google.android.exoplayer2;

import android.os.Bundle;
import com.google.android.exoplayer2.g;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class x0 extends b2 {

    /* renamed from: e  reason: collision with root package name */
    private static final String f11259e = b6.l0.r0(1);

    /* renamed from: f  reason: collision with root package name */
    private static final String f11260f = b6.l0.r0(2);

    /* renamed from: g  reason: collision with root package name */
    public static final g.a<x0> f11261g = i4.t.a;

    /* renamed from: c  reason: collision with root package name */
    private final boolean f11262c;

    /* renamed from: d  reason: collision with root package name */
    private final boolean f11263d;

    public x0() {
        this.f11262c = false;
        this.f11263d = false;
    }

    public x0(boolean z4) {
        this.f11262c = true;
        this.f11263d = z4;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static x0 d(Bundle bundle) {
        b6.a.a(bundle.getInt(b2.f9474a, -1) == 0);
        return bundle.getBoolean(f11259e, false) ? new x0(bundle.getBoolean(f11260f, false)) : new x0();
    }

    public boolean equals(Object obj) {
        if (obj instanceof x0) {
            x0 x0Var = (x0) obj;
            return this.f11263d == x0Var.f11263d && this.f11262c == x0Var.f11262c;
        }
        return false;
    }

    public int hashCode() {
        return com.google.common.base.k.b(Boolean.valueOf(this.f11262c), Boolean.valueOf(this.f11263d));
    }
}
