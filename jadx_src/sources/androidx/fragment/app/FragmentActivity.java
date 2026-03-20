package androidx.fragment.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import androidx.activity.ComponentActivity;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.core.app.b;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.i0;
import androidx.lifecycle.j0;
import androidx.savedstate.a;
import java.io.FileDescriptor;
import java.io.PrintWriter;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class FragmentActivity extends ComponentActivity implements b.e, b.g {
    static final String FRAGMENTS_TAG = "android:support:fragments";
    boolean mCreated;
    final androidx.lifecycle.k mFragmentLifecycleRegistry;
    final f mFragments;
    boolean mResumed;
    boolean mStopped;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements a.c {
        a() {
        }

        @Override // androidx.savedstate.a.c
        public Bundle a() {
            Bundle bundle = new Bundle();
            FragmentActivity.this.markFragmentsCreated();
            FragmentActivity.this.mFragmentLifecycleRegistry.h(Lifecycle.Event.ON_STOP);
            Parcelable x8 = FragmentActivity.this.mFragments.x();
            if (x8 != null) {
                bundle.putParcelable(FragmentActivity.FRAGMENTS_TAG, x8);
            }
            return bundle;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b implements e.d {
        b() {
        }

        @Override // e.d
        public void a(Context context) {
            FragmentActivity.this.mFragments.a(null);
            Bundle b9 = FragmentActivity.this.getSavedStateRegistry().b(FragmentActivity.FRAGMENTS_TAG);
            if (b9 != null) {
                FragmentActivity.this.mFragments.w(b9.getParcelable(FragmentActivity.FRAGMENTS_TAG));
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class c extends h<FragmentActivity> implements j0, androidx.activity.n, androidx.activity.result.c, m {
        public c() {
            super(FragmentActivity.this);
        }

        @Override // androidx.fragment.app.m
        public void a(FragmentManager fragmentManager, Fragment fragment) {
            FragmentActivity.this.onAttachFragment(fragment);
        }

        @Override // androidx.fragment.app.h, androidx.fragment.app.e
        public View c(int i8) {
            return FragmentActivity.this.findViewById(i8);
        }

        @Override // androidx.fragment.app.h, androidx.fragment.app.e
        public boolean d() {
            Window window = FragmentActivity.this.getWindow();
            return (window == null || window.peekDecorView() == null) ? false : true;
        }

        @Override // androidx.activity.result.c
        public ActivityResultRegistry getActivityResultRegistry() {
            return FragmentActivity.this.getActivityResultRegistry();
        }

        @Override // androidx.lifecycle.j
        public Lifecycle getLifecycle() {
            return FragmentActivity.this.mFragmentLifecycleRegistry;
        }

        @Override // androidx.activity.n
        public OnBackPressedDispatcher getOnBackPressedDispatcher() {
            return FragmentActivity.this.getOnBackPressedDispatcher();
        }

        @Override // androidx.lifecycle.j0
        public i0 getViewModelStore() {
            return FragmentActivity.this.getViewModelStore();
        }

        @Override // androidx.fragment.app.h
        public LayoutInflater i() {
            return FragmentActivity.this.getLayoutInflater().cloneInContext(FragmentActivity.this);
        }

        @Override // androidx.fragment.app.h
        public boolean k(Fragment fragment) {
            return !FragmentActivity.this.isFinishing();
        }

        @Override // androidx.fragment.app.h
        public boolean l(String str) {
            return androidx.core.app.b.w(FragmentActivity.this, str);
        }

        @Override // androidx.fragment.app.h
        public void o() {
            FragmentActivity.this.supportInvalidateOptionsMenu();
        }

        @Override // androidx.fragment.app.h
        /* renamed from: p */
        public FragmentActivity h() {
            return FragmentActivity.this;
        }
    }

    public FragmentActivity() {
        this.mFragments = f.b(new c());
        this.mFragmentLifecycleRegistry = new androidx.lifecycle.k(this);
        this.mStopped = true;
        init();
    }

    public FragmentActivity(int i8) {
        super(i8);
        this.mFragments = f.b(new c());
        this.mFragmentLifecycleRegistry = new androidx.lifecycle.k(this);
        this.mStopped = true;
        init();
    }

    private void init() {
        getSavedStateRegistry().h(FRAGMENTS_TAG, new a());
        addOnContextAvailableListener(new b());
    }

    private static boolean markState(FragmentManager fragmentManager, Lifecycle.State state) {
        boolean z4 = false;
        for (Fragment fragment : fragmentManager.s0()) {
            if (fragment != null) {
                if (fragment.y() != null) {
                    z4 |= markState(fragment.q(), state);
                }
                v vVar = fragment.f5404h0;
                if (vVar != null && vVar.getLifecycle().b().f(Lifecycle.State.STARTED)) {
                    fragment.f5404h0.f(state);
                    z4 = true;
                }
                if (fragment.f5402g0.b().f(Lifecycle.State.STARTED)) {
                    fragment.f5402g0.n(state);
                    z4 = true;
                }
            }
        }
        return z4;
    }

    final View dispatchFragmentsOnCreateView(View view, String str, Context context, AttributeSet attributeSet) {
        return this.mFragments.v(view, str, context, attributeSet);
    }

    @Override // android.app.Activity
    public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        super.dump(str, fileDescriptor, printWriter, strArr);
        printWriter.print(str);
        printWriter.print("Local FragmentActivity ");
        printWriter.print(Integer.toHexString(System.identityHashCode(this)));
        printWriter.println(" State:");
        String str2 = str + "  ";
        printWriter.print(str2);
        printWriter.print("mCreated=");
        printWriter.print(this.mCreated);
        printWriter.print(" mResumed=");
        printWriter.print(this.mResumed);
        printWriter.print(" mStopped=");
        printWriter.print(this.mStopped);
        if (getApplication() != null) {
            androidx.loader.app.a.b(this).a(str2, fileDescriptor, printWriter, strArr);
        }
        this.mFragments.t().W(str, fileDescriptor, printWriter, strArr);
    }

    public FragmentManager getSupportFragmentManager() {
        return this.mFragments.t();
    }

    @Deprecated
    public androidx.loader.app.a getSupportLoaderManager() {
        return androidx.loader.app.a.b(this);
    }

    void markFragmentsCreated() {
        do {
        } while (markState(getSupportFragmentManager(), Lifecycle.State.CREATED));
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    protected void onActivityResult(int i8, int i9, Intent intent) {
        this.mFragments.u();
        super.onActivityResult(i8, i9, intent);
    }

    @Deprecated
    public void onAttachFragment(Fragment fragment) {
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        this.mFragments.u();
        super.onConfigurationChanged(configuration);
        this.mFragments.d(configuration);
    }

    @Override // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mFragmentLifecycleRegistry.h(Lifecycle.Event.ON_CREATE);
        this.mFragments.f();
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity, android.view.Window.Callback
    public boolean onCreatePanelMenu(int i8, Menu menu) {
        return i8 == 0 ? super.onCreatePanelMenu(i8, menu) | this.mFragments.g(menu, getMenuInflater()) : super.onCreatePanelMenu(i8, menu);
    }

    @Override // android.app.Activity, android.view.LayoutInflater.Factory2
    public View onCreateView(View view, String str, Context context, AttributeSet attributeSet) {
        View dispatchFragmentsOnCreateView = dispatchFragmentsOnCreateView(view, str, context, attributeSet);
        return dispatchFragmentsOnCreateView == null ? super.onCreateView(view, str, context, attributeSet) : dispatchFragmentsOnCreateView;
    }

    @Override // android.app.Activity, android.view.LayoutInflater.Factory
    public View onCreateView(String str, Context context, AttributeSet attributeSet) {
        View dispatchFragmentsOnCreateView = dispatchFragmentsOnCreateView(null, str, context, attributeSet);
        return dispatchFragmentsOnCreateView == null ? super.onCreateView(str, context, attributeSet) : dispatchFragmentsOnCreateView;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        this.mFragments.h();
        this.mFragmentLifecycleRegistry.h(Lifecycle.Event.ON_DESTROY);
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onLowMemory() {
        super.onLowMemory();
        this.mFragments.i();
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity, android.view.Window.Callback
    public boolean onMenuItemSelected(int i8, MenuItem menuItem) {
        if (super.onMenuItemSelected(i8, menuItem)) {
            return true;
        }
        if (i8 != 0) {
            if (i8 != 6) {
                return false;
            }
            return this.mFragments.e(menuItem);
        }
        return this.mFragments.k(menuItem);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onMultiWindowModeChanged(boolean z4) {
        this.mFragments.j(z4);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    protected void onNewIntent(@SuppressLint({"UnknownNullness"}) Intent intent) {
        this.mFragments.u();
        super.onNewIntent(intent);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity, android.view.Window.Callback
    public void onPanelClosed(int i8, Menu menu) {
        if (i8 == 0) {
            this.mFragments.l(menu);
        }
        super.onPanelClosed(i8, menu);
    }

    @Override // android.app.Activity
    protected void onPause() {
        super.onPause();
        this.mResumed = false;
        this.mFragments.m();
        this.mFragmentLifecycleRegistry.h(Lifecycle.Event.ON_PAUSE);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onPictureInPictureModeChanged(boolean z4) {
        this.mFragments.n(z4);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onPostResume() {
        super.onPostResume();
        onResumeFragments();
    }

    @Deprecated
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        return super.onPreparePanel(0, view, menu);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity, android.view.Window.Callback
    public boolean onPreparePanel(int i8, View view, Menu menu) {
        return i8 == 0 ? onPrepareOptionsPanel(view, menu) | this.mFragments.o(menu) : super.onPreparePanel(i8, view, menu);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onRequestPermissionsResult(int i8, String[] strArr, int[] iArr) {
        this.mFragments.u();
        super.onRequestPermissionsResult(i8, strArr, iArr);
    }

    @Override // android.app.Activity
    protected void onResume() {
        this.mFragments.u();
        super.onResume();
        this.mResumed = true;
        this.mFragments.s();
    }

    protected void onResumeFragments() {
        this.mFragmentLifecycleRegistry.h(Lifecycle.Event.ON_RESUME);
        this.mFragments.p();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onStart() {
        this.mFragments.u();
        super.onStart();
        this.mStopped = false;
        if (!this.mCreated) {
            this.mCreated = true;
            this.mFragments.c();
        }
        this.mFragments.s();
        this.mFragmentLifecycleRegistry.h(Lifecycle.Event.ON_START);
        this.mFragments.q();
    }

    @Override // android.app.Activity
    public void onStateNotSaved() {
        this.mFragments.u();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onStop() {
        super.onStop();
        this.mStopped = true;
        markFragmentsCreated();
        this.mFragments.r();
        this.mFragmentLifecycleRegistry.h(Lifecycle.Event.ON_STOP);
    }

    public void setEnterSharedElementCallback(androidx.core.app.r rVar) {
        androidx.core.app.b.u(this, rVar);
    }

    public void setExitSharedElementCallback(androidx.core.app.r rVar) {
        androidx.core.app.b.v(this, rVar);
    }

    public void startActivityFromFragment(Fragment fragment, @SuppressLint({"UnknownNullness"}) Intent intent, int i8) {
        startActivityFromFragment(fragment, intent, i8, (Bundle) null);
    }

    public void startActivityFromFragment(Fragment fragment, @SuppressLint({"UnknownNullness"}) Intent intent, int i8, Bundle bundle) {
        if (i8 == -1) {
            androidx.core.app.b.x(this, intent, -1, bundle);
        } else {
            fragment.G1(intent, i8, bundle);
        }
    }

    @Deprecated
    public void startIntentSenderFromFragment(Fragment fragment, @SuppressLint({"UnknownNullness"}) IntentSender intentSender, int i8, Intent intent, int i9, int i10, int i11, Bundle bundle) {
        if (i8 == -1) {
            androidx.core.app.b.y(this, intentSender, i8, intent, i9, i10, i11, bundle);
        } else {
            fragment.H1(intentSender, i8, intent, i9, i10, i11, bundle);
        }
    }

    public void supportFinishAfterTransition() {
        androidx.core.app.b.p(this);
    }

    @Deprecated
    public void supportInvalidateOptionsMenu() {
        invalidateOptionsMenu();
    }

    public void supportPostponeEnterTransition() {
        androidx.core.app.b.r(this);
    }

    public void supportStartPostponedEnterTransition() {
        androidx.core.app.b.z(this);
    }

    @Override // androidx.core.app.b.g
    @Deprecated
    public final void validateRequestPermissionsRequestCode(int i8) {
    }
}
