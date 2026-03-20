package androidx.recyclerview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class i extends RecyclerView.n {

    /* renamed from: d  reason: collision with root package name */
    private static final int[] f6901d = {16843284};

    /* renamed from: a  reason: collision with root package name */
    private Drawable f6902a;

    /* renamed from: b  reason: collision with root package name */
    private int f6903b;

    /* renamed from: c  reason: collision with root package name */
    private final Rect f6904c = new Rect();

    public i(Context context, int i8) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(f6901d);
        Drawable drawable = obtainStyledAttributes.getDrawable(0);
        this.f6902a = drawable;
        if (drawable == null) {
            Log.w("DividerItem", "@android:attr/listDivider was not set in the theme used for this DividerItemDecoration. Please set that attribute all call setDrawable()");
        }
        obtainStyledAttributes.recycle();
        n(i8);
    }

    private void l(Canvas canvas, RecyclerView recyclerView) {
        int height;
        int i8;
        canvas.save();
        if (recyclerView.getClipToPadding()) {
            i8 = recyclerView.getPaddingTop();
            height = recyclerView.getHeight() - recyclerView.getPaddingBottom();
            canvas.clipRect(recyclerView.getPaddingLeft(), i8, recyclerView.getWidth() - recyclerView.getPaddingRight(), height);
        } else {
            height = recyclerView.getHeight();
            i8 = 0;
        }
        int childCount = recyclerView.getChildCount();
        for (int i9 = 0; i9 < childCount; i9++) {
            View childAt = recyclerView.getChildAt(i9);
            recyclerView.getLayoutManager().Q(childAt, this.f6904c);
            int round = this.f6904c.right + Math.round(childAt.getTranslationX());
            this.f6902a.setBounds(round - this.f6902a.getIntrinsicWidth(), i8, round, height);
            this.f6902a.draw(canvas);
        }
        canvas.restore();
    }

    private void m(Canvas canvas, RecyclerView recyclerView) {
        int width;
        int i8;
        canvas.save();
        if (recyclerView.getClipToPadding()) {
            i8 = recyclerView.getPaddingLeft();
            width = recyclerView.getWidth() - recyclerView.getPaddingRight();
            canvas.clipRect(i8, recyclerView.getPaddingTop(), width, recyclerView.getHeight() - recyclerView.getPaddingBottom());
        } else {
            width = recyclerView.getWidth();
            i8 = 0;
        }
        int childCount = recyclerView.getChildCount();
        for (int i9 = 0; i9 < childCount; i9++) {
            View childAt = recyclerView.getChildAt(i9);
            recyclerView.j0(childAt, this.f6904c);
            int round = this.f6904c.bottom + Math.round(childAt.getTranslationY());
            this.f6902a.setBounds(i8, round - this.f6902a.getIntrinsicHeight(), width, round);
            this.f6902a.draw(canvas);
        }
        canvas.restore();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.n
    public void g(Rect rect, View view, RecyclerView recyclerView, RecyclerView.y yVar) {
        Drawable drawable = this.f6902a;
        if (drawable == null) {
            rect.set(0, 0, 0, 0);
        } else if (this.f6903b == 1) {
            rect.set(0, 0, 0, drawable.getIntrinsicHeight());
        } else {
            rect.set(0, 0, drawable.getIntrinsicWidth(), 0);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.n
    public void i(Canvas canvas, RecyclerView recyclerView, RecyclerView.y yVar) {
        if (recyclerView.getLayoutManager() == null || this.f6902a == null) {
            return;
        }
        if (this.f6903b == 1) {
            m(canvas, recyclerView);
        } else {
            l(canvas, recyclerView);
        }
    }

    public void n(int i8) {
        if (i8 != 0 && i8 != 1) {
            throw new IllegalArgumentException("Invalid orientation. It should be either HORIZONTAL or VERTICAL");
        }
        this.f6903b = i8;
    }
}
