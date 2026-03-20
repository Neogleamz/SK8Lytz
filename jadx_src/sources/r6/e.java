package r6;

import android.os.IInterface;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.moduleinstall.ModuleAvailabilityResponse;
import com.google.android.gms.common.moduleinstall.ModuleInstallIntentResponse;
import com.google.android.gms.common.moduleinstall.ModuleInstallResponse;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface e extends IInterface {
    void H0(Status status, ModuleInstallResponse moduleInstallResponse);

    void J1(Status status);

    void Q0(Status status, ModuleAvailabilityResponse moduleAvailabilityResponse);

    void Y(Status status, ModuleInstallIntentResponse moduleInstallIntentResponse);
}
