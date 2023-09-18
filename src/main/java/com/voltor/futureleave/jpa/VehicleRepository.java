package com.voltor.futureleave.jpa;

import org.springframework.stereotype.Repository;

import com.voltor.futureleave.model.Vehicle;

@Repository
public interface VehicleRepository extends BaseCRUDRepository< Vehicle > {
}
