package u;

import android.hardware.camera2.CameraCharacteristics;
import android.util.Range;
import y.s0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a implements s0 {

    /* renamed from: a  reason: collision with root package name */
    private final Range<Integer> f22933a;

    public a(s.y yVar) {
        this.f22933a = d((Range[]) yVar.a(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES));
    }

    private Range<Integer> a(Range<Integer> range) {
        int intValue = range.getUpper().intValue();
        int intValue2 = range.getLower().intValue();
        if (range.getUpper().intValue() >= 1000) {
            intValue = range.getUpper().intValue() / 1000;
        }
        if (range.getLower().intValue() >= 1000) {
            intValue2 = range.getLower().intValue() / 1000;
        }
        return new Range<>(Integer.valueOf(intValue2), Integer.valueOf(intValue));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean c(s.y yVar) {
        Integer num = (Integer) yVar.a(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
        return num != null && num.intValue() == 2;
    }

    private Range<Integer> d(Range<Integer>[] rangeArr) {
        Range<Integer> range = null;
        if (rangeArr != null && rangeArr.length != 0) {
            for (Range<Integer> range2 : rangeArr) {
                Range<Integer> a9 = a(range2);
                if (a9.getUpper().intValue() == 30 && (range == null || a9.getLower().intValue() < range.getLower().intValue())) {
                    range = a9;
                }
            }
        }
        return range;
    }

    public Range<Integer> b() {
        return this.f22933a;
    }
}
