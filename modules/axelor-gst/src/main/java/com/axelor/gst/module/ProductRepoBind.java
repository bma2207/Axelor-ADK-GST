package com.axelor.gst.module;

import com.axelor.app.AxelorModule;
import com.axelor.gst.db.repo.ProductRepo;
import com.axelor.gst.db.repo.ProductRepository;

public class ProductRepoBind extends AxelorModule {

	@Override
	protected void configure() {
		// TODO Auto-generated method stub
		bind(ProductRepository.class).to(ProductRepo.class);
	}

}
