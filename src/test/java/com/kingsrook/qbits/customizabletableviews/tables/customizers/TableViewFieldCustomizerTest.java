/*
 * QQQ - Low-code Application Framework for Engineers.
 * Copyright (C) 2021-2025.  Kingsrook, LLC
 * 651 N Broad St Ste 205 # 6917 | Middletown DE 19709 | United States
 * contact@kingsrook.com
 * https://github.com/Kingsrook/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kingsrook.qbits.customizabletableviews.tables.customizers;


import java.util.List;
import java.util.Optional;
import com.kingsrook.qbits.customizabletableviews.BaseTest;
import com.kingsrook.qbits.customizabletableviews.model.FieldAccessLevel;
import com.kingsrook.qbits.customizabletableviews.model.TableViewField;
import com.kingsrook.qqq.backend.core.context.QContext;
import com.kingsrook.qqq.backend.core.exceptions.QException;
import com.kingsrook.qqq.backend.core.model.data.QRecord;
import com.kingsrook.qqq.backend.core.model.metadata.fields.QFieldMetaData;
import com.kingsrook.qqq.backend.core.model.metadata.fields.QFieldType;
import com.kingsrook.qqq.backend.core.model.metadata.fields.QVirtualFieldMetaData;
import com.kingsrook.qqq.backend.core.model.metadata.tables.QTableMetaData;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


/*******************************************************************************
 ** Unit test for TableViewFieldCustomizer 
 *******************************************************************************/
class TableViewFieldCustomizerTest extends BaseTest
{

   /*******************************************************************************
    **
    *******************************************************************************/
   @Test
   void test() throws QException
   {
      QContext.getQInstance().addTable(new QTableMetaData()
         .withName("testTable")
         .withField(new QFieldMetaData("secret", QFieldType.STRING).withIsHidden(true))
         .withField(new QFieldMetaData("mandatory", QFieldType.STRING).withIsRequired(true))
         .withField(new QFieldMetaData("optional", QFieldType.STRING)));

      assertError(new TableViewField().withFieldName("testTable.notAField").withAccessLevel(FieldAccessLevel.READ_ONLY));
      assertError(new TableViewField().withFieldName("testTable.notAField").withAccessLevel(FieldAccessLevel.EDITABLE_REQUIRED));
      assertError(new TableViewField().withFieldName("testTable.notAField").withAccessLevel(FieldAccessLevel.EDITABLE_OPTIONAL));

      assertError(new TableViewField().withFieldName("testTable.secret").withAccessLevel(FieldAccessLevel.READ_ONLY));
      assertError(new TableViewField().withFieldName("testTable.secret").withAccessLevel(FieldAccessLevel.EDITABLE_REQUIRED));
      assertError(new TableViewField().withFieldName("testTable.secret").withAccessLevel(FieldAccessLevel.EDITABLE_OPTIONAL));

      assertError(new TableViewField().withFieldName("testTable.mandatory").withAccessLevel(FieldAccessLevel.READ_ONLY));
      assertNoError(new TableViewField().withFieldName("testTable.mandatory").withAccessLevel(FieldAccessLevel.EDITABLE_REQUIRED));
      assertError(new TableViewField().withFieldName("testTable.mandatory").withAccessLevel(FieldAccessLevel.EDITABLE_OPTIONAL));

      assertNoError(new TableViewField().withFieldName("testTable.optional").withAccessLevel(FieldAccessLevel.READ_ONLY));
      assertNoError(new TableViewField().withFieldName("testTable.optional").withAccessLevel(FieldAccessLevel.EDITABLE_REQUIRED));
      assertNoError(new TableViewField().withFieldName("testTable.optional").withAccessLevel(FieldAccessLevel.EDITABLE_OPTIONAL));
   }



   /*******************************************************************************
    **
    *******************************************************************************/
   @Test
   void testVirtualField() throws QException
   {
      QContext.getQInstance().addTable(new QTableMetaData()
         .withName("testTable2")
         .withField(new QFieldMetaData("id", QFieldType.STRING))
         .withVirtualField(new QVirtualFieldMetaData("virtualField", QFieldType.STRING).withLabel("Virtual")));

      //////////////////////////////////////////////////////////////////////////////
      // virtual fields are treated as read-only, so only READ_ONLY should pass. //
      //////////////////////////////////////////////////////////////////////////////
      assertNoError(new TableViewField().withFieldName("testTable2.virtualField").withAccessLevel(FieldAccessLevel.READ_ONLY));
      assertError(new TableViewField().withFieldName("testTable2.virtualField").withAccessLevel(FieldAccessLevel.EDITABLE_REQUIRED));
      assertError(new TableViewField().withFieldName("testTable2.virtualField").withAccessLevel(FieldAccessLevel.EDITABLE_OPTIONAL));
   }



   /*******************************************************************************
    ** a sparse update (e.g., only accessLevel set) must validate against the
    ** fieldName from the old record.
    *******************************************************************************/
   @Test
   void testSparseUpdateUsesOldRecordValues() throws QException
   {
      QContext.getQInstance().addTable(new QTableMetaData()
         .withName("testTable3")
         .withField(new QFieldMetaData("mandatory", QFieldType.STRING).withIsRequired(true))
         .withField(new QFieldMetaData("optional", QFieldType.STRING)));

      Optional<List<QRecord>> oldRecordList = Optional.of(List.of(
         new TableViewField().withId(1).withFieldName("testTable3.mandatory").withAccessLevel(FieldAccessLevel.EDITABLE_REQUIRED).toQRecord(),
         new TableViewField().withId(2).withFieldName("testTable3.optional").withAccessLevel(FieldAccessLevel.EDITABLE_OPTIONAL).toQRecord()));

      QRecord mandatoryToReadOnly = new QRecord().withValue("id", 1).withValue("accessLevel", FieldAccessLevel.READ_ONLY.getId());
      QRecord optionalToReadOnly  = new QRecord().withValue("id", 2).withValue("accessLevel", FieldAccessLevel.READ_ONLY.getId());
      new TableViewFieldCustomizer().preInsertOrUpdate(null, List.of(mandatoryToReadOnly, optionalToReadOnly), false, oldRecordList);

      assertThat(mandatoryToReadOnly.getErrors()).hasSizeGreaterThan(0);
      assertThat(optionalToReadOnly.getErrors()).isNullOrEmpty();
   }



   /***************************************************************************
    *
    ***************************************************************************/
   private void assertNoError(TableViewField tableViewField) throws QException
   {
      QRecord qRecord = tableViewField.toQRecord();
      new TableViewFieldCustomizer().preInsertOrUpdate(null, List.of(qRecord), false, Optional.empty());
      assertThat(qRecord.getErrors()).isNullOrEmpty();
   }



   /***************************************************************************
    *
    ***************************************************************************/
   private void assertError(TableViewField tableViewField) throws QException
   {
      QRecord qRecord = tableViewField.toQRecord();
      new TableViewFieldCustomizer().preInsertOrUpdate(null, List.of(qRecord), false, Optional.empty());
      assertThat(qRecord.getErrors()).hasSizeGreaterThan(0);
   }

}