/*package tool.semvision;

import automation.flows.statemachine.ToolLot;
import i18n.slf4j.I18NLogger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.util.EnumSet;
import java.util.regex.Pattern;

import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import processflow.ProcessFlow;
import processflow.ProcessInstance;
import processflow.Vars;
import processflow.compile.ParentClassContent;
import processflow.compile.SourceRef;
import processflow.elements.Element;
import processflow.elements.ElementLocation;
import processflow.elements.activities.AsyncScript;

public class SEMTransferResults extends ProcessFlow {

    public SEMTransferResults() {
        AsyncScript transferFile = new AsyncScript();
        transferFile.setRef("transferFile");
        setStart(transferFile);
        setName("semtransferresultsfile");
        setDescription("Copy Files from SEM systems to FeedBack System");
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

        public static FTPClient loginToSEMSystem(String hostName, String userName, String passWord) throws IOException, Exception {
            FTPClientConfig config = new FTPClientConfig();
            FTPClient client = new FTPClient();
            client.configure(config);
            client.connect(hostName);
            client.setFileType(FTPClient.BINARY_FILE_TYPE);
            int repycode = client.getReplyCode();
            if (!FTPReply.isPositiveCompletion(repycode)) {
                System.err.println("FTP server refused connection.");
                throw new Exception("FTP Server refussed connection");
            } else {
                System.out.println("Connected sucessfuly");
                if (!client.login(userName, passWord)) {
                    System.err.println("FTP server Login Failed.");
                    throw new Exception("Login Failed for User [" + userName + "]");
                }
            }
            return client;
        }

        public void performTransferOperation(FTPClient semClient, String hostName, String remoteDir,String searchString) throws IOException {
            try {
              //  FTPFile localFile = null;
            	semClient.changeWorkingDirectory(remoteDir);
                for (FTPFile remoteSEMFile : semClient.listFiles(remoteDir)) {
                	FTPFile localFile = getRequiredFilesFromSEMSystem(remoteSEMFile,searchString);
                	if (localFile != null){
                		File localDir = createOrFindLocalDirectory(hostName);
                		setPermissionsToPath(localDir);
                		writeFilesToLocalSystem(semClient, localFile, localDir);
                	}
                }

            } catch (Exception e) {
                // TODO Do nothing for now
            } finally {
                semClient.disconnect();
            }
        }

		private FTPFile getRequiredFilesFromSEMSystem(FTPFile remoteSEMFile, String searchString) {
			FTPFile localFile = null;
			if (remoteSEMFile != null) {  //J94333.1  - need to replace this
			    if (remoteSEMFile.getName().startsWith(searchString) &&  remoteSEMFile.getName().contains(".prn")) {
			        if (localFile == null || localFile.getTimestamp().before(remoteSEMFile.getTimestamp())) {
			            localFile = remoteSEMFile;
			        }
			    }
			}
			return localFile;
		}

		private void setPermissionsToPath(File dir) throws IOException {
			if (!SystemUtils.IS_OS_WINDOWS) {
			    Files.setPosixFilePermissions(dir.toPath(), PERM);
			}
		}

		private File createOrFindLocalDirectory(String hostName) {
			File localDir = new File(getBaseDirectory(), hostName.toUpperCase());
			if (!localDir.isDirectory()) {
			    localDir.mkdirs();
			}
			return localDir;
		}

		private File getBaseDirectory(){
			 File baseDir = null;
             if (SystemUtils.IS_OS_WINDOWS) {
                 baseDir = new File("C:/SEMDIR");
             }
             return baseDir;
		}
		
		private void writeFilesToLocalSystem(FTPClient semClient,FTPFile localFile, File localDir) throws FileNotFoundException,IOException {
			if (localFile != null) {
			    File outFile = new File(localDir, localFile.getName());
			    FileOutputStream fos = new FileOutputStream(outFile);
			    try {
			        semClient.enterLocalPassiveMode();
			        semClient.retrieveFile(localFile.getName(), fos);
			    } catch (Exception e) {
			        // TODO do nothing for now
			    } finally {
			        fos.close();
			    }
			    setPermissionsToPath(outFile);
			} else {
			    System.err.print("Received Empty File / File Copying Failed");
			}
		}
    }

    private static class transferFile extends Parent implements AsyncScript.Interface, AsyncScript.CreateInputObject {

        @Override
        @SourceRef("transferFile")
        public Object createInputObject(ProcessInstance process, ElementLocation location, AsyncScript currentElement, Element previousElement, Exception exception, boolean thrownByPrevious, Vars globals, Vars locals, I18NLogger logger) {
            return null;
        }

        @SourceRef("transferFile")
        @Override
        public Object execute(Object input, final i18n.slf4j.I18NLogger logger) throws Exception {
            String userName = "ftpuser";
            String passWord = "ftpuser1";
            String remoteDir = "/usr/local/opal/reports/JAZZ/PRODUCTION/PRODUCTION";
            try {
                FTPClient f3SEM07Client = loginToSEMSystem(tool.common.ToolConstants.F3SEM07, userName, passWord);
                performTransferOperation(f3SEM07Client, tool.common.ToolConstants.F3SEM07, remoteDir,"J94333.1");
            } catch (Exception e) {
                // TODO : Do we need to stop transferring from other or continue.
                throw new Exception("Exception while transferring F3SEM07");
            }
            //Transfer Files from  F3SEM08.
            try {
                FTPClient f3SEM08Client = loginToSEMSystem("f3sem08", userName, passWord);
                performTransferOperation(f3SEM08Client, "f3sem08", remoteDir,"");
            } catch (Exception e) {
                // TODO : Do we need to stop transferring from other or continue.
                throw new Exception("Exception while transferring F3SEM08");
            }
            //Transfer Files from  F3SEM9.
            try {
                FTPClient f3SEM09Client = loginToSEMSystem("f3sem09", userName, passWord);
                performTransferOperation(f3SEM09Client, "f3sem09", remoteDir,"");
            } catch (Exception e) {
                // TODO : Do we need to stop transferring from other or continue.
                throw new Exception("Exception while transferring F3SEM09");
            }

            //Transfer Files from  F3SEM10.
            try {
                FTPClient f3SEM10Client = loginToSEMSystem("f3sem10", userName, passWord);
                performTransferOperation(f3SEM10Client, "f3sem10", remoteDir,"");
            } catch (Exception e) {
                // TODO : Do we need to stop transferring from other or continue.
                throw new Exception("Exception while transferring F3SEM10");
            }
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        System.err.println(oracleupdate.OracleUpdate.upload(((ProcessFlow) Class.forName(Thread.currentThread().getStackTrace()[1].getClassName()).newInstance())));
    }

}
*/