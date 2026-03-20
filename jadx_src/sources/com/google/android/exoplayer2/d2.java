package com.google.android.exoplayer2;

import android.os.Bundle;
import com.google.android.exoplayer2.g;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d2 extends b2 {

    /* renamed from: e  reason: collision with root package name */
    private static final String f9506e = b6.l0.r0(1);

    /* renamed from: f  reason: collision with root package name */
    private static final String f9507f = b6.l0.r0(2);

    /* renamed from: g  reason: collision with root package name */
    public static final g.a<d2> f9508g = i4.j0.a;

    /* renamed from: c  reason: collision with root package name */
    private final int f9509c;

    /* renamed from: d  reason: collision with root package name */
    private final float f9510d;

    public d2(int i8) {
        b6.a.b(i8 > 0, "maxStars must be a positive integer");
        this.f9509c = i8;
        this.f9510d = -1.0f;
    }

    public d2(int i8, float f5) {
        boolean z4 = true;
        b6.a.b(i8 > 0, "maxStars must be a positive integer");
        b6.a.b((f5 < 0.0f || f5 > ((float) i8)) ? false : z4, "starRating is out of range [0, maxStars]");
        this.f9509c = i8;
        this.f9510d = f5;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static d2 d(Bundle bundle) {
        b6.a.a(bundle.getInt(b2.f9474a, -1) == 2);
        int i8 = bundle.getInt(f9506e, 5);
        float f5 = bundle.getFloat(f9507f, -1.0f);
        return f5 == -1.0f ? new d2(i8) : new d2(i8, f5);
    }

    public boolean equals(Object obj) {
        if (obj instanceof d2) {
            d2 d2Var = (d2) obj;
            return this.f9509c == d2Var.f9509c && this.f9510d == d2Var.f9510d;
        }
        return false;
    }

    public int hashCode() {
        return com.google.common.base.k.b(Integer.valueOf(this.f9509c), Float.valueOf(this.f9510d));
    }
}
