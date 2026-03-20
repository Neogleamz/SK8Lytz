package c1;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.util.Pair;
import c1.c;
import java.io.FileDescriptor;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d implements AutoCloseable {

    /* renamed from: a  reason: collision with root package name */
    private final int f8255a;

    /* renamed from: b  reason: collision with root package name */
    private final HandlerThread f8256b;

    /* renamed from: c  reason: collision with root package name */
    private final Handler f8257c;

    /* renamed from: d  reason: collision with root package name */
    int f8258d;

    /* renamed from: e  reason: collision with root package name */
    final int f8259e;

    /* renamed from: f  reason: collision with root package name */
    final int f8260f;

    /* renamed from: g  reason: collision with root package name */
    final int f8261g;

    /* renamed from: j  reason: collision with root package name */
    MediaMuxer f8263j;

    /* renamed from: k  reason: collision with root package name */
    private c1.c f8264k;

    /* renamed from: m  reason: collision with root package name */
    int[] f8266m;

    /* renamed from: n  reason: collision with root package name */
    int f8267n;

    /* renamed from: p  reason: collision with root package name */
    private boolean f8268p;

    /* renamed from: h  reason: collision with root package name */
    final C0098d f8262h = new C0098d();

    /* renamed from: l  reason: collision with root package name */
    final AtomicBoolean f8265l = new AtomicBoolean(false);
    private final List<Pair<Integer, ByteBuffer>> q = new ArrayList();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                d.this.f();
            } catch (Exception unused) {
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {

        /* renamed from: a  reason: collision with root package name */
        private final String f8270a;

        /* renamed from: b  reason: collision with root package name */
        private final FileDescriptor f8271b;

        /* renamed from: c  reason: collision with root package name */
        private final int f8272c;

        /* renamed from: d  reason: collision with root package name */
        private final int f8273d;

        /* renamed from: e  reason: collision with root package name */
        private final int f8274e;

        /* renamed from: f  reason: collision with root package name */
        private boolean f8275f;

        /* renamed from: g  reason: collision with root package name */
        private int f8276g;

        /* renamed from: h  reason: collision with root package name */
        private int f8277h;

        /* renamed from: i  reason: collision with root package name */
        private int f8278i;

        /* renamed from: j  reason: collision with root package name */
        private int f8279j;

        /* renamed from: k  reason: collision with root package name */
        private Handler f8280k;

        public b(String str, int i8, int i9, int i10) {
            this(str, null, i8, i9, i10);
        }

        private b(String str, FileDescriptor fileDescriptor, int i8, int i9, int i10) {
            this.f8275f = true;
            this.f8276g = 100;
            this.f8277h = 1;
            this.f8278i = 0;
            this.f8279j = 0;
            if (i8 <= 0 || i9 <= 0) {
                throw new IllegalArgumentException("Invalid image size: " + i8 + "x" + i9);
            }
            this.f8270a = str;
            this.f8271b = fileDescriptor;
            this.f8272c = i8;
            this.f8273d = i9;
            this.f8274e = i10;
        }

        public d a() {
            return new d(this.f8270a, this.f8271b, this.f8272c, this.f8273d, this.f8279j, this.f8275f, this.f8276g, this.f8277h, this.f8278i, this.f8274e, this.f8280k);
        }

        public b b(int i8) {
            if (i8 > 0) {
                this.f8277h = i8;
                return this;
            }
            throw new IllegalArgumentException("Invalid maxImage: " + i8);
        }

        public b c(int i8) {
            if (i8 >= 0 && i8 <= 100) {
                this.f8276g = i8;
                return this;
            }
            throw new IllegalArgumentException("Invalid quality: " + i8);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class c extends c.AbstractC0097c {

        /* renamed from: a  reason: collision with root package name */
        private boolean f8281a;

        c() {
        }

        private void e(Exception exc) {
            if (this.f8281a) {
                return;
            }
            this.f8281a = true;
            d.this.f8262h.a(exc);
        }

        @Override // c1.c.AbstractC0097c
        public void a(c1.c cVar) {
            e(null);
        }

        @Override // c1.c.AbstractC0097c
        public void b(c1.c cVar, ByteBuffer byteBuffer) {
            if (this.f8281a) {
                return;
            }
            d dVar = d.this;
            if (dVar.f8266m == null) {
                e(new IllegalStateException("Output buffer received before format info"));
                return;
            }
            if (dVar.f8267n < dVar.f8260f * dVar.f8258d) {
                MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
                bufferInfo.set(byteBuffer.position(), byteBuffer.remaining(), 0L, 0);
                d dVar2 = d.this;
                dVar2.f8263j.writeSampleData(dVar2.f8266m[dVar2.f8267n / dVar2.f8258d], byteBuffer, bufferInfo);
            }
            d dVar3 = d.this;
            int i8 = dVar3.f8267n + 1;
            dVar3.f8267n = i8;
            if (i8 == dVar3.f8260f * dVar3.f8258d) {
                e(null);
            }
        }

        @Override // c1.c.AbstractC0097c
        public void c(c1.c cVar, MediaCodec.CodecException codecException) {
            e(codecException);
        }

        @Override // c1.c.AbstractC0097c
        public void d(c1.c cVar, MediaFormat mediaFormat) {
            if (this.f8281a) {
                return;
            }
            if (d.this.f8266m != null) {
                e(new IllegalStateException("Output format changed after muxer started"));
                return;
            }
            try {
                d.this.f8258d = mediaFormat.getInteger("grid-rows") * mediaFormat.getInteger("grid-cols");
            } catch (ClassCastException | NullPointerException unused) {
                d.this.f8258d = 1;
            }
            d dVar = d.this;
            dVar.f8266m = new int[dVar.f8260f];
            if (dVar.f8259e > 0) {
                Log.d("HeifWriter", "setting rotation: " + d.this.f8259e);
                d dVar2 = d.this;
                dVar2.f8263j.setOrientationHint(dVar2.f8259e);
            }
            int i8 = 0;
            while (true) {
                d dVar3 = d.this;
                if (i8 >= dVar3.f8266m.length) {
                    dVar3.f8263j.start();
                    d.this.f8265l.set(true);
                    d.this.h();
                    return;
                }
                mediaFormat.setInteger("is-default", i8 == dVar3.f8261g ? 1 : 0);
                d dVar4 = d.this;
                dVar4.f8266m[i8] = dVar4.f8263j.addTrack(mediaFormat);
                i8++;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: c1.d$d  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class C0098d {

        /* renamed from: a  reason: collision with root package name */
        private boolean f8283a;

        /* renamed from: b  reason: collision with root package name */
        private Exception f8284b;

        C0098d() {
        }

        synchronized void a(Exception exc) {
            if (!this.f8283a) {
                this.f8283a = true;
                this.f8284b = exc;
                notifyAll();
            }
        }

        synchronized void b(long j8) {
            int i8 = (j8 > 0L ? 1 : (j8 == 0L ? 0 : -1));
            if (i8 < 0) {
                throw new IllegalArgumentException("timeoutMs is negative");
            }
            if (i8 == 0) {
                while (!this.f8283a) {
                    try {
                        wait();
                    } catch (InterruptedException unused) {
                    }
                }
            } else {
                long currentTimeMillis = System.currentTimeMillis();
                while (!this.f8283a && j8 > 0) {
                    try {
                        wait(j8);
                    } catch (InterruptedException unused2) {
                    }
                    j8 -= System.currentTimeMillis() - currentTimeMillis;
                }
            }
            if (!this.f8283a) {
                this.f8283a = true;
                this.f8284b = new TimeoutException("timed out waiting for result");
            }
            Exception exc = this.f8284b;
            if (exc != null) {
                throw exc;
            }
        }
    }

    @SuppressLint({"WrongConstant"})
    d(String str, FileDescriptor fileDescriptor, int i8, int i9, int i10, boolean z4, int i11, int i12, int i13, int i14, Handler handler) {
        if (i13 >= i12) {
            throw new IllegalArgumentException("Invalid maxImages (" + i12 + ") or primaryIndex (" + i13 + ")");
        }
        MediaFormat.createVideoFormat("image/vnd.android.heic", i8, i9);
        this.f8258d = 1;
        this.f8259e = i10;
        this.f8255a = i14;
        this.f8260f = i12;
        this.f8261g = i13;
        Looper looper = handler != null ? handler.getLooper() : null;
        if (looper == null) {
            HandlerThread handlerThread = new HandlerThread("HeifEncoderThread", -2);
            this.f8256b = handlerThread;
            handlerThread.start();
            looper = handlerThread.getLooper();
        } else {
            this.f8256b = null;
        }
        Handler handler2 = new Handler(looper);
        this.f8257c = handler2;
        this.f8263j = str != null ? new MediaMuxer(str, 3) : new MediaMuxer(fileDescriptor, 3);
        this.f8264k = new c1.c(i8, i9, z4, i11, i14, handler2, new c());
    }

    private void b(int i8) {
        if (this.f8255a == i8) {
            return;
        }
        throw new IllegalStateException("Not valid in input mode " + this.f8255a);
    }

    private void c(boolean z4) {
        if (this.f8268p != z4) {
            throw new IllegalStateException("Already started");
        }
    }

    private void d(int i8) {
        c(true);
        b(i8);
    }

    public void a(Bitmap bitmap) {
        d(2);
        synchronized (this) {
            c1.c cVar = this.f8264k;
            if (cVar != null) {
                cVar.b(bitmap);
            }
        }
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        this.f8257c.postAtFrontOfQueue(new a());
    }

    void f() {
        MediaMuxer mediaMuxer = this.f8263j;
        if (mediaMuxer != null) {
            mediaMuxer.stop();
            this.f8263j.release();
            this.f8263j = null;
        }
        c1.c cVar = this.f8264k;
        if (cVar != null) {
            cVar.close();
            synchronized (this) {
                this.f8264k = null;
            }
        }
    }

    @SuppressLint({"WrongConstant"})
    void h() {
        Pair<Integer, ByteBuffer> remove;
        if (!this.f8265l.get()) {
            return;
        }
        while (true) {
            synchronized (this.q) {
                if (this.q.isEmpty()) {
                    return;
                }
                remove = this.q.remove(0);
            }
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            bufferInfo.set(((ByteBuffer) remove.second).position(), ((ByteBuffer) remove.second).remaining(), 0L, 16);
            this.f8263j.writeSampleData(this.f8266m[((Integer) remove.first).intValue()], (ByteBuffer) remove.second, bufferInfo);
        }
    }

    public void i() {
        c(false);
        this.f8268p = true;
        this.f8264k.m();
    }

    public void j(long j8) {
        c(true);
        synchronized (this) {
            c1.c cVar = this.f8264k;
            if (cVar != null) {
                cVar.n();
            }
        }
        this.f8262h.b(j8);
        h();
        f();
    }
}
