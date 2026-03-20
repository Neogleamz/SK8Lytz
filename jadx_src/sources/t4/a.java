package t4;

import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.exoplayer2.ParserException;
import java.util.ArrayDeque;
import n4.l;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class a implements c {

    /* renamed from: a  reason: collision with root package name */
    private final byte[] f22849a = new byte[8];

    /* renamed from: b  reason: collision with root package name */
    private final ArrayDeque<b> f22850b = new ArrayDeque<>();

    /* renamed from: c  reason: collision with root package name */
    private final g f22851c = new g();

    /* renamed from: d  reason: collision with root package name */
    private t4.b f22852d;

    /* renamed from: e  reason: collision with root package name */
    private int f22853e;

    /* renamed from: f  reason: collision with root package name */
    private int f22854f;

    /* renamed from: g  reason: collision with root package name */
    private long f22855g;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class b {

        /* renamed from: a  reason: collision with root package name */
        private final int f22856a;

        /* renamed from: b  reason: collision with root package name */
        private final long f22857b;

        private b(int i8, long j8) {
            this.f22856a = i8;
            this.f22857b = j8;
        }
    }

    private long c(l lVar) {
        lVar.h();
        while (true) {
            lVar.k(this.f22849a, 0, 4);
            int c9 = g.c(this.f22849a[0]);
            if (c9 != -1 && c9 <= 4) {
                int a9 = (int) g.a(this.f22849a, c9, false);
                if (this.f22852d.c(a9)) {
                    lVar.i(c9);
                    return a9;
                }
            }
            lVar.i(1);
        }
    }

    private double d(l lVar, int i8) {
        long e8 = e(lVar, i8);
        return i8 == 4 ? Float.intBitsToFloat((int) e8) : Double.longBitsToDouble(e8);
    }

    private long e(l lVar, int i8) {
        lVar.readFully(this.f22849a, 0, i8);
        long j8 = 0;
        for (int i9 = 0; i9 < i8; i9++) {
            j8 = (j8 << 8) | (this.f22849a[i9] & 255);
        }
        return j8;
    }

    private static String f(l lVar, int i8) {
        if (i8 == 0) {
            return BuildConfig.FLAVOR;
        }
        byte[] bArr = new byte[i8];
        lVar.readFully(bArr, 0, i8);
        while (i8 > 0 && bArr[i8 - 1] == 0) {
            i8--;
        }
        return new String(bArr, 0, i8);
    }

    @Override // t4.c
    public boolean a(l lVar) {
        b6.a.h(this.f22852d);
        while (true) {
            b peek = this.f22850b.peek();
            if (peek != null && lVar.getPosition() >= peek.f22857b) {
                this.f22852d.a(this.f22850b.pop().f22856a);
                return true;
            }
            if (this.f22853e == 0) {
                long d8 = this.f22851c.d(lVar, true, false, 4);
                if (d8 == -2) {
                    d8 = c(lVar);
                }
                if (d8 == -1) {
                    return false;
                }
                this.f22854f = (int) d8;
                this.f22853e = 1;
            }
            if (this.f22853e == 1) {
                this.f22855g = this.f22851c.d(lVar, false, true, 8);
                this.f22853e = 2;
            }
            int b9 = this.f22852d.b(this.f22854f);
            if (b9 != 0) {
                if (b9 == 1) {
                    long position = lVar.getPosition();
                    this.f22850b.push(new b(this.f22854f, this.f22855g + position));
                    this.f22852d.g(this.f22854f, position, this.f22855g);
                    this.f22853e = 0;
                    return true;
                } else if (b9 == 2) {
                    long j8 = this.f22855g;
                    if (j8 <= 8) {
                        this.f22852d.h(this.f22854f, e(lVar, (int) j8));
                        this.f22853e = 0;
                        return true;
                    }
                    throw ParserException.a("Invalid integer size: " + this.f22855g, null);
                } else if (b9 == 3) {
                    long j9 = this.f22855g;
                    if (j9 <= 2147483647L) {
                        this.f22852d.d(this.f22854f, f(lVar, (int) j9));
                        this.f22853e = 0;
                        return true;
                    }
                    throw ParserException.a("String element size: " + this.f22855g, null);
                } else if (b9 == 4) {
                    this.f22852d.e(this.f22854f, (int) this.f22855g, lVar);
                    this.f22853e = 0;
                    return true;
                } else if (b9 != 5) {
                    throw ParserException.a("Invalid element type " + b9, null);
                } else {
                    long j10 = this.f22855g;
                    if (j10 == 4 || j10 == 8) {
                        this.f22852d.f(this.f22854f, d(lVar, (int) j10));
                        this.f22853e = 0;
                        return true;
                    }
                    throw ParserException.a("Invalid float size: " + this.f22855g, null);
                }
            }
            lVar.i((int) this.f22855g);
            this.f22853e = 0;
        }
    }

    @Override // t4.c
    public void b(t4.b bVar) {
        this.f22852d = bVar;
    }

    @Override // t4.c
    public void reset() {
        this.f22853e = 0;
        this.f22850b.clear();
        this.f22851c.e();
    }
}
