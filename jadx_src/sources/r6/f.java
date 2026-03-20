package r6;

import android.os.Parcel;
import com.google.android.gms.common.moduleinstall.ModuleInstallStatusUpdate;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class f extends a7.b implements g {
    public f() {
        super("com.google.android.gms.common.moduleinstall.internal.IModuleInstallStatusListener");
    }

    @Override // a7.b
    protected final boolean g(int i8, Parcel parcel, Parcel parcel2, int i9) {
        if (i8 == 1) {
            a7.c.b(parcel);
            F1((ModuleInstallStatusUpdate) a7.c.a(parcel, ModuleInstallStatusUpdate.CREATOR));
            return true;
        }
        return false;
    }
}
