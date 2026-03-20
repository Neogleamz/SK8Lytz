package com.google.android.exoplayer2.mediacodec;

import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.Surface;
import b6.i0;
import com.google.android.exoplayer2.mediacodec.j;
import com.google.common.base.r;
import java.nio.ByteBuffer;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b implements j {

    /* renamed from: a  reason: collision with root package name */
    private final MediaCodec f9973a;

    /* renamed from: b  reason: collision with root package name */
    private final e f9974b;

    /* renamed from: c  reason: collision with root package name */
    private final c f9975c;

    /* renamed from: d  reason: collision with root package name */
    private final boolean f9976d;

    /* renamed from: e  reason: collision with root package name */
    private boolean f9977e;

    /* renamed from: f  reason: collision with root package name */
    private int f9978f;

    /* renamed from: com.google.android.exoplayer2.mediacodec.b$b  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class C0107b implements j.b {

        /* renamed from: a  reason: collision with root package name */
        private final r<HandlerThread> f9979a;

        /* renamed from: b  reason: collision with root package name */
        private final r<HandlerThread> f9980b;

        /* renamed from: c  reason: collision with root package name */
        private final boolean f9981c;

        public C0107b(int i8, boolean z4) {
            this(new z4.a(i8), new z4.b(i8), z4);
        }

        C0107b(r<HandlerThread> rVar, r<HandlerThread> rVar2, boolean z4) {
            this.f9979a = rVar;
            this.f9980b = rVar2;
            this.f9981c = z4;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ HandlerThread e(int i8) {
            return new HandlerThread(b.s(i8));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ HandlerThread f(int i8) {
            return new HandlerThread(b.t(i8));
        }

        @Override // com.google.android.exoplayer2.mediacodec.j.b
        /* renamed from: d */
        public b a(j.a aVar) {
            MediaCodec mediaCodec;
            b bVar;
            String str = aVar.f10024a.f10030a;
            b bVar2 = null;
            try {
                i0.a("createCodec:" + str);
                mediaCodec = MediaCodec.createByCodecName(str);
                try {
                    bVar = new b(mediaCodec, this.f9979a.get(), this.f9980b.get(), this.f9981c);
                } catch (Exception e8) {
                    e = e8;
                }
            } catch (Exception e9) {
                e = e9;
                mediaCodec = null;
            }
            try {
                i0.c();
                bVar.v(aVar.f10025b, aVar.f10027d, aVar.f10028e, aVar.f10029f);
                return bVar;
            } catch (Exception e10) {
                e = e10;
                bVar2 = bVar;
                if (bVar2 != null) {
                    bVar2.release();
                } else if (mediaCodec != null) {
                    mediaCodec.release();
                }
                throw e;
            }
        }
    }

    private b(MediaCodec mediaCodec, HandlerThread handlerThread, HandlerThread handlerThread2, boolean z4) {
        this.f9973a = mediaCodec;
        this.f9974b = new e(handlerThread);
        this.f9975c = new c(mediaCodec, handlerThread2);
        this.f9976d = z4;
        this.f9978f = 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String s(int i8) {
        return u(i8, "ExoPlayer:MediaCodecAsyncAdapter:");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String t(int i8) {
        return u(i8, "ExoPlayer:MediaCodecQueueingThread:");
    }

    private static String u(int i8, String str) {
        String str2;
        StringBuilder sb = new StringBuilder(str);
        if (i8 == 1) {
            str2 = "Audio";
        } else if (i8 == 2) {
            str2 = "Video";
        } else {
            sb.append("Unknown(");
            sb.append(i8);
            str2 = ")";
        }
        sb.append(str2);
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void v(MediaFormat mediaFormat, Surface surface, MediaCrypto mediaCrypto, int i8) {
        this.f9974b.h(this.f9973a);
        i0.a("configureCodec");
        this.f9973a.configure(mediaFormat, surface, mediaCrypto, i8);
        i0.c();
        this.f9975c.q();
        i0.a("startCodec");
        this.f9973a.start();
        i0.c();
        this.f9978f = 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void w(j.c cVar, MediaCodec mediaCodec, long j8, long j9) {
        cVar.a(this, j8, j9);
    }

    private void x() {
        if (this.f9976d) {
            try {
                this.f9975c.r();
            } catch (InterruptedException e8) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException(e8);
            }
        }
    }

    @Override // com.google.android.exoplayer2.mediacodec.j
    public boolean a() {
        return false;
    }

    @Override // com.google.android.exoplayer2.mediacodec.j
    public MediaFormat b() {
        return this.f9974b.g();
    }

    @Override // com.google.android.exoplayer2.mediacodec.j
    public void c(Bundle bundle) {
        x();
        this.f9973a.setParameters(bundle);
    }

    @Override // com.google.android.exoplayer2.mediacodec.j
    public void d(int i8, long j8) {
        this.f9973a.releaseOutputBuffer(i8, j8);
    }

    @Override // com.google.android.exoplayer2.mediacodec.j
    public int e() {
        this.f9975c.l();
        return this.f9974b.c();
    }

    @Override // com.google.android.exoplayer2.mediacodec.j
    public int f(MediaCodec.BufferInfo bufferInfo) {
        this.f9975c.l();
        return this.f9974b.d(bufferInfo);
    }

    @Override // com.google.android.exoplayer2.mediacodec.j
    public void flush() {
        this.f9975c.i();
        this.f9973a.flush();
        this.f9974b.e();
        this.f9973a.start();
    }

    @Override // com.google.android.exoplayer2.mediacodec.j
    public void g(final j.c cVar, Handler handler) {
        x();
        this.f9973a.setOnFrameRenderedListener(new MediaCodec.OnFrameRenderedListener() { // from class: com.google.android.exoplayer2.mediacodec.a
            @Override // android.media.MediaCodec.OnFrameRenderedListener
            public final void onFrameRendered(MediaCodec mediaCodec, long j8, long j9) {
                b.this.w(cVar, mediaCodec, j8, j9);
            }
        }, handler);
    }

    @Override // com.google.android.exoplayer2.mediacodec.j
    public void h(int i8, boolean z4) {
        this.f9973a.releaseOutputBuffer(i8, z4);
    }

    @Override // com.google.android.exoplayer2.mediacodec.j
    public void i(int i8) {
        x();
        this.f9973a.setVideoScalingMode(i8);
    }

    @Override // com.google.android.exoplayer2.mediacodec.j
    public ByteBuffer j(int i8) {
        return this.f9973a.getInputBuffer(i8);
    }

    @Override // com.google.android.exoplayer2.mediacodec.j
    public void k(Surface surface) {
        x();
        this.f9973a.setOutputSurface(surface);
    }

    @Override // com.google.android.exoplayer2.mediacodec.j
    public void l(int i8, int i9, int i10, long j8, int i11) {
        this.f9975c.m(i8, i9, i10, j8, i11);
    }

    @Override // com.google.android.exoplayer2.mediacodec.j
    public ByteBuffer m(int i8) {
        return this.f9973a.getOutputBuffer(i8);
    }

    @Override // com.google.android.exoplayer2.mediacodec.j
    public void n(int i8, int i9, l4.c cVar, long j8, int i10) {
        this.f9975c.n(i8, i9, cVar, j8, i10);
    }

    @Override // com.google.android.exoplayer2.mediacodec.j
    public void release() {
        try {
            if (this.f9978f == 1) {
                this.f9975c.p();
                this.f9974b.o();
            }
            this.f9978f = 2;
        } finally {
            if (!this.f9977e) {
                this.f9973a.release();
                this.f9977e = true;
            }
        }
    }
}
