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

package com.kingsrook.qbits.customizabletableviews.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.kingsrook.qqq.backend.core.actions.tables.QueryAction;
import com.kingsrook.qqq.backend.core.actions.values.QCustomPossibleValueProvider;
import com.kingsrook.qqq.backend.core.context.QContext;
import com.kingsrook.qqq.backend.core.exceptions.QException;
import com.kingsrook.qqq.backend.core.model.actions.tables.query.QCriteriaOperator;
import com.kingsrook.qqq.backend.core.model.actions.tables.query.QQueryFilter;
import com.kingsrook.qqq.backend.core.model.actions.tables.query.QueryInput;
import com.kingsrook.qqq.backend.core.model.actions.tables.query.QueryJoin;
import com.kingsrook.qqq.backend.core.model.actions.values.SearchPossibleValueSourceInput;
import com.kingsrook.qqq.backend.core.model.data.QRecord;
import com.kingsrook.qqq.backend.core.model.metadata.MetaDataProducerInterface;
import com.kingsrook.qqq.backend.core.model.metadata.QInstance;
import com.kingsrook.qqq.backend.core.model.metadata.code.QCodeReference;
import com.kingsrook.qqq.backend.core.model.metadata.dashboard.QWidgetMetaDataInterface;
import com.kingsrook.qqq.backend.core.model.metadata.possiblevalues.QPossibleValue;
import com.kingsrook.qqq.backend.core.model.metadata.possiblevalues.QPossibleValueSource;
import com.kingsrook.qqq.backend.core.model.metadata.possiblevalues.QPossibleValueSourceType;
import com.kingsrook.qqq.backend.core.model.metadata.tables.QFieldSection;
import com.kingsrook.qqq.backend.core.model.metadata.tables.QTableMetaData;
import com.kingsrook.qqq.backend.core.utils.CollectionUtils;
import com.kingsrook.qqq.backend.core.utils.ValueUtils;


/*******************************************************************************
 ** Meta Data Producer custom value provider for CustomizableTableWidgetPVS
 *******************************************************************************/
public class CustomizableTableWidgetPVS implements QCustomPossibleValueProvider<String>, MetaDataProducerInterface<QPossibleValueSource>
{
   public static final String NAME = "CustomizableTableWidgetPVS";



   /*******************************************************************************
    **
    *******************************************************************************/
   @Override
   public QPossibleValueSource produce(QInstance qInstance) throws QException
   {
      return (new QPossibleValueSource()
         .withName(NAME)
         .withType(QPossibleValueSourceType.CUSTOM)
         .withCustomCodeReference(new QCodeReference(getClass())));
   }



   /***************************************************************************
    *
    ***************************************************************************/
   @Override
   public QPossibleValue<String> getPossibleValue(Serializable id)
   {
      String                   idString = ValueUtils.getValueAsString(id);
      QWidgetMetaDataInterface widget   = QContext.getQInstance().getWidget(idString);

      if(widget != null)
      {
         return (new QPossibleValue<>(idString, widget.getLabel()));
      }

      return null;
   }



   /***************************************************************************
    *
    ***************************************************************************/
   @Override
   public List<QPossibleValue<String>> search(SearchPossibleValueSourceInput searchPossibleValueSourceInput) throws QException
   {
      List<QPossibleValue<String>> allPossibleValues = new ArrayList<>();

      Integer tableViewId = ValueUtils.getValueAsInteger(CollectionUtils.nonNullMap(searchPossibleValueSourceInput.getOtherValues()).get("tableViewId"));
      if(tableViewId != null)
      {
         List<QRecord> tableRecords = new QueryAction().execute(new QueryInput(CustomizableTable.TABLE_NAME)
            .withFilter(new QQueryFilter().withCriteria(TableView.TABLE_NAME + ".id", QCriteriaOperator.EQUALS, tableViewId))
            .withQueryJoin(new QueryJoin(TableView.TABLE_NAME))).getRecords();

         if(CollectionUtils.nullSafeHasContents(tableRecords))
         {
            String         tableName = tableRecords.get(0).getValueString("tableName");
            QTableMetaData table     = QContext.getQInstance().getTable(tableName);

            if(table != null)
            {
               for(QFieldSection section : CollectionUtils.nonNullList(table.getSections()))
               {
                  if(section.getWidgetName() != null)
                  {
                     allPossibleValues.add(getPossibleValue(section.getWidgetName()));
                  }
               }
            }
         }
      }

      return completeCustomPVSSearch(searchPossibleValueSourceInput, allPossibleValues);
   }
}
