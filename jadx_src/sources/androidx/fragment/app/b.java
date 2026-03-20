package androidx.fragment.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import androidx.core.os.e;
import androidx.core.view.c0;
import androidx.core.view.f0;
import androidx.fragment.app.d;
import androidx.fragment.app.x;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class b extends x {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static /* synthetic */ class a {

        /* renamed from: a  reason: collision with root package name */
        static final /* synthetic */ int[] f5548a;

        static {
            int[] iArr = new int[x.e.c.values().length];
            f5548a = iArr;
            try {
                iArr[x.e.c.GONE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f5548a[x.e.c.INVISIBLE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f5548a[x.e.c.REMOVED.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f5548a[x.e.c.VISIBLE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    /* renamed from: androidx.fragment.app.b$b  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class RunnableC0054b implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ List f5549a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ x.e f5550b;

        RunnableC0054b(List list, x.e eVar) {
            this.f5549a = list;
            this.f5550b = eVar;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.f5549a.contains(this.f5550b)) {
                this.f5549a.remove(this.f5550b);
                b.this.s(this.f5550b);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class c extends AnimatorListenerAdapter {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ ViewGroup f5552a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ View f5553b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ boolean f5554c;

        /* renamed from: d  reason: collision with root package name */
        final /* synthetic */ x.e f5555d;

        /* renamed from: e  reason: collision with root package name */
        final /* synthetic */ k f5556e;

        c(ViewGroup viewGroup, View view, boolean z4, x.e eVar, k kVar) {
            this.f5552a = viewGroup;
            this.f5553b = view;
            this.f5554c = z4;
            this.f5555d = eVar;
            this.f5556e = kVar;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            this.f5552a.endViewTransition(this.f5553b);
            if (this.f5554c) {
                this.f5555d.e().c(this.f5553b);
            }
            this.f5556e.a();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class d implements e.b {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ Animator f5558a;

        d(Animator animator) {
            this.f5558a = animator;
        }

        @Override // androidx.core.os.e.b
        public void a() {
            this.f5558a.end();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class e implements Animation.AnimationListener {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ ViewGroup f5560a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ View f5561b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ k f5562c;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a implements Runnable {
            a() {
            }

            @Override // java.lang.Runnable
            public void run() {
                e eVar = e.this;
                eVar.f5560a.endViewTransition(eVar.f5561b);
                e.this.f5562c.a();
            }
        }

        e(ViewGroup viewGroup, View view, k kVar) {
            this.f5560a = viewGroup;
            this.f5561b = view;
            this.f5562c = kVar;
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationEnd(Animation animation) {
            this.f5560a.post(new a());
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationRepeat(Animation animation) {
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationStart(Animation animation) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class f implements e.b {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ View f5565a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ ViewGroup f5566b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ k f5567c;

        f(View view, ViewGroup viewGroup, k kVar) {
            this.f5565a = view;
            this.f5566b = viewGroup;
            this.f5567c = kVar;
        }

        @Override // androidx.core.os.e.b
        public void a() {
            this.f5565a.clearAnimation();
            this.f5566b.endViewTransition(this.f5565a);
            this.f5567c.a();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class g implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ x.e f5569a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ x.e f5570b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ boolean f5571c;

        /* renamed from: d  reason: collision with root package name */
        final /* synthetic */ k0.a f5572d;

        g(x.e eVar, x.e eVar2, boolean z4, k0.a aVar) {
            this.f5569a = eVar;
            this.f5570b = eVar2;
            this.f5571c = z4;
            this.f5572d = aVar;
        }

        @Override // java.lang.Runnable
        public void run() {
            s.f(this.f5569a.f(), this.f5570b.f(), this.f5571c, this.f5572d, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class h implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ u f5574a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ View f5575b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ Rect f5576c;

        h(u uVar, View view, Rect rect) {
            this.f5574a = uVar;
            this.f5575b = view;
            this.f5576c = rect;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.f5574a.k(this.f5575b, this.f5576c);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class i implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ ArrayList f5578a;

        i(ArrayList arrayList) {
            this.f5578a = arrayList;
        }

        @Override // java.lang.Runnable
        public void run() {
            s.A(this.f5578a, 4);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class j implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ m f5580a;

        j(m mVar) {
            this.f5580a = mVar;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.f5580a.a();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class k extends l {

        /* renamed from: c  reason: collision with root package name */
        private boolean f5582c;

        /* renamed from: d  reason: collision with root package name */
        private boolean f5583d;

        /* renamed from: e  reason: collision with root package name */
        private d.C0056d f5584e;

        k(x.e eVar, androidx.core.os.e eVar2, boolean z4) {
            super(eVar, eVar2);
            this.f5583d = false;
            this.f5582c = z4;
        }

        d.C0056d e(Context context) {
            if (this.f5583d) {
                return this.f5584e;
            }
            d.C0056d c9 = androidx.fragment.app.d.c(context, b().f(), b().e() == x.e.c.VISIBLE, this.f5582c);
            this.f5584e = c9;
            this.f5583d = true;
            return c9;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class l {

        /* renamed from: a  reason: collision with root package name */
        private final x.e f5585a;

        /* renamed from: b  reason: collision with root package name */
        private final androidx.core.os.e f5586b;

        l(x.e eVar, androidx.core.os.e eVar2) {
            this.f5585a = eVar;
            this.f5586b = eVar2;
        }

        void a() {
            this.f5585a.d(this.f5586b);
        }

        x.e b() {
            return this.f5585a;
        }

        androidx.core.os.e c() {
            return this.f5586b;
        }

        boolean d() {
            x.e.c cVar;
            x.e.c h8 = x.e.c.h(this.f5585a.f().T);
            x.e.c e8 = this.f5585a.e();
            return h8 == e8 || !(h8 == (cVar = x.e.c.VISIBLE) || e8 == cVar);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class m extends l {

        /* renamed from: c  reason: collision with root package name */
        private final Object f5587c;

        /* renamed from: d  reason: collision with root package name */
        private final boolean f5588d;

        /* renamed from: e  reason: collision with root package name */
        private final Object f5589e;

        m(x.e eVar, androidx.core.os.e eVar2, boolean z4, boolean z8) {
            super(eVar, eVar2);
            boolean z9;
            Object obj;
            if (eVar.e() == x.e.c.VISIBLE) {
                Fragment f5 = eVar.f();
                this.f5587c = z4 ? f5.J() : f5.s();
                Fragment f8 = eVar.f();
                z9 = z4 ? f8.m() : f8.l();
            } else {
                Fragment f9 = eVar.f();
                this.f5587c = z4 ? f9.L() : f9.v();
                z9 = true;
            }
            this.f5588d = z9;
            if (z8) {
                Fragment f10 = eVar.f();
                obj = z4 ? f10.N() : f10.M();
            } else {
                obj = null;
            }
            this.f5589e = obj;
        }

        private u f(Object obj) {
            if (obj == null) {
                return null;
            }
            u uVar = s.f5690b;
            if (uVar == null || !uVar.e(obj)) {
                u uVar2 = s.f5691c;
                if (uVar2 == null || !uVar2.e(obj)) {
                    throw new IllegalArgumentException("Transition " + obj + " for fragment " + b().f() + " is not a valid framework Transition or AndroidX Transition");
                }
                return uVar2;
            }
            return uVar;
        }

        u e() {
            u f5 = f(this.f5587c);
            u f8 = f(this.f5589e);
            if (f5 == null || f8 == null || f5 == f8) {
                return f5 != null ? f5 : f8;
            }
            throw new IllegalArgumentException("Mixing framework transitions and AndroidX transitions is not allowed. Fragment " + b().f() + " returned Transition " + this.f5587c + " which uses a different Transition  type than its shared element transition " + this.f5589e);
        }

        public Object g() {
            return this.f5589e;
        }

        Object h() {
            return this.f5587c;
        }

        public boolean i() {
            return this.f5589e != null;
        }

        boolean j() {
            return this.f5588d;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public b(ViewGroup viewGroup) {
        super(viewGroup);
    }

    private void w(List<k> list, List<x.e> list2, boolean z4, Map<x.e, Boolean> map) {
        StringBuilder sb;
        String str;
        d.C0056d e8;
        ViewGroup m8 = m();
        Context context = m8.getContext();
        ArrayList arrayList = new ArrayList();
        boolean z8 = false;
        for (k kVar : list) {
            if (kVar.d() || (e8 = kVar.e(context)) == null) {
                kVar.a();
            } else {
                Animator animator = e8.f5619b;
                if (animator == null) {
                    arrayList.add(kVar);
                } else {
                    x.e b9 = kVar.b();
                    Fragment f5 = b9.f();
                    if (Boolean.TRUE.equals(map.get(b9))) {
                        if (FragmentManager.F0(2)) {
                            Log.v("FragmentManager", "Ignoring Animator set on " + f5 + " as this Fragment was involved in a Transition.");
                        }
                        kVar.a();
                    } else {
                        boolean z9 = b9.e() == x.e.c.GONE;
                        if (z9) {
                            list2.remove(b9);
                        }
                        View view = f5.T;
                        m8.startViewTransition(view);
                        animator.addListener(new c(m8, view, z9, b9, kVar));
                        animator.setTarget(view);
                        animator.start();
                        kVar.c().c(new d(animator));
                        z8 = true;
                    }
                }
            }
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            k kVar2 = (k) it.next();
            x.e b10 = kVar2.b();
            Fragment f8 = b10.f();
            if (z4) {
                if (FragmentManager.F0(2)) {
                    sb = new StringBuilder();
                    sb.append("Ignoring Animation set on ");
                    sb.append(f8);
                    str = " as Animations cannot run alongside Transitions.";
                    sb.append(str);
                    Log.v("FragmentManager", sb.toString());
                }
                kVar2.a();
            } else if (z8) {
                if (FragmentManager.F0(2)) {
                    sb = new StringBuilder();
                    sb.append("Ignoring Animation set on ");
                    sb.append(f8);
                    str = " as Animations cannot run alongside Animators.";
                    sb.append(str);
                    Log.v("FragmentManager", sb.toString());
                }
                kVar2.a();
            } else {
                View view2 = f8.T;
                Animation animation = (Animation) androidx.core.util.h.h(((d.C0056d) androidx.core.util.h.h(kVar2.e(context))).f5618a);
                if (b10.e() != x.e.c.REMOVED) {
                    view2.startAnimation(animation);
                    kVar2.a();
                } else {
                    m8.startViewTransition(view2);
                    d.e eVar = new d.e(animation, m8, view2);
                    eVar.setAnimationListener(new e(m8, view2, kVar2));
                    view2.startAnimation(eVar);
                }
                kVar2.c().c(new f(view2, m8, kVar2));
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private Map<x.e, Boolean> x(List<m> list, List<x.e> list2, boolean z4, x.e eVar, x.e eVar2) {
        View view;
        Object obj;
        ArrayList<View> arrayList;
        Object obj2;
        ArrayList<View> arrayList2;
        x.e eVar3;
        x.e eVar4;
        View view2;
        Object n8;
        k0.a aVar;
        ArrayList<View> arrayList3;
        x.e eVar5;
        ArrayList<View> arrayList4;
        Rect rect;
        View view3;
        u uVar;
        x.e eVar6;
        View view4;
        boolean z8 = z4;
        x.e eVar7 = eVar;
        x.e eVar8 = eVar2;
        HashMap hashMap = new HashMap();
        u uVar2 = null;
        for (m mVar : list) {
            if (!mVar.d()) {
                u e8 = mVar.e();
                if (uVar2 == null) {
                    uVar2 = e8;
                } else if (e8 != null && uVar2 != e8) {
                    throw new IllegalArgumentException("Mixing framework transitions and AndroidX transitions is not allowed. Fragment " + mVar.b().f() + " returned Transition " + mVar.h() + " which uses a different Transition  type than other Fragments.");
                }
            }
        }
        if (uVar2 == null) {
            for (m mVar2 : list) {
                hashMap.put(mVar2.b(), Boolean.FALSE);
                mVar2.a();
            }
            return hashMap;
        }
        View view5 = new View(m().getContext());
        Rect rect2 = new Rect();
        ArrayList<View> arrayList5 = new ArrayList<>();
        ArrayList<View> arrayList6 = new ArrayList<>();
        k0.a aVar2 = new k0.a();
        Object obj3 = null;
        View view6 = null;
        boolean z9 = false;
        for (m mVar3 : list) {
            if (!mVar3.i() || eVar7 == null || eVar8 == null) {
                aVar = aVar2;
                arrayList3 = arrayList6;
                eVar5 = eVar7;
                arrayList4 = arrayList5;
                rect = rect2;
                view3 = view5;
                uVar = uVar2;
                eVar6 = eVar8;
                view6 = view6;
            } else {
                Object B = uVar2.B(uVar2.g(mVar3.g()));
                ArrayList<String> O = eVar2.f().O();
                ArrayList<String> O2 = eVar.f().O();
                ArrayList<String> P = eVar.f().P();
                View view7 = view6;
                int i8 = 0;
                while (i8 < P.size()) {
                    int indexOf = O.indexOf(P.get(i8));
                    ArrayList<String> arrayList7 = P;
                    if (indexOf != -1) {
                        O.set(indexOf, O2.get(i8));
                    }
                    i8++;
                    P = arrayList7;
                }
                ArrayList<String> P2 = eVar2.f().P();
                Fragment f5 = eVar.f();
                if (z8) {
                    f5.t();
                    eVar2.f().w();
                } else {
                    f5.w();
                    eVar2.f().t();
                }
                int i9 = 0;
                for (int size = O.size(); i9 < size; size = size) {
                    aVar2.put(O.get(i9), P2.get(i9));
                    i9++;
                }
                k0.a<String, View> aVar3 = new k0.a<>();
                u(aVar3, eVar.f().T);
                aVar3.q(O);
                aVar2.q(aVar3.keySet());
                k0.a<String, View> aVar4 = new k0.a<>();
                u(aVar4, eVar2.f().T);
                aVar4.q(P2);
                aVar4.q(aVar2.values());
                s.x(aVar2, aVar4);
                v(aVar3, aVar2.keySet());
                v(aVar4, aVar2.values());
                if (aVar2.isEmpty()) {
                    arrayList5.clear();
                    arrayList6.clear();
                    aVar = aVar2;
                    arrayList3 = arrayList6;
                    eVar5 = eVar7;
                    arrayList4 = arrayList5;
                    rect = rect2;
                    view3 = view5;
                    uVar = uVar2;
                    view6 = view7;
                    obj3 = null;
                    eVar6 = eVar8;
                } else {
                    s.f(eVar2.f(), eVar.f(), z8, aVar3, true);
                    aVar = aVar2;
                    ArrayList<View> arrayList8 = arrayList6;
                    androidx.core.view.y.a(m(), new g(eVar2, eVar, z4, aVar4));
                    arrayList5.addAll(aVar3.values());
                    if (O.isEmpty()) {
                        view6 = view7;
                    } else {
                        View view8 = aVar3.get(O.get(0));
                        uVar2.v(B, view8);
                        view6 = view8;
                    }
                    arrayList3 = arrayList8;
                    arrayList3.addAll(aVar4.values());
                    if (!P2.isEmpty() && (view4 = aVar4.get(P2.get(0))) != null) {
                        androidx.core.view.y.a(m(), new h(uVar2, view4, rect2));
                        z9 = true;
                    }
                    uVar2.z(B, view5, arrayList5);
                    arrayList4 = arrayList5;
                    rect = rect2;
                    view3 = view5;
                    uVar = uVar2;
                    uVar2.t(B, null, null, null, null, B, arrayList3);
                    Boolean bool = Boolean.TRUE;
                    eVar5 = eVar;
                    hashMap.put(eVar5, bool);
                    eVar6 = eVar2;
                    hashMap.put(eVar6, bool);
                    obj3 = B;
                }
            }
            eVar7 = eVar5;
            arrayList5 = arrayList4;
            rect2 = rect;
            view5 = view3;
            eVar8 = eVar6;
            aVar2 = aVar;
            z8 = z4;
            arrayList6 = arrayList3;
            uVar2 = uVar;
        }
        View view9 = view6;
        k0.a aVar5 = aVar2;
        Collection<?> collection = arrayList6;
        x.e eVar9 = eVar7;
        Collection<?> collection2 = arrayList5;
        Rect rect3 = rect2;
        View view10 = view5;
        u uVar3 = uVar2;
        boolean z10 = false;
        x.e eVar10 = eVar8;
        ArrayList arrayList9 = new ArrayList();
        Object obj4 = null;
        Object obj5 = null;
        for (m mVar4 : list) {
            if (mVar4.d()) {
                hashMap.put(mVar4.b(), Boolean.FALSE);
                mVar4.a();
            } else {
                Object g8 = uVar3.g(mVar4.h());
                x.e b9 = mVar4.b();
                boolean z11 = (obj3 == null || !(b9 == eVar9 || b9 == eVar10)) ? z10 : true;
                if (g8 == null) {
                    if (!z11) {
                        hashMap.put(b9, Boolean.FALSE);
                        mVar4.a();
                    }
                    arrayList2 = collection;
                    arrayList = collection2;
                    view = view10;
                    n8 = obj4;
                    eVar3 = eVar10;
                    view2 = view9;
                } else {
                    ArrayList<View> arrayList10 = new ArrayList<>();
                    Object obj6 = obj4;
                    t(arrayList10, b9.f().T);
                    if (z11) {
                        if (b9 == eVar9) {
                            arrayList10.removeAll(collection2);
                        } else {
                            arrayList10.removeAll(collection);
                        }
                    }
                    if (arrayList10.isEmpty()) {
                        uVar3.a(g8, view10);
                        arrayList2 = collection;
                        arrayList = collection2;
                        view = view10;
                        eVar4 = b9;
                        obj2 = obj5;
                        eVar3 = eVar10;
                        obj = obj6;
                    } else {
                        uVar3.b(g8, arrayList10);
                        view = view10;
                        obj = obj6;
                        arrayList = collection2;
                        obj2 = obj5;
                        arrayList2 = collection;
                        eVar3 = eVar10;
                        uVar3.t(g8, g8, arrayList10, null, null, null, null);
                        if (b9.e() == x.e.c.GONE) {
                            eVar4 = b9;
                            list2.remove(eVar4);
                            ArrayList<View> arrayList11 = new ArrayList<>(arrayList10);
                            arrayList11.remove(eVar4.f().T);
                            uVar3.r(g8, eVar4.f().T, arrayList11);
                            androidx.core.view.y.a(m(), new i(arrayList10));
                        } else {
                            eVar4 = b9;
                        }
                    }
                    if (eVar4.e() == x.e.c.VISIBLE) {
                        arrayList9.addAll(arrayList10);
                        if (z9) {
                            uVar3.u(g8, rect3);
                        }
                        view2 = view9;
                    } else {
                        view2 = view9;
                        uVar3.v(g8, view2);
                    }
                    hashMap.put(eVar4, Boolean.TRUE);
                    if (mVar4.j()) {
                        obj5 = uVar3.n(obj2, g8, null);
                        n8 = obj;
                    } else {
                        n8 = uVar3.n(obj, g8, null);
                        obj5 = obj2;
                    }
                }
                eVar10 = eVar3;
                obj4 = n8;
                view9 = view2;
                view10 = view;
                collection2 = arrayList;
                collection = arrayList2;
                z10 = false;
            }
        }
        ArrayList<View> arrayList12 = collection;
        ArrayList<View> arrayList13 = collection2;
        x.e eVar11 = eVar10;
        Object m8 = uVar3.m(obj5, obj4, obj3);
        for (m mVar5 : list) {
            if (!mVar5.d()) {
                Object h8 = mVar5.h();
                x.e b10 = mVar5.b();
                boolean z12 = obj3 != null && (b10 == eVar9 || b10 == eVar11);
                if (h8 != null || z12) {
                    if (c0.W(m())) {
                        uVar3.w(mVar5.b().f(), m8, mVar5.c(), new j(mVar5));
                    } else {
                        if (FragmentManager.F0(2)) {
                            Log.v("FragmentManager", "SpecialEffectsController: Container " + m() + " has not been laid out. Completing operation " + b10);
                        }
                        mVar5.a();
                    }
                }
            }
        }
        if (c0.W(m())) {
            s.A(arrayList9, 4);
            ArrayList<String> o5 = uVar3.o(arrayList12);
            uVar3.c(m(), m8);
            uVar3.y(m(), arrayList13, arrayList12, o5, aVar5);
            s.A(arrayList9, 0);
            uVar3.A(obj3, arrayList13, arrayList12);
            return hashMap;
        }
        return hashMap;
    }

    @Override // androidx.fragment.app.x
    void f(List<x.e> list, boolean z4) {
        x.e eVar = null;
        x.e eVar2 = null;
        for (x.e eVar3 : list) {
            x.e.c h8 = x.e.c.h(eVar3.f().T);
            int i8 = a.f5548a[eVar3.e().ordinal()];
            if (i8 == 1 || i8 == 2 || i8 == 3) {
                if (h8 == x.e.c.VISIBLE && eVar == null) {
                    eVar = eVar3;
                }
            } else if (i8 == 4 && h8 != x.e.c.VISIBLE) {
                eVar2 = eVar3;
            }
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ArrayList<x.e> arrayList3 = new ArrayList(list);
        for (x.e eVar4 : list) {
            androidx.core.os.e eVar5 = new androidx.core.os.e();
            eVar4.j(eVar5);
            arrayList.add(new k(eVar4, eVar5, z4));
            androidx.core.os.e eVar6 = new androidx.core.os.e();
            eVar4.j(eVar6);
            boolean z8 = false;
            if (z4) {
                if (eVar4 != eVar) {
                    arrayList2.add(new m(eVar4, eVar6, z4, z8));
                    eVar4.a(new RunnableC0054b(arrayList3, eVar4));
                }
                z8 = true;
                arrayList2.add(new m(eVar4, eVar6, z4, z8));
                eVar4.a(new RunnableC0054b(arrayList3, eVar4));
            } else {
                if (eVar4 != eVar2) {
                    arrayList2.add(new m(eVar4, eVar6, z4, z8));
                    eVar4.a(new RunnableC0054b(arrayList3, eVar4));
                }
                z8 = true;
                arrayList2.add(new m(eVar4, eVar6, z4, z8));
                eVar4.a(new RunnableC0054b(arrayList3, eVar4));
            }
        }
        Map<x.e, Boolean> x8 = x(arrayList2, arrayList3, z4, eVar, eVar2);
        w(arrayList, arrayList3, x8.containsValue(Boolean.TRUE), x8);
        for (x.e eVar7 : arrayList3) {
            s(eVar7);
        }
        arrayList3.clear();
    }

    void s(x.e eVar) {
        eVar.e().c(eVar.f().T);
    }

    void t(ArrayList<View> arrayList, View view) {
        if (!(view instanceof ViewGroup)) {
            if (arrayList.contains(view)) {
                return;
            }
            arrayList.add(view);
            return;
        }
        ViewGroup viewGroup = (ViewGroup) view;
        if (f0.a(viewGroup)) {
            if (arrayList.contains(view)) {
                return;
            }
            arrayList.add(viewGroup);
            return;
        }
        int childCount = viewGroup.getChildCount();
        for (int i8 = 0; i8 < childCount; i8++) {
            View childAt = viewGroup.getChildAt(i8);
            if (childAt.getVisibility() == 0) {
                t(arrayList, childAt);
            }
        }
    }

    void u(Map<String, View> map, View view) {
        String N = c0.N(view);
        if (N != null) {
            map.put(N, view);
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i8 = 0; i8 < childCount; i8++) {
                View childAt = viewGroup.getChildAt(i8);
                if (childAt.getVisibility() == 0) {
                    u(map, childAt);
                }
            }
        }
    }

    void v(k0.a<String, View> aVar, Collection<String> collection) {
        Iterator<Map.Entry<String, View>> it = aVar.entrySet().iterator();
        while (it.hasNext()) {
            if (!collection.contains(c0.N(it.next().getValue()))) {
                it.remove();
            }
        }
    }
}
