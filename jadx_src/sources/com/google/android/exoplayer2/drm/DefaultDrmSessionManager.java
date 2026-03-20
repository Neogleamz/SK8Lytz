package com.google.android.exoplayer2.drm;

import android.annotation.SuppressLint;
import android.media.ResourceBusyException;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import b6.l0;
import b6.t;
import com.google.android.exoplayer2.drm.DefaultDrmSession;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.DrmSession;
import com.google.android.exoplayer2.drm.h;
import com.google.android.exoplayer2.drm.i;
import com.google.android.exoplayer2.drm.m;
import com.google.android.exoplayer2.w0;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.d3;
import com.google.common.collect.p2;
import j4.t1;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class DefaultDrmSessionManager implements i {

    /* renamed from: c  reason: collision with root package name */
    private final UUID f9552c;

    /* renamed from: d  reason: collision with root package name */
    private final m.c f9553d;

    /* renamed from: e  reason: collision with root package name */
    private final p f9554e;

    /* renamed from: f  reason: collision with root package name */
    private final HashMap<String, String> f9555f;

    /* renamed from: g  reason: collision with root package name */
    private final boolean f9556g;

    /* renamed from: h  reason: collision with root package name */
    private final int[] f9557h;

    /* renamed from: i  reason: collision with root package name */
    private final boolean f9558i;

    /* renamed from: j  reason: collision with root package name */
    private final f f9559j;

    /* renamed from: k  reason: collision with root package name */
    private final com.google.android.exoplayer2.upstream.c f9560k;

    /* renamed from: l  reason: collision with root package name */
    private final g f9561l;

    /* renamed from: m  reason: collision with root package name */
    private final long f9562m;

    /* renamed from: n  reason: collision with root package name */
    private final List<DefaultDrmSession> f9563n;

    /* renamed from: o  reason: collision with root package name */
    private final Set<e> f9564o;

    /* renamed from: p  reason: collision with root package name */
    private final Set<DefaultDrmSession> f9565p;
    private int q;

    /* renamed from: r  reason: collision with root package name */
    private m f9566r;

    /* renamed from: s  reason: collision with root package name */
    private DefaultDrmSession f9567s;

    /* renamed from: t  reason: collision with root package name */
    private DefaultDrmSession f9568t;

    /* renamed from: u  reason: collision with root package name */
    private Looper f9569u;

    /* renamed from: v  reason: collision with root package name */
    private Handler f9570v;

    /* renamed from: w  reason: collision with root package name */
    private int f9571w;

    /* renamed from: x  reason: collision with root package name */
    private byte[] f9572x;

    /* renamed from: y  reason: collision with root package name */
    private t1 f9573y;

    /* renamed from: z  reason: collision with root package name */
    volatile d f9574z;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class MissingSchemeDataException extends Exception {
        private MissingSchemeDataException(UUID uuid) {
            super("Media does not support uuid: " + uuid);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {

        /* renamed from: d  reason: collision with root package name */
        private boolean f9578d;

        /* renamed from: f  reason: collision with root package name */
        private boolean f9580f;

        /* renamed from: a  reason: collision with root package name */
        private final HashMap<String, String> f9575a = new HashMap<>();

        /* renamed from: b  reason: collision with root package name */
        private UUID f9576b = i4.b.f20468d;

        /* renamed from: c  reason: collision with root package name */
        private m.c f9577c = n.f9632d;

        /* renamed from: g  reason: collision with root package name */
        private com.google.android.exoplayer2.upstream.c f9581g = new com.google.android.exoplayer2.upstream.b();

        /* renamed from: e  reason: collision with root package name */
        private int[] f9579e = new int[0];

        /* renamed from: h  reason: collision with root package name */
        private long f9582h = 300000;

        public DefaultDrmSessionManager a(p pVar) {
            return new DefaultDrmSessionManager(this.f9576b, this.f9577c, pVar, this.f9575a, this.f9578d, this.f9579e, this.f9580f, this.f9581g, this.f9582h);
        }

        public b b(boolean z4) {
            this.f9578d = z4;
            return this;
        }

        public b c(boolean z4) {
            this.f9580f = z4;
            return this;
        }

        public b d(int... iArr) {
            for (int i8 : iArr) {
                boolean z4 = true;
                if (i8 != 2 && i8 != 1) {
                    z4 = false;
                }
                b6.a.a(z4);
            }
            this.f9579e = (int[]) iArr.clone();
            return this;
        }

        public b e(UUID uuid, m.c cVar) {
            this.f9576b = (UUID) b6.a.e(uuid);
            this.f9577c = (m.c) b6.a.e(cVar);
            return this;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private class c implements m.b {
        private c() {
        }

        @Override // com.google.android.exoplayer2.drm.m.b
        public void a(m mVar, byte[] bArr, int i8, int i9, byte[] bArr2) {
            ((d) b6.a.e(DefaultDrmSessionManager.this.f9574z)).obtainMessage(i8, bArr).sendToTarget();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @SuppressLint({"HandlerLeak"})
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class d extends Handler {
        public d(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            byte[] bArr = (byte[]) message.obj;
            if (bArr == null) {
                return;
            }
            for (DefaultDrmSession defaultDrmSession : DefaultDrmSessionManager.this.f9563n) {
                if (defaultDrmSession.u(bArr)) {
                    defaultDrmSession.C(message.what);
                    return;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class e implements i.b {

        /* renamed from: b  reason: collision with root package name */
        private final h.a f9585b;

        /* renamed from: c  reason: collision with root package name */
        private DrmSession f9586c;

        /* renamed from: d  reason: collision with root package name */
        private boolean f9587d;

        public e(h.a aVar) {
            this.f9585b = aVar;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void f(w0 w0Var) {
            if (DefaultDrmSessionManager.this.q == 0 || this.f9587d) {
                return;
            }
            DefaultDrmSessionManager defaultDrmSessionManager = DefaultDrmSessionManager.this;
            this.f9586c = defaultDrmSessionManager.t((Looper) b6.a.e(defaultDrmSessionManager.f9569u), this.f9585b, w0Var, false);
            DefaultDrmSessionManager.this.f9564o.add(this);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void g() {
            if (this.f9587d) {
                return;
            }
            DrmSession drmSession = this.f9586c;
            if (drmSession != null) {
                drmSession.b(this.f9585b);
            }
            DefaultDrmSessionManager.this.f9564o.remove(this);
            this.f9587d = true;
        }

        public void e(final w0 w0Var) {
            ((Handler) b6.a.e(DefaultDrmSessionManager.this.f9570v)).post(new Runnable() { // from class: com.google.android.exoplayer2.drm.e
                @Override // java.lang.Runnable
                public final void run() {
                    DefaultDrmSessionManager.e.this.f(w0Var);
                }
            });
        }

        @Override // com.google.android.exoplayer2.drm.i.b
        public void release() {
            l0.L0((Handler) b6.a.e(DefaultDrmSessionManager.this.f9570v), new Runnable() { // from class: com.google.android.exoplayer2.drm.d
                @Override // java.lang.Runnable
                public final void run() {
                    DefaultDrmSessionManager.e.this.g();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class f implements DefaultDrmSession.a {

        /* renamed from: a  reason: collision with root package name */
        private final Set<DefaultDrmSession> f9589a = new HashSet();

        /* renamed from: b  reason: collision with root package name */
        private DefaultDrmSession f9590b;

        public f(DefaultDrmSessionManager defaultDrmSessionManager) {
        }

        @Override // com.google.android.exoplayer2.drm.DefaultDrmSession.a
        public void a(Exception exc, boolean z4) {
            this.f9590b = null;
            ImmutableList x8 = ImmutableList.x(this.f9589a);
            this.f9589a.clear();
            d3 it = x8.iterator();
            while (it.hasNext()) {
                ((DefaultDrmSession) it.next()).E(exc, z4);
            }
        }

        @Override // com.google.android.exoplayer2.drm.DefaultDrmSession.a
        public void b(DefaultDrmSession defaultDrmSession) {
            this.f9589a.add(defaultDrmSession);
            if (this.f9590b != null) {
                return;
            }
            this.f9590b = defaultDrmSession;
            defaultDrmSession.I();
        }

        @Override // com.google.android.exoplayer2.drm.DefaultDrmSession.a
        public void c() {
            this.f9590b = null;
            ImmutableList x8 = ImmutableList.x(this.f9589a);
            this.f9589a.clear();
            d3 it = x8.iterator();
            while (it.hasNext()) {
                ((DefaultDrmSession) it.next()).D();
            }
        }

        public void d(DefaultDrmSession defaultDrmSession) {
            this.f9589a.remove(defaultDrmSession);
            if (this.f9590b == defaultDrmSession) {
                this.f9590b = null;
                if (this.f9589a.isEmpty()) {
                    return;
                }
                DefaultDrmSession next = this.f9589a.iterator().next();
                this.f9590b = next;
                next.I();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class g implements DefaultDrmSession.b {
        private g() {
        }

        @Override // com.google.android.exoplayer2.drm.DefaultDrmSession.b
        public void a(DefaultDrmSession defaultDrmSession, int i8) {
            if (DefaultDrmSessionManager.this.f9562m != -9223372036854775807L) {
                DefaultDrmSessionManager.this.f9565p.remove(defaultDrmSession);
                ((Handler) b6.a.e(DefaultDrmSessionManager.this.f9570v)).removeCallbacksAndMessages(defaultDrmSession);
            }
        }

        @Override // com.google.android.exoplayer2.drm.DefaultDrmSession.b
        public void b(final DefaultDrmSession defaultDrmSession, int i8) {
            if (i8 == 1 && DefaultDrmSessionManager.this.q > 0 && DefaultDrmSessionManager.this.f9562m != -9223372036854775807L) {
                DefaultDrmSessionManager.this.f9565p.add(defaultDrmSession);
                ((Handler) b6.a.e(DefaultDrmSessionManager.this.f9570v)).postAtTime(new Runnable() { // from class: com.google.android.exoplayer2.drm.f
                    @Override // java.lang.Runnable
                    public final void run() {
                        DefaultDrmSession.this.b(null);
                    }
                }, defaultDrmSession, SystemClock.uptimeMillis() + DefaultDrmSessionManager.this.f9562m);
            } else if (i8 == 0) {
                DefaultDrmSessionManager.this.f9563n.remove(defaultDrmSession);
                if (DefaultDrmSessionManager.this.f9567s == defaultDrmSession) {
                    DefaultDrmSessionManager.this.f9567s = null;
                }
                if (DefaultDrmSessionManager.this.f9568t == defaultDrmSession) {
                    DefaultDrmSessionManager.this.f9568t = null;
                }
                DefaultDrmSessionManager.this.f9559j.d(defaultDrmSession);
                if (DefaultDrmSessionManager.this.f9562m != -9223372036854775807L) {
                    ((Handler) b6.a.e(DefaultDrmSessionManager.this.f9570v)).removeCallbacksAndMessages(defaultDrmSession);
                    DefaultDrmSessionManager.this.f9565p.remove(defaultDrmSession);
                }
            }
            DefaultDrmSessionManager.this.C();
        }
    }

    private DefaultDrmSessionManager(UUID uuid, m.c cVar, p pVar, HashMap<String, String> hashMap, boolean z4, int[] iArr, boolean z8, com.google.android.exoplayer2.upstream.c cVar2, long j8) {
        b6.a.e(uuid);
        b6.a.b(!i4.b.f20466b.equals(uuid), "Use C.CLEARKEY_UUID instead");
        this.f9552c = uuid;
        this.f9553d = cVar;
        this.f9554e = pVar;
        this.f9555f = hashMap;
        this.f9556g = z4;
        this.f9557h = iArr;
        this.f9558i = z8;
        this.f9560k = cVar2;
        this.f9559j = new f(this);
        this.f9561l = new g();
        this.f9571w = 0;
        this.f9563n = new ArrayList();
        this.f9564o = p2.h();
        this.f9565p = p2.h();
        this.f9562m = j8;
    }

    private DrmSession A(int i8, boolean z4) {
        m mVar = (m) b6.a.e(this.f9566r);
        if ((mVar.l() == 2 && m4.l.f21834d) || l0.z0(this.f9557h, i8) == -1 || mVar.l() == 1) {
            return null;
        }
        DefaultDrmSession defaultDrmSession = this.f9567s;
        if (defaultDrmSession == null) {
            DefaultDrmSession x8 = x(ImmutableList.E(), true, null, z4);
            this.f9563n.add(x8);
            this.f9567s = x8;
        } else {
            defaultDrmSession.a(null);
        }
        return this.f9567s;
    }

    private void B(Looper looper) {
        if (this.f9574z == null) {
            this.f9574z = new d(looper);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void C() {
        if (this.f9566r != null && this.q == 0 && this.f9563n.isEmpty() && this.f9564o.isEmpty()) {
            ((m) b6.a.e(this.f9566r)).release();
            this.f9566r = null;
        }
    }

    private void D() {
        d3 it = ImmutableSet.A(this.f9565p).iterator();
        while (it.hasNext()) {
            ((DrmSession) it.next()).b(null);
        }
    }

    private void E() {
        d3 it = ImmutableSet.A(this.f9564o).iterator();
        while (it.hasNext()) {
            ((e) it.next()).release();
        }
    }

    private void G(DrmSession drmSession, h.a aVar) {
        drmSession.b(aVar);
        if (this.f9562m != -9223372036854775807L) {
            drmSession.b(null);
        }
    }

    private void H(boolean z4) {
        if (z4 && this.f9569u == null) {
            b6.p.j("DefaultDrmSessionMgr", "DefaultDrmSessionManager accessed before setPlayer(), possibly on the wrong thread.", new IllegalStateException());
        } else if (Thread.currentThread() != ((Looper) b6.a.e(this.f9569u)).getThread()) {
            b6.p.j("DefaultDrmSessionMgr", "DefaultDrmSessionManager accessed on the wrong thread.\nCurrent thread: " + Thread.currentThread().getName() + "\nExpected thread: " + this.f9569u.getThread().getName(), new IllegalStateException());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public DrmSession t(Looper looper, h.a aVar, w0 w0Var, boolean z4) {
        List<DrmInitData.SchemeData> list;
        B(looper);
        DrmInitData drmInitData = w0Var.q;
        if (drmInitData == null) {
            return A(t.k(w0Var.f11207m), z4);
        }
        DefaultDrmSession defaultDrmSession = null;
        if (this.f9572x == null) {
            list = y((DrmInitData) b6.a.e(drmInitData), this.f9552c, false);
            if (list.isEmpty()) {
                MissingSchemeDataException missingSchemeDataException = new MissingSchemeDataException(this.f9552c);
                b6.p.d("DefaultDrmSessionMgr", "DRM error", missingSchemeDataException);
                if (aVar != null) {
                    aVar.l(missingSchemeDataException);
                }
                return new l(new DrmSession.DrmSessionException(missingSchemeDataException, 6003));
            }
        } else {
            list = null;
        }
        if (this.f9556g) {
            Iterator<DefaultDrmSession> it = this.f9563n.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                DefaultDrmSession next = it.next();
                if (l0.c(next.f9520a, list)) {
                    defaultDrmSession = next;
                    break;
                }
            }
        } else {
            defaultDrmSession = this.f9568t;
        }
        if (defaultDrmSession == null) {
            defaultDrmSession = x(list, false, aVar, z4);
            if (!this.f9556g) {
                this.f9568t = defaultDrmSession;
            }
            this.f9563n.add(defaultDrmSession);
        } else {
            defaultDrmSession.a(aVar);
        }
        return defaultDrmSession;
    }

    private static boolean u(DrmSession drmSession) {
        return drmSession.getState() == 1 && (l0.f8063a < 19 || (((DrmSession.DrmSessionException) b6.a.e(drmSession.h())).getCause() instanceof ResourceBusyException));
    }

    private boolean v(DrmInitData drmInitData) {
        if (this.f9572x != null) {
            return true;
        }
        if (y(drmInitData, this.f9552c, true).isEmpty()) {
            if (drmInitData.f9595d != 1 || !drmInitData.e(0).d(i4.b.f20466b)) {
                return false;
            }
            b6.p.i("DefaultDrmSessionMgr", "DrmInitData only contains common PSSH SchemeData. Assuming support for: " + this.f9552c);
        }
        String str = drmInitData.f9594c;
        if (str == null || "cenc".equals(str)) {
            return true;
        }
        return "cbcs".equals(str) ? l0.f8063a >= 25 : ("cbc1".equals(str) || "cens".equals(str)) ? false : true;
    }

    private DefaultDrmSession w(List<DrmInitData.SchemeData> list, boolean z4, h.a aVar) {
        b6.a.e(this.f9566r);
        DefaultDrmSession defaultDrmSession = new DefaultDrmSession(this.f9552c, this.f9566r, this.f9559j, this.f9561l, list, this.f9571w, this.f9558i | z4, z4, this.f9572x, this.f9555f, this.f9554e, (Looper) b6.a.e(this.f9569u), this.f9560k, (t1) b6.a.e(this.f9573y));
        defaultDrmSession.a(aVar);
        if (this.f9562m != -9223372036854775807L) {
            defaultDrmSession.a(null);
        }
        return defaultDrmSession;
    }

    private DefaultDrmSession x(List<DrmInitData.SchemeData> list, boolean z4, h.a aVar, boolean z8) {
        DefaultDrmSession w8 = w(list, z4, aVar);
        if (u(w8) && !this.f9565p.isEmpty()) {
            D();
            G(w8, aVar);
            w8 = w(list, z4, aVar);
        }
        if (u(w8) && z8 && !this.f9564o.isEmpty()) {
            E();
            if (!this.f9565p.isEmpty()) {
                D();
            }
            G(w8, aVar);
            return w(list, z4, aVar);
        }
        return w8;
    }

    private static List<DrmInitData.SchemeData> y(DrmInitData drmInitData, UUID uuid, boolean z4) {
        ArrayList arrayList = new ArrayList(drmInitData.f9595d);
        for (int i8 = 0; i8 < drmInitData.f9595d; i8++) {
            DrmInitData.SchemeData e8 = drmInitData.e(i8);
            if ((e8.d(uuid) || (i4.b.f20467c.equals(uuid) && e8.d(i4.b.f20466b))) && (e8.f9600e != null || z4)) {
                arrayList.add(e8);
            }
        }
        return arrayList;
    }

    private synchronized void z(Looper looper) {
        Looper looper2 = this.f9569u;
        if (looper2 == null) {
            this.f9569u = looper;
            this.f9570v = new Handler(looper);
        } else {
            b6.a.f(looper2 == looper);
            b6.a.e(this.f9570v);
        }
    }

    public void F(int i8, byte[] bArr) {
        b6.a.f(this.f9563n.isEmpty());
        if (i8 == 1 || i8 == 3) {
            b6.a.e(bArr);
        }
        this.f9571w = i8;
        this.f9572x = bArr;
    }

    @Override // com.google.android.exoplayer2.drm.i
    public final void a() {
        H(true);
        int i8 = this.q;
        this.q = i8 + 1;
        if (i8 != 0) {
            return;
        }
        if (this.f9566r == null) {
            m a9 = this.f9553d.a(this.f9552c);
            this.f9566r = a9;
            a9.h(new c());
        } else if (this.f9562m != -9223372036854775807L) {
            for (int i9 = 0; i9 < this.f9563n.size(); i9++) {
                this.f9563n.get(i9).a(null);
            }
        }
    }

    @Override // com.google.android.exoplayer2.drm.i
    public void b(Looper looper, t1 t1Var) {
        z(looper);
        this.f9573y = t1Var;
    }

    @Override // com.google.android.exoplayer2.drm.i
    public int c(w0 w0Var) {
        H(false);
        int l8 = ((m) b6.a.e(this.f9566r)).l();
        DrmInitData drmInitData = w0Var.q;
        if (drmInitData != null) {
            if (v(drmInitData)) {
                return l8;
            }
            return 1;
        }
        if (l0.z0(this.f9557h, t.k(w0Var.f11207m)) != -1) {
            return l8;
        }
        return 0;
    }

    @Override // com.google.android.exoplayer2.drm.i
    public DrmSession d(h.a aVar, w0 w0Var) {
        H(false);
        b6.a.f(this.q > 0);
        b6.a.h(this.f9569u);
        return t(this.f9569u, aVar, w0Var, true);
    }

    @Override // com.google.android.exoplayer2.drm.i
    public i.b e(h.a aVar, w0 w0Var) {
        b6.a.f(this.q > 0);
        b6.a.h(this.f9569u);
        e eVar = new e(aVar);
        eVar.e(w0Var);
        return eVar;
    }

    @Override // com.google.android.exoplayer2.drm.i
    public final void release() {
        H(true);
        int i8 = this.q - 1;
        this.q = i8;
        if (i8 != 0) {
            return;
        }
        if (this.f9562m != -9223372036854775807L) {
            ArrayList arrayList = new ArrayList(this.f9563n);
            for (int i9 = 0; i9 < arrayList.size(); i9++) {
                ((DefaultDrmSession) arrayList.get(i9)).b(null);
            }
        }
        E();
        C();
    }
}
