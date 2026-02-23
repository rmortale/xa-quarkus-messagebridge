/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.dulce.messaging;

import ch.dulce.messaging.config.MQProperties;
import com.ibm.mq.jakarta.jms.MQXAConnectionFactory;
import com.ibm.msg.client.jakarta.wmq.WMQConstants;
import io.quarkiverse.messaginghub.pooled.jms.PooledJmsWrapper;
import io.smallrye.common.annotation.Identifier;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import jakarta.transaction.TransactionManager;
import org.apache.camel.component.jms.JmsComponent;
import org.springframework.transaction.jta.JtaTransactionManager;

public class Producers {

    @Inject
    MQProperties mqProperties;

    @Identifier("ibmConnectionFactory")
    public ConnectionFactory createXAConnectionFactory(PooledJmsWrapper wrapper) throws JMSException {
        MQXAConnectionFactory mq = new MQXAConnectionFactory();
        mq.setHostName(mqProperties.host());
        mq.setPort(mqProperties.port());
        mq.setChannel(mqProperties.channel());
        mq.setQueueManager(mqProperties.queueManager());
        mq.setTransportType(WMQConstants.WMQ_CM_CLIENT);
        mq.setStringProperty(WMQConstants.USERID, mqProperties.username());
        mq.setStringProperty(WMQConstants.PASSWORD, mqProperties.password());
        return wrapper.wrapConnectionFactory(mq);
    }

    @Singleton
    JtaTransactionManager manager(TransactionManager transactionManager) {
        return new JtaTransactionManager(transactionManager);
    }

    @Identifier("ibmmq")
    JmsComponent ibmmq(@Identifier("ibmConnectionFactory") ConnectionFactory cf, JtaTransactionManager tm) {
        JmsComponent jmsComponent = JmsComponent.jmsComponent(cf);
        jmsComponent.setTransactionManager(tm);
        return jmsComponent;
    }

    @Identifier("amq")
    JmsComponent amq(ConnectionFactory cf, JtaTransactionManager tm) {
        JmsComponent jmsComponent = JmsComponent.jmsComponent(cf);
        jmsComponent.setTransactionManager(tm);
        return jmsComponent;
    }
}
