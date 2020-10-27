package com.inv.mgmt.repo;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.inv.mgmt.model.LocationMap;
import com.inv.mgmt.model.ProductInventory;



public interface LocationMapRepo extends PagingAndSortingRepository<LocationMap, Long> {

	Optional<LocationMap> findByLocationCode(String locationCode);

}
