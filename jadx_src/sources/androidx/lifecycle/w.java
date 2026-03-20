package androidx.lifecycle;

import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Size;
import android.util.SizeF;
import android.util.SparseArray;
import androidx.savedstate.a;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class w {

    /* renamed from: f  reason: collision with root package name */
    public static final a f5921f = new a(null);

    /* renamed from: g  reason: collision with root package name */
    private static final Class<? extends Object>[] f5922g;

    /* renamed from: a  reason: collision with root package name */
    private final Map<String, Object> f5923a;

    /* renamed from: b  reason: collision with root package name */
    private final Map<String, a.c> f5924b;

    /* renamed from: c  reason: collision with root package name */
    private final Map<String, Object> f5925c;

    /* renamed from: d  reason: collision with root package name */
    private final Map<String, bk.h<Object>> f5926d;

    /* renamed from: e  reason: collision with root package name */
    private final a.c f5927e;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(kotlin.jvm.internal.i iVar) {
            this();
        }

        public final w a(Bundle bundle, Bundle bundle2) {
            if (bundle == null) {
                if (bundle2 == null) {
                    return new w();
                }
                HashMap hashMap = new HashMap();
                for (String str : bundle2.keySet()) {
                    kotlin.jvm.internal.p.d(str, "key");
                    hashMap.put(str, bundle2.get(str));
                }
                return new w(hashMap);
            }
            ArrayList parcelableArrayList = bundle.getParcelableArrayList("keys");
            ArrayList parcelableArrayList2 = bundle.getParcelableArrayList("values");
            if ((parcelableArrayList == null || parcelableArrayList2 == null || parcelableArrayList.size() != parcelableArrayList2.size()) ? false : true) {
                LinkedHashMap linkedHashMap = new LinkedHashMap();
                int size = parcelableArrayList.size();
                for (int i8 = 0; i8 < size; i8++) {
                    Object obj = parcelableArrayList.get(i8);
                    kotlin.jvm.internal.p.c(obj, "null cannot be cast to non-null type kotlin.String");
                    linkedHashMap.put((String) obj, parcelableArrayList2.get(i8));
                }
                return new w(linkedHashMap);
            }
            throw new IllegalStateException("Invalid bundle passed as restored state".toString());
        }

        public final boolean b(Object obj) {
            Class[] clsArr;
            if (obj == null) {
                return true;
            }
            for (Class cls : w.f5922g) {
                kotlin.jvm.internal.p.b(cls);
                if (cls.isInstance(obj)) {
                    return true;
                }
            }
            return false;
        }
    }

    static {
        Class<? extends Object>[] clsArr = new Class[29];
        clsArr[0] = Boolean.TYPE;
        clsArr[1] = boolean[].class;
        clsArr[2] = Double.TYPE;
        clsArr[3] = double[].class;
        Class<SizeF> cls = Integer.TYPE;
        clsArr[4] = cls;
        clsArr[5] = int[].class;
        clsArr[6] = Long.TYPE;
        clsArr[7] = long[].class;
        clsArr[8] = String.class;
        clsArr[9] = String[].class;
        clsArr[10] = Binder.class;
        clsArr[11] = Bundle.class;
        clsArr[12] = Byte.TYPE;
        clsArr[13] = byte[].class;
        clsArr[14] = Character.TYPE;
        clsArr[15] = char[].class;
        clsArr[16] = CharSequence.class;
        clsArr[17] = CharSequence[].class;
        clsArr[18] = ArrayList.class;
        clsArr[19] = Float.TYPE;
        clsArr[20] = float[].class;
        clsArr[21] = Parcelable.class;
        clsArr[22] = Parcelable[].class;
        clsArr[23] = Serializable.class;
        clsArr[24] = Short.TYPE;
        clsArr[25] = short[].class;
        clsArr[26] = SparseArray.class;
        int i8 = Build.VERSION.SDK_INT;
        clsArr[27] = i8 >= 21 ? Size.class : cls;
        if (i8 >= 21) {
            cls = SizeF.class;
        }
        clsArr[28] = cls;
        f5922g = clsArr;
    }

    public w() {
        this.f5923a = new LinkedHashMap();
        this.f5924b = new LinkedHashMap();
        this.f5925c = new LinkedHashMap();
        this.f5926d = new LinkedHashMap();
        this.f5927e = new a.c() { // from class: androidx.lifecycle.v
            @Override // androidx.savedstate.a.c
            public final Bundle a() {
                Bundle d8;
                d8 = w.d(w.this);
                return d8;
            }
        };
    }

    public w(Map<String, ? extends Object> map) {
        kotlin.jvm.internal.p.e(map, "initialState");
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        this.f5923a = linkedHashMap;
        this.f5924b = new LinkedHashMap();
        this.f5925c = new LinkedHashMap();
        this.f5926d = new LinkedHashMap();
        this.f5927e = new a.c() { // from class: androidx.lifecycle.v
            @Override // androidx.savedstate.a.c
            public final Bundle a() {
                Bundle d8;
                d8 = w.d(w.this);
                return d8;
            }
        };
        linkedHashMap.putAll(map);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Bundle d(w wVar) {
        kotlin.jvm.internal.p.e(wVar, "this$0");
        for (Map.Entry entry : kotlin.collections.j0.p(wVar.f5924b).entrySet()) {
            wVar.e((String) entry.getKey(), ((a.c) entry.getValue()).a());
        }
        Set<String> keySet = wVar.f5923a.keySet();
        ArrayList arrayList = new ArrayList(keySet.size());
        ArrayList arrayList2 = new ArrayList(arrayList.size());
        for (String str : keySet) {
            arrayList.add(str);
            arrayList2.add(wVar.f5923a.get(str));
        }
        return androidx.core.os.d.a(cj.q.a("keys", arrayList), cj.q.a("values", arrayList2));
    }

    public final a.c c() {
        return this.f5927e;
    }

    public final <T> void e(String str, T t8) {
        kotlin.jvm.internal.p.e(str, "key");
        if (!f5921f.b(t8)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Can't put value with type ");
            kotlin.jvm.internal.p.b(t8);
            sb.append(t8.getClass());
            sb.append(" into saved state");
            throw new IllegalArgumentException(sb.toString());
        }
        Object obj = this.f5925c.get(str);
        p pVar = obj instanceof p ? (p) obj : null;
        if (pVar != null) {
            pVar.o(t8);
        } else {
            this.f5923a.put(str, t8);
        }
        bk.h<Object> hVar = this.f5926d.get(str);
        if (hVar == null) {
            return;
        }
        hVar.setValue(t8);
    }
}
