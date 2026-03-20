package n6;

import android.os.Bundle;
import com.google.android.gms.common.api.a;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class o implements a.d {

    /* renamed from: b  reason: collision with root package name */
    public static final o f22199b = a().a();

    /* renamed from: a  reason: collision with root package name */
    private final String f22200a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {

        /* renamed from: a  reason: collision with root package name */
        private String f22201a;

        /* synthetic */ a(r rVar) {
        }

        public o a() {
            return new o(this.f22201a, null);
        }

        public a b(String str) {
            this.f22201a = str;
            return this;
        }
    }

    /* synthetic */ o(String str, s sVar) {
        this.f22200a = str;
    }

    public static a a() {
        return new a(null);
    }

    public final Bundle b() {
        Bundle bundle = new Bundle();
        String str = this.f22200a;
        if (str != null) {
            bundle.putString("api", str);
        }
        return bundle;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof o) {
            return i.a(this.f22200a, ((o) obj).f22200a);
        }
        return false;
    }

    public final int hashCode() {
        return i.b(this.f22200a);
    }
}
