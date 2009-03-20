/*******************************************************************************
 * This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Peter Smith
 *******************************************************************************/
package org.boris.pecoff4j;

import java.io.File;

import org.boris.pecoff4j.constant.ImageDataDirectoryType;
import org.boris.pecoff4j.io.PEAssembler;
import org.boris.pecoff4j.io.PEParser;
import org.boris.pecoff4j.util.Diff;
import org.boris.pecoff4j.util.IO;
import org.boris.pecoff4j.util.Reflection;

public class TestPAProblems
{
    static String P1 = "C:\\windows\\system32\\makecab.exe";
    static String P2 = "C:\\windows\\system32\\ds32gt.dll";
    static String P3 = "C:\\windows\\system32\\usrrtosa.dll";
    static String P4 = "C:\\windows\\system32\\xvidcore.dll";
    static String P5 = "C:\\windows\\system32\\narrator.exe";
    static String P6 = "C:\\windows\\system32\\Setup\\msmqocm.dll";
    static String P7 = "C:\\windows\\system32\\esentprf.dll";
    static String P8 = "C:\\windows\\system32\\MRT.exe";
    static String P9 = "C:\\windows\\system32\\dgsetup.dll";
    static String PA = "C:\\windows\\system32\\fde.dll";
    static String PB = "C:\\windows\\system32\\usrrtosa.dll";
    static String PC = "C:\\windows\\system32\\SoftwareDistribution\\Setup\\ServiceStartup\\wups2.dll\\7.2.6001.784\\wups2.dll";

    public static void main(String[] args) throws Exception {
        // test(PC);
        dumpVA(PC);
    }

    public static void test(String s) throws Exception {
        File f = new File(s);
        System.out.println(f);
        byte[] b1 = IO.toBytes(f);
        PE pe = PEParser.parse(f);
        byte[] b2 = PEAssembler.toBytes(pe);
        Diff.findDiff(b1, b2, false);
    }

    public static void dumpVA(String s) throws Exception {
        File f = new File(s);
        System.out.println(f);
        PE pe = PEParser.parse(f);
        SectionTable st = pe.getSectionTable();
        System.out.println("name\tprd \tdex \tvad  \tvex");
        System.out.println("========================================");
        for (int i = 0; i < st.getNumberOfSections(); i++) {
            SectionHeader sh = st.getHeader(i);
            int dex = sh.getPointerToRawData() + sh.getSizeOfRawData();
            int vex = sh.getVirtualAddress() + sh.getVirtualSize();
            System.out.println(sh.getName() + "\t" +
                    make4(Integer.toHexString(sh.getPointerToRawData())) +
                    "\t" + make4(Integer.toHexString(dex)) + "\t" +
                    make4(Integer.toHexString(sh.getVirtualAddress())) + "\t" +
                    make4(Integer.toHexString(vex)));
        }

        System.out.println();
        int dc = pe.getOptionalHeader().getDataDirectoryCount();
        for (int i = 0; i < dc; i++) {
            ImageDataDirectory idd = pe.getOptionalHeader().getDataDirectory(i);
            if (idd.getSize() > 0) {
                String n = Reflection.getConstantName(
                        ImageDataDirectoryType.class, i);
                while (n.length() < 20) {
                    n = n + " ";
                }
                System.out.println(n +
                        "\t" +
                        Integer.toHexString(idd.getVirtualAddress()) +
                        "\t" +
                        Integer.toHexString(idd.getVirtualAddress() +
                                idd.getSize()));
            }
        }
    }

    private static String make4(String s) {
        while (s.length() < 4) {
            s = " " + s;
        }
        return s;
    }
}
