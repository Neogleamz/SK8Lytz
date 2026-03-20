package androidx.appcompat.view;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.view.ViewConfiguration;
import g.j;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a {

    /* renamed from: a  reason: collision with root package name */
    private Context f756a;

    private a(Context context) {
        this.f756a = context;
    }

    public static a b(Context context) {
        return new a(context);
    }

    public boolean a() {
        return this.f756a.getApplicationInfo().targetSdkVersion < 14;
    }

    public int c() {
        return this.f756a.getResources().getDisplayMetrics().widthPixels / 2;
    }

    public int d() {
        Configuration configuration = this.f756a.getResources().getConfiguration();
        int i8 = configuration.screenWidthDp;
        int i9 = configuration.screenHeightDp;
        if (configuration.smallestScreenWidthDp > 600 || i8 > 600) {
            return 5;
        }
        if (i8 <= 960 || i9 <= 720) {
            if (i8 <= 720 || i9 <= 960) {
                if (i8 < 500) {
                    if (i8 <= 640 || i9 <= 480) {
                        if (i8 <= 480 || i9 <= 640) {
                            return i8 >= 360 ? 3 : 2;
                        }
                        return 4;
                    }
                    return 4;
                }
                return 4;
            }
            return 5;
        }
        return 5;
    }

    public int e() {
        return this.f756a.getResources().getDimensionPixelSize(g.d.f19897b);
    }

    public int f() {
        TypedArray obtainStyledAttributes = this.f756a.obtainStyledAttributes(null, j.f20003a, g.a.f19864c, 0);
        int layoutDimension = obtainStyledAttributes.getLayoutDimension(j.f20052j, 0);
        Resources resources = this.f756a.getResources();
        if (!g()) {
            layoutDimension = Math.min(layoutDimension, resources.getDimensionPixelSize(g.d.f19896a));
        }
        obtainStyledAttributes.recycle();
        return layoutDimension;
    }

    public boolean g() {
        return this.f756a.getResources().getBoolean(g.b.f19887a);
    }

    public boolean h() {
        if (Build.VERSION.SDK_INT >= 19) {
            return true;
        }
        return !ViewConfiguration.get(this.f756a).hasPermanentMenuKey();
    }
}
