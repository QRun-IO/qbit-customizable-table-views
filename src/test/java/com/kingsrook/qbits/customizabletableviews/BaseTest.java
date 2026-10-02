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


import com.kingsrook.qqq.backend.core.context.QContext;
import com.kingsrook.qqq.backend.core.exceptions.QException;
import com.kingsrook.qqq.backend.core.instances.QInstanceEnricher;
import com.kingsrook.qqq.backend.core.instances.QInstanceValidator;
import com.kingsrook.qqq.backend.core.model.metadata.QAuthenticationType;
import com.kingsrook.qqq.backend.core.model.metadata.QBackendMetaData;
import com.kingsrook.qqq.backend.core.model.metadata.QInstance;
import com.kingsrook.qqq.backend.core.model.metadata.audits.AuditLevel;
import com.kingsrook.qqq.backend.core.model.metadata.audits.QAuditRules;
import com.kingsrook.qqq.backend.core.model.metadata.authentication.QAuthenticationMetaData;
import com.kingsrook.qqq.backend.core.model.metadata.possiblevalues.PossibleValueEnum;
import com.kingsrook.qqq.backend.core.model.metadata.possiblevalues.QPossibleValueSource;
import com.kingsrook.qqq.backend.core.model.metadata.tables.TablesPossibleValueSourceMetaDataProvider;
import com.kingsrook.qqq.backend.core.model.session.QSession;
import com.kingsrook.qqq.backend.core.modules.backend.implementations.memory.MemoryBackendModule;
import com.kingsrook.qqq.backend.core.modules.backend.implementations.memory.MemoryRecordStore;
import com.kingsrook.qqq.backend.core.utils.StringUtils;
import com.kingsrook.qqq.backend.module.rdbms.model.metadata.RDBMSBackendMetaData;
import com.kingsrook.qqq.backend.module.rdbms.model.metadata.RDBMSTableBackendDetails;
import org.junit.jupiter.api.BeforeEach;


/*******************************************************************************
 **
 *******************************************************************************/
public class BaseTest
{
   public static final String RDBMS_BACKEND_NAME  = "h2rdbms";
   public static final String MEMORY_BACKEND_NAME = "memory";



   /*******************************************************************************
    **
    *******************************************************************************/
   @BeforeEach
   void baseBeforeEach() throws Exception
   {
      QInstance qInstance = defineQInstance();
      new QInstanceValidator().validate(qInstance);
      QContext.init(qInstance, new QSession());

      MemoryRecordStore.fullReset();
   }



   /***************************************************************************
    **
    ***************************************************************************/
   private QInstance defineQInstance() throws QException
   {
      /////////////////////////////////////
      // basic definition of an instance //
      /////////////////////////////////////
      QInstance qInstance = new QInstance();
      qInstance.withInstanceDefaultAuthentication(new QAuthenticationMetaData().withType(QAuthenticationType.FULLY_ANONYMOUS));
      qInstance.addBackend(new RDBMSBackendMetaData()
         .withName(RDBMS_BACKEND_NAME)
         .withVendor("h2")
         .withHostName("mem")
         .withDatabaseName("test_database")
         .withUsername("sa"));

      qInstance.addBackend(new QBackendMetaData()
         .withName(MEMORY_BACKEND_NAME)
         .withBackendType(MemoryBackendModule.class));

      ////////////////////////
      // configure the qbit //
      ////////////////////////
      CustomizableTableViewsQBitConfig config = new CustomizableTableViewsQBitConfig()
         .withTableMetaDataCustomizer((i, table) ->
         {
            if(table.getBackendName() == null)
            {
               // table.setBackendName(RDBMS_BACKEND_NAME);
               table.setBackendName(MEMORY_BACKEND_NAME);
            }

            table.setBackendDetails(new RDBMSTableBackendDetails()
               .withTableName(QInstanceEnricher.inferBackendName(table.getName())));
            QInstanceEnricher.setInferredFieldBackendNames(table);

            return (table);
         });

      //////////////////////
      // produce our qbit //
      //////////////////////
      new CustomizableTableViewsQBitProducer()
         .withQBitConfig(config)
         .produce(qInstance)
         .addSelfToInstance(qInstance);

      //////////////////////
      // add 'tables' pvs //
      //////////////////////
      TablesPossibleValueSourceMetaDataProvider.defineTablesPossibleValueSource(qInstance).addSelfToInstance(qInstance);

      //////////////////////////////////////////
      // add a placeholder for the "role" pvs //
      //////////////////////////////////////////
      QPossibleValueSource.newForEnum("role", TestRolesEnum.values()).addSelfToInstance(qInstance);

      /////////////////////
      // turn off audits //
      /////////////////////
      qInstance.getTables().values().forEach(t -> t.setAuditRules(new QAuditRules().withAuditLevel(AuditLevel.NONE)));
      return qInstance;
   }



   /***************************************************************************
    *
    ***************************************************************************/
   public enum TestRolesEnum implements PossibleValueEnum<Integer>
   {
      ADMIN(1),
      CUSTOMER(2);


      private final int id;



      /***************************************************************************
       *
       ***************************************************************************/
      TestRolesEnum(int id)
      {
         this.id = id;
      }



      /***************************************************************************
       *
       ***************************************************************************/
      @Override
      public Integer getPossibleValueId()
      {
         return id;
      }



      /***************************************************************************
       *
       ***************************************************************************/
      @Override
      public String getPossibleValueLabel()
      {
         return StringUtils.allCapsToMixedCase(name());
      }
   }
}

