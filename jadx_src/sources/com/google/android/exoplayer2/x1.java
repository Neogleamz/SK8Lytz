package com.google.android.exoplayer2;

import android.os.Bundle;
import com.google.android.exoplayer2.g;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class x1 implements g {

    /* renamed from: d  reason: collision with root package name */
    public static final x1 f11264d = new x1(1.0f);

    /* renamed from: e  reason: collision with root package name */
    private static final String f11265e = b6.l0.r0(0);

    /* renamed from: f  reason: collision with root package name */
    private static final String f11266f = b6.l0.r0(1);

    /* renamed from: g  reason: collision with root package name */
    public static final g.a<x1> f11267g = i4.b0.a;

    /* renamed from: a  reason: collision with root package name */
    public final float f11268a;

    /* renamed from: b  reason: collision with root package name */
    public final float f11269b;

    /* renamed from: c  reason: collision with root package name */
    private final int f11270c;

    public x1(float f5) {
        this(f5, 1.0f);
    }

    public x1(float f5, float f8) {
        b6.a.a(f5 > 0.0f);
        b6.a.a(f8 > 0.0f);
        this.f11268a = f5;
        this.f11269b = f8;
        this.f11270c = Math.round(f5 * 1000.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ x1 c(Bundle bundle) {
        return new x1(bundle.getFloat(f11265e, 1.0f), bundle.getFloat(f11266f, 1.0f));
    }

    public long b(long j8) {
        return j8 * this.f11270c;
    }

    public x1 d(float f5) {
        return new x1(f5, this.f11269b);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || x1.class != obj.getClass()) {
            return false;
        }
        x1 x1Var = (x1) obj;
        return this.f11268a == x1Var.f11268a && this.f11269b == x1Var.f11269b;
    }

    public int hashCode() {
        return ((527 + Float.floatToRawIntBits(this.f11268a)) * 31) + Float.floatToRawIntBits(this.f11269b);
    }

    public String toString() {
        return b6.l0.C("PlaybackParameters(speed=%.2f, pitch=%.2f)", Float.valueOf(this.f11268a), Float.valueOf(this.f11269b));
    }
}
