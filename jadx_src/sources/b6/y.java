package b6;

import com.google.android.libraries.barhopper.RecognitionOptions;
import java.nio.charset.Charset;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class y {

    /* renamed from: a  reason: collision with root package name */
    public byte[] f8152a;

    /* renamed from: b  reason: collision with root package name */
    private int f8153b;

    /* renamed from: c  reason: collision with root package name */
    private int f8154c;

    /* renamed from: d  reason: collision with root package name */
    private int f8155d;

    public y() {
        this.f8152a = l0.f8068f;
    }

    public y(byte[] bArr) {
        this(bArr, bArr.length);
    }

    public y(byte[] bArr, int i8) {
        this.f8152a = bArr;
        this.f8155d = i8;
    }

    private void a() {
        int i8;
        int i9 = this.f8153b;
        a.f(i9 >= 0 && (i9 < (i8 = this.f8155d) || (i9 == i8 && this.f8154c == 0)));
    }

    public int b() {
        return ((this.f8155d - this.f8153b) * 8) - this.f8154c;
    }

    public void c() {
        if (this.f8154c == 0) {
            return;
        }
        this.f8154c = 0;
        this.f8153b++;
        a();
    }

    public int d() {
        a.f(this.f8154c == 0);
        return this.f8153b;
    }

    public int e() {
        return (this.f8153b * 8) + this.f8154c;
    }

    public void f(int i8, int i9) {
        if (i9 < 32) {
            i8 &= (1 << i9) - 1;
        }
        int min = Math.min(8 - this.f8154c, i9);
        int i10 = this.f8154c;
        int i11 = (8 - i10) - min;
        byte[] bArr = this.f8152a;
        int i12 = this.f8153b;
        bArr[i12] = (byte) (((65280 >> i10) | ((1 << i11) - 1)) & bArr[i12]);
        int i13 = i9 - min;
        bArr[i12] = (byte) (((i8 >>> i13) << i11) | bArr[i12]);
        int i14 = i12 + 1;
        while (i13 > 8) {
            this.f8152a[i14] = (byte) (i8 >>> (i13 - 8));
            i13 -= 8;
            i14++;
        }
        int i15 = 8 - i13;
        byte[] bArr2 = this.f8152a;
        bArr2[i14] = (byte) (bArr2[i14] & ((1 << i15) - 1));
        bArr2[i14] = (byte) (((i8 & ((1 << i13) - 1)) << i15) | bArr2[i14]);
        r(i9);
        a();
    }

    public boolean g() {
        boolean z4 = (this.f8152a[this.f8153b] & (RecognitionOptions.ITF >> this.f8154c)) != 0;
        q();
        return z4;
    }

    public int h(int i8) {
        int i9;
        if (i8 == 0) {
            return 0;
        }
        this.f8154c += i8;
        int i10 = 0;
        while (true) {
            i9 = this.f8154c;
            if (i9 <= 8) {
                break;
            }
            int i11 = i9 - 8;
            this.f8154c = i11;
            byte[] bArr = this.f8152a;
            int i12 = this.f8153b;
            this.f8153b = i12 + 1;
            i10 |= (bArr[i12] & 255) << i11;
        }
        byte[] bArr2 = this.f8152a;
        int i13 = this.f8153b;
        int i14 = ((-1) >>> (32 - i8)) & (i10 | ((bArr2[i13] & 255) >> (8 - i9)));
        if (i9 == 8) {
            this.f8154c = 0;
            this.f8153b = i13 + 1;
        }
        a();
        return i14;
    }

    public void i(byte[] bArr, int i8, int i9) {
        int i10 = (i9 >> 3) + i8;
        while (i8 < i10) {
            byte[] bArr2 = this.f8152a;
            int i11 = this.f8153b;
            int i12 = i11 + 1;
            this.f8153b = i12;
            byte b9 = bArr2[i11];
            int i13 = this.f8154c;
            bArr[i8] = (byte) (b9 << i13);
            bArr[i8] = (byte) (((255 & bArr2[i12]) >> (8 - i13)) | bArr[i8]);
            i8++;
        }
        int i14 = i9 & 7;
        if (i14 == 0) {
            return;
        }
        bArr[i10] = (byte) (bArr[i10] & (255 >> i14));
        int i15 = this.f8154c;
        if (i15 + i14 > 8) {
            int i16 = bArr[i10];
            byte[] bArr3 = this.f8152a;
            int i17 = this.f8153b;
            this.f8153b = i17 + 1;
            bArr[i10] = (byte) (i16 | ((bArr3[i17] & 255) << i15));
            this.f8154c = i15 - 8;
        }
        int i18 = this.f8154c + i14;
        this.f8154c = i18;
        byte[] bArr4 = this.f8152a;
        int i19 = this.f8153b;
        bArr[i10] = (byte) (((byte) (((255 & bArr4[i19]) >> (8 - i18)) << (8 - i14))) | bArr[i10]);
        if (i18 == 8) {
            this.f8154c = 0;
            this.f8153b = i19 + 1;
        }
        a();
    }

    public long j(int i8) {
        return i8 <= 32 ? l0.Y0(h(i8)) : l0.X0(h(i8 - 32), h(32));
    }

    public void k(byte[] bArr, int i8, int i9) {
        a.f(this.f8154c == 0);
        System.arraycopy(this.f8152a, this.f8153b, bArr, i8, i9);
        this.f8153b += i9;
        a();
    }

    public String l(int i8, Charset charset) {
        byte[] bArr = new byte[i8];
        k(bArr, 0, i8);
        return new String(bArr, charset);
    }

    public void m(z zVar) {
        o(zVar.e(), zVar.g());
        p(zVar.f() * 8);
    }

    public void n(byte[] bArr) {
        o(bArr, bArr.length);
    }

    public void o(byte[] bArr, int i8) {
        this.f8152a = bArr;
        this.f8153b = 0;
        this.f8154c = 0;
        this.f8155d = i8;
    }

    public void p(int i8) {
        int i9 = i8 / 8;
        this.f8153b = i9;
        this.f8154c = i8 - (i9 * 8);
        a();
    }

    public void q() {
        int i8 = this.f8154c + 1;
        this.f8154c = i8;
        if (i8 == 8) {
            this.f8154c = 0;
            this.f8153b++;
        }
        a();
    }

    public void r(int i8) {
        int i9 = i8 / 8;
        int i10 = this.f8153b + i9;
        this.f8153b = i10;
        int i11 = this.f8154c + (i8 - (i9 * 8));
        this.f8154c = i11;
        if (i11 > 7) {
            this.f8153b = i10 + 1;
            this.f8154c = i11 - 8;
        }
        a();
    }

    public void s(int i8) {
        a.f(this.f8154c == 0);
        this.f8153b += i8;
        a();
    }
}
