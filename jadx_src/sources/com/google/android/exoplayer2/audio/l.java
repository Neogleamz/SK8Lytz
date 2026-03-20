package com.google.android.exoplayer2.audio;

import java.nio.ShortBuffer;
import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class l {

    /* renamed from: a  reason: collision with root package name */
    private final int f9408a;

    /* renamed from: b  reason: collision with root package name */
    private final int f9409b;

    /* renamed from: c  reason: collision with root package name */
    private final float f9410c;

    /* renamed from: d  reason: collision with root package name */
    private final float f9411d;

    /* renamed from: e  reason: collision with root package name */
    private final float f9412e;

    /* renamed from: f  reason: collision with root package name */
    private final int f9413f;

    /* renamed from: g  reason: collision with root package name */
    private final int f9414g;

    /* renamed from: h  reason: collision with root package name */
    private final int f9415h;

    /* renamed from: i  reason: collision with root package name */
    private final short[] f9416i;

    /* renamed from: j  reason: collision with root package name */
    private short[] f9417j;

    /* renamed from: k  reason: collision with root package name */
    private int f9418k;

    /* renamed from: l  reason: collision with root package name */
    private short[] f9419l;

    /* renamed from: m  reason: collision with root package name */
    private int f9420m;

    /* renamed from: n  reason: collision with root package name */
    private short[] f9421n;

    /* renamed from: o  reason: collision with root package name */
    private int f9422o;

    /* renamed from: p  reason: collision with root package name */
    private int f9423p;
    private int q;

    /* renamed from: r  reason: collision with root package name */
    private int f9424r;

    /* renamed from: s  reason: collision with root package name */
    private int f9425s;

    /* renamed from: t  reason: collision with root package name */
    private int f9426t;

    /* renamed from: u  reason: collision with root package name */
    private int f9427u;

    /* renamed from: v  reason: collision with root package name */
    private int f9428v;

    public l(int i8, int i9, float f5, float f8, int i10) {
        this.f9408a = i8;
        this.f9409b = i9;
        this.f9410c = f5;
        this.f9411d = f8;
        this.f9412e = i8 / i10;
        this.f9413f = i8 / 400;
        int i11 = i8 / 65;
        this.f9414g = i11;
        int i12 = i11 * 2;
        this.f9415h = i12;
        this.f9416i = new short[i12];
        this.f9417j = new short[i12 * i9];
        this.f9419l = new short[i12 * i9];
        this.f9421n = new short[i12 * i9];
    }

    private void a(float f5, int i8) {
        int i9;
        int i10;
        if (this.f9420m == i8) {
            return;
        }
        int i11 = this.f9408a;
        int i12 = (int) (i11 / f5);
        while (true) {
            if (i12 <= 16384 && i11 <= 16384) {
                break;
            }
            i12 /= 2;
            i11 /= 2;
        }
        o(i8);
        int i13 = 0;
        while (true) {
            int i14 = this.f9422o;
            if (i13 >= i14 - 1) {
                u(i14 - 1);
                return;
            }
            while (true) {
                i9 = this.f9423p;
                int i15 = (i9 + 1) * i12;
                i10 = this.q;
                if (i15 <= i10 * i11) {
                    break;
                }
                this.f9419l = f(this.f9419l, this.f9420m, 1);
                int i16 = 0;
                while (true) {
                    int i17 = this.f9409b;
                    if (i16 < i17) {
                        this.f9419l[(this.f9420m * i17) + i16] = n(this.f9421n, (i17 * i13) + i16, i11, i12);
                        i16++;
                    }
                }
                this.q++;
                this.f9420m++;
            }
            int i18 = i9 + 1;
            this.f9423p = i18;
            if (i18 == i11) {
                this.f9423p = 0;
                b6.a.f(i10 == i12);
                this.q = 0;
            }
            i13++;
        }
    }

    private void b(float f5) {
        int w8;
        int i8 = this.f9418k;
        if (i8 < this.f9415h) {
            return;
        }
        int i9 = 0;
        do {
            if (this.f9424r > 0) {
                w8 = c(i9);
            } else {
                int g8 = g(this.f9417j, i9);
                w8 = ((double) f5) > 1.0d ? g8 + w(this.f9417j, i9, f5, g8) : m(this.f9417j, i9, f5, g8);
            }
            i9 += w8;
        } while (this.f9415h + i9 <= i8);
        v(i9);
    }

    private int c(int i8) {
        int min = Math.min(this.f9415h, this.f9424r);
        d(this.f9417j, i8, min);
        this.f9424r -= min;
        return min;
    }

    private void d(short[] sArr, int i8, int i9) {
        short[] f5 = f(this.f9419l, this.f9420m, i9);
        this.f9419l = f5;
        int i10 = this.f9409b;
        System.arraycopy(sArr, i8 * i10, f5, this.f9420m * i10, i10 * i9);
        this.f9420m += i9;
    }

    private void e(short[] sArr, int i8, int i9) {
        int i10 = this.f9415h / i9;
        int i11 = this.f9409b;
        int i12 = i9 * i11;
        int i13 = i8 * i11;
        for (int i14 = 0; i14 < i10; i14++) {
            int i15 = 0;
            for (int i16 = 0; i16 < i12; i16++) {
                i15 += sArr[(i14 * i12) + i13 + i16];
            }
            this.f9416i[i14] = (short) (i15 / i12);
        }
    }

    private short[] f(short[] sArr, int i8, int i9) {
        int length = sArr.length;
        int i10 = this.f9409b;
        int i11 = length / i10;
        return i8 + i9 <= i11 ? sArr : Arrays.copyOf(sArr, (((i11 * 3) / 2) + i9) * i10);
    }

    private int g(short[] sArr, int i8) {
        int i9;
        int i10 = this.f9408a;
        int i11 = i10 > 4000 ? i10 / 4000 : 1;
        if (this.f9409b == 1 && i11 == 1) {
            i9 = h(sArr, i8, this.f9413f, this.f9414g);
        } else {
            e(sArr, i8, i11);
            int h8 = h(this.f9416i, 0, this.f9413f / i11, this.f9414g / i11);
            if (i11 != 1) {
                int i12 = h8 * i11;
                int i13 = i11 * 4;
                int i14 = i12 - i13;
                int i15 = i12 + i13;
                int i16 = this.f9413f;
                if (i14 < i16) {
                    i14 = i16;
                }
                int i17 = this.f9414g;
                if (i15 > i17) {
                    i15 = i17;
                }
                if (this.f9409b == 1) {
                    i9 = h(sArr, i8, i14, i15);
                } else {
                    e(sArr, i8, 1);
                    i9 = h(this.f9416i, 0, i14, i15);
                }
            } else {
                i9 = h8;
            }
        }
        int i18 = q(this.f9427u, this.f9428v) ? this.f9425s : i9;
        this.f9426t = this.f9427u;
        this.f9425s = i9;
        return i18;
    }

    private int h(short[] sArr, int i8, int i9, int i10) {
        int i11 = i8 * this.f9409b;
        int i12 = 1;
        int i13 = 255;
        int i14 = 0;
        int i15 = 0;
        while (i9 <= i10) {
            int i16 = 0;
            for (int i17 = 0; i17 < i9; i17++) {
                i16 += Math.abs(sArr[i11 + i17] - sArr[(i11 + i9) + i17]);
            }
            if (i16 * i14 < i12 * i9) {
                i14 = i9;
                i12 = i16;
            }
            if (i16 * i13 > i15 * i9) {
                i13 = i9;
                i15 = i16;
            }
            i9++;
        }
        this.f9427u = i12 / i14;
        this.f9428v = i15 / i13;
        return i14;
    }

    private int m(short[] sArr, int i8, float f5, int i9) {
        int i10;
        if (f5 < 0.5f) {
            i10 = (int) ((i9 * f5) / (1.0f - f5));
        } else {
            this.f9424r = (int) ((i9 * ((2.0f * f5) - 1.0f)) / (1.0f - f5));
            i10 = i9;
        }
        int i11 = i9 + i10;
        short[] f8 = f(this.f9419l, this.f9420m, i11);
        this.f9419l = f8;
        int i12 = this.f9409b;
        System.arraycopy(sArr, i8 * i12, f8, this.f9420m * i12, i12 * i9);
        p(i10, this.f9409b, this.f9419l, this.f9420m + i9, sArr, i8 + i9, sArr, i8);
        this.f9420m += i11;
        return i10;
    }

    private short n(short[] sArr, int i8, int i9, int i10) {
        short s8 = sArr[i8];
        short s9 = sArr[i8 + this.f9409b];
        int i11 = this.q * i9;
        int i12 = this.f9423p;
        int i13 = i12 * i10;
        int i14 = (i12 + 1) * i10;
        int i15 = i14 - i11;
        int i16 = i14 - i13;
        return (short) (((s8 * i15) + ((i16 - i15) * s9)) / i16);
    }

    private void o(int i8) {
        int i9 = this.f9420m - i8;
        short[] f5 = f(this.f9421n, this.f9422o, i9);
        this.f9421n = f5;
        short[] sArr = this.f9419l;
        int i10 = this.f9409b;
        System.arraycopy(sArr, i8 * i10, f5, this.f9422o * i10, i10 * i9);
        this.f9420m = i8;
        this.f9422o += i9;
    }

    private static void p(int i8, int i9, short[] sArr, int i10, short[] sArr2, int i11, short[] sArr3, int i12) {
        for (int i13 = 0; i13 < i9; i13++) {
            int i14 = (i10 * i9) + i13;
            int i15 = (i12 * i9) + i13;
            int i16 = (i11 * i9) + i13;
            for (int i17 = 0; i17 < i8; i17++) {
                sArr[i14] = (short) (((sArr2[i16] * (i8 - i17)) + (sArr3[i15] * i17)) / i8);
                i14 += i9;
                i16 += i9;
                i15 += i9;
            }
        }
    }

    private boolean q(int i8, int i9) {
        return i8 != 0 && this.f9425s != 0 && i9 <= i8 * 3 && i8 * 2 > this.f9426t * 3;
    }

    private void r() {
        int i8 = this.f9420m;
        float f5 = this.f9410c;
        float f8 = this.f9411d;
        float f9 = f5 / f8;
        float f10 = this.f9412e * f8;
        double d8 = f9;
        if (d8 > 1.00001d || d8 < 0.99999d) {
            b(f9);
        } else {
            d(this.f9417j, 0, this.f9418k);
            this.f9418k = 0;
        }
        if (f10 != 1.0f) {
            a(f10, i8);
        }
    }

    private void u(int i8) {
        if (i8 == 0) {
            return;
        }
        short[] sArr = this.f9421n;
        int i9 = this.f9409b;
        System.arraycopy(sArr, i8 * i9, sArr, 0, (this.f9422o - i8) * i9);
        this.f9422o -= i8;
    }

    private void v(int i8) {
        int i9 = this.f9418k - i8;
        short[] sArr = this.f9417j;
        int i10 = this.f9409b;
        System.arraycopy(sArr, i8 * i10, sArr, 0, i10 * i9);
        this.f9418k = i9;
    }

    private int w(short[] sArr, int i8, float f5, int i9) {
        int i10;
        if (f5 >= 2.0f) {
            i10 = (int) (i9 / (f5 - 1.0f));
        } else {
            this.f9424r = (int) ((i9 * (2.0f - f5)) / (f5 - 1.0f));
            i10 = i9;
        }
        short[] f8 = f(this.f9419l, this.f9420m, i10);
        this.f9419l = f8;
        p(i10, this.f9409b, f8, this.f9420m, sArr, i8, sArr, i8 + i9);
        this.f9420m += i10;
        return i10;
    }

    public void i() {
        this.f9418k = 0;
        this.f9420m = 0;
        this.f9422o = 0;
        this.f9423p = 0;
        this.q = 0;
        this.f9424r = 0;
        this.f9425s = 0;
        this.f9426t = 0;
        this.f9427u = 0;
        this.f9428v = 0;
    }

    public void j(ShortBuffer shortBuffer) {
        int min = Math.min(shortBuffer.remaining() / this.f9409b, this.f9420m);
        shortBuffer.put(this.f9419l, 0, this.f9409b * min);
        int i8 = this.f9420m - min;
        this.f9420m = i8;
        short[] sArr = this.f9419l;
        int i9 = this.f9409b;
        System.arraycopy(sArr, min * i9, sArr, 0, i8 * i9);
    }

    public int k() {
        return this.f9420m * this.f9409b * 2;
    }

    public int l() {
        return this.f9418k * this.f9409b * 2;
    }

    public void s() {
        int i8;
        int i9 = this.f9418k;
        float f5 = this.f9410c;
        float f8 = this.f9411d;
        int i10 = this.f9420m + ((int) ((((i9 / (f5 / f8)) + this.f9422o) / (this.f9412e * f8)) + 0.5f));
        this.f9417j = f(this.f9417j, i9, (this.f9415h * 2) + i9);
        int i11 = 0;
        while (true) {
            i8 = this.f9415h;
            int i12 = this.f9409b;
            if (i11 >= i8 * 2 * i12) {
                break;
            }
            this.f9417j[(i12 * i9) + i11] = 0;
            i11++;
        }
        this.f9418k += i8 * 2;
        r();
        if (this.f9420m > i10) {
            this.f9420m = i10;
        }
        this.f9418k = 0;
        this.f9424r = 0;
        this.f9422o = 0;
    }

    public void t(ShortBuffer shortBuffer) {
        int remaining = shortBuffer.remaining();
        int i8 = this.f9409b;
        int i9 = remaining / i8;
        short[] f5 = f(this.f9417j, this.f9418k, i9);
        this.f9417j = f5;
        shortBuffer.get(f5, this.f9418k * this.f9409b, ((i8 * i9) * 2) / 2);
        this.f9418k += i9;
        r();
    }
}
