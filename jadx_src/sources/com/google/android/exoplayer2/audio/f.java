package com.google.android.exoplayer2.audio;

import com.google.android.exoplayer2.audio.AudioProcessor;
import java.nio.ByteBuffer;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class f extends e {

    /* renamed from: i  reason: collision with root package name */
    private int[] f9377i;

    /* renamed from: j  reason: collision with root package name */
    private int[] f9378j;

    @Override // com.google.android.exoplayer2.audio.AudioProcessor
    public void e(ByteBuffer byteBuffer) {
        int[] iArr = (int[]) b6.a.e(this.f9378j);
        int position = byteBuffer.position();
        int limit = byteBuffer.limit();
        ByteBuffer l8 = l(((limit - position) / this.f9370b.f9236d) * this.f9371c.f9236d);
        while (position < limit) {
            for (int i8 : iArr) {
                l8.putShort(byteBuffer.getShort((i8 * 2) + position));
            }
            position += this.f9370b.f9236d;
        }
        byteBuffer.position(limit);
        l8.flip();
    }

    @Override // com.google.android.exoplayer2.audio.e
    public AudioProcessor.a h(AudioProcessor.a aVar) {
        int[] iArr = this.f9377i;
        if (iArr == null) {
            return AudioProcessor.a.f9232e;
        }
        if (aVar.f9235c == 2) {
            boolean z4 = aVar.f9234b != iArr.length;
            int i8 = 0;
            while (i8 < iArr.length) {
                int i9 = iArr[i8];
                if (i9 >= aVar.f9234b) {
                    throw new AudioProcessor.UnhandledAudioFormatException(aVar);
                }
                z4 |= i9 != i8;
                i8++;
            }
            return z4 ? new AudioProcessor.a(aVar.f9233a, iArr.length, 2) : AudioProcessor.a.f9232e;
        }
        throw new AudioProcessor.UnhandledAudioFormatException(aVar);
    }

    @Override // com.google.android.exoplayer2.audio.e
    protected void i() {
        this.f9378j = this.f9377i;
    }

    @Override // com.google.android.exoplayer2.audio.e
    protected void k() {
        this.f9378j = null;
        this.f9377i = null;
    }

    public void m(int[] iArr) {
        this.f9377i = iArr;
    }
}
