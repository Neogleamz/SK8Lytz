package androidx.fragment.app;

import android.view.View;
import android.view.ViewGroup;
import androidx.core.view.c0;
import androidx.lifecycle.Lifecycle;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class r {

    /* renamed from: a  reason: collision with root package name */
    private final g f5663a;

    /* renamed from: b  reason: collision with root package name */
    private final ClassLoader f5664b;

    /* renamed from: d  reason: collision with root package name */
    int f5666d;

    /* renamed from: e  reason: collision with root package name */
    int f5667e;

    /* renamed from: f  reason: collision with root package name */
    int f5668f;

    /* renamed from: g  reason: collision with root package name */
    int f5669g;

    /* renamed from: h  reason: collision with root package name */
    int f5670h;

    /* renamed from: i  reason: collision with root package name */
    boolean f5671i;

    /* renamed from: k  reason: collision with root package name */
    String f5673k;

    /* renamed from: l  reason: collision with root package name */
    int f5674l;

    /* renamed from: m  reason: collision with root package name */
    CharSequence f5675m;

    /* renamed from: n  reason: collision with root package name */
    int f5676n;

    /* renamed from: o  reason: collision with root package name */
    CharSequence f5677o;

    /* renamed from: p  reason: collision with root package name */
    ArrayList<String> f5678p;
    ArrayList<String> q;

    /* renamed from: s  reason: collision with root package name */
    ArrayList<Runnable> f5680s;

    /* renamed from: c  reason: collision with root package name */
    ArrayList<a> f5665c = new ArrayList<>();

    /* renamed from: j  reason: collision with root package name */
    boolean f5672j = true;

    /* renamed from: r  reason: collision with root package name */
    boolean f5679r = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        int f5681a;

        /* renamed from: b  reason: collision with root package name */
        Fragment f5682b;

        /* renamed from: c  reason: collision with root package name */
        int f5683c;

        /* renamed from: d  reason: collision with root package name */
        int f5684d;

        /* renamed from: e  reason: collision with root package name */
        int f5685e;

        /* renamed from: f  reason: collision with root package name */
        int f5686f;

        /* renamed from: g  reason: collision with root package name */
        Lifecycle.State f5687g;

        /* renamed from: h  reason: collision with root package name */
        Lifecycle.State f5688h;

        /* JADX INFO: Access modifiers changed from: package-private */
        public a() {
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public a(int i8, Fragment fragment) {
            this.f5681a = i8;
            this.f5682b = fragment;
            Lifecycle.State state = Lifecycle.State.RESUMED;
            this.f5687g = state;
            this.f5688h = state;
        }

        a(int i8, Fragment fragment, Lifecycle.State state) {
            this.f5681a = i8;
            this.f5682b = fragment;
            this.f5687g = fragment.f5400f0;
            this.f5688h = state;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public r(g gVar, ClassLoader classLoader) {
        this.f5663a = gVar;
        this.f5664b = classLoader;
    }

    public r b(int i8, Fragment fragment, String str) {
        o(i8, fragment, str, 1);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public r c(ViewGroup viewGroup, Fragment fragment, String str) {
        fragment.R = viewGroup;
        return b(viewGroup.getId(), fragment, str);
    }

    public r d(Fragment fragment, String str) {
        o(0, fragment, str, 1);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e(a aVar) {
        this.f5665c.add(aVar);
        aVar.f5683c = this.f5666d;
        aVar.f5684d = this.f5667e;
        aVar.f5685e = this.f5668f;
        aVar.f5686f = this.f5669g;
    }

    public r f(View view, String str) {
        if (s.C()) {
            String N = c0.N(view);
            if (N == null) {
                throw new IllegalArgumentException("Unique transitionNames are required for all sharedElements");
            }
            if (this.f5678p == null) {
                this.f5678p = new ArrayList<>();
                this.q = new ArrayList<>();
            } else if (this.q.contains(str)) {
                throw new IllegalArgumentException("A shared element with the target name '" + str + "' has already been added to the transaction.");
            } else if (this.f5678p.contains(N)) {
                throw new IllegalArgumentException("A shared element with the source name '" + N + "' has already been added to the transaction.");
            }
            this.f5678p.add(N);
            this.q.add(str);
        }
        return this;
    }

    public r g(String str) {
        if (this.f5672j) {
            this.f5671i = true;
            this.f5673k = str;
            return this;
        }
        throw new IllegalStateException("This FragmentTransaction is not allowed to be added to the back stack.");
    }

    public r h(Fragment fragment) {
        e(new a(7, fragment));
        return this;
    }

    public abstract int i();

    public abstract int j();

    public abstract void k();

    public abstract void l();

    public r m(Fragment fragment) {
        e(new a(6, fragment));
        return this;
    }

    public r n() {
        if (this.f5671i) {
            throw new IllegalStateException("This transaction is already being added to the back stack");
        }
        this.f5672j = false;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void o(int i8, Fragment fragment, String str, int i9) {
        Class<?> cls = fragment.getClass();
        int modifiers = cls.getModifiers();
        if (cls.isAnonymousClass() || !Modifier.isPublic(modifiers) || (cls.isMemberClass() && !Modifier.isStatic(modifiers))) {
            throw new IllegalStateException("Fragment " + cls.getCanonicalName() + " must be a public static class to be  properly recreated from instance state.");
        }
        if (str != null) {
            String str2 = fragment.F;
            if (str2 != null && !str.equals(str2)) {
                throw new IllegalStateException("Can't change tag of fragment " + fragment + ": was " + fragment.F + " now " + str);
            }
            fragment.F = str;
        }
        if (i8 != 0) {
            if (i8 == -1) {
                throw new IllegalArgumentException("Can't add fragment " + fragment + " with tag " + str + " to container view with no id");
            }
            int i10 = fragment.C;
            if (i10 != 0 && i10 != i8) {
                throw new IllegalStateException("Can't change container ID of fragment " + fragment + ": was " + fragment.C + " now " + i8);
            }
            fragment.C = i8;
            fragment.E = i8;
        }
        e(new a(i9, fragment));
    }

    public r p(Fragment fragment) {
        e(new a(3, fragment));
        return this;
    }

    public r q(int i8, Fragment fragment) {
        return r(i8, fragment, null);
    }

    public r r(int i8, Fragment fragment, String str) {
        if (i8 != 0) {
            o(i8, fragment, str, 2);
            return this;
        }
        throw new IllegalArgumentException("Must use non-zero containerViewId");
    }

    public r s(int i8, int i9, int i10, int i11) {
        this.f5666d = i8;
        this.f5667e = i9;
        this.f5668f = i10;
        this.f5669g = i11;
        return this;
    }

    public r t(Fragment fragment, Lifecycle.State state) {
        e(new a(10, fragment, state));
        return this;
    }

    public r u(Fragment fragment) {
        e(new a(8, fragment));
        return this;
    }

    public r v(boolean z4) {
        this.f5679r = z4;
        return this;
    }
}
