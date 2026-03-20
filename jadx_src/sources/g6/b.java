package g6;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.util.VisibleForTesting;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import n6.j;
import org.json.JSONException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class b {

    /* renamed from: c  reason: collision with root package name */
    private static final Lock f20200c = new ReentrantLock();

    /* renamed from: d  reason: collision with root package name */
    private static b f20201d;

    /* renamed from: a  reason: collision with root package name */
    private final Lock f20202a = new ReentrantLock();

    /* renamed from: b  reason: collision with root package name */
    private final SharedPreferences f20203b;

    @VisibleForTesting
    b(Context context) {
        this.f20203b = context.getSharedPreferences("com.google.android.gms.signin", 0);
    }

    public static b a(Context context) {
        j.l(context);
        Lock lock = f20200c;
        lock.lock();
        try {
            if (f20201d == null) {
                f20201d = new b(context.getApplicationContext());
            }
            b bVar = f20201d;
            lock.unlock();
            return bVar;
        } catch (Throwable th) {
            f20200c.unlock();
            throw th;
        }
    }

    private static final String d(String str, String str2) {
        return str + ":" + str2;
    }

    public GoogleSignInAccount b() {
        String c9;
        String c10 = c("defaultGoogleSignInAccount");
        if (TextUtils.isEmpty(c10) || (c9 = c(d("googleSignInAccount", c10))) == null) {
            return null;
        }
        try {
            return GoogleSignInAccount.Z0(c9);
        } catch (JSONException unused) {
            return null;
        }
    }

    protected final String c(String str) {
        this.f20202a.lock();
        try {
            return this.f20203b.getString(str, null);
        } finally {
            this.f20202a.unlock();
        }
    }
}
