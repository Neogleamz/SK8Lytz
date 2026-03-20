package androidx.recyclerview.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewPropertyAnimator;
import androidx.core.view.c0;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class g extends y {

    /* renamed from: s  reason: collision with root package name */
    private static TimeInterpolator f6831s;

    /* renamed from: h  reason: collision with root package name */
    private ArrayList<RecyclerView.b0> f6832h = new ArrayList<>();

    /* renamed from: i  reason: collision with root package name */
    private ArrayList<RecyclerView.b0> f6833i = new ArrayList<>();

    /* renamed from: j  reason: collision with root package name */
    private ArrayList<j> f6834j = new ArrayList<>();

    /* renamed from: k  reason: collision with root package name */
    private ArrayList<i> f6835k = new ArrayList<>();

    /* renamed from: l  reason: collision with root package name */
    ArrayList<ArrayList<RecyclerView.b0>> f6836l = new ArrayList<>();

    /* renamed from: m  reason: collision with root package name */
    ArrayList<ArrayList<j>> f6837m = new ArrayList<>();

    /* renamed from: n  reason: collision with root package name */
    ArrayList<ArrayList<i>> f6838n = new ArrayList<>();

    /* renamed from: o  reason: collision with root package name */
    ArrayList<RecyclerView.b0> f6839o = new ArrayList<>();

    /* renamed from: p  reason: collision with root package name */
    ArrayList<RecyclerView.b0> f6840p = new ArrayList<>();
    ArrayList<RecyclerView.b0> q = new ArrayList<>();

    /* renamed from: r  reason: collision with root package name */
    ArrayList<RecyclerView.b0> f6841r = new ArrayList<>();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ ArrayList f6842a;

        a(ArrayList arrayList) {
            this.f6842a = arrayList;
        }

        @Override // java.lang.Runnable
        public void run() {
            Iterator it = this.f6842a.iterator();
            while (it.hasNext()) {
                j jVar = (j) it.next();
                g.this.T(jVar.f6876a, jVar.f6877b, jVar.f6878c, jVar.f6879d, jVar.f6880e);
            }
            this.f6842a.clear();
            g.this.f6837m.remove(this.f6842a);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ ArrayList f6844a;

        b(ArrayList arrayList) {
            this.f6844a = arrayList;
        }

        @Override // java.lang.Runnable
        public void run() {
            Iterator it = this.f6844a.iterator();
            while (it.hasNext()) {
                g.this.S((i) it.next());
            }
            this.f6844a.clear();
            g.this.f6838n.remove(this.f6844a);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class c implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ ArrayList f6846a;

        c(ArrayList arrayList) {
            this.f6846a = arrayList;
        }

        @Override // java.lang.Runnable
        public void run() {
            Iterator it = this.f6846a.iterator();
            while (it.hasNext()) {
                g.this.R((RecyclerView.b0) it.next());
            }
            this.f6846a.clear();
            g.this.f6836l.remove(this.f6846a);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class d extends AnimatorListenerAdapter {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ RecyclerView.b0 f6848a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ ViewPropertyAnimator f6849b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ View f6850c;

        d(RecyclerView.b0 b0Var, ViewPropertyAnimator viewPropertyAnimator, View view) {
            this.f6848a = b0Var;
            this.f6849b = viewPropertyAnimator;
            this.f6850c = view;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            this.f6849b.setListener(null);
            this.f6850c.setAlpha(1.0f);
            g.this.H(this.f6848a);
            g.this.q.remove(this.f6848a);
            g.this.W();
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            g.this.I(this.f6848a);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class e extends AnimatorListenerAdapter {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ RecyclerView.b0 f6852a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ View f6853b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ ViewPropertyAnimator f6854c;

        e(RecyclerView.b0 b0Var, View view, ViewPropertyAnimator viewPropertyAnimator) {
            this.f6852a = b0Var;
            this.f6853b = view;
            this.f6854c = viewPropertyAnimator;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            this.f6853b.setAlpha(1.0f);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            this.f6854c.setListener(null);
            g.this.B(this.f6852a);
            g.this.f6839o.remove(this.f6852a);
            g.this.W();
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            g.this.C(this.f6852a);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class f extends AnimatorListenerAdapter {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ RecyclerView.b0 f6856a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ int f6857b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ View f6858c;

        /* renamed from: d  reason: collision with root package name */
        final /* synthetic */ int f6859d;

        /* renamed from: e  reason: collision with root package name */
        final /* synthetic */ ViewPropertyAnimator f6860e;

        f(RecyclerView.b0 b0Var, int i8, View view, int i9, ViewPropertyAnimator viewPropertyAnimator) {
            this.f6856a = b0Var;
            this.f6857b = i8;
            this.f6858c = view;
            this.f6859d = i9;
            this.f6860e = viewPropertyAnimator;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            if (this.f6857b != 0) {
                this.f6858c.setTranslationX(0.0f);
            }
            if (this.f6859d != 0) {
                this.f6858c.setTranslationY(0.0f);
            }
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            this.f6860e.setListener(null);
            g.this.F(this.f6856a);
            g.this.f6840p.remove(this.f6856a);
            g.this.W();
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            g.this.G(this.f6856a);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: androidx.recyclerview.widget.g$g  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class C0074g extends AnimatorListenerAdapter {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ i f6862a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ ViewPropertyAnimator f6863b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ View f6864c;

        C0074g(i iVar, ViewPropertyAnimator viewPropertyAnimator, View view) {
            this.f6862a = iVar;
            this.f6863b = viewPropertyAnimator;
            this.f6864c = view;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            this.f6863b.setListener(null);
            this.f6864c.setAlpha(1.0f);
            this.f6864c.setTranslationX(0.0f);
            this.f6864c.setTranslationY(0.0f);
            g.this.D(this.f6862a.f6870a, true);
            g.this.f6841r.remove(this.f6862a.f6870a);
            g.this.W();
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            g.this.E(this.f6862a.f6870a, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class h extends AnimatorListenerAdapter {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ i f6866a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ ViewPropertyAnimator f6867b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ View f6868c;

        h(i iVar, ViewPropertyAnimator viewPropertyAnimator, View view) {
            this.f6866a = iVar;
            this.f6867b = viewPropertyAnimator;
            this.f6868c = view;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            this.f6867b.setListener(null);
            this.f6868c.setAlpha(1.0f);
            this.f6868c.setTranslationX(0.0f);
            this.f6868c.setTranslationY(0.0f);
            g.this.D(this.f6866a.f6871b, false);
            g.this.f6841r.remove(this.f6866a.f6871b);
            g.this.W();
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            g.this.E(this.f6866a.f6871b, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class i {

        /* renamed from: a  reason: collision with root package name */
        public RecyclerView.b0 f6870a;

        /* renamed from: b  reason: collision with root package name */
        public RecyclerView.b0 f6871b;

        /* renamed from: c  reason: collision with root package name */
        public int f6872c;

        /* renamed from: d  reason: collision with root package name */
        public int f6873d;

        /* renamed from: e  reason: collision with root package name */
        public int f6874e;

        /* renamed from: f  reason: collision with root package name */
        public int f6875f;

        private i(RecyclerView.b0 b0Var, RecyclerView.b0 b0Var2) {
            this.f6870a = b0Var;
            this.f6871b = b0Var2;
        }

        i(RecyclerView.b0 b0Var, RecyclerView.b0 b0Var2, int i8, int i9, int i10, int i11) {
            this(b0Var, b0Var2);
            this.f6872c = i8;
            this.f6873d = i9;
            this.f6874e = i10;
            this.f6875f = i11;
        }

        public String toString() {
            return "ChangeInfo{oldHolder=" + this.f6870a + ", newHolder=" + this.f6871b + ", fromX=" + this.f6872c + ", fromY=" + this.f6873d + ", toX=" + this.f6874e + ", toY=" + this.f6875f + '}';
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class j {

        /* renamed from: a  reason: collision with root package name */
        public RecyclerView.b0 f6876a;

        /* renamed from: b  reason: collision with root package name */
        public int f6877b;

        /* renamed from: c  reason: collision with root package name */
        public int f6878c;

        /* renamed from: d  reason: collision with root package name */
        public int f6879d;

        /* renamed from: e  reason: collision with root package name */
        public int f6880e;

        j(RecyclerView.b0 b0Var, int i8, int i9, int i10, int i11) {
            this.f6876a = b0Var;
            this.f6877b = i8;
            this.f6878c = i9;
            this.f6879d = i10;
            this.f6880e = i11;
        }
    }

    private void U(RecyclerView.b0 b0Var) {
        View view = b0Var.f6628a;
        ViewPropertyAnimator animate = view.animate();
        this.q.add(b0Var);
        animate.setDuration(o()).alpha(0.0f).setListener(new d(b0Var, animate, view)).start();
    }

    private void X(List<i> list, RecyclerView.b0 b0Var) {
        for (int size = list.size() - 1; size >= 0; size--) {
            i iVar = list.get(size);
            if (Z(iVar, b0Var) && iVar.f6870a == null && iVar.f6871b == null) {
                list.remove(iVar);
            }
        }
    }

    private void Y(i iVar) {
        RecyclerView.b0 b0Var = iVar.f6870a;
        if (b0Var != null) {
            Z(iVar, b0Var);
        }
        RecyclerView.b0 b0Var2 = iVar.f6871b;
        if (b0Var2 != null) {
            Z(iVar, b0Var2);
        }
    }

    private boolean Z(i iVar, RecyclerView.b0 b0Var) {
        boolean z4 = false;
        if (iVar.f6871b == b0Var) {
            iVar.f6871b = null;
        } else if (iVar.f6870a != b0Var) {
            return false;
        } else {
            iVar.f6870a = null;
            z4 = true;
        }
        b0Var.f6628a.setAlpha(1.0f);
        b0Var.f6628a.setTranslationX(0.0f);
        b0Var.f6628a.setTranslationY(0.0f);
        D(b0Var, z4);
        return true;
    }

    private void a0(RecyclerView.b0 b0Var) {
        if (f6831s == null) {
            f6831s = new ValueAnimator().getInterpolator();
        }
        b0Var.f6628a.animate().setInterpolator(f6831s);
        j(b0Var);
    }

    @Override // androidx.recyclerview.widget.y
    public boolean A(RecyclerView.b0 b0Var) {
        a0(b0Var);
        this.f6832h.add(b0Var);
        return true;
    }

    void R(RecyclerView.b0 b0Var) {
        View view = b0Var.f6628a;
        ViewPropertyAnimator animate = view.animate();
        this.f6839o.add(b0Var);
        animate.alpha(1.0f).setDuration(l()).setListener(new e(b0Var, view, animate)).start();
    }

    void S(i iVar) {
        RecyclerView.b0 b0Var = iVar.f6870a;
        View view = b0Var == null ? null : b0Var.f6628a;
        RecyclerView.b0 b0Var2 = iVar.f6871b;
        View view2 = b0Var2 != null ? b0Var2.f6628a : null;
        if (view != null) {
            ViewPropertyAnimator duration = view.animate().setDuration(m());
            this.f6841r.add(iVar.f6870a);
            duration.translationX(iVar.f6874e - iVar.f6872c);
            duration.translationY(iVar.f6875f - iVar.f6873d);
            duration.alpha(0.0f).setListener(new C0074g(iVar, duration, view)).start();
        }
        if (view2 != null) {
            ViewPropertyAnimator animate = view2.animate();
            this.f6841r.add(iVar.f6871b);
            animate.translationX(0.0f).translationY(0.0f).setDuration(m()).alpha(1.0f).setListener(new h(iVar, animate, view2)).start();
        }
    }

    void T(RecyclerView.b0 b0Var, int i8, int i9, int i10, int i11) {
        View view = b0Var.f6628a;
        int i12 = i10 - i8;
        int i13 = i11 - i9;
        if (i12 != 0) {
            view.animate().translationX(0.0f);
        }
        if (i13 != 0) {
            view.animate().translationY(0.0f);
        }
        ViewPropertyAnimator animate = view.animate();
        this.f6840p.add(b0Var);
        animate.setDuration(n()).setListener(new f(b0Var, i12, view, i13, animate)).start();
    }

    void V(List<RecyclerView.b0> list) {
        for (int size = list.size() - 1; size >= 0; size--) {
            list.get(size).f6628a.animate().cancel();
        }
    }

    void W() {
        if (p()) {
            return;
        }
        i();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.l
    public boolean g(RecyclerView.b0 b0Var, List<Object> list) {
        return !list.isEmpty() || super.g(b0Var, list);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.l
    public void j(RecyclerView.b0 b0Var) {
        View view = b0Var.f6628a;
        view.animate().cancel();
        int size = this.f6834j.size();
        while (true) {
            size--;
            if (size < 0) {
                break;
            } else if (this.f6834j.get(size).f6876a == b0Var) {
                view.setTranslationY(0.0f);
                view.setTranslationX(0.0f);
                F(b0Var);
                this.f6834j.remove(size);
            }
        }
        X(this.f6835k, b0Var);
        if (this.f6832h.remove(b0Var)) {
            view.setAlpha(1.0f);
            H(b0Var);
        }
        if (this.f6833i.remove(b0Var)) {
            view.setAlpha(1.0f);
            B(b0Var);
        }
        for (int size2 = this.f6838n.size() - 1; size2 >= 0; size2--) {
            ArrayList<i> arrayList = this.f6838n.get(size2);
            X(arrayList, b0Var);
            if (arrayList.isEmpty()) {
                this.f6838n.remove(size2);
            }
        }
        for (int size3 = this.f6837m.size() - 1; size3 >= 0; size3--) {
            ArrayList<j> arrayList2 = this.f6837m.get(size3);
            int size4 = arrayList2.size() - 1;
            while (true) {
                if (size4 < 0) {
                    break;
                } else if (arrayList2.get(size4).f6876a == b0Var) {
                    view.setTranslationY(0.0f);
                    view.setTranslationX(0.0f);
                    F(b0Var);
                    arrayList2.remove(size4);
                    if (arrayList2.isEmpty()) {
                        this.f6837m.remove(size3);
                    }
                } else {
                    size4--;
                }
            }
        }
        for (int size5 = this.f6836l.size() - 1; size5 >= 0; size5--) {
            ArrayList<RecyclerView.b0> arrayList3 = this.f6836l.get(size5);
            if (arrayList3.remove(b0Var)) {
                view.setAlpha(1.0f);
                B(b0Var);
                if (arrayList3.isEmpty()) {
                    this.f6836l.remove(size5);
                }
            }
        }
        this.q.remove(b0Var);
        this.f6839o.remove(b0Var);
        this.f6841r.remove(b0Var);
        this.f6840p.remove(b0Var);
        W();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.l
    public void k() {
        int size = this.f6834j.size();
        while (true) {
            size--;
            if (size < 0) {
                break;
            }
            j jVar = this.f6834j.get(size);
            View view = jVar.f6876a.f6628a;
            view.setTranslationY(0.0f);
            view.setTranslationX(0.0f);
            F(jVar.f6876a);
            this.f6834j.remove(size);
        }
        for (int size2 = this.f6832h.size() - 1; size2 >= 0; size2--) {
            H(this.f6832h.get(size2));
            this.f6832h.remove(size2);
        }
        int size3 = this.f6833i.size();
        while (true) {
            size3--;
            if (size3 < 0) {
                break;
            }
            RecyclerView.b0 b0Var = this.f6833i.get(size3);
            b0Var.f6628a.setAlpha(1.0f);
            B(b0Var);
            this.f6833i.remove(size3);
        }
        for (int size4 = this.f6835k.size() - 1; size4 >= 0; size4--) {
            Y(this.f6835k.get(size4));
        }
        this.f6835k.clear();
        if (p()) {
            for (int size5 = this.f6837m.size() - 1; size5 >= 0; size5--) {
                ArrayList<j> arrayList = this.f6837m.get(size5);
                for (int size6 = arrayList.size() - 1; size6 >= 0; size6--) {
                    j jVar2 = arrayList.get(size6);
                    View view2 = jVar2.f6876a.f6628a;
                    view2.setTranslationY(0.0f);
                    view2.setTranslationX(0.0f);
                    F(jVar2.f6876a);
                    arrayList.remove(size6);
                    if (arrayList.isEmpty()) {
                        this.f6837m.remove(arrayList);
                    }
                }
            }
            for (int size7 = this.f6836l.size() - 1; size7 >= 0; size7--) {
                ArrayList<RecyclerView.b0> arrayList2 = this.f6836l.get(size7);
                for (int size8 = arrayList2.size() - 1; size8 >= 0; size8--) {
                    RecyclerView.b0 b0Var2 = arrayList2.get(size8);
                    b0Var2.f6628a.setAlpha(1.0f);
                    B(b0Var2);
                    arrayList2.remove(size8);
                    if (arrayList2.isEmpty()) {
                        this.f6836l.remove(arrayList2);
                    }
                }
            }
            for (int size9 = this.f6838n.size() - 1; size9 >= 0; size9--) {
                ArrayList<i> arrayList3 = this.f6838n.get(size9);
                for (int size10 = arrayList3.size() - 1; size10 >= 0; size10--) {
                    Y(arrayList3.get(size10));
                    if (arrayList3.isEmpty()) {
                        this.f6838n.remove(arrayList3);
                    }
                }
            }
            V(this.q);
            V(this.f6840p);
            V(this.f6839o);
            V(this.f6841r);
            i();
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.l
    public boolean p() {
        return (this.f6833i.isEmpty() && this.f6835k.isEmpty() && this.f6834j.isEmpty() && this.f6832h.isEmpty() && this.f6840p.isEmpty() && this.q.isEmpty() && this.f6839o.isEmpty() && this.f6841r.isEmpty() && this.f6837m.isEmpty() && this.f6836l.isEmpty() && this.f6838n.isEmpty()) ? false : true;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.l
    public void v() {
        boolean z4 = !this.f6832h.isEmpty();
        boolean z8 = !this.f6834j.isEmpty();
        boolean z9 = !this.f6835k.isEmpty();
        boolean z10 = !this.f6833i.isEmpty();
        if (z4 || z8 || z10 || z9) {
            Iterator<RecyclerView.b0> it = this.f6832h.iterator();
            while (it.hasNext()) {
                U(it.next());
            }
            this.f6832h.clear();
            if (z8) {
                ArrayList<j> arrayList = new ArrayList<>();
                arrayList.addAll(this.f6834j);
                this.f6837m.add(arrayList);
                this.f6834j.clear();
                a aVar = new a(arrayList);
                if (z4) {
                    c0.m0(arrayList.get(0).f6876a.f6628a, aVar, o());
                } else {
                    aVar.run();
                }
            }
            if (z9) {
                ArrayList<i> arrayList2 = new ArrayList<>();
                arrayList2.addAll(this.f6835k);
                this.f6838n.add(arrayList2);
                this.f6835k.clear();
                b bVar = new b(arrayList2);
                if (z4) {
                    c0.m0(arrayList2.get(0).f6870a.f6628a, bVar, o());
                } else {
                    bVar.run();
                }
            }
            if (z10) {
                ArrayList<RecyclerView.b0> arrayList3 = new ArrayList<>();
                arrayList3.addAll(this.f6833i);
                this.f6836l.add(arrayList3);
                this.f6833i.clear();
                c cVar = new c(arrayList3);
                if (z4 || z8 || z9) {
                    c0.m0(arrayList3.get(0).f6628a, cVar, (z4 ? o() : 0L) + Math.max(z8 ? n() : 0L, z9 ? m() : 0L));
                } else {
                    cVar.run();
                }
            }
        }
    }

    @Override // androidx.recyclerview.widget.y
    public boolean x(RecyclerView.b0 b0Var) {
        a0(b0Var);
        b0Var.f6628a.setAlpha(0.0f);
        this.f6833i.add(b0Var);
        return true;
    }

    @Override // androidx.recyclerview.widget.y
    public boolean y(RecyclerView.b0 b0Var, RecyclerView.b0 b0Var2, int i8, int i9, int i10, int i11) {
        if (b0Var == b0Var2) {
            return z(b0Var, i8, i9, i10, i11);
        }
        float translationX = b0Var.f6628a.getTranslationX();
        float translationY = b0Var.f6628a.getTranslationY();
        float alpha = b0Var.f6628a.getAlpha();
        a0(b0Var);
        int i12 = (int) ((i10 - i8) - translationX);
        int i13 = (int) ((i11 - i9) - translationY);
        b0Var.f6628a.setTranslationX(translationX);
        b0Var.f6628a.setTranslationY(translationY);
        b0Var.f6628a.setAlpha(alpha);
        if (b0Var2 != null) {
            a0(b0Var2);
            b0Var2.f6628a.setTranslationX(-i12);
            b0Var2.f6628a.setTranslationY(-i13);
            b0Var2.f6628a.setAlpha(0.0f);
        }
        this.f6835k.add(new i(b0Var, b0Var2, i8, i9, i10, i11));
        return true;
    }

    @Override // androidx.recyclerview.widget.y
    public boolean z(RecyclerView.b0 b0Var, int i8, int i9, int i10, int i11) {
        View view = b0Var.f6628a;
        int translationX = i8 + ((int) view.getTranslationX());
        int translationY = i9 + ((int) b0Var.f6628a.getTranslationY());
        a0(b0Var);
        int i12 = i10 - translationX;
        int i13 = i11 - translationY;
        if (i12 == 0 && i13 == 0) {
            F(b0Var);
            return false;
        }
        if (i12 != 0) {
            view.setTranslationX(-i12);
        }
        if (i13 != 0) {
            view.setTranslationY(-i13);
        }
        this.f6834j.add(new j(b0Var, translationX, translationY, i10, i11));
        return true;
    }
}
