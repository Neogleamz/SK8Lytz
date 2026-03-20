package l0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class b {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a extends b {

        /* renamed from: a  reason: collision with root package name */
        double f21517a;

        /* renamed from: b  reason: collision with root package name */
        double[] f21518b;

        a(double d8, double[] dArr) {
            this.f21517a = d8;
            this.f21518b = dArr;
        }

        @Override // l0.b
        public double c(double d8, int i8) {
            return this.f21518b[i8];
        }

        @Override // l0.b
        public void d(double d8, double[] dArr) {
            double[] dArr2 = this.f21518b;
            System.arraycopy(dArr2, 0, dArr, 0, dArr2.length);
        }

        @Override // l0.b
        public void e(double d8, float[] fArr) {
            int i8 = 0;
            while (true) {
                double[] dArr = this.f21518b;
                if (i8 >= dArr.length) {
                    return;
                }
                fArr[i8] = (float) dArr[i8];
                i8++;
            }
        }

        @Override // l0.b
        public double f(double d8, int i8) {
            return 0.0d;
        }

        @Override // l0.b
        public void g(double d8, double[] dArr) {
            for (int i8 = 0; i8 < this.f21518b.length; i8++) {
                dArr[i8] = 0.0d;
            }
        }

        @Override // l0.b
        public double[] h() {
            return new double[]{this.f21517a};
        }
    }

    public static b a(int i8, double[] dArr, double[][] dArr2) {
        if (dArr.length == 1) {
            i8 = 2;
        }
        return i8 != 0 ? i8 != 2 ? new d(dArr, dArr2) : new a(dArr[0], dArr2[0]) : new e(dArr, dArr2);
    }

    public static b b(int[] iArr, double[] dArr, double[][] dArr2) {
        return new l0.a(iArr, dArr, dArr2);
    }

    public abstract double c(double d8, int i8);

    public abstract void d(double d8, double[] dArr);

    public abstract void e(double d8, float[] fArr);

    public abstract double f(double d8, int i8);

    public abstract void g(double d8, double[] dArr);

    public abstract double[] h();
}
