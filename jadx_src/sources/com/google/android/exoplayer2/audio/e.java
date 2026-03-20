package com.google.android.exoplayer2.audio;

import com.google.android.exoplayer2.audio.AudioProcessor;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class e implements AudioProcessor {

    /* renamed from: b  reason: collision with root package name */
    protected AudioProcessor.a f9370b;

    /* renamed from: c  reason: collision with root package name */
    protected AudioProcessor.a f9371c;

    /* renamed from: d  reason: collision with root package name */
    private AudioProcessor.a f9372d;

    /* renamed from: e  reason: collision with root package name */
    private AudioProcessor.a f9373e;

    /* renamed from: f  reason: collision with root package name */
    private ByteBuffer f9374f;

    /* renamed from: g  reason: collision with root package name */
    private ByteBuffer f9375g;

    /* renamed from: h  reason: collision with root package name */
    private boolean f9376h;

    public e() {
        ByteBuffer byteBuffer = AudioProcessor.f9231a;
        this.f9374f = byteBuffer;
        this.f9375g = byteBuffer;
        AudioProcessor.a aVar = AudioProcessor.a.f9232e;
        this.f9372d = aVar;
        this.f9373e = aVar;
        this.f9370b = aVar;
        this.f9371c = aVar;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final boolean a() {
        return this.f9375g.hasRemaining();
    }

    @Override // com.google.android.exoplayer2.audio.AudioProcessor
    public boolean b() {
        return this.f9376h && this.f9375g == AudioProcessor.f9231a;
    }

    @Override // com.google.android.exoplayer2.audio.AudioProcessor
    public boolean c() {
        return this.f9373e != AudioProcessor.a.f9232e;
    }

    @Override // com.google.android.exoplayer2.audio.AudioProcessor
    public ByteBuffer d() {
        ByteBuffer byteBuffer = this.f9375g;
        this.f9375g = AudioProcessor.f9231a;
        return byteBuffer;
    }

    @Override // com.google.android.exoplayer2.audio.AudioProcessor
    public final AudioProcessor.a f(AudioProcessor.a aVar) {
        this.f9372d = aVar;
        this.f9373e = h(aVar);
        return c() ? this.f9373e : AudioProcessor.a.f9232e;
    }

    @Override // com.google.android.exoplayer2.audio.AudioProcessor
    public final void flush() {
        this.f9375g = AudioProcessor.f9231a;
        this.f9376h = false;
        this.f9370b = this.f9372d;
        this.f9371c = this.f9373e;
        i();
    }

    @Override // com.google.android.exoplayer2.audio.AudioProcessor
    public final void g() {
        this.f9376h = true;
        j();
    }

    protected abstract AudioProcessor.a h(AudioProcessor.a aVar);

    protected void i() {
    }

    protected void j() {
    }

    protected void k() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final ByteBuffer l(int i8) {
        if (this.f9374f.capacity() < i8) {
            this.f9374f = ByteBuffer.allocateDirect(i8).order(ByteOrder.nativeOrder());
        } else {
            this.f9374f.clear();
        }
        ByteBuffer byteBuffer = this.f9374f;
        this.f9375g = byteBuffer;
        return byteBuffer;
    }

    @Override // com.google.android.exoplayer2.audio.AudioProcessor
    public final void reset() {
        flush();
        this.f9374f = AudioProcessor.f9231a;
        AudioProcessor.a aVar = AudioProcessor.a.f9232e;
        this.f9372d = aVar;
        this.f9373e = aVar;
        this.f9370b = aVar;
        this.f9371c = aVar;
        k();
    }
}
