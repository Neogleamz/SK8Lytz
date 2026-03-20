package d4;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.google.android.datatransport.runtime.backends.BackendResponse;
import com.google.android.datatransport.runtime.firebase.transport.LogEventDropped;
import com.google.android.datatransport.runtime.synchronization.SynchronizationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;
import w3.h;
import w3.i;
import w3.o;
import x3.d;
import x3.e;
import x3.k;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class p {

    /* renamed from: a  reason: collision with root package name */
    private final Context f19711a;

    /* renamed from: b  reason: collision with root package name */
    private final d f19712b;

    /* renamed from: c  reason: collision with root package name */
    private final e4.d f19713c;

    /* renamed from: d  reason: collision with root package name */
    private final v f19714d;

    /* renamed from: e  reason: collision with root package name */
    private final Executor f19715e;

    /* renamed from: f  reason: collision with root package name */
    private final f4.a f19716f;

    /* renamed from: g  reason: collision with root package name */
    private final g4.a f19717g;

    /* renamed from: h  reason: collision with root package name */
    private final g4.a f19718h;

    /* renamed from: i  reason: collision with root package name */
    private final e4.c f19719i;

    public p(Context context, d dVar, e4.d dVar2, v vVar, Executor executor, f4.a aVar, g4.a aVar2, g4.a aVar3, e4.c cVar) {
        this.f19711a = context;
        this.f19712b = dVar;
        this.f19713c = dVar2;
        this.f19714d = vVar;
        this.f19715e = executor;
        this.f19716f = aVar;
        this.f19717g = aVar2;
        this.f19718h = aVar3;
        this.f19719i = cVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Boolean l(o oVar) {
        return Boolean.valueOf(this.f19713c.v1(oVar));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Iterable m(o oVar) {
        return this.f19713c.B0(oVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object n(Iterable iterable, o oVar, long j8) {
        this.f19713c.Q1(iterable);
        this.f19713c.E(oVar, this.f19717g.a() + j8);
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object o(Iterable iterable) {
        this.f19713c.D(iterable);
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object p() {
        this.f19719i.a();
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object q(Map map) {
        for (Map.Entry entry : map.entrySet()) {
            this.f19719i.d(((Integer) entry.getValue()).intValue(), LogEventDropped.Reason.INVALID_PAYLOD, (String) entry.getKey());
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object r(o oVar, long j8) {
        this.f19713c.E(oVar, this.f19717g.a() + j8);
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object s(o oVar, int i8) {
        this.f19714d.a(oVar, i8 + 1);
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void t(o oVar, int i8, Runnable runnable) {
        try {
            try {
                f4.a aVar = this.f19716f;
                e4.d dVar = this.f19713c;
                Objects.requireNonNull(dVar);
                aVar.b(new o(dVar));
                if (k()) {
                    u(oVar, i8);
                } else {
                    this.f19716f.b(new l(this, oVar, i8));
                }
            } catch (SynchronizationException unused) {
                this.f19714d.a(oVar, i8 + 1);
            }
        } finally {
            runnable.run();
        }
    }

    public i j(k kVar) {
        f4.a aVar = this.f19716f;
        e4.c cVar = this.f19719i;
        Objects.requireNonNull(cVar);
        return kVar.b(i.a().i(this.f19717g.a()).k(this.f19718h.a()).j("GDT_CLIENT_METRICS").h(new h(u3.c.b("proto"), ((z3.a) aVar.b(new n(cVar))).f())).d());
    }

    boolean k() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) this.f19711a.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public BackendResponse u(o oVar, int i8) {
        BackendResponse a9;
        k a10 = this.f19712b.a(oVar.b());
        long j8 = 0;
        BackendResponse e8 = BackendResponse.e(0L);
        while (true) {
            long j9 = j8;
            while (((Boolean) this.f19716f.b(new j(this, oVar))).booleanValue()) {
                Iterable<e4.k> iterable = (Iterable) this.f19716f.b(new k(this, oVar));
                if (!iterable.iterator().hasNext()) {
                    return e8;
                }
                if (a10 == null) {
                    a4.a.b("Uploader", "Unknown backend for %s, deleting event batch for it...", oVar);
                    a9 = BackendResponse.a();
                } else {
                    ArrayList arrayList = new ArrayList();
                    for (e4.k kVar : iterable) {
                        arrayList.add(kVar.b());
                    }
                    if (oVar.e()) {
                        arrayList.add(j(a10));
                    }
                    a9 = a10.a(e.a().b(arrayList).c(oVar.c()).a());
                }
                e8 = a9;
                if (e8.c() == BackendResponse.Status.TRANSIENT_ERROR) {
                    this.f19716f.b(new h(this, iterable, oVar, j9));
                    this.f19714d.b(oVar, i8 + 1, true);
                    return e8;
                }
                this.f19716f.b(new g(this, iterable));
                if (e8.c() == BackendResponse.Status.OK) {
                    j8 = Math.max(j9, e8.b());
                    if (oVar.e()) {
                        this.f19716f.b(new e(this));
                    }
                } else if (e8.c() == BackendResponse.Status.INVALID_PAYLOAD) {
                    HashMap hashMap = new HashMap();
                    for (e4.k kVar2 : iterable) {
                        String j10 = kVar2.b().j();
                        hashMap.put(j10, !hashMap.containsKey(j10) ? 1 : Integer.valueOf(((Integer) hashMap.get(j10)).intValue() + 1));
                    }
                    this.f19716f.b(new i(this, hashMap));
                }
            }
            this.f19716f.b(new m(this, oVar, j9));
            return e8;
        }
    }

    public void v(o oVar, int i8, Runnable runnable) {
        this.f19715e.execute(new f(this, oVar, i8, runnable));
    }
}
