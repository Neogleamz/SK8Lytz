package androidx.navigation;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.a0;
import androidx.lifecycle.f0;
import androidx.lifecycle.i0;
import androidx.lifecycle.j0;
import java.util.UUID;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e implements androidx.lifecycle.j, j0, androidx.lifecycle.e, s1.d {

    /* renamed from: a  reason: collision with root package name */
    private final Context f6315a;

    /* renamed from: b  reason: collision with root package name */
    private final i f6316b;

    /* renamed from: c  reason: collision with root package name */
    private Bundle f6317c;

    /* renamed from: d  reason: collision with root package name */
    private final androidx.lifecycle.k f6318d;

    /* renamed from: e  reason: collision with root package name */
    private final s1.c f6319e;

    /* renamed from: f  reason: collision with root package name */
    final UUID f6320f;

    /* renamed from: g  reason: collision with root package name */
    private Lifecycle.State f6321g;

    /* renamed from: h  reason: collision with root package name */
    private Lifecycle.State f6322h;

    /* renamed from: j  reason: collision with root package name */
    private f f6323j;

    /* renamed from: k  reason: collision with root package name */
    private f0.b f6324k;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static /* synthetic */ class a {

        /* renamed from: a  reason: collision with root package name */
        static final /* synthetic */ int[] f6325a;

        static {
            int[] iArr = new int[Lifecycle.Event.values().length];
            f6325a = iArr;
            try {
                iArr[Lifecycle.Event.ON_CREATE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f6325a[Lifecycle.Event.ON_STOP.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f6325a[Lifecycle.Event.ON_START.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f6325a[Lifecycle.Event.ON_PAUSE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                f6325a[Lifecycle.Event.ON_RESUME.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                f6325a[Lifecycle.Event.ON_DESTROY.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                f6325a[Lifecycle.Event.ON_ANY.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public e(Context context, i iVar, Bundle bundle, androidx.lifecycle.j jVar, f fVar) {
        this(context, iVar, bundle, jVar, fVar, UUID.randomUUID(), null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public e(Context context, i iVar, Bundle bundle, androidx.lifecycle.j jVar, f fVar, UUID uuid, Bundle bundle2) {
        this.f6318d = new androidx.lifecycle.k(this);
        s1.c a9 = s1.c.a(this);
        this.f6319e = a9;
        this.f6321g = Lifecycle.State.CREATED;
        this.f6322h = Lifecycle.State.RESUMED;
        this.f6315a = context;
        this.f6320f = uuid;
        this.f6316b = iVar;
        this.f6317c = bundle;
        this.f6323j = fVar;
        a9.d(bundle2);
        if (jVar != null) {
            this.f6321g = jVar.getLifecycle().b();
        }
    }

    private static Lifecycle.State d(Lifecycle.Event event) {
        switch (a.f6325a[event.ordinal()]) {
            case 1:
            case 2:
                return Lifecycle.State.CREATED;
            case 3:
            case 4:
                return Lifecycle.State.STARTED;
            case 5:
                return Lifecycle.State.RESUMED;
            case 6:
                return Lifecycle.State.DESTROYED;
            default:
                throw new IllegalArgumentException("Unexpected event value " + event);
        }
    }

    public Bundle a() {
        return this.f6317c;
    }

    public i b() {
        return this.f6316b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Lifecycle.State c() {
        return this.f6322h;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e(Lifecycle.Event event) {
        this.f6321g = d(event);
        i();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void f(Bundle bundle) {
        this.f6317c = bundle;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void g(Bundle bundle) {
        this.f6319e.e(bundle);
    }

    @Override // androidx.lifecycle.e
    public f0.b getDefaultViewModelProviderFactory() {
        if (this.f6324k == null) {
            this.f6324k = new a0((Application) this.f6315a.getApplicationContext(), this, this.f6317c);
        }
        return this.f6324k;
    }

    @Override // androidx.lifecycle.j
    public Lifecycle getLifecycle() {
        return this.f6318d;
    }

    @Override // s1.d
    public androidx.savedstate.a getSavedStateRegistry() {
        return this.f6319e.b();
    }

    @Override // androidx.lifecycle.j0
    public i0 getViewModelStore() {
        f fVar = this.f6323j;
        if (fVar != null) {
            return fVar.h(this.f6320f);
        }
        throw new IllegalStateException("You must call setViewModelStore() on your NavHostController before accessing the ViewModelStore of a navigation graph.");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void h(Lifecycle.State state) {
        this.f6322h = state;
        i();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void i() {
        androidx.lifecycle.k kVar;
        Lifecycle.State state;
        if (this.f6321g.ordinal() < this.f6322h.ordinal()) {
            kVar = this.f6318d;
            state = this.f6321g;
        } else {
            kVar = this.f6318d;
            state = this.f6322h;
        }
        kVar.n(state);
    }
}
