package b6;

import com.daimajia.numberprogressbar.BuildConfig;
import com.google.common.collect.ImmutableSet;
import java.nio.charset.Charset;
import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class z {

    /* renamed from: d  reason: collision with root package name */
    private static final char[] f8156d = {'\r', '\n'};

    /* renamed from: e  reason: collision with root package name */
    private static final char[] f8157e = {'\n'};

    /* renamed from: f  reason: collision with root package name */
    private static final ImmutableSet<Charset> f8158f = ImmutableSet.M(com.google.common.base.e.f18815a, com.google.common.base.e.f18817c, com.google.common.base.e.f18820f, com.google.common.base.e.f18818d, com.google.common.base.e.f18819e);

    /* renamed from: a  reason: collision with root package name */
    private byte[] f8159a;

    /* renamed from: b  reason: collision with root package name */
    private int f8160b;

    /* renamed from: c  reason: collision with root package name */
    private int f8161c;

    public z() {
        this.f8159a = l0.f8068f;
    }

    public z(int i8) {
        this.f8159a = new byte[i8];
        this.f8161c = i8;
    }

    public z(byte[] bArr) {
        this.f8159a = bArr;
        this.f8161c = bArr.length;
    }

    public z(byte[] bArr, int i8) {
        this.f8159a = bArr;
        this.f8161c = i8;
    }

    private void W(Charset charset) {
        if (m(charset, f8156d) == '\r') {
            m(charset, f8157e);
        }
    }

    private int d(Charset charset) {
        int i8;
        if (charset.equals(com.google.common.base.e.f18817c) || charset.equals(com.google.common.base.e.f18815a)) {
            i8 = 1;
        } else if (!charset.equals(com.google.common.base.e.f18820f) && !charset.equals(com.google.common.base.e.f18819e) && !charset.equals(com.google.common.base.e.f18818d)) {
            throw new IllegalArgumentException("Unsupported charset: " + charset);
        } else {
            i8 = 2;
        }
        int i9 = this.f8160b;
        while (true) {
            int i10 = this.f8161c;
            if (i9 >= i10 - (i8 - 1)) {
                return i10;
            }
            if ((charset.equals(com.google.common.base.e.f18817c) || charset.equals(com.google.common.base.e.f18815a)) && l0.v0(this.f8159a[i9])) {
                return i9;
            }
            if (charset.equals(com.google.common.base.e.f18820f) || charset.equals(com.google.common.base.e.f18818d)) {
                byte[] bArr = this.f8159a;
                if (bArr[i9] == 0 && l0.v0(bArr[i9 + 1])) {
                    return i9;
                }
            }
            if (charset.equals(com.google.common.base.e.f18819e)) {
                byte[] bArr2 = this.f8159a;
                if (bArr2[i9 + 1] == 0 && l0.v0(bArr2[i9])) {
                    return i9;
                }
            }
            i9 += i8;
        }
    }

    private int i(Charset charset) {
        byte a9;
        char c9;
        int i8 = 2;
        if ((charset.equals(com.google.common.base.e.f18817c) || charset.equals(com.google.common.base.e.f18815a)) && a() >= 1) {
            a9 = (byte) com.google.common.primitives.b.a(com.google.common.primitives.k.a(this.f8159a[this.f8160b]));
            i8 = 1;
        } else {
            if ((charset.equals(com.google.common.base.e.f18820f) || charset.equals(com.google.common.base.e.f18818d)) && a() >= 2) {
                byte[] bArr = this.f8159a;
                int i9 = this.f8160b;
                c9 = com.google.common.primitives.b.c(bArr[i9], bArr[i9 + 1]);
            } else if (!charset.equals(com.google.common.base.e.f18819e) || a() < 2) {
                return 0;
            } else {
                byte[] bArr2 = this.f8159a;
                int i10 = this.f8160b;
                c9 = com.google.common.primitives.b.c(bArr2[i10 + 1], bArr2[i10]);
            }
            a9 = (byte) c9;
        }
        return (com.google.common.primitives.b.a(a9) << 16) + i8;
    }

    private char m(Charset charset, char[] cArr) {
        int i8 = i(charset);
        if (i8 != 0) {
            char c9 = (char) (i8 >> 16);
            if (com.google.common.primitives.b.b(cArr, c9)) {
                this.f8160b += i8 & 65535;
                return c9;
            }
            return (char) 0;
        }
        return (char) 0;
    }

    public long A() {
        byte[] bArr = this.f8159a;
        int i8 = this.f8160b;
        int i9 = i8 + 1;
        this.f8160b = i9;
        int i10 = i9 + 1;
        this.f8160b = i10;
        int i11 = i10 + 1;
        this.f8160b = i11;
        int i12 = i11 + 1;
        this.f8160b = i12;
        int i13 = i12 + 1;
        this.f8160b = i13;
        int i14 = i13 + 1;
        this.f8160b = i14;
        int i15 = i14 + 1;
        this.f8160b = i15;
        this.f8160b = i15 + 1;
        return ((bArr[i8] & 255) << 56) | ((bArr[i9] & 255) << 48) | ((bArr[i10] & 255) << 40) | ((bArr[i11] & 255) << 32) | ((bArr[i12] & 255) << 24) | ((bArr[i13] & 255) << 16) | ((bArr[i14] & 255) << 8) | (bArr[i15] & 255);
    }

    public String B() {
        return n((char) 0);
    }

    public String C(int i8) {
        if (i8 == 0) {
            return BuildConfig.FLAVOR;
        }
        int i9 = this.f8160b;
        int i10 = (i9 + i8) - 1;
        String E = l0.E(this.f8159a, i9, (i10 >= this.f8161c || this.f8159a[i10] != 0) ? i8 : i8 - 1);
        this.f8160b += i8;
        return E;
    }

    public short D() {
        byte[] bArr = this.f8159a;
        int i8 = this.f8160b;
        int i9 = i8 + 1;
        this.f8160b = i9;
        this.f8160b = i9 + 1;
        return (short) ((bArr[i9] & 255) | ((bArr[i8] & 255) << 8));
    }

    public String E(int i8) {
        return F(i8, com.google.common.base.e.f18817c);
    }

    public String F(int i8, Charset charset) {
        String str = new String(this.f8159a, this.f8160b, i8, charset);
        this.f8160b += i8;
        return str;
    }

    public int G() {
        return (H() << 21) | (H() << 14) | (H() << 7) | H();
    }

    public int H() {
        byte[] bArr = this.f8159a;
        int i8 = this.f8160b;
        this.f8160b = i8 + 1;
        return bArr[i8] & 255;
    }

    public int I() {
        byte[] bArr = this.f8159a;
        int i8 = this.f8160b;
        int i9 = i8 + 1;
        this.f8160b = i9;
        int i10 = i9 + 1;
        this.f8160b = i10;
        int i11 = (bArr[i9] & 255) | ((bArr[i8] & 255) << 8);
        this.f8160b = i10 + 2;
        return i11;
    }

    public long J() {
        byte[] bArr = this.f8159a;
        int i8 = this.f8160b;
        int i9 = i8 + 1;
        this.f8160b = i9;
        int i10 = i9 + 1;
        this.f8160b = i10;
        int i11 = i10 + 1;
        this.f8160b = i11;
        this.f8160b = i11 + 1;
        return ((bArr[i8] & 255) << 24) | ((bArr[i9] & 255) << 16) | ((bArr[i10] & 255) << 8) | (bArr[i11] & 255);
    }

    public int K() {
        byte[] bArr = this.f8159a;
        int i8 = this.f8160b;
        int i9 = i8 + 1;
        this.f8160b = i9;
        int i10 = i9 + 1;
        this.f8160b = i10;
        int i11 = ((bArr[i8] & 255) << 16) | ((bArr[i9] & 255) << 8);
        this.f8160b = i10 + 1;
        return (bArr[i10] & 255) | i11;
    }

    public int L() {
        int q = q();
        if (q >= 0) {
            return q;
        }
        throw new IllegalStateException("Top bit not zero: " + q);
    }

    public long M() {
        long A = A();
        if (A >= 0) {
            return A;
        }
        throw new IllegalStateException("Top bit not zero: " + A);
    }

    public int N() {
        byte[] bArr = this.f8159a;
        int i8 = this.f8160b;
        int i9 = i8 + 1;
        this.f8160b = i9;
        this.f8160b = i9 + 1;
        return (bArr[i9] & 255) | ((bArr[i8] & 255) << 8);
    }

    public long O() {
        int i8;
        int i9;
        byte b9;
        int i10;
        long j8 = this.f8159a[this.f8160b];
        int i11 = 7;
        while (true) {
            if (i11 < 0) {
                break;
            }
            if (((1 << i11) & j8) != 0) {
                i11--;
            } else if (i11 < 6) {
                j8 &= i10 - 1;
                i9 = 7 - i11;
            } else if (i11 == 7) {
                i9 = 1;
            }
        }
        i9 = 0;
        if (i9 == 0) {
            throw new NumberFormatException("Invalid UTF-8 sequence first byte: " + j8);
        }
        for (i8 = 1; i8 < i9; i8++) {
            if ((this.f8159a[this.f8160b + i8] & 192) != 128) {
                throw new NumberFormatException("Invalid UTF-8 sequence continuation byte: " + j8);
            }
            j8 = (j8 << 6) | (b9 & 63);
        }
        this.f8160b += i9;
        return j8;
    }

    public Charset P() {
        if (a() >= 3) {
            byte[] bArr = this.f8159a;
            int i8 = this.f8160b;
            if (bArr[i8] == -17 && bArr[i8 + 1] == -69 && bArr[i8 + 2] == -65) {
                this.f8160b = i8 + 3;
                return com.google.common.base.e.f18817c;
            }
        }
        if (a() >= 2) {
            byte[] bArr2 = this.f8159a;
            int i9 = this.f8160b;
            if (bArr2[i9] == -2 && bArr2[i9 + 1] == -1) {
                this.f8160b = i9 + 2;
                return com.google.common.base.e.f18818d;
            } else if (bArr2[i9] == -1 && bArr2[i9 + 1] == -2) {
                this.f8160b = i9 + 2;
                return com.google.common.base.e.f18819e;
            } else {
                return null;
            }
        }
        return null;
    }

    public void Q(int i8) {
        S(b() < i8 ? new byte[i8] : this.f8159a, i8);
    }

    public void R(byte[] bArr) {
        S(bArr, bArr.length);
    }

    public void S(byte[] bArr, int i8) {
        this.f8159a = bArr;
        this.f8161c = i8;
        this.f8160b = 0;
    }

    public void T(int i8) {
        a.a(i8 >= 0 && i8 <= this.f8159a.length);
        this.f8161c = i8;
    }

    public void U(int i8) {
        a.a(i8 >= 0 && i8 <= this.f8161c);
        this.f8160b = i8;
    }

    public void V(int i8) {
        U(this.f8160b + i8);
    }

    public int a() {
        return this.f8161c - this.f8160b;
    }

    public int b() {
        return this.f8159a.length;
    }

    public void c(int i8) {
        if (i8 > b()) {
            this.f8159a = Arrays.copyOf(this.f8159a, i8);
        }
    }

    public byte[] e() {
        return this.f8159a;
    }

    public int f() {
        return this.f8160b;
    }

    public int g() {
        return this.f8161c;
    }

    public char h(Charset charset) {
        boolean contains = f8158f.contains(charset);
        a.b(contains, "Unsupported charset: " + charset);
        return (char) (i(charset) >> 16);
    }

    public int j() {
        return this.f8159a[this.f8160b] & 255;
    }

    public void k(y yVar, int i8) {
        l(yVar.f8152a, 0, i8);
        yVar.p(0);
    }

    public void l(byte[] bArr, int i8, int i9) {
        System.arraycopy(this.f8159a, this.f8160b, bArr, i8, i9);
        this.f8160b += i9;
    }

    public String n(char c9) {
        if (a() == 0) {
            return null;
        }
        int i8 = this.f8160b;
        while (i8 < this.f8161c && this.f8159a[i8] != c9) {
            i8++;
        }
        byte[] bArr = this.f8159a;
        int i9 = this.f8160b;
        String E = l0.E(bArr, i9, i8 - i9);
        this.f8160b = i8;
        if (i8 < this.f8161c) {
            this.f8160b = i8 + 1;
        }
        return E;
    }

    public double o() {
        return Double.longBitsToDouble(A());
    }

    public float p() {
        return Float.intBitsToFloat(q());
    }

    public int q() {
        byte[] bArr = this.f8159a;
        int i8 = this.f8160b;
        int i9 = i8 + 1;
        this.f8160b = i9;
        int i10 = i9 + 1;
        this.f8160b = i10;
        int i11 = ((bArr[i8] & 255) << 24) | ((bArr[i9] & 255) << 16);
        int i12 = i10 + 1;
        this.f8160b = i12;
        int i13 = i11 | ((bArr[i10] & 255) << 8);
        this.f8160b = i12 + 1;
        return (bArr[i12] & 255) | i13;
    }

    public int r() {
        byte[] bArr = this.f8159a;
        int i8 = this.f8160b;
        int i9 = i8 + 1;
        this.f8160b = i9;
        int i10 = i9 + 1;
        this.f8160b = i10;
        int i11 = (((bArr[i8] & 255) << 24) >> 8) | ((bArr[i9] & 255) << 8);
        this.f8160b = i10 + 1;
        return (bArr[i10] & 255) | i11;
    }

    public String s() {
        return t(com.google.common.base.e.f18817c);
    }

    public String t(Charset charset) {
        boolean contains = f8158f.contains(charset);
        a.b(contains, "Unsupported charset: " + charset);
        if (a() == 0) {
            return null;
        }
        if (!charset.equals(com.google.common.base.e.f18815a)) {
            P();
        }
        String F = F(d(charset) - this.f8160b, charset);
        if (this.f8160b == this.f8161c) {
            return F;
        }
        W(charset);
        return F;
    }

    public int u() {
        byte[] bArr = this.f8159a;
        int i8 = this.f8160b;
        int i9 = i8 + 1;
        this.f8160b = i9;
        int i10 = i9 + 1;
        this.f8160b = i10;
        int i11 = (bArr[i8] & 255) | ((bArr[i9] & 255) << 8);
        int i12 = i10 + 1;
        this.f8160b = i12;
        int i13 = i11 | ((bArr[i10] & 255) << 16);
        this.f8160b = i12 + 1;
        return ((bArr[i12] & 255) << 24) | i13;
    }

    public long v() {
        byte[] bArr = this.f8159a;
        int i8 = this.f8160b;
        int i9 = i8 + 1;
        this.f8160b = i9;
        int i10 = i9 + 1;
        this.f8160b = i10;
        int i11 = i10 + 1;
        this.f8160b = i11;
        int i12 = i11 + 1;
        this.f8160b = i12;
        int i13 = i12 + 1;
        this.f8160b = i13;
        int i14 = i13 + 1;
        this.f8160b = i14;
        int i15 = i14 + 1;
        this.f8160b = i15;
        this.f8160b = i15 + 1;
        return (bArr[i8] & 255) | ((bArr[i9] & 255) << 8) | ((bArr[i10] & 255) << 16) | ((bArr[i11] & 255) << 24) | ((bArr[i12] & 255) << 32) | ((bArr[i13] & 255) << 40) | ((bArr[i14] & 255) << 48) | ((bArr[i15] & 255) << 56);
    }

    public short w() {
        byte[] bArr = this.f8159a;
        int i8 = this.f8160b;
        int i9 = i8 + 1;
        this.f8160b = i9;
        this.f8160b = i9 + 1;
        return (short) (((bArr[i9] & 255) << 8) | (bArr[i8] & 255));
    }

    public long x() {
        byte[] bArr = this.f8159a;
        int i8 = this.f8160b;
        int i9 = i8 + 1;
        this.f8160b = i9;
        int i10 = i9 + 1;
        this.f8160b = i10;
        int i11 = i10 + 1;
        this.f8160b = i11;
        this.f8160b = i11 + 1;
        return (bArr[i8] & 255) | ((bArr[i9] & 255) << 8) | ((bArr[i10] & 255) << 16) | ((bArr[i11] & 255) << 24);
    }

    public int y() {
        int u8 = u();
        if (u8 >= 0) {
            return u8;
        }
        throw new IllegalStateException("Top bit not zero: " + u8);
    }

    public int z() {
        byte[] bArr = this.f8159a;
        int i8 = this.f8160b;
        int i9 = i8 + 1;
        this.f8160b = i9;
        this.f8160b = i9 + 1;
        return ((bArr[i9] & 255) << 8) | (bArr[i8] & 255);
    }
}
