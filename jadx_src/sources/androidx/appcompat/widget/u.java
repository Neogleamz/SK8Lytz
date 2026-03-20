package androidx.appcompat.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class u extends ListView {

    /* renamed from: a  reason: collision with root package name */
    private final Rect f1615a;

    /* renamed from: b  reason: collision with root package name */
    private int f1616b;

    /* renamed from: c  reason: collision with root package name */
    private int f1617c;

    /* renamed from: d  reason: collision with root package name */
    private int f1618d;

    /* renamed from: e  reason: collision with root package name */
    private int f1619e;

    /* renamed from: f  reason: collision with root package name */
    private int f1620f;

    /* renamed from: g  reason: collision with root package name */
    private d f1621g;

    /* renamed from: h  reason: collision with root package name */
    private boolean f1622h;

    /* renamed from: j  reason: collision with root package name */
    private boolean f1623j;

    /* renamed from: k  reason: collision with root package name */
    private boolean f1624k;

    /* renamed from: l  reason: collision with root package name */
    private androidx.core.view.i0 f1625l;

    /* renamed from: m  reason: collision with root package name */
    private androidx.core.widget.h f1626m;

    /* renamed from: n  reason: collision with root package name */
    f f1627n;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {
        static void a(View view, float f5, float f8) {
            view.drawableHotspotChanged(f5, f8);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class b {

        /* renamed from: a  reason: collision with root package name */
        private static Method f1628a;

        /* renamed from: b  reason: collision with root package name */
        private static Method f1629b;

        /* renamed from: c  reason: collision with root package name */
        private static Method f1630c;

        /* renamed from: d  reason: collision with root package name */
        private static boolean f1631d;

        static {
            try {
                Class cls = Integer.TYPE;
                Class cls2 = Float.TYPE;
                Method declaredMethod = AbsListView.class.getDeclaredMethod("positionSelector", cls, View.class, Boolean.TYPE, cls2, cls2);
                f1628a = declaredMethod;
                declaredMethod.setAccessible(true);
                Method declaredMethod2 = AdapterView.class.getDeclaredMethod("setSelectedPositionInt", cls);
                f1629b = declaredMethod2;
                declaredMethod2.setAccessible(true);
                Method declaredMethod3 = AdapterView.class.getDeclaredMethod("setNextSelectedPositionInt", cls);
                f1630c = declaredMethod3;
                declaredMethod3.setAccessible(true);
                f1631d = true;
            } catch (NoSuchMethodException e8) {
                e8.printStackTrace();
            }
        }

        static boolean a() {
            return f1631d;
        }

        @SuppressLint({"BanUncheckedReflection"})
        static void b(u uVar, int i8, View view) {
            try {
                f1628a.invoke(uVar, Integer.valueOf(i8), view, Boolean.FALSE, -1, -1);
                f1629b.invoke(uVar, Integer.valueOf(i8));
                f1630c.invoke(uVar, Integer.valueOf(i8));
            } catch (IllegalAccessException e8) {
                e8.printStackTrace();
            } catch (InvocationTargetException e9) {
                e9.printStackTrace();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class c {
        static boolean a(AbsListView absListView) {
            return absListView.isSelectedChildViewEnabled();
        }

        static void b(AbsListView absListView, boolean z4) {
            absListView.setSelectedChildViewEnabled(z4);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class d extends i.c {

        /* renamed from: b  reason: collision with root package name */
        private boolean f1632b;

        d(Drawable drawable) {
            super(drawable);
            this.f1632b = true;
        }

        void c(boolean z4) {
            this.f1632b = z4;
        }

        @Override // i.c, android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            if (this.f1632b) {
                super.draw(canvas);
            }
        }

        @Override // i.c, android.graphics.drawable.Drawable
        public void setHotspot(float f5, float f8) {
            if (this.f1632b) {
                super.setHotspot(f5, f8);
            }
        }

        @Override // i.c, android.graphics.drawable.Drawable
        public void setHotspotBounds(int i8, int i9, int i10, int i11) {
            if (this.f1632b) {
                super.setHotspotBounds(i8, i9, i10, i11);
            }
        }

        @Override // i.c, android.graphics.drawable.Drawable
        public boolean setState(int[] iArr) {
            if (this.f1632b) {
                return super.setState(iArr);
            }
            return false;
        }

        @Override // i.c, android.graphics.drawable.Drawable
        public boolean setVisible(boolean z4, boolean z8) {
            if (this.f1632b) {
                return super.setVisible(z4, z8);
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class e {

        /* renamed from: a  reason: collision with root package name */
        private static final Field f1633a;

        static {
            Field field = null;
            try {
                field = AbsListView.class.getDeclaredField("mIsChildViewEnabled");
                field.setAccessible(true);
            } catch (NoSuchFieldException e8) {
                e8.printStackTrace();
            }
            f1633a = field;
        }

        static boolean a(AbsListView absListView) {
            Field field = f1633a;
            if (field != null) {
                try {
                    return field.getBoolean(absListView);
                } catch (IllegalAccessException e8) {
                    e8.printStackTrace();
                    return false;
                }
            }
            return false;
        }

        static void b(AbsListView absListView, boolean z4) {
            Field field = f1633a;
            if (field != null) {
                try {
                    field.set(absListView, Boolean.valueOf(z4));
                } catch (IllegalAccessException e8) {
                    e8.printStackTrace();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class f implements Runnable {
        f() {
        }

        public void a() {
            u uVar = u.this;
            uVar.f1627n = null;
            uVar.removeCallbacks(this);
        }

        public void b() {
            u.this.post(this);
        }

        @Override // java.lang.Runnable
        public void run() {
            u uVar = u.this;
            uVar.f1627n = null;
            uVar.drawableStateChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public u(Context context, boolean z4) {
        super(context, null, g.a.C);
        this.f1615a = new Rect();
        this.f1616b = 0;
        this.f1617c = 0;
        this.f1618d = 0;
        this.f1619e = 0;
        this.f1623j = z4;
        setCacheColorHint(0);
    }

    private void a() {
        this.f1624k = false;
        setPressed(false);
        drawableStateChanged();
        View childAt = getChildAt(this.f1620f - getFirstVisiblePosition());
        if (childAt != null) {
            childAt.setPressed(false);
        }
        androidx.core.view.i0 i0Var = this.f1625l;
        if (i0Var != null) {
            i0Var.c();
            this.f1625l = null;
        }
    }

    private void b(View view, int i8) {
        performItemClick(view, i8, getItemIdAtPosition(i8));
    }

    private void c(Canvas canvas) {
        Drawable selector;
        if (this.f1615a.isEmpty() || (selector = getSelector()) == null) {
            return;
        }
        selector.setBounds(this.f1615a);
        selector.draw(canvas);
    }

    private void f(int i8, View view) {
        Rect rect = this.f1615a;
        rect.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        rect.left -= this.f1616b;
        rect.top -= this.f1617c;
        rect.right += this.f1618d;
        rect.bottom += this.f1619e;
        boolean j8 = j();
        if (view.isEnabled() != j8) {
            k(!j8);
            if (i8 != -1) {
                refreshDrawableState();
            }
        }
    }

    private void g(int i8, View view) {
        Drawable selector = getSelector();
        boolean z4 = (selector == null || i8 == -1) ? false : true;
        if (z4) {
            selector.setVisible(false, false);
        }
        f(i8, view);
        if (z4) {
            Rect rect = this.f1615a;
            float exactCenterX = rect.exactCenterX();
            float exactCenterY = rect.exactCenterY();
            selector.setVisible(getVisibility() == 0, false);
            androidx.core.graphics.drawable.a.k(selector, exactCenterX, exactCenterY);
        }
    }

    private void h(int i8, View view, float f5, float f8) {
        g(i8, view);
        Drawable selector = getSelector();
        if (selector == null || i8 == -1) {
            return;
        }
        androidx.core.graphics.drawable.a.k(selector, f5, f8);
    }

    private void i(View view, int i8, float f5, float f8) {
        View childAt;
        this.f1624k = true;
        int i9 = Build.VERSION.SDK_INT;
        if (i9 >= 21) {
            a.a(this, f5, f8);
        }
        if (!isPressed()) {
            setPressed(true);
        }
        layoutChildren();
        int i10 = this.f1620f;
        if (i10 != -1 && (childAt = getChildAt(i10 - getFirstVisiblePosition())) != null && childAt != view && childAt.isPressed()) {
            childAt.setPressed(false);
        }
        this.f1620f = i8;
        float left = f5 - view.getLeft();
        float top = f8 - view.getTop();
        if (i9 >= 21) {
            a.a(view, left, top);
        }
        if (!view.isPressed()) {
            view.setPressed(true);
        }
        h(i8, view, f5, f8);
        setSelectorEnabled(false);
        refreshDrawableState();
    }

    private boolean j() {
        return androidx.core.os.a.d() ? c.a(this) : e.a(this);
    }

    private void k(boolean z4) {
        if (androidx.core.os.a.d()) {
            c.b(this, z4);
        } else {
            e.b(this, z4);
        }
    }

    private boolean l() {
        return this.f1624k;
    }

    private void m() {
        Drawable selector = getSelector();
        if (selector != null && l() && isPressed()) {
            selector.setState(getDrawableState());
        }
    }

    private void setSelectorEnabled(boolean z4) {
        d dVar = this.f1621g;
        if (dVar != null) {
            dVar.c(z4);
        }
    }

    public int d(int i8, int i9, int i10, int i11, int i12) {
        int listPaddingTop = getListPaddingTop();
        int listPaddingBottom = getListPaddingBottom();
        int dividerHeight = getDividerHeight();
        Drawable divider = getDivider();
        ListAdapter adapter = getAdapter();
        int i13 = listPaddingTop + listPaddingBottom;
        if (adapter == null) {
            return i13;
        }
        if (dividerHeight <= 0 || divider == null) {
            dividerHeight = 0;
        }
        int count = adapter.getCount();
        int i14 = 0;
        int i15 = 0;
        int i16 = 0;
        View view = null;
        while (i14 < count) {
            int itemViewType = adapter.getItemViewType(i14);
            if (itemViewType != i15) {
                view = null;
                i15 = itemViewType;
            }
            view = adapter.getView(i14, view, this);
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = generateDefaultLayoutParams();
                view.setLayoutParams(layoutParams);
            }
            int i17 = layoutParams.height;
            view.measure(i8, i17 > 0 ? View.MeasureSpec.makeMeasureSpec(i17, 1073741824) : View.MeasureSpec.makeMeasureSpec(0, 0));
            view.forceLayout();
            if (i14 > 0) {
                i13 += dividerHeight;
            }
            i13 += view.getMeasuredHeight();
            if (i13 >= i11) {
                return (i12 < 0 || i14 <= i12 || i16 <= 0 || i13 == i11) ? i11 : i16;
            }
            if (i12 >= 0 && i14 >= i12) {
                i16 = i13;
            }
            i14++;
        }
        return i13;
    }

    @Override // android.widget.ListView, android.widget.AbsListView, android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        c(canvas);
        super.dispatchDraw(canvas);
    }

    @Override // android.widget.AbsListView, android.view.ViewGroup, android.view.View
    protected void drawableStateChanged() {
        if (this.f1627n != null) {
            return;
        }
        super.drawableStateChanged();
        setSelectorEnabled(true);
        m();
    }

    /* JADX WARN: Code restructure failed: missing block: B:7:0x000c, code lost:
        if (r0 != 3) goto L7;
     */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0048 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:25:0x004f  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0065  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean e(android.view.MotionEvent r8, int r9) {
        /*
            r7 = this;
            int r0 = r8.getActionMasked()
            r1 = 0
            r2 = 1
            if (r0 == r2) goto L16
            r3 = 2
            if (r0 == r3) goto L14
            r9 = 3
            if (r0 == r9) goto L11
        Le:
            r9 = r1
            r3 = r2
            goto L46
        L11:
            r9 = r1
            r3 = r9
            goto L46
        L14:
            r3 = r2
            goto L17
        L16:
            r3 = r1
        L17:
            int r9 = r8.findPointerIndex(r9)
            if (r9 >= 0) goto L1e
            goto L11
        L1e:
            float r4 = r8.getX(r9)
            int r4 = (int) r4
            float r9 = r8.getY(r9)
            int r9 = (int) r9
            int r5 = r7.pointToPosition(r4, r9)
            r6 = -1
            if (r5 != r6) goto L31
            r9 = r2
            goto L46
        L31:
            int r3 = r7.getFirstVisiblePosition()
            int r3 = r5 - r3
            android.view.View r3 = r7.getChildAt(r3)
            float r4 = (float) r4
            float r9 = (float) r9
            r7.i(r3, r5, r4, r9)
            if (r0 != r2) goto Le
            r7.b(r3, r5)
            goto Le
        L46:
            if (r3 == 0) goto L4a
            if (r9 == 0) goto L4d
        L4a:
            r7.a()
        L4d:
            if (r3 == 0) goto L65
            androidx.core.widget.h r9 = r7.f1626m
            if (r9 != 0) goto L5a
            androidx.core.widget.h r9 = new androidx.core.widget.h
            r9.<init>(r7)
            r7.f1626m = r9
        L5a:
            androidx.core.widget.h r9 = r7.f1626m
            r9.m(r2)
            androidx.core.widget.h r9 = r7.f1626m
            r9.onTouch(r7, r8)
            goto L6c
        L65:
            androidx.core.widget.h r8 = r7.f1626m
            if (r8 == 0) goto L6c
            r8.m(r1)
        L6c:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.u.e(android.view.MotionEvent, int):boolean");
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean hasFocus() {
        return this.f1623j || super.hasFocus();
    }

    @Override // android.view.View
    public boolean hasWindowFocus() {
        return this.f1623j || super.hasWindowFocus();
    }

    @Override // android.view.View
    public boolean isFocused() {
        return this.f1623j || super.isFocused();
    }

    @Override // android.view.View
    public boolean isInTouchMode() {
        return (this.f1623j && this.f1622h) || super.isInTouchMode();
    }

    @Override // android.widget.ListView, android.widget.AbsListView, android.widget.AdapterView, android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        this.f1627n = null;
        super.onDetachedFromWindow();
    }

    @Override // android.view.View
    public boolean onHoverEvent(MotionEvent motionEvent) {
        int i8 = Build.VERSION.SDK_INT;
        if (i8 < 26) {
            return super.onHoverEvent(motionEvent);
        }
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 10 && this.f1627n == null) {
            f fVar = new f();
            this.f1627n = fVar;
            fVar.b();
        }
        boolean onHoverEvent = super.onHoverEvent(motionEvent);
        if (actionMasked == 9 || actionMasked == 7) {
            int pointToPosition = pointToPosition((int) motionEvent.getX(), (int) motionEvent.getY());
            if (pointToPosition != -1 && pointToPosition != getSelectedItemPosition()) {
                View childAt = getChildAt(pointToPosition - getFirstVisiblePosition());
                if (childAt.isEnabled()) {
                    requestFocus();
                    if (i8 < 30 || !b.a()) {
                        setSelectionFromTop(pointToPosition, childAt.getTop() - getTop());
                    } else {
                        b.b(this, pointToPosition, childAt);
                    }
                }
                m();
            }
        } else {
            setSelection(-1);
        }
        return onHoverEvent;
    }

    @Override // android.widget.AbsListView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            this.f1620f = pointToPosition((int) motionEvent.getX(), (int) motionEvent.getY());
        }
        f fVar = this.f1627n;
        if (fVar != null) {
            fVar.a();
        }
        return super.onTouchEvent(motionEvent);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setListSelectionHidden(boolean z4) {
        this.f1622h = z4;
    }

    @Override // android.widget.AbsListView
    public void setSelector(Drawable drawable) {
        d dVar = drawable != null ? new d(drawable) : null;
        this.f1621g = dVar;
        super.setSelector(dVar);
        Rect rect = new Rect();
        if (drawable != null) {
            drawable.getPadding(rect);
        }
        this.f1616b = rect.left;
        this.f1617c = rect.top;
        this.f1618d = rect.right;
        this.f1619e = rect.bottom;
    }
}
