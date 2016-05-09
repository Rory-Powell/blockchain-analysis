package org.rpowell.blockchain.services.impl.http;

import org.rpowell.blockchain.domain.Owner;
import org.rpowell.blockchain.services.http.BaseHttpService;
import org.rpowell.blockchain.services.http.IOwnerHttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Http requests for building real world owners.
 */
@Service
public class OwnerHttpServiceImpl extends BaseHttpService implements IOwnerHttpService {

    private static final Logger log = LoggerFactory.getLogger(OwnerHttpServiceImpl.class);

    public OwnerHttpServiceImpl() {
        URI = "https://bitcoin-otc.com/viewgpg.php??outformat=json&outformat=json";
    }

    public List<Owner> getOwners() {
        log.info("Downloading owners");
        Owner[] allOwners = restTemplate.getForObject(URI, Owner[].class);

        List<Owner> filteredOwners = new ArrayList<>();
        for(Owner owner : allOwners) {
            if (owner.getBitcoinaddress() != null) {
                filteredOwners.add(owner);
            }
        }

        return filteredOwners;
    }


}
