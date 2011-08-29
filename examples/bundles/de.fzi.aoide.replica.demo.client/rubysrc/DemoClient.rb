# 
# ReplicaDemoClient.rb
# 
# Created on Oct 21, 2011, 12:58:45
# Author: Jan Novacek, novacek@fzi.de

require 'java'
require 'module/awt'
require 'module/swing'
require 'module/lang'
require 'module/jung'
require 'module/replica'
require 'model/client'

#import java.net.InetAddress;
#import java.net.UnknownHostException;
#import java.util.LinkedList;
#import java.util.List;

puts "Replica Framework Demo Ruby client script running..."

class ReplicaDemoClient < Swing::JFrame
  #constructor
    def initialize(title)
      super(title)
      setSize(Awt::Dimension.new(400, 400))
      setDefaultCloseOperation(EXIT_ON_CLOSE)
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
    
#    private
  
    def initComponents
      lblFrom = Swing::JLabel.new 'Test'
      add(lblFrom)
#      add(create_controls)
      
#      demo = Java::de.fzi.aoide.replica.demo.client.ReplicaOntologyDemo.new
      demo = ReplicaOntologyDemo.new
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
    
    def create_controls(vv)
      controls = Box.new BoxLayout.X_AXIS;
      controls.add(vvFac.createVertexLabelCheckBox(vv));
      controls.add(Box.createHorizontalStrut(8));
      controls.add(vvFac.createEdgeLabelCheckBox(vv));
      controls.add(vv.getGraphMouse().getModeComboBox());
      controls.setBorder(BorderFactory.createTitledBorder("Controls"));
      
      client = createColoredLabel("Client "+CLIENT, getClientColor(CLIENT), Point.new(0,0));
      address = JLabel.new("Address: "+SERVER_HOST+":"+SERVER_PORT);
      
      iBox = new Box(BoxLayout.X_AXIS);
      iBox.add(client);
      iBox.add(Box.createHorizontalStrut(20));
      iBox.add(address);
      iBox.setBorder(BorderFactory.createTitledBorder("Information"));
      
      holder = Box.new(BoxLayout.Y_AXIS);
      holder.add(GraphZoomScrollPane.new(vv));
      holder.add(Box.createVerticalStrut(8));
      holder.add(iBox);
      holder.add(Box.createVerticalStrut(8));
      holder.add(controls);
      return holder
    end
end

#puts "loading..."
#
#x = ReplicaDemoClient.new "Replica Framework Demo Client"
#x.display(true)

puts "Replica Framework Demo Ruby client script loaded"