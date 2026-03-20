package androidx.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.window.OnBackInvokedDispatcher;
import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.IntentSenderRequest;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.f0;
import androidx.lifecycle.i0;
import androidx.lifecycle.j0;
import androidx.lifecycle.k0;
import androidx.lifecycle.l0;
import androidx.lifecycle.u;
import androidx.lifecycle.x;
import androidx.savedstate.a;
import cj.a0;
import f.a;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ComponentActivity extends androidx.core.app.ComponentActivity implements e.a, j0, androidx.lifecycle.e, s1.d, n, androidx.activity.result.c, k {
    private static final String ACTIVITY_RESULT_TAG = "android:support:activity-result";
    private final ActivityResultRegistry mActivityResultRegistry;
    private int mContentLayoutId;
    final e.b mContextAwareHelper;
    private f0.b mDefaultFactory;
    private boolean mDispatchingOnMultiWindowModeChanged;
    private boolean mDispatchingOnPictureInPictureModeChanged;
    final j mFullyDrawnReporter;
    private final androidx.lifecycle.k mLifecycleRegistry;
    private final androidx.core.view.l mMenuHostHelper;
    private final AtomicInteger mNextLocalRequestCode;
    private final OnBackPressedDispatcher mOnBackPressedDispatcher;
    private final CopyOnWriteArrayList<androidx.core.util.a<Configuration>> mOnConfigurationChangedListeners;
    private final CopyOnWriteArrayList<androidx.core.util.a<androidx.core.app.h>> mOnMultiWindowModeChangedListeners;
    private final CopyOnWriteArrayList<androidx.core.util.a<Intent>> mOnNewIntentListeners;
    private final CopyOnWriteArrayList<androidx.core.util.a<androidx.core.app.p>> mOnPictureInPictureModeChangedListeners;
    private final CopyOnWriteArrayList<androidx.core.util.a<Integer>> mOnTrimMemoryListeners;
    final f mReportFullyDrawnExecutor;
    final s1.c mSavedStateRegistryController;
    private i0 mViewModelStore;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                ComponentActivity.super.onBackPressed();
            } catch (IllegalStateException e8) {
                if (!TextUtils.equals(e8.getMessage(), "Can not perform this action after onSaveInstanceState")) {
                    throw e8;
                }
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b extends ActivityResultRegistry {

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a implements Runnable {

            /* renamed from: a  reason: collision with root package name */
            final /* synthetic */ int f354a;

            /* renamed from: b  reason: collision with root package name */
            final /* synthetic */ a.C0167a f355b;

            a(int i8, a.C0167a c0167a) {
                this.f354a = i8;
                this.f355b = c0167a;
            }

            @Override // java.lang.Runnable
            public void run() {
                b.this.c(this.f354a, this.f355b.a());
            }
        }

        /* renamed from: androidx.activity.ComponentActivity$b$b  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class RunnableC0009b implements Runnable {

            /* renamed from: a  reason: collision with root package name */
            final /* synthetic */ int f357a;

            /* renamed from: b  reason: collision with root package name */
            final /* synthetic */ IntentSender.SendIntentException f358b;

            RunnableC0009b(int i8, IntentSender.SendIntentException sendIntentException) {
                this.f357a = i8;
                this.f358b = sendIntentException;
            }

            @Override // java.lang.Runnable
            public void run() {
                b.this.b(this.f357a, 0, new Intent().setAction("androidx.activity.result.contract.action.INTENT_SENDER_REQUEST").putExtra("androidx.activity.result.contract.extra.SEND_INTENT_EXCEPTION", this.f358b));
            }
        }

        b() {
        }

        @Override // androidx.activity.result.ActivityResultRegistry
        public <I, O> void f(int i8, f.a<I, O> aVar, I i9, androidx.core.app.c cVar) {
            ComponentActivity componentActivity = ComponentActivity.this;
            a.C0167a<O> b9 = aVar.b(componentActivity, i9);
            if (b9 != null) {
                new Handler(Looper.getMainLooper()).post(new a(i8, b9));
                return;
            }
            Intent a9 = aVar.a(componentActivity, i9);
            Bundle bundle = null;
            if (a9.getExtras() != null && a9.getExtras().getClassLoader() == null) {
                a9.setExtrasClassLoader(componentActivity.getClassLoader());
            }
            if (a9.hasExtra("androidx.activity.result.contract.extra.ACTIVITY_OPTIONS_BUNDLE")) {
                bundle = a9.getBundleExtra("androidx.activity.result.contract.extra.ACTIVITY_OPTIONS_BUNDLE");
                a9.removeExtra("androidx.activity.result.contract.extra.ACTIVITY_OPTIONS_BUNDLE");
            }
            Bundle bundle2 = bundle;
            if ("androidx.activity.result.contract.action.REQUEST_PERMISSIONS".equals(a9.getAction())) {
                String[] stringArrayExtra = a9.getStringArrayExtra("androidx.activity.result.contract.extra.PERMISSIONS");
                if (stringArrayExtra == null) {
                    stringArrayExtra = new String[0];
                }
                androidx.core.app.b.t(componentActivity, stringArrayExtra, i8);
            } else if (!"androidx.activity.result.contract.action.INTENT_SENDER_REQUEST".equals(a9.getAction())) {
                androidx.core.app.b.x(componentActivity, a9, i8, bundle2);
            } else {
                IntentSenderRequest intentSenderRequest = (IntentSenderRequest) a9.getParcelableExtra("androidx.activity.result.contract.extra.INTENT_SENDER_REQUEST");
                try {
                    androidx.core.app.b.y(componentActivity, intentSenderRequest.d(), i8, intentSenderRequest.a(), intentSenderRequest.b(), intentSenderRequest.c(), 0, bundle2);
                } catch (IntentSender.SendIntentException e8) {
                    new Handler(Looper.getMainLooper()).post(new RunnableC0009b(i8, e8));
                }
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class c {
        static void a(View view) {
            view.cancelPendingInputEvents();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class d {
        static OnBackInvokedDispatcher a(Activity activity) {
            return activity.getOnBackInvokedDispatcher();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class e {

        /* renamed from: a  reason: collision with root package name */
        Object f360a;

        /* renamed from: b  reason: collision with root package name */
        i0 f361b;

        e() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface f extends Executor {
        void B(View view);

        void h();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class g implements f, ViewTreeObserver.OnDrawListener, Runnable {

        /* renamed from: b  reason: collision with root package name */
        Runnable f363b;

        /* renamed from: a  reason: collision with root package name */
        final long f362a = SystemClock.uptimeMillis() + 10000;

        /* renamed from: c  reason: collision with root package name */
        boolean f364c = false;

        g() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void b() {
            Runnable runnable = this.f363b;
            if (runnable != null) {
                runnable.run();
                this.f363b = null;
            }
        }

        @Override // androidx.activity.ComponentActivity.f
        public void B(View view) {
            if (this.f364c) {
                return;
            }
            this.f364c = true;
            view.getViewTreeObserver().addOnDrawListener(this);
        }

        @Override // java.util.concurrent.Executor
        public void execute(Runnable runnable) {
            this.f363b = runnable;
            View decorView = ComponentActivity.this.getWindow().getDecorView();
            if (!this.f364c) {
                decorView.postOnAnimation(new Runnable() { // from class: androidx.activity.f
                    @Override // java.lang.Runnable
                    public final void run() {
                        ComponentActivity.g.this.b();
                    }
                });
            } else if (Looper.myLooper() == Looper.getMainLooper()) {
                decorView.invalidate();
            } else {
                decorView.postInvalidate();
            }
        }

        @Override // androidx.activity.ComponentActivity.f
        public void h() {
            ComponentActivity.this.getWindow().getDecorView().removeCallbacks(this);
            ComponentActivity.this.getWindow().getDecorView().getViewTreeObserver().removeOnDrawListener(this);
        }

        @Override // android.view.ViewTreeObserver.OnDrawListener
        public void onDraw() {
            Runnable runnable = this.f363b;
            if (runnable != null) {
                runnable.run();
                this.f363b = null;
                if (!ComponentActivity.this.mFullyDrawnReporter.c()) {
                    return;
                }
            } else if (SystemClock.uptimeMillis() <= this.f362a) {
                return;
            }
            this.f364c = false;
            ComponentActivity.this.getWindow().getDecorView().post(this);
        }

        @Override // java.lang.Runnable
        public void run() {
            ComponentActivity.this.getWindow().getDecorView().getViewTreeObserver().removeOnDrawListener(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class h implements f {

        /* renamed from: a  reason: collision with root package name */
        final Handler f366a = a();

        h() {
        }

        private Handler a() {
            Looper myLooper = Looper.myLooper();
            if (myLooper == null) {
                myLooper = Looper.getMainLooper();
            }
            return new Handler(myLooper);
        }

        @Override // androidx.activity.ComponentActivity.f
        public void B(View view) {
        }

        @Override // java.util.concurrent.Executor
        public void execute(Runnable runnable) {
            this.f366a.postAtFrontOfQueue(runnable);
        }

        @Override // androidx.activity.ComponentActivity.f
        public void h() {
        }
    }

    public ComponentActivity() {
        this.mContextAwareHelper = new e.b();
        this.mMenuHostHelper = new androidx.core.view.l(new Runnable() { // from class: androidx.activity.d
            @Override // java.lang.Runnable
            public final void run() {
                ComponentActivity.this.invalidateMenu();
            }
        });
        this.mLifecycleRegistry = new androidx.lifecycle.k(this);
        s1.c a9 = s1.c.a(this);
        this.mSavedStateRegistryController = a9;
        this.mOnBackPressedDispatcher = new OnBackPressedDispatcher(new a());
        f createFullyDrawnExecutor = createFullyDrawnExecutor();
        this.mReportFullyDrawnExecutor = createFullyDrawnExecutor;
        this.mFullyDrawnReporter = new j(createFullyDrawnExecutor, new mj.a() { // from class: androidx.activity.e
            public final Object invoke() {
                a0 lambda$new$0;
                lambda$new$0 = ComponentActivity.this.lambda$new$0();
                return lambda$new$0;
            }
        });
        this.mNextLocalRequestCode = new AtomicInteger();
        this.mActivityResultRegistry = new b();
        this.mOnConfigurationChangedListeners = new CopyOnWriteArrayList<>();
        this.mOnTrimMemoryListeners = new CopyOnWriteArrayList<>();
        this.mOnNewIntentListeners = new CopyOnWriteArrayList<>();
        this.mOnMultiWindowModeChangedListeners = new CopyOnWriteArrayList<>();
        this.mOnPictureInPictureModeChangedListeners = new CopyOnWriteArrayList<>();
        this.mDispatchingOnMultiWindowModeChanged = false;
        this.mDispatchingOnPictureInPictureModeChanged = false;
        if (getLifecycle() == null) {
            throw new IllegalStateException("getLifecycle() returned null in ComponentActivity's constructor. Please make sure you are lazily constructing your Lifecycle in the first call to getLifecycle() rather than relying on field initialization.");
        }
        int i8 = Build.VERSION.SDK_INT;
        if (i8 >= 19) {
            getLifecycle().a(new androidx.lifecycle.h() { // from class: androidx.activity.ComponentActivity.3
                @Override // androidx.lifecycle.h
                public void c(androidx.lifecycle.j jVar, Lifecycle.Event event) {
                    if (event == Lifecycle.Event.ON_STOP) {
                        Window window = ComponentActivity.this.getWindow();
                        View peekDecorView = window != null ? window.peekDecorView() : null;
                        if (peekDecorView != null) {
                            c.a(peekDecorView);
                        }
                    }
                }
            });
        }
        getLifecycle().a(new androidx.lifecycle.h() { // from class: androidx.activity.ComponentActivity.4
            @Override // androidx.lifecycle.h
            public void c(androidx.lifecycle.j jVar, Lifecycle.Event event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    ComponentActivity.this.mContextAwareHelper.b();
                    if (!ComponentActivity.this.isChangingConfigurations()) {
                        ComponentActivity.this.getViewModelStore().a();
                    }
                    ComponentActivity.this.mReportFullyDrawnExecutor.h();
                }
            }
        });
        getLifecycle().a(new androidx.lifecycle.h() { // from class: androidx.activity.ComponentActivity.5
            @Override // androidx.lifecycle.h
            public void c(androidx.lifecycle.j jVar, Lifecycle.Event event) {
                ComponentActivity.this.ensureViewModelStore();
                ComponentActivity.this.getLifecycle().c(this);
            }
        });
        a9.c();
        x.c(this);
        if (19 <= i8 && i8 <= 23) {
            getLifecycle().a(new ImmLeaksCleaner(this));
        }
        getSavedStateRegistry().h(ACTIVITY_RESULT_TAG, new a.c() { // from class: androidx.activity.b
            @Override // androidx.savedstate.a.c
            public final Bundle a() {
                Bundle lambda$new$1;
                lambda$new$1 = ComponentActivity.this.lambda$new$1();
                return lambda$new$1;
            }
        });
        addOnContextAvailableListener(new e.d() { // from class: androidx.activity.c
            @Override // e.d
            public final void a(Context context) {
                ComponentActivity.this.lambda$new$2(context);
            }
        });
    }

    public ComponentActivity(int i8) {
        this();
        this.mContentLayoutId = i8;
    }

    private f createFullyDrawnExecutor() {
        return Build.VERSION.SDK_INT < 16 ? new h() : new g();
    }

    private void initViewTreeOwners() {
        k0.a(getWindow().getDecorView(), this);
        l0.a(getWindow().getDecorView(), this);
        s1.e.a(getWindow().getDecorView(), this);
        q.a(getWindow().getDecorView(), this);
        p.a(getWindow().getDecorView(), this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ a0 lambda$new$0() {
        reportFullyDrawn();
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Bundle lambda$new$1() {
        Bundle bundle = new Bundle();
        this.mActivityResultRegistry.h(bundle);
        return bundle;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(Context context) {
        Bundle b9 = getSavedStateRegistry().b(ACTIVITY_RESULT_TAG);
        if (b9 != null) {
            this.mActivityResultRegistry.g(b9);
        }
    }

    @Override // android.app.Activity
    public void addContentView(@SuppressLint({"UnknownNullness", "MissingNullability"}) View view, @SuppressLint({"UnknownNullness", "MissingNullability"}) ViewGroup.LayoutParams layoutParams) {
        initViewTreeOwners();
        this.mReportFullyDrawnExecutor.B(getWindow().getDecorView());
        super.addContentView(view, layoutParams);
    }

    public void addMenuProvider(androidx.core.view.n nVar) {
        this.mMenuHostHelper.c(nVar);
    }

    public void addMenuProvider(androidx.core.view.n nVar, androidx.lifecycle.j jVar) {
        this.mMenuHostHelper.d(nVar, jVar);
    }

    @SuppressLint({"LambdaLast"})
    public void addMenuProvider(androidx.core.view.n nVar, androidx.lifecycle.j jVar, Lifecycle.State state) {
        this.mMenuHostHelper.e(nVar, jVar, state);
    }

    public final void addOnConfigurationChangedListener(androidx.core.util.a<Configuration> aVar) {
        this.mOnConfigurationChangedListeners.add(aVar);
    }

    public final void addOnContextAvailableListener(e.d dVar) {
        this.mContextAwareHelper.a(dVar);
    }

    public final void addOnMultiWindowModeChangedListener(androidx.core.util.a<androidx.core.app.h> aVar) {
        this.mOnMultiWindowModeChangedListeners.add(aVar);
    }

    public final void addOnNewIntentListener(androidx.core.util.a<Intent> aVar) {
        this.mOnNewIntentListeners.add(aVar);
    }

    public final void addOnPictureInPictureModeChangedListener(androidx.core.util.a<androidx.core.app.p> aVar) {
        this.mOnPictureInPictureModeChangedListeners.add(aVar);
    }

    public final void addOnTrimMemoryListener(androidx.core.util.a<Integer> aVar) {
        this.mOnTrimMemoryListeners.add(aVar);
    }

    void ensureViewModelStore() {
        if (this.mViewModelStore == null) {
            e eVar = (e) getLastNonConfigurationInstance();
            if (eVar != null) {
                this.mViewModelStore = eVar.f361b;
            }
            if (this.mViewModelStore == null) {
                this.mViewModelStore = new i0();
            }
        }
    }

    @Override // androidx.activity.result.c
    public final ActivityResultRegistry getActivityResultRegistry() {
        return this.mActivityResultRegistry;
    }

    @Override // androidx.lifecycle.e
    public f1.a getDefaultViewModelCreationExtras() {
        f1.d dVar = new f1.d();
        if (getApplication() != null) {
            dVar.c(f0.a.f5874h, getApplication());
        }
        dVar.c(x.f5928a, this);
        dVar.c(x.f5929b, this);
        if (getIntent() != null && getIntent().getExtras() != null) {
            dVar.c(x.f5930c, getIntent().getExtras());
        }
        return dVar;
    }

    @Override // androidx.lifecycle.e
    public f0.b getDefaultViewModelProviderFactory() {
        if (this.mDefaultFactory == null) {
            this.mDefaultFactory = new androidx.lifecycle.a0(getApplication(), this, getIntent() != null ? getIntent().getExtras() : null);
        }
        return this.mDefaultFactory;
    }

    public j getFullyDrawnReporter() {
        return this.mFullyDrawnReporter;
    }

    @Deprecated
    public Object getLastCustomNonConfigurationInstance() {
        e eVar = (e) getLastNonConfigurationInstance();
        if (eVar != null) {
            return eVar.f360a;
        }
        return null;
    }

    @Override // androidx.core.app.ComponentActivity, androidx.lifecycle.j
    public Lifecycle getLifecycle() {
        return this.mLifecycleRegistry;
    }

    @Override // androidx.activity.n
    public final OnBackPressedDispatcher getOnBackPressedDispatcher() {
        return this.mOnBackPressedDispatcher;
    }

    @Override // s1.d
    public final androidx.savedstate.a getSavedStateRegistry() {
        return this.mSavedStateRegistryController.b();
    }

    @Override // androidx.lifecycle.j0
    public i0 getViewModelStore() {
        if (getApplication() != null) {
            ensureViewModelStore();
            return this.mViewModelStore;
        }
        throw new IllegalStateException("Your activity is not yet attached to the Application instance. You can't request ViewModel before onCreate call.");
    }

    public void invalidateMenu() {
        invalidateOptionsMenu();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.Activity
    @Deprecated
    public void onActivityResult(int i8, int i9, Intent intent) {
        if (this.mActivityResultRegistry.b(i8, i9, intent)) {
            return;
        }
        super.onActivityResult(i8, i9, intent);
    }

    @Override // android.app.Activity
    public void onBackPressed() {
        this.mOnBackPressedDispatcher.e();
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        Iterator<androidx.core.util.a<Configuration>> it = this.mOnConfigurationChangedListeners.iterator();
        while (it.hasNext()) {
            it.next().accept(configuration);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        this.mSavedStateRegistryController.d(bundle);
        this.mContextAwareHelper.c(this);
        super.onCreate(bundle);
        u.e(this);
        if (androidx.core.os.a.d()) {
            this.mOnBackPressedDispatcher.f(d.a(this));
        }
        int i8 = this.mContentLayoutId;
        if (i8 != 0) {
            setContentView(i8);
        }
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public boolean onCreatePanelMenu(int i8, Menu menu) {
        if (i8 == 0) {
            super.onCreatePanelMenu(i8, menu);
            this.mMenuHostHelper.h(menu, getMenuInflater());
            return true;
        }
        return true;
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public boolean onMenuItemSelected(int i8, MenuItem menuItem) {
        if (super.onMenuItemSelected(i8, menuItem)) {
            return true;
        }
        if (i8 == 0) {
            return this.mMenuHostHelper.j(menuItem);
        }
        return false;
    }

    @Override // android.app.Activity
    public void onMultiWindowModeChanged(boolean z4) {
        if (this.mDispatchingOnMultiWindowModeChanged) {
            return;
        }
        Iterator<androidx.core.util.a<androidx.core.app.h>> it = this.mOnMultiWindowModeChangedListeners.iterator();
        while (it.hasNext()) {
            it.next().accept(new androidx.core.app.h(z4));
        }
    }

    @Override // android.app.Activity
    public void onMultiWindowModeChanged(boolean z4, Configuration configuration) {
        this.mDispatchingOnMultiWindowModeChanged = true;
        try {
            super.onMultiWindowModeChanged(z4, configuration);
            this.mDispatchingOnMultiWindowModeChanged = false;
            Iterator<androidx.core.util.a<androidx.core.app.h>> it = this.mOnMultiWindowModeChangedListeners.iterator();
            while (it.hasNext()) {
                it.next().accept(new androidx.core.app.h(z4, configuration));
            }
        } catch (Throwable th) {
            this.mDispatchingOnMultiWindowModeChanged = false;
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onNewIntent(@SuppressLint({"UnknownNullness", "MissingNullability"}) Intent intent) {
        super.onNewIntent(intent);
        Iterator<androidx.core.util.a<Intent>> it = this.mOnNewIntentListeners.iterator();
        while (it.hasNext()) {
            it.next().accept(intent);
        }
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onPanelClosed(int i8, Menu menu) {
        this.mMenuHostHelper.i(menu);
        super.onPanelClosed(i8, menu);
    }

    @Override // android.app.Activity
    public void onPictureInPictureModeChanged(boolean z4) {
        if (this.mDispatchingOnPictureInPictureModeChanged) {
            return;
        }
        Iterator<androidx.core.util.a<androidx.core.app.p>> it = this.mOnPictureInPictureModeChangedListeners.iterator();
        while (it.hasNext()) {
            it.next().accept(new androidx.core.app.p(z4));
        }
    }

    @Override // android.app.Activity
    public void onPictureInPictureModeChanged(boolean z4, Configuration configuration) {
        this.mDispatchingOnPictureInPictureModeChanged = true;
        try {
            super.onPictureInPictureModeChanged(z4, configuration);
            this.mDispatchingOnPictureInPictureModeChanged = false;
            Iterator<androidx.core.util.a<androidx.core.app.p>> it = this.mOnPictureInPictureModeChangedListeners.iterator();
            while (it.hasNext()) {
                it.next().accept(new androidx.core.app.p(z4, configuration));
            }
        } catch (Throwable th) {
            this.mDispatchingOnPictureInPictureModeChanged = false;
            throw th;
        }
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public boolean onPreparePanel(int i8, View view, Menu menu) {
        if (i8 == 0) {
            super.onPreparePanel(i8, view, menu);
            this.mMenuHostHelper.k(menu);
            return true;
        }
        return true;
    }

    @Override // android.app.Activity
    @Deprecated
    public void onRequestPermissionsResult(int i8, String[] strArr, int[] iArr) {
        if (this.mActivityResultRegistry.b(i8, -1, new Intent().putExtra("androidx.activity.result.contract.extra.PERMISSIONS", strArr).putExtra("androidx.activity.result.contract.extra.PERMISSION_GRANT_RESULTS", iArr)) || Build.VERSION.SDK_INT < 23) {
            return;
        }
        super.onRequestPermissionsResult(i8, strArr, iArr);
    }

    @Deprecated
    public Object onRetainCustomNonConfigurationInstance() {
        return null;
    }

    @Override // android.app.Activity
    public final Object onRetainNonConfigurationInstance() {
        e eVar;
        Object onRetainCustomNonConfigurationInstance = onRetainCustomNonConfigurationInstance();
        i0 i0Var = this.mViewModelStore;
        if (i0Var == null && (eVar = (e) getLastNonConfigurationInstance()) != null) {
            i0Var = eVar.f361b;
        }
        if (i0Var == null && onRetainCustomNonConfigurationInstance == null) {
            return null;
        }
        e eVar2 = new e();
        eVar2.f360a = onRetainCustomNonConfigurationInstance;
        eVar2.f361b = i0Var;
        return eVar2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.core.app.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle bundle) {
        Lifecycle lifecycle = getLifecycle();
        if (lifecycle instanceof androidx.lifecycle.k) {
            ((androidx.lifecycle.k) lifecycle).n(Lifecycle.State.CREATED);
        }
        super.onSaveInstanceState(bundle);
        this.mSavedStateRegistryController.e(bundle);
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks2
    public void onTrimMemory(int i8) {
        super.onTrimMemory(i8);
        Iterator<androidx.core.util.a<Integer>> it = this.mOnTrimMemoryListeners.iterator();
        while (it.hasNext()) {
            it.next().accept(Integer.valueOf(i8));
        }
    }

    public Context peekAvailableContext() {
        return this.mContextAwareHelper.d();
    }

    public final <I, O> androidx.activity.result.b<I> registerForActivityResult(f.a<I, O> aVar, ActivityResultRegistry activityResultRegistry, androidx.activity.result.a<O> aVar2) {
        return activityResultRegistry.i("activity_rq#" + this.mNextLocalRequestCode.getAndIncrement(), this, aVar, aVar2);
    }

    public final <I, O> androidx.activity.result.b<I> registerForActivityResult(f.a<I, O> aVar, androidx.activity.result.a<O> aVar2) {
        return registerForActivityResult(aVar, this.mActivityResultRegistry, aVar2);
    }

    public void removeMenuProvider(androidx.core.view.n nVar) {
        this.mMenuHostHelper.l(nVar);
    }

    public final void removeOnConfigurationChangedListener(androidx.core.util.a<Configuration> aVar) {
        this.mOnConfigurationChangedListeners.remove(aVar);
    }

    @Override // e.a
    public final void removeOnContextAvailableListener(e.d dVar) {
        this.mContextAwareHelper.e(dVar);
    }

    public final void removeOnMultiWindowModeChangedListener(androidx.core.util.a<androidx.core.app.h> aVar) {
        this.mOnMultiWindowModeChangedListeners.remove(aVar);
    }

    public final void removeOnNewIntentListener(androidx.core.util.a<Intent> aVar) {
        this.mOnNewIntentListeners.remove(aVar);
    }

    public final void removeOnPictureInPictureModeChangedListener(androidx.core.util.a<androidx.core.app.p> aVar) {
        this.mOnPictureInPictureModeChangedListeners.remove(aVar);
    }

    public final void removeOnTrimMemoryListener(androidx.core.util.a<Integer> aVar) {
        this.mOnTrimMemoryListeners.remove(aVar);
    }

    @Override // android.app.Activity
    public void reportFullyDrawn() {
        try {
            if (w1.a.h()) {
                w1.a.c("reportFullyDrawn() for ComponentActivity");
            }
            int i8 = Build.VERSION.SDK_INT;
            if (i8 <= 19) {
                if (i8 == 19 && androidx.core.content.a.a(this, "android.permission.UPDATE_DEVICE_STATS") == 0) {
                }
                this.mFullyDrawnReporter.b();
            }
            super.reportFullyDrawn();
            this.mFullyDrawnReporter.b();
        } finally {
            w1.a.f();
        }
    }

    @Override // android.app.Activity
    public void setContentView(int i8) {
        initViewTreeOwners();
        this.mReportFullyDrawnExecutor.B(getWindow().getDecorView());
        super.setContentView(i8);
    }

    @Override // android.app.Activity
    public void setContentView(@SuppressLint({"UnknownNullness", "MissingNullability"}) View view) {
        initViewTreeOwners();
        this.mReportFullyDrawnExecutor.B(getWindow().getDecorView());
        super.setContentView(view);
    }

    @Override // android.app.Activity
    public void setContentView(@SuppressLint({"UnknownNullness", "MissingNullability"}) View view, @SuppressLint({"UnknownNullness", "MissingNullability"}) ViewGroup.LayoutParams layoutParams) {
        initViewTreeOwners();
        this.mReportFullyDrawnExecutor.B(getWindow().getDecorView());
        super.setContentView(view, layoutParams);
    }

    @Override // android.app.Activity
    @Deprecated
    public void startActivityForResult(Intent intent, int i8) {
        super.startActivityForResult(intent, i8);
    }

    @Override // android.app.Activity
    @Deprecated
    public void startActivityForResult(Intent intent, int i8, Bundle bundle) {
        super.startActivityForResult(intent, i8, bundle);
    }

    @Override // android.app.Activity
    @Deprecated
    public void startIntentSenderForResult(IntentSender intentSender, int i8, Intent intent, int i9, int i10, int i11) {
        super.startIntentSenderForResult(intentSender, i8, intent, i9, i10, i11);
    }

    @Override // android.app.Activity
    @Deprecated
    public void startIntentSenderForResult(IntentSender intentSender, int i8, Intent intent, int i9, int i10, int i11, Bundle bundle) {
        super.startIntentSenderForResult(intentSender, i8, intent, i9, i10, i11, bundle);
    }
}
