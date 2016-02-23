package rpowell.blockchain.domain;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public abstract class DomainObject {

    @GraphId
    protected Long id;
}