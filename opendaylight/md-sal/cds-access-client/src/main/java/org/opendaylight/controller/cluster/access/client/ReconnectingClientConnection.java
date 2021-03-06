/*
 * Copyright (c) 2016 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.controller.cluster.access.client;

import akka.actor.ActorRef;
import java.util.Map.Entry;
import org.opendaylight.controller.cluster.access.concepts.Request;
import org.opendaylight.controller.cluster.access.concepts.RequestEnvelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An {@link AbstractClientConnection} which is being reconnected after having timed out.
 *
 * @author Robert Varga
 *
 * @param <T> {@link BackendInfo} type
 */
public final class ReconnectingClientConnection<T extends BackendInfo> extends AbstractReceivingClientConnection<T> {
    private static final Logger LOG = LoggerFactory.getLogger(ReconnectingClientConnection.class);

    ReconnectingClientConnection(final ConnectedClientConnection<T> oldConnection) {
        super(oldConnection);
    }

    @Override
    ClientActorBehavior<T> reconnectConnection(final ClientActorBehavior<T> current) {
        // Intentional no-op
        LOG.debug("Skipping reconnect of already-reconnecting connection {}", this);
        return current;
    }

    @Override
    Entry<ActorRef, RequestEnvelope> prepareForTransmit(final Request<?, ?> req) {
        // This is guarded by remoteMaxMessages() == 0
        throw new UnsupportedOperationException("Attempted to transmit on a reconnecting connection");
    }

    @Override
    int remoteMaxMessages() {
        return 0;
    }
}
