package androidx.appcompat.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.ThemedSpinnerAdapter;
import androidx.appcompat.app.c;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class AppCompatSpinner extends Spinner implements androidx.core.view.a0 {
    @SuppressLint({"ResourceType"})

    /* renamed from: j  reason: collision with root package name */
    private static final int[] f1178j = {16843505};

    /* renamed from: a  reason: collision with root package name */
    private final androidx.appcompat.widget.d f1179a;

    /* renamed from: b  reason: collision with root package name */
    private final Context f1180b;

    /* renamed from: c  reason: collision with root package name */
    private w f1181c;

    /* renamed from: d  reason: collision with root package name */
    private SpinnerAdapter f1182d;

    /* renamed from: e  reason: collision with root package name */
    private final boolean f1183e;

    /* renamed from: f  reason: collision with root package name */
    private i f1184f;

    /* renamed from: g  reason: collision with root package name */
    int f1185g;

    /* renamed from: h  reason: collision with root package name */
    final Rect f1186h;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: a  reason: collision with root package name */
        boolean f1187a;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a implements Parcelable.Creator<SavedState> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a */
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: b */
            public SavedState[] newArray(int i8) {
                return new SavedState[i8];
            }
        }

        SavedState(Parcel parcel) {
            super(parcel);
            this.f1187a = parcel.readByte() != 0;
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i8) {
            super.writeToParcel(parcel, i8);
            parcel.writeByte(this.f1187a ? (byte) 1 : (byte) 0);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends w {

        /* renamed from: k  reason: collision with root package name */
        final /* synthetic */ h f1188k;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        a(View view, h hVar) {
            super(view);
            this.f1188k = hVar;
        }

        @Override // androidx.appcompat.widget.w
        public androidx.appcompat.view.menu.p b() {
            return this.f1188k;
        }

        @Override // androidx.appcompat.widget.w
        @SuppressLint({"SyntheticAccessor"})
        public boolean c() {
            if (AppCompatSpinner.this.getInternalPopup().b()) {
                return true;
            }
            AppCompatSpinner.this.b();
            return true;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b implements ViewTreeObserver.OnGlobalLayoutListener {
        b() {
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            if (!AppCompatSpinner.this.getInternalPopup().b()) {
                AppCompatSpinner.this.b();
            }
            ViewTreeObserver viewTreeObserver = AppCompatSpinner.this.getViewTreeObserver();
            if (viewTreeObserver != null) {
                if (Build.VERSION.SDK_INT >= 16) {
                    c.a(viewTreeObserver, this);
                } else {
                    viewTreeObserver.removeGlobalOnLayoutListener(this);
                }
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class c {
        static void a(ViewTreeObserver viewTreeObserver, ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener) {
            viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class d {
        static int a(View view) {
            return view.getTextAlignment();
        }

        static int b(View view) {
            return view.getTextDirection();
        }

        static void c(View view, int i8) {
            view.setTextAlignment(i8);
        }

        static void d(View view, int i8) {
            view.setTextDirection(i8);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class e {
        static void a(ThemedSpinnerAdapter themedSpinnerAdapter, Resources.Theme theme) {
            if (androidx.core.util.c.a(themedSpinnerAdapter.getDropDownViewTheme(), theme)) {
                return;
            }
            themedSpinnerAdapter.setDropDownViewTheme(theme);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class f implements i, DialogInterface.OnClickListener {

        /* renamed from: a  reason: collision with root package name */
        androidx.appcompat.app.c f1191a;

        /* renamed from: b  reason: collision with root package name */
        private ListAdapter f1192b;

        /* renamed from: c  reason: collision with root package name */
        private CharSequence f1193c;

        f() {
        }

        @Override // androidx.appcompat.widget.AppCompatSpinner.i
        public boolean b() {
            androidx.appcompat.app.c cVar = this.f1191a;
            if (cVar != null) {
                return cVar.isShowing();
            }
            return false;
        }

        @Override // androidx.appcompat.widget.AppCompatSpinner.i
        public void c(Drawable drawable) {
            Log.e("AppCompatSpinner", "Cannot set popup background for MODE_DIALOG, ignoring");
        }

        @Override // androidx.appcompat.widget.AppCompatSpinner.i
        public int d() {
            return 0;
        }

        @Override // androidx.appcompat.widget.AppCompatSpinner.i
        public void dismiss() {
            androidx.appcompat.app.c cVar = this.f1191a;
            if (cVar != null) {
                cVar.dismiss();
                this.f1191a = null;
            }
        }

        @Override // androidx.appcompat.widget.AppCompatSpinner.i
        public void f(int i8) {
            Log.e("AppCompatSpinner", "Cannot set horizontal offset for MODE_DIALOG, ignoring");
        }

        @Override // androidx.appcompat.widget.AppCompatSpinner.i
        public CharSequence g() {
            return this.f1193c;
        }

        @Override // androidx.appcompat.widget.AppCompatSpinner.i
        public Drawable i() {
            return null;
        }

        @Override // androidx.appcompat.widget.AppCompatSpinner.i
        public void j(CharSequence charSequence) {
            this.f1193c = charSequence;
        }

        @Override // androidx.appcompat.widget.AppCompatSpinner.i
        public void k(int i8) {
            Log.e("AppCompatSpinner", "Cannot set vertical offset for MODE_DIALOG, ignoring");
        }

        @Override // androidx.appcompat.widget.AppCompatSpinner.i
        public void l(int i8) {
            Log.e("AppCompatSpinner", "Cannot set horizontal (original) offset for MODE_DIALOG, ignoring");
        }

        @Override // androidx.appcompat.widget.AppCompatSpinner.i
        public void n(int i8, int i9) {
            if (this.f1192b == null) {
                return;
            }
            c.a aVar = new c.a(AppCompatSpinner.this.getPopupContext());
            CharSequence charSequence = this.f1193c;
            if (charSequence != null) {
                aVar.m(charSequence);
            }
            androidx.appcompat.app.c a9 = aVar.l(this.f1192b, AppCompatSpinner.this.getSelectedItemPosition(), this).a();
            this.f1191a = a9;
            ListView h8 = a9.h();
            if (Build.VERSION.SDK_INT >= 17) {
                d.d(h8, i8);
                d.c(h8, i9);
            }
            this.f1191a.show();
        }

        @Override // androidx.appcompat.widget.AppCompatSpinner.i
        public int o() {
            return 0;
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i8) {
            AppCompatSpinner.this.setSelection(i8);
            if (AppCompatSpinner.this.getOnItemClickListener() != null) {
                AppCompatSpinner.this.performItemClick(null, i8, this.f1192b.getItemId(i8));
            }
            dismiss();
        }

        @Override // androidx.appcompat.widget.AppCompatSpinner.i
        public void p(ListAdapter listAdapter) {
            this.f1192b = listAdapter;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class g implements ListAdapter, SpinnerAdapter {

        /* renamed from: a  reason: collision with root package name */
        private SpinnerAdapter f1195a;

        /* renamed from: b  reason: collision with root package name */
        private ListAdapter f1196b;

        public g(SpinnerAdapter spinnerAdapter, Resources.Theme theme) {
            this.f1195a = spinnerAdapter;
            if (spinnerAdapter instanceof ListAdapter) {
                this.f1196b = (ListAdapter) spinnerAdapter;
            }
            if (theme != null) {
                if (Build.VERSION.SDK_INT >= 23 && (spinnerAdapter instanceof ThemedSpinnerAdapter)) {
                    e.a((ThemedSpinnerAdapter) spinnerAdapter, theme);
                } else if (spinnerAdapter instanceof f0) {
                    f0 f0Var = (f0) spinnerAdapter;
                    if (f0Var.getDropDownViewTheme() == null) {
                        f0Var.setDropDownViewTheme(theme);
                    }
                }
            }
        }

        @Override // android.widget.ListAdapter
        public boolean areAllItemsEnabled() {
            ListAdapter listAdapter = this.f1196b;
            if (listAdapter != null) {
                return listAdapter.areAllItemsEnabled();
            }
            return true;
        }

        @Override // android.widget.Adapter
        public int getCount() {
            SpinnerAdapter spinnerAdapter = this.f1195a;
            if (spinnerAdapter == null) {
                return 0;
            }
            return spinnerAdapter.getCount();
        }

        @Override // android.widget.SpinnerAdapter
        public View getDropDownView(int i8, View view, ViewGroup viewGroup) {
            SpinnerAdapter spinnerAdapter = this.f1195a;
            if (spinnerAdapter == null) {
                return null;
            }
            return spinnerAdapter.getDropDownView(i8, view, viewGroup);
        }

        @Override // android.widget.Adapter
        public Object getItem(int i8) {
            SpinnerAdapter spinnerAdapter = this.f1195a;
            if (spinnerAdapter == null) {
                return null;
            }
            return spinnerAdapter.getItem(i8);
        }

        @Override // android.widget.Adapter
        public long getItemId(int i8) {
            SpinnerAdapter spinnerAdapter = this.f1195a;
            if (spinnerAdapter == null) {
                return -1L;
            }
            return spinnerAdapter.getItemId(i8);
        }

        @Override // android.widget.Adapter
        public int getItemViewType(int i8) {
            return 0;
        }

        @Override // android.widget.Adapter
        public View getView(int i8, View view, ViewGroup viewGroup) {
            return getDropDownView(i8, view, viewGroup);
        }

        @Override // android.widget.Adapter
        public int getViewTypeCount() {
            return 1;
        }

        @Override // android.widget.Adapter
        public boolean hasStableIds() {
            SpinnerAdapter spinnerAdapter = this.f1195a;
            return spinnerAdapter != null && spinnerAdapter.hasStableIds();
        }

        @Override // android.widget.Adapter
        public boolean isEmpty() {
            return getCount() == 0;
        }

        @Override // android.widget.ListAdapter
        public boolean isEnabled(int i8) {
            ListAdapter listAdapter = this.f1196b;
            if (listAdapter != null) {
                return listAdapter.isEnabled(i8);
            }
            return true;
        }

        @Override // android.widget.Adapter
        public void registerDataSetObserver(DataSetObserver dataSetObserver) {
            SpinnerAdapter spinnerAdapter = this.f1195a;
            if (spinnerAdapter != null) {
                spinnerAdapter.registerDataSetObserver(dataSetObserver);
            }
        }

        @Override // android.widget.Adapter
        public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
            SpinnerAdapter spinnerAdapter = this.f1195a;
            if (spinnerAdapter != null) {
                spinnerAdapter.unregisterDataSetObserver(dataSetObserver);
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class h extends ListPopupWindow implements i {
        private CharSequence X;
        ListAdapter Y;
        private final Rect Z;

        /* renamed from: a0  reason: collision with root package name */
        private int f1197a0;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a implements AdapterView.OnItemClickListener {

            /* renamed from: a  reason: collision with root package name */
            final /* synthetic */ AppCompatSpinner f1199a;

            a(AppCompatSpinner appCompatSpinner) {
                this.f1199a = appCompatSpinner;
            }

            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i8, long j8) {
                AppCompatSpinner.this.setSelection(i8);
                if (AppCompatSpinner.this.getOnItemClickListener() != null) {
                    h hVar = h.this;
                    AppCompatSpinner.this.performItemClick(view, i8, hVar.Y.getItemId(i8));
                }
                h.this.dismiss();
            }
        }

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class b implements ViewTreeObserver.OnGlobalLayoutListener {
            b() {
            }

            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public void onGlobalLayout() {
                h hVar = h.this;
                if (!hVar.U(AppCompatSpinner.this)) {
                    h.this.dismiss();
                    return;
                }
                h.this.S();
                h.super.a();
            }
        }

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class c implements PopupWindow.OnDismissListener {

            /* renamed from: a  reason: collision with root package name */
            final /* synthetic */ ViewTreeObserver.OnGlobalLayoutListener f1202a;

            c(ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener) {
                this.f1202a = onGlobalLayoutListener;
            }

            @Override // android.widget.PopupWindow.OnDismissListener
            public void onDismiss() {
                ViewTreeObserver viewTreeObserver = AppCompatSpinner.this.getViewTreeObserver();
                if (viewTreeObserver != null) {
                    viewTreeObserver.removeGlobalOnLayoutListener(this.f1202a);
                }
            }
        }

        public h(Context context, AttributeSet attributeSet, int i8) {
            super(context, attributeSet, i8);
            this.Z = new Rect();
            D(AppCompatSpinner.this);
            J(true);
            O(0);
            L(new a(AppCompatSpinner.this));
        }

        /* JADX WARN: Removed duplicated region for block: B:23:0x008d  */
        /* JADX WARN: Removed duplicated region for block: B:24:0x009a  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        void S() {
            /*
                r8 = this;
                android.graphics.drawable.Drawable r0 = r8.i()
                r1 = 0
                if (r0 == 0) goto L26
                androidx.appcompat.widget.AppCompatSpinner r1 = androidx.appcompat.widget.AppCompatSpinner.this
                android.graphics.Rect r1 = r1.f1186h
                r0.getPadding(r1)
                androidx.appcompat.widget.AppCompatSpinner r0 = androidx.appcompat.widget.AppCompatSpinner.this
                boolean r0 = androidx.appcompat.widget.u0.b(r0)
                if (r0 == 0) goto L1d
                androidx.appcompat.widget.AppCompatSpinner r0 = androidx.appcompat.widget.AppCompatSpinner.this
                android.graphics.Rect r0 = r0.f1186h
                int r0 = r0.right
                goto L24
            L1d:
                androidx.appcompat.widget.AppCompatSpinner r0 = androidx.appcompat.widget.AppCompatSpinner.this
                android.graphics.Rect r0 = r0.f1186h
                int r0 = r0.left
                int r0 = -r0
            L24:
                r1 = r0
                goto L2e
            L26:
                androidx.appcompat.widget.AppCompatSpinner r0 = androidx.appcompat.widget.AppCompatSpinner.this
                android.graphics.Rect r0 = r0.f1186h
                r0.right = r1
                r0.left = r1
            L2e:
                androidx.appcompat.widget.AppCompatSpinner r0 = androidx.appcompat.widget.AppCompatSpinner.this
                int r0 = r0.getPaddingLeft()
                androidx.appcompat.widget.AppCompatSpinner r2 = androidx.appcompat.widget.AppCompatSpinner.this
                int r2 = r2.getPaddingRight()
                androidx.appcompat.widget.AppCompatSpinner r3 = androidx.appcompat.widget.AppCompatSpinner.this
                int r3 = r3.getWidth()
                androidx.appcompat.widget.AppCompatSpinner r4 = androidx.appcompat.widget.AppCompatSpinner.this
                int r5 = r4.f1185g
                r6 = -2
                if (r5 != r6) goto L78
                android.widget.ListAdapter r5 = r8.Y
                android.widget.SpinnerAdapter r5 = (android.widget.SpinnerAdapter) r5
                android.graphics.drawable.Drawable r6 = r8.i()
                int r4 = r4.a(r5, r6)
                androidx.appcompat.widget.AppCompatSpinner r5 = androidx.appcompat.widget.AppCompatSpinner.this
                android.content.Context r5 = r5.getContext()
                android.content.res.Resources r5 = r5.getResources()
                android.util.DisplayMetrics r5 = r5.getDisplayMetrics()
                int r5 = r5.widthPixels
                androidx.appcompat.widget.AppCompatSpinner r6 = androidx.appcompat.widget.AppCompatSpinner.this
                android.graphics.Rect r6 = r6.f1186h
                int r7 = r6.left
                int r5 = r5 - r7
                int r6 = r6.right
                int r5 = r5 - r6
                if (r4 <= r5) goto L70
                r4 = r5
            L70:
                int r5 = r3 - r0
                int r5 = r5 - r2
                int r4 = java.lang.Math.max(r4, r5)
                goto L7e
            L78:
                r4 = -1
                if (r5 != r4) goto L82
                int r4 = r3 - r0
                int r4 = r4 - r2
            L7e:
                r8.F(r4)
                goto L85
            L82:
                r8.F(r5)
            L85:
                androidx.appcompat.widget.AppCompatSpinner r4 = androidx.appcompat.widget.AppCompatSpinner.this
                boolean r4 = androidx.appcompat.widget.u0.b(r4)
                if (r4 == 0) goto L9a
                int r3 = r3 - r2
                int r0 = r8.z()
                int r3 = r3 - r0
                int r0 = r8.T()
                int r3 = r3 - r0
                int r1 = r1 + r3
                goto La0
            L9a:
                int r2 = r8.T()
                int r0 = r0 + r2
                int r1 = r1 + r0
            La0:
                r8.f(r1)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.AppCompatSpinner.h.S():void");
        }

        public int T() {
            return this.f1197a0;
        }

        boolean U(View view) {
            return androidx.core.view.c0.V(view) && view.getGlobalVisibleRect(this.Z);
        }

        @Override // androidx.appcompat.widget.AppCompatSpinner.i
        public CharSequence g() {
            return this.X;
        }

        @Override // androidx.appcompat.widget.AppCompatSpinner.i
        public void j(CharSequence charSequence) {
            this.X = charSequence;
        }

        @Override // androidx.appcompat.widget.AppCompatSpinner.i
        public void l(int i8) {
            this.f1197a0 = i8;
        }

        @Override // androidx.appcompat.widget.AppCompatSpinner.i
        public void n(int i8, int i9) {
            ViewTreeObserver viewTreeObserver;
            boolean b9 = b();
            S();
            I(2);
            super.a();
            ListView m8 = m();
            m8.setChoiceMode(1);
            if (Build.VERSION.SDK_INT >= 17) {
                d.d(m8, i8);
                d.c(m8, i9);
            }
            P(AppCompatSpinner.this.getSelectedItemPosition());
            if (b9 || (viewTreeObserver = AppCompatSpinner.this.getViewTreeObserver()) == null) {
                return;
            }
            b bVar = new b();
            viewTreeObserver.addOnGlobalLayoutListener(bVar);
            K(new c(bVar));
        }

        @Override // androidx.appcompat.widget.ListPopupWindow, androidx.appcompat.widget.AppCompatSpinner.i
        public void p(ListAdapter listAdapter) {
            super.p(listAdapter);
            this.Y = listAdapter;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface i {
        boolean b();

        void c(Drawable drawable);

        int d();

        void dismiss();

        void f(int i8);

        CharSequence g();

        Drawable i();

        void j(CharSequence charSequence);

        void k(int i8);

        void l(int i8);

        void n(int i8, int i9);

        int o();

        void p(ListAdapter listAdapter);
    }

    public AppCompatSpinner(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, g.a.N);
    }

    public AppCompatSpinner(Context context, AttributeSet attributeSet, int i8) {
        this(context, attributeSet, i8, -1);
    }

    public AppCompatSpinner(Context context, AttributeSet attributeSet, int i8, int i9) {
        this(context, attributeSet, i8, i9, null);
    }

    /* JADX WARN: Code restructure failed: missing block: B:24:0x005e, code lost:
        if (r11 == null) goto L8;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:33:0x006c  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00a6  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00be  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00d7  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x003d A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r11v10 */
    /* JADX WARN: Type inference failed for: r11v11 */
    /* JADX WARN: Type inference failed for: r11v12 */
    /* JADX WARN: Type inference failed for: r11v3 */
    /* JADX WARN: Type inference failed for: r11v4 */
    /* JADX WARN: Type inference failed for: r11v7, types: [android.content.res.TypedArray] */
    /* JADX WARN: Type inference failed for: r6v0, types: [androidx.appcompat.widget.AppCompatSpinner, android.view.View, android.widget.Spinner] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public AppCompatSpinner(android.content.Context r7, android.util.AttributeSet r8, int r9, int r10, android.content.res.Resources.Theme r11) {
        /*
            r6 = this;
            r6.<init>(r7, r8, r9)
            android.graphics.Rect r0 = new android.graphics.Rect
            r0.<init>()
            r6.f1186h = r0
            android.content.Context r0 = r6.getContext()
            androidx.appcompat.widget.e0.a(r6, r0)
            int[] r0 = g.j.H2
            r1 = 0
            androidx.appcompat.widget.j0 r0 = androidx.appcompat.widget.j0.v(r7, r8, r0, r9, r1)
            androidx.appcompat.widget.d r2 = new androidx.appcompat.widget.d
            r2.<init>(r6)
            r6.f1179a = r2
            if (r11 == 0) goto L29
            androidx.appcompat.view.d r2 = new androidx.appcompat.view.d
            r2.<init>(r7, r11)
        L26:
            r6.f1180b = r2
            goto L39
        L29:
            int r11 = g.j.M2
            int r11 = r0.n(r11, r1)
            if (r11 == 0) goto L37
            androidx.appcompat.view.d r2 = new androidx.appcompat.view.d
            r2.<init>(r7, r11)
            goto L26
        L37:
            r6.f1180b = r7
        L39:
            r11 = -1
            r2 = 0
            if (r10 != r11) goto L69
            int[] r11 = androidx.appcompat.widget.AppCompatSpinner.f1178j     // Catch: java.lang.Throwable -> L53 java.lang.Exception -> L55
            android.content.res.TypedArray r11 = r7.obtainStyledAttributes(r8, r11, r9, r1)     // Catch: java.lang.Throwable -> L53 java.lang.Exception -> L55
            boolean r3 = r11.hasValue(r1)     // Catch: java.lang.Exception -> L51 java.lang.Throwable -> L61
            if (r3 == 0) goto L4d
            int r10 = r11.getInt(r1, r1)     // Catch: java.lang.Exception -> L51 java.lang.Throwable -> L61
        L4d:
            r11.recycle()
            goto L69
        L51:
            r3 = move-exception
            goto L57
        L53:
            r7 = move-exception
            goto L63
        L55:
            r3 = move-exception
            r11 = r2
        L57:
            java.lang.String r4 = "AppCompatSpinner"
            java.lang.String r5 = "Could not read android:spinnerMode"
            android.util.Log.i(r4, r5, r3)     // Catch: java.lang.Throwable -> L61
            if (r11 == 0) goto L69
            goto L4d
        L61:
            r7 = move-exception
            r2 = r11
        L63:
            if (r2 == 0) goto L68
            r2.recycle()
        L68:
            throw r7
        L69:
            r11 = 1
            if (r10 == 0) goto La6
            if (r10 == r11) goto L6f
            goto Lb6
        L6f:
            androidx.appcompat.widget.AppCompatSpinner$h r10 = new androidx.appcompat.widget.AppCompatSpinner$h
            android.content.Context r3 = r6.f1180b
            r10.<init>(r3, r8, r9)
            android.content.Context r3 = r6.f1180b
            int[] r4 = g.j.H2
            androidx.appcompat.widget.j0 r1 = androidx.appcompat.widget.j0.v(r3, r8, r4, r9, r1)
            int r3 = g.j.L2
            r4 = -2
            int r3 = r1.m(r3, r4)
            r6.f1185g = r3
            int r3 = g.j.J2
            android.graphics.drawable.Drawable r3 = r1.g(r3)
            r10.c(r3)
            int r3 = g.j.K2
            java.lang.String r3 = r0.o(r3)
            r10.j(r3)
            r1.w()
            r6.f1184f = r10
            androidx.appcompat.widget.AppCompatSpinner$a r1 = new androidx.appcompat.widget.AppCompatSpinner$a
            r1.<init>(r6, r10)
            r6.f1181c = r1
            goto Lb6
        La6:
            androidx.appcompat.widget.AppCompatSpinner$f r10 = new androidx.appcompat.widget.AppCompatSpinner$f
            r10.<init>()
            r6.f1184f = r10
            int r1 = g.j.K2
            java.lang.String r1 = r0.o(r1)
            r10.j(r1)
        Lb6:
            int r10 = g.j.I2
            java.lang.CharSequence[] r10 = r0.q(r10)
            if (r10 == 0) goto Lce
            android.widget.ArrayAdapter r1 = new android.widget.ArrayAdapter
            r3 = 17367048(0x1090008, float:2.5162948E-38)
            r1.<init>(r7, r3, r10)
            int r7 = g.g.f19981v
            r1.setDropDownViewResource(r7)
            r6.setAdapter(r1)
        Lce:
            r0.w()
            r6.f1183e = r11
            android.widget.SpinnerAdapter r7 = r6.f1182d
            if (r7 == 0) goto Ldc
            r6.setAdapter(r7)
            r6.f1182d = r2
        Ldc:
            androidx.appcompat.widget.d r7 = r6.f1179a
            r7.e(r8, r9)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.AppCompatSpinner.<init>(android.content.Context, android.util.AttributeSet, int, int, android.content.res.Resources$Theme):void");
    }

    int a(SpinnerAdapter spinnerAdapter, Drawable drawable) {
        int i8 = 0;
        if (spinnerAdapter == null) {
            return 0;
        }
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 0);
        int makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 0);
        int max = Math.max(0, getSelectedItemPosition());
        int min = Math.min(spinnerAdapter.getCount(), max + 15);
        View view = null;
        int i9 = 0;
        for (int max2 = Math.max(0, max - (15 - (min - max))); max2 < min; max2++) {
            int itemViewType = spinnerAdapter.getItemViewType(max2);
            if (itemViewType != i8) {
                view = null;
                i8 = itemViewType;
            }
            view = spinnerAdapter.getView(max2, view, this);
            if (view.getLayoutParams() == null) {
                view.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
            }
            view.measure(makeMeasureSpec, makeMeasureSpec2);
            i9 = Math.max(i9, view.getMeasuredWidth());
        }
        if (drawable != null) {
            drawable.getPadding(this.f1186h);
            Rect rect = this.f1186h;
            return i9 + rect.left + rect.right;
        }
        return i9;
    }

    void b() {
        if (Build.VERSION.SDK_INT >= 17) {
            this.f1184f.n(d.b(this), d.a(this));
        } else {
            this.f1184f.n(-1, -1);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        androidx.appcompat.widget.d dVar = this.f1179a;
        if (dVar != null) {
            dVar.b();
        }
    }

    @Override // android.widget.Spinner
    public int getDropDownHorizontalOffset() {
        i iVar = this.f1184f;
        if (iVar != null) {
            return iVar.d();
        }
        if (Build.VERSION.SDK_INT >= 16) {
            return super.getDropDownHorizontalOffset();
        }
        return 0;
    }

    @Override // android.widget.Spinner
    public int getDropDownVerticalOffset() {
        i iVar = this.f1184f;
        if (iVar != null) {
            return iVar.o();
        }
        if (Build.VERSION.SDK_INT >= 16) {
            return super.getDropDownVerticalOffset();
        }
        return 0;
    }

    @Override // android.widget.Spinner
    public int getDropDownWidth() {
        if (this.f1184f != null) {
            return this.f1185g;
        }
        if (Build.VERSION.SDK_INT >= 16) {
            return super.getDropDownWidth();
        }
        return 0;
    }

    final i getInternalPopup() {
        return this.f1184f;
    }

    @Override // android.widget.Spinner
    public Drawable getPopupBackground() {
        i iVar = this.f1184f;
        if (iVar != null) {
            return iVar.i();
        }
        if (Build.VERSION.SDK_INT >= 16) {
            return super.getPopupBackground();
        }
        return null;
    }

    @Override // android.widget.Spinner
    public Context getPopupContext() {
        return this.f1180b;
    }

    @Override // android.widget.Spinner
    public CharSequence getPrompt() {
        i iVar = this.f1184f;
        return iVar != null ? iVar.g() : super.getPrompt();
    }

    @Override // androidx.core.view.a0
    public ColorStateList getSupportBackgroundTintList() {
        androidx.appcompat.widget.d dVar = this.f1179a;
        if (dVar != null) {
            return dVar.c();
        }
        return null;
    }

    @Override // androidx.core.view.a0
    public PorterDuff.Mode getSupportBackgroundTintMode() {
        androidx.appcompat.widget.d dVar = this.f1179a;
        if (dVar != null) {
            return dVar.d();
        }
        return null;
    }

    @Override // android.widget.Spinner, android.widget.AdapterView, android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        i iVar = this.f1184f;
        if (iVar == null || !iVar.b()) {
            return;
        }
        this.f1184f.dismiss();
    }

    @Override // android.widget.Spinner, android.widget.AbsSpinner, android.view.View
    protected void onMeasure(int i8, int i9) {
        super.onMeasure(i8, i9);
        if (this.f1184f == null || View.MeasureSpec.getMode(i8) != Integer.MIN_VALUE) {
            return;
        }
        setMeasuredDimension(Math.min(Math.max(getMeasuredWidth(), a(getAdapter(), getBackground())), View.MeasureSpec.getSize(i8)), getMeasuredHeight());
    }

    @Override // android.widget.Spinner, android.widget.AbsSpinner, android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        ViewTreeObserver viewTreeObserver;
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        if (!savedState.f1187a || (viewTreeObserver = getViewTreeObserver()) == null) {
            return;
        }
        viewTreeObserver.addOnGlobalLayoutListener(new b());
    }

    @Override // android.widget.Spinner, android.widget.AbsSpinner, android.view.View
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        i iVar = this.f1184f;
        savedState.f1187a = iVar != null && iVar.b();
        return savedState;
    }

    @Override // android.widget.Spinner, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        w wVar = this.f1181c;
        if (wVar == null || !wVar.onTouch(this, motionEvent)) {
            return super.onTouchEvent(motionEvent);
        }
        return true;
    }

    @Override // android.widget.Spinner, android.view.View
    public boolean performClick() {
        i iVar = this.f1184f;
        if (iVar != null) {
            if (iVar.b()) {
                return true;
            }
            b();
            return true;
        }
        return super.performClick();
    }

    @Override // android.widget.AdapterView
    public void setAdapter(SpinnerAdapter spinnerAdapter) {
        if (!this.f1183e) {
            this.f1182d = spinnerAdapter;
            return;
        }
        super.setAdapter(spinnerAdapter);
        if (this.f1184f != null) {
            Context context = this.f1180b;
            if (context == null) {
                context = getContext();
            }
            this.f1184f.p(new g(spinnerAdapter, context.getTheme()));
        }
    }

    @Override // android.view.View
    public void setBackgroundDrawable(Drawable drawable) {
        super.setBackgroundDrawable(drawable);
        androidx.appcompat.widget.d dVar = this.f1179a;
        if (dVar != null) {
            dVar.f(drawable);
        }
    }

    @Override // android.view.View
    public void setBackgroundResource(int i8) {
        super.setBackgroundResource(i8);
        androidx.appcompat.widget.d dVar = this.f1179a;
        if (dVar != null) {
            dVar.g(i8);
        }
    }

    @Override // android.widget.Spinner
    public void setDropDownHorizontalOffset(int i8) {
        i iVar = this.f1184f;
        if (iVar != null) {
            iVar.l(i8);
            this.f1184f.f(i8);
        } else if (Build.VERSION.SDK_INT >= 16) {
            super.setDropDownHorizontalOffset(i8);
        }
    }

    @Override // android.widget.Spinner
    public void setDropDownVerticalOffset(int i8) {
        i iVar = this.f1184f;
        if (iVar != null) {
            iVar.k(i8);
        } else if (Build.VERSION.SDK_INT >= 16) {
            super.setDropDownVerticalOffset(i8);
        }
    }

    @Override // android.widget.Spinner
    public void setDropDownWidth(int i8) {
        if (this.f1184f != null) {
            this.f1185g = i8;
        } else if (Build.VERSION.SDK_INT >= 16) {
            super.setDropDownWidth(i8);
        }
    }

    @Override // android.widget.Spinner
    public void setPopupBackgroundDrawable(Drawable drawable) {
        i iVar = this.f1184f;
        if (iVar != null) {
            iVar.c(drawable);
        } else if (Build.VERSION.SDK_INT >= 16) {
            super.setPopupBackgroundDrawable(drawable);
        }
    }

    @Override // android.widget.Spinner
    public void setPopupBackgroundResource(int i8) {
        setPopupBackgroundDrawable(h.a.b(getPopupContext(), i8));
    }

    @Override // android.widget.Spinner
    public void setPrompt(CharSequence charSequence) {
        i iVar = this.f1184f;
        if (iVar != null) {
            iVar.j(charSequence);
        } else {
            super.setPrompt(charSequence);
        }
    }

    @Override // androidx.core.view.a0
    public void setSupportBackgroundTintList(ColorStateList colorStateList) {
        androidx.appcompat.widget.d dVar = this.f1179a;
        if (dVar != null) {
            dVar.i(colorStateList);
        }
    }

    @Override // androidx.core.view.a0
    public void setSupportBackgroundTintMode(PorterDuff.Mode mode) {
        androidx.appcompat.widget.d dVar = this.f1179a;
        if (dVar != null) {
            dVar.j(mode);
        }
    }
}
