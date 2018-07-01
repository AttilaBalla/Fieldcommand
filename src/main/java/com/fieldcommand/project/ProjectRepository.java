package com.fieldcommand.project;

import org.springframework.data.repository.CrudRepository;

public interface ProjectRepository extends CrudRepository<Project, Long> {

    Project findByShortName(String shortName);

}
