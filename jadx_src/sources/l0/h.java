package l0;

import androidx.constraintlayout.motion.widget.r;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class h {

    /* renamed from: a  reason: collision with root package name */
    float f21554a;

    /* renamed from: b  reason: collision with root package name */
    float f21555b;

    /* renamed from: c  reason: collision with root package name */
    float f21556c;

    /* renamed from: d  reason: collision with root package name */
    float f21557d;

    /* renamed from: e  reason: collision with root package name */
    float f21558e;

    /* renamed from: f  reason: collision with root package name */
    float f21559f;

    public void a(float f5, float f8, int i8, int i9, float[] fArr) {
        float f9;
        float f10 = fArr[0];
        float f11 = fArr[1];
        float f12 = (f8 - 0.5f) * 2.0f;
        float f13 = f10 + this.f21556c;
        float f14 = f11 + this.f21557d;
        float f15 = f13 + (this.f21554a * (f5 - 0.5f) * 2.0f);
        float f16 = f14 + (this.f21555b * f12);
        float radians = (float) Math.toRadians(this.f21558e);
        double radians2 = (float) Math.toRadians(this.f21559f);
        double d8 = i9 * f12;
        fArr[0] = f15 + (((float) ((((-i8) * f9) * Math.sin(radians2)) - (Math.cos(radians2) * d8))) * radians);
        fArr[1] = f16 + (radians * ((float) (((i8 * f9) * Math.cos(radians2)) - (d8 * Math.sin(radians2)))));
    }

    public void b() {
        this.f21558e = 0.0f;
        this.f21557d = 0.0f;
        this.f21556c = 0.0f;
        this.f21555b = 0.0f;
        this.f21554a = 0.0f;
    }

    public void c(androidx.constraintlayout.motion.widget.g gVar, float f5) {
        if (gVar != null) {
            this.f21558e = gVar.b(f5);
        }
    }

    public void d(r rVar, float f5) {
        if (rVar != null) {
            this.f21558e = rVar.b(f5);
            this.f21559f = rVar.a(f5);
        }
    }

    public void e(androidx.constraintlayout.motion.widget.g gVar, androidx.constraintlayout.motion.widget.g gVar2, float f5) {
        if (gVar == null && gVar2 == null) {
            return;
        }
        if (gVar == null) {
            this.f21554a = gVar.b(f5);
        }
        if (gVar2 == null) {
            this.f21555b = gVar2.b(f5);
        }
    }

    public void f(r rVar, r rVar2, float f5) {
        if (rVar != null) {
            this.f21554a = rVar.b(f5);
        }
        if (rVar2 != null) {
            this.f21555b = rVar2.b(f5);
        }
    }

    public void g(androidx.constraintlayout.motion.widget.g gVar, androidx.constraintlayout.motion.widget.g gVar2, float f5) {
        if (gVar != null) {
            this.f21556c = gVar.b(f5);
        }
        if (gVar2 != null) {
            this.f21557d = gVar2.b(f5);
        }
    }

    public void h(r rVar, r rVar2, float f5) {
        if (rVar != null) {
            this.f21556c = rVar.b(f5);
        }
        if (rVar2 != null) {
            this.f21557d = rVar2.b(f5);
        }
    }
}
