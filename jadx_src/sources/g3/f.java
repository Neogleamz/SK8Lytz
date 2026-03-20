package g3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.example.photo_location.models.ImgProcessor;
import com.example.photo_location.models.LoadConfig;
import com.example.photo_location.models.MaGanModel;
import com.example.photo_location.models.ResultBox;
import com.example.photo_location.models.TFLiteModel;
import io.flutter.plugin.common.i;
import io.flutter.plugin.common.j;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import yf.a;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class f implements yf.a, j.c {

    /* renamed from: a  reason: collision with root package name */
    private j f20189a;

    /* renamed from: b  reason: collision with root package name */
    private MaGanModel f20190b;

    /* renamed from: c  reason: collision with root package name */
    private Context f20191c;

    /* renamed from: d  reason: collision with root package name */
    private int f20192d;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements MaGanModel.ProgressListening {
        a() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void b(int i8) {
            f.this.f20189a.c("sendProgress", Integer.valueOf(i8));
        }

        @Override // com.example.photo_location.models.MaGanModel.ProgressListening
        public void onSendProgress(int i8) {
            Log.i("TAG", "onSendProgress: " + i8);
            new Handler(Looper.getMainLooper()).post(new e(this, i8));
        }
    }

    private List<List<Integer>> g(int i8) {
        Context context;
        String str;
        int ceil = (int) Math.ceil(Math.log(i8) / Math.log(3.0d));
        Log.i("TAG", "需要图片数: " + ceil);
        switch (ceil) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                context = this.f20191c;
                str = "small.json";
                break;
            case 6:
                context = this.f20191c;
                str = "medium.json";
                break;
            default:
                context = this.f20191c;
                str = "large.json";
                break;
        }
        int[][] readFile = LoadConfig.readFile(context, str);
        ArrayList arrayList = new ArrayList();
        for (int[] iArr : readFile) {
            ArrayList arrayList2 = new ArrayList();
            for (int i9 = 0; i9 < Math.min(iArr.length, i8); i9++) {
                arrayList2.add(Integer.valueOf(iArr[i9]));
            }
            arrayList.add(arrayList2);
        }
        Log.i("TAG", "getIdentifyPointColor: " + arrayList.size() + ", " + ((List) arrayList.get(0)).size());
        return arrayList;
    }

    private List<Map<String, Object>> h(List<Bitmap> list, String str, String str2, long j8, int i8) {
        this.f20190b.startListeningPropress(new a());
        MaGanModel.BitmapResult predict2 = this.f20190b.predict2(list, this.f20192d, str, str2, j8, i8);
        if (predict2 == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        int i9 = 0;
        while (true) {
            ResultBox resultBox = predict2.resultBox;
            int[] iArr = resultBox.address;
            if (i9 >= iArr.length) {
                break;
            }
            if (i9 < iArr.length) {
                float[][] fArr = resultBox.boxes;
                if (i9 < fArr.length) {
                    boolean[] zArr = resultBox.fake;
                    if (i9 < zArr.length) {
                        int i10 = iArr[i9];
                        boolean z4 = zArr[i9];
                        float[] fArr2 = fArr[i9];
                        HashMap hashMap = new HashMap();
                        hashMap.put("address", Integer.valueOf(i10));
                        hashMap.put("fake", Boolean.valueOf(z4));
                        hashMap.put("boxes", fArr2);
                        arrayList.add(hashMap);
                    }
                }
            }
            i9++;
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void j(List list, String str, String str2, long j8, int i8, j.d dVar) {
        List<Map<String, Object>> h8 = h(list, str, str2, j8, i8);
        if (h8 == null) {
            dVar.a("INVALID_TOKEN", "The token is wrong", (Object) null);
        } else {
            new Handler(Looper.getMainLooper()).post(new d(dVar, h8));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void l(List list, String str, String str2, long j8, int i8, j.d dVar) {
        List<Map<String, Object>> h8 = h(list, str, str2, j8, i8);
        if (h8 == null) {
            dVar.a("INVALID_TOKEN", "The token is wrong", (Object) null);
        } else {
            new Handler(Looper.getMainLooper()).post(new c(dVar, h8));
        }
    }

    private boolean m(Context context) {
        TFLiteModel tFLiteModel = new TFLiteModel(context, "yolov3_2.tflite", false);
        ImgProcessor imgProcessor = new ImgProcessor();
        imgProcessor.initConfig(LoadConfig.small(context), LoadConfig.medium(context), LoadConfig.large(context));
        this.f20190b = new MaGanModel(tFLiteModel, imgProcessor);
        return true;
    }

    List<Bitmap> f(List<byte[]> list) {
        ArrayList arrayList = new ArrayList();
        for (byte[] bArr : list) {
            Bitmap decodeByteArray = BitmapFactory.decodeByteArray(bArr, 0, bArr.length);
            if (decodeByteArray != null) {
                arrayList.add(decodeByteArray);
            }
        }
        return arrayList;
    }

    public void onAttachedToEngine(a.b bVar) {
        j jVar = new j(bVar.b(), "photo_location");
        this.f20189a = jVar;
        jVar.e(this);
        Context a9 = bVar.a();
        this.f20191c = a9;
        m(a9);
    }

    public void onDetachedFromEngine(a.b bVar) {
        this.f20189a.e((j.c) null);
    }

    public void onMethodCall(i iVar, j.d dVar) {
        String str;
        String str2;
        Thread thread;
        Object g8;
        Log.i("photoLocation", "onMethodCall: " + iVar.a);
        if (iVar.a.equals("getPlatformVersion")) {
            g8 = "Android " + Build.VERSION.RELEASE;
        } else if (!iVar.a.equals("loadModel")) {
            if (!iVar.a.equals("getIdentifyPointColor")) {
                if (iVar.a.equals("getLedDetect")) {
                    if (this.f20190b == null) {
                        m(this.f20191c);
                    }
                    if (this.f20190b == null) {
                        dVar.a("INVALID_MODEL", "The model cannot be loaded.", (Object) null);
                        return;
                    }
                    ArrayList arrayList = new ArrayList();
                    for (String str3 : (List) iVar.a("imagePaths")) {
                        if (new File(str3).exists()) {
                            Bitmap decodeFile = BitmapFactory.decodeFile(str3);
                            if (decodeFile == null) {
                                dVar.a("INVALID_IMAGE", "Image path cannot be null or empty.", (Object) null);
                                return;
                            }
                            arrayList.add(decodeFile);
                        } else {
                            try {
                                Bitmap decodeStream = BitmapFactory.decodeStream(this.f20191c.getAssets().open(str3));
                                if (decodeStream == null) {
                                    dVar.a("INVALID_IMAGE", "Image path cannot be null or empty.", (Object) null);
                                    return;
                                }
                                arrayList.add(decodeStream);
                            } catch (IOException unused) {
                                dVar.a("INVALID_IMAGE", "Image path cannot be null or empty.", (Object) null);
                                return;
                            }
                        }
                    }
                    thread = new Thread((Runnable) new g3.a(this, arrayList, iVar.a, (String) iVar.a("token"), ((Long) iVar.a("timestampMilliseconds")).longValue(), ((Integer) iVar.a("iv")).intValue(), dVar));
                } else if (iVar.a.equals("getLedDetectWithData")) {
                    if (this.f20190b == null) {
                        m(this.f20191c);
                    }
                    if (this.f20190b == null) {
                        dVar.a("INVALID_MODEL", "The model cannot be loaded.", (Object) null);
                        return;
                    }
                    String str4 = iVar.a;
                    String str5 = (String) iVar.a("token");
                    long longValue = ((Long) iVar.a("timestampMilliseconds")).longValue();
                    int intValue = ((Integer) iVar.a("iv")).intValue();
                    List<byte[]> list = (List) iVar.a("imageDataList");
                    Log.i("TAG", "onMethodCall: imageDataList count: " + list.size());
                    thread = new Thread((Runnable) new b(this, f(list), str4, str5, longValue, intValue, dVar));
                } else if (!iVar.a.equals("merge")) {
                    dVar.b();
                    return;
                } else if (this.f20190b == null) {
                    dVar.a("INVALID_MODEL", "The model cannot be loaded.", (Object) null);
                    return;
                } else {
                    ResultBox merge = this.f20190b.merge(ResultBox.getResultBox(iVar.a("front")), ResultBox.getResultBox(iVar.a("rear")), this.f20192d);
                    if (merge == null) {
                        str = "INVALID_TOKEN";
                        str2 = "The token is wrong";
                    } else {
                        ArrayList arrayList2 = new ArrayList();
                        int i8 = 0;
                        while (true) {
                            int[] iArr = merge.address;
                            if (i8 >= iArr.length) {
                                dVar.success(arrayList2);
                                return;
                            }
                            if (i8 < iArr.length) {
                                float[][] fArr = merge.boxes;
                                if (i8 < fArr.length) {
                                    boolean[] zArr = merge.fake;
                                    if (i8 < zArr.length) {
                                        int i9 = iArr[i8];
                                        boolean z4 = zArr[i8];
                                        float[] fArr2 = fArr[i8];
                                        HashMap hashMap = new HashMap();
                                        hashMap.put("address", Integer.valueOf(i9));
                                        hashMap.put("fake", Boolean.valueOf(z4));
                                        hashMap.put("boxes", fArr2);
                                        arrayList2.add(hashMap);
                                    }
                                }
                            }
                            i8++;
                        }
                    }
                }
                thread.start();
                return;
            }
            int intValue2 = ((Integer) iVar.a("pointNum")).intValue();
            Log.i("TAG", "pointNum: " + intValue2);
            if (intValue2 == 0) {
                Log.e("pointNum", "argument: " + iVar.b.toString());
                str = "INVALID_POINTS";
                str2 = "The point num cannot be Zero.";
            } else {
                this.f20192d = intValue2;
                g8 = g(intValue2);
            }
            dVar.a(str, str2, (Object) null);
            return;
        } else {
            m(this.f20191c);
            g8 = "success";
        }
        dVar.success(g8);
    }
}
