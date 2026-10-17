# language: pt
@ui @webtables
Funcionalidade: Web Tables
  Como usuário do demoqa
  Quero gerenciar registros na tabela
  Para manter os dados de funcionários atualizados

  Contexto:
    Dado que eu acesso o site demoqa
    E escolho a opção "Elements" na página inicial
    E clico no submenu "Web Tables"

  Cenário: Criar, editar e deletar um registro
    Quando eu crio um novo registro
    Então o novo registro deve ser exibido na tabela
    Quando eu edito o novo registro criado
    Então o registro deve ser exibido com os dados atualizados
    Quando eu deleto o novo registro criado
    Então o registro não deve mais ser exibido na tabela

  @bonus
  Esquema do Cenário: Criar <quantidade> registros de forma dinâmica e deletar todos
    Quando eu crio <quantidade> novos registros de forma dinâmica
    Então os <quantidade> registros devem ser exibidos na tabela
    Quando eu deleto todos os registros criados
    Então nenhum dos registros criados deve ser exibido na tabela

    Exemplos:
      | quantidade |
      | 12         |
