package t;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.InputConfiguration;
import android.hardware.camera2.params.OutputConfiguration;
import android.hardware.camera2.params.SessionConfiguration;
import android.os.Build;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class h {

    /* renamed from: a  reason: collision with root package name */
    private final c f22825a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class a implements c {

        /* renamed from: a  reason: collision with root package name */
        private final SessionConfiguration f22826a;

        /* renamed from: b  reason: collision with root package name */
        private final List<t.b> f22827b;

        a(int i8, List<t.b> list, Executor executor, CameraCaptureSession.StateCallback stateCallback) {
            this(new SessionConfiguration(i8, h.h(list), executor, stateCallback));
        }

        a(Object obj) {
            SessionConfiguration sessionConfiguration = (SessionConfiguration) obj;
            this.f22826a = sessionConfiguration;
            this.f22827b = Collections.unmodifiableList(h.i(sessionConfiguration.getOutputConfigurations()));
        }

        @Override // t.h.c
        public t.a a() {
            return t.a.b(this.f22826a.getInputConfiguration());
        }

        @Override // t.h.c
        public Executor b() {
            return this.f22826a.getExecutor();
        }

        @Override // t.h.c
        public CameraCaptureSession.StateCallback c() {
            return this.f22826a.getStateCallback();
        }

        @Override // t.h.c
        public Object d() {
            return this.f22826a;
        }

        @Override // t.h.c
        public int e() {
            return this.f22826a.getSessionType();
        }

        public boolean equals(Object obj) {
            if (obj instanceof a) {
                return Objects.equals(this.f22826a, ((a) obj).f22826a);
            }
            return false;
        }

        @Override // t.h.c
        public void f(t.a aVar) {
            this.f22826a.setInputConfiguration((InputConfiguration) aVar.a());
        }

        @Override // t.h.c
        public List<t.b> g() {
            return this.f22827b;
        }

        @Override // t.h.c
        public void h(CaptureRequest captureRequest) {
            this.f22826a.setSessionParameters(captureRequest);
        }

        public int hashCode() {
            return this.f22826a.hashCode();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class b implements c {

        /* renamed from: a  reason: collision with root package name */
        private final List<t.b> f22828a;

        /* renamed from: b  reason: collision with root package name */
        private final CameraCaptureSession.StateCallback f22829b;

        /* renamed from: c  reason: collision with root package name */
        private final Executor f22830c;

        /* renamed from: d  reason: collision with root package name */
        private final int f22831d;

        /* renamed from: e  reason: collision with root package name */
        private t.a f22832e = null;

        /* renamed from: f  reason: collision with root package name */
        private CaptureRequest f22833f = null;

        b(int i8, List<t.b> list, Executor executor, CameraCaptureSession.StateCallback stateCallback) {
            this.f22831d = i8;
            this.f22828a = Collections.unmodifiableList(new ArrayList(list));
            this.f22829b = stateCallback;
            this.f22830c = executor;
        }

        @Override // t.h.c
        public t.a a() {
            return this.f22832e;
        }

        @Override // t.h.c
        public Executor b() {
            return this.f22830c;
        }

        @Override // t.h.c
        public CameraCaptureSession.StateCallback c() {
            return this.f22829b;
        }

        @Override // t.h.c
        public Object d() {
            return null;
        }

        @Override // t.h.c
        public int e() {
            return this.f22831d;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof b) {
                b bVar = (b) obj;
                if (Objects.equals(this.f22832e, bVar.f22832e) && this.f22831d == bVar.f22831d && this.f22828a.size() == bVar.f22828a.size()) {
                    for (int i8 = 0; i8 < this.f22828a.size(); i8++) {
                        if (!this.f22828a.get(i8).equals(bVar.f22828a.get(i8))) {
                            return false;
                        }
                    }
                    return true;
                }
            }
            return false;
        }

        @Override // t.h.c
        public void f(t.a aVar) {
            if (this.f22831d == 1) {
                throw new UnsupportedOperationException("Method not supported for high speed session types");
            }
            this.f22832e = aVar;
        }

        @Override // t.h.c
        public List<t.b> g() {
            return this.f22828a;
        }

        @Override // t.h.c
        public void h(CaptureRequest captureRequest) {
            this.f22833f = captureRequest;
        }

        public int hashCode() {
            int hashCode = this.f22828a.hashCode() ^ 31;
            int i8 = (hashCode << 5) - hashCode;
            t.a aVar = this.f22832e;
            int hashCode2 = (aVar == null ? 0 : aVar.hashCode()) ^ i8;
            return this.f22831d ^ ((hashCode2 << 5) - hashCode2);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private interface c {
        t.a a();

        Executor b();

        CameraCaptureSession.StateCallback c();

        Object d();

        int e();

        void f(t.a aVar);

        List<t.b> g();

        void h(CaptureRequest captureRequest);
    }

    public h(int i8, List<t.b> list, Executor executor, CameraCaptureSession.StateCallback stateCallback) {
        this.f22825a = Build.VERSION.SDK_INT < 28 ? new b(i8, list, executor, stateCallback) : new a(i8, list, executor, stateCallback);
    }

    public static List<OutputConfiguration> h(List<t.b> list) {
        ArrayList arrayList = new ArrayList(list.size());
        for (t.b bVar : list) {
            arrayList.add((OutputConfiguration) bVar.g());
        }
        return arrayList;
    }

    static List<t.b> i(List<OutputConfiguration> list) {
        ArrayList arrayList = new ArrayList(list.size());
        for (OutputConfiguration outputConfiguration : list) {
            arrayList.add(t.b.h(outputConfiguration));
        }
        return arrayList;
    }

    public Executor a() {
        return this.f22825a.b();
    }

    public t.a b() {
        return this.f22825a.a();
    }

    public List<t.b> c() {
        return this.f22825a.g();
    }

    public int d() {
        return this.f22825a.e();
    }

    public CameraCaptureSession.StateCallback e() {
        return this.f22825a.c();
    }

    public boolean equals(Object obj) {
        if (obj instanceof h) {
            return this.f22825a.equals(((h) obj).f22825a);
        }
        return false;
    }

    public void f(t.a aVar) {
        this.f22825a.f(aVar);
    }

    public void g(CaptureRequest captureRequest) {
        this.f22825a.h(captureRequest);
    }

    public int hashCode() {
        return this.f22825a.hashCode();
    }

    public Object j() {
        return this.f22825a.d();
    }
}
