package org.coode.owlapi.rdfxml.parser;

import org.semanticweb.owlapi.model.*;
/*
 * Copyright (C) 2008, University of Manchester
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
 * Author: Matthew Horridge<br> The University Of Manchester<br> Information Management Group<br> Date:
 * 05-Nov-2008<br><br>
 */
public class SKOSDataTripleHandler extends AbstractLiteralTripleHandler {

    private IRI predicate;


    public SKOSDataTripleHandler(OWLRDFConsumer consumer, IRI predicate) {
        super(consumer);
        this.predicate = predicate;
    }

    public void handleTriple(IRI subject, IRI predicate, OWLLiteral object) {
        OWLIndividual subj = getDataFactory().getOWLNamedIndividual(subject);
        OWLDataProperty prop = getDataFactory().getOWLDataProperty(predicate);
        addAxiom(getDataFactory().getOWLDataPropertyAssertionAxiom(prop, subj, object));
    }


    public boolean canHandle(IRI subject, IRI predicate, OWLLiteral object) {
        return predicate.equals(this.predicate);
    }


    public boolean canHandleStreaming(IRI subject, IRI predicate, OWLLiteral object) {
        return predicate.equals(this.predicate);
    }
}
