package androidx.appcompat.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.UiModeManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.AndroidRuntimeException;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.ContextThemeWrapper;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.window.OnBackInvokedCallback;
import android.window.OnBackInvokedDispatcher;
import androidx.appcompat.app.a;
import androidx.appcompat.view.b;
import androidx.appcompat.view.f;
import androidx.appcompat.view.menu.g;
import androidx.appcompat.view.menu.m;
import androidx.appcompat.widget.ActionBarContextView;
import androidx.appcompat.widget.ContentFrameLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.j0;
import androidx.appcompat.widget.u0;
import androidx.appcompat.widget.v;
import androidx.core.content.res.h;
import androidx.core.view.c0;
import androidx.core.view.g;
import androidx.core.view.i0;
import androidx.core.view.k0;
import androidx.core.view.m0;
import androidx.lifecycle.Lifecycle;
import com.example.seedpoint.R;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.lang.Thread;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class AppCompatDelegateImpl extends androidx.appcompat.app.f implements g.a, LayoutInflater.Factory2 {
    private static final boolean A0;
    private static final boolean B0;
    private static boolean C0;

    /* renamed from: x0  reason: collision with root package name */
    private static final k0.g<String, Integer> f550x0 = new k0.g<>();

    /* renamed from: y0  reason: collision with root package name */
    private static final boolean f551y0;

    /* renamed from: z0  reason: collision with root package name */
    private static final int[] f552z0;
    androidx.appcompat.view.b A;
    ActionBarContextView B;
    PopupWindow C;
    Runnable E;
    i0 F;
    private boolean G;
    private boolean H;
    ViewGroup K;
    private TextView L;
    private View O;
    private boolean P;
    private boolean Q;
    boolean R;
    boolean T;
    boolean W;
    boolean X;
    boolean Y;
    private boolean Z;

    /* renamed from: a0  reason: collision with root package name */
    private PanelFeatureState[] f553a0;

    /* renamed from: b0  reason: collision with root package name */
    private PanelFeatureState f554b0;

    /* renamed from: c0  reason: collision with root package name */
    private boolean f555c0;

    /* renamed from: d0  reason: collision with root package name */
    private boolean f556d0;

    /* renamed from: e0  reason: collision with root package name */
    private boolean f557e0;

    /* renamed from: f0  reason: collision with root package name */
    boolean f558f0;

    /* renamed from: g0  reason: collision with root package name */
    private Configuration f559g0;

    /* renamed from: h0  reason: collision with root package name */
    private int f560h0;

    /* renamed from: i0  reason: collision with root package name */
    private int f561i0;

    /* renamed from: j0  reason: collision with root package name */
    private int f562j0;

    /* renamed from: k  reason: collision with root package name */
    final Object f563k;

    /* renamed from: k0  reason: collision with root package name */
    private boolean f564k0;

    /* renamed from: l  reason: collision with root package name */
    final Context f565l;

    /* renamed from: l0  reason: collision with root package name */
    private s f566l0;

    /* renamed from: m  reason: collision with root package name */
    Window f567m;

    /* renamed from: m0  reason: collision with root package name */
    private s f568m0;

    /* renamed from: n  reason: collision with root package name */
    private q f569n;

    /* renamed from: n0  reason: collision with root package name */
    boolean f570n0;

    /* renamed from: o0  reason: collision with root package name */
    int f571o0;

    /* renamed from: p  reason: collision with root package name */
    final androidx.appcompat.app.d f572p;

    /* renamed from: p0  reason: collision with root package name */
    private final Runnable f573p0;
    ActionBar q;

    /* renamed from: q0  reason: collision with root package name */
    private boolean f574q0;

    /* renamed from: r0  reason: collision with root package name */
    private Rect f575r0;

    /* renamed from: s0  reason: collision with root package name */
    private Rect f576s0;

    /* renamed from: t  reason: collision with root package name */
    MenuInflater f577t;

    /* renamed from: t0  reason: collision with root package name */
    private androidx.appcompat.app.k f578t0;

    /* renamed from: u0  reason: collision with root package name */
    private androidx.appcompat.app.n f579u0;

    /* renamed from: v0  reason: collision with root package name */
    private OnBackInvokedDispatcher f580v0;

    /* renamed from: w  reason: collision with root package name */
    private CharSequence f581w;

    /* renamed from: w0  reason: collision with root package name */
    private OnBackInvokedCallback f582w0;

    /* renamed from: x  reason: collision with root package name */
    private androidx.appcompat.widget.r f583x;

    /* renamed from: y  reason: collision with root package name */
    private j f584y;

    /* renamed from: z  reason: collision with root package name */
    private w f585z;

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class PanelFeatureState {

        /* renamed from: a  reason: collision with root package name */
        int f586a;

        /* renamed from: b  reason: collision with root package name */
        int f587b;

        /* renamed from: c  reason: collision with root package name */
        int f588c;

        /* renamed from: d  reason: collision with root package name */
        int f589d;

        /* renamed from: e  reason: collision with root package name */
        int f590e;

        /* renamed from: f  reason: collision with root package name */
        int f591f;

        /* renamed from: g  reason: collision with root package name */
        ViewGroup f592g;

        /* renamed from: h  reason: collision with root package name */
        View f593h;

        /* renamed from: i  reason: collision with root package name */
        View f594i;

        /* renamed from: j  reason: collision with root package name */
        androidx.appcompat.view.menu.g f595j;

        /* renamed from: k  reason: collision with root package name */
        androidx.appcompat.view.menu.e f596k;

        /* renamed from: l  reason: collision with root package name */
        Context f597l;

        /* renamed from: m  reason: collision with root package name */
        boolean f598m;

        /* renamed from: n  reason: collision with root package name */
        boolean f599n;

        /* renamed from: o  reason: collision with root package name */
        boolean f600o;

        /* renamed from: p  reason: collision with root package name */
        public boolean f601p;
        boolean q = false;

        /* renamed from: r  reason: collision with root package name */
        boolean f602r;

        /* renamed from: s  reason: collision with root package name */
        Bundle f603s;

        /* JADX INFO: Access modifiers changed from: private */
        @SuppressLint({"BanParcelableUsage"})
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static class SavedState implements Parcelable {
            public static final Parcelable.Creator<SavedState> CREATOR = new a();

            /* renamed from: a  reason: collision with root package name */
            int f604a;

            /* renamed from: b  reason: collision with root package name */
            boolean f605b;

            /* renamed from: c  reason: collision with root package name */
            Bundle f606c;

            /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
            class a implements Parcelable.ClassLoaderCreator<SavedState> {
                a() {
                }

                @Override // android.os.Parcelable.Creator
                /* renamed from: a */
                public SavedState createFromParcel(Parcel parcel) {
                    return SavedState.a(parcel, null);
                }

                @Override // android.os.Parcelable.ClassLoaderCreator
                /* renamed from: b */
                public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                    return SavedState.a(parcel, classLoader);
                }

                @Override // android.os.Parcelable.Creator
                /* renamed from: c */
                public SavedState[] newArray(int i8) {
                    return new SavedState[i8];
                }
            }

            SavedState() {
            }

            static SavedState a(Parcel parcel, ClassLoader classLoader) {
                SavedState savedState = new SavedState();
                savedState.f604a = parcel.readInt();
                boolean z4 = parcel.readInt() == 1;
                savedState.f605b = z4;
                if (z4) {
                    savedState.f606c = parcel.readBundle(classLoader);
                }
                return savedState;
            }

            @Override // android.os.Parcelable
            public int describeContents() {
                return 0;
            }

            @Override // android.os.Parcelable
            public void writeToParcel(Parcel parcel, int i8) {
                parcel.writeInt(this.f604a);
                parcel.writeInt(this.f605b ? 1 : 0);
                if (this.f605b) {
                    parcel.writeBundle(this.f606c);
                }
            }
        }

        PanelFeatureState(int i8) {
            this.f586a = i8;
        }

        androidx.appcompat.view.menu.n a(m.a aVar) {
            if (this.f595j == null) {
                return null;
            }
            if (this.f596k == null) {
                androidx.appcompat.view.menu.e eVar = new androidx.appcompat.view.menu.e(this.f597l, g.g.f19972l);
                this.f596k = eVar;
                eVar.j(aVar);
                this.f595j.b(this.f596k);
            }
            return this.f596k.b(this.f592g);
        }

        public boolean b() {
            if (this.f593h == null) {
                return false;
            }
            return this.f594i != null || this.f596k.a().getCount() > 0;
        }

        void c(androidx.appcompat.view.menu.g gVar) {
            androidx.appcompat.view.menu.e eVar;
            androidx.appcompat.view.menu.g gVar2 = this.f595j;
            if (gVar == gVar2) {
                return;
            }
            if (gVar2 != null) {
                gVar2.Q(this.f596k);
            }
            this.f595j = gVar;
            if (gVar == null || (eVar = this.f596k) == null) {
                return;
            }
            gVar.b(eVar);
        }

        void d(Context context) {
            TypedValue typedValue = new TypedValue();
            Resources.Theme newTheme = context.getResources().newTheme();
            newTheme.setTo(context.getTheme());
            newTheme.resolveAttribute(g.a.f19862a, typedValue, true);
            int i8 = typedValue.resourceId;
            if (i8 != 0) {
                newTheme.applyStyle(i8, true);
            }
            newTheme.resolveAttribute(g.a.I, typedValue, true);
            int i9 = typedValue.resourceId;
            if (i9 == 0) {
                i9 = g.i.f20000d;
            }
            newTheme.applyStyle(i9, true);
            androidx.appcompat.view.d dVar = new androidx.appcompat.view.d(context, 0);
            dVar.getTheme().setTo(newTheme);
            this.f597l = dVar;
            TypedArray obtainStyledAttributes = dVar.obtainStyledAttributes(g.j.A0);
            this.f587b = obtainStyledAttributes.getResourceId(g.j.D0, 0);
            this.f591f = obtainStyledAttributes.getResourceId(g.j.C0, 0);
            obtainStyledAttributes.recycle();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Thread.UncaughtExceptionHandler {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ Thread.UncaughtExceptionHandler f607a;

        a(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
            this.f607a = uncaughtExceptionHandler;
        }

        private boolean a(Throwable th) {
            String message;
            if (!(th instanceof Resources.NotFoundException) || (message = th.getMessage()) == null) {
                return false;
            }
            return message.contains("drawable") || message.contains("Drawable");
        }

        @Override // java.lang.Thread.UncaughtExceptionHandler
        public void uncaughtException(Thread thread, Throwable th) {
            if (!a(th)) {
                this.f607a.uncaughtException(thread, th);
                return;
            }
            Resources.NotFoundException notFoundException = new Resources.NotFoundException(th.getMessage() + ". If the resource you are trying to use is a vector resource, you may be referencing it in an unsupported way. See AppCompatDelegate.setCompatVectorFromResourcesEnabled() for more info.");
            notFoundException.initCause(th.getCause());
            notFoundException.setStackTrace(th.getStackTrace());
            this.f607a.uncaughtException(thread, notFoundException);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b implements Runnable {
        b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            AppCompatDelegateImpl appCompatDelegateImpl = AppCompatDelegateImpl.this;
            if ((appCompatDelegateImpl.f571o0 & 1) != 0) {
                appCompatDelegateImpl.j0(0);
            }
            AppCompatDelegateImpl appCompatDelegateImpl2 = AppCompatDelegateImpl.this;
            if ((appCompatDelegateImpl2.f571o0 & RecognitionOptions.AZTEC) != 0) {
                appCompatDelegateImpl2.j0(108);
            }
            AppCompatDelegateImpl appCompatDelegateImpl3 = AppCompatDelegateImpl.this;
            appCompatDelegateImpl3.f570n0 = false;
            appCompatDelegateImpl3.f571o0 = 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class c implements androidx.core.view.v {
        c() {
        }

        @Override // androidx.core.view.v
        public m0 a(View view, m0 m0Var) {
            int m8 = m0Var.m();
            int f12 = AppCompatDelegateImpl.this.f1(m0Var, null);
            if (m8 != f12) {
                m0Var = m0Var.s(m0Var.k(), f12, m0Var.l(), m0Var.j());
            }
            return c0.e0(view, m0Var);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class d implements v.a {
        d() {
        }

        @Override // androidx.appcompat.widget.v.a
        public void a(Rect rect) {
            rect.top = AppCompatDelegateImpl.this.f1(null, rect);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class e implements ContentFrameLayout.a {
        e() {
        }

        @Override // androidx.appcompat.widget.ContentFrameLayout.a
        public void a() {
        }

        @Override // androidx.appcompat.widget.ContentFrameLayout.a
        public void onDetachedFromWindow() {
            AppCompatDelegateImpl.this.h0();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class f implements Runnable {

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a extends k0 {
            a() {
            }

            @Override // androidx.core.view.j0
            public void b(View view) {
                AppCompatDelegateImpl.this.B.setAlpha(1.0f);
                AppCompatDelegateImpl.this.F.h(null);
                AppCompatDelegateImpl.this.F = null;
            }

            @Override // androidx.core.view.k0, androidx.core.view.j0
            public void c(View view) {
                AppCompatDelegateImpl.this.B.setVisibility(0);
            }
        }

        f() {
        }

        @Override // java.lang.Runnable
        public void run() {
            AppCompatDelegateImpl appCompatDelegateImpl = AppCompatDelegateImpl.this;
            appCompatDelegateImpl.C.showAtLocation(appCompatDelegateImpl.B, 55, 0, 0);
            AppCompatDelegateImpl.this.k0();
            if (!AppCompatDelegateImpl.this.V0()) {
                AppCompatDelegateImpl.this.B.setAlpha(1.0f);
                AppCompatDelegateImpl.this.B.setVisibility(0);
                return;
            }
            AppCompatDelegateImpl.this.B.setAlpha(0.0f);
            AppCompatDelegateImpl appCompatDelegateImpl2 = AppCompatDelegateImpl.this;
            appCompatDelegateImpl2.F = c0.e(appCompatDelegateImpl2.B).b(1.0f);
            AppCompatDelegateImpl.this.F.h(new a());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class g extends k0 {
        g() {
        }

        @Override // androidx.core.view.j0
        public void b(View view) {
            AppCompatDelegateImpl.this.B.setAlpha(1.0f);
            AppCompatDelegateImpl.this.F.h(null);
            AppCompatDelegateImpl.this.F = null;
        }

        @Override // androidx.core.view.k0, androidx.core.view.j0
        public void c(View view) {
            AppCompatDelegateImpl.this.B.setVisibility(0);
            if (AppCompatDelegateImpl.this.B.getParent() instanceof View) {
                c0.q0((View) AppCompatDelegateImpl.this.B.getParent());
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private class h implements a.b {
        h() {
        }

        @Override // androidx.appcompat.app.a.b
        public boolean a() {
            ActionBar s8 = AppCompatDelegateImpl.this.s();
            return (s8 == null || (s8.j() & 4) == 0) ? false : true;
        }

        @Override // androidx.appcompat.app.a.b
        public Context b() {
            return AppCompatDelegateImpl.this.p0();
        }

        @Override // androidx.appcompat.app.a.b
        public void c(Drawable drawable, int i8) {
            ActionBar s8 = AppCompatDelegateImpl.this.s();
            if (s8 != null) {
                s8.v(drawable);
                s8.u(i8);
            }
        }

        @Override // androidx.appcompat.app.a.b
        public Drawable d() {
            j0 u8 = j0.u(b(), null, new int[]{g.a.E});
            Drawable g8 = u8.g(0);
            u8.w();
            return g8;
        }

        @Override // androidx.appcompat.app.a.b
        public void e(int i8) {
            ActionBar s8 = AppCompatDelegateImpl.this.s();
            if (s8 != null) {
                s8.u(i8);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface i {
        boolean a(int i8);

        View onCreatePanelView(int i8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public final class j implements m.a {
        j() {
        }

        @Override // androidx.appcompat.view.menu.m.a
        public void c(androidx.appcompat.view.menu.g gVar, boolean z4) {
            AppCompatDelegateImpl.this.a0(gVar);
        }

        @Override // androidx.appcompat.view.menu.m.a
        public boolean d(androidx.appcompat.view.menu.g gVar) {
            Window.Callback w02 = AppCompatDelegateImpl.this.w0();
            if (w02 != null) {
                w02.onMenuOpened(108, gVar);
                return true;
            }
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class k implements b.a {

        /* renamed from: a  reason: collision with root package name */
        private b.a f617a;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a extends k0 {
            a() {
            }

            @Override // androidx.core.view.j0
            public void b(View view) {
                AppCompatDelegateImpl.this.B.setVisibility(8);
                AppCompatDelegateImpl appCompatDelegateImpl = AppCompatDelegateImpl.this;
                PopupWindow popupWindow = appCompatDelegateImpl.C;
                if (popupWindow != null) {
                    popupWindow.dismiss();
                } else if (appCompatDelegateImpl.B.getParent() instanceof View) {
                    c0.q0((View) AppCompatDelegateImpl.this.B.getParent());
                }
                AppCompatDelegateImpl.this.B.k();
                AppCompatDelegateImpl.this.F.h(null);
                AppCompatDelegateImpl appCompatDelegateImpl2 = AppCompatDelegateImpl.this;
                appCompatDelegateImpl2.F = null;
                c0.q0(appCompatDelegateImpl2.K);
            }
        }

        public k(b.a aVar) {
            this.f617a = aVar;
        }

        @Override // androidx.appcompat.view.b.a
        public void a(androidx.appcompat.view.b bVar) {
            this.f617a.a(bVar);
            AppCompatDelegateImpl appCompatDelegateImpl = AppCompatDelegateImpl.this;
            if (appCompatDelegateImpl.C != null) {
                appCompatDelegateImpl.f567m.getDecorView().removeCallbacks(AppCompatDelegateImpl.this.E);
            }
            AppCompatDelegateImpl appCompatDelegateImpl2 = AppCompatDelegateImpl.this;
            if (appCompatDelegateImpl2.B != null) {
                appCompatDelegateImpl2.k0();
                AppCompatDelegateImpl appCompatDelegateImpl3 = AppCompatDelegateImpl.this;
                appCompatDelegateImpl3.F = c0.e(appCompatDelegateImpl3.B).b(0.0f);
                AppCompatDelegateImpl.this.F.h(new a());
            }
            AppCompatDelegateImpl appCompatDelegateImpl4 = AppCompatDelegateImpl.this;
            androidx.appcompat.app.d dVar = appCompatDelegateImpl4.f572p;
            if (dVar != null) {
                dVar.onSupportActionModeFinished(appCompatDelegateImpl4.A);
            }
            AppCompatDelegateImpl appCompatDelegateImpl5 = AppCompatDelegateImpl.this;
            appCompatDelegateImpl5.A = null;
            c0.q0(appCompatDelegateImpl5.K);
            AppCompatDelegateImpl.this.d1();
        }

        @Override // androidx.appcompat.view.b.a
        public boolean b(androidx.appcompat.view.b bVar, Menu menu) {
            return this.f617a.b(bVar, menu);
        }

        @Override // androidx.appcompat.view.b.a
        public boolean c(androidx.appcompat.view.b bVar, Menu menu) {
            c0.q0(AppCompatDelegateImpl.this.K);
            return this.f617a.c(bVar, menu);
        }

        @Override // androidx.appcompat.view.b.a
        public boolean d(androidx.appcompat.view.b bVar, MenuItem menuItem) {
            return this.f617a.d(bVar, menuItem);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class l {
        static Context a(Context context, Configuration configuration) {
            return context.createConfigurationContext(configuration);
        }

        static void b(Configuration configuration, Configuration configuration2, Configuration configuration3) {
            int i8 = configuration.densityDpi;
            int i9 = configuration2.densityDpi;
            if (i8 != i9) {
                configuration3.densityDpi = i9;
            }
        }

        static void c(Configuration configuration, Locale locale) {
            configuration.setLayoutDirection(locale);
        }

        static void d(Configuration configuration, Locale locale) {
            configuration.setLocale(locale);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class m {
        static boolean a(PowerManager powerManager) {
            return powerManager.isPowerSaveMode();
        }

        static String b(Locale locale) {
            return locale.toLanguageTag();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class n {
        static void a(Configuration configuration, Configuration configuration2, Configuration configuration3) {
            LocaleList locales = configuration.getLocales();
            LocaleList locales2 = configuration2.getLocales();
            if (locales.equals(locales2)) {
                return;
            }
            configuration3.setLocales(locales2);
            configuration3.locale = configuration2.locale;
        }

        static androidx.core.os.j b(Configuration configuration) {
            return androidx.core.os.j.c(configuration.getLocales().toLanguageTags());
        }

        public static void c(androidx.core.os.j jVar) {
            LocaleList.setDefault(LocaleList.forLanguageTags(jVar.h()));
        }

        static void d(Configuration configuration, androidx.core.os.j jVar) {
            configuration.setLocales(LocaleList.forLanguageTags(jVar.h()));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class o {
        static void a(Configuration configuration, Configuration configuration2, Configuration configuration3) {
            int i8 = configuration.colorMode & 3;
            int i9 = configuration2.colorMode;
            if (i8 != (i9 & 3)) {
                configuration3.colorMode |= i9 & 3;
            }
            int i10 = configuration.colorMode & 12;
            int i11 = configuration2.colorMode;
            if (i10 != (i11 & 12)) {
                configuration3.colorMode |= i11 & 12;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class p {
        static OnBackInvokedDispatcher a(Activity activity) {
            return activity.getOnBackInvokedDispatcher();
        }

        static OnBackInvokedCallback b(Object obj, final AppCompatDelegateImpl appCompatDelegateImpl) {
            Objects.requireNonNull(appCompatDelegateImpl);
            OnBackInvokedCallback onBackInvokedCallback = new OnBackInvokedCallback() { // from class: androidx.appcompat.app.g
                public final void onBackInvoked() {
                    AppCompatDelegateImpl.this.E0();
                }
            };
            ((OnBackInvokedDispatcher) obj).registerOnBackInvokedCallback(1000000, onBackInvokedCallback);
            return onBackInvokedCallback;
        }

        static void c(Object obj, Object obj2) {
            ((OnBackInvokedDispatcher) obj).unregisterOnBackInvokedCallback((OnBackInvokedCallback) obj2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class q extends androidx.appcompat.view.i {

        /* renamed from: b  reason: collision with root package name */
        private i f620b;

        /* renamed from: c  reason: collision with root package name */
        private boolean f621c;

        /* renamed from: d  reason: collision with root package name */
        private boolean f622d;

        /* renamed from: e  reason: collision with root package name */
        private boolean f623e;

        q(Window.Callback callback) {
            super(callback);
        }

        public boolean b(Window.Callback callback, KeyEvent keyEvent) {
            try {
                this.f622d = true;
                return callback.dispatchKeyEvent(keyEvent);
            } finally {
                this.f622d = false;
            }
        }

        public void c(Window.Callback callback) {
            try {
                this.f621c = true;
                callback.onContentChanged();
            } finally {
                this.f621c = false;
            }
        }

        public void d(Window.Callback callback, int i8, Menu menu) {
            try {
                this.f623e = true;
                callback.onPanelClosed(i8, menu);
            } finally {
                this.f623e = false;
            }
        }

        @Override // androidx.appcompat.view.i, android.view.Window.Callback
        public boolean dispatchKeyEvent(KeyEvent keyEvent) {
            return this.f622d ? a().dispatchKeyEvent(keyEvent) : AppCompatDelegateImpl.this.i0(keyEvent) || super.dispatchKeyEvent(keyEvent);
        }

        @Override // androidx.appcompat.view.i, android.view.Window.Callback
        public boolean dispatchKeyShortcutEvent(KeyEvent keyEvent) {
            return super.dispatchKeyShortcutEvent(keyEvent) || AppCompatDelegateImpl.this.H0(keyEvent.getKeyCode(), keyEvent);
        }

        void e(i iVar) {
            this.f620b = iVar;
        }

        final ActionMode f(ActionMode.Callback callback) {
            f.a aVar = new f.a(AppCompatDelegateImpl.this.f565l, callback);
            androidx.appcompat.view.b Q = AppCompatDelegateImpl.this.Q(aVar);
            if (Q != null) {
                return aVar.e(Q);
            }
            return null;
        }

        @Override // android.view.Window.Callback
        public void onContentChanged() {
            if (this.f621c) {
                a().onContentChanged();
            }
        }

        @Override // androidx.appcompat.view.i, android.view.Window.Callback
        public boolean onCreatePanelMenu(int i8, Menu menu) {
            if (i8 != 0 || (menu instanceof androidx.appcompat.view.menu.g)) {
                return super.onCreatePanelMenu(i8, menu);
            }
            return false;
        }

        @Override // androidx.appcompat.view.i, android.view.Window.Callback
        public View onCreatePanelView(int i8) {
            View onCreatePanelView;
            i iVar = this.f620b;
            return (iVar == null || (onCreatePanelView = iVar.onCreatePanelView(i8)) == null) ? super.onCreatePanelView(i8) : onCreatePanelView;
        }

        @Override // androidx.appcompat.view.i, android.view.Window.Callback
        public boolean onMenuOpened(int i8, Menu menu) {
            super.onMenuOpened(i8, menu);
            AppCompatDelegateImpl.this.K0(i8);
            return true;
        }

        @Override // androidx.appcompat.view.i, android.view.Window.Callback
        public void onPanelClosed(int i8, Menu menu) {
            if (this.f623e) {
                a().onPanelClosed(i8, menu);
                return;
            }
            super.onPanelClosed(i8, menu);
            AppCompatDelegateImpl.this.L0(i8);
        }

        @Override // androidx.appcompat.view.i, android.view.Window.Callback
        public boolean onPreparePanel(int i8, View view, Menu menu) {
            androidx.appcompat.view.menu.g gVar = menu instanceof androidx.appcompat.view.menu.g ? (androidx.appcompat.view.menu.g) menu : null;
            if (i8 == 0 && gVar == null) {
                return false;
            }
            boolean z4 = true;
            if (gVar != null) {
                gVar.e0(true);
            }
            i iVar = this.f620b;
            if (iVar == null || !iVar.a(i8)) {
                z4 = false;
            }
            if (!z4) {
                z4 = super.onPreparePanel(i8, view, menu);
            }
            if (gVar != null) {
                gVar.e0(false);
            }
            return z4;
        }

        @Override // androidx.appcompat.view.i, android.view.Window.Callback
        public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> list, Menu menu, int i8) {
            androidx.appcompat.view.menu.g gVar;
            PanelFeatureState u02 = AppCompatDelegateImpl.this.u0(0, true);
            if (u02 == null || (gVar = u02.f595j) == null) {
                super.onProvideKeyboardShortcuts(list, menu, i8);
            } else {
                super.onProvideKeyboardShortcuts(list, gVar, i8);
            }
        }

        @Override // androidx.appcompat.view.i, android.view.Window.Callback
        public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
            if (Build.VERSION.SDK_INT >= 23) {
                return null;
            }
            return AppCompatDelegateImpl.this.C0() ? f(callback) : super.onWindowStartingActionMode(callback);
        }

        @Override // androidx.appcompat.view.i, android.view.Window.Callback
        public ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int i8) {
            return (AppCompatDelegateImpl.this.C0() && i8 == 0) ? f(callback) : super.onWindowStartingActionMode(callback, i8);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class r extends s {

        /* renamed from: c  reason: collision with root package name */
        private final PowerManager f625c;

        r(Context context) {
            super();
            this.f625c = (PowerManager) context.getApplicationContext().getSystemService("power");
        }

        @Override // androidx.appcompat.app.AppCompatDelegateImpl.s
        IntentFilter b() {
            if (Build.VERSION.SDK_INT >= 21) {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("android.os.action.POWER_SAVE_MODE_CHANGED");
                return intentFilter;
            }
            return null;
        }

        @Override // androidx.appcompat.app.AppCompatDelegateImpl.s
        public int c() {
            return (Build.VERSION.SDK_INT < 21 || !m.a(this.f625c)) ? 1 : 2;
        }

        @Override // androidx.appcompat.app.AppCompatDelegateImpl.s
        public void d() {
            AppCompatDelegateImpl.this.U();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public abstract class s {

        /* renamed from: a  reason: collision with root package name */
        private BroadcastReceiver f627a;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public class a extends BroadcastReceiver {
            a() {
            }

            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                s.this.d();
            }
        }

        s() {
        }

        void a() {
            BroadcastReceiver broadcastReceiver = this.f627a;
            if (broadcastReceiver != null) {
                try {
                    AppCompatDelegateImpl.this.f565l.unregisterReceiver(broadcastReceiver);
                } catch (IllegalArgumentException unused) {
                }
                this.f627a = null;
            }
        }

        abstract IntentFilter b();

        abstract int c();

        abstract void d();

        void e() {
            a();
            IntentFilter b9 = b();
            if (b9 == null || b9.countActions() == 0) {
                return;
            }
            if (this.f627a == null) {
                this.f627a = new a();
            }
            AppCompatDelegateImpl.this.f565l.registerReceiver(this.f627a, b9);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class t extends s {

        /* renamed from: c  reason: collision with root package name */
        private final androidx.appcompat.app.s f630c;

        t(androidx.appcompat.app.s sVar) {
            super();
            this.f630c = sVar;
        }

        @Override // androidx.appcompat.app.AppCompatDelegateImpl.s
        IntentFilter b() {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.TIME_SET");
            intentFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
            intentFilter.addAction("android.intent.action.TIME_TICK");
            return intentFilter;
        }

        @Override // androidx.appcompat.app.AppCompatDelegateImpl.s
        public int c() {
            return this.f630c.d() ? 2 : 1;
        }

        @Override // androidx.appcompat.app.AppCompatDelegateImpl.s
        public void d() {
            AppCompatDelegateImpl.this.U();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class u {
        static void a(ContextThemeWrapper contextThemeWrapper, Configuration configuration) {
            contextThemeWrapper.applyOverrideConfiguration(configuration);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class v extends ContentFrameLayout {
        public v(Context context) {
            super(context);
        }

        private boolean c(int i8, int i9) {
            return i8 < -5 || i9 < -5 || i8 > getWidth() + 5 || i9 > getHeight() + 5;
        }

        @Override // android.view.ViewGroup, android.view.View
        public boolean dispatchKeyEvent(KeyEvent keyEvent) {
            return AppCompatDelegateImpl.this.i0(keyEvent) || super.dispatchKeyEvent(keyEvent);
        }

        @Override // android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            if (motionEvent.getAction() == 0 && c((int) motionEvent.getX(), (int) motionEvent.getY())) {
                AppCompatDelegateImpl.this.c0(0);
                return true;
            }
            return super.onInterceptTouchEvent(motionEvent);
        }

        @Override // android.view.View
        public void setBackgroundResource(int i8) {
            setBackgroundDrawable(h.a.b(getContext(), i8));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public final class w implements m.a {
        w() {
        }

        @Override // androidx.appcompat.view.menu.m.a
        public void c(androidx.appcompat.view.menu.g gVar, boolean z4) {
            androidx.appcompat.view.menu.g F = gVar.F();
            boolean z8 = F != gVar;
            AppCompatDelegateImpl appCompatDelegateImpl = AppCompatDelegateImpl.this;
            if (z8) {
                gVar = F;
            }
            PanelFeatureState n02 = appCompatDelegateImpl.n0(gVar);
            if (n02 != null) {
                if (!z8) {
                    AppCompatDelegateImpl.this.d0(n02, z4);
                    return;
                }
                AppCompatDelegateImpl.this.Z(n02.f586a, n02, F);
                AppCompatDelegateImpl.this.d0(n02, true);
            }
        }

        @Override // androidx.appcompat.view.menu.m.a
        public boolean d(androidx.appcompat.view.menu.g gVar) {
            Window.Callback w02;
            if (gVar == gVar.F()) {
                AppCompatDelegateImpl appCompatDelegateImpl = AppCompatDelegateImpl.this;
                if (!appCompatDelegateImpl.R || (w02 = appCompatDelegateImpl.w0()) == null || AppCompatDelegateImpl.this.f558f0) {
                    return true;
                }
                w02.onMenuOpened(108, gVar);
                return true;
            }
            return true;
        }
    }

    static {
        int i8 = Build.VERSION.SDK_INT;
        boolean z4 = i8 < 21;
        f551y0 = z4;
        f552z0 = new int[]{16842836};
        A0 = !"robolectric".equals(Build.FINGERPRINT);
        B0 = i8 >= 17;
        if (!z4 || C0) {
            return;
        }
        Thread.setDefaultUncaughtExceptionHandler(new a(Thread.getDefaultUncaughtExceptionHandler()));
        C0 = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AppCompatDelegateImpl(Activity activity, androidx.appcompat.app.d dVar) {
        this(activity, null, dVar, activity);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AppCompatDelegateImpl(Dialog dialog, androidx.appcompat.app.d dVar) {
        this(dialog.getContext(), dialog.getWindow(), dVar, dialog);
    }

    private AppCompatDelegateImpl(Context context, Window window, androidx.appcompat.app.d dVar, Object obj) {
        k0.g<String, Integer> gVar;
        Integer num;
        AppCompatActivity a12;
        this.F = null;
        this.G = true;
        this.f560h0 = -100;
        this.f573p0 = new b();
        this.f565l = context;
        this.f572p = dVar;
        this.f563k = obj;
        if (this.f560h0 == -100 && (obj instanceof Dialog) && (a12 = a1()) != null) {
            this.f560h0 = a12.getDelegate().o();
        }
        if (this.f560h0 == -100 && (num = (gVar = f550x0).get(obj.getClass().getName())) != null) {
            this.f560h0 = num.intValue();
            gVar.remove(obj.getClass().getName());
        }
        if (window != null) {
            W(window);
        }
        androidx.appcompat.widget.g.h();
    }

    private boolean A0(PanelFeatureState panelFeatureState) {
        Context context = this.f565l;
        int i8 = panelFeatureState.f586a;
        if ((i8 == 0 || i8 == 108) && this.f583x != null) {
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = context.getTheme();
            theme.resolveAttribute(g.a.f19867f, typedValue, true);
            Resources.Theme theme2 = null;
            if (typedValue.resourceId != 0) {
                theme2 = context.getResources().newTheme();
                theme2.setTo(theme);
                theme2.applyStyle(typedValue.resourceId, true);
                theme2.resolveAttribute(g.a.f19868g, typedValue, true);
            } else {
                theme.resolveAttribute(g.a.f19868g, typedValue, true);
            }
            if (typedValue.resourceId != 0) {
                if (theme2 == null) {
                    theme2 = context.getResources().newTheme();
                    theme2.setTo(theme);
                }
                theme2.applyStyle(typedValue.resourceId, true);
            }
            if (theme2 != null) {
                androidx.appcompat.view.d dVar = new androidx.appcompat.view.d(context, 0);
                dVar.getTheme().setTo(theme2);
                context = dVar;
            }
        }
        androidx.appcompat.view.menu.g gVar = new androidx.appcompat.view.menu.g(context);
        gVar.V(this);
        panelFeatureState.c(gVar);
        return true;
    }

    private void B0(int i8) {
        this.f571o0 = (1 << i8) | this.f571o0;
        if (this.f570n0) {
            return;
        }
        c0.l0(this.f567m.getDecorView(), this.f573p0);
        this.f570n0 = true;
    }

    private boolean G0(int i8, KeyEvent keyEvent) {
        if (keyEvent.getRepeatCount() == 0) {
            PanelFeatureState u02 = u0(i8, true);
            if (u02.f600o) {
                return false;
            }
            return Q0(u02, keyEvent);
        }
        return false;
    }

    private boolean J0(int i8, KeyEvent keyEvent) {
        boolean z4;
        androidx.appcompat.widget.r rVar;
        if (this.A != null) {
            return false;
        }
        boolean z8 = true;
        PanelFeatureState u02 = u0(i8, true);
        if (i8 != 0 || (rVar = this.f583x) == null || !rVar.d() || ViewConfiguration.get(this.f565l).hasPermanentMenuKey()) {
            boolean z9 = u02.f600o;
            if (z9 || u02.f599n) {
                d0(u02, true);
                z8 = z9;
            } else {
                if (u02.f598m) {
                    if (u02.f602r) {
                        u02.f598m = false;
                        z4 = Q0(u02, keyEvent);
                    } else {
                        z4 = true;
                    }
                    if (z4) {
                        N0(u02, keyEvent);
                    }
                }
                z8 = false;
            }
        } else if (this.f583x.b()) {
            z8 = this.f583x.f();
        } else {
            if (!this.f558f0 && Q0(u02, keyEvent)) {
                z8 = this.f583x.g();
            }
            z8 = false;
        }
        if (z8) {
            AudioManager audioManager = (AudioManager) this.f565l.getApplicationContext().getSystemService("audio");
            if (audioManager != null) {
                audioManager.playSoundEffect(0);
            } else {
                Log.w("AppCompatDelegate", "Couldn't get audio manager");
            }
        }
        return z8;
    }

    /* JADX WARN: Removed duplicated region for block: B:67:0x00f2  */
    /* JADX WARN: Removed duplicated region for block: B:72:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void N0(androidx.appcompat.app.AppCompatDelegateImpl.PanelFeatureState r14, android.view.KeyEvent r15) {
        /*
            Method dump skipped, instructions count: 249
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.app.AppCompatDelegateImpl.N0(androidx.appcompat.app.AppCompatDelegateImpl$PanelFeatureState, android.view.KeyEvent):void");
    }

    private boolean P0(PanelFeatureState panelFeatureState, int i8, KeyEvent keyEvent, int i9) {
        androidx.appcompat.view.menu.g gVar;
        boolean z4 = false;
        if (keyEvent.isSystem()) {
            return false;
        }
        if ((panelFeatureState.f598m || Q0(panelFeatureState, keyEvent)) && (gVar = panelFeatureState.f595j) != null) {
            z4 = gVar.performShortcut(i8, keyEvent, i9);
        }
        if (z4 && (i9 & 1) == 0 && this.f583x == null) {
            d0(panelFeatureState, true);
        }
        return z4;
    }

    private boolean Q0(PanelFeatureState panelFeatureState, KeyEvent keyEvent) {
        androidx.appcompat.widget.r rVar;
        androidx.appcompat.widget.r rVar2;
        androidx.appcompat.widget.r rVar3;
        if (this.f558f0) {
            return false;
        }
        if (panelFeatureState.f598m) {
            return true;
        }
        PanelFeatureState panelFeatureState2 = this.f554b0;
        if (panelFeatureState2 != null && panelFeatureState2 != panelFeatureState) {
            d0(panelFeatureState2, false);
        }
        Window.Callback w02 = w0();
        if (w02 != null) {
            panelFeatureState.f594i = w02.onCreatePanelView(panelFeatureState.f586a);
        }
        int i8 = panelFeatureState.f586a;
        boolean z4 = i8 == 0 || i8 == 108;
        if (z4 && (rVar3 = this.f583x) != null) {
            rVar3.c();
        }
        if (panelFeatureState.f594i == null && (!z4 || !(O0() instanceof androidx.appcompat.app.q))) {
            androidx.appcompat.view.menu.g gVar = panelFeatureState.f595j;
            if (gVar == null || panelFeatureState.f602r) {
                if (gVar == null && (!A0(panelFeatureState) || panelFeatureState.f595j == null)) {
                    return false;
                }
                if (z4 && this.f583x != null) {
                    if (this.f584y == null) {
                        this.f584y = new j();
                    }
                    this.f583x.a(panelFeatureState.f595j, this.f584y);
                }
                panelFeatureState.f595j.h0();
                if (!w02.onCreatePanelMenu(panelFeatureState.f586a, panelFeatureState.f595j)) {
                    panelFeatureState.c(null);
                    if (z4 && (rVar = this.f583x) != null) {
                        rVar.a(null, this.f584y);
                    }
                    return false;
                }
                panelFeatureState.f602r = false;
            }
            panelFeatureState.f595j.h0();
            Bundle bundle = panelFeatureState.f603s;
            if (bundle != null) {
                panelFeatureState.f595j.R(bundle);
                panelFeatureState.f603s = null;
            }
            if (!w02.onPreparePanel(0, panelFeatureState.f594i, panelFeatureState.f595j)) {
                if (z4 && (rVar2 = this.f583x) != null) {
                    rVar2.a(null, this.f584y);
                }
                panelFeatureState.f595j.g0();
                return false;
            }
            boolean z8 = KeyCharacterMap.load(keyEvent != null ? keyEvent.getDeviceId() : -1).getKeyboardType() != 1;
            panelFeatureState.f601p = z8;
            panelFeatureState.f595j.setQwertyMode(z8);
            panelFeatureState.f595j.g0();
        }
        panelFeatureState.f598m = true;
        panelFeatureState.f599n = false;
        this.f554b0 = panelFeatureState;
        return true;
    }

    private void R0(boolean z4) {
        androidx.appcompat.widget.r rVar = this.f583x;
        if (rVar == null || !rVar.d() || (ViewConfiguration.get(this.f565l).hasPermanentMenuKey() && !this.f583x.e())) {
            PanelFeatureState u02 = u0(0, true);
            u02.q = true;
            d0(u02, false);
            N0(u02, null);
            return;
        }
        Window.Callback w02 = w0();
        if (this.f583x.b() && z4) {
            this.f583x.f();
            if (this.f558f0) {
                return;
            }
            w02.onPanelClosed(108, u0(0, true).f595j);
        } else if (w02 == null || this.f558f0) {
        } else {
            if (this.f570n0 && (this.f571o0 & 1) != 0) {
                this.f567m.getDecorView().removeCallbacks(this.f573p0);
                this.f573p0.run();
            }
            PanelFeatureState u03 = u0(0, true);
            androidx.appcompat.view.menu.g gVar = u03.f595j;
            if (gVar == null || u03.f602r || !w02.onPreparePanel(0, u03.f594i, gVar)) {
                return;
            }
            w02.onMenuOpened(108, u03.f595j);
            this.f583x.g();
        }
    }

    private boolean S(boolean z4) {
        return T(z4, true);
    }

    private int S0(int i8) {
        if (i8 == 8) {
            Log.i("AppCompatDelegate", "You should now use the AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR id when requesting this feature.");
            return 108;
        } else if (i8 == 9) {
            Log.i("AppCompatDelegate", "You should now use the AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY id when requesting this feature.");
            return R.styleable.AppCompatTheme_textAppearanceSmallPopupMenu;
        } else {
            return i8;
        }
    }

    private boolean T(boolean z4, boolean z8) {
        if (this.f558f0) {
            return false;
        }
        int Y = Y();
        int D0 = D0(this.f565l, Y);
        androidx.core.os.j X = Build.VERSION.SDK_INT < 33 ? X(this.f565l) : null;
        if (!z8 && X != null) {
            X = t0(this.f565l.getResources().getConfiguration());
        }
        boolean c12 = c1(D0, X, z4);
        if (Y == 0) {
            s0(this.f565l).e();
        } else {
            s sVar = this.f566l0;
            if (sVar != null) {
                sVar.a();
            }
        }
        if (Y == 3) {
            r0(this.f565l).e();
        } else {
            s sVar2 = this.f568m0;
            if (sVar2 != null) {
                sVar2.a();
            }
        }
        return c12;
    }

    private void V() {
        ContentFrameLayout contentFrameLayout = (ContentFrameLayout) this.K.findViewById(16908290);
        View decorView = this.f567m.getDecorView();
        contentFrameLayout.b(decorView.getPaddingLeft(), decorView.getPaddingTop(), decorView.getPaddingRight(), decorView.getPaddingBottom());
        TypedArray obtainStyledAttributes = this.f565l.obtainStyledAttributes(g.j.A0);
        obtainStyledAttributes.getValue(g.j.M0, contentFrameLayout.getMinWidthMajor());
        obtainStyledAttributes.getValue(g.j.N0, contentFrameLayout.getMinWidthMinor());
        int i8 = g.j.K0;
        if (obtainStyledAttributes.hasValue(i8)) {
            obtainStyledAttributes.getValue(i8, contentFrameLayout.getFixedWidthMajor());
        }
        int i9 = g.j.L0;
        if (obtainStyledAttributes.hasValue(i9)) {
            obtainStyledAttributes.getValue(i9, contentFrameLayout.getFixedWidthMinor());
        }
        int i10 = g.j.I0;
        if (obtainStyledAttributes.hasValue(i10)) {
            obtainStyledAttributes.getValue(i10, contentFrameLayout.getFixedHeightMajor());
        }
        int i11 = g.j.J0;
        if (obtainStyledAttributes.hasValue(i11)) {
            obtainStyledAttributes.getValue(i11, contentFrameLayout.getFixedHeightMinor());
        }
        obtainStyledAttributes.recycle();
        contentFrameLayout.requestLayout();
    }

    private void W(Window window) {
        if (this.f567m != null) {
            throw new IllegalStateException("AppCompat has already installed itself into the Window");
        }
        Window.Callback callback = window.getCallback();
        if (callback instanceof q) {
            throw new IllegalStateException("AppCompat has already installed itself into the Window");
        }
        q qVar = new q(callback);
        this.f569n = qVar;
        window.setCallback(qVar);
        j0 u8 = j0.u(this.f565l, null, f552z0);
        Drawable h8 = u8.h(0);
        if (h8 != null) {
            window.setBackgroundDrawable(h8);
        }
        u8.w();
        this.f567m = window;
        if (Build.VERSION.SDK_INT < 33 || this.f580v0 != null) {
            return;
        }
        M(null);
    }

    private boolean W0(ViewParent viewParent) {
        if (viewParent == null) {
            return false;
        }
        View decorView = this.f567m.getDecorView();
        while (viewParent != null) {
            if (viewParent == decorView || !(viewParent instanceof View) || c0.V((View) viewParent)) {
                return false;
            }
            viewParent = viewParent.getParent();
        }
        return true;
    }

    private int Y() {
        int i8 = this.f560h0;
        return i8 != -100 ? i8 : androidx.appcompat.app.f.m();
    }

    private void Z0() {
        if (this.H) {
            throw new AndroidRuntimeException("Window feature must be requested before adding content");
        }
    }

    private AppCompatActivity a1() {
        for (Context context = this.f565l; context != null; context = ((ContextWrapper) context).getBaseContext()) {
            if (context instanceof AppCompatActivity) {
                return (AppCompatActivity) context;
            }
            if (!(context instanceof ContextWrapper)) {
                break;
            }
        }
        return null;
    }

    private void b0() {
        s sVar = this.f566l0;
        if (sVar != null) {
            sVar.a();
        }
        s sVar2 = this.f568m0;
        if (sVar2 != null) {
            sVar2.a();
        }
    }

    private void b1(Configuration configuration) {
        Activity activity = (Activity) this.f563k;
        if (activity instanceof androidx.lifecycle.j) {
            if (!((androidx.lifecycle.j) activity).getLifecycle().b().f(Lifecycle.State.CREATED)) {
                return;
            }
        } else if (!this.f557e0 || this.f558f0) {
            return;
        }
        activity.onConfigurationChanged(configuration);
    }

    /* JADX WARN: Removed duplicated region for block: B:42:0x0088  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean c1(int r9, androidx.core.os.j r10, boolean r11) {
        /*
            r8 = this;
            android.content.Context r1 = r8.f565l
            r4 = 0
            r5 = 0
            r0 = r8
            r2 = r9
            r3 = r10
            android.content.res.Configuration r0 = r0.e0(r1, r2, r3, r4, r5)
            android.content.Context r1 = r8.f565l
            int r1 = r8.q0(r1)
            android.content.res.Configuration r2 = r8.f559g0
            if (r2 != 0) goto L1f
            android.content.Context r2 = r8.f565l
            android.content.res.Resources r2 = r2.getResources()
            android.content.res.Configuration r2 = r2.getConfiguration()
        L1f:
            int r3 = r2.uiMode
            r3 = r3 & 48
            int r4 = r0.uiMode
            r4 = r4 & 48
            androidx.core.os.j r2 = r8.t0(r2)
            r5 = 0
            if (r10 != 0) goto L30
            r0 = r5
            goto L34
        L30:
            androidx.core.os.j r0 = r8.t0(r0)
        L34:
            r6 = 0
            if (r3 == r4) goto L3a
            r3 = 512(0x200, float:7.175E-43)
            goto L3b
        L3a:
            r3 = r6
        L3b:
            if (r0 == 0) goto L4d
            boolean r2 = r2.equals(r0)
            if (r2 != 0) goto L4d
            r3 = r3 | 4
            int r2 = android.os.Build.VERSION.SDK_INT
            r7 = 17
            if (r2 < r7) goto L4d
            r3 = r3 | 8192(0x2000, float:1.14794E-41)
        L4d:
            int r2 = ~r1
            r2 = r2 & r3
            r7 = 1
            if (r2 == 0) goto L77
            if (r11 == 0) goto L77
            boolean r11 = r8.f556d0
            if (r11 == 0) goto L77
            boolean r11 = androidx.appcompat.app.AppCompatDelegateImpl.A0
            if (r11 != 0) goto L60
            boolean r11 = r8.f557e0
            if (r11 == 0) goto L77
        L60:
            java.lang.Object r11 = r8.f563k
            boolean r2 = r11 instanceof android.app.Activity
            if (r2 == 0) goto L77
            android.app.Activity r11 = (android.app.Activity) r11
            boolean r11 = r11.isChild()
            if (r11 != 0) goto L77
            java.lang.Object r11 = r8.f563k
            android.app.Activity r11 = (android.app.Activity) r11
            androidx.core.app.b.s(r11)
            r11 = r7
            goto L78
        L77:
            r11 = r6
        L78:
            if (r11 != 0) goto L85
            if (r3 == 0) goto L85
            r11 = r3 & r1
            if (r11 != r3) goto L81
            r6 = r7
        L81:
            r8.e1(r4, r0, r6, r5)
            goto L86
        L85:
            r7 = r11
        L86:
            if (r7 == 0) goto La2
            java.lang.Object r11 = r8.f563k
            boolean r1 = r11 instanceof androidx.appcompat.app.AppCompatActivity
            if (r1 == 0) goto La2
            r1 = r3 & 512(0x200, float:7.175E-43)
            if (r1 == 0) goto L97
            androidx.appcompat.app.AppCompatActivity r11 = (androidx.appcompat.app.AppCompatActivity) r11
            r11.onNightModeChanged(r9)
        L97:
            r9 = r3 & 4
            if (r9 == 0) goto La2
            java.lang.Object r9 = r8.f563k
            androidx.appcompat.app.AppCompatActivity r9 = (androidx.appcompat.app.AppCompatActivity) r9
            r9.onLocalesChanged(r10)
        La2:
            if (r7 == 0) goto Lb7
            if (r0 == 0) goto Lb7
            android.content.Context r9 = r8.f565l
            android.content.res.Resources r9 = r9.getResources()
            android.content.res.Configuration r9 = r9.getConfiguration()
            androidx.core.os.j r9 = r8.t0(r9)
            r8.U0(r9)
        Lb7:
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.app.AppCompatDelegateImpl.c1(int, androidx.core.os.j, boolean):boolean");
    }

    private Configuration e0(Context context, int i8, androidx.core.os.j jVar, Configuration configuration, boolean z4) {
        int i9 = i8 != 1 ? i8 != 2 ? z4 ? 0 : context.getApplicationContext().getResources().getConfiguration().uiMode & 48 : 32 : 16;
        Configuration configuration2 = new Configuration();
        configuration2.fontScale = 0.0f;
        if (configuration != null) {
            configuration2.setTo(configuration);
        }
        configuration2.uiMode = i9 | (configuration2.uiMode & (-49));
        if (jVar != null) {
            T0(configuration2, jVar);
        }
        return configuration2;
    }

    private void e1(int i8, androidx.core.os.j jVar, boolean z4, Configuration configuration) {
        Resources resources = this.f565l.getResources();
        Configuration configuration2 = new Configuration(resources.getConfiguration());
        if (configuration != null) {
            configuration2.updateFrom(configuration);
        }
        configuration2.uiMode = i8 | (resources.getConfiguration().uiMode & (-49));
        if (jVar != null) {
            T0(configuration2, jVar);
        }
        resources.updateConfiguration(configuration2, null);
        int i9 = Build.VERSION.SDK_INT;
        if (i9 < 26) {
            androidx.appcompat.app.p.a(resources);
        }
        int i10 = this.f561i0;
        if (i10 != 0) {
            this.f565l.setTheme(i10);
            if (i9 >= 23) {
                this.f565l.getTheme().applyStyle(this.f561i0, true);
            }
        }
        if (z4 && (this.f563k instanceof Activity)) {
            b1(configuration2);
        }
    }

    private ViewGroup f0() {
        ViewGroup viewGroup;
        TypedArray obtainStyledAttributes = this.f565l.obtainStyledAttributes(g.j.A0);
        int i8 = g.j.F0;
        if (!obtainStyledAttributes.hasValue(i8)) {
            obtainStyledAttributes.recycle();
            throw new IllegalStateException("You need to use a Theme.AppCompat theme (or descendant) with this activity.");
        }
        if (obtainStyledAttributes.getBoolean(g.j.O0, false)) {
            H(1);
        } else if (obtainStyledAttributes.getBoolean(i8, false)) {
            H(108);
        }
        if (obtainStyledAttributes.getBoolean(g.j.G0, false)) {
            H(R.styleable.AppCompatTheme_textAppearanceSmallPopupMenu);
        }
        if (obtainStyledAttributes.getBoolean(g.j.H0, false)) {
            H(10);
        }
        this.X = obtainStyledAttributes.getBoolean(g.j.B0, false);
        obtainStyledAttributes.recycle();
        m0();
        this.f567m.getDecorView();
        LayoutInflater from = LayoutInflater.from(this.f565l);
        if (this.Y) {
            viewGroup = (ViewGroup) from.inflate(this.W ? g.g.q : g.g.f19976p, (ViewGroup) null);
        } else if (this.X) {
            viewGroup = (ViewGroup) from.inflate(g.g.f19968h, (ViewGroup) null);
            this.T = false;
            this.R = false;
        } else if (this.R) {
            TypedValue typedValue = new TypedValue();
            this.f565l.getTheme().resolveAttribute(g.a.f19867f, typedValue, true);
            viewGroup = (ViewGroup) LayoutInflater.from(typedValue.resourceId != 0 ? new androidx.appcompat.view.d(this.f565l, typedValue.resourceId) : this.f565l).inflate(g.g.f19977r, (ViewGroup) null);
            androidx.appcompat.widget.r rVar = (androidx.appcompat.widget.r) viewGroup.findViewById(g.f.q);
            this.f583x = rVar;
            rVar.setWindowCallback(w0());
            if (this.T) {
                this.f583x.h(R.styleable.AppCompatTheme_textAppearanceSmallPopupMenu);
            }
            if (this.P) {
                this.f583x.h(2);
            }
            if (this.Q) {
                this.f583x.h(5);
            }
        } else {
            viewGroup = null;
        }
        if (viewGroup == null) {
            throw new IllegalArgumentException("AppCompat does not support the current theme features: { windowActionBar: " + this.R + ", windowActionBarOverlay: " + this.T + ", android:windowIsFloating: " + this.X + ", windowActionModeOverlay: " + this.W + ", windowNoTitle: " + this.Y + " }");
        }
        if (Build.VERSION.SDK_INT >= 21) {
            c0.I0(viewGroup, new c());
        } else if (viewGroup instanceof androidx.appcompat.widget.v) {
            ((androidx.appcompat.widget.v) viewGroup).setOnFitSystemWindowsListener(new d());
        }
        if (this.f583x == null) {
            this.L = (TextView) viewGroup.findViewById(g.f.S);
        }
        u0.c(viewGroup);
        ContentFrameLayout contentFrameLayout = (ContentFrameLayout) viewGroup.findViewById(g.f.f19937b);
        ViewGroup viewGroup2 = (ViewGroup) this.f567m.findViewById(16908290);
        if (viewGroup2 != null) {
            while (viewGroup2.getChildCount() > 0) {
                View childAt = viewGroup2.getChildAt(0);
                viewGroup2.removeViewAt(0);
                contentFrameLayout.addView(childAt);
            }
            viewGroup2.setId(-1);
            contentFrameLayout.setId(16908290);
            if (viewGroup2 instanceof FrameLayout) {
                ((FrameLayout) viewGroup2).setForeground(null);
            }
        }
        this.f567m.setContentView(viewGroup);
        contentFrameLayout.setAttachListener(new e());
        return viewGroup;
    }

    private void g1(View view) {
        Context context;
        int i8;
        if ((c0.P(view) & 8192) != 0) {
            context = this.f565l;
            i8 = g.c.f19889b;
        } else {
            context = this.f565l;
            i8 = g.c.f19888a;
        }
        view.setBackgroundColor(androidx.core.content.a.d(context, i8));
    }

    private void l0() {
        if (this.H) {
            return;
        }
        this.K = f0();
        CharSequence v02 = v0();
        if (!TextUtils.isEmpty(v02)) {
            androidx.appcompat.widget.r rVar = this.f583x;
            if (rVar != null) {
                rVar.setWindowTitle(v02);
            } else if (O0() != null) {
                O0().y(v02);
            } else {
                TextView textView = this.L;
                if (textView != null) {
                    textView.setText(v02);
                }
            }
        }
        V();
        M0(this.K);
        this.H = true;
        PanelFeatureState u02 = u0(0, false);
        if (this.f558f0) {
            return;
        }
        if (u02 == null || u02.f595j == null) {
            B0(108);
        }
    }

    private void m0() {
        if (this.f567m == null) {
            Object obj = this.f563k;
            if (obj instanceof Activity) {
                W(((Activity) obj).getWindow());
            }
        }
        if (this.f567m == null) {
            throw new IllegalStateException("We have not been given a Window");
        }
    }

    private static Configuration o0(Configuration configuration, Configuration configuration2) {
        Configuration configuration3 = new Configuration();
        configuration3.fontScale = 0.0f;
        if (configuration2 != null && configuration.diff(configuration2) != 0) {
            float f5 = configuration.fontScale;
            float f8 = configuration2.fontScale;
            if (f5 != f8) {
                configuration3.fontScale = f8;
            }
            int i8 = configuration.mcc;
            int i9 = configuration2.mcc;
            if (i8 != i9) {
                configuration3.mcc = i9;
            }
            int i10 = configuration.mnc;
            int i11 = configuration2.mnc;
            if (i10 != i11) {
                configuration3.mnc = i11;
            }
            int i12 = Build.VERSION.SDK_INT;
            if (i12 >= 24) {
                n.a(configuration, configuration2, configuration3);
            } else if (!androidx.core.util.c.a(configuration.locale, configuration2.locale)) {
                configuration3.locale = configuration2.locale;
            }
            int i13 = configuration.touchscreen;
            int i14 = configuration2.touchscreen;
            if (i13 != i14) {
                configuration3.touchscreen = i14;
            }
            int i15 = configuration.keyboard;
            int i16 = configuration2.keyboard;
            if (i15 != i16) {
                configuration3.keyboard = i16;
            }
            int i17 = configuration.keyboardHidden;
            int i18 = configuration2.keyboardHidden;
            if (i17 != i18) {
                configuration3.keyboardHidden = i18;
            }
            int i19 = configuration.navigation;
            int i20 = configuration2.navigation;
            if (i19 != i20) {
                configuration3.navigation = i20;
            }
            int i21 = configuration.navigationHidden;
            int i22 = configuration2.navigationHidden;
            if (i21 != i22) {
                configuration3.navigationHidden = i22;
            }
            int i23 = configuration.orientation;
            int i24 = configuration2.orientation;
            if (i23 != i24) {
                configuration3.orientation = i24;
            }
            int i25 = configuration.screenLayout & 15;
            int i26 = configuration2.screenLayout;
            if (i25 != (i26 & 15)) {
                configuration3.screenLayout |= i26 & 15;
            }
            int i27 = configuration.screenLayout & 192;
            int i28 = configuration2.screenLayout;
            if (i27 != (i28 & 192)) {
                configuration3.screenLayout |= i28 & 192;
            }
            int i29 = configuration.screenLayout & 48;
            int i30 = configuration2.screenLayout;
            if (i29 != (i30 & 48)) {
                configuration3.screenLayout |= i30 & 48;
            }
            int i31 = configuration.screenLayout & 768;
            int i32 = configuration2.screenLayout;
            if (i31 != (i32 & 768)) {
                configuration3.screenLayout |= i32 & 768;
            }
            if (i12 >= 26) {
                o.a(configuration, configuration2, configuration3);
            }
            int i33 = configuration.uiMode & 15;
            int i34 = configuration2.uiMode;
            if (i33 != (i34 & 15)) {
                configuration3.uiMode |= i34 & 15;
            }
            int i35 = configuration.uiMode & 48;
            int i36 = configuration2.uiMode;
            if (i35 != (i36 & 48)) {
                configuration3.uiMode |= i36 & 48;
            }
            int i37 = configuration.screenWidthDp;
            int i38 = configuration2.screenWidthDp;
            if (i37 != i38) {
                configuration3.screenWidthDp = i38;
            }
            int i39 = configuration.screenHeightDp;
            int i40 = configuration2.screenHeightDp;
            if (i39 != i40) {
                configuration3.screenHeightDp = i40;
            }
            int i41 = configuration.smallestScreenWidthDp;
            int i42 = configuration2.smallestScreenWidthDp;
            if (i41 != i42) {
                configuration3.smallestScreenWidthDp = i42;
            }
            if (i12 >= 17) {
                l.b(configuration, configuration2, configuration3);
            }
        }
        return configuration3;
    }

    private int q0(Context context) {
        if (!this.f564k0 && (this.f563k instanceof Activity)) {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager == null) {
                return 0;
            }
            try {
                int i8 = Build.VERSION.SDK_INT;
                ActivityInfo activityInfo = packageManager.getActivityInfo(new ComponentName(context, this.f563k.getClass()), i8 >= 29 ? 269221888 : i8 >= 24 ? 786432 : 0);
                if (activityInfo != null) {
                    this.f562j0 = activityInfo.configChanges;
                }
            } catch (PackageManager.NameNotFoundException e8) {
                Log.d("AppCompatDelegate", "Exception while getting ActivityInfo", e8);
                this.f562j0 = 0;
            }
        }
        this.f564k0 = true;
        return this.f562j0;
    }

    private s r0(Context context) {
        if (this.f568m0 == null) {
            this.f568m0 = new r(context);
        }
        return this.f568m0;
    }

    private s s0(Context context) {
        if (this.f566l0 == null) {
            this.f566l0 = new t(androidx.appcompat.app.s.a(context));
        }
        return this.f566l0;
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0032  */
    /* JADX WARN: Removed duplicated region for block: B:19:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void x0() {
        /*
            r3 = this;
            r3.l0()
            boolean r0 = r3.R
            if (r0 == 0) goto L37
            androidx.appcompat.app.ActionBar r0 = r3.q
            if (r0 == 0) goto Lc
            goto L37
        Lc:
            java.lang.Object r0 = r3.f563k
            boolean r1 = r0 instanceof android.app.Activity
            if (r1 == 0) goto L20
            androidx.appcompat.app.t r0 = new androidx.appcompat.app.t
            java.lang.Object r1 = r3.f563k
            android.app.Activity r1 = (android.app.Activity) r1
            boolean r2 = r3.T
            r0.<init>(r1, r2)
        L1d:
            r3.q = r0
            goto L2e
        L20:
            boolean r0 = r0 instanceof android.app.Dialog
            if (r0 == 0) goto L2e
            androidx.appcompat.app.t r0 = new androidx.appcompat.app.t
            java.lang.Object r1 = r3.f563k
            android.app.Dialog r1 = (android.app.Dialog) r1
            r0.<init>(r1)
            goto L1d
        L2e:
            androidx.appcompat.app.ActionBar r0 = r3.q
            if (r0 == 0) goto L37
            boolean r1 = r3.f574q0
            r0.r(r1)
        L37:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.app.AppCompatDelegateImpl.x0():void");
    }

    private boolean y0(PanelFeatureState panelFeatureState) {
        View view = panelFeatureState.f594i;
        if (view != null) {
            panelFeatureState.f593h = view;
            return true;
        } else if (panelFeatureState.f595j == null) {
            return false;
        } else {
            if (this.f585z == null) {
                this.f585z = new w();
            }
            View view2 = (View) panelFeatureState.a(this.f585z);
            panelFeatureState.f593h = view2;
            return view2 != null;
        }
    }

    private boolean z0(PanelFeatureState panelFeatureState) {
        panelFeatureState.d(p0());
        panelFeatureState.f592g = new v(panelFeatureState.f597l);
        panelFeatureState.f588c = 81;
        return true;
    }

    @Override // androidx.appcompat.app.f
    public void A(Bundle bundle) {
        l0();
    }

    @Override // androidx.appcompat.app.f
    public void B() {
        ActionBar s8 = s();
        if (s8 != null) {
            s8.w(true);
        }
    }

    @Override // androidx.appcompat.app.f
    public void C(Bundle bundle) {
    }

    public boolean C0() {
        return this.G;
    }

    @Override // androidx.appcompat.app.f
    public void D() {
        T(true, false);
    }

    int D0(Context context, int i8) {
        s s02;
        if (i8 != -100) {
            if (i8 != -1) {
                if (i8 != 0) {
                    if (i8 != 1 && i8 != 2) {
                        if (i8 != 3) {
                            throw new IllegalStateException("Unknown value set for night mode. Please use one of the MODE_NIGHT values from AppCompatDelegate.");
                        }
                        s02 = r0(context);
                    }
                } else if (Build.VERSION.SDK_INT >= 23 && ((UiModeManager) context.getApplicationContext().getSystemService("uimode")).getNightMode() == 0) {
                    return -1;
                } else {
                    s02 = s0(context);
                }
                return s02.c();
            }
            return i8;
        }
        return -1;
    }

    @Override // androidx.appcompat.app.f
    public void E() {
        ActionBar s8 = s();
        if (s8 != null) {
            s8.w(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean E0() {
        boolean z4 = this.f555c0;
        this.f555c0 = false;
        PanelFeatureState u02 = u0(0, false);
        if (u02 != null && u02.f600o) {
            if (!z4) {
                d0(u02, true);
            }
            return true;
        }
        androidx.appcompat.view.b bVar = this.A;
        if (bVar != null) {
            bVar.c();
            return true;
        }
        ActionBar s8 = s();
        return s8 != null && s8.h();
    }

    boolean F0(int i8, KeyEvent keyEvent) {
        if (i8 == 4) {
            this.f555c0 = (keyEvent.getFlags() & RecognitionOptions.ITF) != 0;
        } else if (i8 == 82) {
            G0(0, keyEvent);
            return true;
        }
        return false;
    }

    @Override // androidx.appcompat.app.f
    public boolean H(int i8) {
        int S0 = S0(i8);
        if (this.Y && S0 == 108) {
            return false;
        }
        if (this.R && S0 == 1) {
            this.R = false;
        }
        if (S0 == 1) {
            Z0();
            this.Y = true;
            return true;
        } else if (S0 == 2) {
            Z0();
            this.P = true;
            return true;
        } else if (S0 == 5) {
            Z0();
            this.Q = true;
            return true;
        } else if (S0 == 10) {
            Z0();
            this.W = true;
            return true;
        } else if (S0 == 108) {
            Z0();
            this.R = true;
            return true;
        } else if (S0 != 109) {
            return this.f567m.requestFeature(S0);
        } else {
            Z0();
            this.T = true;
            return true;
        }
    }

    boolean H0(int i8, KeyEvent keyEvent) {
        ActionBar s8 = s();
        if (s8 == null || !s8.o(i8, keyEvent)) {
            PanelFeatureState panelFeatureState = this.f554b0;
            if (panelFeatureState != null && P0(panelFeatureState, keyEvent.getKeyCode(), keyEvent, 1)) {
                PanelFeatureState panelFeatureState2 = this.f554b0;
                if (panelFeatureState2 != null) {
                    panelFeatureState2.f599n = true;
                }
                return true;
            }
            if (this.f554b0 == null) {
                PanelFeatureState u02 = u0(0, true);
                Q0(u02, keyEvent);
                boolean P0 = P0(u02, keyEvent.getKeyCode(), keyEvent, 1);
                u02.f598m = false;
                if (P0) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    boolean I0(int i8, KeyEvent keyEvent) {
        if (i8 != 4) {
            if (i8 == 82) {
                J0(0, keyEvent);
                return true;
            }
        } else if (E0()) {
            return true;
        }
        return false;
    }

    @Override // androidx.appcompat.app.f
    public void J(int i8) {
        l0();
        ViewGroup viewGroup = (ViewGroup) this.K.findViewById(16908290);
        viewGroup.removeAllViews();
        LayoutInflater.from(this.f565l).inflate(i8, viewGroup);
        this.f569n.c(this.f567m.getCallback());
    }

    @Override // androidx.appcompat.app.f
    public void K(View view) {
        l0();
        ViewGroup viewGroup = (ViewGroup) this.K.findViewById(16908290);
        viewGroup.removeAllViews();
        viewGroup.addView(view);
        this.f569n.c(this.f567m.getCallback());
    }

    void K0(int i8) {
        ActionBar s8;
        if (i8 != 108 || (s8 = s()) == null) {
            return;
        }
        s8.i(true);
    }

    @Override // androidx.appcompat.app.f
    public void L(View view, ViewGroup.LayoutParams layoutParams) {
        l0();
        ViewGroup viewGroup = (ViewGroup) this.K.findViewById(16908290);
        viewGroup.removeAllViews();
        viewGroup.addView(view, layoutParams);
        this.f569n.c(this.f567m.getCallback());
    }

    void L0(int i8) {
        if (i8 == 108) {
            ActionBar s8 = s();
            if (s8 != null) {
                s8.i(false);
            }
        } else if (i8 == 0) {
            PanelFeatureState u02 = u0(i8, true);
            if (u02.f600o) {
                d0(u02, false);
            }
        }
    }

    @Override // androidx.appcompat.app.f
    public void M(OnBackInvokedDispatcher onBackInvokedDispatcher) {
        OnBackInvokedCallback onBackInvokedCallback;
        super.M(onBackInvokedDispatcher);
        OnBackInvokedDispatcher onBackInvokedDispatcher2 = this.f580v0;
        if (onBackInvokedDispatcher2 != null && (onBackInvokedCallback = this.f582w0) != null) {
            p.c(onBackInvokedDispatcher2, onBackInvokedCallback);
            this.f582w0 = null;
        }
        if (onBackInvokedDispatcher == null) {
            Object obj = this.f563k;
            if ((obj instanceof Activity) && ((Activity) obj).getWindow() != null) {
                onBackInvokedDispatcher = p.a((Activity) this.f563k);
            }
        }
        this.f580v0 = onBackInvokedDispatcher;
        d1();
    }

    void M0(ViewGroup viewGroup) {
    }

    @Override // androidx.appcompat.app.f
    public void N(Toolbar toolbar) {
        if (this.f563k instanceof Activity) {
            ActionBar s8 = s();
            if (s8 instanceof androidx.appcompat.app.t) {
                throw new IllegalStateException("This Activity already has an action bar supplied by the window decor. Do not request Window.FEATURE_SUPPORT_ACTION_BAR and set windowActionBar to false in your theme to use a Toolbar instead.");
            }
            this.f577t = null;
            if (s8 != null) {
                s8.n();
            }
            this.q = null;
            if (toolbar != null) {
                androidx.appcompat.app.q qVar = new androidx.appcompat.app.q(toolbar, v0(), this.f569n);
                this.q = qVar;
                this.f569n.e(qVar.f700c);
                toolbar.setBackInvokedCallbackEnabled(true);
            } else {
                this.f569n.e(null);
            }
            u();
        }
    }

    @Override // androidx.appcompat.app.f
    public void O(int i8) {
        this.f561i0 = i8;
    }

    final ActionBar O0() {
        return this.q;
    }

    @Override // androidx.appcompat.app.f
    public final void P(CharSequence charSequence) {
        this.f581w = charSequence;
        androidx.appcompat.widget.r rVar = this.f583x;
        if (rVar != null) {
            rVar.setWindowTitle(charSequence);
        } else if (O0() != null) {
            O0().y(charSequence);
        } else {
            TextView textView = this.L;
            if (textView != null) {
                textView.setText(charSequence);
            }
        }
    }

    @Override // androidx.appcompat.app.f
    public androidx.appcompat.view.b Q(b.a aVar) {
        androidx.appcompat.app.d dVar;
        if (aVar != null) {
            androidx.appcompat.view.b bVar = this.A;
            if (bVar != null) {
                bVar.c();
            }
            k kVar = new k(aVar);
            ActionBar s8 = s();
            if (s8 != null) {
                androidx.appcompat.view.b z4 = s8.z(kVar);
                this.A = z4;
                if (z4 != null && (dVar = this.f572p) != null) {
                    dVar.onSupportActionModeStarted(z4);
                }
            }
            if (this.A == null) {
                this.A = Y0(kVar);
            }
            d1();
            return this.A;
        }
        throw new IllegalArgumentException("ActionMode callback can not be null.");
    }

    void T0(Configuration configuration, androidx.core.os.j jVar) {
        int i8 = Build.VERSION.SDK_INT;
        if (i8 >= 24) {
            n.d(configuration, jVar);
        } else if (i8 < 17) {
            configuration.locale = jVar.d(0);
        } else {
            l.d(configuration, jVar.d(0));
            l.c(configuration, jVar.d(0));
        }
    }

    public boolean U() {
        return S(true);
    }

    void U0(androidx.core.os.j jVar) {
        if (Build.VERSION.SDK_INT >= 24) {
            n.c(jVar);
        } else {
            Locale.setDefault(jVar.d(0));
        }
    }

    final boolean V0() {
        ViewGroup viewGroup;
        return this.H && (viewGroup = this.K) != null && c0.W(viewGroup);
    }

    androidx.core.os.j X(Context context) {
        androidx.core.os.j r4;
        int i8 = Build.VERSION.SDK_INT;
        if (i8 < 33 && (r4 = androidx.appcompat.app.f.r()) != null) {
            androidx.core.os.j t02 = t0(context.getApplicationContext().getResources().getConfiguration());
            androidx.core.os.j b9 = i8 >= 24 ? androidx.appcompat.app.o.b(r4, t02) : r4.f() ? androidx.core.os.j.e() : androidx.core.os.j.c(r4.d(0).toString());
            return b9.f() ? t02 : b9;
        }
        return null;
    }

    boolean X0() {
        if (this.f580v0 == null) {
            return false;
        }
        PanelFeatureState u02 = u0(0, false);
        return (u02 != null && u02.f600o) || this.A != null;
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0025  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0029  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    androidx.appcompat.view.b Y0(androidx.appcompat.view.b.a r8) {
        /*
            Method dump skipped, instructions count: 364
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.app.AppCompatDelegateImpl.Y0(androidx.appcompat.view.b$a):androidx.appcompat.view.b");
    }

    void Z(int i8, PanelFeatureState panelFeatureState, Menu menu) {
        if (menu == null) {
            if (panelFeatureState == null && i8 >= 0) {
                PanelFeatureState[] panelFeatureStateArr = this.f553a0;
                if (i8 < panelFeatureStateArr.length) {
                    panelFeatureState = panelFeatureStateArr[i8];
                }
            }
            if (panelFeatureState != null) {
                menu = panelFeatureState.f595j;
            }
        }
        if ((panelFeatureState == null || panelFeatureState.f600o) && !this.f558f0) {
            this.f569n.d(this.f567m.getCallback(), i8, menu);
        }
    }

    @Override // androidx.appcompat.view.menu.g.a
    public boolean a(androidx.appcompat.view.menu.g gVar, MenuItem menuItem) {
        PanelFeatureState n02;
        Window.Callback w02 = w0();
        if (w02 == null || this.f558f0 || (n02 = n0(gVar.F())) == null) {
            return false;
        }
        return w02.onMenuItemSelected(n02.f586a, menuItem);
    }

    void a0(androidx.appcompat.view.menu.g gVar) {
        if (this.Z) {
            return;
        }
        this.Z = true;
        this.f583x.i();
        Window.Callback w02 = w0();
        if (w02 != null && !this.f558f0) {
            w02.onPanelClosed(108, gVar);
        }
        this.Z = false;
    }

    @Override // androidx.appcompat.view.menu.g.a
    public void b(androidx.appcompat.view.menu.g gVar) {
        R0(true);
    }

    void c0(int i8) {
        d0(u0(i8, true), true);
    }

    void d0(PanelFeatureState panelFeatureState, boolean z4) {
        ViewGroup viewGroup;
        androidx.appcompat.widget.r rVar;
        if (z4 && panelFeatureState.f586a == 0 && (rVar = this.f583x) != null && rVar.b()) {
            a0(panelFeatureState.f595j);
            return;
        }
        WindowManager windowManager = (WindowManager) this.f565l.getSystemService("window");
        if (windowManager != null && panelFeatureState.f600o && (viewGroup = panelFeatureState.f592g) != null) {
            windowManager.removeView(viewGroup);
            if (z4) {
                Z(panelFeatureState.f586a, panelFeatureState, null);
            }
        }
        panelFeatureState.f598m = false;
        panelFeatureState.f599n = false;
        panelFeatureState.f600o = false;
        panelFeatureState.f593h = null;
        panelFeatureState.q = true;
        if (this.f554b0 == panelFeatureState) {
            this.f554b0 = null;
        }
        if (panelFeatureState.f586a == 0) {
            d1();
        }
    }

    void d1() {
        OnBackInvokedCallback onBackInvokedCallback;
        if (Build.VERSION.SDK_INT >= 33) {
            boolean X0 = X0();
            if (X0 && this.f582w0 == null) {
                this.f582w0 = p.b(this.f580v0, this);
            } else if (X0 || (onBackInvokedCallback = this.f582w0) == null) {
            } else {
                p.c(this.f580v0, onBackInvokedCallback);
            }
        }
    }

    @Override // androidx.appcompat.app.f
    public void e(View view, ViewGroup.LayoutParams layoutParams) {
        l0();
        ((ViewGroup) this.K.findViewById(16908290)).addView(view, layoutParams);
        this.f569n.c(this.f567m.getCallback());
    }

    final int f1(m0 m0Var, Rect rect) {
        boolean z4;
        boolean z8;
        int m8 = m0Var != null ? m0Var.m() : rect != null ? rect.top : 0;
        ActionBarContextView actionBarContextView = this.B;
        if (actionBarContextView == null || !(actionBarContextView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)) {
            z4 = false;
        } else {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.B.getLayoutParams();
            if (this.B.isShown()) {
                if (this.f575r0 == null) {
                    this.f575r0 = new Rect();
                    this.f576s0 = new Rect();
                }
                Rect rect2 = this.f575r0;
                Rect rect3 = this.f576s0;
                if (m0Var == null) {
                    rect2.set(rect);
                } else {
                    rect2.set(m0Var.k(), m0Var.m(), m0Var.l(), m0Var.j());
                }
                u0.a(this.K, rect2, rect3);
                int i8 = rect2.top;
                int i9 = rect2.left;
                int i10 = rect2.right;
                m0 L = c0.L(this.K);
                int k8 = L == null ? 0 : L.k();
                int l8 = L == null ? 0 : L.l();
                if (marginLayoutParams.topMargin == i8 && marginLayoutParams.leftMargin == i9 && marginLayoutParams.rightMargin == i10) {
                    z8 = false;
                } else {
                    marginLayoutParams.topMargin = i8;
                    marginLayoutParams.leftMargin = i9;
                    marginLayoutParams.rightMargin = i10;
                    z8 = true;
                }
                if (i8 <= 0 || this.O != null) {
                    View view = this.O;
                    if (view != null) {
                        ViewGroup.MarginLayoutParams marginLayoutParams2 = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                        int i11 = marginLayoutParams2.height;
                        int i12 = marginLayoutParams.topMargin;
                        if (i11 != i12 || marginLayoutParams2.leftMargin != k8 || marginLayoutParams2.rightMargin != l8) {
                            marginLayoutParams2.height = i12;
                            marginLayoutParams2.leftMargin = k8;
                            marginLayoutParams2.rightMargin = l8;
                            this.O.setLayoutParams(marginLayoutParams2);
                        }
                    }
                } else {
                    View view2 = new View(this.f565l);
                    this.O = view2;
                    view2.setVisibility(8);
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, marginLayoutParams.topMargin, 51);
                    layoutParams.leftMargin = k8;
                    layoutParams.rightMargin = l8;
                    this.K.addView(this.O, -1, layoutParams);
                }
                View view3 = this.O;
                r5 = view3 != null;
                if (r5 && view3.getVisibility() != 0) {
                    g1(this.O);
                }
                if (!this.W && r5) {
                    m8 = 0;
                }
                z4 = r5;
                r5 = z8;
            } else if (marginLayoutParams.topMargin != 0) {
                marginLayoutParams.topMargin = 0;
                z4 = false;
            } else {
                z4 = false;
                r5 = false;
            }
            if (r5) {
                this.B.setLayoutParams(marginLayoutParams);
            }
        }
        View view4 = this.O;
        if (view4 != null) {
            view4.setVisibility(z4 ? 0 : 8);
        }
        return m8;
    }

    @Override // androidx.appcompat.app.f
    public Context g(Context context) {
        this.f556d0 = true;
        int D0 = D0(context, Y());
        if (androidx.appcompat.app.f.v(context)) {
            androidx.appcompat.app.f.R(context);
        }
        androidx.core.os.j X = X(context);
        if (B0 && (context instanceof ContextThemeWrapper)) {
            try {
                u.a((ContextThemeWrapper) context, e0(context, D0, X, null, false));
                return context;
            } catch (IllegalStateException unused) {
            }
        }
        if (context instanceof androidx.appcompat.view.d) {
            try {
                ((androidx.appcompat.view.d) context).a(e0(context, D0, X, null, false));
                return context;
            } catch (IllegalStateException unused2) {
            }
        }
        if (A0) {
            Configuration configuration = null;
            if (Build.VERSION.SDK_INT >= 17) {
                Configuration configuration2 = new Configuration();
                configuration2.uiMode = -1;
                configuration2.fontScale = 0.0f;
                Configuration configuration3 = l.a(context, configuration2).getResources().getConfiguration();
                Configuration configuration4 = context.getResources().getConfiguration();
                configuration3.uiMode = configuration4.uiMode;
                if (!configuration3.equals(configuration4)) {
                    configuration = o0(configuration3, configuration4);
                }
            }
            Configuration e02 = e0(context, D0, X, configuration, true);
            androidx.appcompat.view.d dVar = new androidx.appcompat.view.d(context, g.i.f20001e);
            dVar.a(e02);
            boolean z4 = false;
            try {
                z4 = context.getTheme() != null;
            } catch (NullPointerException unused3) {
            }
            if (z4) {
                h.g.a(dVar.getTheme());
            }
            return super.g(dVar);
        }
        return super.g(context);
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x007f, code lost:
        if (((org.xmlpull.v1.XmlPullParser) r15).getDepth() > 1) goto L27;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public android.view.View g0(android.view.View r12, java.lang.String r13, android.content.Context r14, android.util.AttributeSet r15) {
        /*
            r11 = this;
            androidx.appcompat.app.k r0 = r11.f578t0
            r1 = 0
            if (r0 != 0) goto L5a
            android.content.Context r0 = r11.f565l
            int[] r2 = g.j.A0
            android.content.res.TypedArray r0 = r0.obtainStyledAttributes(r2)
            int r2 = g.j.E0
            java.lang.String r0 = r0.getString(r2)
            if (r0 != 0) goto L1d
            androidx.appcompat.app.k r0 = new androidx.appcompat.app.k
            r0.<init>()
        L1a:
            r11.f578t0 = r0
            goto L5a
        L1d:
            android.content.Context r2 = r11.f565l     // Catch: java.lang.Throwable -> L38
            java.lang.ClassLoader r2 = r2.getClassLoader()     // Catch: java.lang.Throwable -> L38
            java.lang.Class r2 = r2.loadClass(r0)     // Catch: java.lang.Throwable -> L38
            java.lang.Class[] r3 = new java.lang.Class[r1]     // Catch: java.lang.Throwable -> L38
            java.lang.reflect.Constructor r2 = r2.getDeclaredConstructor(r3)     // Catch: java.lang.Throwable -> L38
            java.lang.Object[] r3 = new java.lang.Object[r1]     // Catch: java.lang.Throwable -> L38
            java.lang.Object r2 = r2.newInstance(r3)     // Catch: java.lang.Throwable -> L38
            androidx.appcompat.app.k r2 = (androidx.appcompat.app.k) r2     // Catch: java.lang.Throwable -> L38
            r11.f578t0 = r2     // Catch: java.lang.Throwable -> L38
            goto L5a
        L38:
            r2 = move-exception
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Failed to instantiate custom view inflater "
            r3.append(r4)
            r3.append(r0)
            java.lang.String r0 = ". Falling back to default."
            r3.append(r0)
            java.lang.String r0 = r3.toString()
            java.lang.String r3 = "AppCompatDelegate"
            android.util.Log.i(r3, r0, r2)
            androidx.appcompat.app.k r0 = new androidx.appcompat.app.k
            r0.<init>()
            goto L1a
        L5a:
            boolean r8 = androidx.appcompat.app.AppCompatDelegateImpl.f551y0
            r0 = 1
            if (r8 == 0) goto L8a
            androidx.appcompat.app.n r2 = r11.f579u0
            if (r2 != 0) goto L6a
            androidx.appcompat.app.n r2 = new androidx.appcompat.app.n
            r2.<init>()
            r11.f579u0 = r2
        L6a:
            androidx.appcompat.app.n r2 = r11.f579u0
            boolean r2 = r2.a(r15)
            if (r2 == 0) goto L74
            r7 = r0
            goto L8b
        L74:
            boolean r2 = r15 instanceof org.xmlpull.v1.XmlPullParser
            if (r2 == 0) goto L82
            r2 = r15
            org.xmlpull.v1.XmlPullParser r2 = (org.xmlpull.v1.XmlPullParser) r2
            int r2 = r2.getDepth()
            if (r2 <= r0) goto L8a
            goto L89
        L82:
            r0 = r12
            android.view.ViewParent r0 = (android.view.ViewParent) r0
            boolean r0 = r11.W0(r0)
        L89:
            r1 = r0
        L8a:
            r7 = r1
        L8b:
            androidx.appcompat.app.k r2 = r11.f578t0
            r9 = 1
            boolean r10 = androidx.appcompat.widget.t0.d()
            r3 = r12
            r4 = r13
            r5 = r14
            r6 = r15
            android.view.View r12 = r2.r(r3, r4, r5, r6, r7, r8, r9, r10)
            return r12
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.app.AppCompatDelegateImpl.g0(android.view.View, java.lang.String, android.content.Context, android.util.AttributeSet):android.view.View");
    }

    void h0() {
        androidx.appcompat.view.menu.g gVar;
        androidx.appcompat.widget.r rVar = this.f583x;
        if (rVar != null) {
            rVar.i();
        }
        if (this.C != null) {
            this.f567m.getDecorView().removeCallbacks(this.E);
            if (this.C.isShowing()) {
                try {
                    this.C.dismiss();
                } catch (IllegalArgumentException unused) {
                }
            }
            this.C = null;
        }
        k0();
        PanelFeatureState u02 = u0(0, false);
        if (u02 == null || (gVar = u02.f595j) == null) {
            return;
        }
        gVar.close();
    }

    boolean i0(KeyEvent keyEvent) {
        View decorView;
        Object obj = this.f563k;
        if (((obj instanceof g.a) || (obj instanceof androidx.appcompat.app.i)) && (decorView = this.f567m.getDecorView()) != null && androidx.core.view.g.d(decorView, keyEvent)) {
            return true;
        }
        if (keyEvent.getKeyCode() == 82 && this.f569n.b(this.f567m.getCallback(), keyEvent)) {
            return true;
        }
        int keyCode = keyEvent.getKeyCode();
        return keyEvent.getAction() == 0 ? F0(keyCode, keyEvent) : I0(keyCode, keyEvent);
    }

    @Override // androidx.appcompat.app.f
    public <T extends View> T j(int i8) {
        l0();
        return (T) this.f567m.findViewById(i8);
    }

    void j0(int i8) {
        PanelFeatureState u02;
        PanelFeatureState u03 = u0(i8, true);
        if (u03.f595j != null) {
            Bundle bundle = new Bundle();
            u03.f595j.T(bundle);
            if (bundle.size() > 0) {
                u03.f603s = bundle;
            }
            u03.f595j.h0();
            u03.f595j.clear();
        }
        u03.f602r = true;
        u03.q = true;
        if ((i8 != 108 && i8 != 0) || this.f583x == null || (u02 = u0(0, false)) == null) {
            return;
        }
        u02.f598m = false;
        Q0(u02, null);
    }

    void k0() {
        i0 i0Var = this.F;
        if (i0Var != null) {
            i0Var.c();
        }
    }

    @Override // androidx.appcompat.app.f
    public Context l() {
        return this.f565l;
    }

    @Override // androidx.appcompat.app.f
    public final a.b n() {
        return new h();
    }

    PanelFeatureState n0(Menu menu) {
        PanelFeatureState[] panelFeatureStateArr = this.f553a0;
        int length = panelFeatureStateArr != null ? panelFeatureStateArr.length : 0;
        for (int i8 = 0; i8 < length; i8++) {
            PanelFeatureState panelFeatureState = panelFeatureStateArr[i8];
            if (panelFeatureState != null && panelFeatureState.f595j == menu) {
                return panelFeatureState;
            }
        }
        return null;
    }

    @Override // androidx.appcompat.app.f
    public int o() {
        return this.f560h0;
    }

    @Override // android.view.LayoutInflater.Factory2
    public final View onCreateView(View view, String str, Context context, AttributeSet attributeSet) {
        return g0(view, str, context, attributeSet);
    }

    @Override // android.view.LayoutInflater.Factory
    public View onCreateView(String str, Context context, AttributeSet attributeSet) {
        return onCreateView(null, str, context, attributeSet);
    }

    final Context p0() {
        ActionBar s8 = s();
        Context k8 = s8 != null ? s8.k() : null;
        return k8 == null ? this.f565l : k8;
    }

    @Override // androidx.appcompat.app.f
    public MenuInflater q() {
        if (this.f577t == null) {
            x0();
            ActionBar actionBar = this.q;
            this.f577t = new androidx.appcompat.view.g(actionBar != null ? actionBar.k() : this.f565l);
        }
        return this.f577t;
    }

    @Override // androidx.appcompat.app.f
    public ActionBar s() {
        x0();
        return this.q;
    }

    @Override // androidx.appcompat.app.f
    public void t() {
        LayoutInflater from = LayoutInflater.from(this.f565l);
        if (from.getFactory() == null) {
            androidx.core.view.h.b(from, this);
        } else if (from.getFactory2() instanceof AppCompatDelegateImpl) {
        } else {
            Log.i("AppCompatDelegate", "The Activity's LayoutInflater already has a Factory installed so we can not install AppCompat's");
        }
    }

    androidx.core.os.j t0(Configuration configuration) {
        int i8 = Build.VERSION.SDK_INT;
        return i8 >= 24 ? n.b(configuration) : i8 >= 21 ? androidx.core.os.j.c(m.b(configuration.locale)) : androidx.core.os.j.a(configuration.locale);
    }

    @Override // androidx.appcompat.app.f
    public void u() {
        if (O0() == null || s().l()) {
            return;
        }
        B0(0);
    }

    protected PanelFeatureState u0(int i8, boolean z4) {
        PanelFeatureState[] panelFeatureStateArr = this.f553a0;
        if (panelFeatureStateArr == null || panelFeatureStateArr.length <= i8) {
            PanelFeatureState[] panelFeatureStateArr2 = new PanelFeatureState[i8 + 1];
            if (panelFeatureStateArr != null) {
                System.arraycopy(panelFeatureStateArr, 0, panelFeatureStateArr2, 0, panelFeatureStateArr.length);
            }
            this.f553a0 = panelFeatureStateArr2;
            panelFeatureStateArr = panelFeatureStateArr2;
        }
        PanelFeatureState panelFeatureState = panelFeatureStateArr[i8];
        if (panelFeatureState == null) {
            PanelFeatureState panelFeatureState2 = new PanelFeatureState(i8);
            panelFeatureStateArr[i8] = panelFeatureState2;
            return panelFeatureState2;
        }
        return panelFeatureState;
    }

    final CharSequence v0() {
        Object obj = this.f563k;
        return obj instanceof Activity ? ((Activity) obj).getTitle() : this.f581w;
    }

    final Window.Callback w0() {
        return this.f567m.getCallback();
    }

    @Override // androidx.appcompat.app.f
    public void x(Configuration configuration) {
        ActionBar s8;
        if (this.R && this.H && (s8 = s()) != null) {
            s8.m(configuration);
        }
        androidx.appcompat.widget.g.b().g(this.f565l);
        this.f559g0 = new Configuration(this.f565l.getResources().getConfiguration());
        T(false, false);
    }

    @Override // androidx.appcompat.app.f
    public void y(Bundle bundle) {
        this.f556d0 = true;
        S(false);
        m0();
        Object obj = this.f563k;
        if (obj instanceof Activity) {
            String str = null;
            try {
                str = androidx.core.app.i.c((Activity) obj);
            } catch (IllegalArgumentException unused) {
            }
            if (str != null) {
                ActionBar O0 = O0();
                if (O0 == null) {
                    this.f574q0 = true;
                } else {
                    O0.r(true);
                }
            }
            androidx.appcompat.app.f.d(this);
        }
        this.f559g0 = new Configuration(this.f565l.getResources().getConfiguration());
        this.f557e0 = true;
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0058  */
    @Override // androidx.appcompat.app.f
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void z() {
        /*
            r3 = this;
            java.lang.Object r0 = r3.f563k
            boolean r0 = r0 instanceof android.app.Activity
            if (r0 == 0) goto L9
            androidx.appcompat.app.f.F(r3)
        L9:
            boolean r0 = r3.f570n0
            if (r0 == 0) goto L18
            android.view.Window r0 = r3.f567m
            android.view.View r0 = r0.getDecorView()
            java.lang.Runnable r1 = r3.f573p0
            r0.removeCallbacks(r1)
        L18:
            r0 = 1
            r3.f558f0 = r0
            int r0 = r3.f560h0
            r1 = -100
            if (r0 == r1) goto L45
            java.lang.Object r0 = r3.f563k
            boolean r1 = r0 instanceof android.app.Activity
            if (r1 == 0) goto L45
            android.app.Activity r0 = (android.app.Activity) r0
            boolean r0 = r0.isChangingConfigurations()
            if (r0 == 0) goto L45
            k0.g<java.lang.String, java.lang.Integer> r0 = androidx.appcompat.app.AppCompatDelegateImpl.f550x0
            java.lang.Object r1 = r3.f563k
            java.lang.Class r1 = r1.getClass()
            java.lang.String r1 = r1.getName()
            int r2 = r3.f560h0
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
            r0.put(r1, r2)
            goto L54
        L45:
            k0.g<java.lang.String, java.lang.Integer> r0 = androidx.appcompat.app.AppCompatDelegateImpl.f550x0
            java.lang.Object r1 = r3.f563k
            java.lang.Class r1 = r1.getClass()
            java.lang.String r1 = r1.getName()
            r0.remove(r1)
        L54:
            androidx.appcompat.app.ActionBar r0 = r3.q
            if (r0 == 0) goto L5b
            r0.n()
        L5b:
            r3.b0()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.app.AppCompatDelegateImpl.z():void");
    }
}
