package f3;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextPaint;
import cj.q;
import io.flutter.plugin.common.i;
import io.flutter.plugin.common.j;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import kotlin.Pair;
import kotlin.collections.j0;
import kotlin.jvm.internal.p;
import v2.a;
import vj.e;
import yf.a;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a implements yf.a, j.c {

    /* renamed from: a  reason: collision with root package name */
    private j f19842a;

    private final Map<String, Object> a(String str, int i8, int i9, float f5, Typeface typeface) {
        TextPaint textPaint = new TextPaint();
        textPaint.setColor(i9);
        textPaint.setAntiAlias(false);
        textPaint.setTextSize(i8);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTypeface(typeface);
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        List<String> X = e.X(str, new String[]{"\n"}, false, 0, 6, (Object) null);
        float f8 = fontMetrics.descent - fontMetrics.top;
        float f9 = (1.1f * f8) + f5;
        Iterator it = X.iterator();
        if (it.hasNext()) {
            float measureText = textPaint.measureText((String) it.next());
            while (it.hasNext()) {
                measureText = Math.max(measureText, textPaint.measureText((String) it.next()));
            }
            Bitmap createBitmap = Bitmap.createBitmap((int) measureText, oj.a.a(f8 + ((1 + f9) * (X.size() - 1))), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            float abs = Math.abs(fontMetrics.top);
            for (String str2 : X) {
                canvas.drawText(str2, 0.0f, abs, textPaint);
                abs += f9;
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            createBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            return j0.i(new Pair[]{q.a("data", byteArrayOutputStream.toByteArray()), q.a("width", Integer.valueOf(createBitmap.getWidth())), q.a("height", Integer.valueOf(createBitmap.getHeight()))});
        }
        throw new NoSuchElementException();
    }

    public void onAttachedToEngine(a.b bVar) {
        p.e(bVar, "flutterPluginBinding");
        j jVar = new j(bVar.b(), "native_font_data");
        this.f19842a = jVar;
        jVar.e(this);
    }

    public void onDetachedFromEngine(a.b bVar) {
        p.e(bVar, "binding");
        j jVar = this.f19842a;
        if (jVar == null) {
            p.t("channel");
            jVar = null;
        }
        jVar.e((j.c) null);
    }

    @SuppressLint({"SdCardPath"})
    public void onMethodCall(i iVar, j.d dVar) {
        int i8;
        boolean z4;
        boolean z8;
        int i9;
        Typeface c9;
        Object a9;
        p.e(iVar, "call");
        p.e(dVar, "result");
        String str = iVar.a;
        if (p.a(str, "getPlatformVersion")) {
            a9 = "Android " + Build.VERSION.RELEASE;
        } else if (!p.a(str, "getTextData")) {
            dVar.b();
            return;
        } else {
            Object obj = iVar.b;
            p.c(obj, "null cannot be cast to non-null type kotlin.collections.Map<*, *>");
            Map map = (Map) obj;
            Object obj2 = map.get("text");
            p.c(obj2, "null cannot be cast to non-null type kotlin.String");
            String str2 = (String) obj2;
            Object obj3 = map.get("fontSize");
            p.c(obj3, "null cannot be cast to non-null type kotlin.Int");
            int intValue = ((Integer) obj3).intValue();
            Object obj4 = map.get("fontColor");
            p.c(obj4, "null cannot be cast to non-null type kotlin.Long");
            long longValue = ((Long) obj4).longValue();
            Object obj5 = map.get("isBold");
            p.c(obj5, "null cannot be cast to non-null type kotlin.Boolean");
            boolean booleanValue = ((Boolean) obj5).booleanValue();
            Object obj6 = map.get("isCloud");
            p.c(obj6, "null cannot be cast to non-null type kotlin.Boolean");
            boolean booleanValue2 = ((Boolean) obj6).booleanValue();
            Object obj7 = map.get("lineSpacing");
            p.c(obj7, "null cannot be cast to non-null type kotlin.Int");
            float intValue2 = ((Integer) obj7).intValue();
            Object obj8 = map.get("fontFamily");
            p.c(obj8, "null cannot be cast to non-null type kotlin.String");
            String str3 = (String) obj8;
            if (booleanValue2) {
                c9 = Typeface.createFromFile((String) map.get("fontUrl"));
                p.b(c9);
            } else {
                a.C0215a c0215a = v2.a.f23159a;
                if (booleanValue) {
                    i8 = 700;
                    z4 = false;
                    z8 = false;
                    i9 = 12;
                } else {
                    i8 = 0;
                    z4 = false;
                    z8 = false;
                    i9 = 14;
                }
                c9 = a.C0215a.c(c0215a, str3, i8, z4, z8, i9, null);
            }
            a9 = a(str2, intValue, (int) longValue, intValue2, c9);
        }
        dVar.success(a9);
    }
}
