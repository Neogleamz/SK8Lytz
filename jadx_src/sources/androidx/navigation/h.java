package androidx.navigation;

import android.content.Intent;
import android.net.Uri;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class h {

    /* renamed from: a  reason: collision with root package name */
    private final Uri f6384a;

    /* renamed from: b  reason: collision with root package name */
    private final String f6385b;

    /* renamed from: c  reason: collision with root package name */
    private final String f6386c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public h(Intent intent) {
        this(intent.getData(), intent.getAction(), intent.getType());
    }

    h(Uri uri, String str, String str2) {
        this.f6384a = uri;
        this.f6385b = str;
        this.f6386c = str2;
    }

    public String a() {
        return this.f6385b;
    }

    public String b() {
        return this.f6386c;
    }

    public Uri c() {
        return this.f6384a;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("NavDeepLinkRequest");
        sb.append("{");
        if (this.f6384a != null) {
            sb.append(" uri=");
            sb.append(this.f6384a.toString());
        }
        if (this.f6385b != null) {
            sb.append(" action=");
            sb.append(this.f6385b);
        }
        if (this.f6386c != null) {
            sb.append(" mimetype=");
            sb.append(this.f6386c);
        }
        sb.append(" }");
        return sb.toString();
    }
}
