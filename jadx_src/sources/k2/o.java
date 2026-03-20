package k2;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class o {
    private static String a(Context context, String str) {
        int i8 = Build.VERSION.SDK_INT;
        if (i8 < 31 || !d(context, null, str)) {
            if (i8 >= 29) {
                if (i8 < 29 || !d(context, null, "android.permission.ACCESS_FINE_LOCATION")) {
                    return null;
                }
                return "android.permission.ACCESS_FINE_LOCATION";
            } else if (d(context, null, "android.permission.ACCESS_FINE_LOCATION")) {
                return "android.permission.ACCESS_FINE_LOCATION";
            } else {
                if (d(context, null, "android.permission.ACCESS_COARSE_LOCATION")) {
                    return "android.permission.ACCESS_COARSE_LOCATION";
                }
                return null;
            }
        }
        return str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:106:0x0159, code lost:
        if (d(r5, r0, "android.permission.BODY_SENSORS") != false) goto L42;
     */
    /* JADX WARN: Code restructure failed: missing block: B:111:0x0167, code lost:
        if (d(r5, r0, "android.permission.READ_MEDIA_IMAGES") != false) goto L42;
     */
    /* JADX WARN: Code restructure failed: missing block: B:146:0x01ed, code lost:
        if (d(r5, r0, "android.permission.RECORD_AUDIO") != false) goto L42;
     */
    /* JADX WARN: Code restructure failed: missing block: B:152:0x01fb, code lost:
        if (d(r5, r0, "android.permission.ACCESS_BACKGROUND_LOCATION") != false) goto L42;
     */
    /* JADX WARN: Code restructure failed: missing block: B:158:0x020f, code lost:
        if (d(r5, r0, "android.permission.ACCESS_FINE_LOCATION") != false) goto L42;
     */
    /* JADX WARN: Code restructure failed: missing block: B:170:0x023a, code lost:
        if (d(r5, r0, "android.permission.CAMERA") != false) goto L42;
     */
    /* JADX WARN: Code restructure failed: missing block: B:171:0x023c, code lost:
        r0.add(r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:176:0x0251, code lost:
        if (d(r5, r0, "android.permission.WRITE_CALENDAR") != false) goto L42;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0055, code lost:
        if (r5 != null) goto L8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x005e, code lost:
        if (r5 != null) goto L8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0067, code lost:
        if (r5 != null) goto L8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x0092, code lost:
        if (d(r5, r0, "android.permission.SYSTEM_ALERT_WINDOW") != false) goto L42;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x00ad, code lost:
        if (d(r5, r0, "android.permission.BLUETOOTH") != false) goto L42;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x00ea, code lost:
        if (d(r5, r0, "android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS") != false) goto L42;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.util.List<java.lang.String> b(android.content.Context r5, int r6) {
        /*
            Method dump skipped, instructions count: 672
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: k2.o.b(android.content.Context, int):java.util.List");
    }

    private static PackageInfo c(Context context) {
        PackageManager packageManager = context.getPackageManager();
        int i8 = Build.VERSION.SDK_INT;
        String packageName = context.getPackageName();
        return i8 >= 33 ? packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(4096L)) : packageManager.getPackageInfo(packageName, RecognitionOptions.AZTEC);
    }

    private static boolean d(Context context, ArrayList<String> arrayList, String str) {
        if (arrayList != null) {
            try {
                Iterator<String> it = arrayList.iterator();
                while (it.hasNext()) {
                    if (it.next().equals(str)) {
                        return true;
                    }
                }
            } catch (Exception e8) {
                Log.d("permissions_handler", "Unable to check manifest for permission: ", e8);
            }
        }
        if (context == null) {
            Log.d("permissions_handler", "Unable to detect current Activity or App Context.");
            return false;
        }
        PackageInfo c9 = c(context);
        if (c9 == null) {
            Log.d("permissions_handler", "Unable to get Package info, will not be able to determine permissions to request.");
            return false;
        }
        Iterator it2 = new ArrayList(Arrays.asList(c9.requestedPermissions)).iterator();
        while (it2.hasNext()) {
            if (((String) it2.next()).equals(str)) {
                return true;
            }
        }
        return false;
    }

    static boolean e(Activity activity, String str) {
        if (activity == null) {
            return false;
        }
        return !androidx.core.app.b.w(activity, str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int f(String str) {
        str.hashCode();
        char c9 = 65535;
        switch (str.hashCode()) {
            case -2062386608:
                if (str.equals("android.permission.READ_SMS")) {
                    c9 = 0;
                    break;
                }
                break;
            case -1928411001:
                if (str.equals("android.permission.READ_CALENDAR")) {
                    c9 = 1;
                    break;
                }
                break;
            case -1925850455:
                if (str.equals("android.permission.POST_NOTIFICATIONS")) {
                    c9 = 2;
                    break;
                }
                break;
            case -1921431796:
                if (str.equals("android.permission.READ_CALL_LOG")) {
                    c9 = 3;
                    break;
                }
                break;
            case -1888586689:
                if (str.equals("android.permission.ACCESS_FINE_LOCATION")) {
                    c9 = 4;
                    break;
                }
                break;
            case -1813079487:
                if (str.equals("android.permission.MANAGE_EXTERNAL_STORAGE")) {
                    c9 = 5;
                    break;
                }
                break;
            case -1783097621:
                if (str.equals("android.permission.ACCESS_NOTIFICATION_POLICY")) {
                    c9 = 6;
                    break;
                }
                break;
            case -1561629405:
                if (str.equals("android.permission.SYSTEM_ALERT_WINDOW")) {
                    c9 = 7;
                    break;
                }
                break;
            case -1479758289:
                if (str.equals("android.permission.RECEIVE_WAP_PUSH")) {
                    c9 = '\b';
                    break;
                }
                break;
            case -1238066820:
                if (str.equals("android.permission.BODY_SENSORS")) {
                    c9 = '\t';
                    break;
                }
                break;
            case -1164582768:
                if (str.equals("android.permission.READ_PHONE_NUMBERS")) {
                    c9 = '\n';
                    break;
                }
                break;
            case -909527021:
                if (str.equals("android.permission.NEARBY_WIFI_DEVICES")) {
                    c9 = 11;
                    break;
                }
                break;
            case -901151997:
                if (str.equals("android.permission.BIND_CALL_REDIRECTION_SERVICE")) {
                    c9 = '\f';
                    break;
                }
                break;
            case -895679497:
                if (str.equals("android.permission.RECEIVE_MMS")) {
                    c9 = '\r';
                    break;
                }
                break;
            case -895673731:
                if (str.equals("android.permission.RECEIVE_SMS")) {
                    c9 = 14;
                    break;
                }
                break;
            case -798669607:
                if (str.equals("android.permission.BLUETOOTH_CONNECT")) {
                    c9 = 15;
                    break;
                }
                break;
            case -406040016:
                if (str.equals("android.permission.READ_EXTERNAL_STORAGE")) {
                    c9 = 16;
                    break;
                }
                break;
            case -63024214:
                if (str.equals("android.permission.ACCESS_COARSE_LOCATION")) {
                    c9 = 17;
                    break;
                }
                break;
            case -5573545:
                if (str.equals("android.permission.READ_PHONE_STATE")) {
                    c9 = 18;
                    break;
                }
                break;
            case 52602690:
                if (str.equals("android.permission.SEND_SMS")) {
                    c9 = 19;
                    break;
                }
                break;
            case 112197485:
                if (str.equals("android.permission.CALL_PHONE")) {
                    c9 = 20;
                    break;
                }
                break;
            case 175802396:
                if (str.equals("android.permission.READ_MEDIA_IMAGES")) {
                    c9 = 21;
                    break;
                }
                break;
            case 214526995:
                if (str.equals("android.permission.WRITE_CONTACTS")) {
                    c9 = 22;
                    break;
                }
                break;
            case 463403621:
                if (str.equals("android.permission.CAMERA")) {
                    c9 = 23;
                    break;
                }
                break;
            case 603653886:
                if (str.equals("android.permission.WRITE_CALENDAR")) {
                    c9 = 24;
                    break;
                }
                break;
            case 610633091:
                if (str.equals("android.permission.WRITE_CALL_LOG")) {
                    c9 = 25;
                    break;
                }
                break;
            case 691260818:
                if (str.equals("android.permission.READ_MEDIA_AUDIO")) {
                    c9 = 26;
                    break;
                }
                break;
            case 710297143:
                if (str.equals("android.permission.READ_MEDIA_VIDEO")) {
                    c9 = 27;
                    break;
                }
                break;
            case 784519842:
                if (str.equals("android.permission.USE_SIP")) {
                    c9 = 28;
                    break;
                }
                break;
            case 970694249:
                if (str.equals("android.permission.SCHEDULE_EXACT_ALARM")) {
                    c9 = 29;
                    break;
                }
                break;
            case 1166454870:
                if (str.equals("android.permission.BLUETOOTH_ADVERTISE")) {
                    c9 = 30;
                    break;
                }
                break;
            case 1271781903:
                if (str.equals("android.permission.GET_ACCOUNTS")) {
                    c9 = 31;
                    break;
                }
                break;
            case 1365911975:
                if (str.equals("android.permission.WRITE_EXTERNAL_STORAGE")) {
                    c9 = ' ';
                    break;
                }
                break;
            case 1777263169:
                if (str.equals("android.permission.REQUEST_INSTALL_PACKAGES")) {
                    c9 = '!';
                    break;
                }
                break;
            case 1780337063:
                if (str.equals("android.permission.ACTIVITY_RECOGNITION")) {
                    c9 = '\"';
                    break;
                }
                break;
            case 1831139720:
                if (str.equals("android.permission.RECORD_AUDIO")) {
                    c9 = '#';
                    break;
                }
                break;
            case 1977429404:
                if (str.equals("android.permission.READ_CONTACTS")) {
                    c9 = '$';
                    break;
                }
                break;
            case 2024715147:
                if (str.equals("android.permission.ACCESS_BACKGROUND_LOCATION")) {
                    c9 = '%';
                    break;
                }
                break;
            case 2062356686:
                if (str.equals("android.permission.BLUETOOTH_SCAN")) {
                    c9 = '&';
                    break;
                }
                break;
            case 2114579147:
                if (str.equals("android.permission.ACCESS_MEDIA_LOCATION")) {
                    c9 = '\'';
                    break;
                }
                break;
            case 2133799037:
                if (str.equals("com.android.voicemail.permission.ADD_VOICEMAIL")) {
                    c9 = '(';
                    break;
                }
                break;
        }
        switch (c9) {
            case 0:
            case '\b':
            case '\r':
            case 14:
            case 19:
                return 13;
            case 1:
            case 24:
                return 0;
            case 2:
                return 17;
            case 3:
            case '\n':
            case '\f':
            case 18:
            case 20:
            case 25:
            case 28:
            case '(':
                return 8;
            case 4:
            case 17:
                return 3;
            case 5:
                return 22;
            case 6:
                return 27;
            case 7:
                return 23;
            case '\t':
                return 12;
            case 11:
                return 31;
            case 15:
                return 30;
            case 16:
            case ' ':
                return 15;
            case 21:
                return 9;
            case 22:
            case 31:
            case '$':
                return 2;
            case 23:
                return 1;
            case 26:
                return 33;
            case 27:
                return 32;
            case 29:
                return 34;
            case 30:
                return 29;
            case '!':
                return 24;
            case '\"':
                return 19;
            case '#':
                return 7;
            case '%':
                return 4;
            case '&':
                return 28;
            case '\'':
                return 18;
            default:
                return 20;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int g(Activity activity, String str, int i8) {
        if (i8 == -1) {
            return (Build.VERSION.SDK_INT < 23 || !e(activity, str)) ? 0 : 4;
        }
        return 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void h(Activity activity, int i8) {
        List<String> b9;
        if (activity == null || (b9 = b(activity, i8)) == null) {
            return;
        }
        b9.isEmpty();
    }
}
