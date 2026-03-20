package l0;

import java.lang.reflect.Array;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class e extends b {

    /* renamed from: a  reason: collision with root package name */
    private double[] f21531a;

    /* renamed from: b  reason: collision with root package name */
    private double[][] f21532b;

    /* renamed from: c  reason: collision with root package name */
    private double[][] f21533c;

    public e(double[] dArr, double[][] dArr2) {
        int length = dArr.length;
        int length2 = dArr2[0].length;
        int i8 = length - 1;
        double[][] dArr3 = (double[][]) Array.newInstance(double.class, i8, length2);
        double[][] dArr4 = (double[][]) Array.newInstance(double.class, length, length2);
        for (int i9 = 0; i9 < length2; i9++) {
            int i10 = 0;
            while (i10 < i8) {
                int i11 = i10 + 1;
                dArr3[i10][i9] = (dArr2[i11][i9] - dArr2[i10][i9]) / (dArr[i11] - dArr[i10]);
                if (i10 == 0) {
                    dArr4[i10][i9] = dArr3[i10][i9];
                } else {
                    dArr4[i10][i9] = (dArr3[i10 - 1][i9] + dArr3[i10][i9]) * 0.5d;
                }
                i10 = i11;
            }
            dArr4[i8][i9] = dArr3[length - 2][i9];
        }
        for (int i12 = 0; i12 < i8; i12++) {
            for (int i13 = 0; i13 < length2; i13++) {
                if (dArr3[i12][i13] == 0.0d) {
                    dArr4[i12][i13] = 0.0d;
                    dArr4[i12 + 1][i13] = 0.0d;
                } else {
                    double d8 = dArr4[i12][i13] / dArr3[i12][i13];
                    int i14 = i12 + 1;
                    double d9 = dArr4[i14][i13] / dArr3[i12][i13];
                    double hypot = Math.hypot(d8, d9);
                    if (hypot > 9.0d) {
                        double d10 = 3.0d / hypot;
                        dArr4[i12][i13] = d8 * d10 * dArr3[i12][i13];
                        dArr4[i14][i13] = d10 * d9 * dArr3[i12][i13];
                    }
                }
            }
        }
        this.f21531a = dArr;
        this.f21532b = dArr2;
        this.f21533c = dArr4;
    }

    private static double i(double d8, double d9, double d10, double d11, double d12, double d13) {
        double d14 = d9 * d9;
        double d15 = d9 * 6.0d;
        double d16 = 3.0d * d8;
        return ((((((((((-6.0d) * d14) * d11) + (d15 * d11)) + ((6.0d * d14) * d10)) - (d15 * d10)) + ((d16 * d13) * d14)) + ((d16 * d12) * d14)) - (((2.0d * d8) * d13) * d9)) - (((4.0d * d8) * d12) * d9)) + (d8 * d12);
    }

    private static double j(double d8, double d9, double d10, double d11, double d12, double d13) {
        double d14 = d9 * d9;
        double d15 = d14 * d9;
        double d16 = 3.0d * d14;
        double d17 = d8 * d13;
        double d18 = d8 * d12;
        return (((((((((((-2.0d) * d15) * d11) + (d16 * d11)) + ((d15 * 2.0d) * d10)) - (d16 * d10)) + d10) + (d17 * d15)) + (d15 * d18)) - (d17 * d14)) - (((d8 * 2.0d) * d12) * d14)) + (d18 * d9);
    }

    @Override // l0.b
    public double c(double d8, int i8) {
        double[] dArr = this.f21531a;
        int length = dArr.length;
        int i9 = 0;
        if (d8 <= dArr[0]) {
            return this.f21532b[0][i8];
        }
        int i10 = length - 1;
        if (d8 >= dArr[i10]) {
            return this.f21532b[i10][i8];
        }
        while (i9 < i10) {
            double[] dArr2 = this.f21531a;
            if (d8 == dArr2[i9]) {
                return this.f21532b[i9][i8];
            }
            int i11 = i9 + 1;
            if (d8 < dArr2[i11]) {
                double d9 = dArr2[i11] - dArr2[i9];
                double d10 = (d8 - dArr2[i9]) / d9;
                double[][] dArr3 = this.f21532b;
                double d11 = dArr3[i9][i8];
                double d12 = dArr3[i11][i8];
                double[][] dArr4 = this.f21533c;
                return j(d9, d10, d11, d12, dArr4[i9][i8], dArr4[i11][i8]);
            }
            i9 = i11;
        }
        return 0.0d;
    }

    @Override // l0.b
    public void d(double d8, double[] dArr) {
        double[] dArr2 = this.f21531a;
        int length = dArr2.length;
        int i8 = 0;
        int length2 = this.f21532b[0].length;
        if (d8 <= dArr2[0]) {
            for (int i9 = 0; i9 < length2; i9++) {
                dArr[i9] = this.f21532b[0][i9];
            }
            return;
        }
        int i10 = length - 1;
        if (d8 >= dArr2[i10]) {
            while (i8 < length2) {
                dArr[i8] = this.f21532b[i10][i8];
                i8++;
            }
            return;
        }
        int i11 = 0;
        while (i11 < i10) {
            if (d8 == this.f21531a[i11]) {
                for (int i12 = 0; i12 < length2; i12++) {
                    dArr[i12] = this.f21532b[i11][i12];
                }
            }
            double[] dArr3 = this.f21531a;
            int i13 = i11 + 1;
            if (d8 < dArr3[i13]) {
                double d9 = dArr3[i13] - dArr3[i11];
                double d10 = (d8 - dArr3[i11]) / d9;
                while (i8 < length2) {
                    double[][] dArr4 = this.f21532b;
                    double d11 = dArr4[i11][i8];
                    double d12 = dArr4[i13][i8];
                    double[][] dArr5 = this.f21533c;
                    dArr[i8] = j(d9, d10, d11, d12, dArr5[i11][i8], dArr5[i13][i8]);
                    i8++;
                }
                return;
            }
            i11 = i13;
        }
    }

    @Override // l0.b
    public void e(double d8, float[] fArr) {
        double[] dArr = this.f21531a;
        int length = dArr.length;
        int i8 = 0;
        int length2 = this.f21532b[0].length;
        if (d8 <= dArr[0]) {
            for (int i9 = 0; i9 < length2; i9++) {
                fArr[i9] = (float) this.f21532b[0][i9];
            }
            return;
        }
        int i10 = length - 1;
        if (d8 >= dArr[i10]) {
            while (i8 < length2) {
                fArr[i8] = (float) this.f21532b[i10][i8];
                i8++;
            }
            return;
        }
        int i11 = 0;
        while (i11 < i10) {
            if (d8 == this.f21531a[i11]) {
                for (int i12 = 0; i12 < length2; i12++) {
                    fArr[i12] = (float) this.f21532b[i11][i12];
                }
            }
            double[] dArr2 = this.f21531a;
            int i13 = i11 + 1;
            if (d8 < dArr2[i13]) {
                double d9 = dArr2[i13] - dArr2[i11];
                double d10 = (d8 - dArr2[i11]) / d9;
                while (i8 < length2) {
                    double[][] dArr3 = this.f21532b;
                    double d11 = dArr3[i11][i8];
                    double d12 = dArr3[i13][i8];
                    double[][] dArr4 = this.f21533c;
                    fArr[i8] = (float) j(d9, d10, d11, d12, dArr4[i11][i8], dArr4[i13][i8]);
                    i8++;
                }
                return;
            }
            i11 = i13;
        }
    }

    @Override // l0.b
    public double f(double d8, int i8) {
        double d9;
        double[] dArr = this.f21531a;
        int length = dArr.length;
        int i9 = 0;
        if (d8 < dArr[0]) {
            d9 = dArr[0];
        } else {
            int i10 = length - 1;
            d9 = d8 >= dArr[i10] ? dArr[i10] : d8;
        }
        while (i9 < length - 1) {
            double[] dArr2 = this.f21531a;
            int i11 = i9 + 1;
            if (d9 <= dArr2[i11]) {
                double d10 = dArr2[i11] - dArr2[i9];
                double[][] dArr3 = this.f21532b;
                double d11 = dArr3[i9][i8];
                double d12 = dArr3[i11][i8];
                double[][] dArr4 = this.f21533c;
                return i(d10, (d9 - dArr2[i9]) / d10, d11, d12, dArr4[i9][i8], dArr4[i11][i8]) / d10;
            }
            i9 = i11;
        }
        return 0.0d;
    }

    @Override // l0.b
    public void g(double d8, double[] dArr) {
        double d9;
        double[] dArr2 = this.f21531a;
        int length = dArr2.length;
        int length2 = this.f21532b[0].length;
        if (d8 <= dArr2[0]) {
            d9 = dArr2[0];
        } else {
            int i8 = length - 1;
            d9 = d8 >= dArr2[i8] ? dArr2[i8] : d8;
        }
        int i9 = 0;
        while (i9 < length - 1) {
            double[] dArr3 = this.f21531a;
            int i10 = i9 + 1;
            if (d9 <= dArr3[i10]) {
                double d10 = dArr3[i10] - dArr3[i9];
                double d11 = (d9 - dArr3[i9]) / d10;
                for (int i11 = 0; i11 < length2; i11++) {
                    double[][] dArr4 = this.f21532b;
                    double d12 = dArr4[i9][i11];
                    double d13 = dArr4[i10][i11];
                    double[][] dArr5 = this.f21533c;
                    dArr[i11] = i(d10, d11, d12, d13, dArr5[i9][i11], dArr5[i10][i11]) / d10;
                }
                return;
            }
            i9 = i10;
        }
    }

    @Override // l0.b
    public double[] h() {
        return this.f21531a;
    }
}
