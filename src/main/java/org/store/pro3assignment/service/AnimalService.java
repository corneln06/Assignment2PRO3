package org.store.pro3assignment.service;

import org.springframework.stereotype.Service;
import org.store.pro3assignment.model.AnimalRecord;
import org.store.pro3assignment.model.ProductRecord;
import org.store.pro3assignment.repository.TraceabilityRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AnimalService
{
  private final Map<String, AnimalRecord> animalMap = new ConcurrentHashMap<>();
  private final TraceabilityRepository repository;

  public AnimalService(TraceabilityRepository repository)
  {
    this.repository = repository;
  }
  public List<String> getAnimalIdsByProduct(String productId) throws Exception
  {
  requireNumericId(productId);
  if (!repository.productExists(productId)) {
    throw new Exception("Product " + productId + " not found");
  }
  return repository.findAnimalIdsByProduct(productId);
}

  public List<ProductRecord> getProductsByAnimal(String animalId) throws Exception{
    requireNumericId(animalId);
    if (!repository.animalExists(animalId)) {
      throw new Exception("Animal " + animalId + " not found");
    }
    return repository.findProductsByAnimal(animalId);
  }

  private void requireNumericId(String id) {
    if (id == null || !id.matches("\\d+")) {
      throw new IllegalArgumentException("'" + id + "' is not a valid id");
    }
  }

  public AnimalRecord addAnimal(String name){
    var id = UUID.randomUUID().toString();
    var animal = new AnimalRecord(id, name);
    animalMap.put(id, animal);

    return animal;
  }
  public AnimalRecord getAnimal(String id){
    return animalMap.get(id);
  }
}
