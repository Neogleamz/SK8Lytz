package androidx.transition;

import android.animation.TimeInterpolator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.util.AndroidRuntimeException;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.transition.Transition;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class TransitionSet extends Transition {
    private ArrayList<Transition> Y;
    private boolean Z;

    /* renamed from: a0  reason: collision with root package name */
    int f7490a0;

    /* renamed from: b0  reason: collision with root package name */
    boolean f7491b0;

    /* renamed from: c0  reason: collision with root package name */
    private int f7492c0;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends r {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ Transition f7493a;

        a(Transition transition) {
            this.f7493a = transition;
        }

        @Override // androidx.transition.Transition.f
        public void c(Transition transition) {
            this.f7493a.e0();
            transition.a0(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b extends r {

        /* renamed from: a  reason: collision with root package name */
        TransitionSet f7495a;

        b(TransitionSet transitionSet) {
            this.f7495a = transitionSet;
        }

        @Override // androidx.transition.r, androidx.transition.Transition.f
        public void a(Transition transition) {
            TransitionSet transitionSet = this.f7495a;
            if (transitionSet.f7491b0) {
                return;
            }
            transitionSet.m0();
            this.f7495a.f7491b0 = true;
        }

        @Override // androidx.transition.Transition.f
        public void c(Transition transition) {
            TransitionSet transitionSet = this.f7495a;
            int i8 = transitionSet.f7490a0 - 1;
            transitionSet.f7490a0 = i8;
            if (i8 == 0) {
                transitionSet.f7491b0 = false;
                transitionSet.w();
            }
            transition.a0(this);
        }
    }

    public TransitionSet() {
        this.Y = new ArrayList<>();
        this.Z = true;
        this.f7491b0 = false;
        this.f7492c0 = 0;
    }

    @SuppressLint({"RestrictedApi"})
    public TransitionSet(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.Y = new ArrayList<>();
        this.Z = true;
        this.f7491b0 = false;
        this.f7492c0 = 0;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, q.f7603i);
        y0(androidx.core.content.res.k.g(obtainStyledAttributes, (XmlResourceParser) attributeSet, "transitionOrdering", 0, 0));
        obtainStyledAttributes.recycle();
    }

    private void A0() {
        b bVar = new b(this);
        Iterator<Transition> it = this.Y.iterator();
        while (it.hasNext()) {
            it.next().b(bVar);
        }
        this.f7490a0 = this.Y.size();
    }

    private void r0(Transition transition) {
        this.Y.add(transition);
        transition.f7479x = this;
    }

    @Override // androidx.transition.Transition
    public void X(View view) {
        super.X(view);
        int size = this.Y.size();
        for (int i8 = 0; i8 < size; i8++) {
            this.Y.get(i8).X(view);
        }
    }

    @Override // androidx.transition.Transition
    public void c0(View view) {
        super.c0(view);
        int size = this.Y.size();
        for (int i8 = 0; i8 < size; i8++) {
            this.Y.get(i8).c0(view);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.transition.Transition
    public void cancel() {
        super.cancel();
        int size = this.Y.size();
        for (int i8 = 0; i8 < size; i8++) {
            this.Y.get(i8).cancel();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.transition.Transition
    public void e0() {
        if (this.Y.isEmpty()) {
            m0();
            w();
            return;
        }
        A0();
        if (this.Z) {
            Iterator<Transition> it = this.Y.iterator();
            while (it.hasNext()) {
                it.next().e0();
            }
            return;
        }
        for (int i8 = 1; i8 < this.Y.size(); i8++) {
            this.Y.get(i8 - 1).b(new a(this.Y.get(i8)));
        }
        Transition transition = this.Y.get(0);
        if (transition != null) {
            transition.e0();
        }
    }

    @Override // androidx.transition.Transition
    public void g0(Transition.e eVar) {
        super.g0(eVar);
        this.f7492c0 |= 8;
        int size = this.Y.size();
        for (int i8 = 0; i8 < size; i8++) {
            this.Y.get(i8).g0(eVar);
        }
    }

    @Override // androidx.transition.Transition
    public void j(u uVar) {
        if (P(uVar.f7620b)) {
            Iterator<Transition> it = this.Y.iterator();
            while (it.hasNext()) {
                Transition next = it.next();
                if (next.P(uVar.f7620b)) {
                    next.j(uVar);
                    uVar.f7621c.add(next);
                }
            }
        }
    }

    @Override // androidx.transition.Transition
    public void j0(PathMotion pathMotion) {
        super.j0(pathMotion);
        this.f7492c0 |= 4;
        if (this.Y != null) {
            for (int i8 = 0; i8 < this.Y.size(); i8++) {
                this.Y.get(i8).j0(pathMotion);
            }
        }
    }

    @Override // androidx.transition.Transition
    public void k0(x1.d dVar) {
        super.k0(dVar);
        this.f7492c0 |= 2;
        int size = this.Y.size();
        for (int i8 = 0; i8 < size; i8++) {
            this.Y.get(i8).k0(dVar);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.transition.Transition
    public void l(u uVar) {
        super.l(uVar);
        int size = this.Y.size();
        for (int i8 = 0; i8 < size; i8++) {
            this.Y.get(i8).l(uVar);
        }
    }

    @Override // androidx.transition.Transition
    public void m(u uVar) {
        if (P(uVar.f7620b)) {
            Iterator<Transition> it = this.Y.iterator();
            while (it.hasNext()) {
                Transition next = it.next();
                if (next.P(uVar.f7620b)) {
                    next.m(uVar);
                    uVar.f7621c.add(next);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.transition.Transition
    public String n0(String str) {
        String n02 = super.n0(str);
        for (int i8 = 0; i8 < this.Y.size(); i8++) {
            StringBuilder sb = new StringBuilder();
            sb.append(n02);
            sb.append("\n");
            sb.append(this.Y.get(i8).n0(str + "  "));
            n02 = sb.toString();
        }
        return n02;
    }

    @Override // androidx.transition.Transition
    /* renamed from: o0 */
    public TransitionSet b(Transition.f fVar) {
        return (TransitionSet) super.b(fVar);
    }

    @Override // androidx.transition.Transition
    /* renamed from: p0 */
    public TransitionSet c(View view) {
        for (int i8 = 0; i8 < this.Y.size(); i8++) {
            this.Y.get(i8).c(view);
        }
        return (TransitionSet) super.c(view);
    }

    public TransitionSet q0(Transition transition) {
        r0(transition);
        long j8 = this.f7465c;
        if (j8 >= 0) {
            transition.f0(j8);
        }
        if ((this.f7492c0 & 1) != 0) {
            transition.h0(A());
        }
        if ((this.f7492c0 & 2) != 0) {
            transition.k0(E());
        }
        if ((this.f7492c0 & 4) != 0) {
            transition.j0(D());
        }
        if ((this.f7492c0 & 8) != 0) {
            transition.g0(z());
        }
        return this;
    }

    @Override // androidx.transition.Transition
    /* renamed from: r */
    public Transition clone() {
        TransitionSet transitionSet = (TransitionSet) super.clone();
        transitionSet.Y = new ArrayList<>();
        int size = this.Y.size();
        for (int i8 = 0; i8 < size; i8++) {
            transitionSet.r0(this.Y.get(i8).clone());
        }
        return transitionSet;
    }

    public Transition s0(int i8) {
        if (i8 < 0 || i8 >= this.Y.size()) {
            return null;
        }
        return this.Y.get(i8);
    }

    public int t0() {
        return this.Y.size();
    }

    @Override // androidx.transition.Transition
    /* renamed from: u0 */
    public TransitionSet a0(Transition.f fVar) {
        return (TransitionSet) super.a0(fVar);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.transition.Transition
    public void v(ViewGroup viewGroup, v vVar, v vVar2, ArrayList<u> arrayList, ArrayList<u> arrayList2) {
        long G = G();
        int size = this.Y.size();
        for (int i8 = 0; i8 < size; i8++) {
            Transition transition = this.Y.get(i8);
            if (G > 0 && (this.Z || i8 == 0)) {
                long G2 = transition.G();
                if (G2 > 0) {
                    transition.l0(G2 + G);
                } else {
                    transition.l0(G);
                }
            }
            transition.v(viewGroup, vVar, vVar2, arrayList, arrayList2);
        }
    }

    @Override // androidx.transition.Transition
    /* renamed from: v0 */
    public TransitionSet b0(View view) {
        for (int i8 = 0; i8 < this.Y.size(); i8++) {
            this.Y.get(i8).b0(view);
        }
        return (TransitionSet) super.b0(view);
    }

    @Override // androidx.transition.Transition
    /* renamed from: w0 */
    public TransitionSet f0(long j8) {
        ArrayList<Transition> arrayList;
        super.f0(j8);
        if (this.f7465c >= 0 && (arrayList = this.Y) != null) {
            int size = arrayList.size();
            for (int i8 = 0; i8 < size; i8++) {
                this.Y.get(i8).f0(j8);
            }
        }
        return this;
    }

    @Override // androidx.transition.Transition
    /* renamed from: x0 */
    public TransitionSet h0(TimeInterpolator timeInterpolator) {
        this.f7492c0 |= 1;
        ArrayList<Transition> arrayList = this.Y;
        if (arrayList != null) {
            int size = arrayList.size();
            for (int i8 = 0; i8 < size; i8++) {
                this.Y.get(i8).h0(timeInterpolator);
            }
        }
        return (TransitionSet) super.h0(timeInterpolator);
    }

    public TransitionSet y0(int i8) {
        if (i8 == 0) {
            this.Z = true;
        } else if (i8 != 1) {
            throw new AndroidRuntimeException("Invalid parameter for TransitionSet ordering: " + i8);
        } else {
            this.Z = false;
        }
        return this;
    }

    @Override // androidx.transition.Transition
    /* renamed from: z0 */
    public TransitionSet l0(long j8) {
        return (TransitionSet) super.l0(j8);
    }
}
