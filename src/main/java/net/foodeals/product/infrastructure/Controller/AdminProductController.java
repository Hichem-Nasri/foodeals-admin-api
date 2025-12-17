package net.foodeals.product.infrastructure.Controller;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.zxing.NotFoundException;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.foodeals.common.services.ExcelService;
import net.foodeals.notification.application.dtos.NotificationRequest;
import net.foodeals.product.application.dtos.requests.ProductCreateRequest;
import net.foodeals.product.application.dtos.requests.ProductDto;
import net.foodeals.product.application.dtos.requests.ProductUpdateRequest;
import net.foodeals.product.application.dtos.responses.ProductResponse;
import net.foodeals.product.application.services.ProductService;
import net.foodeals.product.domain.entities.Product;
import net.foodeals.product.domain.enums.CreatedBy;
import net.foodeals.product.domain.enums.ProductType;
import net.foodeals.product.domain.exceptions.ProductNotFoundException;

@RestController
@RequestMapping("v1/admin/products")
@RequiredArgsConstructor

public class AdminProductController {
    private final ProductService service;
   // private final ExcelService excelService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse create(@RequestPart @Valid ProductCreateRequest req,
    		@RequestPart(value = "file", required = false) MultipartFile photo) throws Exception {
    	String path = null;
		if (photo != null) {
			path = service.saveFile(photo);
			req=new ProductCreateRequest(req.name(),req.slug(),req.description(),req.title(),
					req.barcode(),req.brand(),req.type(),path,req.categoryId(),
					req.subcategoryId(),req.rayonId());
		}
    	return service.createProduct(req);
    }

    @PutMapping("/{id}")
    public ProductResponse update(@PathVariable UUID id, @RequestPart @Valid 
    		ProductUpdateRequest req,@RequestPart(value = "file", required = false) MultipartFile photo)
    		 {
    	
    	String path = null;
		if (photo != null) {
			path = service.saveFile(photo);
			req=new ProductUpdateRequest(req.name(),req.slug(),req.description(),req.title(),
					req.barcode(),req.brand(),req.type(),req.price(),path,req.categoryId(),
					req.subcategoryId(),req.rayonId());
		}
        return service.updateProduct(id, req);
    }

   

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id,@RequestParam String reason, @RequestParam String motif) {
        service.deleteProduct(id,reason,motif);
    }
    
    @GetMapping("/archived-products")
    @Transactional
	public ResponseEntity<Page<ProductResponse>> getAllDeletedProducts
	(@RequestParam(defaultValue = "0") Integer pageNum,
			@RequestParam(defaultValue = "10") Integer pageSize) {
		Page<ProductResponse> response =null;
		
		response=service.findDeletedProducts(PageRequest.of(pageNum, pageSize))
					.map(product -> service.toResponse(product));
		
		return ResponseEntity.ok(response);
	}

   /* @PostMapping("/upload")
	public ResponseEntity<String> uploadProducts(@RequestParam("file") MultipartFile file) {
		try {
			excelService.readProductsFromExcel(file);

			excelService.readProductsFromExcel(file);

			return ResponseEntity.ok("Products uploaded successfully");
		} catch (IOException e) {
			return ResponseEntity.status(500).body("Failed to upload products");
		}
	}
*/
    // A) Tous produits (filtrables par créateur)
    // GET /api/admin/products?creator=all|admin|partner&subEntityId=...&page=0&size=20&sort=createdAt,desc
    @GetMapping
    public Page<ProductDto> list(
            @RequestParam(defaultValue = "all") String creator,
            @RequestParam(required = false) UUID subEntityId,
            Pageable pageable
    ) {
        return service.listByCreator(creator, subEntityId, pageable);
    }
    
    
    @GetMapping("/{id}")
    @Transactional
    public ProductDto get(@PathVariable UUID id )
     {
        return service.toDto(service.findById(id));
    }

    // B) Produits par magasin
    // GET /api/admin/products/by-sub-entity/{subEntityId}
    @GetMapping("/by-sub-entity/{subEntityId}")
    public Page<ProductDto> listBySubEntity(@PathVariable UUID subEntityId, Pageable pageable) {
        return service.listByCreator("partner", subEntityId, pageable);
    }

    // C) Recherche avancée
    // GET /api/admin/products/search?creatorType=ADMIN|PARTNER_SB&subEntityId=...
    @GetMapping("/search")
    public Page<ProductDto> search(
            @RequestParam(required = false) UUID subEntityId,
            @RequestParam(required = false) CreatedBy createdBy,
            Pageable pageable
    ) {
        return service.search(subEntityId, createdBy, pageable);
    }
    
    @PostMapping("/scan")
	public ResponseEntity<?> getProductByBarcode(@RequestParam("file") MultipartFile file) {
		if (file.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty.");
		}

		try (InputStream inputStream = file.getInputStream()) {
			ProductResponse productResponse = service.getProductByBarCode(inputStream);

			return ResponseEntity.ok(productResponse);

		} catch (ProductNotFoundException |NotFoundException e) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found for barcode.");

		} catch (IOException e) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the file.");

		} catch (RuntimeException e) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to read barcode from image.");
		}
	}
    
    @GetMapping("/search-products")
	public ResponseEntity<Page<ProductResponse>> searchProducts(@RequestParam(required = false) String name,
			@RequestParam(required = false) String brand, 
			@RequestParam(required = false) UUID categoryId,
			@RequestParam(required = false) UUID subCategoryId,
			@RequestParam(required = false) String barcode,
			@RequestParam(required = false) Instant startDate,
	        @RequestParam(required = false) Instant endDate,
			Pageable pageable) {
		Page<ProductResponse> products = service
				.searchProducts(name, brand, categoryId, subCategoryId, barcode,
					startDate,endDate, pageable)
				.map(product -> service.toResponse(product));
		
		if (!products.hasContent()) {
			products= service.findAll(pageable.getPageNumber(), pageable.getPageSize())
					.map(product -> service.toResponse(product));
		}
		return ResponseEntity.ok(products);
	}
}
