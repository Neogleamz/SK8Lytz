package a1;

import android.annotation.SuppressLint;
import android.text.Editable;
import androidx.emoji2.text.n;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class b extends Editable.Factory {

    /* renamed from: a  reason: collision with root package name */
    private static final Object f43a = new Object();

    /* renamed from: b  reason: collision with root package name */
    private static volatile Editable.Factory f44b;

    /* renamed from: c  reason: collision with root package name */
    private static Class<?> f45c;

    @SuppressLint({"PrivateApi"})
    private b() {
        try {
            f45c = Class.forName("android.text.DynamicLayout$ChangeWatcher", false, b.class.getClassLoader());
        } catch (Throwable unused) {
        }
    }

    public static Editable.Factory getInstance() {
        if (f44b == null) {
            synchronized (f43a) {
                if (f44b == null) {
                    f44b = new b();
                }
            }
        }
        return f44b;
    }

    @Override // android.text.Editable.Factory
    public Editable newEditable(CharSequence charSequence) {
        Class<?> cls = f45c;
        return cls != null ? n.c(cls, charSequence) : super.newEditable(charSequence);
    }
}
