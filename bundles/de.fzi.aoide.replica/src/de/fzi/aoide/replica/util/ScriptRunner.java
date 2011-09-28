/*
   Copyright 2011 Jan Novacek

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package de.fzi.aoide.replica.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;

/**
 * This is the runner for the Replica Framework Demo client. It starts the JRuby
 * scripting engine and runs the demo client script.
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version 0.1, 22.06.2011
 */
public class ScriptRunner {

	private ScriptEngine engine;

//	TODO implement cli
//	/**
//	 * Meant to run a script from the command line.
//	 * 
//	 * @param args
//	 *            First argument expected to be the path to the script file.
//	 * @throws ScriptException
//	 * @throws FileNotFoundException
//	 */
//	public static void main(String[] args) throws ScriptException,
//			FileNotFoundException {
//		ScriptRunner scriptRunner = new ScriptRunner(new JRubyEngineFactory());
//		scriptRunner.run(new File(args[0]));
//	}

	public ScriptRunner(ScriptEngineFactory factory) throws ScriptException,
			FileNotFoundException {
		setScriptEngineFactory(factory);
	}
	
	public void setScriptEngineFactory(ScriptEngineFactory factory) {
		engine = factory.getScriptEngine();
	}

	public void run(File file) throws ScriptException, FileNotFoundException {
		BufferedInputStream bfIs = new BufferedInputStream(new FileInputStream(
				file));
		run(new InputStreamReader(bfIs));
	}

	public void run(InputStream is) throws ScriptException {
		BufferedInputStream bfIs = new BufferedInputStream(is);
		run(new InputStreamReader(bfIs));
	}

	public void run(Reader reader) throws ScriptException {
		checkEngine();
		engine.eval(reader);
	}

	public void run(String source) throws ScriptException {
		checkEngine();
		engine.eval(source);
	}

	private void checkEngine() {
		if (engine == null) {
			throw new UnsupportedOperationException("Could not get engine");
		}
	}

}
