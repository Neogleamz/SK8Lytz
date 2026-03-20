package androidx.transition;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.os.e;
import androidx.fragment.app.Fragment;
import androidx.transition.Transition;
import java.util.ArrayList;
import java.util.List;
@SuppressLint({"RestrictedApi"})
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class d extends androidx.fragment.app.u {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends Transition.e {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ Rect f7531a;

        a(Rect rect) {
            this.f7531a = rect;
        }

        @Override // androidx.transition.Transition.e
        public Rect a(Transition transition) {
            return this.f7531a;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b implements Transition.f {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ View f7533a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ ArrayList f7534b;

        b(View view, ArrayList arrayList) {
            this.f7533a = view;
            this.f7534b = arrayList;
        }

        @Override // androidx.transition.Transition.f
        public void a(Transition transition) {
            transition.a0(this);
            transition.b(this);
        }

        @Override // androidx.transition.Transition.f
        public void b(Transition transition) {
        }

        @Override // androidx.transition.Transition.f
        public void c(Transition transition) {
            transition.a0(this);
            this.f7533a.setVisibility(8);
            int size = this.f7534b.size();
            for (int i8 = 0; i8 < size; i8++) {
                ((View) this.f7534b.get(i8)).setVisibility(0);
            }
        }

        @Override // androidx.transition.Transition.f
        public void d(Transition transition) {
        }

        @Override // androidx.transition.Transition.f
        public void e(Transition transition) {
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class c extends r {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ Object f7536a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ ArrayList f7537b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ Object f7538c;

        /* renamed from: d  reason: collision with root package name */
        final /* synthetic */ ArrayList f7539d;

        /* renamed from: e  reason: collision with root package name */
        final /* synthetic */ Object f7540e;

        /* renamed from: f  reason: collision with root package name */
        final /* synthetic */ ArrayList f7541f;

        c(Object obj, ArrayList arrayList, Object obj2, ArrayList arrayList2, Object obj3, ArrayList arrayList3) {
            this.f7536a = obj;
            this.f7537b = arrayList;
            this.f7538c = obj2;
            this.f7539d = arrayList2;
            this.f7540e = obj3;
            this.f7541f = arrayList3;
        }

        @Override // androidx.transition.r, androidx.transition.Transition.f
        public void a(Transition transition) {
            Object obj = this.f7536a;
            if (obj != null) {
                d.this.q(obj, this.f7537b, null);
            }
            Object obj2 = this.f7538c;
            if (obj2 != null) {
                d.this.q(obj2, this.f7539d, null);
            }
            Object obj3 = this.f7540e;
            if (obj3 != null) {
                d.this.q(obj3, this.f7541f, null);
            }
        }

        @Override // androidx.transition.Transition.f
        public void c(Transition transition) {
            transition.a0(this);
        }
    }

    /* renamed from: androidx.transition.d$d  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class C0083d implements e.b {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ Transition f7543a;

        C0083d(Transition transition) {
            this.f7543a = transition;
        }

        @Override // androidx.core.os.e.b
        public void a() {
            this.f7543a.cancel();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class e implements Transition.f {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ Runnable f7545a;

        e(Runnable runnable) {
            this.f7545a = runnable;
        }

        @Override // androidx.transition.Transition.f
        public void a(Transition transition) {
        }

        @Override // androidx.transition.Transition.f
        public void b(Transition transition) {
        }

        @Override // androidx.transition.Transition.f
        public void c(Transition transition) {
            this.f7545a.run();
        }

        @Override // androidx.transition.Transition.f
        public void d(Transition transition) {
        }

        @Override // androidx.transition.Transition.f
        public void e(Transition transition) {
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class f extends Transition.e {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ Rect f7547a;

        f(Rect rect) {
            this.f7547a = rect;
        }

        @Override // androidx.transition.Transition.e
        public Rect a(Transition transition) {
            Rect rect = this.f7547a;
            if (rect == null || rect.isEmpty()) {
                return null;
            }
            return this.f7547a;
        }
    }

    private static boolean C(Transition transition) {
        return (androidx.fragment.app.u.l(transition.H()) && androidx.fragment.app.u.l(transition.I()) && androidx.fragment.app.u.l(transition.J())) ? false : true;
    }

    @Override // androidx.fragment.app.u
    public void A(Object obj, ArrayList<View> arrayList, ArrayList<View> arrayList2) {
        TransitionSet transitionSet = (TransitionSet) obj;
        if (transitionSet != null) {
            transitionSet.K().clear();
            transitionSet.K().addAll(arrayList2);
            q(transitionSet, arrayList, arrayList2);
        }
    }

    @Override // androidx.fragment.app.u
    public Object B(Object obj) {
        if (obj == null) {
            return null;
        }
        TransitionSet transitionSet = new TransitionSet();
        transitionSet.q0((Transition) obj);
        return transitionSet;
    }

    @Override // androidx.fragment.app.u
    public void a(Object obj, View view) {
        if (obj != null) {
            ((Transition) obj).c(view);
        }
    }

    @Override // androidx.fragment.app.u
    public void b(Object obj, ArrayList<View> arrayList) {
        Transition transition = (Transition) obj;
        if (transition == null) {
            return;
        }
        int i8 = 0;
        if (transition instanceof TransitionSet) {
            TransitionSet transitionSet = (TransitionSet) transition;
            int t02 = transitionSet.t0();
            while (i8 < t02) {
                b(transitionSet.s0(i8), arrayList);
                i8++;
            }
        } else if (C(transition) || !androidx.fragment.app.u.l(transition.K())) {
        } else {
            int size = arrayList.size();
            while (i8 < size) {
                transition.c(arrayList.get(i8));
                i8++;
            }
        }
    }

    @Override // androidx.fragment.app.u
    public void c(ViewGroup viewGroup, Object obj) {
        s.a(viewGroup, (Transition) obj);
    }

    @Override // androidx.fragment.app.u
    public boolean e(Object obj) {
        return obj instanceof Transition;
    }

    @Override // androidx.fragment.app.u
    public Object g(Object obj) {
        if (obj != null) {
            return ((Transition) obj).clone();
        }
        return null;
    }

    @Override // androidx.fragment.app.u
    public Object m(Object obj, Object obj2, Object obj3) {
        Transition transition = (Transition) obj;
        Transition transition2 = (Transition) obj2;
        Transition transition3 = (Transition) obj3;
        if (transition != null && transition2 != null) {
            transition = new TransitionSet().q0(transition).q0(transition2).y0(1);
        } else if (transition == null) {
            transition = transition2 != null ? transition2 : null;
        }
        if (transition3 != null) {
            TransitionSet transitionSet = new TransitionSet();
            if (transition != null) {
                transitionSet.q0(transition);
            }
            transitionSet.q0(transition3);
            return transitionSet;
        }
        return transition;
    }

    @Override // androidx.fragment.app.u
    public Object n(Object obj, Object obj2, Object obj3) {
        TransitionSet transitionSet = new TransitionSet();
        if (obj != null) {
            transitionSet.q0((Transition) obj);
        }
        if (obj2 != null) {
            transitionSet.q0((Transition) obj2);
        }
        if (obj3 != null) {
            transitionSet.q0((Transition) obj3);
        }
        return transitionSet;
    }

    @Override // androidx.fragment.app.u
    public void p(Object obj, View view) {
        if (obj != null) {
            ((Transition) obj).b0(view);
        }
    }

    @Override // androidx.fragment.app.u
    public void q(Object obj, ArrayList<View> arrayList, ArrayList<View> arrayList2) {
        Transition transition = (Transition) obj;
        int i8 = 0;
        if (transition instanceof TransitionSet) {
            TransitionSet transitionSet = (TransitionSet) transition;
            int t02 = transitionSet.t0();
            while (i8 < t02) {
                q(transitionSet.s0(i8), arrayList, arrayList2);
                i8++;
            }
        } else if (!C(transition)) {
            List<View> K = transition.K();
            if (K.size() == arrayList.size() && K.containsAll(arrayList)) {
                int size = arrayList2 == null ? 0 : arrayList2.size();
                while (i8 < size) {
                    transition.c(arrayList2.get(i8));
                    i8++;
                }
                for (int size2 = arrayList.size() - 1; size2 >= 0; size2--) {
                    transition.b0(arrayList.get(size2));
                }
            }
        }
    }

    @Override // androidx.fragment.app.u
    public void r(Object obj, View view, ArrayList<View> arrayList) {
        ((Transition) obj).b(new b(view, arrayList));
    }

    @Override // androidx.fragment.app.u
    public void t(Object obj, Object obj2, ArrayList<View> arrayList, Object obj3, ArrayList<View> arrayList2, Object obj4, ArrayList<View> arrayList3) {
        ((Transition) obj).b(new c(obj2, arrayList, obj3, arrayList2, obj4, arrayList3));
    }

    @Override // androidx.fragment.app.u
    public void u(Object obj, Rect rect) {
        if (obj != null) {
            ((Transition) obj).g0(new f(rect));
        }
    }

    @Override // androidx.fragment.app.u
    public void v(Object obj, View view) {
        if (view != null) {
            Rect rect = new Rect();
            k(view, rect);
            ((Transition) obj).g0(new a(rect));
        }
    }

    @Override // androidx.fragment.app.u
    public void w(Fragment fragment, Object obj, androidx.core.os.e eVar, Runnable runnable) {
        Transition transition = (Transition) obj;
        eVar.c(new C0083d(transition));
        transition.b(new e(runnable));
    }

    @Override // androidx.fragment.app.u
    public void z(Object obj, View view, ArrayList<View> arrayList) {
        TransitionSet transitionSet = (TransitionSet) obj;
        List<View> K = transitionSet.K();
        K.clear();
        int size = arrayList.size();
        for (int i8 = 0; i8 < size; i8++) {
            androidx.fragment.app.u.d(K, arrayList.get(i8));
        }
        K.add(view);
        arrayList.add(view);
        b(transitionSet, arrayList);
    }
}
