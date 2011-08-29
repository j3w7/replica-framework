package org.semanticweb.owlapi.util;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.io.UnparsableOntologyException;

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
 * Date: 07-Jul-2009
 */
public class TestDir {

    public static void main(String[] args) {
        try {
            UnparsableOntologyException.setIncludeStackTraceInMessage(true);
            OWLOntologyManager man = OWLManager.createOWLOntologyManager();
            long t0 = System.currentTimeMillis();
//            OWLOntology ont = man.loadOntologyFromOntologyDocument(URI.create("file:/Users/matthewhorridge/ontologies/Thesaurus.08.02d.owl.zip"));
//            OWLOntology ont = man.loadOntologyFromOntologyDocument(URI.create("file:/Users/matthewhorridge/Desktop/SNOMED-20090731.owl.zip"));
            OWLOntology ont = man.loadOntologyFromOntologyDocument(IRI.create("http://www.loa-cnr.it/ontologies/DUL.owl"));
            long t1 = System.currentTimeMillis();
            dumpMemStats();
            System.out.println("TIME: " + (t1 - t0));
            System.gc();
            System.gc();
            System.gc();
            dumpMemStats();


//            for(OWLAnnotationAssertionAxiom ax : ont.getAxioms(AxiomType.ANNOTATION_ASSERTION)) {
//                System.out.println(ax);
//            }
        }
        catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        }
    }

    private static void dumpMemStats() {
        Runtime r = Runtime.getRuntime();
        long total = r.totalMemory();
        long free = r.freeMemory();
        System.out.println("Mem: " + ((total - free) / (1024 * 1024)));
    }
}
