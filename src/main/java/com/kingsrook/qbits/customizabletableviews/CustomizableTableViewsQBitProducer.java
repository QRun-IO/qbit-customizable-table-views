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

package com.kingsrook.qbits.customizabletableviews;


import com.kingsrook.qbits.customizabletableviews.logic.CustomizableTableViewsTablePersonalizer;
import com.kingsrook.qbits.customizabletableviews.model.CustomizableTable;
import com.kingsrook.qbits.customizabletableviews.model.TableView;
import com.kingsrook.qbits.customizabletableviews.model.TableViewField;
import com.kingsrook.qbits.customizabletableviews.model.TableViewRoleInt;
import com.kingsrook.qbits.customizabletableviews.model.TableViewWidget;
import com.kingsrook.qqq.backend.core.actions.metadata.personalization.TableMetaDataPersonalizerInterface;
import com.kingsrook.qqq.backend.core.model.metadata.QInstance;
import com.kingsrook.qqq.backend.core.model.metadata.code.QCodeReference;
import com.kingsrook.qqq.backend.core.model.metadata.layout.QAppSection;
import com.kingsrook.qqq.backend.core.model.metadata.qbits.QBitMetaData;
import com.kingsrook.qqq.backend.core.model.metadata.qbits.QBitMetaDataProducer;


/*******************************************************************************
 **
 *******************************************************************************/
public class CustomizableTableViewsQBitProducer implements QBitMetaDataProducer<CustomizableTableViewsQBitConfig>
{
   public static final String GROUP_ID    = "com.kingsrook.qbits";
   public static final String ARTIFACT_ID = "customizable-table-views";
   public static final String VERSION     = "0.5.0-SNAPSHOT";

   private CustomizableTableViewsQBitConfig customizableTableViewsQBitConfig;



   /***************************************************************************
    **
    ***************************************************************************/
   @Override
   public QBitMetaData getQBitMetaData()
   {
      QBitMetaData qBitMetaData = new QBitMetaData()
         .withGroupId(GROUP_ID)
         .withArtifactId(ARTIFACT_ID)
         .withVersion(VERSION)
         .withNamespace(getNamespace())
         .withConfig(getQBitConfig());

      return qBitMetaData;
   }



   /***************************************************************************
    *
    ***************************************************************************/
   public static QAppSection getAppSection(QInstance qInstance)
   {
      return (new QAppSection().withName("tableViews")
         .withTable(CustomizableTable.TABLE_NAME)
         .withTable(TableView.TABLE_NAME)
         .withTable(TableViewField.TABLE_NAME)
         .withTable(TableViewWidget.TABLE_NAME)
         .withTable(TableViewRoleInt.TABLE_NAME)
      );
   }



   /***************************************************************************
    *
    ***************************************************************************/
   public static void activateTableMetaDataPersonalizerInterface(QInstance qInstance)
   {
      qInstance.addSupplementalCustomizer(TableMetaDataPersonalizerInterface.CUSTOMIZER_TYPE, new QCodeReference(CustomizableTableViewsTablePersonalizer.class));
   }



   /*******************************************************************************
    ** Getter for qBitConfig
    *******************************************************************************/
   @Override
   public CustomizableTableViewsQBitConfig getQBitConfig()
   {
      return (this.customizableTableViewsQBitConfig);
   }



   /*******************************************************************************
    ** Setter for qBitConfig
    *******************************************************************************/
   public void setQBitConfig(CustomizableTableViewsQBitConfig customizableTableViewsQBitConfig)
   {
      this.customizableTableViewsQBitConfig = customizableTableViewsQBitConfig;
   }



   /*******************************************************************************
    ** Fluent setter for qBitConfig
    *******************************************************************************/
   public CustomizableTableViewsQBitProducer withQBitConfig(CustomizableTableViewsQBitConfig customizableTableViewsQBitConfig)
   {
      this.customizableTableViewsQBitConfig = customizableTableViewsQBitConfig;
      return (this);
   }

}
