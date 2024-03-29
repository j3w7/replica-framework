package org.semanticweb.owlapi.util;

import org.semanticweb.owlapi.model.OWLEntity;
/*
 * Copyright (C) 2007, University of Manchester
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
 * Date: 18-Apr-2007<br><br>
 *
 * A short form provider produces renderings of entities.  These
 * renderings are strings which in general can be used for display
 * and serialisation purposes.  A given entity only has one short
 * form for a given short form provider.  However, a short form may
 * map to multiple enntities for a given short form provider.  In
 * other words, for a given short form provider the mapping from
 * entity to short form is functional, but is not inverse functional i.e.
 * an injective mapping.
 */
public interface ShortFormProvider {

    /**
     * Gets the short form for the specified entity.
     * @param entity The entity.
     * @return A string which represents a short rendering
     * of the speicified entity.
     */
    String getShortForm(OWLEntity entity);


    /**
     * Disposes of the short form proivider.  This frees any
     * resources and clears any caches.
     */
    void dispose();
}
