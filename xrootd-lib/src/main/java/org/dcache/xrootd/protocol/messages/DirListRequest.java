package org.dcache.xrootd.protocol.messages;

import static org.dcache.xrootd.protocol.XrootdProtocol.*;

import org.jboss.netty.buffer.ChannelBuffer;

public class DirListRequest extends AbstractRequestMessage {
    private final String path;
    private final String opaque;

    public DirListRequest(ChannelBuffer buffer) {
        super(buffer);

        if (getRequestID() != kXR_dirlist) {
            throw new IllegalArgumentException("doesn't seem to be a kXR_dirlist message");
        }

        int dlen = buffer.getInt(20);
        int end = 24 + dlen;
        int pos = buffer.indexOf(24, end, (byte)0x3f);
        if (pos > -1) {
            path = buffer.toString(24,
                                   pos - 24,
                                   XROOTD_CHARSET);
            opaque = buffer.toString(pos + 1,
                                     end - (pos + 1),
                                     XROOTD_CHARSET);
        } else {
            path = buffer.toString(24,
                                   end - 24,
                                   XROOTD_CHARSET);
            opaque = null;
        }
    }

    public String getPath()
    {
        return path;
    }

    public String getOpaque() {
        return opaque;
    }
}
