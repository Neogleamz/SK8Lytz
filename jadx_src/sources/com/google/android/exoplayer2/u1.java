package com.google.android.exoplayer2;

import android.os.Bundle;
import com.google.android.exoplayer2.g;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class u1 extends b2 {

    /* renamed from: d  reason: collision with root package name */
    private static final String f10880d = b6.l0.r0(1);

    /* renamed from: e  reason: collision with root package name */
    public static final g.a<u1> f10881e = i4.a0.a;

    /* renamed from: c  reason: collision with root package name */
    private final float f10882c;

    public u1() {
        this.f10882c = -1.0f;
    }

    public u1(float f5) {
        b6.a.b(f5 >= 0.0f && f5 <= 100.0f, "percent must be in the range of [0, 100]");
        this.f10882c = f5;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static u1 d(Bundle bundle) {
        b6.a.a(bundle.getInt(b2.f9474a, -1) == 1);
        float f5 = bundle.getFloat(f10880d, -1.0f);
        return f5 == -1.0f ? new u1() : new u1(f5);
    }

    public boolean equals(Object obj) {
        return (obj instanceof u1) && this.f10882c == ((u1) obj).f10882c;
    }

    public int hashCode() {
        return com.google.common.base.k.b(Float.valueOf(this.f10882c));
    }
}
