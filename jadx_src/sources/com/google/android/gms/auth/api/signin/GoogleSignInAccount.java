package com.google.android.gms.auth.api.signin;

import android.accounts.Account;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.util.VisibleForTesting;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import n6.j;
import org.json.JSONArray;
import org.json.JSONObject;
import u6.d;
import u6.g;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class GoogleSignInAccount extends AbstractSafeParcelable implements ReflectedParcelable {
    public static final Parcelable.Creator<GoogleSignInAccount> CREATOR = new a();
    @VisibleForTesting

    /* renamed from: p  reason: collision with root package name */
    public static d f11426p = g.d();

    /* renamed from: a  reason: collision with root package name */
    final int f11427a;

    /* renamed from: b  reason: collision with root package name */
    private String f11428b;

    /* renamed from: c  reason: collision with root package name */
    private String f11429c;

    /* renamed from: d  reason: collision with root package name */
    private String f11430d;

    /* renamed from: e  reason: collision with root package name */
    private String f11431e;

    /* renamed from: f  reason: collision with root package name */
    private Uri f11432f;

    /* renamed from: g  reason: collision with root package name */
    private String f11433g;

    /* renamed from: h  reason: collision with root package name */
    private long f11434h;

    /* renamed from: j  reason: collision with root package name */
    private String f11435j;

    /* renamed from: k  reason: collision with root package name */
    List f11436k;

    /* renamed from: l  reason: collision with root package name */
    private String f11437l;

    /* renamed from: m  reason: collision with root package name */
    private String f11438m;

    /* renamed from: n  reason: collision with root package name */
    private Set f11439n = new HashSet();

    /* JADX INFO: Access modifiers changed from: package-private */
    public GoogleSignInAccount(int i8, String str, String str2, String str3, String str4, Uri uri, String str5, long j8, String str6, List list, String str7, String str8) {
        this.f11427a = i8;
        this.f11428b = str;
        this.f11429c = str2;
        this.f11430d = str3;
        this.f11431e = str4;
        this.f11432f = uri;
        this.f11433g = str5;
        this.f11434h = j8;
        this.f11435j = str6;
        this.f11436k = list;
        this.f11437l = str7;
        this.f11438m = str8;
    }

    public static GoogleSignInAccount X0(String str, String str2, String str3, String str4, String str5, String str6, Uri uri, Long l8, String str7, Set set) {
        return new GoogleSignInAccount(3, str, str2, str3, str4, uri, null, l8.longValue(), j.f(str7), new ArrayList((Collection) j.l(set)), str5, str6);
    }

    public static GoogleSignInAccount Z0(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        JSONObject jSONObject = new JSONObject(str);
        String optString = jSONObject.optString("photoUrl");
        Uri parse = !TextUtils.isEmpty(optString) ? Uri.parse(optString) : null;
        long parseLong = Long.parseLong(jSONObject.getString("expirationTime"));
        HashSet hashSet = new HashSet();
        JSONArray jSONArray = jSONObject.getJSONArray("grantedScopes");
        int length = jSONArray.length();
        for (int i8 = 0; i8 < length; i8++) {
            hashSet.add(new Scope(jSONArray.getString(i8)));
        }
        GoogleSignInAccount X0 = X0(jSONObject.optString("id"), jSONObject.has("tokenId") ? jSONObject.optString("tokenId") : null, jSONObject.has("email") ? jSONObject.optString("email") : null, jSONObject.has("displayName") ? jSONObject.optString("displayName") : null, jSONObject.has("givenName") ? jSONObject.optString("givenName") : null, jSONObject.has("familyName") ? jSONObject.optString("familyName") : null, parse, Long.valueOf(parseLong), jSONObject.getString("obfuscatedIdentifier"), hashSet);
        X0.f11433g = jSONObject.has("serverAuthCode") ? jSONObject.optString("serverAuthCode") : null;
        return X0;
    }

    public String D0() {
        return this.f11437l;
    }

    public String E0() {
        return this.f11429c;
    }

    public Uri I0() {
        return this.f11432f;
    }

    public Set<Scope> T0() {
        HashSet hashSet = new HashSet(this.f11436k);
        hashSet.addAll(this.f11439n);
        return hashSet;
    }

    public String W0() {
        return this.f11433g;
    }

    public String Z() {
        return this.f11438m;
    }

    public String e() {
        return this.f11428b;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj instanceof GoogleSignInAccount) {
            GoogleSignInAccount googleSignInAccount = (GoogleSignInAccount) obj;
            return googleSignInAccount.f11435j.equals(this.f11435j) && googleSignInAccount.T0().equals(T0());
        }
        return false;
    }

    public int hashCode() {
        return ((this.f11435j.hashCode() + 527) * 31) + T0().hashCode();
    }

    public Account n() {
        String str = this.f11430d;
        if (str == null) {
            return null;
        }
        return new Account(str, "com.google");
    }

    public String t() {
        return this.f11431e;
    }

    public String u() {
        return this.f11430d;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 1, this.f11427a);
        o6.a.r(parcel, 2, e(), false);
        o6.a.r(parcel, 3, E0(), false);
        o6.a.r(parcel, 4, u(), false);
        o6.a.r(parcel, 5, t(), false);
        o6.a.q(parcel, 6, I0(), i8, false);
        o6.a.r(parcel, 7, W0(), false);
        o6.a.n(parcel, 8, this.f11434h);
        o6.a.r(parcel, 9, this.f11435j, false);
        o6.a.v(parcel, 10, this.f11436k, false);
        o6.a.r(parcel, 11, D0(), false);
        o6.a.r(parcel, 12, Z(), false);
        o6.a.b(parcel, a9);
    }
}
