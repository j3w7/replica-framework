package org.semanticweb.owlapi.model;

import java.io.Serializable;
import java.util.Set;

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
 * Date: 24-Oct-2006
 */
public interface OWLObject extends Comparable<OWLObject>, Serializable {


    /**
     * Gets the signature of this object
     * @return A set of entities that correspond to the
     *         signature of this object.
     */
    Set<OWLEntity> getSignature();


    /**
     * A convenience method that obtains the classes
     * that are in the signature of this object
     * @return A set containing the classes that are in the signature
     *         of this object.
     */
    Set<OWLClass> getClassesInSignature();

    /**
     * A convenience method that obtains the data properties
     * that are in the signature of this object
     * @return A set containing the data properties that are in the signature
     *         of this object.
     */
    Set<OWLDataProperty> getDataPropertiesInSignature();

    /**
     * A convenience method that obtains the object properties
     * that are in the signature of this object
     * @return A set containing the object properties that are in the signature
     *         of this object.
     */
    Set<OWLObjectProperty> getObjectPropertiesInSignature();


    /**
     * A convenience method that obtains the individuals
     * that are in the signature of this object
     * @return A set containing the individuals that are in the signature
     *         of this object.
     */
    Set<OWLNamedIndividual> getIndividualsInSignature();

    /**
     * A convenience method that obtains the datatypes
     * that are in the signature of this object
     * @return A set containing the datatypes that are in the signature
     *         of this object.
     */
    Set<OWLDatatype> getDatatypesInSignature();

    public void accept(OWLObjectVisitor visitor);

    <O> O accept(OWLObjectVisitorEx<O> visitor);

    
}
