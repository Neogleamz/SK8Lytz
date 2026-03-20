package i3;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import com.daimajia.numberprogressbar.BuildConfig;
import io.flutter.plugin.common.j;
import java.io.File;
import kj.f;
import kotlin.jvm.internal.p;
import org.json.JSONObject;
import vj.e;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b {

    /* renamed from: a  reason: collision with root package name */
    private final String f20449a;

    public b(String str) {
        p.e(str, "channelName");
        this.f20449a = str;
    }

    public final void a(Context context, j.d dVar) {
        p.e(context, "context");
        p.e(dVar, "result");
        File externalFilesDir = context.getExternalFilesDir("video_compress");
        dVar.success(externalFilesDir != null ? Boolean.valueOf(f.e(externalFilesDir)) : null);
    }

    public final void b(File file) {
        p.e(file, "file");
        if (file.exists()) {
            file.delete();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0040  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0059  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final android.graphics.Bitmap c(java.lang.String r5, long r6, io.flutter.plugin.common.j.d r8) {
        /*
            r4 = this;
            java.lang.String r0 = "Assume this is a corrupt video file"
            java.lang.String r1 = "Ignore failures while cleaning up"
            java.lang.String r2 = "path"
            kotlin.jvm.internal.p.e(r5, r2)
            java.lang.String r2 = "result"
            kotlin.jvm.internal.p.e(r8, r2)
            android.media.MediaMetadataRetriever r2 = new android.media.MediaMetadataRetriever
            r2.<init>()
            r3 = 0
            r2.setDataSource(r5)     // Catch: java.lang.Throwable -> L27 java.lang.RuntimeException -> L29 java.lang.IllegalArgumentException -> L38
            r5 = 2
            android.graphics.Bitmap r5 = r2.getFrameAtTime(r6, r5)     // Catch: java.lang.Throwable -> L27 java.lang.RuntimeException -> L29 java.lang.IllegalArgumentException -> L38
            r2.release()     // Catch: java.lang.RuntimeException -> L20
            goto L25
        L20:
            java.lang.String r6 = r4.f20449a
            r8.a(r6, r1, r3)
        L25:
            r3 = r5
            goto L3e
        L27:
            r5 = move-exception
            goto L72
        L29:
            java.lang.String r5 = r4.f20449a     // Catch: java.lang.Throwable -> L27
            r8.a(r5, r0, r3)     // Catch: java.lang.Throwable -> L27
        L2e:
            r2.release()     // Catch: java.lang.RuntimeException -> L32
            goto L3e
        L32:
            java.lang.String r5 = r4.f20449a
            r8.a(r5, r1, r3)
            goto L3e
        L38:
            java.lang.String r5 = r4.f20449a     // Catch: java.lang.Throwable -> L27
            r8.a(r5, r0, r3)     // Catch: java.lang.Throwable -> L27
            goto L2e
        L3e:
            if (r3 != 0) goto L46
            r5 = 0
            java.lang.Integer[] r5 = new java.lang.Integer[r5]
            r8.success(r5)
        L46:
            kotlin.jvm.internal.p.b(r3)
            int r5 = r3.getWidth()
            int r6 = r3.getHeight()
            int r7 = java.lang.Math.max(r5, r6)
            r8 = 512(0x200, float:7.175E-43)
            if (r7 <= r8) goto L6e
            r8 = 1140850688(0x44000000, float:512.0)
            float r7 = (float) r7
            float r8 = r8 / r7
            float r5 = (float) r5
            float r5 = r5 * r8
            int r5 = java.lang.Math.round(r5)
            float r6 = (float) r6
            float r8 = r8 * r6
            int r6 = java.lang.Math.round(r8)
            r7 = 1
            android.graphics.Bitmap r3 = android.graphics.Bitmap.createScaledBitmap(r3, r5, r6, r7)
        L6e:
            kotlin.jvm.internal.p.b(r3)
            return r3
        L72:
            r2.release()     // Catch: java.lang.RuntimeException -> L76
            goto L7b
        L76:
            java.lang.String r6 = r4.f20449a
            r8.a(r6, r1, r3)
        L7b:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: i3.b.c(java.lang.String, long, io.flutter.plugin.common.j$d):android.graphics.Bitmap");
    }

    public final JSONObject d(Context context, String str) {
        p.e(context, "context");
        p.e(str, "path");
        File file = new File(str);
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(context, Uri.fromFile(file));
        String extractMetadata = mediaMetadataRetriever.extractMetadata(9);
        String extractMetadata2 = mediaMetadataRetriever.extractMetadata(7);
        String str2 = BuildConfig.FLAVOR;
        if (extractMetadata2 == null) {
            extractMetadata2 = BuildConfig.FLAVOR;
        }
        String extractMetadata3 = mediaMetadataRetriever.extractMetadata(3);
        if (extractMetadata3 != null) {
            str2 = extractMetadata3;
        }
        String extractMetadata4 = mediaMetadataRetriever.extractMetadata(18);
        String extractMetadata5 = mediaMetadataRetriever.extractMetadata(19);
        long parseLong = Long.parseLong(extractMetadata);
        long parseLong2 = Long.parseLong(extractMetadata4);
        long parseLong3 = Long.parseLong(extractMetadata5);
        long length = file.length();
        String extractMetadata6 = Build.VERSION.SDK_INT >= 17 ? mediaMetadataRetriever.extractMetadata(24) : null;
        Integer g8 = extractMetadata6 != null ? e.g(extractMetadata6) : null;
        if (g8 != null && e(g8.intValue())) {
            parseLong3 = parseLong2;
            parseLong2 = parseLong3;
        }
        mediaMetadataRetriever.release();
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("path", str);
        jSONObject.put("title", extractMetadata2);
        jSONObject.put("author", str2);
        jSONObject.put("width", parseLong2);
        jSONObject.put("height", parseLong3);
        jSONObject.put("duration", parseLong);
        jSONObject.put("filesize", length);
        if (g8 != null) {
            jSONObject.put("orientation", g8.intValue());
        }
        return jSONObject;
    }

    public final boolean e(int i8) {
        return (i8 == 90 || i8 == 270) ? false : true;
    }
}
