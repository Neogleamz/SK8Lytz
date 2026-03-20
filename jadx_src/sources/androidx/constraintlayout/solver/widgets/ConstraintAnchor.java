package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.SolverVariable;
import java.util.HashSet;
import java.util.Iterator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ConstraintAnchor {

    /* renamed from: b  reason: collision with root package name */
    public final ConstraintWidget f3647b;

    /* renamed from: c  reason: collision with root package name */
    public final Type f3648c;

    /* renamed from: d  reason: collision with root package name */
    public ConstraintAnchor f3649d;

    /* renamed from: g  reason: collision with root package name */
    SolverVariable f3652g;

    /* renamed from: a  reason: collision with root package name */
    private HashSet<ConstraintAnchor> f3646a = null;

    /* renamed from: e  reason: collision with root package name */
    public int f3650e = 0;

    /* renamed from: f  reason: collision with root package name */
    int f3651f = -1;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public enum Type {
        NONE,
        LEFT,
        TOP,
        RIGHT,
        BOTTOM,
        BASELINE,
        CENTER,
        CENTER_X,
        CENTER_Y
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static /* synthetic */ class a {

        /* renamed from: a  reason: collision with root package name */
        static final /* synthetic */ int[] f3663a;

        static {
            int[] iArr = new int[Type.values().length];
            f3663a = iArr;
            try {
                iArr[Type.CENTER.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f3663a[Type.LEFT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f3663a[Type.RIGHT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f3663a[Type.TOP.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                f3663a[Type.BOTTOM.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                f3663a[Type.BASELINE.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                f3663a[Type.CENTER_X.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                f3663a[Type.CENTER_Y.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                f3663a[Type.NONE.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
        }
    }

    public ConstraintAnchor(ConstraintWidget constraintWidget, Type type) {
        this.f3647b = constraintWidget;
        this.f3648c = type;
    }

    public boolean a(ConstraintAnchor constraintAnchor, int i8) {
        return b(constraintAnchor, i8, -1, false);
    }

    public boolean b(ConstraintAnchor constraintAnchor, int i8, int i9, boolean z4) {
        if (constraintAnchor == null) {
            l();
            return true;
        } else if (z4 || k(constraintAnchor)) {
            this.f3649d = constraintAnchor;
            if (constraintAnchor.f3646a == null) {
                constraintAnchor.f3646a = new HashSet<>();
            }
            this.f3649d.f3646a.add(this);
            if (i8 > 0) {
                this.f3650e = i8;
            } else {
                this.f3650e = 0;
            }
            this.f3651f = i9;
            return true;
        } else {
            return false;
        }
    }

    public int c() {
        ConstraintAnchor constraintAnchor;
        if (this.f3647b.P() == 8) {
            return 0;
        }
        return (this.f3651f <= -1 || (constraintAnchor = this.f3649d) == null || constraintAnchor.f3647b.P() != 8) ? this.f3650e : this.f3651f;
    }

    public final ConstraintAnchor d() {
        switch (a.f3663a[this.f3648c.ordinal()]) {
            case 1:
            case 6:
            case 7:
            case 8:
            case 9:
                return null;
            case 2:
                return this.f3647b.F;
            case 3:
                return this.f3647b.D;
            case 4:
                return this.f3647b.G;
            case 5:
                return this.f3647b.E;
            default:
                throw new AssertionError(this.f3648c.name());
        }
    }

    public ConstraintWidget e() {
        return this.f3647b;
    }

    public SolverVariable f() {
        return this.f3652g;
    }

    public ConstraintAnchor g() {
        return this.f3649d;
    }

    public Type h() {
        return this.f3648c;
    }

    public boolean i() {
        HashSet<ConstraintAnchor> hashSet = this.f3646a;
        if (hashSet == null) {
            return false;
        }
        Iterator<ConstraintAnchor> it = hashSet.iterator();
        while (it.hasNext()) {
            if (it.next().d().j()) {
                return true;
            }
        }
        return false;
    }

    public boolean j() {
        return this.f3649d != null;
    }

    public boolean k(ConstraintAnchor constraintAnchor) {
        boolean z4 = false;
        if (constraintAnchor == null) {
            return false;
        }
        Type h8 = constraintAnchor.h();
        Type type = this.f3648c;
        if (h8 == type) {
            return type != Type.BASELINE || (constraintAnchor.e().T() && e().T());
        }
        switch (a.f3663a[type.ordinal()]) {
            case 1:
                return (h8 == Type.BASELINE || h8 == Type.CENTER_X || h8 == Type.CENTER_Y) ? false : true;
            case 2:
            case 3:
                boolean z8 = h8 == Type.LEFT || h8 == Type.RIGHT;
                if (constraintAnchor.e() instanceof f) {
                    if (z8 || h8 == Type.CENTER_X) {
                        z4 = true;
                    }
                    return z4;
                }
                return z8;
            case 4:
            case 5:
                boolean z9 = h8 == Type.TOP || h8 == Type.BOTTOM;
                if (constraintAnchor.e() instanceof f) {
                    if (z9 || h8 == Type.CENTER_Y) {
                        z4 = true;
                    }
                    return z4;
                }
                return z9;
            case 6:
            case 7:
            case 8:
            case 9:
                return false;
            default:
                throw new AssertionError(this.f3648c.name());
        }
    }

    public void l() {
        HashSet<ConstraintAnchor> hashSet;
        ConstraintAnchor constraintAnchor = this.f3649d;
        if (constraintAnchor != null && (hashSet = constraintAnchor.f3646a) != null) {
            hashSet.remove(this);
        }
        this.f3649d = null;
        this.f3650e = 0;
        this.f3651f = -1;
    }

    public void m(androidx.constraintlayout.solver.c cVar) {
        SolverVariable solverVariable = this.f3652g;
        if (solverVariable == null) {
            this.f3652g = new SolverVariable(SolverVariable.Type.UNRESTRICTED, null);
        } else {
            solverVariable.d();
        }
    }

    public void n(int i8) {
        if (j()) {
            this.f3651f = i8;
        }
    }

    public String toString() {
        return this.f3647b.s() + ":" + this.f3648c.toString();
    }
}
