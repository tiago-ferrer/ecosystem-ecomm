package br.com.fiap.postech.adjt.cart.useCase.cart.impl;

import br.com.fiap.postech.adjt.cart.domain.cart.Consumer;
import br.com.fiap.postech.adjt.cart.domain.cart.StatusEnum;
import br.com.fiap.postech.adjt.cart.domain.exception.ApiProductsException;
import br.com.fiap.postech.adjt.cart.infrastructure.cart.controller.dto.AdicionaItemRequestDTO;
import br.com.fiap.postech.adjt.cart.infrastructure.cart.controller.dto.DeletaItemRequestDTO;
import br.com.fiap.postech.adjt.cart.infrastructure.cart.controller.dto.ItemResponseDTO;
import br.com.fiap.postech.adjt.cart.infrastructure.cart.controller.dto.ProductsResponseErrorDTO;
import br.com.fiap.postech.adjt.cart.infrastructure.cart.model.CarrinhoEntity;
import br.com.fiap.postech.adjt.cart.infrastructure.cart.model.ItensNoCarrinhoEntity;
import br.com.fiap.postech.adjt.cart.infrastructure.cart.model.ItensNoCarrinhoId;
import br.com.fiap.postech.adjt.cart.infrastructure.cart.repository.CarrinhoRepository;
import br.com.fiap.postech.adjt.cart.infrastructure.cart.repository.ItensNoCarrinhoRepository;
import br.com.fiap.postech.adjt.cart.infrastructure.products.client.ProductsClient;
import br.com.fiap.postech.adjt.cart.infrastructure.products.client.response.ProductsResponseDTO;
import br.com.fiap.postech.adjt.cart.useCase.cart.CartUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;


@Service
@Slf4j
public class CartUseCaseImpl implements CartUseCase {

    private final ProductsClient productsClient;
    private final CarrinhoRepository repositoryCarrinho;
    private final ItensNoCarrinhoRepository repositoryItensNoCarrinho;
    private final ObjectMapper mapper;

    public CartUseCaseImpl(final ProductsClient productsClient,
                           final CarrinhoRepository repositoryCarrinho,
                           final ItensNoCarrinhoRepository repositoryItensNoCarrinho,
                           final ObjectMapper mapper) {
        this.productsClient = productsClient;
        this.repositoryCarrinho = repositoryCarrinho;
        this.repositoryItensNoCarrinho = repositoryItensNoCarrinho;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public ItemResponseDTO adiciona(final AdicionaItemRequestDTO dadosItem) {
        final var consumer = new Consumer(dadosItem.consumerId());

        ProductsResponseDTO product = null;
        ProductsResponseErrorDTO errorResponse = null;
        try {
            product = this.productsClient.busca(dadosItem.itemId(), dadosItem.quantity());
        } catch (FeignException.FeignClientException errorExpected) {
            if(errorExpected.status() == HttpStatus.BAD_REQUEST.value()
                && errorExpected.responseBody().isPresent()) {
                final var byteBuffer = errorExpected.responseBody().get();
                byte[] bytes = new byte[byteBuffer.remaining()];
                byteBuffer.get(bytes);
                try {
                    errorResponse = this.mapper.readValue(bytes, ProductsResponseErrorDTO.class);
                } catch (IOException e) {
                    log.error("Erro na conversão do JSON de erro", e);
                    throw new RuntimeException(e);
                }
            }
        } catch (Exception e) {
            log.error("Problemas com a API de PRODUCTS ", e);
            throw new ApiProductsException("Problemas com a API de PRODUCTS");
        }

        if(Objects.nonNull(errorResponse)) {
            if(errorResponse.codeError() == 1) {
                log.error("Item não encontrado. Objeto: {}", errorResponse);
                throw new ApiProductsException("Invalid itemId does not exist");
            } else if (errorResponse.codeError() == 2) {
                log.error("Quantidade inválida. Objeto: {}", errorResponse);
                throw new ApiProductsException("Invalid itemId quantity");
            } else {
                log.error("Erro não mapeado. Objeto: {}", errorResponse);
                throw new ApiProductsException("Unmapped error PRODUCTS API");
            }
        }

        final var carrinho = this.repositoryCarrinho
                .findByUsuarioAndStatus(consumer.consumerId(), StatusEnum.ABERTO);
        if(carrinho.isEmpty()) {
            final var valorTotal = product.price().multiply(new BigDecimal(dadosItem.quantity()));

            final var carrinhoEntity = CarrinhoEntity.builder()
                    .usuario(consumer.consumerId())
                    .status(StatusEnum.ABERTO)
                    .dataDeCriacao(LocalDateTime.now())
                    .valorTotal(valorTotal)
                    .build();
            final var carrinhoSalvo = this.repositoryCarrinho.save(carrinhoEntity);

            final var itemEntity = ItensNoCarrinhoEntity.builder()
                    .id(ItensNoCarrinhoId
                            .builder()
                            .idCarrinho(carrinhoSalvo.getId())
                            .ean(product.id())
                            .build()
                    )
                    .precoTotal(valorTotal)
                    .build();
            this.repositoryItensNoCarrinho.save(itemEntity);

            return new ItemResponseDTO(
                    "Item added to cart successfully"
            );
        }

        final var carrinhoExistente = carrinho.get();

        final var valorTotalItem = product.price().multiply(new BigDecimal(dadosItem.quantity()));
        final var itemEntity = ItensNoCarrinhoEntity.builder()
                .id(ItensNoCarrinhoId
                        .builder()
                        .idCarrinho(carrinhoExistente.getId())
                        .ean(product.id())
                        .build()
                )
                .precoTotal(valorTotalItem)
                .build();
        this.repositoryItensNoCarrinho.save(itemEntity);

        final var valorTotal = this.repositoryItensNoCarrinho.findByIdIdCarrinho(carrinhoExistente.getId())
                .stream()
                .map(ItensNoCarrinhoEntity::getPrecoTotal)
                .reduce(BigDecimal::add)
                .get();

        carrinhoExistente.setValorTotal(valorTotal);
        this.repositoryCarrinho.save(carrinhoExistente);

        return new ItemResponseDTO(
                "Item added to cart successfully"
        );
    }

    @Override
    public ItemResponseDTO deleta(final DeletaItemRequestDTO dadosItem) {
        final var consumerId = new Consumer(dadosItem.consumerId());

        return new ItemResponseDTO(
                "Item removed from cart successfully"
        );
    }

}
