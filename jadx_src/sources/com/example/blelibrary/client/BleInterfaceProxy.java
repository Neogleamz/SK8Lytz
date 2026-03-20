package com.example.blelibrary.client;

import android.util.Log;
import com.example.blelibrary.DisconnectException;
import com.example.blelibrary.client.BleInterfaceProxy;
import com.google.gson.JsonSyntaxException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Objects;
import java.util.UUID;
import x2.f;
import x2.g;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class BleInterfaceProxy {
    private static final String TAG = "com.example.blelibrary.client.BleInterfaceProxy";
    private com.example.blelibrary.client.a client;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a implements InvocationHandler {

        /* renamed from: a  reason: collision with root package name */
        private Class<?> f8868a;

        /* renamed from: b  reason: collision with root package name */
        private com.example.blelibrary.client.a f8869b;

        /* renamed from: com.example.blelibrary.client.BleInterfaceProxy$a$a  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class C0100a extends c3.e<Object> {
            C0100a() {
            }

            @Override // c3.e
            public void onError(Throwable th) {
                String str = BleInterfaceProxy.TAG;
                String message = th.getMessage();
                Objects.requireNonNull(message);
                Log.i(str, message);
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static class b {

            /* renamed from: a  reason: collision with root package name */
            int f8871a;

            /* renamed from: b  reason: collision with root package name */
            Object f8872b;

            /* renamed from: c  reason: collision with root package name */
            int f8873c;

            /* renamed from: d  reason: collision with root package name */
            boolean f8874d;

            public b(int i8, Object obj, int i9, boolean z4) {
                this.f8871a = i8;
                this.f8872b = obj;
                this.f8873c = i9;
                this.f8874d = z4;
            }
        }

        public a(Class<?> cls, com.example.blelibrary.client.a aVar) {
            this.f8868a = cls;
            this.f8869b = aVar;
        }

        private b d(Method method, Object[] objArr) {
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            Object obj = null;
            int i8 = 3000;
            int i9 = -1;
            boolean z4 = false;
            for (int i10 = 0; i10 < objArr.length; i10++) {
                Annotation[] annotationArr = parameterAnnotations[i10];
                if (annotationArr.length == 1) {
                    Annotation annotation = annotationArr[0];
                    if (annotation instanceof x2.c) {
                        i9 = ((Integer) objArr[i10]).intValue();
                    } else if (annotation instanceof x2.d) {
                        obj = objArr[i10];
                    } else if (annotation instanceof g) {
                        i8 = ((Integer) objArr[i10]).intValue();
                    } else if (annotation instanceof x2.b) {
                        obj = objArr[i10];
                        z4 = true;
                    }
                }
            }
            if (i9 != -1) {
                if (obj != null) {
                    return new b(i9, obj, i8, z4);
                }
                throw new RuntimeException("Empty request body.");
            }
            throw new RuntimeException("cmd id not found.");
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void e(Method method, Object[] objArr, gi.e eVar) {
            RuntimeException runtimeException;
            Type genericReturnType = method.getGenericReturnType();
            if (genericReturnType instanceof ParameterizedType) {
                Type[] actualTypeArguments = ((ParameterizedType) genericReturnType).getActualTypeArguments();
                if (actualTypeArguments.length == 1) {
                    String str = BleInterfaceProxy.TAG;
                    Log.i(str, "publish : " + method.getName() + " method " + UUID.randomUUID().toString());
                    eVar.onNext(h(method, objArr, actualTypeArguments[0]));
                    eVar.onComplete();
                }
                runtimeException = new RuntimeException("types length error.");
            } else {
                runtimeException = new RuntimeException("type error.");
            }
            eVar.onError(runtimeException);
            eVar.onComplete();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void f(Method method, Object[] objArr, gi.e eVar) {
            b d8 = d(method, objArr);
            if (d8.f8874d) {
                this.f8869b.b(d8.f8871a, (byte[]) d8.f8872b);
            } else {
                this.f8869b.d(d8.f8871a, d8.f8872b);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void g(c3.c cVar, int i8, Object obj, Throwable th) {
            if (obj != null) {
                cVar.d(obj);
            } else {
                cVar.c(th);
            }
        }

        private Object h(Method method, Object[] objArr, Type type) {
            if (this.f8869b.g()) {
                b d8 = d(method, objArr);
                final c3.c cVar = new c3.c();
                e eVar = new e() { // from class: com.example.blelibrary.client.b
                    @Override // com.example.blelibrary.client.e
                    public final void a(int i8, Object obj, Throwable th) {
                        BleInterfaceProxy.a.g(c3.c.this, i8, obj, th);
                    }
                };
                try {
                    this.f8869b.a(d8.f8871a, eVar, type);
                    if (d8.f8874d) {
                        this.f8869b.b(d8.f8871a, (byte[]) d8.f8872b);
                    } else {
                        this.f8869b.d(d8.f8871a, d8.f8872b);
                    }
                    return cVar.b(d8.f8873c);
                } catch (Throwable th) {
                    try {
                        String str = BleInterfaceProxy.TAG;
                        Log.e(str, " throwable " + th);
                        if (th instanceof JsonSyntaxException) {
                            throw th;
                        }
                        throw new RuntimeException(th.getMessage());
                    } finally {
                        this.f8869b.e(d8.f8871a, eVar);
                    }
                }
            }
            throw new DisconnectException("device had been disconnect.");
        }

        @Override // java.lang.reflect.InvocationHandler
        public Object invoke(Object obj, final Method method, final Object[] objArr) {
            if (this.f8869b != null && ((x2.a) this.f8868a.getAnnotation(x2.a.class)) != null) {
                if (((f) method.getAnnotation(f.class)) != null) {
                    return gi.d.class.equals(method.getReturnType()) ? c3.d.a(new gi.f() { // from class: com.example.blelibrary.client.c
                        public final void a(gi.e eVar) {
                            BleInterfaceProxy.a.this.e(method, objArr, eVar);
                        }
                    }) : h(method, objArr, method.getGenericReturnType());
                } else if (((x2.e) method.getAnnotation(x2.e.class)) != null) {
                    c3.d.a(new gi.f() { // from class: com.example.blelibrary.client.d
                        public final void a(gi.e eVar) {
                            BleInterfaceProxy.a.this.f(method, objArr, eVar);
                        }
                    }).a(new C0100a());
                    return null;
                }
            }
            return method.invoke(this, objArr);
        }
    }

    public BleInterfaceProxy(com.example.blelibrary.client.a aVar) {
        this.client = aVar;
    }

    public <T> T getInterface(Class<T> cls) {
        return (T) Proxy.newProxyInstance(BleInterfaceProxy.class.getClassLoader(), new Class[]{cls}, new a(cls, this.client));
    }
}
