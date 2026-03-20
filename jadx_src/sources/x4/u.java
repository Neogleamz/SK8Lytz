package x4;

import b6.a;
import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class u {

    /* renamed from: a  reason: collision with root package name */
    private final int f24120a;

    /* renamed from: b  reason: collision with root package name */
    private boolean f24121b;

    /* renamed from: c  reason: collision with root package name */
    private boolean f24122c;

    /* renamed from: d  reason: collision with root package name */
    public byte[] f24123d;

    /* renamed from: e  reason: collision with root package name */
    public int f24124e;

    public u(int i8, int i9) {
        this.f24120a = i8;
        byte[] bArr = new byte[i9 + 3];
        this.f24123d = bArr;
        bArr[2] = 1;
    }

    public void a(byte[] bArr, int i8, int i9) {
        if (this.f24121b) {
            int i10 = i9 - i8;
            byte[] bArr2 = this.f24123d;
            int length = bArr2.length;
            int i11 = this.f24124e;
            if (length < i11 + i10) {
                this.f24123d = Arrays.copyOf(bArr2, (i11 + i10) * 2);
            }
            System.arraycopy(bArr, i8, this.f24123d, this.f24124e, i10);
            this.f24124e += i10;
        }
    }

    public boolean b(int i8) {
        if (this.f24121b) {
            this.f24124e -= i8;
            this.f24121b = false;
            this.f24122c = true;
            return true;
        }
        return false;
    }

    public boolean c() {
        return this.f24122c;
    }

    public void d() {
        this.f24121b = false;
        this.f24122c = false;
    }

    public void e(int i8) {
        a.f(!this.f24121b);
        boolean z4 = i8 == this.f24120a;
        this.f24121b = z4;
        if (z4) {
            this.f24124e = 3;
            this.f24122c = false;
        }
    }
}
