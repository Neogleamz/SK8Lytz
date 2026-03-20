package androidx.core.view;

import android.annotation.SuppressLint;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.lifecycle.Lifecycle;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class l {

    /* renamed from: a  reason: collision with root package name */
    private final Runnable f5032a;

    /* renamed from: b  reason: collision with root package name */
    private final CopyOnWriteArrayList<n> f5033b = new CopyOnWriteArrayList<>();

    /* renamed from: c  reason: collision with root package name */
    private final Map<n, a> f5034c = new HashMap();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {

        /* renamed from: a  reason: collision with root package name */
        final Lifecycle f5035a;

        /* renamed from: b  reason: collision with root package name */
        private androidx.lifecycle.h f5036b;

        a(Lifecycle lifecycle, androidx.lifecycle.h hVar) {
            this.f5035a = lifecycle;
            this.f5036b = hVar;
            lifecycle.a(hVar);
        }

        void a() {
            this.f5035a.c(this.f5036b);
            this.f5036b = null;
        }
    }

    public l(Runnable runnable) {
        this.f5032a = runnable;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void f(n nVar, androidx.lifecycle.j jVar, Lifecycle.Event event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            l(nVar);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void g(Lifecycle.State state, n nVar, androidx.lifecycle.j jVar, Lifecycle.Event event) {
        if (event == Lifecycle.Event.i(state)) {
            c(nVar);
        } else if (event == Lifecycle.Event.ON_DESTROY) {
            l(nVar);
        } else if (event == Lifecycle.Event.f(state)) {
            this.f5033b.remove(nVar);
            this.f5032a.run();
        }
    }

    public void c(n nVar) {
        this.f5033b.add(nVar);
        this.f5032a.run();
    }

    public void d(final n nVar, androidx.lifecycle.j jVar) {
        c(nVar);
        Lifecycle lifecycle = jVar.getLifecycle();
        a remove = this.f5034c.remove(nVar);
        if (remove != null) {
            remove.a();
        }
        this.f5034c.put(nVar, new a(lifecycle, new androidx.lifecycle.h() { // from class: androidx.core.view.j
            @Override // androidx.lifecycle.h
            public final void c(androidx.lifecycle.j jVar2, Lifecycle.Event event) {
                l.this.f(nVar, jVar2, event);
            }
        }));
    }

    @SuppressLint({"LambdaLast"})
    public void e(final n nVar, androidx.lifecycle.j jVar, final Lifecycle.State state) {
        Lifecycle lifecycle = jVar.getLifecycle();
        a remove = this.f5034c.remove(nVar);
        if (remove != null) {
            remove.a();
        }
        this.f5034c.put(nVar, new a(lifecycle, new androidx.lifecycle.h() { // from class: androidx.core.view.k
            @Override // androidx.lifecycle.h
            public final void c(androidx.lifecycle.j jVar2, Lifecycle.Event event) {
                l.this.g(state, nVar, jVar2, event);
            }
        }));
    }

    public void h(Menu menu, MenuInflater menuInflater) {
        Iterator<n> it = this.f5033b.iterator();
        while (it.hasNext()) {
            it.next().c(menu, menuInflater);
        }
    }

    public void i(Menu menu) {
        Iterator<n> it = this.f5033b.iterator();
        while (it.hasNext()) {
            it.next().b(menu);
        }
    }

    public boolean j(MenuItem menuItem) {
        Iterator<n> it = this.f5033b.iterator();
        while (it.hasNext()) {
            if (it.next().a(menuItem)) {
                return true;
            }
        }
        return false;
    }

    public void k(Menu menu) {
        Iterator<n> it = this.f5033b.iterator();
        while (it.hasNext()) {
            it.next().d(menu);
        }
    }

    public void l(n nVar) {
        this.f5033b.remove(nVar);
        a remove = this.f5034c.remove(nVar);
        if (remove != null) {
            remove.a();
        }
        this.f5032a.run();
    }
}
