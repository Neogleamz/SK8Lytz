package r6;

import android.os.Parcel;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.moduleinstall.ModuleAvailabilityResponse;
import com.google.android.gms.common.moduleinstall.ModuleInstallIntentResponse;
import com.google.android.gms.common.moduleinstall.ModuleInstallResponse;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class d extends a7.b implements e {
    public d() {
        super("com.google.android.gms.common.moduleinstall.internal.IModuleInstallCallbacks");
    }

    @Override // a7.b
    protected final boolean g(int i8, Parcel parcel, Parcel parcel2, int i9) {
        if (i8 == 1) {
            a7.c.b(parcel);
            Q0((Status) a7.c.a(parcel, Status.CREATOR), (ModuleAvailabilityResponse) a7.c.a(parcel, ModuleAvailabilityResponse.CREATOR));
        } else if (i8 == 2) {
            a7.c.b(parcel);
            H0((Status) a7.c.a(parcel, Status.CREATOR), (ModuleInstallResponse) a7.c.a(parcel, ModuleInstallResponse.CREATOR));
        } else if (i8 == 3) {
            a7.c.b(parcel);
            Y((Status) a7.c.a(parcel, Status.CREATOR), (ModuleInstallIntentResponse) a7.c.a(parcel, ModuleInstallIntentResponse.CREATOR));
        } else if (i8 != 4) {
            return false;
        } else {
            a7.c.b(parcel);
            J1((Status) a7.c.a(parcel, Status.CREATOR));
        }
        return true;
    }
}
