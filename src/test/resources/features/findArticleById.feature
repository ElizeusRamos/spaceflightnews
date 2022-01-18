# language: pt
@FindArticleByIdTest
Funcionalidade: Buscar article por id
  Esquema do Cenario: Buscar article por id
    Dado parâmetro id = <id>
    Quando O serviço de Buscar article por id
    Então O status HTTP do consultar deverá ser 200
    Exemplos:
      | id |
      | 0  |