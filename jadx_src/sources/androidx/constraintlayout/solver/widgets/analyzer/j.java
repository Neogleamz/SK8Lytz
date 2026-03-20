package androidx.constraintlayout.solver.widgets.analyzer;

import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.analyzer.d;
import androidx.constraintlayout.solver.widgets.analyzer.k;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class j extends k {

    /* renamed from: k  reason: collision with root package name */
    public d f3779k;

    /* renamed from: l  reason: collision with root package name */
    e f3780l;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static /* synthetic */ class a {

        /* renamed from: a  reason: collision with root package name */
        static final /* synthetic */ int[] f3781a;

        static {
            int[] iArr = new int[k.b.values().length];
            f3781a = iArr;
            try {
                iArr[k.b.START.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f3781a[k.b.END.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f3781a[k.b.CENTER.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    public j(ConstraintWidget constraintWidget) {
        super(constraintWidget);
        d dVar = new d(this);
        this.f3779k = dVar;
        this.f3780l = null;
        this.f3789h.f3751e = d.a.TOP;
        this.f3790i.f3751e = d.a.BOTTOM;
        dVar.f3751e = d.a.BASELINE;
        this.f3787f = 1;
    }

    @Override // androidx.constraintlayout.solver.widgets.analyzer.k, o0.a
    public void a(o0.a aVar) {
        e eVar;
        int i8;
        ConstraintWidget constraintWidget;
        float u8;
        ConstraintWidget constraintWidget2;
        int i9 = a.f3781a[this.f3791j.ordinal()];
        if (i9 == 1) {
            p(aVar);
        } else if (i9 == 2) {
            o(aVar);
        } else if (i9 == 3) {
            ConstraintWidget constraintWidget3 = this.f3783b;
            n(aVar, constraintWidget3.E, constraintWidget3.G, 1);
            return;
        }
        e eVar2 = this.f3786e;
        if (eVar2.f3749c && !eVar2.f3756j && this.f3785d == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            ConstraintWidget constraintWidget4 = this.f3783b;
            int i10 = constraintWidget4.f3688m;
            if (i10 == 2) {
                ConstraintWidget H = constraintWidget4.H();
                if (H != null) {
                    if (H.f3674f.f3786e.f3756j) {
                        i8 = (int) ((eVar.f3753g * this.f3783b.f3701t) + 0.5f);
                        this.f3786e.d(i8);
                    }
                }
            } else if (i10 == 3 && constraintWidget4.f3672e.f3786e.f3756j) {
                int v8 = constraintWidget4.v();
                if (v8 != -1) {
                    if (v8 == 0) {
                        u8 = constraintWidget2.f3672e.f3786e.f3753g * this.f3783b.u();
                        i8 = (int) (u8 + 0.5f);
                        this.f3786e.d(i8);
                    } else if (v8 != 1) {
                        i8 = 0;
                        this.f3786e.d(i8);
                    }
                }
                u8 = constraintWidget.f3672e.f3786e.f3753g / this.f3783b.u();
                i8 = (int) (u8 + 0.5f);
                this.f3786e.d(i8);
            }
        }
        d dVar = this.f3789h;
        if (dVar.f3749c) {
            d dVar2 = this.f3790i;
            if (dVar2.f3749c) {
                if (dVar.f3756j && dVar2.f3756j && this.f3786e.f3756j) {
                    return;
                }
                if (!this.f3786e.f3756j && this.f3785d == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    ConstraintWidget constraintWidget5 = this.f3783b;
                    if (constraintWidget5.f3686l == 0 && !constraintWidget5.Y()) {
                        int i11 = this.f3789h.f3758l.get(0).f3753g;
                        d dVar3 = this.f3789h;
                        int i12 = i11 + dVar3.f3752f;
                        int i13 = this.f3790i.f3758l.get(0).f3753g + this.f3790i.f3752f;
                        dVar3.d(i12);
                        this.f3790i.d(i13);
                        this.f3786e.d(i13 - i12);
                        return;
                    }
                }
                if (!this.f3786e.f3756j && this.f3785d == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && this.f3782a == 1 && this.f3789h.f3758l.size() > 0 && this.f3790i.f3758l.size() > 0) {
                    int i14 = (this.f3790i.f3758l.get(0).f3753g + this.f3790i.f3752f) - (this.f3789h.f3758l.get(0).f3753g + this.f3789h.f3752f);
                    e eVar3 = this.f3786e;
                    int i15 = eVar3.f3768m;
                    if (i14 < i15) {
                        eVar3.d(i14);
                    } else {
                        eVar3.d(i15);
                    }
                }
                if (this.f3786e.f3756j && this.f3789h.f3758l.size() > 0 && this.f3790i.f3758l.size() > 0) {
                    d dVar4 = this.f3789h.f3758l.get(0);
                    d dVar5 = this.f3790i.f3758l.get(0);
                    int i16 = dVar4.f3753g + this.f3789h.f3752f;
                    int i17 = dVar5.f3753g + this.f3790i.f3752f;
                    float L = this.f3783b.L();
                    if (dVar4 == dVar5) {
                        i16 = dVar4.f3753g;
                        i17 = dVar5.f3753g;
                        L = 0.5f;
                    }
                    this.f3789h.d((int) (i16 + 0.5f + (((i17 - i16) - this.f3786e.f3753g) * L)));
                    this.f3790i.d(this.f3789h.f3753g + this.f3786e.f3753g);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:110:0x02e3, code lost:
        if (r9.f3783b.T() != false) goto L102;
     */
    /* JADX WARN: Code restructure failed: missing block: B:111:0x02e5, code lost:
        r0 = r9.f3779k;
        r1 = r9.f3789h;
        r2 = r9.f3780l;
     */
    /* JADX WARN: Code restructure failed: missing block: B:125:0x033e, code lost:
        if (r0.f3785d == r1) goto L123;
     */
    /* JADX WARN: Code restructure failed: missing block: B:132:0x0370, code lost:
        if (r9.f3783b.T() != false) goto L102;
     */
    /* JADX WARN: Code restructure failed: missing block: B:151:0x03e7, code lost:
        if (r0.f3785d == r1) goto L123;
     */
    /* JADX WARN: Code restructure failed: missing block: B:152:0x03e9, code lost:
        r0.f3786e.f3757k.add(r9.f3786e);
        r9.f3786e.f3758l.add(r9.f3783b.f3672e.f3786e);
        r9.f3786e.f3747a = r9;
     */
    /* JADX WARN: Removed duplicated region for block: B:155:0x040d  */
    /* JADX WARN: Removed duplicated region for block: B:167:? A[RETURN, SYNTHETIC] */
    @Override // androidx.constraintlayout.solver.widgets.analyzer.k
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void d() {
        /*
            Method dump skipped, instructions count: 1042
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.analyzer.j.d():void");
    }

    @Override // androidx.constraintlayout.solver.widgets.analyzer.k
    public void e() {
        d dVar = this.f3789h;
        if (dVar.f3756j) {
            this.f3783b.H0(dVar.f3753g);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.constraintlayout.solver.widgets.analyzer.k
    public void f() {
        this.f3784c = null;
        this.f3789h.c();
        this.f3790i.c();
        this.f3779k.c();
        this.f3786e.c();
        this.f3788g = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.constraintlayout.solver.widgets.analyzer.k
    public boolean m() {
        return this.f3785d != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT || this.f3783b.f3688m == 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void q() {
        this.f3788g = false;
        this.f3789h.c();
        this.f3789h.f3756j = false;
        this.f3790i.c();
        this.f3790i.f3756j = false;
        this.f3779k.c();
        this.f3779k.f3756j = false;
        this.f3786e.f3756j = false;
    }

    public String toString() {
        return "VerticalRun " + this.f3783b.s();
    }
}
