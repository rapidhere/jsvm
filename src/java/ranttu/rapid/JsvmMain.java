package ranttu.rapid;

import org.apache.commons.cli.*;
import ranttu.rapid.jsvm.common.SystemProperty;
import ranttu.rapid.jsvm.exp.JSVMBaseException;
import ranttu.rapid.jsvm.jscomp.ast.AbstractSyntaxTree;
import ranttu.rapid.jsvm.jscomp.comp.Compiler;
import ranttu.rapid.jsvm.jscomp.parser.AcornJSParser;
import ranttu.rapid.jsvmnode.MainLoop;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

/**
 * the main entry
 * @author rapidhere@gmail.com
 * @version $id: JsvmMain.java, v0.1 2016/12/8 dongwei.dq Exp $
 */
public class JsvmMain {
    public static void main(String[] args) throws Throwable {
        JsvmMain main = new JsvmMain(args);
        main.processArguments();
        main.run();
    }

    // impl
    private String[] arguments;
    private static final Options options;
    private static final CommandLineParser parser = new DefaultParser();

    private String source;
    private CommandLine cl;

    private JsvmMain(String[] args) {
        arguments = args;
    }

    private void processArguments()  {
        try {
            cl = parser.parse(options, arguments);
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("jsvm", options);
            e.printStackTrace();
            System.exit(1);
        }


        source = cl.getArgs()[0];
    }

    private void run() throws Throwable {
        if (cl.hasOption("c")) {
            compile();
        } else {
            // run
            runNode();
        }
    }

    private void runNode() {
        SystemProperty.UseVAC = Boolean.valueOf(
            cl.getOptionValue("vac", "true"));
        SystemProperty.UseOptimisticCallSite = Boolean.valueOf(
            cl.getOptionValue("opcs", "true"));

        MainLoop.get().start(source);
    }

    private void compile() throws Throwable {
        try {
            String outputPath = cl.getOptionValue("o");
            String packagePath = cl.getOptionValue("package");

            System.out.println("process source: " + source);
            System.out.println("package name: " + packagePath);
            System.out.println("==================================");
            System.out.println();

            ranttu.rapid.jsvm.jscomp.parser.Parser parser = new AcornJSParser();
            File f = new File(source);

            System.out.println("parsing ...");
            AbstractSyntaxTree ast = parser.parse(f);

            System.out.println("compiling ...");
            Compiler compiler = new Compiler(ast);
            Map<String, byte[]> result = compiler.compileWithPackage(f.getName(), packagePath);

            System.out.println("writing class:");
            for (String className: result.keySet()) {
                int i = className.lastIndexOf("/");
                String fileName = className.substring(i + 1) + ".class";
                String filePath = outputPath + "/" + fileName;
                System.out.println("  " + filePath);
                new FileOutputStream(new File(filePath)).write(result.get(className));
            }
            System.out.println("done");
        } catch (JSVMBaseException e) {
            System.err.println("failed to compile source: " + e.getMessage());
            System.exit(1);
        }
    }

    static {
        options = new Options()
            // compile options
            .addOption(null, "package", true,
                "compile option: the package path, use with -c")
            .addOption("c", "compile", false,
                "do the compile job")
            .addOption("o", "output", true,
                "compile option: the output directory, use with -c")

            // run options
            .addOption(null, "vac", true,
                "use VM Anonymous Class or not, default is true")
            .addOption(null, "opcs", true,
                "use Optimistic Call Site or not, default is true");
    }
}
