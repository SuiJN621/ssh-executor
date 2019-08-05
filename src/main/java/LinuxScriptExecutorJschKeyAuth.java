import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Vector;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

/**
 * @author Sui
 * @date 2019.04.26 15:22
 */
public class LinuxScriptExecutorJschKeyAuth {

    public static void main(String[] args) throws JSchException, InterruptedException {
//        JSch.setLogger(new Logger() {
//            @Override
//            public boolean isEnabled(int level) {
//                return true;
//            }
//
//            @Override
//            public void log(int level, String message) {
//                System.out.println("log level " + level + ":  " + message + "\n");
//            }
//        });
        String userName = "SuiJN";
        //String userName = "root";
        //String password = "suijianan1993";
        long startTime = System.currentTimeMillis();
        JSch jSch = new JSch();
        JSch.setConfig("PreferredAuthentications", "publickey");
        JSch.setConfig("StrictHostKeyChecking", "no");

        jSch.addIdentity(File.separator + System.getProperty("user.home") + File.separator +
                ".ssh" + File.separator + "id_rsa", (String) null);
        Vector identityNames = jSch.getIdentityNames();
        Session session = jSch.getSession(userName, "192.168.112.130", 22);

        session.connect();   // making a connection with timeout.

        ChannelExec channelExec = null;
        try {
            channelExec = (ChannelExec) session.openChannel("exec");

            InputStream in = channelExec.getInputStream();
            channelExec.setCommand("cat /root/done.tag");

            channelExec.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(channelExec.getErrStream()));
            String line;
            ArrayList<String> result = new ArrayList<>();
            ArrayList<String> error = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                result.add(line);
            }
            while ((line = errorReader.readLine()) != null) {
                error.add(line);
            }
            int exitStatus = channelExec.getExitStatus();
            System.out.println("-----------------------------------");
            System.out.println("执行结果状态码: " + exitStatus);
            System.out.println("-----------------------------------");
            System.out.println("执行结果: " + result);
            System.out.println("-----------------------------------");
            System.out.println("执行异常: " + error);
            System.out.println("执行时间: " + (System.currentTimeMillis() - startTime)/1000 + "秒");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (channelExec != null) {
                channelExec.disconnect();
            }
            session.disconnect();
        }
    }


    public static class MyUserInfo implements UserInfo {

        @Override
        public String getPassphrase() {
            return null;
        }

        @Override
        public String getPassword() {
            return null;
        }

        @Override
        public boolean promptPassword(String message) {
            return false;
        }

        @Override
        public boolean promptPassphrase(String message) {
            return false;
        }

        @Override
        public boolean promptYesNo(String message) {
            return false;
        }

        @Override
        public void showMessage(String message) {

        }
    }
}
