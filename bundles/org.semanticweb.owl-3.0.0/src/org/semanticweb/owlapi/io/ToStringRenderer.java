package org.semanticweb.owlapi.io;

import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.util.SimpleRenderer;
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
 * Date: 14-Jan-2008<br><br>
 *
 * A utility class which can be used by implementations to provide
 * a toString rendering of OWL API objects.  The idea is that this
 * is pluggable.
 */
public class ToStringRenderer {

    private static ToStringRenderer instance;

    private OWLObjectRenderer renderer;

    private ToStringRenderer() {
        renderer = new SimpleRenderer();
    }

    public static synchronized ToStringRenderer getInstance() {
        if(instance == null) {
            instance = new ToStringRenderer();
        }
        return instance;
    }

    public OWLObjectRenderer getRenderer() {
        return renderer;
    }

    public void setRenderer(OWLObjectRenderer renderer) {
        this.renderer = renderer;
    }

    public String getRendering(OWLObject object) {
        return renderer.render(object);
    }
}
