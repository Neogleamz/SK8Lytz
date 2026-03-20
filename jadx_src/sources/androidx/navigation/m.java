package androidx.navigation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.util.Xml;
import androidx.navigation.d;
import androidx.navigation.g;
import androidx.navigation.n;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class m {

    /* renamed from: c  reason: collision with root package name */
    private static final ThreadLocal<TypedValue> f6408c = new ThreadLocal<>();

    /* renamed from: a  reason: collision with root package name */
    private Context f6409a;

    /* renamed from: b  reason: collision with root package name */
    private r f6410b;

    public m(Context context, r rVar) {
        this.f6409a = context;
        this.f6410b = rVar;
    }

    private static o a(TypedValue typedValue, o oVar, o oVar2, String str, String str2) {
        if (oVar == null || oVar == oVar2) {
            return oVar != null ? oVar : oVar2;
        }
        throw new XmlPullParserException("Type is " + str + " but found " + str2 + ": " + typedValue.data);
    }

    private i b(Resources resources, XmlResourceParser xmlResourceParser, AttributeSet attributeSet, int i8) {
        int depth;
        i a9 = this.f6410b.e(xmlResourceParser.getName()).a();
        a9.x(this.f6409a, attributeSet);
        int depth2 = xmlResourceParser.getDepth() + 1;
        while (true) {
            int next = xmlResourceParser.next();
            if (next == 1 || ((depth = xmlResourceParser.getDepth()) < depth2 && next == 3)) {
                break;
            } else if (next == 2 && depth <= depth2) {
                String name = xmlResourceParser.getName();
                if ("argument".equals(name)) {
                    g(resources, a9, attributeSet, i8);
                } else if ("deepLink".equals(name)) {
                    h(resources, a9, attributeSet);
                } else if ("action".equals(name)) {
                    d(resources, a9, attributeSet, xmlResourceParser, i8);
                } else if ("include".equals(name) && (a9 instanceof j)) {
                    TypedArray obtainAttributes = resources.obtainAttributes(attributeSet, t.f6461r);
                    ((j) a9).F(c(obtainAttributes.getResourceId(t.f6462s, 0)));
                    obtainAttributes.recycle();
                } else if (a9 instanceof j) {
                    ((j) a9).F(b(resources, xmlResourceParser, attributeSet, i8));
                }
            }
        }
        return a9;
    }

    private void d(Resources resources, i iVar, AttributeSet attributeSet, XmlResourceParser xmlResourceParser, int i8) {
        int depth;
        TypedArray obtainAttributes = resources.obtainAttributes(attributeSet, j1.a.f20613f);
        int resourceId = obtainAttributes.getResourceId(j1.a.f20614g, 0);
        c cVar = new c(obtainAttributes.getResourceId(j1.a.f20615h, 0));
        n.a aVar = new n.a();
        aVar.d(obtainAttributes.getBoolean(j1.a.f20618k, false));
        aVar.g(obtainAttributes.getResourceId(j1.a.f20621n, -1), obtainAttributes.getBoolean(j1.a.f20622o, false));
        aVar.b(obtainAttributes.getResourceId(j1.a.f20616i, -1));
        aVar.c(obtainAttributes.getResourceId(j1.a.f20617j, -1));
        aVar.e(obtainAttributes.getResourceId(j1.a.f20619l, -1));
        aVar.f(obtainAttributes.getResourceId(j1.a.f20620m, -1));
        cVar.b(aVar.a());
        Bundle bundle = new Bundle();
        int depth2 = xmlResourceParser.getDepth() + 1;
        while (true) {
            int next = xmlResourceParser.next();
            if (next == 1 || ((depth = xmlResourceParser.getDepth()) < depth2 && next == 3)) {
                break;
            } else if (next == 2 && depth <= depth2 && "argument".equals(xmlResourceParser.getName())) {
                f(resources, bundle, attributeSet, i8);
            }
        }
        if (!bundle.isEmpty()) {
            cVar.a(bundle);
        }
        iVar.y(resourceId, cVar);
        obtainAttributes.recycle();
    }

    private d e(TypedArray typedArray, Resources resources, int i8) {
        float f5;
        int dimension;
        d.a aVar = new d.a();
        aVar.c(typedArray.getBoolean(j1.a.f20626t, false));
        ThreadLocal<TypedValue> threadLocal = f6408c;
        TypedValue typedValue = threadLocal.get();
        if (typedValue == null) {
            typedValue = new TypedValue();
            threadLocal.set(typedValue);
        }
        String string = typedArray.getString(j1.a.f20625s);
        Object obj = null;
        o<Integer> a9 = string != null ? o.a(string, resources.getResourcePackageName(i8)) : null;
        int i9 = j1.a.f20624r;
        if (typedArray.getValue(i9, typedValue)) {
            o<Integer> oVar = o.f6426c;
            if (a9 == oVar) {
                dimension = typedValue.resourceId;
                if (dimension == 0) {
                    if (typedValue.type != 16 || typedValue.data != 0) {
                        throw new XmlPullParserException("unsupported value '" + ((Object) typedValue.string) + "' for " + a9.c() + ". Must be a reference to a resource.");
                    }
                    obj = 0;
                }
                obj = Integer.valueOf(dimension);
            } else {
                int i10 = typedValue.resourceId;
                if (i10 != 0) {
                    if (a9 != null) {
                        throw new XmlPullParserException("unsupported value '" + ((Object) typedValue.string) + "' for " + a9.c() + ". You must use a \"" + oVar.c() + "\" type to reference other resources.");
                    }
                    a9 = oVar;
                    obj = Integer.valueOf(i10);
                } else if (a9 == o.f6434k) {
                    obj = typedArray.getString(i9);
                } else {
                    int i11 = typedValue.type;
                    if (i11 != 3) {
                        if (i11 != 4) {
                            if (i11 == 5) {
                                a9 = a(typedValue, a9, o.f6425b, string, "dimension");
                                dimension = (int) typedValue.getDimension(resources.getDisplayMetrics());
                            } else if (i11 == 18) {
                                a9 = a(typedValue, a9, o.f6432i, string, "boolean");
                                obj = Boolean.valueOf(typedValue.data != 0);
                            } else if (i11 < 16 || i11 > 31) {
                                throw new XmlPullParserException("unsupported argument type " + typedValue.type);
                            } else {
                                o<Float> oVar2 = o.f6430g;
                                if (a9 == oVar2) {
                                    a9 = a(typedValue, a9, oVar2, string, "float");
                                    f5 = typedValue.data;
                                } else {
                                    a9 = a(typedValue, a9, o.f6425b, string, "integer");
                                    dimension = typedValue.data;
                                }
                            }
                            obj = Integer.valueOf(dimension);
                        } else {
                            a9 = a(typedValue, a9, o.f6430g, string, "float");
                            f5 = typedValue.getFloat();
                        }
                        obj = Float.valueOf(f5);
                    } else {
                        String charSequence = typedValue.string.toString();
                        if (a9 == null) {
                            a9 = o.d(charSequence);
                        }
                        obj = a9.h(charSequence);
                    }
                }
            }
        }
        if (obj != null) {
            aVar.b(obj);
        }
        if (a9 != null) {
            aVar.d(a9);
        }
        return aVar.a();
    }

    private void f(Resources resources, Bundle bundle, AttributeSet attributeSet, int i8) {
        TypedArray obtainAttributes = resources.obtainAttributes(attributeSet, j1.a.f20623p);
        String string = obtainAttributes.getString(j1.a.q);
        if (string == null) {
            throw new XmlPullParserException("Arguments must have a name");
        }
        d e8 = e(obtainAttributes, resources, i8);
        if (e8.b()) {
            e8.c(string, bundle);
        }
        obtainAttributes.recycle();
    }

    private void g(Resources resources, i iVar, AttributeSet attributeSet, int i8) {
        TypedArray obtainAttributes = resources.obtainAttributes(attributeSet, j1.a.f20623p);
        String string = obtainAttributes.getString(j1.a.q);
        if (string == null) {
            throw new XmlPullParserException("Arguments must have a name");
        }
        iVar.e(string, e(obtainAttributes, resources, i8));
        obtainAttributes.recycle();
    }

    private void h(Resources resources, i iVar, AttributeSet attributeSet) {
        TypedArray obtainAttributes = resources.obtainAttributes(attributeSet, j1.a.f20627u);
        String string = obtainAttributes.getString(j1.a.f20630x);
        String string2 = obtainAttributes.getString(j1.a.f20628v);
        String string3 = obtainAttributes.getString(j1.a.f20629w);
        if (TextUtils.isEmpty(string) && TextUtils.isEmpty(string2) && TextUtils.isEmpty(string3)) {
            throw new XmlPullParserException("Every <deepLink> must include at least one of app:uri, app:action, or app:mimeType");
        }
        g.a aVar = new g.a();
        if (string != null) {
            aVar.d(string.replace("${applicationId}", this.f6409a.getPackageName()));
        }
        if (!TextUtils.isEmpty(string2)) {
            aVar.b(string2.replace("${applicationId}", this.f6409a.getPackageName()));
        }
        if (string3 != null) {
            aVar.c(string3.replace("${applicationId}", this.f6409a.getPackageName()));
        }
        iVar.g(aVar.a());
        obtainAttributes.recycle();
    }

    @SuppressLint({"ResourceType"})
    public j c(int i8) {
        int next;
        Resources resources = this.f6409a.getResources();
        XmlResourceParser xml = resources.getXml(i8);
        AttributeSet asAttributeSet = Xml.asAttributeSet(xml);
        while (true) {
            try {
                try {
                    next = xml.next();
                    if (next == 2 || next == 1) {
                        break;
                    }
                } catch (Exception e8) {
                    throw new RuntimeException("Exception inflating " + resources.getResourceName(i8) + " line " + xml.getLineNumber(), e8);
                }
            } finally {
                xml.close();
            }
        }
        if (next == 2) {
            String name = xml.getName();
            i b9 = b(resources, xml, asAttributeSet, i8);
            if (b9 instanceof j) {
                return (j) b9;
            }
            throw new IllegalArgumentException("Root element <" + name + "> did not inflate into a NavGraph");
        }
        throw new XmlPullParserException("No start tag found");
    }
}
