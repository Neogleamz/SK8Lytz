package d5;

import a5.c;
import a5.e;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.icy.IcyInfo;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a extends e {

    /* renamed from: c  reason: collision with root package name */
    private static final Pattern f19737c = Pattern.compile("(.+?)='(.*?)';", 32);

    /* renamed from: a  reason: collision with root package name */
    private final CharsetDecoder f19738a = com.google.common.base.e.f18817c.newDecoder();

    /* renamed from: b  reason: collision with root package name */
    private final CharsetDecoder f19739b = com.google.common.base.e.f18816b.newDecoder();

    private String c(ByteBuffer byteBuffer) {
        String str;
        CharsetDecoder charsetDecoder;
        try {
            str = this.f19738a.decode(byteBuffer).toString();
            charsetDecoder = this.f19738a;
        } catch (CharacterCodingException unused) {
            this.f19738a.reset();
            byteBuffer.rewind();
            try {
                str = this.f19739b.decode(byteBuffer).toString();
            } catch (CharacterCodingException unused2) {
                str = null;
            } catch (Throwable th) {
                this.f19739b.reset();
                byteBuffer.rewind();
                throw th;
            }
            charsetDecoder = this.f19739b;
        } catch (Throwable th2) {
            this.f19738a.reset();
            byteBuffer.rewind();
            throw th2;
        }
        charsetDecoder.reset();
        byteBuffer.rewind();
        return str;
    }

    @Override // a5.e
    protected Metadata b(c cVar, ByteBuffer byteBuffer) {
        String c9 = c(byteBuffer);
        byte[] bArr = new byte[byteBuffer.limit()];
        byteBuffer.get(bArr);
        String str = null;
        if (c9 == null) {
            return new Metadata(new IcyInfo(bArr, null, null));
        }
        Matcher matcher = f19737c.matcher(c9);
        String str2 = null;
        for (int i8 = 0; matcher.find(i8); i8 = matcher.end()) {
            String group = matcher.group(1);
            String group2 = matcher.group(2);
            if (group != null) {
                String e8 = com.google.common.base.c.e(group);
                e8.hashCode();
                if (e8.equals("streamurl")) {
                    str2 = group2;
                } else if (e8.equals("streamtitle")) {
                    str = group2;
                }
            }
        }
        return new Metadata(new IcyInfo(bArr, str, str2));
    }
}
