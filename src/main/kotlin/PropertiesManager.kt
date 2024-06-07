package utils

import java.io.FileReader
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import org.apache.logging.log4j.kotlin.logger

private const val CONFIGFILELOCAL = "src/test/resources/appLocal.properties"
private const val CONFIGFILE = "src/test/resources/app.properties"

class PropertiesManager {

    private var properties: Properties? = null

    init {
        properties = Properties()
        val globalProperties = Properties()
        val localProperties = Properties()
        logger.info("ConfigFile:  $CONFIGFILE\n")
        logger.info(
            "\n Full path: user.dir + configFile:  " + System.getProperty("user.dir") + "\\"
                    + CONFIGFILE
        )
        logger.info("Checking existence of configFileLocal...  " + "\n")
        val existsPropertiesLocal = Files.exists(Paths.get(CONFIGFILELOCAL))
        logger.info("ConfigFileLocal exist: $existsPropertiesLocal\n")
        try {
            if (existsPropertiesLocal) {
                localProperties.load(FileReader(CONFIGFILELOCAL))
                globalProperties.load(FileReader(CONFIGFILE))
                properties!!.putAll(globalProperties)
                properties!!.putAll(localProperties)
            } else {
                properties!!.load(FileReader(CONFIGFILE))
            }
        } catch (exc: IOException) {
            throw RuntimeException(exc)
        }
        for (key: String in properties!!.stringPropertyNames()) {
            if (System.getProperty(key) != null) {
                val value = System.getProperty(key)
                logger.info("Overriding property $key with value passed directly: $value")
                properties!!.setProperty(key, value)
            }
        }
        logger.info("Properties info: ... " + "\n")
        val keys = properties!!.stringPropertyNames()
        for (key: String in keys) {
            logger.info("Key:Value - " + key + " : " + properties!!.getProperty(key))
        }
        logger.info("\n\nGoing ahead ... \n")
    }

   fun  getProperties(): Properties? {
        return this.properties
    }


}
