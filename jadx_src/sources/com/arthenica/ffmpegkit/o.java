package com.arthenica.ffmpegkit;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class o {
    public static n a(String str) {
        JSONObject jSONObject = new JSONObject(str);
        JSONArray optJSONArray = jSONObject.optJSONArray("streams");
        JSONArray optJSONArray2 = jSONObject.optJSONArray("chapters");
        ArrayList arrayList = new ArrayList();
        for (int i8 = 0; optJSONArray != null && i8 < optJSONArray.length(); i8++) {
            JSONObject optJSONObject = optJSONArray.optJSONObject(i8);
            if (optJSONObject != null) {
                arrayList.add(new x(optJSONObject));
            }
        }
        ArrayList arrayList2 = new ArrayList();
        for (int i9 = 0; optJSONArray2 != null && i9 < optJSONArray2.length(); i9++) {
            JSONObject optJSONObject2 = optJSONArray2.optJSONObject(i9);
            if (optJSONObject2 != null) {
                arrayList2.add(new e(optJSONObject2));
            }
        }
        return new n(jSONObject, arrayList, arrayList2);
    }
}
