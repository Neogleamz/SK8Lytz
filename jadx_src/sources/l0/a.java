package l0;

import java.util.Arrays;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a extends b {

    /* renamed from: a  reason: collision with root package name */
    private final double[] f21497a;

    /* renamed from: b  reason: collision with root package name */
    C0185a[] f21498b;

    /* renamed from: l0.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class C0185a {

        /* renamed from: s  reason: collision with root package name */
        private static double[] f21499s = new double[91];

        /* renamed from: a  reason: collision with root package name */
        double[] f21500a;

        /* renamed from: b  reason: collision with root package name */
        double f21501b;

        /* renamed from: c  reason: collision with root package name */
        double f21502c;

        /* renamed from: d  reason: collision with root package name */
        double f21503d;

        /* renamed from: e  reason: collision with root package name */
        double f21504e;

        /* renamed from: f  reason: collision with root package name */
        double f21505f;

        /* renamed from: g  reason: collision with root package name */
        double f21506g;

        /* renamed from: h  reason: collision with root package name */
        double f21507h;

        /* renamed from: i  reason: collision with root package name */
        double f21508i;

        /* renamed from: j  reason: collision with root package name */
        double f21509j;

        /* renamed from: k  reason: collision with root package name */
        double f21510k;

        /* renamed from: l  reason: collision with root package name */
        double f21511l;

        /* renamed from: m  reason: collision with root package name */
        double f21512m;

        /* renamed from: n  reason: collision with root package name */
        double f21513n;

        /* renamed from: o  reason: collision with root package name */
        double f21514o;

        /* renamed from: p  reason: collision with root package name */
        double f21515p;
        boolean q;

        /* renamed from: r  reason: collision with root package name */
        boolean f21516r;

        C0185a(int i8, double d8, double d9, double d10, double d11, double d12, double d13) {
            this.f21516r = false;
            this.q = i8 == 1;
            this.f21502c = d8;
            this.f21503d = d9;
            this.f21508i = 1.0d / (d9 - d8);
            if (3 == i8) {
                this.f21516r = true;
            }
            double d14 = d12 - d10;
            double d15 = d13 - d11;
            if (!this.f21516r && Math.abs(d14) >= 0.001d && Math.abs(d15) >= 0.001d) {
                this.f21500a = new double[101];
                boolean z4 = this.q;
                this.f21509j = d14 * (z4 ? -1 : 1);
                this.f21510k = d15 * (z4 ? 1 : -1);
                this.f21511l = z4 ? d12 : d10;
                this.f21512m = z4 ? d11 : d13;
                a(d10, d11, d12, d13);
                this.f21513n = this.f21501b * this.f21508i;
                return;
            }
            this.f21516r = true;
            this.f21504e = d10;
            this.f21505f = d12;
            this.f21506g = d11;
            this.f21507h = d13;
            double hypot = Math.hypot(d15, d14);
            this.f21501b = hypot;
            this.f21513n = hypot * this.f21508i;
            double d16 = this.f21503d;
            double d17 = this.f21502c;
            this.f21511l = d14 / (d16 - d17);
            this.f21512m = d15 / (d16 - d17);
        }

        private void a(double d8, double d9, double d10, double d11) {
            double[] dArr;
            double[] dArr2;
            double d12;
            double d13 = d10 - d8;
            double d14 = d9 - d11;
            int i8 = 0;
            double d15 = 0.0d;
            double d16 = 0.0d;
            double d17 = 0.0d;
            while (true) {
                if (i8 >= f21499s.length) {
                    break;
                }
                double d18 = d15;
                double radians = Math.toRadians((i8 * 90.0d) / (dArr.length - 1));
                double sin = Math.sin(radians) * d13;
                double cos = Math.cos(radians) * d14;
                if (i8 > 0) {
                    d12 = Math.hypot(sin - d16, cos - d17) + d18;
                    f21499s[i8] = d12;
                } else {
                    d12 = d18;
                }
                i8++;
                d17 = cos;
                d15 = d12;
                d16 = sin;
            }
            double d19 = d15;
            this.f21501b = d19;
            int i9 = 0;
            while (true) {
                double[] dArr3 = f21499s;
                if (i9 >= dArr3.length) {
                    break;
                }
                dArr3[i9] = dArr3[i9] / d19;
                i9++;
            }
            int i10 = 0;
            while (true) {
                if (i10 >= this.f21500a.length) {
                    return;
                }
                double length = i10 / (dArr2.length - 1);
                int binarySearch = Arrays.binarySearch(f21499s, length);
                if (binarySearch >= 0) {
                    this.f21500a[i10] = binarySearch / (f21499s.length - 1);
                } else if (binarySearch == -1) {
                    this.f21500a[i10] = 0.0d;
                } else {
                    int i11 = -binarySearch;
                    int i12 = i11 - 2;
                    double[] dArr4 = f21499s;
                    this.f21500a[i10] = (i12 + ((length - dArr4[i12]) / (dArr4[i11 - 1] - dArr4[i12]))) / (dArr4.length - 1);
                }
                i10++;
            }
        }

        double b() {
            double d8 = this.f21509j * this.f21515p;
            double hypot = this.f21513n / Math.hypot(d8, (-this.f21510k) * this.f21514o);
            if (this.q) {
                d8 = -d8;
            }
            return d8 * hypot;
        }

        double c() {
            double d8 = this.f21509j * this.f21515p;
            double d9 = (-this.f21510k) * this.f21514o;
            double hypot = this.f21513n / Math.hypot(d8, d9);
            return this.q ? (-d9) * hypot : d9 * hypot;
        }

        public double d(double d8) {
            return this.f21511l;
        }

        public double e(double d8) {
            return this.f21512m;
        }

        public double f(double d8) {
            double d9 = (d8 - this.f21502c) * this.f21508i;
            double d10 = this.f21504e;
            return d10 + (d9 * (this.f21505f - d10));
        }

        public double g(double d8) {
            double d9 = (d8 - this.f21502c) * this.f21508i;
            double d10 = this.f21506g;
            return d10 + (d9 * (this.f21507h - d10));
        }

        double h() {
            return this.f21511l + (this.f21509j * this.f21514o);
        }

        double i() {
            return this.f21512m + (this.f21510k * this.f21515p);
        }

        double j(double d8) {
            if (d8 <= 0.0d) {
                return 0.0d;
            }
            if (d8 >= 1.0d) {
                return 1.0d;
            }
            double[] dArr = this.f21500a;
            double length = d8 * (dArr.length - 1);
            int i8 = (int) length;
            return dArr[i8] + ((length - i8) * (dArr[i8 + 1] - dArr[i8]));
        }

        void k(double d8) {
            double j8 = j((this.q ? this.f21503d - d8 : d8 - this.f21502c) * this.f21508i) * 1.5707963267948966d;
            this.f21514o = Math.sin(j8);
            this.f21515p = Math.cos(j8);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0026, code lost:
        if (r5 == 1) goto L12;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public a(int[] r25, double[] r26, double[][] r27) {
        /*
            r24 = this;
            r0 = r24
            r1 = r26
            r24.<init>()
            r0.f21497a = r1
            int r2 = r1.length
            r3 = 1
            int r2 = r2 - r3
            l0.a$a[] r2 = new l0.a.C0185a[r2]
            r0.f21498b = r2
            r2 = 0
            r4 = r2
            r5 = r3
            r6 = r5
        L14:
            l0.a$a[] r7 = r0.f21498b
            int r8 = r7.length
            if (r4 >= r8) goto L51
            r8 = r25[r4]
            r9 = 3
            r10 = 2
            if (r8 == 0) goto L2d
            if (r8 == r3) goto L2a
            if (r8 == r10) goto L28
            if (r8 == r9) goto L26
            goto L2e
        L26:
            if (r5 != r3) goto L2a
        L28:
            r5 = r10
            goto L2b
        L2a:
            r5 = r3
        L2b:
            r6 = r5
            goto L2e
        L2d:
            r6 = r9
        L2e:
            l0.a$a r22 = new l0.a$a
            r10 = r1[r4]
            int r23 = r4 + 1
            r12 = r1[r23]
            r8 = r27[r4]
            r14 = r8[r2]
            r8 = r27[r4]
            r16 = r8[r3]
            r8 = r27[r23]
            r18 = r8[r2]
            r8 = r27[r23]
            r20 = r8[r3]
            r8 = r22
            r9 = r6
            r8.<init>(r9, r10, r12, r14, r16, r18, r20)
            r7[r4] = r22
            r4 = r23
            goto L14
        L51:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: l0.a.<init>(int[], double[], double[][]):void");
    }

    @Override // l0.b
    public double c(double d8, int i8) {
        C0185a[] c0185aArr = this.f21498b;
        int i9 = 0;
        if (d8 < c0185aArr[0].f21502c) {
            d8 = c0185aArr[0].f21502c;
        } else if (d8 > c0185aArr[c0185aArr.length - 1].f21503d) {
            d8 = c0185aArr[c0185aArr.length - 1].f21503d;
        }
        while (true) {
            C0185a[] c0185aArr2 = this.f21498b;
            if (i9 >= c0185aArr2.length) {
                return Double.NaN;
            }
            if (d8 <= c0185aArr2[i9].f21503d) {
                if (c0185aArr2[i9].f21516r) {
                    return i8 == 0 ? c0185aArr2[i9].f(d8) : c0185aArr2[i9].g(d8);
                }
                c0185aArr2[i9].k(d8);
                C0185a[] c0185aArr3 = this.f21498b;
                return i8 == 0 ? c0185aArr3[i9].h() : c0185aArr3[i9].i();
            }
            i9++;
        }
    }

    @Override // l0.b
    public void d(double d8, double[] dArr) {
        C0185a[] c0185aArr = this.f21498b;
        if (d8 < c0185aArr[0].f21502c) {
            d8 = c0185aArr[0].f21502c;
        }
        if (d8 > c0185aArr[c0185aArr.length - 1].f21503d) {
            d8 = c0185aArr[c0185aArr.length - 1].f21503d;
        }
        int i8 = 0;
        while (true) {
            C0185a[] c0185aArr2 = this.f21498b;
            if (i8 >= c0185aArr2.length) {
                return;
            }
            if (d8 <= c0185aArr2[i8].f21503d) {
                if (c0185aArr2[i8].f21516r) {
                    dArr[0] = c0185aArr2[i8].f(d8);
                    dArr[1] = this.f21498b[i8].g(d8);
                    return;
                }
                c0185aArr2[i8].k(d8);
                dArr[0] = this.f21498b[i8].h();
                dArr[1] = this.f21498b[i8].i();
                return;
            }
            i8++;
        }
    }

    @Override // l0.b
    public void e(double d8, float[] fArr) {
        C0185a[] c0185aArr = this.f21498b;
        if (d8 < c0185aArr[0].f21502c) {
            d8 = c0185aArr[0].f21502c;
        } else if (d8 > c0185aArr[c0185aArr.length - 1].f21503d) {
            d8 = c0185aArr[c0185aArr.length - 1].f21503d;
        }
        int i8 = 0;
        while (true) {
            C0185a[] c0185aArr2 = this.f21498b;
            if (i8 >= c0185aArr2.length) {
                return;
            }
            if (d8 <= c0185aArr2[i8].f21503d) {
                if (c0185aArr2[i8].f21516r) {
                    fArr[0] = (float) c0185aArr2[i8].f(d8);
                    fArr[1] = (float) this.f21498b[i8].g(d8);
                    return;
                }
                c0185aArr2[i8].k(d8);
                fArr[0] = (float) this.f21498b[i8].h();
                fArr[1] = (float) this.f21498b[i8].i();
                return;
            }
            i8++;
        }
    }

    @Override // l0.b
    public double f(double d8, int i8) {
        C0185a[] c0185aArr = this.f21498b;
        int i9 = 0;
        if (d8 < c0185aArr[0].f21502c) {
            d8 = c0185aArr[0].f21502c;
        }
        if (d8 > c0185aArr[c0185aArr.length - 1].f21503d) {
            d8 = c0185aArr[c0185aArr.length - 1].f21503d;
        }
        while (true) {
            C0185a[] c0185aArr2 = this.f21498b;
            if (i9 >= c0185aArr2.length) {
                return Double.NaN;
            }
            if (d8 <= c0185aArr2[i9].f21503d) {
                if (c0185aArr2[i9].f21516r) {
                    return i8 == 0 ? c0185aArr2[i9].d(d8) : c0185aArr2[i9].e(d8);
                }
                c0185aArr2[i9].k(d8);
                C0185a[] c0185aArr3 = this.f21498b;
                return i8 == 0 ? c0185aArr3[i9].b() : c0185aArr3[i9].c();
            }
            i9++;
        }
    }

    @Override // l0.b
    public void g(double d8, double[] dArr) {
        C0185a[] c0185aArr = this.f21498b;
        if (d8 < c0185aArr[0].f21502c) {
            d8 = c0185aArr[0].f21502c;
        } else if (d8 > c0185aArr[c0185aArr.length - 1].f21503d) {
            d8 = c0185aArr[c0185aArr.length - 1].f21503d;
        }
        int i8 = 0;
        while (true) {
            C0185a[] c0185aArr2 = this.f21498b;
            if (i8 >= c0185aArr2.length) {
                return;
            }
            if (d8 <= c0185aArr2[i8].f21503d) {
                if (c0185aArr2[i8].f21516r) {
                    dArr[0] = c0185aArr2[i8].d(d8);
                    dArr[1] = this.f21498b[i8].e(d8);
                    return;
                }
                c0185aArr2[i8].k(d8);
                dArr[0] = this.f21498b[i8].b();
                dArr[1] = this.f21498b[i8].c();
                return;
            }
            i8++;
        }
    }

    @Override // l0.b
    public double[] h() {
        return this.f21497a;
    }
}
