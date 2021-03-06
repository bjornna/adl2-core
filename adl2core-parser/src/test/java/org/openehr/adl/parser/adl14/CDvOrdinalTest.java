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
import org.openehr.jaxb.am.ArchetypeConstraint;
import org.openehr.jaxb.am.CDvOrdinal;
import org.openehr.jaxb.rm.DvOrdinal;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.openehr.adl.rm.RmObjectFactory.*;
import static org.openehr.adl.util.TestUtils.assertDvOrdinal;

/**
 * Test case tests parsing of domain type constraints extension.
 *
 * @author Rong Chen
 * @version 1.0
 */

public class CDvOrdinalTest extends ParserTestBase {


    /**
     * The fixture set up called before every test method.
     */
    @BeforeClass
    protected void setUp() throws Exception {
        archetype = parseArchetype("adl14/adl-test-entry.c_dv_ordinal.test.adl");
    }

    /**
     * The fixture clean up called after every test method.
     */
    @AfterTest
    protected void cleanup() throws Exception {
        node = null;
    }

    @Test
    public void testCDvOrdinalWithoutAssumedValue() throws Exception {
        node = AmQuery.find(archetype, "/types[at0001]/items[at10001]/value");
        String[] codes = {
                "at0003.0", "at0003.1", "at0003.2", "at0003.3", "at0003.4"
        };
        String terminology = "local";

        assertNull("unexpected assumed value", ((CDvOrdinal) node).getAssumedValue());

        assertCDvOrdinal(node, terminology, codes, null);
    }

    @Test
    public void testCDvOrdinalWithAssumedValue() throws Exception {
        node = AmQuery.find(archetype, "/types[at0001]/items[at10002]/value");
        String[] codes = {
                "at0003.0", "at0003.1", "at0003.2", "at0003.3", "at0003.4"
        };
        String terminology = "local";
        DvOrdinal assumed = newDvOrdinal(0, newCodePhrase(newTerminologyId(terminology), codes[0]));

        assertNotNull("expected to have assumed value", ((CDvOrdinal) node).getAssumedValue());

        assertCDvOrdinal(node, terminology, codes, assumed);
    }

    @Test
    public void testEmptyCDvOrdinal() throws Exception {
        node = AmQuery.find(archetype, "/types[at0001]/items[at10003]/value");
        assertTrue("CDvOrdinal expected", node instanceof CDvOrdinal);
        CDvOrdinal cordinal = (CDvOrdinal) node;
        assertTrue("list should be empty on unconstrained node", cordinal.getList().isEmpty());
    }

    private void assertCDvOrdinal(ArchetypeConstraint node, String terminoloy,
            String[] codes, DvOrdinal assumedValue) {

        assertTrue("CDvOrdinal expected", node instanceof CDvOrdinal);
        CDvOrdinal cordinal = (CDvOrdinal) node;

        List codeList = Arrays.asList(codes);
        List<DvOrdinal> list = cordinal.getList();
        assertEquals("codes.size", codes.length, list.size());
        for (DvOrdinal ordinal : list) {
            assertEquals("terminology", "local",
                    ordinal.getSymbol().getDefiningCode().getTerminologyId().getValue());
            assertTrue("code missing",
                    codeList.contains(ordinal.getSymbol().getDefiningCode().getCodeString()));
        }
        assertDvOrdinal(assumedValue, cordinal.getAssumedValue());
    }

    private Archetype archetype;
    private ArchetypeConstraint node;
}
