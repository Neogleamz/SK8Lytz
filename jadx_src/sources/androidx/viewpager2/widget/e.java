package androidx.viewpager2.widget;

import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import java.util.Locale;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e extends RecyclerView.s {

    /* renamed from: a  reason: collision with root package name */
    private ViewPager2.i f7885a;

    /* renamed from: b  reason: collision with root package name */
    private final ViewPager2 f7886b;

    /* renamed from: c  reason: collision with root package name */
    private final RecyclerView f7887c;

    /* renamed from: d  reason: collision with root package name */
    private final LinearLayoutManager f7888d;

    /* renamed from: e  reason: collision with root package name */
    private int f7889e;

    /* renamed from: f  reason: collision with root package name */
    private int f7890f;

    /* renamed from: g  reason: collision with root package name */
    private a f7891g;

    /* renamed from: h  reason: collision with root package name */
    private int f7892h;

    /* renamed from: i  reason: collision with root package name */
    private int f7893i;

    /* renamed from: j  reason: collision with root package name */
    private boolean f7894j;

    /* renamed from: k  reason: collision with root package name */
    private boolean f7895k;

    /* renamed from: l  reason: collision with root package name */
    private boolean f7896l;

    /* renamed from: m  reason: collision with root package name */
    private boolean f7897m;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        int f7898a;

        /* renamed from: b  reason: collision with root package name */
        float f7899b;

        /* renamed from: c  reason: collision with root package name */
        int f7900c;

        a() {
        }

        void a() {
            this.f7898a = -1;
            this.f7899b = 0.0f;
            this.f7900c = 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public e(ViewPager2 viewPager2) {
        this.f7886b = viewPager2;
        RecyclerView recyclerView = viewPager2.f7847k;
        this.f7887c = recyclerView;
        this.f7888d = (LinearLayoutManager) recyclerView.getLayoutManager();
        this.f7891g = new a();
        n();
    }

    private void c(int i8, float f5, int i9) {
        ViewPager2.i iVar = this.f7885a;
        if (iVar != null) {
            iVar.b(i8, f5, i9);
        }
    }

    private void d(int i8) {
        ViewPager2.i iVar = this.f7885a;
        if (iVar != null) {
            iVar.c(i8);
        }
    }

    private void e(int i8) {
        if ((this.f7889e == 3 && this.f7890f == 0) || this.f7890f == i8) {
            return;
        }
        this.f7890f = i8;
        ViewPager2.i iVar = this.f7885a;
        if (iVar != null) {
            iVar.a(i8);
        }
    }

    private int f() {
        return this.f7888d.a2();
    }

    private boolean k() {
        int i8 = this.f7889e;
        return i8 == 1 || i8 == 4;
    }

    private void n() {
        this.f7889e = 0;
        this.f7890f = 0;
        this.f7891g.a();
        this.f7892h = -1;
        this.f7893i = -1;
        this.f7894j = false;
        this.f7895k = false;
        this.f7897m = false;
        this.f7896l = false;
    }

    private void p(boolean z4) {
        this.f7897m = z4;
        this.f7889e = z4 ? 4 : 1;
        int i8 = this.f7893i;
        if (i8 != -1) {
            this.f7892h = i8;
            this.f7893i = -1;
        } else if (this.f7892h == -1) {
            this.f7892h = f();
        }
        e(1);
    }

    private void q() {
        int top;
        a aVar = this.f7891g;
        int a22 = this.f7888d.a2();
        aVar.f7898a = a22;
        if (a22 == -1) {
            aVar.a();
            return;
        }
        View D = this.f7888d.D(a22);
        if (D == null) {
            aVar.a();
            return;
        }
        int b02 = this.f7888d.b0(D);
        int k02 = this.f7888d.k0(D);
        int n02 = this.f7888d.n0(D);
        int I = this.f7888d.I(D);
        ViewGroup.LayoutParams layoutParams = D.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
            b02 += marginLayoutParams.leftMargin;
            k02 += marginLayoutParams.rightMargin;
            n02 += marginLayoutParams.topMargin;
            I += marginLayoutParams.bottomMargin;
        }
        int height = D.getHeight() + n02 + I;
        int width = D.getWidth() + b02 + k02;
        if (this.f7888d.q2() == 0) {
            top = (D.getLeft() - b02) - this.f7887c.getPaddingLeft();
            if (this.f7886b.d()) {
                top = -top;
            }
            height = width;
        } else {
            top = (D.getTop() - n02) - this.f7887c.getPaddingTop();
        }
        int i8 = -top;
        aVar.f7900c = i8;
        if (i8 >= 0) {
            aVar.f7899b = height == 0 ? 0.0f : i8 / height;
        } else if (!new androidx.viewpager2.widget.a(this.f7888d).d()) {
            throw new IllegalStateException(String.format(Locale.US, "Page can only be offset by a positive amount, not by %d", Integer.valueOf(aVar.f7900c)));
        } else {
            throw new IllegalStateException("Page(s) contain a ViewGroup with a LayoutTransition (or animateLayoutChanges=\"true\"), which interferes with the scrolling animation. Make sure to call getLayoutTransition().setAnimateParentHierarchy(false) on all ViewGroups with a LayoutTransition before an animation is started.");
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.s
    public void a(RecyclerView recyclerView, int i8) {
        boolean z4 = true;
        if (!(this.f7889e == 1 && this.f7890f == 1) && i8 == 1) {
            p(false);
        } else if (k() && i8 == 2) {
            if (this.f7895k) {
                e(2);
                this.f7894j = true;
            }
        } else {
            if (k() && i8 == 0) {
                q();
                if (this.f7895k) {
                    a aVar = this.f7891g;
                    if (aVar.f7900c == 0) {
                        int i9 = this.f7892h;
                        int i10 = aVar.f7898a;
                        if (i9 != i10) {
                            d(i10);
                        }
                    } else {
                        z4 = false;
                    }
                } else {
                    int i11 = this.f7891g.f7898a;
                    if (i11 != -1) {
                        c(i11, 0.0f, 0);
                    }
                }
                if (z4) {
                    e(0);
                    n();
                }
            }
            if (this.f7889e == 2 && i8 == 0 && this.f7896l) {
                q();
                a aVar2 = this.f7891g;
                if (aVar2.f7900c == 0) {
                    int i12 = this.f7893i;
                    int i13 = aVar2.f7898a;
                    if (i12 != i13) {
                        if (i13 == -1) {
                            i13 = 0;
                        }
                        d(i13);
                    }
                    e(0);
                    n();
                }
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x001d, code lost:
        if ((r5 < 0) == r3.f7886b.d()) goto L36;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0037, code lost:
        if (r3.f7892h != r5) goto L34;
     */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0025  */
    @Override // androidx.recyclerview.widget.RecyclerView.s
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void b(androidx.recyclerview.widget.RecyclerView r4, int r5, int r6) {
        /*
            r3 = this;
            r4 = 1
            r3.f7895k = r4
            r3.q()
            boolean r0 = r3.f7894j
            r1 = -1
            r2 = 0
            if (r0 == 0) goto L3a
            r3.f7894j = r2
            if (r6 > 0) goto L22
            if (r6 != 0) goto L20
            if (r5 >= 0) goto L16
            r5 = r4
            goto L17
        L16:
            r5 = r2
        L17:
            androidx.viewpager2.widget.ViewPager2 r6 = r3.f7886b
            boolean r6 = r6.d()
            if (r5 != r6) goto L20
            goto L22
        L20:
            r5 = r2
            goto L23
        L22:
            r5 = r4
        L23:
            if (r5 == 0) goto L2f
            androidx.viewpager2.widget.e$a r5 = r3.f7891g
            int r6 = r5.f7900c
            if (r6 == 0) goto L2f
            int r5 = r5.f7898a
            int r5 = r5 + r4
            goto L33
        L2f:
            androidx.viewpager2.widget.e$a r5 = r3.f7891g
            int r5 = r5.f7898a
        L33:
            r3.f7893i = r5
            int r6 = r3.f7892h
            if (r6 == r5) goto L48
            goto L45
        L3a:
            int r5 = r3.f7889e
            if (r5 != 0) goto L48
            androidx.viewpager2.widget.e$a r5 = r3.f7891g
            int r5 = r5.f7898a
            if (r5 != r1) goto L45
            r5 = r2
        L45:
            r3.d(r5)
        L48:
            androidx.viewpager2.widget.e$a r5 = r3.f7891g
            int r6 = r5.f7898a
            if (r6 != r1) goto L4f
            r6 = r2
        L4f:
            float r0 = r5.f7899b
            int r5 = r5.f7900c
            r3.c(r6, r0, r5)
            androidx.viewpager2.widget.e$a r5 = r3.f7891g
            int r6 = r5.f7898a
            int r0 = r3.f7893i
            if (r6 == r0) goto L60
            if (r0 != r1) goto L6e
        L60:
            int r5 = r5.f7900c
            if (r5 != 0) goto L6e
            int r5 = r3.f7890f
            if (r5 == r4) goto L6e
            r3.e(r2)
            r3.n()
        L6e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.viewpager2.widget.e.b(androidx.recyclerview.widget.RecyclerView, int, int):void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public double g() {
        q();
        a aVar = this.f7891g;
        return aVar.f7898a + aVar.f7899b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int h() {
        return this.f7890f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean i() {
        return this.f7897m;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean j() {
        return this.f7890f == 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void l() {
        this.f7896l = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void m(int i8, boolean z4) {
        this.f7889e = z4 ? 2 : 3;
        this.f7897m = false;
        boolean z8 = this.f7893i != i8;
        this.f7893i = i8;
        e(2);
        if (z8) {
            d(i8);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void o(ViewPager2.i iVar) {
        this.f7885a = iVar;
    }
}
