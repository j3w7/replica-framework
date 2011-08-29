package org.semanticweb.owlapi.apibinding;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

import java.io.File;

import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.*;
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
 * Date: 17-Jan-2010
 */
public class TestFactory {

    public static void main(String[] args) {

        try {
            PrefixManager pm = new DefaultPrefixManager("http://www.sematicweb.org/myOnt#");
            OWLIndividual matt = NamedIndividual("Matt", pm);
            OWLIndividual peter = NamedIndividual("Peter", pm);
            OWLClass person = Class("Person", pm);
            OWLObjectProperty hasFather = ObjectProperty("hasFather", pm);
            OWLObjectProperty hasMother = ObjectProperty("hasMother", pm);
            OWLNamedIndividual jean = NamedIndividual("Jean", pm);

            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
            OWLOntology ont = Ontology(
                    manager,
                    ClassAssertion(person, matt),
                    ClassAssertion(person, peter),
                    ObjectPropertyAssertion(hasFather, matt, peter),
                    ObjectPropertyAssertion(hasMother, matt, jean),
                    ClassAssertion(person, jean),
                    ObjectPropertyDomain(hasFather, person),
                    ObjectPropertyRange(hasFather, person),
                    ObjectPropertyDomain(hasMother, person),
                    ObjectPropertyDomain(hasMother, person)
            );
            manager.saveOntology(ont, System.out);
            manager.saveOntology(ont, IRI.create("file:/tmp/out.txt"));
        }
        catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        }
        catch (OWLOntologyStorageException e) {
            e.printStackTrace();
        }
    }
}
