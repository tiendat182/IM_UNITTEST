package com.viettel.fw.gencode;

import com.viettel.fw.common.util.DateUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

/**
 * Created by vtsoft on 4/6/2015.
 */
public class FileSourceWriter implements SourceWriter {
    private boolean overrideFile = false;

    @Override
    public void writeSource(String filePath, Object fileContent) {
        try {
            String strContent = (String) fileContent;
            File file = new File(filePath);
            File folder = new File(file.getParent());
            if (!folder.exists()) {
                folder.mkdirs();
            }

            if (file.exists() && !overrideFile) {
                File bkfile = new File(file.getPath() + ".bk" + DateUtil.date2HHMMssNoSlash(new Date()));
                file.renameTo(bkfile);
            }

            FileOutputStream sfile = new FileOutputStream(file);
            sfile.write(strContent.getBytes());
            sfile.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public boolean isOverrideFile() {
        return overrideFile;
    }

    public void setOverrideFile(boolean overrideFile) {
        this.overrideFile = overrideFile;
    }
}
