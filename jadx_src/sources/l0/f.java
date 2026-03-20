package l0;

import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class f {

    /* renamed from: c  reason: collision with root package name */
    double[] f21536c;

    /* renamed from: d  reason: collision with root package name */
    int f21537d;

    /* renamed from: a  reason: collision with root package name */
    float[] f21534a = new float[0];

    /* renamed from: b  reason: collision with root package name */
    double[] f21535b = new double[0];

    /* renamed from: e  reason: collision with root package name */
    double f21538e = 6.283185307179586d;

    /* renamed from: f  reason: collision with root package name */
    private boolean f21539f = false;

    public void a(double d8, float f5) {
        int length = this.f21534a.length + 1;
        int binarySearch = Arrays.binarySearch(this.f21535b, d8);
        if (binarySearch < 0) {
            binarySearch = (-binarySearch) - 1;
        }
        this.f21535b = Arrays.copyOf(this.f21535b, length);
        this.f21534a = Arrays.copyOf(this.f21534a, length);
        this.f21536c = new double[length];
        double[] dArr = this.f21535b;
        System.arraycopy(dArr, binarySearch, dArr, binarySearch + 1, (length - binarySearch) - 1);
        this.f21535b[binarySearch] = d8;
        this.f21534a[binarySearch] = f5;
        this.f21539f = false;
    }

    double b(double d8) {
        if (d8 <= 0.0d) {
            d8 = 1.0E-5d;
        } else if (d8 >= 1.0d) {
            d8 = 0.999999d;
        }
        int binarySearch = Arrays.binarySearch(this.f21535b, d8);
        if (binarySearch <= 0 && binarySearch != 0) {
            int i8 = (-binarySearch) - 1;
            float[] fArr = this.f21534a;
            int i9 = i8 - 1;
            double d9 = fArr[i8] - fArr[i9];
            double[] dArr = this.f21535b;
            double d10 = d9 / (dArr[i8] - dArr[i9]);
            return (fArr[i9] - (d10 * dArr[i9])) + (d8 * d10);
        }
        return 0.0d;
    }

    double c(double d8) {
        if (d8 < 0.0d) {
            d8 = 0.0d;
        } else if (d8 > 1.0d) {
            d8 = 1.0d;
        }
        int binarySearch = Arrays.binarySearch(this.f21535b, d8);
        if (binarySearch > 0) {
            return 1.0d;
        }
        if (binarySearch != 0) {
            int i8 = (-binarySearch) - 1;
            float[] fArr = this.f21534a;
            int i9 = i8 - 1;
            double d9 = fArr[i8] - fArr[i9];
            double[] dArr = this.f21535b;
            double d10 = d9 / (dArr[i8] - dArr[i9]);
            return this.f21536c[i9] + ((fArr[i9] - (dArr[i9] * d10)) * (d8 - dArr[i9])) + ((d10 * ((d8 * d8) - (dArr[i9] * dArr[i9]))) / 2.0d);
        }
        return 0.0d;
    }

    public double d(double d8) {
        double b9;
        double signum;
        double b10;
        double b11;
        double sin;
        switch (this.f21537d) {
            case 1:
                return 0.0d;
            case 2:
                b9 = b(d8) * 4.0d;
                signum = Math.signum((((c(d8) * 4.0d) + 3.0d) % 4.0d) - 2.0d);
                return b9 * signum;
            case 3:
                b10 = b(d8);
                return b10 * 2.0d;
            case 4:
                b10 = -b(d8);
                return b10 * 2.0d;
            case 5:
                b11 = (-this.f21538e) * b(d8);
                sin = Math.sin(this.f21538e * c(d8));
                return b11 * sin;
            case 6:
                b9 = b(d8) * 4.0d;
                signum = (((c(d8) * 4.0d) + 2.0d) % 4.0d) - 2.0d;
                return b9 * signum;
            default:
                b11 = this.f21538e * b(d8);
                sin = Math.cos(this.f21538e * c(d8));
                return b11 * sin;
        }
    }

    public double e(double d8) {
        double abs;
        switch (this.f21537d) {
            case 1:
                return Math.signum(0.5d - (c(d8) % 1.0d));
            case 2:
                abs = Math.abs((((c(d8) * 4.0d) + 1.0d) % 4.0d) - 2.0d);
                break;
            case 3:
                return (((c(d8) * 2.0d) + 1.0d) % 2.0d) - 1.0d;
            case 4:
                abs = ((c(d8) * 2.0d) + 1.0d) % 2.0d;
                break;
            case 5:
                return Math.cos(this.f21538e * c(d8));
            case 6:
                double abs2 = 1.0d - Math.abs(((c(d8) * 4.0d) % 4.0d) - 2.0d);
                abs = abs2 * abs2;
                break;
            default:
                return Math.sin(this.f21538e * c(d8));
        }
        return 1.0d - abs;
    }

    public void f() {
        float[] fArr;
        float[] fArr2;
        float[] fArr3;
        int i8;
        int i9 = 0;
        double d8 = 0.0d;
        while (true) {
            if (i9 >= this.f21534a.length) {
                break;
            }
            d8 += fArr[i9];
            i9++;
        }
        double d9 = 0.0d;
        int i10 = 1;
        while (true) {
            if (i10 >= this.f21534a.length) {
                break;
            }
            double[] dArr = this.f21535b;
            d9 += (dArr[i10] - dArr[i10 - 1]) * ((fArr2[i8] + fArr2[i10]) / 2.0f);
            i10++;
        }
        int i11 = 0;
        while (true) {
            float[] fArr4 = this.f21534a;
            if (i11 >= fArr4.length) {
                break;
            }
            fArr4[i11] = (float) (fArr4[i11] * (d8 / d9));
            i11++;
        }
        this.f21536c[0] = 0.0d;
        int i12 = 1;
        while (true) {
            if (i12 >= this.f21534a.length) {
                this.f21539f = true;
                return;
            }
            int i13 = i12 - 1;
            double[] dArr2 = this.f21535b;
            double d10 = dArr2[i12] - dArr2[i13];
            double[] dArr3 = this.f21536c;
            dArr3[i12] = dArr3[i13] + (d10 * ((fArr3[i13] + fArr3[i12]) / 2.0f));
            i12++;
        }
    }

    public void g(int i8) {
        this.f21537d = i8;
    }

    public String toString() {
        return "pos =" + Arrays.toString(this.f21535b) + " period=" + Arrays.toString(this.f21534a);
    }
}
