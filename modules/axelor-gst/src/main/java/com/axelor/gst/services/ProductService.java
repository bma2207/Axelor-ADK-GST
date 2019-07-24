package com.axelor.gst.services;

import java.util.List;

import com.axelor.gst.db.Product;

public interface ProductService {
	List<Product> productList(List<Integer> list);
}
