# language: pt
@FindArticlesTest
Funcionalidade: Buscar article lista
  Esquema do Cenario: Buscar article lista
    Dado parâmetro sucesso = <sucesso>
    Quando O serviço de Buscar article lista
    Então O status HTTP do enviar deverá ser 200
    Exemplos:
      | sucesso |
      | "true"  |