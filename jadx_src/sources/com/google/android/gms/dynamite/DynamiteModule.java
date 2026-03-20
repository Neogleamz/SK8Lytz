package com.google.android.gms.dynamite;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.os.Build;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.util.DynamiteApi;
import dalvik.system.DelegateLastClassLoader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class DynamiteModule {

    /* renamed from: h  reason: collision with root package name */
    private static Boolean f12021h = null;

    /* renamed from: i  reason: collision with root package name */
    private static String f12022i = null;

    /* renamed from: j  reason: collision with root package name */
    private static boolean f12023j = false;

    /* renamed from: k  reason: collision with root package name */
    private static int f12024k = -1;

    /* renamed from: l  reason: collision with root package name */
    private static Boolean f12025l;
    private static m q;

    /* renamed from: r  reason: collision with root package name */
    private static n f12030r;

    /* renamed from: a  reason: collision with root package name */
    private final Context f12031a;

    /* renamed from: m  reason: collision with root package name */
    private static final ThreadLocal f12026m = new ThreadLocal();

    /* renamed from: n  reason: collision with root package name */
    private static final ThreadLocal f12027n = new b();

    /* renamed from: o  reason: collision with root package name */
    private static final a.InterfaceC0126a f12028o = new c();

    /* renamed from: b  reason: collision with root package name */
    public static final a f12015b = new d();

    /* renamed from: c  reason: collision with root package name */
    public static final a f12016c = new e();

    /* renamed from: d  reason: collision with root package name */
    public static final a f12017d = new f();

    /* renamed from: e  reason: collision with root package name */
    public static final a f12018e = new g();

    /* renamed from: f  reason: collision with root package name */
    public static final a f12019f = new h();

    /* renamed from: g  reason: collision with root package name */
    public static final a f12020g = new i();

    /* renamed from: p  reason: collision with root package name */
    public static final a f12029p = new j();

    @DynamiteApi
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class DynamiteLoaderClassLoader {
        public static ClassLoader sClassLoader;
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class LoadingException extends Exception {
        /* synthetic */ LoadingException(String str, Throwable th, y6.d dVar) {
            super(str, th);
        }

        /* synthetic */ LoadingException(String str, y6.d dVar) {
            super(str);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a {

        /* renamed from: com.google.android.gms.dynamite.DynamiteModule$a$a  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public interface InterfaceC0126a {
            int a(Context context, String str, boolean z4);

            int b(Context context, String str);
        }

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static class b {

            /* renamed from: a  reason: collision with root package name */
            public int f12032a = 0;

            /* renamed from: b  reason: collision with root package name */
            public int f12033b = 0;

            /* renamed from: c  reason: collision with root package name */
            public int f12034c = 0;
        }

        b a(Context context, String str, InterfaceC0126a interfaceC0126a);
    }

    private DynamiteModule(Context context) {
        n6.j.l(context);
        this.f12031a = context;
    }

    public static int a(Context context, String str) {
        try {
            ClassLoader classLoader = context.getApplicationContext().getClassLoader();
            Class<?> loadClass = classLoader.loadClass("com.google.android.gms.dynamite.descriptors." + str + ".ModuleDescriptor");
            Field declaredField = loadClass.getDeclaredField("MODULE_ID");
            Field declaredField2 = loadClass.getDeclaredField("MODULE_VERSION");
            if (n6.i.a(declaredField.get(null), str)) {
                return declaredField2.getInt(null);
            }
            String valueOf = String.valueOf(declaredField.get(null));
            Log.e("DynamiteModule", "Module descriptor id '" + valueOf + "' didn't match expected id '" + str + "'");
            return 0;
        } catch (ClassNotFoundException unused) {
            Log.w("DynamiteModule", "Local module descriptor class for " + str + " not found.");
            return 0;
        } catch (Exception e8) {
            Log.e("DynamiteModule", "Failed to load module descriptor class: ".concat(String.valueOf(e8.getMessage())));
            return 0;
        }
    }

    public static int b(Context context, String str) {
        return e(context, str, false);
    }

    /* JADX WARN: Removed duplicated region for block: B:109:0x0236  */
    /* JADX WARN: Removed duplicated region for block: B:110:0x023c  */
    /* JADX WARN: Removed duplicated region for block: B:113:0x0249  */
    @com.google.errorprone.annotations.ResultIgnorabilityUnspecified
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static com.google.android.gms.dynamite.DynamiteModule d(android.content.Context r16, com.google.android.gms.dynamite.DynamiteModule.a r17, java.lang.String r18) {
        /*
            Method dump skipped, instructions count: 720
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.dynamite.DynamiteModule.d(android.content.Context, com.google.android.gms.dynamite.DynamiteModule$a, java.lang.String):com.google.android.gms.dynamite.DynamiteModule");
    }

    public static int e(Context context, String str, boolean z4) {
        Field declaredField;
        Throwable th;
        RemoteException e8;
        Cursor cursor;
        try {
            synchronized (DynamiteModule.class) {
                Boolean bool = f12021h;
                Cursor cursor2 = null;
                int i8 = 0;
                if (bool == null) {
                    try {
                        declaredField = context.getApplicationContext().getClassLoader().loadClass(DynamiteLoaderClassLoader.class.getName()).getDeclaredField("sClassLoader");
                    } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException e9) {
                        String obj = e9.toString();
                        Log.w("DynamiteModule", "Failed to load module via V2: " + obj);
                        bool = Boolean.FALSE;
                    }
                    synchronized (declaredField.getDeclaringClass()) {
                        ClassLoader classLoader = (ClassLoader) declaredField.get(null);
                        if (classLoader != ClassLoader.getSystemClassLoader()) {
                            if (classLoader != null) {
                                try {
                                    h(classLoader);
                                } catch (LoadingException unused) {
                                }
                                bool = Boolean.TRUE;
                                f12021h = bool;
                            } else if (!j(context)) {
                                return 0;
                            } else {
                                if (!f12023j) {
                                    Boolean bool2 = Boolean.TRUE;
                                    if (!bool2.equals(null)) {
                                        try {
                                            int f5 = f(context, str, z4, true);
                                            String str2 = f12022i;
                                            if (str2 != null && !str2.isEmpty()) {
                                                ClassLoader a9 = y6.b.a();
                                                if (a9 == null) {
                                                    if (Build.VERSION.SDK_INT >= 29) {
                                                        String str3 = f12022i;
                                                        n6.j.l(str3);
                                                        a9 = new DelegateLastClassLoader(str3, ClassLoader.getSystemClassLoader());
                                                    } else {
                                                        String str4 = f12022i;
                                                        n6.j.l(str4);
                                                        a9 = new com.google.android.gms.dynamite.a(str4, ClassLoader.getSystemClassLoader());
                                                    }
                                                }
                                                h(a9);
                                                declaredField.set(null, a9);
                                                f12021h = bool2;
                                                return f5;
                                            }
                                            return f5;
                                        } catch (LoadingException unused2) {
                                            declaredField.set(null, ClassLoader.getSystemClassLoader());
                                        }
                                    }
                                }
                                declaredField.set(null, ClassLoader.getSystemClassLoader());
                            }
                        }
                        bool = Boolean.FALSE;
                        f12021h = bool;
                    }
                }
                if (bool.booleanValue()) {
                    try {
                        return f(context, str, z4, false);
                    } catch (LoadingException e10) {
                        String message = e10.getMessage();
                        Log.w("DynamiteModule", "Failed to retrieve remote module version: " + message);
                        return 0;
                    }
                }
                m k8 = k(context);
                if (k8 != null) {
                    try {
                        try {
                            int f8 = k8.f();
                            if (f8 >= 3) {
                                k kVar = (k) f12026m.get();
                                if (kVar == null || (cursor = kVar.f12035a) == null) {
                                    Cursor cursor3 = (Cursor) x6.b.f(k8.y(x6.b.g(context), str, z4, ((Long) f12027n.get()).longValue()));
                                    if (cursor3 != null) {
                                        try {
                                            if (cursor3.moveToFirst()) {
                                                int i9 = cursor3.getInt(0);
                                                if (i9 <= 0 || !i(cursor3)) {
                                                    cursor2 = cursor3;
                                                }
                                                if (cursor2 != null) {
                                                    cursor2.close();
                                                }
                                                i8 = i9;
                                            }
                                        } catch (RemoteException e11) {
                                            e8 = e11;
                                            cursor2 = cursor3;
                                            String message2 = e8.getMessage();
                                            Log.w("DynamiteModule", "Failed to retrieve remote module version: " + message2);
                                            if (cursor2 != null) {
                                                cursor2.close();
                                            }
                                            return i8;
                                        } catch (Throwable th2) {
                                            th = th2;
                                            cursor2 = cursor3;
                                            if (cursor2 != null) {
                                                cursor2.close();
                                            }
                                            throw th;
                                        }
                                    }
                                    Log.w("DynamiteModule", "Failed to retrieve remote module version.");
                                    if (cursor3 != null) {
                                        cursor3.close();
                                    }
                                } else {
                                    i8 = cursor.getInt(0);
                                }
                            } else if (f8 == 2) {
                                Log.w("DynamiteModule", "IDynamite loader version = 2, no high precision latency measurement.");
                                i8 = k8.k(x6.b.g(context), str, z4);
                            } else {
                                Log.w("DynamiteModule", "IDynamite loader version < 2, falling back to getModuleVersion2");
                                i8 = k8.g(x6.b.g(context), str, z4);
                            }
                        } catch (RemoteException e12) {
                            e8 = e12;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                    }
                }
                return i8;
            }
        } catch (Throwable th4) {
            u6.f.a(context, th4);
            throw th4;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:40:0x009f, code lost:
        r8.close();
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:56:0x00dc  */
    /* JADX WARN: Type inference failed for: r0v0 */
    /* JADX WARN: Type inference failed for: r0v1, types: [android.database.Cursor] */
    /* JADX WARN: Type inference failed for: r0v2 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static int f(android.content.Context r8, java.lang.String r9, boolean r10, boolean r11) {
        /*
            Method dump skipped, instructions count: 224
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.dynamite.DynamiteModule.f(android.content.Context, java.lang.String, boolean, boolean):int");
    }

    private static DynamiteModule g(Context context, String str) {
        Log.i("DynamiteModule", "Selected local version of ".concat(String.valueOf(str)));
        return new DynamiteModule(context);
    }

    private static void h(ClassLoader classLoader) {
        n nVar;
        try {
            IBinder iBinder = (IBinder) classLoader.loadClass("com.google.android.gms.dynamiteloader.DynamiteLoaderV2").getConstructor(new Class[0]).newInstance(new Object[0]);
            if (iBinder == null) {
                nVar = null;
            } else {
                IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.dynamite.IDynamiteLoaderV2");
                nVar = queryLocalInterface instanceof n ? (n) queryLocalInterface : new n(iBinder);
            }
            f12030r = nVar;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e8) {
            throw new LoadingException("Failed to instantiate dynamite loader", e8, null);
        }
    }

    private static boolean i(Cursor cursor) {
        k kVar = (k) f12026m.get();
        if (kVar == null || kVar.f12035a != null) {
            return false;
        }
        kVar.f12035a = cursor;
        return true;
    }

    private static boolean j(Context context) {
        ApplicationInfo applicationInfo;
        Boolean bool = Boolean.TRUE;
        if (bool.equals(null) || bool.equals(f12025l)) {
            return true;
        }
        boolean z4 = false;
        if (f12025l == null) {
            ProviderInfo resolveContentProvider = context.getPackageManager().resolveContentProvider("com.google.android.gms.chimera", 0);
            if (com.google.android.gms.common.b.f().h(context, 10000000) == 0 && resolveContentProvider != null && "com.google.android.gms".equals(resolveContentProvider.packageName)) {
                z4 = true;
            }
            Boolean valueOf = Boolean.valueOf(z4);
            f12025l = valueOf;
            z4 = valueOf.booleanValue();
            if (z4 && (applicationInfo = resolveContentProvider.applicationInfo) != null && (applicationInfo.flags & 129) == 0) {
                Log.i("DynamiteModule", "Non-system-image GmsCore APK, forcing V1");
                f12023j = true;
            }
        }
        if (!z4) {
            Log.e("DynamiteModule", "Invalid GmsCore APK, remote loading disabled.");
        }
        return z4;
    }

    private static m k(Context context) {
        m mVar;
        synchronized (DynamiteModule.class) {
            m mVar2 = q;
            if (mVar2 != null) {
                return mVar2;
            }
            try {
                IBinder iBinder = (IBinder) context.createPackageContext("com.google.android.gms", 3).getClassLoader().loadClass("com.google.android.gms.chimera.container.DynamiteLoaderImpl").newInstance();
                if (iBinder == null) {
                    mVar = null;
                } else {
                    IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.dynamite.IDynamiteLoader");
                    mVar = queryLocalInterface instanceof m ? (m) queryLocalInterface : new m(iBinder);
                }
                if (mVar != null) {
                    q = mVar;
                    return mVar;
                }
            } catch (Exception e8) {
                Log.e("DynamiteModule", "Failed to load IDynamiteLoader from GmsCore: " + e8.getMessage());
            }
            return null;
        }
    }

    public IBinder c(String str) {
        try {
            return (IBinder) this.f12031a.getClassLoader().loadClass(str).newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e8) {
            throw new LoadingException("Failed to instantiate module class: ".concat(String.valueOf(str)), e8, null);
        }
    }
}
