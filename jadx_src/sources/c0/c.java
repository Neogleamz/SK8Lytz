package c0;

import android.media.ImageWriter;
import android.os.Build;
import android.util.Log;
import android.view.Surface;
import androidx.core.util.h;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class c {

    /* renamed from: a  reason: collision with root package name */
    private static Method f8208a;

    static {
        try {
            Class cls = Integer.TYPE;
            f8208a = ImageWriter.class.getMethod("newInstance", Surface.class, cls, cls);
        } catch (NoSuchMethodException e8) {
            Log.i("ImageWriterCompatApi26", "Unable to initialize via reflection.", e8);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ImageWriter a(Surface surface, int i8, int i9) {
        Throwable th = null;
        if (Build.VERSION.SDK_INT >= 26) {
            try {
                return (ImageWriter) h.h(f8208a.invoke(null, surface, Integer.valueOf(i8), Integer.valueOf(i9)));
            } catch (IllegalAccessException | InvocationTargetException e8) {
                th = e8;
            }
        }
        throw new RuntimeException("Unable to invoke newInstance(Surface, int, int) via reflection.", th);
    }
}
