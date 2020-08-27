package com.vikas.vaadindemo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vikas.vaadindemo.entity.Port;

@Repository
public interface PortRepository extends CrudRepository<Port, Integer> {

}
