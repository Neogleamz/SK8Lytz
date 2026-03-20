package androidx.appcompat.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextDirectionHeuristic;
import android.text.TextDirectionHeuristics;
import android.text.TextPaint;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class q {

    /* renamed from: l  reason: collision with root package name */
    private static final RectF f1570l = new RectF();
    @SuppressLint({"BanConcurrentHashMap"})

    /* renamed from: m  reason: collision with root package name */
    private static ConcurrentHashMap<String, Method> f1571m = new ConcurrentHashMap<>();
    @SuppressLint({"BanConcurrentHashMap"})

    /* renamed from: n  reason: collision with root package name */
    private static ConcurrentHashMap<String, Field> f1572n = new ConcurrentHashMap<>();

    /* renamed from: a  reason: collision with root package name */
    private int f1573a = 0;

    /* renamed from: b  reason: collision with root package name */
    private boolean f1574b = false;

    /* renamed from: c  reason: collision with root package name */
    private float f1575c = -1.0f;

    /* renamed from: d  reason: collision with root package name */
    private float f1576d = -1.0f;

    /* renamed from: e  reason: collision with root package name */
    private float f1577e = -1.0f;

    /* renamed from: f  reason: collision with root package name */
    private int[] f1578f = new int[0];

    /* renamed from: g  reason: collision with root package name */
    private boolean f1579g = false;

    /* renamed from: h  reason: collision with root package name */
    private TextPaint f1580h;

    /* renamed from: i  reason: collision with root package name */
    private final TextView f1581i;

    /* renamed from: j  reason: collision with root package name */
    private final Context f1582j;

    /* renamed from: k  reason: collision with root package name */
    private final f f1583k;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {
        static StaticLayout a(CharSequence charSequence, Layout.Alignment alignment, int i8, TextView textView, TextPaint textPaint) {
            return new StaticLayout(charSequence, textPaint, i8, alignment, textView.getLineSpacingMultiplier(), textView.getLineSpacingExtra(), textView.getIncludeFontPadding());
        }

        static int b(TextView textView) {
            return textView.getMaxLines();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {
        static boolean a(View view) {
            return view.isInLayout();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class c {
        static StaticLayout a(CharSequence charSequence, Layout.Alignment alignment, int i8, int i9, TextView textView, TextPaint textPaint, f fVar) {
            StaticLayout.Builder obtain = StaticLayout.Builder.obtain(charSequence, 0, charSequence.length(), textPaint, i8);
            StaticLayout.Builder hyphenationFrequency = obtain.setAlignment(alignment).setLineSpacing(textView.getLineSpacingExtra(), textView.getLineSpacingMultiplier()).setIncludePad(textView.getIncludeFontPadding()).setBreakStrategy(textView.getBreakStrategy()).setHyphenationFrequency(textView.getHyphenationFrequency());
            if (i9 == -1) {
                i9 = Integer.MAX_VALUE;
            }
            hyphenationFrequency.setMaxLines(i9);
            try {
                fVar.a(obtain, textView);
            } catch (ClassCastException unused) {
                Log.w("ACTVAutoSizeHelper", "Failed to obtain TextDirectionHeuristic, auto size may be incorrect");
            }
            return obtain.build();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class d extends f {
        d() {
        }

        @Override // androidx.appcompat.widget.q.f
        void a(StaticLayout.Builder builder, TextView textView) {
            builder.setTextDirection((TextDirectionHeuristic) q.p(textView, "getTextDirectionHeuristic", TextDirectionHeuristics.FIRSTSTRONG_LTR));
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class e extends d {
        e() {
        }

        @Override // androidx.appcompat.widget.q.d, androidx.appcompat.widget.q.f
        void a(StaticLayout.Builder builder, TextView textView) {
            builder.setTextDirection(textView.getTextDirectionHeuristic());
        }

        @Override // androidx.appcompat.widget.q.f
        boolean b(TextView textView) {
            return textView.isHorizontallyScrollable();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class f {
        f() {
        }

        void a(StaticLayout.Builder builder, TextView textView) {
        }

        boolean b(TextView textView) {
            return ((Boolean) q.p(textView, "getHorizontallyScrolling", Boolean.FALSE)).booleanValue();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public q(TextView textView) {
        this.f1581i = textView;
        this.f1582j = textView.getContext();
        int i8 = Build.VERSION.SDK_INT;
        this.f1583k = i8 >= 29 ? new e() : i8 >= 23 ? new d() : new f();
    }

    private boolean A(int i8, RectF rectF) {
        CharSequence transformation;
        CharSequence text = this.f1581i.getText();
        TransformationMethod transformationMethod = this.f1581i.getTransformationMethod();
        if (transformationMethod != null && (transformation = transformationMethod.getTransformation(text, this.f1581i)) != null) {
            text = transformation;
        }
        int b9 = Build.VERSION.SDK_INT >= 16 ? a.b(this.f1581i) : -1;
        o(i8);
        StaticLayout e8 = e(text, (Layout.Alignment) p(this.f1581i, "getLayoutAlignment", Layout.Alignment.ALIGN_NORMAL), Math.round(rectF.right), b9);
        return (b9 == -1 || (e8.getLineCount() <= b9 && e8.getLineEnd(e8.getLineCount() - 1) == text.length())) && ((float) e8.getHeight()) <= rectF.bottom;
    }

    private boolean B() {
        return !(this.f1581i instanceof AppCompatEditText);
    }

    private void C(float f5, float f8, float f9) {
        if (f5 <= 0.0f) {
            throw new IllegalArgumentException("Minimum auto-size text size (" + f5 + "px) is less or equal to (0px)");
        } else if (f8 <= f5) {
            throw new IllegalArgumentException("Maximum auto-size text size (" + f8 + "px) is less or equal to minimum auto-size text size (" + f5 + "px)");
        } else if (f9 <= 0.0f) {
            throw new IllegalArgumentException("The auto-size step granularity (" + f9 + "px) is less or equal to (0px)");
        } else {
            this.f1573a = 1;
            this.f1576d = f5;
            this.f1577e = f8;
            this.f1575c = f9;
            this.f1579g = false;
        }
    }

    private static <T> T a(Object obj, String str, T t8) {
        try {
            Field m8 = m(str);
            return m8 == null ? t8 : (T) m8.get(obj);
        } catch (IllegalAccessException e8) {
            Log.w("ACTVAutoSizeHelper", "Failed to access TextView#" + str + " member", e8);
            return t8;
        }
    }

    private int[] c(int[] iArr) {
        int length = iArr.length;
        if (length == 0) {
            return iArr;
        }
        Arrays.sort(iArr);
        ArrayList arrayList = new ArrayList();
        for (int i8 : iArr) {
            if (i8 > 0 && Collections.binarySearch(arrayList, Integer.valueOf(i8)) < 0) {
                arrayList.add(Integer.valueOf(i8));
            }
        }
        if (length == arrayList.size()) {
            return iArr;
        }
        int size = arrayList.size();
        int[] iArr2 = new int[size];
        for (int i9 = 0; i9 < size; i9++) {
            iArr2[i9] = ((Integer) arrayList.get(i9)).intValue();
        }
        return iArr2;
    }

    private void d() {
        this.f1573a = 0;
        this.f1576d = -1.0f;
        this.f1577e = -1.0f;
        this.f1575c = -1.0f;
        this.f1578f = new int[0];
        this.f1574b = false;
    }

    private StaticLayout f(CharSequence charSequence, Layout.Alignment alignment, int i8) {
        return new StaticLayout(charSequence, this.f1580h, i8, alignment, ((Float) a(this.f1581i, "mSpacingMult", Float.valueOf(1.0f))).floatValue(), ((Float) a(this.f1581i, "mSpacingAdd", Float.valueOf(0.0f))).floatValue(), ((Boolean) a(this.f1581i, "mIncludePad", Boolean.TRUE)).booleanValue());
    }

    private int g(RectF rectF) {
        int i8;
        int length = this.f1578f.length;
        if (length == 0) {
            throw new IllegalStateException("No available text sizes to choose from.");
        }
        int i9 = 0;
        int i10 = 1;
        int i11 = length - 1;
        while (true) {
            int i12 = i10;
            int i13 = i9;
            i9 = i12;
            while (i9 <= i11) {
                i8 = (i9 + i11) / 2;
                if (A(this.f1578f[i8], rectF)) {
                    break;
                }
                i13 = i8 - 1;
                i11 = i13;
            }
            return this.f1578f[i13];
            i10 = i8 + 1;
        }
    }

    private static Field m(String str) {
        try {
            Field field = f1572n.get(str);
            if (field == null && (field = TextView.class.getDeclaredField(str)) != null) {
                field.setAccessible(true);
                f1572n.put(str, field);
            }
            return field;
        } catch (NoSuchFieldException e8) {
            Log.w("ACTVAutoSizeHelper", "Failed to access TextView#" + str + " member", e8);
            return null;
        }
    }

    private static Method n(String str) {
        try {
            Method method = f1571m.get(str);
            if (method == null && (method = TextView.class.getDeclaredMethod(str, new Class[0])) != null) {
                method.setAccessible(true);
                f1571m.put(str, method);
            }
            return method;
        } catch (Exception e8) {
            Log.w("ACTVAutoSizeHelper", "Failed to retrieve TextView#" + str + "() method", e8);
            return null;
        }
    }

    static <T> T p(Object obj, String str, T t8) {
        try {
            return (T) n(str).invoke(obj, new Object[0]);
        } catch (Exception e8) {
            Log.w("ACTVAutoSizeHelper", "Failed to invoke TextView#" + str + "() method", e8);
            return t8;
        }
    }

    private void v(float f5) {
        if (f5 != this.f1581i.getPaint().getTextSize()) {
            this.f1581i.getPaint().setTextSize(f5);
            boolean a9 = Build.VERSION.SDK_INT >= 18 ? b.a(this.f1581i) : false;
            if (this.f1581i.getLayout() != null) {
                this.f1574b = false;
                try {
                    Method n8 = n("nullLayouts");
                    if (n8 != null) {
                        n8.invoke(this.f1581i, new Object[0]);
                    }
                } catch (Exception e8) {
                    Log.w("ACTVAutoSizeHelper", "Failed to invoke TextView#nullLayouts() method", e8);
                }
                if (a9) {
                    this.f1581i.forceLayout();
                } else {
                    this.f1581i.requestLayout();
                }
                this.f1581i.invalidate();
            }
        }
    }

    private boolean x() {
        if (B() && this.f1573a == 1) {
            if (!this.f1579g || this.f1578f.length == 0) {
                int floor = ((int) Math.floor((this.f1577e - this.f1576d) / this.f1575c)) + 1;
                int[] iArr = new int[floor];
                for (int i8 = 0; i8 < floor; i8++) {
                    iArr[i8] = Math.round(this.f1576d + (i8 * this.f1575c));
                }
                this.f1578f = c(iArr);
            }
            this.f1574b = true;
        } else {
            this.f1574b = false;
        }
        return this.f1574b;
    }

    private void y(TypedArray typedArray) {
        int length = typedArray.length();
        int[] iArr = new int[length];
        if (length > 0) {
            for (int i8 = 0; i8 < length; i8++) {
                iArr[i8] = typedArray.getDimensionPixelSize(i8, -1);
            }
            this.f1578f = c(iArr);
            z();
        }
    }

    private boolean z() {
        int[] iArr = this.f1578f;
        int length = iArr.length;
        boolean z4 = length > 0;
        this.f1579g = z4;
        if (z4) {
            this.f1573a = 1;
            this.f1576d = iArr[0];
            this.f1577e = iArr[length - 1];
            this.f1575c = -1.0f;
        }
        return z4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b() {
        if (q()) {
            if (this.f1574b) {
                if (this.f1581i.getMeasuredHeight() <= 0 || this.f1581i.getMeasuredWidth() <= 0) {
                    return;
                }
                int measuredWidth = this.f1583k.b(this.f1581i) ? 1048576 : (this.f1581i.getMeasuredWidth() - this.f1581i.getTotalPaddingLeft()) - this.f1581i.getTotalPaddingRight();
                int height = (this.f1581i.getHeight() - this.f1581i.getCompoundPaddingBottom()) - this.f1581i.getCompoundPaddingTop();
                if (measuredWidth <= 0 || height <= 0) {
                    return;
                }
                RectF rectF = f1570l;
                synchronized (rectF) {
                    rectF.setEmpty();
                    rectF.right = measuredWidth;
                    rectF.bottom = height;
                    float g8 = g(rectF);
                    if (g8 != this.f1581i.getTextSize()) {
                        w(0, g8);
                    }
                }
            }
            this.f1574b = true;
        }
    }

    StaticLayout e(CharSequence charSequence, Layout.Alignment alignment, int i8, int i9) {
        int i10 = Build.VERSION.SDK_INT;
        return i10 >= 23 ? c.a(charSequence, alignment, i8, i9, this.f1581i, this.f1580h, this.f1583k) : i10 >= 16 ? a.a(charSequence, alignment, i8, this.f1581i, this.f1580h) : f(charSequence, alignment, i8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int h() {
        return Math.round(this.f1577e);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int i() {
        return Math.round(this.f1576d);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int j() {
        return Math.round(this.f1575c);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int[] k() {
        return this.f1578f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int l() {
        return this.f1573a;
    }

    void o(int i8) {
        TextPaint textPaint = this.f1580h;
        if (textPaint == null) {
            this.f1580h = new TextPaint();
        } else {
            textPaint.reset();
        }
        this.f1580h.set(this.f1581i.getPaint());
        this.f1580h.setTextSize(i8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean q() {
        return B() && this.f1573a != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void r(AttributeSet attributeSet, int i8) {
        int resourceId;
        Context context = this.f1582j;
        int[] iArr = g.j.f20048i0;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, iArr, i8, 0);
        TextView textView = this.f1581i;
        androidx.core.view.c0.r0(textView, textView.getContext(), iArr, attributeSet, obtainStyledAttributes, i8, 0);
        int i9 = g.j.f20073n0;
        if (obtainStyledAttributes.hasValue(i9)) {
            this.f1573a = obtainStyledAttributes.getInt(i9, 0);
        }
        int i10 = g.j.f20068m0;
        float dimension = obtainStyledAttributes.hasValue(i10) ? obtainStyledAttributes.getDimension(i10, -1.0f) : -1.0f;
        int i11 = g.j.f20058k0;
        float dimension2 = obtainStyledAttributes.hasValue(i11) ? obtainStyledAttributes.getDimension(i11, -1.0f) : -1.0f;
        int i12 = g.j.f20053j0;
        float dimension3 = obtainStyledAttributes.hasValue(i12) ? obtainStyledAttributes.getDimension(i12, -1.0f) : -1.0f;
        int i13 = g.j.f20063l0;
        if (obtainStyledAttributes.hasValue(i13) && (resourceId = obtainStyledAttributes.getResourceId(i13, 0)) > 0) {
            TypedArray obtainTypedArray = obtainStyledAttributes.getResources().obtainTypedArray(resourceId);
            y(obtainTypedArray);
            obtainTypedArray.recycle();
        }
        obtainStyledAttributes.recycle();
        if (!B()) {
            this.f1573a = 0;
        } else if (this.f1573a == 1) {
            if (!this.f1579g) {
                DisplayMetrics displayMetrics = this.f1582j.getResources().getDisplayMetrics();
                if (dimension2 == -1.0f) {
                    dimension2 = TypedValue.applyDimension(2, 12.0f, displayMetrics);
                }
                if (dimension3 == -1.0f) {
                    dimension3 = TypedValue.applyDimension(2, 112.0f, displayMetrics);
                }
                if (dimension == -1.0f) {
                    dimension = 1.0f;
                }
                C(dimension2, dimension3, dimension);
            }
            x();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void s(int i8, int i9, int i10, int i11) {
        if (B()) {
            DisplayMetrics displayMetrics = this.f1582j.getResources().getDisplayMetrics();
            C(TypedValue.applyDimension(i11, i8, displayMetrics), TypedValue.applyDimension(i11, i9, displayMetrics), TypedValue.applyDimension(i11, i10, displayMetrics));
            if (x()) {
                b();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void t(int[] iArr, int i8) {
        if (B()) {
            int length = iArr.length;
            if (length > 0) {
                int[] iArr2 = new int[length];
                if (i8 == 0) {
                    iArr2 = Arrays.copyOf(iArr, length);
                } else {
                    DisplayMetrics displayMetrics = this.f1582j.getResources().getDisplayMetrics();
                    for (int i9 = 0; i9 < length; i9++) {
                        iArr2[i9] = Math.round(TypedValue.applyDimension(i8, iArr[i9], displayMetrics));
                    }
                }
                this.f1578f = c(iArr2);
                if (!z()) {
                    throw new IllegalArgumentException("None of the preset sizes is valid: " + Arrays.toString(iArr));
                }
            } else {
                this.f1579g = false;
            }
            if (x()) {
                b();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void u(int i8) {
        if (B()) {
            if (i8 == 0) {
                d();
            } else if (i8 != 1) {
                throw new IllegalArgumentException("Unknown auto-size text type: " + i8);
            } else {
                DisplayMetrics displayMetrics = this.f1582j.getResources().getDisplayMetrics();
                C(TypedValue.applyDimension(2, 12.0f, displayMetrics), TypedValue.applyDimension(2, 112.0f, displayMetrics), 1.0f);
                if (x()) {
                    b();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void w(int i8, float f5) {
        Context context = this.f1582j;
        v(TypedValue.applyDimension(i8, f5, (context == null ? Resources.getSystem() : context.getResources()).getDisplayMetrics()));
    }
}
