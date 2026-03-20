package n4;

import b6.l0;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.id3.CommentFrame;
import com.google.android.exoplayer2.metadata.id3.InternalFrame;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class v {

    /* renamed from: c  reason: collision with root package name */
    private static final Pattern f22144c = Pattern.compile("^ [0-9a-fA-F]{8} ([0-9a-fA-F]{8}) ([0-9a-fA-F]{8})");

    /* renamed from: a  reason: collision with root package name */
    public int f22145a = -1;

    /* renamed from: b  reason: collision with root package name */
    public int f22146b = -1;

    private boolean b(String str) {
        Matcher matcher = f22144c.matcher(str);
        if (matcher.find()) {
            try {
                int parseInt = Integer.parseInt((String) l0.j(matcher.group(1)), 16);
                int parseInt2 = Integer.parseInt((String) l0.j(matcher.group(2)), 16);
                if (parseInt > 0 || parseInt2 > 0) {
                    this.f22145a = parseInt;
                    this.f22146b = parseInt2;
                    return true;
                }
                return false;
            } catch (NumberFormatException unused) {
                return false;
            }
        }
        return false;
    }

    public boolean a() {
        return (this.f22145a == -1 || this.f22146b == -1) ? false : true;
    }

    public boolean c(Metadata metadata) {
        for (int i8 = 0; i8 < metadata.e(); i8++) {
            Metadata.Entry d8 = metadata.d(i8);
            if (d8 instanceof CommentFrame) {
                CommentFrame commentFrame = (CommentFrame) d8;
                if ("iTunSMPB".equals(commentFrame.f10103c) && b(commentFrame.f10104d)) {
                    return true;
                }
            } else if (d8 instanceof InternalFrame) {
                InternalFrame internalFrame = (InternalFrame) d8;
                if ("com.apple.iTunes".equals(internalFrame.f10110b) && "iTunSMPB".equals(internalFrame.f10111c) && b(internalFrame.f10112d)) {
                    return true;
                }
            } else {
                continue;
            }
        }
        return false;
    }

    public boolean d(int i8) {
        int i9 = i8 >> 12;
        int i10 = i8 & 4095;
        if (i9 > 0 || i10 > 0) {
            this.f22145a = i9;
            this.f22146b = i10;
            return true;
        }
        return false;
    }
}
