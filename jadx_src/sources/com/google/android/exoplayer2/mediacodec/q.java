package com.google.android.exoplayer2.mediacodec;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.Surface;
import b6.i0;
import b6.l0;
import com.google.android.exoplayer2.mediacodec.j;
import java.io.IOException;
import java.nio.ByteBuffer;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class q implements j {

    /* renamed from: a  reason: collision with root package name */
    private final MediaCodec f10046a;

    /* renamed from: b  reason: collision with root package name */
    private ByteBuffer[] f10047b;

    /* renamed from: c  reason: collision with root package name */
    private ByteBuffer[] f10048c;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b implements j.b {
        @Override // com.google.android.exoplayer2.mediacodec.j.b
        public j a(j.a aVar) {
            MediaCodec b9;
            MediaCodec mediaCodec = null;
            try {
                b9 = b(aVar);
            } catch (IOException e8) {
                e = e8;
            } catch (RuntimeException e9) {
                e = e9;
            }
            try {
                i0.a("configureCodec");
                b9.configure(aVar.f10025b, aVar.f10027d, aVar.f10028e, aVar.f10029f);
                i0.c();
                i0.a("startCodec");
                b9.start();
                i0.c();
                return new q(b9);
            } catch (IOException | RuntimeException e10) {
                e = e10;
                mediaCodec = b9;
                if (mediaCodec != null) {
                    mediaCodec.release();
                }
                throw e;
            }
        }

        protected MediaCodec b(j.a aVar) {
            b6.a.e(aVar.f10024a);
            String str = aVar.f10024a.f10030a;
            i0.a("createCodec:" + str);
            MediaCodec createByCodecName = MediaCodec.createByCodecName(str);
            i0.c();
            return createByCodecName;
        }
    }

    private q(MediaCodec mediaCodec) {
        this.f10046a = mediaCodec;
        if (l0.f8063a < 21) {
            this.f10047b = mediaCodec.getInputBuffers();
            this.f10048c = mediaCodec.getOutputBuffers();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void p(j.c cVar, MediaCodec mediaCodec, long j8, long j9) {
        cVar.a(this, j8, j9);
    }

    @Override // com.google.android.exoplayer2.mediacodec.j
    public boolean a() {
        return false;
    }

    @Override // com.google.android.exoplayer2.mediacodec.j
    public MediaFormat b() {
        return this.f10046a.getOutputFormat();
    }

    @Override // com.google.android.exoplayer2.mediacodec.j
    public void c(Bundle bundle) {
        this.f10046a.setParameters(bundle);
    }

    @Override // com.google.android.exoplayer2.mediacodec.j
    public void d(int i8, long j8) {
        this.f10046a.releaseOutputBuffer(i8, j8);
    }

    @Override // com.google.android.exoplayer2.mediacodec.j
    public int e() {
        return this.f10046a.dequeueInputBuffer(0L);
    }

    @Override // com.google.android.exoplayer2.mediacodec.j
    public int f(MediaCodec.BufferInfo bufferInfo) {
        int dequeueOutputBuffer;
        do {
            dequeueOutputBuffer = this.f10046a.dequeueOutputBuffer(bufferInfo, 0L);
            if (dequeueOutputBuffer == -3 && l0.f8063a < 21) {
                this.f10048c = this.f10046a.getOutputBuffers();
                continue;
            }
        } while (dequeueOutputBuffer == -3);
        return dequeueOutputBuffer;
    }

    @Override // com.google.android.exoplayer2.mediacodec.j
    public void flush() {
        this.f10046a.flush();
    }

    @Override // com.google.android.exoplayer2.mediacodec.j
    public void g(j.c cVar, Handler handler) {
        this.f10046a.setOnFrameRenderedListener(new z4.d(this, cVar), handler);
    }

    @Override // com.google.android.exoplayer2.mediacodec.j
    public void h(int i8, boolean z4) {
        this.f10046a.releaseOutputBuffer(i8, z4);
    }

    @Override // com.google.android.exoplayer2.mediacodec.j
    public void i(int i8) {
        this.f10046a.setVideoScalingMode(i8);
    }

    @Override // com.google.android.exoplayer2.mediacodec.j
    public ByteBuffer j(int i8) {
        return l0.f8063a >= 21 ? this.f10046a.getInputBuffer(i8) : ((ByteBuffer[]) l0.j(this.f10047b))[i8];
    }

    @Override // com.google.android.exoplayer2.mediacodec.j
    public void k(Surface surface) {
        this.f10046a.setOutputSurface(surface);
    }

    @Override // com.google.android.exoplayer2.mediacodec.j
    public void l(int i8, int i9, int i10, long j8, int i11) {
        this.f10046a.queueInputBuffer(i8, i9, i10, j8, i11);
    }

    @Override // com.google.android.exoplayer2.mediacodec.j
    public ByteBuffer m(int i8) {
        return l0.f8063a >= 21 ? this.f10046a.getOutputBuffer(i8) : ((ByteBuffer[]) l0.j(this.f10048c))[i8];
    }

    @Override // com.google.android.exoplayer2.mediacodec.j
    public void n(int i8, int i9, l4.c cVar, long j8, int i10) {
        this.f10046a.queueSecureInputBuffer(i8, i9, cVar.a(), j8, i10);
    }

    @Override // com.google.android.exoplayer2.mediacodec.j
    public void release() {
        this.f10047b = null;
        this.f10048c = null;
        this.f10046a.release();
    }
}
