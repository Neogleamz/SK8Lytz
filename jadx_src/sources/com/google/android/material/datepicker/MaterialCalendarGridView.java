package com.google.android.material.datepicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ListAdapter;
import androidx.core.view.c0;
import com.google.android.material.internal.s;
import java.util.Calendar;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class MaterialCalendarGridView extends GridView {

    /* renamed from: a  reason: collision with root package name */
    private final Calendar f17776a;

    /* renamed from: b  reason: collision with root package name */
    private final boolean f17777b;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends androidx.core.view.a {
        a() {
        }

        @Override // androidx.core.view.a
        public void g(View view, androidx.core.view.accessibility.c cVar) {
            super.g(view, cVar);
            cVar.e0(null);
        }
    }

    public MaterialCalendarGridView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public MaterialCalendarGridView(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        this.f17776a = p.q();
        if (g.o2(getContext())) {
            setNextFocusLeftId(k7.f.f21149a);
            setNextFocusRightId(k7.f.f21153c);
        }
        this.f17777b = g.p2(getContext());
        c0.t0(this, new a());
    }

    private void a(int i8, Rect rect) {
        int b9;
        if (i8 == 33) {
            b9 = getAdapter2().i();
        } else if (i8 != 130) {
            super.onFocusChanged(true, i8, rect);
            return;
        } else {
            b9 = getAdapter2().b();
        }
        setSelection(b9);
    }

    private static int c(View view) {
        return view.getLeft() + (view.getWidth() / 2);
    }

    private static boolean d(Long l8, Long l9, Long l10, Long l11) {
        return l8 == null || l9 == null || l10 == null || l11 == null || l10.longValue() > l9.longValue() || l11.longValue() < l8.longValue();
    }

    @Override // android.widget.GridView, android.widget.AdapterView
    /* renamed from: b */
    public j getAdapter2() {
        return (j) super.getAdapter();
    }

    @Override // android.widget.AbsListView, android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getAdapter2().notifyDataSetChanged();
    }

    @Override // android.view.View
    protected final void onDraw(Canvas canvas) {
        int a9;
        int c9;
        int a10;
        int c10;
        int width;
        int i8;
        MaterialCalendarGridView materialCalendarGridView = this;
        super.onDraw(canvas);
        j adapter2 = getAdapter2();
        DateSelector<?> dateSelector = adapter2.f17870b;
        b bVar = adapter2.f17872d;
        Long item = adapter2.getItem(adapter2.b());
        Long item2 = adapter2.getItem(adapter2.i());
        for (androidx.core.util.d<Long, Long> dVar : dateSelector.M()) {
            Long l8 = dVar.f4889a;
            if (l8 != null) {
                if (dVar.f4890b != null) {
                    long longValue = l8.longValue();
                    long longValue2 = dVar.f4890b.longValue();
                    if (!d(item, item2, Long.valueOf(longValue), Long.valueOf(longValue2))) {
                        boolean h8 = s.h(this);
                        if (longValue < item.longValue()) {
                            a9 = adapter2.b();
                            if (adapter2.f(a9)) {
                                c9 = 0;
                            } else {
                                View childAt = materialCalendarGridView.getChildAt(a9 - 1);
                                c9 = !h8 ? childAt.getRight() : childAt.getLeft();
                            }
                        } else {
                            materialCalendarGridView.f17776a.setTimeInMillis(longValue);
                            a9 = adapter2.a(materialCalendarGridView.f17776a.get(5));
                            c9 = c(materialCalendarGridView.getChildAt(a9));
                        }
                        if (longValue2 > item2.longValue()) {
                            a10 = Math.min(adapter2.i(), getChildCount() - 1);
                            if (adapter2.g(a10)) {
                                c10 = getWidth();
                            } else {
                                View childAt2 = materialCalendarGridView.getChildAt(a10);
                                c10 = !h8 ? childAt2.getRight() : childAt2.getLeft();
                            }
                        } else {
                            materialCalendarGridView.f17776a.setTimeInMillis(longValue2);
                            a10 = adapter2.a(materialCalendarGridView.f17776a.get(5));
                            c10 = c(materialCalendarGridView.getChildAt(a10));
                        }
                        int itemId = (int) adapter2.getItemId(a9);
                        int itemId2 = (int) adapter2.getItemId(a10);
                        while (itemId <= itemId2) {
                            int numColumns = getNumColumns() * itemId;
                            int numColumns2 = (getNumColumns() + numColumns) - 1;
                            View childAt3 = materialCalendarGridView.getChildAt(numColumns);
                            int top = childAt3.getTop() + bVar.f17809a.c();
                            int bottom = childAt3.getBottom() - bVar.f17809a.b();
                            if (h8) {
                                int i9 = a10 > numColumns2 ? 0 : c10;
                                width = numColumns > a9 ? getWidth() : c9;
                                i8 = i9;
                            } else {
                                i8 = numColumns > a9 ? 0 : c9;
                                width = a10 > numColumns2 ? getWidth() : c10;
                            }
                            canvas.drawRect(i8, top, width, bottom, bVar.f17816h);
                            itemId++;
                            materialCalendarGridView = this;
                            adapter2 = adapter2;
                        }
                    }
                }
            }
            materialCalendarGridView = this;
        }
    }

    @Override // android.widget.GridView, android.widget.AbsListView, android.view.View
    protected void onFocusChanged(boolean z4, int i8, Rect rect) {
        if (z4) {
            a(i8, rect);
        } else {
            super.onFocusChanged(false, i8, rect);
        }
    }

    @Override // android.widget.GridView, android.widget.AbsListView, android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i8, KeyEvent keyEvent) {
        if (super.onKeyDown(i8, keyEvent)) {
            if (getSelectedItemPosition() == -1 || getSelectedItemPosition() >= getAdapter2().b()) {
                return true;
            }
            if (19 == i8) {
                setSelection(getAdapter2().b());
                return true;
            }
            return false;
        }
        return false;
    }

    @Override // android.widget.GridView, android.widget.AbsListView, android.view.View
    public void onMeasure(int i8, int i9) {
        if (!this.f17777b) {
            super.onMeasure(i8, i9);
            return;
        }
        super.onMeasure(i8, View.MeasureSpec.makeMeasureSpec(16777215, Integer.MIN_VALUE));
        getLayoutParams().height = getMeasuredHeight();
    }

    @Override // android.widget.AdapterView
    public final void setAdapter(ListAdapter listAdapter) {
        if (!(listAdapter instanceof j)) {
            throw new IllegalArgumentException(String.format("%1$s must have its Adapter set to a %2$s", MaterialCalendarGridView.class.getCanonicalName(), j.class.getCanonicalName()));
        }
        super.setAdapter(listAdapter);
    }

    @Override // android.widget.GridView, android.widget.AdapterView
    public void setSelection(int i8) {
        if (i8 < getAdapter2().b()) {
            i8 = getAdapter2().b();
        }
        super.setSelection(i8);
    }
}
