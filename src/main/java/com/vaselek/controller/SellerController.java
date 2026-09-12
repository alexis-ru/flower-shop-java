package com.vaselek.controller;

import com.vaselek.model.User;
import com.vaselek.model.dto;
import com.vaselek.service.FlowerService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seller")
public class SellerController {

    private final FlowerService flowerService;

    public SellerController(FlowerService flowerService) {
        this.flowerService = flowerService;
    }

    @GetMapping("/flowers")
    public List<dto.FlowerDTO> listFlowers(@AuthenticationPrincipal User user) {
        return flowerService.findBySeller(user.getId());
    }

    @PostMapping("/flowers")
    public dto.FlowerDTO createFlower(@RequestBody dto.CreateFlowerRequest req,
                                     @AuthenticationPrincipal User user) {
        return flowerService.create(req, user.getId());
    }

    @PutMapping("/flowers/{id}")
    public dto.FlowerDTO updateFlower(@PathVariable Long id,
                                      @RequestBody dto.UpdateFlowerRequest req) {
        return flowerService.update(id, req);
    }
}
