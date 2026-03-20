package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.util.Log;
import android.util.Xml;
import androidx.constraintlayout.widget.ConstraintAttribute;
import androidx.constraintlayout.widget.ConstraintLayout;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class h {

    /* renamed from: b  reason: collision with root package name */
    static HashMap<String, Constructor<? extends c>> f3316b;

    /* renamed from: a  reason: collision with root package name */
    private HashMap<Integer, ArrayList<c>> f3317a = new HashMap<>();

    static {
        HashMap<String, Constructor<? extends c>> hashMap = new HashMap<>();
        f3316b = hashMap;
        try {
            hashMap.put("KeyAttribute", d.class.getConstructor(new Class[0]));
            f3316b.put("KeyPosition", i.class.getConstructor(new Class[0]));
            f3316b.put("KeyCycle", f.class.getConstructor(new Class[0]));
            f3316b.put("KeyTimeCycle", k.class.getConstructor(new Class[0]));
            f3316b.put("KeyTrigger", l.class.getConstructor(new Class[0]));
        } catch (NoSuchMethodException e8) {
            Log.e("KeyFrames", "unable to load", e8);
        }
    }

    public h(Context context, XmlPullParser xmlPullParser) {
        c cVar;
        Exception e8;
        HashMap<String, ConstraintAttribute> hashMap;
        c cVar2 = null;
        try {
            int eventType = xmlPullParser.getEventType();
            while (eventType != 1) {
                if (eventType == 2) {
                    String name = xmlPullParser.getName();
                    if (f3316b.containsKey(name)) {
                        try {
                            cVar = f3316b.get(name).newInstance(new Object[0]);
                        } catch (Exception e9) {
                            cVar = cVar2;
                            e8 = e9;
                        }
                        try {
                            cVar.c(context, Xml.asAttributeSet(xmlPullParser));
                            b(cVar);
                        } catch (Exception e10) {
                            e8 = e10;
                            Log.e("KeyFrames", "unable to create ", e8);
                            cVar2 = cVar;
                            continue;
                            eventType = xmlPullParser.next();
                        }
                        cVar2 = cVar;
                        continue;
                    } else if (name.equalsIgnoreCase("CustomAttribute") && cVar2 != null && (hashMap = cVar2.f3252e) != null) {
                        ConstraintAttribute.g(context, xmlPullParser, hashMap);
                        continue;
                    }
                } else if (eventType == 3 && "KeyFrameSet".equals(xmlPullParser.getName())) {
                    return;
                }
                eventType = xmlPullParser.next();
            }
        } catch (IOException e11) {
            e11.printStackTrace();
        } catch (XmlPullParserException e12) {
            e12.printStackTrace();
        }
    }

    private void b(c cVar) {
        if (!this.f3317a.containsKey(Integer.valueOf(cVar.f3249b))) {
            this.f3317a.put(Integer.valueOf(cVar.f3249b), new ArrayList<>());
        }
        this.f3317a.get(Integer.valueOf(cVar.f3249b)).add(cVar);
    }

    public void a(n nVar) {
        ArrayList<c> arrayList = this.f3317a.get(Integer.valueOf(nVar.f3388b));
        if (arrayList != null) {
            nVar.b(arrayList);
        }
        ArrayList<c> arrayList2 = this.f3317a.get(-1);
        if (arrayList2 != null) {
            Iterator<c> it = arrayList2.iterator();
            while (it.hasNext()) {
                c next = it.next();
                if (next.d(((ConstraintLayout.LayoutParams) nVar.f3387a.getLayoutParams()).V)) {
                    nVar.a(next);
                }
            }
        }
    }
}
