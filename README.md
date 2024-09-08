# Desafio de Hackathon: "Ecommerce Manager"

## Contexto

Bem-vindo à nossa equipe!

Gostaria de dar as boas-vindas e compartilhar algumas informações importantes sobre o nosso projeto. Estamos trabalhando no desenvolvimento de uma plataforma de e-commerce que precisa oferecer todas as funcionalidades essenciais para um site de compras online.

Utilizaremos conceitos avançados de arquitetura distribuída, implementados com Java e Spring, para assegurar que entregamos a melhor solução possível para a empresa. Isso envolve não apenas o desenvolvimento das nossas aplicações, mas também a integração com serviços externos que outras equipes utilizarão.

O diagrama abaixo fornece uma visão geral do nosso ecossistema, destacando as aplicações sob nossa responsabilidade. As requisições são inicialmente processadas por um BFF (Backend for Frontend), que gerencia a segurança para o acesso às aplicações dentro da VPC. Após essa verificação, as requisições são encaminhadas para os serviços internos.

![Ecossistema ADJT](/docs/readme-files/diagrama.svg)

De acordo com a estratégia da nossa empresa, cada equipe possui um Api-Gateway que controla quais endpoints podem ser acessados através do BFF. Nossa equipe se encarrega da funcionalidade de busca de produtos (excluindo controle de estoque), da gestão dos carrinhos de compras dos clientes e do processo de checkout desses carrinhos.

## Requisitos gerais 

1. **Sistema Distribuído:** Nossos sistemas são complemente independentes, porém estão organizados neste monorepo, com o intuito de facilitar o agrupamento das aplicações de cada squad. Você deverá clonar esse repositório e em seguida criar uma Branch com o nome do seu grupo. Após a conclusão, você deverá abrir uma Pull Request para Branch `develop`. Os requisitos funcionais de cada microsserviço está nos seus respectivos readmes:
   - [**Catalog Service**]()
   - [**Cart Service**](cart/README.md)
   - [**Checkout Service:**](checkout/README.md)
   - [**API Gateway**](gateway/README.md)

2. **Arquitetura Clean:** Aplique os conceitos de Clean Architecture para garantir que cada módulo tenha uma separação clara entre as camadas de apresentação, aplicação, domínio e infraestrutura.

5. **Documentação e Testes:** Inclua documentação básica sobre como executar e testar cada módulo. Implemente testes unitários e de integração para garantir que o sistema funcione corretamente.

6. **CI:** Utilizaremos Github Actions para validar a cobertura de testes e a funcionalidade dos endpoints. **Não será permitida nenhuma alteração na pasta .github!!!**

7. **Armazenamento de Dados:** Você poderá escolher qual tecnologia de banco de dados que utilizará, sem qualquer restrição. No entanto, é importante lembrar que nessa etapa seram necessarias alterações no [`docker-compose.yml`](docker-compose.yml) para que seu ecossistema continue completamente funcional. Entregas sem banco dados, ou que não consigam ser executas completamente via `docker-compose up` e via Github Action serão penalizadas.

Estamos entusiasmados com as contribuições que você trará para o projeto e confiantes de que, juntos, alcançaremos excelentes resultados. Boa sorte com o desafio!

