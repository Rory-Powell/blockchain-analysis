package org.rpowell.blockchain.graph;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.RelationshipType;

/**
 * String constants used by Neo4j.
 */
public class GraphConstants {

    /**
     * Node labels.
     */
    public static class Labels {

        public static final Label WALLET = DynamicLabel.label("Wallet");
        public static final Label ADDRESS = DynamicLabel.label("Address");
        public static final Label TRANSACTION = DynamicLabel.label("Transaction");

    }

    /**
     * Property names for nodes / relationships
     */
    public static class Props {

        // Transaction properties
        public static String INDEX = "Index";
        public static String HASH = "Hash";
        public static String TIMESTAMP = "Timestamp";

        // Address properties
        public static String ADDR = "Addr";
        public static String AMOUNT = "Amount";

        // Input
        public static String FROM_TX_INDEX = "FromTxIndex";

        // Output
        public static String OUTPUT_NUM = "OutputNum";
        public static String TX_INDEX = "TxIndex";
    }

    /**
     * Relationship names.
     */
    public static class Relationships {

        public static RelationshipType WITHDRAW = DynamicRelationshipType.withName("Withdraw");
        public static RelationshipType SAME_OWNER = DynamicRelationshipType.withName("Same_Owner");
        public static RelationshipType DEPOSIT = DynamicRelationshipType.withName("Deposit");

    }

}
