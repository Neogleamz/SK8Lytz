package androidx.navigation;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class i {

    /* renamed from: j  reason: collision with root package name */
    private static final HashMap<String, Class<?>> f6387j = new HashMap<>();

    /* renamed from: a  reason: collision with root package name */
    private final String f6388a;

    /* renamed from: b  reason: collision with root package name */
    private j f6389b;

    /* renamed from: c  reason: collision with root package name */
    private int f6390c;

    /* renamed from: d  reason: collision with root package name */
    private String f6391d;

    /* renamed from: e  reason: collision with root package name */
    private CharSequence f6392e;

    /* renamed from: f  reason: collision with root package name */
    private ArrayList<g> f6393f;

    /* renamed from: g  reason: collision with root package name */
    private k0.h<c> f6394g;

    /* renamed from: h  reason: collision with root package name */
    private HashMap<String, d> f6395h;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a implements Comparable<a> {

        /* renamed from: a  reason: collision with root package name */
        private final i f6396a;

        /* renamed from: b  reason: collision with root package name */
        private final Bundle f6397b;

        /* renamed from: c  reason: collision with root package name */
        private final boolean f6398c;

        /* renamed from: d  reason: collision with root package name */
        private final boolean f6399d;

        /* renamed from: e  reason: collision with root package name */
        private final int f6400e;

        a(i iVar, Bundle bundle, boolean z4, boolean z8, int i8) {
            this.f6396a = iVar;
            this.f6397b = bundle;
            this.f6398c = z4;
            this.f6399d = z8;
            this.f6400e = i8;
        }

        @Override // java.lang.Comparable
        /* renamed from: c */
        public int compareTo(a aVar) {
            boolean z4 = this.f6398c;
            if (!z4 || aVar.f6398c) {
                if (z4 || !aVar.f6398c) {
                    Bundle bundle = this.f6397b;
                    if (bundle == null || aVar.f6397b != null) {
                        if (bundle != null || aVar.f6397b == null) {
                            if (bundle != null) {
                                int size = bundle.size() - aVar.f6397b.size();
                                if (size > 0) {
                                    return 1;
                                }
                                if (size < 0) {
                                    return -1;
                                }
                            }
                            boolean z8 = this.f6399d;
                            if (!z8 || aVar.f6399d) {
                                if (z8 || !aVar.f6399d) {
                                    return this.f6400e - aVar.f6400e;
                                }
                                return -1;
                            }
                            return 1;
                        }
                        return -1;
                    }
                    return 1;
                }
                return -1;
            }
            return 1;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public i f() {
            return this.f6396a;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Bundle h() {
            return this.f6397b;
        }
    }

    public i(q<? extends i> qVar) {
        this(r.c(qVar.getClass()));
    }

    public i(String str) {
        this.f6388a = str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String p(Context context, int i8) {
        if (i8 <= 16777215) {
            return Integer.toString(i8);
        }
        try {
            return context.getResources().getResourceName(i8);
        } catch (Resources.NotFoundException unused) {
            return Integer.toString(i8);
        }
    }

    public final void A(int i8) {
        this.f6390c = i8;
        this.f6391d = null;
    }

    public final void C(CharSequence charSequence) {
        this.f6392e = charSequence;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void D(j jVar) {
        this.f6389b = jVar;
    }

    boolean E() {
        return true;
    }

    public final void e(String str, d dVar) {
        if (this.f6395h == null) {
            this.f6395h = new HashMap<>();
        }
        this.f6395h.put(str, dVar);
    }

    public final void g(g gVar) {
        if (this.f6393f == null) {
            this.f6393f = new ArrayList<>();
        }
        this.f6393f.add(gVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Bundle h(Bundle bundle) {
        HashMap<String, d> hashMap;
        if (bundle == null && ((hashMap = this.f6395h) == null || hashMap.isEmpty())) {
            return null;
        }
        Bundle bundle2 = new Bundle();
        HashMap<String, d> hashMap2 = this.f6395h;
        if (hashMap2 != null) {
            for (Map.Entry<String, d> entry : hashMap2.entrySet()) {
                entry.getValue().c(entry.getKey(), bundle2);
            }
        }
        if (bundle != null) {
            bundle2.putAll(bundle);
            HashMap<String, d> hashMap3 = this.f6395h;
            if (hashMap3 != null) {
                for (Map.Entry<String, d> entry2 : hashMap3.entrySet()) {
                    if (!entry2.getValue().d(entry2.getKey(), bundle2)) {
                        throw new IllegalArgumentException("Wrong argument type for '" + entry2.getKey() + "' in argument bundle. " + entry2.getValue().a().c() + " expected.");
                    }
                }
            }
        }
        return bundle2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int[] i() {
        ArrayDeque arrayDeque = new ArrayDeque();
        i iVar = this;
        while (true) {
            j u8 = iVar.u();
            if (u8 == null || u8.K() != iVar.q()) {
                arrayDeque.addFirst(iVar);
            }
            if (u8 == null) {
                break;
            }
            iVar = u8;
        }
        int[] iArr = new int[arrayDeque.size()];
        int i8 = 0;
        Iterator it = arrayDeque.iterator();
        while (it.hasNext()) {
            iArr[i8] = ((i) it.next()).q();
            i8++;
        }
        return iArr;
    }

    public final Map<String, d> k() {
        HashMap<String, d> hashMap = this.f6395h;
        return hashMap == null ? Collections.emptyMap() : Collections.unmodifiableMap(hashMap);
    }

    public String n() {
        if (this.f6391d == null) {
            this.f6391d = Integer.toString(this.f6390c);
        }
        return this.f6391d;
    }

    public final int q() {
        return this.f6390c;
    }

    public final String t() {
        return this.f6388a;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append("(");
        String str = this.f6391d;
        if (str == null) {
            sb.append("0x");
            str = Integer.toHexString(this.f6390c);
        }
        sb.append(str);
        sb.append(")");
        if (this.f6392e != null) {
            sb.append(" label=");
            sb.append(this.f6392e);
        }
        return sb.toString();
    }

    public final j u() {
        return this.f6389b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public a v(h hVar) {
        ArrayList<g> arrayList = this.f6393f;
        if (arrayList == null) {
            return null;
        }
        Iterator<g> it = arrayList.iterator();
        a aVar = null;
        while (it.hasNext()) {
            g next = it.next();
            Uri c9 = hVar.c();
            Bundle c10 = c9 != null ? next.c(c9, k()) : null;
            String a9 = hVar.a();
            boolean z4 = a9 != null && a9.equals(next.b());
            String b9 = hVar.b();
            int d8 = b9 != null ? next.d(b9) : -1;
            if (c10 != null || z4 || d8 > -1) {
                a aVar2 = new a(this, c10, next.e(), z4, d8);
                if (aVar == null || aVar2.compareTo(aVar) > 0) {
                    aVar = aVar2;
                }
            }
        }
        return aVar;
    }

    public void x(Context context, AttributeSet attributeSet) {
        TypedArray obtainAttributes = context.getResources().obtainAttributes(attributeSet, j1.a.A);
        A(obtainAttributes.getResourceId(j1.a.C, 0));
        this.f6391d = p(context, this.f6390c);
        C(obtainAttributes.getText(j1.a.B));
        obtainAttributes.recycle();
    }

    public final void y(int i8, c cVar) {
        if (E()) {
            if (i8 == 0) {
                throw new IllegalArgumentException("Cannot have an action with actionId 0");
            }
            if (this.f6394g == null) {
                this.f6394g = new k0.h<>();
            }
            this.f6394g.l(i8, cVar);
            return;
        }
        throw new UnsupportedOperationException("Cannot add action " + i8 + " to " + this + " as it does not support actions, indicating that it is a terminal destination in your navigation graph and will never trigger actions.");
    }
}
