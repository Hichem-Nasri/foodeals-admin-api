package net.foodeals.product.application.services;

import net.foodeals.common.contracts.CrudService;
import net.foodeals.product.application.dtos.requests.ProductCreateRequest;
import net.foodeals.product.application.dtos.requests.ProductDto;
import net.foodeals.product.application.dtos.requests.ProductRequest;
import net.foodeals.product.application.dtos.requests.ProductUpdateRequest;
import net.foodeals.product.application.dtos.responses.ProductResponse;
import net.foodeals.product.domain.entities.Product;
import net.foodeals.product.domain.enums.CreatedBy;
import net.foodeals.product.domain.exceptions.ProductNotFoundException;

import java.io.InputStream;
import java.time.Instant;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.google.zxing.NotFoundException;

public interface ProductService extends CrudService<Product, UUID, ProductRequest> {
	
	public Page<ProductDto> listByCreator(String creator, UUID subEntityId, Pageable pageable);
	public Page<ProductDto> search(UUID subEntityId, CreatedBy createdBy, Pageable pageable) ;

	public ProductDto toDto(Product p);
	
	public ProductResponse createProduct(ProductCreateRequest req) throws Exception;
	
	public ProductResponse updateProduct(UUID id, ProductUpdateRequest request) ;
	
	public void deleteProduct(UUID id, String reason,String motif) ;
	 public ProductResponse toResponse(Product e);
	ProductResponse getProductByBarCode(InputStream imageStream) throws ProductNotFoundException,NotFoundException;
    
	Page<Product> findDeletedProducts(Pageable pageable);

	
	String saveFile(MultipartFile file);
	
	public Page<Product> searchProducts(String name, String brand, UUID categoryId, UUID subCategoryId,
			String barcode,Instant startDate, Instant endDate,Pageable pageable);
	
	
	
}
