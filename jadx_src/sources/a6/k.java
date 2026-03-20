package a6;

import a6.b;
import b6.l0;
import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class k implements b {

    /* renamed from: a  reason: collision with root package name */
    private final boolean f98a;

    /* renamed from: b  reason: collision with root package name */
    private final int f99b;

    /* renamed from: c  reason: collision with root package name */
    private final byte[] f100c;

    /* renamed from: d  reason: collision with root package name */
    private int f101d;

    /* renamed from: e  reason: collision with root package name */
    private int f102e;

    /* renamed from: f  reason: collision with root package name */
    private int f103f;

    /* renamed from: g  reason: collision with root package name */
    private a[] f104g;

    public k(boolean z4, int i8) {
        this(z4, i8, 0);
    }

    public k(boolean z4, int i8, int i9) {
        b6.a.a(i8 > 0);
        b6.a.a(i9 >= 0);
        this.f98a = z4;
        this.f99b = i8;
        this.f103f = i9;
        this.f104g = new a[i9 + 100];
        if (i9 <= 0) {
            this.f100c = null;
            return;
        }
        this.f100c = new byte[i9 * i8];
        for (int i10 = 0; i10 < i9; i10++) {
            this.f104g[i10] = new a(this.f100c, i10 * i8);
        }
    }

    @Override // a6.b
    public synchronized void a(b.a aVar) {
        while (aVar != null) {
            a[] aVarArr = this.f104g;
            int i8 = this.f103f;
            this.f103f = i8 + 1;
            aVarArr[i8] = aVar.a();
            this.f102e--;
            aVar = aVar.next();
        }
        notifyAll();
    }

    @Override // a6.b
    public synchronized a b() {
        a aVar;
        this.f102e++;
        int i8 = this.f103f;
        if (i8 > 0) {
            a[] aVarArr = this.f104g;
            int i9 = i8 - 1;
            this.f103f = i9;
            aVar = (a) b6.a.e(aVarArr[i9]);
            this.f104g[this.f103f] = null;
        } else {
            aVar = new a(new byte[this.f99b], 0);
            int i10 = this.f102e;
            a[] aVarArr2 = this.f104g;
            if (i10 > aVarArr2.length) {
                this.f104g = (a[]) Arrays.copyOf(aVarArr2, aVarArr2.length * 2);
            }
        }
        return aVar;
    }

    @Override // a6.b
    public synchronized void c() {
        int i8 = 0;
        int max = Math.max(0, l0.l(this.f101d, this.f99b) - this.f102e);
        int i9 = this.f103f;
        if (max >= i9) {
            return;
        }
        if (this.f100c != null) {
            int i10 = i9 - 1;
            while (i8 <= i10) {
                a aVar = (a) b6.a.e(this.f104g[i8]);
                if (aVar.f74a == this.f100c) {
                    i8++;
                } else {
                    a aVar2 = (a) b6.a.e(this.f104g[i10]);
                    if (aVar2.f74a != this.f100c) {
                        i10--;
                    } else {
                        a[] aVarArr = this.f104g;
                        aVarArr[i8] = aVar2;
                        aVarArr[i10] = aVar;
                        i10--;
                        i8++;
                    }
                }
            }
            max = Math.max(max, i8);
            if (max >= this.f103f) {
                return;
            }
        }
        Arrays.fill(this.f104g, max, this.f103f, (Object) null);
        this.f103f = max;
    }

    @Override // a6.b
    public synchronized void d(a aVar) {
        a[] aVarArr = this.f104g;
        int i8 = this.f103f;
        this.f103f = i8 + 1;
        aVarArr[i8] = aVar;
        this.f102e--;
        notifyAll();
    }

    @Override // a6.b
    public int e() {
        return this.f99b;
    }

    public synchronized int f() {
        return this.f102e * this.f99b;
    }

    public synchronized void g() {
        if (this.f98a) {
            h(0);
        }
    }

    public synchronized void h(int i8) {
        boolean z4 = i8 < this.f101d;
        this.f101d = i8;
        if (z4) {
            c();
        }
    }
}
