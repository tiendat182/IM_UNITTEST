package com.viettel.fw.gencode;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.viettel.fw.common.util.DataUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class GenCodeForm extends JDialog implements SourceWriter {

    public enum GenerateCodeType {
        BUSINESS,
        WS_BUSINESS_ADVANCE,
        WS_ENDPOIN,
        WS_ERROR,
        WS_ESB_ENDPOINT
    }

    public enum SrcFolderType {
        BASE,
        SRV_IMPL,
        WS_IMPL
    }

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField businessProjFolderTxt;
    private JButton businessBtn;
    private JTextField webProjFolderTxt;
    private JButton webBtn;
    private JTextField businessNameTxt;
    private JCheckBox respositoryCheckBox;
    private JCheckBox serviceCheckBox;
    private JCheckBox wsServiceCheckBox;
    private JCheckBox controllerCheckBox;
    private JCheckBox jsfCheckBox;
    private JCheckBox createFileCheckBox;
    private JTable modelTbl;
    private JPanel GenerateComponentPanel;
    private JPanel ProjFolderPanel;
    private JPanel FooterPanel;
    private JPanel GenerateOptionPanel;
    private JCheckBox dtoCheckBox;
    private JCheckBox mapperCheckBox;
    private JCheckBox qObjectCheckBox;
    private JButton refreshModelBtn;
    private JTabbedPane tabbedPanel;
    private JTextArea outputTxt;
    private JTextField wsBusinessProjFolderTxt;
    private JButton wsBusinessBtn;
    private JTextField classesFolderTxt;
    private JButton classesBtn;
    private JButton clearButton;
    private JButton refreshInterfaceBtn;
    private JTable interfaceTable;
    private JButton generateWsServiceButton;
    private JTextField projectNameTxt;
    private JCheckBox overrideFileNotBackupCheckBox;
    private JButton savePreferencesButton;
    private JButton removeAllBackupButton;
    private JTextField basePathTxt;
    private JButton registerWsEndpointButton;
    private JCheckBox allModelCheckBox;
    private JCheckBox allInterfaceCheckBox;
    private JCheckBox generateImplInBusinessCheckBox;
    private JButton scanWsErrorButton;
    private JButton genEsbEndpointButton;
    private JTextField txtSearch;
    private JTextField txtSearchService;

    private JFileChooser businessFc;
    private JFileChooser wsBusinessFc;
    private JFileChooser webFc;
    private JFileChooser classesFc;
    private ModelFileTable modelFileTable;
    private ModelFileTable interfaceFileTable;

    public GenCodeForm() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        createUIComponents();

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generateCode(GenerateCodeType.BUSINESS);
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


        webBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (webFc.showOpenDialog(getParent()) == JFileChooser.APPROVE_OPTION) {
                    webProjFolderTxt.setText(webFc.getSelectedFile().getAbsolutePath());
                    if (Strings.isNullOrEmpty(classesFolderTxt.getText()) && !Strings.isNullOrEmpty(webProjFolderTxt.getText())) {
                        classesFolderTxt.setText(webProjFolderTxt.getText());
                    }
                }
            }
        });

        businessBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Strings.isNullOrEmpty(businessNameTxt.getText()) && !Strings.isNullOrEmpty(webProjFolderTxt.getText())) {
                    businessFc.setCurrentDirectory(getSuggetDirectory(webProjFolderTxt.getText()));
                }

                if (businessFc.showOpenDialog(getParent()) == JFileChooser.APPROVE_OPTION) {
                    businessProjFolderTxt.setText(businessFc.getSelectedFile().getAbsolutePath());
                }
            }

        });

        wsBusinessBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Strings.isNullOrEmpty(wsBusinessProjFolderTxt.getText()) && !Strings.isNullOrEmpty(webProjFolderTxt.getText())) {
                    wsBusinessFc.setCurrentDirectory(getSuggetDirectory(webProjFolderTxt.getText()));
                }

                if (wsBusinessFc.showOpenDialog(getParent()) == JFileChooser.APPROVE_OPTION) {
                    wsBusinessProjFolderTxt.setText(wsBusinessFc.getSelectedFile().getAbsolutePath());
                }
            }
        });

        refreshModelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reloadModelTable();
            }
        });

        classesBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (classesFc.showOpenDialog(getParent()) == JFileChooser.APPROVE_OPTION) {
                    classesFolderTxt.setText(classesFc.getSelectedFile().getAbsolutePath());
                }
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outputTxt.setText(null
                );
            }
        });
        generateWsServiceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateCode(GenerateCodeType.WS_BUSINESS_ADVANCE);
            }
        });
        refreshInterfaceBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reloadInterfaceTable();
            }
        });
        savePreferencesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    List<String> preferences = Lists.newArrayList();
                    preferences.add("web.folder=" + webProjFolderTxt.getText());
                    preferences.add("business.folder=" + businessProjFolderTxt.getText());
                    preferences.add("wsbusiness.folder=" + wsBusinessProjFolderTxt.getText());
                    preferences.add("classes.folder=" + classesFolderTxt.getText());
                    preferences.add("proj.name=" + projectNameTxt.getText());
                    preferences.add("base.path=" + basePathTxt.getText());
                    writeSmallTextFile(preferences, "D:\\config.txt");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(getParent(), ExceptionUtils.getStackTrace(ex));
                }
            }
        });

        removeAllBackupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onRemoveAllBackupButtonClick(businessProjFolderTxt.getText());
                onRemoveAllBackupButtonClick(wsBusinessProjFolderTxt.getText());
            }
        });

        registerWsEndpointButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                generateCode(GenerateCodeType.WS_ENDPOIN);
            }
        });

        allModelCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modelFileTable.changeSelectAll(allModelCheckBox.isSelected());
            }
        });
        allInterfaceCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                interfaceFileTable.changeSelectAll(allInterfaceCheckBox.isSelected());
            }
        });
        scanWsErrorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateCode(GenerateCodeType.WS_ERROR);
            }
        });
        genEsbEndpointButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateCode(GenerateCodeType.WS_ESB_ENDPOINT);
            }
        });
    }

    public void onRemoveAllBackupButtonClick(String projFolder) {
        Iterator<File> files = FileUtils.iterateFilesAndDirs(new File(projFolder), new IOFileFilter() {
            @Override
            public boolean accept(File file) {
                return true;
            }

            @Override
            public boolean accept(File file, String s) {
                return true;
            }
        }, new IOFileFilter() {
            @Override
            public boolean accept(File file) {
                return true;
            }

            @Override
            public boolean accept(File file, String s) {
                return true;
            }
        });

        while (files.hasNext()) {
            File file = files.next();
            if (file.isDirectory() || !file.getName().contains(".bk")) continue;
            file.delete();
        }
    }

    private File getSuggetDirectory(String path) {
        File file = new File(path);
        if (file.exists()) return file.getParentFile();
        return new File(GenCodeForm.class.getProtectionDomain().getCodeSource().getLocation().getPath());
    }

    private void reloadModelTable() {
        modelFileTable.removeAll();
        interfaceFileTable.removeAll();

        File modelFolder = getSrcFolder("model", SrcFolderType.BASE);
        if (modelFolder == null) return;

        if (modelFolder.isDirectory()) {
            for (File xfile : modelFolder.listFiles(new FilenameFilter() {

                @Override
                public boolean accept(File dir, String name) {
                    if (name.endsWith(".java") && !name.startsWith("Q")) {
                        if (!DataUtil.isNullOrEmpty(txtSearch.getText())) {
                            if (name.contains(txtSearch.getText().trim())) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                        return true;
                    } else {
                        return false;
                    }
                }
            })) {
                modelFileTable.addRow(new Object[]{xfile.getParent(), xfile.getName().replace(".java", ""), new Boolean(true)});
            }
        }
    }


    private void reloadInterfaceTable() {
        interfaceFileTable.removeAll();
        File serviceFolder = getSrcFolder("service", SrcFolderType.BASE);
        if (serviceFolder == null) return;

        if (serviceFolder.isDirectory()) {
            for (File xfile : serviceFolder.listFiles(new FilenameFilter() {

                @Override
                public boolean accept(File dir, String name) {
                    if (name.endsWith(".java")) {
                        if (!DataUtil.isNullOrEmpty(txtSearchService.getText())) {
                            if (name.contains(txtSearchService.getText().trim())) {
                                return true;
                            } else {
                                return false;
                            }
                        }

                        return true;
                    } else {
                        return false;
                    }
                }
            })) {
                interfaceFileTable.addRow(new Object[]{xfile.getParent(), xfile.getName().replace(".java", ""), new Boolean(true)});
            }
        }
    }

    private File getSrcFolder(String packageName, SrcFolderType srcFolderType) {
        String projName = projectNameTxt.getText();
        String basePath = basePathTxt.getText();
        if (Strings.isNullOrEmpty(projName)
                || Strings.isNullOrEmpty(basePath)
                || Strings.isNullOrEmpty(businessProjFolderTxt.getText())) {
            JOptionPane.showMessageDialog(getParent(), "Bat buoc nhap Business proj folder, Project name va Base path");
            return null;
        }

        String businessProjFolder = businessProjFolderTxt.getText() + "\\"
                + (srcFolderType == SrcFolderType.BASE ? "base" : ((srcFolderType == SrcFolderType.SRV_IMPL) ? "srv-impl" : "ws-impl"));

        if (!basePath.endsWith("\\."))
            basePath = basePath + ".";

        basePath = basePath.replaceAll("\\.", "\\\\");
        basePath = basePath + projName.toLowerCase().trim() + "\\" + packageName;

        File folder = new File(businessProjFolder + "\\" + basePath);
        if (!folder.exists()) {
            folder = new File(businessProjFolder + "\\java\\" + basePath);
            if (!folder.exists()) {
                folder = new File(businessProjFolder + "\\src\\" + basePath);
            }
        }

        if (!folder.exists()) {
            JOptionPane.showMessageDialog(getParent(),
                    MessageFormat.format("Khong tim thay thu muc source:\n{0} \nHoac: {1}\nHoac:{2}",
                            businessProjFolder + "\\" + basePath,
                            businessProjFolder + "\\java\\" + basePath,
                            businessProjFolder + "\\src\\" + basePath
                    ));
        }

        return folder;
    }


    private void generateCode(GenerateCodeType codeType) {
        try {
            this.setEnabled(false);
            String prjName = projectNameTxt.getText();
            if (Strings.isNullOrEmpty(prjName)) {
                JOptionPane.showMessageDialog(getParent(), "Please input project name", "Error", JOptionPane.ERROR_MESSAGE);
                projectNameTxt.requestFocus();
                return;
            }

            prjName = prjName.toLowerCase().trim();
            String basePath = basePathTxt.getText();

            if (Strings.isNullOrEmpty(businessProjFolderTxt.getText())) {
                JOptionPane.showMessageDialog(getParent(), "Please input business project folder", "Error", JOptionPane.ERROR_MESSAGE);
                businessProjFolderTxt.requestFocus();
                return;
            }

            if (Strings.isNullOrEmpty(wsBusinessProjFolderTxt.getText())) {
                JOptionPane.showMessageDialog(getParent(), "Please input WS business project folder", "Error", JOptionPane.ERROR_MESSAGE);
                wsBusinessProjFolderTxt.requestFocus();
                return;
            }

            if (Strings.isNullOrEmpty(basePath)) {
                JOptionPane.showMessageDialog(getParent(), "Please input base path project folder", "Error", JOptionPane.ERROR_MESSAGE);
                basePathTxt.requestFocus();
                return;
            }
            if (!basePath.endsWith(".")) basePath = basePath + ".";


            GenerateCodeHelper generateCodeHelper = new GenerateCodeHelper(basePath + prjName);
            generateCodeHelper.setWriteToFile(createFileCheckBox.isSelected());
            generateCodeHelper.addSourceWriter(this);
            generateCodeHelper.setBusinessSrcPath(businessProjFolderTxt.getText());
            generateCodeHelper.setWsBusinessSrcPath(wsBusinessProjFolderTxt.getText());
            generateCodeHelper.setWebSrcPath(webProjFolderTxt.getText());
            generateCodeHelper.setProjName(projectNameTxt.getText());

            generateCodeHelper.setOverrideFile(overrideFileNotBackupCheckBox.isSelected());
            generateCodeHelper.setCreateQModel(qObjectCheckBox.isSelected());
            generateCodeHelper.setCreateDTO(dtoCheckBox.isSelected());
            generateCodeHelper.setCreateMapper(mapperCheckBox.isSelected());
            generateCodeHelper.setCreateRepository(respositoryCheckBox.isSelected());
            generateCodeHelper.setCreateServiceImpl(serviceCheckBox.isSelected());
            generateCodeHelper.setCreateWsService(wsServiceCheckBox.isSelected());

            File file = new File(classesFolderTxt.getText());
            URL url = file.toURI().toURL();
            ClassLoader urlClassLoader = new URLClassLoader(new URL[]{url});

            if (codeType == GenerateCodeType.BUSINESS) {
                List<String> modelLst = modelFileTable.getSelected();

                if (createFileCheckBox.isSelected()) {
                    generateCodeHelper.addSourceWriter(new FileSourceWriter());
                }

                for (String className : modelLst) {
                    try {
                        Class myClass = urlClassLoader.loadClass(MessageFormat.format("{0}.{1}.model.{2}", basePathTxt.getText(), prjName, className));
                        generateCodeHelper.generateCode(myClass, businessNameTxt.getText());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            if (codeType == GenerateCodeType.WS_BUSINESS_ADVANCE) {
                if (createFileCheckBox.isSelected()) {
                    generateCodeHelper.addSourceWriter(new FileSourceWriter());
                }

                List<String> interfaceLst = interfaceFileTable.getSelected();
                for (String className : interfaceLst) {
                    try {
                        Class myClass = urlClassLoader.loadClass(MessageFormat.format("{0}.{1}.service.{2}", basePathTxt.getText(), prjName, className));
                        generateCodeHelper.generateWsServiceFromClass(generateImplInBusinessCheckBox.isSelected(), myClass, businessNameTxt.getText());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }

            if (codeType == GenerateCodeType.WS_ENDPOIN) {
                if (createFileCheckBox.isSelected()) {
                    generateCodeHelper.addSourceWriter(new WsEndpointWriter());
                }

                List<String> interfaceLst = interfaceFileTable.getSelected();
                for (String className : interfaceLst) {
                    try {
                        Class myClass = urlClassLoader.loadClass(MessageFormat.format("{0}.{1}.service.{2}", basePathTxt.getText(), prjName, className));
                        generateCodeHelper.generateWsEndPoint(myClass, businessNameTxt.getText());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }

            if (codeType == GenerateCodeType.WS_ERROR) {
                List<String> interfaceLst = interfaceFileTable.getSelected();
                for (String className : interfaceLst) {
                    try {
                        Class myClass = urlClassLoader.loadClass(MessageFormat.format("{0}.{1}.service.{2}", basePathTxt.getText(), prjName, className));
                        generateCodeHelper.scanWsErrMethod(myClass, businessNameTxt.getText());

                        Class myClassImpl = urlClassLoader.loadClass(MessageFormat.format("{0}.{1}.service.{2}Impl", basePathTxt.getText(), prjName, className));
                        generateCodeHelper.scanWsErrMethod(myClassImpl, businessNameTxt.getText());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }

            if (codeType == GenerateCodeType.WS_ESB_ENDPOINT) {
                List<String> interfaceLst = interfaceFileTable.getSelected();
                for (String className : interfaceLst) {
                    try {
                        Class myClass = urlClassLoader.loadClass(MessageFormat.format("{0}.{1}.service.{2}", basePathTxt.getText(), prjName, className));
                        generateCodeHelper.generateWsEsb("proxy", myClass, businessNameTxt.getText());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                for (String className : interfaceLst) {
                    try {
                        Class myClass = urlClassLoader.loadClass(MessageFormat.format("{0}.{1}.service.{2}", basePathTxt.getText(), prjName, className));
                        generateCodeHelper.generateWsEsb("endpoint", myClass, businessNameTxt.getText());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(getParent(), ex, "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            this.setEnabled(true);
        }
    }


    private void onCancel() {
// add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        GenCodeForm dialog = new GenCodeForm();
        dialog.pack();

        dialog.setPreferredSize(new Dimension(800, 600));
        dialog.setVisible(true);
        System.exit(0);
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
        String currDir = GenCodeForm.class.getProtectionDomain().getCodeSource().getLocation().getPath();


        File file = (new File(currDir)).getParentFile().getParentFile().getParentFile();
        webFc = new JFileChooser(file);
        webFc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        webFc.setAcceptAllFileFilterUsed(false);

        businessFc = new JFileChooser(file);
        businessFc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        businessFc.setAcceptAllFileFilterUsed(false);

        wsBusinessFc = new JFileChooser(file);
        wsBusinessFc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        wsBusinessFc.setAcceptAllFileFilterUsed(false);

        classesFc = new JFileChooser(file);
        classesFc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        classesFc.setAcceptAllFileFilterUsed(false);

        modelFileTable = new ModelFileTable();
        modelTbl.setModel(modelFileTable);

        interfaceFileTable = new ModelFileTable();
        interfaceTable.setModel(interfaceFileTable);

        webProjFolderTxt.setText(getText("web.folder"));
        businessProjFolderTxt.setText(getText("business.folder"));
        wsBusinessProjFolderTxt.setText(getText("wsbusiness.folder"));
        classesFolderTxt.setText(getText("classes.folder"));
        projectNameTxt.setText(getText("proj.name"));
        basePathTxt.setText(Strings.isNullOrEmpty(getText("base.path")) ? "com.viettel.bccs" : getText("base.path"));
    }


    @Override
    public void writeSource(String filePath, Object fileContent) {
        if (DataUtil.isNullOrEmpty(filePath)) {
            return;
        }

        outputTxt.append("\n\n\n");
        if (!(fileContent instanceof EndPointInfo))
            outputTxt.append("******************************" + filePath + "******************************");
        outputTxt.append("\n");
        outputTxt.append(fileContent.toString());
    }

    private static String getText(String key) {
        if (config != null) {
            return config.get(key);
        }

        return null;
    }


    private static HashMap<String, String> config = new HashMap<>();

    static {
        try {
            String FILE_NAME = "C:\\Temp\\config.txt";
            List<String> lst = readSmallTextFile(FILE_NAME);
            for (String str : lst) {
                String[] strs = str.split("=");
                if (strs.length == 2)
                    config.put(strs[0], strs[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<String> readSmallTextFile(String aFileName) throws IOException {
        Path path = Paths.get(aFileName);
        return Files.readAllLines(path, StandardCharsets.UTF_8);
    }

    private static void writeSmallTextFile(List<String> aLines, String aFileName) throws IOException {
        File tmpFolder = new File("C:\\Temp");
        if (!tmpFolder.exists()) tmpFolder.mkdir();

        Path path = Paths.get(aFileName);
        Files.write(path, aLines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
    }

}
