package com.google.android.exoplayer2.drm;

import android.annotation.SuppressLint;
import android.media.NotProvisionedException;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Pair;
import b6.l0;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.DrmSession;
import com.google.android.exoplayer2.drm.h;
import com.google.android.exoplayer2.drm.m;
import com.google.android.exoplayer2.upstream.c;
import j4.t1;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class DefaultDrmSession implements DrmSession {

    /* renamed from: a  reason: collision with root package name */
    public final List<DrmInitData.SchemeData> f9520a;

    /* renamed from: b  reason: collision with root package name */
    private final m f9521b;

    /* renamed from: c  reason: collision with root package name */
    private final a f9522c;

    /* renamed from: d  reason: collision with root package name */
    private final b f9523d;

    /* renamed from: e  reason: collision with root package name */
    private final int f9524e;

    /* renamed from: f  reason: collision with root package name */
    private final boolean f9525f;

    /* renamed from: g  reason: collision with root package name */
    private final boolean f9526g;

    /* renamed from: h  reason: collision with root package name */
    private final HashMap<String, String> f9527h;

    /* renamed from: i  reason: collision with root package name */
    private final b6.i<h.a> f9528i;

    /* renamed from: j  reason: collision with root package name */
    private final com.google.android.exoplayer2.upstream.c f9529j;

    /* renamed from: k  reason: collision with root package name */
    private final t1 f9530k;

    /* renamed from: l  reason: collision with root package name */
    private final p f9531l;

    /* renamed from: m  reason: collision with root package name */
    private final UUID f9532m;

    /* renamed from: n  reason: collision with root package name */
    private final Looper f9533n;

    /* renamed from: o  reason: collision with root package name */
    private final e f9534o;

    /* renamed from: p  reason: collision with root package name */
    private int f9535p;
    private int q;

    /* renamed from: r  reason: collision with root package name */
    private HandlerThread f9536r;

    /* renamed from: s  reason: collision with root package name */
    private c f9537s;

    /* renamed from: t  reason: collision with root package name */
    private l4.b f9538t;

    /* renamed from: u  reason: collision with root package name */
    private DrmSession.DrmSessionException f9539u;

    /* renamed from: v  reason: collision with root package name */
    private byte[] f9540v;

    /* renamed from: w  reason: collision with root package name */
    private byte[] f9541w;

    /* renamed from: x  reason: collision with root package name */
    private m.a f9542x;

    /* renamed from: y  reason: collision with root package name */
    private m.d f9543y;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class UnexpectedDrmSessionException extends IOException {
        public UnexpectedDrmSessionException(Throwable th) {
            super(th);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a {
        void a(Exception exc, boolean z4);

        void b(DefaultDrmSession defaultDrmSession);

        void c();
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b {
        void a(DefaultDrmSession defaultDrmSession, int i8);

        void b(DefaultDrmSession defaultDrmSession, int i8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @SuppressLint({"HandlerLeak"})
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class c extends Handler {

        /* renamed from: a  reason: collision with root package name */
        private boolean f9544a;

        public c(Looper looper) {
            super(looper);
        }

        private boolean a(Message message, MediaDrmCallbackException mediaDrmCallbackException) {
            d dVar = (d) message.obj;
            if (dVar.f9547b) {
                int i8 = dVar.f9550e + 1;
                dVar.f9550e = i8;
                if (i8 > DefaultDrmSession.this.f9529j.d(3)) {
                    return false;
                }
                long a9 = DefaultDrmSession.this.f9529j.a(new c.C0117c(new h5.h(dVar.f9546a, mediaDrmCallbackException.f9602a, mediaDrmCallbackException.f9603b, mediaDrmCallbackException.f9604c, SystemClock.elapsedRealtime(), SystemClock.elapsedRealtime() - dVar.f9548c, mediaDrmCallbackException.f9605d), new h5.i(3), mediaDrmCallbackException.getCause() instanceof IOException ? (IOException) mediaDrmCallbackException.getCause() : new UnexpectedDrmSessionException(mediaDrmCallbackException.getCause()), dVar.f9550e));
                if (a9 == -9223372036854775807L) {
                    return false;
                }
                synchronized (this) {
                    if (this.f9544a) {
                        return false;
                    }
                    sendMessageDelayed(Message.obtain(message), a9);
                    return true;
                }
            }
            return false;
        }

        void b(int i8, Object obj, boolean z4) {
            obtainMessage(i8, new d(h5.h.a(), z4, SystemClock.elapsedRealtime(), obj)).sendToTarget();
        }

        public synchronized void c() {
            removeCallbacksAndMessages(null);
            this.f9544a = true;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r1v0, types: [java.lang.Throwable, java.lang.Exception] */
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            byte[] bArr;
            d dVar = (d) message.obj;
            try {
                int i8 = message.what;
                if (i8 == 0) {
                    bArr = DefaultDrmSession.this.f9531l.b(DefaultDrmSession.this.f9532m, (m.d) dVar.f9549d);
                } else if (i8 != 1) {
                    throw new RuntimeException();
                } else {
                    bArr = DefaultDrmSession.this.f9531l.a(DefaultDrmSession.this.f9532m, (m.a) dVar.f9549d);
                }
            } catch (MediaDrmCallbackException e8) {
                boolean a9 = a(message, e8);
                bArr = e8;
                if (a9) {
                    return;
                }
            } catch (Exception e9) {
                b6.p.j("DefaultDrmSession", "Key/provisioning request produced an unexpected exception. Not retrying.", e9);
                bArr = e9;
            }
            DefaultDrmSession.this.f9529j.c(dVar.f9546a);
            synchronized (this) {
                if (!this.f9544a) {
                    DefaultDrmSession.this.f9534o.obtainMessage(message.what, Pair.create(dVar.f9549d, bArr)).sendToTarget();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class d {

        /* renamed from: a  reason: collision with root package name */
        public final long f9546a;

        /* renamed from: b  reason: collision with root package name */
        public final boolean f9547b;

        /* renamed from: c  reason: collision with root package name */
        public final long f9548c;

        /* renamed from: d  reason: collision with root package name */
        public final Object f9549d;

        /* renamed from: e  reason: collision with root package name */
        public int f9550e;

        public d(long j8, boolean z4, long j9, Object obj) {
            this.f9546a = j8;
            this.f9547b = z4;
            this.f9548c = j9;
            this.f9549d = obj;
        }
    }

    @SuppressLint({"HandlerLeak"})
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private class e extends Handler {
        public e(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            Pair pair = (Pair) message.obj;
            Object obj = pair.first;
            Object obj2 = pair.second;
            int i8 = message.what;
            if (i8 == 0) {
                DefaultDrmSession.this.F(obj, obj2);
            } else if (i8 != 1) {
            } else {
                DefaultDrmSession.this.z(obj, obj2);
            }
        }
    }

    public DefaultDrmSession(UUID uuid, m mVar, a aVar, b bVar, List<DrmInitData.SchemeData> list, int i8, boolean z4, boolean z8, byte[] bArr, HashMap<String, String> hashMap, p pVar, Looper looper, com.google.android.exoplayer2.upstream.c cVar, t1 t1Var) {
        List<DrmInitData.SchemeData> unmodifiableList;
        if (i8 == 1 || i8 == 3) {
            b6.a.e(bArr);
        }
        this.f9532m = uuid;
        this.f9522c = aVar;
        this.f9523d = bVar;
        this.f9521b = mVar;
        this.f9524e = i8;
        this.f9525f = z4;
        this.f9526g = z8;
        if (bArr != null) {
            this.f9541w = bArr;
            unmodifiableList = null;
        } else {
            unmodifiableList = Collections.unmodifiableList((List) b6.a.e(list));
        }
        this.f9520a = unmodifiableList;
        this.f9527h = hashMap;
        this.f9531l = pVar;
        this.f9528i = new b6.i<>();
        this.f9529j = cVar;
        this.f9530k = t1Var;
        this.f9535p = 2;
        this.f9533n = looper;
        this.f9534o = new e(looper);
    }

    private void A(Exception exc, boolean z4) {
        if (exc instanceof NotProvisionedException) {
            this.f9522c.b(this);
        } else {
            y(exc, z4 ? 1 : 2);
        }
    }

    private void B() {
        if (this.f9524e == 0 && this.f9535p == 4) {
            l0.j(this.f9540v);
            s(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void F(Object obj, Object obj2) {
        if (obj == this.f9543y) {
            if (this.f9535p == 2 || v()) {
                this.f9543y = null;
                if (obj2 instanceof Exception) {
                    this.f9522c.a((Exception) obj2, false);
                    return;
                }
                try {
                    this.f9521b.j((byte[]) obj2);
                    this.f9522c.c();
                } catch (Exception e8) {
                    this.f9522c.a(e8, true);
                }
            }
        }
    }

    private boolean G() {
        if (v()) {
            return true;
        }
        try {
            byte[] d8 = this.f9521b.d();
            this.f9540v = d8;
            this.f9521b.m(d8, this.f9530k);
            this.f9538t = this.f9521b.c(this.f9540v);
            this.f9535p = 3;
            r(new b6.h() { // from class: com.google.android.exoplayer2.drm.b
                @Override // b6.h
                public final void accept(Object obj) {
                    ((h.a) obj).k(r1);
                }
            });
            b6.a.e(this.f9540v);
            return true;
        } catch (NotProvisionedException unused) {
            this.f9522c.b(this);
            return false;
        } catch (Exception e8) {
            y(e8, 1);
            return false;
        }
    }

    private void H(byte[] bArr, int i8, boolean z4) {
        try {
            this.f9542x = this.f9521b.k(bArr, this.f9520a, i8, this.f9527h);
            ((c) l0.j(this.f9537s)).b(1, b6.a.e(this.f9542x), z4);
        } catch (Exception e8) {
            A(e8, true);
        }
    }

    private boolean J() {
        try {
            this.f9521b.f(this.f9540v, this.f9541w);
            return true;
        } catch (Exception e8) {
            y(e8, 1);
            return false;
        }
    }

    private void K() {
        if (Thread.currentThread() != this.f9533n.getThread()) {
            b6.p.j("DefaultDrmSession", "DefaultDrmSession accessed on the wrong thread.\nCurrent thread: " + Thread.currentThread().getName() + "\nExpected thread: " + this.f9533n.getThread().getName(), new IllegalStateException());
        }
    }

    private void r(b6.h<h.a> hVar) {
        for (h.a aVar : this.f9528i.l()) {
            hVar.accept(aVar);
        }
    }

    private void s(boolean z4) {
        if (this.f9526g) {
            return;
        }
        byte[] bArr = (byte[]) l0.j(this.f9540v);
        int i8 = this.f9524e;
        if (i8 == 0 || i8 == 1) {
            if (this.f9541w == null) {
                H(bArr, 1, z4);
                return;
            } else if (this.f9535p != 4 && !J()) {
                return;
            } else {
                long t8 = t();
                if (this.f9524e != 0 || t8 > 60) {
                    if (t8 <= 0) {
                        y(new KeysExpiredException(), 2);
                        return;
                    }
                    this.f9535p = 4;
                    r(m4.c.a);
                    return;
                }
                b6.p.b("DefaultDrmSession", "Offline license has expired or will expire soon. Remaining seconds: " + t8);
            }
        } else if (i8 != 2) {
            if (i8 != 3) {
                return;
            }
            b6.a.e(this.f9541w);
            b6.a.e(this.f9540v);
            H(this.f9541w, 3, z4);
            return;
        } else if (this.f9541w != null && !J()) {
            return;
        }
        H(bArr, 2, z4);
    }

    private long t() {
        if (i4.b.f20468d.equals(this.f9532m)) {
            Pair pair = (Pair) b6.a.e(m4.o.b(this));
            return Math.min(((Long) pair.first).longValue(), ((Long) pair.second).longValue());
        }
        return Long.MAX_VALUE;
    }

    private boolean v() {
        int i8 = this.f9535p;
        return i8 == 3 || i8 == 4;
    }

    private void y(final Exception exc, int i8) {
        this.f9539u = new DrmSession.DrmSessionException(exc, j.a(exc, i8));
        b6.p.d("DefaultDrmSession", "DRM session error", exc);
        r(new b6.h() { // from class: com.google.android.exoplayer2.drm.c
            @Override // b6.h
            public final void accept(Object obj) {
                ((h.a) obj).l(exc);
            }
        });
        if (this.f9535p != 4) {
            this.f9535p = 1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void z(Object obj, Object obj2) {
        m4.b bVar;
        if (obj == this.f9542x && v()) {
            this.f9542x = null;
            if (obj2 instanceof Exception) {
                A((Exception) obj2, false);
                return;
            }
            try {
                byte[] bArr = (byte[]) obj2;
                if (this.f9524e == 3) {
                    this.f9521b.i((byte[]) l0.j(this.f9541w), bArr);
                    bVar = m4.b.a;
                } else {
                    byte[] i8 = this.f9521b.i(this.f9540v, bArr);
                    int i9 = this.f9524e;
                    if ((i9 == 2 || (i9 == 0 && this.f9541w != null)) && i8 != null && i8.length != 0) {
                        this.f9541w = i8;
                    }
                    this.f9535p = 4;
                    bVar = m4.a.a;
                }
                r(bVar);
            } catch (Exception e8) {
                A(e8, true);
            }
        }
    }

    public void C(int i8) {
        if (i8 != 2) {
            return;
        }
        B();
    }

    public void D() {
        if (G()) {
            s(true);
        }
    }

    public void E(Exception exc, boolean z4) {
        y(exc, z4 ? 1 : 3);
    }

    public void I() {
        this.f9543y = this.f9521b.b();
        ((c) l0.j(this.f9537s)).b(0, b6.a.e(this.f9543y), true);
    }

    @Override // com.google.android.exoplayer2.drm.DrmSession
    public void a(h.a aVar) {
        K();
        if (this.q < 0) {
            b6.p.c("DefaultDrmSession", "Session reference count less than zero: " + this.q);
            this.q = 0;
        }
        if (aVar != null) {
            this.f9528i.e(aVar);
        }
        int i8 = this.q + 1;
        this.q = i8;
        if (i8 == 1) {
            b6.a.f(this.f9535p == 2);
            HandlerThread handlerThread = new HandlerThread("ExoPlayer:DrmRequestHandler");
            this.f9536r = handlerThread;
            handlerThread.start();
            this.f9537s = new c(this.f9536r.getLooper());
            if (G()) {
                s(true);
            }
        } else if (aVar != null && v() && this.f9528i.m0(aVar) == 1) {
            aVar.k(this.f9535p);
        }
        this.f9523d.a(this, this.q);
    }

    @Override // com.google.android.exoplayer2.drm.DrmSession
    public void b(h.a aVar) {
        K();
        int i8 = this.q;
        if (i8 <= 0) {
            b6.p.c("DefaultDrmSession", "release() called on a session that's already fully released.");
            return;
        }
        int i9 = i8 - 1;
        this.q = i9;
        if (i9 == 0) {
            this.f9535p = 0;
            ((e) l0.j(this.f9534o)).removeCallbacksAndMessages(null);
            ((c) l0.j(this.f9537s)).c();
            this.f9537s = null;
            ((HandlerThread) l0.j(this.f9536r)).quit();
            this.f9536r = null;
            this.f9538t = null;
            this.f9539u = null;
            this.f9542x = null;
            this.f9543y = null;
            byte[] bArr = this.f9540v;
            if (bArr != null) {
                this.f9521b.g(bArr);
                this.f9540v = null;
            }
        }
        if (aVar != null) {
            this.f9528i.g(aVar);
            if (this.f9528i.m0(aVar) == 0) {
                aVar.m();
            }
        }
        this.f9523d.b(this, this.q);
    }

    @Override // com.google.android.exoplayer2.drm.DrmSession
    public final UUID d() {
        K();
        return this.f9532m;
    }

    @Override // com.google.android.exoplayer2.drm.DrmSession
    public boolean e() {
        K();
        return this.f9525f;
    }

    @Override // com.google.android.exoplayer2.drm.DrmSession
    public Map<String, String> f() {
        K();
        byte[] bArr = this.f9540v;
        if (bArr == null) {
            return null;
        }
        return this.f9521b.a(bArr);
    }

    @Override // com.google.android.exoplayer2.drm.DrmSession
    public boolean g(String str) {
        K();
        return this.f9521b.e((byte[]) b6.a.h(this.f9540v), str);
    }

    @Override // com.google.android.exoplayer2.drm.DrmSession
    public final int getState() {
        K();
        return this.f9535p;
    }

    @Override // com.google.android.exoplayer2.drm.DrmSession
    public final DrmSession.DrmSessionException h() {
        K();
        if (this.f9535p == 1) {
            return this.f9539u;
        }
        return null;
    }

    @Override // com.google.android.exoplayer2.drm.DrmSession
    public final l4.b i() {
        K();
        return this.f9538t;
    }

    public boolean u(byte[] bArr) {
        K();
        return Arrays.equals(this.f9540v, bArr);
    }
}
