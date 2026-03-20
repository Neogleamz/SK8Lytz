package androidx.lifecycle;

import android.app.Application;
import f1.a;
import java.lang.reflect.InvocationTargetException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class f0 {

    /* renamed from: a  reason: collision with root package name */
    private final i0 f5869a;

    /* renamed from: b  reason: collision with root package name */
    private final b f5870b;

    /* renamed from: c  reason: collision with root package name */
    private final f1.a f5871c;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a extends c {

        /* renamed from: g  reason: collision with root package name */
        private static a f5873g;

        /* renamed from: e  reason: collision with root package name */
        private final Application f5875e;

        /* renamed from: f  reason: collision with root package name */
        public static final C0059a f5872f = new C0059a(null);

        /* renamed from: h  reason: collision with root package name */
        public static final a.b<Application> f5874h = C0059a.C0060a.f5876a;

        /* renamed from: androidx.lifecycle.f0$a$a  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static final class C0059a {

            /* renamed from: androidx.lifecycle.f0$a$a$a  reason: collision with other inner class name */
            /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
            private static final class C0060a implements a.b<Application> {

                /* renamed from: a  reason: collision with root package name */
                public static final C0060a f5876a = new C0060a();

                private C0060a() {
                }
            }

            private C0059a() {
            }

            public /* synthetic */ C0059a(kotlin.jvm.internal.i iVar) {
                this();
            }

            public final b a(j0 j0Var) {
                kotlin.jvm.internal.p.e(j0Var, "owner");
                return j0Var instanceof e ? ((e) j0Var).getDefaultViewModelProviderFactory() : c.f5879b.a();
            }

            public final a b(Application application) {
                kotlin.jvm.internal.p.e(application, "application");
                if (a.f5873g == null) {
                    a.f5873g = new a(application);
                }
                a aVar = a.f5873g;
                kotlin.jvm.internal.p.b(aVar);
                return aVar;
            }
        }

        public a() {
            this(null, 0);
        }

        /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
        public a(Application application) {
            this(application, 0);
            kotlin.jvm.internal.p.e(application, "application");
        }

        private a(Application application, int i8) {
            this.f5875e = application;
        }

        private final <T extends e0> T g(Class<T> cls, Application application) {
            if (androidx.lifecycle.a.class.isAssignableFrom(cls)) {
                try {
                    T newInstance = cls.getConstructor(Application.class).newInstance(application);
                    kotlin.jvm.internal.p.d(newInstance, "{\n                try {\n…          }\n            }");
                    return newInstance;
                } catch (IllegalAccessException e8) {
                    throw new RuntimeException("Cannot create an instance of " + cls, e8);
                } catch (InstantiationException e9) {
                    throw new RuntimeException("Cannot create an instance of " + cls, e9);
                } catch (NoSuchMethodException e10) {
                    throw new RuntimeException("Cannot create an instance of " + cls, e10);
                } catch (InvocationTargetException e11) {
                    throw new RuntimeException("Cannot create an instance of " + cls, e11);
                }
            }
            return (T) super.a(cls);
        }

        public static final a h(Application application) {
            return f5872f.b(application);
        }

        @Override // androidx.lifecycle.f0.c, androidx.lifecycle.f0.b
        public <T extends e0> T a(Class<T> cls) {
            kotlin.jvm.internal.p.e(cls, "modelClass");
            Application application = this.f5875e;
            if (application != null) {
                return (T) g(cls, application);
            }
            throw new UnsupportedOperationException("AndroidViewModelFactory constructed with empty constructor works only with create(modelClass: Class<T>, extras: CreationExtras).");
        }

        @Override // androidx.lifecycle.f0.b
        public <T extends e0> T b(Class<T> cls, f1.a aVar) {
            kotlin.jvm.internal.p.e(cls, "modelClass");
            kotlin.jvm.internal.p.e(aVar, "extras");
            if (this.f5875e != null) {
                return (T) a(cls);
            }
            Application application = (Application) aVar.a(f5874h);
            if (application != null) {
                return (T) g(cls, application);
            }
            if (androidx.lifecycle.a.class.isAssignableFrom(cls)) {
                throw new IllegalArgumentException("CreationExtras must have an application by `APPLICATION_KEY`");
            }
            return (T) super.a(cls);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b {

        /* renamed from: a  reason: collision with root package name */
        public static final a f5877a = a.f5878a;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static final class a {

            /* renamed from: a  reason: collision with root package name */
            static final /* synthetic */ a f5878a = new a();

            private a() {
            }
        }

        default <T extends e0> T a(Class<T> cls) {
            kotlin.jvm.internal.p.e(cls, "modelClass");
            throw new UnsupportedOperationException("Factory.create(String) is unsupported.  This Factory requires `CreationExtras` to be passed into `create` method.");
        }

        default <T extends e0> T b(Class<T> cls, f1.a aVar) {
            kotlin.jvm.internal.p.e(cls, "modelClass");
            kotlin.jvm.internal.p.e(aVar, "extras");
            return (T) a(cls);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class c implements b {

        /* renamed from: c  reason: collision with root package name */
        private static c f5880c;

        /* renamed from: b  reason: collision with root package name */
        public static final a f5879b = new a(null);

        /* renamed from: d  reason: collision with root package name */
        public static final a.b<String> f5881d = a.C0061a.f5882a;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static final class a {

            /* renamed from: androidx.lifecycle.f0$c$a$a  reason: collision with other inner class name */
            /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
            private static final class C0061a implements a.b<String> {

                /* renamed from: a  reason: collision with root package name */
                public static final C0061a f5882a = new C0061a();

                private C0061a() {
                }
            }

            private a() {
            }

            public /* synthetic */ a(kotlin.jvm.internal.i iVar) {
                this();
            }

            public final c a() {
                if (c.f5880c == null) {
                    c.f5880c = new c();
                }
                c cVar = c.f5880c;
                kotlin.jvm.internal.p.b(cVar);
                return cVar;
            }
        }

        @Override // androidx.lifecycle.f0.b
        public <T extends e0> T a(Class<T> cls) {
            kotlin.jvm.internal.p.e(cls, "modelClass");
            try {
                T newInstance = cls.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
                kotlin.jvm.internal.p.d(newInstance, "{\n                modelC…wInstance()\n            }");
                return newInstance;
            } catch (IllegalAccessException e8) {
                throw new RuntimeException("Cannot create an instance of " + cls, e8);
            } catch (InstantiationException e9) {
                throw new RuntimeException("Cannot create an instance of " + cls, e9);
            } catch (NoSuchMethodException e10) {
                throw new RuntimeException("Cannot create an instance of " + cls, e10);
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class d {
        public void c(e0 e0Var) {
            kotlin.jvm.internal.p.e(e0Var, "viewModel");
        }
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public f0(i0 i0Var, b bVar) {
        this(i0Var, bVar, null, 4, null);
        kotlin.jvm.internal.p.e(i0Var, "store");
        kotlin.jvm.internal.p.e(bVar, "factory");
    }

    public f0(i0 i0Var, b bVar, f1.a aVar) {
        kotlin.jvm.internal.p.e(i0Var, "store");
        kotlin.jvm.internal.p.e(bVar, "factory");
        kotlin.jvm.internal.p.e(aVar, "defaultCreationExtras");
        this.f5869a = i0Var;
        this.f5870b = bVar;
        this.f5871c = aVar;
    }

    public /* synthetic */ f0(i0 i0Var, b bVar, f1.a aVar, int i8, kotlin.jvm.internal.i iVar) {
        this(i0Var, bVar, (i8 & 4) != 0 ? a.C0169a.f19836b : aVar);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public f0(j0 j0Var) {
        this(j0Var.getViewModelStore(), a.f5872f.a(j0Var), g0.a(j0Var));
        kotlin.jvm.internal.p.e(j0Var, "owner");
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public f0(j0 j0Var, b bVar) {
        this(j0Var.getViewModelStore(), bVar, g0.a(j0Var));
        kotlin.jvm.internal.p.e(j0Var, "owner");
        kotlin.jvm.internal.p.e(bVar, "factory");
    }

    public <T extends e0> T a(Class<T> cls) {
        kotlin.jvm.internal.p.e(cls, "modelClass");
        String canonicalName = cls.getCanonicalName();
        if (canonicalName != null) {
            return (T) b("androidx.lifecycle.ViewModelProvider.DefaultKey:" + canonicalName, cls);
        }
        throw new IllegalArgumentException("Local and anonymous classes can not be ViewModels");
    }

    public <T extends e0> T b(String str, Class<T> cls) {
        T t8;
        kotlin.jvm.internal.p.e(str, "key");
        kotlin.jvm.internal.p.e(cls, "modelClass");
        T t9 = (T) this.f5869a.b(str);
        if (!cls.isInstance(t9)) {
            f1.d dVar = new f1.d(this.f5871c);
            dVar.c(c.f5881d, str);
            try {
                t8 = (T) this.f5870b.b(cls, dVar);
            } catch (AbstractMethodError unused) {
                t8 = (T) this.f5870b.a(cls);
            }
            this.f5869a.d(str, t8);
            return t8;
        }
        b bVar = this.f5870b;
        d dVar2 = bVar instanceof d ? (d) bVar : null;
        if (dVar2 != null) {
            kotlin.jvm.internal.p.b(t9);
            dVar2.c(t9);
        }
        kotlin.jvm.internal.p.c(t9, "null cannot be cast to non-null type T of androidx.lifecycle.ViewModelProvider.get");
        return t9;
    }
}
