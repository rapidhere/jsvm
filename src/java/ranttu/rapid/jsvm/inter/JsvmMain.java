package ranttu.rapid.jsvm.inter;

import org.apache.commons.cli.*;
import ranttu.rapid.jsvm.exp.JSVMBaseException;
import ranttu.rapid.jsvm.jscomp.ast.AbstractSyntaxTree;
import ranttu.rapid.jsvm.jscomp.comp.Compiler;
import ranttu.rapid.jsvm.jscomp.parser.AcornJSParser;

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

    private String sourceFilePath;
    private String packagePath;
    private String outputPath;
    private CommandLine cl;

    private JsvmMain(String[] args) {
        arguments = args;
    }

    private void processArguments()  {
        try {
            cl = parser.parse(options, arguments);
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp( "jsvm", options );
            System.exit(1);
        }

        packagePath = cl.getOptionValue("package");
        sourceFilePath = cl.getArgs()[0];
        outputPath = cl.getOptionValue("o");
    }

    private void run() throws Throwable {
        if (cl.hasOption("c")) {
            compile();
        } else {
            System.err.println("don't know what to do");
            System.exit(1);
        }
    }

    private void compile() throws Throwable {
        try {
            System.out.println("process source: " + sourceFilePath);
            System.out.println("package name: " + packagePath);
            System.out.println("==================================");
            System.out.println();

            ranttu.rapid.jsvm.jscomp.parser.Parser parser = new AcornJSParser();
            File f = new File(sourceFilePath);

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
            .addRequiredOption(null, "package", true, "the package path")
            .addOption("c", "compile", false, "do the compile job")
            .addOption("o", "output", true, "the output directory");
    }
}
