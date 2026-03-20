package com.google.android.gms.measurement.internal;

import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.gms.internal.measurement.ye;
import java.util.Arrays;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class j5 {

    /* renamed from: a  reason: collision with root package name */
    private final String f16696a;

    /* renamed from: b  reason: collision with root package name */
    private final Bundle f16697b;

    /* renamed from: c  reason: collision with root package name */
    private Bundle f16698c;

    /* renamed from: d  reason: collision with root package name */
    private final /* synthetic */ h5 f16699d;

    public j5(h5 h5Var, String str, Bundle bundle) {
        this.f16699d = h5Var;
        n6.j.f(str);
        this.f16696a = str;
        this.f16697b = new Bundle();
    }

    private final String c(Bundle bundle) {
        z4 E;
        Class<?> cls;
        String str;
        JSONArray jSONArray = new JSONArray();
        for (String str2 : bundle.keySet()) {
            Object obj = bundle.get(str2);
            if (obj != null) {
                try {
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("n", str2);
                    if (!ye.a() || !this.f16699d.a().r(c0.K0)) {
                        jSONObject.put("v", String.valueOf(obj));
                        if (obj instanceof String) {
                            jSONObject.put("t", "s");
                        } else if (obj instanceof Long) {
                            jSONObject.put("t", "l");
                        } else if (obj instanceof Double) {
                            jSONObject.put("t", "d");
                        } else {
                            E = this.f16699d.i().E();
                            cls = obj.getClass();
                            E.b("Cannot serialize bundle value to SharedPreferences. Type", cls);
                        }
                    } else if (obj instanceof String) {
                        jSONObject.put("v", String.valueOf(obj));
                        jSONObject.put("t", "s");
                    } else if (obj instanceof Long) {
                        jSONObject.put("v", String.valueOf(obj));
                        jSONObject.put("t", "l");
                    } else {
                        if (obj instanceof int[]) {
                            jSONObject.put("v", Arrays.toString((int[]) obj));
                            str = "ia";
                        } else if (obj instanceof long[]) {
                            jSONObject.put("v", Arrays.toString((long[]) obj));
                            str = "la";
                        } else if (obj instanceof Double) {
                            jSONObject.put("v", String.valueOf(obj));
                            jSONObject.put("t", "d");
                        } else {
                            E = this.f16699d.i().E();
                            cls = obj.getClass();
                            E.b("Cannot serialize bundle value to SharedPreferences. Type", cls);
                        }
                        jSONObject.put("t", str);
                    }
                    jSONArray.put(jSONObject);
                } catch (JSONException e8) {
                    this.f16699d.i().E().b("Cannot serialize bundle value to SharedPreferences", e8);
                }
            }
        }
        return jSONArray.toString();
    }

    public final Bundle a() {
        if (this.f16698c == null) {
            String string = this.f16699d.G().getString(this.f16696a, null);
            if (string != null) {
                try {
                    Bundle bundle = new Bundle();
                    JSONArray jSONArray = new JSONArray(string);
                    for (int i8 = 0; i8 < jSONArray.length(); i8++) {
                        try {
                            JSONObject jSONObject = jSONArray.getJSONObject(i8);
                            String string2 = jSONObject.getString("n");
                            String string3 = jSONObject.getString("t");
                            char c9 = 65535;
                            int hashCode = string3.hashCode();
                            if (hashCode != 100) {
                                if (hashCode != 108) {
                                    if (hashCode != 115) {
                                        if (hashCode != 3352) {
                                            if (hashCode == 3445 && string3.equals("la")) {
                                                c9 = 4;
                                            }
                                        } else if (string3.equals("ia")) {
                                            c9 = 3;
                                        }
                                    } else if (string3.equals("s")) {
                                        c9 = 0;
                                    }
                                } else if (string3.equals("l")) {
                                    c9 = 2;
                                }
                            } else if (string3.equals("d")) {
                                c9 = 1;
                            }
                            if (c9 == 0) {
                                bundle.putString(string2, jSONObject.getString("v"));
                            } else if (c9 == 1) {
                                bundle.putDouble(string2, Double.parseDouble(jSONObject.getString("v")));
                            } else if (c9 == 2) {
                                bundle.putLong(string2, Long.parseLong(jSONObject.getString("v")));
                            } else if (c9 != 3) {
                                if (c9 != 4) {
                                    this.f16699d.i().E().b("Unrecognized persisted bundle type. Type", string3);
                                } else if (ye.a() && this.f16699d.a().r(c0.K0)) {
                                    JSONArray jSONArray2 = new JSONArray(jSONObject.getString("v"));
                                    int length = jSONArray2.length();
                                    long[] jArr = new long[length];
                                    for (int i9 = 0; i9 < length; i9++) {
                                        jArr[i9] = jSONArray2.optLong(i9);
                                    }
                                    bundle.putLongArray(string2, jArr);
                                }
                            } else if (ye.a() && this.f16699d.a().r(c0.K0)) {
                                JSONArray jSONArray3 = new JSONArray(jSONObject.getString("v"));
                                int length2 = jSONArray3.length();
                                int[] iArr = new int[length2];
                                for (int i10 = 0; i10 < length2; i10++) {
                                    iArr[i10] = jSONArray3.optInt(i10);
                                }
                                bundle.putIntArray(string2, iArr);
                            }
                        } catch (NumberFormatException | JSONException unused) {
                            this.f16699d.i().E().a("Error reading value from SharedPreferences. Value dropped");
                        }
                    }
                    this.f16698c = bundle;
                } catch (JSONException unused2) {
                    this.f16699d.i().E().a("Error loading bundle from SharedPreferences. Values will be lost");
                }
            }
            if (this.f16698c == null) {
                this.f16698c = this.f16697b;
            }
        }
        return this.f16698c;
    }

    public final void b(Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        SharedPreferences.Editor edit = this.f16699d.G().edit();
        if (bundle.size() == 0) {
            edit.remove(this.f16696a);
        } else {
            edit.putString(this.f16696a, c(bundle));
        }
        edit.apply();
        this.f16698c = bundle;
    }
}
