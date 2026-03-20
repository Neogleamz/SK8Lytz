package androidx.loader.app;

import android.os.Bundle;
import android.util.Log;
import androidx.lifecycle.e0;
import androidx.lifecycle.f0;
import androidx.lifecycle.i0;
import androidx.lifecycle.j;
import androidx.lifecycle.p;
import androidx.lifecycle.q;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import k0.h;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class b extends androidx.loader.app.a {

    /* renamed from: c  reason: collision with root package name */
    static boolean f5938c = false;

    /* renamed from: a  reason: collision with root package name */
    private final j f5939a;

    /* renamed from: b  reason: collision with root package name */
    private final C0062b f5940b;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a<D> extends p<D> {

        /* renamed from: l  reason: collision with root package name */
        private final int f5941l;

        /* renamed from: m  reason: collision with root package name */
        private final Bundle f5942m;

        /* renamed from: n  reason: collision with root package name */
        private final g1.a<D> f5943n;

        /* renamed from: o  reason: collision with root package name */
        private j f5944o;

        @Override // androidx.lifecycle.LiveData
        protected void j() {
            if (b.f5938c) {
                Log.v("LoaderManager", "  Starting: " + this);
            }
            throw null;
        }

        @Override // androidx.lifecycle.LiveData
        protected void k() {
            if (b.f5938c) {
                Log.v("LoaderManager", "  Stopping: " + this);
            }
            throw null;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // androidx.lifecycle.LiveData
        public void m(q<? super D> qVar) {
            super.m(qVar);
            this.f5944o = null;
        }

        @Override // androidx.lifecycle.p, androidx.lifecycle.LiveData
        public void o(D d8) {
            super.o(d8);
        }

        g1.a<D> p(boolean z4) {
            if (b.f5938c) {
                Log.v("LoaderManager", "  Destroying: " + this);
            }
            throw null;
        }

        public void q(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            printWriter.print(str);
            printWriter.print("mId=");
            printWriter.print(this.f5941l);
            printWriter.print(" mArgs=");
            printWriter.println(this.f5942m);
            printWriter.print(str);
            printWriter.print("mLoader=");
            printWriter.println(this.f5943n);
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append("  ");
            throw null;
        }

        void r() {
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(64);
            sb.append("LoaderInfo{");
            sb.append(Integer.toHexString(System.identityHashCode(this)));
            sb.append(" #");
            sb.append(this.f5941l);
            sb.append(" : ");
            androidx.core.util.b.a(this.f5943n, sb);
            sb.append("}}");
            return sb.toString();
        }
    }

    /* renamed from: androidx.loader.app.b$b  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class C0062b extends e0 {

        /* renamed from: f  reason: collision with root package name */
        private static final f0.b f5945f = new a();

        /* renamed from: d  reason: collision with root package name */
        private h<a> f5946d = new h<>();

        /* renamed from: e  reason: collision with root package name */
        private boolean f5947e = false;

        /* renamed from: androidx.loader.app.b$b$a */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        static class a implements f0.b {
            a() {
            }

            @Override // androidx.lifecycle.f0.b
            public <T extends e0> T a(Class<T> cls) {
                return new C0062b();
            }
        }

        C0062b() {
        }

        static C0062b g(i0 i0Var) {
            return (C0062b) new f0(i0Var, f5945f).a(C0062b.class);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // androidx.lifecycle.e0
        public void d() {
            super.d();
            int o5 = this.f5946d.o();
            for (int i8 = 0; i8 < o5; i8++) {
                this.f5946d.q(i8).p(true);
            }
            this.f5946d.c();
        }

        public void f(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            if (this.f5946d.o() > 0) {
                printWriter.print(str);
                printWriter.println("Loaders:");
                String str2 = str + "    ";
                for (int i8 = 0; i8 < this.f5946d.o(); i8++) {
                    a q = this.f5946d.q(i8);
                    printWriter.print(str);
                    printWriter.print("  #");
                    printWriter.print(this.f5946d.k(i8));
                    printWriter.print(": ");
                    printWriter.println(q.toString());
                    q.q(str2, fileDescriptor, printWriter, strArr);
                }
            }
        }

        void h() {
            int o5 = this.f5946d.o();
            for (int i8 = 0; i8 < o5; i8++) {
                this.f5946d.q(i8).r();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public b(j jVar, i0 i0Var) {
        this.f5939a = jVar;
        this.f5940b = C0062b.g(i0Var);
    }

    @Override // androidx.loader.app.a
    @Deprecated
    public void a(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        this.f5940b.f(str, fileDescriptor, printWriter, strArr);
    }

    @Override // androidx.loader.app.a
    public void c() {
        this.f5940b.h();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder((int) RecognitionOptions.ITF);
        sb.append("LoaderManager{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(" in ");
        androidx.core.util.b.a(this.f5939a, sb);
        sb.append("}}");
        return sb.toString();
    }
}
