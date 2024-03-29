package org.semanticweb.owlapi.model;

import java.net.URI;
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
 * Bio-Health Informatics Group
 * Date: 25-Oct-2006
 * <p/>
 * Represents an import statement in an ontology.
 */
public interface OWLImportsDeclaration extends Comparable<OWLImportsDeclaration> {

    /**
     * Gets the import IRI
     * @return The import IRI that points to the ontology to be imported.  The imported
     * ontology should have this IRI as its IRI (although this isn't enforced).
     */
    IRI getIRI();

    /**
     * A convenience method to obtain the import IRI as a URI
     * @return The import IRI as a URI
     */
    URI getURI();
}
