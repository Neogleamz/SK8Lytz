package n6;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;
import java.util.Locale;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class u {

    /* renamed from: a  reason: collision with root package name */
    private static final k0.g f22203a = new k0.g();

    /* renamed from: b  reason: collision with root package name */
    private static Locale f22204b;

    public static String a(Context context) {
        String packageName = context.getPackageName();
        try {
            return w6.c.a(context).d(packageName).toString();
        } catch (PackageManager.NameNotFoundException | NullPointerException unused) {
            String str = context.getApplicationInfo().name;
            return TextUtils.isEmpty(str) ? packageName : str;
        }
    }

    public static String b(Context context) {
        return context.getResources().getString(h6.c.f20333g);
    }

    public static String c(Context context, int i8) {
        return context.getResources().getString(i8 != 1 ? i8 != 2 ? i8 != 3 ? 17039370 : h6.c.f20327a : h6.c.f20336j : h6.c.f20330d);
    }

    public static String d(Context context, int i8) {
        Resources resources = context.getResources();
        String a9 = a(context);
        if (i8 != 1) {
            if (i8 == 2) {
                return u6.h.d(context) ? resources.getString(h6.c.f20340n) : resources.getString(h6.c.f20337k, a9);
            } else if (i8 != 3) {
                if (i8 != 5) {
                    if (i8 != 7) {
                        if (i8 != 9) {
                            if (i8 != 20) {
                                switch (i8) {
                                    case 16:
                                        return h(context, "common_google_play_services_api_unavailable_text", a9);
                                    case 17:
                                        return h(context, "common_google_play_services_sign_in_failed_text", a9);
                                    case 18:
                                        return resources.getString(h6.c.f20339m, a9);
                                    default:
                                        return resources.getString(j6.c.f20797a, a9);
                                }
                            }
                            return h(context, "common_google_play_services_restricted_profile_text", a9);
                        }
                        return resources.getString(h6.c.f20335i, a9);
                    }
                    return h(context, "common_google_play_services_network_error_text", a9);
                }
                return h(context, "common_google_play_services_invalid_account_text", a9);
            } else {
                return resources.getString(h6.c.f20328b, a9);
            }
        }
        return resources.getString(h6.c.f20331e, a9);
    }

    public static String e(Context context, int i8) {
        return (i8 == 6 || i8 == 19) ? h(context, "common_google_play_services_resolution_required_text", a(context)) : d(context, i8);
    }

    public static String f(Context context, int i8) {
        String i9 = i8 == 6 ? i(context, "common_google_play_services_resolution_required_title") : g(context, i8);
        return i9 == null ? context.getResources().getString(h6.c.f20334h) : i9;
    }

    public static String g(Context context, int i8) {
        String str;
        Resources resources = context.getResources();
        switch (i8) {
            case 1:
                return resources.getString(h6.c.f20332f);
            case 2:
                return resources.getString(h6.c.f20338l);
            case 3:
                return resources.getString(h6.c.f20329c);
            case 4:
            case 6:
            case 18:
                return null;
            case 5:
                Log.e("GoogleApiAvailability", "An invalid account was specified when connecting. Please provide a valid account.");
                return i(context, "common_google_play_services_invalid_account_title");
            case 7:
                Log.e("GoogleApiAvailability", "Network error occurred. Please retry request later.");
                return i(context, "common_google_play_services_network_error_title");
            case 8:
                str = "Internal error occurred. Please see logs for detailed information";
                break;
            case 9:
                str = "Google Play services is invalid. Cannot recover.";
                break;
            case 10:
                str = "Developer error occurred. Please see logs for detailed information";
                break;
            case 11:
                str = "The application is not licensed to the user.";
                break;
            case 12:
            case 13:
            case 14:
            case 15:
            case 19:
            default:
                str = "Unexpected error code " + i8;
                break;
            case 16:
                str = "One of the API components you attempted to connect to is not available.";
                break;
            case 17:
                Log.e("GoogleApiAvailability", "The specified account could not be signed in.");
                return i(context, "common_google_play_services_sign_in_failed_title");
            case 20:
                Log.e("GoogleApiAvailability", "The current user profile is restricted and could not use authenticated features.");
                return i(context, "common_google_play_services_restricted_profile_title");
        }
        Log.e("GoogleApiAvailability", str);
        return null;
    }

    private static String h(Context context, String str, String str2) {
        Resources resources = context.getResources();
        String i8 = i(context, str);
        if (i8 == null) {
            i8 = resources.getString(j6.c.f20797a);
        }
        return String.format(resources.getConfiguration().locale, i8, str2);
    }

    private static String i(Context context, String str) {
        k0.g gVar = f22203a;
        synchronized (gVar) {
            Locale d8 = androidx.core.os.f.a(context.getResources().getConfiguration()).d(0);
            if (!d8.equals(f22204b)) {
                gVar.clear();
                f22204b = d8;
            }
            String str2 = (String) gVar.get(str);
            if (str2 != null) {
                return str2;
            }
            Resources d9 = com.google.android.gms.common.c.d(context);
            if (d9 == null) {
                return null;
            }
            int identifier = d9.getIdentifier(str, "string", "com.google.android.gms");
            if (identifier == 0) {
                Log.w("GoogleApiAvailability", "Missing resource: " + str);
                return null;
            }
            String string = d9.getString(identifier);
            if (!TextUtils.isEmpty(string)) {
                gVar.put(str, string);
                return string;
            }
            Log.w("GoogleApiAvailability", "Got empty resource: " + str);
            return null;
        }
    }
}
