package androidx.constraintlayout.solver.widgets.analyzer;

import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class k implements o0.a {

    /* renamed from: a  reason: collision with root package name */
    public int f3782a;

    /* renamed from: b  reason: collision with root package name */
    ConstraintWidget f3783b;

    /* renamed from: c  reason: collision with root package name */
    i f3784c;

    /* renamed from: d  reason: collision with root package name */
    protected ConstraintWidget.DimensionBehaviour f3785d;

    /* renamed from: e  reason: collision with root package name */
    e f3786e = new e(this);

    /* renamed from: f  reason: collision with root package name */
    public int f3787f = 0;

    /* renamed from: g  reason: collision with root package name */
    boolean f3788g = false;

    /* renamed from: h  reason: collision with root package name */
    public d f3789h = new d(this);

    /* renamed from: i  reason: collision with root package name */
    public d f3790i = new d(this);

    /* renamed from: j  reason: collision with root package name */
    protected b f3791j = b.NONE;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static /* synthetic */ class a {

        /* renamed from: a  reason: collision with root package name */
        static final /* synthetic */ int[] f3792a;

        static {
            int[] iArr = new int[ConstraintAnchor.Type.values().length];
            f3792a = iArr;
            try {
                iArr[ConstraintAnchor.Type.LEFT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f3792a[ConstraintAnchor.Type.RIGHT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f3792a[ConstraintAnchor.Type.TOP.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f3792a[ConstraintAnchor.Type.BASELINE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                f3792a[ConstraintAnchor.Type.BOTTOM.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    enum b {
        NONE,
        START,
        END,
        CENTER
    }

    public k(ConstraintWidget constraintWidget) {
        this.f3783b = constraintWidget;
    }

    private void l(int i8, int i9) {
        e eVar;
        int g8;
        int i10 = this.f3782a;
        if (i10 != 0) {
            if (i10 == 1) {
                int g9 = g(this.f3786e.f3768m, i8);
                eVar = this.f3786e;
                g8 = Math.min(g9, i9);
                eVar.d(g8);
            } else if (i10 != 2) {
                if (i10 != 3) {
                    return;
                }
                ConstraintWidget constraintWidget = this.f3783b;
                k kVar = constraintWidget.f3672e;
                ConstraintWidget.DimensionBehaviour dimensionBehaviour = kVar.f3785d;
                ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
                if (dimensionBehaviour == dimensionBehaviour2 && kVar.f3782a == 3) {
                    j jVar = constraintWidget.f3674f;
                    if (jVar.f3785d == dimensionBehaviour2 && jVar.f3782a == 3) {
                        return;
                    }
                }
                if (i8 == 0) {
                    kVar = constraintWidget.f3674f;
                }
                if (kVar.f3786e.f3756j) {
                    float u8 = constraintWidget.u();
                    this.f3786e.d(i8 == 1 ? (int) ((kVar.f3786e.f3753g / u8) + 0.5f) : (int) ((u8 * kVar.f3786e.f3753g) + 0.5f));
                    return;
                }
                return;
            } else {
                ConstraintWidget H = this.f3783b.H();
                if (H == null) {
                    return;
                }
                e eVar2 = (i8 == 0 ? H.f3672e : H.f3674f).f3786e;
                if (!eVar2.f3756j) {
                    return;
                }
                ConstraintWidget constraintWidget2 = this.f3783b;
                i9 = (int) ((eVar2.f3753g * (i8 == 0 ? constraintWidget2.q : constraintWidget2.f3701t)) + 0.5f);
            }
        }
        eVar = this.f3786e;
        g8 = g(i9, i8);
        eVar.d(g8);
    }

    @Override // o0.a
    public void a(o0.a aVar) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void b(d dVar, d dVar2, int i8) {
        dVar.f3758l.add(dVar2);
        dVar.f3752f = i8;
        dVar2.f3757k.add(dVar);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void c(d dVar, d dVar2, int i8, e eVar) {
        dVar.f3758l.add(dVar2);
        dVar.f3758l.add(this.f3786e);
        dVar.f3754h = i8;
        dVar.f3755i = eVar;
        dVar2.f3757k.add(dVar);
        eVar.f3757k.add(dVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void d();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void e();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void f();

    /* JADX INFO: Access modifiers changed from: protected */
    public final int g(int i8, int i9) {
        int max;
        if (i9 == 0) {
            ConstraintWidget constraintWidget = this.f3783b;
            int i10 = constraintWidget.f3694p;
            max = Math.max(constraintWidget.f3692o, i8);
            if (i10 > 0) {
                max = Math.min(i10, i8);
            }
            if (max == i8) {
                return i8;
            }
        } else {
            ConstraintWidget constraintWidget2 = this.f3783b;
            int i11 = constraintWidget2.f3699s;
            max = Math.max(constraintWidget2.f3697r, i8);
            if (i11 > 0) {
                max = Math.min(i11, i8);
            }
            if (max == i8) {
                return i8;
            }
        }
        return max;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final d h(ConstraintAnchor constraintAnchor) {
        k kVar;
        k kVar2;
        ConstraintAnchor constraintAnchor2 = constraintAnchor.f3649d;
        if (constraintAnchor2 == null) {
            return null;
        }
        ConstraintWidget constraintWidget = constraintAnchor2.f3647b;
        int i8 = a.f3792a[constraintAnchor2.f3648c.ordinal()];
        if (i8 != 1) {
            if (i8 == 2) {
                kVar2 = constraintWidget.f3672e;
            } else if (i8 == 3) {
                kVar = constraintWidget.f3674f;
            } else if (i8 == 4) {
                return constraintWidget.f3674f.f3779k;
            } else {
                if (i8 != 5) {
                    return null;
                }
                kVar2 = constraintWidget.f3674f;
            }
            return kVar2.f3790i;
        }
        kVar = constraintWidget.f3672e;
        return kVar.f3789h;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final d i(ConstraintAnchor constraintAnchor, int i8) {
        ConstraintAnchor constraintAnchor2 = constraintAnchor.f3649d;
        if (constraintAnchor2 == null) {
            return null;
        }
        ConstraintWidget constraintWidget = constraintAnchor2.f3647b;
        k kVar = i8 == 0 ? constraintWidget.f3672e : constraintWidget.f3674f;
        int i9 = a.f3792a[constraintAnchor2.f3648c.ordinal()];
        if (i9 != 1) {
            if (i9 != 2) {
                if (i9 != 3) {
                    if (i9 != 5) {
                        return null;
                    }
                }
            }
            return kVar.f3790i;
        }
        return kVar.f3789h;
    }

    public long j() {
        e eVar = this.f3786e;
        if (eVar.f3756j) {
            return eVar.f3753g;
        }
        return 0L;
    }

    public boolean k() {
        return this.f3788g;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract boolean m();

    /* JADX INFO: Access modifiers changed from: protected */
    public void n(o0.a aVar, ConstraintAnchor constraintAnchor, ConstraintAnchor constraintAnchor2, int i8) {
        d dVar;
        d h8 = h(constraintAnchor);
        d h9 = h(constraintAnchor2);
        if (h8.f3756j && h9.f3756j) {
            int c9 = h8.f3753g + constraintAnchor.c();
            int c10 = h9.f3753g - constraintAnchor2.c();
            int i9 = c10 - c9;
            if (!this.f3786e.f3756j && this.f3785d == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                l(i8, i9);
            }
            e eVar = this.f3786e;
            if (eVar.f3756j) {
                if (eVar.f3753g == i9) {
                    this.f3789h.d(c9);
                    dVar = this.f3790i;
                } else {
                    ConstraintWidget constraintWidget = this.f3783b;
                    float x8 = i8 == 0 ? constraintWidget.x() : constraintWidget.L();
                    if (h8 == h9) {
                        c9 = h8.f3753g;
                        c10 = h9.f3753g;
                        x8 = 0.5f;
                    }
                    this.f3789h.d((int) (c9 + 0.5f + (((c10 - c9) - this.f3786e.f3753g) * x8)));
                    dVar = this.f3790i;
                    c10 = this.f3789h.f3753g + this.f3786e.f3753g;
                }
                dVar.d(c10);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void o(o0.a aVar) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void p(o0.a aVar) {
    }
}
