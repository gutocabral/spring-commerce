# SpringCommerce

## Visão Geral

SpringCommerce é um projeto educacional de e-commerce desenvolvido como parte do meu processo de aprendizagem do ecossistema Spring Boot. Esta aplicação implementa uma plataforma de comércio eletrônico utilizando Spring Boot 3.4.3 e Java 17, seguindo as melhores práticas de desenvolvimento e arquitetura de software.

Este projeto serve como um estudo prático dos conceitos fundamentais do Spring Boot, incluindo injeção de dependência, persistência de dados, desenvolvimento de APIs RESTful, segurança e arquitetura em camadas.

## Objetivos de Aprendizado

- Dominar os fundamentos do Spring Boot e seu ecossistema
- Implementar uma arquitetura em camadas seguindo padrões de projeto
- Desenvolver APIs RESTful seguindo as melhores práticas
- Compreender os conceitos de persistência de dados com Spring Data JPA
- Aplicar segurança em aplicações web com Spring Security
- Praticar o desenvolvimento orientado a testes
- Implementar validação de dados e tratamento de exceções
- Aprender sobre documentação de API e implementação de funcionalidades de e-commerce

## Tecnologias Utilizadas

- **Spring Boot 3.4.3**: Framework principal para desenvolvimento
- **Java 17**: Versão LTS do Java com recursos modernos
- **Spring Data JPA**: Para abstração da camada de persistência
- **Spring Security**: Para implementação de autenticação e autorização
- **H2 Database**: Banco de dados em memória para ambiente de desenvolvimento
- **Lombok**: Para redução de código boilerplate
- **Bean Validation**: Para validação declarativa de dados

## Estrutura do Projeto

O projeto segue uma arquitetura em camadas bem definida:

```
com.springcommerce
├── config          // Configurações da aplicação e beans personalizados
├── controller      // Controllers REST para exposição da API
├── dto             // Objetos de transferência de dados
├── exception       // Manipuladores de exceções e exceções personalizadas
├── model           // Entidades JPA representando o modelo de dados
├── repository      // Interfaces de repositório para acesso a dados
├── security        // Configurações e classes relacionadas à segurança
├── service         // Camada de serviço contendo lógica de negócios
└── util            // Classes utilitárias e helpers
```

## Funcionalidades

### Implementadas

- **Gerenciamento de Produtos**:
    - API RESTful completa para operações CRUD
    - Busca de produtos por nome ou quantidade em estoque
    - Validação robusta de dados de entrada
    - Tratamento elegante de exceções

- **Configuração de Desenvolvimento**:
    - Configuração de segurança para facilitar o desenvolvimento
    - Banco de dados H2 em memória com console web
    - Tratamento global de exceções para respostas de erro consistentes

### Em Desenvolvimento

- **Sistema de Usuários e Autenticação**
- **Gerenciamento de Carrinho de Compras**
- **Processamento de Pedidos**
- **Implementação de JWT para autenticação stateless**

## API RESTful

### Endpoints de Produtos

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/products` | Lista todos os produtos |
| GET | `/api/products/{id}` | Recupera um produto específico por ID |
| POST | `/api/products` | Cria um novo produto |
| PUT | `/api/products/{id}` | Atualiza um produto existente |
| DELETE | `/api/products/{id}` | Remove um produto |
| GET | `/api/products/search?name={name}` | Busca produtos por nome |
| GET | `/api/products/search?minStock={quantity}` | Busca produtos com estoque mínimo |

## Configuração e Execução

### Pré-requisitos

- JDK 17 ou superior
- Maven 3.6+ ou Gradle 7+
- IDE com suporte a Spring Boot (IntelliJ IDEA, Eclipse, VS Code)

### Como Executar

1. Clone o repositório:
   ```
   git clone https://github.com/SEU-USUARIO/spring-commerce.git
   ```

2. Navegue até o diretório do projeto:
   ```
   cd spring-commerce
   ```

3. Execute a aplicação:
   ```
   ./mvnw spring-boot:run
   ```

4. Acesse os recursos disponíveis:
    - API: http://localhost:8080/api/products
    - Console H2: http://localhost:8080/h2-console
        - JDBC URL: `jdbc:h2:mem:springcommercedb`
        - Username: `sa`
        - Password: `password`

## Roadmap de Desenvolvimento

- [x] Configuração inicial do projeto
- [x] Implementação da entidade Product e sua API
- [x] Sistema de usuários com perfis de administrador e cliente
- [x] Implementação de autenticação JWT
- [ ] Desenvolvimento do carrinho de compras
- [ ] Sistema de processamento de pedidos
- [ ] Integração com serviço de pagamento (simulado)
- [ ] Documentação da API com Swagger/OpenAPI
- [ ] Testes unitários e de integração
- [ ] Deploy em ambiente de nuvem

## Considerações sobre o Aprendizado

Este projeto está sendo desenvolvido como parte do meu processo de aprendizagem. Feedback construtivo, sugestões de melhorias e discussões sobre padrões de design e implementação são sempre bem-vindos.

A intenção é evoluir este projeto incrementalmente, aplicando novos conceitos à medida que são aprendidos e refinando o código com base em melhores práticas.

## Contribuição

Contribuições que possam enriquecer este projeto de aprendizado são bem-vindas! Se você tiver sugestões de melhorias ou encontrar problemas, sinta-se à vontade para abrir uma issue ou enviar um pull request.

## Licença

Este projeto está licenciado sob a licença MIT - veja o arquivo LICENSE para detalhes.