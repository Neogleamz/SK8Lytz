package com.google.android.gms.internal.measurement;

import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class v0 extends z {
    /* JADX INFO: Access modifiers changed from: protected */
    public v0() {
        this.f12724a.add(zzbv.ASSIGN);
        this.f12724a.add(zzbv.CONST);
        this.f12724a.add(zzbv.CREATE_ARRAY);
        this.f12724a.add(zzbv.CREATE_OBJECT);
        this.f12724a.add(zzbv.EXPRESSION_LIST);
        this.f12724a.add(zzbv.GET);
        this.f12724a.add(zzbv.GET_INDEX);
        this.f12724a.add(zzbv.GET_PROPERTY);
        this.f12724a.add(zzbv.NULL);
        this.f12724a.add(zzbv.SET_PROPERTY);
        this.f12724a.add(zzbv.TYPEOF);
        this.f12724a.add(zzbv.UNDEFINED);
        this.f12724a.add(zzbv.VAR);
    }

    @Override // com.google.android.gms.internal.measurement.z
    public final r b(String str, g6 g6Var, List<r> list) {
        String str2;
        int i8 = 0;
        switch (u0.f12544a[e5.c(str).ordinal()]) {
            case 1:
                e5.f(zzbv.ASSIGN, 2, list);
                r b9 = g6Var.b(list.get(0));
                if (b9 instanceof t) {
                    if (g6Var.g(b9.e())) {
                        r b10 = g6Var.b(list.get(1));
                        g6Var.h(b9.e(), b10);
                        return b10;
                    }
                    throw new IllegalArgumentException(String.format("Attempting to assign undefined value %s", b9.e()));
                }
                throw new IllegalArgumentException(String.format("Expected string for assign var. got %s", b9.getClass().getCanonicalName()));
            case 2:
                e5.j(zzbv.CONST, 2, list);
                if (list.size() % 2 == 0) {
                    for (int i9 = 0; i9 < list.size() - 1; i9 += 2) {
                        r b11 = g6Var.b(list.get(i9));
                        if (!(b11 instanceof t)) {
                            throw new IllegalArgumentException(String.format("Expected string for const name. got %s", b11.getClass().getCanonicalName()));
                        }
                        g6Var.f(b11.e(), g6Var.b(list.get(i9 + 1)));
                    }
                    return r.f12463r;
                }
                throw new IllegalArgumentException(String.format("CONST requires an even number of arguments, found %s", Integer.valueOf(list.size())));
            case 3:
                if (list.isEmpty()) {
                    return new g();
                }
                g gVar = new g();
                for (r rVar : list) {
                    r b12 = g6Var.b(rVar);
                    if (b12 instanceof k) {
                        throw new IllegalStateException("Failed to evaluate array element");
                    }
                    gVar.y(i8, b12);
                    i8++;
                }
                return gVar;
            case 4:
                if (list.isEmpty()) {
                    return new q();
                }
                if (list.size() % 2 == 0) {
                    q qVar = new q();
                    while (i8 < list.size() - 1) {
                        r b13 = g6Var.b(list.get(i8));
                        r b14 = g6Var.b(list.get(i8 + 1));
                        if ((b13 instanceof k) || (b14 instanceof k)) {
                            throw new IllegalStateException("Failed to evaluate map entry");
                        }
                        qVar.n(b13.e(), b14);
                        i8 += 2;
                    }
                    return qVar;
                }
                throw new IllegalArgumentException(String.format("CREATE_OBJECT requires an even number of arguments, found %s", Integer.valueOf(list.size())));
            case 5:
                e5.j(zzbv.EXPRESSION_LIST, 1, list);
                r rVar2 = r.f12463r;
                while (i8 < list.size()) {
                    rVar2 = g6Var.b(list.get(i8));
                    if (rVar2 instanceof k) {
                        throw new IllegalStateException("ControlValue cannot be in an expression list");
                    }
                    i8++;
                }
                return rVar2;
            case 6:
                e5.f(zzbv.GET, 1, list);
                r b15 = g6Var.b(list.get(0));
                if (b15 instanceof t) {
                    return g6Var.c(b15.e());
                }
                throw new IllegalArgumentException(String.format("Expected string for get var. got %s", b15.getClass().getCanonicalName()));
            case 7:
            case 8:
                e5.f(zzbv.GET_PROPERTY, 2, list);
                r b16 = g6Var.b(list.get(0));
                r b17 = g6Var.b(list.get(1));
                if ((b16 instanceof g) && e5.l(b17)) {
                    return ((g) b16).p(b17.d().intValue());
                }
                if (b16 instanceof l) {
                    return ((l) b16).h(b17.e());
                }
                if (b16 instanceof t) {
                    if ("length".equals(b17.e())) {
                        return new j(Double.valueOf(b16.e().length()));
                    }
                    if (e5.l(b17) && b17.d().doubleValue() < b16.e().length()) {
                        return new t(String.valueOf(b16.e().charAt(b17.d().intValue())));
                    }
                }
                return r.f12463r;
            case 9:
                e5.f(zzbv.NULL, 0, list);
                return r.f12464s;
            case 10:
                e5.f(zzbv.SET_PROPERTY, 3, list);
                r b18 = g6Var.b(list.get(0));
                r b19 = g6Var.b(list.get(1));
                r b20 = g6Var.b(list.get(2));
                if (b18 == r.f12463r || b18 == r.f12464s) {
                    throw new IllegalStateException(String.format("Can't set property %s of %s", b19.e(), b18.e()));
                }
                if ((b18 instanceof g) && (b19 instanceof j)) {
                    ((g) b18).y(b19.d().intValue(), b20);
                } else if (b18 instanceof l) {
                    ((l) b18).n(b19.e(), b20);
                }
                return b20;
            case 11:
                e5.f(zzbv.TYPEOF, 1, list);
                r b21 = g6Var.b(list.get(0));
                if (b21 instanceof y) {
                    str2 = "undefined";
                } else if (b21 instanceof h) {
                    str2 = "boolean";
                } else if (b21 instanceof j) {
                    str2 = "number";
                } else if (b21 instanceof t) {
                    str2 = "string";
                } else if (b21 instanceof s) {
                    str2 = "function";
                } else if ((b21 instanceof u) || (b21 instanceof k)) {
                    throw new IllegalArgumentException(String.format("Unsupported value type %s in typeof", b21));
                } else {
                    str2 = "object";
                }
                return new t(str2);
            case 12:
                e5.f(zzbv.UNDEFINED, 0, list);
                return r.f12463r;
            case 13:
                e5.j(zzbv.VAR, 1, list);
                for (r rVar3 : list) {
                    r b22 = g6Var.b(rVar3);
                    if (!(b22 instanceof t)) {
                        throw new IllegalArgumentException(String.format("Expected string for var name. got %s", b22.getClass().getCanonicalName()));
                    }
                    g6Var.e(b22.e(), r.f12463r);
                }
                return r.f12463r;
            default:
                return super.a(str);
        }
    }
}
