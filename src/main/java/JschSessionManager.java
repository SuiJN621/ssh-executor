import java.io.Closeable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * @author Sui
 * @date 2019.05.16 15:39
 */
public class JschSessionManager implements Closeable {

    private static final Logger logger = LoggerFactory.getLogger(JschSessionManager.class);

    private final SessionFactory sessionFactory;
    private Session session;

    /**
     * Creates a SessionManager for the supplied <code>sessionFactory</code>.
     *
     * @param sessionFactory The session factory
     */
    public JschSessionManager(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void close() {
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
        session = null;
    }

    public Session getSession() throws JSchException {
        if (session == null || !session.isConnected()) {
            logger.debug("getting new session from factory session");
            session = sessionFactory.newSession();
            logger.debug("connecting session");
            session.connect();
        }
        return session;
    }

    /**
     * Returns the session factory used by this manager.
     *
     * @return The session factory
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
