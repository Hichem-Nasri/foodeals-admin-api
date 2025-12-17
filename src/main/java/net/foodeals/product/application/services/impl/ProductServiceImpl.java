package net.foodeals.product.application.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.foodeals.common.valueOjects.Price;
import net.foodeals.payment.application.dto.request.PriceDto;
import net.foodeals.product.application.dtos.requests.CategoryProductDto;
import net.foodeals.product.application.dtos.requests.CreatedByDto;
import net.foodeals.product.application.dtos.requests.ProductCreateRequest;
import net.foodeals.product.application.dtos.requests.ProductDto;
import net.foodeals.product.application.dtos.requests.ProductRequest;
import net.foodeals.product.application.dtos.requests.ProductUpdateRequest;
import net.foodeals.product.application.dtos.responses.ProductResponse;
import net.foodeals.product.application.services.BarcodeService;
import net.foodeals.product.application.services.ProductCategoryService;
import net.foodeals.product.application.services.ProductService;
import net.foodeals.product.domain.entities.Product;
import net.foodeals.product.domain.entities.ProductCategory;
import net.foodeals.product.domain.enums.CreatedBy;
import net.foodeals.product.domain.exceptions.ProductNotFoundException;
import net.foodeals.product.domain.repositories.ProductCategoryRepository;
import net.foodeals.product.domain.repositories.ProductRepository;
import net.foodeals.product.domain.repositories.ProductSubCategoryRepository;
import net.foodeals.product.domain.repositories.RayonRepository;

import org.modelmapper.ModelMapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.zxing.NotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

import static net.foodeals.common.Utils.SlugUtil.makeUniqueSlug;
import static net.foodeals.common.Utils.SlugUtil.toSlug;

@Service
@Transactional
@RequiredArgsConstructor
class ProductServiceImpl implements ProductService {

	private final ProductRepository repository;
	private final ProductCategoryRepository productCategoryRepository;
	private final ProductSubCategoryRepository productSubCategoryRepository;
	private final RayonRepository rayonRepository;
	private final ProductCategoryService categoryService;
	private final BarcodeService barcodeService;
	private final ModelMapper mapper;

	@Override
	@Transactional
	public List<Product> findAll() {
		return repository.findAll();
	}

	@Override
	@Transactional
	public Page<Product> findAll(Integer pageNumber, Integer pageSize) {
		return repository.findAll(PageRequest.of(pageNumber, pageSize));
	}

	@Override
	@Transactional
	public Page<Product> findAll(Pageable pageable) {
		return null;
	}

	@Override
	@Transactional
	public Product findById(UUID id) {
		return repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
	}

	@Override
	@Transactional
	public Product create(ProductRequest request) {
		final ProductCategory category = categoryService.findById(request.categoryId());
		final Product product = mapper.map(request, Product.class);

		product.setCategory(category);
		product.setSlug(makeUniqueSlug(toSlug(request.name()), repository));

		return repository.save(product);
	}

	@Override
	@Transactional
	public Product update(UUID id, ProductRequest request) {
		final Product product = findById(id);
		final ProductCategory category = categoryService.findById(request.categoryId());

		mapper.map(request, product);
		product.setCategory(category);
		product.setSlug(makeUniqueSlug(toSlug(request.name()), repository));

		return repository.save(product);
	}

	@Override
	@Transactional
	public void delete(UUID id) {
		if (!repository.existsById(id))
			throw new ProductNotFoundException(id);

		repository.softDelete(id);
	}

	@Override
	public Page<ProductDto> listByCreator(String creator, UUID subEntityId, Pageable pageable) {
		// creator: "all" | "admin" | "partner"
		if (subEntityId != null) {
		    Page<Product> products = repository.findByCreatedBySubEntity_Id(subEntityId, pageable);
		    return products.map(this::toDto);
		}
		if ("admin".equalsIgnoreCase(creator)) {
			Page<Product> products = repository.findByCreatedBy(CreatedBy.ADMIN, pageable);
		    return products.map(this::toDto);
			
		}
		if ("partner".equalsIgnoreCase(creator) || "magasin".equalsIgnoreCase(creator)) {
			
			Page<Product> products = repository.findByCreatedBy(CreatedBy.PARTNER, pageable);
		    return products.map(this::toDto);
		}
		Page<Product> products = repository.findAll(pageable);
		 return products.map(this::toDto);
	}

	@Override
	public Page<ProductDto> search(UUID subEntityId, CreatedBy createdBy, Pageable pageable) {
		
		Page<Product> products = repository.search(subEntityId, createdBy, pageable);
		return products.map(this::toDto);
	}
	
	
	public ProductDto toDto(Product p) {
	    return ProductDto.builder()
	            .id(p.getId())
	            .name(p.getName())
	            .title(p.getTitle())
	            .imageUrl(p.getProductImagePath())
	            .description(p.getDescription())
	            .barcode(p.getBarcode())
	            .type(p.getType() != null ? p.getType().name() : null)
	            .creationDate(p.getCreatedAt())
	            .price(p.getPrice())
	            .brand(p.getBrand())
	            .rayon(p.getRayon().getName())

	            .category(p.getCategory() != null ? CategoryProductDto.builder()
	                    .id(p.getCategory().getId())
	                    .name(p.getCategory().getName())
	                    .slug(p.getCategory().getSlug())
	                    .build() : null)

	            .subCategory(p.getSubcategory() != null ? CategoryProductDto.builder()
	                    .id(p.getSubcategory().getId())
	                    .name(p.getSubcategory().getName())
	                    .slug(p.getSubcategory().getSlug())
	                    .build() : null)

	            .createdBy(p.getCreatedByUser() != null ?CreatedByDto.builder()
	                    .id(p.getCreatedByUser().getId().toString())
	                    .type("ADMIN")
	                    .email(p.getCreatedByUser().getEmail())
	                    .phone(p.getCreatedByUser().getPhone())
	                    .name(p.getCreatedByUser().getName().firstName()+" "+p.getCreatedByUser().getName().lastName())
	                    .build()
	                    :
	                    (p.getCreatedBySubEntity() != null ? CreatedByDto.builder()
	                            .id(p.getCreatedBySubEntity().getId().toString())
	                            .type("PARTNER_SB")
	                            .email(p.getCreatedBySubEntity().getEmail())
	                            .phone(p.getCreatedBySubEntity().getPhone())
	                            .name(p.getCreatedBySubEntity().getName())
	                            .build()
	                            : null)
	            )
	            .build();
	}

	@Override
	public ProductResponse createProduct(ProductCreateRequest req) throws Exception {
		Product p = toEntity(req);
        // slug
        p.setSlug(req.slug());
      
        // relations
        p.setCategory(productCategoryRepository.findById(req.categoryId())
                .orElseThrow(() ->   new Exception()));
        if (req.subcategoryId() != null) {
            p.setSubcategory(productSubCategoryRepository.findById(req.subcategoryId())
            		   .orElseThrow(() ->   new Exception()));
        }
        if (req.rayonId() != null) {
            p.setRayon(rayonRepository.findById(req.rayonId())
                    .orElseThrow(() ->   new Exception()));;
        }
        Product saved = repository.save(p);
        return toResponse(saved);
	}
	
	 /* ---------- CREATE ---------- */
    public Product toEntity(ProductCreateRequest dto) {
        if (dto == null) return null;

        Product p = new Product();
        // champs simples
        p.setName(trim(dto.name()));
        p.setSlug(trim(dto.slug()));                 // le service décidera de normaliser/générer
        p.setDescription(trim(dto.description()));
        p.setTitle(trim(dto.title()));
        p.setBarcode(trim(dto.barcode()));
        p.setBrand(trim(dto.brand()));
        p.setType(dto.type());
        // value object
        //p.setPrice(toPrice(dto.price()));
        // path image (attention: champ Entity = ProductImagePath)
        p.setProductImagePath(trim(dto.productImagePath()));

        // relations NON SET ici : le service s'en charge (category/subcategory/rayon)
        // autres collections: deals/boxItems ignorées au create

        return p;
    }

    /* ---------- UPDATE PARTIEL ---------- */
    public Product applyUpdate(Product entity, ProductUpdateRequest dto) {
        if (entity == null || dto == null) return null;

        // ne met à jour que ce qui n'est pas null
        if (dto.name() != null)         entity.setName(trim(dto.name()));
        if (dto.slug() != null)         entity.setSlug(trim(dto.slug()));
        if (dto.description() != null)  entity.setDescription(trim(dto.description()));
        if (dto.title() != null)        entity.setTitle(trim(dto.title()));
        if (dto.barcode() != null)      entity.setBarcode(trim(dto.barcode()));
        if (dto.brand() != null)        entity.setBrand(trim(dto.brand()));
        if (dto.type() != null)         entity.setType(dto.type());
        if (dto.price() != null)        entity.setPrice(toPrice(dto.price()));
        if (dto.productImagePath() != null)
                                        entity.setProductImagePath(trim(dto.productImagePath()));
        return entity ;
        
        // relations (category/subcategory/rayon) : gérées dans le service
    }

    /* ---------- RESPONSE ---------- */
    public ProductResponse toResponse(Product e) {
        if (e == null) return null;

        return new ProductResponse(
            e.getId(),
            e.getName(),
            e.getSlug(),
            e.getDescription(),
            e.getTitle(),
            e.getBarcode(),
            e.getBrand(),
            e.getType(),
            toPriceDto(e.getPrice()),
            e.getProductImagePath(),
            // relations "light"
            e.getCategory()    != null ? e.getCategory().getId() : null,
            e.getCategory()    != null ? e.getCategory().getName() : null,
            e.getSubcategory() != null ? e.getSubcategory().getId() : null,
            e.getSubcategory() != null ? e.getSubcategory().getName() : null,
            e.getRayon()       != null ? e.getRayon().getId() : null,
            e.getRayon()       != null ? e.getRayon().getName() : null
        );
    }

    /* ---------- Helpers privés ---------- */

    private Price toPrice(PriceDto dto) {
        if (dto == null) return null;
        Price p =new Price(dto.amount(),Currency.getInstance("MAD")); 
        return p;
    }

    private PriceDto toPriceDto(Price p) {
        if (p == null) return null;
        return new PriceDto(p.amount(), p.currency().getCurrencyCode());
    }

    private String trim(String s) {
        return s == null ? null : s.trim();
    }

	@Override
	public ProductResponse updateProduct(UUID id, ProductUpdateRequest request) {
		Product product=repository.findById(id).get();
		return toResponse(applyUpdate(product, request));
	}

	@Override
	public void deleteProduct(UUID id, String reason, String motif) {
		Product product=repository.findById(id).get();
		product.setReason(reason);
		product.setMotif(motif);
		product.setDeletedAt(Instant.now());
		repository.save(product);
		
	}

	@Override
	public ProductResponse getProductByBarCode(InputStream imageStream) throws ProductNotFoundException,
	NotFoundException {
		try {

			String barCode = barcodeService.readBarcode(imageStream).trim();
			Product p =repository.findByBarcode(barCode).get();
			return toResponse(p);
		} catch (IOException e) {

			System.out.println("Error reading barcode from image");
			throw new RuntimeException("Failed to read barcode from image", e);
		}
	
	}

	@Override
	public String saveFile(MultipartFile file) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Product> findDeletedProducts(Pageable pageable) {
		return repository.findDeletedProducts(pageable);
	}

	@Override
	public Page<Product> searchProducts(String name, String brand, UUID categoryId, UUID subCategoryId, String barcode,
			Instant startDate, Instant endDate, Pageable pageable) {

		return repository.searchProducts(name, brand, categoryId, subCategoryId, barcode, startDate, endDate,
				pageable);

	}

}