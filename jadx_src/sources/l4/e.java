package l4;

import b6.l0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e {

    /* renamed from: a  reason: collision with root package name */
    public int f21585a;

    /* renamed from: b  reason: collision with root package name */
    public int f21586b;

    /* renamed from: c  reason: collision with root package name */
    public int f21587c;

    /* renamed from: d  reason: collision with root package name */
    public int f21588d;

    /* renamed from: e  reason: collision with root package name */
    public int f21589e;

    /* renamed from: f  reason: collision with root package name */
    public int f21590f;

    /* renamed from: g  reason: collision with root package name */
    public int f21591g;

    /* renamed from: h  reason: collision with root package name */
    public int f21592h;

    /* renamed from: i  reason: collision with root package name */
    public int f21593i;

    /* renamed from: j  reason: collision with root package name */
    public int f21594j;

    /* renamed from: k  reason: collision with root package name */
    public long f21595k;

    /* renamed from: l  reason: collision with root package name */
    public int f21596l;

    private void b(long j8, int i8) {
        this.f21595k += j8;
        this.f21596l += i8;
    }

    public void a(long j8) {
        b(j8, 1);
    }

    public synchronized void c() {
    }

    public String toString() {
        return l0.C("DecoderCounters {\n decoderInits=%s,\n decoderReleases=%s\n queuedInputBuffers=%s\n skippedInputBuffers=%s\n renderedOutputBuffers=%s\n skippedOutputBuffers=%s\n droppedBuffers=%s\n droppedInputBuffers=%s\n maxConsecutiveDroppedBuffers=%s\n droppedToKeyframeEvents=%s\n totalVideoFrameProcessingOffsetUs=%s\n videoFrameProcessingOffsetCount=%s\n}", Integer.valueOf(this.f21585a), Integer.valueOf(this.f21586b), Integer.valueOf(this.f21587c), Integer.valueOf(this.f21588d), Integer.valueOf(this.f21589e), Integer.valueOf(this.f21590f), Integer.valueOf(this.f21591g), Integer.valueOf(this.f21592h), Integer.valueOf(this.f21593i), Integer.valueOf(this.f21594j), Long.valueOf(this.f21595k), Integer.valueOf(this.f21596l));
    }
}
