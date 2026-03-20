package com.google.android.exoplayer2.mediacodec;

import android.media.MediaCodec;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import b6.l0;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class c {

    /* renamed from: g  reason: collision with root package name */
    private static final ArrayDeque<b> f9982g = new ArrayDeque<>();

    /* renamed from: h  reason: collision with root package name */
    private static final Object f9983h = new Object();

    /* renamed from: a  reason: collision with root package name */
    private final MediaCodec f9984a;

    /* renamed from: b  reason: collision with root package name */
    private final HandlerThread f9985b;

    /* renamed from: c  reason: collision with root package name */
    private Handler f9986c;

    /* renamed from: d  reason: collision with root package name */
    private final AtomicReference<RuntimeException> f9987d;

    /* renamed from: e  reason: collision with root package name */
    private final b6.g f9988e;

    /* renamed from: f  reason: collision with root package name */
    private boolean f9989f;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends Handler {
        a(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            c.this.f(message);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b {

        /* renamed from: a  reason: collision with root package name */
        public int f9991a;

        /* renamed from: b  reason: collision with root package name */
        public int f9992b;

        /* renamed from: c  reason: collision with root package name */
        public int f9993c;

        /* renamed from: d  reason: collision with root package name */
        public final MediaCodec.CryptoInfo f9994d = new MediaCodec.CryptoInfo();

        /* renamed from: e  reason: collision with root package name */
        public long f9995e;

        /* renamed from: f  reason: collision with root package name */
        public int f9996f;

        b() {
        }

        public void a(int i8, int i9, int i10, long j8, int i11) {
            this.f9991a = i8;
            this.f9992b = i9;
            this.f9993c = i10;
            this.f9995e = j8;
            this.f9996f = i11;
        }
    }

    public c(MediaCodec mediaCodec, HandlerThread handlerThread) {
        this(mediaCodec, handlerThread, new b6.g());
    }

    c(MediaCodec mediaCodec, HandlerThread handlerThread, b6.g gVar) {
        this.f9984a = mediaCodec;
        this.f9985b = handlerThread;
        this.f9988e = gVar;
        this.f9987d = new AtomicReference<>();
    }

    private void b() {
        this.f9988e.c();
        ((Handler) b6.a.e(this.f9986c)).obtainMessage(2).sendToTarget();
        this.f9988e.a();
    }

    private static void c(l4.c cVar, MediaCodec.CryptoInfo cryptoInfo) {
        cryptoInfo.numSubSamples = cVar.f21578f;
        cryptoInfo.numBytesOfClearData = e(cVar.f21576d, cryptoInfo.numBytesOfClearData);
        cryptoInfo.numBytesOfEncryptedData = e(cVar.f21577e, cryptoInfo.numBytesOfEncryptedData);
        cryptoInfo.key = (byte[]) b6.a.e(d(cVar.f21574b, cryptoInfo.key));
        cryptoInfo.iv = (byte[]) b6.a.e(d(cVar.f21573a, cryptoInfo.iv));
        cryptoInfo.mode = cVar.f21575c;
        if (l0.f8063a >= 24) {
            cryptoInfo.setPattern(new MediaCodec.CryptoInfo.Pattern(cVar.f21579g, cVar.f21580h));
        }
    }

    private static byte[] d(byte[] bArr, byte[] bArr2) {
        if (bArr == null) {
            return bArr2;
        }
        if (bArr2 == null || bArr2.length < bArr.length) {
            return Arrays.copyOf(bArr, bArr.length);
        }
        System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
        return bArr2;
    }

    private static int[] e(int[] iArr, int[] iArr2) {
        if (iArr == null) {
            return iArr2;
        }
        if (iArr2 == null || iArr2.length < iArr.length) {
            return Arrays.copyOf(iArr, iArr.length);
        }
        System.arraycopy(iArr, 0, iArr2, 0, iArr.length);
        return iArr2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f(Message message) {
        int i8 = message.what;
        b bVar = null;
        if (i8 == 0) {
            bVar = (b) message.obj;
            g(bVar.f9991a, bVar.f9992b, bVar.f9993c, bVar.f9995e, bVar.f9996f);
        } else if (i8 == 1) {
            bVar = (b) message.obj;
            h(bVar.f9991a, bVar.f9992b, bVar.f9994d, bVar.f9995e, bVar.f9996f);
        } else if (i8 != 2) {
            this.f9987d.compareAndSet(null, new IllegalStateException(String.valueOf(message.what)));
        } else {
            this.f9988e.e();
        }
        if (bVar != null) {
            o(bVar);
        }
    }

    private void g(int i8, int i9, int i10, long j8, int i11) {
        try {
            this.f9984a.queueInputBuffer(i8, i9, i10, j8, i11);
        } catch (RuntimeException e8) {
            this.f9987d.compareAndSet(null, e8);
        }
    }

    private void h(int i8, int i9, MediaCodec.CryptoInfo cryptoInfo, long j8, int i10) {
        try {
            synchronized (f9983h) {
                this.f9984a.queueSecureInputBuffer(i8, i9, cryptoInfo, j8, i10);
            }
        } catch (RuntimeException e8) {
            this.f9987d.compareAndSet(null, e8);
        }
    }

    private void j() {
        ((Handler) b6.a.e(this.f9986c)).removeCallbacksAndMessages(null);
        b();
    }

    private static b k() {
        ArrayDeque<b> arrayDeque = f9982g;
        synchronized (arrayDeque) {
            if (arrayDeque.isEmpty()) {
                return new b();
            }
            return arrayDeque.removeFirst();
        }
    }

    private static void o(b bVar) {
        ArrayDeque<b> arrayDeque = f9982g;
        synchronized (arrayDeque) {
            arrayDeque.add(bVar);
        }
    }

    public void i() {
        if (this.f9989f) {
            try {
                j();
            } catch (InterruptedException e8) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException(e8);
            }
        }
    }

    public void l() {
        RuntimeException andSet = this.f9987d.getAndSet(null);
        if (andSet != null) {
            throw andSet;
        }
    }

    public void m(int i8, int i9, int i10, long j8, int i11) {
        l();
        b k8 = k();
        k8.a(i8, i9, i10, j8, i11);
        ((Handler) l0.j(this.f9986c)).obtainMessage(0, k8).sendToTarget();
    }

    public void n(int i8, int i9, l4.c cVar, long j8, int i10) {
        l();
        b k8 = k();
        k8.a(i8, i9, 0, j8, i10);
        c(cVar, k8.f9994d);
        ((Handler) l0.j(this.f9986c)).obtainMessage(1, k8).sendToTarget();
    }

    public void p() {
        if (this.f9989f) {
            i();
            this.f9985b.quit();
        }
        this.f9989f = false;
    }

    public void q() {
        if (this.f9989f) {
            return;
        }
        this.f9985b.start();
        this.f9986c = new a(this.f9985b.getLooper());
        this.f9989f = true;
    }

    public void r() {
        b();
    }
}
