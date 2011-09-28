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

package de.fzi.aoide.replica.app.client;

import org.eclipse.ecf.core.identity.IDCreateException;
import org.eclipse.ecf.core.sharedobject.SharedObjectAddException;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import de.fzi.aoide.replica.app.ApplicationContext;

/**
 * Implements an Eclipse Application. Used for testing and demonstration purposes.
 * The demo code is an ancient relict from the early days of this project
 * and is kept as a memory of the good old days :)
 * 
 * @author Jan Novacek novacek@fzi.de
 * @version 1.0, 13.09.2010
 *
 */
public class ClientEclipseImpl extends ClientImpl 
		implements Client, IApplication {

	protected ClientEclipseImpl(ApplicationContext context) {
		super(context);
	}

	@Override
	public Object start(IApplicationContext context) throws Exception {
		super.start();
		runDemoCode();
		waitUntilShutdown();
		return IApplication.EXIT_OK;
	}
	
	private void runDemoCode() throws OWLOntologyCreationException, 
			SharedObjectAddException, IDCreateException, InterruptedException {
//		String clientName = ""; // was once set as commandline parameter
//		
//		ID sharedOntoID = IDFactory.getDefault().createStringID("SharedOntoID");
//		
//		if("client1".equals(clientName)) {
//			/*
//			 * create a simple test ontology and add it as shared ontology
//			 */
//			Thread.sleep(20000);
//			System.out.println("Adding shared ontology");
//			ISharedObjectManager soManager = getSharedObjectContainer().getSharedObjectManager();
//			
//			/*
//			 * SharedOWLOntologyImpl creation (with OWLOntologyImpl Constructor parameters)
//			 */
//			OWLOntologyManager man = OWLManager.createOWLOntologyManager();
//			OWLOntologyID id = new OWLOntologyID(IRI.create("http://myOntology.org/"));
//			OWLReplicaOntologyImpl sharedOnto = new OWLReplicaOntologyImpl(man, id);
//			
//			soManager.addSharedObject(sharedOntoID, sharedOnto, null);
//			System.out.println("Added shared onto: "+sharedOnto+", ontologyID="+sharedOnto.getOntologyID().getOntologyIRI());
//			
//			Thread.sleep(10000);
//			System.out.println("------\nmy shared onto is now: "+sharedOnto+"\naxioms="+sharedOnto.getAxioms());
//		} else if("client2".equals(clientName)) {
//			/*
//			 * try to receive the shared ontology object
//			 */
//			Thread.sleep(10000);
//			ISharedObjectManager soManager = getSharedObjectContainer().getSharedObjectManager();
//			OWLReplicaOntologyImpl sharedOnto = (OWLReplicaOntologyImpl) soManager.getSharedObject(sharedOntoID);
//			System.out.println("Got shared onto: "+sharedOnto+", ontologyID="+sharedOnto.getOntologyID().getOntologyIRI()+"\nwill add an axiom...");
//			
//			OWLDataFactory fac = sharedOnto.getOWLOntologyManager().getOWLDataFactory();
//			OWLClass a = fac.getOWLClass(IRI.create("A"));
//			OWLClass b = fac.getOWLClass(IRI.create("B"));
//			OWLAxiom axiom = fac.getOWLSubClassOfAxiom(a, b);
//			System.out.println("OWLSubClassOfAxiom="+axiom);
//			sharedOnto.applyChange(new AddAxiom(sharedOnto, axiom));
//			
//			Thread.sleep(10000);
//			System.out.println("------\nmy shared onto is now: "+sharedOnto+"\naxioms="+sharedOnto.getAxioms());
//		}
	}

}
