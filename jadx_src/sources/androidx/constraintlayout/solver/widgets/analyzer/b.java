package androidx.constraintlayout.solver.widgets.analyzer;

import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class b extends k {

    /* renamed from: k  reason: collision with root package name */
    ArrayList<k> f3736k;

    /* renamed from: l  reason: collision with root package name */
    private int f3737l;

    public b(ConstraintWidget constraintWidget, int i8) {
        super(constraintWidget);
        this.f3736k = new ArrayList<>();
        this.f3787f = i8;
        q();
    }

    private void q() {
        ConstraintWidget constraintWidget;
        ConstraintWidget constraintWidget2 = this.f3783b;
        do {
            constraintWidget = constraintWidget2;
            constraintWidget2 = constraintWidget2.I(this.f3787f);
        } while (constraintWidget2 != null);
        this.f3783b = constraintWidget;
        this.f3736k.add(constraintWidget.K(this.f3787f));
        ConstraintWidget G = constraintWidget.G(this.f3787f);
        while (G != null) {
            this.f3736k.add(G.K(this.f3787f));
            G = G.G(this.f3787f);
        }
        Iterator<k> it = this.f3736k.iterator();
        while (it.hasNext()) {
            k next = it.next();
            int i8 = this.f3787f;
            if (i8 == 0) {
                next.f3783b.f3668c = this;
            } else if (i8 == 1) {
                next.f3783b.f3670d = this;
            }
        }
        if ((this.f3787f == 0 && ((androidx.constraintlayout.solver.widgets.d) this.f3783b.H()).c1()) && this.f3736k.size() > 1) {
            ArrayList<k> arrayList = this.f3736k;
            this.f3783b = arrayList.get(arrayList.size() - 1).f3783b;
        }
        this.f3737l = this.f3787f == 0 ? this.f3783b.y() : this.f3783b.M();
    }

    private ConstraintWidget r() {
        for (int i8 = 0; i8 < this.f3736k.size(); i8++) {
            k kVar = this.f3736k.get(i8);
            if (kVar.f3783b.P() != 8) {
                return kVar.f3783b;
            }
        }
        return null;
    }

    private ConstraintWidget s() {
        for (int size = this.f3736k.size() - 1; size >= 0; size--) {
            k kVar = this.f3736k.get(size);
            if (kVar.f3783b.P() != 8) {
                return kVar.f3783b;
            }
        }
        return null;
    }

    /* JADX WARN: Code restructure failed: missing block: B:110:0x019f, code lost:
        if (r1 != r7) goto L115;
     */
    /* JADX WARN: Code restructure failed: missing block: B:119:0x01c5, code lost:
        if (r1 != r7) goto L115;
     */
    /* JADX WARN: Code restructure failed: missing block: B:120:0x01c7, code lost:
        r13 = r13 + 1;
        r7 = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:121:0x01ca, code lost:
        r9.f3786e.d(r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:284:0x03e9, code lost:
        r7 = r7 - r10;
     */
    /* JADX WARN: Removed duplicated region for block: B:64:0x00d9  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x00eb  */
    @Override // androidx.constraintlayout.solver.widgets.analyzer.k, o0.a
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void a(o0.a r26) {
        /*
            Method dump skipped, instructions count: 1032
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.analyzer.b.a(o0.a):void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x006d, code lost:
        if (r1 != null) goto L23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x00a3, code lost:
        if (r1 != null) goto L23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x00a5, code lost:
        b(r5.f3790i, r1, -r0);
     */
    @Override // androidx.constraintlayout.solver.widgets.analyzer.k
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void d() {
        /*
            r5 = this;
            java.util.ArrayList<androidx.constraintlayout.solver.widgets.analyzer.k> r0 = r5.f3736k
            java.util.Iterator r0 = r0.iterator()
        L6:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L16
            java.lang.Object r1 = r0.next()
            androidx.constraintlayout.solver.widgets.analyzer.k r1 = (androidx.constraintlayout.solver.widgets.analyzer.k) r1
            r1.d()
            goto L6
        L16:
            java.util.ArrayList<androidx.constraintlayout.solver.widgets.analyzer.k> r0 = r5.f3736k
            int r0 = r0.size()
            r1 = 1
            if (r0 >= r1) goto L20
            return
        L20:
            java.util.ArrayList<androidx.constraintlayout.solver.widgets.analyzer.k> r2 = r5.f3736k
            r3 = 0
            java.lang.Object r2 = r2.get(r3)
            androidx.constraintlayout.solver.widgets.analyzer.k r2 = (androidx.constraintlayout.solver.widgets.analyzer.k) r2
            androidx.constraintlayout.solver.widgets.ConstraintWidget r2 = r2.f3783b
            java.util.ArrayList<androidx.constraintlayout.solver.widgets.analyzer.k> r4 = r5.f3736k
            int r0 = r0 - r1
            java.lang.Object r0 = r4.get(r0)
            androidx.constraintlayout.solver.widgets.analyzer.k r0 = (androidx.constraintlayout.solver.widgets.analyzer.k) r0
            androidx.constraintlayout.solver.widgets.ConstraintWidget r0 = r0.f3783b
            int r4 = r5.f3787f
            if (r4 != 0) goto L70
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r1 = r2.D
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r0.F
            androidx.constraintlayout.solver.widgets.analyzer.d r2 = r5.i(r1, r3)
            int r1 = r1.c()
            androidx.constraintlayout.solver.widgets.ConstraintWidget r4 = r5.r()
            if (r4 == 0) goto L52
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r1 = r4.D
            int r1 = r1.c()
        L52:
            if (r2 == 0) goto L59
            androidx.constraintlayout.solver.widgets.analyzer.d r4 = r5.f3789h
            r5.b(r4, r2, r1)
        L59:
            androidx.constraintlayout.solver.widgets.analyzer.d r1 = r5.i(r0, r3)
            int r0 = r0.c()
            androidx.constraintlayout.solver.widgets.ConstraintWidget r2 = r5.s()
            if (r2 == 0) goto L6d
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r2.F
            int r0 = r0.c()
        L6d:
            if (r1 == 0) goto Lab
            goto La5
        L70:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r2.E
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r0.G
            androidx.constraintlayout.solver.widgets.analyzer.d r3 = r5.i(r2, r1)
            int r2 = r2.c()
            androidx.constraintlayout.solver.widgets.ConstraintWidget r4 = r5.r()
            if (r4 == 0) goto L88
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r2 = r4.E
            int r2 = r2.c()
        L88:
            if (r3 == 0) goto L8f
            androidx.constraintlayout.solver.widgets.analyzer.d r4 = r5.f3789h
            r5.b(r4, r3, r2)
        L8f:
            androidx.constraintlayout.solver.widgets.analyzer.d r1 = r5.i(r0, r1)
            int r0 = r0.c()
            androidx.constraintlayout.solver.widgets.ConstraintWidget r2 = r5.s()
            if (r2 == 0) goto La3
            androidx.constraintlayout.solver.widgets.ConstraintAnchor r0 = r2.G
            int r0 = r0.c()
        La3:
            if (r1 == 0) goto Lab
        La5:
            androidx.constraintlayout.solver.widgets.analyzer.d r2 = r5.f3790i
            int r0 = -r0
            r5.b(r2, r1, r0)
        Lab:
            androidx.constraintlayout.solver.widgets.analyzer.d r0 = r5.f3789h
            r0.f3747a = r5
            androidx.constraintlayout.solver.widgets.analyzer.d r0 = r5.f3790i
            r0.f3747a = r5
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.analyzer.b.d():void");
    }

    @Override // androidx.constraintlayout.solver.widgets.analyzer.k
    public void e() {
        for (int i8 = 0; i8 < this.f3736k.size(); i8++) {
            this.f3736k.get(i8).e();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.constraintlayout.solver.widgets.analyzer.k
    public void f() {
        this.f3784c = null;
        Iterator<k> it = this.f3736k.iterator();
        while (it.hasNext()) {
            it.next().f();
        }
    }

    @Override // androidx.constraintlayout.solver.widgets.analyzer.k
    public long j() {
        int size = this.f3736k.size();
        long j8 = 0;
        for (int i8 = 0; i8 < size; i8++) {
            k kVar = this.f3736k.get(i8);
            j8 = j8 + kVar.f3789h.f3752f + kVar.j() + kVar.f3790i.f3752f;
        }
        return j8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.constraintlayout.solver.widgets.analyzer.k
    public boolean m() {
        int size = this.f3736k.size();
        for (int i8 = 0; i8 < size; i8++) {
            if (!this.f3736k.get(i8).m()) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        Iterator<k> it;
        StringBuilder sb = new StringBuilder();
        sb.append("ChainRun ");
        sb.append(this.f3787f == 0 ? "horizontal : " : "vertical : ");
        String sb2 = sb.toString();
        while (this.f3736k.iterator().hasNext()) {
            sb2 = ((sb2 + "<") + it.next()) + "> ";
        }
        return sb2;
    }
}
