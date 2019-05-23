import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Proxy;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

/**
 * @author Sui
 * @date 2019.05.16 16:01
 */
public class SimplePasswordSessionFactory implements SessionFactory {
    private static Logger logger = LoggerFactory.getLogger(SimplePasswordSessionFactory.class);

    private Map<String, String> config;

    private String hostname;
    private JSch jsch;
    private String password;
    private int port;
    private String username;

    public SimplePasswordSessionFactory() {
        this(null, null, null, null);
    }

    public SimplePasswordSessionFactory(String username, String hostname, Integer port, JSch jsch) {
        JSch.setLogger(new Slf4jBridge());
        if (jsch == null) {
            this.jsch = new JSch();
        } else {
            this.jsch = jsch;
        }

        if (username == null) {
            this.username = System.getProperty("user.name").toLowerCase();
        } else {
            this.username = username;
        }

        if (hostname == null) {
            this.hostname = "localhost";
        } else {
            this.hostname = hostname;
        }

        if (port == null) {
            this.port = SSH_PORT;
        } else {
            this.port = port;
        }
    }

    @Override
    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    @Override
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public Proxy getProxy() {
        return null;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public UserInfo getUserInfo() {
        return null;
    }

    @Override
    public Session newSession() throws JSchException {
        Session session = jsch.getSession(username, hostname, port);
        if (config != null) {
            for (String key : config.keySet()) {
                session.setConfig(key, config.get(key));
            }
        }
        if (password != null) {
            session.setPassword(password);
        }
        return session;
    }

    @Override
    public SessionFactoryBuilder newSessionFactoryBuilder() {
        return new SessionFactoryBuilder(jsch, username, hostname, port, null, config, null) {
            @Override
            public SessionFactory build() {
                SimplePasswordSessionFactory sessionFactory = new SimplePasswordSessionFactory(username, hostname,
                        port, jsch);
                sessionFactory.config = config;
                sessionFactory.password = password;
                return sessionFactory;
            }
        };
    }

    public void setConfig(Map<String, String> config) {
        this.config = config;
    }

    public void setConfig(String key, String value) {
        if (config == null) {
            config = new HashMap<>();
        }
        config.put(key, value);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "ssh://" + username + "@" + hostname + ":" + port;
    }
}