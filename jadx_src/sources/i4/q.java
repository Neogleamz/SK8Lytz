package i4;

import java.util.HashSet;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class q {

    /* renamed from: a  reason: collision with root package name */
    private static final HashSet<String> f20509a = new HashSet<>();

    /* renamed from: b  reason: collision with root package name */
    private static String f20510b = "goog.exo.core";

    public static synchronized void a(String str) {
        synchronized (q.class) {
            if (f20509a.add(str)) {
                f20510b += ", " + str;
            }
        }
    }

    public static synchronized String b() {
        String str;
        synchronized (q.class) {
            str = f20510b;
        }
        return str;
    }
}
