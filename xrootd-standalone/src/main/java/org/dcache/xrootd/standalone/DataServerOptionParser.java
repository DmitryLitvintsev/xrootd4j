package org.dcache.xrootd.standalone;

import java.io.File;
import static java.util.Arrays.asList;

import joptsimple.OptionParser;
import joptsimple.OptionSpec;

public class DataServerOptionParser extends OptionParser
{
    public final OptionSpec<Integer> port;
    public final OptionSpec<Void> help;
    public final OptionSpec<File> root;
    public final OptionSpec<String> authnPlugin;
    public final OptionSpec<String> authzPlugin;
    public final OptionSpec<File> pluginPath;

    {
        port = acceptsAll(asList("p", "port"))
            .withRequiredArg()
            .describedAs("TCP port")
            .ofType(Integer.class)
            .defaultsTo(1094);
        help = acceptsAll(asList("h", "?", "help"), "show help");
        root = acceptsAll(asList("r", "root"), "root directory")
            .withRequiredArg()
            .describedAs("path")
            .ofType(File.class)
            .defaultsTo(new File("/tmp"));
        authnPlugin = acceptsAll(asList("authn"), "authentication plugin")
            .withRequiredArg()
            .describedAs("plugin")
            .ofType(String.class)
            .defaultsTo("none");
        authzPlugin = acceptsAll(asList("authz"), "authorization plugin")
            .withRequiredArg()
            .describedAs("plugin")
            .ofType(String.class)
            .defaultsTo("none");
        pluginPath = acceptsAll(asList("plugins"), "search path for plugins")
            .withRequiredArg()
            .withValuesSeparatedBy(File.pathSeparatorChar)
            .describedAs("url")
            .ofType(File.class);
    }
}
