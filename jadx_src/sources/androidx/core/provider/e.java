package androidx.core.provider;

import android.util.Base64;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e {

    /* renamed from: a  reason: collision with root package name */
    private final String f4798a;

    /* renamed from: b  reason: collision with root package name */
    private final String f4799b;

    /* renamed from: c  reason: collision with root package name */
    private final String f4800c;

    /* renamed from: d  reason: collision with root package name */
    private final List<List<byte[]>> f4801d;

    /* renamed from: e  reason: collision with root package name */
    private final int f4802e = 0;

    /* renamed from: f  reason: collision with root package name */
    private final String f4803f;

    public e(String str, String str2, String str3, List<List<byte[]>> list) {
        this.f4798a = (String) androidx.core.util.h.h(str);
        this.f4799b = (String) androidx.core.util.h.h(str2);
        this.f4800c = (String) androidx.core.util.h.h(str3);
        this.f4801d = (List) androidx.core.util.h.h(list);
        this.f4803f = a(str, str2, str3);
    }

    private String a(String str, String str2, String str3) {
        return str + "-" + str2 + "-" + str3;
    }

    public List<List<byte[]>> b() {
        return this.f4801d;
    }

    public int c() {
        return this.f4802e;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String d() {
        return this.f4803f;
    }

    public String e() {
        return this.f4798a;
    }

    public String f() {
        return this.f4799b;
    }

    public String g() {
        return this.f4800c;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("FontRequest {mProviderAuthority: " + this.f4798a + ", mProviderPackage: " + this.f4799b + ", mQuery: " + this.f4800c + ", mCertificates:");
        for (int i8 = 0; i8 < this.f4801d.size(); i8++) {
            sb.append(" [");
            List<byte[]> list = this.f4801d.get(i8);
            for (int i9 = 0; i9 < list.size(); i9++) {
                sb.append(" \"");
                sb.append(Base64.encodeToString(list.get(i9), 0));
                sb.append("\"");
            }
            sb.append(" ]");
        }
        sb.append("}");
        sb.append("mCertificatesArray: " + this.f4802e);
        return sb.toString();
    }
}
