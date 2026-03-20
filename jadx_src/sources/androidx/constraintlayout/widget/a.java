package androidx.constraintlayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.util.Log;
import android.util.SparseArray;
import android.util.Xml;
import java.io.IOException;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a {

    /* renamed from: a  reason: collision with root package name */
    private final ConstraintLayout f4023a;

    /* renamed from: b  reason: collision with root package name */
    androidx.constraintlayout.widget.b f4024b;

    /* renamed from: c  reason: collision with root package name */
    int f4025c = -1;

    /* renamed from: d  reason: collision with root package name */
    int f4026d = -1;

    /* renamed from: e  reason: collision with root package name */
    private SparseArray<C0026a> f4027e = new SparseArray<>();

    /* renamed from: f  reason: collision with root package name */
    private SparseArray<androidx.constraintlayout.widget.b> f4028f = new SparseArray<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: androidx.constraintlayout.widget.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class C0026a {

        /* renamed from: a  reason: collision with root package name */
        int f4029a;

        /* renamed from: b  reason: collision with root package name */
        ArrayList<b> f4030b = new ArrayList<>();

        /* renamed from: c  reason: collision with root package name */
        int f4031c;

        /* renamed from: d  reason: collision with root package name */
        androidx.constraintlayout.widget.b f4032d;

        public C0026a(Context context, XmlPullParser xmlPullParser) {
            this.f4031c = -1;
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(Xml.asAttributeSet(xmlPullParser), e.f4311v7);
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i8 = 0; i8 < indexCount; i8++) {
                int index = obtainStyledAttributes.getIndex(i8);
                if (index == e.f4320w7) {
                    this.f4029a = obtainStyledAttributes.getResourceId(index, this.f4029a);
                } else if (index == e.f4329x7) {
                    this.f4031c = obtainStyledAttributes.getResourceId(index, this.f4031c);
                    String resourceTypeName = context.getResources().getResourceTypeName(this.f4031c);
                    context.getResources().getResourceName(this.f4031c);
                    if ("layout".equals(resourceTypeName)) {
                        androidx.constraintlayout.widget.b bVar = new androidx.constraintlayout.widget.b();
                        this.f4032d = bVar;
                        bVar.i(context, this.f4031c);
                    }
                }
            }
            obtainStyledAttributes.recycle();
        }

        void a(b bVar) {
            this.f4030b.add(bVar);
        }

        public int b(float f5, float f8) {
            for (int i8 = 0; i8 < this.f4030b.size(); i8++) {
                if (this.f4030b.get(i8).a(f5, f8)) {
                    return i8;
                }
            }
            return -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b {

        /* renamed from: a  reason: collision with root package name */
        float f4033a;

        /* renamed from: b  reason: collision with root package name */
        float f4034b;

        /* renamed from: c  reason: collision with root package name */
        float f4035c;

        /* renamed from: d  reason: collision with root package name */
        float f4036d;

        /* renamed from: e  reason: collision with root package name */
        int f4037e;

        /* renamed from: f  reason: collision with root package name */
        androidx.constraintlayout.widget.b f4038f;

        public b(Context context, XmlPullParser xmlPullParser) {
            this.f4033a = Float.NaN;
            this.f4034b = Float.NaN;
            this.f4035c = Float.NaN;
            this.f4036d = Float.NaN;
            this.f4037e = -1;
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(Xml.asAttributeSet(xmlPullParser), e.d8);
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i8 = 0; i8 < indexCount; i8++) {
                int index = obtainStyledAttributes.getIndex(i8);
                if (index == e.e8) {
                    this.f4037e = obtainStyledAttributes.getResourceId(index, this.f4037e);
                    String resourceTypeName = context.getResources().getResourceTypeName(this.f4037e);
                    context.getResources().getResourceName(this.f4037e);
                    if ("layout".equals(resourceTypeName)) {
                        androidx.constraintlayout.widget.b bVar = new androidx.constraintlayout.widget.b();
                        this.f4038f = bVar;
                        bVar.i(context, this.f4037e);
                    }
                } else if (index == e.f8) {
                    this.f4036d = obtainStyledAttributes.getDimension(index, this.f4036d);
                } else if (index == e.g8) {
                    this.f4034b = obtainStyledAttributes.getDimension(index, this.f4034b);
                } else if (index == e.h8) {
                    this.f4035c = obtainStyledAttributes.getDimension(index, this.f4035c);
                } else if (index == e.i8) {
                    this.f4033a = obtainStyledAttributes.getDimension(index, this.f4033a);
                } else {
                    Log.v("ConstraintLayoutStates", "Unknown tag");
                }
            }
            obtainStyledAttributes.recycle();
        }

        boolean a(float f5, float f8) {
            if (Float.isNaN(this.f4033a) || f5 >= this.f4033a) {
                if (Float.isNaN(this.f4034b) || f8 >= this.f4034b) {
                    if (Float.isNaN(this.f4035c) || f5 <= this.f4035c) {
                        return Float.isNaN(this.f4036d) || f8 <= this.f4036d;
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public a(Context context, ConstraintLayout constraintLayout, int i8) {
        this.f4023a = constraintLayout;
        a(context, i8);
    }

    private void a(Context context, int i8) {
        XmlResourceParser xml = context.getResources().getXml(i8);
        C0026a c0026a = null;
        try {
            for (int eventType = xml.getEventType(); eventType != 1; eventType = xml.next()) {
                if (eventType == 0) {
                    xml.getName();
                    continue;
                } else if (eventType != 2) {
                    continue;
                } else {
                    String name = xml.getName();
                    char c9 = 65535;
                    switch (name.hashCode()) {
                        case -1349929691:
                            if (name.equals("ConstraintSet")) {
                                c9 = 4;
                                break;
                            }
                            break;
                        case 80204913:
                            if (name.equals("State")) {
                                c9 = 2;
                                break;
                            }
                            break;
                        case 1382829617:
                            if (name.equals("StateSet")) {
                                c9 = 1;
                                break;
                            }
                            break;
                        case 1657696882:
                            if (name.equals("layoutDescription")) {
                                c9 = 0;
                                break;
                            }
                            break;
                        case 1901439077:
                            if (name.equals("Variant")) {
                                c9 = 3;
                                break;
                            }
                            break;
                    }
                    if (c9 != 0 && c9 != 1) {
                        if (c9 == 2) {
                            c0026a = new C0026a(context, xml);
                            this.f4027e.put(c0026a.f4029a, c0026a);
                            continue;
                        } else if (c9 == 3) {
                            b bVar = new b(context, xml);
                            if (c0026a != null) {
                                c0026a.a(bVar);
                                continue;
                            } else {
                                continue;
                            }
                        } else if (c9 != 4) {
                            Log.v("ConstraintLayoutStates", "unknown tag " + name);
                            continue;
                        } else {
                            b(context, xml);
                            continue;
                        }
                    }
                }
            }
        } catch (IOException e8) {
            e8.printStackTrace();
        } catch (XmlPullParserException e9) {
            e9.printStackTrace();
        }
    }

    private void b(Context context, XmlPullParser xmlPullParser) {
        androidx.constraintlayout.widget.b bVar = new androidx.constraintlayout.widget.b();
        int attributeCount = xmlPullParser.getAttributeCount();
        for (int i8 = 0; i8 < attributeCount; i8++) {
            if ("id".equals(xmlPullParser.getAttributeName(i8))) {
                String attributeValue = xmlPullParser.getAttributeValue(i8);
                int identifier = attributeValue.contains("/") ? context.getResources().getIdentifier(attributeValue.substring(attributeValue.indexOf(47) + 1), "id", context.getPackageName()) : -1;
                if (identifier == -1) {
                    if (attributeValue.length() > 1) {
                        identifier = Integer.parseInt(attributeValue.substring(1));
                    } else {
                        Log.e("ConstraintLayoutStates", "error in parsing id");
                    }
                }
                bVar.x(context, xmlPullParser);
                this.f4028f.put(identifier, bVar);
                return;
            }
        }
    }

    public void c(c cVar) {
    }

    public void d(int i8, float f5, float f8) {
        int b9;
        int i9 = this.f4025c;
        if (i9 == i8) {
            C0026a valueAt = i8 == -1 ? this.f4027e.valueAt(0) : this.f4027e.get(i9);
            int i10 = this.f4026d;
            if ((i10 == -1 || !valueAt.f4030b.get(i10).a(f5, f8)) && this.f4026d != (b9 = valueAt.b(f5, f8))) {
                androidx.constraintlayout.widget.b bVar = b9 == -1 ? this.f4024b : valueAt.f4030b.get(b9).f4038f;
                if (b9 != -1) {
                    int i11 = valueAt.f4030b.get(b9).f4037e;
                }
                if (bVar == null) {
                    return;
                }
                this.f4026d = b9;
                bVar.d(this.f4023a);
                return;
            }
            return;
        }
        this.f4025c = i8;
        C0026a c0026a = this.f4027e.get(i8);
        int b10 = c0026a.b(f5, f8);
        androidx.constraintlayout.widget.b bVar2 = b10 == -1 ? c0026a.f4032d : c0026a.f4030b.get(b10).f4038f;
        if (b10 != -1) {
            int i12 = c0026a.f4030b.get(b10).f4037e;
        }
        if (bVar2 != null) {
            this.f4026d = b10;
            bVar2.d(this.f4023a);
            return;
        }
        Log.v("ConstraintLayoutStates", "NO Constraint set found ! id=" + i8 + ", dim =" + f5 + ", " + f8);
    }
}
