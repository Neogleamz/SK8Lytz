package androidx.constraintlayout.solver.widgets.analyzer;

import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class c {

    /* renamed from: a  reason: collision with root package name */
    private androidx.constraintlayout.solver.widgets.d f3738a;

    /* renamed from: d  reason: collision with root package name */
    private androidx.constraintlayout.solver.widgets.d f3741d;

    /* renamed from: b  reason: collision with root package name */
    private boolean f3739b = true;

    /* renamed from: c  reason: collision with root package name */
    private boolean f3740c = true;

    /* renamed from: e  reason: collision with root package name */
    private ArrayList<k> f3742e = new ArrayList<>();

    /* renamed from: f  reason: collision with root package name */
    private ArrayList<i> f3743f = new ArrayList<>();

    /* renamed from: g  reason: collision with root package name */
    private BasicMeasure.b f3744g = null;

    /* renamed from: h  reason: collision with root package name */
    private BasicMeasure.a f3745h = new BasicMeasure.a();

    /* renamed from: i  reason: collision with root package name */
    ArrayList<i> f3746i = new ArrayList<>();

    public c(androidx.constraintlayout.solver.widgets.d dVar) {
        this.f3738a = dVar;
        this.f3741d = dVar;
    }

    private void a(d dVar, int i8, int i9, d dVar2, ArrayList<i> arrayList, i iVar) {
        k kVar = dVar.f3750d;
        if (kVar.f3784c == null) {
            androidx.constraintlayout.solver.widgets.d dVar3 = this.f3738a;
            if (kVar == dVar3.f3672e || kVar == dVar3.f3674f) {
                return;
            }
            if (iVar == null) {
                iVar = new i(kVar, i9);
                arrayList.add(iVar);
            }
            kVar.f3784c = iVar;
            iVar.a(kVar);
            for (o0.a aVar : kVar.f3789h.f3757k) {
                if (aVar instanceof d) {
                    a((d) aVar, i8, 0, dVar2, arrayList, iVar);
                }
            }
            for (o0.a aVar2 : kVar.f3790i.f3757k) {
                if (aVar2 instanceof d) {
                    a((d) aVar2, i8, 1, dVar2, arrayList, iVar);
                }
            }
            if (i8 == 1 && (kVar instanceof j)) {
                for (o0.a aVar3 : ((j) kVar).f3779k.f3757k) {
                    if (aVar3 instanceof d) {
                        a((d) aVar3, i8, 2, dVar2, arrayList, iVar);
                    }
                }
            }
            for (d dVar4 : kVar.f3789h.f3758l) {
                if (dVar4 == dVar2) {
                    iVar.f3773b = true;
                }
                a(dVar4, i8, 0, dVar2, arrayList, iVar);
            }
            for (d dVar5 : kVar.f3790i.f3758l) {
                if (dVar5 == dVar2) {
                    iVar.f3773b = true;
                }
                a(dVar5, i8, 1, dVar2, arrayList, iVar);
            }
            if (i8 == 1 && (kVar instanceof j)) {
                for (d dVar6 : ((j) kVar).f3779k.f3758l) {
                    a(dVar6, i8, 2, dVar2, arrayList, iVar);
                }
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:39:0x0074, code lost:
        if (r2.f3688m == 0) goto L30;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean b(androidx.constraintlayout.solver.widgets.d r17) {
        /*
            Method dump skipped, instructions count: 621
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.analyzer.c.b(androidx.constraintlayout.solver.widgets.d):boolean");
    }

    private int e(androidx.constraintlayout.solver.widgets.d dVar, int i8) {
        int size = this.f3746i.size();
        long j8 = 0;
        for (int i9 = 0; i9 < size; i9++) {
            j8 = Math.max(j8, this.f3746i.get(i9).b(dVar, i8));
        }
        return (int) j8;
    }

    private void i(k kVar, int i8, ArrayList<i> arrayList) {
        for (o0.a aVar : kVar.f3789h.f3757k) {
            if (aVar instanceof d) {
                a((d) aVar, i8, 0, kVar.f3790i, arrayList, null);
            } else if (aVar instanceof k) {
                a(((k) aVar).f3789h, i8, 0, kVar.f3790i, arrayList, null);
            }
        }
        for (o0.a aVar2 : kVar.f3790i.f3757k) {
            if (aVar2 instanceof d) {
                a((d) aVar2, i8, 1, kVar.f3789h, arrayList, null);
            } else if (aVar2 instanceof k) {
                a(((k) aVar2).f3790i, i8, 1, kVar.f3789h, arrayList, null);
            }
        }
        if (i8 == 1) {
            for (o0.a aVar3 : ((j) kVar).f3779k.f3757k) {
                if (aVar3 instanceof d) {
                    a((d) aVar3, i8, 2, null, arrayList, null);
                }
            }
        }
    }

    private void l(ConstraintWidget constraintWidget, ConstraintWidget.DimensionBehaviour dimensionBehaviour, int i8, ConstraintWidget.DimensionBehaviour dimensionBehaviour2, int i9) {
        BasicMeasure.a aVar = this.f3745h;
        aVar.f3726a = dimensionBehaviour;
        aVar.f3727b = dimensionBehaviour2;
        aVar.f3728c = i8;
        aVar.f3729d = i9;
        this.f3744g.b(constraintWidget, aVar);
        constraintWidget.F0(this.f3745h.f3730e);
        constraintWidget.i0(this.f3745h.f3731f);
        constraintWidget.h0(this.f3745h.f3733h);
        constraintWidget.c0(this.f3745h.f3732g);
    }

    public void c() {
        d(this.f3742e);
        this.f3746i.clear();
        i.f3771h = 0;
        i(this.f3738a.f3672e, 0, this.f3746i);
        i(this.f3738a.f3674f, 1, this.f3746i);
        this.f3739b = false;
    }

    public void d(ArrayList<k> arrayList) {
        k fVar;
        arrayList.clear();
        this.f3741d.f3672e.f();
        this.f3741d.f3674f.f();
        arrayList.add(this.f3741d.f3672e);
        arrayList.add(this.f3741d.f3674f);
        Iterator<ConstraintWidget> it = this.f3741d.G0.iterator();
        HashSet hashSet = null;
        while (it.hasNext()) {
            ConstraintWidget next = it.next();
            if (next instanceof androidx.constraintlayout.solver.widgets.f) {
                fVar = new f(next);
            } else {
                if (next.W()) {
                    if (next.f3668c == null) {
                        next.f3668c = new b(next, 0);
                    }
                    if (hashSet == null) {
                        hashSet = new HashSet();
                    }
                    hashSet.add(next.f3668c);
                } else {
                    arrayList.add(next.f3672e);
                }
                if (next.Y()) {
                    if (next.f3670d == null) {
                        next.f3670d = new b(next, 1);
                    }
                    if (hashSet == null) {
                        hashSet = new HashSet();
                    }
                    hashSet.add(next.f3670d);
                } else {
                    arrayList.add(next.f3674f);
                }
                if (next instanceof n0.b) {
                    fVar = new g(next);
                }
            }
            arrayList.add(fVar);
        }
        if (hashSet != null) {
            arrayList.addAll(hashSet);
        }
        Iterator<k> it2 = arrayList.iterator();
        while (it2.hasNext()) {
            it2.next().f();
        }
        Iterator<k> it3 = arrayList.iterator();
        while (it3.hasNext()) {
            k next2 = it3.next();
            if (next2.f3783b != this.f3741d) {
                next2.d();
            }
        }
    }

    public boolean f(boolean z4) {
        boolean z8;
        boolean z9 = true;
        boolean z10 = z4 & true;
        if (this.f3739b || this.f3740c) {
            Iterator<ConstraintWidget> it = this.f3738a.G0.iterator();
            while (it.hasNext()) {
                ConstraintWidget next = it.next();
                next.f3664a = false;
                next.f3672e.r();
                next.f3674f.q();
            }
            androidx.constraintlayout.solver.widgets.d dVar = this.f3738a;
            dVar.f3664a = false;
            dVar.f3672e.r();
            this.f3738a.f3674f.q();
            this.f3740c = false;
        }
        if (b(this.f3741d)) {
            return false;
        }
        this.f3738a.G0(0);
        this.f3738a.H0(0);
        ConstraintWidget.DimensionBehaviour t8 = this.f3738a.t(0);
        ConstraintWidget.DimensionBehaviour t9 = this.f3738a.t(1);
        if (this.f3739b) {
            c();
        }
        int R = this.f3738a.R();
        int S = this.f3738a.S();
        this.f3738a.f3672e.f3789h.d(R);
        this.f3738a.f3674f.f3789h.d(S);
        m();
        ConstraintWidget.DimensionBehaviour dimensionBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        if (t8 == dimensionBehaviour || t9 == dimensionBehaviour) {
            if (z10) {
                Iterator<k> it2 = this.f3742e.iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    } else if (!it2.next().m()) {
                        z10 = false;
                        break;
                    }
                }
            }
            if (z10 && t8 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                this.f3738a.m0(ConstraintWidget.DimensionBehaviour.FIXED);
                androidx.constraintlayout.solver.widgets.d dVar2 = this.f3738a;
                dVar2.F0(e(dVar2, 0));
                androidx.constraintlayout.solver.widgets.d dVar3 = this.f3738a;
                dVar3.f3672e.f3786e.d(dVar3.Q());
            }
            if (z10 && t9 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                this.f3738a.B0(ConstraintWidget.DimensionBehaviour.FIXED);
                androidx.constraintlayout.solver.widgets.d dVar4 = this.f3738a;
                dVar4.i0(e(dVar4, 1));
                androidx.constraintlayout.solver.widgets.d dVar5 = this.f3738a;
                dVar5.f3674f.f3786e.d(dVar5.w());
            }
        }
        androidx.constraintlayout.solver.widgets.d dVar6 = this.f3738a;
        ConstraintWidget.DimensionBehaviour[] dimensionBehaviourArr = dVar6.O;
        ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = dimensionBehaviourArr[0];
        ConstraintWidget.DimensionBehaviour dimensionBehaviour3 = ConstraintWidget.DimensionBehaviour.FIXED;
        if (dimensionBehaviour2 == dimensionBehaviour3 || dimensionBehaviourArr[0] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
            int Q = dVar6.Q() + R;
            this.f3738a.f3672e.f3790i.d(Q);
            this.f3738a.f3672e.f3786e.d(Q - R);
            m();
            androidx.constraintlayout.solver.widgets.d dVar7 = this.f3738a;
            ConstraintWidget.DimensionBehaviour[] dimensionBehaviourArr2 = dVar7.O;
            if (dimensionBehaviourArr2[1] == dimensionBehaviour3 || dimensionBehaviourArr2[1] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
                int w8 = dVar7.w() + S;
                this.f3738a.f3674f.f3790i.d(w8);
                this.f3738a.f3674f.f3786e.d(w8 - S);
            }
            m();
            z8 = true;
        } else {
            z8 = false;
        }
        Iterator<k> it3 = this.f3742e.iterator();
        while (it3.hasNext()) {
            k next2 = it3.next();
            if (next2.f3783b != this.f3738a || next2.f3788g) {
                next2.e();
            }
        }
        Iterator<k> it4 = this.f3742e.iterator();
        while (it4.hasNext()) {
            k next3 = it4.next();
            if (z8 || next3.f3783b != this.f3738a) {
                if (!next3.f3789h.f3756j || ((!next3.f3790i.f3756j && !(next3 instanceof f)) || (!next3.f3786e.f3756j && !(next3 instanceof b) && !(next3 instanceof f)))) {
                    z9 = false;
                    break;
                }
            }
        }
        this.f3738a.m0(t8);
        this.f3738a.B0(t9);
        return z9;
    }

    public boolean g(boolean z4) {
        if (this.f3739b) {
            Iterator<ConstraintWidget> it = this.f3738a.G0.iterator();
            while (it.hasNext()) {
                ConstraintWidget next = it.next();
                next.f3664a = false;
                h hVar = next.f3672e;
                hVar.f3786e.f3756j = false;
                hVar.f3788g = false;
                hVar.r();
                j jVar = next.f3674f;
                jVar.f3786e.f3756j = false;
                jVar.f3788g = false;
                jVar.q();
            }
            androidx.constraintlayout.solver.widgets.d dVar = this.f3738a;
            dVar.f3664a = false;
            h hVar2 = dVar.f3672e;
            hVar2.f3786e.f3756j = false;
            hVar2.f3788g = false;
            hVar2.r();
            j jVar2 = this.f3738a.f3674f;
            jVar2.f3786e.f3756j = false;
            jVar2.f3788g = false;
            jVar2.q();
            c();
        }
        if (b(this.f3741d)) {
            return false;
        }
        this.f3738a.G0(0);
        this.f3738a.H0(0);
        this.f3738a.f3672e.f3789h.d(0);
        this.f3738a.f3674f.f3789h.d(0);
        return true;
    }

    public boolean h(boolean z4, int i8) {
        boolean z8;
        ConstraintWidget.DimensionBehaviour dimensionBehaviour;
        e eVar;
        int w8;
        boolean z9 = true;
        boolean z10 = z4 & true;
        ConstraintWidget.DimensionBehaviour t8 = this.f3738a.t(0);
        ConstraintWidget.DimensionBehaviour t9 = this.f3738a.t(1);
        int R = this.f3738a.R();
        int S = this.f3738a.S();
        if (z10 && (t8 == (dimensionBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) || t9 == dimensionBehaviour)) {
            Iterator<k> it = this.f3742e.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                k next = it.next();
                if (next.f3787f == i8 && !next.m()) {
                    z10 = false;
                    break;
                }
            }
            if (i8 == 0) {
                if (z10 && t8 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                    this.f3738a.m0(ConstraintWidget.DimensionBehaviour.FIXED);
                    androidx.constraintlayout.solver.widgets.d dVar = this.f3738a;
                    dVar.F0(e(dVar, 0));
                    androidx.constraintlayout.solver.widgets.d dVar2 = this.f3738a;
                    eVar = dVar2.f3672e.f3786e;
                    w8 = dVar2.Q();
                    eVar.d(w8);
                }
            } else if (z10 && t9 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                this.f3738a.B0(ConstraintWidget.DimensionBehaviour.FIXED);
                androidx.constraintlayout.solver.widgets.d dVar3 = this.f3738a;
                dVar3.i0(e(dVar3, 1));
                androidx.constraintlayout.solver.widgets.d dVar4 = this.f3738a;
                eVar = dVar4.f3674f.f3786e;
                w8 = dVar4.w();
                eVar.d(w8);
            }
        }
        androidx.constraintlayout.solver.widgets.d dVar5 = this.f3738a;
        if (i8 == 0) {
            ConstraintWidget.DimensionBehaviour[] dimensionBehaviourArr = dVar5.O;
            if (dimensionBehaviourArr[0] == ConstraintWidget.DimensionBehaviour.FIXED || dimensionBehaviourArr[0] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
                int Q = dVar5.Q() + R;
                this.f3738a.f3672e.f3790i.d(Q);
                this.f3738a.f3672e.f3786e.d(Q - R);
                z8 = true;
            }
            z8 = false;
        } else {
            ConstraintWidget.DimensionBehaviour[] dimensionBehaviourArr2 = dVar5.O;
            if (dimensionBehaviourArr2[1] == ConstraintWidget.DimensionBehaviour.FIXED || dimensionBehaviourArr2[1] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
                int w9 = dVar5.w() + S;
                this.f3738a.f3674f.f3790i.d(w9);
                this.f3738a.f3674f.f3786e.d(w9 - S);
                z8 = true;
            }
            z8 = false;
        }
        m();
        Iterator<k> it2 = this.f3742e.iterator();
        while (it2.hasNext()) {
            k next2 = it2.next();
            if (next2.f3787f == i8 && (next2.f3783b != this.f3738a || next2.f3788g)) {
                next2.e();
            }
        }
        Iterator<k> it3 = this.f3742e.iterator();
        while (it3.hasNext()) {
            k next3 = it3.next();
            if (next3.f3787f == i8 && (z8 || next3.f3783b != this.f3738a)) {
                if (!next3.f3789h.f3756j || !next3.f3790i.f3756j || (!(next3 instanceof b) && !next3.f3786e.f3756j)) {
                    z9 = false;
                    break;
                }
            }
        }
        this.f3738a.m0(t8);
        this.f3738a.B0(t9);
        return z9;
    }

    public void j() {
        this.f3739b = true;
    }

    public void k() {
        this.f3740c = true;
    }

    /* JADX WARN: Removed duplicated region for block: B:48:0x00b2 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:52:0x0008 A[ADDED_TO_REGION, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void m() {
        /*
            r12 = this;
            androidx.constraintlayout.solver.widgets.d r0 = r12.f3738a
            java.util.ArrayList<androidx.constraintlayout.solver.widgets.ConstraintWidget> r0 = r0.G0
            java.util.Iterator r0 = r0.iterator()
        L8:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto Lc1
            java.lang.Object r1 = r0.next()
            androidx.constraintlayout.solver.widgets.ConstraintWidget r1 = (androidx.constraintlayout.solver.widgets.ConstraintWidget) r1
            boolean r2 = r1.f3664a
            if (r2 == 0) goto L19
            goto L8
        L19:
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour[] r2 = r1.O
            r3 = 0
            r8 = r2[r3]
            r9 = 1
            r10 = r2[r9]
            int r2 = r1.f3686l
            int r4 = r1.f3688m
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r6 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.WRAP_CONTENT
            if (r8 == r6) goto L32
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r5 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r8 != r5) goto L30
            if (r2 != r9) goto L30
            goto L32
        L30:
            r2 = r3
            goto L33
        L32:
            r2 = r9
        L33:
            if (r10 == r6) goto L3b
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r5 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r10 != r5) goto L3c
            if (r4 != r9) goto L3c
        L3b:
            r3 = r9
        L3c:
            androidx.constraintlayout.solver.widgets.analyzer.h r4 = r1.f3672e
            androidx.constraintlayout.solver.widgets.analyzer.e r4 = r4.f3786e
            boolean r5 = r4.f3756j
            androidx.constraintlayout.solver.widgets.analyzer.j r7 = r1.f3674f
            androidx.constraintlayout.solver.widgets.analyzer.e r7 = r7.f3786e
            boolean r11 = r7.f3756j
            if (r5 == 0) goto L5b
            if (r11 == 0) goto L5b
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r6 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.FIXED
            int r5 = r4.f3753g
            int r7 = r7.f3753g
            r2 = r12
            r3 = r1
            r4 = r6
            r2.l(r3, r4, r5, r6, r7)
        L58:
            r1.f3664a = r9
            goto Lae
        L5b:
            if (r5 == 0) goto L87
            if (r3 == 0) goto L87
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r5 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.FIXED
            int r8 = r4.f3753g
            int r7 = r7.f3753g
            r2 = r12
            r3 = r1
            r4 = r5
            r5 = r8
            r2.l(r3, r4, r5, r6, r7)
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r2 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r10 != r2) goto L7b
            androidx.constraintlayout.solver.widgets.analyzer.j r2 = r1.f3674f
            androidx.constraintlayout.solver.widgets.analyzer.e r2 = r2.f3786e
            int r3 = r1.w()
        L78:
            r2.f3768m = r3
            goto Lae
        L7b:
            androidx.constraintlayout.solver.widgets.analyzer.j r2 = r1.f3674f
            androidx.constraintlayout.solver.widgets.analyzer.e r2 = r2.f3786e
            int r3 = r1.w()
        L83:
            r2.d(r3)
            goto L58
        L87:
            if (r11 == 0) goto Lae
            if (r2 == 0) goto Lae
            int r5 = r4.f3753g
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r10 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.FIXED
            int r7 = r7.f3753g
            r2 = r12
            r3 = r1
            r4 = r6
            r6 = r10
            r2.l(r3, r4, r5, r6, r7)
            androidx.constraintlayout.solver.widgets.ConstraintWidget$DimensionBehaviour r2 = androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT
            if (r8 != r2) goto La5
            androidx.constraintlayout.solver.widgets.analyzer.h r2 = r1.f3672e
            androidx.constraintlayout.solver.widgets.analyzer.e r2 = r2.f3786e
            int r3 = r1.Q()
            goto L78
        La5:
            androidx.constraintlayout.solver.widgets.analyzer.h r2 = r1.f3672e
            androidx.constraintlayout.solver.widgets.analyzer.e r2 = r2.f3786e
            int r3 = r1.Q()
            goto L83
        Lae:
            boolean r2 = r1.f3664a
            if (r2 == 0) goto L8
            androidx.constraintlayout.solver.widgets.analyzer.j r2 = r1.f3674f
            androidx.constraintlayout.solver.widgets.analyzer.e r2 = r2.f3780l
            if (r2 == 0) goto L8
            int r1 = r1.o()
            r2.d(r1)
            goto L8
        Lc1:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.analyzer.c.m():void");
    }

    public void n(BasicMeasure.b bVar) {
        this.f3744g = bVar;
    }
}
