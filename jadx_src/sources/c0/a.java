package c0;

import android.media.Image;
import android.media.ImageWriter;
import android.os.Build;
import android.view.Surface;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a {
    public static void a(ImageWriter imageWriter) {
        int i8 = Build.VERSION.SDK_INT;
        if (i8 >= 23) {
            b.a(imageWriter);
            return;
        }
        throw new RuntimeException("Unable to call close() on API " + i8 + ". Version 23 or higher required.");
    }

    public static Image b(ImageWriter imageWriter) {
        int i8 = Build.VERSION.SDK_INT;
        if (i8 >= 23) {
            return b.b(imageWriter);
        }
        throw new RuntimeException("Unable to call dequeueInputImage() on API " + i8 + ". Version 23 or higher required.");
    }

    public static ImageWriter c(Surface surface, int i8) {
        int i9 = Build.VERSION.SDK_INT;
        if (i9 >= 23) {
            return b.c(surface, i8);
        }
        throw new RuntimeException("Unable to call newInstance(Surface, int) on API " + i9 + ". Version 23 or higher required.");
    }

    public static ImageWriter d(Surface surface, int i8, int i9) {
        int i10 = Build.VERSION.SDK_INT;
        if (i10 >= 29) {
            return d.a(surface, i8, i9);
        }
        if (i10 >= 26) {
            return c.a(surface, i8, i9);
        }
        throw new RuntimeException("Unable to call newInstance(Surface, int, int) on API " + i10 + ". Version 26 or higher required.");
    }

    public static void e(ImageWriter imageWriter, Image image) {
        int i8 = Build.VERSION.SDK_INT;
        if (i8 >= 23) {
            b.d(imageWriter, image);
            return;
        }
        throw new RuntimeException("Unable to call queueInputImage() on API " + i8 + ". Version 23 or higher required.");
    }
}
