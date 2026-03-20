package androidx.camera.view;

import androidx.camera.core.impl.CameraInternal;
import androidx.camera.core.p1;
import androidx.camera.view.PreviewView;
import androidx.concurrent.futures.c;
import java.util.ArrayList;
import java.util.List;
import y.p0;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e implements p0.a<CameraInternal.State> {

    /* renamed from: a  reason: collision with root package name */
    private final y.q f2991a;

    /* renamed from: b  reason: collision with root package name */
    private final androidx.lifecycle.p<PreviewView.StreamState> f2992b;

    /* renamed from: c  reason: collision with root package name */
    private PreviewView.StreamState f2993c;

    /* renamed from: d  reason: collision with root package name */
    private final k f2994d;

    /* renamed from: e  reason: collision with root package name */
    com.google.common.util.concurrent.d<Void> f2995e;

    /* renamed from: f  reason: collision with root package name */
    private boolean f2996f = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements a0.c<Void> {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ List f2997a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ androidx.camera.core.s f2998b;

        a(List list, androidx.camera.core.s sVar) {
            this.f2997a = list;
            this.f2998b = sVar;
        }

        @Override // a0.c
        /* renamed from: a */
        public void c(Void r22) {
            e.this.f2995e = null;
        }

        @Override // a0.c
        public void onFailure(Throwable th) {
            e.this.f2995e = null;
            if (this.f2997a.isEmpty()) {
                return;
            }
            for (y.h hVar : this.f2997a) {
                ((y.q) this.f2998b).i(hVar);
            }
            this.f2997a.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b extends y.h {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ c.a f3000a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ androidx.camera.core.s f3001b;

        b(c.a aVar, androidx.camera.core.s sVar) {
            this.f3000a = aVar;
            this.f3001b = sVar;
        }

        @Override // y.h
        public void b(y.j jVar) {
            this.f3000a.c(null);
            ((y.q) this.f3001b).i(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public e(y.q qVar, androidx.lifecycle.p<PreviewView.StreamState> pVar, k kVar) {
        this.f2991a = qVar;
        this.f2992b = pVar;
        this.f2994d = kVar;
        synchronized (this) {
            this.f2993c = pVar.e();
        }
    }

    private void e() {
        com.google.common.util.concurrent.d<Void> dVar = this.f2995e;
        if (dVar != null) {
            dVar.cancel(false);
            this.f2995e = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ com.google.common.util.concurrent.d g(Void r12) {
        return this.f2994d.j();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Void h(Void r12) {
        l(PreviewView.StreamState.STREAMING);
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object i(androidx.camera.core.s sVar, List list, c.a aVar) {
        b bVar = new b(aVar, sVar);
        list.add(bVar);
        ((y.q) sVar).e(z.a.a(), bVar);
        return "waitForCaptureResult";
    }

    private void k(androidx.camera.core.s sVar) {
        l(PreviewView.StreamState.IDLE);
        ArrayList arrayList = new ArrayList();
        a0.d e8 = a0.d.a(m(sVar, arrayList)).f(new a0.a() { // from class: androidx.camera.view.b
            @Override // a0.a
            public final com.google.common.util.concurrent.d apply(Object obj) {
                com.google.common.util.concurrent.d g8;
                g8 = e.this.g((Void) obj);
                return g8;
            }
        }, z.a.a()).e(new n.a() { // from class: androidx.camera.view.d
            @Override // n.a
            public final Object apply(Object obj) {
                Void h8;
                h8 = e.this.h((Void) obj);
                return h8;
            }
        }, z.a.a());
        this.f2995e = e8;
        a0.f.b(e8, new a(arrayList, sVar), z.a.a());
    }

    private com.google.common.util.concurrent.d<Void> m(final androidx.camera.core.s sVar, final List<y.h> list) {
        return androidx.concurrent.futures.c.a(new c.InterfaceC0024c() { // from class: androidx.camera.view.c
            @Override // androidx.concurrent.futures.c.InterfaceC0024c
            public final Object a(c.a aVar) {
                Object i8;
                i8 = e.this.i(sVar, list, aVar);
                return i8;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void f() {
        e();
    }

    @Override // y.p0.a
    /* renamed from: j */
    public void a(CameraInternal.State state) {
        if (state == CameraInternal.State.CLOSING || state == CameraInternal.State.CLOSED || state == CameraInternal.State.RELEASING || state == CameraInternal.State.RELEASED) {
            l(PreviewView.StreamState.IDLE);
            if (this.f2996f) {
                this.f2996f = false;
                e();
            }
        } else if ((state == CameraInternal.State.OPENING || state == CameraInternal.State.OPEN || state == CameraInternal.State.PENDING_OPEN) && !this.f2996f) {
            k(this.f2991a);
            this.f2996f = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void l(PreviewView.StreamState streamState) {
        synchronized (this) {
            if (this.f2993c.equals(streamState)) {
                return;
            }
            this.f2993c = streamState;
            p1.a("StreamStateObserver", "Update Preview stream state to " + streamState);
            this.f2992b.l(streamState);
        }
    }

    @Override // y.p0.a
    public void onError(Throwable th) {
        f();
        l(PreviewView.StreamState.IDLE);
    }
}
