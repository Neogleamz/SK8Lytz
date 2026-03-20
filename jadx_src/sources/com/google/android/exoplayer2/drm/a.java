package com.google.android.exoplayer2.drm;

import b6.l0;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class a {
    public static byte[] a(byte[] bArr) {
        return l0.f8063a >= 27 ? bArr : l0.m0(c(l0.D(bArr)));
    }

    public static byte[] b(byte[] bArr) {
        if (l0.f8063a >= 27) {
            return bArr;
        }
        try {
            JSONObject jSONObject = new JSONObject(l0.D(bArr));
            StringBuilder sb = new StringBuilder("{\"keys\":[");
            JSONArray jSONArray = jSONObject.getJSONArray("keys");
            for (int i8 = 0; i8 < jSONArray.length(); i8++) {
                if (i8 != 0) {
                    sb.append(",");
                }
                JSONObject jSONObject2 = jSONArray.getJSONObject(i8);
                sb.append("{\"k\":\"");
                sb.append(d(jSONObject2.getString("k")));
                sb.append("\",\"kid\":\"");
                sb.append(d(jSONObject2.getString("kid")));
                sb.append("\",\"kty\":\"");
                sb.append(jSONObject2.getString("kty"));
                sb.append("\"}");
            }
            sb.append("]}");
            return l0.m0(sb.toString());
        } catch (JSONException e8) {
            b6.p.d("ClearKeyUtil", "Failed to adjust response data: " + l0.D(bArr), e8);
            return bArr;
        }
    }

    private static String c(String str) {
        return str.replace('+', '-').replace('/', '_');
    }

    private static String d(String str) {
        return str.replace('-', '+').replace('_', '/');
    }
}
