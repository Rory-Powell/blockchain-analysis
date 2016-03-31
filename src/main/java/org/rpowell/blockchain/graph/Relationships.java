package org.rpowell.blockchain.graph;

import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.RelationshipType;

public class Relationships {

    public static RelationshipType WITHDRAW = DynamicRelationshipType.withName("Withdraw");
    public static RelationshipType SAME_OWNER = DynamicRelationshipType.withName("Same_Owner");
    public static RelationshipType DEPOSIT = DynamicRelationshipType.withName("Deposit");
}
