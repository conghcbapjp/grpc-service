package jp.legalontech.cabinet.application.config;

import jp.legalontech.cabinet.Main;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {
    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String DB_NAME = "local_low_code";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456789";

    public static SqlSessionFactory buildSessionFactory() {
        Properties properties = new Properties();
        properties.put("url", "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME);
        properties.put("username", USERNAME);
        properties.put("password", PASSWORD);

        return getSessionFactory(properties);
    }

    private static SqlSessionFactory getSessionFactory(Properties properties) {
        try (InputStream in = Main.class.getResourceAsStream("/database-config.xml")) {
            return new SqlSessionFactoryBuilder().build(in, properties);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read the MyBatis configuration file", e);
        }
    }
}
