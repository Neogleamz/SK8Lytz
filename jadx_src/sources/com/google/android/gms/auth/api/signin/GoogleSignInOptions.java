package com.google.android.gms.auth.api.signin;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.auth.api.signin.internal.GoogleSignInOptionsExtensionParcelable;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.a;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.util.VisibleForTesting;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class GoogleSignInOptions extends AbstractSafeParcelable implements a.d, ReflectedParcelable {
    public static final Parcelable.Creator<GoogleSignInOptions> CREATOR;

    /* renamed from: m  reason: collision with root package name */
    public static final GoogleSignInOptions f11440m;

    /* renamed from: n  reason: collision with root package name */
    public static final GoogleSignInOptions f11441n;
    @VisibleForTesting

    /* renamed from: p  reason: collision with root package name */
    public static final Scope f11442p = new Scope("profile");
    @VisibleForTesting
    public static final Scope q = new Scope("email");
    @VisibleForTesting

    /* renamed from: t  reason: collision with root package name */
    public static final Scope f11443t = new Scope("openid");
    @VisibleForTesting

    /* renamed from: w  reason: collision with root package name */
    public static final Scope f11444w;
    @VisibleForTesting

    /* renamed from: x  reason: collision with root package name */
    public static final Scope f11445x;

    /* renamed from: y  reason: collision with root package name */
    private static Comparator f11446y;

    /* renamed from: a  reason: collision with root package name */
    final int f11447a;

    /* renamed from: b  reason: collision with root package name */
    private final ArrayList f11448b;

    /* renamed from: c  reason: collision with root package name */
    private Account f11449c;

    /* renamed from: d  reason: collision with root package name */
    private boolean f11450d;

    /* renamed from: e  reason: collision with root package name */
    private final boolean f11451e;

    /* renamed from: f  reason: collision with root package name */
    private final boolean f11452f;

    /* renamed from: g  reason: collision with root package name */
    private String f11453g;

    /* renamed from: h  reason: collision with root package name */
    private String f11454h;

    /* renamed from: j  reason: collision with root package name */
    private ArrayList f11455j;

    /* renamed from: k  reason: collision with root package name */
    private String f11456k;

    /* renamed from: l  reason: collision with root package name */
    private Map f11457l;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: b  reason: collision with root package name */
        private boolean f11459b;

        /* renamed from: c  reason: collision with root package name */
        private boolean f11460c;

        /* renamed from: d  reason: collision with root package name */
        private boolean f11461d;

        /* renamed from: e  reason: collision with root package name */
        private String f11462e;

        /* renamed from: f  reason: collision with root package name */
        private Account f11463f;

        /* renamed from: g  reason: collision with root package name */
        private String f11464g;

        /* renamed from: i  reason: collision with root package name */
        private String f11466i;

        /* renamed from: a  reason: collision with root package name */
        private Set f11458a = new HashSet();

        /* renamed from: h  reason: collision with root package name */
        private Map f11465h = new HashMap();

        public GoogleSignInOptions a() {
            if (this.f11458a.contains(GoogleSignInOptions.f11445x)) {
                Set set = this.f11458a;
                Scope scope = GoogleSignInOptions.f11444w;
                if (set.contains(scope)) {
                    this.f11458a.remove(scope);
                }
            }
            if (this.f11461d && (this.f11463f == null || !this.f11458a.isEmpty())) {
                b();
            }
            return new GoogleSignInOptions(new ArrayList(this.f11458a), this.f11463f, this.f11461d, this.f11459b, this.f11460c, this.f11462e, this.f11464g, this.f11465h, this.f11466i);
        }

        public a b() {
            this.f11458a.add(GoogleSignInOptions.f11443t);
            return this;
        }

        public a c() {
            this.f11458a.add(GoogleSignInOptions.f11442p);
            return this;
        }

        public a d(Scope scope, Scope... scopeArr) {
            this.f11458a.add(scope);
            this.f11458a.addAll(Arrays.asList(scopeArr));
            return this;
        }
    }

    static {
        Scope scope = new Scope("https://www.googleapis.com/auth/games_lite");
        f11444w = scope;
        f11445x = new Scope("https://www.googleapis.com/auth/games");
        a aVar = new a();
        aVar.b();
        aVar.c();
        f11440m = aVar.a();
        a aVar2 = new a();
        aVar2.d(scope, new Scope[0]);
        f11441n = aVar2.a();
        CREATOR = new c();
        f11446y = new b();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public GoogleSignInOptions(int i8, ArrayList arrayList, Account account, boolean z4, boolean z8, boolean z9, String str, String str2, ArrayList arrayList2, String str3) {
        this(i8, arrayList, account, z4, z8, z9, str, str2, W0(arrayList2), str3);
    }

    private GoogleSignInOptions(int i8, ArrayList arrayList, Account account, boolean z4, boolean z8, boolean z9, String str, String str2, Map map, String str3) {
        this.f11447a = i8;
        this.f11448b = arrayList;
        this.f11449c = account;
        this.f11450d = z4;
        this.f11451e = z8;
        this.f11452f = z9;
        this.f11453g = str;
        this.f11454h = str2;
        this.f11455j = new ArrayList(map.values());
        this.f11457l = map;
        this.f11456k = str3;
    }

    private static Map W0(List list) {
        HashMap hashMap = new HashMap();
        if (list == null) {
            return hashMap;
        }
        Iterator it = list.iterator();
        while (it.hasNext()) {
            GoogleSignInOptionsExtensionParcelable googleSignInOptionsExtensionParcelable = (GoogleSignInOptionsExtensionParcelable) it.next();
            hashMap.put(Integer.valueOf(googleSignInOptionsExtensionParcelable.t()), googleSignInOptionsExtensionParcelable);
        }
        return hashMap;
    }

    public String D0() {
        return this.f11453g;
    }

    public boolean E0() {
        return this.f11452f;
    }

    public boolean I0() {
        return this.f11450d;
    }

    public boolean T0() {
        return this.f11451e;
    }

    public ArrayList<Scope> Z() {
        return new ArrayList<>(this.f11448b);
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x0048, code lost:
        if (r1.equals(r4.n()) != false) goto L21;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean equals(java.lang.Object r4) {
        /*
            r3 = this;
            r0 = 0
            if (r4 != 0) goto L4
            return r0
        L4:
            com.google.android.gms.auth.api.signin.GoogleSignInOptions r4 = (com.google.android.gms.auth.api.signin.GoogleSignInOptions) r4     // Catch: java.lang.ClassCastException -> L90
            java.util.ArrayList r1 = r3.f11455j     // Catch: java.lang.ClassCastException -> L90
            int r1 = r1.size()     // Catch: java.lang.ClassCastException -> L90
            if (r1 > 0) goto L90
            java.util.ArrayList r1 = r4.f11455j     // Catch: java.lang.ClassCastException -> L90
            int r1 = r1.size()     // Catch: java.lang.ClassCastException -> L90
            if (r1 <= 0) goto L18
            goto L90
        L18:
            java.util.ArrayList r1 = r3.f11448b     // Catch: java.lang.ClassCastException -> L90
            int r1 = r1.size()     // Catch: java.lang.ClassCastException -> L90
            java.util.ArrayList r2 = r4.Z()     // Catch: java.lang.ClassCastException -> L90
            int r2 = r2.size()     // Catch: java.lang.ClassCastException -> L90
            if (r1 != r2) goto L90
            java.util.ArrayList r1 = r3.f11448b     // Catch: java.lang.ClassCastException -> L90
            java.util.ArrayList r2 = r4.Z()     // Catch: java.lang.ClassCastException -> L90
            boolean r1 = r1.containsAll(r2)     // Catch: java.lang.ClassCastException -> L90
            if (r1 != 0) goto L35
            goto L90
        L35:
            android.accounts.Account r1 = r3.f11449c     // Catch: java.lang.ClassCastException -> L90
            if (r1 != 0) goto L40
            android.accounts.Account r1 = r4.n()     // Catch: java.lang.ClassCastException -> L90
            if (r1 != 0) goto L90
            goto L4a
        L40:
            android.accounts.Account r2 = r4.n()     // Catch: java.lang.ClassCastException -> L90
            boolean r1 = r1.equals(r2)     // Catch: java.lang.ClassCastException -> L90
            if (r1 == 0) goto L90
        L4a:
            java.lang.String r1 = r3.f11453g     // Catch: java.lang.ClassCastException -> L90
            boolean r1 = android.text.TextUtils.isEmpty(r1)     // Catch: java.lang.ClassCastException -> L90
            if (r1 == 0) goto L5d
            java.lang.String r1 = r4.D0()     // Catch: java.lang.ClassCastException -> L90
            boolean r1 = android.text.TextUtils.isEmpty(r1)     // Catch: java.lang.ClassCastException -> L90
            if (r1 == 0) goto L90
            goto L6a
        L5d:
            java.lang.String r1 = r3.f11453g     // Catch: java.lang.ClassCastException -> L90
            java.lang.String r2 = r4.D0()     // Catch: java.lang.ClassCastException -> L90
            boolean r1 = r1.equals(r2)     // Catch: java.lang.ClassCastException -> L90
            if (r1 != 0) goto L6a
            goto L90
        L6a:
            boolean r1 = r3.f11452f     // Catch: java.lang.ClassCastException -> L90
            boolean r2 = r4.E0()     // Catch: java.lang.ClassCastException -> L90
            if (r1 != r2) goto L90
            boolean r1 = r3.f11450d     // Catch: java.lang.ClassCastException -> L90
            boolean r2 = r4.I0()     // Catch: java.lang.ClassCastException -> L90
            if (r1 != r2) goto L90
            boolean r1 = r3.f11451e     // Catch: java.lang.ClassCastException -> L90
            boolean r2 = r4.T0()     // Catch: java.lang.ClassCastException -> L90
            if (r1 != r2) goto L90
            java.lang.String r1 = r3.f11456k     // Catch: java.lang.ClassCastException -> L90
            java.lang.String r4 = r4.u()     // Catch: java.lang.ClassCastException -> L90
            boolean r4 = android.text.TextUtils.equals(r1, r4)     // Catch: java.lang.ClassCastException -> L90
            if (r4 == 0) goto L90
            r4 = 1
            return r4
        L90:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.auth.api.signin.GoogleSignInOptions.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = this.f11448b;
        int size = arrayList2.size();
        for (int i8 = 0; i8 < size; i8++) {
            arrayList.add(((Scope) arrayList2.get(i8)).t());
        }
        Collections.sort(arrayList);
        g6.a aVar = new g6.a();
        aVar.a(arrayList);
        aVar.a(this.f11449c);
        aVar.a(this.f11453g);
        aVar.c(this.f11452f);
        aVar.c(this.f11450d);
        aVar.c(this.f11451e);
        aVar.a(this.f11456k);
        return aVar.b();
    }

    public Account n() {
        return this.f11449c;
    }

    public ArrayList<GoogleSignInOptionsExtensionParcelable> t() {
        return this.f11455j;
    }

    public String u() {
        return this.f11456k;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 1, this.f11447a);
        o6.a.v(parcel, 2, Z(), false);
        o6.a.q(parcel, 3, n(), i8, false);
        o6.a.c(parcel, 4, I0());
        o6.a.c(parcel, 5, T0());
        o6.a.c(parcel, 6, E0());
        o6.a.r(parcel, 7, D0(), false);
        o6.a.r(parcel, 8, this.f11454h, false);
        o6.a.v(parcel, 9, t(), false);
        o6.a.r(parcel, 10, u(), false);
        o6.a.b(parcel, a9);
    }
}
