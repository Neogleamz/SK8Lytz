package androidx.camera.camera2.internal;

import androidx.camera.core.impl.SurfaceConfig;
import java.util.ArrayList;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c2 {
    public static List<y.y0> a(int i8, boolean z4, boolean z8) {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(d());
        if (i8 == 0 || i8 == 1 || i8 == 3) {
            arrayList.addAll(f());
        }
        if (i8 == 1 || i8 == 3) {
            arrayList.addAll(c());
        }
        if (z4) {
            arrayList.addAll(g());
        }
        if (z8 && i8 == 0) {
            arrayList.addAll(b());
        }
        if (i8 == 3) {
            arrayList.addAll(e());
        }
        return arrayList;
    }

    public static List<y.y0> b() {
        ArrayList arrayList = new ArrayList();
        y.y0 y0Var = new y.y0();
        SurfaceConfig.ConfigType configType = SurfaceConfig.ConfigType.PRIV;
        SurfaceConfig.ConfigSize configSize = SurfaceConfig.ConfigSize.PREVIEW;
        y0Var.a(SurfaceConfig.a(configType, configSize));
        SurfaceConfig.ConfigSize configSize2 = SurfaceConfig.ConfigSize.MAXIMUM;
        y0Var.a(SurfaceConfig.a(configType, configSize2));
        arrayList.add(y0Var);
        y.y0 y0Var2 = new y.y0();
        y0Var2.a(SurfaceConfig.a(configType, configSize));
        SurfaceConfig.ConfigType configType2 = SurfaceConfig.ConfigType.YUV;
        y0Var2.a(SurfaceConfig.a(configType2, configSize2));
        arrayList.add(y0Var2);
        y.y0 y0Var3 = new y.y0();
        y0Var3.a(SurfaceConfig.a(configType2, configSize));
        y0Var3.a(SurfaceConfig.a(configType2, configSize2));
        arrayList.add(y0Var3);
        return arrayList;
    }

    public static List<y.y0> c() {
        ArrayList arrayList = new ArrayList();
        y.y0 y0Var = new y.y0();
        SurfaceConfig.ConfigType configType = SurfaceConfig.ConfigType.PRIV;
        SurfaceConfig.ConfigSize configSize = SurfaceConfig.ConfigSize.PREVIEW;
        y0Var.a(SurfaceConfig.a(configType, configSize));
        SurfaceConfig.ConfigSize configSize2 = SurfaceConfig.ConfigSize.MAXIMUM;
        y0Var.a(SurfaceConfig.a(configType, configSize2));
        arrayList.add(y0Var);
        y.y0 y0Var2 = new y.y0();
        y0Var2.a(SurfaceConfig.a(configType, configSize));
        SurfaceConfig.ConfigType configType2 = SurfaceConfig.ConfigType.YUV;
        y0Var2.a(SurfaceConfig.a(configType2, configSize2));
        arrayList.add(y0Var2);
        y.y0 y0Var3 = new y.y0();
        y0Var3.a(SurfaceConfig.a(configType2, configSize));
        y0Var3.a(SurfaceConfig.a(configType2, configSize2));
        arrayList.add(y0Var3);
        y.y0 y0Var4 = new y.y0();
        y0Var4.a(SurfaceConfig.a(configType, configSize));
        y0Var4.a(SurfaceConfig.a(configType, configSize));
        y0Var4.a(SurfaceConfig.a(SurfaceConfig.ConfigType.JPEG, configSize2));
        arrayList.add(y0Var4);
        y.y0 y0Var5 = new y.y0();
        SurfaceConfig.ConfigSize configSize3 = SurfaceConfig.ConfigSize.VGA;
        y0Var5.a(SurfaceConfig.a(configType2, configSize3));
        y0Var5.a(SurfaceConfig.a(configType, configSize));
        y0Var5.a(SurfaceConfig.a(configType2, configSize2));
        arrayList.add(y0Var5);
        y.y0 y0Var6 = new y.y0();
        y0Var6.a(SurfaceConfig.a(configType2, configSize3));
        y0Var6.a(SurfaceConfig.a(configType2, configSize));
        y0Var6.a(SurfaceConfig.a(configType2, configSize2));
        arrayList.add(y0Var6);
        return arrayList;
    }

    public static List<y.y0> d() {
        ArrayList arrayList = new ArrayList();
        y.y0 y0Var = new y.y0();
        SurfaceConfig.ConfigType configType = SurfaceConfig.ConfigType.PRIV;
        SurfaceConfig.ConfigSize configSize = SurfaceConfig.ConfigSize.MAXIMUM;
        y0Var.a(SurfaceConfig.a(configType, configSize));
        arrayList.add(y0Var);
        y.y0 y0Var2 = new y.y0();
        SurfaceConfig.ConfigType configType2 = SurfaceConfig.ConfigType.JPEG;
        y0Var2.a(SurfaceConfig.a(configType2, configSize));
        arrayList.add(y0Var2);
        y.y0 y0Var3 = new y.y0();
        SurfaceConfig.ConfigType configType3 = SurfaceConfig.ConfigType.YUV;
        y0Var3.a(SurfaceConfig.a(configType3, configSize));
        arrayList.add(y0Var3);
        y.y0 y0Var4 = new y.y0();
        SurfaceConfig.ConfigSize configSize2 = SurfaceConfig.ConfigSize.PREVIEW;
        y0Var4.a(SurfaceConfig.a(configType, configSize2));
        y0Var4.a(SurfaceConfig.a(configType2, configSize));
        arrayList.add(y0Var4);
        y.y0 y0Var5 = new y.y0();
        y0Var5.a(SurfaceConfig.a(configType3, configSize2));
        y0Var5.a(SurfaceConfig.a(configType2, configSize));
        arrayList.add(y0Var5);
        y.y0 y0Var6 = new y.y0();
        y0Var6.a(SurfaceConfig.a(configType, configSize2));
        y0Var6.a(SurfaceConfig.a(configType, configSize2));
        arrayList.add(y0Var6);
        y.y0 y0Var7 = new y.y0();
        y0Var7.a(SurfaceConfig.a(configType, configSize2));
        y0Var7.a(SurfaceConfig.a(configType3, configSize2));
        arrayList.add(y0Var7);
        y.y0 y0Var8 = new y.y0();
        y0Var8.a(SurfaceConfig.a(configType, configSize2));
        y0Var8.a(SurfaceConfig.a(configType3, configSize2));
        y0Var8.a(SurfaceConfig.a(configType2, configSize));
        arrayList.add(y0Var8);
        return arrayList;
    }

    public static List<y.y0> e() {
        ArrayList arrayList = new ArrayList();
        y.y0 y0Var = new y.y0();
        SurfaceConfig.ConfigType configType = SurfaceConfig.ConfigType.PRIV;
        SurfaceConfig.ConfigSize configSize = SurfaceConfig.ConfigSize.PREVIEW;
        y0Var.a(SurfaceConfig.a(configType, configSize));
        SurfaceConfig.ConfigSize configSize2 = SurfaceConfig.ConfigSize.VGA;
        y0Var.a(SurfaceConfig.a(configType, configSize2));
        SurfaceConfig.ConfigType configType2 = SurfaceConfig.ConfigType.YUV;
        SurfaceConfig.ConfigSize configSize3 = SurfaceConfig.ConfigSize.MAXIMUM;
        y0Var.a(SurfaceConfig.a(configType2, configSize3));
        SurfaceConfig.ConfigType configType3 = SurfaceConfig.ConfigType.RAW;
        y0Var.a(SurfaceConfig.a(configType3, configSize3));
        arrayList.add(y0Var);
        y.y0 y0Var2 = new y.y0();
        y0Var2.a(SurfaceConfig.a(configType, configSize));
        y0Var2.a(SurfaceConfig.a(configType, configSize2));
        y0Var2.a(SurfaceConfig.a(SurfaceConfig.ConfigType.JPEG, configSize3));
        y0Var2.a(SurfaceConfig.a(configType3, configSize3));
        arrayList.add(y0Var2);
        return arrayList;
    }

    public static List<y.y0> f() {
        ArrayList arrayList = new ArrayList();
        y.y0 y0Var = new y.y0();
        SurfaceConfig.ConfigType configType = SurfaceConfig.ConfigType.PRIV;
        SurfaceConfig.ConfigSize configSize = SurfaceConfig.ConfigSize.PREVIEW;
        y0Var.a(SurfaceConfig.a(configType, configSize));
        SurfaceConfig.ConfigSize configSize2 = SurfaceConfig.ConfigSize.RECORD;
        y0Var.a(SurfaceConfig.a(configType, configSize2));
        arrayList.add(y0Var);
        y.y0 y0Var2 = new y.y0();
        y0Var2.a(SurfaceConfig.a(configType, configSize));
        SurfaceConfig.ConfigType configType2 = SurfaceConfig.ConfigType.YUV;
        y0Var2.a(SurfaceConfig.a(configType2, configSize2));
        arrayList.add(y0Var2);
        y.y0 y0Var3 = new y.y0();
        y0Var3.a(SurfaceConfig.a(configType2, configSize));
        y0Var3.a(SurfaceConfig.a(configType2, configSize2));
        arrayList.add(y0Var3);
        y.y0 y0Var4 = new y.y0();
        y0Var4.a(SurfaceConfig.a(configType, configSize));
        y0Var4.a(SurfaceConfig.a(configType, configSize2));
        SurfaceConfig.ConfigType configType3 = SurfaceConfig.ConfigType.JPEG;
        y0Var4.a(SurfaceConfig.a(configType3, configSize2));
        arrayList.add(y0Var4);
        y.y0 y0Var5 = new y.y0();
        y0Var5.a(SurfaceConfig.a(configType, configSize));
        y0Var5.a(SurfaceConfig.a(configType2, configSize2));
        y0Var5.a(SurfaceConfig.a(configType3, configSize2));
        arrayList.add(y0Var5);
        y.y0 y0Var6 = new y.y0();
        y0Var6.a(SurfaceConfig.a(configType2, configSize));
        y0Var6.a(SurfaceConfig.a(configType2, configSize));
        y0Var6.a(SurfaceConfig.a(configType3, SurfaceConfig.ConfigSize.MAXIMUM));
        arrayList.add(y0Var6);
        return arrayList;
    }

    public static List<y.y0> g() {
        ArrayList arrayList = new ArrayList();
        y.y0 y0Var = new y.y0();
        SurfaceConfig.ConfigType configType = SurfaceConfig.ConfigType.RAW;
        SurfaceConfig.ConfigSize configSize = SurfaceConfig.ConfigSize.MAXIMUM;
        y0Var.a(SurfaceConfig.a(configType, configSize));
        arrayList.add(y0Var);
        y.y0 y0Var2 = new y.y0();
        SurfaceConfig.ConfigType configType2 = SurfaceConfig.ConfigType.PRIV;
        SurfaceConfig.ConfigSize configSize2 = SurfaceConfig.ConfigSize.PREVIEW;
        y0Var2.a(SurfaceConfig.a(configType2, configSize2));
        y0Var2.a(SurfaceConfig.a(configType, configSize));
        arrayList.add(y0Var2);
        y.y0 y0Var3 = new y.y0();
        SurfaceConfig.ConfigType configType3 = SurfaceConfig.ConfigType.YUV;
        y0Var3.a(SurfaceConfig.a(configType3, configSize2));
        y0Var3.a(SurfaceConfig.a(configType, configSize));
        arrayList.add(y0Var3);
        y.y0 y0Var4 = new y.y0();
        y0Var4.a(SurfaceConfig.a(configType2, configSize2));
        y0Var4.a(SurfaceConfig.a(configType2, configSize2));
        y0Var4.a(SurfaceConfig.a(configType, configSize));
        arrayList.add(y0Var4);
        y.y0 y0Var5 = new y.y0();
        y0Var5.a(SurfaceConfig.a(configType2, configSize2));
        y0Var5.a(SurfaceConfig.a(configType3, configSize2));
        y0Var5.a(SurfaceConfig.a(configType, configSize));
        arrayList.add(y0Var5);
        y.y0 y0Var6 = new y.y0();
        y0Var6.a(SurfaceConfig.a(configType3, configSize2));
        y0Var6.a(SurfaceConfig.a(configType3, configSize2));
        y0Var6.a(SurfaceConfig.a(configType, configSize));
        arrayList.add(y0Var6);
        y.y0 y0Var7 = new y.y0();
        y0Var7.a(SurfaceConfig.a(configType2, configSize2));
        SurfaceConfig.ConfigType configType4 = SurfaceConfig.ConfigType.JPEG;
        y0Var7.a(SurfaceConfig.a(configType4, configSize));
        y0Var7.a(SurfaceConfig.a(configType, configSize));
        arrayList.add(y0Var7);
        y.y0 y0Var8 = new y.y0();
        y0Var8.a(SurfaceConfig.a(configType3, configSize2));
        y0Var8.a(SurfaceConfig.a(configType4, configSize));
        y0Var8.a(SurfaceConfig.a(configType, configSize));
        arrayList.add(y0Var8);
        return arrayList;
    }
}
