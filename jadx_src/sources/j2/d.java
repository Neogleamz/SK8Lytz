package j2;

import com.daimajia.numberprogressbar.BuildConfig;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class d implements i2.d {
    @Override // i2.d
    public String a(StackTraceElement stackTraceElement) {
        return BuildConfig.FLAVOR;
    }

    @Override // i2.d
    public String b(StackTraceElement stackTraceElement, boolean z4, boolean z8) {
        String e8;
        StringBuilder sb = new StringBuilder();
        sb.append(stackTraceElement.getClassName());
        sb.append(".");
        sb.append(stackTraceElement.getMethodName());
        if (stackTraceElement.isNativeMethod()) {
            e8 = d();
        } else if (stackTraceElement.getFileName() == null || stackTraceElement.getFileName().length() <= 0) {
            e8 = e();
        } else {
            sb.append("(");
            sb.append(stackTraceElement.getFileName());
            if (stackTraceElement.getLineNumber() >= 0) {
                sb.append(":");
                sb.append(stackTraceElement.getLineNumber());
            }
            e8 = ")";
        }
        sb.append(e8);
        if (z8) {
            sb.append(c(stackTraceElement));
        }
        return sb.toString();
    }

    @Override // i2.d
    public String c(StackTraceElement stackTraceElement) {
        StringBuilder sb = new StringBuilder();
        String className = stackTraceElement.getClassName();
        Class<?> a9 = a.f20634b.a(className);
        if (a9 != null) {
            sb.append(i2.a.m(i2.a.l(a9), i2.a.q(a.f20633a, a9, i2.a.n(className))));
        }
        return sb.toString();
    }

    public String d() {
        return "(Native Method)";
    }

    public String e() {
        return "(Unknown Source)";
    }
}
