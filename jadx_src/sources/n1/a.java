package n1;

import android.content.Context;
import android.net.Uri;
import android.view.InputEvent;
import cj.a0;
import cj.n;
import com.google.common.util.concurrent.d;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.internal.i;
import kotlinx.coroutines.CoroutineStart;
import mj.p;
import xj.g0;
import xj.h0;
import xj.t0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class a {

    /* renamed from: a  reason: collision with root package name */
    public static final b f21970a = new b(null);

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: n1.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class C0188a extends a {

        /* renamed from: b  reason: collision with root package name */
        private final o1.b f21971b;

        @kotlin.coroutines.jvm.internal.d(c = "androidx.privacysandbox.ads.adservices.java.measurement.MeasurementManagerFutures$Api33Ext5JavaImpl$deleteRegistrationsAsync$1", f = "MeasurementManagerFutures.kt", l = {122}, m = "invokeSuspend")
        /* renamed from: n1.a$a$a  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        static final class C0189a extends SuspendLambda implements p<g0, fj.c<? super a0>, Object> {

            /* renamed from: a  reason: collision with root package name */
            int f21972a;

            /* renamed from: c  reason: collision with root package name */
            final /* synthetic */ o1.a f21974c;

            C0189a(o1.a aVar, fj.c<? super C0189a> cVar) {
                super(2, cVar);
            }

            public final fj.c<a0> create(Object obj, fj.c<?> cVar) {
                return new C0189a(this.f21974c, cVar);
            }

            public final Object invoke(g0 g0Var, fj.c<? super a0> cVar) {
                return create(g0Var, cVar).invokeSuspend(a0.a);
            }

            public final Object invokeSuspend(Object obj) {
                Object e8 = kotlin.coroutines.intrinsics.a.e();
                int i8 = this.f21972a;
                if (i8 == 0) {
                    n.b(obj);
                    o1.b bVar = C0188a.this.f21971b;
                    o1.a aVar = this.f21974c;
                    this.f21972a = 1;
                    if (bVar.a(aVar, this) == e8) {
                        return e8;
                    }
                } else if (i8 != 1) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                } else {
                    n.b(obj);
                }
                return a0.a;
            }
        }

        @kotlin.coroutines.jvm.internal.d(c = "androidx.privacysandbox.ads.adservices.java.measurement.MeasurementManagerFutures$Api33Ext5JavaImpl$getMeasurementApiStatusAsync$1", f = "MeasurementManagerFutures.kt", l = {169}, m = "invokeSuspend")
        /* renamed from: n1.a$a$b */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        static final class b extends SuspendLambda implements p<g0, fj.c<? super Integer>, Object> {

            /* renamed from: a  reason: collision with root package name */
            int f21975a;

            b(fj.c<? super b> cVar) {
                super(2, cVar);
            }

            public final fj.c<a0> create(Object obj, fj.c<?> cVar) {
                return new b(cVar);
            }

            public final Object invoke(g0 g0Var, fj.c<? super Integer> cVar) {
                return create(g0Var, cVar).invokeSuspend(a0.a);
            }

            public final Object invokeSuspend(Object obj) {
                Object e8 = kotlin.coroutines.intrinsics.a.e();
                int i8 = this.f21975a;
                if (i8 == 0) {
                    n.b(obj);
                    o1.b bVar = C0188a.this.f21971b;
                    this.f21975a = 1;
                    obj = bVar.b(this);
                    if (obj == e8) {
                        return e8;
                    }
                } else if (i8 != 1) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                } else {
                    n.b(obj);
                }
                return obj;
            }
        }

        @kotlin.coroutines.jvm.internal.d(c = "androidx.privacysandbox.ads.adservices.java.measurement.MeasurementManagerFutures$Api33Ext5JavaImpl$registerSourceAsync$1", f = "MeasurementManagerFutures.kt", l = {133}, m = "invokeSuspend")
        /* renamed from: n1.a$a$c */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        static final class c extends SuspendLambda implements p<g0, fj.c<? super a0>, Object> {

            /* renamed from: a  reason: collision with root package name */
            int f21977a;

            /* renamed from: c  reason: collision with root package name */
            final /* synthetic */ Uri f21979c;

            /* renamed from: d  reason: collision with root package name */
            final /* synthetic */ InputEvent f21980d;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            c(Uri uri, InputEvent inputEvent, fj.c<? super c> cVar) {
                super(2, cVar);
                this.f21979c = uri;
                this.f21980d = inputEvent;
            }

            public final fj.c<a0> create(Object obj, fj.c<?> cVar) {
                return new c(this.f21979c, this.f21980d, cVar);
            }

            public final Object invoke(g0 g0Var, fj.c<? super a0> cVar) {
                return create(g0Var, cVar).invokeSuspend(a0.a);
            }

            public final Object invokeSuspend(Object obj) {
                Object e8 = kotlin.coroutines.intrinsics.a.e();
                int i8 = this.f21977a;
                if (i8 == 0) {
                    n.b(obj);
                    o1.b bVar = C0188a.this.f21971b;
                    Uri uri = this.f21979c;
                    InputEvent inputEvent = this.f21980d;
                    this.f21977a = 1;
                    if (bVar.c(uri, inputEvent, this) == e8) {
                        return e8;
                    }
                } else if (i8 != 1) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                } else {
                    n.b(obj);
                }
                return a0.a;
            }
        }

        @kotlin.coroutines.jvm.internal.d(c = "androidx.privacysandbox.ads.adservices.java.measurement.MeasurementManagerFutures$Api33Ext5JavaImpl$registerTriggerAsync$1", f = "MeasurementManagerFutures.kt", l = {141}, m = "invokeSuspend")
        /* renamed from: n1.a$a$d */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        static final class d extends SuspendLambda implements p<g0, fj.c<? super a0>, Object> {

            /* renamed from: a  reason: collision with root package name */
            int f21981a;

            /* renamed from: c  reason: collision with root package name */
            final /* synthetic */ Uri f21983c;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            d(Uri uri, fj.c<? super d> cVar) {
                super(2, cVar);
                this.f21983c = uri;
            }

            public final fj.c<a0> create(Object obj, fj.c<?> cVar) {
                return new d(this.f21983c, cVar);
            }

            public final Object invoke(g0 g0Var, fj.c<? super a0> cVar) {
                return create(g0Var, cVar).invokeSuspend(a0.a);
            }

            public final Object invokeSuspend(Object obj) {
                Object e8 = kotlin.coroutines.intrinsics.a.e();
                int i8 = this.f21981a;
                if (i8 == 0) {
                    n.b(obj);
                    o1.b bVar = C0188a.this.f21971b;
                    Uri uri = this.f21983c;
                    this.f21981a = 1;
                    if (bVar.d(uri, this) == e8) {
                        return e8;
                    }
                } else if (i8 != 1) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                } else {
                    n.b(obj);
                }
                return a0.a;
            }
        }

        @kotlin.coroutines.jvm.internal.d(c = "androidx.privacysandbox.ads.adservices.java.measurement.MeasurementManagerFutures$Api33Ext5JavaImpl$registerWebSourceAsync$1", f = "MeasurementManagerFutures.kt", l = {151}, m = "invokeSuspend")
        /* renamed from: n1.a$a$e */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        static final class e extends SuspendLambda implements p<g0, fj.c<? super a0>, Object> {

            /* renamed from: a  reason: collision with root package name */
            int f21984a;

            /* renamed from: c  reason: collision with root package name */
            final /* synthetic */ o1.c f21986c;

            e(o1.c cVar, fj.c<? super e> cVar2) {
                super(2, cVar2);
            }

            public final fj.c<a0> create(Object obj, fj.c<?> cVar) {
                return new e(this.f21986c, cVar);
            }

            public final Object invoke(g0 g0Var, fj.c<? super a0> cVar) {
                return create(g0Var, cVar).invokeSuspend(a0.a);
            }

            public final Object invokeSuspend(Object obj) {
                Object e8 = kotlin.coroutines.intrinsics.a.e();
                int i8 = this.f21984a;
                if (i8 == 0) {
                    n.b(obj);
                    o1.b bVar = C0188a.this.f21971b;
                    o1.c cVar = this.f21986c;
                    this.f21984a = 1;
                    if (bVar.e(cVar, this) == e8) {
                        return e8;
                    }
                } else if (i8 != 1) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                } else {
                    n.b(obj);
                }
                return a0.a;
            }
        }

        @kotlin.coroutines.jvm.internal.d(c = "androidx.privacysandbox.ads.adservices.java.measurement.MeasurementManagerFutures$Api33Ext5JavaImpl$registerWebTriggerAsync$1", f = "MeasurementManagerFutures.kt", l = {161}, m = "invokeSuspend")
        /* renamed from: n1.a$a$f */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        static final class f extends SuspendLambda implements p<g0, fj.c<? super a0>, Object> {

            /* renamed from: a  reason: collision with root package name */
            int f21987a;

            /* renamed from: c  reason: collision with root package name */
            final /* synthetic */ o1.d f21989c;

            f(o1.d dVar, fj.c<? super f> cVar) {
                super(2, cVar);
            }

            public final fj.c<a0> create(Object obj, fj.c<?> cVar) {
                return new f(this.f21989c, cVar);
            }

            public final Object invoke(g0 g0Var, fj.c<? super a0> cVar) {
                return create(g0Var, cVar).invokeSuspend(a0.a);
            }

            public final Object invokeSuspend(Object obj) {
                Object e8 = kotlin.coroutines.intrinsics.a.e();
                int i8 = this.f21987a;
                if (i8 == 0) {
                    n.b(obj);
                    o1.b bVar = C0188a.this.f21971b;
                    o1.d dVar = this.f21989c;
                    this.f21987a = 1;
                    if (bVar.f(dVar, this) == e8) {
                        return e8;
                    }
                } else if (i8 != 1) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                } else {
                    n.b(obj);
                }
                return a0.a;
            }
        }

        public C0188a(o1.b bVar) {
            kotlin.jvm.internal.p.e(bVar, "mMeasurementManager");
            this.f21971b = bVar;
        }

        @Override // n1.a
        public com.google.common.util.concurrent.d<Integer> b() {
            return m1.b.c(xj.f.b(h0.a(t0.a()), (fj.f) null, (CoroutineStart) null, new b(null), 3, (Object) null), null, 1, null);
        }

        @Override // n1.a
        public com.google.common.util.concurrent.d<a0> c(Uri uri) {
            kotlin.jvm.internal.p.e(uri, "trigger");
            return m1.b.c(xj.f.b(h0.a(t0.a()), (fj.f) null, (CoroutineStart) null, new d(uri, null), 3, (Object) null), null, 1, null);
        }

        public com.google.common.util.concurrent.d<a0> e(o1.a aVar) {
            kotlin.jvm.internal.p.e(aVar, "deletionRequest");
            return m1.b.c(xj.f.b(h0.a(t0.a()), (fj.f) null, (CoroutineStart) null, new C0189a(aVar, null), 3, (Object) null), null, 1, null);
        }

        public com.google.common.util.concurrent.d<a0> f(Uri uri, InputEvent inputEvent) {
            kotlin.jvm.internal.p.e(uri, "attributionSource");
            return m1.b.c(xj.f.b(h0.a(t0.a()), (fj.f) null, (CoroutineStart) null, new c(uri, inputEvent, null), 3, (Object) null), null, 1, null);
        }

        public com.google.common.util.concurrent.d<a0> g(o1.c cVar) {
            kotlin.jvm.internal.p.e(cVar, "request");
            return m1.b.c(xj.f.b(h0.a(t0.a()), (fj.f) null, (CoroutineStart) null, new e(cVar, null), 3, (Object) null), null, 1, null);
        }

        public com.google.common.util.concurrent.d<a0> h(o1.d dVar) {
            kotlin.jvm.internal.p.e(dVar, "request");
            return m1.b.c(xj.f.b(h0.a(t0.a()), (fj.f) null, (CoroutineStart) null, new f(dVar, null), 3, (Object) null), null, 1, null);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {
        private b() {
        }

        public /* synthetic */ b(i iVar) {
            this();
        }

        public final a a(Context context) {
            kotlin.jvm.internal.p.e(context, "context");
            o1.b a9 = o1.b.f22214a.a(context);
            if (a9 != null) {
                return new C0188a(a9);
            }
            return null;
        }
    }

    public static final a a(Context context) {
        return f21970a.a(context);
    }

    public abstract d<Integer> b();

    public abstract d<a0> c(Uri uri);
}
