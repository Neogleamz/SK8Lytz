package androidx.core.content.res;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a {

    /* renamed from: a  reason: collision with root package name */
    private final float f4631a;

    /* renamed from: b  reason: collision with root package name */
    private final float f4632b;

    /* renamed from: c  reason: collision with root package name */
    private final float f4633c;

    /* renamed from: d  reason: collision with root package name */
    private final float f4634d;

    /* renamed from: e  reason: collision with root package name */
    private final float f4635e;

    /* renamed from: f  reason: collision with root package name */
    private final float f4636f;

    /* renamed from: g  reason: collision with root package name */
    private final float f4637g;

    /* renamed from: h  reason: collision with root package name */
    private final float f4638h;

    /* renamed from: i  reason: collision with root package name */
    private final float f4639i;

    a(float f5, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15) {
        this.f4631a = f5;
        this.f4632b = f8;
        this.f4633c = f9;
        this.f4634d = f10;
        this.f4635e = f11;
        this.f4636f = f12;
        this.f4637g = f13;
        this.f4638h = f14;
        this.f4639i = f15;
    }

    private static a b(float f5, float f8, float f9) {
        float f10 = 1000.0f;
        float f11 = 0.0f;
        a aVar = null;
        float f12 = 100.0f;
        float f13 = 1000.0f;
        while (Math.abs(f11 - f12) > 0.01f) {
            float f14 = ((f12 - f11) / 2.0f) + f11;
            int p8 = e(f14, f8, f5).p();
            float b9 = b.b(p8);
            float abs = Math.abs(f9 - b9);
            if (abs < 0.2f) {
                a c9 = c(p8);
                float a9 = c9.a(e(c9.k(), c9.i(), f5));
                if (a9 <= 1.0f) {
                    aVar = c9;
                    f10 = abs;
                    f13 = a9;
                }
            }
            if (f10 == 0.0f && f13 == 0.0f) {
                break;
            } else if (b9 < f9) {
                f11 = f14;
            } else {
                f12 = f14;
            }
        }
        return aVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static a c(int i8) {
        return d(i8, l.f4676k);
    }

    static a d(int i8, l lVar) {
        float pow;
        float[] f5 = b.f(i8);
        float[][] fArr = b.f4640a;
        float f8 = (f5[0] * fArr[0][0]) + (f5[1] * fArr[0][1]) + (f5[2] * fArr[0][2]);
        float f9 = (f5[0] * fArr[1][0]) + (f5[1] * fArr[1][1]) + (f5[2] * fArr[1][2]);
        float f10 = (f5[0] * fArr[2][0]) + (f5[1] * fArr[2][1]) + (f5[2] * fArr[2][2]);
        float f11 = lVar.i()[0] * f8;
        float f12 = lVar.i()[1] * f9;
        float f13 = lVar.i()[2] * f10;
        float pow2 = (float) Math.pow((lVar.c() * Math.abs(f11)) / 100.0d, 0.42d);
        float pow3 = (float) Math.pow((lVar.c() * Math.abs(f12)) / 100.0d, 0.42d);
        float pow4 = (float) Math.pow((lVar.c() * Math.abs(f13)) / 100.0d, 0.42d);
        float signum = ((Math.signum(f11) * 400.0f) * pow2) / (pow2 + 27.13f);
        float signum2 = ((Math.signum(f12) * 400.0f) * pow3) / (pow3 + 27.13f);
        float signum3 = ((Math.signum(f13) * 400.0f) * pow4) / (pow4 + 27.13f);
        double d8 = signum3;
        float f14 = ((float) (((signum * 11.0d) + (signum2 * (-12.0d))) + d8)) / 11.0f;
        float f15 = ((float) ((signum + signum2) - (d8 * 2.0d))) / 9.0f;
        float f16 = signum2 * 20.0f;
        float f17 = (((signum * 20.0f) + f16) + (21.0f * signum3)) / 20.0f;
        float f18 = (((signum * 40.0f) + f16) + signum3) / 20.0f;
        float atan2 = (((float) Math.atan2(f15, f14)) * 180.0f) / 3.1415927f;
        if (atan2 < 0.0f) {
            atan2 += 360.0f;
        } else if (atan2 >= 360.0f) {
            atan2 -= 360.0f;
        }
        float f19 = atan2;
        float f20 = (3.1415927f * f19) / 180.0f;
        float pow5 = ((float) Math.pow((f18 * lVar.f()) / lVar.a(), lVar.b() * lVar.j())) * 100.0f;
        float d9 = lVar.d() * (4.0f / lVar.b()) * ((float) Math.sqrt(pow5 / 100.0f)) * (lVar.a() + 4.0f);
        float pow6 = ((float) Math.pow(1.64d - Math.pow(0.29d, lVar.e()), 0.73d)) * ((float) Math.pow((((((((float) (Math.cos((((((double) f19) < 20.14d ? 360.0f + f19 : f19) * 3.141592653589793d) / 180.0d) + 2.0d) + 3.8d)) * 0.25f) * 3846.1538f) * lVar.g()) * lVar.h()) * ((float) Math.sqrt((f14 * f14) + (f15 * f15)))) / (f17 + 0.305f), 0.9d)) * ((float) Math.sqrt(pow5 / 100.0d));
        float d10 = pow6 * lVar.d();
        float sqrt = ((float) Math.sqrt((pow * lVar.b()) / (lVar.a() + 4.0f))) * 50.0f;
        float f21 = (1.7f * pow5) / ((0.007f * pow5) + 1.0f);
        float log = ((float) Math.log((0.0228f * d10) + 1.0f)) * 43.85965f;
        double d11 = f20;
        return new a(f19, pow6, pow5, d9, d10, sqrt, f21, log * ((float) Math.cos(d11)), log * ((float) Math.sin(d11)));
    }

    private static a e(float f5, float f8, float f9) {
        return f(f5, f8, f9, l.f4676k);
    }

    private static a f(float f5, float f8, float f9, l lVar) {
        double d8;
        float b9 = (4.0f / lVar.b()) * ((float) Math.sqrt(f5 / 100.0d)) * (lVar.a() + 4.0f) * lVar.d();
        float d9 = f8 * lVar.d();
        float sqrt = ((float) Math.sqrt(((f8 / ((float) Math.sqrt(d8))) * lVar.b()) / (lVar.a() + 4.0f))) * 50.0f;
        float f10 = (1.7f * f5) / ((0.007f * f5) + 1.0f);
        float log = ((float) Math.log((d9 * 0.0228d) + 1.0d)) * 43.85965f;
        double d10 = (3.1415927f * f9) / 180.0f;
        return new a(f9, f8, f5, b9, d9, sqrt, f10, log * ((float) Math.cos(d10)), log * ((float) Math.sin(d10)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int m(float f5, float f8, float f9) {
        return n(f5, f8, f9, l.f4676k);
    }

    static int n(float f5, float f8, float f9, l lVar) {
        if (f8 < 1.0d || Math.round(f9) <= 0.0d || Math.round(f9) >= 100.0d) {
            return b.a(f9);
        }
        float min = f5 < 0.0f ? 0.0f : Math.min(360.0f, f5);
        a aVar = null;
        boolean z4 = true;
        float f10 = 0.0f;
        float f11 = f8;
        while (Math.abs(f10 - f8) >= 0.4f) {
            a b9 = b(min, f11, f9);
            if (z4) {
                if (b9 != null) {
                    return b9.o(lVar);
                }
                z4 = false;
            } else if (b9 == null) {
                f8 = f11;
            } else {
                f10 = f11;
                aVar = b9;
            }
            f11 = ((f8 - f10) / 2.0f) + f10;
        }
        return aVar == null ? b.a(f9) : aVar.o(lVar);
    }

    float a(a aVar) {
        float l8 = l() - aVar.l();
        float g8 = g() - aVar.g();
        float h8 = h() - aVar.h();
        return (float) (Math.pow(Math.sqrt((l8 * l8) + (g8 * g8) + (h8 * h8)), 0.63d) * 1.41d);
    }

    float g() {
        return this.f4638h;
    }

    float h() {
        return this.f4639i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float i() {
        return this.f4632b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float j() {
        return this.f4631a;
    }

    float k() {
        return this.f4633c;
    }

    float l() {
        return this.f4637g;
    }

    int o(l lVar) {
        float f5;
        float pow = (float) Math.pow(((((double) i()) == 0.0d || ((double) k()) == 0.0d) ? 0.0f : i() / ((float) Math.sqrt(k() / 100.0d))) / Math.pow(1.64d - Math.pow(0.29d, lVar.e()), 0.73d), 1.1111111111111112d);
        double j8 = (j() * 3.1415927f) / 180.0f;
        float a9 = lVar.a() * ((float) Math.pow(k() / 100.0d, (1.0d / lVar.b()) / lVar.j()));
        float cos = ((float) (Math.cos(2.0d + j8) + 3.8d)) * 0.25f * 3846.1538f * lVar.g() * lVar.h();
        float f8 = a9 / lVar.f();
        float sin = (float) Math.sin(j8);
        float cos2 = (float) Math.cos(j8);
        float f9 = (((0.305f + f8) * 23.0f) * pow) / (((cos * 23.0f) + ((11.0f * pow) * cos2)) + ((pow * 108.0f) * sin));
        float f10 = cos2 * f9;
        float f11 = f9 * sin;
        float f12 = f8 * 460.0f;
        float f13 = (((451.0f * f10) + f12) + (288.0f * f11)) / 1403.0f;
        float f14 = ((f12 - (891.0f * f10)) - (261.0f * f11)) / 1403.0f;
        float signum = Math.signum(f13) * (100.0f / lVar.c()) * ((float) Math.pow((float) Math.max(0.0d, (Math.abs(f13) * 27.13d) / (400.0d - Math.abs(f13))), 2.380952380952381d));
        float signum2 = Math.signum(f14) * (100.0f / lVar.c()) * ((float) Math.pow((float) Math.max(0.0d, (Math.abs(f14) * 27.13d) / (400.0d - Math.abs(f14))), 2.380952380952381d));
        float signum3 = Math.signum(((f12 - (f10 * 220.0f)) - (f11 * 6300.0f)) / 1403.0f) * (100.0f / lVar.c()) * ((float) Math.pow((float) Math.max(0.0d, (Math.abs(f5) * 27.13d) / (400.0d - Math.abs(f5))), 2.380952380952381d));
        float f15 = signum / lVar.i()[0];
        float f16 = signum2 / lVar.i()[1];
        float f17 = signum3 / lVar.i()[2];
        float[][] fArr = b.f4641b;
        return androidx.core.graphics.b.d((fArr[0][0] * f15) + (fArr[0][1] * f16) + (fArr[0][2] * f17), (fArr[1][0] * f15) + (fArr[1][1] * f16) + (fArr[1][2] * f17), (f15 * fArr[2][0]) + (f16 * fArr[2][1]) + (f17 * fArr[2][2]));
    }

    int p() {
        return o(l.f4676k);
    }
}
