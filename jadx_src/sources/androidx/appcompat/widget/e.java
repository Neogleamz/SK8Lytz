package androidx.appcompat.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.widget.CheckedTextView;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class e {

    /* renamed from: a  reason: collision with root package name */
    private final CheckedTextView f1461a;

    /* renamed from: b  reason: collision with root package name */
    private ColorStateList f1462b = null;

    /* renamed from: c  reason: collision with root package name */
    private PorterDuff.Mode f1463c = null;

    /* renamed from: d  reason: collision with root package name */
    private boolean f1464d = false;

    /* renamed from: e  reason: collision with root package name */
    private boolean f1465e = false;

    /* renamed from: f  reason: collision with root package name */
    private boolean f1466f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public e(CheckedTextView checkedTextView) {
        this.f1461a = checkedTextView;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a() {
        Drawable a9 = androidx.core.widget.b.a(this.f1461a);
        if (a9 != null) {
            if (this.f1464d || this.f1465e) {
                Drawable mutate = androidx.core.graphics.drawable.a.r(a9).mutate();
                if (this.f1464d) {
                    androidx.core.graphics.drawable.a.o(mutate, this.f1462b);
                }
                if (this.f1465e) {
                    androidx.core.graphics.drawable.a.p(mutate, this.f1463c);
                }
                if (mutate.isStateful()) {
                    mutate.setState(this.f1461a.getDrawableState());
                }
                this.f1461a.setCheckMarkDrawable(mutate);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ColorStateList b() {
        return this.f1462b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PorterDuff.Mode c() {
        return this.f1463c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:11:0x003d A[Catch: all -> 0x0084, TRY_ENTER, TryCatch #1 {all -> 0x0084, blocks: (B:3:0x001d, B:5:0x0025, B:7:0x002b, B:11:0x003d, B:13:0x0045, B:15:0x004b, B:16:0x0058, B:18:0x0060, B:19:0x0069, B:21:0x0071), top: B:30:0x001d }] */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0060 A[Catch: all -> 0x0084, TryCatch #1 {all -> 0x0084, blocks: (B:3:0x001d, B:5:0x0025, B:7:0x002b, B:11:0x003d, B:13:0x0045, B:15:0x004b, B:16:0x0058, B:18:0x0060, B:19:0x0069, B:21:0x0071), top: B:30:0x001d }] */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0071 A[Catch: all -> 0x0084, TRY_LEAVE, TryCatch #1 {all -> 0x0084, blocks: (B:3:0x001d, B:5:0x0025, B:7:0x002b, B:11:0x003d, B:13:0x0045, B:15:0x004b, B:16:0x0058, B:18:0x0060, B:19:0x0069, B:21:0x0071), top: B:30:0x001d }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void d(android.util.AttributeSet r10, int r11) {
        /*
            r9 = this;
            android.widget.CheckedTextView r0 = r9.f1461a
            android.content.Context r0 = r0.getContext()
            int[] r3 = g.j.R0
            r8 = 0
            androidx.appcompat.widget.j0 r0 = androidx.appcompat.widget.j0.v(r0, r10, r3, r11, r8)
            android.widget.CheckedTextView r1 = r9.f1461a
            android.content.Context r2 = r1.getContext()
            android.content.res.TypedArray r5 = r0.r()
            r7 = 0
            r4 = r10
            r6 = r11
            androidx.core.view.c0.r0(r1, r2, r3, r4, r5, r6, r7)
            int r10 = g.j.T0     // Catch: java.lang.Throwable -> L84
            boolean r11 = r0.s(r10)     // Catch: java.lang.Throwable -> L84
            if (r11 == 0) goto L3a
            int r10 = r0.n(r10, r8)     // Catch: java.lang.Throwable -> L84
            if (r10 == 0) goto L3a
            android.widget.CheckedTextView r11 = r9.f1461a     // Catch: android.content.res.Resources.NotFoundException -> L3a java.lang.Throwable -> L84
            android.content.Context r1 = r11.getContext()     // Catch: android.content.res.Resources.NotFoundException -> L3a java.lang.Throwable -> L84
            android.graphics.drawable.Drawable r10 = h.a.b(r1, r10)     // Catch: android.content.res.Resources.NotFoundException -> L3a java.lang.Throwable -> L84
            r11.setCheckMarkDrawable(r10)     // Catch: android.content.res.Resources.NotFoundException -> L3a java.lang.Throwable -> L84
            r10 = 1
            goto L3b
        L3a:
            r10 = r8
        L3b:
            if (r10 != 0) goto L58
            int r10 = g.j.S0     // Catch: java.lang.Throwable -> L84
            boolean r11 = r0.s(r10)     // Catch: java.lang.Throwable -> L84
            if (r11 == 0) goto L58
            int r10 = r0.n(r10, r8)     // Catch: java.lang.Throwable -> L84
            if (r10 == 0) goto L58
            android.widget.CheckedTextView r11 = r9.f1461a     // Catch: java.lang.Throwable -> L84
            android.content.Context r1 = r11.getContext()     // Catch: java.lang.Throwable -> L84
            android.graphics.drawable.Drawable r10 = h.a.b(r1, r10)     // Catch: java.lang.Throwable -> L84
            r11.setCheckMarkDrawable(r10)     // Catch: java.lang.Throwable -> L84
        L58:
            int r10 = g.j.U0     // Catch: java.lang.Throwable -> L84
            boolean r11 = r0.s(r10)     // Catch: java.lang.Throwable -> L84
            if (r11 == 0) goto L69
            android.widget.CheckedTextView r11 = r9.f1461a     // Catch: java.lang.Throwable -> L84
            android.content.res.ColorStateList r10 = r0.c(r10)     // Catch: java.lang.Throwable -> L84
            androidx.core.widget.b.b(r11, r10)     // Catch: java.lang.Throwable -> L84
        L69:
            int r10 = g.j.V0     // Catch: java.lang.Throwable -> L84
            boolean r11 = r0.s(r10)     // Catch: java.lang.Throwable -> L84
            if (r11 == 0) goto L80
            android.widget.CheckedTextView r11 = r9.f1461a     // Catch: java.lang.Throwable -> L84
            r1 = -1
            int r10 = r0.k(r10, r1)     // Catch: java.lang.Throwable -> L84
            r1 = 0
            android.graphics.PorterDuff$Mode r10 = androidx.appcompat.widget.t.e(r10, r1)     // Catch: java.lang.Throwable -> L84
            androidx.core.widget.b.c(r11, r10)     // Catch: java.lang.Throwable -> L84
        L80:
            r0.w()
            return
        L84:
            r10 = move-exception
            r0.w()
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.e.d(android.util.AttributeSet, int):void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e() {
        if (this.f1466f) {
            this.f1466f = false;
            return;
        }
        this.f1466f = true;
        a();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void f(ColorStateList colorStateList) {
        this.f1462b = colorStateList;
        this.f1464d = true;
        a();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void g(PorterDuff.Mode mode) {
        this.f1463c = mode;
        this.f1465e = true;
        a();
    }
}
