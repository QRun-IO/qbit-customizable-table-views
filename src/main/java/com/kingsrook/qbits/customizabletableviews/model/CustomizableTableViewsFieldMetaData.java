/*
 * QQQ - Low-code Application Framework for Engineers.
 * Copyright (C) 2021-2026.  Kingsrook, LLC
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


import com.kingsrook.qqq.backend.core.model.metadata.fields.QSupplementalFieldMetaData;


/*******************************************************************************
 **
 *******************************************************************************/
public class CustomizableTableViewsFieldMetaData extends QSupplementalFieldMetaData
{
   public static final String TYPE = CustomizableTableViewsFieldMetaData.class.getName();

   private Rule rule;

   public enum Rule
   {
      ALWAYS_KEEP_FIELD
   }

   /***************************************************************************
    *
    ***************************************************************************/
   @Override
   public String getType()
   {
      return TYPE;
   }


   /*******************************************************************************
    * Getter for rule
    * @see #withRule(Rule)
    *******************************************************************************/
   public Rule getRule()
   {
      return (this.rule);
   }



   /*******************************************************************************
    * Setter for rule
    * @see #withRule(Rule)
    *******************************************************************************/
   public void setRule(Rule rule)
   {
      this.rule = rule;
   }



   /*******************************************************************************
    * Fluent setter for rule
    *
    * @param rule
    * TODO document this property
    *
    * @return this
    *******************************************************************************/
   public CustomizableTableViewsFieldMetaData withRule(Rule rule)
   {
      this.rule = rule;
      return (this);
   }


}
