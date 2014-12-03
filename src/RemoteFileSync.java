import java.io.File;
import com.rfsync.core.constants.CoreConstants;
import com.rfsync.core.network.CommandServerMaster;
import com.rfsync.core.network.FileServerForSlave;
import com.rfsync.core.network.SlaveRegister;
import com.rfsync.core.tasks.FileSystemWatcher;
import com.rfsync.core.tasks.PreInitializer;
import com.rfsync.core.util.RFSyncUtil;

/**
 * A Master-Slave model Remote File Sync Software
 * 
 * Run master as java RemoteFileSync master 8888 c:\\test\\
 * Run slave  as java RemoteFileSync slave 8080 c:\\test\\ localhost:8888
 * 
 * Now files get synced between two machines automaticall. Only for given directory 
 * 
 * @author MIBRLK0
 * 
 */
public class RemoteFileSync {

	String processType = null;
	String port = null;
	String path = null;
	String masterURL = null;

	/**
	 * Start here
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		new RemoteFileSync().start(args);

	}

	/**
	 * Starting File Sync
	 */
	private void start(String args[]) throws Exception {

		try {
			// Check Args:
			System.out.println("Starting Remote File Sync.");
			// make sure all arguments are good
			checkArgs(args);
			// make sure system has proper software installed to start
			new PreInitializer().execute();
			// Index Directory
			CoreConstants.setFileIndex(path);

			if (RFSyncUtil.isMaster(processType)) {
				System.out.println("Starting in MASTER mode. port " + port);
				CommandServerMaster.startThread(Integer.parseInt(port));
				// start watching directory
				new FileSystemWatcher().execute(path);
			} else {
				System.out.println("Starting in SLAVE mode.");
				SlaveRegister.handshake(masterURL, Integer.parseInt(port));
				System.out.println("Master handshake completed master = "
						+ masterURL + ":" + port);
				// new SlaveCommandListener().execute(port);

				FileServerForSlave.startThread(Integer.parseInt(port), path);
			}
			System.out.println("Remote File Sync Started successfully.");

		} catch (Exception excep) {
			System.out.println("Error while starting: " + excep.getMessage());
			excep.printStackTrace();
			//System.out.println("PRESS ANY KEY TO EXIT");
			//System.in.read();

		}
	}

	private void checkArgs(String args[]) throws Exception {

		if (args.length < 3) {
			throw new Exception("Run with required paramters. Example: java RemoteFileSync master 8888 c:\\test\\");
		}

		if (RFSyncUtil.isMaster(args[0]) || RFSyncUtil.isSlave(args[0])) {
			processType = args[0];
		} else {
			throw new Exception("First argument should be 'master' or 'slave' ");
		}

		port = args[1];
		path = args[2];

		if (Integer.parseInt(port) < CoreConstants.MIN_PORT) {
			throw new Exception("Please use port greater than 1024 inorder to avoid protocol conflicts.");
		}
		File f = new File(path);
		if (!f.exists() || !f.isDirectory()) {
			throw new Exception("File system path given does not exists or not a valid directory. Please create a folder:  "
							+ path);
		}

		if (RFSyncUtil.isSlave(args[0])) {
			if (args.length < 4) {
				throw new Exception(
						"Run with required paramters. Example: java RemoteFileSync slave 8080 c:\\test\\ localhost:8888");
			} else {
				masterURL = args[3];
			}

		}

		System.out.println("Parameter check successful.");
	}

}
