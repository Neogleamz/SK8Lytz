package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.InflateException;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import com.daimajia.numberprogressbar.BuildConfig;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class Transition implements Cloneable {
    private static final int[] T = {2, 1, 3, 4};
    private static final PathMotion W = new a();
    private static ThreadLocal<k0.a<Animator, d>> X = new ThreadLocal<>();
    private ArrayList<u> A;
    x1.d O;
    private e P;
    private k0.a<String, String> Q;

    /* renamed from: z  reason: collision with root package name */
    private ArrayList<u> f7481z;

    /* renamed from: a  reason: collision with root package name */
    private String f7463a = getClass().getName();

    /* renamed from: b  reason: collision with root package name */
    private long f7464b = -1;

    /* renamed from: c  reason: collision with root package name */
    long f7465c = -1;

    /* renamed from: d  reason: collision with root package name */
    private TimeInterpolator f7466d = null;

    /* renamed from: e  reason: collision with root package name */
    ArrayList<Integer> f7467e = new ArrayList<>();

    /* renamed from: f  reason: collision with root package name */
    ArrayList<View> f7468f = new ArrayList<>();

    /* renamed from: g  reason: collision with root package name */
    private ArrayList<String> f7469g = null;

    /* renamed from: h  reason: collision with root package name */
    private ArrayList<Class<?>> f7470h = null;

    /* renamed from: j  reason: collision with root package name */
    private ArrayList<Integer> f7471j = null;

    /* renamed from: k  reason: collision with root package name */
    private ArrayList<View> f7472k = null;

    /* renamed from: l  reason: collision with root package name */
    private ArrayList<Class<?>> f7473l = null;

    /* renamed from: m  reason: collision with root package name */
    private ArrayList<String> f7474m = null;

    /* renamed from: n  reason: collision with root package name */
    private ArrayList<Integer> f7475n = null;

    /* renamed from: p  reason: collision with root package name */
    private ArrayList<View> f7476p = null;
    private ArrayList<Class<?>> q = null;

    /* renamed from: t  reason: collision with root package name */
    private v f7477t = new v();

    /* renamed from: w  reason: collision with root package name */
    private v f7478w = new v();

    /* renamed from: x  reason: collision with root package name */
    TransitionSet f7479x = null;

    /* renamed from: y  reason: collision with root package name */
    private int[] f7480y = T;
    private ViewGroup B = null;
    boolean C = false;
    ArrayList<Animator> E = new ArrayList<>();
    private int F = 0;
    private boolean G = false;
    private boolean H = false;
    private ArrayList<f> K = null;
    private ArrayList<Animator> L = new ArrayList<>();
    private PathMotion R = W;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a extends PathMotion {
        a() {
        }

        @Override // androidx.transition.PathMotion
        public Path a(float f5, float f8, float f9, float f10) {
            Path path = new Path();
            path.moveTo(f5, f8);
            path.lineTo(f9, f10);
            return path;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b extends AnimatorListenerAdapter {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ k0.a f7482a;

        b(k0.a aVar) {
            this.f7482a = aVar;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            this.f7482a.remove(animator);
            Transition.this.E.remove(animator);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            Transition.this.E.add(animator);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class c extends AnimatorListenerAdapter {
        c() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            Transition.this.w();
            animator.removeListener(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class d {

        /* renamed from: a  reason: collision with root package name */
        View f7485a;

        /* renamed from: b  reason: collision with root package name */
        String f7486b;

        /* renamed from: c  reason: collision with root package name */
        u f7487c;

        /* renamed from: d  reason: collision with root package name */
        o0 f7488d;

        /* renamed from: e  reason: collision with root package name */
        Transition f7489e;

        d(View view, String str, Transition transition, o0 o0Var, u uVar) {
            this.f7485a = view;
            this.f7486b = str;
            this.f7487c = uVar;
            this.f7488d = o0Var;
            this.f7489e = transition;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class e {
        public abstract Rect a(Transition transition);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface f {
        void a(Transition transition);

        void b(Transition transition);

        void c(Transition transition);

        void d(Transition transition);

        void e(Transition transition);
    }

    public Transition() {
    }

    @SuppressLint({"RestrictedApi"})
    public Transition(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, q.f7597c);
        XmlResourceParser xmlResourceParser = (XmlResourceParser) attributeSet;
        long g8 = androidx.core.content.res.k.g(obtainStyledAttributes, xmlResourceParser, "duration", 1, -1);
        if (g8 >= 0) {
            f0(g8);
        }
        long g9 = androidx.core.content.res.k.g(obtainStyledAttributes, xmlResourceParser, "startDelay", 2, -1);
        if (g9 > 0) {
            l0(g9);
        }
        int h8 = androidx.core.content.res.k.h(obtainStyledAttributes, xmlResourceParser, "interpolator", 0, 0);
        if (h8 > 0) {
            h0(AnimationUtils.loadInterpolator(context, h8));
        }
        String i8 = androidx.core.content.res.k.i(obtainStyledAttributes, xmlResourceParser, "matchOrder", 3);
        if (i8 != null) {
            i0(W(i8));
        }
        obtainStyledAttributes.recycle();
    }

    private static k0.a<Animator, d> F() {
        k0.a<Animator, d> aVar = X.get();
        if (aVar == null) {
            k0.a<Animator, d> aVar2 = new k0.a<>();
            X.set(aVar2);
            return aVar2;
        }
        return aVar;
    }

    private static boolean O(int i8) {
        return i8 >= 1 && i8 <= 4;
    }

    private static boolean Q(u uVar, u uVar2, String str) {
        Object obj = uVar.f7619a.get(str);
        Object obj2 = uVar2.f7619a.get(str);
        if (obj == null && obj2 == null) {
            return false;
        }
        if (obj == null || obj2 == null) {
            return true;
        }
        return true ^ obj.equals(obj2);
    }

    private void R(k0.a<View, u> aVar, k0.a<View, u> aVar2, SparseArray<View> sparseArray, SparseArray<View> sparseArray2) {
        View view;
        int size = sparseArray.size();
        for (int i8 = 0; i8 < size; i8++) {
            View valueAt = sparseArray.valueAt(i8);
            if (valueAt != null && P(valueAt) && (view = sparseArray2.get(sparseArray.keyAt(i8))) != null && P(view)) {
                u uVar = aVar.get(valueAt);
                u uVar2 = aVar2.get(view);
                if (uVar != null && uVar2 != null) {
                    this.f7481z.add(uVar);
                    this.A.add(uVar2);
                    aVar.remove(valueAt);
                    aVar2.remove(view);
                }
            }
        }
    }

    private void S(k0.a<View, u> aVar, k0.a<View, u> aVar2) {
        u remove;
        for (int size = aVar.size() - 1; size >= 0; size--) {
            View k8 = aVar.k(size);
            if (k8 != null && P(k8) && (remove = aVar2.remove(k8)) != null && P(remove.f7620b)) {
                this.f7481z.add(aVar.m(size));
                this.A.add(remove);
            }
        }
    }

    private void T(k0.a<View, u> aVar, k0.a<View, u> aVar2, k0.d<View> dVar, k0.d<View> dVar2) {
        View f5;
        int q = dVar.q();
        for (int i8 = 0; i8 < q; i8++) {
            View r4 = dVar.r(i8);
            if (r4 != null && P(r4) && (f5 = dVar2.f(dVar.k(i8))) != null && P(f5)) {
                u uVar = aVar.get(r4);
                u uVar2 = aVar2.get(f5);
                if (uVar != null && uVar2 != null) {
                    this.f7481z.add(uVar);
                    this.A.add(uVar2);
                    aVar.remove(r4);
                    aVar2.remove(f5);
                }
            }
        }
    }

    private void U(k0.a<View, u> aVar, k0.a<View, u> aVar2, k0.a<String, View> aVar3, k0.a<String, View> aVar4) {
        View view;
        int size = aVar3.size();
        for (int i8 = 0; i8 < size; i8++) {
            View o5 = aVar3.o(i8);
            if (o5 != null && P(o5) && (view = aVar4.get(aVar3.k(i8))) != null && P(view)) {
                u uVar = aVar.get(o5);
                u uVar2 = aVar2.get(view);
                if (uVar != null && uVar2 != null) {
                    this.f7481z.add(uVar);
                    this.A.add(uVar2);
                    aVar.remove(o5);
                    aVar2.remove(view);
                }
            }
        }
    }

    private void V(v vVar, v vVar2) {
        k0.a<View, u> aVar = new k0.a<>(vVar.f7622a);
        k0.a<View, u> aVar2 = new k0.a<>(vVar2.f7622a);
        int i8 = 0;
        while (true) {
            int[] iArr = this.f7480y;
            if (i8 >= iArr.length) {
                d(aVar, aVar2);
                return;
            }
            int i9 = iArr[i8];
            if (i9 == 1) {
                S(aVar, aVar2);
            } else if (i9 == 2) {
                U(aVar, aVar2, vVar.f7625d, vVar2.f7625d);
            } else if (i9 == 3) {
                R(aVar, aVar2, vVar.f7623b, vVar2.f7623b);
            } else if (i9 == 4) {
                T(aVar, aVar2, vVar.f7624c, vVar2.f7624c);
            }
            i8++;
        }
    }

    private static int[] W(String str) {
        StringTokenizer stringTokenizer = new StringTokenizer(str, ",");
        int[] iArr = new int[stringTokenizer.countTokens()];
        int i8 = 0;
        while (stringTokenizer.hasMoreTokens()) {
            String trim = stringTokenizer.nextToken().trim();
            if ("id".equalsIgnoreCase(trim)) {
                iArr[i8] = 3;
            } else if ("instance".equalsIgnoreCase(trim)) {
                iArr[i8] = 1;
            } else if ("name".equalsIgnoreCase(trim)) {
                iArr[i8] = 2;
            } else if ("itemId".equalsIgnoreCase(trim)) {
                iArr[i8] = 4;
            } else if (!trim.isEmpty()) {
                throw new InflateException("Unknown match type in matchOrder: '" + trim + "'");
            } else {
                int[] iArr2 = new int[iArr.length - 1];
                System.arraycopy(iArr, 0, iArr2, 0, i8);
                i8--;
                iArr = iArr2;
            }
            i8++;
        }
        return iArr;
    }

    private void d(k0.a<View, u> aVar, k0.a<View, u> aVar2) {
        for (int i8 = 0; i8 < aVar.size(); i8++) {
            u o5 = aVar.o(i8);
            if (P(o5.f7620b)) {
                this.f7481z.add(o5);
                this.A.add(null);
            }
        }
        for (int i9 = 0; i9 < aVar2.size(); i9++) {
            u o8 = aVar2.o(i9);
            if (P(o8.f7620b)) {
                this.A.add(o8);
                this.f7481z.add(null);
            }
        }
    }

    private void d0(Animator animator, k0.a<Animator, d> aVar) {
        if (animator != null) {
            animator.addListener(new b(aVar));
            g(animator);
        }
    }

    private static void e(v vVar, View view, u uVar) {
        vVar.f7622a.put(view, uVar);
        int id = view.getId();
        if (id >= 0) {
            if (vVar.f7623b.indexOfKey(id) >= 0) {
                vVar.f7623b.put(id, null);
            } else {
                vVar.f7623b.put(id, view);
            }
        }
        String N = androidx.core.view.c0.N(view);
        if (N != null) {
            if (vVar.f7625d.containsKey(N)) {
                vVar.f7625d.put(N, null);
            } else {
                vVar.f7625d.put(N, view);
            }
        }
        if (view.getParent() instanceof ListView) {
            ListView listView = (ListView) view.getParent();
            if (listView.getAdapter().hasStableIds()) {
                long itemIdAtPosition = listView.getItemIdAtPosition(listView.getPositionForView(view));
                if (vVar.f7624c.j(itemIdAtPosition) < 0) {
                    androidx.core.view.c0.D0(view, true);
                    vVar.f7624c.l(itemIdAtPosition, view);
                    return;
                }
                View f5 = vVar.f7624c.f(itemIdAtPosition);
                if (f5 != null) {
                    androidx.core.view.c0.D0(f5, false);
                    vVar.f7624c.l(itemIdAtPosition, null);
                }
            }
        }
    }

    private static boolean f(int[] iArr, int i8) {
        int i9 = iArr[i8];
        for (int i10 = 0; i10 < i8; i10++) {
            if (iArr[i10] == i9) {
                return true;
            }
        }
        return false;
    }

    private void k(View view, boolean z4) {
        if (view == null) {
            return;
        }
        int id = view.getId();
        ArrayList<Integer> arrayList = this.f7471j;
        if (arrayList == null || !arrayList.contains(Integer.valueOf(id))) {
            ArrayList<View> arrayList2 = this.f7472k;
            if (arrayList2 == null || !arrayList2.contains(view)) {
                ArrayList<Class<?>> arrayList3 = this.f7473l;
                if (arrayList3 != null) {
                    int size = arrayList3.size();
                    for (int i8 = 0; i8 < size; i8++) {
                        if (this.f7473l.get(i8).isInstance(view)) {
                            return;
                        }
                    }
                }
                if (view.getParent() instanceof ViewGroup) {
                    u uVar = new u(view);
                    if (z4) {
                        m(uVar);
                    } else {
                        j(uVar);
                    }
                    uVar.f7621c.add(this);
                    l(uVar);
                    e(z4 ? this.f7477t : this.f7478w, view, uVar);
                }
                if (view instanceof ViewGroup) {
                    ArrayList<Integer> arrayList4 = this.f7475n;
                    if (arrayList4 == null || !arrayList4.contains(Integer.valueOf(id))) {
                        ArrayList<View> arrayList5 = this.f7476p;
                        if (arrayList5 == null || !arrayList5.contains(view)) {
                            ArrayList<Class<?>> arrayList6 = this.q;
                            if (arrayList6 != null) {
                                int size2 = arrayList6.size();
                                for (int i9 = 0; i9 < size2; i9++) {
                                    if (this.q.get(i9).isInstance(view)) {
                                        return;
                                    }
                                }
                            }
                            ViewGroup viewGroup = (ViewGroup) view;
                            for (int i10 = 0; i10 < viewGroup.getChildCount(); i10++) {
                                k(viewGroup.getChildAt(i10), z4);
                            }
                        }
                    }
                }
            }
        }
    }

    public TimeInterpolator A() {
        return this.f7466d;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public u B(View view, boolean z4) {
        TransitionSet transitionSet = this.f7479x;
        if (transitionSet != null) {
            return transitionSet.B(view, z4);
        }
        ArrayList<u> arrayList = z4 ? this.f7481z : this.A;
        if (arrayList == null) {
            return null;
        }
        int size = arrayList.size();
        int i8 = -1;
        int i9 = 0;
        while (true) {
            if (i9 >= size) {
                break;
            }
            u uVar = arrayList.get(i9);
            if (uVar == null) {
                return null;
            }
            if (uVar.f7620b == view) {
                i8 = i9;
                break;
            }
            i9++;
        }
        if (i8 >= 0) {
            return (z4 ? this.A : this.f7481z).get(i8);
        }
        return null;
    }

    public String C() {
        return this.f7463a;
    }

    public PathMotion D() {
        return this.R;
    }

    public x1.d E() {
        return this.O;
    }

    public long G() {
        return this.f7464b;
    }

    public List<Integer> H() {
        return this.f7467e;
    }

    public List<String> I() {
        return this.f7469g;
    }

    public List<Class<?>> J() {
        return this.f7470h;
    }

    public List<View> K() {
        return this.f7468f;
    }

    public String[] L() {
        return null;
    }

    public u M(View view, boolean z4) {
        TransitionSet transitionSet = this.f7479x;
        if (transitionSet != null) {
            return transitionSet.M(view, z4);
        }
        return (z4 ? this.f7477t : this.f7478w).f7622a.get(view);
    }

    public boolean N(u uVar, u uVar2) {
        if (uVar == null || uVar2 == null) {
            return false;
        }
        String[] L = L();
        if (L == null) {
            for (String str : uVar.f7619a.keySet()) {
                if (Q(uVar, uVar2, str)) {
                }
            }
            return false;
        }
        for (String str2 : L) {
            if (!Q(uVar, uVar2, str2)) {
            }
        }
        return false;
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean P(View view) {
        ArrayList<Class<?>> arrayList;
        ArrayList<String> arrayList2;
        int id = view.getId();
        ArrayList<Integer> arrayList3 = this.f7471j;
        if (arrayList3 == null || !arrayList3.contains(Integer.valueOf(id))) {
            ArrayList<View> arrayList4 = this.f7472k;
            if (arrayList4 == null || !arrayList4.contains(view)) {
                ArrayList<Class<?>> arrayList5 = this.f7473l;
                if (arrayList5 != null) {
                    int size = arrayList5.size();
                    for (int i8 = 0; i8 < size; i8++) {
                        if (this.f7473l.get(i8).isInstance(view)) {
                            return false;
                        }
                    }
                }
                if (this.f7474m == null || androidx.core.view.c0.N(view) == null || !this.f7474m.contains(androidx.core.view.c0.N(view))) {
                    if ((this.f7467e.size() == 0 && this.f7468f.size() == 0 && (((arrayList = this.f7470h) == null || arrayList.isEmpty()) && ((arrayList2 = this.f7469g) == null || arrayList2.isEmpty()))) || this.f7467e.contains(Integer.valueOf(id)) || this.f7468f.contains(view)) {
                        return true;
                    }
                    ArrayList<String> arrayList6 = this.f7469g;
                    if (arrayList6 == null || !arrayList6.contains(androidx.core.view.c0.N(view))) {
                        if (this.f7470h != null) {
                            for (int i9 = 0; i9 < this.f7470h.size(); i9++) {
                                if (this.f7470h.get(i9).isInstance(view)) {
                                    return true;
                                }
                            }
                        }
                        return false;
                    }
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    public void X(View view) {
        if (this.H) {
            return;
        }
        k0.a<Animator, d> F = F();
        int size = F.size();
        o0 d8 = f0.d(view);
        for (int i8 = size - 1; i8 >= 0; i8--) {
            d o5 = F.o(i8);
            if (o5.f7485a != null && d8.equals(o5.f7488d)) {
                androidx.transition.a.b(F.k(i8));
            }
        }
        ArrayList<f> arrayList = this.K;
        if (arrayList != null && arrayList.size() > 0) {
            ArrayList arrayList2 = (ArrayList) this.K.clone();
            int size2 = arrayList2.size();
            for (int i9 = 0; i9 < size2; i9++) {
                ((f) arrayList2.get(i9)).b(this);
            }
        }
        this.G = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void Y(ViewGroup viewGroup) {
        d dVar;
        this.f7481z = new ArrayList<>();
        this.A = new ArrayList<>();
        V(this.f7477t, this.f7478w);
        k0.a<Animator, d> F = F();
        int size = F.size();
        o0 d8 = f0.d(viewGroup);
        for (int i8 = size - 1; i8 >= 0; i8--) {
            Animator k8 = F.k(i8);
            if (k8 != null && (dVar = F.get(k8)) != null && dVar.f7485a != null && d8.equals(dVar.f7488d)) {
                u uVar = dVar.f7487c;
                View view = dVar.f7485a;
                u M = M(view, true);
                u B = B(view, true);
                if (M == null && B == null) {
                    B = this.f7478w.f7622a.get(view);
                }
                if (!(M == null && B == null) && dVar.f7489e.N(uVar, B)) {
                    if (k8.isRunning() || k8.isStarted()) {
                        k8.cancel();
                    } else {
                        F.remove(k8);
                    }
                }
            }
        }
        v(viewGroup, this.f7477t, this.f7478w, this.f7481z, this.A);
        e0();
    }

    public Transition a0(f fVar) {
        ArrayList<f> arrayList = this.K;
        if (arrayList == null) {
            return this;
        }
        arrayList.remove(fVar);
        if (this.K.size() == 0) {
            this.K = null;
        }
        return this;
    }

    public Transition b(f fVar) {
        if (this.K == null) {
            this.K = new ArrayList<>();
        }
        this.K.add(fVar);
        return this;
    }

    public Transition b0(View view) {
        this.f7468f.remove(view);
        return this;
    }

    public Transition c(View view) {
        this.f7468f.add(view);
        return this;
    }

    public void c0(View view) {
        if (this.G) {
            if (!this.H) {
                k0.a<Animator, d> F = F();
                int size = F.size();
                o0 d8 = f0.d(view);
                for (int i8 = size - 1; i8 >= 0; i8--) {
                    d o5 = F.o(i8);
                    if (o5.f7485a != null && d8.equals(o5.f7488d)) {
                        androidx.transition.a.c(F.k(i8));
                    }
                }
                ArrayList<f> arrayList = this.K;
                if (arrayList != null && arrayList.size() > 0) {
                    ArrayList arrayList2 = (ArrayList) this.K.clone();
                    int size2 = arrayList2.size();
                    for (int i9 = 0; i9 < size2; i9++) {
                        ((f) arrayList2.get(i9)).e(this);
                    }
                }
            }
            this.G = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void cancel() {
        for (int size = this.E.size() - 1; size >= 0; size--) {
            this.E.get(size).cancel();
        }
        ArrayList<f> arrayList = this.K;
        if (arrayList == null || arrayList.size() <= 0) {
            return;
        }
        ArrayList arrayList2 = (ArrayList) this.K.clone();
        int size2 = arrayList2.size();
        for (int i8 = 0; i8 < size2; i8++) {
            ((f) arrayList2.get(i8)).d(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void e0() {
        m0();
        k0.a<Animator, d> F = F();
        Iterator<Animator> it = this.L.iterator();
        while (it.hasNext()) {
            Animator next = it.next();
            if (F.containsKey(next)) {
                m0();
                d0(next, F);
            }
        }
        this.L.clear();
        w();
    }

    public Transition f0(long j8) {
        this.f7465c = j8;
        return this;
    }

    protected void g(Animator animator) {
        if (animator == null) {
            w();
            return;
        }
        if (x() >= 0) {
            animator.setDuration(x());
        }
        if (G() >= 0) {
            animator.setStartDelay(G() + animator.getStartDelay());
        }
        if (A() != null) {
            animator.setInterpolator(A());
        }
        animator.addListener(new c());
        animator.start();
    }

    public void g0(e eVar) {
        this.P = eVar;
    }

    public Transition h0(TimeInterpolator timeInterpolator) {
        this.f7466d = timeInterpolator;
        return this;
    }

    public void i0(int... iArr) {
        if (iArr == null || iArr.length == 0) {
            this.f7480y = T;
            return;
        }
        for (int i8 = 0; i8 < iArr.length; i8++) {
            if (!O(iArr[i8])) {
                throw new IllegalArgumentException("matches contains invalid value");
            }
            if (f(iArr, i8)) {
                throw new IllegalArgumentException("matches contains a duplicate value");
            }
        }
        this.f7480y = (int[]) iArr.clone();
    }

    public abstract void j(u uVar);

    public void j0(PathMotion pathMotion) {
        if (pathMotion == null) {
            pathMotion = W;
        }
        this.R = pathMotion;
    }

    public void k0(x1.d dVar) {
        this.O = dVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void l(u uVar) {
        String[] b9;
        if (this.O == null || uVar.f7619a.isEmpty() || (b9 = this.O.b()) == null) {
            return;
        }
        boolean z4 = false;
        int i8 = 0;
        while (true) {
            if (i8 >= b9.length) {
                z4 = true;
                break;
            } else if (!uVar.f7619a.containsKey(b9[i8])) {
                break;
            } else {
                i8++;
            }
        }
        if (z4) {
            return;
        }
        this.O.a(uVar);
    }

    public Transition l0(long j8) {
        this.f7464b = j8;
        return this;
    }

    public abstract void m(u uVar);

    /* JADX INFO: Access modifiers changed from: protected */
    public void m0() {
        if (this.F == 0) {
            ArrayList<f> arrayList = this.K;
            if (arrayList != null && arrayList.size() > 0) {
                ArrayList arrayList2 = (ArrayList) this.K.clone();
                int size = arrayList2.size();
                for (int i8 = 0; i8 < size; i8++) {
                    ((f) arrayList2.get(i8)).a(this);
                }
            }
            this.H = false;
        }
        this.F++;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String n0(String str) {
        String str2 = str + getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + ": ";
        if (this.f7465c != -1) {
            str2 = str2 + "dur(" + this.f7465c + ") ";
        }
        if (this.f7464b != -1) {
            str2 = str2 + "dly(" + this.f7464b + ") ";
        }
        if (this.f7466d != null) {
            str2 = str2 + "interp(" + this.f7466d + ") ";
        }
        if (this.f7467e.size() > 0 || this.f7468f.size() > 0) {
            String str3 = str2 + "tgts(";
            if (this.f7467e.size() > 0) {
                for (int i8 = 0; i8 < this.f7467e.size(); i8++) {
                    if (i8 > 0) {
                        str3 = str3 + ", ";
                    }
                    str3 = str3 + this.f7467e.get(i8);
                }
            }
            if (this.f7468f.size() > 0) {
                for (int i9 = 0; i9 < this.f7468f.size(); i9++) {
                    if (i9 > 0) {
                        str3 = str3 + ", ";
                    }
                    str3 = str3 + this.f7468f.get(i9);
                }
            }
            return str3 + ")";
        }
        return str2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void o(ViewGroup viewGroup, boolean z4) {
        ArrayList<String> arrayList;
        ArrayList<Class<?>> arrayList2;
        k0.a<String, String> aVar;
        q(z4);
        if ((this.f7467e.size() > 0 || this.f7468f.size() > 0) && (((arrayList = this.f7469g) == null || arrayList.isEmpty()) && ((arrayList2 = this.f7470h) == null || arrayList2.isEmpty()))) {
            for (int i8 = 0; i8 < this.f7467e.size(); i8++) {
                View findViewById = viewGroup.findViewById(this.f7467e.get(i8).intValue());
                if (findViewById != null) {
                    u uVar = new u(findViewById);
                    if (z4) {
                        m(uVar);
                    } else {
                        j(uVar);
                    }
                    uVar.f7621c.add(this);
                    l(uVar);
                    e(z4 ? this.f7477t : this.f7478w, findViewById, uVar);
                }
            }
            for (int i9 = 0; i9 < this.f7468f.size(); i9++) {
                View view = this.f7468f.get(i9);
                u uVar2 = new u(view);
                if (z4) {
                    m(uVar2);
                } else {
                    j(uVar2);
                }
                uVar2.f7621c.add(this);
                l(uVar2);
                e(z4 ? this.f7477t : this.f7478w, view, uVar2);
            }
        } else {
            k(viewGroup, z4);
        }
        if (z4 || (aVar = this.Q) == null) {
            return;
        }
        int size = aVar.size();
        ArrayList arrayList3 = new ArrayList(size);
        for (int i10 = 0; i10 < size; i10++) {
            arrayList3.add(this.f7477t.f7625d.remove(this.Q.k(i10)));
        }
        for (int i11 = 0; i11 < size; i11++) {
            View view2 = (View) arrayList3.get(i11);
            if (view2 != null) {
                this.f7477t.f7625d.put(this.Q.o(i11), view2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void q(boolean z4) {
        v vVar;
        if (z4) {
            this.f7477t.f7622a.clear();
            this.f7477t.f7623b.clear();
            vVar = this.f7477t;
        } else {
            this.f7478w.f7622a.clear();
            this.f7478w.f7623b.clear();
            vVar = this.f7478w;
        }
        vVar.f7624c.c();
    }

    @Override // 
    /* renamed from: r */
    public Transition clone() {
        try {
            Transition transition = (Transition) super.clone();
            transition.L = new ArrayList<>();
            transition.f7477t = new v();
            transition.f7478w = new v();
            transition.f7481z = null;
            transition.A = null;
            return transition;
        } catch (CloneNotSupportedException unused) {
            return null;
        }
    }

    public Animator s(ViewGroup viewGroup, u uVar, u uVar2) {
        return null;
    }

    public String toString() {
        return n0(BuildConfig.FLAVOR);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void v(ViewGroup viewGroup, v vVar, v vVar2, ArrayList<u> arrayList, ArrayList<u> arrayList2) {
        Animator s8;
        int i8;
        int i9;
        View view;
        Animator animator;
        u uVar;
        Animator animator2;
        u uVar2;
        k0.a<Animator, d> F = F();
        SparseIntArray sparseIntArray = new SparseIntArray();
        int size = arrayList.size();
        long j8 = Long.MAX_VALUE;
        int i10 = 0;
        while (i10 < size) {
            u uVar3 = arrayList.get(i10);
            u uVar4 = arrayList2.get(i10);
            if (uVar3 != null && !uVar3.f7621c.contains(this)) {
                uVar3 = null;
            }
            if (uVar4 != null && !uVar4.f7621c.contains(this)) {
                uVar4 = null;
            }
            if (uVar3 != null || uVar4 != null) {
                if ((uVar3 == null || uVar4 == null || N(uVar3, uVar4)) && (s8 = s(viewGroup, uVar3, uVar4)) != null) {
                    if (uVar4 != null) {
                        view = uVar4.f7620b;
                        String[] L = L();
                        if (L != null && L.length > 0) {
                            uVar2 = new u(view);
                            i8 = size;
                            u uVar5 = vVar2.f7622a.get(view);
                            if (uVar5 != null) {
                                int i11 = 0;
                                while (i11 < L.length) {
                                    uVar2.f7619a.put(L[i11], uVar5.f7619a.get(L[i11]));
                                    i11++;
                                    i10 = i10;
                                    uVar5 = uVar5;
                                }
                            }
                            i9 = i10;
                            int size2 = F.size();
                            int i12 = 0;
                            while (true) {
                                if (i12 >= size2) {
                                    animator2 = s8;
                                    break;
                                }
                                d dVar = F.get(F.k(i12));
                                if (dVar.f7487c != null && dVar.f7485a == view && dVar.f7486b.equals(C()) && dVar.f7487c.equals(uVar2)) {
                                    animator2 = null;
                                    break;
                                }
                                i12++;
                            }
                        } else {
                            i8 = size;
                            i9 = i10;
                            animator2 = s8;
                            uVar2 = null;
                        }
                        animator = animator2;
                        uVar = uVar2;
                    } else {
                        i8 = size;
                        i9 = i10;
                        view = uVar3.f7620b;
                        animator = s8;
                        uVar = null;
                    }
                    if (animator != null) {
                        x1.d dVar2 = this.O;
                        if (dVar2 != null) {
                            long c9 = dVar2.c(viewGroup, this, uVar3, uVar4);
                            sparseIntArray.put(this.L.size(), (int) c9);
                            j8 = Math.min(c9, j8);
                        }
                        F.put(animator, new d(view, C(), this, f0.d(viewGroup), uVar));
                        this.L.add(animator);
                        j8 = j8;
                    }
                    i10 = i9 + 1;
                    size = i8;
                }
            }
            i8 = size;
            i9 = i10;
            i10 = i9 + 1;
            size = i8;
        }
        if (sparseIntArray.size() != 0) {
            for (int i13 = 0; i13 < sparseIntArray.size(); i13++) {
                Animator animator3 = this.L.get(sparseIntArray.keyAt(i13));
                animator3.setStartDelay((sparseIntArray.valueAt(i13) - j8) + animator3.getStartDelay());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void w() {
        int i8 = this.F - 1;
        this.F = i8;
        if (i8 == 0) {
            ArrayList<f> arrayList = this.K;
            if (arrayList != null && arrayList.size() > 0) {
                ArrayList arrayList2 = (ArrayList) this.K.clone();
                int size = arrayList2.size();
                for (int i9 = 0; i9 < size; i9++) {
                    ((f) arrayList2.get(i9)).c(this);
                }
            }
            for (int i10 = 0; i10 < this.f7477t.f7624c.q(); i10++) {
                View r4 = this.f7477t.f7624c.r(i10);
                if (r4 != null) {
                    androidx.core.view.c0.D0(r4, false);
                }
            }
            for (int i11 = 0; i11 < this.f7478w.f7624c.q(); i11++) {
                View r8 = this.f7478w.f7624c.r(i11);
                if (r8 != null) {
                    androidx.core.view.c0.D0(r8, false);
                }
            }
            this.H = true;
        }
    }

    public long x() {
        return this.f7465c;
    }

    public Rect y() {
        e eVar = this.P;
        if (eVar == null) {
            return null;
        }
        return eVar.a(this);
    }

    public e z() {
        return this.P;
    }
}
