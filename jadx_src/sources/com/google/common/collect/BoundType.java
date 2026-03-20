package com.google.common.collect;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public enum BoundType {
    OPEN(false),
    CLOSED(true);
    

    /* renamed from: a  reason: collision with root package name */
    final boolean f18888a;

    BoundType(boolean z4) {
        this.f18888a = z4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static BoundType f(boolean z4) {
        return z4 ? CLOSED : OPEN;
    }
}
