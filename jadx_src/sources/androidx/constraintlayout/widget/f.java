package androidx.constraintlayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;
import android.util.SparseArray;
import android.util.Xml;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class f {

    /* renamed from: a  reason: collision with root package name */
    int f4347a = -1;

    /* renamed from: b  reason: collision with root package name */
    int f4348b = -1;

    /* renamed from: c  reason: collision with root package name */
    int f4349c = -1;

    /* renamed from: d  reason: collision with root package name */
    private SparseArray<a> f4350d = new SparseArray<>();

    /* renamed from: e  reason: collision with root package name */
    private SparseArray<androidx.constraintlayout.widget.b> f4351e = new SparseArray<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {

        /* renamed from: a  reason: collision with root package name */
        int f4352a;

        /* renamed from: b  reason: collision with root package name */
        ArrayList<b> f4353b = new ArrayList<>();

        /* renamed from: c  reason: collision with root package name */
        int f4354c;

        /* renamed from: d  reason: collision with root package name */
        boolean f4355d;

        public a(Context context, XmlPullParser xmlPullParser) {
            this.f4354c = -1;
            this.f4355d = false;
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(Xml.asAttributeSet(xmlPullParser), e.f4311v7);
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i8 = 0; i8 < indexCount; i8++) {
                int index = obtainStyledAttributes.getIndex(i8);
                if (index == e.f4320w7) {
                    this.f4352a = obtainStyledAttributes.getResourceId(index, this.f4352a);
                } else if (index == e.f4329x7) {
                    this.f4354c = obtainStyledAttributes.getResourceId(index, this.f4354c);
                    String resourceTypeName = context.getResources().getResourceTypeName(this.f4354c);
                    context.getResources().getResourceName(this.f4354c);
                    if ("layout".equals(resourceTypeName)) {
                        this.f4355d = true;
                    }
                }
            }
            obtainStyledAttributes.recycle();
        }

        void a(b bVar) {
            this.f4353b.add(bVar);
        }

        public int b(float f5, float f8) {
            for (int i8 = 0; i8 < this.f4353b.size(); i8++) {
                if (this.f4353b.get(i8).a(f5, f8)) {
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
        float f4356a;

        /* renamed from: b  reason: collision with root package name */
        float f4357b;

        /* renamed from: c  reason: collision with root package name */
        float f4358c;

        /* renamed from: d  reason: collision with root package name */
        float f4359d;

        /* renamed from: e  reason: collision with root package name */
        int f4360e;

        /* renamed from: f  reason: collision with root package name */
        boolean f4361f;

        public b(Context context, XmlPullParser xmlPullParser) {
            this.f4356a = Float.NaN;
            this.f4357b = Float.NaN;
            this.f4358c = Float.NaN;
            this.f4359d = Float.NaN;
            this.f4360e = -1;
            this.f4361f = false;
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(Xml.asAttributeSet(xmlPullParser), e.d8);
            int indexCount = obtainStyledAttributes.getIndexCount();
            for (int i8 = 0; i8 < indexCount; i8++) {
                int index = obtainStyledAttributes.getIndex(i8);
                if (index == e.e8) {
                    this.f4360e = obtainStyledAttributes.getResourceId(index, this.f4360e);
                    String resourceTypeName = context.getResources().getResourceTypeName(this.f4360e);
                    context.getResources().getResourceName(this.f4360e);
                    if ("layout".equals(resourceTypeName)) {
                        this.f4361f = true;
                    }
                } else if (index == e.f8) {
                    this.f4359d = obtainStyledAttributes.getDimension(index, this.f4359d);
                } else if (index == e.g8) {
                    this.f4357b = obtainStyledAttributes.getDimension(index, this.f4357b);
                } else if (index == e.h8) {
                    this.f4358c = obtainStyledAttributes.getDimension(index, this.f4358c);
                } else if (index == e.i8) {
                    this.f4356a = obtainStyledAttributes.getDimension(index, this.f4356a);
                } else {
                    Log.v("ConstraintLayoutStates", "Unknown tag");
                }
            }
            obtainStyledAttributes.recycle();
        }

        boolean a(float f5, float f8) {
            if (Float.isNaN(this.f4356a) || f5 >= this.f4356a) {
                if (Float.isNaN(this.f4357b) || f8 >= this.f4357b) {
                    if (Float.isNaN(this.f4358c) || f5 <= this.f4358c) {
                        return Float.isNaN(this.f4359d) || f8 <= this.f4359d;
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
    }

    public f(Context context, XmlPullParser xmlPullParser) {
        b(context, xmlPullParser);
    }

    private void b(Context context, XmlPullParser xmlPullParser) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(Xml.asAttributeSet(xmlPullParser), e.A7);
        int indexCount = obtainStyledAttributes.getIndexCount();
        for (int i8 = 0; i8 < indexCount; i8++) {
            int index = obtainStyledAttributes.getIndex(i8);
            if (index == e.B7) {
                this.f4347a = obtainStyledAttributes.getResourceId(index, this.f4347a);
            }
        }
        a aVar = null;
        try {
            int eventType = xmlPullParser.getEventType();
            while (eventType != 1) {
                if (eventType == 0) {
                    xmlPullParser.getName();
                } else if (eventType == 2) {
                    String name = xmlPullParser.getName();
                    char c9 = 65535;
                    switch (name.hashCode()) {
                        case 80204913:
                            if (name.equals("State")) {
                                c9 = 2;
                                break;
                            }
                            break;
                        case 1301459538:
                            if (name.equals("LayoutDescription")) {
                                c9 = 0;
                                break;
                            }
                            break;
                        case 1382829617:
                            if (name.equals("StateSet")) {
                                c9 = 1;
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
                            aVar = new a(context, xmlPullParser);
                            this.f4350d.put(aVar.f4352a, aVar);
                        } else if (c9 != 3) {
                            Log.v("ConstraintLayoutStates", "unknown tag " + name);
                        } else {
                            b bVar = new b(context, xmlPullParser);
                            if (aVar != null) {
                                aVar.a(bVar);
                            }
                        }
                    }
                } else if (eventType != 3) {
                    continue;
                } else if ("StateSet".equals(xmlPullParser.getName())) {
                    return;
                }
                eventType = xmlPullParser.next();
            }
        } catch (IOException e8) {
            e8.printStackTrace();
        } catch (XmlPullParserException e9) {
            e9.printStackTrace();
        }
    }

    public int a(int i8, int i9, float f5, float f8) {
        a aVar = this.f4350d.get(i9);
        if (aVar == null) {
            return i9;
        }
        if (f5 == -1.0f || f8 == -1.0f) {
            if (aVar.f4354c == i8) {
                return i8;
            }
            Iterator<b> it = aVar.f4353b.iterator();
            while (it.hasNext()) {
                if (i8 == it.next().f4360e) {
                    return i8;
                }
            }
            return aVar.f4354c;
        }
        b bVar = null;
        Iterator<b> it2 = aVar.f4353b.iterator();
        while (it2.hasNext()) {
            b next = it2.next();
            if (next.a(f5, f8)) {
                if (i8 == next.f4360e) {
                    return i8;
                }
                bVar = next;
            }
        }
        return bVar != null ? bVar.f4360e : aVar.f4354c;
    }

    public int c(int i8, int i9, int i10) {
        return d(-1, i8, i9, i10);
    }

    public int d(int i8, int i9, float f5, float f8) {
        int b9;
        if (i8 == i9) {
            a valueAt = i9 == -1 ? this.f4350d.valueAt(0) : this.f4350d.get(this.f4348b);
            if (valueAt == null) {
                return -1;
            }
            return ((this.f4349c == -1 || !valueAt.f4353b.get(i8).a(f5, f8)) && i8 != (b9 = valueAt.b(f5, f8))) ? b9 == -1 ? valueAt.f4354c : valueAt.f4353b.get(b9).f4360e : i8;
        }
        a aVar = this.f4350d.get(i9);
        if (aVar == null) {
            return -1;
        }
        int b10 = aVar.b(f5, f8);
        return b10 == -1 ? aVar.f4354c : aVar.f4353b.get(b10).f4360e;
    }
}
