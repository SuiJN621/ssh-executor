import java.util.Map;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Proxy;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

/**
 * @author Sui
 * @date 2019.05.16 15:51
 */
public interface SessionFactory {
    int SSH_PORT = 22;

    String getHostname();

    int getPort();

    Proxy getProxy();

    String getUsername();

    UserInfo getUserInfo();

    Session newSession() throws JSchException;

    SessionFactoryBuilder newSessionFactoryBuilder();

    abstract class SessionFactoryBuilder {
        protected Map<String, String> config;
        protected String hostname;
        protected JSch jsch;
        protected int port;
        protected Proxy proxy;
        protected String username;
        protected UserInfo userInfo;

        protected SessionFactoryBuilder(JSch jsch, String username, String hostname, int port, Proxy proxy,
                                        Map<String, String> config, UserInfo userInfo) {
            this.jsch = jsch;
            this.username = username;
            this.hostname = hostname;
            this.port = port;
            this.proxy = proxy;
            this.config = config;
            this.userInfo = userInfo;
        }

        public SessionFactoryBuilder setConfig(Map<String, String> config) {
            this.config = config;
            return this;
        }

        public SessionFactoryBuilder setHostname(String hostname) {
            this.hostname = hostname;
            return this;
        }

        public SessionFactoryBuilder setPort(int port) {
            this.port = port;
            return this;
        }

        public SessionFactoryBuilder setProxy(Proxy proxy) {
            this.proxy = proxy;
            return this;
        }

        public SessionFactoryBuilder setUsername(String username) {
            this.username = username;
            return this;
        }

        public SessionFactoryBuilder setUserInfo(UserInfo userInfo) {
            this.userInfo = userInfo;
            return this;
        }

        abstract public SessionFactory build();
    }
}
