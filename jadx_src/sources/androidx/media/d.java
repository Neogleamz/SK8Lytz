package androidx.media;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import androidx.media.f;
import androidx.media.g;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d {

    /* renamed from: b  reason: collision with root package name */
    static final boolean f6085b = Log.isLoggable("MediaSessionManager", 3);

    /* renamed from: c  reason: collision with root package name */
    private static final Object f6086c = new Object();

    /* renamed from: d  reason: collision with root package name */
    private static volatile d f6087d;

    /* renamed from: a  reason: collision with root package name */
    a f6088a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    interface a {
        boolean a(c cVar);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {

        /* renamed from: a  reason: collision with root package name */
        c f6089a;

        public b(String str, int i8, int i9) {
            Objects.requireNonNull(str, "package shouldn't be null");
            if (TextUtils.isEmpty(str)) {
                throw new IllegalArgumentException("packageName should be nonempty");
            }
            this.f6089a = Build.VERSION.SDK_INT >= 28 ? new f.a(str, i8, i9) : new g.a(str, i8, i9);
        }

        public String a() {
            return this.f6089a.c();
        }

        public int b() {
            return this.f6089a.a();
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof b) {
                return this.f6089a.equals(((b) obj).f6089a);
            }
            return false;
        }

        public int hashCode() {
            return this.f6089a.hashCode();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface c {
        int a();

        int b();

        String c();
    }

    private d(Context context) {
        int i8 = Build.VERSION.SDK_INT;
        this.f6088a = i8 >= 28 ? new f(context) : i8 >= 21 ? new e(context) : new g(context);
    }

    public static d a(Context context) {
        d dVar;
        if (context != null) {
            synchronized (f6086c) {
                if (f6087d == null) {
                    f6087d = new d(context.getApplicationContext());
                }
                dVar = f6087d;
            }
            return dVar;
        }
        throw new IllegalArgumentException("context cannot be null");
    }

    public boolean b(b bVar) {
        if (bVar != null) {
            return this.f6088a.a(bVar.f6089a);
        }
        throw new IllegalArgumentException("userInfo should not be null");
    }
}
