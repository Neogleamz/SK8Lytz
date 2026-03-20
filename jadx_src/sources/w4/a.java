package w4;

import b6.l0;
import java.io.EOFException;
import java.io.IOException;
import n4.a0;
import n4.l;
import n4.n;
import n4.z;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a implements g {

    /* renamed from: a  reason: collision with root package name */
    private final f f23533a;

    /* renamed from: b  reason: collision with root package name */
    private final long f23534b;

    /* renamed from: c  reason: collision with root package name */
    private final long f23535c;

    /* renamed from: d  reason: collision with root package name */
    private final i f23536d;

    /* renamed from: e  reason: collision with root package name */
    private int f23537e;

    /* renamed from: f  reason: collision with root package name */
    private long f23538f;

    /* renamed from: g  reason: collision with root package name */
    private long f23539g;

    /* renamed from: h  reason: collision with root package name */
    private long f23540h;

    /* renamed from: i  reason: collision with root package name */
    private long f23541i;

    /* renamed from: j  reason: collision with root package name */
    private long f23542j;

    /* renamed from: k  reason: collision with root package name */
    private long f23543k;

    /* renamed from: l  reason: collision with root package name */
    private long f23544l;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public final class b implements z {
        private b() {
        }

        @Override // n4.z
        public long d() {
            return a.this.f23536d.b(a.this.f23538f);
        }

        @Override // n4.z
        public boolean h() {
            return true;
        }

        @Override // n4.z
        public z.a i(long j8) {
            return new z.a(new a0(j8, l0.r((a.this.f23534b + ((a.this.f23536d.c(j8) * (a.this.f23535c - a.this.f23534b)) / a.this.f23538f)) - 30000, a.this.f23534b, a.this.f23535c - 1)));
        }
    }

    public a(i iVar, long j8, long j9, long j10, long j11, boolean z4) {
        b6.a.a(j8 >= 0 && j9 > j8);
        this.f23536d = iVar;
        this.f23534b = j8;
        this.f23535c = j9;
        if (j10 == j9 - j8 || z4) {
            this.f23538f = j11;
            this.f23537e = 4;
        } else {
            this.f23537e = 0;
        }
        this.f23533a = new f();
    }

    private long i(l lVar) {
        if (this.f23541i == this.f23542j) {
            return -1L;
        }
        long position = lVar.getPosition();
        if (!this.f23533a.d(lVar, this.f23542j)) {
            long j8 = this.f23541i;
            if (j8 != position) {
                return j8;
            }
            throw new IOException("No ogg page can be found.");
        }
        this.f23533a.a(lVar, false);
        lVar.h();
        long j9 = this.f23540h;
        f fVar = this.f23533a;
        long j10 = fVar.f23563c;
        long j11 = j9 - j10;
        int i8 = fVar.f23568h + fVar.f23569i;
        if (0 > j11 || j11 >= 72000) {
            int i9 = (j11 > 0L ? 1 : (j11 == 0L ? 0 : -1));
            if (i9 < 0) {
                this.f23542j = position;
                this.f23544l = j10;
            } else {
                this.f23541i = lVar.getPosition() + i8;
                this.f23543k = this.f23533a.f23563c;
            }
            long j12 = this.f23542j;
            long j13 = this.f23541i;
            if (j12 - j13 < 100000) {
                this.f23542j = j13;
                return j13;
            }
            long position2 = lVar.getPosition() - (i8 * (i9 <= 0 ? 2L : 1L));
            long j14 = this.f23542j;
            long j15 = this.f23541i;
            return l0.r(position2 + ((j11 * (j14 - j15)) / (this.f23544l - this.f23543k)), j15, j14 - 1);
        }
        return -1L;
    }

    private void k(l lVar) {
        while (true) {
            this.f23533a.c(lVar);
            this.f23533a.a(lVar, false);
            f fVar = this.f23533a;
            if (fVar.f23563c > this.f23540h) {
                lVar.h();
                return;
            }
            lVar.i(fVar.f23568h + fVar.f23569i);
            this.f23541i = lVar.getPosition();
            this.f23543k = this.f23533a.f23563c;
        }
    }

    @Override // w4.g
    public long a(l lVar) {
        int i8 = this.f23537e;
        if (i8 == 0) {
            long position = lVar.getPosition();
            this.f23539g = position;
            this.f23537e = 1;
            long j8 = this.f23535c - 65307;
            if (j8 > position) {
                return j8;
            }
        } else if (i8 != 1) {
            if (i8 == 2) {
                long i9 = i(lVar);
                if (i9 != -1) {
                    return i9;
                }
                this.f23537e = 3;
            } else if (i8 != 3) {
                if (i8 == 4) {
                    return -1L;
                }
                throw new IllegalStateException();
            }
            k(lVar);
            this.f23537e = 4;
            return -(this.f23543k + 2);
        }
        this.f23538f = j(lVar);
        this.f23537e = 4;
        return this.f23539g;
    }

    @Override // w4.g
    public void c(long j8) {
        this.f23540h = l0.r(j8, 0L, this.f23538f - 1);
        this.f23537e = 2;
        this.f23541i = this.f23534b;
        this.f23542j = this.f23535c;
        this.f23543k = 0L;
        this.f23544l = this.f23538f;
    }

    @Override // w4.g
    /* renamed from: h */
    public b b() {
        if (this.f23538f != 0) {
            return new b();
        }
        return null;
    }

    long j(l lVar) {
        long j8;
        f fVar;
        this.f23533a.b();
        if (this.f23533a.c(lVar)) {
            this.f23533a.a(lVar, false);
            f fVar2 = this.f23533a;
            lVar.i(fVar2.f23568h + fVar2.f23569i);
            do {
                j8 = this.f23533a.f23563c;
                f fVar3 = this.f23533a;
                if ((fVar3.f23562b & 4) == 4 || !fVar3.c(lVar) || lVar.getPosition() >= this.f23535c || !this.f23533a.a(lVar, true)) {
                    break;
                }
                fVar = this.f23533a;
            } while (n.e(lVar, fVar.f23568h + fVar.f23569i));
            return j8;
        }
        throw new EOFException();
    }
}
