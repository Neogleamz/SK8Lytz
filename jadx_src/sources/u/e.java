package u;

import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build;
import android.util.Size;
import androidx.camera.core.p1;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import y.s0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class e implements s0 {

    /* renamed from: a  reason: collision with root package name */
    private final List<Size> f22935a;

    public e(s.y yVar) {
        StreamConfigurationMap streamConfigurationMap = (StreamConfigurationMap) yVar.a(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        if (streamConfigurationMap == null) {
            p1.c("CamcorderProfileResolutionQuirk", "StreamConfigurationMap is null");
        }
        Size[] sizeArr = null;
        if (Build.VERSION.SDK_INT < 23) {
            if (streamConfigurationMap != null) {
                sizeArr = streamConfigurationMap.getOutputSizes(SurfaceTexture.class);
            }
        } else if (streamConfigurationMap != null) {
            sizeArr = streamConfigurationMap.getOutputSizes(34);
        }
        List<Size> asList = sizeArr != null ? Arrays.asList((Size[]) sizeArr.clone()) : Collections.emptyList();
        this.f22935a = asList;
        p1.a("CamcorderProfileResolutionQuirk", "mSupportedResolutions = " + asList);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean b(s.y yVar) {
        Integer num = (Integer) yVar.a(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
        return num != null && num.intValue() == 2;
    }

    public List<Size> a() {
        return new ArrayList(this.f22935a);
    }
}
