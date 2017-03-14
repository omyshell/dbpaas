/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lushell.tc.dbpaas.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tangchao
 */
public class UtilsFile {

    public static List<File> getFiles(File fileDir, String fileType) {
        List<File> lfile = new ArrayList<File>();
        File[] fs = fileDir.listFiles();
        for (File f : fs) {
            if (f.isFile()) {
                if (fileType.equals(
                        f.getName().substring(f.getName().lastIndexOf(".") + 1,
                                f.getName().length()))) {
                    lfile.add(f);
                }
            } else {
                List<File> ftemps = getFiles(f, fileType);
                lfile.addAll(ftemps);
            }
        }
        return lfile;
    }
}
