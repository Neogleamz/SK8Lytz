package l2;

import android.media.AudioAttributes;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a {

    /* renamed from: a  reason: collision with root package name */
    private final Vibrator f21562a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public a(Vibrator vibrator) {
        this.f21562a = vibrator;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Vibrator a() {
        return this.f21562a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(long j8, int i8) {
        if (this.f21562a.hasVibrator()) {
            if (Build.VERSION.SDK_INT < 26) {
                this.f21562a.vibrate(j8);
            } else if (this.f21562a.hasAmplitudeControl()) {
                this.f21562a.vibrate(VibrationEffect.createOneShot(j8, i8), new AudioAttributes.Builder().setContentType(4).setUsage(4).build());
            } else {
                this.f21562a.vibrate(VibrationEffect.createOneShot(j8, -1), new AudioAttributes.Builder().setContentType(4).setUsage(4).build());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c(List<Integer> list, int i8) {
        int size = list.size();
        long[] jArr = new long[size];
        for (int i9 = 0; i9 < size; i9++) {
            jArr[i9] = list.get(i9).intValue();
        }
        if (this.f21562a.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= 26) {
                this.f21562a.vibrate(VibrationEffect.createWaveform(jArr, i8), new AudioAttributes.Builder().setContentType(4).setUsage(4).build());
            } else {
                this.f21562a.vibrate(jArr, i8);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d(List<Integer> list, int i8, List<Integer> list2) {
        Vibrator vibrator;
        VibrationEffect createWaveform;
        AudioAttributes.Builder builder;
        int size = list.size();
        long[] jArr = new long[size];
        int size2 = list2.size();
        int[] iArr = new int[size2];
        for (int i9 = 0; i9 < size; i9++) {
            jArr[i9] = list.get(i9).intValue();
        }
        for (int i10 = 0; i10 < size2; i10++) {
            iArr[i10] = list2.get(i10).intValue();
        }
        if (this.f21562a.hasVibrator()) {
            if (Build.VERSION.SDK_INT < 26) {
                this.f21562a.vibrate(jArr, i8);
                return;
            }
            if (this.f21562a.hasAmplitudeControl()) {
                vibrator = this.f21562a;
                createWaveform = VibrationEffect.createWaveform(jArr, iArr, i8);
                builder = new AudioAttributes.Builder();
            } else {
                vibrator = this.f21562a;
                createWaveform = VibrationEffect.createWaveform(jArr, i8);
                builder = new AudioAttributes.Builder();
            }
            vibrator.vibrate(createWaveform, builder.setContentType(4).setUsage(4).build());
        }
    }
}
