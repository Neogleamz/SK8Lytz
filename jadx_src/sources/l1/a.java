package l1;

import android.os.Build;
import android.os.ext.SdkExtensions;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a {

    /* renamed from: a  reason: collision with root package name */
    public static final a f21560a = new a();

    /* renamed from: l1.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class C0186a {

        /* renamed from: a  reason: collision with root package name */
        public static final C0186a f21561a = new C0186a();

        private C0186a() {
        }

        public final int a() {
            return SdkExtensions.getExtensionVersion(1000000);
        }
    }

    private a() {
    }

    public final int a() {
        if (Build.VERSION.SDK_INT >= 30) {
            return C0186a.f21561a.a();
        }
        return 0;
    }
}
