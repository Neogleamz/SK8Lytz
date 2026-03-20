package o1;

import android.adservices.measurement.DeletionRequest;
import android.adservices.measurement.MeasurementManager;
import android.adservices.measurement.WebSourceRegistrationRequest;
import android.adservices.measurement.WebTriggerRegistrationRequest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.InputEvent;
import androidx.core.os.n;
import androidx.profileinstaller.g;
import cj.a0;
import kotlin.coroutines.jvm.internal.f;
import kotlin.jvm.internal.i;
import kotlin.jvm.internal.p;
import xj.k;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class b {

    /* renamed from: a  reason: collision with root package name */
    public static final C0193b f22214a = new C0193b(null);

    @SuppressLint({"NewApi", "ClassVerificationFailure"})
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class a extends b {

        /* renamed from: b  reason: collision with root package name */
        private final MeasurementManager f22215b;

        public a(MeasurementManager measurementManager) {
            p.e(measurementManager, "mMeasurementManager");
            this.f22215b = measurementManager;
        }

        /* JADX WARN: Illegal instructions before constructor call */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public a(android.content.Context r2) {
            /*
                r1 = this;
                java.lang.String r0 = "context"
                kotlin.jvm.internal.p.e(r2, r0)
                java.lang.Class<android.adservices.measurement.MeasurementManager> r0 = android.adservices.measurement.MeasurementManager.class
                java.lang.Object r2 = r2.getSystemService(r0)
                java.lang.String r0 = "context.getSystemService…:class.java\n            )"
                kotlin.jvm.internal.p.d(r2, r0)
                android.adservices.measurement.MeasurementManager r2 = (android.adservices.measurement.MeasurementManager) r2
                r1.<init>(r2)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: o1.b.a.<init>(android.content.Context):void");
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final DeletionRequest k(o1.a aVar) {
            new DeletionRequest.Builder();
            throw null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final WebSourceRegistrationRequest l(c cVar) {
            throw null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final WebTriggerRegistrationRequest m(d dVar) {
            throw null;
        }

        @Override // o1.b
        public Object a(o1.a aVar, fj.c<? super a0> cVar) {
            k kVar = new k(kotlin.coroutines.intrinsics.a.c(cVar), 1);
            kVar.x();
            this.f22215b.deleteRegistrations(k(aVar), g.f6495a, n.a(kVar));
            Object u8 = kVar.u();
            if (u8 == kotlin.coroutines.intrinsics.a.e()) {
                f.c(cVar);
            }
            return u8 == kotlin.coroutines.intrinsics.a.e() ? u8 : a0.a;
        }

        @Override // o1.b
        public Object b(fj.c<? super Integer> cVar) {
            k kVar = new k(kotlin.coroutines.intrinsics.a.c(cVar), 1);
            kVar.x();
            this.f22215b.getMeasurementApiStatus(g.f6495a, n.a(kVar));
            Object u8 = kVar.u();
            if (u8 == kotlin.coroutines.intrinsics.a.e()) {
                f.c(cVar);
            }
            return u8;
        }

        @Override // o1.b
        public Object c(Uri uri, InputEvent inputEvent, fj.c<? super a0> cVar) {
            k kVar = new k(kotlin.coroutines.intrinsics.a.c(cVar), 1);
            kVar.x();
            this.f22215b.registerSource(uri, inputEvent, g.f6495a, n.a(kVar));
            Object u8 = kVar.u();
            if (u8 == kotlin.coroutines.intrinsics.a.e()) {
                f.c(cVar);
            }
            return u8 == kotlin.coroutines.intrinsics.a.e() ? u8 : a0.a;
        }

        @Override // o1.b
        public Object d(Uri uri, fj.c<? super a0> cVar) {
            k kVar = new k(kotlin.coroutines.intrinsics.a.c(cVar), 1);
            kVar.x();
            this.f22215b.registerTrigger(uri, g.f6495a, n.a(kVar));
            Object u8 = kVar.u();
            if (u8 == kotlin.coroutines.intrinsics.a.e()) {
                f.c(cVar);
            }
            return u8 == kotlin.coroutines.intrinsics.a.e() ? u8 : a0.a;
        }

        @Override // o1.b
        public Object e(c cVar, fj.c<? super a0> cVar2) {
            k kVar = new k(kotlin.coroutines.intrinsics.a.c(cVar2), 1);
            kVar.x();
            this.f22215b.registerWebSource(l(cVar), g.f6495a, n.a(kVar));
            Object u8 = kVar.u();
            if (u8 == kotlin.coroutines.intrinsics.a.e()) {
                f.c(cVar2);
            }
            return u8 == kotlin.coroutines.intrinsics.a.e() ? u8 : a0.a;
        }

        @Override // o1.b
        public Object f(d dVar, fj.c<? super a0> cVar) {
            k kVar = new k(kotlin.coroutines.intrinsics.a.c(cVar), 1);
            kVar.x();
            this.f22215b.registerWebTrigger(m(dVar), g.f6495a, n.a(kVar));
            Object u8 = kVar.u();
            if (u8 == kotlin.coroutines.intrinsics.a.e()) {
                f.c(cVar);
            }
            return u8 == kotlin.coroutines.intrinsics.a.e() ? u8 : a0.a;
        }
    }

    /* renamed from: o1.b$b  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class C0193b {
        private C0193b() {
        }

        public /* synthetic */ C0193b(i iVar) {
            this();
        }

        @SuppressLint({"NewApi", "ClassVerificationFailure"})
        public final b a(Context context) {
            p.e(context, "context");
            StringBuilder sb = new StringBuilder();
            sb.append("AdServicesInfo.version=");
            l1.a aVar = l1.a.f21560a;
            sb.append(aVar.a());
            Log.d("MeasurementManager", sb.toString());
            if (aVar.a() >= 5) {
                return new a(context);
            }
            return null;
        }
    }

    public abstract Object a(o1.a aVar, fj.c<? super a0> cVar);

    public abstract Object b(fj.c<? super Integer> cVar);

    public abstract Object c(Uri uri, InputEvent inputEvent, fj.c<? super a0> cVar);

    public abstract Object d(Uri uri, fj.c<? super a0> cVar);

    public abstract Object e(c cVar, fj.c<? super a0> cVar2);

    public abstract Object f(d dVar, fj.c<? super a0> cVar);
}
