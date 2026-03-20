package androidx.core.view;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class g0 {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a {
        static boolean a(ViewParent viewParent, View view, float f5, float f8, boolean z4) {
            return viewParent.onNestedFling(view, f5, f8, z4);
        }

        static boolean b(ViewParent viewParent, View view, float f5, float f8) {
            return viewParent.onNestedPreFling(view, f5, f8);
        }

        static void c(ViewParent viewParent, View view, int i8, int i9, int[] iArr) {
            viewParent.onNestedPreScroll(view, i8, i9, iArr);
        }

        static void d(ViewParent viewParent, View view, int i8, int i9, int i10, int i11) {
            viewParent.onNestedScroll(view, i8, i9, i10, i11);
        }

        static void e(ViewParent viewParent, View view, View view2, int i8) {
            viewParent.onNestedScrollAccepted(view, view2, i8);
        }

        static boolean f(ViewParent viewParent, View view, View view2, int i8) {
            return viewParent.onStartNestedScroll(view, view2, i8);
        }

        static void g(ViewParent viewParent, View view) {
            viewParent.onStopNestedScroll(view);
        }
    }

    public static boolean a(ViewParent viewParent, View view, float f5, float f8, boolean z4) {
        if (Build.VERSION.SDK_INT < 21) {
            if (viewParent instanceof t) {
                return ((t) viewParent).onNestedFling(view, f5, f8, z4);
            }
            return false;
        }
        try {
            return a.a(viewParent, view, f5, f8, z4);
        } catch (AbstractMethodError e8) {
            Log.e("ViewParentCompat", "ViewParent " + viewParent + " does not implement interface method onNestedFling", e8);
            return false;
        }
    }

    public static boolean b(ViewParent viewParent, View view, float f5, float f8) {
        if (Build.VERSION.SDK_INT < 21) {
            if (viewParent instanceof t) {
                return ((t) viewParent).onNestedPreFling(view, f5, f8);
            }
            return false;
        }
        try {
            return a.b(viewParent, view, f5, f8);
        } catch (AbstractMethodError e8) {
            Log.e("ViewParentCompat", "ViewParent " + viewParent + " does not implement interface method onNestedPreFling", e8);
            return false;
        }
    }

    public static void c(ViewParent viewParent, View view, int i8, int i9, int[] iArr, int i10) {
        if (viewParent instanceof r) {
            ((r) viewParent).o(view, i8, i9, iArr, i10);
        } else if (i10 == 0) {
            if (Build.VERSION.SDK_INT < 21) {
                if (viewParent instanceof t) {
                    ((t) viewParent).onNestedPreScroll(view, i8, i9, iArr);
                    return;
                }
                return;
            }
            try {
                a.c(viewParent, view, i8, i9, iArr);
            } catch (AbstractMethodError e8) {
                Log.e("ViewParentCompat", "ViewParent " + viewParent + " does not implement interface method onNestedPreScroll", e8);
            }
        }
    }

    public static void d(ViewParent viewParent, View view, int i8, int i9, int i10, int i11, int i12, int[] iArr) {
        if (viewParent instanceof s) {
            ((s) viewParent).j(view, i8, i9, i10, i11, i12, iArr);
            return;
        }
        iArr[0] = iArr[0] + i10;
        iArr[1] = iArr[1] + i11;
        if (viewParent instanceof r) {
            ((r) viewParent).k(view, i8, i9, i10, i11, i12);
        } else if (i12 == 0) {
            if (Build.VERSION.SDK_INT < 21) {
                if (viewParent instanceof t) {
                    ((t) viewParent).onNestedScroll(view, i8, i9, i10, i11);
                    return;
                }
                return;
            }
            try {
                a.d(viewParent, view, i8, i9, i10, i11);
            } catch (AbstractMethodError e8) {
                Log.e("ViewParentCompat", "ViewParent " + viewParent + " does not implement interface method onNestedScroll", e8);
            }
        }
    }

    public static void e(ViewParent viewParent, View view, View view2, int i8, int i9) {
        if (viewParent instanceof r) {
            ((r) viewParent).m(view, view2, i8, i9);
        } else if (i9 == 0) {
            if (Build.VERSION.SDK_INT < 21) {
                if (viewParent instanceof t) {
                    ((t) viewParent).onNestedScrollAccepted(view, view2, i8);
                    return;
                }
                return;
            }
            try {
                a.e(viewParent, view, view2, i8);
            } catch (AbstractMethodError e8) {
                Log.e("ViewParentCompat", "ViewParent " + viewParent + " does not implement interface method onNestedScrollAccepted", e8);
            }
        }
    }

    public static boolean f(ViewParent viewParent, View view, View view2, int i8, int i9) {
        if (viewParent instanceof r) {
            return ((r) viewParent).l(view, view2, i8, i9);
        }
        if (i9 == 0) {
            if (Build.VERSION.SDK_INT < 21) {
                if (viewParent instanceof t) {
                    return ((t) viewParent).onStartNestedScroll(view, view2, i8);
                }
                return false;
            }
            try {
                return a.f(viewParent, view, view2, i8);
            } catch (AbstractMethodError e8) {
                Log.e("ViewParentCompat", "ViewParent " + viewParent + " does not implement interface method onStartNestedScroll", e8);
                return false;
            }
        }
        return false;
    }

    public static void g(ViewParent viewParent, View view, int i8) {
        if (viewParent instanceof r) {
            ((r) viewParent).n(view, i8);
        } else if (i8 == 0) {
            if (Build.VERSION.SDK_INT < 21) {
                if (viewParent instanceof t) {
                    ((t) viewParent).onStopNestedScroll(view);
                    return;
                }
                return;
            }
            try {
                a.g(viewParent, view);
            } catch (AbstractMethodError e8) {
                Log.e("ViewParentCompat", "ViewParent " + viewParent + " does not implement interface method onStopNestedScroll", e8);
            }
        }
    }
}
