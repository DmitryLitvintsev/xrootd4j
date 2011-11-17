package org.dcache.xrootd.protocol.messages;

import java.util.zip.Adler32;

import static org.dcache.xrootd.protocol.XrootdProtocol.*;
import org.jboss.netty.buffer.ChannelBuffer;

public class OpenRequest extends AbstractRequestMessage
{
    private final int mode;
    private final int options;
    private final String path;
    private final String opaque;
    private final long checksum;

    public OpenRequest(ChannelBuffer buffer)
    {
        super(buffer);

        if (getRequestID() != kXR_open)
            throw new IllegalArgumentException("doesn't seem to be a kXR_open message");

        mode = buffer.getUnsignedShort(4);
        options = buffer.getUnsignedShort(6);

        // look for '?' character, indicating beginning of optional
        // opaque information (see xrootd-protocol spec.)
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

        byte[] a = new byte[8 + dlen];
        buffer.getBytes(4, a, 0, 4); // mode field + options
        buffer.getBytes(20, a, 4, 4 + dlen); // dlen + data

        Adler32 adler32 = new Adler32();
        adler32.update(a);
        checksum = adler32.getValue();
    }

    public int getUMask()
    {
        return mode;
    }

    public int getOptions()
    {
        return options;
    }

    public String getPath()
    {
        return path;
    }

    public String getOpaque()
    {
        return opaque;
    }

    public boolean isAsync() {
        return (getOptions() & kXR_async) == kXR_async;
    }

    public boolean isCompress() {
        return (getOptions() & kXR_compress) == kXR_compress;
    }

    public boolean isDelete() {
        return (getOptions() & kXR_delete) == kXR_delete;
    }

    public boolean isForce() {
        return (getOptions() & kXR_force) == kXR_force;
    }

    public boolean isNew() {
        return (getOptions() & kXR_new) == kXR_new;
    }

    public boolean isReadOnly() {
        return (getOptions() & kXR_open_read) == kXR_open_read;
    }

    public boolean isReadWrite() {
        return (getOptions() & kXR_open_updt) == kXR_open_updt;
    }

    public boolean isRefresh() {
        return (getOptions() & kXR_refresh) == kXR_refresh;
    }

    public boolean isRetStat() {
        return (getOptions() & kXR_retstat) == kXR_retstat;
    }

    public boolean isMkPath() {
        return (getOptions() & kXR_mkpath) == kXR_mkpath;
    }

    /**
     * Calculates the Adler32 checksum over the binary content of the
     * OpenRequest.  The checksum covers the following fields: mode,
     * options, plen, path (with opaque included) The StreamID is NOT
     * considered.
     * @return the Adler32-bit checksum
     */
    public long calcChecksum()
    {
        return checksum;
    }

    @Override
    public String toString()
    {
        return String.format("open[%d,%d,%s,%s]", mode, options, path, opaque);
    }
}
