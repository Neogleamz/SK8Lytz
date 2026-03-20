package l7;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class i {

    /* renamed from: a  reason: collision with root package name */
    private long f21801a;

    /* renamed from: b  reason: collision with root package name */
    private long f21802b;

    /* renamed from: c  reason: collision with root package name */
    private TimeInterpolator f21803c;

    /* renamed from: d  reason: collision with root package name */
    private int f21804d;

    /* renamed from: e  reason: collision with root package name */
    private int f21805e;

    public i(long j8, long j9) {
        this.f21801a = 0L;
        this.f21802b = 300L;
        this.f21803c = null;
        this.f21804d = 0;
        this.f21805e = 1;
        this.f21801a = j8;
        this.f21802b = j9;
    }

    public i(long j8, long j9, TimeInterpolator timeInterpolator) {
        this.f21801a = 0L;
        this.f21802b = 300L;
        this.f21803c = null;
        this.f21804d = 0;
        this.f21805e = 1;
        this.f21801a = j8;
        this.f21802b = j9;
        this.f21803c = timeInterpolator;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static i b(ValueAnimator valueAnimator) {
        i iVar = new i(valueAnimator.getStartDelay(), valueAnimator.getDuration(), f(valueAnimator));
        iVar.f21804d = valueAnimator.getRepeatCount();
        iVar.f21805e = valueAnimator.getRepeatMode();
        return iVar;
    }

    private static TimeInterpolator f(ValueAnimator valueAnimator) {
        TimeInterpolator interpolator = valueAnimator.getInterpolator();
        return ((interpolator instanceof AccelerateDecelerateInterpolator) || interpolator == null) ? a.f21787b : interpolator instanceof AccelerateInterpolator ? a.f21788c : interpolator instanceof DecelerateInterpolator ? a.f21789d : interpolator;
    }

    public void a(Animator animator) {
        animator.setStartDelay(c());
        animator.setDuration(d());
        animator.setInterpolator(e());
        if (animator instanceof ValueAnimator) {
            ValueAnimator valueAnimator = (ValueAnimator) animator;
            valueAnimator.setRepeatCount(g());
            valueAnimator.setRepeatMode(h());
        }
    }

    public long c() {
        return this.f21801a;
    }

    public long d() {
        return this.f21802b;
    }

    public TimeInterpolator e() {
        TimeInterpolator timeInterpolator = this.f21803c;
        return timeInterpolator != null ? timeInterpolator : a.f21787b;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof i) {
            i iVar = (i) obj;
            if (c() == iVar.c() && d() == iVar.d() && g() == iVar.g() && h() == iVar.h()) {
                return e().getClass().equals(iVar.e().getClass());
            }
            return false;
        }
        return false;
    }

    public int g() {
        return this.f21804d;
    }

    public int h() {
        return this.f21805e;
    }

    public int hashCode() {
        return (((((((((int) (c() ^ (c() >>> 32))) * 31) + ((int) (d() ^ (d() >>> 32)))) * 31) + e().getClass().hashCode()) * 31) + g()) * 31) + h();
    }

    public String toString() {
        return '\n' + getClass().getName() + '{' + Integer.toHexString(System.identityHashCode(this)) + " delay: " + c() + " duration: " + d() + " interpolator: " + e().getClass() + " repeatCount: " + g() + " repeatMode: " + h() + "}\n";
    }
}
