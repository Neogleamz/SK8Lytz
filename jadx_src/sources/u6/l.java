package u6;

import java.util.HashMap;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class l {
    public static void a(StringBuilder sb, HashMap<String, String> hashMap) {
        sb.append("{");
        boolean z4 = true;
        for (String str : hashMap.keySet()) {
            if (!z4) {
                sb.append(",");
            }
            String str2 = hashMap.get(str);
            sb.append("\"");
            sb.append(str);
            sb.append("\":");
            if (str2 == null) {
                sb.append("null");
            } else {
                sb.append("\"");
                sb.append(str2);
                sb.append("\"");
            }
            z4 = false;
        }
        sb.append("}");
    }
}
