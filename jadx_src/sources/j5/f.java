package j5;

import a6.x;
import android.net.Uri;
import com.google.android.exoplayer2.upstream.Loader;
import com.google.android.exoplayer2.w0;
import java.util.List;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class f implements Loader.e {

    /* renamed from: a  reason: collision with root package name */
    public final long f20741a = h5.h.a();

    /* renamed from: b  reason: collision with root package name */
    public final com.google.android.exoplayer2.upstream.a f20742b;

    /* renamed from: c  reason: collision with root package name */
    public final int f20743c;

    /* renamed from: d  reason: collision with root package name */
    public final w0 f20744d;

    /* renamed from: e  reason: collision with root package name */
    public final int f20745e;

    /* renamed from: f  reason: collision with root package name */
    public final Object f20746f;

    /* renamed from: g  reason: collision with root package name */
    public final long f20747g;

    /* renamed from: h  reason: collision with root package name */
    public final long f20748h;

    /* renamed from: i  reason: collision with root package name */
    protected final x f20749i;

    public f(a6.h hVar, com.google.android.exoplayer2.upstream.a aVar, int i8, w0 w0Var, int i9, Object obj, long j8, long j9) {
        this.f20749i = new x(hVar);
        this.f20742b = (com.google.android.exoplayer2.upstream.a) b6.a.e(aVar);
        this.f20743c = i8;
        this.f20744d = w0Var;
        this.f20745e = i9;
        this.f20746f = obj;
        this.f20747g = j8;
        this.f20748h = j9;
    }

    public final long b() {
        return this.f20749i.l();
    }

    public final long d() {
        return this.f20748h - this.f20747g;
    }

    public final Map<String, List<String>> e() {
        return this.f20749i.n();
    }

    public final Uri f() {
        return this.f20749i.m();
    }
}
