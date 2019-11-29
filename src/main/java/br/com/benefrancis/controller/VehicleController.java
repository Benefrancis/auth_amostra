package br.com.benefrancis.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.benefrancis.exception.VehicleNotFoundException;
import br.com.benefrancis.model.Vehicle;
import br.com.benefrancis.repository.VehicleRepository;
import br.com.benefrancis.view.VehicleForm;

@RestController
@RequestMapping("/v1/vehicles")
public class VehicleController {
    private VehicleRepository vehicles;
    
    public VehicleController(VehicleRepository vehicles) {
        this.vehicles = vehicles;
    }
    @GetMapping("")
    public ResponseEntity<List<Vehicle>> all() {
        return ResponseEntity.ok(this.vehicles.findAll());
    }
    @PostMapping("")
    public ResponseEntity<Vehicle> save(@RequestBody VehicleForm form, HttpServletRequest request) {
        Vehicle saved = this.vehicles.save(Vehicle.builder().name(form.getName()).build());
        return ResponseEntity.created(
            ServletUriComponentsBuilder
                .fromContextPath(request)
                .path("/v1/vehicles/{id}")
                .buildAndExpand(saved.getId())
                .toUri())
            .build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> get(@PathVariable("id") Long id) {
        return ResponseEntity.ok(this.vehicles.findById(id).orElseThrow(() -> new VehicleNotFoundException()));
    }
    @PutMapping("/{id}")
    public ResponseEntity<Vehicle> update(@PathVariable("id") Long id, @RequestBody VehicleForm form) {
        Vehicle existed = this.vehicles.findById(id).orElseThrow(() -> new VehicleNotFoundException());
        existed.setName(form.getName());
        this.vehicles.save(existed);
        return ResponseEntity.notFound().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Vehicle> delete(@PathVariable("id") Long id) {
        Vehicle existed = this.vehicles.findById(id).orElseThrow(() -> new VehicleNotFoundException());
        this.vehicles.delete(existed);
        return ResponseEntity.notFound().build();
    }
}