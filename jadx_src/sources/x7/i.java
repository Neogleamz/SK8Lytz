package x7;

import android.graphics.drawable.Drawable;
import android.view.View;
import com.google.android.material.internal.s;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class i {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static d a(int i8) {
        return i8 != 0 ? i8 != 1 ? b() : new e() : new l();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static d b() {
        return new l();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static f c() {
        return new f();
    }

    public static void d(View view, float f5) {
        Drawable background = view.getBackground();
        if (background instanceof h) {
            ((h) background).Z(f5);
        }
    }

    public static void e(View view) {
        Drawable background = view.getBackground();
        if (background instanceof h) {
            f(view, (h) background);
        }
    }

    public static void f(View view, h hVar) {
        if (hVar.R()) {
            hVar.e0(s.g(view));
        }
    }
}
