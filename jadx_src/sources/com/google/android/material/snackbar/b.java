package com.google.android.material.snackbar;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.lang.ref.WeakReference;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class b {

    /* renamed from: e  reason: collision with root package name */
    private static b f18461e;

    /* renamed from: a  reason: collision with root package name */
    private final Object f18462a = new Object();

    /* renamed from: b  reason: collision with root package name */
    private final Handler f18463b = new Handler(Looper.getMainLooper(), new a());

    /* renamed from: c  reason: collision with root package name */
    private c f18464c;

    /* renamed from: d  reason: collision with root package name */
    private c f18465d;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Handler.Callback {
        a() {
        }

        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            if (message.what != 0) {
                return false;
            }
            b.this.d((c) message.obj);
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.google.android.material.snackbar.b$b  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface InterfaceC0140b {
        void a();

        void b(int i8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class c {

        /* renamed from: a  reason: collision with root package name */
        final WeakReference<InterfaceC0140b> f18467a;

        /* renamed from: b  reason: collision with root package name */
        int f18468b;

        /* renamed from: c  reason: collision with root package name */
        boolean f18469c;

        c(int i8, InterfaceC0140b interfaceC0140b) {
            this.f18467a = new WeakReference<>(interfaceC0140b);
            this.f18468b = i8;
        }

        boolean a(InterfaceC0140b interfaceC0140b) {
            return interfaceC0140b != null && this.f18467a.get() == interfaceC0140b;
        }
    }

    private b() {
    }

    private boolean a(c cVar, int i8) {
        InterfaceC0140b interfaceC0140b = cVar.f18467a.get();
        if (interfaceC0140b != null) {
            this.f18463b.removeCallbacksAndMessages(cVar);
            interfaceC0140b.b(i8);
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static b c() {
        if (f18461e == null) {
            f18461e = new b();
        }
        return f18461e;
    }

    private boolean f(InterfaceC0140b interfaceC0140b) {
        c cVar = this.f18464c;
        return cVar != null && cVar.a(interfaceC0140b);
    }

    private boolean g(InterfaceC0140b interfaceC0140b) {
        c cVar = this.f18465d;
        return cVar != null && cVar.a(interfaceC0140b);
    }

    private void l(c cVar) {
        int i8 = cVar.f18468b;
        if (i8 == -2) {
            return;
        }
        if (i8 <= 0) {
            i8 = i8 == -1 ? 1500 : 2750;
        }
        this.f18463b.removeCallbacksAndMessages(cVar);
        Handler handler = this.f18463b;
        handler.sendMessageDelayed(Message.obtain(handler, 0, cVar), i8);
    }

    private void n() {
        c cVar = this.f18465d;
        if (cVar != null) {
            this.f18464c = cVar;
            this.f18465d = null;
            InterfaceC0140b interfaceC0140b = cVar.f18467a.get();
            if (interfaceC0140b != null) {
                interfaceC0140b.a();
            } else {
                this.f18464c = null;
            }
        }
    }

    public void b(InterfaceC0140b interfaceC0140b, int i8) {
        c cVar;
        synchronized (this.f18462a) {
            if (f(interfaceC0140b)) {
                cVar = this.f18464c;
            } else if (g(interfaceC0140b)) {
                cVar = this.f18465d;
            }
            a(cVar, i8);
        }
    }

    void d(c cVar) {
        synchronized (this.f18462a) {
            if (this.f18464c == cVar || this.f18465d == cVar) {
                a(cVar, 2);
            }
        }
    }

    public boolean e(InterfaceC0140b interfaceC0140b) {
        boolean z4;
        synchronized (this.f18462a) {
            z4 = f(interfaceC0140b) || g(interfaceC0140b);
        }
        return z4;
    }

    public void h(InterfaceC0140b interfaceC0140b) {
        synchronized (this.f18462a) {
            if (f(interfaceC0140b)) {
                this.f18464c = null;
                if (this.f18465d != null) {
                    n();
                }
            }
        }
    }

    public void i(InterfaceC0140b interfaceC0140b) {
        synchronized (this.f18462a) {
            if (f(interfaceC0140b)) {
                l(this.f18464c);
            }
        }
    }

    public void j(InterfaceC0140b interfaceC0140b) {
        synchronized (this.f18462a) {
            if (f(interfaceC0140b)) {
                c cVar = this.f18464c;
                if (!cVar.f18469c) {
                    cVar.f18469c = true;
                    this.f18463b.removeCallbacksAndMessages(cVar);
                }
            }
        }
    }

    public void k(InterfaceC0140b interfaceC0140b) {
        synchronized (this.f18462a) {
            if (f(interfaceC0140b)) {
                c cVar = this.f18464c;
                if (cVar.f18469c) {
                    cVar.f18469c = false;
                    l(cVar);
                }
            }
        }
    }

    public void m(int i8, InterfaceC0140b interfaceC0140b) {
        synchronized (this.f18462a) {
            if (f(interfaceC0140b)) {
                c cVar = this.f18464c;
                cVar.f18468b = i8;
                this.f18463b.removeCallbacksAndMessages(cVar);
                l(this.f18464c);
                return;
            }
            if (g(interfaceC0140b)) {
                this.f18465d.f18468b = i8;
            } else {
                this.f18465d = new c(i8, interfaceC0140b);
            }
            c cVar2 = this.f18464c;
            if (cVar2 == null || !a(cVar2, 4)) {
                this.f18464c = null;
                n();
            }
        }
    }
}
