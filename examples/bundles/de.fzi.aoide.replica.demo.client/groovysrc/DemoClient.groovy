import java.util.ResourceBundle;

import model.Client


println "Replica Framework Demo Client Groovy script running..."

String CFG_CLIENT
String CFG_SERVER_HOST
String CFG_SERVER_PORT
try {
	cfgBundle = ResourceBundle.getBundle("config")
	CFG_CLIENT = Integer.valueOf(cfgBundle.getString("client"))
	CFG_SERVER_HOST = cfgBundle.getString("ip")
	CFG_SERVER_PORT = cfgBundle.getString("port")
} catch (Exception e) {
	println "Could not read config.properties, cause: "+ e.getCause()
	e.printStackTrace()
}

cTypeCl = "ecf.generic.client" // Container type
target = "ecftcp://"+CFG_SERVER_HOST+":"+CFG_SERVER_PORT+"/server"

// Create client instance
def c = new Client(CFG_CLIENT)
c.connect(cTypeCl, target)

println "Replica Framework Demo Client Groovy script loaded"