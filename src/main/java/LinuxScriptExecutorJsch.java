import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * @author Sui
 * @date 2019.04.26 15:22
 */
public class LinuxScriptExecutorJsch {

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
        String userName = "root";
        String password = "suijianan1993";
        long startTime = System.currentTimeMillis();
        JSch jSch = new JSch();
        Session session = jSch.getSession("root", "192.168.112.130", 22);
        session.setConfig("StrictHostKeyChecking", "no");
        //session.setConfig("CheckCiphers", "");
        session.setPassword("suijianan1993");
        session.setConfig("PreferredAuthentications", "password");
        session.connect();   // making a connection with timeout.

//        CountDownLatch countDownLatch = new CountDownLatch(20);
//        for (int i = 0; i < 20; i++) {
//            new Thread(() -> {
//                ChannelExec channelExec = null;
//                try {
//                    channelExec = (ChannelExec) session.openChannel("exec");
//
//                    InputStream in = channelExec.getInputStream();
//                    channelExec.setCommand("sh ~/remote_execute.sh " + userName + " 192.168.112.131 " + password + " " +
//                            "\"cat /proc/cpuinfo\"");
//
//                    channelExec.connect();
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//                    String line;
//                    ArrayList<String> result = new ArrayList<>();
//                    int exitStatus = channelExec.getExitStatus();
//                    while ((line = reader.readLine()) != null) {
//                        result.add(line);
//                    }
//                        System.out.println("-----------------------------------");
//                        System.out.println("执行结果状态码: " + exitStatus);
//                        System.out.println("-----------------------------------");
//                        System.out.println("执行结果: " + result);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    countDownLatch.countDown();
//                    if (channelExec != null) channelExec.disconnect();
//                }
//            }).start();
//        }
//        countDownLatch.await();
        //System.out.println("-----------------------------------");
        //System.out.println("执行时间: " + (System.currentTimeMillis() - startTime) / 1000d + "秒");
        long startTime2 = System.currentTimeMillis();
        while(session.isConnected()){
            ChannelExec exec = (ChannelExec) session.openChannel("exec");
            exec.setCommand("ls");
            exec.connect();
            exec.disconnect();
            System.out.println("connected");
            Thread.sleep(5000);
        }
        System.out.println("执行时间: " + (System.currentTimeMillis() - startTime2) / 1000d + "秒");
        session.disconnect();
    }
}
