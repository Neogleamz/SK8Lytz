package n4;

import a6.f;
import b6.l0;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.io.EOFException;
import java.io.InterruptedIOException;
import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e implements l {

    /* renamed from: b  reason: collision with root package name */
    private final f f22077b;

    /* renamed from: c  reason: collision with root package name */
    private final long f22078c;

    /* renamed from: d  reason: collision with root package name */
    private long f22079d;

    /* renamed from: f  reason: collision with root package name */
    private int f22081f;

    /* renamed from: g  reason: collision with root package name */
    private int f22082g;

    /* renamed from: e  reason: collision with root package name */
    private byte[] f22080e = new byte[65536];

    /* renamed from: a  reason: collision with root package name */
    private final byte[] f22076a = new byte[RecognitionOptions.AZTEC];

    static {
        i4.q.a("goog.exo.extractor");
    }

    public e(f fVar, long j8, long j9) {
        this.f22077b = fVar;
        this.f22079d = j8;
        this.f22078c = j9;
    }

    private void l(int i8) {
        if (i8 != -1) {
            this.f22079d += i8;
        }
    }

    private void m(int i8) {
        int i9 = this.f22081f + i8;
        byte[] bArr = this.f22080e;
        if (i9 > bArr.length) {
            this.f22080e = Arrays.copyOf(this.f22080e, l0.q(bArr.length * 2, 65536 + i9, i9 + 524288));
        }
    }

    private int n(byte[] bArr, int i8, int i9) {
        int i10 = this.f22082g;
        if (i10 == 0) {
            return 0;
        }
        int min = Math.min(i10, i9);
        System.arraycopy(this.f22080e, 0, bArr, i8, min);
        r(min);
        return min;
    }

    private int o(byte[] bArr, int i8, int i9, int i10, boolean z4) {
        if (Thread.interrupted()) {
            throw new InterruptedIOException();
        }
        int read = this.f22077b.read(bArr, i8 + i10, i9 - i10);
        if (read == -1) {
            if (i10 == 0 && z4) {
                return -1;
            }
            throw new EOFException();
        }
        return i10 + read;
    }

    private int p(int i8) {
        int min = Math.min(this.f22082g, i8);
        r(min);
        return min;
    }

    private void r(int i8) {
        int i9 = this.f22082g - i8;
        this.f22082g = i9;
        this.f22081f = 0;
        byte[] bArr = this.f22080e;
        byte[] bArr2 = i9 < bArr.length - 524288 ? new byte[65536 + i9] : bArr;
        System.arraycopy(bArr, i8, bArr2, 0, i9);
        this.f22080e = bArr2;
    }

    @Override // n4.l
    public int a(int i8) {
        int p8 = p(i8);
        if (p8 == 0) {
            byte[] bArr = this.f22076a;
            p8 = o(bArr, 0, Math.min(i8, bArr.length), 0, true);
        }
        l(p8);
        return p8;
    }

    @Override // n4.l
    public long b() {
        return this.f22078c;
    }

    @Override // n4.l
    public boolean c(byte[] bArr, int i8, int i9, boolean z4) {
        int n8 = n(bArr, i8, i9);
        while (n8 < i9 && n8 != -1) {
            n8 = o(bArr, i8, i9, n8, z4);
        }
        l(n8);
        return n8 != -1;
    }

    @Override // n4.l
    public boolean d(byte[] bArr, int i8, int i9, boolean z4) {
        if (j(i9, z4)) {
            System.arraycopy(this.f22080e, this.f22081f - i9, bArr, i8, i9);
            return true;
        }
        return false;
    }

    @Override // n4.l
    public long e() {
        return this.f22079d + this.f22081f;
    }

    @Override // n4.l
    public void f(int i8) {
        j(i8, false);
    }

    @Override // n4.l
    public int g(byte[] bArr, int i8, int i9) {
        int min;
        m(i9);
        int i10 = this.f22082g;
        int i11 = this.f22081f;
        int i12 = i10 - i11;
        if (i12 == 0) {
            min = o(this.f22080e, i11, i9, 0, true);
            if (min == -1) {
                return -1;
            }
            this.f22082g += min;
        } else {
            min = Math.min(i9, i12);
        }
        System.arraycopy(this.f22080e, this.f22081f, bArr, i8, min);
        this.f22081f += min;
        return min;
    }

    @Override // n4.l
    public long getPosition() {
        return this.f22079d;
    }

    @Override // n4.l
    public void h() {
        this.f22081f = 0;
    }

    @Override // n4.l
    public void i(int i8) {
        q(i8, false);
    }

    @Override // n4.l
    public boolean j(int i8, boolean z4) {
        m(i8);
        int i9 = this.f22082g - this.f22081f;
        while (i9 < i8) {
            i9 = o(this.f22080e, this.f22081f, i8, i9, z4);
            if (i9 == -1) {
                return false;
            }
            this.f22082g = this.f22081f + i9;
        }
        this.f22081f += i8;
        return true;
    }

    @Override // n4.l
    public void k(byte[] bArr, int i8, int i9) {
        d(bArr, i8, i9, false);
    }

    public boolean q(int i8, boolean z4) {
        int p8 = p(i8);
        while (p8 < i8 && p8 != -1) {
            p8 = o(this.f22076a, -p8, Math.min(i8, this.f22076a.length + p8), p8, z4);
        }
        l(p8);
        return p8 != -1;
    }

    @Override // n4.l, a6.f
    public int read(byte[] bArr, int i8, int i9) {
        int n8 = n(bArr, i8, i9);
        if (n8 == 0) {
            n8 = o(bArr, i8, i9, 0, true);
        }
        l(n8);
        return n8;
    }

    @Override // n4.l
    public void readFully(byte[] bArr, int i8, int i9) {
        c(bArr, i8, i9, false);
    }
}
