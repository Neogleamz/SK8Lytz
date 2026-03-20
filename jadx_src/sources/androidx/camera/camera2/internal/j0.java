package androidx.camera.camera2.internal;

import android.hardware.camera2.CameraCharacteristics;
import android.util.Pair;
import androidx.camera.camera2.internal.j0;
import androidx.camera.core.CameraState;
import androidx.lifecycle.LiveData;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class j0 implements y.q {

    /* renamed from: a  reason: collision with root package name */
    private final String f1886a;

    /* renamed from: b  reason: collision with root package name */
    private final s.y f1887b;

    /* renamed from: c  reason: collision with root package name */
    private final w.h f1888c;

    /* renamed from: e  reason: collision with root package name */
    private t f1890e;

    /* renamed from: h  reason: collision with root package name */
    private final a<CameraState> f1893h;

    /* renamed from: j  reason: collision with root package name */
    private final y.t0 f1895j;

    /* renamed from: k  reason: collision with root package name */
    private final y.g f1896k;

    /* renamed from: l  reason: collision with root package name */
    private final s.l0 f1897l;

    /* renamed from: d  reason: collision with root package name */
    private final Object f1889d = new Object();

    /* renamed from: f  reason: collision with root package name */
    private a<Integer> f1891f = null;

    /* renamed from: g  reason: collision with root package name */
    private a<androidx.camera.core.h3> f1892g = null;

    /* renamed from: i  reason: collision with root package name */
    private List<Pair<y.h, Executor>> f1894i = null;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a<T> extends androidx.lifecycle.n<T> {

        /* renamed from: m  reason: collision with root package name */
        private LiveData<T> f1898m;

        /* renamed from: n  reason: collision with root package name */
        private final T f1899n;

        a(T t8) {
            this.f1899n = t8;
        }

        @Override // androidx.lifecycle.LiveData
        public T e() {
            LiveData<T> liveData = this.f1898m;
            return liveData == null ? this.f1899n : liveData.e();
        }

        @Override // androidx.lifecycle.n
        public <S> void p(LiveData<S> liveData, androidx.lifecycle.q<? super S> qVar) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Multi-variable type inference failed */
        void r(LiveData<T> liveData) {
            LiveData liveData2 = (LiveData<T>) this.f1898m;
            if (liveData2 != null) {
                super.q(liveData2);
            }
            this.f1898m = liveData;
            super.p(liveData, new androidx.lifecycle.q() { // from class: androidx.camera.camera2.internal.i0
                @Override // androidx.lifecycle.q
                public final void b(Object obj) {
                    j0.a.this.o(obj);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public j0(String str, s.l0 l0Var) {
        String str2 = (String) androidx.core.util.h.h(str);
        this.f1886a = str2;
        this.f1897l = l0Var;
        s.y c9 = l0Var.c(str2);
        this.f1887b = c9;
        this.f1888c = new w.h(this);
        this.f1895j = u.g.a(str, c9);
        this.f1896k = new d(str, c9);
        this.f1893h = new a<>(CameraState.a(CameraState.Type.CLOSED));
    }

    private void p() {
        q();
    }

    private void q() {
        String str;
        int n8 = n();
        if (n8 == 0) {
            str = "INFO_SUPPORTED_HARDWARE_LEVEL_LIMITED";
        } else if (n8 == 1) {
            str = "INFO_SUPPORTED_HARDWARE_LEVEL_FULL";
        } else if (n8 == 2) {
            str = "INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY";
        } else if (n8 == 3) {
            str = "INFO_SUPPORTED_HARDWARE_LEVEL_3";
        } else if (n8 != 4) {
            str = "Unknown value: " + n8;
        } else {
            str = "INFO_SUPPORTED_HARDWARE_LEVEL_EXTERNAL";
        }
        androidx.camera.core.p1.e("Camera2CameraInfo", "Device Level: " + str);
    }

    @Override // y.q
    public Integer a() {
        Integer num = (Integer) this.f1887b.a(CameraCharacteristics.LENS_FACING);
        androidx.core.util.h.h(num);
        int intValue = num.intValue();
        if (intValue != 0) {
            return intValue != 1 ? null : 1;
        }
        return 0;
    }

    @Override // androidx.camera.core.s
    public int b() {
        return g(0);
    }

    @Override // y.q
    public String c() {
        return this.f1886a;
    }

    @Override // androidx.camera.core.s
    public LiveData<Integer> d() {
        synchronized (this.f1889d) {
            t tVar = this.f1890e;
            if (tVar == null) {
                if (this.f1891f == null) {
                    this.f1891f = new a<>(0);
                }
                return this.f1891f;
            }
            a<Integer> aVar = this.f1891f;
            if (aVar != null) {
                return aVar;
            }
            return tVar.F().f();
        }
    }

    @Override // y.q
    public void e(Executor executor, y.h hVar) {
        synchronized (this.f1889d) {
            t tVar = this.f1890e;
            if (tVar != null) {
                tVar.v(executor, hVar);
                return;
            }
            if (this.f1894i == null) {
                this.f1894i = new ArrayList();
            }
            this.f1894i.add(new Pair<>(hVar, executor));
        }
    }

    @Override // androidx.camera.core.s
    public String f() {
        return n() == 2 ? "androidx.camera.camera2.legacy" : "androidx.camera.camera2";
    }

    @Override // androidx.camera.core.s
    public int g(int i8) {
        int m8 = m();
        int b9 = androidx.camera.core.impl.utils.c.b(i8);
        Integer a9 = a();
        boolean z4 = true;
        return androidx.camera.core.impl.utils.c.a(b9, m8, (a9 == null || 1 != a9.intValue()) ? false : false);
    }

    @Override // androidx.camera.core.s
    public boolean h() {
        return v.f.c(this.f1887b);
    }

    @Override // y.q
    public void i(y.h hVar) {
        synchronized (this.f1889d) {
            t tVar = this.f1890e;
            if (tVar != null) {
                tVar.Z(hVar);
                return;
            }
            List<Pair<y.h, Executor>> list = this.f1894i;
            if (list == null) {
                return;
            }
            Iterator<Pair<y.h, Executor>> it = list.iterator();
            while (it.hasNext()) {
                if (it.next().first == hVar) {
                    it.remove();
                }
            }
        }
    }

    @Override // y.q
    public y.t0 j() {
        return this.f1895j;
    }

    @Override // androidx.camera.core.s
    public LiveData<androidx.camera.core.h3> k() {
        synchronized (this.f1889d) {
            t tVar = this.f1890e;
            if (tVar == null) {
                if (this.f1892g == null) {
                    this.f1892g = new a<>(j3.g(this.f1887b));
                }
                return this.f1892g;
            }
            a<androidx.camera.core.h3> aVar = this.f1892g;
            if (aVar != null) {
                return aVar;
            }
            return tVar.H().i();
        }
    }

    public s.y l() {
        return this.f1887b;
    }

    int m() {
        Integer num = (Integer) this.f1887b.a(CameraCharacteristics.SENSOR_ORIENTATION);
        androidx.core.util.h.h(num);
        return num.intValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int n() {
        Integer num = (Integer) this.f1887b.a(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
        androidx.core.util.h.h(num);
        return num.intValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void o(t tVar) {
        synchronized (this.f1889d) {
            this.f1890e = tVar;
            a<androidx.camera.core.h3> aVar = this.f1892g;
            if (aVar != null) {
                aVar.r(tVar.H().i());
            }
            a<Integer> aVar2 = this.f1891f;
            if (aVar2 != null) {
                aVar2.r(this.f1890e.F().f());
            }
            List<Pair<y.h, Executor>> list = this.f1894i;
            if (list != null) {
                for (Pair<y.h, Executor> pair : list) {
                    this.f1890e.v((Executor) pair.second, (y.h) pair.first);
                }
                this.f1894i = null;
            }
        }
        p();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void r(LiveData<CameraState> liveData) {
        this.f1893h.r(liveData);
    }
}
