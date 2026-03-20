package androidx.navigation;

import android.annotation.SuppressLint;
import androidx.navigation.q;
import java.util.HashMap;
import java.util.Map;
@SuppressLint({"TypeParameterUnusedInFormals"})
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class r {

    /* renamed from: b  reason: collision with root package name */
    private static final HashMap<Class<?>, String> f6442b = new HashMap<>();

    /* renamed from: a  reason: collision with root package name */
    private final HashMap<String, q<? extends i>> f6443a = new HashMap<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String c(Class<? extends q> cls) {
        HashMap<Class<?>, String> hashMap = f6442b;
        String str = hashMap.get(cls);
        if (str == null) {
            q.b bVar = (q.b) cls.getAnnotation(q.b.class);
            str = bVar != null ? bVar.value() : null;
            if (!g(str)) {
                throw new IllegalArgumentException("No @Navigator.Name annotation found for " + cls.getSimpleName());
            }
            hashMap.put(cls, str);
        }
        return str;
    }

    private static boolean g(String str) {
        return (str == null || str.isEmpty()) ? false : true;
    }

    public final q<? extends i> a(q<? extends i> qVar) {
        return b(c(qVar.getClass()), qVar);
    }

    public q<? extends i> b(String str, q<? extends i> qVar) {
        if (g(str)) {
            return this.f6443a.put(str, qVar);
        }
        throw new IllegalArgumentException("navigator name cannot be an empty string");
    }

    public final <T extends q<?>> T d(Class<T> cls) {
        return (T) e(c(cls));
    }

    public <T extends q<?>> T e(String str) {
        if (g(str)) {
            q<? extends i> qVar = this.f6443a.get(str);
            if (qVar != null) {
                return qVar;
            }
            throw new IllegalStateException("Could not find Navigator with name \"" + str + "\". You must call NavController.addNavigator() for each navigation type.");
        }
        throw new IllegalArgumentException("navigator name cannot be an empty string");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Map<String, q<? extends i>> f() {
        return this.f6443a;
    }
}
