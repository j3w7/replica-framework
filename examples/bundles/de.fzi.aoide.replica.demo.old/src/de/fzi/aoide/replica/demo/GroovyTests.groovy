package de.fzi.aoide.replica.demo

smth = new Something()
smth.setSmth "jausen"
c = new RegexClass()
c.method smth

class RegexClass {
	
	def regex = ~"/^[a-z]+&/"
	def clos = { msg -> println "message for you: "+msg }
	
	Something method(args) {
		args.each { i -> clos i }
	}
	
}

class Something {
	def smth
}