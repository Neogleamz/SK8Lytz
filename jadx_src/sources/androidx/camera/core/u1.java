package androidx.camera.core;

import android.graphics.Matrix;
import android.media.ImageReader;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class u1 extends d {

    /* renamed from: d  reason: collision with root package name */
    private volatile y.a1 f2821d;

    /* renamed from: e  reason: collision with root package name */
    private volatile Long f2822e;

    /* renamed from: f  reason: collision with root package name */
    private volatile Integer f2823f;

    /* renamed from: g  reason: collision with root package name */
    private volatile Matrix f2824g;

    /* JADX INFO: Access modifiers changed from: package-private */
    public u1(ImageReader imageReader) {
        super(imageReader);
        this.f2821d = null;
        this.f2822e = null;
        this.f2823f = null;
        this.f2824g = null;
    }

    private l1 k(l1 l1Var) {
        i1 e12 = l1Var.e1();
        return new n2(l1Var, o1.f(this.f2821d != null ? this.f2821d : e12.a(), this.f2822e != null ? this.f2822e.longValue() : e12.d(), this.f2823f != null ? this.f2823f.intValue() : e12.b(), this.f2824g != null ? this.f2824g : e12.e()));
    }

    @Override // androidx.camera.core.d, y.g0
    public l1 acquireLatestImage() {
        return k(super.f());
    }

    @Override // androidx.camera.core.d, y.g0
    public l1 f() {
        return k(super.f());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void l(y.a1 a1Var) {
        this.f2821d = a1Var;
    }
}
