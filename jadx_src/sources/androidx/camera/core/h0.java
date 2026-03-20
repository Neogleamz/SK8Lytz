package androidx.camera.core;

import android.graphics.Rect;
import android.media.Image;
import androidx.camera.core.l1;
import java.util.HashSet;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class h0 implements l1 {

    /* renamed from: b  reason: collision with root package name */
    protected final l1 f2405b;

    /* renamed from: a  reason: collision with root package name */
    private final Object f2404a = new Object();

    /* renamed from: c  reason: collision with root package name */
    private final Set<a> f2406c = new HashSet();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a {
        void b(l1 l1Var);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public h0(l1 l1Var) {
        this.f2405b = l1Var;
    }

    @Override // androidx.camera.core.l1
    public l1.a[] A() {
        return this.f2405b.A();
    }

    @Override // androidx.camera.core.l1
    public Image B1() {
        return this.f2405b.B1();
    }

    @Override // androidx.camera.core.l1
    public void Y0(Rect rect) {
        this.f2405b.Y0(rect);
    }

    public void a(a aVar) {
        synchronized (this.f2404a) {
            this.f2406c.add(aVar);
        }
    }

    protected void b() {
        HashSet<a> hashSet;
        synchronized (this.f2404a) {
            hashSet = new HashSet(this.f2406c);
        }
        for (a aVar : hashSet) {
            aVar.b(this);
        }
    }

    @Override // androidx.camera.core.l1, java.lang.AutoCloseable
    public void close() {
        this.f2405b.close();
        b();
    }

    @Override // androidx.camera.core.l1
    public i1 e1() {
        return this.f2405b.e1();
    }

    @Override // androidx.camera.core.l1
    public int getFormat() {
        return this.f2405b.getFormat();
    }

    @Override // androidx.camera.core.l1
    public int getHeight() {
        return this.f2405b.getHeight();
    }

    @Override // androidx.camera.core.l1
    public int getWidth() {
        return this.f2405b.getWidth();
    }
}
