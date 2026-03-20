package androidx.appcompat.view;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.InflateException;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import androidx.appcompat.view.menu.j;
import androidx.appcompat.widget.j0;
import androidx.appcompat.widget.t;
import androidx.core.view.m;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class g extends MenuInflater {

    /* renamed from: e  reason: collision with root package name */
    static final Class<?>[] f778e;

    /* renamed from: f  reason: collision with root package name */
    static final Class<?>[] f779f;

    /* renamed from: a  reason: collision with root package name */
    final Object[] f780a;

    /* renamed from: b  reason: collision with root package name */
    final Object[] f781b;

    /* renamed from: c  reason: collision with root package name */
    Context f782c;

    /* renamed from: d  reason: collision with root package name */
    private Object f783d;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a implements MenuItem.OnMenuItemClickListener {

        /* renamed from: c  reason: collision with root package name */
        private static final Class<?>[] f784c = {MenuItem.class};

        /* renamed from: a  reason: collision with root package name */
        private Object f785a;

        /* renamed from: b  reason: collision with root package name */
        private Method f786b;

        public a(Object obj, String str) {
            this.f785a = obj;
            Class<?> cls = obj.getClass();
            try {
                this.f786b = cls.getMethod(str, f784c);
            } catch (Exception e8) {
                InflateException inflateException = new InflateException("Couldn't resolve menu item onClick handler " + str + " in class " + cls.getName());
                inflateException.initCause(e8);
                throw inflateException;
            }
        }

        @Override // android.view.MenuItem.OnMenuItemClickListener
        public boolean onMenuItemClick(MenuItem menuItem) {
            try {
                if (this.f786b.getReturnType() == Boolean.TYPE) {
                    return ((Boolean) this.f786b.invoke(this.f785a, menuItem)).booleanValue();
                }
                this.f786b.invoke(this.f785a, menuItem);
                return true;
            } catch (Exception e8) {
                throw new RuntimeException(e8);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b {
        androidx.core.view.b A;
        private CharSequence B;
        private CharSequence C;
        private ColorStateList D = null;
        private PorterDuff.Mode E = null;

        /* renamed from: a  reason: collision with root package name */
        private Menu f787a;

        /* renamed from: b  reason: collision with root package name */
        private int f788b;

        /* renamed from: c  reason: collision with root package name */
        private int f789c;

        /* renamed from: d  reason: collision with root package name */
        private int f790d;

        /* renamed from: e  reason: collision with root package name */
        private int f791e;

        /* renamed from: f  reason: collision with root package name */
        private boolean f792f;

        /* renamed from: g  reason: collision with root package name */
        private boolean f793g;

        /* renamed from: h  reason: collision with root package name */
        private boolean f794h;

        /* renamed from: i  reason: collision with root package name */
        private int f795i;

        /* renamed from: j  reason: collision with root package name */
        private int f796j;

        /* renamed from: k  reason: collision with root package name */
        private CharSequence f797k;

        /* renamed from: l  reason: collision with root package name */
        private CharSequence f798l;

        /* renamed from: m  reason: collision with root package name */
        private int f799m;

        /* renamed from: n  reason: collision with root package name */
        private char f800n;

        /* renamed from: o  reason: collision with root package name */
        private int f801o;

        /* renamed from: p  reason: collision with root package name */
        private char f802p;
        private int q;

        /* renamed from: r  reason: collision with root package name */
        private int f803r;

        /* renamed from: s  reason: collision with root package name */
        private boolean f804s;

        /* renamed from: t  reason: collision with root package name */
        private boolean f805t;

        /* renamed from: u  reason: collision with root package name */
        private boolean f806u;

        /* renamed from: v  reason: collision with root package name */
        private int f807v;

        /* renamed from: w  reason: collision with root package name */
        private int f808w;

        /* renamed from: x  reason: collision with root package name */
        private String f809x;

        /* renamed from: y  reason: collision with root package name */
        private String f810y;

        /* renamed from: z  reason: collision with root package name */
        private String f811z;

        public b(Menu menu) {
            this.f787a = menu;
            h();
        }

        private char c(String str) {
            if (str == null) {
                return (char) 0;
            }
            return str.charAt(0);
        }

        private <T> T e(String str, Class<?>[] clsArr, Object[] objArr) {
            try {
                Constructor<?> constructor = Class.forName(str, false, g.this.f782c.getClassLoader()).getConstructor(clsArr);
                constructor.setAccessible(true);
                return (T) constructor.newInstance(objArr);
            } catch (Exception e8) {
                Log.w("SupportMenuInflater", "Cannot instantiate class: " + str, e8);
                return null;
            }
        }

        private void i(MenuItem menuItem) {
            boolean z4 = false;
            menuItem.setChecked(this.f804s).setVisible(this.f805t).setEnabled(this.f806u).setCheckable(this.f803r >= 1).setTitleCondensed(this.f798l).setIcon(this.f799m);
            int i8 = this.f807v;
            if (i8 >= 0) {
                menuItem.setShowAsAction(i8);
            }
            if (this.f811z != null) {
                if (g.this.f782c.isRestricted()) {
                    throw new IllegalStateException("The android:onClick attribute cannot be used within a restricted context");
                }
                menuItem.setOnMenuItemClickListener(new a(g.this.b(), this.f811z));
            }
            if (this.f803r >= 2) {
                if (menuItem instanceof androidx.appcompat.view.menu.i) {
                    ((androidx.appcompat.view.menu.i) menuItem).t(true);
                } else if (menuItem instanceof j) {
                    ((j) menuItem).h(true);
                }
            }
            String str = this.f809x;
            if (str != null) {
                menuItem.setActionView((View) e(str, g.f778e, g.this.f780a));
                z4 = true;
            }
            int i9 = this.f808w;
            if (i9 > 0) {
                if (z4) {
                    Log.w("SupportMenuInflater", "Ignoring attribute 'itemActionViewLayout'. Action view already specified.");
                } else {
                    menuItem.setActionView(i9);
                }
            }
            androidx.core.view.b bVar = this.A;
            if (bVar != null) {
                m.a(menuItem, bVar);
            }
            m.c(menuItem, this.B);
            m.g(menuItem, this.C);
            m.b(menuItem, this.f800n, this.f801o);
            m.f(menuItem, this.f802p, this.q);
            PorterDuff.Mode mode = this.E;
            if (mode != null) {
                m.e(menuItem, mode);
            }
            ColorStateList colorStateList = this.D;
            if (colorStateList != null) {
                m.d(menuItem, colorStateList);
            }
        }

        public void a() {
            this.f794h = true;
            i(this.f787a.add(this.f788b, this.f795i, this.f796j, this.f797k));
        }

        public SubMenu b() {
            this.f794h = true;
            SubMenu addSubMenu = this.f787a.addSubMenu(this.f788b, this.f795i, this.f796j, this.f797k);
            i(addSubMenu.getItem());
            return addSubMenu;
        }

        public boolean d() {
            return this.f794h;
        }

        public void f(AttributeSet attributeSet) {
            TypedArray obtainStyledAttributes = g.this.f782c.obtainStyledAttributes(attributeSet, g.j.f20128y1);
            this.f788b = obtainStyledAttributes.getResourceId(g.j.A1, 0);
            this.f789c = obtainStyledAttributes.getInt(g.j.C1, 0);
            this.f790d = obtainStyledAttributes.getInt(g.j.D1, 0);
            this.f791e = obtainStyledAttributes.getInt(g.j.E1, 0);
            this.f792f = obtainStyledAttributes.getBoolean(g.j.B1, true);
            this.f793g = obtainStyledAttributes.getBoolean(g.j.f20133z1, true);
            obtainStyledAttributes.recycle();
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void g(AttributeSet attributeSet) {
            j0 u8 = j0.u(g.this.f782c, attributeSet, g.j.F1);
            this.f795i = u8.n(g.j.I1, 0);
            this.f796j = (u8.k(g.j.L1, this.f789c) & (-65536)) | (u8.k(g.j.M1, this.f790d) & 65535);
            this.f797k = u8.p(g.j.N1);
            this.f798l = u8.p(g.j.O1);
            this.f799m = u8.n(g.j.G1, 0);
            this.f800n = c(u8.o(g.j.P1));
            this.f801o = u8.k(g.j.W1, RecognitionOptions.AZTEC);
            this.f802p = c(u8.o(g.j.Q1));
            this.q = u8.k(g.j.f20006a2, RecognitionOptions.AZTEC);
            int i8 = g.j.R1;
            this.f803r = u8.s(i8) ? u8.a(i8, false) : this.f791e;
            this.f804s = u8.a(g.j.J1, false);
            this.f805t = u8.a(g.j.K1, this.f792f);
            this.f806u = u8.a(g.j.H1, this.f793g);
            this.f807v = u8.k(g.j.f20012b2, -1);
            this.f811z = u8.o(g.j.S1);
            this.f808w = u8.n(g.j.T1, 0);
            this.f809x = u8.o(g.j.V1);
            String o5 = u8.o(g.j.U1);
            this.f810y = o5;
            boolean z4 = o5 != null;
            if (z4 && this.f808w == 0 && this.f809x == null) {
                this.A = (androidx.core.view.b) e(o5, g.f779f, g.this.f781b);
            } else {
                if (z4) {
                    Log.w("SupportMenuInflater", "Ignoring attribute 'actionProviderClass'. Action view already specified.");
                }
                this.A = null;
            }
            this.B = u8.p(g.j.X1);
            this.C = u8.p(g.j.f20018c2);
            int i9 = g.j.Z1;
            if (u8.s(i9)) {
                this.E = t.e(u8.k(i9, -1), this.E);
            } else {
                this.E = null;
            }
            int i10 = g.j.Y1;
            if (u8.s(i10)) {
                this.D = u8.c(i10);
            } else {
                this.D = null;
            }
            u8.w();
            this.f794h = false;
        }

        public void h() {
            this.f788b = 0;
            this.f789c = 0;
            this.f790d = 0;
            this.f791e = 0;
            this.f792f = true;
            this.f793g = true;
        }
    }

    static {
        Class<?>[] clsArr = {Context.class};
        f778e = clsArr;
        f779f = clsArr;
    }

    public g(Context context) {
        super(context);
        this.f782c = context;
        Object[] objArr = {context};
        this.f780a = objArr;
        this.f781b = objArr;
    }

    private Object a(Object obj) {
        return (!(obj instanceof Activity) && (obj instanceof ContextWrapper)) ? a(((ContextWrapper) obj).getBaseContext()) : obj;
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x003b, code lost:
        r8 = null;
        r6 = false;
        r7 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0040, code lost:
        if (r6 != false) goto L64;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0042, code lost:
        if (r15 == 1) goto L60;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0048, code lost:
        if (r15 == 2) goto L44;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x004b, code lost:
        if (r15 == 3) goto L14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x004f, code lost:
        r15 = r13.getName();
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0053, code lost:
        if (r7 == false) goto L21;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0059, code lost:
        if (r15.equals(r8) == false) goto L21;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x005b, code lost:
        r8 = null;
        r7 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0062, code lost:
        if (r15.equals("group") == false) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0064, code lost:
        r0.h();
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x006c, code lost:
        if (r15.equals("item") == false) goto L38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x0072, code lost:
        if (r0.d() != false) goto L37;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0074, code lost:
        r15 = r0.A;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x0076, code lost:
        if (r15 == null) goto L35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x007c, code lost:
        if (r15.a() == false) goto L35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x007e, code lost:
        r0.b();
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0082, code lost:
        r0.a();
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x008a, code lost:
        if (r15.equals("menu") == false) goto L42;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x008c, code lost:
        r6 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x008e, code lost:
        if (r7 == false) goto L45;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x0091, code lost:
        r15 = r13.getName();
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x0099, code lost:
        if (r15.equals("group") == false) goto L49;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x009b, code lost:
        r0.f(r14);
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00a3, code lost:
        if (r15.equals("item") == false) goto L53;
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x00a5, code lost:
        r0.g(r14);
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x00ad, code lost:
        if (r15.equals("menu") == false) goto L57;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x00af, code lost:
        c(r13, r14, r0.b());
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x00b7, code lost:
        r8 = r15;
        r7 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x00b9, code lost:
        r15 = r13.next();
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x00c5, code lost:
        throw new java.lang.RuntimeException("Unexpected end of document");
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x00c6, code lost:
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void c(org.xmlpull.v1.XmlPullParser r13, android.util.AttributeSet r14, android.view.Menu r15) {
        /*
            r12 = this;
            androidx.appcompat.view.g$b r0 = new androidx.appcompat.view.g$b
            r0.<init>(r15)
            int r15 = r13.getEventType()
        L9:
            r1 = 2
            java.lang.String r2 = "menu"
            r3 = 1
            if (r15 != r1) goto L35
            java.lang.String r15 = r13.getName()
            boolean r4 = r15.equals(r2)
            if (r4 == 0) goto L1e
            int r15 = r13.next()
            goto L3b
        L1e:
            java.lang.RuntimeException r13 = new java.lang.RuntimeException
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            java.lang.String r0 = "Expecting menu, got "
            r14.append(r0)
            r14.append(r15)
            java.lang.String r14 = r14.toString()
            r13.<init>(r14)
            throw r13
        L35:
            int r15 = r13.next()
            if (r15 != r3) goto L9
        L3b:
            r4 = 0
            r5 = 0
            r8 = r4
            r6 = r5
            r7 = r6
        L40:
            if (r6 != 0) goto Lc6
            if (r15 == r3) goto Lbe
            java.lang.String r9 = "item"
            java.lang.String r10 = "group"
            if (r15 == r1) goto L8e
            r11 = 3
            if (r15 == r11) goto L4f
            goto Lb9
        L4f:
            java.lang.String r15 = r13.getName()
            if (r7 == 0) goto L5e
            boolean r11 = r15.equals(r8)
            if (r11 == 0) goto L5e
            r8 = r4
            r7 = r5
            goto Lb9
        L5e:
            boolean r10 = r15.equals(r10)
            if (r10 == 0) goto L68
            r0.h()
            goto Lb9
        L68:
            boolean r9 = r15.equals(r9)
            if (r9 == 0) goto L86
            boolean r15 = r0.d()
            if (r15 != 0) goto Lb9
            androidx.core.view.b r15 = r0.A
            if (r15 == 0) goto L82
            boolean r15 = r15.a()
            if (r15 == 0) goto L82
            r0.b()
            goto Lb9
        L82:
            r0.a()
            goto Lb9
        L86:
            boolean r15 = r15.equals(r2)
            if (r15 == 0) goto Lb9
            r6 = r3
            goto Lb9
        L8e:
            if (r7 == 0) goto L91
            goto Lb9
        L91:
            java.lang.String r15 = r13.getName()
            boolean r10 = r15.equals(r10)
            if (r10 == 0) goto L9f
            r0.f(r14)
            goto Lb9
        L9f:
            boolean r9 = r15.equals(r9)
            if (r9 == 0) goto La9
            r0.g(r14)
            goto Lb9
        La9:
            boolean r9 = r15.equals(r2)
            if (r9 == 0) goto Lb7
            android.view.SubMenu r15 = r0.b()
            r12.c(r13, r14, r15)
            goto Lb9
        Lb7:
            r8 = r15
            r7 = r3
        Lb9:
            int r15 = r13.next()
            goto L40
        Lbe:
            java.lang.RuntimeException r13 = new java.lang.RuntimeException
            java.lang.String r14 = "Unexpected end of document"
            r13.<init>(r14)
            throw r13
        Lc6:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.view.g.c(org.xmlpull.v1.XmlPullParser, android.util.AttributeSet, android.view.Menu):void");
    }

    Object b() {
        if (this.f783d == null) {
            this.f783d = a(this.f782c);
        }
        return this.f783d;
    }

    @Override // android.view.MenuInflater
    public void inflate(int i8, Menu menu) {
        if (!(menu instanceof s0.a)) {
            super.inflate(i8, menu);
            return;
        }
        XmlResourceParser xmlResourceParser = null;
        try {
            try {
                try {
                    xmlResourceParser = this.f782c.getResources().getLayout(i8);
                    c(xmlResourceParser, Xml.asAttributeSet(xmlResourceParser), menu);
                } catch (XmlPullParserException e8) {
                    throw new InflateException("Error inflating menu XML", e8);
                }
            } catch (IOException e9) {
                throw new InflateException("Error inflating menu XML", e9);
            }
        } finally {
            if (xmlResourceParser != null) {
                xmlResourceParser.close();
            }
        }
    }
}
