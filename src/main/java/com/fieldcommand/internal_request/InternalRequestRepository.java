package com.fieldcommand.internal_request;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("RequestModel")
interface InternalRequestRepository extends CrudRepository<RequestModel, Long> {

    List<RequestModel> findAllByOrderByIdDesc();


}
