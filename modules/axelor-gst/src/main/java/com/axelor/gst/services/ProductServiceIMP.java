package com.axelor.gst.services;

import java.util.ArrayList;
import java.util.List;

import com.axelor.gst.db.Product;
import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.ProductRepository;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.gst.services.ProductService;
import com.axelor.inject.Beans;

public class ProductServiceIMP implements ProductService {

	@Override
	public List<Product> productList(List<Integer> lstSelectedLocations) {
		Product pr = new Product();
		List<Product> productLst = new ArrayList<>();
		 
		for (int _id : lstSelectedLocations) {
			pr = Beans.get(ProductRepository.class).find(Long.parseLong("" + _id));
			productLst.add(pr);
		}
		return productLst;
	}

}
