# 
# swing_module.rb
# 
# Created on Oct 4, 2007, 10:43:11 PM
# Author: Teera Kanokkanjanarat (http://blogs.sun.com/teera)

# Bundle Swing API and Swing Layout Extension (require swing-layout-1.0.2.jar)
 
require 'java'
require 'lib/swing-layout-1.0.2.jar'

module Swing
  include_package 'javax.swing'
  include_package 'javax.swing.event'
  include_class 'org.jdesktop.layout.GroupLayout'
  include_class 'org.jdesktop.layout.LayoutStyle'
  JOptionPane = javax.swing.JOptionPane
end