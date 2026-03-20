package com.google.android.material.snackbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.view.c0;
import k7.f;
import k7.l;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class SnackbarContentLayout extends LinearLayout implements a {

    /* renamed from: a  reason: collision with root package name */
    private TextView f18457a;

    /* renamed from: b  reason: collision with root package name */
    private Button f18458b;

    /* renamed from: c  reason: collision with root package name */
    private int f18459c;

    /* renamed from: d  reason: collision with root package name */
    private int f18460d;

    public SnackbarContentLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, l.f21273b7);
        this.f18459c = obtainStyledAttributes.getDimensionPixelSize(l.f21283c7, -1);
        this.f18460d = obtainStyledAttributes.getDimensionPixelSize(l.f21346j7, -1);
        obtainStyledAttributes.recycle();
    }

    private static void d(View view, int i8, int i9) {
        if (c0.Y(view)) {
            c0.J0(view, c0.J(view), i8, c0.I(view), i9);
        } else {
            view.setPadding(view.getPaddingLeft(), i8, view.getPaddingRight(), i9);
        }
    }

    private boolean e(int i8, int i9, int i10) {
        boolean z4;
        if (i8 != getOrientation()) {
            setOrientation(i8);
            z4 = true;
        } else {
            z4 = false;
        }
        if (this.f18457a.getPaddingTop() == i9 && this.f18457a.getPaddingBottom() == i10) {
            return z4;
        }
        d(this.f18457a, i9, i10);
        return true;
    }

    @Override // com.google.android.material.snackbar.a
    public void a(int i8, int i9) {
        this.f18457a.setAlpha(0.0f);
        long j8 = i9;
        long j9 = i8;
        this.f18457a.animate().alpha(1.0f).setDuration(j8).setStartDelay(j9).start();
        if (this.f18458b.getVisibility() == 0) {
            this.f18458b.setAlpha(0.0f);
            this.f18458b.animate().alpha(1.0f).setDuration(j8).setStartDelay(j9).start();
        }
    }

    @Override // com.google.android.material.snackbar.a
    public void b(int i8, int i9) {
        this.f18457a.setAlpha(1.0f);
        long j8 = i9;
        long j9 = i8;
        this.f18457a.animate().alpha(0.0f).setDuration(j8).setStartDelay(j9).start();
        if (this.f18458b.getVisibility() == 0) {
            this.f18458b.setAlpha(1.0f);
            this.f18458b.animate().alpha(0.0f).setDuration(j8).setStartDelay(j9).start();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c(float f5) {
        if (f5 != 1.0f) {
            this.f18458b.setTextColor(n7.a.h(n7.a.d(this, k7.b.f21066s), this.f18458b.getCurrentTextColor(), f5));
        }
    }

    public Button getActionView() {
        return this.f18458b;
    }

    public TextView getMessageView() {
        return this.f18457a;
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.f18457a = (TextView) findViewById(f.T);
        this.f18458b = (Button) findViewById(f.S);
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x0053, code lost:
        if (e(1, r0, r0 - r1) != false) goto L21;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x005e, code lost:
        if (e(0, r0, r0) != false) goto L21;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0060, code lost:
        r3 = true;
     */
    @Override // android.widget.LinearLayout, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void onMeasure(int r8, int r9) {
        /*
            r7 = this;
            super.onMeasure(r8, r9)
            int r0 = r7.f18459c
            if (r0 <= 0) goto L18
            int r0 = r7.getMeasuredWidth()
            int r1 = r7.f18459c
            if (r0 <= r1) goto L18
            r8 = 1073741824(0x40000000, float:2.0)
            int r8 = android.view.View.MeasureSpec.makeMeasureSpec(r1, r8)
            super.onMeasure(r8, r9)
        L18:
            android.content.res.Resources r0 = r7.getResources()
            int r1 = k7.d.f21118o
            int r0 = r0.getDimensionPixelSize(r1)
            android.content.res.Resources r1 = r7.getResources()
            int r2 = k7.d.f21116n
            int r1 = r1.getDimensionPixelSize(r2)
            android.widget.TextView r2 = r7.f18457a
            android.text.Layout r2 = r2.getLayout()
            int r2 = r2.getLineCount()
            r3 = 0
            r4 = 1
            if (r2 <= r4) goto L3c
            r2 = r4
            goto L3d
        L3c:
            r2 = r3
        L3d:
            if (r2 == 0) goto L56
            int r5 = r7.f18460d
            if (r5 <= 0) goto L56
            android.widget.Button r5 = r7.f18458b
            int r5 = r5.getMeasuredWidth()
            int r6 = r7.f18460d
            if (r5 <= r6) goto L56
            int r1 = r0 - r1
            boolean r0 = r7.e(r4, r0, r1)
            if (r0 == 0) goto L61
            goto L60
        L56:
            if (r2 == 0) goto L59
            goto L5a
        L59:
            r0 = r1
        L5a:
            boolean r0 = r7.e(r3, r0, r0)
            if (r0 == 0) goto L61
        L60:
            r3 = r4
        L61:
            if (r3 == 0) goto L66
            super.onMeasure(r8, r9)
        L66:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.snackbar.SnackbarContentLayout.onMeasure(int, int):void");
    }

    public void setMaxInlineActionWidth(int i8) {
        this.f18460d = i8;
    }
}
