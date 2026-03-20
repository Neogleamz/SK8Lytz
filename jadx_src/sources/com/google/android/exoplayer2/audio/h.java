package com.google.android.exoplayer2.audio;

import b6.l0;
import com.google.android.exoplayer2.audio.AudioProcessor;
import java.nio.ByteBuffer;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class h extends e {

    /* renamed from: i  reason: collision with root package name */
    private static final int f9391i = Float.floatToIntBits(Float.NaN);

    private static void m(int i8, ByteBuffer byteBuffer) {
        int floatToIntBits = Float.floatToIntBits((float) (i8 * 4.656612875245797E-10d));
        if (floatToIntBits == f9391i) {
            floatToIntBits = Float.floatToIntBits(0.0f);
        }
        byteBuffer.putInt(floatToIntBits);
    }

    @Override // com.google.android.exoplayer2.audio.AudioProcessor
    public void e(ByteBuffer byteBuffer) {
        ByteBuffer l8;
        int position = byteBuffer.position();
        int limit = byteBuffer.limit();
        int i8 = limit - position;
        int i9 = this.f9370b.f9235c;
        if (i9 == 536870912) {
            l8 = l((i8 / 3) * 4);
            while (position < limit) {
                m(((byteBuffer.get(position) & 255) << 8) | ((byteBuffer.get(position + 1) & 255) << 16) | ((byteBuffer.get(position + 2) & 255) << 24), l8);
                position += 3;
            }
        } else if (i9 != 805306368) {
            throw new IllegalStateException();
        } else {
            l8 = l(i8);
            while (position < limit) {
                m((byteBuffer.get(position) & 255) | ((byteBuffer.get(position + 1) & 255) << 8) | ((byteBuffer.get(position + 2) & 255) << 16) | ((byteBuffer.get(position + 3) & 255) << 24), l8);
                position += 4;
            }
        }
        byteBuffer.position(byteBuffer.limit());
        l8.flip();
    }

    @Override // com.google.android.exoplayer2.audio.e
    public AudioProcessor.a h(AudioProcessor.a aVar) {
        int i8 = aVar.f9235c;
        if (l0.t0(i8)) {
            return i8 != 4 ? new AudioProcessor.a(aVar.f9233a, aVar.f9234b, 4) : AudioProcessor.a.f9232e;
        }
        throw new AudioProcessor.UnhandledAudioFormatException(aVar);
    }
}
