package androidx.camera.camera2.internal;

import androidx.camera.core.impl.f;
import r.a;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class d2 extends l0 {

    /* renamed from: c  reason: collision with root package name */
    static final d2 f1777c = new d2(new v.i());

    /* renamed from: b  reason: collision with root package name */
    private final v.i f1778b;

    private d2(v.i iVar) {
        this.f1778b = iVar;
    }

    @Override // androidx.camera.camera2.internal.l0, androidx.camera.core.impl.f.b
    public void a(androidx.camera.core.impl.v<?> vVar, f.a aVar) {
        super.a(vVar, aVar);
        if (!(vVar instanceof androidx.camera.core.impl.j)) {
            throw new IllegalArgumentException("config is not ImageCaptureConfig");
        }
        androidx.camera.core.impl.j jVar = (androidx.camera.core.impl.j) vVar;
        a.C0201a c0201a = new a.C0201a();
        if (jVar.U()) {
            this.f1778b.a(jVar.M(), c0201a);
        }
        aVar.e(c0201a.c());
    }
}
