package l2;

import android.os.Build;
import io.flutter.plugin.common.i;
import io.flutter.plugin.common.j;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class b implements j.c {

    /* renamed from: a  reason: collision with root package name */
    private final a f21563a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public b(a aVar) {
        this.f21563a = aVar;
    }

    public void onMethodCall(i iVar, j.d dVar) {
        boolean hasVibrator;
        Boolean valueOf;
        String str = iVar.a;
        str.hashCode();
        char c9 = 65535;
        switch (str.hashCode()) {
            case -1367724422:
                if (str.equals("cancel")) {
                    c9 = 0;
                    break;
                }
                break;
            case -314771757:
                if (str.equals("hasVibrator")) {
                    c9 = 1;
                    break;
                }
                break;
            case 86129172:
                if (str.equals("hasAmplitudeControl")) {
                    c9 = 2;
                    break;
                }
                break;
            case 451310959:
                if (str.equals("vibrate")) {
                    c9 = 3;
                    break;
                }
                break;
            case 890723587:
                if (str.equals("hasCustomVibrationsSupport")) {
                    c9 = 4;
                    break;
                }
                break;
        }
        switch (c9) {
            case 0:
                this.f21563a.a().cancel();
                dVar.success((Object) null);
                return;
            case 1:
                hasVibrator = this.f21563a.a().hasVibrator();
                valueOf = Boolean.valueOf(hasVibrator);
                dVar.success(valueOf);
                return;
            case 2:
                if (Build.VERSION.SDK_INT < 26) {
                    valueOf = Boolean.FALSE;
                    dVar.success(valueOf);
                    return;
                }
                hasVibrator = this.f21563a.a().hasAmplitudeControl();
                valueOf = Boolean.valueOf(hasVibrator);
                dVar.success(valueOf);
                return;
            case 3:
                int intValue = ((Integer) iVar.a("duration")).intValue();
                List<Integer> list = (List) iVar.a("pattern");
                int intValue2 = ((Integer) iVar.a("repeat")).intValue();
                List<Integer> list2 = (List) iVar.a("intensities");
                int intValue3 = ((Integer) iVar.a("amplitude")).intValue();
                if (list.size() > 0 && list2.size() > 0) {
                    this.f21563a.d(list, intValue2, list2);
                } else if (list.size() > 0) {
                    this.f21563a.c(list, intValue2);
                } else {
                    this.f21563a.b(intValue, intValue3);
                }
                dVar.success((Object) null);
                return;
            case 4:
                valueOf = Boolean.TRUE;
                dVar.success(valueOf);
                return;
            default:
                dVar.b();
                return;
        }
    }
}
