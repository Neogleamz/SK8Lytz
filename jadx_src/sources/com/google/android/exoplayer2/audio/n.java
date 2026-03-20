package com.google.android.exoplayer2.audio;

import b6.l0;
import com.google.android.exoplayer2.audio.AudioProcessor;
import java.nio.ByteBuffer;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class n extends e {

    /* renamed from: i  reason: collision with root package name */
    private int f9444i;

    /* renamed from: j  reason: collision with root package name */
    private int f9445j;

    /* renamed from: k  reason: collision with root package name */
    private boolean f9446k;

    /* renamed from: l  reason: collision with root package name */
    private int f9447l;

    /* renamed from: m  reason: collision with root package name */
    private byte[] f9448m = l0.f8068f;

    /* renamed from: n  reason: collision with root package name */
    private int f9449n;

    /* renamed from: o  reason: collision with root package name */
    private long f9450o;

    @Override // com.google.android.exoplayer2.audio.e, com.google.android.exoplayer2.audio.AudioProcessor
    public boolean b() {
        return super.b() && this.f9449n == 0;
    }

    @Override // com.google.android.exoplayer2.audio.e, com.google.android.exoplayer2.audio.AudioProcessor
    public ByteBuffer d() {
        int i8;
        if (super.b() && (i8 = this.f9449n) > 0) {
            l(i8).put(this.f9448m, 0, this.f9449n).flip();
            this.f9449n = 0;
        }
        return super.d();
    }

    @Override // com.google.android.exoplayer2.audio.AudioProcessor
    public void e(ByteBuffer byteBuffer) {
        int position = byteBuffer.position();
        int limit = byteBuffer.limit();
        int i8 = limit - position;
        if (i8 == 0) {
            return;
        }
        int min = Math.min(i8, this.f9447l);
        this.f9450o += min / this.f9370b.f9236d;
        this.f9447l -= min;
        byteBuffer.position(position + min);
        if (this.f9447l > 0) {
            return;
        }
        int i9 = i8 - min;
        int length = (this.f9449n + i9) - this.f9448m.length;
        ByteBuffer l8 = l(length);
        int q = l0.q(length, 0, this.f9449n);
        l8.put(this.f9448m, 0, q);
        int q8 = l0.q(length - q, 0, i9);
        byteBuffer.limit(byteBuffer.position() + q8);
        l8.put(byteBuffer);
        byteBuffer.limit(limit);
        int i10 = i9 - q8;
        int i11 = this.f9449n - q;
        this.f9449n = i11;
        byte[] bArr = this.f9448m;
        System.arraycopy(bArr, q, bArr, 0, i11);
        byteBuffer.get(this.f9448m, this.f9449n, i10);
        this.f9449n += i10;
        l8.flip();
    }

    @Override // com.google.android.exoplayer2.audio.e
    public AudioProcessor.a h(AudioProcessor.a aVar) {
        if (aVar.f9235c == 2) {
            this.f9446k = true;
            return (this.f9444i == 0 && this.f9445j == 0) ? AudioProcessor.a.f9232e : aVar;
        }
        throw new AudioProcessor.UnhandledAudioFormatException(aVar);
    }

    @Override // com.google.android.exoplayer2.audio.e
    protected void i() {
        if (this.f9446k) {
            this.f9446k = false;
            int i8 = this.f9445j;
            int i9 = this.f9370b.f9236d;
            this.f9448m = new byte[i8 * i9];
            this.f9447l = this.f9444i * i9;
        }
        this.f9449n = 0;
    }

    @Override // com.google.android.exoplayer2.audio.e
    protected void j() {
        int i8;
        if (this.f9446k) {
            if (this.f9449n > 0) {
                this.f9450o += i8 / this.f9370b.f9236d;
            }
            this.f9449n = 0;
        }
    }

    @Override // com.google.android.exoplayer2.audio.e
    protected void k() {
        this.f9448m = l0.f8068f;
    }

    public long m() {
        return this.f9450o;
    }

    public void n() {
        this.f9450o = 0L;
    }

    public void o(int i8, int i9) {
        this.f9444i = i8;
        this.f9445j = i9;
    }
}
