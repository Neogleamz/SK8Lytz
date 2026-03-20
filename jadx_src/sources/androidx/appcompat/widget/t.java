package androidx.appcompat.widget;

import android.graphics.Insets;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Build;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class t {

    /* renamed from: a  reason: collision with root package name */
    private static final int[] f1604a = {16842912};

    /* renamed from: b  reason: collision with root package name */
    private static final int[] f1605b = new int[0];

    /* renamed from: c  reason: collision with root package name */
    public static final Rect f1606c = new Rect();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a {

        /* renamed from: a  reason: collision with root package name */
        private static final boolean f1607a;

        /* renamed from: b  reason: collision with root package name */
        private static final Method f1608b;

        /* renamed from: c  reason: collision with root package name */
        private static final Field f1609c;

        /* renamed from: d  reason: collision with root package name */
        private static final Field f1610d;

        /* renamed from: e  reason: collision with root package name */
        private static final Field f1611e;

        /* renamed from: f  reason: collision with root package name */
        private static final Field f1612f;

        /* JADX WARN: Removed duplicated region for block: B:25:0x004b  */
        /* JADX WARN: Removed duplicated region for block: B:26:0x0058  */
        static {
            /*
                r0 = 1
                r1 = 0
                r2 = 0
                java.lang.String r3 = "android.graphics.Insets"
                java.lang.Class r3 = java.lang.Class.forName(r3)     // Catch: java.lang.NoSuchFieldException -> L3b java.lang.ClassNotFoundException -> L3f java.lang.NoSuchMethodException -> L43
                java.lang.Class<android.graphics.drawable.Drawable> r4 = android.graphics.drawable.Drawable.class
                java.lang.String r5 = "getOpticalInsets"
                java.lang.Class[] r6 = new java.lang.Class[r1]     // Catch: java.lang.NoSuchFieldException -> L3b java.lang.ClassNotFoundException -> L3f java.lang.NoSuchMethodException -> L43
                java.lang.reflect.Method r4 = r4.getMethod(r5, r6)     // Catch: java.lang.NoSuchFieldException -> L3b java.lang.ClassNotFoundException -> L3f java.lang.NoSuchMethodException -> L43
                java.lang.String r5 = "left"
                java.lang.reflect.Field r5 = r3.getField(r5)     // Catch: java.lang.NoSuchFieldException -> L35 java.lang.ClassNotFoundException -> L37 java.lang.NoSuchMethodException -> L39
                java.lang.String r6 = "top"
                java.lang.reflect.Field r6 = r3.getField(r6)     // Catch: java.lang.NoSuchFieldException -> L2f java.lang.ClassNotFoundException -> L31 java.lang.NoSuchMethodException -> L33
                java.lang.String r7 = "right"
                java.lang.reflect.Field r7 = r3.getField(r7)     // Catch: java.lang.Throwable -> L2d
                java.lang.String r8 = "bottom"
                java.lang.reflect.Field r3 = r3.getField(r8)     // Catch: java.lang.Throwable -> L47
                r8 = r0
                goto L49
            L2d:
                r7 = r2
                goto L47
            L2f:
                r6 = r2
                goto L46
            L31:
                r6 = r2
                goto L46
            L33:
                r6 = r2
                goto L46
            L35:
                r5 = r2
                goto L3d
            L37:
                r5 = r2
                goto L41
            L39:
                r5 = r2
                goto L45
            L3b:
                r4 = r2
                r5 = r4
            L3d:
                r6 = r5
                goto L46
            L3f:
                r4 = r2
                r5 = r4
            L41:
                r6 = r5
                goto L46
            L43:
                r4 = r2
                r5 = r4
            L45:
                r6 = r5
            L46:
                r7 = r6
            L47:
                r8 = r1
                r3 = r2
            L49:
                if (r8 == 0) goto L58
                androidx.appcompat.widget.t.a.f1608b = r4
                androidx.appcompat.widget.t.a.f1609c = r5
                androidx.appcompat.widget.t.a.f1610d = r6
                androidx.appcompat.widget.t.a.f1611e = r7
                androidx.appcompat.widget.t.a.f1612f = r3
                androidx.appcompat.widget.t.a.f1607a = r0
                goto L64
            L58:
                androidx.appcompat.widget.t.a.f1608b = r2
                androidx.appcompat.widget.t.a.f1609c = r2
                androidx.appcompat.widget.t.a.f1610d = r2
                androidx.appcompat.widget.t.a.f1611e = r2
                androidx.appcompat.widget.t.a.f1612f = r2
                androidx.appcompat.widget.t.a.f1607a = r1
            L64:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.t.a.<clinit>():void");
        }

        static Rect a(Drawable drawable) {
            if (Build.VERSION.SDK_INT < 29 && f1607a) {
                try {
                    Object invoke = f1608b.invoke(drawable, new Object[0]);
                    if (invoke != null) {
                        return new Rect(f1609c.getInt(invoke), f1610d.getInt(invoke), f1611e.getInt(invoke), f1612f.getInt(invoke));
                    }
                } catch (IllegalAccessException | InvocationTargetException unused) {
                }
            }
            return t.f1606c;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class b {
        static Insets a(Drawable drawable) {
            return drawable.getOpticalInsets();
        }
    }

    public static boolean a(Drawable drawable) {
        Drawable drawable2;
        int i8 = Build.VERSION.SDK_INT;
        if (i8 >= 17) {
            return true;
        }
        if (i8 >= 15 || !(drawable instanceof InsetDrawable)) {
            if (i8 >= 15 || !(drawable instanceof GradientDrawable)) {
                if (i8 >= 17 || !(drawable instanceof LayerDrawable)) {
                    if (!(drawable instanceof DrawableContainer)) {
                        if (drawable instanceof androidx.core.graphics.drawable.c) {
                            drawable2 = ((androidx.core.graphics.drawable.c) drawable).b();
                        } else if (drawable instanceof i.c) {
                            drawable2 = ((i.c) drawable).a();
                        } else if (drawable instanceof ScaleDrawable) {
                            drawable2 = ((ScaleDrawable) drawable).getDrawable();
                        }
                        return a(drawable2);
                    }
                    Drawable.ConstantState constantState = drawable.getConstantState();
                    if (constantState instanceof DrawableContainer.DrawableContainerState) {
                        for (Drawable drawable3 : ((DrawableContainer.DrawableContainerState) constantState).getChildren()) {
                            if (!a(drawable3)) {
                                return false;
                            }
                        }
                    }
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void b(Drawable drawable) {
        String name = drawable.getClass().getName();
        int i8 = Build.VERSION.SDK_INT;
        if (!(i8 == 21 && "android.graphics.drawable.VectorDrawable".equals(name)) && (i8 < 29 || i8 >= 31 || !"android.graphics.drawable.ColorStateListDrawable".equals(name))) {
            return;
        }
        c(drawable);
    }

    private static void c(Drawable drawable) {
        int[] state = drawable.getState();
        if (state == null || state.length == 0) {
            drawable.setState(f1604a);
        } else {
            drawable.setState(f1605b);
        }
        drawable.setState(state);
    }

    public static Rect d(Drawable drawable) {
        int i8 = Build.VERSION.SDK_INT;
        if (i8 < 29) {
            return i8 >= 18 ? a.a(androidx.core.graphics.drawable.a.q(drawable)) : f1606c;
        }
        Insets a9 = b.a(drawable);
        return new Rect(a9.left, a9.top, a9.right, a9.bottom);
    }

    public static PorterDuff.Mode e(int i8, PorterDuff.Mode mode) {
        if (i8 != 3) {
            if (i8 != 5) {
                if (i8 != 9) {
                    switch (i8) {
                        case 14:
                            return PorterDuff.Mode.MULTIPLY;
                        case 15:
                            return PorterDuff.Mode.SCREEN;
                        case 16:
                            return PorterDuff.Mode.ADD;
                        default:
                            return mode;
                    }
                }
                return PorterDuff.Mode.SRC_ATOP;
            }
            return PorterDuff.Mode.SRC_IN;
        }
        return PorterDuff.Mode.SRC_OVER;
    }
}
