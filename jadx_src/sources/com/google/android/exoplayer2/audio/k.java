package com.google.android.exoplayer2.audio;

import b6.l0;
import com.google.android.exoplayer2.audio.AudioProcessor;
import java.nio.ByteBuffer;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class k extends e {

    /* renamed from: i  reason: collision with root package name */
    private final long f9397i;

    /* renamed from: j  reason: collision with root package name */
    private final long f9398j;

    /* renamed from: k  reason: collision with root package name */
    private final short f9399k;

    /* renamed from: l  reason: collision with root package name */
    private int f9400l;

    /* renamed from: m  reason: collision with root package name */
    private boolean f9401m;

    /* renamed from: n  reason: collision with root package name */
    private byte[] f9402n;

    /* renamed from: o  reason: collision with root package name */
    private byte[] f9403o;

    /* renamed from: p  reason: collision with root package name */
    private int f9404p;
    private int q;

    /* renamed from: r  reason: collision with root package name */
    private int f9405r;

    /* renamed from: s  reason: collision with root package name */
    private boolean f9406s;

    /* renamed from: t  reason: collision with root package name */
    private long f9407t;

    public k() {
        this(150000L, 20000L, (short) 1024);
    }

    public k(long j8, long j9, short s8) {
        b6.a.a(j9 <= j8);
        this.f9397i = j8;
        this.f9398j = j9;
        this.f9399k = s8;
        byte[] bArr = l0.f8068f;
        this.f9402n = bArr;
        this.f9403o = bArr;
    }

    private int m(long j8) {
        return (int) ((j8 * this.f9370b.f9233a) / 1000000);
    }

    private int n(ByteBuffer byteBuffer) {
        int limit = byteBuffer.limit();
        while (true) {
            limit -= 2;
            if (limit < byteBuffer.position()) {
                return byteBuffer.position();
            }
            if (Math.abs((int) byteBuffer.getShort(limit)) > this.f9399k) {
                int i8 = this.f9400l;
                return ((limit / i8) * i8) + i8;
            }
        }
    }

    private int o(ByteBuffer byteBuffer) {
        for (int position = byteBuffer.position(); position < byteBuffer.limit(); position += 2) {
            if (Math.abs((int) byteBuffer.getShort(position)) > this.f9399k) {
                int i8 = this.f9400l;
                return i8 * (position / i8);
            }
        }
        return byteBuffer.limit();
    }

    private void q(ByteBuffer byteBuffer) {
        int remaining = byteBuffer.remaining();
        l(remaining).put(byteBuffer).flip();
        if (remaining > 0) {
            this.f9406s = true;
        }
    }

    private void r(byte[] bArr, int i8) {
        l(i8).put(bArr, 0, i8).flip();
        if (i8 > 0) {
            this.f9406s = true;
        }
    }

    private void s(ByteBuffer byteBuffer) {
        int limit = byteBuffer.limit();
        int o5 = o(byteBuffer);
        int position = o5 - byteBuffer.position();
        byte[] bArr = this.f9402n;
        int length = bArr.length;
        int i8 = this.q;
        int i9 = length - i8;
        if (o5 < limit && position < i9) {
            r(bArr, i8);
            this.q = 0;
            this.f9404p = 0;
            return;
        }
        int min = Math.min(position, i9);
        byteBuffer.limit(byteBuffer.position() + min);
        byteBuffer.get(this.f9402n, this.q, min);
        int i10 = this.q + min;
        this.q = i10;
        byte[] bArr2 = this.f9402n;
        if (i10 == bArr2.length) {
            if (this.f9406s) {
                r(bArr2, this.f9405r);
                this.f9407t += (this.q - (this.f9405r * 2)) / this.f9400l;
            } else {
                this.f9407t += (i10 - this.f9405r) / this.f9400l;
            }
            w(byteBuffer, this.f9402n, this.q);
            this.q = 0;
            this.f9404p = 2;
        }
        byteBuffer.limit(limit);
    }

    private void t(ByteBuffer byteBuffer) {
        int limit = byteBuffer.limit();
        byteBuffer.limit(Math.min(limit, byteBuffer.position() + this.f9402n.length));
        int n8 = n(byteBuffer);
        if (n8 == byteBuffer.position()) {
            this.f9404p = 1;
        } else {
            byteBuffer.limit(n8);
            q(byteBuffer);
        }
        byteBuffer.limit(limit);
    }

    private void u(ByteBuffer byteBuffer) {
        int limit = byteBuffer.limit();
        int o5 = o(byteBuffer);
        byteBuffer.limit(o5);
        this.f9407t += byteBuffer.remaining() / this.f9400l;
        w(byteBuffer, this.f9403o, this.f9405r);
        if (o5 < limit) {
            r(this.f9403o, this.f9405r);
            this.f9404p = 0;
            byteBuffer.limit(limit);
        }
    }

    private void w(ByteBuffer byteBuffer, byte[] bArr, int i8) {
        int min = Math.min(byteBuffer.remaining(), this.f9405r);
        int i9 = this.f9405r - min;
        System.arraycopy(bArr, i8 - i9, this.f9403o, 0, i9);
        byteBuffer.position(byteBuffer.limit() - min);
        byteBuffer.get(this.f9403o, i9, min);
    }

    @Override // com.google.android.exoplayer2.audio.e, com.google.android.exoplayer2.audio.AudioProcessor
    public boolean c() {
        return this.f9401m;
    }

    @Override // com.google.android.exoplayer2.audio.AudioProcessor
    public void e(ByteBuffer byteBuffer) {
        while (byteBuffer.hasRemaining() && !a()) {
            int i8 = this.f9404p;
            if (i8 == 0) {
                t(byteBuffer);
            } else if (i8 == 1) {
                s(byteBuffer);
            } else if (i8 != 2) {
                throw new IllegalStateException();
            } else {
                u(byteBuffer);
            }
        }
    }

    @Override // com.google.android.exoplayer2.audio.e
    public AudioProcessor.a h(AudioProcessor.a aVar) {
        if (aVar.f9235c == 2) {
            return this.f9401m ? aVar : AudioProcessor.a.f9232e;
        }
        throw new AudioProcessor.UnhandledAudioFormatException(aVar);
    }

    @Override // com.google.android.exoplayer2.audio.e
    protected void i() {
        if (this.f9401m) {
            this.f9400l = this.f9370b.f9236d;
            int m8 = m(this.f9397i) * this.f9400l;
            if (this.f9402n.length != m8) {
                this.f9402n = new byte[m8];
            }
            int m9 = m(this.f9398j) * this.f9400l;
            this.f9405r = m9;
            if (this.f9403o.length != m9) {
                this.f9403o = new byte[m9];
            }
        }
        this.f9404p = 0;
        this.f9407t = 0L;
        this.q = 0;
        this.f9406s = false;
    }

    @Override // com.google.android.exoplayer2.audio.e
    protected void j() {
        int i8 = this.q;
        if (i8 > 0) {
            r(this.f9402n, i8);
        }
        if (this.f9406s) {
            return;
        }
        this.f9407t += this.f9405r / this.f9400l;
    }

    @Override // com.google.android.exoplayer2.audio.e
    protected void k() {
        this.f9401m = false;
        this.f9405r = 0;
        byte[] bArr = l0.f8068f;
        this.f9402n = bArr;
        this.f9403o = bArr;
    }

    public long p() {
        return this.f9407t;
    }

    public void v(boolean z4) {
        this.f9401m = z4;
    }
}
