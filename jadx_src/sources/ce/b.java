package ce;

import android.content.DialogInterface;
import android.widget.SeekBar;
import com.zengge.wifi.activity.NewSymphony.CanvasActivity;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final /* synthetic */ class b implements DialogInterface.OnClickListener {

    /* renamed from: a  reason: collision with root package name */
    public final /* synthetic */ CanvasActivity f8471a;

    /* renamed from: b  reason: collision with root package name */
    public final /* synthetic */ SeekBar f8472b;

    public /* synthetic */ b(CanvasActivity canvasActivity, SeekBar seekBar) {
        this.f8471a = canvasActivity;
        this.f8472b = seekBar;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i8) {
        CanvasActivity.g0(this.f8471a, this.f8472b, dialogInterface, i8);
    }
}
