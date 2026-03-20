package com.google.android.exoplayer2.mediacodec;

import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.Surface;
import com.google.android.exoplayer2.w0;
import java.nio.ByteBuffer;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface j {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        public final k f10024a;

        /* renamed from: b  reason: collision with root package name */
        public final MediaFormat f10025b;

        /* renamed from: c  reason: collision with root package name */
        public final w0 f10026c;

        /* renamed from: d  reason: collision with root package name */
        public final Surface f10027d;

        /* renamed from: e  reason: collision with root package name */
        public final MediaCrypto f10028e;

        /* renamed from: f  reason: collision with root package name */
        public final int f10029f;

        private a(k kVar, MediaFormat mediaFormat, w0 w0Var, Surface surface, MediaCrypto mediaCrypto, int i8) {
            this.f10024a = kVar;
            this.f10025b = mediaFormat;
            this.f10026c = w0Var;
            this.f10027d = surface;
            this.f10028e = mediaCrypto;
            this.f10029f = i8;
        }

        public static a a(k kVar, MediaFormat mediaFormat, w0 w0Var, MediaCrypto mediaCrypto) {
            return new a(kVar, mediaFormat, w0Var, null, mediaCrypto, 0);
        }

        public static a b(k kVar, MediaFormat mediaFormat, w0 w0Var, Surface surface, MediaCrypto mediaCrypto) {
            return new a(kVar, mediaFormat, w0Var, surface, mediaCrypto, 0);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b {
        j a(a aVar);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface c {
        void a(j jVar, long j8, long j9);
    }

    boolean a();

    MediaFormat b();

    void c(Bundle bundle);

    void d(int i8, long j8);

    int e();

    int f(MediaCodec.BufferInfo bufferInfo);

    void flush();

    void g(c cVar, Handler handler);

    void h(int i8, boolean z4);

    void i(int i8);

    ByteBuffer j(int i8);

    void k(Surface surface);

    void l(int i8, int i9, int i10, long j8, int i11);

    ByteBuffer m(int i8);

    void n(int i8, int i9, l4.c cVar, long j8, int i10);

    void release();
}
