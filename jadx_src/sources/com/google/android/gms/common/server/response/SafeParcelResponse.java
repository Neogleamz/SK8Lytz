package com.google.android.gms.common.server.response;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
import com.google.android.gms.common.server.response.FastJsonResponse;
import com.google.android.gms.common.util.VisibleForTesting;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import n6.j;
import u6.k;
import u6.l;
@VisibleForTesting
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class SafeParcelResponse extends FastSafeParcelableJsonResponse {
    public static final Parcelable.Creator<SafeParcelResponse> CREATOR = new e();

    /* renamed from: a  reason: collision with root package name */
    private final int f11963a;

    /* renamed from: b  reason: collision with root package name */
    private final Parcel f11964b;

    /* renamed from: c  reason: collision with root package name */
    private final int f11965c = 2;

    /* renamed from: d  reason: collision with root package name */
    private final zan f11966d;

    /* renamed from: e  reason: collision with root package name */
    private final String f11967e;

    /* renamed from: f  reason: collision with root package name */
    private int f11968f;

    /* renamed from: g  reason: collision with root package name */
    private int f11969g;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SafeParcelResponse(int i8, Parcel parcel, zan zanVar) {
        this.f11963a = i8;
        this.f11964b = (Parcel) j.l(parcel);
        this.f11966d = zanVar;
        this.f11967e = zanVar == null ? null : zanVar.t();
        this.f11968f = 2;
    }

    private final void i(StringBuilder sb, Map map, Parcel parcel) {
        Object c9;
        String a9;
        String str;
        Object valueOf;
        Object f5;
        SparseArray sparseArray = new SparseArray();
        for (Map.Entry entry : map.entrySet()) {
            sparseArray.put(((FastJsonResponse.Field) entry.getValue()).t(), entry);
        }
        sb.append('{');
        int I = SafeParcelReader.I(parcel);
        boolean z4 = false;
        while (parcel.dataPosition() < I) {
            int B = SafeParcelReader.B(parcel);
            Map.Entry entry2 = (Map.Entry) sparseArray.get(SafeParcelReader.u(B));
            if (entry2 != null) {
                if (z4) {
                    sb.append(",");
                }
                FastJsonResponse.Field field = (FastJsonResponse.Field) entry2.getValue();
                sb.append("\"");
                sb.append((String) entry2.getKey());
                sb.append("\":");
                if (field.W0()) {
                    int i8 = field.f11955d;
                    switch (i8) {
                        case 0:
                            valueOf = Integer.valueOf(SafeParcelReader.D(parcel, B));
                            f5 = FastJsonResponse.f(field, valueOf);
                            break;
                        case 1:
                            valueOf = SafeParcelReader.c(parcel, B);
                            f5 = FastJsonResponse.f(field, valueOf);
                            break;
                        case 2:
                            valueOf = Long.valueOf(SafeParcelReader.E(parcel, B));
                            f5 = FastJsonResponse.f(field, valueOf);
                            break;
                        case 3:
                            valueOf = Float.valueOf(SafeParcelReader.z(parcel, B));
                            f5 = FastJsonResponse.f(field, valueOf);
                            break;
                        case 4:
                            valueOf = Double.valueOf(SafeParcelReader.x(parcel, B));
                            f5 = FastJsonResponse.f(field, valueOf);
                            break;
                        case 5:
                            valueOf = SafeParcelReader.a(parcel, B);
                            f5 = FastJsonResponse.f(field, valueOf);
                            break;
                        case 6:
                            valueOf = Boolean.valueOf(SafeParcelReader.v(parcel, B));
                            f5 = FastJsonResponse.f(field, valueOf);
                            break;
                        case 7:
                            valueOf = SafeParcelReader.o(parcel, B);
                            f5 = FastJsonResponse.f(field, valueOf);
                            break;
                        case 8:
                        case 9:
                            f5 = FastJsonResponse.f(field, SafeParcelReader.g(parcel, B));
                            break;
                        case 10:
                            Bundle f8 = SafeParcelReader.f(parcel, B);
                            HashMap hashMap = new HashMap();
                            for (String str2 : f8.keySet()) {
                                hashMap.put(str2, (String) j.l(f8.getString(str2)));
                            }
                            f5 = FastJsonResponse.f(field, hashMap);
                            break;
                        case 11:
                            throw new IllegalArgumentException("Method does not accept concrete type.");
                        default:
                            throw new IllegalArgumentException("Unknown field out type = " + i8);
                    }
                    k(sb, field, f5);
                } else {
                    if (field.f11956e) {
                        sb.append("[");
                        switch (field.f11955d) {
                            case 0:
                                u6.b.e(sb, SafeParcelReader.j(parcel, B));
                                break;
                            case 1:
                                u6.b.g(sb, SafeParcelReader.d(parcel, B));
                                break;
                            case 2:
                                u6.b.f(sb, SafeParcelReader.k(parcel, B));
                                break;
                            case 3:
                                u6.b.d(sb, SafeParcelReader.i(parcel, B));
                                break;
                            case 4:
                                u6.b.c(sb, SafeParcelReader.h(parcel, B));
                                break;
                            case 5:
                                u6.b.g(sb, SafeParcelReader.b(parcel, B));
                                break;
                            case 6:
                                u6.b.h(sb, SafeParcelReader.e(parcel, B));
                                break;
                            case 7:
                                u6.b.i(sb, SafeParcelReader.p(parcel, B));
                                break;
                            case 8:
                            case 9:
                            case 10:
                                throw new UnsupportedOperationException("List of type BASE64, BASE64_URL_SAFE, or STRING_MAP is not supported");
                            case 11:
                                Parcel[] m8 = SafeParcelReader.m(parcel, B);
                                int length = m8.length;
                                for (int i9 = 0; i9 < length; i9++) {
                                    if (i9 > 0) {
                                        sb.append(",");
                                    }
                                    m8[i9].setDataPosition(0);
                                    i(sb, field.I0(), m8[i9]);
                                }
                                break;
                            default:
                                throw new IllegalStateException("Unknown field type out.");
                        }
                        str = "]";
                    } else {
                        switch (field.f11955d) {
                            case 0:
                                sb.append(SafeParcelReader.D(parcel, B));
                                break;
                            case 1:
                                c9 = SafeParcelReader.c(parcel, B);
                                sb.append(c9);
                                break;
                            case 2:
                                sb.append(SafeParcelReader.E(parcel, B));
                                break;
                            case 3:
                                sb.append(SafeParcelReader.z(parcel, B));
                                break;
                            case 4:
                                sb.append(SafeParcelReader.x(parcel, B));
                                break;
                            case 5:
                                c9 = SafeParcelReader.a(parcel, B);
                                sb.append(c9);
                                break;
                            case 6:
                                sb.append(SafeParcelReader.v(parcel, B));
                                break;
                            case 7:
                                String o5 = SafeParcelReader.o(parcel, B);
                                sb.append("\"");
                                a9 = k.a(o5);
                                sb.append(a9);
                                sb.append("\"");
                                break;
                            case 8:
                                byte[] g8 = SafeParcelReader.g(parcel, B);
                                sb.append("\"");
                                a9 = u6.c.a(g8);
                                sb.append(a9);
                                sb.append("\"");
                                break;
                            case 9:
                                byte[] g9 = SafeParcelReader.g(parcel, B);
                                sb.append("\"");
                                a9 = u6.c.b(g9);
                                sb.append(a9);
                                sb.append("\"");
                                break;
                            case 10:
                                Bundle f9 = SafeParcelReader.f(parcel, B);
                                Set<String> keySet = f9.keySet();
                                sb.append("{");
                                boolean z8 = true;
                                for (String str3 : keySet) {
                                    if (!z8) {
                                        sb.append(",");
                                    }
                                    sb.append("\"");
                                    sb.append(str3);
                                    sb.append("\":\"");
                                    sb.append(k.a(f9.getString(str3)));
                                    sb.append("\"");
                                    z8 = false;
                                }
                                str = "}";
                                break;
                            case 11:
                                Parcel l8 = SafeParcelReader.l(parcel, B);
                                l8.setDataPosition(0);
                                i(sb, field.I0(), l8);
                                break;
                            default:
                                throw new IllegalStateException("Unknown field type out");
                        }
                    }
                    sb.append(str);
                }
                z4 = true;
            }
        }
        if (parcel.dataPosition() == I) {
            sb.append('}');
            return;
        }
        throw new SafeParcelReader.ParseException("Overread allowed size end=" + I, parcel);
    }

    private static final void j(StringBuilder sb, int i8, Object obj) {
        switch (i8) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                sb.append(obj);
                return;
            case 7:
                sb.append("\"");
                sb.append(k.a(j.l(obj).toString()));
                sb.append("\"");
                return;
            case 8:
                sb.append("\"");
                sb.append(u6.c.a((byte[]) obj));
                sb.append("\"");
                return;
            case 9:
                sb.append("\"");
                sb.append(u6.c.b((byte[]) obj));
                sb.append("\"");
                return;
            case 10:
                l.a(sb, (HashMap) j.l(obj));
                return;
            case 11:
                throw new IllegalArgumentException("Method does not accept concrete type.");
            default:
                throw new IllegalArgumentException("Unknown type = " + i8);
        }
    }

    private static final void k(StringBuilder sb, FastJsonResponse.Field field, Object obj) {
        if (!field.f11954c) {
            j(sb, field.f11953b, obj);
            return;
        }
        ArrayList arrayList = (ArrayList) obj;
        sb.append("[");
        int size = arrayList.size();
        for (int i8 = 0; i8 < size; i8++) {
            if (i8 != 0) {
                sb.append(",");
            }
            j(sb, field.f11953b, arrayList.get(i8));
        }
        sb.append("]");
    }

    @Override // com.google.android.gms.common.server.response.FastJsonResponse
    public final Map<String, FastJsonResponse.Field<?, ?>> a() {
        zan zanVar = this.f11966d;
        if (zanVar == null) {
            return null;
        }
        return zanVar.u((String) j.l(this.f11967e));
    }

    @Override // com.google.android.gms.common.server.response.FastSafeParcelableJsonResponse, com.google.android.gms.common.server.response.FastJsonResponse
    public final Object c(String str) {
        throw new UnsupportedOperationException("Converting to JSON does not require this method.");
    }

    @Override // com.google.android.gms.common.server.response.FastSafeParcelableJsonResponse, com.google.android.gms.common.server.response.FastJsonResponse
    public final boolean e(String str) {
        throw new UnsupportedOperationException("Converting to JSON does not require this method.");
    }

    public final Parcel h() {
        int i8 = this.f11968f;
        if (i8 != 0) {
            if (i8 == 1) {
                o6.a.b(this.f11964b, this.f11969g);
            }
            return this.f11964b;
        }
        int a9 = o6.a.a(this.f11964b);
        this.f11969g = a9;
        o6.a.b(this.f11964b, a9);
        this.f11968f = 2;
        return this.f11964b;
    }

    @Override // com.google.android.gms.common.server.response.FastJsonResponse
    public final String toString() {
        j.m(this.f11966d, "Cannot convert to JSON on client side.");
        Parcel h8 = h();
        h8.setDataPosition(0);
        StringBuilder sb = new StringBuilder(100);
        i(sb, (Map) j.l(this.f11966d.u((String) j.l(this.f11967e))), h8);
        return sb.toString();
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 1, this.f11963a);
        o6.a.p(parcel, 2, h(), false);
        o6.a.q(parcel, 3, this.f11965c != 0 ? this.f11966d : null, i8, false);
        o6.a.b(parcel, a9);
    }
}
