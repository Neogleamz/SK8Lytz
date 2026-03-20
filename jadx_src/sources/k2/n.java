package k2;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import io.flutter.plugin.common.l;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class n implements l.a, l.d {

    /* renamed from: a  reason: collision with root package name */
    private b f20971a;

    /* renamed from: b  reason: collision with root package name */
    private Activity f20972b;

    /* renamed from: c  reason: collision with root package name */
    private Map<Integer, Integer> f20973c;

    /* renamed from: d  reason: collision with root package name */
    private boolean f20974d = false;

    @FunctionalInterface
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    interface a {
        void a(int i8);
    }

    @FunctionalInterface
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    interface b {
        void a(Map<Integer, Integer> map);
    }

    @FunctionalInterface
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    interface c {
        void a(boolean z4);
    }

    private int a(Context context) {
        List<String> b9 = o.b(context, 21);
        if (b9 == null || b9.isEmpty()) {
            Log.d("permissions_handler", "Bluetooth permission missing in manifest");
            return 0;
        }
        return 1;
    }

    private int b(Context context) {
        return Build.VERSION.SDK_INT < 33 ? androidx.core.app.n.e(context).a() ? 1 : 0 : context.checkSelfPermission("android.permission.POST_NOTIFICATIONS") == 0 ? 1 : 0;
    }

    private int d(int i8, Context context) {
        if (i8 == 17) {
            return b(context);
        }
        if (i8 == 21) {
            return a(context);
        }
        if ((i8 == 30 || i8 == 28 || i8 == 29) && Build.VERSION.SDK_INT < 31) {
            return a(context);
        }
        List<String> b9 = o.b(context, i8);
        if (b9 == null) {
            Log.d("permissions_handler", "No android specific permissions needed for: " + i8);
            return 1;
        } else if (b9.size() == 0) {
            Log.d("permissions_handler", "No permissions found in manifest for: " + b9 + i8);
            if (i8 != 16 || Build.VERSION.SDK_INT >= 23) {
                if (i8 != 22 || Build.VERSION.SDK_INT >= 30) {
                    return Build.VERSION.SDK_INT < 23 ? 1 : 0;
                }
                return 2;
            }
            return 2;
        } else {
            Object[] objArr = context.getApplicationInfo().targetSdkVersion >= 23 ? 1 : null;
            for (String str : b9) {
                if (objArr != null) {
                    if (i8 == 16) {
                        String packageName = context.getPackageName();
                        PowerManager powerManager = (PowerManager) context.getSystemService("power");
                        if (Build.VERSION.SDK_INT >= 23) {
                            return (powerManager == null || !powerManager.isIgnoringBatteryOptimizations(packageName)) ? 0 : 1;
                        }
                        return 2;
                    } else if (i8 == 22) {
                        if (Build.VERSION.SDK_INT < 30) {
                            return 2;
                        }
                        return Environment.isExternalStorageManager() ? 1 : 0;
                    } else if (i8 == 23 && Build.VERSION.SDK_INT >= 23) {
                        return Settings.canDrawOverlays(context) ? 1 : 0;
                    } else {
                        if (i8 == 24 && Build.VERSION.SDK_INT >= 26) {
                            return context.getPackageManager().canRequestPackageInstalls() ? 1 : 0;
                        }
                        if (i8 == 27 && Build.VERSION.SDK_INT >= 23) {
                            return ((NotificationManager) context.getSystemService("notification")).isNotificationPolicyAccessGranted() ? 1 : 0;
                        }
                        if (androidx.core.content.a.a(context, str) != 0) {
                            return 0;
                        }
                    }
                }
            }
            return 1;
        }
    }

    private void e(String str, int i8) {
        String packageName = this.f20972b.getPackageName();
        Intent intent = new Intent();
        intent.setAction(str);
        intent.setData(Uri.parse("package:" + packageName));
        this.f20972b.startActivityForResult(intent, i8);
    }

    private void f(String str, int i8) {
        this.f20972b.startActivityForResult(new Intent(str), i8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c(int i8, Context context, a aVar) {
        aVar.a(d(i8, context));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void g(List<Integer> list, Activity activity, b bVar, k2.b bVar2) {
        int i8;
        Map<Integer, Integer> map;
        Map<Integer, Integer> map2;
        int i9;
        int i10;
        String str;
        String str2;
        if (this.f20974d) {
            str2 = "A request for permissions is already running, please wait for it to finish before doing another request (note that you can request multiple permissions at the same time).";
        } else if (activity != null) {
            this.f20971a = bVar;
            this.f20972b = activity;
            this.f20973c = new HashMap();
            ArrayList arrayList = new ArrayList();
            Iterator<Integer> it = list.iterator();
            while (true) {
                int i11 = 1;
                if (!it.hasNext()) {
                    break;
                }
                Integer next = it.next();
                if (d(next.intValue(), activity) != 1) {
                    List<String> b9 = o.b(activity, next.intValue());
                    if (b9 != null && !b9.isEmpty()) {
                        int i12 = Build.VERSION.SDK_INT;
                        if (i12 >= 23 && next.intValue() == 16) {
                            i10 = 209;
                            str = "android.settings.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS";
                        } else if (i12 >= 30 && next.intValue() == 22) {
                            i10 = 210;
                            str = "android.settings.MANAGE_APP_ALL_FILES_ACCESS_PERMISSION";
                        } else if (i12 >= 23 && next.intValue() == 23) {
                            i10 = 211;
                            str = "android.settings.action.MANAGE_OVERLAY_PERMISSION";
                        } else if (i12 >= 26 && next.intValue() == 24) {
                            i10 = 212;
                            str = "android.settings.MANAGE_UNKNOWN_APP_SOURCES";
                        } else if (i12 < 23 || next.intValue() != 27) {
                            arrayList.addAll(b9);
                        } else {
                            f("android.settings.NOTIFICATION_POLICY_ACCESS_SETTINGS", 213);
                        }
                        e(str, i10);
                    } else if (!this.f20973c.containsKey(next)) {
                        i11 = 2;
                        if (next.intValue() != 16 || Build.VERSION.SDK_INT >= 23) {
                            map2 = this.f20973c;
                            i9 = 0;
                        } else {
                            map2 = this.f20973c;
                            i9 = 2;
                        }
                        map2.put(next, i9);
                        if (next.intValue() != 22 || Build.VERSION.SDK_INT >= 30) {
                            map = this.f20973c;
                            i8 = 0;
                        }
                        map = this.f20973c;
                        i8 = Integer.valueOf(i11);
                    }
                } else if (!this.f20973c.containsKey(next)) {
                    map = this.f20973c;
                    i8 = Integer.valueOf(i11);
                }
                map.put(next, i8);
            }
            String[] strArr = (String[]) arrayList.toArray(new String[0]);
            if (arrayList.size() > 0) {
                this.f20974d = true;
                androidx.core.app.b.t(activity, strArr, 24);
                return;
            }
            this.f20974d = false;
            if (this.f20973c.size() > 0) {
                bVar.a(this.f20973c);
                return;
            }
            return;
        } else {
            Log.d("permissions_handler", "Unable to detect current Activity.");
            str2 = "Unable to detect current Android Activity.";
        }
        bVar2.a("PermissionHandler.PermissionManager", str2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void i(int i8, Activity activity, c cVar, k2.b bVar) {
        if (activity == null) {
            Log.d("permissions_handler", "Unable to detect current Activity.");
            bVar.a("PermissionHandler.PermissionManager", "Unable to detect current Android Activity.");
            return;
        }
        List<String> b9 = o.b(activity, i8);
        if (b9 == null) {
            Log.d("permissions_handler", "No android specific permissions needed for: " + i8);
            cVar.a(false);
        } else if (!b9.isEmpty()) {
            cVar.a(androidx.core.app.b.w(activity, b9.get(0)));
        } else {
            Log.d("permissions_handler", "No permissions found in manifest for: " + i8 + " no need to show request rationale");
            cVar.a(false);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean onActivityResult(int i8, int i9, Intent intent) {
        int i10;
        if (i8 == 209 || i8 == 210 || i8 == 211 || i8 == 212 || i8 == 213) {
            boolean z4 = i9 == -1;
            int i11 = 23;
            if (i8 == 209) {
                i11 = 16;
                i10 = z4;
            } else if (i8 == 210) {
                if (Build.VERSION.SDK_INT < 30) {
                    return false;
                }
                i11 = 22;
                i10 = Environment.isExternalStorageManager();
            } else if (i8 == 211) {
                if (Build.VERSION.SDK_INT < 23) {
                    return false;
                }
                i10 = Settings.canDrawOverlays(this.f20972b);
            } else if (i8 == 212) {
                if (Build.VERSION.SDK_INT < 26) {
                    return false;
                }
                i11 = 24;
                i10 = this.f20972b.getPackageManager().canRequestPackageInstalls();
            } else if (i8 != 213 || Build.VERSION.SDK_INT < 23) {
                return false;
            } else {
                i11 = 27;
                i10 = ((NotificationManager) this.f20972b.getSystemService("notification")).isNotificationPolicyAccessGranted();
            }
            HashMap hashMap = new HashMap();
            hashMap.put(Integer.valueOf(i11), Integer.valueOf(i10));
            this.f20971a.a(hashMap);
            return true;
        }
        return false;
    }

    public boolean onRequestPermissionsResult(int i8, String[] strArr, int[] iArr) {
        Map<Integer, Integer> map;
        int valueOf;
        int g8;
        Map<Integer, Integer> map2;
        int valueOf2;
        if (i8 != 24) {
            this.f20974d = false;
            return false;
        } else if (this.f20973c == null) {
            return false;
        } else {
            for (int i9 = 0; i9 < strArr.length; i9++) {
                String str = strArr[i9];
                int f5 = o.f(str);
                if (f5 != 20) {
                    int i10 = iArr[i9];
                    if (f5 == 7) {
                        if (!this.f20973c.containsKey(7)) {
                            this.f20973c.put(7, Integer.valueOf(o.g(this.f20972b, str, i10)));
                        }
                        if (!this.f20973c.containsKey(14)) {
                            map = this.f20973c;
                            valueOf = 14;
                            map.put(valueOf, Integer.valueOf(o.g(this.f20972b, str, i10)));
                        }
                        o.h(this.f20972b, f5);
                    } else if (f5 == 4) {
                        g8 = o.g(this.f20972b, str, i10);
                        if (!this.f20973c.containsKey(4)) {
                            map2 = this.f20973c;
                            valueOf2 = 4;
                            map2.put(valueOf2, Integer.valueOf(g8));
                        }
                        o.h(this.f20972b, f5);
                    } else if (f5 == 3) {
                        g8 = o.g(this.f20972b, str, i10);
                        if (Build.VERSION.SDK_INT < 29 && !this.f20973c.containsKey(4)) {
                            this.f20973c.put(4, Integer.valueOf(g8));
                        }
                        if (!this.f20973c.containsKey(5)) {
                            this.f20973c.put(5, Integer.valueOf(g8));
                        }
                        map2 = this.f20973c;
                        valueOf2 = Integer.valueOf(f5);
                        map2.put(valueOf2, Integer.valueOf(g8));
                        o.h(this.f20972b, f5);
                    } else {
                        if (!this.f20973c.containsKey(Integer.valueOf(f5))) {
                            map = this.f20973c;
                            valueOf = Integer.valueOf(f5);
                            map.put(valueOf, Integer.valueOf(o.g(this.f20972b, str, i10)));
                        }
                        o.h(this.f20972b, f5);
                    }
                }
            }
            this.f20971a.a(this.f20973c);
            this.f20974d = false;
            return true;
        }
    }
}
