package com.google.android.gms.internal.measurement;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;
import com.google.common.base.Optional;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class k6 {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {

        /* renamed from: a  reason: collision with root package name */
        private static volatile Optional<l6> f12280a;

        private a() {
        }

        public static Optional<l6> a(Context context) {
            Optional<l6> b9;
            Optional<l6> optional = f12280a;
            if (optional == null) {
                synchronized (a.class) {
                    optional = f12280a;
                    if (optional == null) {
                        new k6();
                        if (o6.c(Build.TYPE, Build.TAGS)) {
                            if (w5.a() && !context.isDeviceProtectedStorage()) {
                                context = context.createDeviceProtectedStorageContext();
                            }
                            b9 = k6.b(context);
                        } else {
                            b9 = Optional.a();
                        }
                        f12280a = b9;
                        optional = b9;
                    }
                }
            }
            return optional;
        }
    }

    private static l6 a(Context context, File file) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            k0.g gVar = new k0.g();
            HashMap hashMap = new HashMap();
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    String valueOf = String.valueOf(file);
                    String packageName = context.getPackageName();
                    Log.w("HermeticFileOverrides", "Parsed " + valueOf + " for Android package " + packageName);
                    b6 b6Var = new b6(gVar);
                    bufferedReader.close();
                    return b6Var;
                }
                String[] split = readLine.split(" ", 3);
                if (split.length != 3) {
                    Log.e("HermeticFileOverrides", "Invalid: " + readLine);
                } else {
                    String c9 = c(split[0]);
                    String decode = Uri.decode(c(split[1]));
                    String str = (String) hashMap.get(split[2]);
                    if (str == null) {
                        String c10 = c(split[2]);
                        str = Uri.decode(c10);
                        if (str.length() < 1024 || str == c10) {
                            hashMap.put(c10, str);
                        }
                    }
                    k0.g gVar2 = (k0.g) gVar.get(c9);
                    if (gVar2 == null) {
                        gVar2 = new k0.g();
                        gVar.put(c9, gVar2);
                    }
                    gVar2.put(decode, str);
                }
            }
        } catch (IOException e8) {
            throw new RuntimeException(e8);
        }
    }

    static Optional<l6> b(Context context) {
        StrictMode.ThreadPolicy allowThreadDiskReads = StrictMode.allowThreadDiskReads();
        try {
            StrictMode.allowThreadDiskWrites();
            Optional<File> d8 = d(context);
            return d8.c() ? Optional.d(a(context, d8.b())) : Optional.a();
        } finally {
            StrictMode.setThreadPolicy(allowThreadDiskReads);
        }
    }

    private static final String c(String str) {
        return new String(str);
    }

    private static Optional<File> d(Context context) {
        try {
            File file = new File(context.getDir("phenotype_hermetic", 0), "overrides.txt");
            return file.exists() ? Optional.d(file) : Optional.a();
        } catch (RuntimeException e8) {
            Log.e("HermeticFileOverrides", "no data dir", e8);
            return Optional.a();
        }
    }
}
