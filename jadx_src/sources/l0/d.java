package l0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class d extends b {

    /* renamed from: a  reason: collision with root package name */
    private double[] f21528a;

    /* renamed from: b  reason: collision with root package name */
    private double[][] f21529b;

    /* renamed from: c  reason: collision with root package name */
    private double f21530c;

    public d(double[] dArr, double[][] dArr2) {
        this.f21530c = Double.NaN;
        int length = dArr.length;
        int length2 = dArr2[0].length;
        this.f21528a = dArr;
        this.f21529b = dArr2;
        if (length2 > 2) {
            int i8 = 0;
            double d8 = 0.0d;
            double d9 = 0.0d;
            while (i8 < dArr.length) {
                double d10 = dArr2[i8][0];
                double d11 = dArr2[i8][0];
                if (i8 > 0) {
                    Math.hypot(d10 - d8, d11 - d9);
                }
                i8++;
                d8 = d10;
                d9 = d11;
            }
            this.f21530c = 0.0d;
        }
    }

    @Override // l0.b
    public double c(double d8, int i8) {
        double[] dArr = this.f21528a;
        int length = dArr.length;
        int i9 = 0;
        if (d8 <= dArr[0]) {
            return this.f21529b[0][i8];
        }
        int i10 = length - 1;
        if (d8 >= dArr[i10]) {
            return this.f21529b[i10][i8];
        }
        while (i9 < i10) {
            double[] dArr2 = this.f21528a;
            if (d8 == dArr2[i9]) {
                return this.f21529b[i9][i8];
            }
            int i11 = i9 + 1;
            if (d8 < dArr2[i11]) {
                double d9 = (d8 - dArr2[i9]) / (dArr2[i11] - dArr2[i9]);
                double[][] dArr3 = this.f21529b;
                return (dArr3[i9][i8] * (1.0d - d9)) + (dArr3[i11][i8] * d9);
            }
            i9 = i11;
        }
        return 0.0d;
    }

    @Override // l0.b
    public void d(double d8, double[] dArr) {
        double[] dArr2 = this.f21528a;
        int length = dArr2.length;
        int i8 = 0;
        int length2 = this.f21529b[0].length;
        if (d8 <= dArr2[0]) {
            for (int i9 = 0; i9 < length2; i9++) {
                dArr[i9] = this.f21529b[0][i9];
            }
            return;
        }
        int i10 = length - 1;
        if (d8 >= dArr2[i10]) {
            while (i8 < length2) {
                dArr[i8] = this.f21529b[i10][i8];
                i8++;
            }
            return;
        }
        int i11 = 0;
        while (i11 < i10) {
            if (d8 == this.f21528a[i11]) {
                for (int i12 = 0; i12 < length2; i12++) {
                    dArr[i12] = this.f21529b[i11][i12];
                }
            }
            double[] dArr3 = this.f21528a;
            int i13 = i11 + 1;
            if (d8 < dArr3[i13]) {
                double d9 = (d8 - dArr3[i11]) / (dArr3[i13] - dArr3[i11]);
                while (i8 < length2) {
                    double[][] dArr4 = this.f21529b;
                    dArr[i8] = (dArr4[i11][i8] * (1.0d - d9)) + (dArr4[i13][i8] * d9);
                    i8++;
                }
                return;
            }
            i11 = i13;
        }
    }

    @Override // l0.b
    public void e(double d8, float[] fArr) {
        double[] dArr = this.f21528a;
        int length = dArr.length;
        int i8 = 0;
        int length2 = this.f21529b[0].length;
        if (d8 <= dArr[0]) {
            for (int i9 = 0; i9 < length2; i9++) {
                fArr[i9] = (float) this.f21529b[0][i9];
            }
            return;
        }
        int i10 = length - 1;
        if (d8 >= dArr[i10]) {
            while (i8 < length2) {
                fArr[i8] = (float) this.f21529b[i10][i8];
                i8++;
            }
            return;
        }
        int i11 = 0;
        while (i11 < i10) {
            if (d8 == this.f21528a[i11]) {
                for (int i12 = 0; i12 < length2; i12++) {
                    fArr[i12] = (float) this.f21529b[i11][i12];
                }
            }
            double[] dArr2 = this.f21528a;
            int i13 = i11 + 1;
            if (d8 < dArr2[i13]) {
                double d9 = (d8 - dArr2[i11]) / (dArr2[i13] - dArr2[i11]);
                while (i8 < length2) {
                    double[][] dArr3 = this.f21529b;
                    fArr[i8] = (float) ((dArr3[i11][i8] * (1.0d - d9)) + (dArr3[i13][i8] * d9));
                    i8++;
                }
                return;
            }
            i11 = i13;
        }
    }

    @Override // l0.b
    public double f(double d8, int i8) {
        double[] dArr = this.f21528a;
        int length = dArr.length;
        int i9 = 0;
        if (d8 < dArr[0]) {
            d8 = dArr[0];
        } else {
            int i10 = length - 1;
            if (d8 >= dArr[i10]) {
                d8 = dArr[i10];
            }
        }
        while (i9 < length - 1) {
            double[] dArr2 = this.f21528a;
            int i11 = i9 + 1;
            if (d8 <= dArr2[i11]) {
                double d9 = dArr2[i11] - dArr2[i9];
                double d10 = dArr2[i9];
                double[][] dArr3 = this.f21529b;
                return (dArr3[i11][i8] - dArr3[i9][i8]) / d9;
            }
            i9 = i11;
        }
        return 0.0d;
    }

    @Override // l0.b
    public void g(double d8, double[] dArr) {
        double[] dArr2 = this.f21528a;
        int length = dArr2.length;
        int length2 = this.f21529b[0].length;
        if (d8 <= dArr2[0]) {
            d8 = dArr2[0];
        } else {
            int i8 = length - 1;
            if (d8 >= dArr2[i8]) {
                d8 = dArr2[i8];
            }
        }
        int i9 = 0;
        while (i9 < length - 1) {
            double[] dArr3 = this.f21528a;
            int i10 = i9 + 1;
            if (d8 <= dArr3[i10]) {
                double d9 = dArr3[i10] - dArr3[i9];
                double d10 = dArr3[i9];
                for (int i11 = 0; i11 < length2; i11++) {
                    double[][] dArr4 = this.f21529b;
                    dArr[i11] = (dArr4[i10][i11] - dArr4[i9][i11]) / d9;
                }
                return;
            }
            i9 = i10;
        }
    }

    @Override // l0.b
    public double[] h() {
        return this.f21528a;
    }
}
