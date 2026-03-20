package p2;

import android.animation.Animator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import p2.d;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class b<T, K extends p2.d> extends RecyclerView.g<K> {
    protected List<T> A;
    private RecyclerView B;
    private boolean C;
    private boolean D;
    private j E;
    private int F;
    private boolean G;
    private boolean H;
    private i I;
    private int J;

    /* renamed from: c  reason: collision with root package name */
    private boolean f22289c;

    /* renamed from: d  reason: collision with root package name */
    private boolean f22290d;

    /* renamed from: e  reason: collision with root package name */
    private boolean f22291e;

    /* renamed from: f  reason: collision with root package name */
    private s2.a f22292f;

    /* renamed from: g  reason: collision with root package name */
    private h f22293g;

    /* renamed from: h  reason: collision with root package name */
    private boolean f22294h;

    /* renamed from: i  reason: collision with root package name */
    private f f22295i;

    /* renamed from: j  reason: collision with root package name */
    private g f22296j;

    /* renamed from: k  reason: collision with root package name */
    private boolean f22297k;

    /* renamed from: l  reason: collision with root package name */
    private boolean f22298l;

    /* renamed from: m  reason: collision with root package name */
    private Interpolator f22299m;

    /* renamed from: n  reason: collision with root package name */
    private int f22300n;

    /* renamed from: o  reason: collision with root package name */
    private int f22301o;

    /* renamed from: p  reason: collision with root package name */
    private q2.b f22302p;
    private q2.b q;

    /* renamed from: r  reason: collision with root package name */
    private LinearLayout f22303r;

    /* renamed from: s  reason: collision with root package name */
    private LinearLayout f22304s;

    /* renamed from: t  reason: collision with root package name */
    private FrameLayout f22305t;

    /* renamed from: u  reason: collision with root package name */
    private boolean f22306u;

    /* renamed from: v  reason: collision with root package name */
    private boolean f22307v;

    /* renamed from: w  reason: collision with root package name */
    private boolean f22308w;

    /* renamed from: x  reason: collision with root package name */
    protected Context f22309x;

    /* renamed from: y  reason: collision with root package name */
    protected int f22310y;

    /* renamed from: z  reason: collision with root package name */
    protected LayoutInflater f22311z;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements View.OnClickListener {
        a() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (b.this.f22292f.e() == 3) {
                b.this.g0();
            }
            if (b.this.f22294h && b.this.f22292f.e() == 4) {
                b.this.g0();
            }
        }
    }

    /* renamed from: p2.b$b  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class C0194b extends GridLayoutManager.b {

        /* renamed from: e  reason: collision with root package name */
        final /* synthetic */ GridLayoutManager f22313e;

        C0194b(GridLayoutManager gridLayoutManager) {
            this.f22313e = gridLayoutManager;
        }

        @Override // androidx.recyclerview.widget.GridLayoutManager.b
        public int f(int i8) {
            int e8 = b.this.e(i8);
            if (e8 == 273 && b.this.d0()) {
                return 1;
            }
            if (e8 == 819 && b.this.c0()) {
                return 1;
            }
            if (b.this.I != null) {
                return b.this.b0(e8) ? this.f22313e.Z2() : b.this.I.a(this.f22313e, i8 - b.this.R());
            } else if (b.this.b0(e8)) {
                return this.f22313e.Z2();
            } else {
                return 1;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class c implements View.OnClickListener {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ p2.d f22315a;

        c(p2.d dVar) {
            this.f22315a = dVar;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            b.this.m0(view, this.f22315a.m() - b.this.R());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class d implements View.OnLongClickListener {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ p2.d f22317a;

        d(p2.d dVar) {
            this.f22317a = dVar;
        }

        @Override // android.view.View.OnLongClickListener
        public boolean onLongClick(View view) {
            return b.this.n0(view, this.f22317a.m() - b.this.R());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class e implements Runnable {
        e() {
        }

        @Override // java.lang.Runnable
        public void run() {
            b.this.f22293g.a();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface f {
        void a(b bVar, View view, int i8);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface g {
        boolean a(b bVar, View view, int i8);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface h {
        void a();
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface i {
        int a(GridLayoutManager gridLayoutManager, int i8);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface j {
        void a();
    }

    public b(int i8, List<T> list) {
        this.f22289c = false;
        this.f22290d = false;
        this.f22291e = false;
        this.f22292f = new s2.b();
        this.f22294h = false;
        this.f22297k = true;
        this.f22298l = false;
        this.f22299m = new LinearInterpolator();
        this.f22300n = 300;
        this.f22301o = -1;
        this.q = new q2.a();
        this.f22306u = true;
        this.F = 1;
        this.J = 1;
        this.A = list == null ? new ArrayList<>() : list;
        if (i8 != 0) {
            this.f22310y = i8;
        }
    }

    public b(List<T> list) {
        this(0, list);
    }

    private void G(RecyclerView.b0 b0Var) {
        if (this.f22298l) {
            if (!this.f22297k || b0Var.m() > this.f22301o) {
                q2.b bVar = this.f22302p;
                if (bVar == null) {
                    bVar = this.q;
                }
                for (Animator animator : bVar.a(b0Var.f6628a)) {
                    o0(animator, b0Var.m());
                }
                this.f22301o = b0Var.m();
            }
        }
    }

    private void H(int i8) {
        if (V() != 0 && i8 >= c() - this.J && this.f22292f.e() == 1) {
            this.f22292f.h(2);
            if (this.f22291e) {
                return;
            }
            this.f22291e = true;
            if (a0() != null) {
                a0().post(new e());
            } else {
                this.f22293g.a();
            }
        }
    }

    private void I(int i8) {
        j jVar;
        if (!e0() || f0() || i8 > this.F || (jVar = this.E) == null) {
            return;
        }
        jVar.a();
    }

    private void J(p2.d dVar) {
        View view;
        if (dVar == null || (view = dVar.f6628a) == null) {
            return;
        }
        if (Y() != null) {
            view.setOnClickListener(new c(dVar));
        }
        if (Z() != null) {
            view.setOnLongClickListener(new d(dVar));
        }
    }

    private K N(Class cls, View view) {
        try {
            if (!cls.isMemberClass() || Modifier.isStatic(cls.getModifiers())) {
                Constructor<T> declaredConstructor = cls.getDeclaredConstructor(View.class);
                declaredConstructor.setAccessible(true);
                return (K) declaredConstructor.newInstance(view);
            }
            Constructor<T> declaredConstructor2 = cls.getDeclaredConstructor(getClass(), View.class);
            declaredConstructor2.setAccessible(true);
            return (K) declaredConstructor2.newInstance(this, view);
        } catch (IllegalAccessException e8) {
            e8.printStackTrace();
            return null;
        } catch (InstantiationException e9) {
            e9.printStackTrace();
            return null;
        } catch (NoSuchMethodException e10) {
            e10.printStackTrace();
            return null;
        } catch (InvocationTargetException e11) {
            e11.printStackTrace();
            return null;
        }
    }

    private Class S(Class cls) {
        Type[] actualTypeArguments;
        Type genericSuperclass = cls.getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            for (Type type : ((ParameterizedType) genericSuperclass).getActualTypeArguments()) {
                if (type instanceof Class) {
                    Class cls2 = (Class) type;
                    if (p2.d.class.isAssignableFrom(cls2)) {
                        return cls2;
                    }
                } else if (type instanceof ParameterizedType) {
                    Type rawType = ((ParameterizedType) type).getRawType();
                    if (rawType instanceof Class) {
                        Class cls3 = (Class) rawType;
                        if (p2.d.class.isAssignableFrom(cls3)) {
                            return cls3;
                        }
                    } else {
                        continue;
                    }
                } else {
                    continue;
                }
            }
            return null;
        }
        return null;
    }

    private K X(ViewGroup viewGroup) {
        K L = L(U(this.f22292f.b(), viewGroup));
        L.f6628a.setOnClickListener(new a());
        return L;
    }

    protected abstract void K(K k8, T t8);

    /* JADX INFO: Access modifiers changed from: protected */
    public K L(View view) {
        Class cls = null;
        for (Class<?> cls2 = getClass(); cls == null && cls2 != null; cls2 = cls2.getSuperclass()) {
            cls = S(cls2);
        }
        K N = cls == null ? (K) new p2.d(view) : N(cls, view);
        return N != null ? N : (K) new p2.d(view);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public K M(ViewGroup viewGroup, int i8) {
        return L(U(i8, viewGroup));
    }

    protected abstract int O(int i8);

    public int P() {
        FrameLayout frameLayout = this.f22305t;
        return (frameLayout == null || frameLayout.getChildCount() == 0 || !this.f22306u || this.A.size() != 0) ? 0 : 1;
    }

    public int Q() {
        LinearLayout linearLayout = this.f22304s;
        return (linearLayout == null || linearLayout.getChildCount() == 0) ? 0 : 1;
    }

    public int R() {
        LinearLayout linearLayout = this.f22303r;
        return (linearLayout == null || linearLayout.getChildCount() == 0) ? 0 : 1;
    }

    public T T(int i8) {
        if (i8 < 0 || i8 >= this.A.size()) {
            return null;
        }
        return this.A.get(i8);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public View U(int i8, ViewGroup viewGroup) {
        return this.f22311z.inflate(i8, viewGroup, false);
    }

    public int V() {
        if (this.f22293g == null || !this.f22290d) {
            return 0;
        }
        return ((this.f22289c || !this.f22292f.g()) && this.A.size() != 0) ? 1 : 0;
    }

    public int W() {
        return R() + this.A.size() + Q();
    }

    public final f Y() {
        return this.f22295i;
    }

    public final g Z() {
        return this.f22296j;
    }

    protected RecyclerView a0() {
        return this.B;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean b0(int i8) {
        return i8 == 1365 || i8 == 273 || i8 == 819 || i8 == 546;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.g
    public int c() {
        int i8 = 1;
        if (P() != 1) {
            return V() + R() + this.A.size() + Q();
        }
        if (this.f22307v && R() != 0) {
            i8 = 2;
        }
        return (!this.f22308w || Q() == 0) ? i8 : i8 + 1;
    }

    public boolean c0() {
        return this.H;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.g
    public long d(int i8) {
        return i8;
    }

    public boolean d0() {
        return this.G;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.g
    public int e(int i8) {
        if (P() == 1) {
            boolean z4 = this.f22307v && R() != 0;
            return i8 != 0 ? i8 != 1 ? i8 != 2 ? 1365 : 819 : z4 ? 1365 : 819 : z4 ? 273 : 1365;
        }
        int R = R();
        if (i8 < R) {
            return 273;
        }
        int i9 = i8 - R;
        int size = this.A.size();
        return i9 < size ? O(i9) : i9 - size < Q() ? 819 : 546;
    }

    public boolean e0() {
        return this.C;
    }

    public boolean f0() {
        return this.D;
    }

    public void g0() {
        if (this.f22292f.e() == 2) {
            return;
        }
        this.f22292f.h(1);
        i(W());
    }

    @Override // androidx.recyclerview.widget.RecyclerView.g
    /* renamed from: h0 */
    public void r(K k8, int i8) {
        I(i8);
        H(i8);
        int l8 = k8.l();
        if (l8 != 0) {
            if (l8 == 273) {
                return;
            }
            if (l8 == 546) {
                this.f22292f.a(k8);
                return;
            } else if (l8 == 819 || l8 == 1365) {
                return;
            }
        }
        K(k8, T(i8 - R()));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public K i0(ViewGroup viewGroup, int i8) {
        return M(viewGroup, this.f22310y);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.g
    /* renamed from: j0 */
    public K t(ViewGroup viewGroup, int i8) {
        View view;
        K L;
        Context context = viewGroup.getContext();
        this.f22309x = context;
        this.f22311z = LayoutInflater.from(context);
        if (i8 != 273) {
            if (i8 == 546) {
                L = X(viewGroup);
            } else if (i8 == 819) {
                view = this.f22304s;
            } else if (i8 != 1365) {
                L = i0(viewGroup, i8);
                J(L);
            } else {
                view = this.f22305t;
            }
            L.N(this);
            return L;
        }
        view = this.f22303r;
        L = L(view);
        L.N(this);
        return L;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.g
    /* renamed from: k0 */
    public void w(K k8) {
        super.w(k8);
        int l8 = k8.l();
        if (l8 == 1365 || l8 == 273 || l8 == 819 || l8 == 546) {
            l0(k8);
        } else {
            G(k8);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void l0(RecyclerView.b0 b0Var) {
        if (b0Var.f6628a.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
            ((StaggeredGridLayoutManager.LayoutParams) b0Var.f6628a.getLayoutParams()).g(true);
        }
    }

    public void m0(View view, int i8) {
        Y().a(this, view, i8);
    }

    public boolean n0(View view, int i8) {
        return Z().a(this, view, i8);
    }

    protected void o0(Animator animator, int i8) {
        animator.setDuration(this.f22300n).start();
        animator.setInterpolator(this.f22299m);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.g
    public void q(RecyclerView recyclerView) {
        super.q(recyclerView);
        RecyclerView.o layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.h3(new C0194b(gridLayoutManager));
        }
    }
}
