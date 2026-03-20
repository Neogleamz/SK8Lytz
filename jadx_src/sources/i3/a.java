package i3;

import android.content.Context;
import android.graphics.Bitmap;
import io.flutter.plugin.common.j;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import kj.f;
import kotlin.collections.q;
import kotlin.jvm.internal.p;
import vj.e;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a {

    /* renamed from: a  reason: collision with root package name */
    private final b f20448a;

    public a(String str) {
        p.e(str, "channelName");
        this.f20448a = new b(str);
    }

    public final void a(String str, int i8, long j8, j.d dVar) {
        p.e(str, "path");
        p.e(dVar, "result");
        Bitmap c9 = this.f20448a.c(str, j8, dVar);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        c9.compress(Bitmap.CompressFormat.JPEG, i8, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        c9.recycle();
        p.b(byteArray);
        dVar.success(q.N(kotlin.collections.j.F(byteArray)));
    }

    public final void b(Context context, String str, int i8, long j8, j.d dVar) {
        p.e(context, "context");
        p.e(str, "path");
        p.e(dVar, "result");
        Bitmap c9 = this.f20448a.c(str, j8, dVar);
        File externalFilesDir = context.getExternalFilesDir("video_compress");
        if (externalFilesDir != null && !externalFilesDir.exists()) {
            externalFilesDir.mkdirs();
        }
        StringBuilder sb = new StringBuilder();
        String substring = str.substring(e.K(str, '/', 0, false, 6, (Object) null), e.K(str, '.', 0, false, 6, (Object) null));
        p.d(substring, "this as java.lang.String…ing(startIndex, endIndex)");
        sb.append(substring);
        sb.append(".jpg");
        File file = new File(externalFilesDir, sb.toString());
        this.f20448a.b(file);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        c9.compress(Bitmap.CompressFormat.JPEG, i8, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        try {
            file.createNewFile();
            p.b(byteArray);
            f.b(file, byteArray);
        } catch (IOException e8) {
            e8.printStackTrace();
        }
        c9.recycle();
        dVar.success(file.getAbsolutePath());
    }
}
