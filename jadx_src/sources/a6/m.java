package a6;

import a6.d;
import android.content.Context;
import android.os.Handler;
import b6.l0;
import b6.w;
import com.example.seedpoint.R;
import com.google.android.libraries.barhopper.RecognitionOptions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class m implements d, y {

    /* renamed from: p  reason: collision with root package name */
    public static final ImmutableList<Long> f106p = ImmutableList.I(4400000L, 3200000L, 2300000L, 1600000L, 810000L);
    public static final ImmutableList<Long> q = ImmutableList.I(1400000L, 990000L, 730000L, 510000L, 230000L);

    /* renamed from: r  reason: collision with root package name */
    public static final ImmutableList<Long> f107r = ImmutableList.I(2100000L, 1400000L, 1000000L, 890000L, 640000L);

    /* renamed from: s  reason: collision with root package name */
    public static final ImmutableList<Long> f108s = ImmutableList.I(2600000L, 1700000L, 1300000L, 1000000L, 700000L);

    /* renamed from: t  reason: collision with root package name */
    public static final ImmutableList<Long> f109t = ImmutableList.I(5700000L, 3700000L, 2300000L, 1700000L, 990000L);

    /* renamed from: u  reason: collision with root package name */
    public static final ImmutableList<Long> f110u = ImmutableList.I(2800000L, 1800000L, 1400000L, 1100000L, 870000L);

    /* renamed from: v  reason: collision with root package name */
    private static m f111v;

    /* renamed from: a  reason: collision with root package name */
    private final ImmutableMap<Integer, Long> f112a;

    /* renamed from: b  reason: collision with root package name */
    private final d.a.C0002a f113b;

    /* renamed from: c  reason: collision with root package name */
    private final w f114c;

    /* renamed from: d  reason: collision with root package name */
    private final b6.d f115d;

    /* renamed from: e  reason: collision with root package name */
    private final boolean f116e;

    /* renamed from: f  reason: collision with root package name */
    private int f117f;

    /* renamed from: g  reason: collision with root package name */
    private long f118g;

    /* renamed from: h  reason: collision with root package name */
    private long f119h;

    /* renamed from: i  reason: collision with root package name */
    private int f120i;

    /* renamed from: j  reason: collision with root package name */
    private long f121j;

    /* renamed from: k  reason: collision with root package name */
    private long f122k;

    /* renamed from: l  reason: collision with root package name */
    private long f123l;

    /* renamed from: m  reason: collision with root package name */
    private long f124m;

    /* renamed from: n  reason: collision with root package name */
    private boolean f125n;

    /* renamed from: o  reason: collision with root package name */
    private int f126o;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {

        /* renamed from: a  reason: collision with root package name */
        private final Context f127a;

        /* renamed from: b  reason: collision with root package name */
        private Map<Integer, Long> f128b;

        /* renamed from: c  reason: collision with root package name */
        private int f129c;

        /* renamed from: d  reason: collision with root package name */
        private b6.d f130d;

        /* renamed from: e  reason: collision with root package name */
        private boolean f131e;

        public b(Context context) {
            this.f127a = context == null ? null : context.getApplicationContext();
            this.f128b = b(l0.N(context));
            this.f129c = 2000;
            this.f130d = b6.d.f8029a;
            this.f131e = true;
        }

        private static Map<Integer, Long> b(String str) {
            int[] l8 = m.l(str);
            HashMap hashMap = new HashMap(8);
            hashMap.put(0, 1000000L);
            ImmutableList<Long> immutableList = m.f106p;
            hashMap.put(2, immutableList.get(l8[0]));
            hashMap.put(3, m.q.get(l8[1]));
            hashMap.put(4, m.f107r.get(l8[2]));
            hashMap.put(5, m.f108s.get(l8[3]));
            hashMap.put(10, m.f109t.get(l8[4]));
            hashMap.put(9, m.f110u.get(l8[5]));
            hashMap.put(7, immutableList.get(l8[0]));
            return hashMap;
        }

        public m a() {
            return new m(this.f127a, this.f128b, this.f129c, this.f130d, this.f131e);
        }
    }

    private m(Context context, Map<Integer, Long> map, int i8, b6.d dVar, boolean z4) {
        this.f112a = ImmutableMap.c(map);
        this.f113b = new d.a.C0002a();
        this.f114c = new w(i8);
        this.f115d = dVar;
        this.f116e = z4;
        if (context == null) {
            this.f120i = 0;
            this.f123l = m(0);
            return;
        }
        b6.w d8 = b6.w.d(context);
        int f5 = d8.f();
        this.f120i = f5;
        this.f123l = m(f5);
        d8.i(new w.c() { // from class: a6.l
            @Override // b6.w.c
            public final void a(int i9) {
                m.this.q(i9);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int[] l(String str) {
        str.hashCode();
        char c9 = 65535;
        switch (str.hashCode()) {
            case 2083:
                if (str.equals("AD")) {
                    c9 = 0;
                    break;
                }
                break;
            case 2084:
                if (str.equals("AE")) {
                    c9 = 1;
                    break;
                }
                break;
            case 2085:
                if (str.equals("AF")) {
                    c9 = 2;
                    break;
                }
                break;
            case 2086:
                if (str.equals("AG")) {
                    c9 = 3;
                    break;
                }
                break;
            case 2088:
                if (str.equals("AI")) {
                    c9 = 4;
                    break;
                }
                break;
            case 2091:
                if (str.equals("AL")) {
                    c9 = 5;
                    break;
                }
                break;
            case 2092:
                if (str.equals("AM")) {
                    c9 = 6;
                    break;
                }
                break;
            case 2094:
                if (str.equals("AO")) {
                    c9 = 7;
                    break;
                }
                break;
            case 2096:
                if (str.equals("AQ")) {
                    c9 = '\b';
                    break;
                }
                break;
            case 2098:
                if (str.equals("AS")) {
                    c9 = '\t';
                    break;
                }
                break;
            case 2099:
                if (str.equals("AT")) {
                    c9 = '\n';
                    break;
                }
                break;
            case 2100:
                if (str.equals("AU")) {
                    c9 = 11;
                    break;
                }
                break;
            case 2102:
                if (str.equals("AW")) {
                    c9 = '\f';
                    break;
                }
                break;
            case 2103:
                if (str.equals("AX")) {
                    c9 = '\r';
                    break;
                }
                break;
            case 2105:
                if (str.equals("AZ")) {
                    c9 = 14;
                    break;
                }
                break;
            case 2111:
                if (str.equals("BA")) {
                    c9 = 15;
                    break;
                }
                break;
            case 2112:
                if (str.equals("BB")) {
                    c9 = 16;
                    break;
                }
                break;
            case 2114:
                if (str.equals("BD")) {
                    c9 = 17;
                    break;
                }
                break;
            case 2115:
                if (str.equals("BE")) {
                    c9 = 18;
                    break;
                }
                break;
            case 2116:
                if (str.equals("BF")) {
                    c9 = 19;
                    break;
                }
                break;
            case 2117:
                if (str.equals("BG")) {
                    c9 = 20;
                    break;
                }
                break;
            case 2118:
                if (str.equals("BH")) {
                    c9 = 21;
                    break;
                }
                break;
            case 2119:
                if (str.equals("BI")) {
                    c9 = 22;
                    break;
                }
                break;
            case 2120:
                if (str.equals("BJ")) {
                    c9 = 23;
                    break;
                }
                break;
            case 2122:
                if (str.equals("BL")) {
                    c9 = 24;
                    break;
                }
                break;
            case 2123:
                if (str.equals("BM")) {
                    c9 = 25;
                    break;
                }
                break;
            case 2124:
                if (str.equals("BN")) {
                    c9 = 26;
                    break;
                }
                break;
            case 2125:
                if (str.equals("BO")) {
                    c9 = 27;
                    break;
                }
                break;
            case 2127:
                if (str.equals("BQ")) {
                    c9 = 28;
                    break;
                }
                break;
            case 2128:
                if (str.equals("BR")) {
                    c9 = 29;
                    break;
                }
                break;
            case 2129:
                if (str.equals("BS")) {
                    c9 = 30;
                    break;
                }
                break;
            case 2130:
                if (str.equals("BT")) {
                    c9 = 31;
                    break;
                }
                break;
            case 2133:
                if (str.equals("BW")) {
                    c9 = ' ';
                    break;
                }
                break;
            case 2135:
                if (str.equals("BY")) {
                    c9 = '!';
                    break;
                }
                break;
            case 2136:
                if (str.equals("BZ")) {
                    c9 = '\"';
                    break;
                }
                break;
            case 2142:
                if (str.equals("CA")) {
                    c9 = '#';
                    break;
                }
                break;
            case 2145:
                if (str.equals("CD")) {
                    c9 = '$';
                    break;
                }
                break;
            case 2147:
                if (str.equals("CF")) {
                    c9 = '%';
                    break;
                }
                break;
            case 2148:
                if (str.equals("CG")) {
                    c9 = '&';
                    break;
                }
                break;
            case 2149:
                if (str.equals("CH")) {
                    c9 = '\'';
                    break;
                }
                break;
            case 2150:
                if (str.equals("CI")) {
                    c9 = '(';
                    break;
                }
                break;
            case 2152:
                if (str.equals("CK")) {
                    c9 = ')';
                    break;
                }
                break;
            case 2153:
                if (str.equals("CL")) {
                    c9 = '*';
                    break;
                }
                break;
            case 2154:
                if (str.equals("CM")) {
                    c9 = '+';
                    break;
                }
                break;
            case 2155:
                if (str.equals("CN")) {
                    c9 = ',';
                    break;
                }
                break;
            case 2156:
                if (str.equals("CO")) {
                    c9 = '-';
                    break;
                }
                break;
            case 2159:
                if (str.equals("CR")) {
                    c9 = '.';
                    break;
                }
                break;
            case 2162:
                if (str.equals("CU")) {
                    c9 = '/';
                    break;
                }
                break;
            case 2163:
                if (str.equals("CV")) {
                    c9 = '0';
                    break;
                }
                break;
            case 2164:
                if (str.equals("CW")) {
                    c9 = '1';
                    break;
                }
                break;
            case 2165:
                if (str.equals("CX")) {
                    c9 = '2';
                    break;
                }
                break;
            case 2166:
                if (str.equals("CY")) {
                    c9 = '3';
                    break;
                }
                break;
            case 2167:
                if (str.equals("CZ")) {
                    c9 = '4';
                    break;
                }
                break;
            case 2177:
                if (str.equals("DE")) {
                    c9 = '5';
                    break;
                }
                break;
            case 2182:
                if (str.equals("DJ")) {
                    c9 = '6';
                    break;
                }
                break;
            case 2183:
                if (str.equals("DK")) {
                    c9 = '7';
                    break;
                }
                break;
            case 2185:
                if (str.equals("DM")) {
                    c9 = '8';
                    break;
                }
                break;
            case 2187:
                if (str.equals("DO")) {
                    c9 = '9';
                    break;
                }
                break;
            case 2198:
                if (str.equals("DZ")) {
                    c9 = ':';
                    break;
                }
                break;
            case 2206:
                if (str.equals("EC")) {
                    c9 = ';';
                    break;
                }
                break;
            case 2208:
                if (str.equals("EE")) {
                    c9 = '<';
                    break;
                }
                break;
            case 2210:
                if (str.equals("EG")) {
                    c9 = '=';
                    break;
                }
                break;
            case 2221:
                if (str.equals("ER")) {
                    c9 = '>';
                    break;
                }
                break;
            case 2222:
                if (str.equals("ES")) {
                    c9 = '?';
                    break;
                }
                break;
            case 2223:
                if (str.equals("ET")) {
                    c9 = '@';
                    break;
                }
                break;
            case 2243:
                if (str.equals("FI")) {
                    c9 = 'A';
                    break;
                }
                break;
            case 2244:
                if (str.equals("FJ")) {
                    c9 = 'B';
                    break;
                }
                break;
            case 2247:
                if (str.equals("FM")) {
                    c9 = 'C';
                    break;
                }
                break;
            case 2249:
                if (str.equals("FO")) {
                    c9 = 'D';
                    break;
                }
                break;
            case 2252:
                if (str.equals("FR")) {
                    c9 = 'E';
                    break;
                }
                break;
            case 2266:
                if (str.equals("GA")) {
                    c9 = 'F';
                    break;
                }
                break;
            case 2267:
                if (str.equals("GB")) {
                    c9 = 'G';
                    break;
                }
                break;
            case 2269:
                if (str.equals("GD")) {
                    c9 = 'H';
                    break;
                }
                break;
            case 2270:
                if (str.equals("GE")) {
                    c9 = 'I';
                    break;
                }
                break;
            case 2271:
                if (str.equals("GF")) {
                    c9 = 'J';
                    break;
                }
                break;
            case 2272:
                if (str.equals("GG")) {
                    c9 = 'K';
                    break;
                }
                break;
            case 2273:
                if (str.equals("GH")) {
                    c9 = 'L';
                    break;
                }
                break;
            case 2274:
                if (str.equals("GI")) {
                    c9 = 'M';
                    break;
                }
                break;
            case 2277:
                if (str.equals("GL")) {
                    c9 = 'N';
                    break;
                }
                break;
            case 2278:
                if (str.equals("GM")) {
                    c9 = 'O';
                    break;
                }
                break;
            case 2279:
                if (str.equals("GN")) {
                    c9 = 'P';
                    break;
                }
                break;
            case 2281:
                if (str.equals("GP")) {
                    c9 = 'Q';
                    break;
                }
                break;
            case 2282:
                if (str.equals("GQ")) {
                    c9 = 'R';
                    break;
                }
                break;
            case 2283:
                if (str.equals("GR")) {
                    c9 = 'S';
                    break;
                }
                break;
            case 2285:
                if (str.equals("GT")) {
                    c9 = 'T';
                    break;
                }
                break;
            case 2286:
                if (str.equals("GU")) {
                    c9 = 'U';
                    break;
                }
                break;
            case 2288:
                if (str.equals("GW")) {
                    c9 = 'V';
                    break;
                }
                break;
            case 2290:
                if (str.equals("GY")) {
                    c9 = 'W';
                    break;
                }
                break;
            case 2307:
                if (str.equals("HK")) {
                    c9 = 'X';
                    break;
                }
                break;
            case 2310:
                if (str.equals("HN")) {
                    c9 = 'Y';
                    break;
                }
                break;
            case 2314:
                if (str.equals("HR")) {
                    c9 = 'Z';
                    break;
                }
                break;
            case 2316:
                if (str.equals("HT")) {
                    c9 = '[';
                    break;
                }
                break;
            case 2317:
                if (str.equals("HU")) {
                    c9 = '\\';
                    break;
                }
                break;
            case 2331:
                if (str.equals("ID")) {
                    c9 = ']';
                    break;
                }
                break;
            case 2332:
                if (str.equals("IE")) {
                    c9 = '^';
                    break;
                }
                break;
            case 2339:
                if (str.equals("IL")) {
                    c9 = '_';
                    break;
                }
                break;
            case 2340:
                if (str.equals("IM")) {
                    c9 = '`';
                    break;
                }
                break;
            case 2341:
                if (str.equals("IN")) {
                    c9 = 'a';
                    break;
                }
                break;
            case 2342:
                if (str.equals("IO")) {
                    c9 = 'b';
                    break;
                }
                break;
            case 2344:
                if (str.equals("IQ")) {
                    c9 = 'c';
                    break;
                }
                break;
            case 2345:
                if (str.equals("IR")) {
                    c9 = 'd';
                    break;
                }
                break;
            case 2346:
                if (str.equals("IS")) {
                    c9 = 'e';
                    break;
                }
                break;
            case 2347:
                if (str.equals("IT")) {
                    c9 = 'f';
                    break;
                }
                break;
            case 2363:
                if (str.equals("JE")) {
                    c9 = 'g';
                    break;
                }
                break;
            case 2371:
                if (str.equals("JM")) {
                    c9 = 'h';
                    break;
                }
                break;
            case 2373:
                if (str.equals("JO")) {
                    c9 = 'i';
                    break;
                }
                break;
            case 2374:
                if (str.equals("JP")) {
                    c9 = 'j';
                    break;
                }
                break;
            case 2394:
                if (str.equals("KE")) {
                    c9 = 'k';
                    break;
                }
                break;
            case 2396:
                if (str.equals("KG")) {
                    c9 = 'l';
                    break;
                }
                break;
            case 2397:
                if (str.equals("KH")) {
                    c9 = 'm';
                    break;
                }
                break;
            case 2398:
                if (str.equals("KI")) {
                    c9 = 'n';
                    break;
                }
                break;
            case 2402:
                if (str.equals("KM")) {
                    c9 = 'o';
                    break;
                }
                break;
            case 2403:
                if (str.equals("KN")) {
                    c9 = 'p';
                    break;
                }
                break;
            case 2407:
                if (str.equals("KR")) {
                    c9 = 'q';
                    break;
                }
                break;
            case 2412:
                if (str.equals("KW")) {
                    c9 = 'r';
                    break;
                }
                break;
            case 2414:
                if (str.equals("KY")) {
                    c9 = 's';
                    break;
                }
                break;
            case 2415:
                if (str.equals("KZ")) {
                    c9 = 't';
                    break;
                }
                break;
            case 2421:
                if (str.equals("LA")) {
                    c9 = 'u';
                    break;
                }
                break;
            case 2422:
                if (str.equals("LB")) {
                    c9 = 'v';
                    break;
                }
                break;
            case 2423:
                if (str.equals("LC")) {
                    c9 = 'w';
                    break;
                }
                break;
            case 2429:
                if (str.equals("LI")) {
                    c9 = 'x';
                    break;
                }
                break;
            case 2431:
                if (str.equals("LK")) {
                    c9 = 'y';
                    break;
                }
                break;
            case 2438:
                if (str.equals("LR")) {
                    c9 = 'z';
                    break;
                }
                break;
            case 2439:
                if (str.equals("LS")) {
                    c9 = '{';
                    break;
                }
                break;
            case 2440:
                if (str.equals("LT")) {
                    c9 = '|';
                    break;
                }
                break;
            case 2441:
                if (str.equals("LU")) {
                    c9 = '}';
                    break;
                }
                break;
            case 2442:
                if (str.equals("LV")) {
                    c9 = '~';
                    break;
                }
                break;
            case 2445:
                if (str.equals("LY")) {
                    c9 = 127;
                    break;
                }
                break;
            case 2452:
                if (str.equals("MA")) {
                    c9 = 128;
                    break;
                }
                break;
            case 2454:
                if (str.equals("MC")) {
                    c9 = 129;
                    break;
                }
                break;
            case 2455:
                if (str.equals("MD")) {
                    c9 = 130;
                    break;
                }
                break;
            case 2456:
                if (str.equals("ME")) {
                    c9 = 131;
                    break;
                }
                break;
            case 2457:
                if (str.equals("MF")) {
                    c9 = 132;
                    break;
                }
                break;
            case 2458:
                if (str.equals("MG")) {
                    c9 = 133;
                    break;
                }
                break;
            case 2459:
                if (str.equals("MH")) {
                    c9 = 134;
                    break;
                }
                break;
            case 2462:
                if (str.equals("MK")) {
                    c9 = 135;
                    break;
                }
                break;
            case 2463:
                if (str.equals("ML")) {
                    c9 = 136;
                    break;
                }
                break;
            case 2464:
                if (str.equals("MM")) {
                    c9 = 137;
                    break;
                }
                break;
            case 2465:
                if (str.equals("MN")) {
                    c9 = 138;
                    break;
                }
                break;
            case 2466:
                if (str.equals("MO")) {
                    c9 = 139;
                    break;
                }
                break;
            case 2467:
                if (str.equals("MP")) {
                    c9 = 140;
                    break;
                }
                break;
            case 2468:
                if (str.equals("MQ")) {
                    c9 = 141;
                    break;
                }
                break;
            case 2469:
                if (str.equals("MR")) {
                    c9 = 142;
                    break;
                }
                break;
            case 2470:
                if (str.equals("MS")) {
                    c9 = 143;
                    break;
                }
                break;
            case 2471:
                if (str.equals("MT")) {
                    c9 = 144;
                    break;
                }
                break;
            case 2472:
                if (str.equals("MU")) {
                    c9 = 145;
                    break;
                }
                break;
            case 2473:
                if (str.equals("MV")) {
                    c9 = 146;
                    break;
                }
                break;
            case 2474:
                if (str.equals("MW")) {
                    c9 = 147;
                    break;
                }
                break;
            case 2475:
                if (str.equals("MX")) {
                    c9 = 148;
                    break;
                }
                break;
            case 2476:
                if (str.equals("MY")) {
                    c9 = 149;
                    break;
                }
                break;
            case 2477:
                if (str.equals("MZ")) {
                    c9 = 150;
                    break;
                }
                break;
            case 2483:
                if (str.equals("NA")) {
                    c9 = 151;
                    break;
                }
                break;
            case 2485:
                if (str.equals("NC")) {
                    c9 = 152;
                    break;
                }
                break;
            case 2487:
                if (str.equals("NE")) {
                    c9 = 153;
                    break;
                }
                break;
            case 2489:
                if (str.equals("NG")) {
                    c9 = 154;
                    break;
                }
                break;
            case 2491:
                if (str.equals("NI")) {
                    c9 = 155;
                    break;
                }
                break;
            case 2494:
                if (str.equals("NL")) {
                    c9 = 156;
                    break;
                }
                break;
            case 2497:
                if (str.equals("NO")) {
                    c9 = 157;
                    break;
                }
                break;
            case 2498:
                if (str.equals("NP")) {
                    c9 = 158;
                    break;
                }
                break;
            case 2500:
                if (str.equals("NR")) {
                    c9 = 159;
                    break;
                }
                break;
            case 2503:
                if (str.equals("NU")) {
                    c9 = 160;
                    break;
                }
                break;
            case 2508:
                if (str.equals("NZ")) {
                    c9 = 161;
                    break;
                }
                break;
            case 2526:
                if (str.equals("OM")) {
                    c9 = 162;
                    break;
                }
                break;
            case 2545:
                if (str.equals("PA")) {
                    c9 = 163;
                    break;
                }
                break;
            case 2549:
                if (str.equals("PE")) {
                    c9 = 164;
                    break;
                }
                break;
            case 2550:
                if (str.equals("PF")) {
                    c9 = 165;
                    break;
                }
                break;
            case 2551:
                if (str.equals("PG")) {
                    c9 = 166;
                    break;
                }
                break;
            case 2552:
                if (str.equals("PH")) {
                    c9 = 167;
                    break;
                }
                break;
            case 2555:
                if (str.equals("PK")) {
                    c9 = 168;
                    break;
                }
                break;
            case 2556:
                if (str.equals("PL")) {
                    c9 = 169;
                    break;
                }
                break;
            case 2557:
                if (str.equals("PM")) {
                    c9 = 170;
                    break;
                }
                break;
            case 2562:
                if (str.equals("PR")) {
                    c9 = 171;
                    break;
                }
                break;
            case 2563:
                if (str.equals("PS")) {
                    c9 = 172;
                    break;
                }
                break;
            case 2564:
                if (str.equals("PT")) {
                    c9 = 173;
                    break;
                }
                break;
            case 2567:
                if (str.equals("PW")) {
                    c9 = 174;
                    break;
                }
                break;
            case 2569:
                if (str.equals("PY")) {
                    c9 = 175;
                    break;
                }
                break;
            case 2576:
                if (str.equals("QA")) {
                    c9 = 176;
                    break;
                }
                break;
            case 2611:
                if (str.equals("RE")) {
                    c9 = 177;
                    break;
                }
                break;
            case 2621:
                if (str.equals("RO")) {
                    c9 = 178;
                    break;
                }
                break;
            case 2625:
                if (str.equals("RS")) {
                    c9 = 179;
                    break;
                }
                break;
            case 2627:
                if (str.equals("RU")) {
                    c9 = 180;
                    break;
                }
                break;
            case 2629:
                if (str.equals("RW")) {
                    c9 = 181;
                    break;
                }
                break;
            case 2638:
                if (str.equals("SA")) {
                    c9 = 182;
                    break;
                }
                break;
            case 2639:
                if (str.equals("SB")) {
                    c9 = 183;
                    break;
                }
                break;
            case 2640:
                if (str.equals("SC")) {
                    c9 = 184;
                    break;
                }
                break;
            case 2641:
                if (str.equals("SD")) {
                    c9 = 185;
                    break;
                }
                break;
            case 2642:
                if (str.equals("SE")) {
                    c9 = 186;
                    break;
                }
                break;
            case 2644:
                if (str.equals("SG")) {
                    c9 = 187;
                    break;
                }
                break;
            case 2645:
                if (str.equals("SH")) {
                    c9 = 188;
                    break;
                }
                break;
            case 2646:
                if (str.equals("SI")) {
                    c9 = 189;
                    break;
                }
                break;
            case 2647:
                if (str.equals("SJ")) {
                    c9 = 190;
                    break;
                }
                break;
            case 2648:
                if (str.equals("SK")) {
                    c9 = 191;
                    break;
                }
                break;
            case 2649:
                if (str.equals("SL")) {
                    c9 = 192;
                    break;
                }
                break;
            case 2650:
                if (str.equals("SM")) {
                    c9 = 193;
                    break;
                }
                break;
            case 2651:
                if (str.equals("SN")) {
                    c9 = 194;
                    break;
                }
                break;
            case 2652:
                if (str.equals("SO")) {
                    c9 = 195;
                    break;
                }
                break;
            case 2655:
                if (str.equals("SR")) {
                    c9 = 196;
                    break;
                }
                break;
            case 2656:
                if (str.equals("SS")) {
                    c9 = 197;
                    break;
                }
                break;
            case 2657:
                if (str.equals("ST")) {
                    c9 = 198;
                    break;
                }
                break;
            case 2659:
                if (str.equals("SV")) {
                    c9 = 199;
                    break;
                }
                break;
            case 2661:
                if (str.equals("SX")) {
                    c9 = 200;
                    break;
                }
                break;
            case 2662:
                if (str.equals("SY")) {
                    c9 = 201;
                    break;
                }
                break;
            case 2663:
                if (str.equals("SZ")) {
                    c9 = 202;
                    break;
                }
                break;
            case 2671:
                if (str.equals("TC")) {
                    c9 = 203;
                    break;
                }
                break;
            case 2672:
                if (str.equals("TD")) {
                    c9 = 204;
                    break;
                }
                break;
            case 2675:
                if (str.equals("TG")) {
                    c9 = 205;
                    break;
                }
                break;
            case 2676:
                if (str.equals("TH")) {
                    c9 = 206;
                    break;
                }
                break;
            case 2678:
                if (str.equals("TJ")) {
                    c9 = 207;
                    break;
                }
                break;
            case 2679:
                if (str.equals("TK")) {
                    c9 = 208;
                    break;
                }
                break;
            case 2680:
                if (str.equals("TL")) {
                    c9 = 209;
                    break;
                }
                break;
            case 2681:
                if (str.equals("TM")) {
                    c9 = 210;
                    break;
                }
                break;
            case 2682:
                if (str.equals("TN")) {
                    c9 = 211;
                    break;
                }
                break;
            case 2683:
                if (str.equals("TO")) {
                    c9 = 212;
                    break;
                }
                break;
            case 2686:
                if (str.equals("TR")) {
                    c9 = 213;
                    break;
                }
                break;
            case 2688:
                if (str.equals("TT")) {
                    c9 = 214;
                    break;
                }
                break;
            case 2690:
                if (str.equals("TV")) {
                    c9 = 215;
                    break;
                }
                break;
            case 2691:
                if (str.equals("TW")) {
                    c9 = 216;
                    break;
                }
                break;
            case 2694:
                if (str.equals("TZ")) {
                    c9 = 217;
                    break;
                }
                break;
            case 2700:
                if (str.equals("UA")) {
                    c9 = 218;
                    break;
                }
                break;
            case 2706:
                if (str.equals("UG")) {
                    c9 = 219;
                    break;
                }
                break;
            case 2718:
                if (str.equals("US")) {
                    c9 = 220;
                    break;
                }
                break;
            case 2724:
                if (str.equals("UY")) {
                    c9 = 221;
                    break;
                }
                break;
            case 2725:
                if (str.equals("UZ")) {
                    c9 = 222;
                    break;
                }
                break;
            case 2731:
                if (str.equals("VA")) {
                    c9 = 223;
                    break;
                }
                break;
            case 2733:
                if (str.equals("VC")) {
                    c9 = 224;
                    break;
                }
                break;
            case 2735:
                if (str.equals("VE")) {
                    c9 = 225;
                    break;
                }
                break;
            case 2737:
                if (str.equals("VG")) {
                    c9 = 226;
                    break;
                }
                break;
            case 2739:
                if (str.equals("VI")) {
                    c9 = 227;
                    break;
                }
                break;
            case 2744:
                if (str.equals("VN")) {
                    c9 = 228;
                    break;
                }
                break;
            case 2751:
                if (str.equals("VU")) {
                    c9 = 229;
                    break;
                }
                break;
            case 2767:
                if (str.equals("WF")) {
                    c9 = 230;
                    break;
                }
                break;
            case 2780:
                if (str.equals("WS")) {
                    c9 = 231;
                    break;
                }
                break;
            case 2803:
                if (str.equals("XK")) {
                    c9 = 232;
                    break;
                }
                break;
            case 2828:
                if (str.equals("YE")) {
                    c9 = 233;
                    break;
                }
                break;
            case 2843:
                if (str.equals("YT")) {
                    c9 = 234;
                    break;
                }
                break;
            case 2855:
                if (str.equals("ZA")) {
                    c9 = 235;
                    break;
                }
                break;
            case 2867:
                if (str.equals("ZM")) {
                    c9 = 236;
                    break;
                }
                break;
            case 2877:
                if (str.equals("ZW")) {
                    c9 = 237;
                    break;
                }
                break;
        }
        int[] iArr = {2, 2, 0, 0, 2, 2};
        switch (c9) {
            case 0:
            case '1':
                return iArr;
            case 1:
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 4;
                iArr[2] = 3;
                iArr[3] = 4;
                iArr[4] = 4;
                iArr[5] = 2;
                return iArr;
            case 2:
            case 166:
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 3;
                iArr[2] = 3;
                iArr[3] = 3;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 3:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 4;
                iArr[2] = 3;
                iArr[3] = 4;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 4:
            case 16:
            case 25:
            case 28:
            case '8':
            case 'D':
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 2;
                iArr[2] = 0;
                iArr[3] = 0;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 5:
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 1;
                iArr[2] = 1;
                iArr[3] = 3;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 6:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 3;
                iArr[2] = 2;
                iArr[3] = 3;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 7:
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 4;
                iArr[2] = 4;
                iArr[3] = 3;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case '\b':
            case '>':
            case 188:
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 2;
                iArr[2] = 2;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case '\t':
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 2;
                iArr[2] = 3;
                iArr[3] = 3;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case '\n':
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 2;
                iArr[2] = 1;
                iArr[3] = 4;
                iArr[4] = 1;
                iArr[5] = 4;
                return iArr;
            case 11:
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 2;
                iArr[2] = 1;
                iArr[3] = 1;
                iArr[4] = 3;
                iArr[5] = 0;
                return iArr;
            case '\f':
            case 'U':
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 2;
                iArr[2] = 4;
                iArr[3] = 4;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case '\r':
            case '2':
            case 'x':
            case 140:
            case 143:
            case 170:
            case 193:
            case 223:
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 2;
                iArr[2] = 2;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 14:
            case 19:
            case ':':
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 3;
                iArr[2] = 4;
                iArr[3] = 4;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 15:
            case '^':
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 1;
                iArr[2] = 1;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 17:
            case 't':
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 1;
                iArr[2] = 2;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 18:
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 1;
                iArr[2] = 4;
                iArr[3] = 4;
                iArr[4] = 3;
                iArr[5] = 2;
                return iArr;
            case 20:
            case '?':
            case 'S':
            case 189:
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 0;
                iArr[2] = 0;
                iArr[3] = 0;
                iArr[4] = 1;
                iArr[5] = 2;
                return iArr;
            case 21:
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 3;
                iArr[2] = 1;
                iArr[3] = 4;
                iArr[4] = 4;
                iArr[5] = 2;
                return iArr;
            case 22:
            case '[':
            case 133:
            case 153:
            case 204:
            case 225:
            case 233:
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 4;
                iArr[2] = 4;
                iArr[3] = 4;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 23:
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 4;
                iArr[2] = 2;
                iArr[3] = 3;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 24:
            case 132:
            case 175:
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 2;
                iArr[2] = 2;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 26:
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 2;
                iArr[2] = 0;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 27:
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 2;
                iArr[2] = 3;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 29:
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 1;
                iArr[2] = 2;
                iArr[3] = 1;
                iArr[4] = 1;
                iArr[5] = 0;
                return iArr;
            case 30:
            case 'v':
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 2;
                iArr[2] = 1;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 31:
            case 150:
            case 231:
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 1;
                iArr[2] = 2;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case ' ':
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 2;
                iArr[2] = 1;
                iArr[3] = 0;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case '!':
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 1;
                iArr[2] = 2;
                iArr[3] = 3;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case '\"':
            case ')':
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 2;
                iArr[2] = 2;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case '#':
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 2;
                iArr[2] = 3;
                iArr[3] = 3;
                iArr[4] = 3;
                iArr[5] = 3;
                return iArr;
            case '$':
            case R.styleable.AppCompatTheme_textColorSearchUrl /* 111 */:
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 3;
                iArr[2] = 3;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case '%':
            case 183:
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 2;
                iArr[2] = 4;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case '&':
            case 'L':
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 3;
                iArr[2] = 3;
                iArr[3] = 3;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case '\'':
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 0;
                iArr[2] = 0;
                iArr[3] = 0;
                iArr[4] = 0;
                iArr[5] = 3;
                return iArr;
            case '(':
            case '=':
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 4;
                iArr[2] = 3;
                iArr[3] = 3;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case '*':
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 1;
                iArr[2] = 2;
                iArr[3] = 1;
                iArr[4] = 3;
                iArr[5] = 2;
                return iArr;
            case '+':
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 3;
                iArr[2] = 3;
                iArr[3] = 4;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case ',':
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 0;
                iArr[2] = 4;
                iArr[3] = 3;
                iArr[4] = 3;
                iArr[5] = 1;
                return iArr;
            case '-':
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 3;
                iArr[2] = 4;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case '.':
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 4;
                iArr[2] = 4;
                iArr[3] = 4;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case '/':
            case R.styleable.AppCompatTheme_textColorAlertDialogListItem /* 110 */:
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 2;
                iArr[2] = 4;
                iArr[3] = 3;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case '0':
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 3;
                iArr[2] = 0;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case '3':
            case 'Z':
            case '~':
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 0;
                iArr[2] = 0;
                iArr[3] = 0;
                iArr[4] = 0;
                iArr[5] = 2;
                return iArr;
            case '4':
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 0;
                iArr[2] = 2;
                iArr[3] = 0;
                iArr[4] = 1;
                iArr[5] = 2;
                return iArr;
            case '5':
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 1;
                iArr[2] = 3;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case '6':
            case 201:
            case 207:
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 3;
                iArr[2] = 4;
                iArr[3] = 4;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case '7':
            case '<':
            case '\\':
            case '|':
            case 144:
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 0;
                iArr[2] = 0;
                iArr[3] = 0;
                iArr[4] = 0;
                iArr[5] = 2;
                return iArr;
            case '9':
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 4;
                iArr[2] = 4;
                iArr[3] = 4;
                iArr[4] = 4;
                iArr[5] = 2;
                return iArr;
            case ';':
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 3;
                iArr[2] = 2;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case '@':
            case 194:
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 4;
                iArr[2] = 3;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 'A':
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 0;
                iArr[2] = 0;
                iArr[3] = 2;
                iArr[4] = 0;
                iArr[5] = 2;
                return iArr;
            case 'B':
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 1;
                iArr[2] = 2;
                iArr[3] = 3;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 'C':
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 2;
                iArr[2] = 3;
                iArr[3] = 0;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 'E':
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 1;
                iArr[2] = 2;
                iArr[3] = 1;
                iArr[4] = 1;
                iArr[5] = 2;
                return iArr;
            case 'F':
            case 205:
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 4;
                iArr[2] = 1;
                iArr[3] = 0;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 'G':
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 1;
                iArr[2] = 1;
                iArr[3] = 2;
                iArr[4] = 1;
                iArr[5] = 2;
                return iArr;
            case 'H':
            case R.styleable.AppCompatTheme_toolbarNavigationButtonStyle /* 112 */:
            case R.styleable.AppCompatTheme_tooltipFrameBackground /* 115 */:
            case 'w':
            case 200:
            case 224:
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 2;
                iArr[2] = 0;
                iArr[3] = 0;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 'I':
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 0;
                iArr[2] = 0;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 'J':
            case 168:
            case 192:
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 2;
                iArr[2] = 3;
                iArr[3] = 3;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 'K':
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 2;
                iArr[2] = 1;
                iArr[3] = 0;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 'M':
            case 'g':
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 2;
                iArr[2] = 0;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 'N':
            case 208:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 2;
                iArr[2] = 2;
                iArr[3] = 4;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 'O':
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 3;
                iArr[2] = 2;
                iArr[3] = 4;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 'P':
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 4;
                iArr[2] = 4;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 'Q':
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 1;
                iArr[2] = 1;
                iArr[3] = 3;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 'R':
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 4;
                iArr[2] = 3;
                iArr[3] = 3;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 'T':
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 2;
                iArr[2] = 2;
                iArr[3] = 1;
                iArr[4] = 1;
                iArr[5] = 2;
                return iArr;
            case 'V':
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 4;
                iArr[2] = 2;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 'W':
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 0;
                iArr[2] = 1;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 'X':
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 1;
                iArr[2] = 1;
                iArr[3] = 3;
                iArr[4] = 2;
                iArr[5] = 0;
                return iArr;
            case 'Y':
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 3;
                iArr[2] = 2;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case ']':
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 1;
                iArr[2] = 1;
                iArr[3] = 2;
                iArr[4] = 3;
                iArr[5] = 2;
                return iArr;
            case '_':
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 2;
                iArr[2] = 2;
                iArr[3] = 3;
                iArr[4] = 4;
                iArr[5] = 2;
                return iArr;
            case '`':
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 2;
                iArr[2] = 0;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 'a':
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 1;
                iArr[2] = 2;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 1;
                return iArr;
            case 'b':
            case 215:
            case 230:
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 2;
                iArr[2] = 2;
                iArr[3] = 4;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 'c':
            case 190:
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 2;
                iArr[2] = 2;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 'd':
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 2;
                iArr[2] = 3;
                iArr[3] = 3;
                iArr[4] = 4;
                iArr[5] = 2;
                return iArr;
            case 'e':
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 0;
                iArr[2] = 1;
                iArr[3] = 0;
                iArr[4] = 0;
                iArr[5] = 2;
                return iArr;
            case 'f':
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 0;
                iArr[2] = 1;
                iArr[3] = 1;
                iArr[4] = 1;
                iArr[5] = 2;
                return iArr;
            case 'h':
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 4;
                iArr[2] = 2;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 'i':
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 0;
                iArr[2] = 1;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 'j':
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 3;
                iArr[2] = 3;
                iArr[3] = 3;
                iArr[4] = 4;
                iArr[5] = 4;
                return iArr;
            case 'k':
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 2;
                iArr[2] = 2;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 'l':
            case 141:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 1;
                iArr[2] = 1;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case R.styleable.AppCompatTheme_textAppearanceSmallPopupMenu /* 109 */:
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 0;
                iArr[2] = 4;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case R.styleable.AppCompatTheme_toolbarStyle /* 113 */:
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 2;
                iArr[2] = 2;
                iArr[3] = 4;
                iArr[4] = 4;
                iArr[5] = 4;
                return iArr;
            case R.styleable.AppCompatTheme_tooltipForegroundColor /* 114 */:
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 0;
                iArr[2] = 1;
                iArr[3] = 0;
                iArr[4] = 0;
                iArr[5] = 2;
                return iArr;
            case 'u':
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 2;
                iArr[2] = 1;
                iArr[3] = 3;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 'y':
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 2;
                iArr[2] = 3;
                iArr[3] = 4;
                iArr[4] = 4;
                iArr[5] = 2;
                return iArr;
            case 'z':
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 4;
                iArr[2] = 3;
                iArr[3] = 4;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case '{':
            case 219:
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 3;
                iArr[2] = 3;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case '}':
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 1;
                iArr[2] = 4;
                iArr[3] = 2;
                iArr[4] = 0;
                iArr[5] = 2;
                return iArr;
            case 127:
            case 212:
            case 237:
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 2;
                iArr[2] = 4;
                iArr[3] = 3;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case RecognitionOptions.ITF /* 128 */:
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 3;
                iArr[2] = 2;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 129:
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 2;
                iArr[2] = 2;
                iArr[3] = 0;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 130:
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 0;
                iArr[2] = 0;
                iArr[3] = 0;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 131:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 0;
                iArr[2] = 0;
                iArr[3] = 1;
                iArr[4] = 1;
                iArr[5] = 2;
                return iArr;
            case 134:
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 2;
                iArr[2] = 1;
                iArr[3] = 3;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 135:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 0;
                iArr[2] = 0;
                iArr[3] = 1;
                iArr[4] = 3;
                iArr[5] = 2;
                return iArr;
            case 136:
            case 217:
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 4;
                iArr[2] = 2;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 137:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 2;
                iArr[2] = 2;
                iArr[3] = 3;
                iArr[4] = 4;
                iArr[5] = 2;
                return iArr;
            case 138:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 0;
                iArr[2] = 1;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 139:
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 2;
                iArr[2] = 4;
                iArr[3] = 4;
                iArr[4] = 4;
                iArr[5] = 2;
                return iArr;
            case 142:
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 2;
                iArr[2] = 3;
                iArr[3] = 4;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 145:
            case 182:
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 1;
                iArr[2] = 1;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 146:
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 4;
                iArr[2] = 1;
                iArr[3] = 3;
                iArr[4] = 3;
                iArr[5] = 2;
                return iArr;
            case 147:
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 2;
                iArr[2] = 3;
                iArr[3] = 3;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 148:
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 4;
                iArr[2] = 4;
                iArr[3] = 4;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 149:
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 0;
                iArr[2] = 4;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 151:
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 4;
                iArr[2] = 3;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 152:
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 2;
                iArr[2] = 3;
                iArr[3] = 4;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 154:
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 4;
                iArr[2] = 2;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 155:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 3;
                iArr[2] = 4;
                iArr[3] = 3;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 156:
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 2;
                iArr[2] = 3;
                iArr[3] = 3;
                iArr[4] = 0;
                iArr[5] = 4;
                return iArr;
            case 157:
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 1;
                iArr[2] = 2;
                iArr[3] = 1;
                iArr[4] = 1;
                iArr[5] = 2;
                return iArr;
            case 158:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 1;
                iArr[2] = 4;
                iArr[3] = 3;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 159:
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 0;
                iArr[2] = 3;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 160:
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 2;
                iArr[2] = 2;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 161:
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 0;
                iArr[2] = 2;
                iArr[3] = 2;
                iArr[4] = 4;
                iArr[5] = 2;
                return iArr;
            case 162:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 3;
                iArr[2] = 1;
                iArr[3] = 3;
                iArr[4] = 4;
                iArr[5] = 2;
                return iArr;
            case 163:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 3;
                iArr[2] = 3;
                iArr[3] = 3;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 164:
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 2;
                iArr[2] = 4;
                iArr[3] = 4;
                iArr[4] = 3;
                iArr[5] = 2;
                return iArr;
            case 165:
            case 199:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 3;
                iArr[2] = 3;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 167:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 1;
                iArr[2] = 3;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 0;
                return iArr;
            case 169:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 1;
                iArr[2] = 2;
                iArr[3] = 2;
                iArr[4] = 4;
                iArr[5] = 2;
                return iArr;
            case 171:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 0;
                iArr[2] = 2;
                iArr[3] = 0;
                iArr[4] = 2;
                iArr[5] = 1;
                return iArr;
            case 172:
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 4;
                iArr[2] = 1;
                iArr[3] = 4;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 173:
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 0;
                iArr[2] = 0;
                iArr[3] = 0;
                iArr[4] = 1;
                iArr[5] = 2;
                return iArr;
            case 174:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 2;
                iArr[2] = 4;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 176:
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 4;
                iArr[2] = 4;
                iArr[3] = 4;
                iArr[4] = 4;
                iArr[5] = 2;
                return iArr;
            case 177:
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 2;
                iArr[2] = 2;
                iArr[3] = 3;
                iArr[4] = 1;
                iArr[5] = 2;
                return iArr;
            case 178:
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 0;
                iArr[2] = 1;
                iArr[3] = 2;
                iArr[4] = 1;
                iArr[5] = 2;
                return iArr;
            case 179:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 0;
                iArr[2] = 0;
                iArr[3] = 0;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 180:
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 0;
                iArr[2] = 0;
                iArr[3] = 0;
                iArr[4] = 3;
                iArr[5] = 3;
                return iArr;
            case 181:
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 3;
                iArr[2] = 1;
                iArr[3] = 0;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 184:
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 3;
                iArr[2] = 1;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 185:
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 3;
                iArr[2] = 4;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 186:
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 1;
                iArr[2] = 1;
                iArr[3] = 1;
                iArr[4] = 0;
                iArr[5] = 2;
                return iArr;
            case 187:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 3;
                iArr[2] = 3;
                iArr[3] = 3;
                iArr[4] = 3;
                iArr[5] = 3;
                return iArr;
            case 191:
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 1;
                iArr[2] = 1;
                iArr[3] = 1;
                iArr[4] = 3;
                iArr[5] = 2;
                return iArr;
            case 195:
                // fill-array-data instruction
                iArr[0] = 3;
                iArr[1] = 2;
                iArr[2] = 2;
                iArr[3] = 4;
                iArr[4] = 4;
                iArr[5] = 2;
                return iArr;
            case 196:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 4;
                iArr[2] = 3;
                iArr[3] = 0;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 197:
            case 210:
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 2;
                iArr[2] = 2;
                iArr[3] = 3;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 198:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 2;
                iArr[2] = 1;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 202:
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 4;
                iArr[2] = 3;
                iArr[3] = 4;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 203:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 2;
                iArr[2] = 1;
                iArr[3] = 3;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 206:
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 1;
                iArr[2] = 2;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 209:
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 2;
                iArr[2] = 4;
                iArr[3] = 4;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 211:
            case 221:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 1;
                iArr[2] = 1;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 213:
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 0;
                iArr[2] = 0;
                iArr[3] = 1;
                iArr[4] = 3;
                iArr[5] = 2;
                return iArr;
            case 214:
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 4;
                iArr[2] = 0;
                iArr[3] = 0;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 216:
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 2;
                iArr[2] = 0;
                iArr[3] = 0;
                iArr[4] = 0;
                iArr[5] = 0;
                return iArr;
            case 218:
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 1;
                iArr[2] = 1;
                iArr[3] = 2;
                iArr[4] = 4;
                iArr[5] = 2;
                return iArr;
            case 220:
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 1;
                iArr[2] = 4;
                iArr[3] = 1;
                iArr[4] = 3;
                iArr[5] = 1;
                return iArr;
            case 222:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 2;
                iArr[2] = 3;
                iArr[3] = 4;
                iArr[4] = 3;
                iArr[5] = 2;
                return iArr;
            case 226:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 2;
                iArr[2] = 0;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 227:
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 2;
                iArr[2] = 1;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 228:
                // fill-array-data instruction
                iArr[0] = 0;
                iArr[1] = 0;
                iArr[2] = 1;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 1;
                return iArr;
            case 229:
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 3;
                iArr[2] = 3;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 232:
                // fill-array-data instruction
                iArr[0] = 1;
                iArr[1] = 2;
                iArr[2] = 1;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 234:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 3;
                iArr[2] = 3;
                iArr[3] = 4;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 235:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 3;
                iArr[2] = 2;
                iArr[3] = 1;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
            case 236:
                // fill-array-data instruction
                iArr[0] = 4;
                iArr[1] = 4;
                iArr[2] = 4;
                iArr[3] = 3;
                iArr[4] = 3;
                iArr[5] = 2;
                return iArr;
            default:
                // fill-array-data instruction
                iArr[0] = 2;
                iArr[1] = 2;
                iArr[2] = 2;
                iArr[3] = 2;
                iArr[4] = 2;
                iArr[5] = 2;
                return iArr;
        }
    }

    private long m(int i8) {
        Long l8 = this.f112a.get(Integer.valueOf(i8));
        if (l8 == null) {
            l8 = this.f112a.get(0);
        }
        if (l8 == null) {
            l8 = 1000000L;
        }
        return l8.longValue();
    }

    public static synchronized m n(Context context) {
        m mVar;
        synchronized (m.class) {
            if (f111v == null) {
                f111v = new b(context).a();
            }
            mVar = f111v;
        }
        return mVar;
    }

    private static boolean o(com.google.android.exoplayer2.upstream.a aVar, boolean z4) {
        return z4 && !aVar.d(8);
    }

    private void p(int i8, long j8, long j9) {
        if (i8 == 0 && j8 == 0 && j9 == this.f124m) {
            return;
        }
        this.f124m = j9;
        this.f113b.c(i8, j8, j9);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void q(int i8) {
        int i9 = this.f120i;
        if (i9 == 0 || this.f116e) {
            if (this.f125n) {
                i8 = this.f126o;
            }
            if (i9 == i8) {
                return;
            }
            this.f120i = i8;
            if (i8 != 1 && i8 != 0 && i8 != 8) {
                this.f123l = m(i8);
                long b9 = this.f115d.b();
                p(this.f117f > 0 ? (int) (b9 - this.f118g) : 0, this.f119h, this.f123l);
                this.f118g = b9;
                this.f119h = 0L;
                this.f122k = 0L;
                this.f121j = 0L;
                this.f114c.i();
            }
        }
    }

    @Override // a6.d
    public void a(d.a aVar) {
        this.f113b.e(aVar);
    }

    @Override // a6.y
    public synchronized void c(h hVar, com.google.android.exoplayer2.upstream.a aVar, boolean z4, int i8) {
        if (o(aVar, z4)) {
            this.f119h += i8;
        }
    }

    @Override // a6.d
    public y d() {
        return this;
    }

    @Override // a6.y
    public void e(h hVar, com.google.android.exoplayer2.upstream.a aVar, boolean z4) {
    }

    @Override // a6.d
    public synchronized long f() {
        return this.f123l;
    }

    @Override // a6.y
    public synchronized void g(h hVar, com.google.android.exoplayer2.upstream.a aVar, boolean z4) {
        if (o(aVar, z4)) {
            b6.a.f(this.f117f > 0);
            long b9 = this.f115d.b();
            int i8 = (int) (b9 - this.f118g);
            this.f121j += i8;
            long j8 = this.f122k;
            long j9 = this.f119h;
            this.f122k = j8 + j9;
            if (i8 > 0) {
                this.f114c.c((int) Math.sqrt(j9), (((float) j9) * 8000.0f) / i8);
                if (this.f121j >= 2000 || this.f122k >= 524288) {
                    this.f123l = this.f114c.f(0.5f);
                }
                p(i8, this.f119h, this.f123l);
                this.f118g = b9;
                this.f119h = 0L;
            }
            this.f117f--;
        }
    }

    @Override // a6.d
    public void h(Handler handler, d.a aVar) {
        b6.a.e(handler);
        b6.a.e(aVar);
        this.f113b.b(handler, aVar);
    }

    @Override // a6.y
    public synchronized void i(h hVar, com.google.android.exoplayer2.upstream.a aVar, boolean z4) {
        if (o(aVar, z4)) {
            if (this.f117f == 0) {
                this.f118g = this.f115d.b();
            }
            this.f117f++;
        }
    }
}
