package com.mlimavieira.elasticcache.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mlimavieira.elasticcache.model.Airport;
import com.mlimavieira.elasticcache.repository.AirportRepository;

@RestController
@RequestMapping("/v2")
public class AirportController {

	@Value("${THREAD_SLEEP_TIME:5000}")
	private Long sleep;
	@Autowired
	private AirportRepository airportRepository;

	@RequestMapping(value = "/airport", method = RequestMethod.GET)
	public List<Airport> list() {
		forceDelay();
		return (List<Airport>) airportRepository.findAll();
	}

	@RequestMapping(value = "/airport/{id}", method = RequestMethod.GET)
	public Airport getByCity(@PathVariable("id") Long id) {

		forceDelay();
		return airportRepository.findOne(id);
	}

	@RequestMapping(value = "/airport/city/{city}", method = RequestMethod.GET)
	public List<Airport> getByCity(@PathVariable("city") String city) {

		forceDelay();
		return (List<Airport>) airportRepository.findByCityContainingIgnoreCase(city);
	}

	@RequestMapping(value = "/airport/country/{country}", method = RequestMethod.GET)
	public List<Airport> getByCountry(@PathVariable("country") String country) {

		forceDelay();
		return (List<Airport>) airportRepository.findByCountryContainingIgnoreCase(country);
	}

	@RequestMapping(value = "/airport/countries", method = RequestMethod.GET)
	public List<Airport> getCountries() {

		forceDelay();
		return (List<Airport>) airportRepository.listOfCountries();
	}

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
