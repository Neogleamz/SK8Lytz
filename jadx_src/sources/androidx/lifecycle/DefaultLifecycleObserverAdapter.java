package androidx.lifecycle;

import androidx.lifecycle.Lifecycle;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class DefaultLifecycleObserverAdapter implements h {

    /* renamed from: a  reason: collision with root package name */
    private final DefaultLifecycleObserver f5799a;

    /* renamed from: b  reason: collision with root package name */
    private final h f5800b;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public /* synthetic */ class a {

        /* renamed from: a  reason: collision with root package name */
        public static final /* synthetic */ int[] f5801a;

        static {
            int[] iArr = new int[Lifecycle.Event.values().length];
            try {
                iArr[Lifecycle.Event.ON_CREATE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[Lifecycle.Event.ON_START.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[Lifecycle.Event.ON_RESUME.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                iArr[Lifecycle.Event.ON_PAUSE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                iArr[Lifecycle.Event.ON_STOP.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                iArr[Lifecycle.Event.ON_DESTROY.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                iArr[Lifecycle.Event.ON_ANY.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            f5801a = iArr;
        }
    }

    public DefaultLifecycleObserverAdapter(DefaultLifecycleObserver defaultLifecycleObserver, h hVar) {
        kotlin.jvm.internal.p.e(defaultLifecycleObserver, "defaultLifecycleObserver");
        this.f5799a = defaultLifecycleObserver;
        this.f5800b = hVar;
    }

    @Override // androidx.lifecycle.h
    public void c(j jVar, Lifecycle.Event event) {
        kotlin.jvm.internal.p.e(jVar, "source");
        kotlin.jvm.internal.p.e(event, "event");
        switch (a.f5801a[event.ordinal()]) {
            case 1:
                this.f5799a.onCreate(jVar);
                break;
            case 2:
                this.f5799a.onStart(jVar);
                break;
            case 3:
                this.f5799a.onResume(jVar);
                break;
            case 4:
                this.f5799a.onPause(jVar);
                break;
            case 5:
                this.f5799a.onStop(jVar);
                break;
            case 6:
                this.f5799a.onDestroy(jVar);
                break;
            case 7:
                throw new IllegalArgumentException("ON_ANY must not been send by anybody");
        }
        h hVar = this.f5800b;
        if (hVar != null) {
            hVar.c(jVar, event);
        }
    }
}
