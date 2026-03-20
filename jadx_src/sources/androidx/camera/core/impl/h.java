package androidx.camera.core.impl;

import android.view.Surface;
import androidx.camera.core.impl.DeferrableSurface;
import androidx.concurrent.futures.c;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import y.a0;
import y.b0;
import y.y;
import y.z;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class h {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements a0.c<List<Surface>> {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ boolean f2572a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ c.a f2573b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ ScheduledFuture f2574c;

        a(boolean z4, c.a aVar, ScheduledFuture scheduledFuture) {
            this.f2572a = z4;
            this.f2573b = aVar;
            this.f2574c = scheduledFuture;
        }

        @Override // a0.c
        /* renamed from: a */
        public void c(List<Surface> list) {
            ArrayList arrayList = new ArrayList(list);
            if (this.f2572a) {
                arrayList.removeAll(Collections.singleton(null));
            }
            this.f2573b.c(arrayList);
            this.f2574c.cancel(true);
        }

        @Override // a0.c
        public void onFailure(Throwable th) {
            this.f2573b.c(Collections.unmodifiableList(Collections.emptyList()));
            this.f2574c.cancel(true);
        }
    }

    public static void e(List<DeferrableSurface> list) {
        for (DeferrableSurface deferrableSurface : list) {
            deferrableSurface.d();
        }
    }

    public static void f(List<DeferrableSurface> list) {
        if (list.isEmpty()) {
            return;
        }
        int i8 = 0;
        do {
            try {
                list.get(i8).j();
                i8++;
            } catch (DeferrableSurface.SurfaceClosedException e8) {
                for (int i9 = i8 - 1; i9 >= 0; i9--) {
                    list.get(i9).d();
                }
                throw e8;
            }
        } while (i8 < list.size());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void g(com.google.common.util.concurrent.d dVar, c.a aVar, long j8) {
        if (dVar.isDone()) {
            return;
        }
        aVar.f(new TimeoutException("Cannot complete surfaceList within " + j8));
        dVar.cancel(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void h(Executor executor, com.google.common.util.concurrent.d dVar, c.a aVar, long j8) {
        executor.execute(new a0(dVar, aVar, j8));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Object j(List list, ScheduledExecutorService scheduledExecutorService, Executor executor, long j8, boolean z4, c.a aVar) {
        com.google.common.util.concurrent.d n8 = a0.f.n(list);
        ScheduledFuture<?> schedule = scheduledExecutorService.schedule((Runnable) new b0(executor, n8, aVar, j8), j8, TimeUnit.MILLISECONDS);
        aVar.a(new z(n8), executor);
        a0.f.b(n8, new a(z4, aVar, schedule), executor);
        return "surfaceList";
    }

    public static com.google.common.util.concurrent.d<List<Surface>> k(Collection<DeferrableSurface> collection, boolean z4, long j8, Executor executor, ScheduledExecutorService scheduledExecutorService) {
        ArrayList arrayList = new ArrayList();
        for (DeferrableSurface deferrableSurface : collection) {
            arrayList.add(a0.f.j(deferrableSurface.h()));
        }
        return androidx.concurrent.futures.c.a(new y(arrayList, scheduledExecutorService, executor, j8, z4));
    }
}
