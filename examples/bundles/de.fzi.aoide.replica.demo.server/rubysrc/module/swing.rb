require 'java'
require 'lib/swing-layout-1.0.2.jar'

module Swing
  include_package 'javax.swing'
  include_package 'javax.swing.event'
  include_class 'org.jdesktop.layout.GroupLayout'
  include_class 'org.jdesktop.layout.LayoutStyle'
  JOptionPane = javax.swing.JOptionPane
end
