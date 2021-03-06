/*
 * ADL2-core
 * Copyright (c) 2013-2014 Marand d.o.o. (www.marand.com)
 *
 * This file is part of ADL2-core.
 *
 * ADL2-core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.openehr.adl.parser.adl14;

import org.openehr.adl.ParserTestBase;
import org.openehr.adl.am.AmQuery;
import org.openehr.jaxb.am.Archetype;
import org.openehr.jaxb.am.CAttribute;
import org.openehr.jaxb.am.CComplexObject;
import org.openehr.jaxb.am.CDvOrdinal;
import org.testng.annotations.Test;

import static org.fest.assertions.Assertions.assertThat;

public class MixedNodeTypesTest extends ParserTestBase {

    @Test
    public void testMixedNodeTypes() throws Exception {

        Archetype archetype = parseArchetype("adl14/adl-test-entry.mixed_node_types.draft.adl");
        CComplexObject items2 = AmQuery.get(archetype, "/items2");

        assertThat(items2.getAttributes()).hasSize(1);

        CAttribute attr = items2.getAttributes().get(0);
        assertThat(attr.getChildren()).hasSize(2);

        CComplexObject first = (CComplexObject) attr.getChildren().get(0);
        assertCComplexObject(first, "DV_CODED_TEXT", null, null, 1);
        assertThat(first.getAttributes().get(0).getRmAttributeName()).isEqualTo("defining_code");

        CDvOrdinal second = (CDvOrdinal) attr.getChildren().get(1);
        assertCObject(second, "DV_ORDINAL", null, null);
        assertThat(second.getList().get(0).getSymbol().getValue()).isEqualTo("at0002");
        assertThat(second.getList().get(1).getSymbol().getValue()).isEqualTo("at0003");

    }
}
