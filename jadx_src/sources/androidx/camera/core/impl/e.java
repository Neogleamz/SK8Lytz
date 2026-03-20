package androidx.camera.core.impl;

import androidx.camera.core.impl.CameraInternal;
import androidx.camera.core.p1;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e {

    /* renamed from: c  reason: collision with root package name */
    private final int f2549c;

    /* renamed from: e  reason: collision with root package name */
    private int f2551e;

    /* renamed from: a  reason: collision with root package name */
    private final StringBuilder f2547a = new StringBuilder();

    /* renamed from: b  reason: collision with root package name */
    private final Object f2548b = new Object();

    /* renamed from: d  reason: collision with root package name */
    private final Map<androidx.camera.core.m, a> f2550d = new HashMap();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {

        /* renamed from: a  reason: collision with root package name */
        private CameraInternal.State f2552a;

        /* renamed from: b  reason: collision with root package name */
        private final Executor f2553b;

        /* renamed from: c  reason: collision with root package name */
        private final b f2554c;

        a(CameraInternal.State state, Executor executor, b bVar) {
            this.f2552a = state;
            this.f2553b = executor;
            this.f2554c = bVar;
        }

        CameraInternal.State a() {
            return this.f2552a;
        }

        void b() {
            try {
                Executor executor = this.f2553b;
                b bVar = this.f2554c;
                Objects.requireNonNull(bVar);
                executor.execute(new y.s(bVar));
            } catch (RejectedExecutionException e8) {
                p1.d("CameraStateRegistry", "Unable to notify camera.", e8);
            }
        }

        CameraInternal.State c(CameraInternal.State state) {
            CameraInternal.State state2 = this.f2552a;
            this.f2552a = state;
            return state2;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b {
        void a();
    }

    public e(int i8) {
        this.f2549c = i8;
        synchronized ("mLock") {
            this.f2551e = i8;
        }
    }

    private static boolean b(CameraInternal.State state) {
        return state != null && state.c();
    }

    private void d() {
        if (p1.f("CameraStateRegistry")) {
            this.f2547a.setLength(0);
            this.f2547a.append("Recalculating open cameras:\n");
            this.f2547a.append(String.format(Locale.US, "%-45s%-22s\n", "Camera", "State"));
            this.f2547a.append("-------------------------------------------------------------------\n");
        }
        int i8 = 0;
        for (Map.Entry<androidx.camera.core.m, a> entry : this.f2550d.entrySet()) {
            if (p1.f("CameraStateRegistry")) {
                this.f2547a.append(String.format(Locale.US, "%-45s%-22s\n", entry.getKey().toString(), entry.getValue().a() != null ? entry.getValue().a().toString() : "UNKNOWN"));
            }
            if (b(entry.getValue().a())) {
                i8++;
            }
        }
        if (p1.f("CameraStateRegistry")) {
            this.f2547a.append("-------------------------------------------------------------------\n");
            this.f2547a.append(String.format(Locale.US, "Open count: %d (Max allowed: %d)", Integer.valueOf(i8), Integer.valueOf(this.f2549c)));
            p1.a("CameraStateRegistry", this.f2547a.toString());
        }
        this.f2551e = Math.max(this.f2549c - i8, 0);
    }

    private CameraInternal.State g(androidx.camera.core.m mVar) {
        a remove = this.f2550d.remove(mVar);
        if (remove != null) {
            d();
            return remove.a();
        }
        return null;
    }

    private CameraInternal.State h(androidx.camera.core.m mVar, CameraInternal.State state) {
        CameraInternal.State c9 = ((a) androidx.core.util.h.i(this.f2550d.get(mVar), "Cannot update state of camera which has not yet been registered. Register with CameraStateRegistry.registerCamera()")).c(state);
        CameraInternal.State state2 = CameraInternal.State.OPENING;
        if (state == state2) {
            androidx.core.util.h.k(b(state) || c9 == state2, "Cannot mark camera as opening until camera was successful at calling CameraStateRegistry.tryOpenCamera()");
        }
        if (c9 != state) {
            d();
        }
        return c9;
    }

    public boolean a() {
        synchronized (this.f2548b) {
            for (Map.Entry<androidx.camera.core.m, a> entry : this.f2550d.entrySet()) {
                if (entry.getValue().a() == CameraInternal.State.CLOSING) {
                    return true;
                }
            }
            return false;
        }
    }

    public void c(androidx.camera.core.m mVar, CameraInternal.State state, boolean z4) {
        HashMap hashMap;
        synchronized (this.f2548b) {
            int i8 = this.f2551e;
            if ((state == CameraInternal.State.RELEASED ? g(mVar) : h(mVar, state)) == state) {
                return;
            }
            if (i8 < 1 && this.f2551e > 0) {
                hashMap = new HashMap();
                for (Map.Entry<androidx.camera.core.m, a> entry : this.f2550d.entrySet()) {
                    if (entry.getValue().a() == CameraInternal.State.PENDING_OPEN) {
                        hashMap.put(entry.getKey(), entry.getValue());
                    }
                }
            } else if (state != CameraInternal.State.PENDING_OPEN || this.f2551e <= 0) {
                hashMap = null;
            } else {
                hashMap = new HashMap();
                hashMap.put(mVar, this.f2550d.get(mVar));
            }
            if (hashMap != null && !z4) {
                hashMap.remove(mVar);
            }
            if (hashMap != null) {
                for (a aVar : hashMap.values()) {
                    aVar.b();
                }
            }
        }
    }

    public void e(androidx.camera.core.m mVar, Executor executor, b bVar) {
        synchronized (this.f2548b) {
            boolean z4 = !this.f2550d.containsKey(mVar);
            androidx.core.util.h.k(z4, "Camera is already registered: " + mVar);
            this.f2550d.put(mVar, new a(null, executor, bVar));
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0071 A[Catch: all -> 0x009b, TryCatch #0 {, blocks: (B:4:0x0003, B:6:0x001d, B:7:0x0052, B:9:0x0056, B:14:0x0069, B:16:0x0071, B:20:0x0080, B:22:0x0096, B:23:0x0099, B:13:0x0063), top: B:28:0x0003 }] */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0096 A[Catch: all -> 0x009b, TryCatch #0 {, blocks: (B:4:0x0003, B:6:0x001d, B:7:0x0052, B:9:0x0056, B:14:0x0069, B:16:0x0071, B:20:0x0080, B:22:0x0096, B:23:0x0099, B:13:0x0063), top: B:28:0x0003 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean f(androidx.camera.core.m r10) {
        /*
            r9 = this;
            java.lang.Object r0 = r9.f2548b
            monitor-enter(r0)
            java.util.Map<androidx.camera.core.m, androidx.camera.core.impl.e$a> r1 = r9.f2550d     // Catch: java.lang.Throwable -> L9b
            java.lang.Object r1 = r1.get(r10)     // Catch: java.lang.Throwable -> L9b
            androidx.camera.core.impl.e$a r1 = (androidx.camera.core.impl.e.a) r1     // Catch: java.lang.Throwable -> L9b
            java.lang.String r2 = "Camera must first be registered with registerCamera()"
            java.lang.Object r1 = androidx.core.util.h.i(r1, r2)     // Catch: java.lang.Throwable -> L9b
            androidx.camera.core.impl.e$a r1 = (androidx.camera.core.impl.e.a) r1     // Catch: java.lang.Throwable -> L9b
            java.lang.String r2 = "CameraStateRegistry"
            boolean r2 = androidx.camera.core.p1.f(r2)     // Catch: java.lang.Throwable -> L9b
            r3 = 1
            r4 = 0
            if (r2 == 0) goto L52
            java.lang.StringBuilder r2 = r9.f2547a     // Catch: java.lang.Throwable -> L9b
            r2.setLength(r4)     // Catch: java.lang.Throwable -> L9b
            java.lang.StringBuilder r2 = r9.f2547a     // Catch: java.lang.Throwable -> L9b
            java.util.Locale r5 = java.util.Locale.US     // Catch: java.lang.Throwable -> L9b
            java.lang.String r6 = "tryOpenCamera(%s) [Available Cameras: %d, Already Open: %b (Previous state: %s)]"
            r7 = 4
            java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch: java.lang.Throwable -> L9b
            r7[r4] = r10     // Catch: java.lang.Throwable -> L9b
            int r10 = r9.f2551e     // Catch: java.lang.Throwable -> L9b
            java.lang.Integer r10 = java.lang.Integer.valueOf(r10)     // Catch: java.lang.Throwable -> L9b
            r7[r3] = r10     // Catch: java.lang.Throwable -> L9b
            r10 = 2
            androidx.camera.core.impl.CameraInternal$State r8 = r1.a()     // Catch: java.lang.Throwable -> L9b
            boolean r8 = b(r8)     // Catch: java.lang.Throwable -> L9b
            java.lang.Boolean r8 = java.lang.Boolean.valueOf(r8)     // Catch: java.lang.Throwable -> L9b
            r7[r10] = r8     // Catch: java.lang.Throwable -> L9b
            r10 = 3
            androidx.camera.core.impl.CameraInternal$State r8 = r1.a()     // Catch: java.lang.Throwable -> L9b
            r7[r10] = r8     // Catch: java.lang.Throwable -> L9b
            java.lang.String r10 = java.lang.String.format(r5, r6, r7)     // Catch: java.lang.Throwable -> L9b
            r2.append(r10)     // Catch: java.lang.Throwable -> L9b
        L52:
            int r10 = r9.f2551e     // Catch: java.lang.Throwable -> L9b
            if (r10 > 0) goto L63
            androidx.camera.core.impl.CameraInternal$State r10 = r1.a()     // Catch: java.lang.Throwable -> L9b
            boolean r10 = b(r10)     // Catch: java.lang.Throwable -> L9b
            if (r10 == 0) goto L61
            goto L63
        L61:
            r10 = r4
            goto L69
        L63:
            androidx.camera.core.impl.CameraInternal$State r10 = androidx.camera.core.impl.CameraInternal.State.OPENING     // Catch: java.lang.Throwable -> L9b
            r1.c(r10)     // Catch: java.lang.Throwable -> L9b
            r10 = r3
        L69:
            java.lang.String r1 = "CameraStateRegistry"
            boolean r1 = androidx.camera.core.p1.f(r1)     // Catch: java.lang.Throwable -> L9b
            if (r1 == 0) goto L94
            java.lang.StringBuilder r1 = r9.f2547a     // Catch: java.lang.Throwable -> L9b
            java.util.Locale r2 = java.util.Locale.US     // Catch: java.lang.Throwable -> L9b
            java.lang.String r5 = " --> %s"
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch: java.lang.Throwable -> L9b
            if (r10 == 0) goto L7e
            java.lang.String r6 = "SUCCESS"
            goto L80
        L7e:
            java.lang.String r6 = "FAIL"
        L80:
            r3[r4] = r6     // Catch: java.lang.Throwable -> L9b
            java.lang.String r2 = java.lang.String.format(r2, r5, r3)     // Catch: java.lang.Throwable -> L9b
            r1.append(r2)     // Catch: java.lang.Throwable -> L9b
            java.lang.String r1 = "CameraStateRegistry"
            java.lang.StringBuilder r2 = r9.f2547a     // Catch: java.lang.Throwable -> L9b
            java.lang.String r2 = r2.toString()     // Catch: java.lang.Throwable -> L9b
            androidx.camera.core.p1.a(r1, r2)     // Catch: java.lang.Throwable -> L9b
        L94:
            if (r10 == 0) goto L99
            r9.d()     // Catch: java.lang.Throwable -> L9b
        L99:
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L9b
            return r10
        L9b:
            r10 = move-exception
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L9b
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.camera.core.impl.e.f(androidx.camera.core.m):boolean");
    }
}
