package uk.ac.manchester.cs.owl.owlapi;

import org.semanticweb.owlapi.io.DefaultOntologyFormat;
import org.semanticweb.owlapi.io.OWLOntologyDocumentSource;
import org.semanticweb.owlapi.model.*;
/*
 * Copyright (C) 2006, University of Manchester
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
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 15-Nov-2006<br><br>
 */
public class EmptyInMemOWLOntologyFactory extends AbstractInMemOWLOntologyFactory {

    public OWLOntology loadOWLOntology(OWLOntologyDocumentSource documentSource, OWLOntologyCreationHandler mediator) throws OWLOntologyCreationException {
        throw new OWLRuntimeException(new UnsupportedOperationException("Cannot load OWL ontologies."));
    }


    public OWLOntology createOWLOntology(OWLOntologyID ontologyID, IRI documentIRI,
                                         OWLOntologyCreationHandler handler) throws OWLOntologyCreationException {
        OWLOntology ont = super.createOWLOntology(ontologyID, documentIRI, handler);
        handler.setOntologyFormat(ont, new DefaultOntologyFormat());
        return ont;
    }


    public boolean canLoad(OWLOntologyDocumentSource documentSource) {
        return false;
    }
}
