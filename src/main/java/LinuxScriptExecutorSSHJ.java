import java.io.IOException;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;

/**
 * @author Sui
 * @date 2019.04.26 15:22
 */
public class LinuxScriptExecutorSSHJ {

    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        final SSHClient ssh = new SSHClient();
        ssh.addHostKeyVerifier(new PromiscuousVerifier());
        ssh.connect("192.168.112.130");
        ssh.authPassword("root", "suijianan1993");
        Session session = null;
        try {
            session = ssh.startSession();
            final Session.Command cmd = session.exec("sh ~/test.sh");
            String result = IOUtils.readFully(cmd.getInputStream()).toString();
            System.out.println("-----------------------------------");
            System.out.println("执行结果状态码: " + cmd.getExitStatus());
            System.out.println("-----------------------------------");
            System.out.println("执行结果: " + result);
            System.out.println("-----------------------------------");
            System.out.println("执行时间: " + (System.currentTimeMillis() - startTime) / 1000d + "秒");
        } finally {
            try {
                if (session != null) {
                    session.close();
                }
            } catch (IOException e) {
                // Do Nothing
            }

            ssh.disconnect();
        }
    }
}
