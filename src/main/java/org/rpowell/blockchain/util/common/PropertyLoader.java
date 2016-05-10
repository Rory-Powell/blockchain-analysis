package org.rpowell.blockchain.util.common;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.util.Properties;

public class PropertyLoader {
    private static final Logger log = LoggerFactory.getLogger(PropertyLoader.class);
    private static final String PROP_FILE="config.properties";

    /**
     * Read a property given the property name
     * @param propertyName  The property name
     * @return              The property value
     */
    public static String loadProperty(String propertyName) {
        Properties properties = new Properties();
        InputStream input = null;
        String propertyValue = null;

        try {
            input = PropertyLoader.class.getClassLoader().getResourceAsStream(PROP_FILE);
            properties.load(input);
            propertyValue = properties.getProperty(propertyName);
        } catch (IOException e) {
            log.error("IOException when loading property", e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    log.error("IOException when closing input stream", e);
                }
            }
        }
        return propertyValue;
    }
}