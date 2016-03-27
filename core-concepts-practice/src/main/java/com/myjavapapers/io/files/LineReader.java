/*package com.myjavapapers.io.files;

import i18n.slf4j.I18NLogger;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.util.List;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import processflow.ProcessFlow;
import processflow.ProcessInstance;
import processflow.Vars;
import processflow.compile.ParentClassContent;
import processflow.compile.SourceRef;
import processflow.elements.Element;
import processflow.elements.ElementLocation;
import processflow.elements.GoTo;
import processflow.elements.NextElements;
import processflow.elements.activities.AsyncScript;
import processflow.elements.activities.Script;
import processflow.elements.mail.Mail;

public class SEMVisionTransferResultsFile extends ProcessFlow {

    public SEMVisionTransferResultsFile() {
        Mail mail = new Mail();
        mail.setRef("mail");
        
        Script error = new Script();
        error.setRef("error");
        error.setNext(mail);
        
        AsyncScript transferFile = new AsyncScript();
        transferFile.setRef("transferFile");
        transferFile.setError(error);
        
        setStart(transferFile);
        
        setName("semvisiontransferresultsfile");
        setDescription("Copy Files from SEM systems to FeedBack System");
    }

    @ParentClassContent
    private static class Parent {
        
        private static final EnumSet<PosixFilePermission> PERM = EnumSet.allOf(PosixFilePermission.class);
        // User Details 
        public static final String USERNAME = "ftpuser";
        public static final String PASSWORD = "ftpuser1";

        public static File createOrFindLocalDirectory(String hostName) {
            File dir = new File((SystemUtils.IS_OS_WINDOWS ? "C:/SEMDIR/" : "/tools/photocim/sem/") + hostName.toUpperCase());
            if (!dir.isDirectory()) {
                dir.mkdirs();
            }
            return dir;
        }

        public static void writeFilesToLocalSystem(FTPClient semClient, FTPFile localFile, File localDir) throws FileNotFoundException, IOException {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            semClient.retrieveFile(localFile.getName(), os);
            List<String> outLines = new ArrayList<>();
            try (LineNumberReader lnr = new LineNumberReader(new InputStreamReader(new ByteArrayInputStream(os.toByteArray())))) {
                String line = lnr.readLine();
                while (line!=null) {
                    String[] split = StringUtils.split(line, " ");
                    if (split != null && split.length >= 5) {
                        String intVal = split[4];
                        if (!intVal.isEmpty()) {
                            try {
                                double value = Double.parseDouble(intVal);
                                if (value > 10) {  
                                    split[4] = String.valueOf(value / 1000);
                                }
                            } catch (NumberFormatException e) {
                            }
                            line = StringUtils.join(split, " ");
                        }
                    }
                    outLines.add(line);
                    line = lnr.readLine();
                }
            }
            File outFile = new File(localDir, localFile.getName());
            FileUtils.writeLines(outFile, outLines);
            if (!SystemUtils.IS_OS_WINDOWS) {
                Files.setPosixFilePermissions(outFile.toPath(), PERM);
            }
        }

    }

    private static class transferFile extends Parent implements AsyncScript.Interface, AsyncScript.CreateInputObject {

        @Override
        @SourceRef("transferFile")
        public Object createInputObject(ProcessInstance process, ElementLocation location, AsyncScript currentElement, Element previousElement, Exception exception, boolean thrownByPrevious, Vars globals, Vars locals, I18NLogger logger) throws Exception {
            String contextName = process.getContext().getName();
            Map<String, String> contextAndLotMap = new HashMap<>();
            contextAndLotMap.put("contextName", contextName);
            contextAndLotMap.put("startswith", globals.get("startswith").toString());
            return contextAndLotMap;
        }

        @SourceRef("transferFile")
        @Override
        public Object execute(Object input, final i18n.slf4j.I18NLogger logger) throws Exception {
            Map<String, String> contextAndLotMap = (HashMap<String, String>) input;
            String hostName = contextAndLotMap.get("contextName");
            String startsWith = contextAndLotMap.get("startswith");
            String remoteDir = "/usr/local/opal/reports/JAZZ/PRODUCTION/PRODUCTION";
            FTPClientConfig config = new FTPClientConfig();
            FTPClient semClient = new FTPClient();
            semClient.configure(config);
            semClient.connect(hostName);
            try {
                semClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                if (!semClient.login(USERNAME, PASSWORD)) {
                    throw new Exception("Login Failed for User [" + USERNAME + "]");
                }
                semClient.enterLocalPassiveMode();
                semClient.changeWorkingDirectory(remoteDir);
                FTPFile localFile = null;
                for (FTPFile remoteSEMFile : semClient.listFiles(remoteDir)) {
                    if (remoteSEMFile.getName().startsWith(startsWith)) {
                        if (localFile == null || localFile.getTimestamp().before(remoteSEMFile.getTimestamp())) {
                            localFile = remoteSEMFile;
                        }
                    }
                }
                if (localFile != null) {
                    logger.info("Found remote file " + localFile);
                    File localDir = createOrFindLocalDirectory(hostName);
                    writeFilesToLocalSystem(semClient, localFile, localDir);
                } else {
                    throw new FileNotFoundException("Could not find file matching " + startsWith);
                }
            } finally {
                semClient.disconnect();
            }
            return null;
        }
    }

    private static class error extends Parent implements Script.Interface {
        @Override
        @SourceRef("error")
        public GoTo run(final processflow.ProcessInstance process, final processflow.elements.ElementLocation location, final Script currentElement, final processflow.elements.Element previousElement, final Exception exception, final boolean thrownByPrevious, final processflow.Vars globals, final processflow.Vars locals, final i18n.slf4j.I18NLogger logger, final NextElements next) throws Exception {
            locals.put(Mail.FROM, process.getContext().getName() + ".automation@towerjazz.com");
            if (exception instanceof FileNotFoundException) {
                locals.put(Mail.TO, "kenneth.gendron@towerjazz.com");
                locals.put(Mail.SUBJECT, process.getContext().getName() + " Error: " + exception.getMessage());
            } else {
                process.restartProcess(true, null, 1, TimeUnit.MINUTES);
                if (exception instanceof IOException) {
                    locals.put(Mail.TO, "kenneth.gendron@towerjazz.com;kenny.do@towerjazz.com;julia.wu@towerjazz.com;glenn.miyagi@towerjazz.com");
                } else {
                    locals.put(Mail.TO, "kenneth.gendron@towerjazz.com");
                }
                locals.put(Mail.SUBJECT, process.getContext().getName() + " Error: " + exception.getMessage());
                StringWriter sw = new StringWriter();
                exception.printStackTrace(new PrintWriter(sw));
                locals.put(Mail.BODY, sw.toString());
                locals.put(Mail.MIMETYPE, "text/plain");
            }
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        System.err.println(oracleupdate.OracleUpdate.upload(((ProcessFlow) Class.forName(Thread.currentThread().getStackTrace()[1].getClassName()).newInstance())));
    }

}
*/