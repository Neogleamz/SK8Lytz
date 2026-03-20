package o3;

import android.util.SparseArray;
import kotlin.jvm.internal.p;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a {

    /* renamed from: a  reason: collision with root package name */
    public static final a f22220a = new a();

    /* renamed from: b  reason: collision with root package name */
    private static final SparseArray<p3.a> f22221b = new SparseArray<>();

    private a() {
    }

    public final p3.a a(int i8) {
        return f22221b.get(i8);
    }

    public final void b(p3.a aVar) {
        p.e(aVar, "handler");
        f22221b.append(aVar.getType(), aVar);
    }
}
