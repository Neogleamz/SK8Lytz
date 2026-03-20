package l3;

import android.content.Context;
import com.fluttercandies.flutter_image_compress.exception.CompressError;
import io.flutter.plugin.common.i;
import io.flutter.plugin.common.j;
import java.io.ByteArrayOutputStream;
import java.util.List;
import kotlin.jvm.internal.p;
import m3.a;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e extends g {

    /* renamed from: f  reason: collision with root package name */
    private final i f21566f;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public e(i iVar, j.d dVar) {
        super(dVar);
        p.e(iVar, "call");
        p.e(dVar, "result");
        this.f21566f = iVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void g(e eVar, Context context) {
        int i8;
        p.e(eVar, "this$0");
        p.e(context, "$context");
        Object obj = eVar.f21566f.b;
        p.c(obj, "null cannot be cast to non-null type kotlin.collections.List<kotlin.Any>");
        List list = (List) obj;
        Object obj2 = list.get(0);
        p.c(obj2, "null cannot be cast to non-null type kotlin.ByteArray");
        byte[] bArr = (byte[]) obj2;
        Object obj3 = list.get(1);
        p.c(obj3, "null cannot be cast to non-null type kotlin.Int");
        int intValue = ((Integer) obj3).intValue();
        Object obj4 = list.get(2);
        p.c(obj4, "null cannot be cast to non-null type kotlin.Int");
        int intValue2 = ((Integer) obj4).intValue();
        Object obj5 = list.get(3);
        p.c(obj5, "null cannot be cast to non-null type kotlin.Int");
        int intValue3 = ((Integer) obj5).intValue();
        Object obj6 = list.get(4);
        p.c(obj6, "null cannot be cast to non-null type kotlin.Int");
        int intValue4 = ((Integer) obj6).intValue();
        Object obj7 = list.get(5);
        p.c(obj7, "null cannot be cast to non-null type kotlin.Boolean");
        boolean booleanValue = ((Boolean) obj7).booleanValue();
        Object obj8 = list.get(6);
        p.c(obj8, "null cannot be cast to non-null type kotlin.Int");
        int intValue5 = ((Integer) obj8).intValue();
        Object obj9 = list.get(7);
        p.c(obj9, "null cannot be cast to non-null type kotlin.Boolean");
        boolean booleanValue2 = ((Boolean) obj9).booleanValue();
        Object obj10 = list.get(8);
        p.c(obj10, "null cannot be cast to non-null type kotlin.Int");
        int intValue6 = ((Integer) obj10).intValue();
        int c9 = booleanValue ? a.f21831a.c(bArr) : 0;
        if (c9 == 90 || c9 == 270) {
            i8 = intValue2;
        } else {
            i8 = intValue;
            intValue = intValue2;
        }
        p3.a a9 = o3.a.f22220a.a(intValue5);
        if (a9 == null) {
            s3.a.a("No support format.");
            eVar.c(null);
            return;
        }
        int i9 = intValue4 + c9;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            try {
                try {
                    a9.a(context, bArr, byteArrayOutputStream, i8, intValue, intValue3, i9, booleanValue2, intValue6);
                    eVar.c(byteArrayOutputStream.toByteArray());
                } catch (Exception e8) {
                    if (k3.a.f20975c.a()) {
                        e8.printStackTrace();
                    }
                    eVar.c(null);
                }
            } catch (CompressError e9) {
                s3.a.a(e9.getMessage());
                if (k3.a.f20975c.a()) {
                    e9.printStackTrace();
                }
                eVar.c(null);
            }
        } finally {
            byteArrayOutputStream.close();
        }
    }

    public final void f(Context context) {
        p.e(context, "context");
        g.f21567c.a().execute(new d(this, context));
    }
}
