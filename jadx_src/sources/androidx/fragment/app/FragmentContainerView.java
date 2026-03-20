package androidx.fragment.app;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import androidx.core.view.c0;
import androidx.core.view.m0;
import com.daimajia.numberprogressbar.BuildConfig;
import java.util.ArrayList;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class FragmentContainerView extends FrameLayout {

    /* renamed from: a  reason: collision with root package name */
    private ArrayList<View> f5456a;

    /* renamed from: b  reason: collision with root package name */
    private ArrayList<View> f5457b;

    /* renamed from: c  reason: collision with root package name */
    private View.OnApplyWindowInsetsListener f5458c;

    /* renamed from: d  reason: collision with root package name */
    private boolean f5459d;

    public FragmentContainerView(Context context) {
        super(context);
        this.f5459d = true;
    }

    public FragmentContainerView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public FragmentContainerView(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        String str;
        this.f5459d = true;
        if (attributeSet != null) {
            String classAttribute = attributeSet.getClassAttribute();
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, b1.c.f7957h);
            if (classAttribute == null) {
                classAttribute = obtainStyledAttributes.getString(b1.c.f7958i);
                str = "android:name";
            } else {
                str = "class";
            }
            obtainStyledAttributes.recycle();
            if (classAttribute == null || isInEditMode()) {
                return;
            }
            throw new UnsupportedOperationException("FragmentContainerView must be within a FragmentActivity to use " + str + "=\"" + classAttribute + "\"");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FragmentContainerView(Context context, AttributeSet attributeSet, FragmentManager fragmentManager) {
        super(context, attributeSet);
        String str;
        this.f5459d = true;
        String classAttribute = attributeSet.getClassAttribute();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, b1.c.f7957h);
        classAttribute = classAttribute == null ? obtainStyledAttributes.getString(b1.c.f7958i) : classAttribute;
        String string = obtainStyledAttributes.getString(b1.c.f7959j);
        obtainStyledAttributes.recycle();
        int id = getId();
        Fragment h02 = fragmentManager.h0(id);
        if (classAttribute != null && h02 == null) {
            if (id <= 0) {
                if (string != null) {
                    str = " with tag " + string;
                } else {
                    str = BuildConfig.FLAVOR;
                }
                throw new IllegalStateException("FragmentContainerView must have an android:id to add Fragment " + classAttribute + str);
            }
            Fragment a9 = fragmentManager.q0().a(context.getClassLoader(), classAttribute);
            a9.A0(context, attributeSet, null);
            fragmentManager.l().v(true).c(this, a9, string).l();
        }
        fragmentManager.U0(this);
    }

    private void a(View view) {
        ArrayList<View> arrayList = this.f5457b;
        if (arrayList == null || !arrayList.contains(view)) {
            return;
        }
        if (this.f5456a == null) {
            this.f5456a = new ArrayList<>();
        }
        this.f5456a.add(view);
    }

    @Override // android.view.ViewGroup
    public void addView(View view, int i8, ViewGroup.LayoutParams layoutParams) {
        if (FragmentManager.z0(view) != null) {
            super.addView(view, i8, layoutParams);
            return;
        }
        throw new IllegalStateException("Views added to a FragmentContainerView must be associated with a Fragment. View " + view + " is not associated with a Fragment.");
    }

    @Override // android.view.ViewGroup
    protected boolean addViewInLayout(View view, int i8, ViewGroup.LayoutParams layoutParams, boolean z4) {
        if (FragmentManager.z0(view) != null) {
            return super.addViewInLayout(view, i8, layoutParams, z4);
        }
        throw new IllegalStateException("Views added to a FragmentContainerView must be associated with a Fragment. View " + view + " is not associated with a Fragment.");
    }

    @Override // android.view.ViewGroup, android.view.View
    public WindowInsets dispatchApplyWindowInsets(WindowInsets windowInsets) {
        m0 y8 = m0.y(windowInsets);
        View.OnApplyWindowInsetsListener onApplyWindowInsetsListener = this.f5458c;
        m0 y9 = onApplyWindowInsetsListener != null ? m0.y(onApplyWindowInsetsListener.onApplyWindowInsets(this, windowInsets)) : c0.e0(this, y8);
        if (!y9.q()) {
            int childCount = getChildCount();
            for (int i8 = 0; i8 < childCount; i8++) {
                c0.i(getChildAt(i8), y9);
            }
        }
        return windowInsets;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        if (this.f5459d && this.f5456a != null) {
            for (int i8 = 0; i8 < this.f5456a.size(); i8++) {
                super.drawChild(canvas, this.f5456a.get(i8), getDrawingTime());
            }
        }
        super.dispatchDraw(canvas);
    }

    @Override // android.view.ViewGroup
    protected boolean drawChild(Canvas canvas, View view, long j8) {
        ArrayList<View> arrayList;
        if (!this.f5459d || (arrayList = this.f5456a) == null || arrayList.size() <= 0 || !this.f5456a.contains(view)) {
            return super.drawChild(canvas, view, j8);
        }
        return false;
    }

    @Override // android.view.ViewGroup
    public void endViewTransition(View view) {
        ArrayList<View> arrayList = this.f5457b;
        if (arrayList != null) {
            arrayList.remove(view);
            ArrayList<View> arrayList2 = this.f5456a;
            if (arrayList2 != null && arrayList2.remove(view)) {
                this.f5459d = true;
            }
        }
        super.endViewTransition(view);
    }

    @Override // android.view.View
    public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        return windowInsets;
    }

    @Override // android.view.ViewGroup
    public void removeAllViewsInLayout() {
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            a(getChildAt(childCount));
        }
        super.removeAllViewsInLayout();
    }

    @Override // android.view.ViewGroup
    protected void removeDetachedView(View view, boolean z4) {
        if (z4) {
            a(view);
        }
        super.removeDetachedView(view, z4);
    }

    @Override // android.view.ViewGroup, android.view.ViewManager
    public void removeView(View view) {
        a(view);
        super.removeView(view);
    }

    @Override // android.view.ViewGroup
    public void removeViewAt(int i8) {
        a(getChildAt(i8));
        super.removeViewAt(i8);
    }

    @Override // android.view.ViewGroup
    public void removeViewInLayout(View view) {
        a(view);
        super.removeViewInLayout(view);
    }

    @Override // android.view.ViewGroup
    public void removeViews(int i8, int i9) {
        for (int i10 = i8; i10 < i8 + i9; i10++) {
            a(getChildAt(i10));
        }
        super.removeViews(i8, i9);
    }

    @Override // android.view.ViewGroup
    public void removeViewsInLayout(int i8, int i9) {
        for (int i10 = i8; i10 < i8 + i9; i10++) {
            a(getChildAt(i10));
        }
        super.removeViewsInLayout(i8, i9);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDrawDisappearingViewsLast(boolean z4) {
        this.f5459d = z4;
    }

    @Override // android.view.ViewGroup
    public void setLayoutTransition(LayoutTransition layoutTransition) {
        if (Build.VERSION.SDK_INT >= 18) {
            throw new UnsupportedOperationException("FragmentContainerView does not support Layout Transitions or animateLayoutChanges=\"true\".");
        }
        super.setLayoutTransition(layoutTransition);
    }

    @Override // android.view.View
    public void setOnApplyWindowInsetsListener(View.OnApplyWindowInsetsListener onApplyWindowInsetsListener) {
        this.f5458c = onApplyWindowInsetsListener;
    }

    @Override // android.view.ViewGroup
    public void startViewTransition(View view) {
        if (view.getParent() == this) {
            if (this.f5457b == null) {
                this.f5457b = new ArrayList<>();
            }
            this.f5457b.add(view);
        }
        super.startViewTransition(view);
    }
}
