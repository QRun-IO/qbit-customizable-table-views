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


import com.kingsrook.qqq.backend.core.model.metadata.fields.QFieldMetaData;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;


/***************************************************************************
 *
 ***************************************************************************/
public class QFieldMetaDataAssert extends AbstractAssert<QFieldMetaDataAssert, QFieldMetaData>
{

   /***************************************************************************
    *
    ***************************************************************************/
   protected QFieldMetaDataAssert(QFieldMetaData qFieldMetaData, Class<?> selfType)
   {
      super(qFieldMetaData, selfType);
   }



   /***************************************************************************
    *
    ***************************************************************************/
   public static QFieldMetaDataAssert assertThat(QFieldMetaData qFieldMetaData)
   {
      return (new QFieldMetaDataAssert(qFieldMetaData, QFieldMetaDataAssert.class));
   }



   /***************************************************************************
    *
    ***************************************************************************/
   public QFieldMetaDataAssert isHidden()
   {
      Assertions.assertThat(actual.getIsHidden()).isTrue();
      return (this);
   }



   /***************************************************************************
    *
    ***************************************************************************/
   public QFieldMetaDataAssert isNotHidden()
   {
      Assertions.assertThat(actual.getIsHidden()).isFalse();
      return (this);
   }



   /***************************************************************************
    *
    ***************************************************************************/
   public QFieldMetaDataAssert isRequired()
   {
      Assertions.assertThat(actual.getIsRequired()).isTrue();
      return (this);
   }



   /***************************************************************************
    *
    ***************************************************************************/
   public QFieldMetaDataAssert isNotRequired()
   {
      Assertions.assertThat(actual.getIsRequired()).isFalse();
      return (this);
   }



   /***************************************************************************
    *
    ***************************************************************************/
   public QFieldMetaDataAssert isEditable()
   {
      Assertions.assertThat(actual.getIsEditable()).isTrue();
      return (this);
   }



   /***************************************************************************
    *
    ***************************************************************************/
   public QFieldMetaDataAssert isNotEditable()
   {
      Assertions.assertThat(actual.getIsEditable()).isFalse();
      return (this);
   }
}
