package y0;

import y0.b;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e {

    /* renamed from: a  reason: collision with root package name */
    double f24363a;

    /* renamed from: b  reason: collision with root package name */
    double f24364b;

    /* renamed from: c  reason: collision with root package name */
    private boolean f24365c;

    /* renamed from: d  reason: collision with root package name */
    private double f24366d;

    /* renamed from: e  reason: collision with root package name */
    private double f24367e;

    /* renamed from: f  reason: collision with root package name */
    private double f24368f;

    /* renamed from: g  reason: collision with root package name */
    private double f24369g;

    /* renamed from: h  reason: collision with root package name */
    private double f24370h;

    /* renamed from: i  reason: collision with root package name */
    private double f24371i;

    /* renamed from: j  reason: collision with root package name */
    private final b.o f24372j;

    public e() {
        this.f24363a = Math.sqrt(1500.0d);
        this.f24364b = 0.5d;
        this.f24365c = false;
        this.f24371i = Double.MAX_VALUE;
        this.f24372j = new b.o();
    }

    public e(float f5) {
        this.f24363a = Math.sqrt(1500.0d);
        this.f24364b = 0.5d;
        this.f24365c = false;
        this.f24371i = Double.MAX_VALUE;
        this.f24372j = new b.o();
        this.f24371i = f5;
    }

    private void b() {
        if (this.f24365c) {
            return;
        }
        if (this.f24371i == Double.MAX_VALUE) {
            throw new IllegalStateException("Error: Final position of the spring must be set before the animation starts");
        }
        double d8 = this.f24364b;
        if (d8 > 1.0d) {
            double d9 = this.f24363a;
            this.f24368f = ((-d8) * d9) + (d9 * Math.sqrt((d8 * d8) - 1.0d));
            double d10 = this.f24364b;
            double d11 = this.f24363a;
            this.f24369g = ((-d10) * d11) - (d11 * Math.sqrt((d10 * d10) - 1.0d));
        } else if (d8 >= 0.0d && d8 < 1.0d) {
            this.f24370h = this.f24363a * Math.sqrt(1.0d - (d8 * d8));
        }
        this.f24365c = true;
    }

    public float a() {
        return (float) this.f24371i;
    }

    public boolean c(float f5, float f8) {
        return ((double) Math.abs(f8)) < this.f24367e && ((double) Math.abs(f5 - a())) < this.f24366d;
    }

    public e d(float f5) {
        if (f5 >= 0.0f) {
            this.f24364b = f5;
            this.f24365c = false;
            return this;
        }
        throw new IllegalArgumentException("Damping ratio must be non-negative");
    }

    public e e(float f5) {
        this.f24371i = f5;
        return this;
    }

    public e f(float f5) {
        if (f5 > 0.0f) {
            this.f24363a = Math.sqrt(f5);
            this.f24365c = false;
            return this;
        }
        throw new IllegalArgumentException("Spring stiffness constant must be positive.");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void g(double d8) {
        double abs = Math.abs(d8);
        this.f24366d = abs;
        this.f24367e = abs * 62.5d;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public b.o h(double d8, double d9, long j8) {
        double cos;
        double d10;
        b();
        double d11 = j8 / 1000.0d;
        double d12 = d8 - this.f24371i;
        double d13 = this.f24364b;
        if (d13 > 1.0d) {
            double d14 = this.f24369g;
            double d15 = this.f24368f;
            double d16 = d12 - (((d14 * d12) - d9) / (d14 - d15));
            double d17 = ((d12 * d14) - d9) / (d14 - d15);
            d10 = (Math.pow(2.718281828459045d, d14 * d11) * d16) + (Math.pow(2.718281828459045d, this.f24368f * d11) * d17);
            double d18 = this.f24369g;
            double pow = d16 * d18 * Math.pow(2.718281828459045d, d18 * d11);
            double d19 = this.f24368f;
            cos = pow + (d17 * d19 * Math.pow(2.718281828459045d, d19 * d11));
        } else if (d13 == 1.0d) {
            double d20 = this.f24363a;
            double d21 = d9 + (d20 * d12);
            double d22 = d12 + (d21 * d11);
            d10 = Math.pow(2.718281828459045d, (-d20) * d11) * d22;
            double pow2 = d22 * Math.pow(2.718281828459045d, (-this.f24363a) * d11);
            double d23 = this.f24363a;
            cos = (d21 * Math.pow(2.718281828459045d, (-d23) * d11)) + (pow2 * (-d23));
        } else {
            double d24 = 1.0d / this.f24370h;
            double d25 = this.f24363a;
            double d26 = d24 * ((d13 * d25 * d12) + d9);
            double pow3 = Math.pow(2.718281828459045d, (-d13) * d25 * d11) * ((Math.cos(this.f24370h * d11) * d12) + (Math.sin(this.f24370h * d11) * d26));
            double d27 = this.f24363a;
            double d28 = this.f24364b;
            double pow4 = Math.pow(2.718281828459045d, (-d28) * d27 * d11);
            double d29 = this.f24370h;
            double sin = (-d29) * d12 * Math.sin(d29 * d11);
            double d30 = this.f24370h;
            cos = ((-d27) * pow3 * d28) + (pow4 * (sin + (d26 * d30 * Math.cos(d30 * d11))));
            d10 = pow3;
        }
        b.o oVar = this.f24372j;
        oVar.f24360a = (float) (d10 + this.f24371i);
        oVar.f24361b = (float) cos;
        return oVar;
    }
}
