package com.google.android.material.bottomappbar;

import x7.f;
import x7.o;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a extends f implements Cloneable {

    /* renamed from: a  reason: collision with root package name */
    private float f17519a;

    /* renamed from: b  reason: collision with root package name */
    private float f17520b;

    /* renamed from: c  reason: collision with root package name */
    private float f17521c;

    /* renamed from: d  reason: collision with root package name */
    private float f17522d;

    /* renamed from: e  reason: collision with root package name */
    private float f17523e;

    /* renamed from: f  reason: collision with root package name */
    private float f17524f = -1.0f;

    public a(float f5, float f8, float f9) {
        this.f17520b = f5;
        this.f17519a = f8;
        l(f9);
        this.f17523e = 0.0f;
    }

    @Override // x7.f
    public void c(float f5, float f8, float f9, o oVar) {
        float f10;
        float f11;
        float f12;
        float f13;
        float f14;
        float f15;
        float f16;
        float f17;
        float f18 = this.f17521c;
        if (f18 == 0.0f) {
            oVar.m(f5, 0.0f);
            return;
        }
        float f19 = ((this.f17520b * 2.0f) + f18) / 2.0f;
        float f20 = f9 * this.f17519a;
        float f21 = f8 + this.f17523e;
        float f22 = (this.f17522d * f9) + ((1.0f - f9) * f19);
        if (f22 / f19 >= 1.0f) {
            oVar.m(f5, 0.0f);
            return;
        }
        float f23 = this.f17524f;
        float f24 = f23 * f9;
        boolean z4 = f23 == -1.0f || Math.abs((f23 * 2.0f) - f18) < 0.1f;
        if (z4) {
            f10 = f22;
            f11 = 0.0f;
        } else {
            f11 = 1.75f;
            f10 = 0.0f;
        }
        float f25 = f19 + f20;
        float f26 = f10 + f20;
        float sqrt = (float) Math.sqrt((f25 * f25) - (f26 * f26));
        float f27 = f21 - sqrt;
        float f28 = f21 + sqrt;
        float degrees = (float) Math.toDegrees(Math.atan(sqrt / f26));
        float f29 = (90.0f - degrees) + f11;
        oVar.m(f27, 0.0f);
        float f30 = f20 * 2.0f;
        oVar.a(f27 - f20, 0.0f, f27 + f20, f30, 270.0f, degrees);
        if (z4) {
            f13 = f21 - f19;
            f14 = (-f19) - f10;
            f12 = f21 + f19;
            f15 = f19 - f10;
            f16 = 180.0f - f29;
            f17 = (f29 * 2.0f) - 180.0f;
        } else {
            float f31 = this.f17520b;
            float f32 = f24 * 2.0f;
            float f33 = f21 - f19;
            oVar.a(f33, -(f24 + f31), f33 + f31 + f32, f31 + f24, 180.0f - f29, ((f29 * 2.0f) - 180.0f) / 2.0f);
            f12 = f21 + f19;
            float f34 = this.f17520b;
            oVar.m(f12 - ((f34 / 2.0f) + f24), f34 + f24);
            float f35 = this.f17520b;
            f13 = f12 - (f32 + f35);
            f14 = -(f24 + f35);
            f15 = f35 + f24;
            f16 = 90.0f;
            f17 = f29 - 90.0f;
        }
        oVar.a(f13, f14, f12, f15, f16, f17);
        oVar.a(f28 - f20, 0.0f, f28 + f20, f30, 270.0f - degrees, degrees);
        oVar.m(f5, 0.0f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float d() {
        return this.f17522d;
    }

    public float e() {
        return this.f17524f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float f() {
        return this.f17520b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float g() {
        return this.f17519a;
    }

    public float j() {
        return this.f17521c;
    }

    public float k() {
        return this.f17523e;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void l(float f5) {
        if (f5 < 0.0f) {
            throw new IllegalArgumentException("cradleVerticalOffset must be positive.");
        }
        this.f17522d = f5;
    }

    public void m(float f5) {
        this.f17524f = f5;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void o(float f5) {
        this.f17520b = f5;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void q(float f5) {
        this.f17519a = f5;
    }

    public void r(float f5) {
        this.f17521c = f5;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void s(float f5) {
        this.f17523e = f5;
    }
}
