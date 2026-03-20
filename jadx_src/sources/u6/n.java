package u6;

import android.app.Application;
import android.os.Build;
import android.os.Process;
import android.os.StrictMode;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class n {

    /* renamed from: a  reason: collision with root package name */
    private static String f23077a;

    /* renamed from: b  reason: collision with root package name */
    private static int f23078b;

    public static String a() {
        BufferedReader bufferedReader;
        if (f23077a == null) {
            if (Build.VERSION.SDK_INT >= 28) {
                f23077a = Application.getProcessName();
            } else {
                int i8 = f23078b;
                if (i8 == 0) {
                    i8 = Process.myPid();
                    f23078b = i8;
                }
                String str = null;
                str = null;
                str = null;
                BufferedReader bufferedReader2 = null;
                if (i8 > 0) {
                    try {
                        String str2 = "/proc/" + i8 + "/cmdline";
                        StrictMode.ThreadPolicy allowThreadDiskReads = StrictMode.allowThreadDiskReads();
                        try {
                            bufferedReader = new BufferedReader(new FileReader(str2));
                            try {
                                String readLine = bufferedReader.readLine();
                                n6.j.l(readLine);
                                str = readLine.trim();
                            } catch (IOException unused) {
                            } catch (Throwable th) {
                                th = th;
                                bufferedReader2 = bufferedReader;
                                j.a(bufferedReader2);
                                throw th;
                            }
                        } finally {
                            StrictMode.setThreadPolicy(allowThreadDiskReads);
                        }
                    } catch (IOException unused2) {
                        bufferedReader = null;
                    } catch (Throwable th2) {
                        th = th2;
                    }
                    j.a(bufferedReader);
                }
                f23077a = str;
            }
        }
        return f23077a;
    }
}
