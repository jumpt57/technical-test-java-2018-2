package com.github.jumpt57.booking.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jumpt57.booking.domain.Block;
import com.github.jumpt57.booking.domain.Booking;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BlockChainService {

    static ObjectMapper objectMapper = new ObjectMapper();

    private enum BlockChainInstance {
        INSTANCE;
        private Collection<Block> blockChain = new LinkedHashSet<>();

        public static Collection<Block> get() {
            return INSTANCE.blockChain;
        }
    }


    /**
     * Sorted from newer to oldest
     * @return
     */
    public Collection<Block> getBlockChain() {
        List<Block> blocks = new ArrayList<>(BlockChainInstance.get());
        Collections.reverse(blocks);
        return blocks;
    }

    public boolean createBlock(Booking booking) {
        try {
            String data = objectMapper.writeValueAsString(booking);
            String previousHash = getLastHash();
            Block block = new Block(data, previousHash);
            return updateWith(block);
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    synchronized boolean updateWith(Block block) {
        String lastHash = getLastHash();
        if (block.getPreviousHash().equals(lastHash)) {
            return BlockChainInstance.get().add(block);
        } else {
            return false;
        }
    }

    synchronized String getLastHash() {
        int sizeOfBlockChain = BlockChainInstance.get().size() - 1;
        Optional<Block> lastBlock = BlockChainInstance.get().parallelStream()
                .skip(sizeOfBlockChain == -1 ? 0 : sizeOfBlockChain)
                .findFirst();
        return lastBlock.isPresent() ? lastBlock.get().getHash() : "0";
    }


}
