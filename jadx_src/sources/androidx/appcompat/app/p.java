package androidx.appcompat.app;

import android.content.res.Resources;
import android.os.Build;
import android.util.Log;
import android.util.LongSparseArray;
import java.lang.reflect.Field;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class p {

    /* renamed from: a  reason: collision with root package name */
    private static Field f690a;

    /* renamed from: b  reason: collision with root package name */
    private static boolean f691b;

    /* renamed from: c  reason: collision with root package name */
    private static Class<?> f692c;

    /* renamed from: d  reason: collision with root package name */
    private static boolean f693d;

    /* renamed from: e  reason: collision with root package name */
    private static Field f694e;

    /* renamed from: f  reason: collision with root package name */
    private static boolean f695f;

    /* renamed from: g  reason: collision with root package name */
    private static Field f696g;

    /* renamed from: h  reason: collision with root package name */
    private static boolean f697h;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {
        static void a(LongSparseArray longSparseArray) {
            longSparseArray.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(Resources resources) {
        int i8 = Build.VERSION.SDK_INT;
        if (i8 >= 28) {
            return;
        }
        if (i8 >= 24) {
            d(resources);
        } else if (i8 >= 23) {
            c(resources);
        } else if (i8 >= 21) {
            b(resources);
        }
    }

    private static void b(Resources resources) {
        if (!f691b) {
            try {
                Field declaredField = Resources.class.getDeclaredField("mDrawableCache");
                f690a = declaredField;
                declaredField.setAccessible(true);
            } catch (NoSuchFieldException e8) {
                Log.e("ResourcesFlusher", "Could not retrieve Resources#mDrawableCache field", e8);
            }
            f691b = true;
        }
        Field field = f690a;
        if (field != null) {
            Map map = null;
            try {
                map = (Map) field.get(resources);
            } catch (IllegalAccessException e9) {
                Log.e("ResourcesFlusher", "Could not retrieve value from Resources#mDrawableCache", e9);
            }
            if (map != null) {
                map.clear();
            }
        }
    }

    private static void c(Resources resources) {
        if (!f691b) {
            try {
                Field declaredField = Resources.class.getDeclaredField("mDrawableCache");
                f690a = declaredField;
                declaredField.setAccessible(true);
            } catch (NoSuchFieldException e8) {
                Log.e("ResourcesFlusher", "Could not retrieve Resources#mDrawableCache field", e8);
            }
            f691b = true;
        }
        Object obj = null;
        Field field = f690a;
        if (field != null) {
            try {
                obj = field.get(resources);
            } catch (IllegalAccessException e9) {
                Log.e("ResourcesFlusher", "Could not retrieve value from Resources#mDrawableCache", e9);
            }
        }
        if (obj == null) {
            return;
        }
        e(obj);
    }

    private static void d(Resources resources) {
        Object obj;
        if (!f697h) {
            try {
                Field declaredField = Resources.class.getDeclaredField("mResourcesImpl");
                f696g = declaredField;
                declaredField.setAccessible(true);
            } catch (NoSuchFieldException e8) {
                Log.e("ResourcesFlusher", "Could not retrieve Resources#mResourcesImpl field", e8);
            }
            f697h = true;
        }
        Field field = f696g;
        if (field == null) {
            return;
        }
        Object obj2 = null;
        try {
            obj = field.get(resources);
        } catch (IllegalAccessException e9) {
            Log.e("ResourcesFlusher", "Could not retrieve value from Resources#mResourcesImpl", e9);
            obj = null;
        }
        if (obj == null) {
            return;
        }
        if (!f691b) {
            try {
                Field declaredField2 = obj.getClass().getDeclaredField("mDrawableCache");
                f690a = declaredField2;
                declaredField2.setAccessible(true);
            } catch (NoSuchFieldException e10) {
                Log.e("ResourcesFlusher", "Could not retrieve ResourcesImpl#mDrawableCache field", e10);
            }
            f691b = true;
        }
        Field field2 = f690a;
        if (field2 != null) {
            try {
                obj2 = field2.get(obj);
            } catch (IllegalAccessException e11) {
                Log.e("ResourcesFlusher", "Could not retrieve value from ResourcesImpl#mDrawableCache", e11);
            }
        }
        if (obj2 != null) {
            e(obj2);
        }
    }

    private static void e(Object obj) {
        if (!f693d) {
            try {
                f692c = Class.forName("android.content.res.ThemedResourceCache");
            } catch (ClassNotFoundException e8) {
                Log.e("ResourcesFlusher", "Could not find ThemedResourceCache class", e8);
            }
            f693d = true;
        }
        Class<?> cls = f692c;
        if (cls == null) {
            return;
        }
        if (!f695f) {
            try {
                Field declaredField = cls.getDeclaredField("mUnthemedEntries");
                f694e = declaredField;
                declaredField.setAccessible(true);
            } catch (NoSuchFieldException e9) {
                Log.e("ResourcesFlusher", "Could not retrieve ThemedResourceCache#mUnthemedEntries field", e9);
            }
            f695f = true;
        }
        Field field = f694e;
        if (field == null) {
            return;
        }
        LongSparseArray longSparseArray = null;
        try {
            longSparseArray = (LongSparseArray) field.get(obj);
        } catch (IllegalAccessException e10) {
            Log.e("ResourcesFlusher", "Could not retrieve value from ThemedResourceCache#mUnthemedEntries", e10);
        }
        if (longSparseArray != null) {
            a.a(longSparseArray);
        }
    }
}
