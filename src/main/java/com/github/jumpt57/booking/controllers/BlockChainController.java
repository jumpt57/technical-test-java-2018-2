package com.github.jumpt57.booking.controllers;


import com.github.jumpt57.booking.domain.Block;
import com.github.jumpt57.booking.services.BlockChainService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping(path = "/blockchain")
@AllArgsConstructor
public class BlockChainController {

    private final BlockChainService blockChainService;

    @GetMapping
    public ResponseEntity<Collection<Block>> getBookings() {
        return ResponseEntity.ok(blockChainService.getBlockChain());
    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validate() {
        return ResponseEntity.ok(blockChainService.isChainValid());
    }

}
