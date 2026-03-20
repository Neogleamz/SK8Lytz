package f;

import android.content.Context;
import android.content.Intent;
import cj.q;
import f.a;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Pair;
import kotlin.collections.j0;
import kotlin.jvm.internal.i;
import kotlin.jvm.internal.p;
import sj.j;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e extends f.a<String[], Map<String, Boolean>> {

    /* renamed from: a  reason: collision with root package name */
    public static final a f19825a = new a(null);

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(i iVar) {
            this();
        }

        public final Intent a(String[] strArr) {
            p.e(strArr, "input");
            Intent putExtra = new Intent("androidx.activity.result.contract.action.REQUEST_PERMISSIONS").putExtra("androidx.activity.result.contract.extra.PERMISSIONS", strArr);
            p.d(putExtra, "Intent(ACTION_REQUEST_PE…EXTRA_PERMISSIONS, input)");
            return putExtra;
        }
    }

    @Override // f.a
    /* renamed from: d */
    public Intent a(Context context, String[] strArr) {
        p.e(context, "context");
        p.e(strArr, "input");
        return f19825a.a(strArr);
    }

    @Override // f.a
    /* renamed from: e */
    public a.C0167a<Map<String, Boolean>> b(Context context, String[] strArr) {
        p.e(context, "context");
        p.e(strArr, "input");
        boolean z4 = true;
        if (strArr.length == 0) {
            return new a.C0167a<>(j0.g());
        }
        int length = strArr.length;
        int i8 = 0;
        while (true) {
            if (i8 >= length) {
                break;
            }
            if (!(androidx.core.content.a.a(context, strArr[i8]) == 0)) {
                z4 = false;
                break;
            }
            i8++;
        }
        if (z4) {
            LinkedHashMap linkedHashMap = new LinkedHashMap(j.b(j0.d(strArr.length), 16));
            for (String str : strArr) {
                Pair a9 = q.a(str, Boolean.TRUE);
                linkedHashMap.put(a9.c(), a9.d());
            }
            return new a.C0167a<>(linkedHashMap);
        }
        return null;
    }

    @Override // f.a
    /* renamed from: f */
    public Map<String, Boolean> c(int i8, Intent intent) {
        if (i8 == -1 && intent != null) {
            String[] stringArrayExtra = intent.getStringArrayExtra("androidx.activity.result.contract.extra.PERMISSIONS");
            int[] intArrayExtra = intent.getIntArrayExtra("androidx.activity.result.contract.extra.PERMISSION_GRANT_RESULTS");
            if (intArrayExtra == null || stringArrayExtra == null) {
                return j0.g();
            }
            ArrayList arrayList = new ArrayList(intArrayExtra.length);
            for (int i9 : intArrayExtra) {
                arrayList.add(Boolean.valueOf(i9 == 0));
            }
            return j0.n(kotlin.collections.q.W(kotlin.collections.j.q(stringArrayExtra), arrayList));
        }
        return j0.g();
    }
}
