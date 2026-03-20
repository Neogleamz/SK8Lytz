package androidx.core.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.InsetDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import java.lang.reflect.Method;
import org.xmlpull.v1.XmlPullParser;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a {

    /* renamed from: a  reason: collision with root package name */
    private static Method f4724a;

    /* renamed from: b  reason: collision with root package name */
    private static boolean f4725b;

    /* renamed from: c  reason: collision with root package name */
    private static Method f4726c;

    /* renamed from: d  reason: collision with root package name */
    private static boolean f4727d;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: androidx.core.graphics.drawable.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class C0035a {
        static int a(Drawable drawable) {
            return drawable.getAlpha();
        }

        static Drawable b(DrawableContainer.DrawableContainerState drawableContainerState, int i8) {
            return drawableContainerState.getChild(i8);
        }

        static Drawable c(InsetDrawable insetDrawable) {
            return insetDrawable.getDrawable();
        }

        static boolean d(Drawable drawable) {
            return drawable.isAutoMirrored();
        }

        static void e(Drawable drawable, boolean z4) {
            drawable.setAutoMirrored(z4);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class b {
        static void a(Drawable drawable, Resources.Theme theme) {
            drawable.applyTheme(theme);
        }

        static boolean b(Drawable drawable) {
            return drawable.canApplyTheme();
        }

        static ColorFilter c(Drawable drawable) {
            return drawable.getColorFilter();
        }

        static void d(Drawable drawable, Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) {
            drawable.inflate(resources, xmlPullParser, attributeSet, theme);
        }

        static void e(Drawable drawable, float f5, float f8) {
            drawable.setHotspot(f5, f8);
        }

        static void f(Drawable drawable, int i8, int i9, int i10, int i11) {
            drawable.setHotspotBounds(i8, i9, i10, i11);
        }

        static void g(Drawable drawable, int i8) {
            drawable.setTint(i8);
        }

        static void h(Drawable drawable, ColorStateList colorStateList) {
            drawable.setTintList(colorStateList);
        }

        static void i(Drawable drawable, PorterDuff.Mode mode) {
            drawable.setTintMode(mode);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class c {
        static int a(Drawable drawable) {
            return drawable.getLayoutDirection();
        }

        static boolean b(Drawable drawable, int i8) {
            return drawable.setLayoutDirection(i8);
        }
    }

    public static void a(Drawable drawable, Resources.Theme theme) {
        if (Build.VERSION.SDK_INT >= 21) {
            b.a(drawable, theme);
        }
    }

    public static boolean b(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= 21) {
            return b.b(drawable);
        }
        return false;
    }

    public static void c(Drawable drawable) {
        DrawableContainer.DrawableContainerState drawableContainerState;
        Drawable b9;
        int i8 = Build.VERSION.SDK_INT;
        if (i8 >= 23 || i8 < 21) {
            drawable.clearColorFilter();
            return;
        }
        drawable.clearColorFilter();
        if (drawable instanceof InsetDrawable) {
            b9 = C0035a.c((InsetDrawable) drawable);
        } else if (!(drawable instanceof androidx.core.graphics.drawable.c)) {
            if (!(drawable instanceof DrawableContainer) || (drawableContainerState = (DrawableContainer.DrawableContainerState) ((DrawableContainer) drawable).getConstantState()) == null) {
                return;
            }
            int childCount = drawableContainerState.getChildCount();
            for (int i9 = 0; i9 < childCount; i9++) {
                Drawable b10 = C0035a.b(drawableContainerState, i9);
                if (b10 != null) {
                    c(b10);
                }
            }
            return;
        } else {
            b9 = ((androidx.core.graphics.drawable.c) drawable).b();
        }
        c(b9);
    }

    public static int d(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= 19) {
            return C0035a.a(drawable);
        }
        return 0;
    }

    public static ColorFilter e(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= 21) {
            return b.c(drawable);
        }
        return null;
    }

    public static int f(Drawable drawable) {
        int i8 = Build.VERSION.SDK_INT;
        if (i8 >= 23) {
            return c.a(drawable);
        }
        if (i8 >= 17) {
            if (!f4727d) {
                try {
                    Method declaredMethod = Drawable.class.getDeclaredMethod("getLayoutDirection", new Class[0]);
                    f4726c = declaredMethod;
                    declaredMethod.setAccessible(true);
                } catch (NoSuchMethodException e8) {
                    Log.i("DrawableCompat", "Failed to retrieve getLayoutDirection() method", e8);
                }
                f4727d = true;
            }
            Method method = f4726c;
            if (method != null) {
                try {
                    return ((Integer) method.invoke(drawable, new Object[0])).intValue();
                } catch (Exception e9) {
                    Log.i("DrawableCompat", "Failed to invoke getLayoutDirection() via reflection", e9);
                    f4726c = null;
                }
            }
        }
        return 0;
    }

    public static void g(Drawable drawable, Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) {
        if (Build.VERSION.SDK_INT >= 21) {
            b.d(drawable, resources, xmlPullParser, attributeSet, theme);
        } else {
            drawable.inflate(resources, xmlPullParser, attributeSet);
        }
    }

    public static boolean h(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= 19) {
            return C0035a.d(drawable);
        }
        return false;
    }

    @Deprecated
    public static void i(Drawable drawable) {
        drawable.jumpToCurrentState();
    }

    public static void j(Drawable drawable, boolean z4) {
        if (Build.VERSION.SDK_INT >= 19) {
            C0035a.e(drawable, z4);
        }
    }

    public static void k(Drawable drawable, float f5, float f8) {
        if (Build.VERSION.SDK_INT >= 21) {
            b.e(drawable, f5, f8);
        }
    }

    public static void l(Drawable drawable, int i8, int i9, int i10, int i11) {
        if (Build.VERSION.SDK_INT >= 21) {
            b.f(drawable, i8, i9, i10, i11);
        }
    }

    public static boolean m(Drawable drawable, int i8) {
        int i9 = Build.VERSION.SDK_INT;
        if (i9 >= 23) {
            return c.b(drawable, i8);
        }
        if (i9 >= 17) {
            if (!f4725b) {
                try {
                    Method declaredMethod = Drawable.class.getDeclaredMethod("setLayoutDirection", Integer.TYPE);
                    f4724a = declaredMethod;
                    declaredMethod.setAccessible(true);
                } catch (NoSuchMethodException e8) {
                    Log.i("DrawableCompat", "Failed to retrieve setLayoutDirection(int) method", e8);
                }
                f4725b = true;
            }
            Method method = f4724a;
            if (method != null) {
                try {
                    method.invoke(drawable, Integer.valueOf(i8));
                    return true;
                } catch (Exception e9) {
                    Log.i("DrawableCompat", "Failed to invoke setLayoutDirection(int) via reflection", e9);
                    f4724a = null;
                }
            }
        }
        return false;
    }

    public static void n(Drawable drawable, int i8) {
        if (Build.VERSION.SDK_INT >= 21) {
            b.g(drawable, i8);
        } else if (drawable instanceof androidx.core.graphics.drawable.b) {
            ((androidx.core.graphics.drawable.b) drawable).setTint(i8);
        }
    }

    public static void o(Drawable drawable, ColorStateList colorStateList) {
        if (Build.VERSION.SDK_INT >= 21) {
            b.h(drawable, colorStateList);
        } else if (drawable instanceof androidx.core.graphics.drawable.b) {
            ((androidx.core.graphics.drawable.b) drawable).setTintList(colorStateList);
        }
    }

    public static void p(Drawable drawable, PorterDuff.Mode mode) {
        if (Build.VERSION.SDK_INT >= 21) {
            b.i(drawable, mode);
        } else if (drawable instanceof androidx.core.graphics.drawable.b) {
            ((androidx.core.graphics.drawable.b) drawable).setTintMode(mode);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T extends Drawable> T q(Drawable drawable) {
        return drawable instanceof androidx.core.graphics.drawable.c ? (T) ((androidx.core.graphics.drawable.c) drawable).b() : drawable;
    }

    public static Drawable r(Drawable drawable) {
        int i8 = Build.VERSION.SDK_INT;
        return i8 >= 23 ? drawable : i8 >= 21 ? !(drawable instanceof androidx.core.graphics.drawable.b) ? new e(drawable) : drawable : !(drawable instanceof androidx.core.graphics.drawable.b) ? new d(drawable) : drawable;
    }
}
