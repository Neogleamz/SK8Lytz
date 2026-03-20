package l3;

import android.content.Context;
import io.flutter.plugin.common.i;
import io.flutter.plugin.common.j;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;
import kj.f;
import kotlin.jvm.internal.p;
import p3.a;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c extends g {

    /* renamed from: f  reason: collision with root package name */
    private final i f21565f;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public c(i iVar, j.d dVar) {
        super(dVar);
        p.e(iVar, "call");
        p.e(dVar, "result");
        this.f21565f = iVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void h(c cVar, Context context) {
        int i8;
        p.e(cVar, "this$0");
        p.e(context, "$context");
        Object obj = cVar.f21565f.b;
        p.c(obj, "null cannot be cast to non-null type kotlin.collections.List<kotlin.Any>");
        List list = (List) obj;
        int i9 = 0;
        Object obj2 = list.get(0);
        p.c(obj2, "null cannot be cast to non-null type kotlin.String");
        String str = (String) obj2;
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
        Object obj11 = list.get(9);
        p.c(obj11, "null cannot be cast to non-null type kotlin.Int");
        int intValue7 = ((Integer) obj11).intValue();
        a a9 = o3.a.f22220a.a(intValue5);
        if (a9 == null) {
            s3.a.a("No support format.");
            cVar.c(null);
            return;
        }
        if (booleanValue) {
            i9 = m3.a.f21831a.c(f.a(new File(str)));
        }
        if (i9 == 90 || i9 == 270) {
            i8 = intValue;
        } else {
            i8 = intValue2;
            intValue2 = intValue;
        }
        int i10 = intValue4 + i9;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            try {
                a9.b(context, str, byteArrayOutputStream, intValue2, i8, intValue3, i10, booleanValue2, intValue6, intValue7);
                cVar.c(byteArrayOutputStream.toByteArray());
            } catch (Exception e8) {
                if (k3.a.f20975c.a()) {
                    e8.printStackTrace();
                }
                cVar.c(null);
            }
        } finally {
            byteArrayOutputStream.close();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Not initialized variable reg: 15, insn: 0x011b: MOVE  (r14 I:??[OBJECT, ARRAY]) = (r15 I:??[OBJECT, ARRAY]), block:B:34:0x011b */
    /* JADX WARN: Removed duplicated region for block: B:36:0x011e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final void j(l3.c r17, android.content.Context r18) {
        /*
            Method dump skipped, instructions count: 290
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: l3.c.j(l3.c, android.content.Context):void");
    }

    public final void g(Context context) {
        p.e(context, "context");
        g.f21567c.a().execute(new a(this, context));
    }

    public final void i(Context context) {
        p.e(context, "context");
        g.f21567c.a().execute(new b(this, context));
    }
}
