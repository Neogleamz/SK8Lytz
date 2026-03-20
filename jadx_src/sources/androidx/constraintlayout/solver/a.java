package androidx.constraintlayout.solver;

import androidx.constraintlayout.solver.b;
import com.daimajia.numberprogressbar.BuildConfig;
import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a implements b.a {

    /* renamed from: l  reason: collision with root package name */
    private static float f3538l = 0.001f;

    /* renamed from: b  reason: collision with root package name */
    private final b f3540b;

    /* renamed from: c  reason: collision with root package name */
    protected final c f3541c;

    /* renamed from: a  reason: collision with root package name */
    int f3539a = 0;

    /* renamed from: d  reason: collision with root package name */
    private int f3542d = 8;

    /* renamed from: e  reason: collision with root package name */
    private SolverVariable f3543e = null;

    /* renamed from: f  reason: collision with root package name */
    private int[] f3544f = new int[8];

    /* renamed from: g  reason: collision with root package name */
    private int[] f3545g = new int[8];

    /* renamed from: h  reason: collision with root package name */
    private float[] f3546h = new float[8];

    /* renamed from: i  reason: collision with root package name */
    private int f3547i = -1;

    /* renamed from: j  reason: collision with root package name */
    private int f3548j = -1;

    /* renamed from: k  reason: collision with root package name */
    private boolean f3549k = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    public a(b bVar, c cVar) {
        this.f3540b = bVar;
        this.f3541c = cVar;
    }

    @Override // androidx.constraintlayout.solver.b.a
    public int a() {
        return this.f3539a;
    }

    @Override // androidx.constraintlayout.solver.b.a
    public SolverVariable b(int i8) {
        int i9 = this.f3547i;
        for (int i10 = 0; i9 != -1 && i10 < this.f3539a; i10++) {
            if (i10 == i8) {
                return this.f3541c.f3559d[this.f3544f[i9]];
            }
            i9 = this.f3545g[i9];
        }
        return null;
    }

    @Override // androidx.constraintlayout.solver.b.a
    public float c(int i8) {
        int i9 = this.f3547i;
        for (int i10 = 0; i9 != -1 && i10 < this.f3539a; i10++) {
            if (i10 == i8) {
                return this.f3546h[i9];
            }
            i9 = this.f3545g[i9];
        }
        return 0.0f;
    }

    @Override // androidx.constraintlayout.solver.b.a
    public final void clear() {
        int i8 = this.f3547i;
        for (int i9 = 0; i8 != -1 && i9 < this.f3539a; i9++) {
            SolverVariable solverVariable = this.f3541c.f3559d[this.f3544f[i8]];
            if (solverVariable != null) {
                solverVariable.c(this.f3540b);
            }
            i8 = this.f3545g[i8];
        }
        this.f3547i = -1;
        this.f3548j = -1;
        this.f3549k = false;
        this.f3539a = 0;
    }

    @Override // androidx.constraintlayout.solver.b.a
    public void d(SolverVariable solverVariable, float f5, boolean z4) {
        float f8 = f3538l;
        if (f5 <= (-f8) || f5 >= f8) {
            int i8 = this.f3547i;
            if (i8 == -1) {
                this.f3547i = 0;
                this.f3546h[0] = f5;
                this.f3544f[0] = solverVariable.f3520c;
                this.f3545g[0] = -1;
                solverVariable.f3530m++;
                solverVariable.a(this.f3540b);
                this.f3539a++;
                if (this.f3549k) {
                    return;
                }
                int i9 = this.f3548j + 1;
                this.f3548j = i9;
                int[] iArr = this.f3544f;
                if (i9 >= iArr.length) {
                    this.f3549k = true;
                    this.f3548j = iArr.length - 1;
                    return;
                }
                return;
            }
            int i10 = -1;
            for (int i11 = 0; i8 != -1 && i11 < this.f3539a; i11++) {
                int[] iArr2 = this.f3544f;
                int i12 = iArr2[i8];
                int i13 = solverVariable.f3520c;
                if (i12 == i13) {
                    float[] fArr = this.f3546h;
                    float f9 = fArr[i8] + f5;
                    float f10 = f3538l;
                    if (f9 > (-f10) && f9 < f10) {
                        f9 = 0.0f;
                    }
                    fArr[i8] = f9;
                    if (f9 == 0.0f) {
                        if (i8 == this.f3547i) {
                            this.f3547i = this.f3545g[i8];
                        } else {
                            int[] iArr3 = this.f3545g;
                            iArr3[i10] = iArr3[i8];
                        }
                        if (z4) {
                            solverVariable.c(this.f3540b);
                        }
                        if (this.f3549k) {
                            this.f3548j = i8;
                        }
                        solverVariable.f3530m--;
                        this.f3539a--;
                        return;
                    }
                    return;
                }
                if (iArr2[i8] < i13) {
                    i10 = i8;
                }
                i8 = this.f3545g[i8];
            }
            int i14 = this.f3548j;
            int i15 = i14 + 1;
            if (this.f3549k) {
                int[] iArr4 = this.f3544f;
                if (iArr4[i14] != -1) {
                    i14 = iArr4.length;
                }
            } else {
                i14 = i15;
            }
            int[] iArr5 = this.f3544f;
            if (i14 >= iArr5.length && this.f3539a < iArr5.length) {
                int i16 = 0;
                while (true) {
                    int[] iArr6 = this.f3544f;
                    if (i16 >= iArr6.length) {
                        break;
                    } else if (iArr6[i16] == -1) {
                        i14 = i16;
                        break;
                    } else {
                        i16++;
                    }
                }
            }
            int[] iArr7 = this.f3544f;
            if (i14 >= iArr7.length) {
                i14 = iArr7.length;
                int i17 = this.f3542d * 2;
                this.f3542d = i17;
                this.f3549k = false;
                this.f3548j = i14 - 1;
                this.f3546h = Arrays.copyOf(this.f3546h, i17);
                this.f3544f = Arrays.copyOf(this.f3544f, this.f3542d);
                this.f3545g = Arrays.copyOf(this.f3545g, this.f3542d);
            }
            this.f3544f[i14] = solverVariable.f3520c;
            this.f3546h[i14] = f5;
            int[] iArr8 = this.f3545g;
            if (i10 != -1) {
                iArr8[i14] = iArr8[i10];
                iArr8[i10] = i14;
            } else {
                iArr8[i14] = this.f3547i;
                this.f3547i = i14;
            }
            solverVariable.f3530m++;
            solverVariable.a(this.f3540b);
            this.f3539a++;
            if (!this.f3549k) {
                this.f3548j++;
            }
            int i18 = this.f3548j;
            int[] iArr9 = this.f3544f;
            if (i18 >= iArr9.length) {
                this.f3549k = true;
                this.f3548j = iArr9.length - 1;
            }
        }
    }

    @Override // androidx.constraintlayout.solver.b.a
    public final float e(SolverVariable solverVariable) {
        int i8 = this.f3547i;
        for (int i9 = 0; i8 != -1 && i9 < this.f3539a; i9++) {
            if (this.f3544f[i8] == solverVariable.f3520c) {
                return this.f3546h[i8];
            }
            i8 = this.f3545g[i8];
        }
        return 0.0f;
    }

    @Override // androidx.constraintlayout.solver.b.a
    public boolean f(SolverVariable solverVariable) {
        int i8 = this.f3547i;
        if (i8 == -1) {
            return false;
        }
        for (int i9 = 0; i8 != -1 && i9 < this.f3539a; i9++) {
            if (this.f3544f[i8] == solverVariable.f3520c) {
                return true;
            }
            i8 = this.f3545g[i8];
        }
        return false;
    }

    @Override // androidx.constraintlayout.solver.b.a
    public float g(b bVar, boolean z4) {
        float e8 = e(bVar.f3550a);
        i(bVar.f3550a, z4);
        b.a aVar = bVar.f3554e;
        int a9 = aVar.a();
        for (int i8 = 0; i8 < a9; i8++) {
            SolverVariable b9 = aVar.b(i8);
            d(b9, aVar.e(b9) * e8, z4);
        }
        return e8;
    }

    @Override // androidx.constraintlayout.solver.b.a
    public final void h(SolverVariable solverVariable, float f5) {
        if (f5 == 0.0f) {
            i(solverVariable, true);
            return;
        }
        int i8 = this.f3547i;
        if (i8 == -1) {
            this.f3547i = 0;
            this.f3546h[0] = f5;
            this.f3544f[0] = solverVariable.f3520c;
            this.f3545g[0] = -1;
            solverVariable.f3530m++;
            solverVariable.a(this.f3540b);
            this.f3539a++;
            if (this.f3549k) {
                return;
            }
            int i9 = this.f3548j + 1;
            this.f3548j = i9;
            int[] iArr = this.f3544f;
            if (i9 >= iArr.length) {
                this.f3549k = true;
                this.f3548j = iArr.length - 1;
                return;
            }
            return;
        }
        int i10 = -1;
        for (int i11 = 0; i8 != -1 && i11 < this.f3539a; i11++) {
            int[] iArr2 = this.f3544f;
            int i12 = iArr2[i8];
            int i13 = solverVariable.f3520c;
            if (i12 == i13) {
                this.f3546h[i8] = f5;
                return;
            }
            if (iArr2[i8] < i13) {
                i10 = i8;
            }
            i8 = this.f3545g[i8];
        }
        int i14 = this.f3548j;
        int i15 = i14 + 1;
        if (this.f3549k) {
            int[] iArr3 = this.f3544f;
            if (iArr3[i14] != -1) {
                i14 = iArr3.length;
            }
        } else {
            i14 = i15;
        }
        int[] iArr4 = this.f3544f;
        if (i14 >= iArr4.length && this.f3539a < iArr4.length) {
            int i16 = 0;
            while (true) {
                int[] iArr5 = this.f3544f;
                if (i16 >= iArr5.length) {
                    break;
                } else if (iArr5[i16] == -1) {
                    i14 = i16;
                    break;
                } else {
                    i16++;
                }
            }
        }
        int[] iArr6 = this.f3544f;
        if (i14 >= iArr6.length) {
            i14 = iArr6.length;
            int i17 = this.f3542d * 2;
            this.f3542d = i17;
            this.f3549k = false;
            this.f3548j = i14 - 1;
            this.f3546h = Arrays.copyOf(this.f3546h, i17);
            this.f3544f = Arrays.copyOf(this.f3544f, this.f3542d);
            this.f3545g = Arrays.copyOf(this.f3545g, this.f3542d);
        }
        this.f3544f[i14] = solverVariable.f3520c;
        this.f3546h[i14] = f5;
        int[] iArr7 = this.f3545g;
        if (i10 != -1) {
            iArr7[i14] = iArr7[i10];
            iArr7[i10] = i14;
        } else {
            iArr7[i14] = this.f3547i;
            this.f3547i = i14;
        }
        solverVariable.f3530m++;
        solverVariable.a(this.f3540b);
        int i18 = this.f3539a + 1;
        this.f3539a = i18;
        if (!this.f3549k) {
            this.f3548j++;
        }
        int[] iArr8 = this.f3544f;
        if (i18 >= iArr8.length) {
            this.f3549k = true;
        }
        if (this.f3548j >= iArr8.length) {
            this.f3549k = true;
            this.f3548j = iArr8.length - 1;
        }
    }

    @Override // androidx.constraintlayout.solver.b.a
    public final float i(SolverVariable solverVariable, boolean z4) {
        if (this.f3543e == solverVariable) {
            this.f3543e = null;
        }
        int i8 = this.f3547i;
        if (i8 == -1) {
            return 0.0f;
        }
        int i9 = 0;
        int i10 = -1;
        while (i8 != -1 && i9 < this.f3539a) {
            if (this.f3544f[i8] == solverVariable.f3520c) {
                if (i8 == this.f3547i) {
                    this.f3547i = this.f3545g[i8];
                } else {
                    int[] iArr = this.f3545g;
                    iArr[i10] = iArr[i8];
                }
                if (z4) {
                    solverVariable.c(this.f3540b);
                }
                solverVariable.f3530m--;
                this.f3539a--;
                this.f3544f[i8] = -1;
                if (this.f3549k) {
                    this.f3548j = i8;
                }
                return this.f3546h[i8];
            }
            i9++;
            i10 = i8;
            i8 = this.f3545g[i8];
        }
        return 0.0f;
    }

    @Override // androidx.constraintlayout.solver.b.a
    public void invert() {
        int i8 = this.f3547i;
        for (int i9 = 0; i8 != -1 && i9 < this.f3539a; i9++) {
            float[] fArr = this.f3546h;
            fArr[i8] = fArr[i8] * (-1.0f);
            i8 = this.f3545g[i8];
        }
    }

    @Override // androidx.constraintlayout.solver.b.a
    public void j(float f5) {
        int i8 = this.f3547i;
        for (int i9 = 0; i8 != -1 && i9 < this.f3539a; i9++) {
            float[] fArr = this.f3546h;
            fArr[i8] = fArr[i8] / f5;
            i8 = this.f3545g[i8];
        }
    }

    public String toString() {
        int i8 = this.f3547i;
        String str = BuildConfig.FLAVOR;
        for (int i9 = 0; i8 != -1 && i9 < this.f3539a; i9++) {
            str = ((str + " -> ") + this.f3546h[i8] + " : ") + this.f3541c.f3559d[this.f3544f[i8]];
            i8 = this.f3545g[i8];
        }
        return str;
    }
}
