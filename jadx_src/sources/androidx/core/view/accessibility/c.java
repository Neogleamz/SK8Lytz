package androidx.core.view.accessibility;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.core.view.accessibility.f;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class c {

    /* renamed from: d  reason: collision with root package name */
    private static int f4903d;

    /* renamed from: a  reason: collision with root package name */
    private final AccessibilityNodeInfo f4904a;

    /* renamed from: b  reason: collision with root package name */
    public int f4905b = -1;

    /* renamed from: c  reason: collision with root package name */
    private int f4906c = -1;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {
        public static final a A;
        public static final a B;
        public static final a C;
        public static final a D;
        public static final a E;
        public static final a F;
        public static final a G;
        public static final a H;
        public static final a I;
        public static final a J;
        public static final a K;
        public static final a L;
        public static final a M;
        public static final a N;
        public static final a O;
        public static final a P;
        public static final a Q;

        /* renamed from: e  reason: collision with root package name */
        public static final a f4907e = new a(1, null);

        /* renamed from: f  reason: collision with root package name */
        public static final a f4908f = new a(2, null);

        /* renamed from: g  reason: collision with root package name */
        public static final a f4909g = new a(4, null);

        /* renamed from: h  reason: collision with root package name */
        public static final a f4910h = new a(8, null);

        /* renamed from: i  reason: collision with root package name */
        public static final a f4911i = new a(16, null);

        /* renamed from: j  reason: collision with root package name */
        public static final a f4912j = new a(32, null);

        /* renamed from: k  reason: collision with root package name */
        public static final a f4913k = new a(64, null);

        /* renamed from: l  reason: collision with root package name */
        public static final a f4914l = new a(RecognitionOptions.ITF, null);

        /* renamed from: m  reason: collision with root package name */
        public static final a f4915m = new a((int) RecognitionOptions.QR_CODE, (CharSequence) null, f.b.class);

        /* renamed from: n  reason: collision with root package name */
        public static final a f4916n = new a((int) RecognitionOptions.UPC_A, (CharSequence) null, f.b.class);

        /* renamed from: o  reason: collision with root package name */
        public static final a f4917o = new a((int) RecognitionOptions.UPC_E, (CharSequence) null, f.c.class);

        /* renamed from: p  reason: collision with root package name */
        public static final a f4918p = new a((int) RecognitionOptions.PDF417, (CharSequence) null, f.c.class);
        public static final a q = new a(RecognitionOptions.AZTEC, null);

        /* renamed from: r  reason: collision with root package name */
        public static final a f4919r = new a(8192, null);

        /* renamed from: s  reason: collision with root package name */
        public static final a f4920s = new a(16384, null);

        /* renamed from: t  reason: collision with root package name */
        public static final a f4921t = new a(RecognitionOptions.TEZ_CODE, null);

        /* renamed from: u  reason: collision with root package name */
        public static final a f4922u = new a(65536, null);

        /* renamed from: v  reason: collision with root package name */
        public static final a f4923v = new a(131072, (CharSequence) null, f.g.class);

        /* renamed from: w  reason: collision with root package name */
        public static final a f4924w = new a(262144, null);

        /* renamed from: x  reason: collision with root package name */
        public static final a f4925x = new a(524288, null);

        /* renamed from: y  reason: collision with root package name */
        public static final a f4926y = new a(1048576, null);

        /* renamed from: z  reason: collision with root package name */
        public static final a f4927z = new a(2097152, (CharSequence) null, f.h.class);

        /* renamed from: a  reason: collision with root package name */
        final Object f4928a;

        /* renamed from: b  reason: collision with root package name */
        private final int f4929b;

        /* renamed from: c  reason: collision with root package name */
        private final Class<? extends f.a> f4930c;

        /* renamed from: d  reason: collision with root package name */
        protected final f f4931d;

        static {
            int i8 = Build.VERSION.SDK_INT;
            A = new a(i8 >= 23 ? AccessibilityNodeInfo.AccessibilityAction.ACTION_SHOW_ON_SCREEN : null, 16908342, null, null, null);
            B = new a(i8 >= 23 ? AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_TO_POSITION : null, 16908343, null, null, f.e.class);
            C = new a(i8 >= 23 ? AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_UP : null, 16908344, null, null, null);
            D = new a(i8 >= 23 ? AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_LEFT : null, 16908345, null, null, null);
            E = new a(i8 >= 23 ? AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_DOWN : null, 16908346, null, null, null);
            F = new a(i8 >= 23 ? AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_RIGHT : null, 16908347, null, null, null);
            G = new a(i8 >= 29 ? AccessibilityNodeInfo.AccessibilityAction.ACTION_PAGE_UP : null, 16908358, null, null, null);
            H = new a(i8 >= 29 ? AccessibilityNodeInfo.AccessibilityAction.ACTION_PAGE_DOWN : null, 16908359, null, null, null);
            I = new a(i8 >= 29 ? AccessibilityNodeInfo.AccessibilityAction.ACTION_PAGE_LEFT : null, 16908360, null, null, null);
            J = new a(i8 >= 29 ? AccessibilityNodeInfo.AccessibilityAction.ACTION_PAGE_RIGHT : null, 16908361, null, null, null);
            K = new a(i8 >= 23 ? AccessibilityNodeInfo.AccessibilityAction.ACTION_CONTEXT_CLICK : null, 16908348, null, null, null);
            L = new a(i8 >= 24 ? AccessibilityNodeInfo.AccessibilityAction.ACTION_SET_PROGRESS : null, 16908349, null, null, f.C0044f.class);
            M = new a(i8 >= 26 ? AccessibilityNodeInfo.AccessibilityAction.ACTION_MOVE_WINDOW : null, 16908354, null, null, f.d.class);
            N = new a(i8 >= 28 ? AccessibilityNodeInfo.AccessibilityAction.ACTION_SHOW_TOOLTIP : null, 16908356, null, null, null);
            O = new a(i8 >= 28 ? AccessibilityNodeInfo.AccessibilityAction.ACTION_HIDE_TOOLTIP : null, 16908357, null, null, null);
            P = new a(i8 >= 30 ? AccessibilityNodeInfo.AccessibilityAction.ACTION_PRESS_AND_HOLD : null, 16908362, null, null, null);
            Q = new a(i8 >= 30 ? AccessibilityNodeInfo.AccessibilityAction.ACTION_IME_ENTER : null, 16908372, null, null, null);
        }

        public a(int i8, CharSequence charSequence) {
            this(null, i8, charSequence, null, null);
        }

        public a(int i8, CharSequence charSequence, f fVar) {
            this(null, i8, charSequence, fVar, null);
        }

        private a(int i8, CharSequence charSequence, Class<? extends f.a> cls) {
            this(null, i8, charSequence, null, cls);
        }

        a(Object obj) {
            this(obj, 0, null, null, null);
        }

        a(Object obj, int i8, CharSequence charSequence, f fVar, Class<? extends f.a> cls) {
            this.f4929b = i8;
            this.f4931d = fVar;
            if (Build.VERSION.SDK_INT >= 21 && obj == null) {
                obj = new AccessibilityNodeInfo.AccessibilityAction(i8, charSequence);
            }
            this.f4928a = obj;
            this.f4930c = cls;
        }

        public a a(CharSequence charSequence, f fVar) {
            return new a(null, this.f4929b, charSequence, fVar, this.f4930c);
        }

        public int b() {
            if (Build.VERSION.SDK_INT >= 21) {
                return ((AccessibilityNodeInfo.AccessibilityAction) this.f4928a).getId();
            }
            return 0;
        }

        public CharSequence c() {
            if (Build.VERSION.SDK_INT >= 21) {
                return ((AccessibilityNodeInfo.AccessibilityAction) this.f4928a).getLabel();
            }
            return null;
        }

        public boolean d(View view, Bundle bundle) {
            f.a newInstance;
            if (this.f4931d != null) {
                f.a aVar = null;
                Class<? extends f.a> cls = this.f4930c;
                if (cls != null) {
                    try {
                        newInstance = cls.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
                    } catch (Exception e8) {
                        e = e8;
                    }
                    try {
                        newInstance.a(bundle);
                        aVar = newInstance;
                    } catch (Exception e9) {
                        e = e9;
                        aVar = newInstance;
                        Class<? extends f.a> cls2 = this.f4930c;
                        String name = cls2 == null ? "null" : cls2.getName();
                        Log.e("A11yActionCompat", "Failed to execute command with argument class ViewCommandArgument: " + name, e);
                        return this.f4931d.a(view, aVar);
                    }
                }
                return this.f4931d.a(view, aVar);
            }
            return false;
        }

        public boolean equals(Object obj) {
            if (obj != null && (obj instanceof a)) {
                Object obj2 = this.f4928a;
                Object obj3 = ((a) obj).f4928a;
                return obj2 == null ? obj3 == null : obj2.equals(obj3);
            }
            return false;
        }

        public int hashCode() {
            Object obj = this.f4928a;
            if (obj != null) {
                return obj.hashCode();
            }
            return 0;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b {

        /* renamed from: a  reason: collision with root package name */
        final Object f4932a;

        b(Object obj) {
            this.f4932a = obj;
        }

        public static b a(int i8, int i9, boolean z4) {
            return Build.VERSION.SDK_INT >= 19 ? new b(AccessibilityNodeInfo.CollectionInfo.obtain(i8, i9, z4)) : new b(null);
        }

        public static b b(int i8, int i9, boolean z4, int i10) {
            int i11 = Build.VERSION.SDK_INT;
            return i11 >= 21 ? new b(AccessibilityNodeInfo.CollectionInfo.obtain(i8, i9, z4, i10)) : i11 >= 19 ? new b(AccessibilityNodeInfo.CollectionInfo.obtain(i8, i9, z4)) : new b(null);
        }
    }

    /* renamed from: androidx.core.view.accessibility.c$c  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class C0043c {

        /* renamed from: a  reason: collision with root package name */
        final Object f4933a;

        C0043c(Object obj) {
            this.f4933a = obj;
        }

        public static C0043c a(int i8, int i9, int i10, int i11, boolean z4, boolean z8) {
            int i12 = Build.VERSION.SDK_INT;
            return i12 >= 21 ? new C0043c(AccessibilityNodeInfo.CollectionItemInfo.obtain(i8, i9, i10, i11, z4, z8)) : i12 >= 19 ? new C0043c(AccessibilityNodeInfo.CollectionItemInfo.obtain(i8, i9, i10, i11, z4)) : new C0043c(null);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class d {

        /* renamed from: a  reason: collision with root package name */
        final Object f4934a;

        d(Object obj) {
            this.f4934a = obj;
        }

        public static d a(int i8, float f5, float f8, float f9) {
            return Build.VERSION.SDK_INT >= 19 ? new d(AccessibilityNodeInfo.RangeInfo.obtain(i8, f5, f8, f9)) : new d(null);
        }
    }

    private c(AccessibilityNodeInfo accessibilityNodeInfo) {
        this.f4904a = accessibilityNodeInfo;
    }

    private int A(ClickableSpan clickableSpan, SparseArray<WeakReference<ClickableSpan>> sparseArray) {
        if (sparseArray != null) {
            for (int i8 = 0; i8 < sparseArray.size(); i8++) {
                if (clickableSpan.equals(sparseArray.valueAt(i8).get())) {
                    return sparseArray.keyAt(i8);
                }
            }
        }
        int i9 = f4903d;
        f4903d = i9 + 1;
        return i9;
    }

    public static c I0(AccessibilityNodeInfo accessibilityNodeInfo) {
        return new c(accessibilityNodeInfo);
    }

    public static c O() {
        return I0(AccessibilityNodeInfo.obtain());
    }

    public static c P(View view) {
        return I0(AccessibilityNodeInfo.obtain(view));
    }

    public static c Q(c cVar) {
        return I0(AccessibilityNodeInfo.obtain(cVar.f4904a));
    }

    private void U(View view) {
        SparseArray<WeakReference<ClickableSpan>> w8 = w(view);
        if (w8 != null) {
            ArrayList arrayList = new ArrayList();
            for (int i8 = 0; i8 < w8.size(); i8++) {
                if (w8.valueAt(i8).get() == null) {
                    arrayList.add(Integer.valueOf(i8));
                }
            }
            for (int i9 = 0; i9 < arrayList.size(); i9++) {
                w8.remove(((Integer) arrayList.get(i9)).intValue());
            }
        }
    }

    private void W(int i8, boolean z4) {
        Bundle s8 = s();
        if (s8 != null) {
            int i9 = s8.getInt("androidx.view.accessibility.AccessibilityNodeInfoCompat.BOOLEAN_PROPERTY_KEY", 0) & (~i8);
            if (!z4) {
                i8 = 0;
            }
            s8.putInt("androidx.view.accessibility.AccessibilityNodeInfoCompat.BOOLEAN_PROPERTY_KEY", i8 | i9);
        }
    }

    private void e(ClickableSpan clickableSpan, Spanned spanned, int i8) {
        h("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_START_KEY").add(Integer.valueOf(spanned.getSpanStart(clickableSpan)));
        h("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_END_KEY").add(Integer.valueOf(spanned.getSpanEnd(clickableSpan)));
        h("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_FLAGS_KEY").add(Integer.valueOf(spanned.getSpanFlags(clickableSpan)));
        h("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_ID_KEY").add(Integer.valueOf(i8));
    }

    private void g() {
        if (Build.VERSION.SDK_INT >= 19) {
            this.f4904a.getExtras().remove("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_START_KEY");
            this.f4904a.getExtras().remove("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_END_KEY");
            this.f4904a.getExtras().remove("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_FLAGS_KEY");
            this.f4904a.getExtras().remove("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_ID_KEY");
        }
    }

    private List<Integer> h(String str) {
        if (Build.VERSION.SDK_INT < 19) {
            return new ArrayList();
        }
        ArrayList<Integer> integerArrayList = this.f4904a.getExtras().getIntegerArrayList(str);
        if (integerArrayList == null) {
            ArrayList<Integer> arrayList = new ArrayList<>();
            this.f4904a.getExtras().putIntegerArrayList(str, arrayList);
            return arrayList;
        }
        return integerArrayList;
    }

    private static String j(int i8) {
        if (i8 != 1) {
            if (i8 != 2) {
                switch (i8) {
                    case 4:
                        return "ACTION_SELECT";
                    case 8:
                        return "ACTION_CLEAR_SELECTION";
                    case 16:
                        return "ACTION_CLICK";
                    case 32:
                        return "ACTION_LONG_CLICK";
                    case 64:
                        return "ACTION_ACCESSIBILITY_FOCUS";
                    case RecognitionOptions.ITF /* 128 */:
                        return "ACTION_CLEAR_ACCESSIBILITY_FOCUS";
                    case RecognitionOptions.QR_CODE /* 256 */:
                        return "ACTION_NEXT_AT_MOVEMENT_GRANULARITY";
                    case RecognitionOptions.UPC_A /* 512 */:
                        return "ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY";
                    case RecognitionOptions.UPC_E /* 1024 */:
                        return "ACTION_NEXT_HTML_ELEMENT";
                    case RecognitionOptions.PDF417 /* 2048 */:
                        return "ACTION_PREVIOUS_HTML_ELEMENT";
                    case RecognitionOptions.AZTEC /* 4096 */:
                        return "ACTION_SCROLL_FORWARD";
                    case 8192:
                        return "ACTION_SCROLL_BACKWARD";
                    case 16384:
                        return "ACTION_COPY";
                    case RecognitionOptions.TEZ_CODE /* 32768 */:
                        return "ACTION_PASTE";
                    case 65536:
                        return "ACTION_CUT";
                    case 131072:
                        return "ACTION_SET_SELECTION";
                    case 262144:
                        return "ACTION_EXPAND";
                    case 524288:
                        return "ACTION_COLLAPSE";
                    case 2097152:
                        return "ACTION_SET_TEXT";
                    case 16908354:
                        return "ACTION_MOVE_WINDOW";
                    case 16908372:
                        return "ACTION_IME_ENTER";
                    default:
                        switch (i8) {
                            case 16908342:
                                return "ACTION_SHOW_ON_SCREEN";
                            case 16908343:
                                return "ACTION_SCROLL_TO_POSITION";
                            case 16908344:
                                return "ACTION_SCROLL_UP";
                            case 16908345:
                                return "ACTION_SCROLL_LEFT";
                            case 16908346:
                                return "ACTION_SCROLL_DOWN";
                            case 16908347:
                                return "ACTION_SCROLL_RIGHT";
                            case 16908348:
                                return "ACTION_CONTEXT_CLICK";
                            case 16908349:
                                return "ACTION_SET_PROGRESS";
                            default:
                                switch (i8) {
                                    case 16908356:
                                        return "ACTION_SHOW_TOOLTIP";
                                    case 16908357:
                                        return "ACTION_HIDE_TOOLTIP";
                                    case 16908358:
                                        return "ACTION_PAGE_UP";
                                    case 16908359:
                                        return "ACTION_PAGE_DOWN";
                                    case 16908360:
                                        return "ACTION_PAGE_LEFT";
                                    case 16908361:
                                        return "ACTION_PAGE_RIGHT";
                                    case 16908362:
                                        return "ACTION_PRESS_AND_HOLD";
                                    default:
                                        return "ACTION_UNKNOWN";
                                }
                        }
                }
            }
            return "ACTION_CLEAR_FOCUS";
        }
        return "ACTION_FOCUS";
    }

    private boolean l(int i8) {
        Bundle s8 = s();
        return s8 != null && (s8.getInt("androidx.view.accessibility.AccessibilityNodeInfoCompat.BOOLEAN_PROPERTY_KEY", 0) & i8) == i8;
    }

    public static ClickableSpan[] q(CharSequence charSequence) {
        if (charSequence instanceof Spanned) {
            return (ClickableSpan[]) ((Spanned) charSequence).getSpans(0, charSequence.length(), ClickableSpan.class);
        }
        return null;
    }

    private SparseArray<WeakReference<ClickableSpan>> u(View view) {
        SparseArray<WeakReference<ClickableSpan>> w8 = w(view);
        if (w8 == null) {
            SparseArray<WeakReference<ClickableSpan>> sparseArray = new SparseArray<>();
            view.setTag(q0.e.X, sparseArray);
            return sparseArray;
        }
        return w8;
    }

    private SparseArray<WeakReference<ClickableSpan>> w(View view) {
        return (SparseArray) view.getTag(q0.e.X);
    }

    private boolean z() {
        return !h("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_START_KEY").isEmpty();
    }

    public void A0(boolean z4) {
        if (Build.VERSION.SDK_INT >= 26) {
            this.f4904a.setShowingHintText(z4);
        } else {
            W(4, z4);
        }
    }

    public boolean B() {
        if (Build.VERSION.SDK_INT >= 16) {
            return this.f4904a.isAccessibilityFocused();
        }
        return false;
    }

    public void B0(View view) {
        this.f4906c = -1;
        this.f4904a.setSource(view);
    }

    public boolean C() {
        return this.f4904a.isCheckable();
    }

    public void C0(View view, int i8) {
        this.f4906c = i8;
        if (Build.VERSION.SDK_INT >= 16) {
            this.f4904a.setSource(view, i8);
        }
    }

    public boolean D() {
        return this.f4904a.isChecked();
    }

    public void D0(CharSequence charSequence) {
        if (androidx.core.os.a.b()) {
            this.f4904a.setStateDescription(charSequence);
        } else if (Build.VERSION.SDK_INT >= 19) {
            this.f4904a.getExtras().putCharSequence("androidx.view.accessibility.AccessibilityNodeInfoCompat.STATE_DESCRIPTION_KEY", charSequence);
        }
    }

    public boolean E() {
        return this.f4904a.isClickable();
    }

    public void E0(CharSequence charSequence) {
        this.f4904a.setText(charSequence);
    }

    public boolean F() {
        return this.f4904a.isEnabled();
    }

    public void F0(View view) {
        if (Build.VERSION.SDK_INT >= 22) {
            this.f4904a.setTraversalAfter(view);
        }
    }

    public boolean G() {
        return this.f4904a.isFocusable();
    }

    public void G0(boolean z4) {
        if (Build.VERSION.SDK_INT >= 16) {
            this.f4904a.setVisibleToUser(z4);
        }
    }

    public boolean H() {
        return this.f4904a.isFocused();
    }

    public AccessibilityNodeInfo H0() {
        return this.f4904a;
    }

    public boolean I() {
        return this.f4904a.isLongClickable();
    }

    public boolean J() {
        return this.f4904a.isPassword();
    }

    public boolean K() {
        return this.f4904a.isScrollable();
    }

    public boolean L() {
        return this.f4904a.isSelected();
    }

    public boolean M() {
        return Build.VERSION.SDK_INT >= 26 ? this.f4904a.isShowingHintText() : l(4);
    }

    public boolean N() {
        if (Build.VERSION.SDK_INT >= 16) {
            return this.f4904a.isVisibleToUser();
        }
        return false;
    }

    public boolean R(int i8, Bundle bundle) {
        if (Build.VERSION.SDK_INT >= 16) {
            return this.f4904a.performAction(i8, bundle);
        }
        return false;
    }

    public void S() {
        this.f4904a.recycle();
    }

    public boolean T(a aVar) {
        if (Build.VERSION.SDK_INT >= 21) {
            return this.f4904a.removeAction((AccessibilityNodeInfo.AccessibilityAction) aVar.f4928a);
        }
        return false;
    }

    public void V(boolean z4) {
        if (Build.VERSION.SDK_INT >= 16) {
            this.f4904a.setAccessibilityFocused(z4);
        }
    }

    @Deprecated
    public void X(Rect rect) {
        this.f4904a.setBoundsInParent(rect);
    }

    public void Y(Rect rect) {
        this.f4904a.setBoundsInScreen(rect);
    }

    public void Z(boolean z4) {
        if (Build.VERSION.SDK_INT >= 19) {
            this.f4904a.setCanOpenPopup(z4);
        }
    }

    public void a(int i8) {
        this.f4904a.addAction(i8);
    }

    public void a0(boolean z4) {
        this.f4904a.setCheckable(z4);
    }

    public void b(a aVar) {
        if (Build.VERSION.SDK_INT >= 21) {
            this.f4904a.addAction((AccessibilityNodeInfo.AccessibilityAction) aVar.f4928a);
        }
    }

    public void b0(boolean z4) {
        this.f4904a.setChecked(z4);
    }

    public void c(View view) {
        this.f4904a.addChild(view);
    }

    public void c0(CharSequence charSequence) {
        this.f4904a.setClassName(charSequence);
    }

    public void d(View view, int i8) {
        if (Build.VERSION.SDK_INT >= 16) {
            this.f4904a.addChild(view, i8);
        }
    }

    public void d0(boolean z4) {
        this.f4904a.setClickable(z4);
    }

    public void e0(Object obj) {
        if (Build.VERSION.SDK_INT >= 19) {
            this.f4904a.setCollectionInfo(obj == null ? null : (AccessibilityNodeInfo.CollectionInfo) ((b) obj).f4932a);
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && (obj instanceof c)) {
            c cVar = (c) obj;
            AccessibilityNodeInfo accessibilityNodeInfo = this.f4904a;
            if (accessibilityNodeInfo == null) {
                if (cVar.f4904a != null) {
                    return false;
                }
            } else if (!accessibilityNodeInfo.equals(cVar.f4904a)) {
                return false;
            }
            return this.f4906c == cVar.f4906c && this.f4905b == cVar.f4905b;
        }
        return false;
    }

    public void f(CharSequence charSequence, View view) {
        int i8 = Build.VERSION.SDK_INT;
        if (i8 < 19 || i8 >= 26) {
            return;
        }
        g();
        U(view);
        ClickableSpan[] q = q(charSequence);
        if (q == null || q.length <= 0) {
            return;
        }
        s().putInt("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_ACTION_ID_KEY", q0.e.f22456a);
        SparseArray<WeakReference<ClickableSpan>> u8 = u(view);
        for (int i9 = 0; i9 < q.length; i9++) {
            int A = A(q[i9], u8);
            u8.put(A, new WeakReference<>(q[i9]));
            e(q[i9], (Spanned) charSequence, A);
        }
    }

    public void f0(Object obj) {
        if (Build.VERSION.SDK_INT >= 19) {
            this.f4904a.setCollectionItemInfo(obj == null ? null : (AccessibilityNodeInfo.CollectionItemInfo) ((C0043c) obj).f4933a);
        }
    }

    public void g0(CharSequence charSequence) {
        this.f4904a.setContentDescription(charSequence);
    }

    public void h0(boolean z4) {
        if (Build.VERSION.SDK_INT >= 19) {
            this.f4904a.setDismissable(z4);
        }
    }

    public int hashCode() {
        AccessibilityNodeInfo accessibilityNodeInfo = this.f4904a;
        if (accessibilityNodeInfo == null) {
            return 0;
        }
        return accessibilityNodeInfo.hashCode();
    }

    public List<a> i() {
        List<AccessibilityNodeInfo.AccessibilityAction> actionList = Build.VERSION.SDK_INT >= 21 ? this.f4904a.getActionList() : null;
        if (actionList != null) {
            ArrayList arrayList = new ArrayList();
            int size = actionList.size();
            for (int i8 = 0; i8 < size; i8++) {
                arrayList.add(new a(actionList.get(i8)));
            }
            return arrayList;
        }
        return Collections.emptyList();
    }

    public void i0(boolean z4) {
        this.f4904a.setEnabled(z4);
    }

    public void j0(CharSequence charSequence) {
        if (Build.VERSION.SDK_INT >= 21) {
            this.f4904a.setError(charSequence);
        }
    }

    public int k() {
        return this.f4904a.getActions();
    }

    public void k0(boolean z4) {
        this.f4904a.setFocusable(z4);
    }

    public void l0(boolean z4) {
        this.f4904a.setFocused(z4);
    }

    @Deprecated
    public void m(Rect rect) {
        this.f4904a.getBoundsInParent(rect);
    }

    public void m0(boolean z4) {
        if (Build.VERSION.SDK_INT >= 28) {
            this.f4904a.setHeading(z4);
        } else {
            W(2, z4);
        }
    }

    public void n(Rect rect) {
        this.f4904a.getBoundsInScreen(rect);
    }

    public void n0(CharSequence charSequence) {
        int i8 = Build.VERSION.SDK_INT;
        if (i8 >= 26) {
            this.f4904a.setHintText(charSequence);
        } else if (i8 >= 19) {
            this.f4904a.getExtras().putCharSequence("androidx.view.accessibility.AccessibilityNodeInfoCompat.HINT_TEXT_KEY", charSequence);
        }
    }

    public int o() {
        return this.f4904a.getChildCount();
    }

    public void o0(boolean z4) {
        this.f4904a.setLongClickable(z4);
    }

    public CharSequence p() {
        return this.f4904a.getClassName();
    }

    public void p0(int i8) {
        if (Build.VERSION.SDK_INT >= 21) {
            this.f4904a.setMaxTextLength(i8);
        }
    }

    public void q0(int i8) {
        if (Build.VERSION.SDK_INT >= 16) {
            this.f4904a.setMovementGranularities(i8);
        }
    }

    public CharSequence r() {
        return this.f4904a.getContentDescription();
    }

    public void r0(CharSequence charSequence) {
        this.f4904a.setPackageName(charSequence);
    }

    public Bundle s() {
        return Build.VERSION.SDK_INT >= 19 ? this.f4904a.getExtras() : new Bundle();
    }

    public void s0(CharSequence charSequence) {
        int i8 = Build.VERSION.SDK_INT;
        if (i8 >= 28) {
            this.f4904a.setPaneTitle(charSequence);
        } else if (i8 >= 19) {
            this.f4904a.getExtras().putCharSequence("androidx.view.accessibility.AccessibilityNodeInfoCompat.PANE_TITLE_KEY", charSequence);
        }
    }

    public int t() {
        if (Build.VERSION.SDK_INT >= 16) {
            return this.f4904a.getMovementGranularities();
        }
        return 0;
    }

    public void t0(View view) {
        this.f4905b = -1;
        this.f4904a.setParent(view);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        Rect rect = new Rect();
        m(rect);
        sb.append("; boundsInParent: " + rect);
        n(rect);
        sb.append("; boundsInScreen: " + rect);
        sb.append("; packageName: ");
        sb.append(v());
        sb.append("; className: ");
        sb.append(p());
        sb.append("; text: ");
        sb.append(x());
        sb.append("; contentDescription: ");
        sb.append(r());
        sb.append("; viewId: ");
        sb.append(y());
        sb.append("; checkable: ");
        sb.append(C());
        sb.append("; checked: ");
        sb.append(D());
        sb.append("; focusable: ");
        sb.append(G());
        sb.append("; focused: ");
        sb.append(H());
        sb.append("; selected: ");
        sb.append(L());
        sb.append("; clickable: ");
        sb.append(E());
        sb.append("; longClickable: ");
        sb.append(I());
        sb.append("; enabled: ");
        sb.append(F());
        sb.append("; password: ");
        sb.append(J());
        sb.append("; scrollable: " + K());
        sb.append("; [");
        if (Build.VERSION.SDK_INT >= 21) {
            List<a> i8 = i();
            for (int i9 = 0; i9 < i8.size(); i9++) {
                a aVar = i8.get(i9);
                String j8 = j(aVar.b());
                if (j8.equals("ACTION_UNKNOWN") && aVar.c() != null) {
                    j8 = aVar.c().toString();
                }
                sb.append(j8);
                if (i9 != i8.size() - 1) {
                    sb.append(", ");
                }
            }
        } else {
            int k8 = k();
            while (k8 != 0) {
                int numberOfTrailingZeros = 1 << Integer.numberOfTrailingZeros(k8);
                k8 &= ~numberOfTrailingZeros;
                sb.append(j(numberOfTrailingZeros));
                if (k8 != 0) {
                    sb.append(", ");
                }
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public void u0(View view, int i8) {
        this.f4905b = i8;
        if (Build.VERSION.SDK_INT >= 16) {
            this.f4904a.setParent(view, i8);
        }
    }

    public CharSequence v() {
        return this.f4904a.getPackageName();
    }

    public void v0(d dVar) {
        if (Build.VERSION.SDK_INT >= 19) {
            this.f4904a.setRangeInfo((AccessibilityNodeInfo.RangeInfo) dVar.f4934a);
        }
    }

    public void w0(CharSequence charSequence) {
        if (Build.VERSION.SDK_INT >= 19) {
            this.f4904a.getExtras().putCharSequence("AccessibilityNodeInfo.roleDescription", charSequence);
        }
    }

    public CharSequence x() {
        if (z()) {
            List<Integer> h8 = h("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_START_KEY");
            List<Integer> h9 = h("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_END_KEY");
            List<Integer> h10 = h("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_FLAGS_KEY");
            List<Integer> h11 = h("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_ID_KEY");
            SpannableString spannableString = new SpannableString(TextUtils.substring(this.f4904a.getText(), 0, this.f4904a.getText().length()));
            for (int i8 = 0; i8 < h8.size(); i8++) {
                spannableString.setSpan(new androidx.core.view.accessibility.a(h11.get(i8).intValue(), this, s().getInt("androidx.view.accessibility.AccessibilityNodeInfoCompat.SPANS_ACTION_ID_KEY")), h8.get(i8).intValue(), h9.get(i8).intValue(), h10.get(i8).intValue());
            }
            return spannableString;
        }
        return this.f4904a.getText();
    }

    public void x0(boolean z4) {
        if (Build.VERSION.SDK_INT >= 28) {
            this.f4904a.setScreenReaderFocusable(z4);
        } else {
            W(1, z4);
        }
    }

    public String y() {
        if (Build.VERSION.SDK_INT >= 18) {
            return this.f4904a.getViewIdResourceName();
        }
        return null;
    }

    public void y0(boolean z4) {
        this.f4904a.setScrollable(z4);
    }

    public void z0(boolean z4) {
        this.f4904a.setSelected(z4);
    }
}
