package androidx.camera.camera2.internal;

import android.content.Context;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.util.Size;
import android.view.Display;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class x1 {

    /* renamed from: d  reason: collision with root package name */
    private static final Size f2163d = new Size(1920, 1080);

    /* renamed from: e  reason: collision with root package name */
    private static final Object f2164e = new Object();

    /* renamed from: f  reason: collision with root package name */
    private static volatile x1 f2165f;

    /* renamed from: a  reason: collision with root package name */
    private final DisplayManager f2166a;

    /* renamed from: b  reason: collision with root package name */
    private volatile Size f2167b = null;

    /* renamed from: c  reason: collision with root package name */
    private final v.j f2168c = new v.j();

    private x1(Context context) {
        this.f2166a = (DisplayManager) context.getSystemService("display");
    }

    private Size a() {
        Point point = new Point();
        c().getRealSize(point);
        Size size = point.x > point.y ? new Size(point.x, point.y) : new Size(point.y, point.x);
        int width = size.getWidth() * size.getHeight();
        Size size2 = f2163d;
        if (width > size2.getWidth() * size2.getHeight()) {
            size = size2;
        }
        return this.f2168c.a(size);
    }

    public static x1 b(Context context) {
        if (f2165f == null) {
            synchronized (f2164e) {
                if (f2165f == null) {
                    f2165f = new x1(context);
                }
            }
        }
        return f2165f;
    }

    public Display c() {
        Display[] displays = this.f2166a.getDisplays();
        if (displays.length == 1) {
            return displays[0];
        }
        Display display = null;
        int i8 = -1;
        for (Display display2 : displays) {
            if (display2.getState() != 1) {
                Point point = new Point();
                display2.getRealSize(point);
                int i9 = point.x;
                int i10 = point.y;
                if (i9 * i10 > i8) {
                    display = display2;
                    i8 = i9 * i10;
                }
            }
        }
        if (display != null) {
            return display;
        }
        throw new IllegalArgumentException("No display can be found from the input display manager!");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Size d() {
        if (this.f2167b != null) {
            return this.f2167b;
        }
        this.f2167b = a();
        return this.f2167b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e() {
        this.f2167b = a();
    }
}
