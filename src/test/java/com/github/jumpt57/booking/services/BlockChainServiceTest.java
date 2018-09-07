package com.github.jumpt57.booking.services;

import com.github.jumpt57.booking.domain.Block;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class BlockChainServiceTest {

    @InjectMocks
    private BlockChainService blockChainService;

    @Test
    public void should_have_instance_of_blockChain() {
        // GIVEN
        Collection<Block> blockChain = blockChainService.getBlockChain();
        //THEN
        assertThat(blockChain).isNotNull();
    }

    @Test
    public void should_create_a_block() {
        // GIVEN
        Collection<Block> blockChain = blockChainService.getBlockChain();
        Block block = new Block("Some data", "0");
        // WHEN
        boolean isOk = blockChainService.updateWith(block);
        // THEN
        assertThat(isOk).isTrue();

        assertThat(blockChain)
                .isNotNull()
                .hasSize(0);

        assertThat(blockChainService.getBlockChain())
                .isNotNull()
                .hasSize(1);
    }

    @Test
    public void should_fail_creating_a_block_with_same_hash() {
        // GIVEN
        Block block = new Block("Some data", "0");
        blockChainService.updateWith(block);
        // WHEN
        boolean isOk = blockChainService.updateWith(block);
        // THEN
        assertThat(isOk).isFalse();

        assertThat(blockChainService.getBlockChain())
                .isNotNull()
                .hasSize(1);
    }

    @Test
    public void should_get_the_last_hash() {
        // GIVEN
        Block block = new Block("Some data", "0");
        blockChainService.updateWith(block);
        // WHEN
        String hash = blockChainService.getLastHash();
        // THEN
        assertThat(hash).isNotEqualTo("0");
    }

    @Test
    public void should_create_two_blocks() {
        // GIVEN
        Block block = new Block("Some data", "0");
        blockChainService.updateWith(block);
        String hash = blockChainService.getLastHash();
        Block block2 = new Block("Some data 2", hash);

        // WHEN
        boolean isOk = blockChainService.updateWith(block2);
        // THEN
        assertThat(isOk).isTrue();

        assertThat(blockChainService.getBlockChain())
                .isNotNull()
                .hasSize(2);
    }

}