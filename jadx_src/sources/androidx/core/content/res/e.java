package androidx.core.content.res;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.Base64;
import android.util.TypedValue;
import android.util.Xml;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class e {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {
        static int a(TypedArray typedArray, int i8) {
            return typedArray.getType(i8);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b {
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class c implements b {

        /* renamed from: a  reason: collision with root package name */
        private final d[] f4648a;

        public c(d[] dVarArr) {
            this.f4648a = dVarArr;
        }

        public d[] a() {
            return this.f4648a;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class d {

        /* renamed from: a  reason: collision with root package name */
        private final String f4649a;

        /* renamed from: b  reason: collision with root package name */
        private final int f4650b;

        /* renamed from: c  reason: collision with root package name */
        private final boolean f4651c;

        /* renamed from: d  reason: collision with root package name */
        private final String f4652d;

        /* renamed from: e  reason: collision with root package name */
        private final int f4653e;

        /* renamed from: f  reason: collision with root package name */
        private final int f4654f;

        public d(String str, int i8, boolean z4, String str2, int i9, int i10) {
            this.f4649a = str;
            this.f4650b = i8;
            this.f4651c = z4;
            this.f4652d = str2;
            this.f4653e = i9;
            this.f4654f = i10;
        }

        public String a() {
            return this.f4649a;
        }

        public int b() {
            return this.f4654f;
        }

        public int c() {
            return this.f4653e;
        }

        public String d() {
            return this.f4652d;
        }

        public int e() {
            return this.f4650b;
        }

        public boolean f() {
            return this.f4651c;
        }
    }

    /* renamed from: androidx.core.content.res.e$e  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class C0033e implements b {

        /* renamed from: a  reason: collision with root package name */
        private final androidx.core.provider.e f4655a;

        /* renamed from: b  reason: collision with root package name */
        private final int f4656b;

        /* renamed from: c  reason: collision with root package name */
        private final int f4657c;

        /* renamed from: d  reason: collision with root package name */
        private final String f4658d;

        public C0033e(androidx.core.provider.e eVar, int i8, int i9, String str) {
            this.f4655a = eVar;
            this.f4657c = i8;
            this.f4656b = i9;
            this.f4658d = str;
        }

        public int a() {
            return this.f4657c;
        }

        public androidx.core.provider.e b() {
            return this.f4655a;
        }

        public String c() {
            return this.f4658d;
        }

        public int d() {
            return this.f4656b;
        }
    }

    private static int a(TypedArray typedArray, int i8) {
        if (Build.VERSION.SDK_INT >= 21) {
            return a.a(typedArray, i8);
        }
        TypedValue typedValue = new TypedValue();
        typedArray.getValue(i8, typedValue);
        return typedValue.type;
    }

    public static b b(XmlPullParser xmlPullParser, Resources resources) {
        int next;
        do {
            next = xmlPullParser.next();
            if (next == 2) {
                break;
            }
        } while (next != 1);
        if (next == 2) {
            return d(xmlPullParser, resources);
        }
        throw new XmlPullParserException("No start tag found");
    }

    public static List<List<byte[]>> c(Resources resources, int i8) {
        if (i8 == 0) {
            return Collections.emptyList();
        }
        TypedArray obtainTypedArray = resources.obtainTypedArray(i8);
        try {
            if (obtainTypedArray.length() == 0) {
                return Collections.emptyList();
            }
            ArrayList arrayList = new ArrayList();
            if (a(obtainTypedArray, 0) == 1) {
                for (int i9 = 0; i9 < obtainTypedArray.length(); i9++) {
                    int resourceId = obtainTypedArray.getResourceId(i9, 0);
                    if (resourceId != 0) {
                        arrayList.add(h(resources.getStringArray(resourceId)));
                    }
                }
            } else {
                arrayList.add(h(resources.getStringArray(i8)));
            }
            return arrayList;
        } finally {
            obtainTypedArray.recycle();
        }
    }

    private static b d(XmlPullParser xmlPullParser, Resources resources) {
        xmlPullParser.require(2, null, "font-family");
        if (xmlPullParser.getName().equals("font-family")) {
            return e(xmlPullParser, resources);
        }
        g(xmlPullParser);
        return null;
    }

    private static b e(XmlPullParser xmlPullParser, Resources resources) {
        TypedArray obtainAttributes = resources.obtainAttributes(Xml.asAttributeSet(xmlPullParser), q0.i.f22506h);
        String string = obtainAttributes.getString(q0.i.f22507i);
        String string2 = obtainAttributes.getString(q0.i.f22511m);
        String string3 = obtainAttributes.getString(q0.i.f22512n);
        int resourceId = obtainAttributes.getResourceId(q0.i.f22508j, 0);
        int integer = obtainAttributes.getInteger(q0.i.f22509k, 1);
        int integer2 = obtainAttributes.getInteger(q0.i.f22510l, 500);
        String string4 = obtainAttributes.getString(q0.i.f22513o);
        obtainAttributes.recycle();
        if (string != null && string2 != null && string3 != null) {
            while (xmlPullParser.next() != 3) {
                g(xmlPullParser);
            }
            return new C0033e(new androidx.core.provider.e(string, string2, string3, c(resources, resourceId)), integer, integer2, string4);
        }
        ArrayList arrayList = new ArrayList();
        while (xmlPullParser.next() != 3) {
            if (xmlPullParser.getEventType() == 2) {
                if (xmlPullParser.getName().equals("font")) {
                    arrayList.add(f(xmlPullParser, resources));
                } else {
                    g(xmlPullParser);
                }
            }
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        return new c((d[]) arrayList.toArray(new d[0]));
    }

    private static d f(XmlPullParser xmlPullParser, Resources resources) {
        TypedArray obtainAttributes = resources.obtainAttributes(Xml.asAttributeSet(xmlPullParser), q0.i.f22514p);
        int i8 = q0.i.f22522y;
        if (!obtainAttributes.hasValue(i8)) {
            i8 = q0.i.f22515r;
        }
        int i9 = obtainAttributes.getInt(i8, 400);
        int i10 = q0.i.f22520w;
        if (!obtainAttributes.hasValue(i10)) {
            i10 = q0.i.f22516s;
        }
        boolean z4 = 1 == obtainAttributes.getInt(i10, 0);
        int i11 = q0.i.f22523z;
        if (!obtainAttributes.hasValue(i11)) {
            i11 = q0.i.f22517t;
        }
        int i12 = q0.i.f22521x;
        if (!obtainAttributes.hasValue(i12)) {
            i12 = q0.i.f22518u;
        }
        String string = obtainAttributes.getString(i12);
        int i13 = obtainAttributes.getInt(i11, 0);
        int i14 = q0.i.f22519v;
        if (!obtainAttributes.hasValue(i14)) {
            i14 = q0.i.q;
        }
        int resourceId = obtainAttributes.getResourceId(i14, 0);
        String string2 = obtainAttributes.getString(i14);
        obtainAttributes.recycle();
        while (xmlPullParser.next() != 3) {
            g(xmlPullParser);
        }
        return new d(string2, i9, z4, string, i13, resourceId);
    }

    private static void g(XmlPullParser xmlPullParser) {
        int i8 = 1;
        while (i8 > 0) {
            int next = xmlPullParser.next();
            if (next == 2) {
                i8++;
            } else if (next == 3) {
                i8--;
            }
        }
    }

    private static List<byte[]> h(String[] strArr) {
        ArrayList arrayList = new ArrayList();
        for (String str : strArr) {
            arrayList.add(Base64.decode(str, 0));
        }
        return arrayList;
    }
}
