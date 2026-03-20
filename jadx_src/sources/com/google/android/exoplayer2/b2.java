package com.google.android.exoplayer2;

import android.os.Bundle;
import com.google.android.exoplayer2.g;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class b2 implements g {

    /* renamed from: a  reason: collision with root package name */
    static final String f9474a = b6.l0.r0(0);

    /* renamed from: b  reason: collision with root package name */
    public static final g.a<b2> f9475b = i4.e0.a;

    /* JADX INFO: Access modifiers changed from: private */
    public static b2 b(Bundle bundle) {
        g.a aVar;
        int i8 = bundle.getInt(f9474a, -1);
        if (i8 == 0) {
            aVar = x0.f11261g;
        } else if (i8 == 1) {
            aVar = u1.f10881e;
        } else if (i8 == 2) {
            aVar = d2.f9508g;
        } else if (i8 != 3) {
            throw new IllegalArgumentException("Unknown RatingType: " + i8);
        } else {
            aVar = g2.f9714g;
        }
        return (b2) aVar.a(bundle);
    }
}
