package com.mlimavieira.elasticcache.rest;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PathVariable;
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

	@RequestMapping(value = "/clean-cache", method = RequestMethod.GET)
	public void cleanCache() {
		Collection<String> cacheNames = cacheManager.getCacheNames();
		for (String cacheName : cacheNames) {
			Cache cache = cacheManager.getCache(cacheName);
			cache.clear();
		}
	}

	@Cacheable(cacheNames = "listAirports", unless = "#result != null and #result.size() == 0")
	@RequestMapping(value = "/airport", method = RequestMethod.GET)
	public List<Airport> list() {
		forceDelay();
		return (List<Airport>) airportRepository.findAll();
	}

	@Cacheable(cacheNames = "airport", unless = "#result != null")
	@RequestMapping(value = "/airport/{id}", method = RequestMethod.GET)
	public Airport getByCity(@PathVariable("id") Long id) {

		forceDelay();
		return airportRepository.findOne(id);
	}

	@Cacheable(cacheNames = "airportCity", key = "#city", unless = "#result != null and #result.size() == 0")
	@RequestMapping(value = "/airport/city/{city}", method = RequestMethod.GET)
	public List<Airport> getByCity(@PathVariable("city") String city) {

		forceDelay();
		return (List<Airport>) airportRepository.findByCityContainingIgnoreCase(city);
	}

	@Cacheable(cacheNames = "airportCountry", key = "#country", unless = "#result != null and #result.size() == 0")
	@RequestMapping(value = "/airport/country/{country}", method = RequestMethod.GET)
	public List<Airport> getByCountry(@PathVariable("country") String country) {

		forceDelay();
		return (List<Airport>) airportRepository.findByCountryContainingIgnoreCase(country);
	}

	@Cacheable(cacheNames = "airportCountries", unless = "#result != null and #result.size() == 0")
	@RequestMapping(value = "/airport/countries", method = RequestMethod.GET)
	public List<Airport> getCountries() {

		forceDelay();
		return (List<Airport>) airportRepository.listOfCountries();
	}

	@Cacheable(cacheNames = "airportCities", unless = "#result != null and #result.size() == 0")
	@RequestMapping(value = "/airport/cities", method = RequestMethod.GET)
	public List<Airport> getCities() {
		forceDelay();
		return (List<Airport>) airportRepository.listOfCities();
	}

	private void forceDelay() {
		try {
			Thread.sleep(sleep);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
