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


import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.kingsrook.qbits.customizabletableviews.model.FieldAccessLevel;
import com.kingsrook.qqq.backend.core.actions.customizers.RecordCustomizerUtilityInterface;
import com.kingsrook.qqq.backend.core.actions.customizers.TableCustomizerInterface;
import com.kingsrook.qqq.backend.core.context.QContext;
import com.kingsrook.qqq.backend.core.exceptions.QException;
import com.kingsrook.qqq.backend.core.model.actions.AbstractActionInput;
import com.kingsrook.qqq.backend.core.model.data.QRecord;
import com.kingsrook.qqq.backend.core.model.metadata.fields.QFieldMetaData;
import com.kingsrook.qqq.backend.core.model.statusmessages.BadInputStatusMessage;
import com.kingsrook.qqq.backend.core.model.statusmessages.SystemErrorStatusMessage;
import com.kingsrook.qqq.backend.core.utils.ValueUtils;


/*******************************************************************************
 **
 *******************************************************************************/
public class TableViewFieldCustomizer implements TableCustomizerInterface
{

   /***************************************************************************
    *
    ***************************************************************************/
   @Override
   public List<QRecord> preInsertOrUpdate(AbstractActionInput input, List<QRecord> records, boolean isPreview, Optional<List<QRecord>> oldRecordList) throws QException
   {
      Optional<Map<Serializable, QRecord>> oldRecordMap = oldRecordListToMap("id", oldRecordList);

      for(QRecord record : records)
      {
         try
         {
            Integer id            = record.getValueInteger("id");
            String  fieldName     = ValueUtils.getValueAsString(RecordCustomizerUtilityInterface.getValueFromRecordElseFromOldRecord("fieldName", record, id, oldRecordMap));
            String  accessLevelId = ValueUtils.getValueAsString(RecordCustomizerUtilityInterface.getValueFromRecordElseFromOldRecord("accessLevel", record, id, oldRecordMap));

            String[] parts     = fieldName.split("\\.");
            String   tableName = parts[0];
            fieldName = parts[1];

            QFieldMetaData field = QContext.getQInstance().getTable(tableName).getFieldOrVirtualField(fieldName);

            FieldAccessLevel fieldAccessLevel = FieldAccessLevel.getById(accessLevelId);
            if(fieldAccessLevel != null)
            {
               String errorMessage = fieldAccessLevel.validateForField(field);
               if(errorMessage != null)
               {
                  record.addError(new BadInputStatusMessage(errorMessage));
               }
            }
         }
         catch(Exception e)
         {
            record.addError(new SystemErrorStatusMessage("Error evaluating record prior to saving: " + e.getMessage()));
         }
      }
      return records;
   }

}
