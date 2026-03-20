package c1;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.media.Image;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.opengl.GLES20;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.Surface;
import java.nio.ByteBuffer;
import java.util.ArrayList;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c implements AutoCloseable, SurfaceTexture.OnFrameAvailableListener {
    e B;
    private SurfaceTexture C;
    private Surface E;
    private Surface F;
    private c1.b G;
    private c1.a H;
    private int K;

    /* renamed from: a  reason: collision with root package name */
    MediaCodec f8223a;

    /* renamed from: b  reason: collision with root package name */
    final AbstractC0097c f8224b;

    /* renamed from: c  reason: collision with root package name */
    private final HandlerThread f8225c;

    /* renamed from: d  reason: collision with root package name */
    final Handler f8226d;

    /* renamed from: e  reason: collision with root package name */
    private final int f8227e;

    /* renamed from: f  reason: collision with root package name */
    final int f8228f;

    /* renamed from: g  reason: collision with root package name */
    final int f8229g;

    /* renamed from: h  reason: collision with root package name */
    final int f8230h;

    /* renamed from: j  reason: collision with root package name */
    final int f8231j;

    /* renamed from: k  reason: collision with root package name */
    final int f8232k;

    /* renamed from: l  reason: collision with root package name */
    final int f8233l;

    /* renamed from: m  reason: collision with root package name */
    private final int f8234m;

    /* renamed from: n  reason: collision with root package name */
    final boolean f8235n;

    /* renamed from: p  reason: collision with root package name */
    private int f8236p;
    boolean q;

    /* renamed from: t  reason: collision with root package name */
    private final Rect f8237t;

    /* renamed from: w  reason: collision with root package name */
    private final Rect f8238w;

    /* renamed from: x  reason: collision with root package name */
    private ByteBuffer f8239x;

    /* renamed from: y  reason: collision with root package name */
    private final ArrayList<ByteBuffer> f8240y = new ArrayList<>();

    /* renamed from: z  reason: collision with root package name */
    private final ArrayList<ByteBuffer> f8241z = new ArrayList<>();
    final ArrayList<Integer> A = new ArrayList<>();
    private final float[] L = new float[16];

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            c.this.j();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b implements Runnable {
        b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            c.this.o();
        }
    }

    /* renamed from: c1.c$c  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class AbstractC0097c {
        public abstract void a(c cVar);

        public abstract void b(c cVar, ByteBuffer byteBuffer);

        public abstract void c(c cVar, MediaCodec.CodecException codecException);

        public abstract void d(c cVar, MediaFormat mediaFormat);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class d extends MediaCodec.Callback {

        /* renamed from: a  reason: collision with root package name */
        private boolean f8244a;

        d() {
        }

        private void a(MediaCodec.CodecException codecException) {
            c.this.o();
            if (codecException == null) {
                c cVar = c.this;
                cVar.f8224b.a(cVar);
                return;
            }
            c cVar2 = c.this;
            cVar2.f8224b.c(cVar2, codecException);
        }

        @Override // android.media.MediaCodec.Callback
        public void onError(MediaCodec mediaCodec, MediaCodec.CodecException codecException) {
            if (mediaCodec != c.this.f8223a) {
                return;
            }
            Log.e("HeifEncoder", "onError: " + codecException);
            a(codecException);
        }

        @Override // android.media.MediaCodec.Callback
        public void onInputBufferAvailable(MediaCodec mediaCodec, int i8) {
            c cVar = c.this;
            if (mediaCodec != cVar.f8223a || cVar.q) {
                return;
            }
            cVar.A.add(Integer.valueOf(i8));
            c.this.j();
        }

        @Override // android.media.MediaCodec.Callback
        public void onOutputBufferAvailable(MediaCodec mediaCodec, int i8, MediaCodec.BufferInfo bufferInfo) {
            if (mediaCodec != c.this.f8223a || this.f8244a) {
                return;
            }
            if (bufferInfo.size > 0 && (bufferInfo.flags & 2) == 0) {
                ByteBuffer outputBuffer = mediaCodec.getOutputBuffer(i8);
                outputBuffer.position(bufferInfo.offset);
                outputBuffer.limit(bufferInfo.offset + bufferInfo.size);
                e eVar = c.this.B;
                if (eVar != null) {
                    eVar.e(bufferInfo.presentationTimeUs);
                }
                c cVar = c.this;
                cVar.f8224b.b(cVar, outputBuffer);
            }
            this.f8244a = ((bufferInfo.flags & 4) != 0) | this.f8244a;
            mediaCodec.releaseOutputBuffer(i8, false);
            if (this.f8244a) {
                a(null);
            }
        }

        @Override // android.media.MediaCodec.Callback
        public void onOutputFormatChanged(MediaCodec mediaCodec, MediaFormat mediaFormat) {
            if (mediaCodec != c.this.f8223a) {
                return;
            }
            if (!"image/vnd.android.heic".equals(mediaFormat.getString("mime"))) {
                mediaFormat.setString("mime", "image/vnd.android.heic");
                mediaFormat.setInteger("width", c.this.f8228f);
                mediaFormat.setInteger("height", c.this.f8229g);
                c cVar = c.this;
                if (cVar.f8235n) {
                    mediaFormat.setInteger("tile-width", cVar.f8230h);
                    mediaFormat.setInteger("tile-height", c.this.f8231j);
                    mediaFormat.setInteger("grid-rows", c.this.f8232k);
                    mediaFormat.setInteger("grid-cols", c.this.f8233l);
                }
            }
            c cVar2 = c.this;
            cVar2.f8224b.d(cVar2, mediaFormat);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private class e {

        /* renamed from: a  reason: collision with root package name */
        final boolean f8246a;

        /* renamed from: b  reason: collision with root package name */
        long f8247b = -1;

        /* renamed from: c  reason: collision with root package name */
        long f8248c = -1;

        /* renamed from: d  reason: collision with root package name */
        long f8249d = -1;

        /* renamed from: e  reason: collision with root package name */
        long f8250e = -1;

        /* renamed from: f  reason: collision with root package name */
        long f8251f = -1;

        /* renamed from: g  reason: collision with root package name */
        boolean f8252g;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public class a implements Runnable {
            a() {
            }

            @Override // java.lang.Runnable
            public void run() {
                MediaCodec mediaCodec = c.this.f8223a;
                if (mediaCodec != null) {
                    mediaCodec.signalEndOfInputStream();
                }
            }
        }

        e(boolean z4) {
            this.f8246a = z4;
        }

        private void a() {
            c.this.f8226d.post(new a());
            this.f8252g = true;
        }

        private void b() {
            if (this.f8252g) {
                return;
            }
            if (this.f8249d < 0) {
                long j8 = this.f8247b;
                if (j8 >= 0 && this.f8248c >= j8) {
                    long j9 = this.f8250e;
                    if (j9 < 0) {
                        a();
                        return;
                    }
                    this.f8249d = j9;
                }
            }
            long j10 = this.f8249d;
            if (j10 < 0 || j10 > this.f8251f) {
                return;
            }
            a();
        }

        synchronized void c(long j8) {
            if (this.f8246a) {
                if (this.f8247b < 0) {
                    this.f8247b = j8;
                }
            } else if (this.f8249d < 0) {
                this.f8249d = j8 / 1000;
            }
            b();
        }

        /* JADX WARN: Removed duplicated region for block: B:11:0x0013 A[Catch: all -> 0x001c, TryCatch #0 {, blocks: (B:3:0x0001, B:11:0x0013, B:12:0x0015), top: B:18:0x0001 }] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        synchronized boolean d(long r5, long r7) {
            /*
                r4 = this;
                monitor-enter(r4)
                long r0 = r4.f8247b     // Catch: java.lang.Throwable -> L1c
                r2 = 0
                int r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
                if (r2 < 0) goto L10
                int r0 = (r5 > r0 ? 1 : (r5 == r0 ? 0 : -1))
                if (r0 > 0) goto Le
                goto L10
            Le:
                r0 = 0
                goto L11
            L10:
                r0 = 1
            L11:
                if (r0 == 0) goto L15
                r4.f8250e = r7     // Catch: java.lang.Throwable -> L1c
            L15:
                r4.f8248c = r5     // Catch: java.lang.Throwable -> L1c
                r4.b()     // Catch: java.lang.Throwable -> L1c
                monitor-exit(r4)
                return r0
            L1c:
                r5 = move-exception
                monitor-exit(r4)
                throw r5
            */
            throw new UnsupportedOperationException("Method not decompiled: c1.c.e.d(long, long):boolean");
        }

        synchronized void e(long j8) {
            this.f8251f = j8;
            b();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:71:0x01ef  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0236  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public c(int r20, int r21, boolean r22, int r23, int r24, android.os.Handler r25, c1.c.AbstractC0097c r26) {
        /*
            Method dump skipped, instructions count: 623
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: c1.c.<init>(int, int, boolean, int, int, android.os.Handler, c1.c$c):void");
    }

    private ByteBuffer a() {
        ByteBuffer remove;
        synchronized (this.f8240y) {
            while (!this.q && this.f8240y.isEmpty()) {
                try {
                    this.f8240y.wait();
                } catch (InterruptedException unused) {
                }
            }
            remove = this.q ? null : this.f8240y.remove(0);
        }
        return remove;
    }

    private void c(byte[] bArr) {
        ByteBuffer a9 = a();
        if (a9 == null) {
            return;
        }
        a9.clear();
        if (bArr != null) {
            a9.put(bArr);
        }
        a9.flip();
        synchronized (this.f8241z) {
            this.f8241z.add(a9);
        }
        this.f8226d.post(new a());
    }

    private long d(int i8) {
        return ((i8 * 1000000) / this.f8234m) + 132;
    }

    private static void f(ByteBuffer byteBuffer, Image image, int i8, int i9, Rect rect, Rect rect2) {
        int i10;
        int i11;
        if (rect.width() != rect2.width() || rect.height() != rect2.height()) {
            throw new IllegalArgumentException("src and dst rect size are different!");
        }
        if (i8 % 2 == 0 && i9 % 2 == 0) {
            int i12 = 2;
            if (rect.left % 2 == 0 && rect.top % 2 == 0 && rect.right % 2 == 0 && rect.bottom % 2 == 0 && rect2.left % 2 == 0 && rect2.top % 2 == 0 && rect2.right % 2 == 0 && rect2.bottom % 2 == 0) {
                Image.Plane[] planes = image.getPlanes();
                int i13 = 0;
                while (i13 < planes.length) {
                    ByteBuffer buffer = planes[i13].getBuffer();
                    int pixelStride = planes[i13].getPixelStride();
                    int min = Math.min(rect.width(), i8 - rect.left);
                    int min2 = Math.min(rect.height(), i9 - rect.top);
                    if (i13 > 0) {
                        i11 = ((i8 * i9) * (i13 + 3)) / 4;
                        i10 = i12;
                    } else {
                        i10 = 1;
                        i11 = 0;
                    }
                    for (int i14 = 0; i14 < min2 / i10; i14++) {
                        byteBuffer.position(((((rect.top / i10) + i14) * i8) / i10) + i11 + (rect.left / i10));
                        buffer.position((((rect2.top / i10) + i14) * planes[i13].getRowStride()) + ((rect2.left * pixelStride) / i10));
                        int i15 = 0;
                        while (true) {
                            int i16 = min / i10;
                            if (i15 < i16) {
                                buffer.put(byteBuffer.get());
                                if (pixelStride > 1 && i15 != i16 - 1) {
                                    buffer.position((buffer.position() + pixelStride) - 1);
                                }
                                i15++;
                            }
                        }
                    }
                    i13++;
                    i12 = 2;
                }
                return;
            }
        }
        throw new IllegalArgumentException("src or dst are not aligned!");
    }

    private void h() {
        GLES20.glViewport(0, 0, this.f8230h, this.f8231j);
        for (int i8 = 0; i8 < this.f8232k; i8++) {
            for (int i9 = 0; i9 < this.f8233l; i9++) {
                int i10 = this.f8230h;
                int i11 = i9 * i10;
                int i12 = this.f8231j;
                int i13 = i8 * i12;
                this.f8237t.set(i11, i13, i10 + i11, i12 + i13);
                this.H.a(this.K, c1.e.f8286i, this.f8237t);
                c1.b bVar = this.G;
                int i14 = this.f8236p;
                this.f8236p = i14 + 1;
                bVar.i(d(i14) * 1000);
                this.G.j();
            }
        }
    }

    private ByteBuffer i() {
        if (!this.q && this.f8239x == null) {
            synchronized (this.f8241z) {
                this.f8239x = this.f8241z.isEmpty() ? null : this.f8241z.remove(0);
            }
        }
        if (this.q) {
            return null;
        }
        return this.f8239x;
    }

    private void l(boolean z4) {
        synchronized (this.f8240y) {
            this.q = z4 | this.q;
            this.f8240y.add(this.f8239x);
            this.f8240y.notifyAll();
        }
        this.f8239x = null;
    }

    public void b(Bitmap bitmap) {
        if (this.f8227e != 2) {
            throw new IllegalStateException("addBitmap is only allowed in bitmap input mode");
        }
        if (this.B.d(d(this.f8236p) * 1000, d((this.f8236p + this.f8234m) - 1))) {
            synchronized (this) {
                c1.b bVar = this.G;
                if (bVar == null) {
                    return;
                }
                bVar.f();
                this.H.d(this.K, bitmap);
                h();
                this.G.g();
            }
        }
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        synchronized (this.f8240y) {
            this.q = true;
            this.f8240y.notifyAll();
        }
        this.f8226d.postAtFrontOfQueue(new b());
    }

    void j() {
        while (true) {
            ByteBuffer i8 = i();
            if (i8 == null || this.A.isEmpty()) {
                return;
            }
            int intValue = this.A.remove(0).intValue();
            boolean z4 = this.f8236p % this.f8234m == 0 && i8.remaining() == 0;
            if (!z4) {
                Image inputImage = this.f8223a.getInputImage(intValue);
                int i9 = this.f8230h;
                int i10 = this.f8236p;
                int i11 = this.f8233l;
                int i12 = (i10 % i11) * i9;
                int i13 = this.f8231j;
                int i14 = ((i10 / i11) % this.f8232k) * i13;
                this.f8237t.set(i12, i14, i9 + i12, i13 + i14);
                f(i8, inputImage, this.f8228f, this.f8229g, this.f8237t, this.f8238w);
            }
            MediaCodec mediaCodec = this.f8223a;
            int capacity = z4 ? 0 : mediaCodec.getInputBuffer(intValue).capacity();
            int i15 = this.f8236p;
            this.f8236p = i15 + 1;
            mediaCodec.queueInputBuffer(intValue, 0, capacity, d(i15), z4 ? 4 : 0);
            if (z4 || this.f8236p % this.f8234m == 0) {
                l(z4);
            }
        }
    }

    public void m() {
        this.f8223a.start();
    }

    public void n() {
        int i8 = this.f8227e;
        if (i8 == 2) {
            this.B.c(0L);
        } else if (i8 == 0) {
            c(null);
        }
    }

    void o() {
        MediaCodec mediaCodec = this.f8223a;
        if (mediaCodec != null) {
            mediaCodec.stop();
            this.f8223a.release();
            this.f8223a = null;
        }
        synchronized (this.f8240y) {
            this.q = true;
            this.f8240y.notifyAll();
        }
        synchronized (this) {
            c1.a aVar = this.H;
            if (aVar != null) {
                aVar.e(false);
                this.H = null;
            }
            c1.b bVar = this.G;
            if (bVar != null) {
                bVar.h();
                this.G = null;
            }
            SurfaceTexture surfaceTexture = this.C;
            if (surfaceTexture != null) {
                surfaceTexture.release();
                this.C = null;
            }
        }
    }

    @Override // android.graphics.SurfaceTexture.OnFrameAvailableListener
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        synchronized (this) {
            c1.b bVar = this.G;
            if (bVar == null) {
                return;
            }
            bVar.f();
            surfaceTexture.updateTexImage();
            surfaceTexture.getTransformMatrix(this.L);
            if (this.B.d(surfaceTexture.getTimestamp(), d((this.f8236p + this.f8234m) - 1))) {
                h();
            }
            surfaceTexture.releaseTexImage();
            this.G.g();
        }
    }
}
