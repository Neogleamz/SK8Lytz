package r1;

import android.database.Cursor;
import android.os.Build;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class f {

    /* renamed from: a  reason: collision with root package name */
    public final String f22647a;

    /* renamed from: b  reason: collision with root package name */
    public final Map<String, a> f22648b;

    /* renamed from: c  reason: collision with root package name */
    public final Set<b> f22649c;

    /* renamed from: d  reason: collision with root package name */
    public final Set<d> f22650d;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        public final String f22651a;

        /* renamed from: b  reason: collision with root package name */
        public final String f22652b;

        /* renamed from: c  reason: collision with root package name */
        public final int f22653c;

        /* renamed from: d  reason: collision with root package name */
        public final boolean f22654d;

        /* renamed from: e  reason: collision with root package name */
        public final int f22655e;

        /* renamed from: f  reason: collision with root package name */
        public final String f22656f;

        /* renamed from: g  reason: collision with root package name */
        private final int f22657g;

        public a(String str, String str2, boolean z4, int i8, String str3, int i9) {
            this.f22651a = str;
            this.f22652b = str2;
            this.f22654d = z4;
            this.f22655e = i8;
            this.f22653c = a(str2);
            this.f22656f = str3;
            this.f22657g = i9;
        }

        private static int a(String str) {
            if (str == null) {
                return 5;
            }
            String upperCase = str.toUpperCase(Locale.US);
            if (upperCase.contains("INT")) {
                return 3;
            }
            if (upperCase.contains("CHAR") || upperCase.contains("CLOB") || upperCase.contains("TEXT")) {
                return 2;
            }
            if (upperCase.contains("BLOB")) {
                return 5;
            }
            return (upperCase.contains("REAL") || upperCase.contains("FLOA") || upperCase.contains("DOUB")) ? 4 : 1;
        }

        public boolean b() {
            return this.f22655e > 0;
        }

        public boolean equals(Object obj) {
            String str;
            String str2;
            String str3;
            if (this == obj) {
                return true;
            }
            if (obj instanceof a) {
                a aVar = (a) obj;
                if (Build.VERSION.SDK_INT >= 20) {
                    if (this.f22655e != aVar.f22655e) {
                        return false;
                    }
                } else if (b() != aVar.b()) {
                    return false;
                }
                if (this.f22651a.equals(aVar.f22651a) && this.f22654d == aVar.f22654d) {
                    if (this.f22657g != 1 || aVar.f22657g != 2 || (str3 = this.f22656f) == null || str3.equals(aVar.f22656f)) {
                        if (this.f22657g != 2 || aVar.f22657g != 1 || (str2 = aVar.f22656f) == null || str2.equals(this.f22656f)) {
                            int i8 = this.f22657g;
                            return (i8 == 0 || i8 != aVar.f22657g || ((str = this.f22656f) == null ? aVar.f22656f == null : str.equals(aVar.f22656f))) && this.f22653c == aVar.f22653c;
                        }
                        return false;
                    }
                    return false;
                }
                return false;
            }
            return false;
        }

        public int hashCode() {
            return (((((this.f22651a.hashCode() * 31) + this.f22653c) * 31) + (this.f22654d ? 1231 : 1237)) * 31) + this.f22655e;
        }

        public String toString() {
            return "Column{name='" + this.f22651a + "', type='" + this.f22652b + "', affinity='" + this.f22653c + "', notNull=" + this.f22654d + ", primaryKeyPosition=" + this.f22655e + ", defaultValue='" + this.f22656f + "'}";
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {

        /* renamed from: a  reason: collision with root package name */
        public final String f22658a;

        /* renamed from: b  reason: collision with root package name */
        public final String f22659b;

        /* renamed from: c  reason: collision with root package name */
        public final String f22660c;

        /* renamed from: d  reason: collision with root package name */
        public final List<String> f22661d;

        /* renamed from: e  reason: collision with root package name */
        public final List<String> f22662e;

        public b(String str, String str2, String str3, List<String> list, List<String> list2) {
            this.f22658a = str;
            this.f22659b = str2;
            this.f22660c = str3;
            this.f22661d = Collections.unmodifiableList(list);
            this.f22662e = Collections.unmodifiableList(list2);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof b) {
                b bVar = (b) obj;
                if (this.f22658a.equals(bVar.f22658a) && this.f22659b.equals(bVar.f22659b) && this.f22660c.equals(bVar.f22660c) && this.f22661d.equals(bVar.f22661d)) {
                    return this.f22662e.equals(bVar.f22662e);
                }
                return false;
            }
            return false;
        }

        public int hashCode() {
            return (((((((this.f22658a.hashCode() * 31) + this.f22659b.hashCode()) * 31) + this.f22660c.hashCode()) * 31) + this.f22661d.hashCode()) * 31) + this.f22662e.hashCode();
        }

        public String toString() {
            return "ForeignKey{referenceTable='" + this.f22658a + "', onDelete='" + this.f22659b + "', onUpdate='" + this.f22660c + "', columnNames=" + this.f22661d + ", referenceColumnNames=" + this.f22662e + '}';
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class c implements Comparable<c> {

        /* renamed from: a  reason: collision with root package name */
        final int f22663a;

        /* renamed from: b  reason: collision with root package name */
        final int f22664b;

        /* renamed from: c  reason: collision with root package name */
        final String f22665c;

        /* renamed from: d  reason: collision with root package name */
        final String f22666d;

        c(int i8, int i9, String str, String str2) {
            this.f22663a = i8;
            this.f22664b = i9;
            this.f22665c = str;
            this.f22666d = str2;
        }

        @Override // java.lang.Comparable
        /* renamed from: c */
        public int compareTo(c cVar) {
            int i8 = this.f22663a - cVar.f22663a;
            return i8 == 0 ? this.f22664b - cVar.f22664b : i8;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class d {

        /* renamed from: a  reason: collision with root package name */
        public final String f22667a;

        /* renamed from: b  reason: collision with root package name */
        public final boolean f22668b;

        /* renamed from: c  reason: collision with root package name */
        public final List<String> f22669c;

        public d(String str, boolean z4, List<String> list) {
            this.f22667a = str;
            this.f22668b = z4;
            this.f22669c = list;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof d) {
                d dVar = (d) obj;
                if (this.f22668b == dVar.f22668b && this.f22669c.equals(dVar.f22669c)) {
                    return this.f22667a.startsWith("index_") ? dVar.f22667a.startsWith("index_") : this.f22667a.equals(dVar.f22667a);
                }
                return false;
            }
            return false;
        }

        public int hashCode() {
            return ((((this.f22667a.startsWith("index_") ? -1184239155 : this.f22667a.hashCode()) * 31) + (this.f22668b ? 1 : 0)) * 31) + this.f22669c.hashCode();
        }

        public String toString() {
            return "Index{name='" + this.f22667a + "', unique=" + this.f22668b + ", columns=" + this.f22669c + '}';
        }
    }

    public f(String str, Map<String, a> map, Set<b> set, Set<d> set2) {
        this.f22647a = str;
        this.f22648b = Collections.unmodifiableMap(map);
        this.f22649c = Collections.unmodifiableSet(set);
        this.f22650d = set2 == null ? null : Collections.unmodifiableSet(set2);
    }

    public static f a(t1.b bVar, String str) {
        return new f(str, b(bVar, str), d(bVar, str), f(bVar, str));
    }

    private static Map<String, a> b(t1.b bVar, String str) {
        Cursor w02 = bVar.w0("PRAGMA table_info(`" + str + "`)");
        HashMap hashMap = new HashMap();
        try {
            if (w02.getColumnCount() > 0) {
                int columnIndex = w02.getColumnIndex("name");
                int columnIndex2 = w02.getColumnIndex("type");
                int columnIndex3 = w02.getColumnIndex("notnull");
                int columnIndex4 = w02.getColumnIndex("pk");
                int columnIndex5 = w02.getColumnIndex("dflt_value");
                while (w02.moveToNext()) {
                    String string = w02.getString(columnIndex);
                    hashMap.put(string, new a(string, w02.getString(columnIndex2), w02.getInt(columnIndex3) != 0, w02.getInt(columnIndex4), w02.getString(columnIndex5), 2));
                }
            }
            return hashMap;
        } finally {
            w02.close();
        }
    }

    private static List<c> c(Cursor cursor) {
        int columnIndex = cursor.getColumnIndex("id");
        int columnIndex2 = cursor.getColumnIndex("seq");
        int columnIndex3 = cursor.getColumnIndex("from");
        int columnIndex4 = cursor.getColumnIndex("to");
        int count = cursor.getCount();
        ArrayList arrayList = new ArrayList();
        for (int i8 = 0; i8 < count; i8++) {
            cursor.moveToPosition(i8);
            arrayList.add(new c(cursor.getInt(columnIndex), cursor.getInt(columnIndex2), cursor.getString(columnIndex3), cursor.getString(columnIndex4)));
        }
        Collections.sort(arrayList);
        return arrayList;
    }

    private static Set<b> d(t1.b bVar, String str) {
        HashSet hashSet = new HashSet();
        Cursor w02 = bVar.w0("PRAGMA foreign_key_list(`" + str + "`)");
        try {
            int columnIndex = w02.getColumnIndex("id");
            int columnIndex2 = w02.getColumnIndex("seq");
            int columnIndex3 = w02.getColumnIndex("table");
            int columnIndex4 = w02.getColumnIndex("on_delete");
            int columnIndex5 = w02.getColumnIndex("on_update");
            List<c> c9 = c(w02);
            int count = w02.getCount();
            for (int i8 = 0; i8 < count; i8++) {
                w02.moveToPosition(i8);
                if (w02.getInt(columnIndex2) == 0) {
                    int i9 = w02.getInt(columnIndex);
                    ArrayList arrayList = new ArrayList();
                    ArrayList arrayList2 = new ArrayList();
                    for (c cVar : c9) {
                        if (cVar.f22663a == i9) {
                            arrayList.add(cVar.f22665c);
                            arrayList2.add(cVar.f22666d);
                        }
                    }
                    hashSet.add(new b(w02.getString(columnIndex3), w02.getString(columnIndex4), w02.getString(columnIndex5), arrayList, arrayList2));
                }
            }
            return hashSet;
        } finally {
            w02.close();
        }
    }

    private static d e(t1.b bVar, String str, boolean z4) {
        Cursor w02 = bVar.w0("PRAGMA index_xinfo(`" + str + "`)");
        try {
            int columnIndex = w02.getColumnIndex("seqno");
            int columnIndex2 = w02.getColumnIndex("cid");
            int columnIndex3 = w02.getColumnIndex("name");
            if (columnIndex != -1 && columnIndex2 != -1 && columnIndex3 != -1) {
                TreeMap treeMap = new TreeMap();
                while (w02.moveToNext()) {
                    if (w02.getInt(columnIndex2) >= 0) {
                        int i8 = w02.getInt(columnIndex);
                        treeMap.put(Integer.valueOf(i8), w02.getString(columnIndex3));
                    }
                }
                ArrayList arrayList = new ArrayList(treeMap.size());
                arrayList.addAll(treeMap.values());
                return new d(str, z4, arrayList);
            }
            return null;
        } finally {
            w02.close();
        }
    }

    private static Set<d> f(t1.b bVar, String str) {
        Cursor w02 = bVar.w0("PRAGMA index_list(`" + str + "`)");
        try {
            int columnIndex = w02.getColumnIndex("name");
            int columnIndex2 = w02.getColumnIndex("origin");
            int columnIndex3 = w02.getColumnIndex("unique");
            if (columnIndex != -1 && columnIndex2 != -1 && columnIndex3 != -1) {
                HashSet hashSet = new HashSet();
                while (w02.moveToNext()) {
                    if ("c".equals(w02.getString(columnIndex2))) {
                        String string = w02.getString(columnIndex);
                        boolean z4 = true;
                        if (w02.getInt(columnIndex3) != 1) {
                            z4 = false;
                        }
                        d e8 = e(bVar, string, z4);
                        if (e8 == null) {
                            return null;
                        }
                        hashSet.add(e8);
                    }
                }
                return hashSet;
            }
            return null;
        } finally {
            w02.close();
        }
    }

    public boolean equals(Object obj) {
        Set<d> set;
        if (this == obj) {
            return true;
        }
        if (obj instanceof f) {
            f fVar = (f) obj;
            String str = this.f22647a;
            if (str == null ? fVar.f22647a == null : str.equals(fVar.f22647a)) {
                Map<String, a> map = this.f22648b;
                if (map == null ? fVar.f22648b == null : map.equals(fVar.f22648b)) {
                    Set<b> set2 = this.f22649c;
                    if (set2 == null ? fVar.f22649c == null : set2.equals(fVar.f22649c)) {
                        Set<d> set3 = this.f22650d;
                        if (set3 == null || (set = fVar.f22650d) == null) {
                            return true;
                        }
                        return set3.equals(set);
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        String str = this.f22647a;
        int hashCode = (str != null ? str.hashCode() : 0) * 31;
        Map<String, a> map = this.f22648b;
        int hashCode2 = (hashCode + (map != null ? map.hashCode() : 0)) * 31;
        Set<b> set = this.f22649c;
        return hashCode2 + (set != null ? set.hashCode() : 0);
    }

    public String toString() {
        return "TableInfo{name='" + this.f22647a + "', columns=" + this.f22648b + ", foreignKeys=" + this.f22649c + ", indices=" + this.f22650d + '}';
    }
}
