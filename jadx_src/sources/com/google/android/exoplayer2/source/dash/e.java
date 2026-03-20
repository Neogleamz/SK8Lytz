package com.google.android.exoplayer2.source.dash;

import a6.f;
import android.os.Handler;
import android.os.Message;
import b6.l0;
import b6.z;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.emsg.EventMessage;
import com.google.android.exoplayer2.source.v;
import com.google.android.exoplayer2.w0;
import i4.s;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import n4.b0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e implements Handler.Callback {

    /* renamed from: a  reason: collision with root package name */
    private final a6.b f10404a;

    /* renamed from: b  reason: collision with root package name */
    private final b f10405b;

    /* renamed from: f  reason: collision with root package name */
    private l5.c f10409f;

    /* renamed from: g  reason: collision with root package name */
    private long f10410g;

    /* renamed from: h  reason: collision with root package name */
    private boolean f10411h;

    /* renamed from: j  reason: collision with root package name */
    private boolean f10412j;

    /* renamed from: k  reason: collision with root package name */
    private boolean f10413k;

    /* renamed from: e  reason: collision with root package name */
    private final TreeMap<Long, Long> f10408e = new TreeMap<>();

    /* renamed from: d  reason: collision with root package name */
    private final Handler f10407d = l0.x(this);

    /* renamed from: c  reason: collision with root package name */
    private final c5.a f10406c = new c5.a();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        public final long f10414a;

        /* renamed from: b  reason: collision with root package name */
        public final long f10415b;

        public a(long j8, long j9) {
            this.f10414a = j8;
            this.f10415b = j9;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b {
        void a();

        void b(long j8);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public final class c implements b0 {

        /* renamed from: a  reason: collision with root package name */
        private final v f10416a;

        /* renamed from: b  reason: collision with root package name */
        private final s f10417b = new s();

        /* renamed from: c  reason: collision with root package name */
        private final a5.c f10418c = new a5.c();

        /* renamed from: d  reason: collision with root package name */
        private long f10419d = -9223372036854775807L;

        c(a6.b bVar) {
            this.f10416a = v.l(bVar);
        }

        private a5.c g() {
            this.f10418c.k();
            if (this.f10416a.S(this.f10417b, this.f10418c, 0, false) == -4) {
                this.f10418c.A();
                return this.f10418c;
            }
            return null;
        }

        private void k(long j8, long j9) {
            e.this.f10407d.sendMessage(e.this.f10407d.obtainMessage(1, new a(j8, j9)));
        }

        private void l() {
            while (this.f10416a.K(false)) {
                a5.c g8 = g();
                if (g8 != null) {
                    long j8 = g8.f9514e;
                    Metadata a9 = e.this.f10406c.a(g8);
                    if (a9 != null) {
                        EventMessage eventMessage = (EventMessage) a9.d(0);
                        if (e.h(eventMessage.f10061a, eventMessage.f10062b)) {
                            m(j8, eventMessage);
                        }
                    }
                }
            }
            this.f10416a.s();
        }

        private void m(long j8, EventMessage eventMessage) {
            long f5 = e.f(eventMessage);
            if (f5 == -9223372036854775807L) {
                return;
            }
            k(j8, f5);
        }

        @Override // n4.b0
        public int a(f fVar, int i8, boolean z4, int i9) {
            return this.f10416a.c(fVar, i8, z4);
        }

        @Override // n4.b0
        public void d(long j8, int i8, int i9, int i10, b0.a aVar) {
            this.f10416a.d(j8, i8, i9, i10, aVar);
            l();
        }

        @Override // n4.b0
        public void e(z zVar, int i8, int i9) {
            this.f10416a.b(zVar, i8);
        }

        @Override // n4.b0
        public void f(w0 w0Var) {
            this.f10416a.f(w0Var);
        }

        public boolean h(long j8) {
            return e.this.j(j8);
        }

        public void i(j5.f fVar) {
            long j8 = this.f10419d;
            if (j8 == -9223372036854775807L || fVar.f20748h > j8) {
                this.f10419d = fVar.f20748h;
            }
            e.this.m(fVar);
        }

        public boolean j(j5.f fVar) {
            long j8 = this.f10419d;
            return e.this.n(j8 != -9223372036854775807L && j8 < fVar.f20747g);
        }

        public void n() {
            this.f10416a.T();
        }
    }

    public e(l5.c cVar, b bVar, a6.b bVar2) {
        this.f10409f = cVar;
        this.f10405b = bVar;
        this.f10404a = bVar2;
    }

    private Map.Entry<Long, Long> e(long j8) {
        return this.f10408e.ceilingEntry(Long.valueOf(j8));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static long f(EventMessage eventMessage) {
        try {
            return l0.J0(l0.D(eventMessage.f10065e));
        } catch (ParserException unused) {
            return -9223372036854775807L;
        }
    }

    private void g(long j8, long j9) {
        Long l8 = this.f10408e.get(Long.valueOf(j9));
        if (l8 != null && l8.longValue() <= j8) {
            return;
        }
        this.f10408e.put(Long.valueOf(j9), Long.valueOf(j8));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean h(String str, String str2) {
        return "urn:mpeg:dash:event:2012".equals(str) && ("1".equals(str2) || "2".equals(str2) || "3".equals(str2));
    }

    private void i() {
        if (this.f10411h) {
            this.f10412j = true;
            this.f10411h = false;
            this.f10405b.a();
        }
    }

    private void l() {
        this.f10405b.b(this.f10410g);
    }

    private void p() {
        Iterator<Map.Entry<Long, Long>> it = this.f10408e.entrySet().iterator();
        while (it.hasNext()) {
            if (it.next().getKey().longValue() < this.f10409f.f21641h) {
                it.remove();
            }
        }
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        if (this.f10413k) {
            return true;
        }
        if (message.what != 1) {
            return false;
        }
        a aVar = (a) message.obj;
        g(aVar.f10414a, aVar.f10415b);
        return true;
    }

    boolean j(long j8) {
        l5.c cVar = this.f10409f;
        boolean z4 = false;
        if (cVar.f21637d) {
            if (this.f10412j) {
                return true;
            }
            Map.Entry<Long, Long> e8 = e(cVar.f21641h);
            if (e8 != null && e8.getValue().longValue() < j8) {
                this.f10410g = e8.getKey().longValue();
                l();
                z4 = true;
            }
            if (z4) {
                i();
            }
            return z4;
        }
        return false;
    }

    public c k() {
        return new c(this.f10404a);
    }

    void m(j5.f fVar) {
        this.f10411h = true;
    }

    boolean n(boolean z4) {
        if (this.f10409f.f21637d) {
            if (this.f10412j) {
                return true;
            }
            if (z4) {
                i();
                return true;
            }
            return false;
        }
        return false;
    }

    public void o() {
        this.f10413k = true;
        this.f10407d.removeCallbacksAndMessages(null);
    }

    public void q(l5.c cVar) {
        this.f10412j = false;
        this.f10410g = -9223372036854775807L;
        this.f10409f = cVar;
        p();
    }
}
