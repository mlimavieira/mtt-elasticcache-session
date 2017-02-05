package com.mlimavieira.elasticcache.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.mlimavieira.elasticcache.model.Airport;

public interface AirportRepository extends CrudRepository<Airport, Long> {

	List<Airport> findByCityContainingIgnoreCase(String city);

	List<Airport> findByCountryContainingIgnoreCase(String country);
	
	
	@Query("SELECT a.country FROM Airport a GROUP BY country")
	List<Airport> listOfCountries();
	
	@Query("SELECT a.city FROM Airport a GROUP BY city")
	List<Airport> listOfCities();

}
