package com.whx.changecode;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.SwitchEntryStmt;
import com.github.javaparser.printer.PrettyPrintVisitor;
import com.github.javaparser.printer.PrettyPrinterConfiguration;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by whx on 2018/3/14.
 */

public class ChangeIf {

    private static final String TAB = "    ";
    private File file;

    public ChangeIf(File file) {
        this.file = file;
    }

    public void changeIf() {
        try {
            CompilationUnit result = JavaParser.parse(file);

            PrettyPrinterConfiguration conf = new PrettyPrinterConfiguration();
            conf.setIndent(TAB);

            PrettyPrintVisitor printer = new PrettyPrintVisitor(conf) {
                @Override
                public void visit(IfStmt n, Void arg) {

                    if (n.getComment().isPresent()) {
                        n.getComment().get().accept(this, arg);
                    }

                    printer.print("if (");
                    n.getCondition().accept(this, arg);
                    printer.println(") {");
                    printer.print(TAB);
                    n.getThenStmt().accept(this, arg);
                    printer.print("\n" + TAB + TAB +"}");

                    if (n.hasCascadingIfStmt()) {
                        printer.print(" else ");

                        if (n.getElseStmt().isPresent()) {
                            n.getElseStmt().get().accept(this, arg);
                        }
                    } else if (n.hasElseBranch()) {
                        printer.println(" else {");
                        if (n.getElseStmt().isPresent()) {
                            printer.print(TAB);
                            n.getElseStmt().get().accept(this, arg);
                        }
                        printer.println("\n" + TAB + TAB +"}");
                    }
                }
            };

            printer.visit(result, null);

            FileWriter fw = new FileWriter(file);
            fw.write(printer.getSource());

            fw.flush();
            fw.close();

            System.out.println(printer.getSource());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String formatSwitch() {
        PrettyPrinterConfiguration conf = new PrettyPrinterConfiguration();
        conf.setIndent("\t");

        PrettyPrintVisitor printer = new PrettyPrintVisitor(conf) {
            @Override
            public void visit(SwitchEntryStmt n, Void arg) {

                if (n.getComment().isPresent()) {
                    n.getComment().get().accept(this, arg);
                }

                if (n.getLabel().isPresent()) {
                    printer.print("case ");
                    n.getLabel().get().accept(this, arg);
                    printer.print(":");
                } else {
                    printer.print("default:");
                }
                printer.print(" ");

                if (n.getStatements() != null) {
                    if (n.getStatements().size() == 2) {
                        // 表示case 后只有一行代码，则紧接着break
                        n.getStatement(0).accept(this, arg);
                        printer.print(" ");
                        n.getStatement(1).accept(this, arg);
                        printer.println();
                    } else {
                        printer.println("{");
                        for (Statement s : n.getStatements()) {
                            printer.print("\t");
                            s.accept(this, arg);
                            printer.println();
                        }
                        printer.println("}");
                    }
                }
            }
        };

        // 执行重新格式化
        try {
            CompilationUnit cu = JavaParser.parse(file);
            printer.visit(cu, null);

            FileWriter fw = new FileWriter(file);
            fw.write(printer.getSource());

            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return printer.getSource();
    }
}
