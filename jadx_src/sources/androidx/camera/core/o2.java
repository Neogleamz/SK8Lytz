package androidx.camera.core;

import android.util.SparseArray;
import androidx.concurrent.futures.c;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class o2 implements y.f0 {

    /* renamed from: e  reason: collision with root package name */
    private final List<Integer> f2765e;

    /* renamed from: f  reason: collision with root package name */
    private String f2766f;

    /* renamed from: a  reason: collision with root package name */
    final Object f2761a = new Object();

    /* renamed from: b  reason: collision with root package name */
    final SparseArray<c.a<l1>> f2762b = new SparseArray<>();

    /* renamed from: c  reason: collision with root package name */
    private final SparseArray<com.google.common.util.concurrent.d<l1>> f2763c = new SparseArray<>();

    /* renamed from: d  reason: collision with root package name */
    private final List<l1> f2764d = new ArrayList();

    /* renamed from: g  reason: collision with root package name */
    private boolean f2767g = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements c.InterfaceC0024c<l1> {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ int f2768a;

        a(int i8) {
            this.f2768a = i8;
        }

        @Override // androidx.concurrent.futures.c.InterfaceC0024c
        public Object a(c.a<l1> aVar) {
            synchronized (o2.this.f2761a) {
                o2.this.f2762b.put(this.f2768a, aVar);
            }
            return "getImageProxy(id: " + this.f2768a + ")";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public o2(List<Integer> list, String str) {
        this.f2766f = null;
        this.f2765e = list;
        this.f2766f = str;
        f();
    }

    private void f() {
        synchronized (this.f2761a) {
            for (Integer num : this.f2765e) {
                int intValue = num.intValue();
                this.f2763c.put(intValue, androidx.concurrent.futures.c.a(new a(intValue)));
            }
        }
    }

    @Override // y.f0
    public com.google.common.util.concurrent.d<l1> a(int i8) {
        com.google.common.util.concurrent.d<l1> dVar;
        synchronized (this.f2761a) {
            if (this.f2767g) {
                throw new IllegalStateException("ImageProxyBundle already closed.");
            }
            dVar = this.f2763c.get(i8);
            if (dVar == null) {
                throw new IllegalArgumentException("ImageProxyBundle does not contain this id: " + i8);
            }
        }
        return dVar;
    }

    @Override // y.f0
    public List<Integer> b() {
        return Collections.unmodifiableList(this.f2765e);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c(l1 l1Var) {
        synchronized (this.f2761a) {
            if (this.f2767g) {
                return;
            }
            Integer num = (Integer) l1Var.e1().a().c(this.f2766f);
            if (num == null) {
                throw new IllegalArgumentException("CaptureId is null.");
            }
            c.a<l1> aVar = this.f2762b.get(num.intValue());
            if (aVar != null) {
                this.f2764d.add(l1Var);
                aVar.c(l1Var);
                return;
            }
            throw new IllegalArgumentException("ImageProxyBundle does not contain this id: " + num);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d() {
        synchronized (this.f2761a) {
            if (this.f2767g) {
                return;
            }
            for (l1 l1Var : this.f2764d) {
                l1Var.close();
            }
            this.f2764d.clear();
            this.f2763c.clear();
            this.f2762b.clear();
            this.f2767g = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e() {
        synchronized (this.f2761a) {
            if (this.f2767g) {
                return;
            }
            for (l1 l1Var : this.f2764d) {
                l1Var.close();
            }
            this.f2764d.clear();
            this.f2763c.clear();
            this.f2762b.clear();
            f();
        }
    }
}
