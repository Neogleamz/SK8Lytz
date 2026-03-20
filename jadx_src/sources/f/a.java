package f;

import android.content.Context;
import android.content.Intent;
import kotlin.jvm.internal.p;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class a<I, O> {

    /* renamed from: f.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class C0167a<T> {

        /* renamed from: a  reason: collision with root package name */
        private final T f19816a;

        public C0167a(T t8) {
            this.f19816a = t8;
        }

        public final T a() {
            return this.f19816a;
        }
    }

    public abstract Intent a(Context context, I i8);

    public C0167a<O> b(Context context, I i8) {
        p.e(context, "context");
        return null;
    }

    public abstract O c(int i8, Intent intent);
}
