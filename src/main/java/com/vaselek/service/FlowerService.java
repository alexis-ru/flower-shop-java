package com.vaselek.service;

import com.vaselek.model.Flower;
import com.vaselek.model.User;
import com.vaselek.model.dto;
import com.vaselek.repository.FlowerRepository;
import com.vaselek.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlowerService {

    private final FlowerRepository flowerRepo;
    private final UserRepository userRepo;

    public FlowerService(FlowerRepository flowerRepo, UserRepository userRepo) {
        this.flowerRepo = flowerRepo;
        this.userRepo = userRepo;
    }

    public List<dto.FlowerDTO> findBySeller(Long sellerId) {
        return flowerRepo.findBySellerId(sellerId).stream().map(dto.FlowerDTO::from).toList();
    }

    public dto.FlowerDTO create(dto.CreateFlowerRequest req, Long sellerId) {
        User seller = userRepo.findById(sellerId).orElseThrow();
        Flower f = new Flower();
        f.setName(req.name());
        f.setQuantity(req.quantity());
        f.setArrivalDate(req.arrivalDate());
        f.setSeller(seller);
        return dto.FlowerDTO.from(flowerRepo.save(f));
    }

    public dto.FlowerDTO update(Long id, dto.UpdateFlowerRequest req) {
        Flower f = flowerRepo.findById(id).orElseThrow();
        if (req.quantity() != null) f.setQuantity(req.quantity());
        if (req.saleDate() != null) f.setSaleDate(req.saleDate());
        return dto.FlowerDTO.from(flowerRepo.save(f));
    }
}
