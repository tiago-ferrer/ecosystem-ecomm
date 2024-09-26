# Checkout Service

Esta aplicação é responsável por gerenciar o processo de checkout no ecossistema de e-commerce. Ele atua como o ponto central para a finalização de compras, garantindo que todas as etapas necessárias sejam executadas corretamente.

## Responsabilidades

- **Processamento de Pagamentos:** Gerencia a comunicação com a API de pagamento externa para efetuar cobranças.
- **Persistência de Pedidos:** Cria e armazena objetos de pedido (`order`) após a confirmação do pagamento.
- **Integração com Carrinho:** Obtém informações do carrinho de compras para processar o pedido.


#### Fluxo de Processamento
1. Obter Carrinho: O serviço deve buscar o carrinho do consumidor através do endpoint GET /cart/.
2. Persistir Pedido: Após obter o carrinho, deve persistir um objeto chamado order, gerando um UUID para o mesmo e com os items e suas quantidades.
3. Efetuar Cobrança: Utilizar a API externa payment-api para efetuar a cobrança.
4. Tempo de Resposta: A requisição deve responder em menos de 300 ms.
5. Processamento Assíncrono: Estar preparado para cenários onde a payment-api não consiga responder a tempo, recomendamos o processamento do pagamento de forma assíncrona.
6. O order pode ter 3 status: `pending`,`approved` e `declined`
7. O carrinho deve ser limpo após a geração do pedido


## Requisitos do Serviço

### Criar um checkout

O endpoint `/` deve receber uma chamada `POST` com o `consumerId` e as informações de pagamento.

- **Endpoint:** `POST /`
- **Descrição:** Processa o pagamento e cria um pedido. Casso 
- **Corpo da Requisição (JSON):**
  ```json
  {
    "consumerId": "153e23c8-322e-4fec-b9c4-72b8f74ad002",
    "amount": 1050,
    "currency": "BRL",
    "payment_method": {
        "type": "br_credit_card",
        "fields": {
            "number": "4111111111111111",
            "expiration_month": "12",
            "expiration_year": "25",
            "cvv": "789",
            "name": "John Doe"
        }
    }
  }
  ```

- **Respostas**
  - 200 OK
      ```
        {
        "orderId": "e7b8f8e2-8b8d-4d8b-8b8d-4d8b8d8b8d8b",
        "status": "pending"
        }
      ```
    - 400 Bad request
        - Quando o OrderId não for um UUID
      ```
        {
         "error": "Invalid orderId format"
        }
        ```


### Busca todos os checkouts feitos

- **Endpoint:** `GET /{consumerId}`
- **Descrição:** Processa o pagamento e cria um pedido.
- **Resposta esperada (JSON):**
  ```json
  {
    "orders" : [{
      "orderId" : "53e2d452-7337-4569-9c7b-75ef99c3820d",
      "items": [
      {
        "itemId": 1,
        "qnt": 4
      }
    ],
    "paymentType" : "br_credit_card",
    "value" : 1050,
    "paymentStatus" : "approved"
    }]
  }
  ```

### Busca por pedido

- **Endpoint:** `GET /by-order-id/{orderId}`
- **Descrição:** Processa o pagamento e cria um pedido.
- **Resposta esperada (JSON):**
  ```json
  {
      "orderId" : "53e2d452-7337-4569-9c7b-75ef99c3820d",
      "items": [
      {
        "itemId": 1,
        "qnt": 4
      }
    ],
    "paymentType" : "br_credit_card",
    "value" : 1050,
    "paymentStatus" : "declined"
    }
  ```
## Validações e Erros
### Pagamento
- Invalid payment information: As informações de pagamento devem ser válidas e completas. Caso contrário, retorna erro 400.
- Payment processing failed: Caso a payment-api não consiga processar o pagamento, retorna erro 500.
### Carrinho
- Empty cart: O carrinho do consumidor não pode estar vazio. Caso contrário, retorna erro 400.
- Invalid consumerId format: O consumerId deve ser um UUID válido. Caso contrário, retorna erro 400

## Payment API (EXTERNA) - Informações importantes

### Gerar ApiKey

- **Endpoint:** `POST https://payment-api-latest.onrender.com/create-group`
- **Descrição:** Cria uma ApiKey para o seu grupo. Essa apiKey deverá ser usada em todas as requisições de pagamento. Caso não seja fornecida apiKey a resposta será um HttpStatus 401.
- **Corpo da Requisição (JSON):**
````json
{
	"groupName":"Grupo11",
	"studentNames":["José Almeida","Maria da Silva"] 
}
````

- **Resposta esperada:**

```json
{
	"apiKey": "777396e205b7881490af58e82df453333673428889284694abab7dd9"
}
```

### Solicitar processamento de pagamento

- **Endpoint:** `POST https://payment-api-latest.onrender.com/create-payment`
- **Descrição:** Cria uma ApiKey para o seu grupo. Essa apiKey deverá ser usada em todas as requisições de pagamento. Caso não seja fornecida apiKey a resposta será um HttpStatus 401.


- **Observação sobre API:** 
  - Caso o já exista um **orderId** na Api de pagamentos ela retornará o status já salvo.
  - Essa API usa CloudFunction, isso indica que o sistema pode demorar para responder. Atente-se que sua aplicação deve ter tempo de resposta inferior a 500ms. Faça o manejo necessário na sua aplicação para assegurar esse requisito. 


- **Corpo da Requisição (JSON):**

- **Header:**
```json
{
  "apiKey": "777396e205b7881490af58e82df453333673428889284694abab7dd9"
}
```

- **Body:**
````json
{
  "orderId": "98b251ab-e4f1-487e-a26b-fa8fdbe356ac",
  "amount": 1050,
  "currency": "BRL",
  "payment_method": {
    "type": "br_credit_card",
    "fields": {
      "number": "4111111111111111",
      "expiration_month": "12",
      "expiration_year": "25",
      "cvv": "789",
      "name": "John Doe"
    }
  }
}
````

- **Resposta esperada**:

```json
{
  "orderId": "98b251ab-e4f1-487e-a26b-fa8fdbe356ac",
  "status": "declined"
}
```
