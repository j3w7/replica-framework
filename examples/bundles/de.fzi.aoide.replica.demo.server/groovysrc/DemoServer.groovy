import java.util.ResourceBundle;

import model.Server


println "Replica Framework Demo Server Groovy script running..."

String CFG_SERVER_HOST
String CFG_SERVER_PORT
try {
	cfgBundle = ResourceBundle.getBundle("config")
	CFG_SERVER_HOST = cfgBundle.getString("ip")
	CFG_SERVER_PORT = cfgBundle.getString("port")
} catch (Exception e) {
	println "Could not read config.properties, cause: "+ e.getCause()
	e.printStackTrace()
}

cTypeSrv = "ecf.generic.server" // Container type
target = "ecftcp://"+CFG_SERVER_HOST+":"+CFG_SERVER_PORT+"/server"

def s = new Server()
s.start(cTypeSrv, target)

println "Replica Framework Demo Server Groovy script loaded"