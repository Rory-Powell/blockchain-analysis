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
    public static class Properties {

        public static String ADDR = "Addr";

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
