package androidx.appcompat.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewStub;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.CursorAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.view.c0;
import androidx.core.widget.NestedScrollView;
import java.lang.ref.WeakReference;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class AlertController {
    NestedScrollView A;
    private Drawable C;
    private ImageView D;
    private TextView E;
    private TextView F;
    private View G;
    ListAdapter H;
    private int J;
    private int K;
    int L;
    int M;
    int N;
    int O;
    private boolean P;
    Handler R;

    /* renamed from: a  reason: collision with root package name */
    private final Context f470a;

    /* renamed from: b  reason: collision with root package name */
    final i f471b;

    /* renamed from: c  reason: collision with root package name */
    private final Window f472c;

    /* renamed from: d  reason: collision with root package name */
    private final int f473d;

    /* renamed from: e  reason: collision with root package name */
    private CharSequence f474e;

    /* renamed from: f  reason: collision with root package name */
    private CharSequence f475f;

    /* renamed from: g  reason: collision with root package name */
    ListView f476g;

    /* renamed from: h  reason: collision with root package name */
    private View f477h;

    /* renamed from: i  reason: collision with root package name */
    private int f478i;

    /* renamed from: j  reason: collision with root package name */
    private int f479j;

    /* renamed from: k  reason: collision with root package name */
    private int f480k;

    /* renamed from: l  reason: collision with root package name */
    private int f481l;

    /* renamed from: m  reason: collision with root package name */
    private int f482m;

    /* renamed from: o  reason: collision with root package name */
    Button f484o;

    /* renamed from: p  reason: collision with root package name */
    private CharSequence f485p;
    Message q;

    /* renamed from: r  reason: collision with root package name */
    private Drawable f486r;

    /* renamed from: s  reason: collision with root package name */
    Button f487s;

    /* renamed from: t  reason: collision with root package name */
    private CharSequence f488t;

    /* renamed from: u  reason: collision with root package name */
    Message f489u;

    /* renamed from: v  reason: collision with root package name */
    private Drawable f490v;

    /* renamed from: w  reason: collision with root package name */
    Button f491w;

    /* renamed from: x  reason: collision with root package name */
    private CharSequence f492x;

    /* renamed from: y  reason: collision with root package name */
    Message f493y;

    /* renamed from: z  reason: collision with root package name */
    private Drawable f494z;

    /* renamed from: n  reason: collision with root package name */
    private boolean f483n = false;
    private int B = 0;
    int I = -1;
    private int Q = 0;
    private final View.OnClickListener S = new a();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class RecycleListView extends ListView {

        /* renamed from: a  reason: collision with root package name */
        private final int f495a;

        /* renamed from: b  reason: collision with root package name */
        private final int f496b;

        public RecycleListView(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, g.j.f20070m2);
            this.f496b = obtainStyledAttributes.getDimensionPixelOffset(g.j.f20075n2, -1);
            this.f495a = obtainStyledAttributes.getDimensionPixelOffset(g.j.f20080o2, -1);
        }

        public void a(boolean z4, boolean z8) {
            if (z8 && z4) {
                return;
            }
            setPadding(getPaddingLeft(), z4 ? getPaddingTop() : this.f495a, getPaddingRight(), z8 ? getPaddingBottom() : this.f496b);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements View.OnClickListener {
        a() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            Message message;
            Message message2;
            AlertController alertController = AlertController.this;
            Message obtain = ((view != alertController.f484o || (message2 = alertController.q) == null) && (view != alertController.f487s || (message2 = alertController.f489u) == null)) ? (view != alertController.f491w || (message = alertController.f493y) == null) ? null : Message.obtain(message) : Message.obtain(message2);
            if (obtain != null) {
                obtain.sendToTarget();
            }
            AlertController alertController2 = AlertController.this;
            alertController2.R.obtainMessage(1, alertController2.f471b).sendToTarget();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b implements NestedScrollView.c {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ View f498a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ View f499b;

        b(View view, View view2) {
            this.f498a = view;
            this.f499b = view2;
        }

        @Override // androidx.core.widget.NestedScrollView.c
        public void a(NestedScrollView nestedScrollView, int i8, int i9, int i10, int i11) {
            AlertController.f(nestedScrollView, this.f498a, this.f499b);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class c implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ View f501a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ View f502b;

        c(View view, View view2) {
            this.f501a = view;
            this.f502b = view2;
        }

        @Override // java.lang.Runnable
        public void run() {
            AlertController.f(AlertController.this.A, this.f501a, this.f502b);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class d implements AbsListView.OnScrollListener {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ View f504a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ View f505b;

        d(View view, View view2) {
            this.f504a = view;
            this.f505b = view2;
        }

        @Override // android.widget.AbsListView.OnScrollListener
        public void onScroll(AbsListView absListView, int i8, int i9, int i10) {
            AlertController.f(absListView, this.f504a, this.f505b);
        }

        @Override // android.widget.AbsListView.OnScrollListener
        public void onScrollStateChanged(AbsListView absListView, int i8) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class e implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ View f507a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ View f508b;

        e(View view, View view2) {
            this.f507a = view;
            this.f508b = view2;
        }

        @Override // java.lang.Runnable
        public void run() {
            AlertController.f(AlertController.this.f476g, this.f507a, this.f508b);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class f {
        public int A;
        public int B;
        public int C;
        public int D;
        public boolean[] F;
        public boolean G;
        public boolean H;
        public DialogInterface.OnMultiChoiceClickListener J;
        public Cursor K;
        public String L;
        public String M;
        public AdapterView.OnItemSelectedListener N;
        public e O;

        /* renamed from: a  reason: collision with root package name */
        public final Context f510a;

        /* renamed from: b  reason: collision with root package name */
        public final LayoutInflater f511b;

        /* renamed from: d  reason: collision with root package name */
        public Drawable f513d;

        /* renamed from: f  reason: collision with root package name */
        public CharSequence f515f;

        /* renamed from: g  reason: collision with root package name */
        public View f516g;

        /* renamed from: h  reason: collision with root package name */
        public CharSequence f517h;

        /* renamed from: i  reason: collision with root package name */
        public CharSequence f518i;

        /* renamed from: j  reason: collision with root package name */
        public Drawable f519j;

        /* renamed from: k  reason: collision with root package name */
        public DialogInterface.OnClickListener f520k;

        /* renamed from: l  reason: collision with root package name */
        public CharSequence f521l;

        /* renamed from: m  reason: collision with root package name */
        public Drawable f522m;

        /* renamed from: n  reason: collision with root package name */
        public DialogInterface.OnClickListener f523n;

        /* renamed from: o  reason: collision with root package name */
        public CharSequence f524o;

        /* renamed from: p  reason: collision with root package name */
        public Drawable f525p;
        public DialogInterface.OnClickListener q;

        /* renamed from: s  reason: collision with root package name */
        public DialogInterface.OnCancelListener f527s;

        /* renamed from: t  reason: collision with root package name */
        public DialogInterface.OnDismissListener f528t;

        /* renamed from: u  reason: collision with root package name */
        public DialogInterface.OnKeyListener f529u;

        /* renamed from: v  reason: collision with root package name */
        public CharSequence[] f530v;

        /* renamed from: w  reason: collision with root package name */
        public ListAdapter f531w;

        /* renamed from: x  reason: collision with root package name */
        public DialogInterface.OnClickListener f532x;

        /* renamed from: y  reason: collision with root package name */
        public int f533y;

        /* renamed from: z  reason: collision with root package name */
        public View f534z;

        /* renamed from: c  reason: collision with root package name */
        public int f512c = 0;

        /* renamed from: e  reason: collision with root package name */
        public int f514e = 0;
        public boolean E = false;
        public int I = -1;
        public boolean P = true;

        /* renamed from: r  reason: collision with root package name */
        public boolean f526r = true;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public class a extends ArrayAdapter<CharSequence> {

            /* renamed from: a  reason: collision with root package name */
            final /* synthetic */ RecycleListView f535a;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            a(Context context, int i8, int i9, CharSequence[] charSequenceArr, RecycleListView recycleListView) {
                super(context, i8, i9, charSequenceArr);
                this.f535a = recycleListView;
            }

            @Override // android.widget.ArrayAdapter, android.widget.Adapter
            public View getView(int i8, View view, ViewGroup viewGroup) {
                View view2 = super.getView(i8, view, viewGroup);
                boolean[] zArr = f.this.F;
                if (zArr != null && zArr[i8]) {
                    this.f535a.setItemChecked(i8, true);
                }
                return view2;
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public class b extends CursorAdapter {

            /* renamed from: a  reason: collision with root package name */
            private final int f537a;

            /* renamed from: b  reason: collision with root package name */
            private final int f538b;

            /* renamed from: c  reason: collision with root package name */
            final /* synthetic */ RecycleListView f539c;

            /* renamed from: d  reason: collision with root package name */
            final /* synthetic */ AlertController f540d;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            b(Context context, Cursor cursor, boolean z4, RecycleListView recycleListView, AlertController alertController) {
                super(context, cursor, z4);
                this.f539c = recycleListView;
                this.f540d = alertController;
                Cursor cursor2 = getCursor();
                this.f537a = cursor2.getColumnIndexOrThrow(f.this.L);
                this.f538b = cursor2.getColumnIndexOrThrow(f.this.M);
            }

            @Override // android.widget.CursorAdapter
            public void bindView(View view, Context context, Cursor cursor) {
                ((CheckedTextView) view.findViewById(16908308)).setText(cursor.getString(this.f537a));
                this.f539c.setItemChecked(cursor.getPosition(), cursor.getInt(this.f538b) == 1);
            }

            @Override // android.widget.CursorAdapter
            public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
                return f.this.f511b.inflate(this.f540d.M, viewGroup, false);
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public class c implements AdapterView.OnItemClickListener {

            /* renamed from: a  reason: collision with root package name */
            final /* synthetic */ AlertController f542a;

            c(AlertController alertController) {
                this.f542a = alertController;
            }

            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i8, long j8) {
                f.this.f532x.onClick(this.f542a.f471b, i8);
                if (f.this.H) {
                    return;
                }
                this.f542a.f471b.dismiss();
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public class d implements AdapterView.OnItemClickListener {

            /* renamed from: a  reason: collision with root package name */
            final /* synthetic */ RecycleListView f544a;

            /* renamed from: b  reason: collision with root package name */
            final /* synthetic */ AlertController f545b;

            d(RecycleListView recycleListView, AlertController alertController) {
                this.f544a = recycleListView;
                this.f545b = alertController;
            }

            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i8, long j8) {
                boolean[] zArr = f.this.F;
                if (zArr != null) {
                    zArr[i8] = this.f544a.isItemChecked(i8);
                }
                f.this.J.onClick(this.f545b.f471b, i8, this.f544a.isItemChecked(i8));
            }
        }

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public interface e {
            void a(ListView listView);
        }

        public f(Context context) {
            this.f510a = context;
            this.f511b = (LayoutInflater) context.getSystemService("layout_inflater");
        }

        /* JADX WARN: Removed duplicated region for block: B:31:0x0093  */
        /* JADX WARN: Removed duplicated region for block: B:34:0x009a  */
        /* JADX WARN: Removed duplicated region for block: B:35:0x009e  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private void b(androidx.appcompat.app.AlertController r11) {
            /*
                r10 = this;
                android.view.LayoutInflater r0 = r10.f511b
                int r1 = r11.L
                r2 = 0
                android.view.View r0 = r0.inflate(r1, r2)
                androidx.appcompat.app.AlertController$RecycleListView r0 = (androidx.appcompat.app.AlertController.RecycleListView) r0
                boolean r1 = r10.G
                r8 = 1
                if (r1 == 0) goto L35
                android.database.Cursor r1 = r10.K
                if (r1 != 0) goto L26
                androidx.appcompat.app.AlertController$f$a r9 = new androidx.appcompat.app.AlertController$f$a
                android.content.Context r3 = r10.f510a
                int r4 = r11.M
                r5 = 16908308(0x1020014, float:2.3877285E-38)
                java.lang.CharSequence[] r6 = r10.f530v
                r1 = r9
                r2 = r10
                r7 = r0
                r1.<init>(r3, r4, r5, r6, r7)
                goto L6b
            L26:
                androidx.appcompat.app.AlertController$f$b r9 = new androidx.appcompat.app.AlertController$f$b
                android.content.Context r3 = r10.f510a
                android.database.Cursor r4 = r10.K
                r5 = 0
                r1 = r9
                r2 = r10
                r6 = r0
                r7 = r11
                r1.<init>(r3, r4, r5, r6, r7)
                goto L6b
            L35:
                boolean r1 = r10.H
                if (r1 == 0) goto L3c
                int r1 = r11.N
                goto L3e
            L3c:
                int r1 = r11.O
            L3e:
                r4 = r1
                android.database.Cursor r1 = r10.K
                r2 = 16908308(0x1020014, float:2.3877285E-38)
                if (r1 == 0) goto L5d
                android.widget.SimpleCursorAdapter r9 = new android.widget.SimpleCursorAdapter
                android.content.Context r3 = r10.f510a
                android.database.Cursor r5 = r10.K
                java.lang.String[] r6 = new java.lang.String[r8]
                java.lang.String r1 = r10.L
                r7 = 0
                r6[r7] = r1
                int[] r1 = new int[r8]
                r1[r7] = r2
                r2 = r9
                r7 = r1
                r2.<init>(r3, r4, r5, r6, r7)
                goto L6b
            L5d:
                android.widget.ListAdapter r9 = r10.f531w
                if (r9 == 0) goto L62
                goto L6b
            L62:
                androidx.appcompat.app.AlertController$h r9 = new androidx.appcompat.app.AlertController$h
                android.content.Context r1 = r10.f510a
                java.lang.CharSequence[] r3 = r10.f530v
                r9.<init>(r1, r4, r2, r3)
            L6b:
                androidx.appcompat.app.AlertController$f$e r1 = r10.O
                if (r1 == 0) goto L72
                r1.a(r0)
            L72:
                r11.H = r9
                int r1 = r10.I
                r11.I = r1
                android.content.DialogInterface$OnClickListener r1 = r10.f532x
                if (r1 == 0) goto L85
                androidx.appcompat.app.AlertController$f$c r1 = new androidx.appcompat.app.AlertController$f$c
                r1.<init>(r11)
            L81:
                r0.setOnItemClickListener(r1)
                goto L8f
            L85:
                android.content.DialogInterface$OnMultiChoiceClickListener r1 = r10.J
                if (r1 == 0) goto L8f
                androidx.appcompat.app.AlertController$f$d r1 = new androidx.appcompat.app.AlertController$f$d
                r1.<init>(r0, r11)
                goto L81
            L8f:
                android.widget.AdapterView$OnItemSelectedListener r1 = r10.N
                if (r1 == 0) goto L96
                r0.setOnItemSelectedListener(r1)
            L96:
                boolean r1 = r10.H
                if (r1 == 0) goto L9e
                r0.setChoiceMode(r8)
                goto La6
            L9e:
                boolean r1 = r10.G
                if (r1 == 0) goto La6
                r1 = 2
                r0.setChoiceMode(r1)
            La6:
                r11.f476g = r0
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.app.AlertController.f.b(androidx.appcompat.app.AlertController):void");
        }

        public void a(AlertController alertController) {
            View view = this.f516g;
            if (view != null) {
                alertController.l(view);
            } else {
                CharSequence charSequence = this.f515f;
                if (charSequence != null) {
                    alertController.q(charSequence);
                }
                Drawable drawable = this.f513d;
                if (drawable != null) {
                    alertController.n(drawable);
                }
                int i8 = this.f512c;
                if (i8 != 0) {
                    alertController.m(i8);
                }
                int i9 = this.f514e;
                if (i9 != 0) {
                    alertController.m(alertController.c(i9));
                }
            }
            CharSequence charSequence2 = this.f517h;
            if (charSequence2 != null) {
                alertController.o(charSequence2);
            }
            CharSequence charSequence3 = this.f518i;
            if (charSequence3 != null || this.f519j != null) {
                alertController.k(-1, charSequence3, this.f520k, null, this.f519j);
            }
            CharSequence charSequence4 = this.f521l;
            if (charSequence4 != null || this.f522m != null) {
                alertController.k(-2, charSequence4, this.f523n, null, this.f522m);
            }
            CharSequence charSequence5 = this.f524o;
            if (charSequence5 != null || this.f525p != null) {
                alertController.k(-3, charSequence5, this.q, null, this.f525p);
            }
            if (this.f530v != null || this.K != null || this.f531w != null) {
                b(alertController);
            }
            View view2 = this.f534z;
            if (view2 != null) {
                if (this.E) {
                    alertController.t(view2, this.A, this.B, this.C, this.D);
                    return;
                } else {
                    alertController.s(view2);
                    return;
                }
            }
            int i10 = this.f533y;
            if (i10 != 0) {
                alertController.r(i10);
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class g extends Handler {

        /* renamed from: a  reason: collision with root package name */
        private WeakReference<DialogInterface> f547a;

        public g(DialogInterface dialogInterface) {
            this.f547a = new WeakReference<>(dialogInterface);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i8 = message.what;
            if (i8 == -3 || i8 == -2 || i8 == -1) {
                ((DialogInterface.OnClickListener) message.obj).onClick(this.f547a.get(), message.what);
            } else if (i8 != 1) {
            } else {
                ((DialogInterface) message.obj).dismiss();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class h extends ArrayAdapter<CharSequence> {
        public h(Context context, int i8, int i9, CharSequence[] charSequenceArr) {
            super(context, i8, i9, charSequenceArr);
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public long getItemId(int i8) {
            return i8;
        }

        @Override // android.widget.BaseAdapter, android.widget.Adapter
        public boolean hasStableIds() {
            return true;
        }
    }

    public AlertController(Context context, i iVar, Window window) {
        this.f470a = context;
        this.f471b = iVar;
        this.f472c = window;
        this.R = new g(iVar);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(null, g.j.H, g.a.f19875n, 0);
        this.J = obtainStyledAttributes.getResourceId(g.j.I, 0);
        this.K = obtainStyledAttributes.getResourceId(g.j.K, 0);
        this.L = obtainStyledAttributes.getResourceId(g.j.M, 0);
        this.M = obtainStyledAttributes.getResourceId(g.j.N, 0);
        this.N = obtainStyledAttributes.getResourceId(g.j.P, 0);
        this.O = obtainStyledAttributes.getResourceId(g.j.L, 0);
        this.P = obtainStyledAttributes.getBoolean(g.j.O, true);
        this.f473d = obtainStyledAttributes.getDimensionPixelSize(g.j.J, 0);
        obtainStyledAttributes.recycle();
        iVar.g(1);
    }

    static boolean a(View view) {
        if (view.onCheckIsTextEditor()) {
            return true;
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            while (childCount > 0) {
                childCount--;
                if (a(viewGroup.getChildAt(childCount))) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private void b(Button button) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) button.getLayoutParams();
        layoutParams.gravity = 1;
        layoutParams.weight = 0.5f;
        button.setLayoutParams(layoutParams);
    }

    static void f(View view, View view2, View view3) {
        if (view2 != null) {
            view2.setVisibility(view.canScrollVertically(-1) ? 0 : 4);
        }
        if (view3 != null) {
            view3.setVisibility(view.canScrollVertically(1) ? 0 : 4);
        }
    }

    private ViewGroup i(View view, View view2) {
        if (view == null) {
            if (view2 instanceof ViewStub) {
                view2 = ((ViewStub) view2).inflate();
            }
            return (ViewGroup) view2;
        }
        if (view2 != null) {
            ViewParent parent = view2.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(view2);
            }
        }
        if (view instanceof ViewStub) {
            view = ((ViewStub) view).inflate();
        }
        return (ViewGroup) view;
    }

    private int j() {
        int i8 = this.K;
        return (i8 != 0 && this.Q == 1) ? i8 : this.J;
    }

    private void p(ViewGroup viewGroup, View view, int i8, int i9) {
        View findViewById = this.f472c.findViewById(g.f.B);
        View findViewById2 = this.f472c.findViewById(g.f.A);
        if (Build.VERSION.SDK_INT >= 23) {
            c0.M0(view, i8, i9);
            if (findViewById != null) {
                viewGroup.removeView(findViewById);
            }
            if (findViewById2 == null) {
                return;
            }
        } else {
            if (findViewById != null && (i8 & 1) == 0) {
                viewGroup.removeView(findViewById);
                findViewById = null;
            }
            if (findViewById2 != null && (i8 & 2) == 0) {
                viewGroup.removeView(findViewById2);
                findViewById2 = null;
            }
            if (findViewById == null && findViewById2 == null) {
                return;
            }
            if (this.f475f != null) {
                this.A.setOnScrollChangeListener(new b(findViewById, findViewById2));
                this.A.post(new c(findViewById, findViewById2));
                return;
            }
            ListView listView = this.f476g;
            if (listView != null) {
                listView.setOnScrollListener(new d(findViewById, findViewById2));
                this.f476g.post(new e(findViewById, findViewById2));
                return;
            }
            if (findViewById != null) {
                viewGroup.removeView(findViewById);
            }
            if (findViewById2 == null) {
                return;
            }
        }
        viewGroup.removeView(findViewById2);
    }

    private void u(ViewGroup viewGroup) {
        boolean z4;
        Button button;
        Button button2 = (Button) viewGroup.findViewById(16908313);
        this.f484o = button2;
        button2.setOnClickListener(this.S);
        if (TextUtils.isEmpty(this.f485p) && this.f486r == null) {
            this.f484o.setVisibility(8);
            z4 = false;
        } else {
            this.f484o.setText(this.f485p);
            Drawable drawable = this.f486r;
            if (drawable != null) {
                int i8 = this.f473d;
                drawable.setBounds(0, 0, i8, i8);
                this.f484o.setCompoundDrawables(this.f486r, null, null, null);
            }
            this.f484o.setVisibility(0);
            z4 = true;
        }
        Button button3 = (Button) viewGroup.findViewById(16908314);
        this.f487s = button3;
        button3.setOnClickListener(this.S);
        if (TextUtils.isEmpty(this.f488t) && this.f490v == null) {
            this.f487s.setVisibility(8);
        } else {
            this.f487s.setText(this.f488t);
            Drawable drawable2 = this.f490v;
            if (drawable2 != null) {
                int i9 = this.f473d;
                drawable2.setBounds(0, 0, i9, i9);
                this.f487s.setCompoundDrawables(this.f490v, null, null, null);
            }
            this.f487s.setVisibility(0);
            z4 |= true;
        }
        Button button4 = (Button) viewGroup.findViewById(16908315);
        this.f491w = button4;
        button4.setOnClickListener(this.S);
        if (TextUtils.isEmpty(this.f492x) && this.f494z == null) {
            this.f491w.setVisibility(8);
        } else {
            this.f491w.setText(this.f492x);
            Drawable drawable3 = this.f494z;
            if (drawable3 != null) {
                int i10 = this.f473d;
                drawable3.setBounds(0, 0, i10, i10);
                this.f491w.setCompoundDrawables(this.f494z, null, null, null);
            }
            this.f491w.setVisibility(0);
            z4 |= true;
        }
        if (z(this.f470a)) {
            if (z4) {
                button = this.f484o;
            } else if (z4) {
                button = this.f487s;
            } else if (z4) {
                button = this.f491w;
            }
            b(button);
        }
        if (z4) {
            return;
        }
        viewGroup.setVisibility(8);
    }

    private void v(ViewGroup viewGroup) {
        NestedScrollView nestedScrollView = (NestedScrollView) this.f472c.findViewById(g.f.C);
        this.A = nestedScrollView;
        nestedScrollView.setFocusable(false);
        this.A.setNestedScrollingEnabled(false);
        TextView textView = (TextView) viewGroup.findViewById(16908299);
        this.F = textView;
        if (textView == null) {
            return;
        }
        CharSequence charSequence = this.f475f;
        if (charSequence != null) {
            textView.setText(charSequence);
            return;
        }
        textView.setVisibility(8);
        this.A.removeView(this.F);
        if (this.f476g == null) {
            viewGroup.setVisibility(8);
            return;
        }
        ViewGroup viewGroup2 = (ViewGroup) this.A.getParent();
        int indexOfChild = viewGroup2.indexOfChild(this.A);
        viewGroup2.removeViewAt(indexOfChild);
        viewGroup2.addView(this.f476g, indexOfChild, new ViewGroup.LayoutParams(-1, -1));
    }

    private void w(ViewGroup viewGroup) {
        View view = this.f477h;
        if (view == null) {
            view = this.f478i != 0 ? LayoutInflater.from(this.f470a).inflate(this.f478i, viewGroup, false) : null;
        }
        boolean z4 = view != null;
        if (!z4 || !a(view)) {
            this.f472c.setFlags(131072, 131072);
        }
        if (!z4) {
            viewGroup.setVisibility(8);
            return;
        }
        FrameLayout frameLayout = (FrameLayout) this.f472c.findViewById(g.f.f19950o);
        frameLayout.addView(view, new ViewGroup.LayoutParams(-1, -1));
        if (this.f483n) {
            frameLayout.setPadding(this.f479j, this.f480k, this.f481l, this.f482m);
        }
        if (this.f476g != null) {
            ((LinearLayout.LayoutParams) ((LinearLayoutCompat.LayoutParams) viewGroup.getLayoutParams())).weight = 0.0f;
        }
    }

    private void x(ViewGroup viewGroup) {
        if (this.G != null) {
            viewGroup.addView(this.G, 0, new ViewGroup.LayoutParams(-1, -2));
            this.f472c.findViewById(g.f.U).setVisibility(8);
            return;
        }
        this.D = (ImageView) this.f472c.findViewById(16908294);
        if (!(!TextUtils.isEmpty(this.f474e)) || !this.P) {
            this.f472c.findViewById(g.f.U).setVisibility(8);
            this.D.setVisibility(8);
            viewGroup.setVisibility(8);
            return;
        }
        TextView textView = (TextView) this.f472c.findViewById(g.f.f19946k);
        this.E = textView;
        textView.setText(this.f474e);
        int i8 = this.B;
        if (i8 != 0) {
            this.D.setImageResource(i8);
            return;
        }
        Drawable drawable = this.C;
        if (drawable != null) {
            this.D.setImageDrawable(drawable);
            return;
        }
        this.E.setPadding(this.D.getPaddingLeft(), this.D.getPaddingTop(), this.D.getPaddingRight(), this.D.getPaddingBottom());
        this.D.setVisibility(8);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void y() {
        View findViewById;
        ListAdapter listAdapter;
        View findViewById2;
        View findViewById3 = this.f472c.findViewById(g.f.f19960z);
        int i8 = g.f.V;
        View findViewById4 = findViewById3.findViewById(i8);
        int i9 = g.f.f19949n;
        View findViewById5 = findViewById3.findViewById(i9);
        int i10 = g.f.f19947l;
        View findViewById6 = findViewById3.findViewById(i10);
        ViewGroup viewGroup = (ViewGroup) findViewById3.findViewById(g.f.f19951p);
        w(viewGroup);
        View findViewById7 = viewGroup.findViewById(i8);
        View findViewById8 = viewGroup.findViewById(i9);
        View findViewById9 = viewGroup.findViewById(i10);
        ViewGroup i11 = i(findViewById7, findViewById4);
        ViewGroup i12 = i(findViewById8, findViewById5);
        ViewGroup i13 = i(findViewById9, findViewById6);
        v(i12);
        u(i13);
        x(i11);
        boolean z4 = viewGroup.getVisibility() != 8;
        boolean z8 = (i11 == null || i11.getVisibility() == 8) ? false : 1;
        boolean z9 = (i13 == null || i13.getVisibility() == 8) ? false : true;
        if (!z9 && i12 != null && (findViewById2 = i12.findViewById(g.f.Q)) != null) {
            findViewById2.setVisibility(0);
        }
        if (z8) {
            NestedScrollView nestedScrollView = this.A;
            if (nestedScrollView != null) {
                nestedScrollView.setClipToPadding(true);
            }
            View view = null;
            if (this.f475f != null || this.f476g != null) {
                view = i11.findViewById(g.f.T);
            }
            if (view != null) {
                view.setVisibility(0);
            }
        } else if (i12 != null && (findViewById = i12.findViewById(g.f.R)) != null) {
            findViewById.setVisibility(0);
        }
        ListView listView = this.f476g;
        if (listView instanceof RecycleListView) {
            ((RecycleListView) listView).a(z8, z9);
        }
        if (!z4) {
            View view2 = this.f476g;
            if (view2 == null) {
                view2 = this.A;
            }
            if (view2 != null) {
                p(i12, view2, z8 | (z9 ? 2 : 0), 3);
            }
        }
        ListView listView2 = this.f476g;
        if (listView2 == null || (listAdapter = this.H) == null) {
            return;
        }
        listView2.setAdapter(listAdapter);
        int i14 = this.I;
        if (i14 > -1) {
            listView2.setItemChecked(i14, true);
            listView2.setSelection(i14);
        }
    }

    private static boolean z(Context context) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(g.a.f19874m, typedValue, true);
        return typedValue.data != 0;
    }

    public int c(int i8) {
        TypedValue typedValue = new TypedValue();
        this.f470a.getTheme().resolveAttribute(i8, typedValue, true);
        return typedValue.resourceId;
    }

    public ListView d() {
        return this.f476g;
    }

    public void e() {
        this.f471b.setContentView(j());
        y();
    }

    public boolean g(int i8, KeyEvent keyEvent) {
        NestedScrollView nestedScrollView = this.A;
        return nestedScrollView != null && nestedScrollView.r(keyEvent);
    }

    public boolean h(int i8, KeyEvent keyEvent) {
        NestedScrollView nestedScrollView = this.A;
        return nestedScrollView != null && nestedScrollView.r(keyEvent);
    }

    public void k(int i8, CharSequence charSequence, DialogInterface.OnClickListener onClickListener, Message message, Drawable drawable) {
        if (message == null && onClickListener != null) {
            message = this.R.obtainMessage(i8, onClickListener);
        }
        if (i8 == -3) {
            this.f492x = charSequence;
            this.f493y = message;
            this.f494z = drawable;
        } else if (i8 == -2) {
            this.f488t = charSequence;
            this.f489u = message;
            this.f490v = drawable;
        } else if (i8 != -1) {
            throw new IllegalArgumentException("Button does not exist");
        } else {
            this.f485p = charSequence;
            this.q = message;
            this.f486r = drawable;
        }
    }

    public void l(View view) {
        this.G = view;
    }

    public void m(int i8) {
        this.C = null;
        this.B = i8;
        ImageView imageView = this.D;
        if (imageView != null) {
            if (i8 == 0) {
                imageView.setVisibility(8);
                return;
            }
            imageView.setVisibility(0);
            this.D.setImageResource(this.B);
        }
    }

    public void n(Drawable drawable) {
        this.C = drawable;
        this.B = 0;
        ImageView imageView = this.D;
        if (imageView != null) {
            if (drawable == null) {
                imageView.setVisibility(8);
                return;
            }
            imageView.setVisibility(0);
            this.D.setImageDrawable(drawable);
        }
    }

    public void o(CharSequence charSequence) {
        this.f475f = charSequence;
        TextView textView = this.F;
        if (textView != null) {
            textView.setText(charSequence);
        }
    }

    public void q(CharSequence charSequence) {
        this.f474e = charSequence;
        TextView textView = this.E;
        if (textView != null) {
            textView.setText(charSequence);
        }
    }

    public void r(int i8) {
        this.f477h = null;
        this.f478i = i8;
        this.f483n = false;
    }

    public void s(View view) {
        this.f477h = view;
        this.f478i = 0;
        this.f483n = false;
    }

    public void t(View view, int i8, int i9, int i10, int i11) {
        this.f477h = view;
        this.f478i = 0;
        this.f483n = true;
        this.f479j = i8;
        this.f480k = i9;
        this.f481l = i10;
        this.f482m = i11;
    }
}
