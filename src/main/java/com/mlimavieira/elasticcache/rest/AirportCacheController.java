package com.mlimavieira.elasticcache.rest;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mlimavieira.elasticcache.model.Airport;
import com.mlimavieira.elasticcache.repository.AirportRepository;

@RestController
@RequestMapping("/v1")
public class AirportCacheController {

	@Value("${THREAD_SLEEP_TIME:5000}")
	private Long sleep;
	@Autowired
	private AirportRepository airportRepository;

	@Autowired
	private CacheManager cacheManager;

	/* Clean all the cache entries */
	@RequestMapping(value = "/clean-cache", method = RequestMethod.GET)
	public void cleanCache() {
		Collection<String> cacheNames = cacheManager.getCacheNames();
		for (String cacheName : cacheNames) {
			Cache cache = cacheManager.getCache(cacheName);
			cache.clear();
		}
	}

	/*
	 * Clean all entries for the cache named "listAirports" Remove the entry
	 * with specific id in cache named airport
	 */
	@Caching(evict = { 
			@CacheEvict(cacheNames = "airport", key = "#id"), 
			@CacheEvict(cacheNames = "listAirports", allEntries = true) })
	@RequestMapping(value = "/airport/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable Long id) {
		airportRepository.delete(id);
	}

	// Just update the Key with the airport id
	@CachePut(key = "#result.id", cacheNames = "airport")
	@RequestMapping(value = "/airport/{id}", method = RequestMethod.POST)
	public Airport save(@PathVariable Long id, @RequestBody Airport airport) {
		return airportRepository.save(airport);
	}

	// Creat cache for the list of airports if the result is different of null
	// and not empty
	@Cacheable(cacheNames = "listAirports", unless = "#result == null and #result.size() == 0")
	@RequestMapping(value = "/airport", method = RequestMethod.GET)
	public List<Airport> list() {
		forceDelay();
		return (List<Airport>) airportRepository.findAll();
	}

	/*
	 * ****************************************
	 * 
	 * ****************************************
	 */

	@Cacheable(cacheNames = "airport", unless = "#result == null")
	@RequestMapping(value = "/airport/{id}", method = RequestMethod.GET)
	public Airport getByCity(@PathVariable("id") Long id) {

		forceDelay();
		return airportRepository.findOne(id);
	}

	@Cacheable(cacheNames = "airportCities", unless = "#result == null and #result.size() == 0")
	@RequestMapping(value = "/airport/cities", method = RequestMethod.GET)
	public List<Airport> getCities() {
		forceDelay();
		return (List<Airport>) airportRepository.listOfCities();
	}

	@Cacheable(cacheNames = "airportCity", key = "#city", unless = "#result == null and #result.size() == 0")
	@RequestMapping(value = "/airport/city/{city}", method = RequestMethod.GET)
	public List<Airport> getByCity(@PathVariable("city") String city) {

		forceDelay();
		return (List<Airport>) airportRepository.findByCityContainingIgnoreCase(city);
	}

	@Cacheable(cacheNames = "airportCountries", unless = "#result == null and #result.size() == 0")
	@RequestMapping(value = "/airport/countries", method = RequestMethod.GET)
	public List<Airport> getCountries() {

		forceDelay();
		return (List<Airport>) airportRepository.listOfCountries();
	}

	@Cacheable(cacheNames = "airportCountry", key = "#country", unless = "#result == null and #result.size() == 0")
	@RequestMapping(value = "/airport/country/{country}", method = RequestMethod.GET)
	public List<Airport> getByCountry(@PathVariable("country") String country) {

		forceDelay();
		return (List<Airport>) airportRepository.findByCountryContainingIgnoreCase(country);
	}

	private void forceDelay() {
		try {
			Thread.sleep(sleep);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
