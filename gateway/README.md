# Gateway Service

Esta aplicação é responsável por gerenciar e rotear as requisições para os serviços apropriados dentro do sistema de e-commerce. Ele atua como um ponto central de entrada para as requisições vindas do BFF, garantindo que cada solicitação seja encaminhada corretamente para o serviço correspondente.

## Responsabilidades

- **Roteamento de Requisições:** Direciona as requisições para os serviços apropriados com base no padrão do endpoint.
- **Segurança:** Restringe o acesso a determinados endpoints para aplicações internas.
- **Integração com API Externa:** Interage com serviços externos e manipula as respostas conforme necessário.

## Requisições

### /cart**

As requisições com o padrão `/cart**` devem ser repassadas para o serviço de carrinho de compras (`cart`).

- **Exemplo de Endpoint:** `POST /cart/items`
- **Serviço Destino:** `cart`

#### Acesso Restrito

O endpoint `DELETE /` deve ter acesso restrito a aplicações internas.

- **Exemplo de Endpoint:** `DELETE /cart`
- **Acesso:** Restrito a aplicações internas, portanto deve retornar http status 404.

### /checkout**

As requisições com o padrão `/checkout**` devem ser direcionadas para o serviço de checkout (`checkout`).

- **Exemplo de Endpoint:** `POST /checkout`
- **Serviço Destino:** `checkout`

### /products**

As requisições com o padrão `/products` devem ser repassadas para a API externa `https://fakestoreapi.com`.

- **Exemplo de Endpoint:** `GET /products`
- **Serviço Destino:** `https://fakestoreapi.com` (valide a documentação no site indicado)

#### Manipulação de Respostas

- Quando a resposta da API externa for `200 OK` mas o corpo da resposta estiver vazio, a informação repassada deve ser `400 Id não localizado`.

