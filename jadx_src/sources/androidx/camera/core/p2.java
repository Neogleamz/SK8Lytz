package androidx.camera.core;

import java.util.concurrent.atomic.AtomicBoolean;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class p2 extends h0 {

    /* renamed from: d  reason: collision with root package name */
    private final AtomicBoolean f2774d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public p2(l1 l1Var) {
        super(l1Var);
        this.f2774d = new AtomicBoolean(false);
    }

    @Override // androidx.camera.core.h0, androidx.camera.core.l1, java.lang.AutoCloseable
    public void close() {
        if (this.f2774d.getAndSet(true)) {
            return;
        }
        super.close();
    }
}
