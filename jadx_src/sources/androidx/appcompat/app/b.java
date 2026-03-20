package androidx.appcompat.app;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.lang.reflect.Method;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class b {

    /* renamed from: a  reason: collision with root package name */
    private static final int[] f650a = {16843531};

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a {

        /* renamed from: a  reason: collision with root package name */
        public Method f651a;

        /* renamed from: b  reason: collision with root package name */
        public Method f652b;

        /* renamed from: c  reason: collision with root package name */
        public ImageView f653c;

        a(Activity activity) {
            try {
                this.f651a = android.app.ActionBar.class.getDeclaredMethod("setHomeAsUpIndicator", Drawable.class);
                this.f652b = android.app.ActionBar.class.getDeclaredMethod("setHomeActionContentDescription", Integer.TYPE);
            } catch (NoSuchMethodException unused) {
                View findViewById = activity.findViewById(16908332);
                if (findViewById == null) {
                    return;
                }
                ViewGroup viewGroup = (ViewGroup) findViewById.getParent();
                if (viewGroup.getChildCount() != 2) {
                    return;
                }
                View childAt = viewGroup.getChildAt(0);
                childAt = childAt.getId() == 16908332 ? viewGroup.getChildAt(1) : childAt;
                if (childAt instanceof ImageView) {
                    this.f653c = (ImageView) childAt;
                }
            }
        }
    }

    public static Drawable a(Activity activity) {
        TypedArray obtainStyledAttributes = activity.obtainStyledAttributes(f650a);
        Drawable drawable = obtainStyledAttributes.getDrawable(0);
        obtainStyledAttributes.recycle();
        return drawable;
    }

    public static a b(a aVar, Activity activity, int i8) {
        if (aVar == null) {
            aVar = new a(activity);
        }
        if (aVar.f651a != null) {
            try {
                android.app.ActionBar actionBar = activity.getActionBar();
                aVar.f652b.invoke(actionBar, Integer.valueOf(i8));
                if (Build.VERSION.SDK_INT <= 19) {
                    actionBar.setSubtitle(actionBar.getSubtitle());
                }
            } catch (Exception e8) {
                Log.w("ActionBarDrawerToggleHC", "Couldn't set content description via JB-MR2 API", e8);
            }
        }
        return aVar;
    }

    public static a c(Activity activity, Drawable drawable, int i8) {
        a aVar = new a(activity);
        if (aVar.f651a != null) {
            try {
                android.app.ActionBar actionBar = activity.getActionBar();
                aVar.f651a.invoke(actionBar, drawable);
                aVar.f652b.invoke(actionBar, Integer.valueOf(i8));
            } catch (Exception e8) {
                Log.w("ActionBarDrawerToggleHC", "Couldn't set home-as-up indicator via JB-MR2 API", e8);
            }
        } else {
            ImageView imageView = aVar.f653c;
            if (imageView != null) {
                imageView.setImageDrawable(drawable);
            } else {
                Log.w("ActionBarDrawerToggleHC", "Couldn't set home-as-up indicator");
            }
        }
        return aVar;
    }
}
