package d3;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import com.daimajia.numberprogressbar.BuildConfig;
import io.flutter.plugin.common.i;
import io.flutter.plugin.common.j;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;
import org.json.JSONObject;
import yf.a;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a implements yf.a, j.c {

    /* renamed from: b  reason: collision with root package name */
    public static Context f19701b;

    /* renamed from: a  reason: collision with root package name */
    private String f19702a = "flutter_video_info";

    String a(int i8, MediaMetadataRetriever mediaMetadataRetriever) {
        try {
            return mediaMetadataRetriever.extractMetadata(i8);
        } catch (Exception unused) {
            return null;
        }
    }

    String b(String str) {
        double d8;
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        String str7;
        String str8;
        String str9;
        File file = new File(str);
        boolean exists = file.exists();
        String str10 = BuildConfig.FLAVOR;
        if (exists) {
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            try {
                mediaMetadataRetriever.setDataSource(f19701b, Uri.fromFile(file));
            } catch (Exception e8) {
                e8.printStackTrace();
            }
            str3 = a(3, mediaMetadataRetriever);
            str4 = a(5, mediaMetadataRetriever);
            try {
                str4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new SimpleDateFormat("yyyyMMdd'T'HHmmss.SSS", Locale.getDefault()).parse(str4));
            } catch (Exception e9) {
                e9.printStackTrace();
            }
            String a9 = a(12, mediaMetadataRetriever);
            str5 = a(23, mediaMetadataRetriever);
            str6 = a(25, mediaMetadataRetriever);
            str7 = a(9, mediaMetadataRetriever);
            str8 = a(18, mediaMetadataRetriever);
            str9 = a(19, mediaMetadataRetriever);
            d8 = file.length();
            str2 = Build.VERSION.SDK_INT >= 17 ? a(24, mediaMetadataRetriever) : null;
            try {
                mediaMetadataRetriever.release();
            } catch (Exception e10) {
                e10.printStackTrace();
            }
            str10 = a9;
        } else {
            d8 = 0.0d;
            str2 = BuildConfig.FLAVOR;
            str3 = str2;
            str4 = str3;
            str5 = str4;
            str6 = str5;
            str7 = str6;
            str8 = str7;
            str9 = str8;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("path", str);
            jSONObject.put("mimetype", str10);
            jSONObject.put("author", str3);
            jSONObject.put("date", str4);
            jSONObject.put("width", str8);
            jSONObject.put("height", str9);
            jSONObject.put("location", str5);
            jSONObject.put("framerate", str6);
            jSONObject.put("duration", str7);
            jSONObject.put("filesize", d8);
            jSONObject.put("orientation", str2);
            jSONObject.put("isfileexist", exists);
        } catch (Exception e11) {
            e11.printStackTrace();
        }
        return jSONObject.toString();
    }

    public void onAttachedToEngine(a.b bVar) {
        new j(bVar.b(), "flutter_video_info").e(new a());
        f19701b = bVar.a();
    }

    public void onDetachedFromEngine(a.b bVar) {
    }

    public void onMethodCall(i iVar, j.d dVar) {
        if (iVar.a.equals("getVidInfo")) {
            dVar.success(b((String) iVar.a("path")));
        } else {
            dVar.b();
        }
    }
}
