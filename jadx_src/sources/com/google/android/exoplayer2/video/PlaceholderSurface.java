package com.google.android.exoplayer2.video;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.Surface;
import b6.p;
import com.google.android.exoplayer2.util.GlUtil;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class PlaceholderSurface extends Surface {

    /* renamed from: d  reason: collision with root package name */
    private static int f11063d;

    /* renamed from: e  reason: collision with root package name */
    private static boolean f11064e;

    /* renamed from: a  reason: collision with root package name */
    public final boolean f11065a;

    /* renamed from: b  reason: collision with root package name */
    private final b f11066b;

    /* renamed from: c  reason: collision with root package name */
    private boolean f11067c;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b extends HandlerThread implements Handler.Callback {

        /* renamed from: a  reason: collision with root package name */
        private com.google.android.exoplayer2.util.a f11068a;

        /* renamed from: b  reason: collision with root package name */
        private Handler f11069b;

        /* renamed from: c  reason: collision with root package name */
        private Error f11070c;

        /* renamed from: d  reason: collision with root package name */
        private RuntimeException f11071d;

        /* renamed from: e  reason: collision with root package name */
        private PlaceholderSurface f11072e;

        public b() {
            super("ExoPlayer:PlaceholderSurface");
        }

        private void b(int i8) {
            b6.a.e(this.f11068a);
            this.f11068a.h(i8);
            this.f11072e = new PlaceholderSurface(this, this.f11068a.g(), i8 != 0);
        }

        private void d() {
            b6.a.e(this.f11068a);
            this.f11068a.i();
        }

        public PlaceholderSurface a(int i8) {
            boolean z4;
            start();
            this.f11069b = new Handler(getLooper(), this);
            this.f11068a = new com.google.android.exoplayer2.util.a(this.f11069b);
            synchronized (this) {
                z4 = false;
                this.f11069b.obtainMessage(1, i8, 0).sendToTarget();
                while (this.f11072e == null && this.f11071d == null && this.f11070c == null) {
                    try {
                        wait();
                    } catch (InterruptedException unused) {
                        z4 = true;
                    }
                }
            }
            if (z4) {
                Thread.currentThread().interrupt();
            }
            RuntimeException runtimeException = this.f11071d;
            if (runtimeException == null) {
                Error error = this.f11070c;
                if (error == null) {
                    return (PlaceholderSurface) b6.a.e(this.f11072e);
                }
                throw error;
            }
            throw runtimeException;
        }

        public void c() {
            b6.a.e(this.f11069b);
            this.f11069b.sendEmptyMessage(2);
        }

        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            int i8 = message.what;
            try {
                if (i8 != 1) {
                    if (i8 != 2) {
                        return true;
                    }
                    try {
                        d();
                    } finally {
                        try {
                            return true;
                        } finally {
                        }
                    }
                    return true;
                }
                try {
                    b(message.arg1);
                    synchronized (this) {
                        notify();
                    }
                } catch (GlUtil.GlException e8) {
                    p.d("PlaceholderSurface", "Failed to initialize placeholder surface", e8);
                    this.f11071d = new IllegalStateException(e8);
                    synchronized (this) {
                        notify();
                    }
                } catch (Error e9) {
                    p.d("PlaceholderSurface", "Failed to initialize placeholder surface", e9);
                    this.f11070c = e9;
                    synchronized (this) {
                        notify();
                    }
                } catch (RuntimeException e10) {
                    p.d("PlaceholderSurface", "Failed to initialize placeholder surface", e10);
                    this.f11071d = e10;
                    synchronized (this) {
                        notify();
                    }
                }
                return true;
            } catch (Throwable th) {
                synchronized (this) {
                    notify();
                    throw th;
                }
            }
        }
    }

    private PlaceholderSurface(b bVar, SurfaceTexture surfaceTexture, boolean z4) {
        super(surfaceTexture);
        this.f11066b = bVar;
        this.f11065a = z4;
    }

    private static int a(Context context) {
        if (GlUtil.h(context)) {
            return GlUtil.i() ? 1 : 2;
        }
        return 0;
    }

    public static synchronized boolean b(Context context) {
        boolean z4;
        synchronized (PlaceholderSurface.class) {
            if (!f11064e) {
                f11063d = a(context);
                f11064e = true;
            }
            z4 = f11063d != 0;
        }
        return z4;
    }

    public static PlaceholderSurface c(Context context, boolean z4) {
        b6.a.f(!z4 || b(context));
        return new b().a(z4 ? f11063d : 0);
    }

    @Override // android.view.Surface
    public void release() {
        super.release();
        synchronized (this.f11066b) {
            if (!this.f11067c) {
                this.f11066b.c();
                this.f11067c = true;
            }
        }
    }
}
