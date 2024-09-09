# Aplicação de Gerenciamento de Carrinho de Compras

Esta aplicação fornece um serviço de gerenciamento de carrinho de compras para consumidores em um ambiente de e-commerce. O serviço permite que os usuários interajam com seus carrinhos de compras de forma eficiente e prática através de uma API RESTful. As principais funcionalidades incluem:

- **Adicionar Itens ao Carrinho:** Permite que os consumidores adicionem itens ao seu carrinho, especificando a quantidade desejada.
- **Remover Itens do Carrinho:** Oferece a capacidade de remover itens específicos do carrinho.
- **Limpar o Carrinho:** Facilita a limpeza total do carrinho quando o checkout é finalizado.

A aplicação é projetada para lidar com a manipulação de itens em carrinhos de compras de forma robusta, garantindo que as operações sejam realizadas com dados válidos e retornando mensagens de erro claras quando necessário.

A aplicação visa melhorar a experiência do usuário e a eficiência no gerenciamento de carrinhos de compras em plataformas de e-commerce.

## Especificação do Serviço de Carrinho de Compras

### 1. Adicionar Item ao Carrinho

- **Endpoint:** `POST /items`
- **Descrição:** Adiciona um item ao carrinho de um consumidor específico.
- **Corpo da Requisição (JSON):**
  ```json
  {
    "consumerId": "153e23c8-322e-4fec-b9c4-72b8f74ad002",
    "itemId": "1",
    "quantity": "2"
  }
  ````
- **Respostas**
  - 200 OK
  ```
  {
  "message": "Item added to cart successfully"
  }
  ```
  - 400 Bad request
    - Quando o consumerId não for um UUID
    ```
    {
     "error": "Invalid consumerId format"
    }
    ```
    - Quando o itemId não existir
    ```
    {
     "error": "Invalid itemId does not exist"
    }
    ```
    - Quando a quantidade do itemId for inválida
    ```
    {
     "error": "Invalid itemId quantity"
    }
    ```
### 2. Remover Item do Carrinho
- **Endpoint:** `DELETE /item`
- **Descrição:** Remove uma unidade do item específico do carrinho de um consumidor. Caso o item atinga quantidade zero, removê-lo.
- **Corpo da Requisição (JSON):**
  ```json
    {
    "consumerId": "153e23c8-322e-4fec-b9c4-72b8f74ad002",
    "itemId": 1
    }
  ````
- **Respostas**
    - 200 OK
  ```
    {
    "message": "Item removed from cart successfully"
    }
  ```
  - 400 Bad request
      - Quando o consumerId não for um UUID
    ```
    {
     "error": "Invalid consumerId format"
    }
    ```
### 3. Incrementar Quantidade de Item do Carrinho
- **Endpoint:** `UPDATE /item`
- **Descrição:** Incrementa uma unidade de um item específico do carrinho de um consumidor.
- **Corpo da Requisição (JSON):**
  ```json
    {
    "consumerId": "153e23c8-322e-4fec-b9c4-72b8f74ad002",
    "itemId": 1
    }
  ````
- **Respostas**
    - 200 OK
  ```
    {
    "message": "Item removed from cart successfully"
    }
  ```
    - 400 Bad request
        - Quando o consumerId não for um UUID
      ```
      {
       "error": "Invalid consumerId format"
      }
      ```
        - Quando o itemID não existir no carrinho
      ```
      {
       "error": "Invalid itemId"
      }
      ```
      
### 4. Limpar o Carrinho
- **Endpoint:** `DELETE /`
- **Descrição:** Remove todo o carrinho de um consumidor.
- **Corpo da Requisição (JSON):**
  ```json
    {
    "consumerId": "153e23c8-322e-4fec-b9c4-72b8f74ad002"
    }
  ````
- **Respostas**
    - 200 OK
  ```
    {
    "message": "Items removed from cart successfully"
    }
  ```
    - 400 Bad request
        - Quando o consumerId não for um UUID
      ```
      {
       "error": "Invalid consumerId format"
      }
      ```

## Validações e Erros

### Consumer
- **Invalid consumerId format:** O consumerId deve ser um UUID válido. Caso contrário, retorna erro 400.

### Item
- **Invalid itemId:** O itemId deve corresponder a um item existente no sistema. Caso contrário, retorna erro 400. O item deve existir na 
- **Invalid quantity:** A quantidade deve ser um número inteiro positivo. Caso contrário, retorna erro 400.

***ATENÇÃO: As validações que envolvem a manipulação do item devem consultar a api responsável pelos produtos.***