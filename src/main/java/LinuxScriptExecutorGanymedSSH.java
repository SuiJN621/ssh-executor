import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.CountDownLatch;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

/**
 * @author Sui
 * @date 2019.04.26 15:22
 */
public class LinuxScriptExecutorGanymedSSH {

    public static void main(String[] args) throws IOException, InterruptedException {
        Connection conn;
        String ip = "192.168.112.130";
        String userName = "root";
        String password = "suijianan1993";

        long startTime = System.currentTimeMillis();
        //Logger.enabled = true;
        conn = new Connection(ip);
        conn.connect(); // 连接

        boolean auth = conn.authenticateWithPassword(userName, password);// 认证

        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                StringBuilder out = new StringBuilder();
                StringBuilder err = new StringBuilder();
                try {
                    if (auth) {
                        Session session = conn.openSession(); // 打开一个会话
                        session.execCommand(
                                "sh ~/remote_execute.sh " + userName + " 192.168.112.131 " + password + " " +
                                        "\"cat /proc/cpuinfo\"");

                        StreamGobbler stdout = new StreamGobbler(session.getStdout());
                        BufferedReader readerOut = new BufferedReader(new InputStreamReader(stdout));
                        StreamGobbler stderr = new StreamGobbler(session.getStderr());
                        BufferedReader readerErr = new BufferedReader(new InputStreamReader(stderr));
                        String line;
                        while ((line = readerOut.readLine()) != null)
                        {
                            out.append(line);
                            out.append(System.getProperty("line.separator"));
                        }

                        while ((line = readerErr.readLine()) != null)
                        {
                            err.append(line);
                            err.append(System.getProperty("line.separator"));
                        }

                        int exitStatus = session.getExitStatus();

                        System.out.println("-----------------------------------");
                        System.out.println("执行结果状态码: " + exitStatus);
                        System.out.println("-----------------------------------");
                        System.out.println("执行结果: " + out);
                        System.out.println("-----------------------------------");
                        System.out.println("执行结果错误信息: " + err);
                        //System.out.println("-----------------------------------");
                        //System.out.println("执行时间: " + (System.currentTimeMillis() - startTime)/1000d + "秒");
                        session.close();
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                } finally {
                    countDownLatch.countDown();
                }
            }).start();
        }
        countDownLatch.await();
        conn.close();
        System.out.println("----------------------------");
        System.out.println("任务结束");
        System.out.println("执行时间: " + (System.currentTimeMillis() - startTime)/1000d + "秒");
    }
}

