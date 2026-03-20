package androidx.camera.camera2.internal;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class k3 implements androidx.camera.core.h3 {

    /* renamed from: a  reason: collision with root package name */
    private float f1941a;

    /* renamed from: b  reason: collision with root package name */
    private final float f1942b;

    /* renamed from: c  reason: collision with root package name */
    private final float f1943c;

    /* renamed from: d  reason: collision with root package name */
    private float f1944d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public k3(float f5, float f8) {
        this.f1942b = f5;
        this.f1943c = f8;
    }

    private float e(float f5) {
        float f8 = this.f1942b;
        float f9 = this.f1943c;
        if (f8 == f9) {
            return 0.0f;
        }
        if (f5 == f8) {
            return 1.0f;
        }
        if (f5 == f9) {
            return 0.0f;
        }
        float f10 = 1.0f / f9;
        return ((1.0f / f5) - f10) / ((1.0f / f8) - f10);
    }

    private float f(float f5) {
        if (f5 == 1.0f) {
            return this.f1942b;
        }
        if (f5 == 0.0f) {
            return this.f1943c;
        }
        float f8 = this.f1942b;
        float f9 = this.f1943c;
        double d8 = 1.0f / f9;
        return (float) t0.a.a(1.0d / (d8 + (((1.0f / f8) - d8) * f5)), f9, f8);
    }

    @Override // androidx.camera.core.h3
    public float a() {
        return this.f1942b;
    }

    @Override // androidx.camera.core.h3
    public float b() {
        return this.f1943c;
    }

    @Override // androidx.camera.core.h3
    public float c() {
        return this.f1941a;
    }

    @Override // androidx.camera.core.h3
    public float d() {
        return this.f1944d;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void g(float f5) {
        if (f5 <= 1.0f && f5 >= 0.0f) {
            this.f1944d = f5;
            this.f1941a = f(f5);
            return;
        }
        throw new IllegalArgumentException("Requested linearZoom " + f5 + " is not within valid range [0..1]");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void h(float f5) {
        if (f5 <= this.f1942b && f5 >= this.f1943c) {
            this.f1941a = f5;
            this.f1944d = e(f5);
            return;
        }
        throw new IllegalArgumentException("Requested zoomRatio " + f5 + " is not within valid range [" + this.f1943c + " , " + this.f1942b + "]");
    }
}
