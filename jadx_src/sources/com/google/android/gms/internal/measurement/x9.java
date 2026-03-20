package com.google.android.gms.internal.measurement;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class x9 implements fa {

    /* renamed from: a  reason: collision with root package name */
    private fa[] f12670a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public x9(fa... faVarArr) {
        this.f12670a = faVarArr;
    }

    @Override // com.google.android.gms.internal.measurement.fa
    public final ga a(Class<?> cls) {
        fa[] faVarArr;
        for (fa faVar : this.f12670a) {
            if (faVar.b(cls)) {
                return faVar.a(cls);
            }
        }
        throw new UnsupportedOperationException("No factory is available for message type: " + cls.getName());
    }

    @Override // com.google.android.gms.internal.measurement.fa
    public final boolean b(Class<?> cls) {
        for (fa faVar : this.f12670a) {
            if (faVar.b(cls)) {
                return true;
            }
        }
        return false;
    }
}
