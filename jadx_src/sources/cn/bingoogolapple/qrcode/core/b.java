package cn.bingoogolapple.qrcode.core;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.view.WindowManager;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class b {

    /* renamed from: a  reason: collision with root package name */
    private final Context f8546a;

    /* renamed from: b  reason: collision with root package name */
    private Point f8547b;

    /* renamed from: c  reason: collision with root package name */
    private Point f8548c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public b(Context context) {
        this.f8546a = context;
    }

    private void b(Camera camera, boolean z4) {
        Camera.Parameters parameters = camera.getParameters();
        String d8 = z4 ? d(parameters.getSupportedFlashModes(), "torch", "on") : d(parameters.getSupportedFlashModes(), "off");
        if (d8 != null) {
            parameters.setFlashMode(d8);
        }
        camera.setParameters(parameters);
    }

    private static Point c(List<Camera.Size> list, Point point) {
        Iterator<Camera.Size> it = list.iterator();
        int i8 = 0;
        int i9 = Integer.MAX_VALUE;
        int i10 = 0;
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Camera.Size next = it.next();
            int i11 = next.width;
            int i12 = next.height;
            int abs = Math.abs(i11 - point.x) + Math.abs(i12 - point.y);
            if (abs == 0) {
                i10 = i12;
                i8 = i11;
                break;
            } else if (abs < i9) {
                i10 = i12;
                i8 = i11;
                i9 = abs;
            }
        }
        if (i8 <= 0 || i10 <= 0) {
            return null;
        }
        return new Point(i8, i10);
    }

    private static String d(Collection<String> collection, String... strArr) {
        if (collection != null) {
            for (String str : strArr) {
                if (collection.contains(str)) {
                    return str;
                }
            }
        }
        return null;
    }

    private int f() {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        int i8 = 0;
        Camera.getCameraInfo(0, cameraInfo);
        WindowManager windowManager = (WindowManager) this.f8546a.getSystemService("window");
        if (windowManager == null) {
            return 0;
        }
        int rotation = windowManager.getDefaultDisplay().getRotation();
        if (rotation != 0) {
            if (rotation == 1) {
                i8 = 90;
            } else if (rotation == 2) {
                i8 = 180;
            } else if (rotation == 3) {
                i8 = 270;
            }
        }
        int i9 = cameraInfo.facing;
        int i10 = cameraInfo.orientation;
        return (i9 == 1 ? 360 - ((i10 + i8) % 360) : (i10 - i8) + 360) % 360;
    }

    private static Point g(Camera.Parameters parameters, Point point) {
        Point c9 = c(parameters.getSupportedPreviewSizes(), point);
        return c9 == null ? new Point((point.x >> 3) << 3, (point.y >> 3) << 3) : c9;
    }

    private int[] j(Camera camera, float f5) {
        int i8 = (int) (f5 * 1000.0f);
        int[] iArr = null;
        int i9 = Integer.MAX_VALUE;
        for (int[] iArr2 : camera.getParameters().getSupportedPreviewFpsRange()) {
            int abs = Math.abs(i8 - iArr2[0]) + Math.abs(i8 - iArr2[1]);
            if (abs < i9) {
                iArr = iArr2;
                i9 = abs;
            }
        }
        return iArr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(Camera camera) {
        b(camera, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Point e() {
        return this.f8547b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void h(Camera camera) {
        Point point;
        Point j8 = a.j(this.f8546a);
        Point point2 = new Point();
        point2.x = j8.x;
        point2.y = j8.y;
        if (a.m(this.f8546a)) {
            point2.x = j8.y;
            point2.y = j8.x;
        }
        this.f8548c = g(camera.getParameters(), point2);
        if (a.m(this.f8546a)) {
            Point point3 = this.f8548c;
            point = new Point(point3.y, point3.x);
        } else {
            point = this.f8548c;
        }
        this.f8547b = point;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void i(Camera camera) {
        b(camera, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void k(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        Point point = this.f8548c;
        parameters.setPreviewSize(point.x, point.y);
        int[] j8 = j(camera, 60.0f);
        if (j8 != null) {
            parameters.setPreviewFpsRange(j8[0], j8[1]);
        }
        camera.setDisplayOrientation(f());
        camera.setParameters(parameters);
    }
}
