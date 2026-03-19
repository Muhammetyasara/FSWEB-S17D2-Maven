package com.workintech.s17d2.rest;

import com.workintech.s17d2.model.*;
import com.workintech.s17d2.tax.Taxable;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/developers")
public class DeveloperController {

    public Map<Integer, Developer> developers;
    private final Taxable developerTax;

    @Autowired
    public DeveloperController(Taxable developerTax) {
        this.developerTax = developerTax;
    }

    @PostConstruct
    public void init() {
        developers = new HashMap<>();
    }

    @GetMapping
    public List<Developer> getAllDevelopers() {
        return new ArrayList<>(developers.values());
    }

    @GetMapping("/{id}")
    public Developer getDeveloperById(@PathVariable int id) {
        return developers.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Developer addDeveloper(@RequestBody Developer dev) {
        Developer newDev;
        switch (dev.getExperience()) {
            case JUNIOR -> newDev = new JuniorDeveloper(dev.getId(), dev.getName(), dev.getSalary() * (1 - developerTax.getSimpleTaxRate() / 100));
            case MID -> newDev = new MidDeveloper(dev.getId(), dev.getName(), dev.getSalary() * (1 - developerTax.getMiddleTaxRate() / 100));
            case SENIOR -> newDev = new SeniorDeveloper(dev.getId(), dev.getName(), dev.getSalary() * (1 - developerTax.getUpperTaxRate() / 100));
            default -> newDev = dev;
        }
        developers.put(newDev.getId(), newDev);
        return newDev;
    }

    @PutMapping("/{id}")
    public Developer updateDeveloper(@PathVariable int id, @RequestBody Developer dev) {
        developers.put(id, dev);
        return dev;
    }

    @DeleteMapping("/{id}")
    public void deleteDeveloper(@PathVariable int id) {
        developers.remove(id);
    }
}