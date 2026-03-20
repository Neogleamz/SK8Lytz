package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.util.AttributeSet;
import androidx.constraintlayout.widget.ConstraintAttribute;
import java.util.HashMap;
import java.util.HashSet;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class c {

    /* renamed from: f  reason: collision with root package name */
    public static int f3247f = -1;

    /* renamed from: a  reason: collision with root package name */
    int f3248a;

    /* renamed from: b  reason: collision with root package name */
    int f3249b;

    /* renamed from: c  reason: collision with root package name */
    String f3250c;

    /* renamed from: d  reason: collision with root package name */
    protected int f3251d;

    /* renamed from: e  reason: collision with root package name */
    HashMap<String, ConstraintAttribute> f3252e;

    public c() {
        int i8 = f3247f;
        this.f3248a = i8;
        this.f3249b = i8;
        this.f3250c = null;
    }

    public abstract void a(HashMap<String, r> hashMap);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void b(HashSet<String> hashSet);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void c(Context context, AttributeSet attributeSet);

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean d(String str) {
        String str2 = this.f3250c;
        if (str2 == null || str == null) {
            return false;
        }
        return str.matches(str2);
    }

    public void e(HashMap<String, Integer> hashMap) {
    }
}
