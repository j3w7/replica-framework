package org.coode.owlapi.owlxmlparser;

import org.semanticweb.owlapi.model.OWLClassExpression;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 14-Dec-2006<br><br>
 */
public class OWLDataMinCardinalityElementHandler extends AbstractDataCardinalityRestrictionElementHandler {

    public OWLDataMinCardinalityElementHandler(OWLXMLParserHandler handler) {
        super(handler);
    }


    @Override
	protected OWLClassExpression createRestriction() {
        return getOWLDataFactory().getOWLDataMinCardinality(getCardinality(), getProperty(), getFiller());
    }
}
