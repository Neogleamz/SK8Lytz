package androidx.constraintlayout.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.b;
import java.util.Arrays;
import java.util.HashMap;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class ConstraintHelper extends View {

    /* renamed from: a  reason: collision with root package name */
    protected int[] f3929a;

    /* renamed from: b  reason: collision with root package name */
    protected int f3930b;

    /* renamed from: c  reason: collision with root package name */
    protected Context f3931c;

    /* renamed from: d  reason: collision with root package name */
    protected n0.a f3932d;

    /* renamed from: e  reason: collision with root package name */
    protected boolean f3933e;

    /* renamed from: f  reason: collision with root package name */
    protected String f3934f;

    /* renamed from: g  reason: collision with root package name */
    private View[] f3935g;

    /* renamed from: h  reason: collision with root package name */
    private HashMap<Integer, String> f3936h;

    public ConstraintHelper(Context context) {
        super(context);
        this.f3929a = new int[32];
        this.f3933e = false;
        this.f3935g = null;
        this.f3936h = new HashMap<>();
        this.f3931c = context;
        m(null);
    }

    public ConstraintHelper(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f3929a = new int[32];
        this.f3933e = false;
        this.f3935g = null;
        this.f3936h = new HashMap<>();
        this.f3931c = context;
        m(attributeSet);
    }

    public ConstraintHelper(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        this.f3929a = new int[32];
        this.f3933e = false;
        this.f3935g = null;
        this.f3936h = new HashMap<>();
        this.f3931c = context;
        m(attributeSet);
    }

    private void e(String str) {
        if (str == null || str.length() == 0 || this.f3931c == null) {
            return;
        }
        String trim = str.trim();
        if (getParent() instanceof ConstraintLayout) {
            ConstraintLayout constraintLayout = (ConstraintLayout) getParent();
        }
        int k8 = k(trim);
        if (k8 != 0) {
            this.f3936h.put(Integer.valueOf(k8), trim);
            f(k8);
            return;
        }
        Log.w("ConstraintHelper", "Could not find id of \"" + trim + "\"");
    }

    private void f(int i8) {
        if (i8 == getId()) {
            return;
        }
        int i9 = this.f3930b + 1;
        int[] iArr = this.f3929a;
        if (i9 > iArr.length) {
            this.f3929a = Arrays.copyOf(iArr, iArr.length * 2);
        }
        int[] iArr2 = this.f3929a;
        int i10 = this.f3930b;
        iArr2[i10] = i8;
        this.f3930b = i10 + 1;
    }

    private int[] i(View view, String str) {
        String[] split = str.split(",");
        view.getContext();
        int[] iArr = new int[split.length];
        int i8 = 0;
        for (String str2 : split) {
            int k8 = k(str2.trim());
            if (k8 != 0) {
                iArr[i8] = k8;
                i8++;
            }
        }
        return i8 != split.length ? Arrays.copyOf(iArr, i8) : iArr;
    }

    private int j(ConstraintLayout constraintLayout, String str) {
        Resources resources;
        if (str == null || constraintLayout == null || (resources = this.f3931c.getResources()) == null) {
            return 0;
        }
        int childCount = constraintLayout.getChildCount();
        for (int i8 = 0; i8 < childCount; i8++) {
            View childAt = constraintLayout.getChildAt(i8);
            if (childAt.getId() != -1) {
                String str2 = null;
                try {
                    str2 = resources.getResourceEntryName(childAt.getId());
                } catch (Resources.NotFoundException unused) {
                }
                if (str.equals(str2)) {
                    return childAt.getId();
                }
            }
        }
        return 0;
    }

    private int k(String str) {
        ConstraintLayout constraintLayout = getParent() instanceof ConstraintLayout ? (ConstraintLayout) getParent() : null;
        int i8 = 0;
        if (isInEditMode() && constraintLayout != null) {
            Object f5 = constraintLayout.f(0, str);
            if (f5 instanceof Integer) {
                i8 = ((Integer) f5).intValue();
            }
        }
        if (i8 == 0 && constraintLayout != null) {
            i8 = j(constraintLayout, str);
        }
        if (i8 == 0) {
            try {
                i8 = d.class.getField(str).getInt(null);
            } catch (Exception unused) {
            }
        }
        return i8 == 0 ? this.f3931c.getResources().getIdentifier(str, "id", this.f3931c.getPackageName()) : i8;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void g() {
        ViewParent parent = getParent();
        if (parent == null || !(parent instanceof ConstraintLayout)) {
            return;
        }
        h((ConstraintLayout) parent);
    }

    public int[] getReferencedIds() {
        return Arrays.copyOf(this.f3929a, this.f3930b);
    }

    protected void h(ConstraintLayout constraintLayout) {
        int visibility = getVisibility();
        float elevation = Build.VERSION.SDK_INT >= 21 ? getElevation() : 0.0f;
        for (int i8 = 0; i8 < this.f3930b; i8++) {
            View h8 = constraintLayout.h(this.f3929a[i8]);
            if (h8 != null) {
                h8.setVisibility(visibility);
                if (elevation > 0.0f && Build.VERSION.SDK_INT >= 21) {
                    h8.setTranslationZ(h8.getTranslationZ() + elevation);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public View[] l(ConstraintLayout constraintLayout) {
        View[] viewArr = this.f3935g;
        if (viewArr == null || viewArr.length != this.f3930b) {
            this.f3935g = new View[this.f3930b];
        }
        for (int i8 = 0; i8 < this.f3930b; i8++) {
            this.f3935g[i8] = constraintLayout.h(this.f3929a[i8]);
        }
        return this.f3935g;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void m(AttributeSet attributeSet) {
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, e.f4117a1);
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i8 = 0; i8 < indexCount; i8++) {
                int index = obtainStyledAttributes.getIndex(i8);
                if (index == e.f4287t1) {
                    String string = obtainStyledAttributes.getString(index);
                    this.f3934f = string;
                    setIds(string);
                }
            }
        }
    }

    public void n(b.a aVar, n0.b bVar, ConstraintLayout.LayoutParams layoutParams, SparseArray<ConstraintWidget> sparseArray) {
        b.C0027b c0027b = aVar.f4048d;
        int[] iArr = c0027b.f4061e0;
        if (iArr != null) {
            setReferencedIds(iArr);
        } else {
            String str = c0027b.f4063f0;
            if (str != null && str.length() > 0) {
                b.C0027b c0027b2 = aVar.f4048d;
                c0027b2.f4061e0 = i(this, c0027b2.f4063f0);
            }
        }
        bVar.b();
        if (aVar.f4048d.f4061e0 == null) {
            return;
        }
        int i8 = 0;
        while (true) {
            int[] iArr2 = aVar.f4048d.f4061e0;
            if (i8 >= iArr2.length) {
                return;
            }
            ConstraintWidget constraintWidget = sparseArray.get(iArr2[i8]);
            if (constraintWidget != null) {
                bVar.a(constraintWidget);
            }
            i8++;
        }
    }

    public void o(ConstraintWidget constraintWidget, boolean z4) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        String str = this.f3934f;
        if (str != null) {
            setIds(str);
        }
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
    }

    @Override // android.view.View
    protected void onMeasure(int i8, int i9) {
        if (this.f3933e) {
            super.onMeasure(i8, i9);
        } else {
            setMeasuredDimension(0, 0);
        }
    }

    public void p(ConstraintLayout constraintLayout) {
    }

    public void q(ConstraintLayout constraintLayout) {
    }

    public void r(ConstraintLayout constraintLayout) {
    }

    public void s(androidx.constraintlayout.solver.widgets.d dVar, n0.a aVar, SparseArray<ConstraintWidget> sparseArray) {
        aVar.b();
        for (int i8 = 0; i8 < this.f3930b; i8++) {
            aVar.a(sparseArray.get(this.f3929a[i8]));
        }
    }

    protected void setIds(String str) {
        this.f3934f = str;
        if (str == null) {
            return;
        }
        int i8 = 0;
        this.f3930b = 0;
        while (true) {
            int indexOf = str.indexOf(44, i8);
            if (indexOf == -1) {
                e(str.substring(i8));
                return;
            } else {
                e(str.substring(i8, indexOf));
                i8 = indexOf + 1;
            }
        }
    }

    public void setReferencedIds(int[] iArr) {
        this.f3934f = null;
        this.f3930b = 0;
        for (int i8 : iArr) {
            f(i8);
        }
    }

    public void t(ConstraintLayout constraintLayout) {
        String str;
        int j8;
        if (isInEditMode()) {
            setIds(this.f3934f);
        }
        n0.a aVar = this.f3932d;
        if (aVar == null) {
            return;
        }
        aVar.b();
        for (int i8 = 0; i8 < this.f3930b; i8++) {
            int i9 = this.f3929a[i8];
            View h8 = constraintLayout.h(i9);
            if (h8 == null && (j8 = j(constraintLayout, (str = this.f3936h.get(Integer.valueOf(i9))))) != 0) {
                this.f3929a[i8] = j8;
                this.f3936h.put(Integer.valueOf(j8), str);
                h8 = constraintLayout.h(j8);
            }
            if (h8 != null) {
                this.f3932d.a(constraintLayout.i(h8));
            }
        }
        this.f3932d.c(constraintLayout.f3939c);
    }

    public void u() {
        if (this.f3932d == null) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams instanceof ConstraintLayout.LayoutParams) {
            ((ConstraintLayout.LayoutParams) layoutParams).f3983n0 = (ConstraintWidget) this.f3932d;
        }
    }
}
