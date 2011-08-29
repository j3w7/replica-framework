package org.semanticweb.owlapi.reasoner.structural;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.*;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.*;

import java.net.URI;
/*
 * Copyright (C) 2009, University of Manchester
 *
 * Modifications to the initial code base are copyright of their
 * respective authors, or their employers as appropriate.  Authorship
 * of the modifications may be determined from the ChangeLog placed at
 * the end of this file.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

/**
 * Author: Matthew Horridge<br>
 * The University of Manchester<br>
 * Information Management Group<br>
 * Date: 14-Dec-2009
 */
public class TestStructuralReasoner {

    public static void main(String[] args) {
        try {
            OWLOntologyManager man = OWLManager.createOWLOntologyManager();

            NullReasonerProgressMonitor progressMonitor = new NullReasonerProgressMonitor();
            
//            OWLOntology ont = man.loadOntologyFromOntologyDocument(IRI.create("http://www.co-ode.org/ontologies/pizza/pizza.owl"));
//            OWLOntology ont = man.loadOntologyFromOntologyDocument(IRI.create("file:/Users/matthewhorridge/Desktop/yatbu.zip"));
            OWLOntology ont = man.loadOntologyFromOntologyDocument(IRI.create("file:/Users/matthewhorridge/ontologies/badthesaurus/BadThesaurus.owl.zip"));
            System.out.println("Loaded: " + ont);
            SimpleConfiguration configuration = new SimpleConfiguration(new ConsoleProgressMonitor());
            StructuralReasoner reasoner = new StructuralReasoner(ont, configuration, BufferingMode.NON_BUFFERING);
            long t0 = System.currentTimeMillis();
            reasoner.prepareReasoner();
            long t1 = System.currentTimeMillis();
            System.out.println("Time to prepare reasoner: " + (t1 - t0));
//            reasoner.dumpClassHierarchy(false);
//            reasoner.dumpObjectPropertyHierarchy(false);
//            reasoner.dumpDataPropertyHierarchy(false);
            OWLAxiom ax = Declaration(Class(IRI.create("http://www.co-ode.org/ontologies/pizza/pizza.owl#X")));
            DefaultPrefixManager pm = new DefaultPrefixManager("http://www.co-ode.org/ontologies/pizza/pizza.owl#");
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            man.addAxiom(ont,ax);
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            man.addAxiom(ont, SubClassOf(Class("X", pm), Class("Y", pm)));
            man.addAxiom(ont, SubClassOf(Class("Y", pm), Class("X", pm)));
            man.addAxiom(ont, SubClassOf(Class("E", pm), Class("F", pm)));
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");


//            for(OWLClass cls : ont.getClassesInSignature()) {
//                System.out.println("---------------------");
//                System.out.println(cls);
//                for(Node<OWLClass> nd : reasoner.getDisjointClasses(cls, false)) {
//                    System.out.println("\t" + nd);
//                }
//            }
//            reasoner.dumpClassHierarchy(false);
        }
        catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        }
    }
}
