package com.google.android.gms.measurement.internal;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.ext.SdkExtensions;
import android.text.TextUtils;
import com.google.android.gms.internal.measurement.me;
import com.google.android.gms.internal.measurement.wf;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.TreeSet;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;
import javax.security.auth.x500.X500Principal;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class sb extends d7 {

    /* renamed from: i  reason: collision with root package name */
    private static final String[] f16980i = {"firebase_", "google_", "ga_"};

    /* renamed from: j  reason: collision with root package name */
    private static final String[] f16981j = {"_err"};

    /* renamed from: c  reason: collision with root package name */
    private SecureRandom f16982c;

    /* renamed from: d  reason: collision with root package name */
    private final AtomicLong f16983d;

    /* renamed from: e  reason: collision with root package name */
    private int f16984e;

    /* renamed from: f  reason: collision with root package name */
    private n1.a f16985f;

    /* renamed from: g  reason: collision with root package name */
    private Boolean f16986g;

    /* renamed from: h  reason: collision with root package name */
    private Integer f16987h;

    /* JADX INFO: Access modifiers changed from: package-private */
    public sb(f6 f6Var) {
        super(f6Var);
        this.f16987h = null;
        this.f16983d = new AtomicLong(0L);
    }

    public static Bundle B(Bundle bundle) {
        if (bundle == null) {
            return new Bundle();
        }
        Bundle bundle2 = new Bundle(bundle);
        for (String str : bundle2.keySet()) {
            Object obj = bundle2.get(str);
            if (obj instanceof Bundle) {
                bundle2.putBundle(str, new Bundle((Bundle) obj));
            } else {
                int i8 = 0;
                if (obj instanceof Parcelable[]) {
                    Parcelable[] parcelableArr = (Parcelable[]) obj;
                    while (i8 < parcelableArr.length) {
                        if (parcelableArr[i8] instanceof Bundle) {
                            parcelableArr[i8] = new Bundle((Bundle) parcelableArr[i8]);
                        }
                        i8++;
                    }
                } else if (obj instanceof List) {
                    List list = (List) obj;
                    while (i8 < list.size()) {
                        Object obj2 = list.get(i8);
                        if (obj2 instanceof Bundle) {
                            list.set(i8, new Bundle((Bundle) obj2));
                        }
                        i8++;
                    }
                }
            }
        }
        return bundle2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean B0(String str) {
        return E0(c0.f16367c0.a(null), str);
    }

    public static Bundle E(List<zzno> list) {
        Bundle bundle = new Bundle();
        if (list == null) {
            return bundle;
        }
        for (zzno zznoVar : list) {
            String str = zznoVar.f17312f;
            if (str != null) {
                bundle.putString(zznoVar.f17308b, str);
            } else {
                Long l8 = zznoVar.f17310d;
                if (l8 != null) {
                    bundle.putLong(zznoVar.f17308b, l8.longValue());
                } else {
                    Double d8 = zznoVar.f17314h;
                    if (d8 != null) {
                        bundle.putDouble(zznoVar.f17308b, d8.doubleValue());
                    }
                }
            }
        }
        return bundle;
    }

    private static boolean E0(String str, String str2) {
        return str.equals("*") || Arrays.asList(str.split(",")).contains(str2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean F0(String str) {
        return E0(c0.f16419y0.a(null), str);
    }

    private final Object G(int i8, Object obj, boolean z4, boolean z8, String str) {
        Parcelable[] parcelableArr;
        Bundle C;
        if (obj == null) {
            return null;
        }
        if ((obj instanceof Long) || (obj instanceof Double)) {
            return obj;
        }
        if (obj instanceof Integer) {
            return Long.valueOf(((Integer) obj).intValue());
        }
        if (obj instanceof Byte) {
            return Long.valueOf(((Byte) obj).byteValue());
        }
        if (obj instanceof Short) {
            return Long.valueOf(((Short) obj).shortValue());
        }
        if (obj instanceof Boolean) {
            return Long.valueOf(((Boolean) obj).booleanValue() ? 1L : 0L);
        } else if (obj instanceof Float) {
            return Double.valueOf(((Float) obj).doubleValue());
        } else {
            if ((obj instanceof String) || (obj instanceof Character) || (obj instanceof CharSequence)) {
                return H(String.valueOf(obj), i8, z4);
            }
            if (z8 && ((obj instanceof Bundle[]) || (obj instanceof Parcelable[]))) {
                ArrayList arrayList = new ArrayList();
                for (Parcelable parcelable : (Parcelable[]) obj) {
                    if ((parcelable instanceof Bundle) && (C = C((Bundle) parcelable, null)) != null && !C.isEmpty()) {
                        arrayList.add(C);
                    }
                }
                return arrayList.toArray(new Bundle[arrayList.size()]);
            }
            return null;
        }
    }

    public static String H(String str, int i8, boolean z4) {
        if (str == null) {
            return null;
        }
        if (str.codePointCount(0, str.length()) > i8) {
            if (z4) {
                String substring = str.substring(0, str.offsetByCodePoints(0, i8));
                return substring + "...";
            }
            return null;
        }
        return str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean H0(String str) {
        return !TextUtils.isEmpty(str) && str.startsWith("_");
    }

    private static void J(Bundle bundle, int i8, String str, Object obj) {
        if (t0(bundle, i8)) {
            bundle.putString("_ev", H(str, 40, true));
            if (obj != null) {
                n6.j.l(bundle);
                if ((obj instanceof String) || (obj instanceof CharSequence)) {
                    bundle.putLong("_el", String.valueOf(obj).length());
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean J0(String str) {
        n6.j.f(str);
        return str.charAt(0) != '_' || str.equals("_ep");
    }

    public static boolean L0(String str) {
        for (String str2 : f16981j) {
            if (str2.equals(str)) {
                return false;
            }
        }
        return true;
    }

    private final int M0(String str) {
        if (u0("event param", str)) {
            if (m0("event param", null, str)) {
                return !h0("event param", 40, str) ? 3 : 0;
            }
            return 14;
        }
        return 3;
    }

    private final int N0(String str) {
        if (A0("event param", str)) {
            if (m0("event param", null, str)) {
                return !h0("event param", 40, str) ? 3 : 0;
            }
            return 14;
        }
        return 3;
    }

    private static int O0(String str) {
        return "_ldl".equals(str) ? RecognitionOptions.PDF417 : "_id".equals(str) ? RecognitionOptions.QR_CODE : ("_lgclid".equals(str) || "_gbraid".equals(str)) ? 100 : 36;
    }

    private static boolean R0(String str) {
        n6.j.l(str);
        return str.matches("^(1:\\d+:android:[a-f0-9]+|ca-app-pub-.*)$");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static MessageDigest T0() {
        MessageDigest messageDigest;
        for (int i8 = 0; i8 < 2; i8++) {
            try {
                messageDigest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException unused) {
            }
            if (messageDigest != null) {
                return messageDigest;
            }
        }
        return null;
    }

    public static void V(x8 x8Var, Bundle bundle, boolean z4) {
        if (bundle == null || x8Var == null || (bundle.containsKey("_sc") && !z4)) {
            if (bundle != null && x8Var == null && z4) {
                bundle.remove("_sn");
                bundle.remove("_sc");
                bundle.remove("_si");
                return;
            }
            return;
        }
        String str = x8Var.f17128a;
        if (str != null) {
            bundle.putString("_sn", str);
        } else {
            bundle.remove("_sn");
        }
        String str2 = x8Var.f17129b;
        if (str2 != null) {
            bundle.putString("_sc", str2);
        } else {
            bundle.remove("_sc");
        }
        bundle.putLong("_si", x8Var.f17130c);
    }

    public static void W(rb rbVar, int i8, String str, String str2, int i9) {
        X(rbVar, null, i8, str, str2, i9);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void X(rb rbVar, String str, int i8, String str2, String str3, int i9) {
        Bundle bundle = new Bundle();
        t0(bundle, i8);
        if (!TextUtils.isEmpty(str2) && !TextUtils.isEmpty(str3)) {
            bundle.putString(str2, str3);
        }
        if (i8 == 6 || i8 == 7 || i8 == 2) {
            bundle.putLong("_el", i9);
        }
        rbVar.a(str, "_err", bundle);
    }

    @TargetApi(30)
    private final boolean X0() {
        Integer num;
        if (this.f16986g == null) {
            n1.a Q0 = Q0();
            boolean z4 = false;
            if (Q0 == null) {
                return false;
            }
            Integer num2 = null;
            try {
                num = Q0.b().get(10000L, TimeUnit.MILLISECONDS);
                if (num != null) {
                    try {
                        if (num.intValue() == 1) {
                            z4 = true;
                        }
                    } catch (InterruptedException e8) {
                        e = e8;
                        num2 = num;
                        i().J().b("Measurement manager api exception", e);
                        this.f16986g = Boolean.FALSE;
                        num = num2;
                        i().I().b("Measurement manager api status result", num);
                        return this.f16986g.booleanValue();
                    } catch (CancellationException e9) {
                        e = e9;
                        num2 = num;
                        i().J().b("Measurement manager api exception", e);
                        this.f16986g = Boolean.FALSE;
                        num = num2;
                        i().I().b("Measurement manager api status result", num);
                        return this.f16986g.booleanValue();
                    } catch (ExecutionException e10) {
                        e = e10;
                        num2 = num;
                        i().J().b("Measurement manager api exception", e);
                        this.f16986g = Boolean.FALSE;
                        num = num2;
                        i().I().b("Measurement manager api status result", num);
                        return this.f16986g.booleanValue();
                    } catch (TimeoutException e11) {
                        e = e11;
                        num2 = num;
                        i().J().b("Measurement manager api exception", e);
                        this.f16986g = Boolean.FALSE;
                        num = num2;
                        i().I().b("Measurement manager api status result", num);
                        return this.f16986g.booleanValue();
                    }
                }
                this.f16986g = Boolean.valueOf(z4);
            } catch (InterruptedException e12) {
                e = e12;
            } catch (CancellationException e13) {
                e = e13;
            } catch (ExecutionException e14) {
                e = e14;
            } catch (TimeoutException e15) {
                e = e15;
            }
            i().I().b("Measurement manager api status result", num);
        }
        return this.f16986g.booleanValue();
    }

    private final void Y(String str, String str2, String str3, Bundle bundle, List<String> list, boolean z4) {
        int N0;
        String str4;
        int v8;
        z4 F;
        String c9;
        String a9;
        String str5;
        if (bundle == null) {
            return;
        }
        int v9 = a().v();
        int i8 = 0;
        for (String str6 : new TreeSet(bundle.keySet())) {
            if (list == null || !list.contains(str6)) {
                N0 = !z4 ? N0(str6) : 0;
                if (N0 == 0) {
                    N0 = M0(str6);
                }
            } else {
                N0 = 0;
            }
            if (N0 != 0) {
                J(bundle, N0, str6, N0 == 3 ? str6 : null);
                bundle.remove(str6);
            } else {
                if (f0(bundle.get(str6))) {
                    i().K().d("Nested Bundle parameters are not allowed; discarded. event name, param name, child param name", str2, str3, str6);
                    v8 = 22;
                    str4 = str6;
                } else {
                    str4 = str6;
                    v8 = v(str, str2, str6, bundle.get(str6), bundle, list, z4, false);
                }
                if (v8 != 0 && !"_ev".equals(str4)) {
                    J(bundle, v8, str4, bundle.get(str4));
                    bundle.remove(str4);
                } else if (J0(str4) && !l0(str4, f7.n.f19852d)) {
                    int i9 = i8 + 1;
                    if (a0(231100000, true)) {
                        if (i9 > v9) {
                            if (me.a() && a().r(c0.G0)) {
                                z4 F2 = i().F();
                                F2.c("Item can't contain more than " + v9 + " item-scoped custom params", e().c(str2), e().a(bundle));
                                t0(bundle, 28);
                                bundle.remove(str4);
                            } else {
                                F = i().F();
                                c9 = e().c(str2);
                                a9 = e().a(bundle);
                                str5 = "Item cannot contain custom parameters";
                            }
                        }
                        i8 = i9;
                    } else {
                        F = i().F();
                        c9 = e().c(str2);
                        a9 = e().a(bundle);
                        str5 = "Item array not supported on client's version of Google Play Services (Android Only)";
                    }
                    F.c(str5, c9, a9);
                    t0(bundle, 23);
                    bundle.remove(str4);
                    i8 = i9;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean b0(Context context) {
        ActivityInfo receiverInfo;
        n6.j.l(context);
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager != null && (receiverInfo = packageManager.getReceiverInfo(new ComponentName(context, "com.google.android.gms.measurement.AppMeasurementReceiver"), 0)) != null) {
                if (receiverInfo.enabled) {
                    return true;
                }
            }
        } catch (PackageManager.NameNotFoundException unused) {
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean c0(Context context, boolean z4) {
        n6.j.l(context);
        return z0(context, Build.VERSION.SDK_INT >= 24 ? "com.google.android.gms.measurement.AppMeasurementJobService" : "com.google.android.gms.measurement.AppMeasurementService");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean d0(Intent intent) {
        String stringExtra = intent.getStringExtra("android.intent.extra.REFERRER_NAME");
        return "android-app://com.google.android.googlequicksearchbox/https/www.google.com".equals(stringExtra) || "https://www.google.com".equals(stringExtra) || "android-app://com.google.appcrawler".equals(stringExtra);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean e0(Bundle bundle, int i8) {
        int i9 = 0;
        if (bundle.size() <= i8) {
            return false;
        }
        for (String str : new TreeSet(bundle.keySet())) {
            i9++;
            if (i9 > i8) {
                bundle.remove(str);
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean f0(Object obj) {
        return (obj instanceof Parcelable[]) || (obj instanceof ArrayList) || (obj instanceof Bundle);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean k0(String str, String str2, String str3, String str4) {
        boolean isEmpty = TextUtils.isEmpty(str);
        boolean isEmpty2 = TextUtils.isEmpty(str2);
        if (!isEmpty && !isEmpty2) {
            n6.j.l(str);
            return !str.equals(str2);
        } else if (isEmpty && isEmpty2) {
            return (TextUtils.isEmpty(str3) || TextUtils.isEmpty(str4)) ? !TextUtils.isEmpty(str4) : !str3.equals(str4);
        } else if (isEmpty || !isEmpty2) {
            return TextUtils.isEmpty(str3) || !str3.equals(str4);
        } else if (TextUtils.isEmpty(str4)) {
            return false;
        } else {
            return TextUtils.isEmpty(str3) || !str3.equals(str4);
        }
    }

    private static boolean l0(String str, String[] strArr) {
        n6.j.l(strArr);
        for (String str2 : strArr) {
            if (Objects.equals(str, str2)) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] o0(Parcelable parcelable) {
        if (parcelable == null) {
            return null;
        }
        Parcel obtain = Parcel.obtain();
        try {
            parcelable.writeToParcel(obtain, 0);
            return obtain.marshall();
        } finally {
            obtain.recycle();
        }
    }

    public static ArrayList<Bundle> r0(List<zzac> list) {
        if (list == null) {
            return new ArrayList<>(0);
        }
        ArrayList<Bundle> arrayList = new ArrayList<>(list.size());
        for (zzac zzacVar : list) {
            Bundle bundle = new Bundle();
            bundle.putString("app_id", zzacVar.f17250a);
            bundle.putString("origin", zzacVar.f17251b);
            bundle.putLong("creation_timestamp", zzacVar.f17253d);
            bundle.putString("name", zzacVar.f17252c.f17308b);
            f7.m.b(bundle, n6.j.l(zzacVar.f17252c.t()));
            bundle.putBoolean("active", zzacVar.f17254e);
            String str = zzacVar.f17255f;
            if (str != null) {
                bundle.putString("trigger_event_name", str);
            }
            zzbf zzbfVar = zzacVar.f17256g;
            if (zzbfVar != null) {
                bundle.putString("timed_out_event_name", zzbfVar.f17263a);
                zzba zzbaVar = zzbfVar.f17264b;
                if (zzbaVar != null) {
                    bundle.putBundle("timed_out_event_params", zzbaVar.D0());
                }
            }
            bundle.putLong("trigger_timeout", zzacVar.f17257h);
            zzbf zzbfVar2 = zzacVar.f17258j;
            if (zzbfVar2 != null) {
                bundle.putString("triggered_event_name", zzbfVar2.f17263a);
                zzba zzbaVar2 = zzbfVar2.f17264b;
                if (zzbaVar2 != null) {
                    bundle.putBundle("triggered_event_params", zzbaVar2.D0());
                }
            }
            bundle.putLong("triggered_timestamp", zzacVar.f17252c.f17309c);
            bundle.putLong("time_to_live", zzacVar.f17259k);
            zzbf zzbfVar3 = zzacVar.f17260l;
            if (zzbfVar3 != null) {
                bundle.putString("expired_event_name", zzbfVar3.f17263a);
                zzba zzbaVar3 = zzbfVar3.f17264b;
                if (zzbaVar3 != null) {
                    bundle.putBundle("expired_event_params", zzbaVar3.D0());
                }
            }
            arrayList.add(bundle);
        }
        return arrayList;
    }

    private final boolean s0(Context context, String str) {
        z4 E;
        String str2;
        Signature[] signatureArr;
        X500Principal x500Principal = new X500Principal("CN=Android Debug,O=Android,C=US");
        try {
            PackageInfo e8 = w6.c.a(context).e(str, 64);
            if (e8 == null || (signatureArr = e8.signatures) == null || signatureArr.length <= 0) {
                return true;
            }
            return ((X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(signatureArr[0].toByteArray()))).getSubjectX500Principal().equals(x500Principal);
        } catch (PackageManager.NameNotFoundException e9) {
            e = e9;
            E = i().E();
            str2 = "Package name not found";
            E.b(str2, e);
            return true;
        } catch (CertificateException e10) {
            e = e10;
            E = i().E();
            str2 = "Error obtaining certificate";
            E.b(str2, e);
            return true;
        }
    }

    private static boolean t0(Bundle bundle, int i8) {
        if (bundle != null && bundle.getLong("_err") == 0) {
            bundle.putLong("_err", i8);
            return true;
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x005e  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00b6 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:47:0x00b7  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private final int v(java.lang.String r16, java.lang.String r17, java.lang.String r18, java.lang.Object r19, android.os.Bundle r20, java.util.List<java.lang.String> r21, boolean r22, boolean r23) {
        /*
            Method dump skipped, instructions count: 335
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.sb.v(java.lang.String, java.lang.String, java.lang.String, java.lang.Object, android.os.Bundle, java.util.List, boolean, boolean):int");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Bundle[] v0(Object obj) {
        Object[] array;
        if (obj instanceof Bundle) {
            return new Bundle[]{(Bundle) obj};
        }
        if (obj instanceof Parcelable[]) {
            Parcelable[] parcelableArr = (Parcelable[]) obj;
            array = Arrays.copyOf(parcelableArr, parcelableArr.length, Bundle[].class);
        } else if (!(obj instanceof ArrayList)) {
            return null;
        } else {
            ArrayList arrayList = (ArrayList) obj;
            array = arrayList.toArray(new Bundle[arrayList.size()]);
        }
        return (Bundle[]) array;
    }

    public static long w(long j8, long j9) {
        return (j8 + (j9 * 60000)) / 86400000;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int w0() {
        if (Build.VERSION.SDK_INT < 30 || SdkExtensions.getExtensionVersion(30) <= 3) {
            return 0;
        }
        return SdkExtensions.getExtensionVersion(1000000);
    }

    public static long y(zzba zzbaVar) {
        long j8 = 0;
        if (zzbaVar == null) {
            return 0L;
        }
        Iterator<String> it = zzbaVar.iterator();
        while (it.hasNext()) {
            Object I0 = zzbaVar.I0(it.next());
            if (I0 instanceof Parcelable[]) {
                j8 += ((Parcelable[]) I0).length;
            }
        }
        return j8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long z(byte[] bArr) {
        n6.j.l(bArr);
        int i8 = 0;
        n6.j.p(bArr.length > 0);
        long j8 = 0;
        for (int length = bArr.length - 1; length >= 0 && length >= bArr.length - 8; length--) {
            j8 += (bArr[length] & 255) << i8;
            i8 += 8;
        }
        return j8;
    }

    private static boolean z0(Context context, String str) {
        ServiceInfo serviceInfo;
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager != null && (serviceInfo = packageManager.getServiceInfo(new ComponentName(context, str), 0)) != null) {
                if (serviceInfo.enabled) {
                    return true;
                }
            }
        } catch (PackageManager.NameNotFoundException unused) {
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Bundle A(Uri uri, boolean z4, boolean z8) {
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        String str7;
        String str8;
        String str9;
        String str10;
        if (uri == null) {
            return null;
        }
        try {
            if (uri.isHierarchical()) {
                str = uri.getQueryParameter("utm_campaign");
                str2 = uri.getQueryParameter("utm_source");
                str3 = uri.getQueryParameter("utm_medium");
                str4 = uri.getQueryParameter("gclid");
                str5 = z8 ? uri.getQueryParameter("gbraid") : null;
                str6 = uri.getQueryParameter("utm_id");
                str7 = uri.getQueryParameter("dclid");
                str8 = uri.getQueryParameter("srsltid");
                str9 = z4 ? uri.getQueryParameter("sfmc_id") : null;
            } else {
                str = null;
                str2 = null;
                str3 = null;
                str4 = null;
                str5 = null;
                str6 = null;
                str7 = null;
                str8 = null;
                str9 = null;
            }
            if (TextUtils.isEmpty(str) && TextUtils.isEmpty(str2) && TextUtils.isEmpty(str3) && TextUtils.isEmpty(str4) && ((!z8 || TextUtils.isEmpty(str5)) && TextUtils.isEmpty(str6) && TextUtils.isEmpty(str7) && TextUtils.isEmpty(str8) && (!z4 || TextUtils.isEmpty(str9)))) {
                return null;
            }
            Bundle bundle = new Bundle();
            if (TextUtils.isEmpty(str)) {
                str10 = "sfmc_id";
            } else {
                str10 = "sfmc_id";
                bundle.putString("campaign", str);
            }
            if (!TextUtils.isEmpty(str2)) {
                bundle.putString("source", str2);
            }
            if (!TextUtils.isEmpty(str3)) {
                bundle.putString("medium", str3);
            }
            if (!TextUtils.isEmpty(str4)) {
                bundle.putString("gclid", str4);
            }
            if (z8 && !TextUtils.isEmpty(str5)) {
                bundle.putString("gbraid", str5);
            }
            String queryParameter = uri.getQueryParameter("utm_term");
            if (!TextUtils.isEmpty(queryParameter)) {
                bundle.putString("term", queryParameter);
            }
            String queryParameter2 = uri.getQueryParameter("utm_content");
            if (!TextUtils.isEmpty(queryParameter2)) {
                bundle.putString("content", queryParameter2);
            }
            String queryParameter3 = uri.getQueryParameter("aclid");
            if (!TextUtils.isEmpty(queryParameter3)) {
                bundle.putString("aclid", queryParameter3);
            }
            String queryParameter4 = uri.getQueryParameter("cp1");
            if (!TextUtils.isEmpty(queryParameter4)) {
                bundle.putString("cp1", queryParameter4);
            }
            String queryParameter5 = uri.getQueryParameter("anid");
            if (!TextUtils.isEmpty(queryParameter5)) {
                bundle.putString("anid", queryParameter5);
            }
            if (!TextUtils.isEmpty(str6)) {
                bundle.putString("campaign_id", str6);
            }
            if (!TextUtils.isEmpty(str7)) {
                bundle.putString("dclid", str7);
            }
            String queryParameter6 = uri.getQueryParameter("utm_source_platform");
            if (!TextUtils.isEmpty(queryParameter6)) {
                bundle.putString("source_platform", queryParameter6);
            }
            String queryParameter7 = uri.getQueryParameter("utm_creative_format");
            if (!TextUtils.isEmpty(queryParameter7)) {
                bundle.putString("creative_format", queryParameter7);
            }
            String queryParameter8 = uri.getQueryParameter("utm_marketing_tactic");
            if (!TextUtils.isEmpty(queryParameter8)) {
                bundle.putString("marketing_tactic", queryParameter8);
            }
            if (!TextUtils.isEmpty(str8)) {
                bundle.putString("srsltid", str8);
            }
            if (z4 && !TextUtils.isEmpty(str9)) {
                bundle.putString(str10, str9);
            }
            return bundle;
        } catch (UnsupportedOperationException e8) {
            i().J().b("Install referrer url isn't a hierarchical URI", e8);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean A0(String str, String str2) {
        if (str2 == null) {
            i().F().b("Name is required and can't be null. Type", str);
            return false;
        } else if (str2.length() == 0) {
            i().F().b("Name is required and can't be empty. Type", str);
            return false;
        } else {
            int codePointAt = str2.codePointAt(0);
            if (!Character.isLetter(codePointAt)) {
                i().F().c("Name must start with a letter. Type, name", str, str2);
                return false;
            }
            int length = str2.length();
            int charCount = Character.charCount(codePointAt);
            while (charCount < length) {
                int codePointAt2 = str2.codePointAt(charCount);
                if (codePointAt2 != 95 && !Character.isLetterOrDigit(codePointAt2)) {
                    i().F().c("Name must consist of letters, digits or _ (underscores). Type, name", str, str2);
                    return false;
                }
                charCount += Character.charCount(codePointAt2);
            }
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Bundle C(Bundle bundle, String str) {
        Bundle bundle2 = new Bundle();
        if (bundle != null) {
            for (String str2 : bundle.keySet()) {
                Object q02 = q0(str2, bundle.get(str2));
                if (q02 == null) {
                    i().K().b("Param value can't be null", e().f(str2));
                } else {
                    M(bundle2, str2, q02);
                }
            }
        }
        return bundle2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean C0(String str, String str2) {
        if (wf.a() && a().r(c0.f16417x0) && !TextUtils.isEmpty(str2)) {
            return true;
        }
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return a().N().equals(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Bundle D(String str, String str2, Bundle bundle, List<String> list, boolean z4) {
        int N0;
        int i8;
        sb sbVar = this;
        boolean l02 = l0(str2, f7.o.f19856d);
        if (bundle != null) {
            Bundle bundle2 = new Bundle(bundle);
            int E = a().E();
            int i9 = 0;
            for (String str3 : new TreeSet(bundle.keySet())) {
                if (list == null || !list.contains(str3)) {
                    N0 = !z4 ? sbVar.N0(str3) : 0;
                    if (N0 == 0) {
                        N0 = sbVar.M0(str3);
                    }
                } else {
                    N0 = 0;
                }
                if (N0 != 0) {
                    J(bundle2, N0, str3, N0 == 3 ? str3 : null);
                    bundle2.remove(str3);
                    i8 = E;
                } else {
                    i8 = E;
                    int v8 = v(str, str2, str3, bundle.get(str3), bundle2, list, z4, l02);
                    if (v8 == 17) {
                        J(bundle2, v8, str3, Boolean.FALSE);
                    } else if (v8 != 0 && !"_ev".equals(str3)) {
                        J(bundle2, v8, v8 == 21 ? str2 : str3, bundle.get(str3));
                        bundle2.remove(str3);
                    }
                    if (J0(str3)) {
                        int i10 = i9 + 1;
                        if (i10 > i8) {
                            i().F().c("Event can't contain more than " + i8 + " params", e().c(str2), e().a(bundle));
                            t0(bundle2, 5);
                            bundle2.remove(str3);
                        }
                        i9 = i10;
                    }
                }
                E = i8;
                sbVar = this;
            }
            return bundle2;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean D0(String str) {
        k();
        if (w6.c.a(zza()).a(str) == 0) {
            return true;
        }
        i().D().b("Permission not granted", str);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final zzbf F(String str, String str2, Bundle bundle, String str3, long j8, boolean z4, boolean z8) {
        if (TextUtils.isEmpty(str2)) {
            return null;
        }
        if (t(str2) != 0) {
            i().E().b("Invalid conditional property event name", e().g(str2));
            throw new IllegalArgumentException();
        }
        Bundle bundle2 = bundle != null ? new Bundle(bundle) : new Bundle();
        bundle2.putString("_o", str3);
        Bundle D = D(str, str2, bundle2, u6.e.a("_o"), true);
        if (z4) {
            D = C(D, str);
        }
        n6.j.l(D);
        return new zzbf(str2, new zzba(D), str3, j8);
    }

    public final int G0() {
        if (this.f16987h == null) {
            this.f16987h = Integer.valueOf(com.google.android.gms.common.b.f().a(zza()) / 1000);
        }
        return this.f16987h.intValue();
    }

    public final URL I(long j8, String str, String str2, long j9, String str3) {
        try {
            n6.j.f(str2);
            n6.j.f(str);
            String format = String.format("https://www.googleadservices.com/pagead/conversion/app/deeplink?id_type=adid&sdk_version=%s&rdid=%s&bundleid=%s&retry=%s", String.format("v%s.%s", Long.valueOf(j8), Integer.valueOf(G0())), str2, str, Long.valueOf(j9));
            if (str.equals(a().O())) {
                format = format.concat("&ddl_test=1");
            }
            if (!str3.isEmpty()) {
                if (str3.charAt(0) != '&') {
                    format = format.concat("&");
                }
                format = format.concat(str3);
            }
            return new URL(format);
        } catch (IllegalArgumentException | MalformedURLException e8) {
            i().E().b("Failed to create BOW URL for Deferred Deep Link. exception", e8.getMessage());
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final long I0() {
        k();
        if (B0(this.f16485a.z().D())) {
            long j8 = Build.VERSION.SDK_INT < 30 ? 4L : SdkExtensions.getExtensionVersion(30) < 4 ? 8L : w0() < c0.X.a(null).intValue() ? 16L : 0L;
            if (!D0("android.permission.ACCESS_ADSERVICES_ATTRIBUTION")) {
                j8 |= 2;
            }
            if (j8 == 0 && !X0()) {
                j8 |= 64;
            }
            if (j8 == 0) {
                return 1L;
            }
            return j8;
        }
        return 0L;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void K(Bundle bundle, long j8) {
        long j9 = bundle.getLong("_et");
        if (j9 != 0) {
            i().J().b("Params already contained engagement", Long.valueOf(j9));
        }
        bundle.putLong("_et", j8 + j9);
    }

    public final boolean K0(String str) {
        List<ResolveInfo> queryIntentActivities;
        return (TextUtils.isEmpty(str) || (queryIntentActivities = zza().getPackageManager().queryIntentActivities(new Intent("android.intent.action.VIEW", Uri.parse(str)), 0)) == null || queryIntentActivities.isEmpty()) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void L(Bundle bundle, Bundle bundle2) {
        if (bundle2 == null) {
            return;
        }
        for (String str : bundle2.keySet()) {
            if (!bundle.containsKey(str)) {
                g().M(bundle, str, bundle2.get(str));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void M(Bundle bundle, String str, Object obj) {
        if (bundle == null) {
            return;
        }
        if (obj instanceof Long) {
            bundle.putLong(str, ((Long) obj).longValue());
        } else if (obj instanceof String) {
            bundle.putString(str, String.valueOf(obj));
        } else if (obj instanceof Double) {
            bundle.putDouble(str, ((Double) obj).doubleValue());
        } else if (obj instanceof Bundle[]) {
            bundle.putParcelableArray(str, (Bundle[]) obj);
        } else if (str != null) {
            i().K().c("Not putting event parameter. Invalid value type. name, type", e().f(str), obj != null ? obj.getClass().getSimpleName() : null);
        }
    }

    public final void N(com.google.android.gms.internal.measurement.h2 h2Var, int i8) {
        Bundle bundle = new Bundle();
        bundle.putInt("r", i8);
        try {
            h2Var.c(bundle);
        } catch (RemoteException e8) {
            this.f16485a.i().J().b("Error returning int value to wrapper", e8);
        }
    }

    public final void O(com.google.android.gms.internal.measurement.h2 h2Var, long j8) {
        Bundle bundle = new Bundle();
        bundle.putLong("r", j8);
        try {
            h2Var.c(bundle);
        } catch (RemoteException e8) {
            this.f16485a.i().J().b("Error returning long value to wrapper", e8);
        }
    }

    public final void P(com.google.android.gms.internal.measurement.h2 h2Var, Bundle bundle) {
        try {
            h2Var.c(bundle);
        } catch (RemoteException e8) {
            this.f16485a.i().J().b("Error returning bundle value to wrapper", e8);
        }
    }

    public final long P0() {
        long andIncrement;
        long j8;
        if (this.f16983d.get() != 0) {
            synchronized (this.f16983d) {
                this.f16983d.compareAndSet(-1L, 1L);
                andIncrement = this.f16983d.getAndIncrement();
            }
            return andIncrement;
        }
        synchronized (this.f16983d) {
            long nextLong = new Random(System.nanoTime() ^ zzb().a()).nextLong();
            int i8 = this.f16984e + 1;
            this.f16984e = i8;
            j8 = nextLong + i8;
        }
        return j8;
    }

    public final void Q(com.google.android.gms.internal.measurement.h2 h2Var, String str) {
        Bundle bundle = new Bundle();
        bundle.putString("r", str);
        try {
            h2Var.c(bundle);
        } catch (RemoteException e8) {
            this.f16485a.i().J().b("Error returning string value to wrapper", e8);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final n1.a Q0() {
        if (this.f16985f == null) {
            this.f16985f = n1.a.a(zza());
        }
        return this.f16985f;
    }

    public final void R(com.google.android.gms.internal.measurement.h2 h2Var, ArrayList<Bundle> arrayList) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("r", arrayList);
        try {
            h2Var.c(bundle);
        } catch (RemoteException e8) {
            this.f16485a.i().J().b("Error returning bundle list to wrapper", e8);
        }
    }

    public final void S(com.google.android.gms.internal.measurement.h2 h2Var, boolean z4) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("r", z4);
        try {
            h2Var.c(bundle);
        } catch (RemoteException e8) {
            this.f16485a.i().J().b("Error returning boolean value to wrapper", e8);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final String S0() {
        byte[] bArr = new byte[16];
        U0().nextBytes(bArr);
        return String.format(Locale.US, "%032x", new BigInteger(1, bArr));
    }

    public final void T(com.google.android.gms.internal.measurement.h2 h2Var, byte[] bArr) {
        Bundle bundle = new Bundle();
        bundle.putByteArray("r", bArr);
        try {
            h2Var.c(bundle);
        } catch (RemoteException e8) {
            this.f16485a.i().J().b("Error returning byte array to wrapper", e8);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void U(b5 b5Var, int i8) {
        int i9 = 0;
        for (String str : new TreeSet(b5Var.f16336d.keySet())) {
            if (J0(str) && (i9 = i9 + 1) > i8) {
                i().F().c("Event can't contain more than " + i8 + " params", e().c(b5Var.f16333a), e().a(b5Var.f16336d));
                t0(b5Var.f16336d, 5);
                b5Var.f16336d.remove(str);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final SecureRandom U0() {
        k();
        if (this.f16982c == null) {
            this.f16982c = new SecureRandom();
        }
        return this.f16982c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean V0() {
        k();
        return I0() == 1;
    }

    public final boolean W0() {
        try {
            zza().getClassLoader().loadClass("com.google.firebase.remoteconfig.FirebaseRemoteConfig");
            return true;
        } catch (ClassNotFoundException unused) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void Z(Parcelable[] parcelableArr, int i8, boolean z4) {
        int i9;
        n6.j.l(parcelableArr);
        for (Parcelable parcelable : parcelableArr) {
            Bundle bundle = (Bundle) parcelable;
            int i10 = 0;
            for (String str : new TreeSet(bundle.keySet())) {
                if (J0(str) && !l0(str, f7.n.f19852d) && (i10 = i10 + 1) > i8) {
                    z4 F = i().F();
                    if (z4) {
                        F.c("Param can't contain more than " + i8 + " item-scoped custom parameters", e().f(str), e().a(bundle));
                        i9 = 28;
                    } else {
                        F.c("Param cannot contain item-scoped custom parameters", e().f(str), e().a(bundle));
                        i9 = 23;
                    }
                    t0(bundle, i9);
                    bundle.remove(str);
                }
            }
        }
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ e a() {
        return super.a();
    }

    public final boolean a0(int i8, boolean z4) {
        Boolean U = this.f16485a.H().U();
        if (G0() < i8 / 1000) {
            return (U == null || U.booleanValue()) ? false : true;
        }
        return true;
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ d b() {
        return super.b();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ x c() {
        return super.c();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ s4 e() {
        return super.e();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ h5 f() {
        return super.f();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ sb g() {
        return super.g();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @SuppressLint({"ApplySharedPref"})
    public final boolean g0(String str, double d8) {
        try {
            SharedPreferences.Editor edit = zza().getSharedPreferences("google.analytics.deferred.deeplink.prefs", 0).edit();
            edit.putString("deeplink", str);
            edit.putLong("timestamp", Double.doubleToRawLongBits(d8));
            return edit.commit();
        } catch (RuntimeException e8) {
            i().E().b("Failed to persist Deferred Deep Link. exception", e8);
            return false;
        }
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ void h() {
        super.h();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean h0(String str, int i8, String str2) {
        if (str2 == null) {
            i().F().b("Name is required and can't be null. Type", str);
            return false;
        } else if (str2.codePointCount(0, str2.length()) > i8) {
            i().F().d("Name is too long. Type, maximum supported length, name", str, Integer.valueOf(i8), str2);
            return false;
        } else {
            return true;
        }
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ x4 i() {
        return super.i();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean i0(String str, String str2) {
        if (!TextUtils.isEmpty(str)) {
            if (R0(str)) {
                return true;
            }
            if (this.f16485a.p()) {
                i().F().b("Invalid google_app_id. Firebase Analytics disabled. See https://goo.gl/NAOOOI. provided id", x4.t(str));
            }
            return false;
        } else if (TextUtils.isEmpty(str2)) {
            if (this.f16485a.p()) {
                i().F().a("Missing google_app_id. Firebase Analytics disabled. See https://goo.gl/NAOOOI");
            }
            return false;
        } else if (R0(str2)) {
            return true;
        } else {
            i().F().b("Invalid admob_app_id. Analytics disabled.", x4.t(str2));
            return false;
        }
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ void j() {
        super.j();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean j0(String str, String str2, int i8, Object obj) {
        if (obj != null && !(obj instanceof Long) && !(obj instanceof Float) && !(obj instanceof Integer) && !(obj instanceof Byte) && !(obj instanceof Short) && !(obj instanceof Boolean) && !(obj instanceof Double)) {
            if (!(obj instanceof String) && !(obj instanceof Character) && !(obj instanceof CharSequence)) {
                return false;
            }
            String valueOf = String.valueOf(obj);
            if (valueOf.codePointCount(0, valueOf.length()) > i8) {
                i().K().d("Value is too long; discarded. Value kind, name, value length", str, str2, Integer.valueOf(valueOf.length()));
                return false;
            }
        }
        return true;
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ void k() {
        super.k();
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ a6 l() {
        return super.l();
    }

    @Override // com.google.android.gms.measurement.internal.d7
    protected final void m() {
        k();
        SecureRandom secureRandom = new SecureRandom();
        long nextLong = secureRandom.nextLong();
        if (nextLong == 0) {
            nextLong = secureRandom.nextLong();
            if (nextLong == 0) {
                i().J().a("Utils falling back to Random for random id");
            }
        }
        this.f16983d.set(nextLong);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean m0(String str, String[] strArr, String str2) {
        return n0(str, strArr, null, str2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean n0(String str, String[] strArr, String[] strArr2, String str2) {
        boolean z4;
        if (str2 == null) {
            i().F().b("Name is required and can't be null. Type", str);
            return false;
        }
        n6.j.l(str2);
        String[] strArr3 = f16980i;
        int length = strArr3.length;
        int i8 = 0;
        while (true) {
            if (i8 >= length) {
                z4 = false;
                break;
            } else if (str2.startsWith(strArr3[i8])) {
                z4 = true;
                break;
            } else {
                i8++;
            }
        }
        if (z4) {
            i().F().c("Name starts with reserved prefix. Type, name", str, str2);
            return false;
        } else if (strArr == null || !l0(str2, strArr) || (strArr2 != null && l0(str2, strArr2))) {
            return true;
        } else {
            i().F().c("Name is reserved. Type, name", str, str2);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int p0(String str) {
        if (u0("user property", str)) {
            if (m0("user property", f7.q.f19857a, str)) {
                return !h0("user property", 24, str) ? 6 : 0;
            }
            return 15;
        }
        return 6;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Object q0(String str, Object obj) {
        if ("_ev".equals(str)) {
            return G(a().u(null, false), obj, true, true, null);
        }
        return G(H0(str) ? a().u(null, false) : a().p(null, false), obj, false, true, null);
    }

    @Override // com.google.android.gms.measurement.internal.d7
    protected final boolean r() {
        return true;
    }

    public final int s(int i8) {
        return com.google.android.gms.common.b.f().h(zza(), com.google.android.gms.common.d.f11721a);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int t(String str) {
        if (u0("event", str)) {
            if (n0("event", f7.o.f19853a, f7.o.f19854b, str)) {
                return !h0("event", 40, str) ? 2 : 0;
            }
            return 13;
        }
        return 2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int u(String str, Object obj) {
        int O0;
        String str2;
        if ("_ldl".equals(str)) {
            O0 = O0(str);
            str2 = "user property referrer";
        } else {
            O0 = O0(str);
            str2 = "user property";
        }
        return j0(str2, str, O0, obj) ? 0 : 7;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean u0(String str, String str2) {
        if (str2 == null) {
            i().F().b("Name is required and can't be null. Type", str);
            return false;
        } else if (str2.length() == 0) {
            i().F().b("Name is required and can't be empty. Type", str);
            return false;
        } else {
            int codePointAt = str2.codePointAt(0);
            if (!Character.isLetter(codePointAt) && codePointAt != 95) {
                i().F().c("Name must start with a letter or _ (underscore). Type, name", str, str2);
                return false;
            }
            int length = str2.length();
            int charCount = Character.charCount(codePointAt);
            while (charCount < length) {
                int codePointAt2 = str2.codePointAt(charCount);
                if (codePointAt2 != 95 && !Character.isLetterOrDigit(codePointAt2)) {
                    i().F().c("Name must consist of letters, digits or _ (underscores). Type, name", str, str2);
                    return false;
                }
                charCount += Character.charCount(codePointAt2);
            }
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:16:0x0065 -> B:17:0x0072). Please submit an issue!!! */
    public final long x(Context context, String str) {
        k();
        n6.j.l(context);
        n6.j.f(str);
        PackageManager packageManager = context.getPackageManager();
        MessageDigest T0 = T0();
        long j8 = -1;
        if (T0 == null) {
            i().E().a("Could not get MD5 instance");
        } else {
            if (packageManager != null) {
                try {
                } catch (PackageManager.NameNotFoundException e8) {
                    i().E().b("Package name not found", e8);
                }
                if (!s0(context, str)) {
                    Signature[] signatureArr = w6.c.a(context).e(zza().getPackageName(), 64).signatures;
                    if (signatureArr == null || signatureArr.length <= 0) {
                        i().J().a("Could not get signatures");
                    } else {
                        j8 = z(T0.digest(signatureArr[0].toByteArray()));
                    }
                }
            }
            j8 = 0;
        }
        return j8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final long x0(String str) {
        if (zza().getPackageManager() == null) {
            return 0L;
        }
        int i8 = 0;
        try {
            ApplicationInfo c9 = w6.c.a(zza()).c(str, 0);
            if (c9 != null) {
                i8 = c9.targetSdkVersion;
            }
        } catch (PackageManager.NameNotFoundException unused) {
            i().H().b("PackageManager failed to find running app: app_id", str);
        }
        return i8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Object y0(String str, Object obj) {
        return "_ldl".equals(str) ? G(O0(str), obj, true, false, null) : G(O0(str), obj, false, false, null);
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ Context zza() {
        return super.zza();
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ u6.d zzb() {
        return super.zzb();
    }
}
