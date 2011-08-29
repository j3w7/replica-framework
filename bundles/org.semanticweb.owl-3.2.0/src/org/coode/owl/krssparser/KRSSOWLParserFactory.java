package org.coode.owl.krssparser;

import org.semanticweb.owlapi.io.OWLParser;
import org.semanticweb.owlapi.io.OWLParserFactory;
import org.semanticweb.owlapi.model.OWLOntologyManager;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 15-Nov-2006<br><br>
 */
public class KRSSOWLParserFactory implements OWLParserFactory {
	  @SuppressWarnings("unused")
    public OWLParser createParser(OWLOntologyManager owlOntologyManager) {
        return new KRSSOWLParser();
    }
}
