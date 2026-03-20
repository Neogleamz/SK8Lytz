package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import androidx.core.content.res.h;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class j0 {

    /* renamed from: a  reason: collision with root package name */
    private final Context f1505a;

    /* renamed from: b  reason: collision with root package name */
    private final TypedArray f1506b;

    /* renamed from: c  reason: collision with root package name */
    private TypedValue f1507c;

    private j0(Context context, TypedArray typedArray) {
        this.f1505a = context;
        this.f1506b = typedArray;
    }

    public static j0 t(Context context, int i8, int[] iArr) {
        return new j0(context, context.obtainStyledAttributes(i8, iArr));
    }

    public static j0 u(Context context, AttributeSet attributeSet, int[] iArr) {
        return new j0(context, context.obtainStyledAttributes(attributeSet, iArr));
    }

    public static j0 v(Context context, AttributeSet attributeSet, int[] iArr, int i8, int i9) {
        return new j0(context, context.obtainStyledAttributes(attributeSet, iArr, i8, i9));
    }

    public boolean a(int i8, boolean z4) {
        return this.f1506b.getBoolean(i8, z4);
    }

    public int b(int i8, int i9) {
        return this.f1506b.getColor(i8, i9);
    }

    public ColorStateList c(int i8) {
        int resourceId;
        ColorStateList a9;
        return (!this.f1506b.hasValue(i8) || (resourceId = this.f1506b.getResourceId(i8, 0)) == 0 || (a9 = h.a.a(this.f1505a, resourceId)) == null) ? this.f1506b.getColorStateList(i8) : a9;
    }

    public float d(int i8, float f5) {
        return this.f1506b.getDimension(i8, f5);
    }

    public int e(int i8, int i9) {
        return this.f1506b.getDimensionPixelOffset(i8, i9);
    }

    public int f(int i8, int i9) {
        return this.f1506b.getDimensionPixelSize(i8, i9);
    }

    public Drawable g(int i8) {
        int resourceId;
        return (!this.f1506b.hasValue(i8) || (resourceId = this.f1506b.getResourceId(i8, 0)) == 0) ? this.f1506b.getDrawable(i8) : h.a.b(this.f1505a, resourceId);
    }

    public Drawable h(int i8) {
        int resourceId;
        if (!this.f1506b.hasValue(i8) || (resourceId = this.f1506b.getResourceId(i8, 0)) == 0) {
            return null;
        }
        return g.b().d(this.f1505a, resourceId, true);
    }

    public float i(int i8, float f5) {
        return this.f1506b.getFloat(i8, f5);
    }

    public Typeface j(int i8, int i9, h.f fVar) {
        int resourceId = this.f1506b.getResourceId(i8, 0);
        if (resourceId == 0) {
            return null;
        }
        if (this.f1507c == null) {
            this.f1507c = new TypedValue();
        }
        return androidx.core.content.res.h.h(this.f1505a, resourceId, this.f1507c, i9, fVar);
    }

    public int k(int i8, int i9) {
        return this.f1506b.getInt(i8, i9);
    }

    public int l(int i8, int i9) {
        return this.f1506b.getInteger(i8, i9);
    }

    public int m(int i8, int i9) {
        return this.f1506b.getLayoutDimension(i8, i9);
    }

    public int n(int i8, int i9) {
        return this.f1506b.getResourceId(i8, i9);
    }

    public String o(int i8) {
        return this.f1506b.getString(i8);
    }

    public CharSequence p(int i8) {
        return this.f1506b.getText(i8);
    }

    public CharSequence[] q(int i8) {
        return this.f1506b.getTextArray(i8);
    }

    public TypedArray r() {
        return this.f1506b;
    }

    public boolean s(int i8) {
        return this.f1506b.hasValue(i8);
    }

    public void w() {
        this.f1506b.recycle();
    }
}
