# 
# client.rb
# 
# Created on Oct 21, 2011, 12:58:45
# Author: Jan Novacek, novacek@fzi.de

class Client
  attr_accessor :name, :host, :graph
  @commManager
  
  def initialize(name, host)
    @name = name
    @host = host
    @commManager = de.fzi.aoide.replica.comm::DefaultCommManagerFactory.new
  end
  
end