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


import java.time.Instant;
import com.kingsrook.qbits.customizabletableviews.logic.CustomizableTableViewsTablePersonalizer;
import com.kingsrook.qqq.backend.core.exceptions.QException;
import com.kingsrook.qqq.backend.core.model.actions.tables.query.QCriteriaOperator;
import com.kingsrook.qqq.backend.core.model.actions.tables.query.QFilterCriteria;
import com.kingsrook.qqq.backend.core.model.actions.tables.query.QQueryFilter;
import com.kingsrook.qqq.backend.core.model.data.QField;
import com.kingsrook.qqq.backend.core.model.data.QRecord;
import com.kingsrook.qqq.backend.core.model.data.QRecordEntity;
import com.kingsrook.qqq.backend.core.model.metadata.QInstance;
import com.kingsrook.qqq.backend.core.model.metadata.fields.ValueTooLongBehavior;
import com.kingsrook.qqq.backend.core.model.metadata.joins.QJoinMetaData;
import com.kingsrook.qqq.backend.core.model.metadata.layout.QIcon;
import com.kingsrook.qqq.backend.core.model.metadata.producers.MetaDataCustomizerInterface;
import com.kingsrook.qqq.backend.core.model.metadata.producers.annotations.ChildJoin;
import com.kingsrook.qqq.backend.core.model.metadata.producers.annotations.ChildRecordListWidget;
import com.kingsrook.qqq.backend.core.model.metadata.producers.annotations.ChildTable;
import com.kingsrook.qqq.backend.core.model.metadata.producers.annotations.QMetaDataProducingEntity;
import com.kingsrook.qqq.backend.core.model.metadata.tables.QTableMetaData;
import com.kingsrook.qqq.backend.core.model.metadata.tables.SectionFactory;
import com.kingsrook.qqq.backend.core.model.metadata.tables.TablesPossibleValueSourceMetaDataProvider;
import com.kingsrook.qqq.backend.core.model.metadata.tables.UniqueKey;


/*******************************************************************************
 ** QRecord Entity for CustomizableTable table
 *******************************************************************************/
@QMetaDataProducingEntity(
   producePossibleValueSource = true,
   produceTableMetaData = true,
   tableMetaDataCustomizer = CustomizableTable.TableMetaDataCustomizer.class,
   childTables = {
      @ChildTable(
         childTableEntityClass = TableView.class,
         joinFieldName = "customizableTableId",
         childJoin = @ChildJoin(enabled = true),
         childRecordListWidget = @ChildRecordListWidget(label = "Views", enabled = true, maxRows = 250, canAddChildRecords = true))
   }
)
public class CustomizableTable extends QRecordEntity
{
   public static final String TABLE_NAME = "CustomizableTable";



   /***************************************************************************
    **
    ***************************************************************************/
   public static class TableMetaDataCustomizer implements MetaDataCustomizerInterface<QTableMetaData>
   {

      /***************************************************************************
       **
       ***************************************************************************/
      @Override
      public QTableMetaData customizeMetaData(QInstance qInstance, QTableMetaData table) throws QException
      {
         String childJoinName = QJoinMetaData.makeInferredJoinName(TABLE_NAME, TableView.TABLE_NAME);

         table
            .withUniqueKey(new UniqueKey("tableName"))
            .withIcon(new QIcon().withName("table_view"))
            .withRecordLabelFormat("%s")
            .withRecordLabelFields("tableName")
            .withSection(SectionFactory.defaultT1("id", "tableName"))
            .withSection(SectionFactory.defaultT2("isActive", "defaultTableViewId"))
            .withSection(SectionFactory.customT2("views", new QIcon("table_chart")).withWidgetName(childJoinName))
            .withSection(SectionFactory.defaultT3("createDate", "modifyDate"));

         table.getField("defaultTableViewId").withPossibleValueSourceFilter(new QQueryFilter(new QFilterCriteria(TABLE_NAME + ".tableName", QCriteriaOperator.EQUALS, "${input.tableName}")));

         CustomizableTableViewsTablePersonalizer.MemoizationClearer.addPostActionCustomizersToTable(table);

         return (table);
      }
   }



   @QField(isEditable = false, isPrimaryKey = true)
   private Integer id;

   @QField(label = "Table", isRequired = true, maxLength = 100, valueTooLongBehavior = ValueTooLongBehavior.ERROR, possibleValueSourceName = TablesPossibleValueSourceMetaDataProvider.NAME)
   private String tableName;

   @QField()
   private Boolean isActive;

   @QField(possibleValueSourceName = TableView.TABLE_NAME)
   private Integer defaultTableViewId;

   @QField(isEditable = false)
   private Instant createDate;

   @QField(isEditable = false)
   private Instant modifyDate;



   /*******************************************************************************
    ** Default constructor
    *******************************************************************************/
   public CustomizableTable()
   {
   }



   /*******************************************************************************
    ** Constructor that takes a QRecord
    *******************************************************************************/
   public CustomizableTable(QRecord record)
   {
      populateFromQRecord(record);
   }



   /*******************************************************************************
    * Getter for id
    * @see #withId(Integer)
    *******************************************************************************/
   public Integer getId()
   {
      return (this.id);
   }



   /*******************************************************************************
    * Setter for id
    * @see #withId(Integer)
    *******************************************************************************/
   public void setId(Integer id)
   {
      this.id = id;
   }



   /*******************************************************************************
    * Fluent setter for id
    *
    * @param id
    * TODO document this property
    *
    * @return this
    *******************************************************************************/
   public CustomizableTable withId(Integer id)
   {
      this.id = id;
      return (this);
   }



   /*******************************************************************************
    * Getter for tableName
    * @see #withTableName(String)
    *******************************************************************************/
   public String getTableName()
   {
      return (this.tableName);
   }



   /*******************************************************************************
    * Setter for tableName
    * @see #withTableName(String)
    *******************************************************************************/
   public void setTableName(String tableName)
   {
      this.tableName = tableName;
   }



   /*******************************************************************************
    * Fluent setter for tableName
    *
    * @param tableName
    * TODO document this property
    *
    * @return this
    *******************************************************************************/
   public CustomizableTable withTableName(String tableName)
   {
      this.tableName = tableName;
      return (this);
   }



   /*******************************************************************************
    * Getter for isActive
    * @see #withIsActive(Boolean)
    *******************************************************************************/
   public Boolean getIsActive()
   {
      return (this.isActive);
   }



   /*******************************************************************************
    * Setter for isActive
    * @see #withIsActive(Boolean)
    *******************************************************************************/
   public void setIsActive(Boolean isActive)
   {
      this.isActive = isActive;
   }



   /*******************************************************************************
    * Fluent setter for isActive
    *
    * @param isActive
    * TODO document this property
    *
    * @return this
    *******************************************************************************/
   public CustomizableTable withIsActive(Boolean isActive)
   {
      this.isActive = isActive;
      return (this);
   }



   /*******************************************************************************
    * Getter for defaultTableViewId
    * @see #withDefaultTableViewId(Integer)
    *******************************************************************************/
   public Integer getDefaultTableViewId()
   {
      return (this.defaultTableViewId);
   }



   /*******************************************************************************
    * Setter for defaultTableViewId
    * @see #withDefaultTableViewId(Integer)
    *******************************************************************************/
   public void setDefaultTableViewId(Integer defaultTableViewId)
   {
      this.defaultTableViewId = defaultTableViewId;
   }



   /*******************************************************************************
    * Fluent setter for defaultTableViewId
    *
    * @param defaultTableViewId
    * TODO document this property
    *
    * @return this
    *******************************************************************************/
   public CustomizableTable withDefaultTableViewId(Integer defaultTableViewId)
   {
      this.defaultTableViewId = defaultTableViewId;
      return (this);
   }



   /*******************************************************************************
    * Getter for createDate
    * @see #withCreateDate(Instant)
    *******************************************************************************/
   public Instant getCreateDate()
   {
      return (this.createDate);
   }



   /*******************************************************************************
    * Setter for createDate
    * @see #withCreateDate(Instant)
    *******************************************************************************/
   public void setCreateDate(Instant createDate)
   {
      this.createDate = createDate;
   }



   /*******************************************************************************
    * Fluent setter for createDate
    *
    * @param createDate
    * TODO document this property
    *
    * @return this
    *******************************************************************************/
   public CustomizableTable withCreateDate(Instant createDate)
   {
      this.createDate = createDate;
      return (this);
   }



   /*******************************************************************************
    * Getter for modifyDate
    * @see #withModifyDate(Instant)
    *******************************************************************************/
   public Instant getModifyDate()
   {
      return (this.modifyDate);
   }



   /*******************************************************************************
    * Setter for modifyDate
    * @see #withModifyDate(Instant)
    *******************************************************************************/
   public void setModifyDate(Instant modifyDate)
   {
      this.modifyDate = modifyDate;
   }



   /*******************************************************************************
    * Fluent setter for modifyDate
    *
    * @param modifyDate
    * TODO document this property
    *
    * @return this
    *******************************************************************************/
   public CustomizableTable withModifyDate(Instant modifyDate)
   {
      this.modifyDate = modifyDate;
      return (this);
   }

}
