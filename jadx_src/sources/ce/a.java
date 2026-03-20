package ce;

import android.content.DialogInterface;
import android.widget.SeekBar;
import com.zengge.wifi.activity.NewSymphony.CanvasActivity;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final /* synthetic */ class a implements DialogInterface.OnClickListener {

    /* renamed from: a  reason: collision with root package name */
    public final /* synthetic */ CanvasActivity f8468a;

    /* renamed from: b  reason: collision with root package name */
    public final /* synthetic */ SeekBar f8469b;

    public /* synthetic */ a(CanvasActivity canvasActivity, SeekBar seekBar) {
        this.f8468a = canvasActivity;
        this.f8469b = seekBar;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i8) {
        CanvasActivity.f0(this.f8468a, this.f8469b, dialogInterface, i8);
    }
}
