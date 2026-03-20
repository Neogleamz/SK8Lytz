package e3;

import android.content.Context;
import android.media.ExifInterface;
import android.util.Log;
import io.flutter.plugin.common.j;
import java.io.File;
import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class b implements j.c {

    /* renamed from: a  reason: collision with root package name */
    private final Context f19751a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public b(Context context) {
        this.f19751a = context;
    }

    private void a(String str, String str2) {
        try {
            ExifInterface exifInterface = new ExifInterface(str);
            ExifInterface exifInterface2 = new ExifInterface(str2);
            for (String str3 : Arrays.asList("FNumber", "ExposureTime", "ISOSpeedRatings", "GPSAltitude", "GPSAltitudeRef", "FocalLength", "GPSDateStamp", "WhiteBalance", "GPSProcessingMethod", "GPSTimeStamp", "DateTime", "Flash", "GPSLatitude", "GPSLatitudeRef", "GPSLongitude", "GPSLongitudeRef", "Make", "Model", "Orientation")) {
                try {
                    c(exifInterface, exifInterface2, str3);
                } catch (Exception e8) {
                    e = e8;
                    Log.e("FlutterNativeImagePlugin", "Error preserving Exif data on selected image: " + e);
                    return;
                }
            }
            exifInterface2.saveAttributes();
        } catch (Exception e9) {
            e = e9;
        }
    }

    private static String b(File file) {
        String name = file.getName();
        return name.indexOf(".") > 0 ? name.substring(0, name.lastIndexOf(".")) : name;
    }

    private void c(ExifInterface exifInterface, ExifInterface exifInterface2, String str) {
        if (exifInterface.getAttribute(str) != null) {
            exifInterface2.setAttribute(str, exifInterface.getAttribute(str));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:97:0x0216 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r11v4 */
    /* JADX WARN: Type inference failed for: r11v5, types: [java.io.OutputStream] */
    /* JADX WARN: Type inference failed for: r11v6 */
    /* JADX WARN: Type inference failed for: r12v8, types: [android.graphics.Bitmap] */
    /* JADX WARN: Type inference failed for: r13v1, types: [java.io.OutputStream, java.io.ByteArrayOutputStream] */
    /* JADX WARN: Type inference failed for: r5v2 */
    /* JADX WARN: Type inference failed for: r5v3 */
    /* JADX WARN: Type inference failed for: r5v5, types: [java.io.OutputStream] */
    /* JADX WARN: Type inference failed for: r5v6, types: [java.io.OutputStream] */
    /* JADX WARN: Type inference failed for: r5v8, types: [java.io.OutputStream, java.io.FileOutputStream] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void onMethodCall(io.flutter.plugin.common.i r17, io.flutter.plugin.common.j.d r18) {
        /*
            Method dump skipped, instructions count: 581
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: e3.b.onMethodCall(io.flutter.plugin.common.i, io.flutter.plugin.common.j$d):void");
    }
}
