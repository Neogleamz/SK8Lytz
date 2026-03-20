package com.google.android.gms.measurement.internal;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import com.google.android.gms.measurement.internal.zziq;
import java.util.HashMap;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class xa {

    /* renamed from: b  reason: collision with root package name */
    private static final String[] f17140b = {"GoogleConsent", "gdprApplies", "EnableAdvertiserConsentMode", "PolicyVersion", "PurposeConsents", "CmpSdkID"};

    /* renamed from: a  reason: collision with root package name */
    private final Map<String, String> f17141a;

    private xa(Map<String, String> map) {
        HashMap hashMap = new HashMap();
        this.f17141a = hashMap;
        hashMap.putAll(map);
    }

    private static int a(SharedPreferences sharedPreferences, String str) {
        try {
            return sharedPreferences.getInt(str, -1);
        } catch (ClassCastException unused) {
            return -1;
        }
    }

    public static xa c(SharedPreferences sharedPreferences) {
        HashMap hashMap = new HashMap();
        String f5 = f(sharedPreferences, "IABTCF_VendorConsents");
        if (!"\u0000".equals(f5) && f5.length() > 754) {
            hashMap.put("GoogleConsent", String.valueOf(f5.charAt(754)));
        }
        int a9 = a(sharedPreferences, "IABTCF_gdprApplies");
        if (a9 != -1) {
            hashMap.put("gdprApplies", String.valueOf(a9));
        }
        int a10 = a(sharedPreferences, "IABTCF_EnableAdvertiserConsentMode");
        if (a10 != -1) {
            hashMap.put("EnableAdvertiserConsentMode", String.valueOf(a10));
        }
        int a11 = a(sharedPreferences, "IABTCF_PolicyVersion");
        if (a11 != -1) {
            hashMap.put("PolicyVersion", String.valueOf(a11));
        }
        String f8 = f(sharedPreferences, "IABTCF_PurposeConsents");
        if (!"\u0000".equals(f8)) {
            hashMap.put("PurposeConsents", f8);
        }
        int a12 = a(sharedPreferences, "IABTCF_CmpSdkID");
        if (a12 != -1) {
            hashMap.put("CmpSdkID", String.valueOf(a12));
        }
        return new xa(hashMap);
    }

    public static String d(String str, boolean z4) {
        if (!z4 || str.length() <= 4) {
            return str;
        }
        char[] charArray = str.toCharArray();
        int i8 = 0;
        int i9 = 1;
        while (true) {
            if (i9 >= 64) {
                break;
            } else if (charArray[4] == "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_".charAt(i9)) {
                i8 = i9;
                break;
            } else {
                i9++;
            }
        }
        charArray[4] = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_".charAt(i8 | 1);
        return String.valueOf(charArray);
    }

    private static String f(SharedPreferences sharedPreferences, String str) {
        try {
            return sharedPreferences.getString(str, "\u0000");
        } catch (ClassCastException unused) {
            return "\u0000";
        }
    }

    private final int h() {
        try {
            String str = this.f17141a.get("CmpSdkID");
            if (TextUtils.isEmpty(str)) {
                return -1;
            }
            return Integer.parseInt(str);
        } catch (NumberFormatException unused) {
            return -1;
        }
    }

    private final int i() {
        try {
            String str = this.f17141a.get("PolicyVersion");
            if (TextUtils.isEmpty(str)) {
                return -1;
            }
            return Integer.parseInt(str);
        } catch (NumberFormatException unused) {
            return -1;
        }
    }

    public final Bundle b() {
        int i8;
        if (("1".equals(this.f17141a.get("GoogleConsent")) && "1".equals(this.f17141a.get("gdprApplies")) && "1".equals(this.f17141a.get("EnableAdvertiserConsentMode"))) && (i8 = i()) >= 0) {
            String str = this.f17141a.get("PurposeConsents");
            if (TextUtils.isEmpty(str)) {
                return Bundle.EMPTY;
            }
            Bundle bundle = new Bundle();
            String str2 = "granted";
            if (str.length() > 0) {
                bundle.putString(zziq.zza.AD_STORAGE.f17280a, str.charAt(0) == '1' ? "granted" : "denied");
            }
            if (str.length() > 3) {
                bundle.putString(zziq.zza.AD_PERSONALIZATION.f17280a, (str.charAt(2) == '1' && str.charAt(3) == '1') ? "granted" : "denied");
            }
            if (str.length() > 6 && i8 >= 4) {
                bundle.putString(zziq.zza.AD_USER_DATA.f17280a, (str.charAt(0) == '1' && str.charAt(6) == '1') ? "denied" : "denied");
            }
            return bundle;
        }
        return Bundle.EMPTY;
    }

    public final String e() {
        StringBuilder sb = new StringBuilder();
        sb.append("1");
        int h8 = h();
        if (h8 < 0 || h8 > 4095) {
            sb.append("00");
        } else {
            sb.append("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_".charAt((h8 >> 6) & 63));
            sb.append("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_".charAt(h8 & 63));
        }
        int i8 = i();
        if (i8 < 0 || i8 > 63) {
            sb.append("0");
        } else {
            sb.append("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_".charAt(i8));
        }
        n6.j.a(true);
        int i9 = ("1".equals(this.f17141a.get("gdprApplies")) ? 2 : 0) | 4;
        if ("1".equals(this.f17141a.get("EnableAdvertiserConsentMode"))) {
            i9 |= 8;
        }
        sb.append("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_".charAt(i9));
        return sb.toString();
    }

    public final boolean equals(Object obj) {
        if (obj instanceof xa) {
            return g().equalsIgnoreCase(((xa) obj).g());
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final String g() {
        String[] strArr;
        StringBuilder sb = new StringBuilder();
        for (String str : f17140b) {
            if (this.f17141a.containsKey(str)) {
                if (sb.length() > 0) {
                    sb.append(";");
                }
                sb.append(str);
                sb.append("=");
                sb.append(this.f17141a.get(str));
            }
        }
        return sb.toString();
    }

    public final int hashCode() {
        return g().hashCode();
    }

    public final String toString() {
        return g();
    }
}
