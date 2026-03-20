package com.google.android.exoplayer2.source;

import a6.b;
import b6.l0;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.source.v;
import java.io.EOFException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import n4.b0;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class t {

    /* renamed from: a  reason: collision with root package name */
    private final a6.b f10791a;

    /* renamed from: b  reason: collision with root package name */
    private final int f10792b;

    /* renamed from: c  reason: collision with root package name */
    private final b6.z f10793c;

    /* renamed from: d  reason: collision with root package name */
    private a f10794d;

    /* renamed from: e  reason: collision with root package name */
    private a f10795e;

    /* renamed from: f  reason: collision with root package name */
    private a f10796f;

    /* renamed from: g  reason: collision with root package name */
    private long f10797g;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a implements b.a {

        /* renamed from: a  reason: collision with root package name */
        public long f10798a;

        /* renamed from: b  reason: collision with root package name */
        public long f10799b;

        /* renamed from: c  reason: collision with root package name */
        public a6.a f10800c;

        /* renamed from: d  reason: collision with root package name */
        public a f10801d;

        public a(long j8, int i8) {
            d(j8, i8);
        }

        @Override // a6.b.a
        public a6.a a() {
            return (a6.a) b6.a.e(this.f10800c);
        }

        public a b() {
            this.f10800c = null;
            a aVar = this.f10801d;
            this.f10801d = null;
            return aVar;
        }

        public void c(a6.a aVar, a aVar2) {
            this.f10800c = aVar;
            this.f10801d = aVar2;
        }

        public void d(long j8, int i8) {
            b6.a.f(this.f10800c == null);
            this.f10798a = j8;
            this.f10799b = j8 + i8;
        }

        public int e(long j8) {
            return ((int) (j8 - this.f10798a)) + this.f10800c.f75b;
        }

        @Override // a6.b.a
        public b.a next() {
            a aVar = this.f10801d;
            if (aVar == null || aVar.f10800c == null) {
                return null;
            }
            return aVar;
        }
    }

    public t(a6.b bVar) {
        this.f10791a = bVar;
        int e8 = bVar.e();
        this.f10792b = e8;
        this.f10793c = new b6.z(32);
        a aVar = new a(0L, e8);
        this.f10794d = aVar;
        this.f10795e = aVar;
        this.f10796f = aVar;
    }

    private void a(a aVar) {
        if (aVar.f10800c == null) {
            return;
        }
        this.f10791a.a(aVar);
        aVar.b();
    }

    private static a d(a aVar, long j8) {
        while (j8 >= aVar.f10799b) {
            aVar = aVar.f10801d;
        }
        return aVar;
    }

    private void g(int i8) {
        long j8 = this.f10797g + i8;
        this.f10797g = j8;
        a aVar = this.f10796f;
        if (j8 == aVar.f10799b) {
            this.f10796f = aVar.f10801d;
        }
    }

    private int h(int i8) {
        a aVar = this.f10796f;
        if (aVar.f10800c == null) {
            aVar.c(this.f10791a.b(), new a(this.f10796f.f10799b, this.f10792b));
        }
        return Math.min(i8, (int) (this.f10796f.f10799b - this.f10797g));
    }

    private static a i(a aVar, long j8, ByteBuffer byteBuffer, int i8) {
        a d8 = d(aVar, j8);
        while (i8 > 0) {
            int min = Math.min(i8, (int) (d8.f10799b - j8));
            byteBuffer.put(d8.f10800c.f74a, d8.e(j8), min);
            i8 -= min;
            j8 += min;
            if (j8 == d8.f10799b) {
                d8 = d8.f10801d;
            }
        }
        return d8;
    }

    private static a j(a aVar, long j8, byte[] bArr, int i8) {
        a d8 = d(aVar, j8);
        int i9 = i8;
        while (i9 > 0) {
            int min = Math.min(i9, (int) (d8.f10799b - j8));
            System.arraycopy(d8.f10800c.f74a, d8.e(j8), bArr, i8 - i9, min);
            i9 -= min;
            j8 += min;
            if (j8 == d8.f10799b) {
                d8 = d8.f10801d;
            }
        }
        return d8;
    }

    private static a k(a aVar, DecoderInputBuffer decoderInputBuffer, v.b bVar, b6.z zVar) {
        long j8 = bVar.f10829b;
        int i8 = 1;
        zVar.Q(1);
        a j9 = j(aVar, j8, zVar.e(), 1);
        long j10 = j8 + 1;
        byte b9 = zVar.e()[0];
        boolean z4 = (b9 & 128) != 0;
        int i9 = b9 & Byte.MAX_VALUE;
        l4.c cVar = decoderInputBuffer.f9511b;
        byte[] bArr = cVar.f21573a;
        if (bArr == null) {
            cVar.f21573a = new byte[16];
        } else {
            Arrays.fill(bArr, (byte) 0);
        }
        a j11 = j(j9, j10, cVar.f21573a, i9);
        long j12 = j10 + i9;
        if (z4) {
            zVar.Q(2);
            j11 = j(j11, j12, zVar.e(), 2);
            j12 += 2;
            i8 = zVar.N();
        }
        int i10 = i8;
        int[] iArr = cVar.f21576d;
        if (iArr == null || iArr.length < i10) {
            iArr = new int[i10];
        }
        int[] iArr2 = iArr;
        int[] iArr3 = cVar.f21577e;
        if (iArr3 == null || iArr3.length < i10) {
            iArr3 = new int[i10];
        }
        int[] iArr4 = iArr3;
        if (z4) {
            int i11 = i10 * 6;
            zVar.Q(i11);
            j11 = j(j11, j12, zVar.e(), i11);
            j12 += i11;
            zVar.U(0);
            for (int i12 = 0; i12 < i10; i12++) {
                iArr2[i12] = zVar.N();
                iArr4[i12] = zVar.L();
            }
        } else {
            iArr2[0] = 0;
            iArr4[0] = bVar.f10828a - ((int) (j12 - bVar.f10829b));
        }
        b0.a aVar2 = (b0.a) l0.j(bVar.f10830c);
        cVar.c(i10, iArr2, iArr4, aVar2.f22049b, cVar.f21573a, aVar2.f22048a, aVar2.f22050c, aVar2.f22051d);
        long j13 = bVar.f10829b;
        int i13 = (int) (j12 - j13);
        bVar.f10829b = j13 + i13;
        bVar.f10828a -= i13;
        return j11;
    }

    private static a l(a aVar, DecoderInputBuffer decoderInputBuffer, v.b bVar, b6.z zVar) {
        long j8;
        ByteBuffer byteBuffer;
        if (decoderInputBuffer.B()) {
            aVar = k(aVar, decoderInputBuffer, bVar, zVar);
        }
        if (decoderInputBuffer.r()) {
            zVar.Q(4);
            a j9 = j(aVar, bVar.f10829b, zVar.e(), 4);
            int L = zVar.L();
            bVar.f10829b += 4;
            bVar.f10828a -= 4;
            decoderInputBuffer.z(L);
            aVar = i(j9, bVar.f10829b, decoderInputBuffer.f9512c, L);
            bVar.f10829b += L;
            int i8 = bVar.f10828a - L;
            bVar.f10828a = i8;
            decoderInputBuffer.D(i8);
            j8 = bVar.f10829b;
            byteBuffer = decoderInputBuffer.f9515f;
        } else {
            decoderInputBuffer.z(bVar.f10828a);
            j8 = bVar.f10829b;
            byteBuffer = decoderInputBuffer.f9512c;
        }
        return i(aVar, j8, byteBuffer, bVar.f10828a);
    }

    public void b(long j8) {
        a aVar;
        if (j8 == -1) {
            return;
        }
        while (true) {
            aVar = this.f10794d;
            if (j8 < aVar.f10799b) {
                break;
            }
            this.f10791a.d(aVar.f10800c);
            this.f10794d = this.f10794d.b();
        }
        if (this.f10795e.f10798a < aVar.f10798a) {
            this.f10795e = aVar;
        }
    }

    public void c(long j8) {
        b6.a.a(j8 <= this.f10797g);
        this.f10797g = j8;
        if (j8 != 0) {
            a aVar = this.f10794d;
            if (j8 != aVar.f10798a) {
                while (this.f10797g > aVar.f10799b) {
                    aVar = aVar.f10801d;
                }
                a aVar2 = (a) b6.a.e(aVar.f10801d);
                a(aVar2);
                a aVar3 = new a(aVar.f10799b, this.f10792b);
                aVar.f10801d = aVar3;
                if (this.f10797g == aVar.f10799b) {
                    aVar = aVar3;
                }
                this.f10796f = aVar;
                if (this.f10795e == aVar2) {
                    this.f10795e = aVar3;
                    return;
                }
                return;
            }
        }
        a(this.f10794d);
        a aVar4 = new a(this.f10797g, this.f10792b);
        this.f10794d = aVar4;
        this.f10795e = aVar4;
        this.f10796f = aVar4;
    }

    public long e() {
        return this.f10797g;
    }

    public void f(DecoderInputBuffer decoderInputBuffer, v.b bVar) {
        l(this.f10795e, decoderInputBuffer, bVar, this.f10793c);
    }

    public void m(DecoderInputBuffer decoderInputBuffer, v.b bVar) {
        this.f10795e = l(this.f10795e, decoderInputBuffer, bVar, this.f10793c);
    }

    public void n() {
        a(this.f10794d);
        this.f10794d.d(0L, this.f10792b);
        a aVar = this.f10794d;
        this.f10795e = aVar;
        this.f10796f = aVar;
        this.f10797g = 0L;
        this.f10791a.c();
    }

    public void o() {
        this.f10795e = this.f10794d;
    }

    public int p(a6.f fVar, int i8, boolean z4) {
        int h8 = h(i8);
        a aVar = this.f10796f;
        int read = fVar.read(aVar.f10800c.f74a, aVar.e(this.f10797g), h8);
        if (read != -1) {
            g(read);
            return read;
        } else if (z4) {
            return -1;
        } else {
            throw new EOFException();
        }
    }

    public void q(b6.z zVar, int i8) {
        while (i8 > 0) {
            int h8 = h(i8);
            a aVar = this.f10796f;
            zVar.l(aVar.f10800c.f74a, aVar.e(this.f10797g), h8);
            i8 -= h8;
            g(h8);
        }
    }
}
