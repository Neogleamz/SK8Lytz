package com.google.android.exoplayer2.mediacodec;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.os.Handler;
import android.os.HandlerThread;
import b6.l0;
import java.util.ArrayDeque;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e extends MediaCodec.Callback {

    /* renamed from: b  reason: collision with root package name */
    private final HandlerThread f9999b;

    /* renamed from: c  reason: collision with root package name */
    private Handler f10000c;

    /* renamed from: h  reason: collision with root package name */
    private MediaFormat f10005h;

    /* renamed from: i  reason: collision with root package name */
    private MediaFormat f10006i;

    /* renamed from: j  reason: collision with root package name */
    private MediaCodec.CodecException f10007j;

    /* renamed from: k  reason: collision with root package name */
    private long f10008k;

    /* renamed from: l  reason: collision with root package name */
    private boolean f10009l;

    /* renamed from: m  reason: collision with root package name */
    private IllegalStateException f10010m;

    /* renamed from: a  reason: collision with root package name */
    private final Object f9998a = new Object();

    /* renamed from: d  reason: collision with root package name */
    private final i f10001d = new i();

    /* renamed from: e  reason: collision with root package name */
    private final i f10002e = new i();

    /* renamed from: f  reason: collision with root package name */
    private final ArrayDeque<MediaCodec.BufferInfo> f10003f = new ArrayDeque<>();

    /* renamed from: g  reason: collision with root package name */
    private final ArrayDeque<MediaFormat> f10004g = new ArrayDeque<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    public e(HandlerThread handlerThread) {
        this.f9999b = handlerThread;
    }

    private void b(MediaFormat mediaFormat) {
        this.f10002e.a(-2);
        this.f10004g.add(mediaFormat);
    }

    private void f() {
        if (!this.f10004g.isEmpty()) {
            this.f10006i = this.f10004g.getLast();
        }
        this.f10001d.b();
        this.f10002e.b();
        this.f10003f.clear();
        this.f10004g.clear();
    }

    private boolean i() {
        return this.f10008k > 0 || this.f10009l;
    }

    private void j() {
        k();
        l();
    }

    private void k() {
        IllegalStateException illegalStateException = this.f10010m;
        if (illegalStateException == null) {
            return;
        }
        this.f10010m = null;
        throw illegalStateException;
    }

    private void l() {
        MediaCodec.CodecException codecException = this.f10007j;
        if (codecException == null) {
            return;
        }
        this.f10007j = null;
        throw codecException;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void m() {
        synchronized (this.f9998a) {
            if (this.f10009l) {
                return;
            }
            long j8 = this.f10008k - 1;
            this.f10008k = j8;
            if (j8 > 0) {
                return;
            }
            if (j8 < 0) {
                n(new IllegalStateException());
            } else {
                f();
            }
        }
    }

    private void n(IllegalStateException illegalStateException) {
        synchronized (this.f9998a) {
            this.f10010m = illegalStateException;
        }
    }

    public int c() {
        synchronized (this.f9998a) {
            int i8 = -1;
            if (i()) {
                return -1;
            }
            j();
            if (!this.f10001d.d()) {
                i8 = this.f10001d.e();
            }
            return i8;
        }
    }

    public int d(MediaCodec.BufferInfo bufferInfo) {
        synchronized (this.f9998a) {
            if (i()) {
                return -1;
            }
            j();
            if (this.f10002e.d()) {
                return -1;
            }
            int e8 = this.f10002e.e();
            if (e8 >= 0) {
                b6.a.h(this.f10005h);
                MediaCodec.BufferInfo remove = this.f10003f.remove();
                bufferInfo.set(remove.offset, remove.size, remove.presentationTimeUs, remove.flags);
            } else if (e8 == -2) {
                this.f10005h = this.f10004g.remove();
            }
            return e8;
        }
    }

    public void e() {
        synchronized (this.f9998a) {
            this.f10008k++;
            ((Handler) l0.j(this.f10000c)).post(new Runnable() { // from class: com.google.android.exoplayer2.mediacodec.d
                @Override // java.lang.Runnable
                public final void run() {
                    e.this.m();
                }
            });
        }
    }

    public MediaFormat g() {
        MediaFormat mediaFormat;
        synchronized (this.f9998a) {
            mediaFormat = this.f10005h;
            if (mediaFormat == null) {
                throw new IllegalStateException();
            }
        }
        return mediaFormat;
    }

    public void h(MediaCodec mediaCodec) {
        b6.a.f(this.f10000c == null);
        this.f9999b.start();
        Handler handler = new Handler(this.f9999b.getLooper());
        mediaCodec.setCallback(this, handler);
        this.f10000c = handler;
    }

    public void o() {
        synchronized (this.f9998a) {
            this.f10009l = true;
            this.f9999b.quit();
            f();
        }
    }

    @Override // android.media.MediaCodec.Callback
    public void onError(MediaCodec mediaCodec, MediaCodec.CodecException codecException) {
        synchronized (this.f9998a) {
            this.f10007j = codecException;
        }
    }

    @Override // android.media.MediaCodec.Callback
    public void onInputBufferAvailable(MediaCodec mediaCodec, int i8) {
        synchronized (this.f9998a) {
            this.f10001d.a(i8);
        }
    }

    @Override // android.media.MediaCodec.Callback
    public void onOutputBufferAvailable(MediaCodec mediaCodec, int i8, MediaCodec.BufferInfo bufferInfo) {
        synchronized (this.f9998a) {
            MediaFormat mediaFormat = this.f10006i;
            if (mediaFormat != null) {
                b(mediaFormat);
                this.f10006i = null;
            }
            this.f10002e.a(i8);
            this.f10003f.add(bufferInfo);
        }
    }

    @Override // android.media.MediaCodec.Callback
    public void onOutputFormatChanged(MediaCodec mediaCodec, MediaFormat mediaFormat) {
        synchronized (this.f9998a) {
            b(mediaFormat);
            this.f10006i = null;
        }
    }
}
