package com.google.android.exoplayer2.audio;

import b6.l0;
import com.google.android.exoplayer2.audio.AudioProcessor;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class m implements AudioProcessor {

    /* renamed from: b  reason: collision with root package name */
    private int f9429b;

    /* renamed from: c  reason: collision with root package name */
    private float f9430c = 1.0f;

    /* renamed from: d  reason: collision with root package name */
    private float f9431d = 1.0f;

    /* renamed from: e  reason: collision with root package name */
    private AudioProcessor.a f9432e;

    /* renamed from: f  reason: collision with root package name */
    private AudioProcessor.a f9433f;

    /* renamed from: g  reason: collision with root package name */
    private AudioProcessor.a f9434g;

    /* renamed from: h  reason: collision with root package name */
    private AudioProcessor.a f9435h;

    /* renamed from: i  reason: collision with root package name */
    private boolean f9436i;

    /* renamed from: j  reason: collision with root package name */
    private l f9437j;

    /* renamed from: k  reason: collision with root package name */
    private ByteBuffer f9438k;

    /* renamed from: l  reason: collision with root package name */
    private ShortBuffer f9439l;

    /* renamed from: m  reason: collision with root package name */
    private ByteBuffer f9440m;

    /* renamed from: n  reason: collision with root package name */
    private long f9441n;

    /* renamed from: o  reason: collision with root package name */
    private long f9442o;

    /* renamed from: p  reason: collision with root package name */
    private boolean f9443p;

    public m() {
        AudioProcessor.a aVar = AudioProcessor.a.f9232e;
        this.f9432e = aVar;
        this.f9433f = aVar;
        this.f9434g = aVar;
        this.f9435h = aVar;
        ByteBuffer byteBuffer = AudioProcessor.f9231a;
        this.f9438k = byteBuffer;
        this.f9439l = byteBuffer.asShortBuffer();
        this.f9440m = byteBuffer;
        this.f9429b = -1;
    }

    public long a(long j8) {
        if (this.f9442o >= 1024) {
            long l8 = this.f9441n - ((l) b6.a.e(this.f9437j)).l();
            int i8 = this.f9435h.f9233a;
            int i9 = this.f9434g.f9233a;
            return i8 == i9 ? l0.O0(j8, l8, this.f9442o) : l0.O0(j8, l8 * i8, this.f9442o * i9);
        }
        return (long) (this.f9430c * j8);
    }

    @Override // com.google.android.exoplayer2.audio.AudioProcessor
    public boolean b() {
        l lVar;
        return this.f9443p && ((lVar = this.f9437j) == null || lVar.k() == 0);
    }

    @Override // com.google.android.exoplayer2.audio.AudioProcessor
    public boolean c() {
        return this.f9433f.f9233a != -1 && (Math.abs(this.f9430c - 1.0f) >= 1.0E-4f || Math.abs(this.f9431d - 1.0f) >= 1.0E-4f || this.f9433f.f9233a != this.f9432e.f9233a);
    }

    @Override // com.google.android.exoplayer2.audio.AudioProcessor
    public ByteBuffer d() {
        int k8;
        l lVar = this.f9437j;
        if (lVar != null && (k8 = lVar.k()) > 0) {
            if (this.f9438k.capacity() < k8) {
                ByteBuffer order = ByteBuffer.allocateDirect(k8).order(ByteOrder.nativeOrder());
                this.f9438k = order;
                this.f9439l = order.asShortBuffer();
            } else {
                this.f9438k.clear();
                this.f9439l.clear();
            }
            lVar.j(this.f9439l);
            this.f9442o += k8;
            this.f9438k.limit(k8);
            this.f9440m = this.f9438k;
        }
        ByteBuffer byteBuffer = this.f9440m;
        this.f9440m = AudioProcessor.f9231a;
        return byteBuffer;
    }

    @Override // com.google.android.exoplayer2.audio.AudioProcessor
    public void e(ByteBuffer byteBuffer) {
        if (byteBuffer.hasRemaining()) {
            ShortBuffer asShortBuffer = byteBuffer.asShortBuffer();
            int remaining = byteBuffer.remaining();
            this.f9441n += remaining;
            ((l) b6.a.e(this.f9437j)).t(asShortBuffer);
            byteBuffer.position(byteBuffer.position() + remaining);
        }
    }

    @Override // com.google.android.exoplayer2.audio.AudioProcessor
    public AudioProcessor.a f(AudioProcessor.a aVar) {
        if (aVar.f9235c == 2) {
            int i8 = this.f9429b;
            if (i8 == -1) {
                i8 = aVar.f9233a;
            }
            this.f9432e = aVar;
            AudioProcessor.a aVar2 = new AudioProcessor.a(i8, aVar.f9234b, 2);
            this.f9433f = aVar2;
            this.f9436i = true;
            return aVar2;
        }
        throw new AudioProcessor.UnhandledAudioFormatException(aVar);
    }

    @Override // com.google.android.exoplayer2.audio.AudioProcessor
    public void flush() {
        if (c()) {
            AudioProcessor.a aVar = this.f9432e;
            this.f9434g = aVar;
            AudioProcessor.a aVar2 = this.f9433f;
            this.f9435h = aVar2;
            if (this.f9436i) {
                this.f9437j = new l(aVar.f9233a, aVar.f9234b, this.f9430c, this.f9431d, aVar2.f9233a);
            } else {
                l lVar = this.f9437j;
                if (lVar != null) {
                    lVar.i();
                }
            }
        }
        this.f9440m = AudioProcessor.f9231a;
        this.f9441n = 0L;
        this.f9442o = 0L;
        this.f9443p = false;
    }

    @Override // com.google.android.exoplayer2.audio.AudioProcessor
    public void g() {
        l lVar = this.f9437j;
        if (lVar != null) {
            lVar.s();
        }
        this.f9443p = true;
    }

    public void h(float f5) {
        if (this.f9431d != f5) {
            this.f9431d = f5;
            this.f9436i = true;
        }
    }

    public void i(float f5) {
        if (this.f9430c != f5) {
            this.f9430c = f5;
            this.f9436i = true;
        }
    }

    @Override // com.google.android.exoplayer2.audio.AudioProcessor
    public void reset() {
        this.f9430c = 1.0f;
        this.f9431d = 1.0f;
        AudioProcessor.a aVar = AudioProcessor.a.f9232e;
        this.f9432e = aVar;
        this.f9433f = aVar;
        this.f9434g = aVar;
        this.f9435h = aVar;
        ByteBuffer byteBuffer = AudioProcessor.f9231a;
        this.f9438k = byteBuffer;
        this.f9439l = byteBuffer.asShortBuffer();
        this.f9440m = byteBuffer;
        this.f9429b = -1;
        this.f9436i = false;
        this.f9437j = null;
        this.f9441n = 0L;
        this.f9442o = 0L;
        this.f9443p = false;
    }
}
