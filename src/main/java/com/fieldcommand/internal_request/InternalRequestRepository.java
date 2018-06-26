package com.fieldcommand.internal_request;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

interface InternalRequestRepository extends CrudRepository<RequestModel, Long> {

    List<RequestModel> findAllByOrderByIdDesc();


}
