package com.fieldcommand.intrequest;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InternalRequestRepository extends CrudRepository<InternalRequest, Long> {

    List<InternalRequest> findAllByOrderByIdDesc();


}
