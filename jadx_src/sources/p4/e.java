package p4;

import b6.l0;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.util.Arrays;
import n4.a0;
import n4.b0;
import n4.l;
import n4.z;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e {

    /* renamed from: a  reason: collision with root package name */
    protected final b0 f22355a;

    /* renamed from: b  reason: collision with root package name */
    private final int f22356b;

    /* renamed from: c  reason: collision with root package name */
    private final int f22357c;

    /* renamed from: d  reason: collision with root package name */
    private final long f22358d;

    /* renamed from: e  reason: collision with root package name */
    private final int f22359e;

    /* renamed from: f  reason: collision with root package name */
    private int f22360f;

    /* renamed from: g  reason: collision with root package name */
    private int f22361g;

    /* renamed from: h  reason: collision with root package name */
    private int f22362h;

    /* renamed from: i  reason: collision with root package name */
    private int f22363i;

    /* renamed from: j  reason: collision with root package name */
    private int f22364j;

    /* renamed from: k  reason: collision with root package name */
    private long[] f22365k;

    /* renamed from: l  reason: collision with root package name */
    private int[] f22366l;

    public e(int i8, int i9, long j8, int i10, b0 b0Var) {
        boolean z4 = true;
        if (i9 != 1 && i9 != 2) {
            z4 = false;
        }
        b6.a.a(z4);
        this.f22358d = j8;
        this.f22359e = i10;
        this.f22355a = b0Var;
        this.f22356b = d(i8, i9 == 2 ? 1667497984 : 1651965952);
        this.f22357c = i9 == 2 ? d(i8, 1650720768) : -1;
        this.f22365k = new long[RecognitionOptions.UPC_A];
        this.f22366l = new int[RecognitionOptions.UPC_A];
    }

    private static int d(int i8, int i9) {
        return (((i8 % 10) + 48) << 8) | ((i8 / 10) + 48) | i9;
    }

    private long e(int i8) {
        return (this.f22358d * i8) / this.f22359e;
    }

    private a0 h(int i8) {
        return new a0(this.f22366l[i8] * g(), this.f22365k[i8]);
    }

    public void a() {
        this.f22362h++;
    }

    public void b(long j8) {
        if (this.f22364j == this.f22366l.length) {
            long[] jArr = this.f22365k;
            this.f22365k = Arrays.copyOf(jArr, (jArr.length * 3) / 2);
            int[] iArr = this.f22366l;
            this.f22366l = Arrays.copyOf(iArr, (iArr.length * 3) / 2);
        }
        long[] jArr2 = this.f22365k;
        int i8 = this.f22364j;
        jArr2[i8] = j8;
        this.f22366l[i8] = this.f22363i;
        this.f22364j = i8 + 1;
    }

    public void c() {
        this.f22365k = Arrays.copyOf(this.f22365k, this.f22364j);
        this.f22366l = Arrays.copyOf(this.f22366l, this.f22364j);
    }

    public long f() {
        return e(this.f22362h);
    }

    public long g() {
        return e(1);
    }

    public z.a i(long j8) {
        int g8 = (int) (j8 / g());
        int h8 = l0.h(this.f22366l, g8, true, true);
        if (this.f22366l[h8] == g8) {
            return new z.a(h(h8));
        }
        a0 h9 = h(h8);
        int i8 = h8 + 1;
        return i8 < this.f22365k.length ? new z.a(h9, h(i8)) : new z.a(h9);
    }

    public boolean j(int i8) {
        return this.f22356b == i8 || this.f22357c == i8;
    }

    public void k() {
        this.f22363i++;
    }

    public boolean l() {
        return Arrays.binarySearch(this.f22366l, this.f22362h) >= 0;
    }

    public boolean m(l lVar) {
        int i8 = this.f22361g;
        int c9 = i8 - this.f22355a.c(lVar, i8, false);
        this.f22361g = c9;
        boolean z4 = c9 == 0;
        if (z4) {
            if (this.f22360f > 0) {
                this.f22355a.d(f(), l() ? 1 : 0, this.f22360f, 0, null);
            }
            a();
        }
        return z4;
    }

    public void n(int i8) {
        this.f22360f = i8;
        this.f22361g = i8;
    }

    public void o(long j8) {
        int i8;
        if (this.f22364j == 0) {
            i8 = 0;
        } else {
            i8 = this.f22366l[l0.i(this.f22365k, j8, true, true)];
        }
        this.f22362h = i8;
    }
}
