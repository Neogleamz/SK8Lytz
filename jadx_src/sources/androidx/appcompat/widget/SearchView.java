package androidx.appcompat.widget;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.SearchableInfo;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.customview.view.AbsSavedState;
import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.lang.reflect.Method;
import java.util.WeakHashMap;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class SearchView extends LinearLayoutCompat implements androidx.appcompat.view.c {
    static final o E0;
    final ImageView A;
    private final TextView.OnEditorActionListener A0;
    final ImageView B;
    private final AdapterView.OnItemClickListener B0;
    final ImageView C;
    private final AdapterView.OnItemSelectedListener C0;
    private TextWatcher D0;
    private final View E;
    private p F;
    private Rect G;
    private Rect H;
    private int[] K;
    private int[] L;
    private final ImageView O;
    private final Drawable P;
    private final int Q;
    private final int R;
    private final Intent T;
    private final Intent W;

    /* renamed from: a0  reason: collision with root package name */
    private final CharSequence f1269a0;

    /* renamed from: b0  reason: collision with root package name */
    private m f1270b0;

    /* renamed from: c0  reason: collision with root package name */
    private l f1271c0;

    /* renamed from: d0  reason: collision with root package name */
    View.OnFocusChangeListener f1272d0;

    /* renamed from: e0  reason: collision with root package name */
    private n f1273e0;

    /* renamed from: f0  reason: collision with root package name */
    private View.OnClickListener f1274f0;

    /* renamed from: g0  reason: collision with root package name */
    private boolean f1275g0;

    /* renamed from: h0  reason: collision with root package name */
    private boolean f1276h0;

    /* renamed from: i0  reason: collision with root package name */
    v0.a f1277i0;

    /* renamed from: j0  reason: collision with root package name */
    private boolean f1278j0;

    /* renamed from: k0  reason: collision with root package name */
    private CharSequence f1279k0;

    /* renamed from: l0  reason: collision with root package name */
    private boolean f1280l0;

    /* renamed from: m0  reason: collision with root package name */
    private boolean f1281m0;

    /* renamed from: n0  reason: collision with root package name */
    private int f1282n0;

    /* renamed from: o0  reason: collision with root package name */
    private boolean f1283o0;

    /* renamed from: p0  reason: collision with root package name */
    private CharSequence f1284p0;

    /* renamed from: q0  reason: collision with root package name */
    private CharSequence f1285q0;

    /* renamed from: r0  reason: collision with root package name */
    private boolean f1286r0;

    /* renamed from: s0  reason: collision with root package name */
    private int f1287s0;

    /* renamed from: t  reason: collision with root package name */
    final SearchAutoComplete f1288t;

    /* renamed from: t0  reason: collision with root package name */
    SearchableInfo f1289t0;

    /* renamed from: u0  reason: collision with root package name */
    private Bundle f1290u0;

    /* renamed from: v0  reason: collision with root package name */
    private final Runnable f1291v0;

    /* renamed from: w  reason: collision with root package name */
    private final View f1292w;

    /* renamed from: w0  reason: collision with root package name */
    private Runnable f1293w0;

    /* renamed from: x  reason: collision with root package name */
    private final View f1294x;

    /* renamed from: x0  reason: collision with root package name */
    private final WeakHashMap<String, Drawable.ConstantState> f1295x0;

    /* renamed from: y  reason: collision with root package name */
    private final View f1296y;

    /* renamed from: y0  reason: collision with root package name */
    private final View.OnClickListener f1297y0;

    /* renamed from: z  reason: collision with root package name */
    final ImageView f1298z;

    /* renamed from: z0  reason: collision with root package name */
    View.OnKeyListener f1299z0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: c  reason: collision with root package name */
        boolean f1300c;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a implements Parcelable.ClassLoaderCreator<SavedState> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a */
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }

            @Override // android.os.Parcelable.ClassLoaderCreator
            /* renamed from: b */
            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: c */
            public SavedState[] newArray(int i8) {
                return new SavedState[i8];
            }
        }

        public SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.f1300c = ((Boolean) parcel.readValue(null)).booleanValue();
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public String toString() {
            return "SearchView.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " isIconified=" + this.f1300c + "}";
        }

        @Override // androidx.customview.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i8) {
            super.writeToParcel(parcel, i8);
            parcel.writeValue(Boolean.valueOf(this.f1300c));
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class SearchAutoComplete extends AppCompatAutoCompleteTextView {

        /* renamed from: e  reason: collision with root package name */
        private int f1301e;

        /* renamed from: f  reason: collision with root package name */
        private SearchView f1302f;

        /* renamed from: g  reason: collision with root package name */
        private boolean f1303g;

        /* renamed from: h  reason: collision with root package name */
        final Runnable f1304h;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a implements Runnable {
            a() {
            }

            @Override // java.lang.Runnable
            public void run() {
                SearchAutoComplete.this.d();
            }
        }

        public SearchAutoComplete(Context context, AttributeSet attributeSet) {
            this(context, attributeSet, g.a.f19877p);
        }

        public SearchAutoComplete(Context context, AttributeSet attributeSet, int i8) {
            super(context, attributeSet, i8);
            this.f1304h = new a();
            this.f1301e = getThreshold();
        }

        private int getSearchViewTextMinWidthDp() {
            Configuration configuration = getResources().getConfiguration();
            int i8 = configuration.screenWidthDp;
            int i9 = configuration.screenHeightDp;
            if (i8 < 960 || i9 < 720 || configuration.orientation != 2) {
                if (i8 < 600) {
                    return (i8 < 640 || i9 < 480) ? 160 : 192;
                }
                return 192;
            }
            return RecognitionOptions.QR_CODE;
        }

        void b() {
            if (Build.VERSION.SDK_INT < 29) {
                SearchView.E0.c(this);
                return;
            }
            k.b(this, 1);
            if (enoughToFilter()) {
                showDropDown();
            }
        }

        boolean c() {
            return TextUtils.getTrimmedLength(getText()) == 0;
        }

        void d() {
            if (this.f1303g) {
                ((InputMethodManager) getContext().getSystemService("input_method")).showSoftInput(this, 0);
                this.f1303g = false;
            }
        }

        @Override // android.widget.AutoCompleteTextView
        public boolean enoughToFilter() {
            return this.f1301e <= 0 || super.enoughToFilter();
        }

        @Override // androidx.appcompat.widget.AppCompatAutoCompleteTextView, android.widget.TextView, android.view.View
        public InputConnection onCreateInputConnection(EditorInfo editorInfo) {
            InputConnection onCreateInputConnection = super.onCreateInputConnection(editorInfo);
            if (this.f1303g) {
                removeCallbacks(this.f1304h);
                post(this.f1304h);
            }
            return onCreateInputConnection;
        }

        @Override // android.view.View
        protected void onFinishInflate() {
            super.onFinishInflate();
            setMinWidth((int) TypedValue.applyDimension(1, getSearchViewTextMinWidthDp(), getResources().getDisplayMetrics()));
        }

        @Override // android.widget.AutoCompleteTextView, android.widget.TextView, android.view.View
        protected void onFocusChanged(boolean z4, int i8, Rect rect) {
            super.onFocusChanged(z4, i8, rect);
            this.f1302f.Z();
        }

        @Override // android.widget.AutoCompleteTextView, android.widget.TextView, android.view.View
        public boolean onKeyPreIme(int i8, KeyEvent keyEvent) {
            if (i8 == 4) {
                if (keyEvent.getAction() == 0 && keyEvent.getRepeatCount() == 0) {
                    KeyEvent.DispatcherState keyDispatcherState = getKeyDispatcherState();
                    if (keyDispatcherState != null) {
                        keyDispatcherState.startTracking(keyEvent, this);
                    }
                    return true;
                } else if (keyEvent.getAction() == 1) {
                    KeyEvent.DispatcherState keyDispatcherState2 = getKeyDispatcherState();
                    if (keyDispatcherState2 != null) {
                        keyDispatcherState2.handleUpEvent(keyEvent);
                    }
                    if (keyEvent.isTracking() && !keyEvent.isCanceled()) {
                        this.f1302f.clearFocus();
                        setImeVisibility(false);
                        return true;
                    }
                }
            }
            return super.onKeyPreIme(i8, keyEvent);
        }

        @Override // android.widget.AutoCompleteTextView, android.widget.TextView, android.view.View
        public void onWindowFocusChanged(boolean z4) {
            super.onWindowFocusChanged(z4);
            if (z4 && this.f1302f.hasFocus() && getVisibility() == 0) {
                this.f1303g = true;
                if (SearchView.M(getContext())) {
                    b();
                }
            }
        }

        @Override // android.widget.AutoCompleteTextView
        public void performCompletion() {
        }

        @Override // android.widget.AutoCompleteTextView
        protected void replaceText(CharSequence charSequence) {
        }

        void setImeVisibility(boolean z4) {
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService("input_method");
            if (!z4) {
                this.f1303g = false;
                removeCallbacks(this.f1304h);
                inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
            } else if (!inputMethodManager.isActive(this)) {
                this.f1303g = true;
            } else {
                this.f1303g = false;
                removeCallbacks(this.f1304h);
                inputMethodManager.showSoftInput(this, 0);
            }
        }

        void setSearchView(SearchView searchView) {
            this.f1302f = searchView;
        }

        @Override // android.widget.AutoCompleteTextView
        public void setThreshold(int i8) {
            super.setThreshold(i8);
            this.f1301e = i8;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements TextWatcher {
        a() {
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i8, int i9, int i10) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i8, int i9, int i10) {
            SearchView.this.Y(charSequence);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b implements Runnable {
        b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            SearchView.this.f0();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class c implements Runnable {
        c() {
        }

        @Override // java.lang.Runnable
        public void run() {
            v0.a aVar = SearchView.this.f1277i0;
            if (aVar instanceof d0) {
                aVar.a(null);
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class d implements View.OnFocusChangeListener {
        d() {
        }

        @Override // android.view.View.OnFocusChangeListener
        public void onFocusChange(View view, boolean z4) {
            SearchView searchView = SearchView.this;
            View.OnFocusChangeListener onFocusChangeListener = searchView.f1272d0;
            if (onFocusChangeListener != null) {
                onFocusChangeListener.onFocusChange(searchView, z4);
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class e implements View.OnLayoutChangeListener {
        e() {
        }

        @Override // android.view.View.OnLayoutChangeListener
        public void onLayoutChange(View view, int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15) {
            SearchView.this.B();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class f implements View.OnClickListener {
        f() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            SearchView searchView = SearchView.this;
            if (view == searchView.f1298z) {
                searchView.V();
            } else if (view == searchView.B) {
                searchView.R();
            } else if (view == searchView.A) {
                searchView.W();
            } else if (view == searchView.C) {
                searchView.a0();
            } else if (view == searchView.f1288t) {
                searchView.H();
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class g implements View.OnKeyListener {
        g() {
        }

        @Override // android.view.View.OnKeyListener
        public boolean onKey(View view, int i8, KeyEvent keyEvent) {
            SearchView searchView = SearchView.this;
            if (searchView.f1289t0 == null) {
                return false;
            }
            if (!searchView.f1288t.isPopupShowing() || SearchView.this.f1288t.getListSelection() == -1) {
                if (!SearchView.this.f1288t.c() && keyEvent.hasNoModifiers() && keyEvent.getAction() == 1 && i8 == 66) {
                    view.cancelLongPress();
                    SearchView searchView2 = SearchView.this;
                    searchView2.P(0, null, searchView2.f1288t.getText().toString());
                    return true;
                }
                return false;
            }
            return SearchView.this.X(view, i8, keyEvent);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class h implements TextView.OnEditorActionListener {
        h() {
        }

        @Override // android.widget.TextView.OnEditorActionListener
        public boolean onEditorAction(TextView textView, int i8, KeyEvent keyEvent) {
            SearchView.this.W();
            return true;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class i implements AdapterView.OnItemClickListener {
        i() {
        }

        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> adapterView, View view, int i8, long j8) {
            SearchView.this.S(i8, 0, null);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class j implements AdapterView.OnItemSelectedListener {
        j() {
        }

        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onItemSelected(AdapterView<?> adapterView, View view, int i8, long j8) {
            SearchView.this.T(i8);
        }

        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class k {
        static void a(AutoCompleteTextView autoCompleteTextView) {
            autoCompleteTextView.refreshAutoCompleteResults();
        }

        static void b(SearchAutoComplete searchAutoComplete, int i8) {
            searchAutoComplete.setInputMethodMode(i8);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface l {
        boolean onClose();
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface m {
        boolean a(String str);

        boolean b(String str);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface n {
        boolean a(int i8);

        boolean b(int i8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class o {

        /* renamed from: a  reason: collision with root package name */
        private Method f1316a;

        /* renamed from: b  reason: collision with root package name */
        private Method f1317b;

        /* renamed from: c  reason: collision with root package name */
        private Method f1318c;

        @SuppressLint({"DiscouragedPrivateApi", "SoonBlockedPrivateApi"})
        o() {
            this.f1316a = null;
            this.f1317b = null;
            this.f1318c = null;
            d();
            try {
                Method declaredMethod = AutoCompleteTextView.class.getDeclaredMethod("doBeforeTextChanged", new Class[0]);
                this.f1316a = declaredMethod;
                declaredMethod.setAccessible(true);
            } catch (NoSuchMethodException unused) {
            }
            try {
                Method declaredMethod2 = AutoCompleteTextView.class.getDeclaredMethod("doAfterTextChanged", new Class[0]);
                this.f1317b = declaredMethod2;
                declaredMethod2.setAccessible(true);
            } catch (NoSuchMethodException unused2) {
            }
            try {
                Method method = AutoCompleteTextView.class.getMethod("ensureImeVisible", Boolean.TYPE);
                this.f1318c = method;
                method.setAccessible(true);
            } catch (NoSuchMethodException unused3) {
            }
        }

        private static void d() {
            if (Build.VERSION.SDK_INT >= 29) {
                throw new UnsupportedClassVersionError("This function can only be used for API Level < 29.");
            }
        }

        void a(AutoCompleteTextView autoCompleteTextView) {
            d();
            Method method = this.f1317b;
            if (method != null) {
                try {
                    method.invoke(autoCompleteTextView, new Object[0]);
                } catch (Exception unused) {
                }
            }
        }

        void b(AutoCompleteTextView autoCompleteTextView) {
            d();
            Method method = this.f1316a;
            if (method != null) {
                try {
                    method.invoke(autoCompleteTextView, new Object[0]);
                } catch (Exception unused) {
                }
            }
        }

        void c(AutoCompleteTextView autoCompleteTextView) {
            d();
            Method method = this.f1318c;
            if (method != null) {
                try {
                    method.invoke(autoCompleteTextView, Boolean.TRUE);
                } catch (Exception unused) {
                }
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class p extends TouchDelegate {

        /* renamed from: a  reason: collision with root package name */
        private final View f1319a;

        /* renamed from: b  reason: collision with root package name */
        private final Rect f1320b;

        /* renamed from: c  reason: collision with root package name */
        private final Rect f1321c;

        /* renamed from: d  reason: collision with root package name */
        private final Rect f1322d;

        /* renamed from: e  reason: collision with root package name */
        private final int f1323e;

        /* renamed from: f  reason: collision with root package name */
        private boolean f1324f;

        public p(Rect rect, Rect rect2, View view) {
            super(rect, view);
            this.f1323e = ViewConfiguration.get(view.getContext()).getScaledTouchSlop();
            this.f1320b = new Rect();
            this.f1322d = new Rect();
            this.f1321c = new Rect();
            a(rect, rect2);
            this.f1319a = view;
        }

        public void a(Rect rect, Rect rect2) {
            this.f1320b.set(rect);
            this.f1322d.set(rect);
            Rect rect3 = this.f1322d;
            int i8 = this.f1323e;
            rect3.inset(-i8, -i8);
            this.f1321c.set(rect2);
        }

        @Override // android.view.TouchDelegate
        public boolean onTouchEvent(MotionEvent motionEvent) {
            boolean z4;
            float f5;
            int i8;
            boolean z8;
            int x8 = (int) motionEvent.getX();
            int y8 = (int) motionEvent.getY();
            int action = motionEvent.getAction();
            boolean z9 = true;
            if (action != 0) {
                if (action == 1 || action == 2) {
                    z8 = this.f1324f;
                    if (z8 && !this.f1322d.contains(x8, y8)) {
                        z9 = z8;
                        z4 = false;
                    }
                } else {
                    if (action == 3) {
                        z8 = this.f1324f;
                        this.f1324f = false;
                    }
                    z4 = true;
                    z9 = false;
                }
                z9 = z8;
                z4 = true;
            } else {
                if (this.f1320b.contains(x8, y8)) {
                    this.f1324f = true;
                    z4 = true;
                }
                z4 = true;
                z9 = false;
            }
            if (z9) {
                if (!z4 || this.f1321c.contains(x8, y8)) {
                    Rect rect = this.f1321c;
                    f5 = x8 - rect.left;
                    i8 = y8 - rect.top;
                } else {
                    f5 = this.f1319a.getWidth() / 2;
                    i8 = this.f1319a.getHeight() / 2;
                }
                motionEvent.setLocation(f5, i8);
                return this.f1319a.dispatchTouchEvent(motionEvent);
            }
            return false;
        }
    }

    static {
        E0 = Build.VERSION.SDK_INT < 29 ? new o() : null;
    }

    public SearchView(Context context) {
        this(context, null);
    }

    public SearchView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, g.a.L);
    }

    public SearchView(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        this.G = new Rect();
        this.H = new Rect();
        this.K = new int[2];
        this.L = new int[2];
        this.f1291v0 = new b();
        this.f1293w0 = new c();
        this.f1295x0 = new WeakHashMap<>();
        f fVar = new f();
        this.f1297y0 = fVar;
        this.f1299z0 = new g();
        h hVar = new h();
        this.A0 = hVar;
        i iVar = new i();
        this.B0 = iVar;
        j jVar = new j();
        this.C0 = jVar;
        this.D0 = new a();
        int[] iArr = g.j.f20085p2;
        j0 v8 = j0.v(context, attributeSet, iArr, i8, 0);
        androidx.core.view.c0.r0(this, context, iArr, attributeSet, v8.r(), i8, 0);
        LayoutInflater.from(context).inflate(v8.n(g.j.f20134z2, g.g.f19979t), (ViewGroup) this, true);
        SearchAutoComplete searchAutoComplete = (SearchAutoComplete) findViewById(g.f.J);
        this.f1288t = searchAutoComplete;
        searchAutoComplete.setSearchView(this);
        this.f1292w = findViewById(g.f.F);
        View findViewById = findViewById(g.f.I);
        this.f1294x = findViewById;
        View findViewById2 = findViewById(g.f.P);
        this.f1296y = findViewById2;
        ImageView imageView = (ImageView) findViewById(g.f.D);
        this.f1298z = imageView;
        ImageView imageView2 = (ImageView) findViewById(g.f.G);
        this.A = imageView2;
        ImageView imageView3 = (ImageView) findViewById(g.f.E);
        this.B = imageView3;
        ImageView imageView4 = (ImageView) findViewById(g.f.K);
        this.C = imageView4;
        ImageView imageView5 = (ImageView) findViewById(g.f.H);
        this.O = imageView5;
        androidx.core.view.c0.x0(findViewById, v8.g(g.j.A2));
        androidx.core.view.c0.x0(findViewById2, v8.g(g.j.E2));
        int i9 = g.j.D2;
        imageView.setImageDrawable(v8.g(i9));
        imageView2.setImageDrawable(v8.g(g.j.f20124x2));
        imageView3.setImageDrawable(v8.g(g.j.f20109u2));
        imageView4.setImageDrawable(v8.g(g.j.G2));
        imageView5.setImageDrawable(v8.g(i9));
        this.P = v8.g(g.j.C2);
        o0.a(imageView, getResources().getString(g.h.f19996o));
        this.Q = v8.n(g.j.F2, g.g.f19978s);
        this.R = v8.n(g.j.f20114v2, 0);
        imageView.setOnClickListener(fVar);
        imageView3.setOnClickListener(fVar);
        imageView2.setOnClickListener(fVar);
        imageView4.setOnClickListener(fVar);
        searchAutoComplete.setOnClickListener(fVar);
        searchAutoComplete.addTextChangedListener(this.D0);
        searchAutoComplete.setOnEditorActionListener(hVar);
        searchAutoComplete.setOnItemClickListener(iVar);
        searchAutoComplete.setOnItemSelectedListener(jVar);
        searchAutoComplete.setOnKeyListener(this.f1299z0);
        searchAutoComplete.setOnFocusChangeListener(new d());
        setIconifiedByDefault(v8.a(g.j.f20129y2, true));
        int f5 = v8.f(g.j.f20094r2, -1);
        if (f5 != -1) {
            setMaxWidth(f5);
        }
        this.f1269a0 = v8.p(g.j.f20119w2);
        this.f1279k0 = v8.p(g.j.B2);
        int k8 = v8.k(g.j.f20104t2, -1);
        if (k8 != -1) {
            setImeOptions(k8);
        }
        int k9 = v8.k(g.j.f20099s2, -1);
        if (k9 != -1) {
            setInputType(k9);
        }
        setFocusable(v8.a(g.j.f20089q2, true));
        v8.w();
        Intent intent = new Intent("android.speech.action.WEB_SEARCH");
        this.T = intent;
        intent.addFlags(268435456);
        intent.putExtra("android.speech.extra.LANGUAGE_MODEL", "web_search");
        Intent intent2 = new Intent("android.speech.action.RECOGNIZE_SPEECH");
        this.W = intent2;
        intent2.addFlags(268435456);
        View findViewById3 = findViewById(searchAutoComplete.getDropDownAnchor());
        this.E = findViewById3;
        if (findViewById3 != null) {
            findViewById3.addOnLayoutChangeListener(new e());
        }
        k0(this.f1275g0);
        g0();
    }

    private Intent C(String str, Uri uri, String str2, String str3, int i8, String str4) {
        Intent intent = new Intent(str);
        intent.addFlags(268435456);
        if (uri != null) {
            intent.setData(uri);
        }
        intent.putExtra("user_query", this.f1285q0);
        if (str3 != null) {
            intent.putExtra("query", str3);
        }
        if (str2 != null) {
            intent.putExtra("intent_extra_data_key", str2);
        }
        Bundle bundle = this.f1290u0;
        if (bundle != null) {
            intent.putExtra("app_data", bundle);
        }
        if (i8 != 0) {
            intent.putExtra("action_key", i8);
            intent.putExtra("action_msg", str4);
        }
        intent.setComponent(this.f1289t0.getSearchActivity());
        return intent;
    }

    private Intent D(Cursor cursor, int i8, String str) {
        int i9;
        String o5;
        try {
            String o8 = d0.o(cursor, "suggest_intent_action");
            if (o8 == null) {
                o8 = this.f1289t0.getSuggestIntentAction();
            }
            if (o8 == null) {
                o8 = "android.intent.action.SEARCH";
            }
            String str2 = o8;
            String o9 = d0.o(cursor, "suggest_intent_data");
            if (o9 == null) {
                o9 = this.f1289t0.getSuggestIntentData();
            }
            if (o9 != null && (o5 = d0.o(cursor, "suggest_intent_data_id")) != null) {
                o9 = o9 + "/" + Uri.encode(o5);
            }
            return C(str2, o9 == null ? null : Uri.parse(o9), d0.o(cursor, "suggest_intent_extra_data"), d0.o(cursor, "suggest_intent_query"), i8, str);
        } catch (RuntimeException e8) {
            try {
                i9 = cursor.getPosition();
            } catch (RuntimeException unused) {
                i9 = -1;
            }
            Log.w("SearchView", "Search suggestions cursor at row " + i9 + " returned exception.", e8);
            return null;
        }
    }

    private Intent E(Intent intent, SearchableInfo searchableInfo) {
        ComponentName searchActivity = searchableInfo.getSearchActivity();
        Intent intent2 = new Intent("android.intent.action.SEARCH");
        intent2.setComponent(searchActivity);
        PendingIntent activity = PendingIntent.getActivity(getContext(), 0, intent2, 1107296256);
        Bundle bundle = new Bundle();
        Bundle bundle2 = this.f1290u0;
        if (bundle2 != null) {
            bundle.putParcelable("app_data", bundle2);
        }
        Intent intent3 = new Intent(intent);
        Resources resources = getResources();
        String string = searchableInfo.getVoiceLanguageModeId() != 0 ? resources.getString(searchableInfo.getVoiceLanguageModeId()) : "free_form";
        String string2 = searchableInfo.getVoicePromptTextId() != 0 ? resources.getString(searchableInfo.getVoicePromptTextId()) : null;
        String string3 = searchableInfo.getVoiceLanguageId() != 0 ? resources.getString(searchableInfo.getVoiceLanguageId()) : null;
        int voiceMaxResults = searchableInfo.getVoiceMaxResults() != 0 ? searchableInfo.getVoiceMaxResults() : 1;
        intent3.putExtra("android.speech.extra.LANGUAGE_MODEL", string);
        intent3.putExtra("android.speech.extra.PROMPT", string2);
        intent3.putExtra("android.speech.extra.LANGUAGE", string3);
        intent3.putExtra("android.speech.extra.MAX_RESULTS", voiceMaxResults);
        intent3.putExtra("calling_package", searchActivity != null ? searchActivity.flattenToShortString() : null);
        intent3.putExtra("android.speech.extra.RESULTS_PENDINGINTENT", activity);
        intent3.putExtra("android.speech.extra.RESULTS_PENDINGINTENT_BUNDLE", bundle);
        return intent3;
    }

    private Intent F(Intent intent, SearchableInfo searchableInfo) {
        Intent intent2 = new Intent(intent);
        ComponentName searchActivity = searchableInfo.getSearchActivity();
        intent2.putExtra("calling_package", searchActivity == null ? null : searchActivity.flattenToShortString());
        return intent2;
    }

    private void G() {
        this.f1288t.dismissDropDown();
    }

    private void I(View view, Rect rect) {
        view.getLocationInWindow(this.K);
        getLocationInWindow(this.L);
        int[] iArr = this.K;
        int i8 = iArr[1];
        int[] iArr2 = this.L;
        int i9 = i8 - iArr2[1];
        int i10 = iArr[0] - iArr2[0];
        rect.set(i10, i9, view.getWidth() + i10, view.getHeight() + i9);
    }

    private CharSequence J(CharSequence charSequence) {
        if (!this.f1275g0 || this.P == null) {
            return charSequence;
        }
        int textSize = (int) (this.f1288t.getTextSize() * 1.25d);
        this.P.setBounds(0, 0, textSize, textSize);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("   ");
        spannableStringBuilder.setSpan(new ImageSpan(this.P), 1, 2, 33);
        spannableStringBuilder.append(charSequence);
        return spannableStringBuilder;
    }

    private boolean K() {
        SearchableInfo searchableInfo = this.f1289t0;
        if (searchableInfo == null || !searchableInfo.getVoiceSearchEnabled()) {
            return false;
        }
        Intent intent = null;
        if (this.f1289t0.getVoiceSearchLaunchWebSearch()) {
            intent = this.T;
        } else if (this.f1289t0.getVoiceSearchLaunchRecognizer()) {
            intent = this.W;
        }
        return (intent == null || getContext().getPackageManager().resolveActivity(intent, 65536) == null) ? false : true;
    }

    static boolean M(Context context) {
        return context.getResources().getConfiguration().orientation == 2;
    }

    private boolean N() {
        return (this.f1278j0 || this.f1283o0) && !L();
    }

    private void O(Intent intent) {
        if (intent == null) {
            return;
        }
        try {
            getContext().startActivity(intent);
        } catch (RuntimeException e8) {
            Log.e("SearchView", "Failed launch activity: " + intent, e8);
        }
    }

    private boolean Q(int i8, int i9, String str) {
        Cursor b9 = this.f1277i0.b();
        if (b9 == null || !b9.moveToPosition(i8)) {
            return false;
        }
        O(D(b9, i9, str));
        return true;
    }

    private void b0() {
        post(this.f1291v0);
    }

    private void c0(int i8) {
        CharSequence c9;
        Editable text = this.f1288t.getText();
        Cursor b9 = this.f1277i0.b();
        if (b9 == null) {
            return;
        }
        if (!b9.moveToPosition(i8) || (c9 = this.f1277i0.c(b9)) == null) {
            setQuery(text);
        } else {
            setQuery(c9);
        }
    }

    private void e0() {
        boolean z4 = true;
        boolean z8 = !TextUtils.isEmpty(this.f1288t.getText());
        if (!z8 && (!this.f1275g0 || this.f1286r0)) {
            z4 = false;
        }
        this.B.setVisibility(z4 ? 0 : 8);
        Drawable drawable = this.B.getDrawable();
        if (drawable != null) {
            drawable.setState(z8 ? ViewGroup.ENABLED_STATE_SET : ViewGroup.EMPTY_STATE_SET);
        }
    }

    private void g0() {
        CharSequence queryHint = getQueryHint();
        SearchAutoComplete searchAutoComplete = this.f1288t;
        if (queryHint == null) {
            queryHint = BuildConfig.FLAVOR;
        }
        searchAutoComplete.setHint(J(queryHint));
    }

    private int getPreferredHeight() {
        return getContext().getResources().getDimensionPixelSize(g.d.f19902g);
    }

    private int getPreferredWidth() {
        return getContext().getResources().getDimensionPixelSize(g.d.f19903h);
    }

    private void h0() {
        this.f1288t.setThreshold(this.f1289t0.getSuggestThreshold());
        this.f1288t.setImeOptions(this.f1289t0.getImeOptions());
        int inputType = this.f1289t0.getInputType();
        if ((inputType & 15) == 1) {
            inputType &= -65537;
            if (this.f1289t0.getSuggestAuthority() != null) {
                inputType = inputType | 65536 | 524288;
            }
        }
        this.f1288t.setInputType(inputType);
        v0.a aVar = this.f1277i0;
        if (aVar != null) {
            aVar.a(null);
        }
        if (this.f1289t0.getSuggestAuthority() != null) {
            d0 d0Var = new d0(getContext(), this, this.f1289t0, this.f1295x0);
            this.f1277i0 = d0Var;
            this.f1288t.setAdapter(d0Var);
            ((d0) this.f1277i0).x(this.f1280l0 ? 2 : 1);
        }
    }

    private void i0() {
        this.f1296y.setVisibility((N() && (this.A.getVisibility() == 0 || this.C.getVisibility() == 0)) ? 0 : 8);
    }

    private void j0(boolean z4) {
        this.A.setVisibility((this.f1278j0 && N() && hasFocus() && (z4 || !this.f1283o0)) ? 0 : 8);
    }

    private void k0(boolean z4) {
        this.f1276h0 = z4;
        int i8 = 0;
        int i9 = z4 ? 0 : 8;
        boolean z8 = !TextUtils.isEmpty(this.f1288t.getText());
        this.f1298z.setVisibility(i9);
        j0(z8);
        this.f1292w.setVisibility(z4 ? 8 : 0);
        if (this.O.getDrawable() == null || this.f1275g0) {
            i8 = 8;
        }
        this.O.setVisibility(i8);
        e0();
        l0(!z8);
        i0();
    }

    private void l0(boolean z4) {
        int i8 = 8;
        if (this.f1283o0 && !L() && z4) {
            this.A.setVisibility(8);
            i8 = 0;
        }
        this.C.setVisibility(i8);
    }

    private void setQuery(CharSequence charSequence) {
        this.f1288t.setText(charSequence);
        this.f1288t.setSelection(TextUtils.isEmpty(charSequence) ? 0 : charSequence.length());
    }

    void B() {
        if (this.E.getWidth() > 1) {
            Resources resources = getContext().getResources();
            int paddingLeft = this.f1294x.getPaddingLeft();
            Rect rect = new Rect();
            boolean b9 = u0.b(this);
            int dimensionPixelSize = this.f1275g0 ? resources.getDimensionPixelSize(g.d.f19900e) + resources.getDimensionPixelSize(g.d.f19901f) : 0;
            this.f1288t.getDropDownBackground().getPadding(rect);
            int i8 = rect.left;
            this.f1288t.setDropDownHorizontalOffset(b9 ? -i8 : paddingLeft - (i8 + dimensionPixelSize));
            this.f1288t.setDropDownWidth((((this.E.getWidth() + rect.left) + rect.right) + dimensionPixelSize) - paddingLeft);
        }
    }

    void H() {
        if (Build.VERSION.SDK_INT >= 29) {
            k.a(this.f1288t);
            return;
        }
        o oVar = E0;
        oVar.b(this.f1288t);
        oVar.a(this.f1288t);
    }

    public boolean L() {
        return this.f1276h0;
    }

    void P(int i8, String str, String str2) {
        getContext().startActivity(C("android.intent.action.SEARCH", null, null, str2, i8, str));
    }

    void R() {
        if (!TextUtils.isEmpty(this.f1288t.getText())) {
            this.f1288t.setText(BuildConfig.FLAVOR);
            this.f1288t.requestFocus();
            this.f1288t.setImeVisibility(true);
        } else if (this.f1275g0) {
            l lVar = this.f1271c0;
            if (lVar == null || !lVar.onClose()) {
                clearFocus();
                k0(true);
            }
        }
    }

    boolean S(int i8, int i9, String str) {
        n nVar = this.f1273e0;
        if (nVar == null || !nVar.b(i8)) {
            Q(i8, 0, null);
            this.f1288t.setImeVisibility(false);
            G();
            return true;
        }
        return false;
    }

    boolean T(int i8) {
        n nVar = this.f1273e0;
        if (nVar == null || !nVar.a(i8)) {
            c0(i8);
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void U(CharSequence charSequence) {
        setQuery(charSequence);
    }

    void V() {
        k0(false);
        this.f1288t.requestFocus();
        this.f1288t.setImeVisibility(true);
        View.OnClickListener onClickListener = this.f1274f0;
        if (onClickListener != null) {
            onClickListener.onClick(this);
        }
    }

    void W() {
        Editable text = this.f1288t.getText();
        if (text == null || TextUtils.getTrimmedLength(text) <= 0) {
            return;
        }
        m mVar = this.f1270b0;
        if (mVar == null || !mVar.b(text.toString())) {
            if (this.f1289t0 != null) {
                P(0, null, text.toString());
            }
            this.f1288t.setImeVisibility(false);
            G();
        }
    }

    boolean X(View view, int i8, KeyEvent keyEvent) {
        if (this.f1289t0 != null && this.f1277i0 != null && keyEvent.getAction() == 0 && keyEvent.hasNoModifiers()) {
            if (i8 == 66 || i8 == 84 || i8 == 61) {
                return S(this.f1288t.getListSelection(), 0, null);
            }
            if (i8 == 21 || i8 == 22) {
                this.f1288t.setSelection(i8 == 21 ? 0 : this.f1288t.length());
                this.f1288t.setListSelection(0);
                this.f1288t.clearListSelection();
                this.f1288t.b();
                return true;
            } else if (i8 == 19) {
                this.f1288t.getListSelection();
                return false;
            }
        }
        return false;
    }

    void Y(CharSequence charSequence) {
        Editable text = this.f1288t.getText();
        this.f1285q0 = text;
        boolean z4 = !TextUtils.isEmpty(text);
        j0(z4);
        l0(!z4);
        e0();
        i0();
        if (this.f1270b0 != null && !TextUtils.equals(charSequence, this.f1284p0)) {
            this.f1270b0.a(charSequence.toString());
        }
        this.f1284p0 = charSequence.toString();
    }

    void Z() {
        k0(L());
        b0();
        if (this.f1288t.hasFocus()) {
            H();
        }
    }

    void a0() {
        Intent E;
        SearchableInfo searchableInfo = this.f1289t0;
        if (searchableInfo == null) {
            return;
        }
        try {
            if (searchableInfo.getVoiceSearchLaunchWebSearch()) {
                E = F(this.T, searchableInfo);
            } else if (!searchableInfo.getVoiceSearchLaunchRecognizer()) {
                return;
            } else {
                E = E(this.W, searchableInfo);
            }
            getContext().startActivity(E);
        } catch (ActivityNotFoundException unused) {
            Log.w("SearchView", "Could not find voice search activity");
        }
    }

    @Override // androidx.appcompat.view.c
    public void c() {
        if (this.f1286r0) {
            return;
        }
        this.f1286r0 = true;
        int imeOptions = this.f1288t.getImeOptions();
        this.f1287s0 = imeOptions;
        this.f1288t.setImeOptions(imeOptions | 33554432);
        this.f1288t.setText(BuildConfig.FLAVOR);
        setIconified(false);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void clearFocus() {
        this.f1281m0 = true;
        super.clearFocus();
        this.f1288t.clearFocus();
        this.f1288t.setImeVisibility(false);
        this.f1281m0 = false;
    }

    public void d0(CharSequence charSequence, boolean z4) {
        this.f1288t.setText(charSequence);
        if (charSequence != null) {
            SearchAutoComplete searchAutoComplete = this.f1288t;
            searchAutoComplete.setSelection(searchAutoComplete.length());
            this.f1285q0 = charSequence;
        }
        if (!z4 || TextUtils.isEmpty(charSequence)) {
            return;
        }
        W();
    }

    @Override // androidx.appcompat.view.c
    public void f() {
        d0(BuildConfig.FLAVOR, false);
        clearFocus();
        k0(true);
        this.f1288t.setImeOptions(this.f1287s0);
        this.f1286r0 = false;
    }

    void f0() {
        int[] iArr = this.f1288t.hasFocus() ? ViewGroup.FOCUSED_STATE_SET : ViewGroup.EMPTY_STATE_SET;
        Drawable background = this.f1294x.getBackground();
        if (background != null) {
            background.setState(iArr);
        }
        Drawable background2 = this.f1296y.getBackground();
        if (background2 != null) {
            background2.setState(iArr);
        }
        invalidate();
    }

    public int getImeOptions() {
        return this.f1288t.getImeOptions();
    }

    public int getInputType() {
        return this.f1288t.getInputType();
    }

    public int getMaxWidth() {
        return this.f1282n0;
    }

    public CharSequence getQuery() {
        return this.f1288t.getText();
    }

    public CharSequence getQueryHint() {
        CharSequence charSequence = this.f1279k0;
        if (charSequence != null) {
            return charSequence;
        }
        SearchableInfo searchableInfo = this.f1289t0;
        return (searchableInfo == null || searchableInfo.getHintId() == 0) ? this.f1269a0 : getContext().getText(this.f1289t0.getHintId());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getSuggestionCommitIconResId() {
        return this.R;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getSuggestionRowLayout() {
        return this.Q;
    }

    public v0.a getSuggestionsAdapter() {
        return this.f1277i0;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        removeCallbacks(this.f1291v0);
        post(this.f1293w0);
        super.onDetachedFromWindow();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.widget.LinearLayoutCompat, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z4, int i8, int i9, int i10, int i11) {
        super.onLayout(z4, i8, i9, i10, i11);
        if (z4) {
            I(this.f1288t, this.G);
            Rect rect = this.H;
            Rect rect2 = this.G;
            rect.set(rect2.left, 0, rect2.right, i11 - i9);
            p pVar = this.F;
            if (pVar != null) {
                pVar.a(this.H, this.G);
                return;
            }
            p pVar2 = new p(this.H, this.G, this.f1288t);
            this.F = pVar2;
            setTouchDelegate(pVar2);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x001f, code lost:
        if (r0 <= 0) goto L13;
     */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0043  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x004b  */
    @Override // androidx.appcompat.widget.LinearLayoutCompat, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void onMeasure(int r4, int r5) {
        /*
            r3 = this;
            boolean r0 = r3.L()
            if (r0 == 0) goto La
            super.onMeasure(r4, r5)
            return
        La:
            int r0 = android.view.View.MeasureSpec.getMode(r4)
            int r4 = android.view.View.MeasureSpec.getSize(r4)
            r1 = -2147483648(0xffffffff80000000, float:-0.0)
            r2 = 1073741824(0x40000000, float:2.0)
            if (r0 == r1) goto L2c
            if (r0 == 0) goto L22
            if (r0 == r2) goto L1d
            goto L39
        L1d:
            int r0 = r3.f1282n0
            if (r0 <= 0) goto L39
            goto L30
        L22:
            int r4 = r3.f1282n0
            if (r4 <= 0) goto L27
            goto L39
        L27:
            int r4 = r3.getPreferredWidth()
            goto L39
        L2c:
            int r0 = r3.f1282n0
            if (r0 <= 0) goto L31
        L30:
            goto L35
        L31:
            int r0 = r3.getPreferredWidth()
        L35:
            int r4 = java.lang.Math.min(r0, r4)
        L39:
            int r0 = android.view.View.MeasureSpec.getMode(r5)
            int r5 = android.view.View.MeasureSpec.getSize(r5)
            if (r0 == r1) goto L4b
            if (r0 == 0) goto L46
            goto L53
        L46:
            int r5 = r3.getPreferredHeight()
            goto L53
        L4b:
            int r0 = r3.getPreferredHeight()
            int r5 = java.lang.Math.min(r0, r5)
        L53:
            int r4 = android.view.View.MeasureSpec.makeMeasureSpec(r4, r2)
            int r5 = android.view.View.MeasureSpec.makeMeasureSpec(r5, r2)
            super.onMeasure(r4, r5)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.SearchView.onMeasure(int, int):void");
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.a());
        k0(savedState.f1300c);
        requestLayout();
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.f1300c = L();
        return savedState;
    }

    @Override // android.view.View
    public void onWindowFocusChanged(boolean z4) {
        super.onWindowFocusChanged(z4);
        b0();
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean requestFocus(int i8, Rect rect) {
        if (!this.f1281m0 && isFocusable()) {
            if (L()) {
                return super.requestFocus(i8, rect);
            }
            boolean requestFocus = this.f1288t.requestFocus(i8, rect);
            if (requestFocus) {
                k0(false);
            }
            return requestFocus;
        }
        return false;
    }

    public void setAppSearchData(Bundle bundle) {
        this.f1290u0 = bundle;
    }

    public void setIconified(boolean z4) {
        if (z4) {
            R();
        } else {
            V();
        }
    }

    public void setIconifiedByDefault(boolean z4) {
        if (this.f1275g0 == z4) {
            return;
        }
        this.f1275g0 = z4;
        k0(z4);
        g0();
    }

    public void setImeOptions(int i8) {
        this.f1288t.setImeOptions(i8);
    }

    public void setInputType(int i8) {
        this.f1288t.setInputType(i8);
    }

    public void setMaxWidth(int i8) {
        this.f1282n0 = i8;
        requestLayout();
    }

    public void setOnCloseListener(l lVar) {
        this.f1271c0 = lVar;
    }

    public void setOnQueryTextFocusChangeListener(View.OnFocusChangeListener onFocusChangeListener) {
        this.f1272d0 = onFocusChangeListener;
    }

    public void setOnQueryTextListener(m mVar) {
        this.f1270b0 = mVar;
    }

    public void setOnSearchClickListener(View.OnClickListener onClickListener) {
        this.f1274f0 = onClickListener;
    }

    public void setOnSuggestionListener(n nVar) {
        this.f1273e0 = nVar;
    }

    public void setQueryHint(CharSequence charSequence) {
        this.f1279k0 = charSequence;
        g0();
    }

    public void setQueryRefinementEnabled(boolean z4) {
        this.f1280l0 = z4;
        v0.a aVar = this.f1277i0;
        if (aVar instanceof d0) {
            ((d0) aVar).x(z4 ? 2 : 1);
        }
    }

    public void setSearchableInfo(SearchableInfo searchableInfo) {
        this.f1289t0 = searchableInfo;
        if (searchableInfo != null) {
            h0();
            g0();
        }
        boolean K = K();
        this.f1283o0 = K;
        if (K) {
            this.f1288t.setPrivateImeOptions("nm");
        }
        k0(L());
    }

    public void setSubmitButtonEnabled(boolean z4) {
        this.f1278j0 = z4;
        k0(L());
    }

    public void setSuggestionsAdapter(v0.a aVar) {
        this.f1277i0 = aVar;
        this.f1288t.setAdapter(aVar);
    }
}
