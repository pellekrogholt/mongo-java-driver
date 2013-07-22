/*
 * Copyright (c) 2008 - 2013 10gen, Inc. <http://10gen.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.mongodb.connection.impl;


import java.util.List;

import org.mongodb.MongoCredential;
import org.mongodb.connection.AsyncConnection;
import org.mongodb.connection.AsyncConnectionFactory;
import org.mongodb.connection.BufferProvider;
import org.mongodb.connection.SSLSettings;
import org.mongodb.connection.ServerAddress;


public class DefaultAsyncConnectionFactory implements AsyncConnectionFactory {
    private SSLSettings sslSettings;
    private BufferProvider bufferProvider;
    private List<MongoCredential> credentialList;

    public DefaultAsyncConnectionFactory(final SSLSettings sslSettings, final BufferProvider bufferProvider,
        final List<MongoCredential> credentialList) {
        this.sslSettings = sslSettings;
        this.bufferProvider = bufferProvider;
        this.credentialList = credentialList;
    }

    @Override
    public AsyncConnection create(final ServerAddress serverAddress) {
        AsyncConnection connection;
        if (sslSettings.isEnabled()) {
            connection = new AuthenticatingAsyncConnection(new DefaultSSLAsyncConnection(serverAddress, bufferProvider), credentialList,
                bufferProvider);
        } else {
            connection = new AuthenticatingAsyncConnection(new DefaultAsyncConnection(serverAddress, bufferProvider), credentialList,
                bufferProvider);
        }
        return connection;
    }

}