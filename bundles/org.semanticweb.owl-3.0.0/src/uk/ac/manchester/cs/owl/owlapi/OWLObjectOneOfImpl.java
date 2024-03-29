package uk.ac.manchester.cs.owl.owlapi;

import org.semanticweb.owlapi.model.*;

import java.util.Collections;
import java.util.HashSet;
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
 * Bio-Health Informatics Group<br>
 * Date: 26-Oct-2006<br><br>
 */
public class OWLObjectOneOfImpl extends OWLAnonymousClassExpressionImpl implements OWLObjectOneOf {

    private Set<OWLIndividual> values;


    public OWLObjectOneOfImpl(OWLDataFactory dataFactory, Set<? extends OWLIndividual> values) {
        super(dataFactory);
        this.values = new HashSet<OWLIndividual>(values);
    }


    /**
     * Gets the class expression type for this class expression
     * @return The class expression type
     */
    public ClassExpressionType getClassExpressionType() {
        return ClassExpressionType.OBJECT_ONE_OF;
    }


    public Set<OWLIndividual> getIndividuals() {
        return Collections.unmodifiableSet(values);
    }


    public boolean isClassExpressionLiteral() {
        return false;
    }


    public OWLClassExpression asObjectUnionOf() {
        if (values.size() == 1) {
            return this;
        } else {
            Set<OWLClassExpression> ops = new HashSet<OWLClassExpression>();
            for (OWLIndividual ind : values) {
                ops.add(getOWLDataFactory().getOWLObjectOneOf(ind));
            }
            return getOWLDataFactory().getOWLObjectUnionOf(ops);
        }
    }


    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            if (!(obj instanceof OWLObjectOneOf)) {
                return false;
            }
            return ((OWLObjectOneOf) obj).getIndividuals().equals(values);
        }
        return false;
    }


    public void accept(OWLClassExpressionVisitor visitor) {
        visitor.visit(this);
    }


    public void accept(OWLObjectVisitor visitor) {
        visitor.visit(this);
    }

    public <O> O accept(OWLClassExpressionVisitorEx<O> visitor) {
        return visitor.visit(this);
    }


    public <O> O accept(OWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }


    protected int compareObjectOfSameType(OWLObject object) {
        return compareSets(values, ((OWLObjectOneOf) object).getIndividuals());
    }
}
