package androidx.camera.camera2.internal;

import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import androidx.camera.camera2.internal.t;
import androidx.camera.core.CameraControl;
import androidx.concurrent.futures.c;
import androidx.lifecycle.LiveData;
import java.util.concurrent.Executor;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e3 {

    /* renamed from: a  reason: collision with root package name */
    private final t f1788a;

    /* renamed from: b  reason: collision with root package name */
    private final androidx.lifecycle.p<Integer> f1789b = new androidx.lifecycle.p<>(0);

    /* renamed from: c  reason: collision with root package name */
    private final boolean f1790c;

    /* renamed from: d  reason: collision with root package name */
    private final Executor f1791d;

    /* renamed from: e  reason: collision with root package name */
    private boolean f1792e;

    /* renamed from: f  reason: collision with root package name */
    c.a<Void> f1793f;

    /* renamed from: g  reason: collision with root package name */
    boolean f1794g;

    /* JADX INFO: Access modifiers changed from: package-private */
    public e3(t tVar, s.y yVar, Executor executor) {
        this.f1788a = tVar;
        this.f1791d = executor;
        this.f1790c = v.f.c(yVar);
        tVar.u(new t.c() { // from class: androidx.camera.camera2.internal.b3
            @Override // androidx.camera.camera2.internal.t.c
            public final boolean a(TotalCaptureResult totalCaptureResult) {
                boolean i8;
                i8 = e3.this.i(totalCaptureResult);
                return i8;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object h(final boolean z4, final c.a aVar) {
        this.f1791d.execute(new Runnable() { // from class: androidx.camera.camera2.internal.d3
            @Override // java.lang.Runnable
            public final void run() {
                e3.this.g(aVar, z4);
            }
        });
        return "enableTorch: " + z4;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean i(TotalCaptureResult totalCaptureResult) {
        if (this.f1793f != null) {
            Integer num = (Integer) totalCaptureResult.getRequest().get(CaptureRequest.FLASH_MODE);
            if ((num != null && num.intValue() == 2) == this.f1794g) {
                this.f1793f.c(null);
                this.f1793f = null;
            }
        }
        return false;
    }

    private <T> void k(androidx.lifecycle.p<T> pVar, T t8) {
        if (androidx.camera.core.impl.utils.m.b()) {
            pVar.o(t8);
        } else {
            pVar.l(t8);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public com.google.common.util.concurrent.d<Void> d(final boolean z4) {
        if (this.f1790c) {
            k(this.f1789b, Integer.valueOf(z4 ? 1 : 0));
            return androidx.concurrent.futures.c.a(new c.InterfaceC0024c() { // from class: androidx.camera.camera2.internal.c3
                @Override // androidx.concurrent.futures.c.InterfaceC0024c
                public final Object a(c.a aVar) {
                    Object h8;
                    h8 = e3.this.h(z4, aVar);
                    return h8;
                }
            });
        }
        androidx.camera.core.p1.a("TorchControl", "Unable to enableTorch due to there is no flash unit.");
        return a0.f.f(new IllegalStateException("No flash unit"));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: e */
    public void g(c.a<Void> aVar, boolean z4) {
        if (!this.f1790c) {
            if (aVar != null) {
                aVar.f(new IllegalStateException("No flash unit"));
            }
        } else if (!this.f1792e) {
            k(this.f1789b, 0);
            if (aVar != null) {
                aVar.f(new CameraControl.OperationCanceledException("Camera is not active."));
            }
        } else {
            this.f1794g = z4;
            this.f1788a.x(z4);
            k(this.f1789b, Integer.valueOf(z4 ? 1 : 0));
            c.a<Void> aVar2 = this.f1793f;
            if (aVar2 != null) {
                aVar2.f(new CameraControl.OperationCanceledException("There is a new enableTorch being set"));
            }
            this.f1793f = aVar;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public LiveData<Integer> f() {
        return this.f1789b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void j(boolean z4) {
        if (this.f1792e == z4) {
            return;
        }
        this.f1792e = z4;
        if (z4) {
            return;
        }
        if (this.f1794g) {
            this.f1794g = false;
            this.f1788a.x(false);
            k(this.f1789b, 0);
        }
        c.a<Void> aVar = this.f1793f;
        if (aVar != null) {
            aVar.f(new CameraControl.OperationCanceledException("Camera is not active."));
            this.f1793f = null;
        }
    }
}
