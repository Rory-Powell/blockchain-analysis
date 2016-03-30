package org.rpowell.blockchain.graph;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.Label;

public class Labels {

    public static final Label WALLET = DynamicLabel.label("Wallet");
    public static final Label ADDRESS = DynamicLabel.label("Address");
    public static final Label TRANSACTION = DynamicLabel.label("Transaction");

}
