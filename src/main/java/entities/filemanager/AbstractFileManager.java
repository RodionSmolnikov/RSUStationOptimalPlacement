package entities.filemanager;

import entities.interfaces.ParameterContainer;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Rodion on 03.06.2015.
 */
public abstract class AbstractFileManager {

    private File currentDir;

    protected String EXTENTION;

    protected String FACTORY_NAME;

    private Pattern NAME_PATTERN;

    protected int counter = 0;

    private String currentFileName;

    protected AbstractFileManager (String extention, String factoryName) {
        EXTENTION = extention;
        FACTORY_NAME = factoryName;
        NAME_PATTERN = Pattern.compile(FACTORY_NAME + "([\\d]{1,3})\\." + EXTENTION);

        this.currentDir = new File(System.getProperty("user.dir"));
        currentDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                Matcher  matcher = NAME_PATTERN.matcher(name);
                if (matcher.matches()) {
                    counter = counter < Integer.valueOf(matcher.group(1)) ?
                                           Integer.valueOf(matcher.group(1)) :
                            counter;
                    return true;
                }
                return false;
            }
        });
        if (counter == 0) counter++;
    }


    public void saveFile(ParameterContainer container) throws IOException{
        File newFile = new File(currentDir.getPath() + File.separator + getCurrentFileName());
        if (newFile.createNewFile()) {
            FileOutputStream stream = new  FileOutputStream (newFile);
            try {
                writeInputParamsToFile(stream, container);
            } finally {
                stream.close();
            }
        } else {
            throw new IOException("file exists");
        }
    };

    public ParameterContainer loadFile(File file) throws IOException {
        FileInputStream stream = new FileInputStream(file);
        try {
            return getParamsFromFile(stream);
        } finally {
            stream.close();
        }
    }

    public ParameterContainer loadFile() throws IOException {
        File file = new File(getCurrentFileName());
        FileInputStream stream = new FileInputStream(file);
        try {
            return getParamsFromFile(stream);
        } finally {
            stream.close();
        }
    }

    protected abstract void writeInputParamsToFile(FileOutputStream stream, ParameterContainer container) throws IOException;

    protected abstract ParameterContainer getParamsFromFile(FileInputStream stream) throws IOException;

    public String getCurrentFileName() {
        return currentFileName;
    }

    public void setCurrentFileName(String currentFileName) {
        this.currentFileName = currentFileName;
    }

    public String generateDefaultFileNameToSave() {return FACTORY_NAME  + counter++ + "." + EXTENTION;}

    public File getCurrentDir() {
        return currentDir;
    }

    public void setCurrentDir(File currentDir) {
        this.currentDir = currentDir;
    }
}
