package b6;

import com.google.android.libraries.barhopper.RecognitionOptions;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a0 {

    /* renamed from: a  reason: collision with root package name */
    private byte[] f8014a;

    /* renamed from: b  reason: collision with root package name */
    private int f8015b;

    /* renamed from: c  reason: collision with root package name */
    private int f8016c;

    /* renamed from: d  reason: collision with root package name */
    private int f8017d;

    public a0(byte[] bArr, int i8, int i9) {
        i(bArr, i8, i9);
    }

    private void a() {
        int i8;
        int i9 = this.f8016c;
        a.f(i9 >= 0 && (i9 < (i8 = this.f8015b) || (i9 == i8 && this.f8017d == 0)));
    }

    private int f() {
        int i8 = 0;
        while (!d()) {
            i8++;
        }
        return ((1 << i8) - 1) + (i8 > 0 ? e(i8) : 0);
    }

    private boolean j(int i8) {
        if (2 <= i8 && i8 < this.f8015b) {
            byte[] bArr = this.f8014a;
            if (bArr[i8] == 3 && bArr[i8 - 2] == 0 && bArr[i8 - 1] == 0) {
                return true;
            }
        }
        return false;
    }

    public boolean b(int i8) {
        int i9 = this.f8016c;
        int i10 = i8 / 8;
        int i11 = i9 + i10;
        int i12 = (this.f8017d + i8) - (i10 * 8);
        if (i12 > 7) {
            i11++;
            i12 -= 8;
        }
        while (true) {
            i9++;
            if (i9 > i11 || i11 >= this.f8015b) {
                break;
            } else if (j(i9)) {
                i11++;
                i9 += 2;
            }
        }
        int i13 = this.f8015b;
        if (i11 >= i13) {
            return i11 == i13 && i12 == 0;
        }
        return true;
    }

    public boolean c() {
        int i8 = this.f8016c;
        int i9 = this.f8017d;
        int i10 = 0;
        while (this.f8016c < this.f8015b && !d()) {
            i10++;
        }
        boolean z4 = this.f8016c == this.f8015b;
        this.f8016c = i8;
        this.f8017d = i9;
        return !z4 && b((i10 * 2) + 1);
    }

    public boolean d() {
        boolean z4 = (this.f8014a[this.f8016c] & (RecognitionOptions.ITF >> this.f8017d)) != 0;
        k();
        return z4;
    }

    public int e(int i8) {
        int i9;
        this.f8017d += i8;
        int i10 = 0;
        while (true) {
            i9 = this.f8017d;
            if (i9 <= 8) {
                break;
            }
            int i11 = i9 - 8;
            this.f8017d = i11;
            byte[] bArr = this.f8014a;
            int i12 = this.f8016c;
            i10 |= (bArr[i12] & 255) << i11;
            if (!j(i12 + 1)) {
                r3 = 1;
            }
            this.f8016c = i12 + r3;
        }
        byte[] bArr2 = this.f8014a;
        int i13 = this.f8016c;
        int i14 = ((-1) >>> (32 - i8)) & (i10 | ((bArr2[i13] & 255) >> (8 - i9)));
        if (i9 == 8) {
            this.f8017d = 0;
            this.f8016c = i13 + (j(i13 + 1) ? 2 : 1);
        }
        a();
        return i14;
    }

    public int g() {
        int f5 = f();
        return (f5 % 2 == 0 ? -1 : 1) * ((f5 + 1) / 2);
    }

    public int h() {
        return f();
    }

    public void i(byte[] bArr, int i8, int i9) {
        this.f8014a = bArr;
        this.f8016c = i8;
        this.f8015b = i9;
        this.f8017d = 0;
        a();
    }

    public void k() {
        int i8 = this.f8017d + 1;
        this.f8017d = i8;
        if (i8 == 8) {
            this.f8017d = 0;
            int i9 = this.f8016c;
            this.f8016c = i9 + (j(i9 + 1) ? 2 : 1);
        }
        a();
    }

    public void l(int i8) {
        int i9 = this.f8016c;
        int i10 = i8 / 8;
        int i11 = i9 + i10;
        this.f8016c = i11;
        int i12 = this.f8017d + (i8 - (i10 * 8));
        this.f8017d = i12;
        if (i12 > 7) {
            this.f8016c = i11 + 1;
            this.f8017d = i12 - 8;
        }
        while (true) {
            i9++;
            if (i9 > this.f8016c) {
                a();
                return;
            } else if (j(i9)) {
                this.f8016c++;
                i9 += 2;
            }
        }
    }
}
