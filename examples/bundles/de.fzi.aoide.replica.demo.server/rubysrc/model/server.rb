# 
# server.rb
# 
# Created on Oct 21, 2011, 12:58:45
# Author: Jan Novacek, novacek@fzi.de

require 'module/replica'

# defaults
@SERVER_HOST = "localhost"
@SERVER_PORT = "5888"

class Server
  attr_accessor :name, :host
  @commManager 
  
  def initialize(name, host)
    @name = name
    @host = host
    @commManager = DefaultCommManagerFactory.new
  end
  
  def start
    @configServer = Properties.new
    @configServer.put("containerType", "ecf.generic.server")
    @configServer.put("containerID", "ecftcp://"+@SERVER_HOST+":"+@SERVER_PORT+"/server")
    conSrv = @commManager.createConnection(configServer)
    conSrv.initialize
    conSrv.connect
  end
end