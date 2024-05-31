package com.rashmi.javadev.redis.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rashmi.javadev.redis.entity.Product;

@Repository
public class ProductDao {

	@Autowired
	@Qualifier("stringRedisTemplate")
	private RedisTemplate template;


	public Product save(Product product) {
		String productId = String.valueOf(product.getId()); 
		String productJson = toJson(product); 

		template.opsForHash().put("Product", productId, productJson);
		return product;
	}

	private String toJson(Product product) {
		// Convert Product object to JSON string (You can use any JSON library for this purpose)
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.writeValueAsString(product);
		} catch (JsonProcessingException e) {
			// Handle JSON serialization exception
			e.printStackTrace();
			return null;
		}
	}

	public List<Product> findAll(){


		return template.opsForHash().values("Product");

	}

	public Product findProductbyId(Integer id) {
	    String productId = String.valueOf(id); // Convert Integer key to String
	    String productJson = (String) template.opsForHash().get("Product", productId);
	    
	    return productJson != null ? fromJson(productJson) : null;
	}
	private Product fromJson(String productJson) {
	    // Convert JSON string to Product object (You can use any JSON library for this purpose)
	    ObjectMapper objectMapper = new ObjectMapper();
	    try {
	        return objectMapper.readValue(productJson, Product.class);
	    } catch (JsonProcessingException e) {
	        // Handle JSON deserialization exception
	        e.printStackTrace();
	        return null;
	    }
	}

	public String deleteProduct(Integer id) {
	    String productId = String.valueOf(id); // Convert Integer key to String

		template.opsForHash().delete("Product", productId);
		return "Deleted";
	}


}
