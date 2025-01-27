package com.optilog.setting;

import com.google.gson.Gson;
import com.optilog.log.Optilog;
import com.optilog.util.OnlyInInit;
import com.optilog.util.Util;
import com.optilog.util.exception.ConfigureException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

public class SettingFiles {
    public boolean printError = true;
    public boolean printInfo = true;
    public boolean printDebug = true;
    public boolean printWarn = true;
    public boolean printFatal = true;
    public String defaultConsolePath;
    public String Path1 = "";
    public String Path2 = "";
    public String Path3 = "";
    public String Path4 = "";
    public String Path5 = "";
    public String infoPath = "";
    public String errorPath = "";
    public String warnPath = "";
    public String debugPath = "";
    public String fatalPath = "";
    public boolean consoleInfo = true;
    public boolean consoleError = true;
    public boolean consoleDebug = true;
    public boolean consoleWarn = true;
    public boolean consoleFatal = true;
    public boolean serverInfo = false;
    public boolean serverError = false;
    public boolean serverDebug = false;
    public boolean serverWarn = false;
    public boolean serverFatal = false;
    public boolean startClient = false;
    public String host = "localhost";
    public int socketNumber = 65535;
    public String packingFormat = "[%yyyy-%MM-%dd|%HH:%mm:%ss(%SS)][%class %method(%file:%line)/%thread] %level:%msg";
    
    public String fileName = "%time Log(Client).log";
    
    private static Settings object;
    
    @OnlyInInit
    public static void check(String str, Optilog instance) throws IOException {
        if (!str.isBlank()) {
            if (str.startsWith("%prop -cp ")) {
                String s = str.substring(10);
                try (InputStream input = Optilog.class.getResourceAsStream(s)) {
                    if (input == null) {
                        instance.consoleFileMasterCaution = false;
                        Util.getOutput().println("Optilog Note: Can't find'" + s + "'in classpath.");
                        throw new ConfigureException("Can't find'" + s + "'in classpath.");
                    }
                    PropSettings.properties(input, instance);
                    return;
                }
            }
            if (str.startsWith("%prop ")) {
                return;
            }
            getSetting(str, instance, str.startsWith("-cp "));
        }
    }
    
    @OnlyInInit
    public static void getSetting(String s, Optilog instance, boolean isClasspath) throws IOException {
        try {
            String content;
            if (isClasspath) {
                try (InputStream input = Optilog.class.getResourceAsStream(s.substring(4))) {
                    if (input == null) {
                        instance.consoleFileMasterCaution = false;
                        Util.getOutput().println("Optilog Note: Can't find'" + s.substring(4) + "'in classpath.");
                        throw new ConfigureException("Can't find'" + s.substring(4) + "'in classpath.");
                    }
                    content = readAsString(input);
                }
            } else {
                content = Files.readString(Paths.get(s), StandardCharsets.UTF_8);
            }
            object = new Gson().fromJson(content, Settings.class);
        } catch (NoSuchFileException e) {
            e.printStackTrace();
            System.exit(1);
        }
        
        instance.allSetting = new SettingFiles();
        
        if (object.defaultConsolePath != null) {
            instance.allSetting.defaultConsolePath = object.defaultConsolePath;
        }
        if (object.Path1 != null) {
            instance.allSetting.Path1 = object.Path1;
        }
        if (object.Path2 != null) {
            instance.allSetting.Path2 = object.Path2;
        }
        if (object.Path3 != null) {
            instance.allSetting.Path3 = object.Path3;
        }
        if (object.Path4 != null) {
            instance.allSetting.Path4 = object.Path4;
        }
        if (object.Path5 != null) {
            instance.allSetting.Path5 = object.Path5;
        }
        if (object.infoPath != null) {
            instance.allSetting.infoPath = object.infoPath;
        }
        if (object.errorPath != null) {
            instance.allSetting.errorPath = object.errorPath;
        }
        if (object.warnPath != null) {
            instance.allSetting.warnPath = object.warnPath;
        }
        if (object.debugPath != null) {
            instance.allSetting.debugPath = object.debugPath;
        }
        if (object.fatalPath != null) {
            instance.allSetting.fatalPath = object.fatalPath;
        }
        
        instance.allSetting.printDebug = object.printDebug;
        instance.allSetting.printInfo = object.printInfo;
        instance.allSetting.printError = object.printError;
        instance.allSetting.printWarn = object.printWarn;
        instance.allSetting.printFatal = object.printFatal;
        instance.allSetting.consoleError = object.consoleError;
        instance.allSetting.consoleDebug = object.consoleDebug;
        instance.allSetting.consoleInfo = object.consoleInfo;
        instance.allSetting.consoleWarn = object.consoleWarn;
        instance.allSetting.consoleFatal = object.consoleFatal;
        instance.allSetting.serverInfo = object.infoSendToServer;
        instance.allSetting.serverError = object.errorSendToServer;
        instance.allSetting.serverDebug = object.debugSendToServer;
        instance.allSetting.serverWarn = object.warnSendToServer;
        instance.allSetting.serverFatal = object.fatalSendToServer;
        instance.allSetting.startClient = object.startClient;
        
        if (object.host != null) {
            instance.allSetting.host = object.host;
        }
        if (object.fileName != null) {
            instance.allSetting.fileName = object.fileName;
        }
        if (object.socketNumber != 0) {
            instance.allSetting.socketNumber = object.socketNumber;
        }
        if (object.packingFormat != null) {
            instance.allSetting.packingFormat = object.packingFormat;
        }
    }
    
    @OnlyInInit
    private static String readAsString(InputStream input) throws IOException {
        int n;
        StringBuilder sb = new StringBuilder();
        while ((n = input.read()) != -1) {
            sb.append((char) n);
        }
        return sb.toString();
    }
    
    public static void generateJsonSettings(String path) {
        File f = new File(path + "//Setting.json");
        try {
            if (!f.createNewFile()) {
                throw new IOException();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Files.writeString(f.toPath(), "{\n" +
                    "  \"printInfo\": true,\n" +
                    "  \"printError\": true,\n" +
                    "  \"printWarn\": true,\n" +
                    "  \"printDebug\": true,\n" +
                    "  \"printFatal\": true,\n" +
                    "  \"defaultConsolePath\": \"\",\n" +
                    "  \"consoleInfo\": false,\n" +
                    "  \"consoleDebug\": false,\n" +
                    "  \"consoleError\": false,\n" +
                    "  \"consoleWarn\": false,\n" +
                    "  \"consoleFatal\": false,\n" +
                    "  \"infoSendToServer\": false,\n" +
                    "  \"errorSendToServer\": false,\n" +
                    "  \"warnSendToServer\": false,\n" +
                    "  \"debugSendToServer\": false,\n" +
                    "  \"fatalSendToServer\": false,\n" +
                    "  \"startClient\": false,\n" +
                    "  \"socketNumber\": 65535,\n" +
                    "  \"packingFormat\": \"[%yyyy-%MM-%dd|%HH:%mm:%ss(%SS))][%class %method(%file:%line)/%thread] %level:%msg\",\n" +
                    "  \"fileName\": \"%timeLog(Client).log\",\n" +
                    "  \"host\": \"localhost\"\n" +
                    "}", StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void generatePropertiesSettings(String path) {
        File f = new File(path + "//Setting.properties");
        try {
            if (!f.createNewFile()) {
                throw new IOException();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Files.writeString(f.toPath(), "printInfo=true\n" +
                    "printError=true\n" +
                    "printWarn=true\n" +
                    "printDebug=true\n" +
                    "printFatal=true\n" +
                    "defaultConsolePath=\n" +
                    "#Path1=\n" +
                    "#Path2=\n" +
                    "#infoPath=%path1\n" +
                    "#errorPath=%path2\n" +
                    "#warnPath=%path1\n" +
                    "#debugPath=%path1\n" +
                    "#fatalPath=%path2\n" +
                    "consoleInfo=false\n" +
                    "consoleDebug=false\n" +
                    "consoleError=false\n" +
                    "consoleWarn=false\n" +
                    "consoleFatal=false\n" +
                    "infoSendToServer=false\n" +
                    "errorSendToServer=false\n" +
                    "warnSendToServer=false\n" +
                    "debugSendToServer=false\n" +
                    "fatalSendToServer=false\n" +
                    "startClient=false\n" +
                    "socketNumber=65535\n" +
                    "packingFormat=[%yyyy-%MM-%dd|%HH:%mm:%ss(%SS))][%class %method(%file:%line)/%thread] %level:%msg\n" +
                    "fileName=%timeLog.log\n" +
                    "host=localhost", StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
