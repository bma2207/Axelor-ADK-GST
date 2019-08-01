package com.axelor.gst.db.repo;

import java.util.Map;

import com.axelor.gst.db.Product;

public class ProductRepo extends ProductRepository {
	public Map<String, Object> populate(Map<String, Object> json, Map<String, Object> context) {
		if (!context.containsKey("json-enhance")) {
			return json;
		}
		try {
			Long id = (Long) json.get("id");

			Product product = find(id);

			json.put("hasImage", product.getImage() != null);
		} catch (Exception e) {
		}

		return json;
	}
}
