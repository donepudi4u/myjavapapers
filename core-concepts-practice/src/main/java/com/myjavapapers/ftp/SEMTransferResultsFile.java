/*package com.myjavapapers.ftp;
package com.myjavapapers.io.files;



import automation.flows.statemachine.ToolLot;
import i18n.slf4j.I18NLogger;
import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.util.EnumSet;
import java.util.regex.Pattern;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import processflow.ProcessFlow;
import processflow.ProcessInstance;
import processflow.Vars;
import processflow.compile.SourceRef;
import processflow.compile.ParentClassContent;
import processflow.elements.Element;
import processflow.elements.ElementLocation;
import processflow.elements.GoTo;
import processflow.elements.NextElements;
import processflow.elements.activities.AsyncScript;
import processflow.elements.activities.Script;

public class SEMTransferResultsFile extends ProcessFlow {
    
    public SEMTransferResultsFile() {
        AsyncScript transferFile = new AsyncScript();
        transferFile.setRef("transferFile");
        
        Script test = new Script();
        test.setRef("test");
        test.setNext(transferFile);
        
        setStart(test);
        
        setName("semtransferresultsfile");
        setDescription("Transfer results file for IVS when lot is completed.");
    }
    
    @ParentClassContent
    private static class Parent {
        public static final Pattern SLASH = Pattern.compile("\\\\");
        EnumSet<PosixFilePermission> PERM = EnumSet.allOf(PosixFilePermission.class);
        public static class Wrapper implements Serializable {
            private static final long serialVersionUID = 1L;
            public final ToolLot lot;
            public final String host;
            public Wrapper(ToolLot lot, String host) {
                this.lot = lot;
                this.host = host;
            }
        }
    }

    private static class test extends Parent implements Script.Interface {
        @Override
        @SourceRef("test")
        public GoTo run(final processflow.ProcessInstance process, final processflow.elements.ElementLocation location, final Script currentElement, final processflow.elements.Element previousElement, final Exception exception, final boolean thrownByPrevious, final processflow.Vars globals, final processflow.Vars locals, final i18n.slf4j.I18NLogger logger, final NextElements next) throws Exception {
            String recipe = "B2397_A\\M4\\RG";
            ToolLot lot = new ToolLot("J90492.1");
            lot.setToolRecipe(recipe);
            lot.getCustomState().put("toollotid", "J9049201");
            locals.put(ToolLot.TOOLLOT, lot);
            return null;
        }
    }

    private static class transferFile extends Parent implements AsyncScript.Interface, AsyncScript.CreateInputObject {
        @Override
        @SourceRef("transferFile")
        public Object createInputObject(ProcessInstance process, ElementLocation location, AsyncScript currentElement, Element previousElement, Exception exception, boolean thrownByPrevious, Vars globals, Vars locals, I18NLogger logger) {
            //String host = process.getContext().getName();
            //ToolLot lot = locals.get(ToolLot.TOOLLOT);
            return null;
        }
        
        @SourceRef("transferFile")
        @Override
        public Object execute(Object input, final i18n.slf4j.I18NLogger logger) throws Exception {
            ToolLot lot = (ToolLot)input;
           Vars var =  (Vars)input;
            FTPClientConfig config = new FTPClientConfig();
            FTPClient client = new FTPClient();
            client.configure(config);
            client.connect("f3sem07");
        //    String startsWith = lot.getCustomState().get("toollotid").asString() + ".R";
            File baseDir;
            if (SystemUtils.IS_OS_WINDOWS) {
                baseDir = new File("C:/SEMDIR");
            } else {
                baseDir = new File("/tools/photocim/sem/" + var.get("host"));
            }
            String lotID = lot.getId();
            //Look for file starting with lot ID and ending in .prn*
            try {
                client.setFileType(FTPClient.BINARY_FILE_TYPE);
                client.login("ftpuser", "ftpuser1");
                FTPFile file = null;
                String dirStr = SLASH.matcher(lot.getToolRecipe()).replaceAll("/");
                for (FTPFile f : client.listFiles(dirStr)) {
                    if (f.getName().startsWith(startsWith)) {
                        if (file==null || file.getTimestamp().before(f.getTimestamp())) {
                            file = f;
                        }
                    }
                }
                File dir = new File(baseDir, dirStr);
                dir.mkdirs();
                if (!SystemUtils.IS_OS_WINDOWS) {
                    Files.setPosixFilePermissions(dir.toPath(), PERM);
                }
                File outFile = new File(dir, file.getName());
                FileOutputStream fos = new FileOutputStream(outFile);
                try {
                    client.retrieveFile(dirStr + "/" + file.getName(), fos);
                } finally {
                    fos.close();
                }
                if (!SystemUtils.IS_OS_WINDOWS) {
                    Files.setPosixFilePermissions(outFile.toPath(), PERM);
                }
            } finally {
                client.disconnect();
            }
            return null;
        }
    }
    
    public FTPClient loginToSEMClient(){
        FTPClient client = null;
        
        return client;
    }
    
    public static void main(String[] args) throws Exception {
        System.err.println(oracleupdate.OracleUpdate.upload(((ProcessFlow) Class.forName(Thread.currentThread().getStackTrace()[1].getClassName()).newInstance())));
    }

}
*/