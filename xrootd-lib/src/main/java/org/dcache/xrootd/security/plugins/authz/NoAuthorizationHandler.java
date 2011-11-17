package org.dcache.xrootd.security.plugins.authz;

import java.util.Map;
import java.net.InetSocketAddress;

import org.dcache.xrootd.protocol.XrootdProtocol.FilePerm;
import org.dcache.xrootd.security.AuthorizationHandler;

public class NoAuthorizationHandler implements AuthorizationHandler
{
    @Override
    public void check(int requestId,
                      String pathToOpen,
                      Map<String,String> opaque,
                      FilePerm mode,
                      InetSocketAddress endpoint)
    {
    }

    @Override
    public boolean providesPFN()
    {
        return false;
    }

    @Override
    public String getPFN()
    {
        return null;
    }

    @Override
    public String getUser()
    {
        return null;
    }
}
