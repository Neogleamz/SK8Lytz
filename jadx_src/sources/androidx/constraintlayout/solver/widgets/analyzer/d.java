package androidx.constraintlayout.solver.widgets.analyzer;

import java.util.ArrayList;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class d implements o0.a {

    /* renamed from: d  reason: collision with root package name */
    k f3750d;

    /* renamed from: f  reason: collision with root package name */
    int f3752f;

    /* renamed from: g  reason: collision with root package name */
    public int f3753g;

    /* renamed from: a  reason: collision with root package name */
    public o0.a f3747a = null;

    /* renamed from: b  reason: collision with root package name */
    public boolean f3748b = false;

    /* renamed from: c  reason: collision with root package name */
    public boolean f3749c = false;

    /* renamed from: e  reason: collision with root package name */
    a f3751e = a.UNKNOWN;

    /* renamed from: h  reason: collision with root package name */
    int f3754h = 1;

    /* renamed from: i  reason: collision with root package name */
    e f3755i = null;

    /* renamed from: j  reason: collision with root package name */
    public boolean f3756j = false;

    /* renamed from: k  reason: collision with root package name */
    List<o0.a> f3757k = new ArrayList();

    /* renamed from: l  reason: collision with root package name */
    List<d> f3758l = new ArrayList();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    enum a {
        UNKNOWN,
        HORIZONTAL_DIMENSION,
        VERTICAL_DIMENSION,
        LEFT,
        RIGHT,
        TOP,
        BOTTOM,
        BASELINE
    }

    public d(k kVar) {
        this.f3750d = kVar;
    }

    @Override // o0.a
    public void a(o0.a aVar) {
        for (d dVar : this.f3758l) {
            if (!dVar.f3756j) {
                return;
            }
        }
        this.f3749c = true;
        o0.a aVar2 = this.f3747a;
        if (aVar2 != null) {
            aVar2.a(this);
        }
        if (this.f3748b) {
            this.f3750d.a(this);
            return;
        }
        d dVar2 = null;
        int i8 = 0;
        for (d dVar3 : this.f3758l) {
            if (!(dVar3 instanceof e)) {
                i8++;
                dVar2 = dVar3;
            }
        }
        if (dVar2 != null && i8 == 1 && dVar2.f3756j) {
            e eVar = this.f3755i;
            if (eVar != null) {
                if (!eVar.f3756j) {
                    return;
                }
                this.f3752f = this.f3754h * eVar.f3753g;
            }
            d(dVar2.f3753g + this.f3752f);
        }
        o0.a aVar3 = this.f3747a;
        if (aVar3 != null) {
            aVar3.a(this);
        }
    }

    public void b(o0.a aVar) {
        this.f3757k.add(aVar);
        if (this.f3756j) {
            aVar.a(aVar);
        }
    }

    public void c() {
        this.f3758l.clear();
        this.f3757k.clear();
        this.f3756j = false;
        this.f3753g = 0;
        this.f3749c = false;
        this.f3748b = false;
    }

    public void d(int i8) {
        if (this.f3756j) {
            return;
        }
        this.f3756j = true;
        this.f3753g = i8;
        for (o0.a aVar : this.f3757k) {
            aVar.a(aVar);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.f3750d.f3783b.s());
        sb.append(":");
        sb.append(this.f3751e);
        sb.append("(");
        sb.append(this.f3756j ? Integer.valueOf(this.f3753g) : "unresolved");
        sb.append(") <t=");
        sb.append(this.f3758l.size());
        sb.append(":d=");
        sb.append(this.f3757k.size());
        sb.append(">");
        return sb.toString();
    }
}
