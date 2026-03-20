package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.Xml;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import androidx.constraintlayout.motion.widget.MotionLayout;
import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class q {

    /* renamed from: a  reason: collision with root package name */
    private final MotionLayout f3427a;

    /* renamed from: n  reason: collision with root package name */
    private MotionEvent f3440n;

    /* renamed from: p  reason: collision with root package name */
    private MotionLayout.f f3442p;
    private boolean q;

    /* renamed from: r  reason: collision with root package name */
    float f3443r;

    /* renamed from: s  reason: collision with root package name */
    float f3444s;

    /* renamed from: b  reason: collision with root package name */
    androidx.constraintlayout.widget.f f3428b = null;

    /* renamed from: c  reason: collision with root package name */
    b f3429c = null;

    /* renamed from: d  reason: collision with root package name */
    private boolean f3430d = false;

    /* renamed from: e  reason: collision with root package name */
    private ArrayList<b> f3431e = new ArrayList<>();

    /* renamed from: f  reason: collision with root package name */
    private b f3432f = null;

    /* renamed from: g  reason: collision with root package name */
    private ArrayList<b> f3433g = new ArrayList<>();

    /* renamed from: h  reason: collision with root package name */
    private SparseArray<androidx.constraintlayout.widget.b> f3434h = new SparseArray<>();

    /* renamed from: i  reason: collision with root package name */
    private HashMap<String, Integer> f3435i = new HashMap<>();

    /* renamed from: j  reason: collision with root package name */
    private SparseIntArray f3436j = new SparseIntArray();

    /* renamed from: k  reason: collision with root package name */
    private boolean f3437k = false;

    /* renamed from: l  reason: collision with root package name */
    private int f3438l = 400;

    /* renamed from: m  reason: collision with root package name */
    private int f3439m = 0;

    /* renamed from: o  reason: collision with root package name */
    private boolean f3441o = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements Interpolator {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ l0.c f3445a;

        a(l0.c cVar) {
            this.f3445a = cVar;
        }

        @Override // android.animation.TimeInterpolator
        public float getInterpolation(float f5) {
            return (float) this.f3445a.a(f5);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b {

        /* renamed from: a  reason: collision with root package name */
        private int f3447a;

        /* renamed from: b  reason: collision with root package name */
        private boolean f3448b;

        /* renamed from: c  reason: collision with root package name */
        private int f3449c;

        /* renamed from: d  reason: collision with root package name */
        private int f3450d;

        /* renamed from: e  reason: collision with root package name */
        private int f3451e;

        /* renamed from: f  reason: collision with root package name */
        private String f3452f;

        /* renamed from: g  reason: collision with root package name */
        private int f3453g;

        /* renamed from: h  reason: collision with root package name */
        private int f3454h;

        /* renamed from: i  reason: collision with root package name */
        private float f3455i;

        /* renamed from: j  reason: collision with root package name */
        private final q f3456j;

        /* renamed from: k  reason: collision with root package name */
        private ArrayList<h> f3457k;

        /* renamed from: l  reason: collision with root package name */
        private t f3458l;

        /* renamed from: m  reason: collision with root package name */
        private ArrayList<a> f3459m;

        /* renamed from: n  reason: collision with root package name */
        private int f3460n;

        /* renamed from: o  reason: collision with root package name */
        private boolean f3461o;

        /* renamed from: p  reason: collision with root package name */
        private int f3462p;
        private int q;

        /* renamed from: r  reason: collision with root package name */
        private int f3463r;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static class a implements View.OnClickListener {

            /* renamed from: a  reason: collision with root package name */
            private final b f3464a;

            /* renamed from: b  reason: collision with root package name */
            int f3465b;

            /* renamed from: c  reason: collision with root package name */
            int f3466c;

            public a(Context context, b bVar, XmlPullParser xmlPullParser) {
                this.f3465b = -1;
                this.f3466c = 17;
                this.f3464a = bVar;
                TypedArray obtainStyledAttributes = context.obtainStyledAttributes(Xml.asAttributeSet(xmlPullParser), androidx.constraintlayout.widget.e.V6);
                int indexCount = obtainStyledAttributes.getIndexCount();
                for (int i8 = 0; i8 < indexCount; i8++) {
                    int index = obtainStyledAttributes.getIndex(i8);
                    if (index == androidx.constraintlayout.widget.e.X6) {
                        this.f3465b = obtainStyledAttributes.getResourceId(index, this.f3465b);
                    } else if (index == androidx.constraintlayout.widget.e.W6) {
                        this.f3466c = obtainStyledAttributes.getInt(index, this.f3466c);
                    }
                }
                obtainStyledAttributes.recycle();
            }

            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r7v4, types: [android.view.View] */
            public void a(MotionLayout motionLayout, int i8, b bVar) {
                int i9 = this.f3465b;
                MotionLayout motionLayout2 = motionLayout;
                if (i9 != -1) {
                    motionLayout2 = motionLayout.findViewById(i9);
                }
                if (motionLayout2 == null) {
                    Log.e("MotionScene", "OnClick could not find id " + this.f3465b);
                    return;
                }
                int i10 = bVar.f3450d;
                int i11 = bVar.f3449c;
                if (i10 == -1) {
                    motionLayout2.setOnClickListener(this);
                    return;
                }
                int i12 = this.f3466c;
                boolean z4 = false;
                boolean z8 = ((i12 & 1) != 0 && i8 == i10) | ((i12 & 1) != 0 && i8 == i10) | ((i12 & RecognitionOptions.QR_CODE) != 0 && i8 == i10) | ((i12 & 16) != 0 && i8 == i11);
                if ((i12 & RecognitionOptions.AZTEC) != 0 && i8 == i11) {
                    z4 = true;
                }
                if (z8 || z4) {
                    motionLayout2.setOnClickListener(this);
                }
            }

            boolean b(b bVar, MotionLayout motionLayout) {
                b bVar2 = this.f3464a;
                if (bVar2 == bVar) {
                    return true;
                }
                int i8 = bVar2.f3449c;
                int i9 = this.f3464a.f3450d;
                int i10 = motionLayout.K;
                return i9 == -1 ? i10 != i8 : i10 == i9 || i10 == i8;
            }

            public void c(MotionLayout motionLayout) {
                int i8 = this.f3465b;
                if (i8 == -1) {
                    return;
                }
                View findViewById = motionLayout.findViewById(i8);
                if (findViewById != null) {
                    findViewById.setOnClickListener(null);
                    return;
                }
                Log.e("MotionScene", " (*)  could not find id " + this.f3465b);
            }

            /* JADX WARN: Removed duplicated region for block: B:44:0x00a3  */
            /* JADX WARN: Removed duplicated region for block: B:61:0x00e6 A[ORIG_RETURN, RETURN] */
            @Override // android.view.View.OnClickListener
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public void onClick(android.view.View r8) {
                /*
                    Method dump skipped, instructions count: 231
                    To view this dump add '--comments-level debug' option
                */
                throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.motion.widget.q.b.a.onClick(android.view.View):void");
            }
        }

        b(q qVar, Context context, XmlPullParser xmlPullParser) {
            this.f3447a = -1;
            this.f3448b = false;
            this.f3449c = -1;
            this.f3450d = -1;
            this.f3451e = 0;
            this.f3452f = null;
            this.f3453g = -1;
            this.f3454h = 400;
            this.f3455i = 0.0f;
            this.f3457k = new ArrayList<>();
            this.f3458l = null;
            this.f3459m = new ArrayList<>();
            this.f3460n = 0;
            this.f3461o = false;
            this.f3462p = -1;
            this.q = 0;
            this.f3463r = 0;
            this.f3454h = qVar.f3438l;
            this.q = qVar.f3439m;
            this.f3456j = qVar;
            w(qVar, context, Xml.asAttributeSet(xmlPullParser));
        }

        b(q qVar, b bVar) {
            this.f3447a = -1;
            this.f3448b = false;
            this.f3449c = -1;
            this.f3450d = -1;
            this.f3451e = 0;
            this.f3452f = null;
            this.f3453g = -1;
            this.f3454h = 400;
            this.f3455i = 0.0f;
            this.f3457k = new ArrayList<>();
            this.f3458l = null;
            this.f3459m = new ArrayList<>();
            this.f3460n = 0;
            this.f3461o = false;
            this.f3462p = -1;
            this.q = 0;
            this.f3463r = 0;
            this.f3456j = qVar;
            if (bVar != null) {
                this.f3462p = bVar.f3462p;
                this.f3451e = bVar.f3451e;
                this.f3452f = bVar.f3452f;
                this.f3453g = bVar.f3453g;
                this.f3454h = bVar.f3454h;
                this.f3457k = bVar.f3457k;
                this.f3455i = bVar.f3455i;
                this.q = bVar.q;
            }
        }

        private void v(q qVar, Context context, TypedArray typedArray) {
            androidx.constraintlayout.widget.b bVar;
            SparseArray sparseArray;
            int i8;
            int indexCount = typedArray.getIndexCount();
            for (int i9 = 0; i9 < indexCount; i9++) {
                int index = typedArray.getIndex(i9);
                if (index == androidx.constraintlayout.widget.e.U7) {
                    this.f3449c = typedArray.getResourceId(index, this.f3449c);
                    if ("layout".equals(context.getResources().getResourceTypeName(this.f3449c))) {
                        bVar = new androidx.constraintlayout.widget.b();
                        bVar.w(context, this.f3449c);
                        sparseArray = qVar.f3434h;
                        i8 = this.f3449c;
                        sparseArray.append(i8, bVar);
                    }
                } else {
                    if (index == androidx.constraintlayout.widget.e.V7) {
                        this.f3450d = typedArray.getResourceId(index, this.f3450d);
                        if ("layout".equals(context.getResources().getResourceTypeName(this.f3450d))) {
                            bVar = new androidx.constraintlayout.widget.b();
                            bVar.w(context, this.f3450d);
                            sparseArray = qVar.f3434h;
                            i8 = this.f3450d;
                            sparseArray.append(i8, bVar);
                        }
                    } else if (index == androidx.constraintlayout.widget.e.Y7) {
                        int i10 = typedArray.peekValue(index).type;
                        if (i10 == 1) {
                            int resourceId = typedArray.getResourceId(index, -1);
                            this.f3453g = resourceId;
                            if (resourceId == -1) {
                            }
                            this.f3451e = -2;
                        } else if (i10 == 3) {
                            String string = typedArray.getString(index);
                            this.f3452f = string;
                            if (string.indexOf("/") > 0) {
                                this.f3453g = typedArray.getResourceId(index, -1);
                                this.f3451e = -2;
                            } else {
                                this.f3451e = -1;
                            }
                        } else {
                            this.f3451e = typedArray.getInteger(index, this.f3451e);
                        }
                    } else if (index == androidx.constraintlayout.widget.e.W7) {
                        this.f3454h = typedArray.getInt(index, this.f3454h);
                    } else if (index == androidx.constraintlayout.widget.e.f4124a8) {
                        this.f3455i = typedArray.getFloat(index, this.f3455i);
                    } else if (index == androidx.constraintlayout.widget.e.T7) {
                        this.f3460n = typedArray.getInteger(index, this.f3460n);
                    } else if (index == androidx.constraintlayout.widget.e.S7) {
                        this.f3447a = typedArray.getResourceId(index, this.f3447a);
                    } else if (index == androidx.constraintlayout.widget.e.f4134b8) {
                        this.f3461o = typedArray.getBoolean(index, this.f3461o);
                    } else if (index == androidx.constraintlayout.widget.e.Z7) {
                        this.f3462p = typedArray.getInteger(index, -1);
                    } else if (index == androidx.constraintlayout.widget.e.X7) {
                        this.q = typedArray.getInteger(index, 0);
                    } else if (index == androidx.constraintlayout.widget.e.f4144c8) {
                        this.f3463r = typedArray.getInteger(index, 0);
                    }
                }
            }
            if (this.f3450d == -1) {
                this.f3448b = true;
            }
        }

        private void w(q qVar, Context context, AttributeSet attributeSet) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, androidx.constraintlayout.widget.e.R7);
            v(qVar, context, obtainStyledAttributes);
            obtainStyledAttributes.recycle();
        }

        public int A() {
            return this.f3450d;
        }

        public t B() {
            return this.f3458l;
        }

        public boolean C() {
            return !this.f3461o;
        }

        public boolean D(int i8) {
            return (i8 & this.f3463r) != 0;
        }

        public void E(int i8) {
            this.f3454h = i8;
        }

        public void t(Context context, XmlPullParser xmlPullParser) {
            this.f3459m.add(new a(context, this, xmlPullParser));
        }

        public String u(Context context) {
            String resourceEntryName = this.f3450d == -1 ? "null" : context.getResources().getResourceEntryName(this.f3450d);
            if (this.f3449c == -1) {
                return resourceEntryName + " -> null";
            }
            return resourceEntryName + " -> " + context.getResources().getResourceEntryName(this.f3449c);
        }

        public int x() {
            return this.f3454h;
        }

        public int y() {
            return this.f3449c;
        }

        public int z() {
            return this.q;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public q(Context context, MotionLayout motionLayout, int i8) {
        this.f3427a = motionLayout;
        C(context, i8);
        SparseArray<androidx.constraintlayout.widget.b> sparseArray = this.f3434h;
        int i9 = androidx.constraintlayout.widget.d.f4114a;
        sparseArray.put(i9, new androidx.constraintlayout.widget.b());
        this.f3435i.put("motion_base", Integer.valueOf(i9));
    }

    private boolean A(int i8) {
        int i9 = this.f3436j.get(i8);
        int size = this.f3436j.size();
        while (i9 > 0) {
            if (i9 == i8) {
                return true;
            }
            int i10 = size - 1;
            if (size < 0) {
                return true;
            }
            i9 = this.f3436j.get(i9);
            size = i10;
        }
        return false;
    }

    private boolean B() {
        return this.f3442p != null;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private void C(Context context, int i8) {
        XmlResourceParser xml = context.getResources().getXml(i8);
        b bVar = null;
        try {
            int eventType = xml.getEventType();
            while (true) {
                char c9 = 1;
                if (eventType == 1) {
                    return;
                }
                if (eventType == 0) {
                    xml.getName();
                    continue;
                } else if (eventType != 2) {
                    continue;
                } else {
                    String name = xml.getName();
                    if (this.f3437k) {
                        System.out.println("parsing = " + name);
                    }
                    switch (name.hashCode()) {
                        case -1349929691:
                            if (name.equals("ConstraintSet")) {
                                c9 = 5;
                                break;
                            }
                            c9 = 65535;
                            break;
                        case -1239391468:
                            if (name.equals("KeyFrameSet")) {
                                c9 = 6;
                                break;
                            }
                            c9 = 65535;
                            break;
                        case 269306229:
                            if (name.equals("Transition")) {
                                break;
                            }
                            c9 = 65535;
                            break;
                        case 312750793:
                            if (name.equals("OnClick")) {
                                c9 = 3;
                                break;
                            }
                            c9 = 65535;
                            break;
                        case 327855227:
                            if (name.equals("OnSwipe")) {
                                c9 = 2;
                                break;
                            }
                            c9 = 65535;
                            break;
                        case 793277014:
                            if (name.equals("MotionScene")) {
                                c9 = 0;
                                break;
                            }
                            c9 = 65535;
                            break;
                        case 1382829617:
                            if (name.equals("StateSet")) {
                                c9 = 4;
                                break;
                            }
                            c9 = 65535;
                            break;
                        default:
                            c9 = 65535;
                            break;
                    }
                    switch (c9) {
                        case 0:
                            E(context, xml);
                            continue;
                        case 1:
                            ArrayList<b> arrayList = this.f3431e;
                            b bVar2 = new b(this, context, xml);
                            arrayList.add(bVar2);
                            if (this.f3429c == null && !bVar2.f3448b) {
                                this.f3429c = bVar2;
                                if (bVar2.f3458l != null) {
                                    this.f3429c.f3458l.p(this.q);
                                }
                            }
                            if (bVar2.f3448b) {
                                if (bVar2.f3449c == -1) {
                                    this.f3432f = bVar2;
                                } else {
                                    this.f3433g.add(bVar2);
                                }
                                this.f3431e.remove(bVar2);
                            }
                            bVar = bVar2;
                            continue;
                        case 2:
                            if (bVar == null) {
                                Log.v("MotionScene", " OnSwipe (" + context.getResources().getResourceEntryName(i8) + ".xml:" + xml.getLineNumber() + ")");
                            }
                            bVar.f3458l = new t(context, this.f3427a, xml);
                            continue;
                        case 3:
                            bVar.t(context, xml);
                            continue;
                        case 4:
                            this.f3428b = new androidx.constraintlayout.widget.f(context, xml);
                            continue;
                        case 5:
                            D(context, xml);
                            continue;
                        case 6:
                            bVar.f3457k.add(new h(context, xml));
                            continue;
                        default:
                            Log.v("MotionScene", "WARNING UNKNOWN ATTRIBUTE " + name);
                            continue;
                    }
                }
                eventType = xml.next();
            }
        } catch (IOException e8) {
            e8.printStackTrace();
        } catch (XmlPullParserException e9) {
            e9.printStackTrace();
        }
    }

    private void D(Context context, XmlPullParser xmlPullParser) {
        androidx.constraintlayout.widget.b bVar = new androidx.constraintlayout.widget.b();
        bVar.C(false);
        int attributeCount = xmlPullParser.getAttributeCount();
        int i8 = -1;
        int i9 = -1;
        for (int i10 = 0; i10 < attributeCount; i10++) {
            String attributeName = xmlPullParser.getAttributeName(i10);
            String attributeValue = xmlPullParser.getAttributeValue(i10);
            if (this.f3437k) {
                System.out.println("id string = " + attributeValue);
            }
            attributeName.hashCode();
            if (attributeName.equals("deriveConstraintsFrom")) {
                i9 = o(context, attributeValue);
            } else if (attributeName.equals("id")) {
                i8 = o(context, attributeValue);
                this.f3435i.put(P(attributeValue), Integer.valueOf(i8));
            }
        }
        if (i8 != -1) {
            if (this.f3427a.f3174k0 != 0) {
                bVar.D(true);
            }
            bVar.x(context, xmlPullParser);
            if (i9 != -1) {
                this.f3436j.put(i8, i9);
            }
            this.f3434h.put(i8, bVar);
        }
    }

    private void E(Context context, XmlPullParser xmlPullParser) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(Xml.asAttributeSet(xmlPullParser), androidx.constraintlayout.widget.e.O6);
        int indexCount = obtainStyledAttributes.getIndexCount();
        for (int i8 = 0; i8 < indexCount; i8++) {
            int index = obtainStyledAttributes.getIndex(i8);
            if (index == androidx.constraintlayout.widget.e.P6) {
                this.f3438l = obtainStyledAttributes.getInt(index, this.f3438l);
            } else if (index == androidx.constraintlayout.widget.e.Q6) {
                this.f3439m = obtainStyledAttributes.getInteger(index, 0);
            }
        }
        obtainStyledAttributes.recycle();
    }

    private void I(int i8) {
        int i9 = this.f3436j.get(i8);
        if (i9 > 0) {
            I(this.f3436j.get(i8));
            androidx.constraintlayout.widget.b bVar = this.f3434h.get(i8);
            androidx.constraintlayout.widget.b bVar2 = this.f3434h.get(i9);
            if (bVar2 != null) {
                bVar.B(bVar2);
                this.f3436j.put(i8, -1);
                return;
            }
            Log.e("MotionScene", "ERROR! invalid deriveConstraintsFrom: @id/" + androidx.constraintlayout.motion.widget.a.b(this.f3427a.getContext(), i9));
        }
    }

    public static String P(String str) {
        if (str == null) {
            return BuildConfig.FLAVOR;
        }
        int indexOf = str.indexOf(47);
        return indexOf < 0 ? str : str.substring(indexOf + 1);
    }

    private int o(Context context, String str) {
        int i8;
        if (str.contains("/")) {
            i8 = context.getResources().getIdentifier(str.substring(str.indexOf(47) + 1), "id", context.getPackageName());
            if (this.f3437k) {
                System.out.println("id getMap res = " + i8);
            }
        } else {
            i8 = -1;
        }
        if (i8 == -1) {
            if (str.length() > 1) {
                return Integer.parseInt(str.substring(1));
            }
            Log.e("MotionScene", "error in parsing id");
            return i8;
        }
        return i8;
    }

    private int v(int i8) {
        int c9;
        androidx.constraintlayout.widget.f fVar = this.f3428b;
        return (fVar == null || (c9 = fVar.c(i8, -1, -1)) == -1) ? i8 : c9;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void F(float f5, float f8) {
        b bVar = this.f3429c;
        if (bVar == null || bVar.f3458l == null) {
            return;
        }
        this.f3429c.f3458l.m(f5, f8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void G(float f5, float f8) {
        b bVar = this.f3429c;
        if (bVar == null || bVar.f3458l == null) {
            return;
        }
        this.f3429c.f3458l.n(f5, f8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void H(MotionEvent motionEvent, int i8, MotionLayout motionLayout) {
        MotionLayout.f fVar;
        MotionEvent motionEvent2;
        RectF rectF = new RectF();
        if (this.f3442p == null) {
            this.f3442p = this.f3427a.k0();
        }
        this.f3442p.b(motionEvent);
        if (i8 != -1) {
            int action = motionEvent.getAction();
            boolean z4 = false;
            if (action == 0) {
                this.f3443r = motionEvent.getRawX();
                this.f3444s = motionEvent.getRawY();
                this.f3440n = motionEvent;
                if (this.f3429c.f3458l != null) {
                    RectF e8 = this.f3429c.f3458l.e(this.f3427a, rectF);
                    if (e8 != null && !e8.contains(this.f3440n.getX(), this.f3440n.getY())) {
                        this.f3440n = null;
                        return;
                    }
                    RectF j8 = this.f3429c.f3458l.j(this.f3427a, rectF);
                    if (j8 == null || j8.contains(this.f3440n.getX(), this.f3440n.getY())) {
                        this.f3441o = false;
                    } else {
                        this.f3441o = true;
                    }
                    this.f3429c.f3458l.o(this.f3443r, this.f3444s);
                    return;
                }
                return;
            } else if (action == 2) {
                float rawY = motionEvent.getRawY() - this.f3444s;
                float rawX = motionEvent.getRawX() - this.f3443r;
                if ((rawX == 0.0d && rawY == 0.0d) || (motionEvent2 = this.f3440n) == null) {
                    return;
                }
                b g8 = g(i8, rawX, rawY, motionEvent2);
                if (g8 != null) {
                    motionLayout.setTransition(g8);
                    RectF j9 = this.f3429c.f3458l.j(this.f3427a, rectF);
                    if (j9 != null && !j9.contains(this.f3440n.getX(), this.f3440n.getY())) {
                        z4 = true;
                    }
                    this.f3441o = z4;
                    this.f3429c.f3458l.q(this.f3443r, this.f3444s);
                }
            }
        }
        b bVar = this.f3429c;
        if (bVar != null && bVar.f3458l != null && !this.f3441o) {
            this.f3429c.f3458l.l(motionEvent, this.f3442p, i8, this);
        }
        this.f3443r = motionEvent.getRawX();
        this.f3444s = motionEvent.getRawY();
        if (motionEvent.getAction() != 1 || (fVar = this.f3442p) == null) {
            return;
        }
        fVar.a();
        this.f3442p = null;
        int i9 = motionLayout.K;
        if (i9 != -1) {
            f(motionLayout, i9);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void J(MotionLayout motionLayout) {
        for (int i8 = 0; i8 < this.f3434h.size(); i8++) {
            int keyAt = this.f3434h.keyAt(i8);
            if (A(keyAt)) {
                Log.e("MotionScene", "Cannot be derived from yourself");
                return;
            }
            I(keyAt);
        }
        for (int i9 = 0; i9 < this.f3434h.size(); i9++) {
            this.f3434h.valueAt(i9).A(motionLayout);
        }
    }

    public void K(int i8) {
        b bVar = this.f3429c;
        if (bVar != null) {
            bVar.E(i8);
        } else {
            this.f3438l = i8;
        }
    }

    public void L(boolean z4) {
        this.q = z4;
        b bVar = this.f3429c;
        if (bVar == null || bVar.f3458l == null) {
            return;
        }
        this.f3429c.f3458l.p(this.q);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0013, code lost:
        if (r2 != (-1)) goto L9;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void M(int r7, int r8) {
        /*
            r6 = this;
            androidx.constraintlayout.widget.f r0 = r6.f3428b
            r1 = -1
            if (r0 == 0) goto L16
            int r0 = r0.c(r7, r1, r1)
            if (r0 == r1) goto Lc
            goto Ld
        Lc:
            r0 = r7
        Ld:
            androidx.constraintlayout.widget.f r2 = r6.f3428b
            int r2 = r2.c(r8, r1, r1)
            if (r2 == r1) goto L17
            goto L18
        L16:
            r0 = r7
        L17:
            r2 = r8
        L18:
            java.util.ArrayList<androidx.constraintlayout.motion.widget.q$b> r3 = r6.f3431e
            java.util.Iterator r3 = r3.iterator()
        L1e:
            boolean r4 = r3.hasNext()
            if (r4 == 0) goto L58
            java.lang.Object r4 = r3.next()
            androidx.constraintlayout.motion.widget.q$b r4 = (androidx.constraintlayout.motion.widget.q.b) r4
            int r5 = androidx.constraintlayout.motion.widget.q.b.a(r4)
            if (r5 != r2) goto L36
            int r5 = androidx.constraintlayout.motion.widget.q.b.c(r4)
            if (r5 == r0) goto L42
        L36:
            int r5 = androidx.constraintlayout.motion.widget.q.b.a(r4)
            if (r5 != r8) goto L1e
            int r5 = androidx.constraintlayout.motion.widget.q.b.c(r4)
            if (r5 != r7) goto L1e
        L42:
            r6.f3429c = r4
            if (r4 == 0) goto L57
            androidx.constraintlayout.motion.widget.t r7 = androidx.constraintlayout.motion.widget.q.b.m(r4)
            if (r7 == 0) goto L57
            androidx.constraintlayout.motion.widget.q$b r7 = r6.f3429c
            androidx.constraintlayout.motion.widget.t r7 = androidx.constraintlayout.motion.widget.q.b.m(r7)
            boolean r8 = r6.q
            r7.p(r8)
        L57:
            return
        L58:
            androidx.constraintlayout.motion.widget.q$b r7 = r6.f3432f
            java.util.ArrayList<androidx.constraintlayout.motion.widget.q$b> r3 = r6.f3433g
            java.util.Iterator r3 = r3.iterator()
        L60:
            boolean r4 = r3.hasNext()
            if (r4 == 0) goto L74
            java.lang.Object r4 = r3.next()
            androidx.constraintlayout.motion.widget.q$b r4 = (androidx.constraintlayout.motion.widget.q.b) r4
            int r5 = androidx.constraintlayout.motion.widget.q.b.a(r4)
            if (r5 != r8) goto L60
            r7 = r4
            goto L60
        L74:
            androidx.constraintlayout.motion.widget.q$b r8 = new androidx.constraintlayout.motion.widget.q$b
            r8.<init>(r6, r7)
            androidx.constraintlayout.motion.widget.q.b.d(r8, r0)
            androidx.constraintlayout.motion.widget.q.b.b(r8, r2)
            if (r0 == r1) goto L86
            java.util.ArrayList<androidx.constraintlayout.motion.widget.q$b> r7 = r6.f3431e
            r7.add(r8)
        L86:
            r6.f3429c = r8
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.motion.widget.q.M(int, int):void");
    }

    public void N(b bVar) {
        this.f3429c = bVar;
        if (bVar == null || bVar.f3458l == null) {
            return;
        }
        this.f3429c.f3458l.p(this.q);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void O() {
        b bVar = this.f3429c;
        if (bVar == null || bVar.f3458l == null) {
            return;
        }
        this.f3429c.f3458l.r();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean Q() {
        Iterator<b> it = this.f3431e.iterator();
        while (it.hasNext()) {
            if (it.next().f3458l != null) {
                return true;
            }
        }
        b bVar = this.f3429c;
        return (bVar == null || bVar.f3458l == null) ? false : true;
    }

    public void e(MotionLayout motionLayout, int i8) {
        Iterator<b> it = this.f3431e.iterator();
        while (it.hasNext()) {
            b next = it.next();
            if (next.f3459m.size() > 0) {
                Iterator it2 = next.f3459m.iterator();
                while (it2.hasNext()) {
                    ((b.a) it2.next()).c(motionLayout);
                }
            }
        }
        Iterator<b> it3 = this.f3433g.iterator();
        while (it3.hasNext()) {
            b next2 = it3.next();
            if (next2.f3459m.size() > 0) {
                Iterator it4 = next2.f3459m.iterator();
                while (it4.hasNext()) {
                    ((b.a) it4.next()).c(motionLayout);
                }
            }
        }
        Iterator<b> it5 = this.f3431e.iterator();
        while (it5.hasNext()) {
            b next3 = it5.next();
            if (next3.f3459m.size() > 0) {
                Iterator it6 = next3.f3459m.iterator();
                while (it6.hasNext()) {
                    ((b.a) it6.next()).a(motionLayout, i8, next3);
                }
            }
        }
        Iterator<b> it7 = this.f3433g.iterator();
        while (it7.hasNext()) {
            b next4 = it7.next();
            if (next4.f3459m.size() > 0) {
                Iterator it8 = next4.f3459m.iterator();
                while (it8.hasNext()) {
                    ((b.a) it8.next()).a(motionLayout, i8, next4);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean f(MotionLayout motionLayout, int i8) {
        if (B() || this.f3430d) {
            return false;
        }
        Iterator<b> it = this.f3431e.iterator();
        while (it.hasNext()) {
            b next = it.next();
            if (next.f3460n != 0) {
                if (i8 == next.f3450d && (next.f3460n == 4 || next.f3460n == 2)) {
                    MotionLayout.j jVar = MotionLayout.j.FINISHED;
                    motionLayout.setState(jVar);
                    motionLayout.setTransition(next);
                    if (next.f3460n == 4) {
                        motionLayout.t0();
                        motionLayout.setState(MotionLayout.j.SETUP);
                        jVar = MotionLayout.j.MOVING;
                    } else {
                        motionLayout.setProgress(1.0f);
                        motionLayout.Z(true);
                        motionLayout.setState(MotionLayout.j.SETUP);
                        motionLayout.setState(MotionLayout.j.MOVING);
                    }
                    motionLayout.setState(jVar);
                    return true;
                } else if (i8 == next.f3449c && (next.f3460n == 3 || next.f3460n == 1)) {
                    MotionLayout.j jVar2 = MotionLayout.j.FINISHED;
                    motionLayout.setState(jVar2);
                    motionLayout.setTransition(next);
                    if (next.f3460n == 3) {
                        motionLayout.u0();
                        motionLayout.setState(MotionLayout.j.SETUP);
                        jVar2 = MotionLayout.j.MOVING;
                    } else {
                        motionLayout.setProgress(0.0f);
                        motionLayout.Z(true);
                        motionLayout.setState(MotionLayout.j.SETUP);
                        motionLayout.setState(MotionLayout.j.MOVING);
                    }
                    motionLayout.setState(jVar2);
                    return true;
                }
            }
        }
        return false;
    }

    public b g(int i8, float f5, float f8, MotionEvent motionEvent) {
        if (i8 != -1) {
            List<b> z4 = z(i8);
            float f9 = 0.0f;
            b bVar = null;
            RectF rectF = new RectF();
            for (b bVar2 : z4) {
                if (!bVar2.f3461o && bVar2.f3458l != null) {
                    bVar2.f3458l.p(this.q);
                    RectF j8 = bVar2.f3458l.j(this.f3427a, rectF);
                    if (j8 == null || motionEvent == null || j8.contains(motionEvent.getX(), motionEvent.getY())) {
                        RectF j9 = bVar2.f3458l.j(this.f3427a, rectF);
                        if (j9 == null || motionEvent == null || j9.contains(motionEvent.getX(), motionEvent.getY())) {
                            float a9 = bVar2.f3458l.a(f5, f8) * (bVar2.f3449c == i8 ? -1.0f : 1.1f);
                            if (a9 > f9) {
                                bVar = bVar2;
                                f9 = a9;
                            }
                        }
                    }
                }
            }
            return bVar;
        }
        return this.f3429c;
    }

    public int h() {
        b bVar = this.f3429c;
        if (bVar != null) {
            return bVar.f3462p;
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public androidx.constraintlayout.widget.b i(int i8) {
        return j(i8, -1, -1);
    }

    androidx.constraintlayout.widget.b j(int i8, int i9, int i10) {
        androidx.constraintlayout.widget.b bVar;
        int c9;
        if (this.f3437k) {
            PrintStream printStream = System.out;
            printStream.println("id " + i8);
            PrintStream printStream2 = System.out;
            printStream2.println("size " + this.f3434h.size());
        }
        androidx.constraintlayout.widget.f fVar = this.f3428b;
        if (fVar != null && (c9 = fVar.c(i8, i9, i10)) != -1) {
            i8 = c9;
        }
        if (this.f3434h.get(i8) == null) {
            Log.e("MotionScene", "Warning could not find ConstraintSet id/" + androidx.constraintlayout.motion.widget.a.b(this.f3427a.getContext(), i8) + " In MotionScene");
            SparseArray<androidx.constraintlayout.widget.b> sparseArray = this.f3434h;
            bVar = sparseArray.get(sparseArray.keyAt(0));
        } else {
            bVar = this.f3434h.get(i8);
        }
        return bVar;
    }

    public int[] k() {
        int size = this.f3434h.size();
        int[] iArr = new int[size];
        for (int i8 = 0; i8 < size; i8++) {
            iArr[i8] = this.f3434h.keyAt(i8);
        }
        return iArr;
    }

    public ArrayList<b> l() {
        return this.f3431e;
    }

    public int m() {
        b bVar = this.f3429c;
        return bVar != null ? bVar.f3454h : this.f3438l;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int n() {
        b bVar = this.f3429c;
        if (bVar == null) {
            return -1;
        }
        return bVar.f3449c;
    }

    public Interpolator p() {
        int i8 = this.f3429c.f3451e;
        if (i8 != -2) {
            if (i8 != -1) {
                if (i8 != 0) {
                    if (i8 != 1) {
                        if (i8 != 2) {
                            if (i8 != 4) {
                                if (i8 != 5) {
                                    return null;
                                }
                                return new BounceInterpolator();
                            }
                            return new AnticipateInterpolator();
                        }
                        return new DecelerateInterpolator();
                    }
                    return new AccelerateInterpolator();
                }
                return new AccelerateDecelerateInterpolator();
            }
            return new a(l0.c.c(this.f3429c.f3452f));
        }
        return AnimationUtils.loadInterpolator(this.f3427a.getContext(), this.f3429c.f3453g);
    }

    public void q(n nVar) {
        b bVar = this.f3429c;
        if (bVar != null) {
            Iterator it = bVar.f3457k.iterator();
            while (it.hasNext()) {
                ((h) it.next()).a(nVar);
            }
            return;
        }
        b bVar2 = this.f3432f;
        if (bVar2 != null) {
            Iterator it2 = bVar2.f3457k.iterator();
            while (it2.hasNext()) {
                ((h) it2.next()).a(nVar);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float r() {
        b bVar = this.f3429c;
        if (bVar == null || bVar.f3458l == null) {
            return 0.0f;
        }
        return this.f3429c.f3458l.f();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float s() {
        b bVar = this.f3429c;
        if (bVar == null || bVar.f3458l == null) {
            return 0.0f;
        }
        return this.f3429c.f3458l.g();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean t() {
        b bVar = this.f3429c;
        if (bVar == null || bVar.f3458l == null) {
            return false;
        }
        return this.f3429c.f3458l.h();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float u(float f5, float f8) {
        b bVar = this.f3429c;
        if (bVar == null || bVar.f3458l == null) {
            return 0.0f;
        }
        return this.f3429c.f3458l.i(f5, f8);
    }

    public float w() {
        b bVar = this.f3429c;
        if (bVar != null) {
            return bVar.f3455i;
        }
        return 0.0f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int x() {
        b bVar = this.f3429c;
        if (bVar == null) {
            return -1;
        }
        return bVar.f3450d;
    }

    public b y(int i8) {
        Iterator<b> it = this.f3431e.iterator();
        while (it.hasNext()) {
            b next = it.next();
            if (next.f3447a == i8) {
                return next;
            }
        }
        return null;
    }

    public List<b> z(int i8) {
        int v8 = v(i8);
        ArrayList arrayList = new ArrayList();
        Iterator<b> it = this.f3431e.iterator();
        while (it.hasNext()) {
            b next = it.next();
            if (next.f3450d == v8 || next.f3449c == v8) {
                arrayList.add(next);
            }
        }
        return arrayList;
    }
}
