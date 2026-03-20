package w4;

import b6.z;
import java.util.Arrays;
import n4.l;
import n4.n;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class e {

    /* renamed from: a  reason: collision with root package name */
    private final f f23556a = new f();

    /* renamed from: b  reason: collision with root package name */
    private final z f23557b = new z(new byte[65025], 0);

    /* renamed from: c  reason: collision with root package name */
    private int f23558c = -1;

    /* renamed from: d  reason: collision with root package name */
    private int f23559d;

    /* renamed from: e  reason: collision with root package name */
    private boolean f23560e;

    private int a(int i8) {
        int i9;
        int i10 = 0;
        this.f23559d = 0;
        do {
            int i11 = this.f23559d;
            int i12 = i8 + i11;
            f fVar = this.f23556a;
            if (i12 >= fVar.f23567g) {
                break;
            }
            int[] iArr = fVar.f23570j;
            this.f23559d = i11 + 1;
            i9 = iArr[i11 + i8];
            i10 += i9;
        } while (i9 == 255);
        return i10;
    }

    public f b() {
        return this.f23556a;
    }

    public z c() {
        return this.f23557b;
    }

    public boolean d(l lVar) {
        int i8;
        b6.a.f(lVar != null);
        if (this.f23560e) {
            this.f23560e = false;
            this.f23557b.Q(0);
        }
        while (!this.f23560e) {
            if (this.f23558c < 0) {
                if (!this.f23556a.c(lVar) || !this.f23556a.a(lVar, true)) {
                    return false;
                }
                f fVar = this.f23556a;
                int i9 = fVar.f23568h;
                if ((fVar.f23562b & 1) == 1 && this.f23557b.g() == 0) {
                    i9 += a(0);
                    i8 = this.f23559d + 0;
                } else {
                    i8 = 0;
                }
                if (!n.e(lVar, i9)) {
                    return false;
                }
                this.f23558c = i8;
            }
            int a9 = a(this.f23558c);
            int i10 = this.f23558c + this.f23559d;
            if (a9 > 0) {
                z zVar = this.f23557b;
                zVar.c(zVar.g() + a9);
                if (!n.d(lVar, this.f23557b.e(), this.f23557b.g(), a9)) {
                    return false;
                }
                z zVar2 = this.f23557b;
                zVar2.T(zVar2.g() + a9);
                this.f23560e = this.f23556a.f23570j[i10 + (-1)] != 255;
            }
            if (i10 == this.f23556a.f23567g) {
                i10 = -1;
            }
            this.f23558c = i10;
        }
        return true;
    }

    public void e() {
        this.f23556a.b();
        this.f23557b.Q(0);
        this.f23558c = -1;
        this.f23560e = false;
    }

    public void f() {
        if (this.f23557b.e().length == 65025) {
            return;
        }
        z zVar = this.f23557b;
        zVar.S(Arrays.copyOf(zVar.e(), Math.max(65025, this.f23557b.g())), this.f23557b.g());
    }
}
