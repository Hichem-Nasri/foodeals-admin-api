package net.foodeals.home.interfaces.web;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.foodeals.home.application.dtos.BestSellerDto;
import net.foodeals.home.application.dtos.BestSellerSaveRequest;
import net.foodeals.home.application.services.BestSellerService;

@RestController
@RequestMapping("v1/best-sellers")
@RequiredArgsConstructor
public class BestSellerController {

    private final BestSellerService bestSellerService;

    @GetMapping
    public ResponseEntity<?> getBestSellers(
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String city,
            @RequestParam(defaultValue = "sellers") String sortedBy
    ) {
        return ResponseEntity.ok(bestSellerService.getBestSellers(country, state, city, sortedBy));
    }

    @PostMapping
    public ResponseEntity<?> saveBestSellers(@RequestBody BestSellerSaveRequest request,
                                             @RequestParam(required = false) String country,
                                             @RequestParam(required = false) String state,
                                             @RequestParam(required = false) String city) {
    	bestSellerService.savePersonalized(request.getPersonalized(), country, state, city);
        return ResponseEntity.ok(Map.of("message", "Saved successfully"));
    }

}
