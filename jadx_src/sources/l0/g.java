package l0;

import androidx.constraintlayout.motion.widget.o;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class g extends o {

    /* renamed from: a  reason: collision with root package name */
    private float f21540a;

    /* renamed from: b  reason: collision with root package name */
    private float f21541b;

    /* renamed from: c  reason: collision with root package name */
    private float f21542c;

    /* renamed from: d  reason: collision with root package name */
    private float f21543d;

    /* renamed from: e  reason: collision with root package name */
    private float f21544e;

    /* renamed from: f  reason: collision with root package name */
    private float f21545f;

    /* renamed from: g  reason: collision with root package name */
    private float f21546g;

    /* renamed from: h  reason: collision with root package name */
    private float f21547h;

    /* renamed from: i  reason: collision with root package name */
    private float f21548i;

    /* renamed from: j  reason: collision with root package name */
    private int f21549j;

    /* renamed from: k  reason: collision with root package name */
    private String f21550k;

    /* renamed from: l  reason: collision with root package name */
    private boolean f21551l = false;

    /* renamed from: m  reason: collision with root package name */
    private float f21552m;

    /* renamed from: n  reason: collision with root package name */
    private float f21553n;

    private float b(float f5) {
        float f8 = this.f21543d;
        if (f5 <= f8) {
            float f9 = this.f21540a;
            return (f9 * f5) + ((((this.f21541b - f9) * f5) * f5) / (f8 * 2.0f));
        }
        int i8 = this.f21549j;
        if (i8 == 1) {
            return this.f21546g;
        }
        float f10 = f5 - f8;
        float f11 = this.f21544e;
        if (f10 < f11) {
            float f12 = this.f21546g;
            float f13 = this.f21541b;
            return f12 + (f13 * f10) + ((((this.f21542c - f13) * f10) * f10) / (f11 * 2.0f));
        } else if (i8 == 2) {
            return this.f21547h;
        } else {
            float f14 = f10 - f11;
            float f15 = this.f21545f;
            if (f14 < f15) {
                float f16 = this.f21547h;
                float f17 = this.f21542c;
                return (f16 + (f17 * f14)) - (((f17 * f14) * f14) / (f15 * 2.0f));
            }
            return this.f21548i;
        }
    }

    private void e(float f5, float f8, float f9, float f10, float f11) {
        if (f5 == 0.0f) {
            f5 = 1.0E-4f;
        }
        this.f21540a = f5;
        float f12 = f5 / f9;
        float f13 = (f12 * f5) / 2.0f;
        if (f5 < 0.0f) {
            float sqrt = (float) Math.sqrt((f8 - ((((-f5) / f9) * f5) / 2.0f)) * f9);
            if (sqrt < f10) {
                this.f21550k = "backward accelerate, decelerate";
                this.f21549j = 2;
                this.f21540a = f5;
                this.f21541b = sqrt;
                this.f21542c = 0.0f;
                float f14 = (sqrt - f5) / f9;
                this.f21543d = f14;
                this.f21544e = sqrt / f9;
                this.f21546g = ((f5 + sqrt) * f14) / 2.0f;
                this.f21547h = f8;
                this.f21548i = f8;
                return;
            }
            this.f21550k = "backward accelerate cruse decelerate";
            this.f21549j = 3;
            this.f21540a = f5;
            this.f21541b = f10;
            this.f21542c = f10;
            float f15 = (f10 - f5) / f9;
            this.f21543d = f15;
            float f16 = f10 / f9;
            this.f21545f = f16;
            float f17 = ((f5 + f10) * f15) / 2.0f;
            float f18 = (f16 * f10) / 2.0f;
            this.f21544e = ((f8 - f17) - f18) / f10;
            this.f21546g = f17;
            this.f21547h = f8 - f18;
            this.f21548i = f8;
        } else if (f13 >= f8) {
            this.f21550k = "hard stop";
            this.f21549j = 1;
            this.f21540a = f5;
            this.f21541b = 0.0f;
            this.f21546g = f8;
            this.f21543d = (2.0f * f8) / f5;
        } else {
            float f19 = f8 - f13;
            float f20 = f19 / f5;
            if (f20 + f12 < f11) {
                this.f21550k = "cruse decelerate";
                this.f21549j = 2;
                this.f21540a = f5;
                this.f21541b = f5;
                this.f21542c = 0.0f;
                this.f21546g = f19;
                this.f21547h = f8;
                this.f21543d = f20;
                this.f21544e = f12;
                return;
            }
            float sqrt2 = (float) Math.sqrt((f9 * f8) + ((f5 * f5) / 2.0f));
            float f21 = (sqrt2 - f5) / f9;
            this.f21543d = f21;
            float f22 = sqrt2 / f9;
            this.f21544e = f22;
            if (sqrt2 < f10) {
                this.f21550k = "accelerate decelerate";
                this.f21549j = 2;
                this.f21540a = f5;
                this.f21541b = sqrt2;
                this.f21542c = 0.0f;
                this.f21543d = f21;
                this.f21544e = f22;
                this.f21546g = ((f5 + sqrt2) * f21) / 2.0f;
                this.f21547h = f8;
                return;
            }
            this.f21550k = "accelerate cruse decelerate";
            this.f21549j = 3;
            this.f21540a = f5;
            this.f21541b = f10;
            this.f21542c = f10;
            float f23 = (f10 - f5) / f9;
            this.f21543d = f23;
            float f24 = f10 / f9;
            this.f21545f = f24;
            float f25 = ((f5 + f10) * f23) / 2.0f;
            float f26 = (f24 * f10) / 2.0f;
            this.f21544e = ((f8 - f25) - f26) / f10;
            this.f21546g = f25;
            this.f21547h = f8 - f26;
            this.f21548i = f8;
        }
    }

    @Override // androidx.constraintlayout.motion.widget.o
    public float a() {
        return this.f21551l ? -d(this.f21553n) : d(this.f21553n);
    }

    public void c(float f5, float f8, float f9, float f10, float f11, float f12) {
        float f13;
        g gVar;
        float f14;
        this.f21552m = f5;
        boolean z4 = f5 > f8;
        this.f21551l = z4;
        if (z4) {
            f14 = -f9;
            f13 = f5 - f8;
            gVar = this;
        } else {
            f13 = f8 - f5;
            gVar = this;
            f14 = f9;
        }
        gVar.e(f14, f13, f11, f12, f10);
    }

    public float d(float f5) {
        float f8 = this.f21543d;
        if (f5 <= f8) {
            float f9 = this.f21540a;
            return f9 + (((this.f21541b - f9) * f5) / f8);
        }
        int i8 = this.f21549j;
        if (i8 == 1) {
            return 0.0f;
        }
        float f10 = f5 - f8;
        float f11 = this.f21544e;
        if (f10 < f11) {
            float f12 = this.f21541b;
            return f12 + (((this.f21542c - f12) * f10) / f11);
        } else if (i8 == 2) {
            return this.f21547h;
        } else {
            float f13 = f10 - f11;
            float f14 = this.f21545f;
            if (f13 < f14) {
                float f15 = this.f21542c;
                return f15 - ((f13 * f15) / f14);
            }
            return this.f21548i;
        }
    }

    @Override // android.animation.TimeInterpolator
    public float getInterpolation(float f5) {
        float b9 = b(f5);
        this.f21553n = f5;
        return this.f21551l ? this.f21552m - b9 : this.f21552m + b9;
    }
}
