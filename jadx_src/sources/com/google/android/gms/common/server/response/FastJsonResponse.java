package com.google.android.gms.common.server.response;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.server.converter.zaa;
import com.google.android.gms.common.util.VisibleForTesting;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import n6.i;
import n6.j;
import u6.k;
import u6.l;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class FastJsonResponse {

    @VisibleForTesting
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class Field<I, O> extends AbstractSafeParcelable {
        public static final com.google.android.gms.common.server.response.a CREATOR = new com.google.android.gms.common.server.response.a();

        /* renamed from: a  reason: collision with root package name */
        private final int f11952a;

        /* renamed from: b  reason: collision with root package name */
        protected final int f11953b;

        /* renamed from: c  reason: collision with root package name */
        protected final boolean f11954c;

        /* renamed from: d  reason: collision with root package name */
        protected final int f11955d;

        /* renamed from: e  reason: collision with root package name */
        protected final boolean f11956e;

        /* renamed from: f  reason: collision with root package name */
        protected final String f11957f;

        /* renamed from: g  reason: collision with root package name */
        protected final int f11958g;

        /* renamed from: h  reason: collision with root package name */
        protected final Class f11959h;

        /* renamed from: j  reason: collision with root package name */
        protected final String f11960j;

        /* renamed from: k  reason: collision with root package name */
        private zan f11961k;

        /* renamed from: l  reason: collision with root package name */
        private a f11962l;

        /* JADX INFO: Access modifiers changed from: package-private */
        public Field(int i8, int i9, boolean z4, int i10, boolean z8, String str, int i11, String str2, zaa zaaVar) {
            this.f11952a = i8;
            this.f11953b = i9;
            this.f11954c = z4;
            this.f11955d = i10;
            this.f11956e = z8;
            this.f11957f = str;
            this.f11958g = i11;
            if (str2 == null) {
                this.f11959h = null;
                this.f11960j = null;
            } else {
                this.f11959h = SafeParcelResponse.class;
                this.f11960j = str2;
            }
            if (zaaVar == null) {
                this.f11962l = null;
            } else {
                this.f11962l = zaaVar.u();
            }
        }

        public final Object D0(Object obj) {
            j.l(this.f11962l);
            return this.f11962l.h(obj);
        }

        final String E0() {
            String str = this.f11960j;
            if (str == null) {
                return null;
            }
            return str;
        }

        public final Map I0() {
            j.l(this.f11960j);
            j.l(this.f11961k);
            return (Map) j.l(this.f11961k.u(this.f11960j));
        }

        public final void T0(zan zanVar) {
            this.f11961k = zanVar;
        }

        public final boolean W0() {
            return this.f11962l != null;
        }

        public int t() {
            return this.f11958g;
        }

        public final String toString() {
            i.a a9 = i.c(this).a("versionCode", Integer.valueOf(this.f11952a)).a("typeIn", Integer.valueOf(this.f11953b)).a("typeInArray", Boolean.valueOf(this.f11954c)).a("typeOut", Integer.valueOf(this.f11955d)).a("typeOutArray", Boolean.valueOf(this.f11956e)).a("outputFieldName", this.f11957f).a("safeParcelFieldId", Integer.valueOf(this.f11958g)).a("concreteTypeName", E0());
            Class cls = this.f11959h;
            if (cls != null) {
                a9.a("concreteType.class", cls.getCanonicalName());
            }
            a aVar = this.f11962l;
            if (aVar != null) {
                a9.a("converterName", aVar.getClass().getCanonicalName());
            }
            return a9.toString();
        }

        final zaa u() {
            a aVar = this.f11962l;
            if (aVar == null) {
                return null;
            }
            return zaa.t(aVar);
        }

        @Override // android.os.Parcelable
        public final void writeToParcel(Parcel parcel, int i8) {
            int a9 = o6.a.a(parcel);
            o6.a.l(parcel, 1, this.f11952a);
            o6.a.l(parcel, 2, this.f11953b);
            o6.a.c(parcel, 3, this.f11954c);
            o6.a.l(parcel, 4, this.f11955d);
            o6.a.c(parcel, 5, this.f11956e);
            o6.a.r(parcel, 6, this.f11957f, false);
            o6.a.l(parcel, 7, t());
            o6.a.r(parcel, 8, E0(), false);
            o6.a.q(parcel, 9, u(), i8, false);
            o6.a.b(parcel, a9);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a<I, O> {
        Object h(Object obj);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static final Object f(Field field, Object obj) {
        return field.f11962l != null ? field.D0(obj) : obj;
    }

    private static final void g(StringBuilder sb, Field field, Object obj) {
        String fastJsonResponse;
        int i8 = field.f11953b;
        if (i8 == 11) {
            Class cls = field.f11959h;
            j.l(cls);
            fastJsonResponse = ((FastJsonResponse) cls.cast(obj)).toString();
        } else if (i8 != 7) {
            sb.append(obj);
            return;
        } else {
            fastJsonResponse = "\"";
            sb.append("\"");
            sb.append(k.a((String) obj));
        }
        sb.append(fastJsonResponse);
    }

    public abstract Map<String, Field<?, ?>> a();

    /* JADX INFO: Access modifiers changed from: protected */
    public Object b(Field field) {
        String str = field.f11957f;
        if (field.f11959h != null) {
            j.r(c(str) == null, "Concrete field shouldn't be value object: %s", field.f11957f);
            try {
                char upperCase = Character.toUpperCase(str.charAt(0));
                String substring = str.substring(1);
                return getClass().getMethod("get" + upperCase + substring, new Class[0]).invoke(this, new Object[0]);
            } catch (Exception e8) {
                throw new RuntimeException(e8);
            }
        }
        return c(str);
    }

    protected abstract Object c(String str);

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean d(Field field) {
        if (field.f11955d == 11) {
            if (field.f11956e) {
                throw new UnsupportedOperationException("Concrete type arrays not supported");
            }
            throw new UnsupportedOperationException("Concrete types not supported");
        }
        return e(field.f11957f);
    }

    protected abstract boolean e(String str);

    public String toString() {
        String str;
        String a9;
        Map<String, Field<?, ?>> a10 = a();
        StringBuilder sb = new StringBuilder(100);
        for (String str2 : a10.keySet()) {
            Field<?, ?> field = a10.get(str2);
            if (d(field)) {
                Object f5 = f(field, b(field));
                if (sb.length() == 0) {
                    sb.append("{");
                } else {
                    sb.append(",");
                }
                sb.append("\"");
                sb.append(str2);
                sb.append("\":");
                if (f5 != null) {
                    switch (field.f11955d) {
                        case 8:
                            sb.append("\"");
                            a9 = u6.c.a((byte[]) f5);
                            sb.append(a9);
                            sb.append("\"");
                            break;
                        case 9:
                            sb.append("\"");
                            a9 = u6.c.b((byte[]) f5);
                            sb.append(a9);
                            sb.append("\"");
                            break;
                        case 10:
                            l.a(sb, (HashMap) f5);
                            break;
                        default:
                            if (field.f11954c) {
                                ArrayList arrayList = (ArrayList) f5;
                                sb.append("[");
                                int size = arrayList.size();
                                for (int i8 = 0; i8 < size; i8++) {
                                    if (i8 > 0) {
                                        sb.append(",");
                                    }
                                    Object obj = arrayList.get(i8);
                                    if (obj != null) {
                                        g(sb, field, obj);
                                    }
                                }
                                str = "]";
                                break;
                            } else {
                                g(sb, field, f5);
                                break;
                            }
                    }
                } else {
                    str = "null";
                }
                sb.append(str);
            }
        }
        sb.append(sb.length() > 0 ? "}" : "{}");
        return sb.toString();
    }
}
