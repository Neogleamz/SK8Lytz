package androidx.appcompat.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import java.lang.ref.WeakReference;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ViewStubCompat extends View {

    /* renamed from: a  reason: collision with root package name */
    private int f1396a;

    /* renamed from: b  reason: collision with root package name */
    private int f1397b;

    /* renamed from: c  reason: collision with root package name */
    private WeakReference<View> f1398c;

    /* renamed from: d  reason: collision with root package name */
    private LayoutInflater f1399d;

    /* renamed from: e  reason: collision with root package name */
    private a f1400e;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a {
        void a(ViewStubCompat viewStubCompat, View view);
    }

    public ViewStubCompat(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ViewStubCompat(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        this.f1396a = 0;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, g.j.f20008a4, i8, 0);
        this.f1397b = obtainStyledAttributes.getResourceId(g.j.f20026d4, -1);
        this.f1396a = obtainStyledAttributes.getResourceId(g.j.f20020c4, 0);
        setId(obtainStyledAttributes.getResourceId(g.j.f20014b4, -1));
        obtainStyledAttributes.recycle();
        setVisibility(8);
        setWillNotDraw(true);
    }

    public View a() {
        ViewParent parent = getParent();
        if (parent instanceof ViewGroup) {
            if (this.f1396a != 0) {
                ViewGroup viewGroup = (ViewGroup) parent;
                LayoutInflater layoutInflater = this.f1399d;
                if (layoutInflater == null) {
                    layoutInflater = LayoutInflater.from(getContext());
                }
                View inflate = layoutInflater.inflate(this.f1396a, viewGroup, false);
                int i8 = this.f1397b;
                if (i8 != -1) {
                    inflate.setId(i8);
                }
                int indexOfChild = viewGroup.indexOfChild(this);
                viewGroup.removeViewInLayout(this);
                ViewGroup.LayoutParams layoutParams = getLayoutParams();
                if (layoutParams != null) {
                    viewGroup.addView(inflate, indexOfChild, layoutParams);
                } else {
                    viewGroup.addView(inflate, indexOfChild);
                }
                this.f1398c = new WeakReference<>(inflate);
                a aVar = this.f1400e;
                if (aVar != null) {
                    aVar.a(this, inflate);
                }
                return inflate;
            }
            throw new IllegalArgumentException("ViewStub must have a valid layoutResource");
        }
        throw new IllegalStateException("ViewStub must have a non-null ViewGroup viewParent");
    }

    @Override // android.view.View
    protected void dispatchDraw(Canvas canvas) {
    }

    @Override // android.view.View
    @SuppressLint({"MissingSuperCall"})
    public void draw(Canvas canvas) {
    }

    public int getInflatedId() {
        return this.f1397b;
    }

    public LayoutInflater getLayoutInflater() {
        return this.f1399d;
    }

    public int getLayoutResource() {
        return this.f1396a;
    }

    @Override // android.view.View
    protected void onMeasure(int i8, int i9) {
        setMeasuredDimension(0, 0);
    }

    public void setInflatedId(int i8) {
        this.f1397b = i8;
    }

    public void setLayoutInflater(LayoutInflater layoutInflater) {
        this.f1399d = layoutInflater;
    }

    public void setLayoutResource(int i8) {
        this.f1396a = i8;
    }

    public void setOnInflateListener(a aVar) {
        this.f1400e = aVar;
    }

    @Override // android.view.View
    public void setVisibility(int i8) {
        WeakReference<View> weakReference = this.f1398c;
        if (weakReference != null) {
            View view = weakReference.get();
            if (view == null) {
                throw new IllegalStateException("setVisibility called on un-referenced view");
            }
            view.setVisibility(i8);
            return;
        }
        super.setVisibility(i8);
        if (i8 == 0 || i8 == 4) {
            a();
        }
    }
}
