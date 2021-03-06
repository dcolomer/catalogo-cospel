package com.catalogo.persistencia;

import java.io.IOException;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

abstract class AbstractDao {

    protected static DataSource ds;

    private static Properties properties;

    private final static Logger log = LogManager.getLogger(AbstractDao.class);

    static {
        try {
            final Context ctx = new InitialContext();
            ds = (DataSource) ctx.lookup("java:comp/env/jdbc/catalogo");

			properties = new Properties();
			properties.load(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("catalogo.properties"));
        } catch (NamingException | IOException e) {
            log.error(e);
        }
    }

	protected String getProperty(final String clave) {
		return properties.getProperty(clave);
	}

}

