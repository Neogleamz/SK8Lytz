package androidx.transition;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class s {

    /* renamed from: a  reason: collision with root package name */
    private static Transition f7606a = new AutoTransition();

    /* renamed from: b  reason: collision with root package name */
    private static ThreadLocal<WeakReference<k0.a<ViewGroup, ArrayList<Transition>>>> f7607b = new ThreadLocal<>();

    /* renamed from: c  reason: collision with root package name */
    static ArrayList<ViewGroup> f7608c = new ArrayList<>();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a implements ViewTreeObserver.OnPreDrawListener, View.OnAttachStateChangeListener {

        /* renamed from: a  reason: collision with root package name */
        Transition f7609a;

        /* renamed from: b  reason: collision with root package name */
        ViewGroup f7610b;

        /* renamed from: androidx.transition.s$a$a  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class C0084a extends r {

            /* renamed from: a  reason: collision with root package name */
            final /* synthetic */ k0.a f7611a;

            C0084a(k0.a aVar) {
                this.f7611a = aVar;
            }

            @Override // androidx.transition.Transition.f
            public void c(Transition transition) {
                ((ArrayList) this.f7611a.get(a.this.f7610b)).remove(transition);
                transition.a0(this);
            }
        }

        a(Transition transition, ViewGroup viewGroup) {
            this.f7609a = transition;
            this.f7610b = viewGroup;
        }

        private void a() {
            this.f7610b.getViewTreeObserver().removeOnPreDrawListener(this);
            this.f7610b.removeOnAttachStateChangeListener(this);
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            a();
            if (s.f7608c.remove(this.f7610b)) {
                k0.a<ViewGroup, ArrayList<Transition>> b9 = s.b();
                ArrayList<Transition> arrayList = b9.get(this.f7610b);
                ArrayList arrayList2 = null;
                if (arrayList == null) {
                    arrayList = new ArrayList<>();
                    b9.put(this.f7610b, arrayList);
                } else if (arrayList.size() > 0) {
                    arrayList2 = new ArrayList(arrayList);
                }
                arrayList.add(this.f7609a);
                this.f7609a.b(new C0084a(b9));
                this.f7609a.o(this.f7610b, false);
                if (arrayList2 != null) {
                    Iterator it = arrayList2.iterator();
                    while (it.hasNext()) {
                        ((Transition) it.next()).c0(this.f7610b);
                    }
                }
                this.f7609a.Y(this.f7610b);
                return true;
            }
            return true;
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewAttachedToWindow(View view) {
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewDetachedFromWindow(View view) {
            a();
            s.f7608c.remove(this.f7610b);
            ArrayList<Transition> arrayList = s.b().get(this.f7610b);
            if (arrayList != null && arrayList.size() > 0) {
                Iterator<Transition> it = arrayList.iterator();
                while (it.hasNext()) {
                    it.next().c0(this.f7610b);
                }
            }
            this.f7609a.q(true);
        }
    }

    public static void a(ViewGroup viewGroup, Transition transition) {
        if (f7608c.contains(viewGroup) || !androidx.core.view.c0.W(viewGroup)) {
            return;
        }
        f7608c.add(viewGroup);
        if (transition == null) {
            transition = f7606a;
        }
        Transition clone = transition.clone();
        d(viewGroup, clone);
        p.c(viewGroup, null);
        c(viewGroup, clone);
    }

    static k0.a<ViewGroup, ArrayList<Transition>> b() {
        k0.a<ViewGroup, ArrayList<Transition>> aVar;
        WeakReference<k0.a<ViewGroup, ArrayList<Transition>>> weakReference = f7607b.get();
        if (weakReference == null || (aVar = weakReference.get()) == null) {
            k0.a<ViewGroup, ArrayList<Transition>> aVar2 = new k0.a<>();
            f7607b.set(new WeakReference<>(aVar2));
            return aVar2;
        }
        return aVar;
    }

    private static void c(ViewGroup viewGroup, Transition transition) {
        if (transition == null || viewGroup == null) {
            return;
        }
        a aVar = new a(transition, viewGroup);
        viewGroup.addOnAttachStateChangeListener(aVar);
        viewGroup.getViewTreeObserver().addOnPreDrawListener(aVar);
    }

    private static void d(ViewGroup viewGroup, Transition transition) {
        ArrayList<Transition> arrayList = b().get(viewGroup);
        if (arrayList != null && arrayList.size() > 0) {
            Iterator<Transition> it = arrayList.iterator();
            while (it.hasNext()) {
                it.next().X(viewGroup);
            }
        }
        if (transition != null) {
            transition.o(viewGroup, true);
        }
        p b9 = p.b(viewGroup);
        if (b9 != null) {
            b9.a();
        }
    }
}
