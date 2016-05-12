package com.akbank.investorrelations.objects;

/**
 * Created by oguzemreozcan on 18/04/16.
 */
public class SavedDocumentObject {

    public final static String FOLDER_PATH = "/akbank_files";
    private String fileName;
    private String filePath;

    public SavedDocumentObject(String fileName){
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
