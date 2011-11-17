package org.dcache.xrootd.protocol.messages;

import static org.dcache.xrootd.protocol.XrootdProtocol.*;

import java.nio.channels.ScatteringByteChannel;
import java.io.IOException;

import org.dcache.xrootd.protocol.messages.GenericReadRequestMessage.EmbeddedReadRequest;

public class ReadResponse extends AbstractResponseMessage
{
    public final static int READ_LIST_HEADER_SIZE = 16;

    public ReadResponse(int sId, int length)
    {
        super(sId, kXR_ok, length);
    }

    /**
     * Set the status field to indicate whether the response is
     * complete or not.
     */
    public void setIncomplete(boolean incomplete)
    {
        setStatus(incomplete ? kXR_oksofar : kXR_ok);
    }

    /**
     * Reads bytes from a channel into the response buffer.
     */
    public int writeBytes(ScatteringByteChannel in, int length)
        throws IOException
    {
        return _buffer.writeBytes(in, length);
    }

    /**
     * Reads bytes from a channel into the response buffer.
     */
    public int writeBytes(EmbeddedReadRequest req)
    {
        putSignedInt(req.getFileHandle());
        putSignedInt(req.BytesToRead());
        putSignedLong(req.getOffset());
        return 16;
    }

    /**
     * Returns the size of the payload. Only accurate as the long as
     * we have not begun to send the buffer.
     */
    public int getDataLength()
    {
        return _buffer.readableBytes() - SERVER_RESPONSE_LEN;
    }

    @Override
    public String toString()
    {
        return String.format("read-response[length=%d]", getDataLength());
    }
}
