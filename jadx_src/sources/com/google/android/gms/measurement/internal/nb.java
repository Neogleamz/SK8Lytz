package com.google.android.gms.measurement.internal;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
import com.google.android.gms.internal.measurement.hd;
import com.google.android.gms.internal.measurement.lf;
import com.google.android.gms.internal.measurement.md;
import com.google.android.gms.internal.measurement.r4;
import com.google.android.gms.internal.measurement.t4;
import com.google.android.gms.internal.measurement.v4;
import com.google.android.gms.internal.measurement.y4;
import com.google.android.gms.internal.measurement.ye;
import com.google.android.gms.internal.measurement.zzfh$zzd;
import com.google.android.gms.internal.measurement.zzfh$zzf;
import com.google.android.gms.internal.measurement.zzft$zzi;
import com.google.android.gms.measurement.internal.zziq;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class nb extends bb {
    /* JADX INFO: Access modifiers changed from: package-private */
    public nb(gb gbVar) {
        super(gbVar);
    }

    private final Bundle A(Map<String, Object> map, boolean z4) {
        String str;
        Bundle bundle = new Bundle();
        for (String str2 : map.keySet()) {
            Object obj = map.get(str2);
            if (obj == null) {
                str = null;
            } else if (obj instanceof Long) {
                bundle.putLong(str2, ((Long) obj).longValue());
            } else if (obj instanceof Double) {
                bundle.putDouble(str2, ((Double) obj).doubleValue());
            } else if (!(obj instanceof ArrayList)) {
                str = obj.toString();
            } else if (z4) {
                ArrayList arrayList = (ArrayList) obj;
                ArrayList arrayList2 = new ArrayList();
                int size = arrayList.size();
                int i8 = 0;
                while (i8 < size) {
                    Object obj2 = arrayList.get(i8);
                    i8++;
                    arrayList2.add(A((Map) obj2, false));
                }
                bundle.putParcelableArray(str2, (Parcelable[]) arrayList2.toArray(new Parcelable[0]));
            }
            bundle.putString(str2, str);
        }
        return bundle;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static com.google.android.gms.internal.measurement.t4 D(com.google.android.gms.internal.measurement.r4 r4Var, String str) {
        for (com.google.android.gms.internal.measurement.t4 t4Var : r4Var.e0()) {
            if (t4Var.e0().equals(str)) {
                return t4Var;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <BuilderT extends com.google.android.gms.internal.measurement.ha> BuilderT E(BuilderT buildert, byte[] bArr) {
        com.google.android.gms.internal.measurement.l8 a9 = com.google.android.gms.internal.measurement.l8.a();
        return a9 != null ? (BuilderT) buildert.E0(bArr, a9) : (BuilderT) buildert.Z(bArr);
    }

    private static String L(boolean z4, boolean z8, boolean z9) {
        StringBuilder sb = new StringBuilder();
        if (z4) {
            sb.append("Dynamic ");
        }
        if (z8) {
            sb.append("Sequence ");
        }
        if (z9) {
            sb.append("Session-Scoped ");
        }
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static List<Long> M(BitSet bitSet) {
        int length = (bitSet.length() + 63) / 64;
        ArrayList arrayList = new ArrayList(length);
        for (int i8 = 0; i8 < length; i8++) {
            long j8 = 0;
            for (int i9 = 0; i9 < 64; i9++) {
                int i10 = (i8 << 6) + i9;
                if (i10 < bitSet.length()) {
                    if (bitSet.get(i10)) {
                        j8 |= 1 << i9;
                    }
                }
            }
            arrayList.add(Long.valueOf(j8));
        }
        return arrayList;
    }

    private static void P(Uri.Builder builder, String str, String str2, Set<String> set) {
        if (set.contains(str) || TextUtils.isEmpty(str2)) {
            return;
        }
        builder.appendQueryParameter(str, str2);
    }

    private static void Q(Uri.Builder builder, String[] strArr, Bundle bundle, Set<String> set) {
        for (String str : strArr) {
            String[] split = str.split(",");
            String str2 = split[0];
            String str3 = split[split.length - 1];
            String string = bundle.getString(str2);
            if (string != null) {
                P(builder, str3, string, set);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void R(r4.a aVar, String str, Object obj) {
        List<com.google.android.gms.internal.measurement.t4> M = aVar.M();
        int i8 = 0;
        while (true) {
            if (i8 >= M.size()) {
                i8 = -1;
                break;
            } else if (str.equals(M.get(i8).e0())) {
                break;
            } else {
                i8++;
            }
        }
        t4.a C = com.google.android.gms.internal.measurement.t4.b0().C(str);
        if (obj instanceof Long) {
            C.z(((Long) obj).longValue());
        } else if (obj instanceof String) {
            C.E((String) obj);
        } else if (obj instanceof Double) {
            C.y(((Double) obj).doubleValue());
        }
        if (i8 >= 0) {
            aVar.z(i8, C);
        } else {
            aVar.C(C);
        }
    }

    private static void V(StringBuilder sb, int i8) {
        for (int i9 = 0; i9 < i8; i9++) {
            sb.append("  ");
        }
    }

    private final void W(StringBuilder sb, int i8, com.google.android.gms.internal.measurement.u3 u3Var) {
        if (u3Var == null) {
            return;
        }
        V(sb, i8);
        sb.append("filter {\n");
        if (u3Var.P()) {
            Z(sb, i8, "complement", Boolean.valueOf(u3Var.O()));
        }
        if (u3Var.R()) {
            Z(sb, i8, "param_name", e().f(u3Var.N()));
        }
        if (u3Var.S()) {
            int i9 = i8 + 1;
            zzfh$zzf M = u3Var.M();
            if (M != null) {
                V(sb, i9);
                sb.append("string_filter");
                sb.append(" {\n");
                if (M.P()) {
                    Z(sb, i9, "match_type", M.H().name());
                }
                if (M.O()) {
                    Z(sb, i9, "expression", M.K());
                }
                if (M.N()) {
                    Z(sb, i9, "case_sensitive", Boolean.valueOf(M.M()));
                }
                if (M.m() > 0) {
                    V(sb, i9 + 1);
                    sb.append("expression_list {\n");
                    for (String str : M.L()) {
                        V(sb, i9 + 2);
                        sb.append(str);
                        sb.append("\n");
                    }
                    sb.append("}\n");
                }
                V(sb, i9);
                sb.append("}\n");
            }
        }
        if (u3Var.Q()) {
            X(sb, i8 + 1, "number_filter", u3Var.L());
        }
        V(sb, i8);
        sb.append("}\n");
    }

    private static void X(StringBuilder sb, int i8, String str, zzfh$zzd zzfh_zzd) {
        if (zzfh_zzd == null) {
            return;
        }
        V(sb, i8);
        sb.append(str);
        sb.append(" {\n");
        if (zzfh_zzd.O()) {
            Z(sb, i8, "comparison_type", zzfh_zzd.H().name());
        }
        if (zzfh_zzd.Q()) {
            Z(sb, i8, "match_as_float", Boolean.valueOf(zzfh_zzd.N()));
        }
        if (zzfh_zzd.P()) {
            Z(sb, i8, "comparison_value", zzfh_zzd.K());
        }
        if (zzfh_zzd.S()) {
            Z(sb, i8, "min_comparison_value", zzfh_zzd.M());
        }
        if (zzfh_zzd.R()) {
            Z(sb, i8, "max_comparison_value", zzfh_zzd.L());
        }
        V(sb, i8);
        sb.append("}\n");
    }

    private static void Y(StringBuilder sb, int i8, String str, com.google.android.gms.internal.measurement.w4 w4Var) {
        if (w4Var == null) {
            return;
        }
        V(sb, 3);
        sb.append(str);
        sb.append(" {\n");
        if (w4Var.K() != 0) {
            V(sb, 4);
            sb.append("results: ");
            int i9 = 0;
            for (Long l8 : w4Var.b0()) {
                int i10 = i9 + 1;
                if (i9 != 0) {
                    sb.append(", ");
                }
                sb.append(l8);
                i9 = i10;
            }
            sb.append('\n');
        }
        if (w4Var.S() != 0) {
            V(sb, 4);
            sb.append("status: ");
            int i11 = 0;
            for (Long l9 : w4Var.d0()) {
                int i12 = i11 + 1;
                if (i11 != 0) {
                    sb.append(", ");
                }
                sb.append(l9);
                i11 = i12;
            }
            sb.append('\n');
        }
        if (w4Var.m() != 0) {
            V(sb, 4);
            sb.append("dynamic_filter_timestamps: {");
            int i13 = 0;
            for (com.google.android.gms.internal.measurement.q4 q4Var : w4Var.a0()) {
                int i14 = i13 + 1;
                if (i13 != 0) {
                    sb.append(", ");
                }
                sb.append(q4Var.P() ? Integer.valueOf(q4Var.m()) : null);
                sb.append(":");
                sb.append(q4Var.O() ? Long.valueOf(q4Var.L()) : null);
                i13 = i14;
            }
            sb.append("}\n");
        }
        if (w4Var.O() != 0) {
            V(sb, 4);
            sb.append("sequence_filter_timestamps: {");
            int i15 = 0;
            for (com.google.android.gms.internal.measurement.x4 x4Var : w4Var.c0()) {
                int i16 = i15 + 1;
                if (i15 != 0) {
                    sb.append(", ");
                }
                sb.append(x4Var.Q() ? Integer.valueOf(x4Var.L()) : null);
                sb.append(": [");
                int i17 = 0;
                for (Long l10 : x4Var.P()) {
                    long longValue = l10.longValue();
                    int i18 = i17 + 1;
                    if (i17 != 0) {
                        sb.append(", ");
                    }
                    sb.append(longValue);
                    i17 = i18;
                }
                sb.append("]");
                i15 = i16;
            }
            sb.append("}\n");
        }
        V(sb, 3);
        sb.append("}\n");
    }

    private static void Z(StringBuilder sb, int i8, String str, Object obj) {
        if (obj == null) {
            return;
        }
        V(sb, i8 + 1);
        sb.append(str);
        sb.append(": ");
        sb.append(obj);
        sb.append('\n');
    }

    private final void a0(StringBuilder sb, int i8, List<com.google.android.gms.internal.measurement.t4> list) {
        if (list == null) {
            return;
        }
        int i9 = i8 + 1;
        for (com.google.android.gms.internal.measurement.t4 t4Var : list) {
            if (t4Var != null) {
                V(sb, i9);
                sb.append("param {\n");
                Z(sb, i9, "name", t4Var.k0() ? e().f(t4Var.e0()) : null);
                Z(sb, i9, "string_value", t4Var.l0() ? t4Var.f0() : null);
                Z(sb, i9, "int_value", t4Var.j0() ? Long.valueOf(t4Var.Y()) : null);
                Z(sb, i9, "double_value", t4Var.h0() ? Double.valueOf(t4Var.H()) : null);
                if (t4Var.W() > 0) {
                    a0(sb, i9, t4Var.g0());
                }
                V(sb, i9);
                sb.append("}\n");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean c0(zzbf zzbfVar, zzn zznVar) {
        n6.j.l(zzbfVar);
        n6.j.l(zznVar);
        return (TextUtils.isEmpty(zznVar.f17289b) && TextUtils.isEmpty(zznVar.f17303w)) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean d0(List<Long> list, int i8) {
        if (i8 < (list.size() << 6)) {
            return ((1 << (i8 % 64)) & list.get(i8 / 64).longValue()) != 0;
        }
        return false;
    }

    private static Bundle e0(List<com.google.android.gms.internal.measurement.t4> list) {
        String valueOf;
        Bundle bundle = new Bundle();
        for (com.google.android.gms.internal.measurement.t4 t4Var : list) {
            String e02 = t4Var.e0();
            if (t4Var.h0()) {
                valueOf = String.valueOf(t4Var.H());
            } else if (t4Var.i0()) {
                valueOf = String.valueOf(t4Var.S());
            } else if (t4Var.l0()) {
                valueOf = t4Var.f0();
            } else if (t4Var.j0()) {
                valueOf = String.valueOf(t4Var.Y());
            }
            bundle.putString(e02, valueOf);
        }
        return bundle;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Object f0(com.google.android.gms.internal.measurement.r4 r4Var, String str) {
        com.google.android.gms.internal.measurement.t4 D = D(r4Var, str);
        if (D != null) {
            if (D.l0()) {
                return D.f0();
            }
            if (D.j0()) {
                return Long.valueOf(D.Y());
            }
            if (D.h0()) {
                return Double.valueOf(D.H());
            }
            if (D.W() > 0) {
                List<com.google.android.gms.internal.measurement.t4> g02 = D.g0();
                ArrayList arrayList = new ArrayList();
                for (com.google.android.gms.internal.measurement.t4 t4Var : g02) {
                    if (t4Var != null) {
                        Bundle bundle = new Bundle();
                        for (com.google.android.gms.internal.measurement.t4 t4Var2 : t4Var.g0()) {
                            if (t4Var2.l0()) {
                                bundle.putString(t4Var2.e0(), t4Var2.f0());
                            } else if (t4Var2.j0()) {
                                bundle.putLong(t4Var2.e0(), t4Var2.Y());
                            } else if (t4Var2.h0()) {
                                bundle.putDouble(t4Var2.e0(), t4Var2.H());
                            }
                        }
                        if (!bundle.isEmpty()) {
                            arrayList.add(bundle);
                        }
                    }
                }
                return (Bundle[]) arrayList.toArray(new Bundle[arrayList.size()]);
            }
            return null;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean g0(String str) {
        return str != null && str.matches("([+-])?([0-9]+\\.?[0-9]*|[0-9]*\\.?[0-9]+)") && str.length() <= 310;
    }

    private static Bundle i0(List<com.google.android.gms.internal.measurement.y4> list) {
        String valueOf;
        Bundle bundle = new Bundle();
        for (com.google.android.gms.internal.measurement.y4 y4Var : list) {
            String b02 = y4Var.b0();
            if (y4Var.d0()) {
                valueOf = String.valueOf(y4Var.H());
            } else if (y4Var.e0()) {
                valueOf = String.valueOf(y4Var.P());
            } else if (y4Var.h0()) {
                valueOf = y4Var.c0();
            } else if (y4Var.f0()) {
                valueOf = String.valueOf(y4Var.V());
            }
            bundle.putString(b02, valueOf);
        }
        return bundle;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int w(v4.a aVar, String str) {
        if (aVar == null) {
            return -1;
        }
        for (int i8 = 0; i8 < aVar.c0(); i8++) {
            if (str.equals(aVar.N0(i8).b0())) {
                return i8;
            }
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Bundle z(List<com.google.android.gms.internal.measurement.t4> list) {
        Bundle bundle = new Bundle();
        for (com.google.android.gms.internal.measurement.t4 t4Var : list) {
            String e02 = t4Var.e0();
            if (t4Var.h0()) {
                bundle.putDouble(e02, t4Var.H());
            } else if (t4Var.i0()) {
                bundle.putFloat(e02, t4Var.S());
            } else if (t4Var.l0()) {
                bundle.putString(e02, t4Var.f0());
            } else if (t4Var.j0()) {
                bundle.putLong(e02, t4Var.Y());
            }
        }
        return bundle;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final <T extends Parcelable> T B(byte[] bArr, Parcelable.Creator<T> creator) {
        if (bArr == null) {
            return null;
        }
        Parcel obtain = Parcel.obtain();
        try {
            obtain.unmarshall(bArr, 0, bArr.length);
            obtain.setDataPosition(0);
            return creator.createFromParcel(obtain);
        } catch (SafeParcelReader.ParseException unused) {
            i().E().a("Failed to load parcelable from buffer");
            return null;
        } finally {
            obtain.recycle();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final com.google.android.gms.internal.measurement.r4 C(w wVar) {
        r4.a B = com.google.android.gms.internal.measurement.r4.b0().B(wVar.f17060e);
        Iterator<String> it = wVar.f17061f.iterator();
        while (it.hasNext()) {
            String next = it.next();
            t4.a C = com.google.android.gms.internal.measurement.t4.b0().C(next);
            Object I0 = wVar.f17061f.I0(next);
            n6.j.l(I0);
            S(C, I0);
            B.C(C);
        }
        return (com.google.android.gms.internal.measurement.r4) ((com.google.android.gms.internal.measurement.x8) B.n());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final zzbf F(com.google.android.gms.internal.measurement.e eVar) {
        Object obj;
        Bundle A = A(eVar.g(), true);
        String obj2 = (!A.containsKey("_o") || (obj = A.get("_o")) == null) ? "app" : obj.toString();
        String b9 = f7.o.b(eVar.e());
        if (b9 == null) {
            b9 = eVar.e();
        }
        return new zzbf(b9, new zzba(A), obj2, eVar.a());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00f0  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0155  */
    @android.annotation.TargetApi(30)
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final com.google.android.gms.measurement.internal.zzmv G(java.lang.String r10, com.google.android.gms.internal.measurement.v4.a r11, com.google.android.gms.internal.measurement.r4.a r12, java.lang.String r13) {
        /*
            Method dump skipped, instructions count: 391
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.nb.G(java.lang.String, com.google.android.gms.internal.measurement.v4$a, com.google.android.gms.internal.measurement.r4$a, java.lang.String):com.google.android.gms.measurement.internal.zzmv");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00f0  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0155  */
    @android.annotation.TargetApi(30)
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final com.google.android.gms.measurement.internal.zzmv H(java.lang.String r10, com.google.android.gms.internal.measurement.v4 r11, com.google.android.gms.internal.measurement.r4.a r12, java.lang.String r13) {
        /*
            Method dump skipped, instructions count: 391
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.nb.H(java.lang.String, com.google.android.gms.internal.measurement.v4, com.google.android.gms.internal.measurement.r4$a, java.lang.String):com.google.android.gms.measurement.internal.zzmv");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final String I(com.google.android.gms.internal.measurement.t3 t3Var) {
        if (t3Var == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\nevent_filter {\n");
        if (t3Var.W()) {
            Z(sb, 0, "filter_id", Integer.valueOf(t3Var.M()));
        }
        Z(sb, 0, "event_name", e().c(t3Var.Q()));
        String L = L(t3Var.S(), t3Var.T(), t3Var.U());
        if (!L.isEmpty()) {
            Z(sb, 0, "filter_type", L);
        }
        if (t3Var.V()) {
            X(sb, 1, "event_count_filter", t3Var.P());
        }
        if (t3Var.m() > 0) {
            sb.append("  filters {\n");
            for (com.google.android.gms.internal.measurement.u3 u3Var : t3Var.R()) {
                W(sb, 2, u3Var);
            }
        }
        V(sb, 1);
        sb.append("}\n}\n");
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final String J(com.google.android.gms.internal.measurement.v3 v3Var) {
        if (v3Var == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\nproperty_filter {\n");
        if (v3Var.Q()) {
            Z(sb, 0, "filter_id", Integer.valueOf(v3Var.m()));
        }
        Z(sb, 0, "property_name", e().g(v3Var.M()));
        String L = L(v3Var.N(), v3Var.O(), v3Var.P());
        if (!L.isEmpty()) {
            Z(sb, 0, "filter_type", L);
        }
        W(sb, 1, v3Var.J());
        sb.append("}\n");
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final String K(zzft$zzi zzft_zzi) {
        com.google.android.gms.internal.measurement.o4 E3;
        if (zzft_zzi == null) {
            return BuildConfig.FLAVOR;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\nbatch {\n");
        for (com.google.android.gms.internal.measurement.v4 v4Var : zzft_zzi.O()) {
            if (v4Var != null) {
                V(sb, 1);
                sb.append("bundle {\n");
                if (v4Var.c1()) {
                    Z(sb, 1, "protocol_version", Integer.valueOf(v4Var.X1()));
                }
                if (lf.a() && a().B(v4Var.I3(), c0.f16411u0) && v4Var.f1()) {
                    Z(sb, 1, "session_stitching_token", v4Var.r0());
                }
                Z(sb, 1, "platform", v4Var.p0());
                if (v4Var.X0()) {
                    Z(sb, 1, "gmp_version", Long.valueOf(v4Var.i3()));
                }
                if (v4Var.k1()) {
                    Z(sb, 1, "uploading_gmp_version", Long.valueOf(v4Var.B3()));
                }
                if (v4Var.V0()) {
                    Z(sb, 1, "dynamite_version", Long.valueOf(v4Var.U2()));
                }
                if (v4Var.D0()) {
                    Z(sb, 1, "config_version", Long.valueOf(v4Var.F2()));
                }
                Z(sb, 1, "gmp_app_id", v4Var.n0());
                Z(sb, 1, "admob_app_id", v4Var.H3());
                Z(sb, 1, "app_id", v4Var.I3());
                Z(sb, 1, "app_version", v4Var.g0());
                if (v4Var.A0()) {
                    Z(sb, 1, "app_version_major", Integer.valueOf(v4Var.G0()));
                }
                Z(sb, 1, "firebase_instance_id", v4Var.m0());
                if (v4Var.U0()) {
                    Z(sb, 1, "dev_cert_hash", Long.valueOf(v4Var.N2()));
                }
                Z(sb, 1, "app_store", v4Var.K3());
                if (v4Var.j1()) {
                    Z(sb, 1, "upload_timestamp_millis", Long.valueOf(v4Var.y3()));
                }
                if (v4Var.g1()) {
                    Z(sb, 1, "start_timestamp_millis", Long.valueOf(v4Var.s3()));
                }
                if (v4Var.W0()) {
                    Z(sb, 1, "end_timestamp_millis", Long.valueOf(v4Var.b3()));
                }
                if (v4Var.b1()) {
                    Z(sb, 1, "previous_bundle_start_timestamp_millis", Long.valueOf(v4Var.p3()));
                }
                if (v4Var.a1()) {
                    Z(sb, 1, "previous_bundle_end_timestamp_millis", Long.valueOf(v4Var.m3()));
                }
                Z(sb, 1, "app_instance_id", v4Var.J3());
                Z(sb, 1, "resettable_device_id", v4Var.q0());
                Z(sb, 1, "ds_id", v4Var.l0());
                if (v4Var.Z0()) {
                    Z(sb, 1, "limited_ad_tracking", Boolean.valueOf(v4Var.y0()));
                }
                Z(sb, 1, "os_version", v4Var.H());
                Z(sb, 1, "device_model", v4Var.k0());
                Z(sb, 1, "user_default_language", v4Var.s0());
                if (v4Var.i1()) {
                    Z(sb, 1, "time_zone_offset_minutes", Integer.valueOf(v4Var.p2()));
                }
                if (v4Var.C0()) {
                    Z(sb, 1, "bundle_sequential_index", Integer.valueOf(v4Var.l1()));
                }
                if (v4Var.e1()) {
                    Z(sb, 1, "service_upload", Boolean.valueOf(v4Var.z0()));
                }
                Z(sb, 1, "health_monitor", v4Var.o0());
                if (v4Var.d1()) {
                    Z(sb, 1, "retry_counter", Integer.valueOf(v4Var.h2()));
                }
                if (v4Var.S0()) {
                    Z(sb, 1, "consent_signals", v4Var.i0());
                }
                if (v4Var.Y0()) {
                    Z(sb, 1, "is_dma_region", Boolean.valueOf(v4Var.x0()));
                }
                if (v4Var.T0()) {
                    Z(sb, 1, "core_platform_services", v4Var.j0());
                }
                if (v4Var.F0()) {
                    Z(sb, 1, "consent_diagnostics", v4Var.h0());
                }
                if (v4Var.h1()) {
                    Z(sb, 1, "target_os_version", Long.valueOf(v4Var.v3()));
                }
                if (ye.a() && a().B(v4Var.I3(), c0.J0)) {
                    Z(sb, 1, "ad_services_version", Integer.valueOf(v4Var.m()));
                    if (v4Var.B0() && (E3 = v4Var.E3()) != null) {
                        V(sb, 2);
                        sb.append("attribution_eligibility_status {\n");
                        Z(sb, 2, "eligible", Boolean.valueOf(E3.Y()));
                        Z(sb, 2, "no_access_adservices_attribution_permission", Boolean.valueOf(E3.d0()));
                        Z(sb, 2, "pre_r", Boolean.valueOf(E3.e0()));
                        Z(sb, 2, "r_extensions_too_old", Boolean.valueOf(E3.f0()));
                        Z(sb, 2, "adservices_extension_too_old", Boolean.valueOf(E3.V()));
                        Z(sb, 2, "ad_storage_not_allowed", Boolean.valueOf(E3.S()));
                        Z(sb, 2, "measurement_manager_disabled", Boolean.valueOf(E3.c0()));
                        V(sb, 2);
                        sb.append("}\n");
                    }
                }
                List<com.google.android.gms.internal.measurement.y4> v02 = v4Var.v0();
                if (v02 != null) {
                    for (com.google.android.gms.internal.measurement.y4 y4Var : v02) {
                        if (y4Var != null) {
                            V(sb, 2);
                            sb.append("user_property {\n");
                            Z(sb, 2, "set_timestamp_millis", y4Var.g0() ? Long.valueOf(y4Var.X()) : null);
                            Z(sb, 2, "name", e().g(y4Var.b0()));
                            Z(sb, 2, "string_value", y4Var.c0());
                            Z(sb, 2, "int_value", y4Var.f0() ? Long.valueOf(y4Var.V()) : null);
                            Z(sb, 2, "double_value", y4Var.d0() ? Double.valueOf(y4Var.H()) : null);
                            V(sb, 2);
                            sb.append("}\n");
                        }
                    }
                }
                List<com.google.android.gms.internal.measurement.p4> t02 = v4Var.t0();
                v4Var.I3();
                if (t02 != null) {
                    for (com.google.android.gms.internal.measurement.p4 p4Var : t02) {
                        if (p4Var != null) {
                            V(sb, 2);
                            sb.append("audience_membership {\n");
                            if (p4Var.U()) {
                                Z(sb, 2, "audience_id", Integer.valueOf(p4Var.m()));
                            }
                            if (p4Var.V()) {
                                Z(sb, 2, "new_audience", Boolean.valueOf(p4Var.T()));
                            }
                            Y(sb, 2, "current_data", p4Var.R());
                            if (p4Var.W()) {
                                Y(sb, 2, "previous_data", p4Var.S());
                            }
                            V(sb, 2);
                            sb.append("}\n");
                        }
                    }
                }
                List<com.google.android.gms.internal.measurement.r4> u02 = v4Var.u0();
                if (u02 != null) {
                    for (com.google.android.gms.internal.measurement.r4 r4Var : u02) {
                        if (r4Var != null) {
                            V(sb, 2);
                            sb.append("event {\n");
                            Z(sb, 2, "name", e().c(r4Var.d0()));
                            if (r4Var.h0()) {
                                Z(sb, 2, "timestamp_millis", Long.valueOf(r4Var.a0()));
                            }
                            if (r4Var.g0()) {
                                Z(sb, 2, "previous_timestamp_millis", Long.valueOf(r4Var.Y()));
                            }
                            if (r4Var.f0()) {
                                Z(sb, 2, "count", Integer.valueOf(r4Var.m()));
                            }
                            if (r4Var.U() != 0) {
                                a0(sb, 2, r4Var.e0());
                            }
                            V(sb, 2);
                            sb.append("}\n");
                        }
                    }
                }
                V(sb, 1);
                sb.append("}\n");
            }
        }
        sb.append("}\n");
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final List<Long> N(List<Long> list, List<Integer> list2) {
        int i8;
        ArrayList arrayList = new ArrayList(list);
        for (Integer num : list2) {
            if (num.intValue() < 0) {
                i().J().b("Ignoring negative bit index to be cleared", num);
            } else {
                int intValue = num.intValue() / 64;
                if (intValue >= arrayList.size()) {
                    i().J().c("Ignoring bit index greater than bitSet size", num, Integer.valueOf(arrayList.size()));
                } else {
                    arrayList.set(intValue, Long.valueOf(((Long) arrayList.get(intValue)).longValue() & (~(1 << (num.intValue() % 64)))));
                }
            }
        }
        int size = arrayList.size();
        int size2 = arrayList.size() - 1;
        while (true) {
            int i9 = size2;
            i8 = size;
            size = i9;
            if (size < 0 || ((Long) arrayList.get(size)).longValue() != 0) {
                break;
            }
            size2 = size - 1;
        }
        return arrayList.subList(0, i8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Map<String, Object> O(Bundle bundle, boolean z4) {
        Parcelable[] parcelableArr;
        HashMap hashMap = new HashMap();
        for (String str : bundle.keySet()) {
            Object obj = bundle.get(str);
            boolean z8 = obj instanceof Parcelable[];
            if (z8 || (obj instanceof ArrayList) || (obj instanceof Bundle)) {
                if (z4) {
                    ArrayList arrayList = new ArrayList();
                    if (z8) {
                        for (Parcelable parcelable : (Parcelable[]) obj) {
                            if (parcelable instanceof Bundle) {
                                arrayList.add(O((Bundle) parcelable, false));
                            }
                        }
                    } else if (obj instanceof ArrayList) {
                        ArrayList arrayList2 = (ArrayList) obj;
                        int size = arrayList2.size();
                        int i8 = 0;
                        while (i8 < size) {
                            Object obj2 = arrayList2.get(i8);
                            i8++;
                            if (obj2 instanceof Bundle) {
                                arrayList.add(O((Bundle) obj2, false));
                            }
                        }
                    } else if (obj instanceof Bundle) {
                        arrayList.add(O((Bundle) obj, false));
                    }
                    hashMap.put(str, arrayList);
                }
            } else if (obj != null) {
                hashMap.put(str, obj);
            }
        }
        return hashMap;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void S(t4.a aVar, Object obj) {
        Bundle[] bundleArr;
        n6.j.l(obj);
        aVar.H().F().D().G();
        if (obj instanceof String) {
            aVar.E((String) obj);
        } else if (obj instanceof Long) {
            aVar.z(((Long) obj).longValue());
        } else if (obj instanceof Double) {
            aVar.y(((Double) obj).doubleValue());
        } else if (!(obj instanceof Bundle[])) {
            i().E().b("Ignoring invalid (type) event param value", obj);
        } else {
            ArrayList arrayList = new ArrayList();
            for (Bundle bundle : (Bundle[]) obj) {
                if (bundle != null) {
                    t4.a b02 = com.google.android.gms.internal.measurement.t4.b0();
                    for (String str : bundle.keySet()) {
                        t4.a C = com.google.android.gms.internal.measurement.t4.b0().C(str);
                        Object obj2 = bundle.get(str);
                        if (obj2 instanceof Long) {
                            C.z(((Long) obj2).longValue());
                        } else if (obj2 instanceof String) {
                            C.E((String) obj2);
                        } else if (obj2 instanceof Double) {
                            C.y(((Double) obj2).doubleValue());
                        }
                        b02.A(C);
                    }
                    if (b02.x() > 0) {
                        arrayList.add((com.google.android.gms.internal.measurement.t4) ((com.google.android.gms.internal.measurement.x8) b02.n()));
                    }
                }
            }
            aVar.B(arrayList);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void T(v4.a aVar) {
        i().I().a("Checking account type status for ad personalization signals");
        if (j0(aVar.f1())) {
            i().D().a("Turning off ad personalization due to account type");
            com.google.android.gms.internal.measurement.y4 y4Var = (com.google.android.gms.internal.measurement.y4) ((com.google.android.gms.internal.measurement.x8) com.google.android.gms.internal.measurement.y4.Y().A("_npa").C(c().s()).z(1L).n());
            boolean z4 = false;
            int i8 = 0;
            while (true) {
                if (i8 >= aVar.c0()) {
                    break;
                } else if ("_npa".equals(aVar.N0(i8).b0())) {
                    aVar.B(i8, y4Var);
                    z4 = true;
                    break;
                } else {
                    i8++;
                }
            }
            if (!z4) {
                aVar.H(y4Var);
            }
            if (md.a() && a().r(c0.S0)) {
                j b9 = j.b(aVar.h1());
                b9.d(zziq.zza.AD_PERSONALIZATION, i.CHILD_ACCOUNT);
                aVar.q0(b9.toString());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void U(y4.a aVar, Object obj) {
        n6.j.l(obj);
        aVar.E().B().x();
        if (obj instanceof String) {
            aVar.D((String) obj);
        } else if (obj instanceof Long) {
            aVar.z(((Long) obj).longValue());
        } else if (obj instanceof Double) {
            aVar.y(((Double) obj).doubleValue());
        } else {
            i().E().b("Ignoring invalid (type) user attribute value", obj);
        }
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ e a() {
        return super.a();
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ d b() {
        return super.b();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean b0(long j8, long j9) {
        return j8 == 0 || j9 <= 0 || Math.abs(zzb().a() - j8) > j9;
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

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ void h() {
        super.h();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final byte[] h0(byte[] bArr) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gZIPOutputStream.write(bArr);
            gZIPOutputStream.close();
            byteArrayOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e8) {
            i().E().b("Failed to gzip content", e8);
            throw e8;
        }
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ x4 i() {
        return super.i();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ void j() {
        super.j();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean j0(String str) {
        if (hd.a() && a().r(c0.f16365b1)) {
            return false;
        }
        n6.j.l(str);
        y3 C0 = o().C0(str);
        return C0 != null && c().w() && C0.v() && p().U(str);
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ void k() {
        super.k();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final byte[] k0(byte[] bArr) {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
            GZIPInputStream gZIPInputStream = new GZIPInputStream(byteArrayInputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bArr2 = new byte[RecognitionOptions.UPC_E];
            while (true) {
                int read = gZIPInputStream.read(bArr2);
                if (read <= 0) {
                    gZIPInputStream.close();
                    byteArrayInputStream.close();
                    return byteArrayOutputStream.toByteArray();
                }
                byteArrayOutputStream.write(bArr2, 0, read);
            }
        } catch (IOException e8) {
            i().E().b("Failed to ungzip content", e8);
            throw e8;
        }
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ a6 l() {
        return super.l();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final List<Integer> l0() {
        Map<String, String> c9 = c0.c(this.f16446b.zza());
        if (c9 == null || c9.isEmpty()) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        int intValue = c0.R.a(null).intValue();
        for (Map.Entry<String, String> entry : c9.entrySet()) {
            if (entry.getKey().startsWith("measurement.id.")) {
                try {
                    int parseInt = Integer.parseInt(entry.getValue());
                    if (parseInt != 0) {
                        arrayList.add(Integer.valueOf(parseInt));
                        if (arrayList.size() >= intValue) {
                            i().J().b("Too many experiment IDs. Number of IDs", Integer.valueOf(arrayList.size()));
                            break;
                        }
                        continue;
                    } else {
                        continue;
                    }
                } catch (NumberFormatException e8) {
                    i().J().b("Experiment ID NumberFormatException", e8);
                }
            }
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        return arrayList;
    }

    @Override // com.google.android.gms.measurement.internal.cb
    public final /* bridge */ /* synthetic */ nb m() {
        return super.m();
    }

    @Override // com.google.android.gms.measurement.internal.cb
    public final /* bridge */ /* synthetic */ wb n() {
        return super.n();
    }

    @Override // com.google.android.gms.measurement.internal.cb
    public final /* bridge */ /* synthetic */ l o() {
        return super.o();
    }

    @Override // com.google.android.gms.measurement.internal.cb
    public final /* bridge */ /* synthetic */ r5 p() {
        return super.p();
    }

    @Override // com.google.android.gms.measurement.internal.cb
    public final /* bridge */ /* synthetic */ ia q() {
        return super.q();
    }

    @Override // com.google.android.gms.measurement.internal.cb
    public final /* bridge */ /* synthetic */ eb r() {
        return super.r();
    }

    @Override // com.google.android.gms.measurement.internal.bb
    protected final boolean v() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final long x(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0L;
        }
        return y(str.getBytes(Charset.forName("UTF-8")));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final long y(byte[] bArr) {
        n6.j.l(bArr);
        g().k();
        MessageDigest T0 = sb.T0();
        if (T0 == null) {
            i().E().a("Failed to get MD5");
            return 0L;
        }
        return sb.z(T0.digest(bArr));
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
