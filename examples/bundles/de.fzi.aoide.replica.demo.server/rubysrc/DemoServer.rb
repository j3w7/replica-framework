# 
# ReplicaDemoClient.rb
# 
# Created on Oct 21, 2011, 12:58:45
# Author: Jan Novacek, novacek@fzi.de

require 'java'
require 'module/awt'
require 'module/swing'
require 'module/lang'
require 'module/ecf'
require 'model/server'

#import java.net.InetAddress;
#import java.net.UnknownHostException;
#import java.util.LinkedList;
#import java.util.List;

puts "Replica Framework Demo Server Ruby script running..."

class ReplicaDemoServer < Swing::JFrame
  #constructor
    def initialize(title)
      super(title)
      setSize(Awt::Dimension.new(400, 400))
      initComponents
    end
  
  #control the display of this UI
    def display(is_visible)
      self.setVisible(is_visible)
    end
    
    def get_user_input
      return {'value' => @txtConvertingValue.getText, 
              'from' => @ddlFromCurrency.getSelectedItem().code,
              'to' => @ddlToCurrency.getSelectedItem().code}
    end
    
    def show_converting_result result
      @lblResult.setText(result.to_s << ' ' << @ddlToCurrency.getSelectedItem().to_s)
    end
    
    private
  
    def initComponents
      @ddlFromCurrency = Swing::JComboBox.new
      @lblFrom = Swing::JLabel.new 'Test'
      add(@ddlFromCurrency)
      add(@lblFrom)
#      self.pack
    end
    
    def create_currency_code_model
      model = Swing::DefaultComboBoxModel.new
      model.addElement Currency.new('USD', 'US $')
      model.addElement Currency.new('CAD', 'Canadian $')
      model.addElement Currency.new('CNY', 'Chinese Yuan')
      model.addElement Currency.new('EUR', 'Euro')
      model.addElement Currency.new('JYP', 'Japanese Yen')
      model.addElement Currency.new('KRW', 'Korean Won')
      model.addElement Currency.new('THB', 'Thai Baht')
      model.addElement Currency.new('AUD', 'Australian $')
      return model
    end
end

#x = ReplicaDemoServer.new "Replica Framework Demo Server"
#x.display(true)
#
#server = Server.new "Replica Framework Demo Server", "localhost"
#server.start

puts "Replica Framework Demo Server Ruby script loaded"