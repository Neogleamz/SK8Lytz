package androidx.core.content.res;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class l {

    /* renamed from: k  reason: collision with root package name */
    static final l f4676k = k(b.f4642c, (float) ((b.h(50.0f) * 63.66197723675813d) / 100.0d), 50.0f, 2.0f, false);

    /* renamed from: a  reason: collision with root package name */
    private final float f4677a;

    /* renamed from: b  reason: collision with root package name */
    private final float f4678b;

    /* renamed from: c  reason: collision with root package name */
    private final float f4679c;

    /* renamed from: d  reason: collision with root package name */
    private final float f4680d;

    /* renamed from: e  reason: collision with root package name */
    private final float f4681e;

    /* renamed from: f  reason: collision with root package name */
    private final float f4682f;

    /* renamed from: g  reason: collision with root package name */
    private final float[] f4683g;

    /* renamed from: h  reason: collision with root package name */
    private final float f4684h;

    /* renamed from: i  reason: collision with root package name */
    private final float f4685i;

    /* renamed from: j  reason: collision with root package name */
    private final float f4686j;

    private l(float f5, float f8, float f9, float f10, float f11, float f12, float[] fArr, float f13, float f14, float f15) {
        this.f4682f = f5;
        this.f4677a = f8;
        this.f4678b = f9;
        this.f4679c = f10;
        this.f4680d = f11;
        this.f4681e = f12;
        this.f4683g = fArr;
        this.f4684h = f13;
        this.f4685i = f14;
        this.f4686j = f15;
    }

    static l k(float[] fArr, float f5, float f8, float f9, boolean z4) {
        float[][] fArr2 = b.f4640a;
        float f10 = (fArr[0] * fArr2[0][0]) + (fArr[1] * fArr2[0][1]) + (fArr[2] * fArr2[0][2]);
        float f11 = (fArr[0] * fArr2[1][0]) + (fArr[1] * fArr2[1][1]) + (fArr[2] * fArr2[1][2]);
        float f12 = (fArr[0] * fArr2[2][0]) + (fArr[1] * fArr2[2][1]) + (fArr[2] * fArr2[2][2]);
        float f13 = (f9 / 10.0f) + 0.8f;
        float d8 = ((double) f13) >= 0.9d ? b.d(0.59f, 0.69f, (f13 - 0.9f) * 10.0f) : b.d(0.525f, 0.59f, (f13 - 0.8f) * 10.0f);
        float exp = z4 ? 1.0f : (1.0f - (((float) Math.exp(((-f5) - 42.0f) / 92.0f)) * 0.2777778f)) * f13;
        double d9 = exp;
        if (d9 > 1.0d) {
            exp = 1.0f;
        } else if (d9 < 0.0d) {
            exp = 0.0f;
        }
        float[] fArr3 = {(((100.0f / f10) * exp) + 1.0f) - exp, (((100.0f / f11) * exp) + 1.0f) - exp, (((100.0f / f12) * exp) + 1.0f) - exp};
        float f14 = 1.0f / ((5.0f * f5) + 1.0f);
        float f15 = f14 * f14 * f14 * f14;
        float f16 = 1.0f - f15;
        float cbrt = (f15 * f5) + (0.1f * f16 * f16 * ((float) Math.cbrt(f5 * 5.0d)));
        float h8 = b.h(f8) / fArr[1];
        double d10 = h8;
        float sqrt = ((float) Math.sqrt(d10)) + 1.48f;
        float pow = 0.725f / ((float) Math.pow(d10, 0.2d));
        float[] fArr4 = {(float) Math.pow(((fArr3[0] * cbrt) * f10) / 100.0d, 0.42d), (float) Math.pow(((fArr3[1] * cbrt) * f11) / 100.0d, 0.42d), (float) Math.pow(((fArr3[2] * cbrt) * f12) / 100.0d, 0.42d)};
        float[] fArr5 = {(fArr4[0] * 400.0f) / (fArr4[0] + 27.13f), (fArr4[1] * 400.0f) / (fArr4[1] + 27.13f), (fArr4[2] * 400.0f) / (fArr4[2] + 27.13f)};
        return new l(h8, ((fArr5[0] * 2.0f) + fArr5[1] + (fArr5[2] * 0.05f)) * pow, pow, pow, d8, f13, fArr3, cbrt, (float) Math.pow(cbrt, 0.25d), sqrt);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float a() {
        return this.f4677a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float b() {
        return this.f4680d;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float c() {
        return this.f4684h;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float d() {
        return this.f4685i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float e() {
        return this.f4682f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float f() {
        return this.f4678b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float g() {
        return this.f4681e;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float h() {
        return this.f4679c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float[] i() {
        return this.f4683g;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float j() {
        return this.f4686j;
    }
}
