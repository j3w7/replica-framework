# 
# awt.rb
# 
# Created on Oct 6, 2007, 12:35:17 AM
# Author: Teera Kanokkanjanarat (http://blogs.sun.com/teera)

# Bundle Java AWT and AWT event packages for ease of reference
 
require 'java'

module Awt
  include_package 'java.awt'
  include_package 'java.awt.event'
end
