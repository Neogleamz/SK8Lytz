package com.google.android.exoplayer2.extractor.flv;

import b6.z;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import n4.j;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class c extends TagPayloadReader {

    /* renamed from: b  reason: collision with root package name */
    private long f9685b;

    /* renamed from: c  reason: collision with root package name */
    private long[] f9686c;

    /* renamed from: d  reason: collision with root package name */
    private long[] f9687d;

    public c() {
        super(new j());
        this.f9685b = -9223372036854775807L;
        this.f9686c = new long[0];
        this.f9687d = new long[0];
    }

    private static Boolean g(z zVar) {
        return Boolean.valueOf(zVar.H() == 1);
    }

    private static Object h(z zVar, int i8) {
        if (i8 != 0) {
            if (i8 != 1) {
                if (i8 != 2) {
                    if (i8 != 3) {
                        if (i8 != 8) {
                            if (i8 != 10) {
                                if (i8 != 11) {
                                    return null;
                                }
                                return i(zVar);
                            }
                            return m(zVar);
                        }
                        return k(zVar);
                    }
                    return l(zVar);
                }
                return n(zVar);
            }
            return g(zVar);
        }
        return j(zVar);
    }

    private static Date i(z zVar) {
        Date date = new Date((long) j(zVar).doubleValue());
        zVar.V(2);
        return date;
    }

    private static Double j(z zVar) {
        return Double.valueOf(Double.longBitsToDouble(zVar.A()));
    }

    private static HashMap<String, Object> k(z zVar) {
        int L = zVar.L();
        HashMap<String, Object> hashMap = new HashMap<>(L);
        for (int i8 = 0; i8 < L; i8++) {
            String n8 = n(zVar);
            Object h8 = h(zVar, o(zVar));
            if (h8 != null) {
                hashMap.put(n8, h8);
            }
        }
        return hashMap;
    }

    private static HashMap<String, Object> l(z zVar) {
        HashMap<String, Object> hashMap = new HashMap<>();
        while (true) {
            String n8 = n(zVar);
            int o5 = o(zVar);
            if (o5 == 9) {
                return hashMap;
            }
            Object h8 = h(zVar, o5);
            if (h8 != null) {
                hashMap.put(n8, h8);
            }
        }
    }

    private static ArrayList<Object> m(z zVar) {
        int L = zVar.L();
        ArrayList<Object> arrayList = new ArrayList<>(L);
        for (int i8 = 0; i8 < L; i8++) {
            Object h8 = h(zVar, o(zVar));
            if (h8 != null) {
                arrayList.add(h8);
            }
        }
        return arrayList;
    }

    private static String n(z zVar) {
        int N = zVar.N();
        int f5 = zVar.f();
        zVar.V(N);
        return new String(zVar.e(), f5, N);
    }

    private static int o(z zVar) {
        return zVar.H();
    }

    @Override // com.google.android.exoplayer2.extractor.flv.TagPayloadReader
    protected boolean b(z zVar) {
        return true;
    }

    @Override // com.google.android.exoplayer2.extractor.flv.TagPayloadReader
    protected boolean c(z zVar, long j8) {
        if (o(zVar) == 2 && "onMetaData".equals(n(zVar)) && zVar.a() != 0 && o(zVar) == 8) {
            HashMap<String, Object> k8 = k(zVar);
            Object obj = k8.get("duration");
            if (obj instanceof Double) {
                double doubleValue = ((Double) obj).doubleValue();
                if (doubleValue > 0.0d) {
                    this.f9685b = (long) (doubleValue * 1000000.0d);
                }
            }
            Object obj2 = k8.get("keyframes");
            if (obj2 instanceof Map) {
                Map map = (Map) obj2;
                Object obj3 = map.get("filepositions");
                Object obj4 = map.get("times");
                if ((obj3 instanceof List) && (obj4 instanceof List)) {
                    List list = (List) obj3;
                    List list2 = (List) obj4;
                    int size = list2.size();
                    this.f9686c = new long[size];
                    this.f9687d = new long[size];
                    for (int i8 = 0; i8 < size; i8++) {
                        Object obj5 = list.get(i8);
                        Object obj6 = list2.get(i8);
                        if (!(obj6 instanceof Double) || !(obj5 instanceof Double)) {
                            this.f9686c = new long[0];
                            this.f9687d = new long[0];
                            break;
                        }
                        this.f9686c[i8] = (long) (((Double) obj6).doubleValue() * 1000000.0d);
                        this.f9687d[i8] = ((Double) obj5).longValue();
                    }
                }
            }
            return false;
        }
        return false;
    }

    public long d() {
        return this.f9685b;
    }

    public long[] e() {
        return this.f9687d;
    }

    public long[] f() {
        return this.f9686c;
    }
}
