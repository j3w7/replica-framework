package org.coode.owlapi.rdfxml.parser;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

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
 * Date: 11-Dec-2006<br><br>
 * <p/>
 * A handler for top level intersection classes.
 */
public class TPIntersectionOfHandler extends AbstractNamedEquivalentClassAxiomHandler {

    public TPIntersectionOfHandler(OWLRDFConsumer consumer) {
        super(consumer, OWLRDFVocabulary.OWL_INTERSECTION_OF.getIRI());
    }


    @Override
	protected OWLClassExpression translateEquivalentClass(IRI mainNode) {
        return getDataFactory().getOWLObjectIntersectionOf(getConsumer().translateToClassExpressionSet(mainNode));
    }

    @Override
    public boolean canHandleStreaming(IRI subject, IRI predicate, IRI object) {
        if(getConsumer().isClassExpression(subject)) {
            getConsumer().addClassExpression(object, false);
        }
        else if (getConsumer().isClassExpression(object)) {
            getConsumer().addClassExpression(subject, false);
        }
        else if (getConsumer().isDataRange(subject)) {
            getConsumer().addDataRange(object, false);
        }
        else if( getConsumer().isDataRange(object)) {
            getConsumer().addDataRange(subject, false);
        }
        return super.canHandleStreaming(subject, predicate, object);
    }
}
