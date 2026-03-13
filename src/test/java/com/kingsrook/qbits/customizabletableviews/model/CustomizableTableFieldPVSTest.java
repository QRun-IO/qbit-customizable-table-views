/*
 * QQQ - Low-code Application Framework for Engineers.
 * Copyright (C) 2021-2025.  Kingsrook, LLC
 * 651 N Broad St Ste 205 # 6917 | Middletown DE 19709 | United States
 * contact@kingsrook.com
 * https://github.com/Kingsrook/
 *
 * This program is free software: you can redistribute it and/or modify
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
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.kingsrook.qbits.customizabletableviews.model;


import java.util.List;
import java.util.Map;
import com.kingsrook.qbits.customizabletableviews.BaseTest;
import com.kingsrook.qqq.backend.core.actions.tables.InsertAction;
import com.kingsrook.qqq.backend.core.context.QContext;
import com.kingsrook.qqq.backend.core.exceptions.QException;
import com.kingsrook.qqq.backend.core.model.actions.tables.insert.InsertInput;
import com.kingsrook.qqq.backend.core.model.actions.values.SearchPossibleValueSourceInput;
import com.kingsrook.qqq.backend.core.model.metadata.fields.QFieldMetaData;
import com.kingsrook.qqq.backend.core.model.metadata.fields.QFieldType;
import com.kingsrook.qqq.backend.core.model.metadata.fields.QVirtualFieldMetaData;
import com.kingsrook.qqq.backend.core.model.metadata.possiblevalues.QPossibleValue;
import com.kingsrook.qqq.backend.core.model.metadata.tables.QTableMetaData;
import com.kingsrook.qqq.backend.core.model.metadata.tables.SectionFactory;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


/*******************************************************************************
 ** Unit test for CustomizableTableFieldPVS 
 *******************************************************************************/
class CustomizableTableFieldPVSTest extends BaseTest
{

   /*******************************************************************************
    **
    *******************************************************************************/
   @Test
   void test() throws QException
   {
      QTableMetaData testTable = new QTableMetaData()
         .withName("testTable")
         .withPrimaryKeyField("id")
         .withField(new QFieldMetaData("id", QFieldType.STRING).withIsEditable(false))
         .withField(new QFieldMetaData("hidden", QFieldType.STRING).withIsHidden(true))
         .withField(new QFieldMetaData("required", QFieldType.STRING).withIsRequired(true))
         .withField(new QFieldMetaData("optional", QFieldType.STRING).withLabel("Optional"))
         .withField(new QFieldMetaData("extra", QFieldType.STRING).withLabel("Extra"))
         .withSection(SectionFactory.defaultT1("id").withName("s0"))
         .withSection(SectionFactory.defaultT2("optional").withName("s1"));
      QContext.getQInstance().addTable(testTable);

      /////////////////////////////////////////////
      // mark table as customizable, with a view //
      /////////////////////////////////////////////
      new InsertAction().execute(new InsertInput(CustomizableTable.TABLE_NAME).withRecordEntities(List.of(
         new CustomizableTable().withTableName(testTable.getName()).withIsActive(true))));

      Integer tableViewId = 17;
      new InsertAction().execute(new InsertInput(TableView.TABLE_NAME).withRecordEntities(List.of(
         new TableView().withId(tableViewId).withCustomizableTableId(1).withName("a"))));

      List<QPossibleValue<String>> possibleValues = new CustomizableTableFieldPVS().search(new SearchPossibleValueSourceInput().withOtherValues(Map.of("tableViewId", tableViewId)));
      /////////////////////////////////////////////////////////
      // note primary key, required, hidden all not included //
      /////////////////////////////////////////////////////////
      assertEquals(2, possibleValues.size());
      assertEquals("testTable.extra", possibleValues.get(0).getId());
      assertEquals("Extra", possibleValues.get(0).getLabel());
      assertEquals("testTable.optional", possibleValues.get(1).getId());
      assertEquals("Optional", possibleValues.get(1).getLabel());
   }



   /*******************************************************************************
    **
    *******************************************************************************/
   @Test
   void testWithVirtualFields() throws QException
   {
      QTableMetaData testTable = new QTableMetaData()
         .withName("testTable")
         .withPrimaryKeyField("id")
         .withField(new QFieldMetaData("id", QFieldType.STRING).withIsEditable(false))
         .withField(new QFieldMetaData("optional", QFieldType.STRING).withLabel("Optional"))
         .withVirtualField(new QVirtualFieldMetaData("computed", QFieldType.STRING).withLabel("Computed"))
         .withVirtualField(new QVirtualFieldMetaData("hiddenVirtual", QFieldType.STRING).withIsHidden(true).withLabel("Hidden Virtual"))
         .withSection(SectionFactory.defaultT1("id").withName("s0"))
         .withSection(SectionFactory.defaultT2("optional").withName("s1"));
      QContext.getQInstance().addTable(testTable);

      new InsertAction().execute(new InsertInput(CustomizableTable.TABLE_NAME).withRecordEntities(List.of(
         new CustomizableTable().withTableName(testTable.getName()).withIsActive(true))));

      Integer tableViewId = 17;
      new InsertAction().execute(new InsertInput(TableView.TABLE_NAME).withRecordEntities(List.of(
         new TableView().withId(tableViewId).withCustomizableTableId(1).withName("a"))));

      /////////////////////////////////////////////////////////////////////
      // search should include the non-hidden virtual field in results  //
      /////////////////////////////////////////////////////////////////////
      List<QPossibleValue<String>> possibleValues = new CustomizableTableFieldPVS().search(new SearchPossibleValueSourceInput().withOtherValues(Map.of("tableViewId", tableViewId)));
      assertEquals(2, possibleValues.size());
      assertThat(possibleValues)
         .anyMatch(pv1 -> "testTable.computed".equals(pv1.getId()) && "Computed".equals(pv1.getLabel()))
         .anyMatch(pv1 -> "testTable.optional".equals(pv1.getId()) && "Optional".equals(pv1.getLabel()));

      ///////////////////////////////////////////////////////////////////
      // getPossibleValue should resolve a virtual field by table.name //
      ///////////////////////////////////////////////////////////////////
      QPossibleValue<String> pv = new CustomizableTableFieldPVS().getPossibleValue("testTable.computed");
      assertNotNull(pv);
      assertEquals("testTable.computed", pv.getId());
      assertEquals("Computed", pv.getLabel());

      /////////////////////////////////////////////////////
      // getPossibleValue for a normal field still works //
      /////////////////////////////////////////////////////
      QPossibleValue<String> pvNormal = new CustomizableTableFieldPVS().getPossibleValue("testTable.optional");
      assertNotNull(pvNormal);
      assertEquals("Optional", pvNormal.getLabel());

      /////////////////////////////////////////////////////////
      // getPossibleValue for a nonexistent field returns null //
      /////////////////////////////////////////////////////////
      assertNull(new CustomizableTableFieldPVS().getPossibleValue("testTable.doesNotExist"));
   }

}