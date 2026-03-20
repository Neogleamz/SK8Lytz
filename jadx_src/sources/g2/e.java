package g2;

import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.zengge.wifi.Common.App;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class e {
    public static String a() {
        String upperCase = ((TelephonyManager) App.o().getSystemService("phone")).getSimCountryIso().toUpperCase();
        return TextUtils.isEmpty(upperCase) ? App.o().getResources().getConfiguration().locale.getCountry() : upperCase;
    }
}
